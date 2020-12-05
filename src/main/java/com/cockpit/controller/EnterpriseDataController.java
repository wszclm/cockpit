package com.cockpit.controller;

import com.baomidou.mybatisplus.annotation.TableField;
import com.cockpit.commons.config.SrConstantMDA;
import com.cockpit.commons.exception.BaseException;
import com.cockpit.commons.model.RestResult;
import com.cockpit.commons.utils.ExcelUtil;
import com.cockpit.model.EnterpriseModel;
import com.cockpit.service.impl.EnterpriseServiceImpl;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(value = "企业信息导入", tags = {"EnterpriseDataController"},description ="企业数据管理")
@RestController
public class EnterpriseDataController {
    private static Logger logger = LoggerFactory.getLogger(EnterpriseDataController.class);

    @Autowired
    private EnterpriseServiceImpl enterpriseService;

    @ApiOperation(value = "企业数据批量导入", notes = "企业数据导入")
    @RequestMapping(value = "/enterprise/batchImport", method = RequestMethod.POST)
    public RestResult batchQueryInstType(@RequestParam("file") MultipartFile file, Model model, HttpServletRequest req ) {
        RestResult result = new RestResult();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        try {
            String fileName = file.getOriginalFilename();
            String str = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            if (str.toUpperCase().equals("XLS") || str.toUpperCase().equals("XLSX")) {
                InputStream is = file.getInputStream();
                int MAX_AMOUNT = SrConstantMDA.BATCH_INST_MAX_AMOUNT;
                List<String[]> reList = ExcelUtil.readOneCol(is, 0,false);
                if (reList == null || reList.size() == 0) {
                    result.setMeta(HttpStatus.BAD_REQUEST.value(),"导入失败");
                    result.setInfo(resultList);
                    return result;
                } else if (reList.size() > MAX_AMOUNT) {
                    result.setMeta(HttpStatus.BAD_REQUEST.value(),"文件导入一次最多支持" + MAX_AMOUNT + "条记录!");
                    result.setInfo(resultList);
                    return result;
                }
                enterpriseService.batchImport(reList);
                result.setMeta(HttpStatus.OK.value(),"");
            }
        } catch (Exception e) {
            logger.debug("企业数据导入异常：{}", e.getMessage());
            result.setMeta(HttpStatus.BAD_REQUEST.value(),"文件导入失败:"+e.getMessage());
            result.setInfo(resultList);
        }
        return result;
    }

    // 企业数据导入
    @ApiOperation(value = "企业数据查询", notes = "企业数据查询")
    @RequestMapping(value = "/enterprise/queryAll", method = RequestMethod.GET)
    public RestResult queryEnterpriseData( HttpServletRequest request,EnterpriseDataModeVo enterpriseModeVo) {
        RestResult result = new RestResult();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        try {
            EnterpriseModel enterpriseModel = new EnterpriseModel();
            enterpriseModel.setCreateDate(enterpriseModeVo.getCreateDate());
            Map<String,Object> results =  enterpriseService.queryEnterpriseData(enterpriseModel);
          result.setInfo(results);
          result.setMeta(HttpStatus.OK.value(),"");
        } catch (BaseException e) {
            logger.debug("企业数据查询异常：{}", e.getMessage());
            result.setMeta(SrConstantMDA.INTF_RET_CODE_EXCEPTION,e.getMessage());
            result.setInfo(resultList);
        }
        return result;
    }

    @ApiModel
    class EnterpriseDataModeVo implements Serializable {

        @ApiModelProperty(value = "创建时间")
        @DateTimeFormat(pattern="yyyy-MM-dd")//页面写入数据库时格式化
        @TableField(value = "create_date")
        private Date createDate;

        public Date getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }

        List<String> ids;

        public List<String> getIds() {
            return ids;
        }

        public void setIds(List<String> ids) {
            this.ids = ids;
        }
    }

    // 企业数据删除
    @ApiOperation(value = "企业数据删除", notes = "企业数据删除")
    @RequestMapping(value = "/enterprise/deleteByCode", method = RequestMethod.DELETE)
    public RestResult deleteEnterpriseData( HttpServletRequest request, @RequestBody EnterpriseDataModeVo enterpriseModeVo) {
        RestResult result = new RestResult();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        try {
            enterpriseService.deleteEnterpriseData(enterpriseModeVo.getIds());
            result.setMeta(HttpStatus.OK.value(),"删除成功");
        } catch (BaseException e) {
            logger.debug("企业数据删除异常：{}", e.getMessage());
            result.setInfo(resultList);
            result.setMeta(HttpStatus.BAD_REQUEST.value(),e.getMessage());
        }
        return result;
    }

}
