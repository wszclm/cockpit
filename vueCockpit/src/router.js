import Vue from 'vue'
import Router from 'vue-router'
import Login from './views/Login.vue'
import Home from './views/Home.vue'
import MainEvaluateMethod from './views/dataMaintain/MainEvaluateMethod.vue'
import Indicators from './views/dataMaintain/MainIndicators.vue'
import DataEnterprise from './views/dataImport/DataEnterprise.vue'

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
                    path: '/dataImport/dataEnterprise',
                    name: '上云企业',
                    component:DataEnterprise,
                    hidden: true
                },
                {
                    path: '/dataImport/dataCount',
                    name: '企业信息',
                    component:DataEnterprise,
                    hidden: true
                },
                {
                    path: '/dataImport/dataEnterprise',
                    name: '隐患整改分析',
                    component:DataEnterprise,
                    hidden: true
                },
                {
                    path: '/dataImport/dataEnterpriseLease',
                    name: '隐患分类分析',
                    component:DataEnterprise,
                    hidden: true
                }
            ]
        }, {
            path: '*',
            redirect: '/home'
        }
    ]
})