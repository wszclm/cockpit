package com.cockpit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cockpit.dao.EnterpriseDao;
import com.cockpit.dao.UserDao;
import com.cockpit.model.EnterpriseModel;
import com.cockpit.model.UserModel;
import com.cockpit.service.IEnterpriseService;
import com.cockpit.service.IUserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl  extends ServiceImpl<UserDao, UserModel> implements IUserService {


    @Autowired
    private UserDao userDao;

    public boolean doLogin(UserModel userModel){
        QueryWrapper<UserModel> wrapper = new QueryWrapper<UserModel>();
        wrapper.eq("username",userModel.getUsername());
        wrapper.eq("password",userModel.getPassword());
        UserModel user = this.getOne(wrapper);
        if (user!=null){
            return  true;
        }
        return false;
    }

}
