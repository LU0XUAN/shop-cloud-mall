import {createRouter, createWebHistory} from "vue-router";
import Layout from '@/layout/index.vue'
import {userStore} from "@/store/user";

const routes = [
    {
        path: "/login",
        name: "login",
        component: () => import("../views/login.vue"),
    },
    {
        path: "/register",
        name: "register",
        component: () => import("../views/register.vue"),
    },
    {
        path: "/",
        name: "layout",
        component: Layout,
        redirect: 'index',
        children: [
            {
                path:"index",
                name:"主页",
                component:()=>import('../views/index.vue'),
            },
            {
                path: '/user',
                name: '用户管理',
                component: () => import("../views/sys/user.vue"),
            },
            {
                path: '/type',
                name: '环保类型',
                component: () => import("../views/sys/type.vue"),
            },
            {
                path: '/goods',
                name: '商品管理',
                component: () => import("../views/sys/goods.vue"),
            },
            {
                path: '/order',
                name: '订单管理',
                component: () => import("../views/sys/order.vue"),
            },
        ]
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes
});


router.beforeEach(((to, from, next) => {
        var token = localStorage.getItem("token");  //获取token信息
        const userstore = userStore()
        if (to.name !== 'login'&&to.name !== 'register') {
            if (!token){
                localStorage.removeItem('token')
                localStorage.removeItem('userInfo')
                next({name:"login"});
            }else {
                if (!userstore.userInfo || Object.keys(userstore.userInfo).length === 0) {
                    userstore.setUserInfo()
                }
                next();
            }
        }else {
            next();
        }
    }
))

router.afterEach(() => {

})


export default router
