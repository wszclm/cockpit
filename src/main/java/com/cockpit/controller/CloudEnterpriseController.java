package com.cockpit.controller;


// 上云企业

import com.baomidou.mybatisplus.annotation.TableField;
import com.cockpit.commons.config.SrConstantMDA;
import com.cockpit.commons.exception.BaseException;
import com.cockpit.commons.model.RestResult;
import com.cockpit.commons.utils.ExcelUtil;
import com.cockpit.model.CloudEnterpriseModel;
import com.cockpit.service.impl.CloudEnterpriseServiceImpl;
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

@Api(value = "上云企业导入", tags = {"CloudEnterpriseController"},description ="上云企业数据管理")
@RestController
public class CloudEnterpriseController {
    private static Logger logger = LoggerFactory.getLogger(CloudEnterpriseController.class);

     @Autowired
    private CloudEnterpriseServiceImpl cloudEnterpriseService;

    @ApiOperation(value = "上云企业导入", notes = "上云企业数据导入")
    @RequestMapping(value = "/cloudenterprise/batchImport", method = RequestMethod.POST)
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
                cloudEnterpriseService.batchImport(reList);
                result.setMeta(HttpStatus.OK.value(),"");
            }
        } catch (Exception e) {
            logger.debug("上云企业导入异常：{}", e.getMessage());
            result.setMeta(HttpStatus.BAD_REQUEST.value(),"文件导入失败:"+e.getMessage());
            result.setInfo(resultList);
        }

        return result;
    }

    @ApiModel
    static class CloudEnterpriseDataModeVo implements Serializable {

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

    // 上云企业数据查询
    @ApiOperation(value = "上云企业数据查询", notes = "上云企业数据查询")
    @RequestMapping(value = "/cloudenterprise/queryAll", method = RequestMethod.GET)
    public RestResult queryEnterpriseData( HttpServletRequest request,CloudEnterpriseController.CloudEnterpriseDataModeVo cloudEnterpriseDataModeVo) {
        RestResult result = new RestResult();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        try {

            CloudEnterpriseModel cloudEnterpriseModel = new CloudEnterpriseModel();
            cloudEnterpriseModel.setCreateDate(cloudEnterpriseDataModeVo.getCreateDate());
            Map<String,Object> results =  cloudEnterpriseService.queryEnterpriseData(cloudEnterpriseModel);
            result.setInfo(results);
            result.setMeta(HttpStatus.OK.value(),"");
        } catch (BaseException e) {
            logger.debug("上云企业数据查询：{}", e.getMessage());
            result.setMeta(SrConstantMDA.INTF_RET_CODE_EXCEPTION,e.getMessage());
            result.setInfo(resultList);
        }
        return result;
    }

    // 上云企业数据删除
    @ApiOperation(value = "上云企业数据删除", notes = "上云企业数据删除")
    @RequestMapping(value = "/cloudenterprise/deleteById", method = RequestMethod.DELETE)
    public RestResult deleteEnterpriseData( HttpServletRequest request, @RequestBody CloudEnterpriseController.CloudEnterpriseDataModeVo cloudEnterpriseDataModeVo) {
        RestResult result = new RestResult();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        try {
            cloudEnterpriseService.deleteEnterpriseData(cloudEnterpriseDataModeVo.getIds());
            result.setMeta(HttpStatus.OK.value(),"删除成功");
        } catch (BaseException e) {
            logger.debug("上云企业数据删除异常：{}", e.getMessage());
            result.setInfo(resultList);
            result.setMeta(HttpStatus.BAD_REQUEST.value(),e.getMessage());
        }
        return result;
    }
}
