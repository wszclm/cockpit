package com.cockpit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cockpit.model.CloudEnterpriseModel;
import com.cockpit.model.EnterpriseModel;
import org.springframework.stereotype.Repository;

@Repository
public interface CloudEnterpriseDao extends BaseMapper<CloudEnterpriseModel> {
}
