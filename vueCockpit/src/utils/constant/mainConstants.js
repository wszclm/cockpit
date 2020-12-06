
 
export default {
    //常量
    PAGE_SIZE: 7,
    MARKET_TEMPLATE_TYPE: 10,
    QUESTIONNAIRE_TEMPLATE_TYPE: "7100",


    //模版操作类型
    TEMPLATE_OPERATE_EDIT : '1',//配置中
    TEMPLATE_OPERATE_RUNNING : '2',// 流程中
    TEMPLATE_OPERATE_VIEW  : '3',//视图状态


    TRAFFIC_TEMPLATE_TYPE: 801, //流量标识需求单模板
    LAYER_4_PROTOCOL_TYPE: [{ label: "UDP", value: "50" }, { label: "TCP", value: "51" }],
    LAYER_7_PROTOCOL_TYPE: [
        { label: "HTTP", value: "1" },
        { label: "WAP1.x", value: "2" },
        { label: "WAP2.0", value: "3" },
        { label: "SMTP", value: "4" },
        { label: "POP3", value: "5" },
        { label: "IMAP4", value: "6" },
        { label: "FTP（包含动静态FTP）", value: "7" },
        { label: "RTSP流媒体", value: "8" },
        { label: "MMS彩信", value: "9" },
        { label: "HTTPS", value: "10" },
    ],
    COMMON_REGION_ID_BY_JT :  "100008100000",
    GET_OFFER_TYPE_BY_CATALOGTYPE:function(catalog){
        if(catalog  == '738'){
            return '10,11';
        }else if(catalog == '739'){
            return '12';
        }else if(catalog == '740'){
            return '13';
        }else{
            return ''
        }
    },
    YEAR:[
        {text:"2010"},
        {text:"2011"},
        {text:"2012"},
        {text:"2013"},
        {text:"2014"},
    ]
}