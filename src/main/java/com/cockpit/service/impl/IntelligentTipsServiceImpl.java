package com.cockpit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cockpit.commons.exception.BaseException;
import com.cockpit.dao.IntelligentTipsDao;
import com.cockpit.model.HiddenDangerModel;
import com.cockpit.model.IntelligentTipsModel;
import com.cockpit.service.IIntelligentTipsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IntelligentTipsServiceImpl extends ServiceImpl<IntelligentTipsDao,IntelligentTipsModel> implements IIntelligentTipsService {

    @Autowired
    private IntelligentTipsDao intelligentTipsDao;

    public void batchImport(List<String[]> list) throws BaseException {
        List<IntelligentTipsModel> entityList = new ArrayList<>(list.size());
        for (String[] str : list) {
            IntelligentTipsModel intelligentTipsModel = new IntelligentTipsModel();
            if (str.length == 2) {
                intelligentTipsModel.setEname(StringUtils.isEmpty(str[1]) ? " " : str[1]);
                intelligentTipsModel.setSdanger(StringUtils.isEmpty(str[2]) ? "" : str[2]);
                intelligentTipsModel.setCreateDate(new Date());
                intelligentTipsModel.setUpdateDate(new Date());
                // 只导入不存在的企业，根据统一认证的企业编号判断库中是否已经存在，若已经存在更新。
                if (isExists(intelligentTipsModel)){
                    continue;
                }

            }
            entityList.add(intelligentTipsModel);
        }
        if (entityList.size()==1000){
            this.saveBatch(entityList);
            entityList.clear();

        }
        this.saveBatch(entityList);
    }

    /**
     * 已经存在的更新
     * @param intelligentTipsModel
     * @return
     */
    public boolean isExists(IntelligentTipsModel intelligentTipsModel){
        QueryWrapper<IntelligentTipsModel> wrapper = new QueryWrapper<IntelligentTipsModel>();
        wrapper.eq("ename",intelligentTipsModel.getEname());
        IntelligentTipsModel res =  this.getOne(wrapper);
        if (res!=null){
            this.update(wrapper);
            return true;
        }
        return false;
    }

    public Map<String, Object> queryIntelligentTips(Map<Object,Object> param) throws BaseException {
        int pageNo = 15;
        int pageSize = 0;
        Map<String, Object> resultMap = new HashMap<>();
        QueryWrapper<IntelligentTipsModel> wrapper = new QueryWrapper<IntelligentTipsModel>();
        Page page = PageHelper.startPage(pageNo, pageSize,true);
        // 条件查询

        List<IntelligentTipsModel> list =  intelligentTipsDao.selectList(wrapper);
        Map<String,Object> pager = new HashMap<>();
        resultMap.put("data", list);
        resultMap.put("total",page.getTotal());
        return resultMap;
    }

    public void deleteIntelligentTips(List<String> ids) throws BaseException {
        QueryWrapper<HiddenDangerModel> wrapper = new QueryWrapper<HiddenDangerModel>();
        if (ids.size()>0){
            wrapper.in("id",ids);
        }
        this.removeByIds(ids);
    }

}
