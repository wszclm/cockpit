package com.cockpit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cockpit.commons.exception.BaseException;
import com.cockpit.dao.CloudEnterpriseDao;
import com.cockpit.model.CloudEnterpriseModel;
import com.cockpit.model.EnterpriseModel;
import com.cockpit.service.ICloudEnterpriseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CloudEnterpriseServiceImpl  extends ServiceImpl<CloudEnterpriseDao, CloudEnterpriseModel> implements ICloudEnterpriseService {

    @Autowired
    private  CloudEnterpriseDao cloudEnterpriseDao;

    public void batchImport(List<String[]> list) throws BaseException {
        List<CloudEnterpriseModel> entityList = new ArrayList<>(list.size());
        for (String[] str : list) {

            CloudEnterpriseModel cloudEnterpriseModel = new CloudEnterpriseModel();
            if (str.length == 2) {
                cloudEnterpriseModel.getTown(StringUtils.isEmpty(str[1]) ? " " : str[1]);
                cloudEnterpriseModel.getNum(str[2].toString() ? " " : str[2]);
                enterpriseModel.setCreateDate(new Date());
                enterpriseModel.setUpdateDate(new Date());
                // 只导入不存在的企业，根据统一认证的企业编号判断库中是否已经存在，若已经存在更新。
                if (isExists(enterpriseModel)){
                    continue;
                }

            }
            entityList.add(enterpriseModel);
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
    public boolean isExists(EnterpriseModel enterpriseModel){
        QueryWrapper<EnterpriseModel> wrapper = new QueryWrapper<EnterpriseModel>();
        wrapper.eq("enterPrise_name",enterpriseModel.getEnterPriseName());
        EnterpriseModel res =  this.getOne(wrapper);
        if (res!=null){
            this.update(wrapper);
            return true;
        }
        return false;
    }

}


