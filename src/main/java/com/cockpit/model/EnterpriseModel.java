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
@TableName("enterprise")
public class EnterpriseModel {


    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 企业名称
     */
    @TableField(value = "enterPrise_name")
    private String enterPriseName;

    /**
     * 联系人
     */
    @TableField(value = "contact")
    private String contact;


    /**
     * 联系电话
     */
    @TableField(value = "contact_num")
    private String contactNum;


    /**
     * 主要产品
     */
    @TableField(value = "porduct")
    private String porduct;

    /**
     * 所在乡镇
     */
    @TableField(value = "detail_tons")
    private String town;


    /**
     * 详细地址
     */
    @TableField(value = "detail_address")
    private String detailAddress;




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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnterPriseName() {
        return enterPriseName;
    }

    public void setEnterPriseName(String enterPriseName) {
        this.enterPriseName = enterPriseName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getPorduct() {
        return porduct;
    }

    public void setPorduct(String porduct) {
        this.porduct = porduct;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
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
