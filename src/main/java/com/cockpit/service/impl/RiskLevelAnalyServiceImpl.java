package com.cockpit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cockpit.commons.exception.BaseException;
import com.cockpit.dao.RiskLevelAnalyDao;
import com.cockpit.model.RiskLevelAnalyModel;
import com.cockpit.service.IRiskLevelAnalyService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class RiskLevelAnalyServiceImpl extends ServiceImpl<RiskLevelAnalyDao,RiskLevelAnalyModel> implements IRiskLevelAnalyService {

    @Autowired
    private RiskLevelAnalyDao riskLevelAnalyDao;

    public void batchImport(List<String[]> list) throws BaseException {
        List<RiskLevelAnalyModel> entityList = new ArrayList<>(list.size());
        for (String[] str : list) {
            RiskLevelAnalyModel riskLevelAnalyModel = new RiskLevelAnalyModel();
            if (str.length == 2) {
                riskLevelAnalyModel.setEnterPriseName(StringUtils.isEmpty(str[1]) ? " " : str[1]);
                riskLevelAnalyModel.setRiskLevel(StringUtils.isEmpty(str[2]) ? "" : str[2]);
                riskLevelAnalyModel.setCreateDate(new Date());
                riskLevelAnalyModel.setUpdateDate(new Date());
                // 只导入不存在的企业，根据统一认证的企业编号判断库中是否已经存在，若已经存在更新。
                if (isExists(riskLevelAnalyModel)){
                    continue;
                }

            }
            entityList.add(riskLevelAnalyModel);
        }
        if (entityList.size()==1000){
            this.saveBatch(entityList);
            entityList.clear();

        }
        this.saveBatch(entityList);
    }

    /**
     * 已经存在的更新
     * @param RiskLevelAnalyModel
     * @return
     */
    public boolean isExists(RiskLevelAnalyModel RiskLevelAnalyModel){
        QueryWrapper<RiskLevelAnalyModel> wrapper = new QueryWrapper<RiskLevelAnalyModel>();
        wrapper.eq("enterPriseName",RiskLevelAnalyModel.getEnterPriseName());
        wrapper.eq("riskLevel",RiskLevelAnalyModel.getRiskLevel());
        RiskLevelAnalyModel res =  this.getOne(wrapper);
        if (res!=null){
            this.update(wrapper);
            return true;
        }
        return false;
    }

    public Map<String, Object> queryRiskLevelAnaly(Map<Object,Object> param) throws BaseException {
        int pageNo = 15;
        int pageSize = 0;
        Map<String, Object> resultMap = new HashMap<>();
        QueryWrapper<RiskLevelAnalyModel> wrapper = new QueryWrapper<RiskLevelAnalyModel>();
        Page page = PageHelper.startPage(pageNo, pageSize,true);
        List<RiskLevelAnalyModel> list =  riskLevelAnalyDao.selectList(wrapper);
        Map<String,Object> pager = new HashMap<>();
        resultMap.put("data", list);
        resultMap.put("total",page.getTotal());
        return resultMap;
    }

    public void deleteRiskLevelAnaly(List<String> ids) throws BaseException {
        QueryWrapper<RiskLevelAnalyModel> wrapper = new QueryWrapper<RiskLevelAnalyModel>();
        if (ids.size()>0){
            wrapper.in("id",ids);
        }
        this.removeByIds(ids);
    }
}
