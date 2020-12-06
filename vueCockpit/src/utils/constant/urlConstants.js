import MAIN_CONSTANT from './mainConstants'
const PRODUCT_URL = 'http://localhost:8090/mujun-mng-service';
const DEV_URL = 'http://localhost:8090/mujun-mng-service';

let ENVIRONMENT = ""

const CURRENT_URL = function () {
  if (process.env.NODE_ENV === 'development') {
    ENVIRONMENT = 'development'
    return DEV_URL;
  }else if (process.env.NODE_ENV === 'serv') {
    ENVIRONMENT = 'serv'
    return DEV_URL;
  } else {//生产
    ENVIRONMENT = 'product'
    return PRODUCT_URL;
  }
}();
const PREFIX = "/apis";

const URLS = {
    /*------------------亩均评价系统begin---------------------------------*/
    LOAD_LAND_DATA: PREFIX + "/countryLand/queryAll",
    LOAD_LAND_IMPORT: PREFIX + "/countryLand/batchImport",
    LOAD_LAND_DEL:  PREFIX +"/countryLand/deleteByCode",
    LOAD_TAX_DATA:  PREFIX +"/countryTaxation/queryAll",
    LOAD_TAX_IMPORT:  PREFIX +"/countryTaxation/batchImport", //国税数据导入
    LOAD_TAX_DEL:  PREFIX +"/countryTaxation/deleteByCode",
    LOAD_ENTERPRISE_ADD:  PREFIX +"/enterprise",
    LOAD_ENTERPRISE_DATA:  PREFIX +"/enterprise/queryAll",
    LOAD_ENTERPRISE_IMPORT:  PREFIX +"/enterprise/batchImport", //企业信息导入
    LOAD_ENTERPRISE_DEL: PREFIX + "/enterprise/deleteByCode",
    LOAD_COUNT_DATA: PREFIX +"/enterpriseCount/queryAll",   // 企业统计数据查询
    LOAD_COUNT_DEL: PREFIX +"/enterpriseCount/deleteByCode",   // 企业统计数据删除
    LOAD_COUNT_IMPORT:PREFIX +"/enterpriseCount/batchImport", // 企业统计数据导入
    LOAD_ENTERPRISE_LEASE_IMPORT:PREFIX +"/leaseMng/batchImport", // 企业租赁数据导入
    LOAD_ENTERPRISE_LEASE_LOAD:  PREFIX +"/leaseMng/queryAll", // 企业租赁查询
    ENTERPRISE_LEASE_ADD:  PREFIX +"/leaseMng/add",  // 企业租赁新增
    ENTERPRISE_LEASE_DEL:  PREFIX +"/leaseMng/delete", // 企业租赁删除
    MAIN_DATA_LIST:PREFIX +"/mainDataList/queryAll", //综合数据列表
    MAIN_DATA_COUNT:PREFIX + "/mainDataList/getCountData",// 综合统计数据查询
    MAIN_EVAL_METHOD_LIST:PREFIX +"/mainDataList/queryEvalMethod", //查询亩均评价办法列表
    MAIN_EVAL_METHOD_DEL: PREFIX + "/mainDataList/deleteByEvalId",//根据ID删除亩均评价办法列表
    MAIN_EVAL_METHOD_ADD:  PREFIX +"/mainDataList/addEvalMethod",//新增亩均评价办法
    MAIN_EVAL_METHOD_DETAIL:  PREFIX +"/mainDataList/getEvalMethodDetail",//获取亩均评价明细
    MAIN_DATA_GET_LEASEDATA:PREFIX +"/mainDataList/getLeaseData", //根据主体企业获取租赁企业的信息
    //文件上传
    API_UPLOAD_FILE: DEV_URL + "/countryLand/batchImport?year={year}&month={month}",
    API_TAX_UPLOAD_FILE: DEV_URL + "/countryTaxation/batchImport?year={year}&month={month}",
    API_ENTERPRISE_UPLOAD_FILE: DEV_URL + "/enterprise/batchImport?year={year}&month={month}",
    API_ENTERPRISE_COUNT_FILE: DEV_URL + "/enterpriseCount/batchImport?year={year}&month={month}",

    API_ENTERPRISE_LEASE_FILE: DEV_URL + "/leaseMng/batchImport",

    /*------------------亩均评价系统end---------------------------------*/
}


export default URLS;

export { CURRENT_URL, ENVIRONMENT, MAIN_CONSTANT}
