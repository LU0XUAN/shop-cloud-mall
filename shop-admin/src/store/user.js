import {defineStore} from "pinia";
import { login} from "@/api/login";

export const userStore = defineStore("user",{
    state:()=>{
        return{
            token:localStorage.getItem("token") || '',
            userInfo: localStorage.getItem("userInfo") ? JSON.parse(localStorage.getItem("userInfo")) : {},
            isrouters:false,
        }
    },
    getters:{

    },
    actions:{
        login(data){
            return new Promise((resolve, reject)=>{
                login(data).then(res=>{
                    localStorage.setItem("token",res.data.access_token);
                    localStorage.setItem("userInfo",JSON.stringify(res.data.userInfo));
                    this.token = res.data.access_token
                    this.userInfo = res.data.userInfo
                    resolve(res)
                }).catch(onerror=>{
                    reject(onerror)
                })
            })
        },
        out(){
            localStorage.removeItem('token')
            localStorage.removeItem('userInfo')
            this.token = ''
            this.userInfo = {}
            this.isrouters = false
        },
        getUserInfo(){
            return this.userInfo
        },
        setUserInfo(){
            const info = localStorage.getItem("userInfo");
            if (info) {
                this.userInfo = JSON.parse(info);
            }
        },
    }

})
