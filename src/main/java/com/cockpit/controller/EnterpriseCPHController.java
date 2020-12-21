package com.cockpit.controller;


import com.cockpit.commons.config.SrConstantMDA;
import com.cockpit.commons.exception.BaseException;
import com.cockpit.commons.model.RestResult;
import com.cockpit.model.EnterpriseComprehensiveModel;
import com.cockpit.service.impl.EnterpriseComprehensiveServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(value = "分水镇驾驶舱企业数据查询", tags = {"EnterpriseCPHController"},description ="分水镇驾驶舱企业数据查询")
@RestController
public class EnterpriseCPHController {



    @Autowired
    private EnterpriseComprehensiveServiceImpl enterpriseComprehensiveService;

    private static Logger logger = LoggerFactory.getLogger(EnterpriseCPHController.class);

    // 分水镇企业数据查询
    @ApiOperation(value = "分水镇驾驶舱企业数据查询", notes = "分水镇驾驶舱企业数据查询")
    @RequestMapping(value = "/enterpriseCPH/queryAll", method = RequestMethod.GET)
    public RestResult queryEnterpriseData(HttpServletRequest request, EnterpriseCPHModeVo enterpriseCPHModeVo) {
        RestResult result = new RestResult();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        try {
            EnterpriseComprehensiveModel enterpriseComprehensiveModel = new EnterpriseComprehensiveModel();
            enterpriseComprehensiveModel.setEnterpriseName(enterpriseCPHModeVo.getEnterpriseName());
            Map<String,Object> results =  enterpriseComprehensiveService.queryEnterpriseCphData(enterpriseComprehensiveModel);
            result.setInfo(results);
            result.setMeta(HttpStatus.OK.value(),"");
        } catch (BaseException e) {
            logger.debug("分水镇驾驶舱企业数据查询：{}", e.getMessage());
            result.setMeta(SrConstantMDA.INTF_RET_CODE_EXCEPTION,e.getMessage());
            result.setInfo(resultList);
        }
        return result;
    }


    @ApiModel
    static class EnterpriseCPHModeVo implements Serializable {

        private String enterpriseName;

        public String getEnterpriseName() {
            return enterpriseName;
        }

        public void setEnterpriseName(String enterpriseName) {
            this.enterpriseName = enterpriseName;
        }
    }
}
