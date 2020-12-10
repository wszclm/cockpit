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
import com.cockpit.dao.HidDangerChangeDao;
import com.cockpit.model.EnterpriseModel;
import com.cockpit.model.HidDangerChangeModel;
import com.cockpit.service.IHidDangerChangeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class HidDangerChangeServiceImpl extends ServiceImpl<HidDangerChangeDao,HidDangerChangeModel> implements IHidDangerChangeService {

    @Autowired
    private HidDangerChangeDao hidDangerChangeDao;

    public void batchImport(List<String[]> list) throws BaseException {
        List<HidDangerChangeModel> entityList = new ArrayList<>(list.size());
        for (String[] str : list) {
            HidDangerChangeModel hidDangerChangeModel = new HidDangerChangeModel();
            if (str.length == 3) {
                hidDangerChangeModel.setTreatment(StringUtils.isEmpty(str[0]) ? " " : str[0]);
                hidDangerChangeModel.setHidDangerNum(StringUtils.isEmpty(str[1]) ? "" : str[1]);
                hidDangerChangeModel.setProportion(StringUtils.isEmpty(str[2]) ? "" : str[2]);
                hidDangerChangeModel.setCreateDate(new Date());
                hidDangerChangeModel.setUpdateDate(new Date());
                // 只导入不存在的企业，根据统一认证的企业编号判断库中是否已经存在，若已经存在更新。
                if (isExists(hidDangerChangeModel)){
                    continue;
                }

            }
            entityList.add(hidDangerChangeModel);
        }
        if (entityList.size()==1000){
            this.saveBatch(entityList);
            entityList.clear();

        }
        remove(new QueryWrapper<HidDangerChangeModel>());
        this.saveBatch(entityList);
    }

    /**
     * 已经存在的更新
     * @param HidDangerChangeModel
     * @return
     */
    public boolean isExists(HidDangerChangeModel hidDangerChangeModel){
        QueryWrapper<HidDangerChangeModel> wrapper = new QueryWrapper<HidDangerChangeModel>();
        wrapper.eq("treatment",hidDangerChangeModel.getTreatment());
        wrapper.eq("hid_danger_num",hidDangerChangeModel.getHidDangerNum());
        wrapper.eq("proportion",hidDangerChangeModel.getProportion());
        HidDangerChangeModel res =  this.getOne(wrapper);
        if (res!=null){
        	 QueryWrapper<HidDangerChangeModel> wheremapper = new QueryWrapper<HidDangerChangeModel>();
             wheremapper.eq("id",res.getId());
             this.update(hidDangerChangeModel,wheremapper);
             return true;
        }
        return false;
    }

    public Map<String, Object> queryHidDangerChange(Map<Object,Object> param) throws BaseException {
        int pageNo = 15;
        int pageSize = 0;
        Map<String, Object> resultMap = new HashMap<>();
        QueryWrapper<HidDangerChangeModel> wrapper = new QueryWrapper<HidDangerChangeModel>();
        Page page = PageHelper.startPage(pageNo, pageSize,true);
        List<HidDangerChangeModel> list =  hidDangerChangeDao.selectList(wrapper);
        Map<String,Object> pager = new HashMap<>();
        resultMap.put("data", list);
        resultMap.put("total",page.getTotal());
        return resultMap;
    }

    public void deleteHidDangerChange(List<String> ids) throws BaseException {
        QueryWrapper<HidDangerChangeModel> wrapper = new QueryWrapper<HidDangerChangeModel>();
        if (ids.size()>0){
            wrapper.in("id",ids);
        }
        this.removeByIds(ids);
    }
}
