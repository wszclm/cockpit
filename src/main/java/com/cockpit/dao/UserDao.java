package com.cockpit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cockpit.model.EnterpriseModel;
import com.cockpit.model.UserModel;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseMapper<UserModel> {

}
