<template>
  <div class="body">
    <header>
      <div class="header">
        <div class="logo">
          <img src="@/assets/img/img.png" alt="环保商城">
          <span>{{ $t('app.logo') }}</span>
        </div>
        <nav>
          <ul style="display: flex">
            <li><router-link  :to="{ path: '/index' }" :class="route.path==='/index' ? 'active': ''">{{ $t('app.index') }}</router-link></li>
            <li><router-link  :to="{ path: '/type' }" :class="route.path==='/type' ? 'active': ''" style="margin-right: 20px;">{{ $t('app.type') }}</router-link></li>
            <div v-if="avatar!==''" >
              <li><router-link  :to="{ path: '/cart' }" :class="route.path==='/cart' ? 'active': ''">{{ $t('app.cart') }}</router-link></li>
              <li><router-link  :to="{ path: '/order' }" :class="route.path==='/order' ? 'active': ''">{{ $t('app.order') }}</router-link></li>
              <li><router-link  :to="{ path: '/addr' }" :class="route.path==='/addr' ? 'active': ''">{{ $t('app.addr') }}</router-link></li>
            </div>
            <div v-else>
              <li><router-link  :to="{ path: '/login' }" :class="route.path==='/login' ? 'active': ''">{{ $t('app.login') }}</router-link></li>
              <li><router-link  :to="{ path: '/register' }" :class="route.path==='/register' ? 'active': ''">{{ $t('app.register') }}</router-link></li>
            </div>
            <el-dropdown @command="handleCommand" class="lang-warp">
              <span class="el-dropdown-link">
                {{ store.language }}
                <el-icon>
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
            <div class="right-icons" v-if="avatar!==''">
              <el-dropdown>
                <img :src="avatar" alt="用户头像" style="border-radius: 50%">
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item><router-link to="user">{{ $t('app.user') }}</router-link></el-dropdown-item>
                    <el-dropdown-item><router-link to="productStats">{{ $t('app.productStats') }}</router-link></el-dropdown-item>
                    <el-dropdown-item><router-link to="pass">{{ $t('app.editPass') }}</router-link></el-dropdown-item>
                    <el-dropdown-item><a @click="logout">{{ $t('app.logout') }}</a></el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </ul>
        </nav>
      </div>
    </header>
    <Main></Main>
    <footer>
      <div class="container">
        <p>&copy; 2024 {{ $t('app.copyright') }}</p>
      </div>
    </footer>
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
import { useLangStore } from '@/store/lang'
import { getCurrentInstance } from 'vue'

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
import Main from '@/layout/components/main.vue'
import {onMounted, reactive, ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import { useRouter, useRoute } from 'vue-router'
let router = useRouter();
let route = useRoute();

const avatar = ref('');
import {userStore} from "@/store/user";
const userstore = userStore();

const title = ref('');
const fromData = reactive({
  id: null,

});
const editPass = () => {

}
const logout = () => {
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
        // 清空localStorage
        localStorage.removeItem("token");
        localStorage.removeItem("userInfo");
        router.replace("/login");
        ElMessage({
          type: 'success',
          message: t('app.success'),
        })
      })
      .catch(() => {

      })
}
onMounted(() => {
  const user = JSON.parse(localStorage.getItem("userInfo"))
  if (user==null) {
    avatar.value = ''
  }else {
    avatar.value = user.avatar
  }
})
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}


.body {
  font-family: Arial, sans-serif;
  line-height: 1.6;
  background-color: #f0f0f0;
  flex-direction: column;
  min-height: 100vh;
  display: flex;
}

.container {
  width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.header {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

header {
  background-color: rgb(11, 169, 232);
  color: #fff;
  padding: 10px 0;
}

.logo {
  display: flex;
  align-items: center;
}

.logo img {
  width: 80px;
  height: auto;
  margin-right: 10px;
}

.logo span {
  font-size: 24px;
  font-weight: bold;
}

nav ul {
  list-style-type: none;
  padding: 0;
  text-align: right;
}

nav ul li {
  display: inline-block;
  margin-left: 20px;
}

nav ul li:first-child {
  margin-left: 0;
}

nav ul li a {
  color: #fff;
  text-decoration: none;
  padding: 10px 15px;
  display: block;
}

nav ul li a:hover {
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 5px;
}
nav ul li a.active {
  background-color: rgb(241, 243, 245);
  border-radius: 10px;
  color: #007bff;
}
footer {
  background-color: #333;
  color: #fff;
  text-align: center;
  padding: 10px 0;
  width: 100%;
  margin-top: auto;
  /*position: sticky;*/
  /*bottom: 0;*/
}

.right-icons {
  margin-left: 20px;
  position: relative;
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.right-icons img {
  margin-right: 50px;
  width: 40px;
}
.dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  width: 120px;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0,0,0,.3);
  z-index: 1;
  display: none;
}

.dropdown a {
  display: block;
  padding: 10px;
  text-decoration: none;
  color: #333;
}

.dropdown a:hover {
  background-color: #f2f2f2;
}

.right-icons:hover .dropdown {
  display: block;
}
</style>
<style>
.el-dropdown {
  /* color: #ffffff !important; */
  display: flex !important;
  align-items: center !important;
  margin-left: 20px !important;
}
</style>
