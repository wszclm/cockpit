package com.cockpit.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("enterprise_comprehensive_info")
public class EnterpriseComprehensiveModel {
    @TableId(type = IdType.AUTO)
    private String id;


    // 行政区域
    @TableField(value = "ADMIN_DIVISION")
    private String adminDivsion;

    // 组织结构代码
    @TableField(value = "ORGANIZATION_CODE")
    private String organizationCode;

    // 组织名称
    @TableField(value = "ORGANIZATION_NAME")
    private String organizationName;

    //地址
    @TableField(value = "ADDRESS")
    private String address;

    //法人
    @TableField(value = "LEGAL_PERSON")
    private String legalPerson;

    //联系电话
    @TableField(value = "CONTACT_NUMBER")
    private String contactNumber;

    //土地性质
    @TableField(value = "LAND_NATURE")
    private String landNature;

    //土地证面积
    @TableField(value = "LAND_CERT_AREA")
    private String landCertArea;

    //实际用地面积
    @TableField(value = "ACTUAL_LAND_AREA")
    private String actualLandArea;

    //
    @TableField(value = "FLOOR_AREA")
    private String floorArea;

    // 是否外租
    @TableField(value = "IS_RENTED_OUT")
    private String isRentedOut;

    // 企业名称
    @TableField(value = "ENTERPRISE_NAME")
    private String enterpriseName;

    // 统一信用代码
    @TableField(value = "UNIFIED_CREDIT_CODE")
    private String unifiedCreditCode;

    // 租赁面积
    @TableField(value = "RENTAL_AREA")
    private String rentalArea;

    // 经营状况
    @TableField(value = "BUSINESS_STATUS")
    private String businessStatus;

    // 职工总数
    @TableField(value = "EMPLOYEES_TOTAL_NUMBER")
    private String employeesTotalNumber;

    // 停工人数
    @TableField(value = "STOPPAGES_NUMBER")
    private String stoppagesNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdminDivsion() {
        return adminDivsion;
    }

    public void setAdminDivsion(String adminDivsion) {
        this.adminDivsion = adminDivsion;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getLandNature() {
        return landNature;
    }

    public void setLandNature(String landNature) {
        this.landNature = landNature;
    }

    public String getLandCertArea() {
        return landCertArea;
    }

    public void setLandCertArea(String landCertArea) {
        this.landCertArea = landCertArea;
    }

    public String getActualLandArea() {
        return actualLandArea;
    }

    public void setActualLandArea(String actualLandArea) {
        this.actualLandArea = actualLandArea;
    }

    public String getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(String floorArea) {
        this.floorArea = floorArea;
    }

    public String getIsRentedOut() {
        return isRentedOut;
    }

    public void setIsRentedOut(String isRentedOut) {
        this.isRentedOut = isRentedOut;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getUnifiedCreditCode() {
        return unifiedCreditCode;
    }

    public void setUnifiedCreditCode(String unifiedCreditCode) {
        this.unifiedCreditCode = unifiedCreditCode;
    }

    public String getRentalArea() {
        return rentalArea;
    }

    public void setRentalArea(String rentalArea) {
        this.rentalArea = rentalArea;
    }

    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public String getEmployeesTotalNumber() {
        return employeesTotalNumber;
    }

    public void setEmployeesTotalNumber(String employeesTotalNumber) {
        this.employeesTotalNumber = employeesTotalNumber;
    }

    public String getStoppagesNumber() {
        return stoppagesNumber;
    }

    public void setStoppagesNumber(String stoppagesNumber) {
        this.stoppagesNumber = stoppagesNumber;
    }
}
