import {defineStore} from "pinia";
import { login} from "@/api/login";
import {ElMessage} from "element-plus";

export const userStore = defineStore("user",{
    state:()=>{
        return{
            token:localStorage.getItem("token") || '',
            userInfo:{},
            isrouters:false,
        }
    },
    getters:{

    },
    actions:{
        login(data){
            return new Promise((resolve, reject)=>{
                login(data).then(res=>{
                    console.log(res.data.userInfo)
                    // if (res.data.userInfo.author.includes('admin')) {
                        localStorage.setItem("token",res.data.access_token);
                        localStorage.setItem("userInfo",JSON.stringify(res.data.userInfo));
                        this.token = res.data.access_token
                        this.userInfo = res.data.userInfo
                        resolve(res)
                    // }else {
                    //     ElMessage.error("非管理员角色")
                    //     //触发登录按钮禁止旋转
                    //     reject()
                    // }

                }).catch(onerror=>{
                    //触发登录按钮禁止旋转
                    reject(onerror)
                })

            })
        },
        out(){
            localStorage.removeItem('token')
            localStorage.removeItem('userInfo')
        },
        getUserInfo(){
            return this.userInfo
        },
        setUserInfo(){
            this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
        },
    }

})
