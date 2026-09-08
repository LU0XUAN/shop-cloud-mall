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
                path:"/index",
                name:"index",
                component:()=>import('../views/index.vue'),
            },
            {
                path:"/type",
                name:"type",
                component:()=>import('../views/page/type.vue'),
            },
            {
                path:"/cart",
                name:"cart",
                component:()=>import('../views/page/cart.vue'),
            },
            {
                path:"/order",
                name:"order",
                component:()=>import('../views/page/order.vue'),
            },
            {
                path:"/addr",
                name:"addr",
                component:()=>import('../views/page/addr.vue'),
            },
            {
                path:"/pass",
                name:"pass",
                component:()=>import('../views/page/pass.vue'),
            },
            {
                path:"/user",
                name:"user",
                component:()=>import('../views/page/user.vue'),
            },
            {
                path: '/details/:id',
                name:"details",
                component:()=>import('../views/page/details.vue'),
            },
            {
                path: '/productStats',
                name:"productStats",
                component:()=>import('../views/page/productStats.vue'),
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
        console.log(to.name)
        if (to.name !== 'login'&&to.name !== 'register'&&to.name !== 'index') {
            if (!token){
                localStorage.removeItem('token')
                localStorage.removeItem('userInfo')
                next({name:"login"});
            }else {
                if (!userstore.isrouters){
                    userstore.setUserInfo()
                    userstore.isrouters = true;
                    next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
                }else {
                    next();
                }
            }
        }else {
            next();
        }
    }
))

router.afterEach(() => {

})


export default router
