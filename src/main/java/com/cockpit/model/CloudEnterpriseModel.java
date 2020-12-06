package com.cockpit.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 企业数据模型
 */
@TableName("cloudEnterprise")
public class CloudEnterpriseModel {

    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 所在乡镇
     */
    @TableField(value = "town")
    private String town;


    /**
     * 上云企业数量
     */
    /**
     * 所在乡镇
     */
    @TableField(value = "num")
    private Long num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }
}
