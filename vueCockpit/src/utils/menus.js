import {getRequest, postRequest} from "./api";

export const initMenu = (router, store) => {
    // if (store.state.routes.length > 0) {
    //     return;
    // }
    const data=[
    {
        "component": "DataCloudEnterprise",
        "enabled": true,
        "iconCls": "fa fa-laptop",
        "id": 1,
        "meta": {
            "requireAuth": true
        },
        "name": "上云企业",
        "parentId": 1,
        "path": "/dataImport/dataCloudEnterprise",
        "url": "/"
    },
    {
        "component": "DataEnterprise",
        "enabled": true,
        "iconCls": "fa fa-legal",
        "id": 2,
        "meta": {
            "requireAuth": true
        },
        "name": "企业信息",
        "parentId": 1,
        "path": "/dataImport/dataEnterprise",
        "url": "/"
    },
    {
        "component": "DataHidDangerClass",
        "enabled": true,
        "iconCls": "fa fa-leanpub",
        "id": 3,
        "meta": {
            "requireAuth": true
        },
        "name": "隐患整改分析",
        "parentId": 1,
        "path": "/dataImport/dataHidDangerChange",
        "url": "/"
    },
    {
        "component": "DataHidDangerClass",
        "enabled": true,
        "iconCls": "fa fa-windows",
        "id": 4,
        "meta": {
            "requireAuth": true
        },
        "name": "隐患分类分析",
        "parentId": 1,
        "path": "/dataImport/dataHidDangerClass",
        "url": "/"
    },{
        "component": "DataHidDangerClass",
        "enabled": true,
        "iconCls": "fa fa-windows",
        "id": 4,
        "meta": {
            "requireAuth": true
        },
        "name": "风险等级分析",
        "parentId": 1,
        "path": "/dataImport/dataHidDangerClass",
        "url": "/"
    },
        {
        "component": "DataCoreMonitor",
        "enabled": true,
        "iconCls": "fa fa-windows",
        "id": 4,
        "meta": {
            "requireAuth": true
        },
        "name": "核心指标监测",
        "parentId": 1,
        "path": "/dataImport/DataCoreMonitor",
        "url": "/"
    },
        {
        "component": "DataIntelligentTip",
        "enabled": true,
        "iconCls": "fa fa-windows",
        "id": 4,
        "meta": {
            "requireAuth": true
        },
        "name": "智能预警",
        "parentId": 1,
        "path": "/dataImport/DataIntelligentTip",
        "url": "/"
    },
        {
        "component": "DataSafeHiddenTrouble",
        "enabled": true,
        "iconCls": "fa fa-windows",
        "id": 4,
        "meta": {
            "requireAuth": true
        },
        "name": "安全隐患",
        "parentId": 1,
        "path": "/dataImport/dataSafeHiddenTrouble",
        "url": "/"
    },
        {
        "component": "DataSafetyCode",
        "enabled": true,
        "iconCls": "fa fa-windows",
        "id": 4,
        "meta": {
            "requireAuth": true
        },
        "name": "安全生产码",
        "parentId": 1,
        "path": "/dataImport/dataSafetyCode",
        "url": "/"
    },
        {
        "component": "DataSmokeInterface",
        "enabled": true,
        "iconCls": "fa fa-windows",
        "id": 4,
        "meta": {
            "requireAuth": true
        },
        "name": "烟感接口",
        "parentId": 1,
        "path": "/dataImport/dataSmokeInterface",
        "url": "/"
    },
        {
        "component": "DataVideoInterface",
        "enabled": true,
        "iconCls": "fa fa-windows",
        "id": 4,
        "meta": {
            "requireAuth": true
        },
        "name": "视屏接口",
        "parentId": 1,
        "path": "/dataImport/dataVideoInterface",
        "url": "/"
    },
        {
        "component": "DataHumidity",
        "enabled": true,
        "iconCls": "fa fa-windows",
        "id": 4,
        "meta": {
            "requireAuth": true
        },
        "name": "温湿数据",
        "parentId": 1,
        "path": "/dataImport/DataHumidity",
        "url": "/"
    },
        {
        "component": "DataUserManage",
        "enabled": true,
        "iconCls": "fa fa-windows",
        "id": 4,
        "meta": {
            "requireAuth": true
        },
        "name": "用户管理",
        "parentId": 1,
        "path": "/dataImport/dataUserManage",
        "url": "/"
    }
];
    let fmtRoutes = formatRoutes(data);
    router.addRoutes(fmtRoutes);
    store.commit('initRoutes', fmtRoutes);
    store.dispatch('connect');
    console.log(JSON.stringify(data));
    window.sessionStorage.setItem("user", JSON.stringify(data));
}
export const formatRoutes = (routes) => {
    let fmRoutes = [];
    routes.forEach(router => {
        let {
            path,
            component,
            name,
            meta,
            iconCls,
            children
        } = router;
        if (children && children instanceof Array) {
            children = formatRoutes(children);
        }
        let fmRouter = {
            path: path,
            name: name,
            iconCls: iconCls,
            meta: meta,
            children: children,
            component(resolve) {
                if (component.startsWith("Home")) {
                    require(['../views/' + component + '.vue'], resolve);
                }else if(component.startsWith("Data")){
                    console.log(component);
                    require(['../views/dataImport/' + component + '.vue'], resolve);
                }
            }
        }
        fmRoutes.push(fmRouter);
    })
    return fmRoutes;
}
