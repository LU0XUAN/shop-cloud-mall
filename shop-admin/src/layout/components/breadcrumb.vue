<template>
  <el-breadcrumb :separator-icon="ArrowRight" class="breadcrumb">
    <el-breadcrumb-item v-for="(item,index) in list" :key="index">{{ item }}</el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script setup>
import { onBeforeRouteUpdate } from "vue-router";
import { onMounted, ref } from 'vue';
import { useRoute } from "vue-router";
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
import { routerStore } from "@/store/router";
import {ArrowRight} from "@element-plus/icons-vue";

const route = useRoute();
const userstore = routerStore();

const list = ref([]);

// 路由名称与翻译映射关系
const routeTranslationMap = {
  index: 'app.userInfo',
  user: 'app.user',
  type: 'app.type',
  goods: 'app.goods',
  order: 'app.order'
};

// 更新面包屑导航函数
function updateBreadcrumb(name) {
  list.value[0] = t(routeTranslationMap[name]);
}

// 初始化
onMounted(() => {
  updateBreadcrumb(route.path.replace("/", ""));
});

// 监听路由更新
onBeforeRouteUpdate((to) => {
  updateBreadcrumb(to.path.replace("/", ""));
});
</script>

<style scoped>

</style>
