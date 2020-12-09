package com.cockpit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cockpit.commons.exception.BaseException;
import com.cockpit.dao.HiddenDangerDao;
import com.cockpit.model.HiddenDangerModel;
import com.cockpit.service.IHiddenDangerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HiddenDangerServiceImpl extends ServiceImpl<HiddenDangerDao,HiddenDangerModel> implements IHiddenDangerService {

    @Autowired
    private HiddenDangerDao hiddenDangerDao;

    public void batchImport(List<String[]> list) throws BaseException {
        List<HiddenDangerModel> entityList = new ArrayList<>(list.size());
        for (String[] str : list) {
            HiddenDangerModel hiddenDangerModel = new HiddenDangerModel();
            if (str.length == 2) {
                hiddenDangerModel.setEname(StringUtils.isEmpty(str[1]) ? " " : str[1]);
                hiddenDangerModel.setContent(StringUtils.isEmpty(str[2]) ? "" : str[2]);
                hiddenDangerModel.setCreateDate(new Date());
                hiddenDangerModel.setUpdateDate(new Date());
                // 只导入不存在的企业，根据统一认证的企业编号判断库中是否已经存在，若已经存在更新。
                if (isExists(hiddenDangerModel)){
                    continue;
                }

            }
            entityList.add(hiddenDangerModel);
        }
        if (entityList.size()==1000){
            this.saveBatch(entityList);
            entityList.clear();

        }
        this.saveBatch(entityList);
    }

    /**
     * 已经存在的更新
     * @param hiddenDangerModel
     * @return
     */
    public boolean isExists(HiddenDangerModel hiddenDangerModel){
        QueryWrapper<HiddenDangerModel> wrapper = new QueryWrapper<HiddenDangerModel>();
        wrapper.eq("ename",hiddenDangerModel.getEname());
        HiddenDangerModel res =  this.getOne(wrapper);
        if (res!=null){
            this.update(wrapper);
            return true;
        }
        return false;
    }

    public Map<String, Object> queryHiddenDanger(Map<Object,Object> param) throws BaseException {
        int pageNo = 15;
        int pageSize = 0;
        Map<String, Object> resultMap = new HashMap<>();
        QueryWrapper<HiddenDangerModel> wrapper = new QueryWrapper<HiddenDangerModel>();
        Page page = PageHelper.startPage(pageNo, pageSize,true);
        List<HiddenDangerModel> list =  hiddenDangerDao.selectList(wrapper);
        Map<String,Object> pager = new HashMap<>();
        resultMap.put("data", list);
        resultMap.put("total",page.getTotal());
        return resultMap;
    }

    public void deleteHiddenDanger(List<String> ids) throws BaseException {
        QueryWrapper<HiddenDangerModel> wrapper = new QueryWrapper<HiddenDangerModel>();
        if (ids.size()>0){
            wrapper.in("id",ids);
        }
        this.removeByIds(ids);
    }
}
