package com.cockpit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cockpit.commons.exception.BaseException;
import com.cockpit.dao.CloudEnterpriseDao;
import com.cockpit.model.CloudEnterpriseModel;
import com.cockpit.model.EnterpriseModel;
import com.cockpit.model.HiddenDangerModel;
import com.cockpit.model.SafeCodeModel;
import com.cockpit.service.ICloudEnterpriseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CloudEnterpriseServiceImpl  extends ServiceImpl<CloudEnterpriseDao, CloudEnterpriseModel> implements ICloudEnterpriseService {

    @Autowired
    private  CloudEnterpriseDao cloudEnterpriseDao;

    public void batchImport(List<String[]> list) throws BaseException {
        List<CloudEnterpriseModel> entityList = new ArrayList<>(list.size());
        for (String[] str : list) {
            CloudEnterpriseModel cloudEnterpriseModel = new CloudEnterpriseModel();
            if (str.length == 2) {
                cloudEnterpriseModel.setTown(StringUtils.isEmpty(str[0]) ? " " : str[0]);
                cloudEnterpriseModel.setNum(StringUtils.isEmpty(str[1]) ? 0 : Long.valueOf(str[1]));
                cloudEnterpriseModel.setCreateDate(new Date());
                cloudEnterpriseModel.setUpdateDate(new Date());
                // 只导入不存在的企业，根据统一认证的企业编号判断库中是否已经存在，若已经存在更新。
                if (isExists(cloudEnterpriseModel)){
                    continue;
                }

            }
            entityList.add(cloudEnterpriseModel);
        }
        if (entityList.size()==1000){
            this.saveBatch(entityList);
            entityList.clear();

        }
        this.saveBatch(entityList);
    }


    /**
     * 已经存在的更新
     * @param enterpriseModel
     * @return
     */
    public boolean isExists(CloudEnterpriseModel cloudEnterpriseModel){
        QueryWrapper<CloudEnterpriseModel> wrapper = new QueryWrapper<CloudEnterpriseModel>();
        wrapper.eq("town",cloudEnterpriseModel.getTown());
        CloudEnterpriseModel res =  this.getOne(wrapper);
        if (res!=null){
        	  QueryWrapper<CloudEnterpriseModel> wheremapper = new QueryWrapper<CloudEnterpriseModel>();
              wheremapper.eq("id",res.getId());
              this.update(cloudEnterpriseModel,wheremapper);
            return true;
        }
        return false;
    }

    public Map<String, Object> queryEnterpriseData(CloudEnterpriseModel cloudEnterpriseModel) throws BaseException {
        int pageNo = 15;
        int pageSize = 0;
        Map<String, Object> resultMap = new HashMap<>();
        QueryWrapper<CloudEnterpriseModel> wrapper = new QueryWrapper<CloudEnterpriseModel>();
        Page page = PageHelper.startPage(pageNo, pageSize,true);
        List<CloudEnterpriseModel> list =  cloudEnterpriseDao.selectList(wrapper);
        Map<String,Object> pager = new HashMap<>();
        resultMap.put("data", list);
        resultMap.put("total",page.getTotal());
        return resultMap;
    }



    public void deleteEnterpriseData(List<String> ids) throws BaseException {
        QueryWrapper<CloudEnterpriseModel> wrapper = new QueryWrapper<CloudEnterpriseModel>();

        if (ids.size()>0){
            wrapper.in("id",ids);
        }
        this.removeByIds(ids);
    }


}


