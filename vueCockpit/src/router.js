import Vue from 'vue'
import Router from 'vue-router'
import Login from './views/Login.vue'
import Home from './views/Home.vue'
import DataEnterprise from './views/dataImport/DataEnterprise.vue'
import DataCloudEnterprise from './views/dataImport/DataCloudEnterprise.vue'
import DataCoreMonitor from './views/dataImport/DataCoreMonitor.vue'
import DataIntelligentTip from './views/dataImport/DataIntelligentTip.vue'
import DataSafeHiddenTrouble from './views/dataImport/DataSafeHiddenTrouble'
import DataSafetyCode from './views/dataImport/DataSafetyCode'
import DataHidDangerFix from './views/dataImport/DataHidDangerFix'
import DataHidDangerClass from './views/dataImport/DataHidDangerClass'
import DataHidDangerWind from './views/dataImport/DataHidDangerWind'

Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/',
            name: 'Login',
            component: Login,
            hidden: true
        }, {
            path: '/home',
            name: 'Home',
            component: Home,
            hidden: true,
            meta: {
                roles: ['admin', 'user']
            },
            children: [
                {
                    path: '/dataImport/dataCloudEnterprise',
                    name: '上云企业',
                    component:DataCloudEnterprise,
                    hidden: true
                },
                {
                    path: '/dataImport/dataEnterprise',
                    name: '企业信息',
                    component:DataEnterprise,
                    hidden: true
                },
                {
                    path: '/dataImport/dataHidDangerFix',
                    name: '隐患整改分析',
                    component:DataHidDangerFix,
                    hidden: true
                },
                {
                    path: '/dataImport/dataHidDangerClass',
                    name: '隐患分类分析',
                    component:DataHidDangerClass,
                    hidden: true
                },
                {
                    path: '/dataImport/dataHidDangerWind',
                    name: '风险等级分析',
                    component:DataHidDangerWind,
                    hidden: true
                },
                {
                    path: '/dataImport/DataCoreMonitor',
                    name: '核心指标监测',
                    component:DataCoreMonitor,
                    hidden: true
                },
                {
                    path: '/dataImport/DataIntelligentTip',
                    name: '智能预警',
                    component:DataIntelligentTip,
                    hidden: true
                },
                {
                    path: '/dataImport/DataSafeHiddenTrouble',
                    name: '安全隐患',
                    component:DataSafeHiddenTrouble,
                    hidden: true
                },
                {
                    path: '/dataImport/DataSafetyCode',
                    name: '安全生产码',
                    component:DataSafetyCode,
                    hidden: true
                }
            ]
        }, {
            path: '*',
            redirect: '/home'
        }
    ]
})