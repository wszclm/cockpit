package com.cockpit.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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



    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写入数据库时格式化
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")//数据库导出页面时json格式化
    @TableField(value = "create_date")
    private Date createDate;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写入数据库时格式化
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")//数据库导出页面时json格式化
    @TableField(value = "update_date")
    private Date updateDate;

    /**
     * 备注信息
     */
    @TableField(value = "remark")
    private String remark;


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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
