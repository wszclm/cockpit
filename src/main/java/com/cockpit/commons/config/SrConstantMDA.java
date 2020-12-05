package com.cockpit.commons.config;


/**
 * 不会更改的常量都放在这个类中
 */
public class SrConstantMDA {

    public static final String VERSION = new String("2020-09-10");

    //导出文件存放根目录
    //public static final String EXPORT_FILE_HOME = new String("/app/data/sr");
    public static final String EXPORT_FILE_HOME = new String("/app/data/sr/");
    public static final int BATCH_INST_MAX_AMOUNT = 10000;//批量执行最大数
    public static final int BATCH_PN_ESS_MAX_AMOUNT = 100000;//本地网号码划拨ESS最大数
    public static final int BATCH_PN_ESS_EVERY_AMOUNT = 10000;//线程每次处理最大数


     public static final String ACCEPT_URI =  new String();

    //接口返回编码：成功
    public static Integer INTF_RET_CODE_SUCC = 0;
    //接口返回编码：失败
    public static Integer INTF_RET_CODE_FAIL = 1;
    //接口返回编码：异常
    public static Integer INTF_RET_CODE_EXCEPTION = -1;
}
