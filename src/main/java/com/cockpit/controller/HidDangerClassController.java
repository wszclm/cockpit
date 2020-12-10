package com.cockpit.controller;


import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cockpit.commons.config.SrConstantMDA;
import com.cockpit.commons.exception.BaseException;
import com.cockpit.commons.model.RestResult;
import com.cockpit.commons.utils.ExcelUtil;
import com.cockpit.service.impl.HidDangerClassServiceImpl;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

@Api(value = "隐患分类分析数据导入", tags = {"HidDangerClassController"},description ="隐患分类分析数据导入")
@RestController
public class HidDangerClassController {

    private static Logger logger = LoggerFactory.getLogger(HidDangerClassController.class);

    @Autowired
    private HidDangerClassServiceImpl hidDangerClassservice;

    @ApiOperation(value = "隐患分类分析数据导入", notes = "隐患分类分析数据导入")
    @RequestMapping(value = "/hidDangerClass/batchImport", method = RequestMethod.POST)
    public RestResult batchHidDangerClass(@RequestParam("file") MultipartFile file, Model model, HttpServletRequest req ) {
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
                hidDangerClassservice.batchImport(reList);
                result.setMeta(HttpStatus.OK.value(),"");
            }
        } catch (Exception e) {
            logger.debug("隐患分类分析导入异常：{}", e.getMessage());
            result.setMeta(HttpStatus.BAD_REQUEST.value(),"文件导入失败:"+e.getMessage());
            result.setInfo(resultList);
        }
        return result;
    }

    @ApiModel
    static class HidDangerClassVo implements Serializable {
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

    // 隐患分类分析查询
    @ApiOperation(value = "隐患分类分析数据查询", notes = "隐患分类分析查询")
    @RequestMapping(value = "/hidDangerClass/queryAll", method = RequestMethod.GET)
    public RestResult queryHidDangerClassData( HttpServletRequest request,HidDangerClassVo hidDangerClassVo) {
        RestResult result = new RestResult();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        try {
            HashMap<Object, Object> map = new HashMap<>();
            map.put("startDate",hidDangerClassVo.getStartDate());
            map.put("endDate",hidDangerClassVo.getEndDate());
            Map<String,Object> results =  hidDangerClassservice.queryHidDangerClass(map);
            result.setInfo(results);
            result.setMeta(HttpStatus.OK.value(),"");
        } catch (BaseException e) {
            logger.debug("隐患分类分析数据查询：{}", e.getMessage());
            result.setMeta(SrConstantMDA.INTF_RET_CODE_EXCEPTION,e.getMessage());
            result.setInfo(resultList);
        }
        return result;
    }

    // 隐患分类分析删除
    @ApiOperation(value = "隐患分类分析删除", notes = "隐患分类分析删除")
    @RequestMapping(value = "/hidDangerClass/deleteById", method = RequestMethod.DELETE)
    public RestResult deleteHidDangerClassData( HttpServletRequest request, @RequestBody HidDangerClassVo hidDangerClassVo) {
        RestResult result = new RestResult();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        try {
        	hidDangerClassservice.deleteHidDangerClass(hidDangerClassVo.getIds());
            result.setMeta(HttpStatus.OK.value(),"删除成功");
        } catch (BaseException e) {
            logger.debug("隐患分类分析删除异常：{}", e.getMessage());
            result.setInfo(resultList);
            result.setMeta(HttpStatus.BAD_REQUEST.value(),e.getMessage());
        }
        return result;
    }
}
