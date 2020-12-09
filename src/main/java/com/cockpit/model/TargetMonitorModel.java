package com.cockpit.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 指标监测
 */
@TableName("target_monitor")
public class TargetMonitorModel {


    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 企业总数
     */
    @TableField(value = "ecount")
    private String ecount;


    /**
     * 已巡检
     */
    @TableField(value = "hascheck")
    private Long hascheck;

    /**
     * 未巡检
     */
    @TableField(value = "notcheck")
    private Long notcheck;

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

    public String getEcount() {
        return ecount;
    }

    public void setEcount(String ecount) {
        this.ecount = ecount;
    }

    public Long getHascheck() {
        return hascheck;
    }

    public void setHascheck(Long hascheck) {
        this.hascheck = hascheck;
    }

    public Long getNotcheck() {
        return notcheck;
    }

    public void setNotcheck(Long notcheck) {
        this.notcheck = notcheck;
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
