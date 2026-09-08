<template>
<!--  1-->
  <div class="common-layout">
    <el-container style="height: 100vh" >
      <el-aside :class="[collapse?'aside-small':'aside','cartoon'] " v-if="horizontal">
        <Aside :isCollapse="collapse"></Aside>
      </el-aside>
      <el-container>
        <el-header>
          <div class="top" :style="{ height: horizontal ? '50px' : '58px', backgroundColor: horizontal ? $config.backHeadergroundColor : $config.backgroundColor }">
            <div class="top-left" v-if="horizontal">
              <component :is="icon" @click="aside" style="cursor:pointer;"></component>
              <breadcrumb></breadcrumb>
            </div>

            <div class="top-right">
              <el-dropdown @command="handleCommand" class="lang-warp">
                <span class="el-dropdown-link">
      {{store.language}}
      <el-icon class="el-icon--right">
        <arrow-down />
      </el-icon>
    </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="zhCn">中文</el-dropdown-item>
                    <el-dropdown-item command="en">English</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
              <div style="display: flex;justify-content: start;align-items: center;width: 150px">
                <span class="ellipsis" :style="{ color: horizontal ? $config.textHeaderColor : $config.textColor }">{{ $t('app.hello') }}：{{ userstore.getUserInfo().nickname }}</span>
                <el-dropdown @command="user">
                                    <span class="el-dropdown-link">
                                       <el-avatar :size="30" :src="userstore.getUserInfo().avatar" />
                                    </span>
                  <template #dropdown>
                    <el-dropdown-menu>
<!--                      <el-dropdown-item command="a">个人中心</el-dropdown-item>-->
                      <el-dropdown-item command="b">{{ $t('app.out') }}</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </div>
          </div>
        </el-header>
        <el-main>
          <!-- 主 -->
          <Main></Main>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script lang="ts" setup>
import Aside from '@/layout/components/aside.vue'
import Main from '@/layout/components/main.vue'
import { useDark, useToggle } from '@vueuse/core'
import { useRouter } from 'vue-router'
import  { ElMessage,ElMessageBox  } from 'element-plus'
import breadcrumb from '@/layout/components/breadcrumb.vue'
import {onMounted, ref} from 'vue'
import {userStore} from "@/store/user";


import { useLangStore } from '@/store/lang'
import { getCurrentInstance } from 'vue'
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
const store = useLangStore()
const { proxy } = getCurrentInstance() as any
const handleCommand = (value: 'zhCn' | 'en') => {
  if (store.language === value) return
  proxy.$i18n.locale = value
  localStorage.setItem('localeLang',value)
  store.changeLang(value)
  ElMessage.closeAll()
  ElMessage.success(`${value === 'en' ? '英文' : '中文'}切换成功！`)
  router.go(0);
}
const userstore = userStore();
const collapse = ref(false); //控制展开缩放
//false 是横向，true是纵向
// const horizontal = ref(false); //菜单样式
const horizontal = ref(true); //菜单样式
const icon = ref("Fold"); // 控制展开缩放的图标
const isDark = useDark()
const router = useRouter();
const aside = () => {
  collapse.value = !collapse.value
  if (collapse.value){
    icon.value = "Expand"
  }else {
    icon.value = "Fold"
  }
}

//全屏
const screen = ()=>{

}
//模式切换
const toggleDark = useToggle(isDark)
// 用户操作
const user = (command)=>{
  if (command === 'b'){
    ElMessageBox.confirm(
        t('app.outMsg'),
        t('app.tip'),
        {
          confirmButtonText: t('app.determine'),
          cancelButtonText: t('app.cancel'),
          type: 'warning',
        }
    )
        .then(() => {
          userstore.out();
          ElMessage({
            message: t('app.success'),
            type: 'success',
          })
          router.push("/login")
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: t('app.cancel'),
          })
        })

  }
}
onMounted(()=>{

})
</script>


<style scoped>
/*滑动效果*/
.cartoon{
  transition: width 0.3s ease-out;
}
.aside{
  width: 201px;
  box-shadow: 3px 0 5px rgba(0, 0, 0, 0.3);
  background-color: #fff;
  z-index: 1;
}
.aside1{
}
.aside-small{
  width: 70px;
  box-shadow: 3px 0 5px rgba(0, 0, 0, 0.3); /* 添加右侧阴影 */
  background-color: #fff; /* 确保背景颜色是白色或你需要的颜色 */
  z-index: 1;
}
.aside-small{
  width: 70px;
}
:deep(.el-header){
  padding: 0;
  height: 50px;
}
:deep(.el-main){
  padding: 20px;
}
.header{
  width: 100%;
  display: flex;
  flex-direction: column;

}
.top{
  display:flex;
  align-items:center;
  border-bottom: 1px solid #cccccc;
  justify-content: space-between;
  height: 50px;
}

svg{
  width: 20px;
  margin-left: 20px;
}
.top-right{
  display: flex;
  margin-right: 20px;
}
.top-righ-item{
  margin-right: 20px;
}
.top-left{
  display: flex;
  align-items:center;
}
.breadcrumb{
  margin-left: 10px;
}

.tags{
  display: flex;
  margin-top:3px;
}

.ellipsis {
  display: inline-block;
  width: 100px;
  padding: 10px;
  color: #ffffff;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-weight: bold;
}

</style>
<style>
.el-switch__core .el-switch__inner .is-text {
  color: #1d0094 !important;
}
.el-dropdown {
  display: flex;
  align-items: center;
}
</style>
