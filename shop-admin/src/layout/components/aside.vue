<template>
<!--  左侧菜单-->
  <div :style="{ background: $config.backgroundColor, height: '100vh', minWidth: '200px' }">
    <el-scrollbar :style="{ background: $config.backgroundColor}">
      <div :style="{display:'flex',justifyContent: $config.logo != '' ? 'start' : 'center',alignItems: 'center'}">
        <img v-if="$config.logo != ''" :src="$config.logo" style="width: 50px;height: 50px;margin-left: 10px"/>
        <h3 :style="{ textAlign: 'center', marginLeft: $config.logo != '' ? '10px' :'0px', color: '#fff' }">商城管理</h3>
      </div>
      <el-menu
          :router="true"
          :default-active="route.path.replace('/','')"
          :collapse="isCollapse"
          :active-text-color="$config.activeTextColor"
          :background-color="$config.backgroundColor"
          class="el-menu-vertical-demo menu"
          default-active="2"
          text-color="#fff"
          :default-openeds="openeds"
      >
        <menus v-for="(item, index) in newlist1" :key="index" :data="item"></menus>
      </el-menu>
    </el-scrollbar>
  </div>

</template>

<script setup>
import menus from '@/layout/components/menus.vue'
import {ref, defineProps} from 'vue'
import { useRoute } from 'vue-router';
const route = useRoute();
const props = defineProps({
  isCollapse:{
    type: Boolean,
    default:false
  }
})
const openeds = ref(['1']);

const newlist1 = [
  {
    id: 1,
    title: '个人中心',
    icon:'HomeFilled',
    url: 'index',
    children:[]
  },
  {
    id: 2,
    title: '用户管理',
    icon:'UserFilled',
    url: 'user',
  },
  {
    id: 3,
    title: '分类管理',
    icon:'Grid',
    url: 'type',
  },
  {
    id: 4,
    title: '商品管理',
    icon:'Goods',
    url: 'goods',
  },
  {
    id: 5,
    title: '订单管理',
    icon:'Tickets',
    url: 'order',
  },
]
</script>

<style scoped>

h3{
  color: white;
}
.menu{
  border: none;
}

</style>
