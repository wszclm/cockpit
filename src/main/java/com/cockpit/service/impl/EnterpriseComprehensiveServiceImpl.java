package com.cockpit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cockpit.commons.exception.BaseException;
import com.cockpit.dao.EnterpriseComprehensiveDao;
import com.cockpit.model.EnterpriseComprehensiveModel;
import com.cockpit.service.IEnterpriseComprehensiveService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EnterpriseComprehensiveServiceImpl extends ServiceImpl<EnterpriseComprehensiveDao, EnterpriseComprehensiveModel> implements IEnterpriseComprehensiveService {

    @Autowired
    private EnterpriseComprehensiveDao enterpriseComprehensiveDao;

    public Map<String, Object> queryEnterpriseCphData(EnterpriseComprehensiveModel enterpriseComprehensiveModel ) throws BaseException {
        int pageNo = 15;
        int pageSize = 0;
        Map<String, Object> resultMap = new HashMap<>();
        QueryWrapper<EnterpriseComprehensiveModel> wrapper = new QueryWrapper<EnterpriseComprehensiveModel>();
        Page page = PageHelper.startPage(pageNo, pageSize,true);
        if (!org.springframework.util.StringUtils.isEmpty(enterpriseComprehensiveModel.getEnterpriseName())){
            wrapper.eq("ENTERPRISE_NAME",enterpriseComprehensiveModel.getEnterpriseName());
        }
        List<EnterpriseComprehensiveModel> list =  enterpriseComprehensiveDao.selectList(wrapper);
        Map<String,Object> pager = new HashMap<>();
        resultMap.put("data", list);
        resultMap.put("total",page.getTotal());
        return resultMap;
    }

}
