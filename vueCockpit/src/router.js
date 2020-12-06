import Vue from 'vue'
import Router from 'vue-router'
import Login from './views/Login.vue'
import Home from './views/Home.vue'
import userInfo from './views/UserInfo.vue'
import MainEvaluateMethod from './views/dataMaintain/MainEvaluateMethod.vue'
import Indicators from './views/dataMaintain/MainIndicators.vue'

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
                    path: '/userInfo',
                    name: '个人中心',
                    component: userInfo,
                    hidden: true
                },
                {
                    path: '/indicators',
                    name: '新增评价办法',
                    component:Indicators,
                    hidden: true
                },
                {
                    path: '/mainEvaluateMethod',
                    name: '综合评价办法',
                    component:MainEvaluateMethod,
                    hidden: true
                }
            ]
        }, {
            path: '*',
            redirect: '/home'
        }
    ]
})