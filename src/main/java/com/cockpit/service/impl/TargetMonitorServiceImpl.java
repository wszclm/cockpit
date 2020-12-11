package com.cockpit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cockpit.commons.exception.BaseException;
import com.cockpit.dao.TargetMonitorDao;
import com.cockpit.model.SafeCodeModel;
import com.cockpit.model.TargetMonitorModel;
import com.cockpit.service.ITargetMonitorService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TargetMonitorServiceImpl extends ServiceImpl<TargetMonitorDao,TargetMonitorModel> implements ITargetMonitorService {

    @Autowired
    private TargetMonitorDao targetMonitorDao;

    public void batchImport(List<String[]> list) throws BaseException {
        List<TargetMonitorModel> entityList = new ArrayList<>(list.size());
        for (String[] str : list) {
            TargetMonitorModel targetMonitorModel = new TargetMonitorModel();
            if (str.length == 3) {
                targetMonitorModel.setEcount(StringUtils.isEmpty(str[0]) ? " " : str[0]);
                targetMonitorModel.setHascheck(StringUtils.isEmpty(str[1]) ? 0L : Long.valueOf(str[1]));
                targetMonitorModel.setNotcheck(StringUtils.isEmpty(str[2]) ? 0L : Long.valueOf(str[2]));
                targetMonitorModel.setCreateDate(new Date());
                targetMonitorModel.setUpdateDate(new Date());
                // 只导入不存在的企业，根据统一认证的企业编号判断库中是否已经存在，若已经存在更新。
                if (isExists(targetMonitorModel)){
                    continue;
                }

            }
            entityList.add(targetMonitorModel);
        }
        if (entityList.size()==1000){
            this.saveBatch(entityList);
            entityList.clear();

        }
        this.remove(new QueryWrapper<TargetMonitorModel>());
        this.saveBatch(entityList);
    }

    public Map<String, Object> queryMonitorData(Map<Object,Object> param) throws BaseException {
        int pageNo = 15;
        int pageSize = 0;
        Map<String, Object> resultMap = new HashMap<>();
        QueryWrapper<TargetMonitorModel> wrapper = new QueryWrapper<TargetMonitorModel>();
        Page page = PageHelper.startPage(pageNo, pageSize,true);
        // 条件查询

        List<TargetMonitorModel> list =  targetMonitorDao.selectList(wrapper);
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
     * @param targetMonitorModel
     * @return
     */
    public boolean isExists(TargetMonitorModel targetMonitorModel){
        QueryWrapper<TargetMonitorModel> wrapper = new QueryWrapper<TargetMonitorModel>();
        wrapper.eq("ecount",targetMonitorModel.getEcount());
        wrapper.eq("hascheck",targetMonitorModel.getHascheck());
        wrapper.eq("notcheck",targetMonitorModel.getNotcheck());
        TargetMonitorModel res =  this.getOne(wrapper);
        if (res!=null){
            QueryWrapper<TargetMonitorModel> wheremapper = new QueryWrapper<TargetMonitorModel>();
            wheremapper.eq("id",res.getId());
            this.update(targetMonitorModel,wrapper);
            return true;
        }
        return false;
    }




}
