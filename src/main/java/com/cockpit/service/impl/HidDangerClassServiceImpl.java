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
import com.cockpit.dao.HidDangerClassDao;
import com.cockpit.model.HidDangerClassModel;
import com.cockpit.service.IHidDangerClassService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class HidDangerClassServiceImpl extends ServiceImpl<HidDangerClassDao,HidDangerClassModel> implements IHidDangerClassService {

    @Autowired
    private HidDangerClassDao hidDangerClassDao;

    public void batchImport(List<String[]> list) throws BaseException {
        List<HidDangerClassModel> entityList = new ArrayList<>(list.size());
        for (String[] str : list) {
            HidDangerClassModel hidDangerClassModel = new HidDangerClassModel();
            if (str.length == 3) {
                hidDangerClassModel.setHidDangerType(StringUtils.isEmpty(str[0]) ? " " : str[0]);
                hidDangerClassModel.setHidDangerNum(StringUtils.isEmpty(str[1]) ? "" : str[1]);
                hidDangerClassModel.setProportion(StringUtils.isEmpty(str[2]) ? "" : str[2]);
                hidDangerClassModel.setCreateDate(new Date());
                hidDangerClassModel.setUpdateDate(new Date());
                // 只导入不存在的企业，根据统一认证的企业编号判断库中是否已经存在，若已经存在更新。
                if (isExists(hidDangerClassModel)){
                    continue;
                }

            }
            entityList.add(hidDangerClassModel);
        }
        if (entityList.size()==1000){
            this.saveBatch(entityList);
            entityList.clear();

        }
        this.saveBatch(entityList);
    }

    /**
     * 已经存在的更新
     * @param HidDangerClassModel
     * @return
     */
    public boolean isExists(HidDangerClassModel hidDangerClassModel){
        QueryWrapper<HidDangerClassModel> wrapper = new QueryWrapper<HidDangerClassModel>();
        wrapper.eq("hid_danger_type",hidDangerClassModel.getHidDangerType());
        wrapper.eq("hid_danger_num",hidDangerClassModel.getHidDangerNum());
        wrapper.eq("proportion",hidDangerClassModel.getProportion());
        HidDangerClassModel res =  this.getOne(wrapper);
        if (res!=null){
        	QueryWrapper<HidDangerClassModel> wheremapper = new QueryWrapper<HidDangerClassModel>();
            wheremapper.eq("id",res.getId());
            this.update(hidDangerClassModel,wheremapper);
            return true;
        }
        return false;
    }

    public Map<String, Object> queryHidDangerClass(Map<Object,Object> param) throws BaseException {
        int pageNo = 15;
        int pageSize = 0;
        Map<String, Object> resultMap = new HashMap<>();
        QueryWrapper<HidDangerClassModel> wrapper = new QueryWrapper<HidDangerClassModel>();
        Page page = PageHelper.startPage(pageNo, pageSize,true);
        List<HidDangerClassModel> list =  hidDangerClassDao.selectList(wrapper);
        Map<String,Object> pager = new HashMap<>();
        resultMap.put("data", list);
        resultMap.put("total",page.getTotal());
        return resultMap;
    }

    public void deleteHidDangerClass(List<String> ids) throws BaseException {
        QueryWrapper<HidDangerClassModel> wrapper = new QueryWrapper<HidDangerClassModel>();
        if (ids.size()>0){
            wrapper.in("id",ids);
        }
        this.removeByIds(ids);
    }
}
