<template>
  <div class="cont">
    <el-form
        ref="ruleFormRef"
        :model="ruleForm"
        :rules="rules"
        class="login-form"
        status-icon>
      <h3 class="title">{{ $t('app.logo') }} - {{ $t('app.login') }}</h3>
      <el-form-item  prop="username">
        <el-input class="input-with-select" v-model="ruleForm.username" :placeholder="$t('app.username')" clearable>
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item  prop="password">
        <el-input
            type="password"
            clearable
            :placeholder="$t('app.password')"
            v-model="ruleForm.password"
            class="input-with-select">
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button :loading="load" type="primary" @click="submitForm(ruleFormRef)" style="width: 100%">{{ $t('app.login') }}</el-button>
      </el-form-item>
      <router-link :to="{path:'/register'}"  class="tips">
        {{ $t('app.noUser') }}
      </router-link>
    </el-form>

  </div>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref} from 'vue'
import type {FormInstance, FormRules} from 'element-plus'
import {userStore} from "@/store/user";
import { useRouter } from 'vue-router'
import {routerStore} from "@/store/router";
import { ElMessage } from 'element-plus'
import {UploadProps} from "element-plus";
import {adduser} from "@/api/user";
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
const file =reactive({
  uploadFileUrl: "http://localhost:8081" + '/file/upload',
  headers: {
    Authorization: localStorage.getItem("token"),
  },
})
const ruleFormRef = ref<FormInstance>()
const form = ref(null);
const useStore = userStore();
const router = useRouter();
const routerstore = routerStore();
const options = ref([]);
//按钮等待加载
const load = ref(false);
const dialogVisible = ref(false);
//验证码图片
const image = ref('');
const sex = [
  {
    value: '0',
    label: '男',
  },
  {
    value: '1',
    label: '女',
  },
]
//表单属性值
const ruleForm = reactive({
  username:'admin',
  password:'123456',
})
const registerForm = reactive({
  id:null,
  username:null,
  password:'123456',
  nickname:null,
  avatar:'',
  sex: '',
  phone:null,
  roleName:0,
});

//校验规则
const rules = reactive<FormRules>({
  username: [
    { required: true, message: t('app.nuUsername'), trigger: 'blur' },
  ],
  password: [
    { required: true, message:  t('app.nuPass'), trigger: 'blur' },
  ],
})
const handleAvatarSuccess: UploadProps['onSuccess'] = (
    response,
    uploadFile
) => {
  registerForm.avatar = response.data.urlIp
}

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Avatar picture size can not exceed 2MB!')
    return false
  }
  return true
}
const register = () => {
  dialogVisible.value=true
}
//表单提交
const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      load.value = true
      console.log('submit!')
      useStore.login(ruleForm).then(res=>{
        load.value = false
        window.location.href = '/index'
      }).catch(err=>{
        load.value = false
        ElMessage.error(err?.msg || '登录失败')
      })
    } else {
      console.log('error submit!')
      return false
    }
  })
}

const submitRegisterForm = () => {
  form.value.validate(valid=>{
    if (valid){
      adduser(registerForm).then(res=>{
        dialogVisible.value = false;
      })
    }
  })
}

//页面加载后执行
onMounted(()=>{

})

</script>

<style scoped>
.cont{
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-image: url(../assets/bj.jpg);

  background-repeat: no-repeat;
  overflow: hidden;
  background-size: cover;
}
.login-form{
  border-radius: 6px;
  background: rgb(137 198 255 / 35%);
  width: 300px;
  padding: 25px 25px 5px 25px;
  margin-bottom: 100px;
}

.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #ffffff;
}
.login-code {
  display: flex;
}

.login-code>img{
  height: 35px;
  margin-left: 10px;
  cursor: pointer;
}
.avatar-uploader .avatar {
  width: 80px;
  height: 80px;
  display: block;
}
</style>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  border-radius: 25px;

}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}
.tips {
  float: right;
  font-size: 14px;
  color: #ffffff;
  margin-bottom: 10px;
}
.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 80px;
  height: 80px;
  text-align: center;
}
</style>
