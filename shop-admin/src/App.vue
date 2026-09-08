<template>
  <el-config-provider :locale="lang[store.language]">
    <router-view></router-view>
  </el-config-provider>
</template>

<script setup lang="ts">
import { useLangStore } from '@/store/lang'
const store = useLangStore()

// 导入 Element Plus 语言包
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import en from 'element-plus/es/locale/lang/en'
const lang = {
  zhCn,
  en,
}
</script>
<style>
a {
  text-decoration: none;
  color: #99a9bf;
}
/* 全局控制elementui的消息窗口只弹出一次 */
.el-message {
  top: 20px !important;
}
.el-message:not(:last-child) {
  visibility: hidden;
}
</style>
