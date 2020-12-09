package com.cockpit.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@TableName("hid_danger_change")
public class HidDangerChangeModel {

	@TableId(type = IdType.AUTO)
	private String id;

	/**
	 * 处理情况
	 */
	@TableField(value = "treatment")
	private String treatment;

	/**
	 * 隐患数量
	 */
	@TableField(value = "hid_danger_num")
	private String hidDangerNum;

	/**
	 * 比例
	 */
	@TableField(value = "proportion")
	private String proportion;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 页面写入数据库时格式化
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 数据库导出页面时json格式化
	@TableField(value = "create_date")
	private Date createDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 页面写入数据库时格式化
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 数据库导出页面时json格式化
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

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public String getHidDangerNum() {
		return hidDangerNum;
	}

	public void setHidDangerNum(String hidDangerNum) {
		this.hidDangerNum = hidDangerNum;
	}

	public String getProportion() {
		return proportion;
	}

	public void setProportion(String proportion) {
		this.proportion = proportion;
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
