package com.cockpit.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("enterprise")
public class UserModel {

    @TableId(type = IdType.AUTO)
    private Integer id;


    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;


    /**
     * 电话
     */
    @TableField(value = "telephone")
    private String telephone;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;



    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
