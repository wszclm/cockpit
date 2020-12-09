package com.cockpit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cockpit.commons.exception.BaseException;
import com.cockpit.dao.SafeCodeDao;
import com.cockpit.model.SafeCodeModel;
import com.cockpit.service.ISafeCodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SafeCodeServiceImpl extends ServiceImpl<SafeCodeDao,SafeCodeModel> implements ISafeCodeService {


    @Autowired
    private  SafeCodeDao safeCodeDao;

    public void batchImport(List<String[]> list) throws BaseException {
        List<SafeCodeModel> entityList = new ArrayList<>(list.size());
        for (String[] str : list) {
            SafeCodeModel safeCodeModel = new SafeCodeModel();
            if (str.length == 4) {
                safeCodeModel.setRed(StringUtils.isEmpty(str[1]) ? " " : str[1]);
                safeCodeModel.setOrange(StringUtils.isEmpty(str[2]) ? "" : str[2]);
                safeCodeModel.setYellow(StringUtils.isEmpty(str[3]) ? " " : str[3]);
                safeCodeModel.setGreen(StringUtils.isEmpty(str[4]) ? "" : str[4]);
                safeCodeModel.setCreateDate(new Date());
                safeCodeModel.setUpdateDate(new Date());
                // 只导入不存在的企业，根据统一认证的企业编号判断库中是否已经存在，若已经存在更新。
                if (isExists(safeCodeModel)){
                    continue;
                }

            }
            entityList.add(safeCodeModel);
        }
        if (entityList.size()==1000){
            this.saveBatch(entityList);
            entityList.clear();

        }
        this.saveBatch(entityList);
    }

    public Map<String, Object> querySafeCode(Map<Object,Object> param) throws BaseException {
        int pageNo = 15;
        int pageSize = 0;
        Map<String, Object> resultMap = new HashMap<>();
        QueryWrapper<SafeCodeModel> wrapper = new QueryWrapper<SafeCodeModel>();
        Page page = PageHelper.startPage(pageNo, pageSize,true);
        // 条件查询

        List<SafeCodeModel> list =  safeCodeDao.selectList(wrapper);
        Map<String,Object> pager = new HashMap<>();
        resultMap.put("data", list);
        resultMap.put("total",page.getTotal());
        return resultMap;
    }

    public void deleteSafeCode(List<String> ids) throws BaseException {
        QueryWrapper<SafeCodeModel> wrapper = new QueryWrapper<SafeCodeModel>();
        if (ids.size()>0){
            wrapper.in("id",ids);
        }
        this.removeByIds(ids);
    }

    /**
     * 已经存在的更新
     * @param safeCodeModel
     * @return
     */
    public boolean isExists(SafeCodeModel safeCodeModel){
        QueryWrapper<SafeCodeModel> wrapper = new QueryWrapper<SafeCodeModel>();
        wrapper.eq("red",safeCodeModel.getRed());
        wrapper.eq("yellow",safeCodeModel.getRed());
        wrapper.eq("orange",safeCodeModel.getRed());
        wrapper.eq("green",safeCodeModel.getRed());
        SafeCodeModel res =  this.getOne(wrapper);
        if (res!=null){
            this.update(wrapper);
            return true;
        }
        return false;
    }

}
