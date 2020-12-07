import {getRequest, postRequest} from "./api";

export const initMenu = (router, store) => {
    if (store.state.routes.length > 0) {
        return;
    }
    debugger
    const data=[
        {
            "component": "DataEnterprise",
            "enabled": true,
            "iconCls": "fa fa-laptop",
            "id": 2,
            "meta": {
                "requireAuth": true
            },
            "name": "上云企业",
            "parentId": 1,
            "path": "/dataImport/dataEnterprise",
            "url": "/"
        },
        {
            "component": "DataCount",
            "enabled": true,
            "iconCls": "fa fa-legal",
            "id": 3,
            "meta": {
                "requireAuth": true
            },
            "name": "企业信息",
            "parentId": 1,
            "path": "/dataImport/dataCount",
            "url": "/"
        },
        {
            "component": "DataEnterprise",
            "enabled": true,
            "iconCls": "fa fa-leanpub",
            "id": 4,
            "meta": {
                "requireAuth": true
            },
            "name": "隐患整改分析",
            "parentId": 1,
            "path": "/dataImport/dataEnterprise",
            "url": "/"
        },
        {
            "component": "DataEnterpriseLease",
            "enabled": true,
            "iconCls": "fa fa-windows",
            "id": 6,
            "meta": {
                "requireAuth": true
            },
            "name": "隐患分类分析",
            "parentId": 1,
            "path": "/dataImport/dataEnterpriseLease",
            "url": "/"
        }
    ];
    let fmtRoutes = formatRoutes(data);
    router.addRoutes(fmtRoutes);
    store.commit('initRoutes', fmtRoutes);
    store.dispatch('connect');
}
export const formatRoutes = (routes) => {
    debugger
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
                } else if (component.startsWith("Emp")) {
                    require(['../views/emp/' + component + '.vue'], resolve);
                }else if(component.startsWith("Sys")){
                    console.log(component);
                  require(['../views/sys/' + component + '.vue'], resolve);
                }else if(component.startsWith("Data")){
                    console.log(component);
                    require(['../views/dataImport/' + component + '.vue'], resolve);
                }else if(component.startsWith("Main")){
                    console.log(component);
                    require(['../views/dataMaintain/' + component + '.vue'], resolve);
                }
            }
        }
        fmRoutes.push(fmRouter);
    })
    return fmRoutes;
}
