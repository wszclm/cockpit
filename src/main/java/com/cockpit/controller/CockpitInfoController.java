package com.cockpit.controller;


import com.cockpit.commons.config.SrConstantMDA;
import com.cockpit.commons.exception.BaseException;
import com.cockpit.commons.model.RestResult;
import com.cockpit.model.CloudEnterpriseModel;
import com.cockpit.model.EnterpriseModel;
import com.cockpit.service.impl.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "驾驶舱数据展示", tags = {"CockpitInfoController"},description ="驾驶舱数据展示")
@RestController
public class CockpitInfoController {

    private static Logger logger = LoggerFactory.getLogger(CockpitInfoController.class);

    @Autowired
    private CloudEnterpriseServiceImpl cloudEnterpriseService;

    @Autowired
    private EnterpriseServiceImpl enterpriseService;

    @Autowired
    private HidDangerChangeServiceImpl hidDangerChangeService;

    @Autowired
    private HidDangerClassServiceImpl hidDangerClassservice;

    @Autowired
    private HiddenDangerServiceImpl hiddenDangerService;

    @Autowired
    private IntelligentTipsServiceImpl intelligentTipsService;

    @Autowired
    private RiskLevelAnalyServiceImpl riskLevelAnalyService;

    @Autowired
    private SafeCodeServiceImpl safeCodeService;

    @Autowired
    private TargetMonitorServiceImpl targetMonitorService;

    @ApiOperation(value = "驾驶舱数据展示", notes = "驾驶舱数据展示")
    @RequestMapping(value = "/cockpit/getInfo", method = RequestMethod.POST)
    public RestResult queryCockpitDataInfo(HttpServletRequest request) {
        RestResult result = new RestResult();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        HashMap<String, Object> resultMap = new HashMap<>();
        try {
            Map<String,Object> enterpriseData =  enterpriseService.queryEnterpriseData(new EnterpriseModel());
            resultMap.put("enterpriseData",enterpriseData);
            Map<String,Object> cloudenterpriseData =  cloudEnterpriseService.queryEnterpriseData(new CloudEnterpriseModel());
            resultMap.put("cloudpriseData",cloudenterpriseData);
            Map<String,Object> intlligenttipsMap = intelligentTipsService.queryIntelligentTips(new HashMap<>());
            resultMap.put("intelligenttips",intlligenttipsMap);
            Map<String,Object> safeCode = safeCodeService.querySafeCode(new HashMap<>());
            resultMap.put("safeCode",safeCode);
            Map<String,Object> targetmonitor = targetMonitorService.queryMonitorData(new HashMap<>());
            resultMap.put("targetmonitor",targetmonitor);
            Map<String,Object> hiddenDanger = hiddenDangerService.queryHiddenDanger(new HashMap<>());
            resultMap.put("hiddenDanger",hiddenDanger);
            result.setInfo(resultMap);
            result.setMeta(HttpStatus.OK.value(),"");
        } catch (BaseException e) {
            logger.debug("企业数据查询异常：{}", e.getMessage());
            result.setMeta(SrConstantMDA.INTF_RET_CODE_EXCEPTION,e.getMessage());
            result.setInfo(resultList);
        }
        return result;
    }

}
