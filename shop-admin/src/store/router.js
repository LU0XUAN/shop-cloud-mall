import {defineStore} from "pinia";

export const routerStore = defineStore("router",{
    state:()=>{
        return{
            routers:[],
            menus:[],
            isrouters:false,
            titles:[],
            authorites:[],
        }
    },
    getters:{

    },
    actions:{
        router(){
        }
    }

})