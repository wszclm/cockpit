package com.cockpit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cockpit.commons.exception.BaseException;
import com.cockpit.dao.EnterpriseDao;
import com.cockpit.model.CloudEnterpriseModel;
import com.cockpit.model.EnterpriseModel;
import com.cockpit.service.IEnterpriseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseDao, EnterpriseModel> implements IEnterpriseService {

    @Autowired
    private EnterpriseDao enterpriseDao;

    public void batchImport(List<String[]> list) throws BaseException {
        List<EnterpriseModel> entityList = new ArrayList<>(list.size());
        for (String[] str : list) {
            EnterpriseModel enterpriseModel = new EnterpriseModel();
            if (str.length == 6) {
                enterpriseModel.setEnterPriseName(StringUtils.isEmpty(str[0]) ? " " : str[0]);
                enterpriseModel.setContact(StringUtils.isEmpty(str[1]) ? " " : str[1]);
                enterpriseModel.setContactNum(StringUtils.isEmpty(str[2]) ? " " : str[2]);
                enterpriseModel.setPorduct(StringUtils.isEmpty(str[3]) ? " " : str[3]);
                enterpriseModel.setTown(StringUtils.isEmpty(str[4]) ? " " : str[4]);
                enterpriseModel.setDetailAddress(StringUtils.isEmpty(str[5]) ? " " : str[5]);
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
        remove(new QueryWrapper<EnterpriseModel>());
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
        	 QueryWrapper<EnterpriseModel> wheremapper = new QueryWrapper<EnterpriseModel>();
             wheremapper.eq("id",res.getId());
             this.update(enterpriseModel,wheremapper);
            return true;
        }
        return false;
    }


    public Map<String, Object> queryEnterpriseData(EnterpriseModel enterpriseModeVo) throws BaseException {
        int pageNo = 15;
        int pageSize = 0;
        Map<String, Object> resultMap = new HashMap<> ();
        QueryWrapper<EnterpriseModel> wrapper = new QueryWrapper<EnterpriseModel>();
        if (!StringUtils.isEmpty(enterpriseModeVo.getEnterPriseName())){
            wrapper.eq("enterPrise_name",enterpriseModeVo.getEnterPriseName());
        }
        if (StringUtils.isNotEmpty(enterpriseModeVo.getCreateDate().toString())){
            wrapper.between("create_date",enterpriseModeVo.getCreateDate(),enterpriseModeVo.getUpdateDate());
        }
            Page page = PageHelper.startPage(pageNo, pageSize,true);
            List<EnterpriseModel> list =  enterpriseDao.selectList(wrapper);
            Map<String,Object> pager = new HashMap<>();
            resultMap.put("data", list);
            resultMap.put("total",page.getTotal());
            return resultMap;
        }


    public void deleteEnterpriseData(List<String> ids) throws BaseException {
        QueryWrapper<EnterpriseModel> wrapper = new QueryWrapper<EnterpriseModel>();

        if (ids.size()>0){
            wrapper.in("id",ids);
        }
        this.removeByIds(ids);
    }

}
