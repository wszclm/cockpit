package com.cockpit.controller;


import com.cockpit.commons.config.SrConstantMDA;
import com.cockpit.commons.exception.BaseException;
import com.cockpit.commons.model.RestResult;
import com.cockpit.commons.utils.ExcelUtil;
import com.cockpit.service.impl.TargetMonitorServiceImpl;
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
import java.util.*;

@Api(value = "指标监测数据导入", tags = {"TargetMonitorController"},description ="指标监测数据导入")
@RestController
public class TargetMonitorController {

    private static Logger logger = LoggerFactory.getLogger(TargetMonitorController.class);

    @Autowired
    private TargetMonitorServiceImpl targetMonitorService;

    @ApiOperation(value = "指标监测数据导入", notes = "指标监测数据导入")
    @RequestMapping(value = "/targetMonitor/batchImport", method = RequestMethod.POST)
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
                targetMonitorService.batchImport(reList);
                result.setMeta(HttpStatus.OK.value(),"");
            }
        } catch (Exception e) {
            logger.debug("指标监测数据导入异常：{}", e);
            result.setMeta(HttpStatus.BAD_REQUEST.value(),"文件导入失败:"+e.getMessage());
            result.setInfo(resultList);
        }
        return result;
    }


    @ApiModel
   static class TargetMonitorVo implements Serializable {

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(pattern ="yyyy-MM-dd")//数据库导出页面时json格式化
        @DateTimeFormat(pattern="yyyy-MM-dd")//页面写入数据库时格式化
        private Date startDate;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(pattern ="yyyy-MM-dd")//数据库导出页面时json格式化
        @DateTimeFormat(pattern="yyyy-MM-dd")//页面写入数据库时格式化
        private Date endDate;

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        List<String> ids;

        public List<String> getIds() {
            return ids;
        }

        public void setIds(List<String> ids) {
            this.ids = ids;
        }
    }

    // 指标监测数据查询
    @ApiOperation(value = "指标监测数据查询", notes = "指标监测数据查询")
    @RequestMapping(value = "/targetMonitor/queryAll", method = RequestMethod.GET)
    public RestResult queryEnterpriseData( HttpServletRequest request, TargetMonitorVo targetMonitorVo) {
        RestResult result = new RestResult();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        try {
            HashMap<Object, Object> map = new HashMap<>();
            map.put("startDate",targetMonitorVo.getStartDate());
            map.put("endDate",targetMonitorVo.getEndDate());
            Map<String,Object> results =  targetMonitorService.querySafeCode(map);
            result.setInfo(results);
            result.setMeta(HttpStatus.OK.value(),"");
        } catch (BaseException e) {
            logger.debug("指标监测数据查询：{}", e.getMessage());
            result.setMeta(SrConstantMDA.INTF_RET_CODE_EXCEPTION,e.getMessage());
            result.setInfo(resultList);
        }
        return result;
    }

    // 安全生产码数据删除
    @ApiOperation(value = "指标监测数据删除", notes = "指标监测数据删除")
    @RequestMapping(value = "/targetMonitor/deleteById", method = RequestMethod.DELETE)
    public RestResult deleteEnterpriseData( HttpServletRequest request, @RequestBody TargetMonitorVo targetMonitorVo) {
        RestResult result = new RestResult();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        try {
            targetMonitorService.deleteSafeCode(targetMonitorVo.getIds());
            result.setMeta(HttpStatus.OK.value(),"删除成功");
        } catch (BaseException e) {
            logger.debug("安全生产码数据删除异常：{}", e.getMessage());
            result.setInfo(resultList);
            result.setMeta(HttpStatus.BAD_REQUEST.value(),e.getMessage());
        }
        return result;
    }


}
