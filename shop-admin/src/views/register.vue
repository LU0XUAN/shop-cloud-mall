<template>
  <div class="cont">
    <el-form ref="form"
             label-position="right"
             label-width="0px"
             class="login-form"
             :model="ruleForm">
      <el-form-item prop="img">
        <div style="width: 100%;display: flex;justify-content: center;align-items: center;padding-bottom: 20px;">
          <el-upload
              class="avatar-uploader"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
              :headers="file.headers"
              :action="file.uploadFileUrl"
          >
            <img v-if="ruleForm.avatar!==''" :src="ruleForm.avatar" class="avatar"/>
            <el-icon v-else class="avatar-uploader-icon">
              <Plus/>
            </el-icon>
          </el-upload>
        </div>
      </el-form-item>
      <el-form-item  prop="nickname">
        <el-input class="input-with-select" v-model="ruleForm.nickname" :placeholder="$t('app.nickname')" clearable>
        <template #prefix>
          <el-icon><User /></el-icon>
        </template>
      </el-input>
      </el-form-item>
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
      <el-form-item  prop="password">
        <el-input
            type="password"
            clearable
            :placeholder="$t('app.confirmedPass')"
            v-model="ruleForm.qrpassword"
            class="input-with-select">
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item prop="sex">
        <el-select v-model="ruleForm.sex" :placeholder="$t('app.selSex')" style="width: 100%">
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
          <el-option
              v-for="item in sex"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button :loading="load" type="primary" @click="submitForm" style="width: 100%">{{ $t('app.reg') }}</el-button>
      </el-form-item>

      <router-link :to="{path:'/login'}" class="tips">
        {{ $t('app.userTip') }}
      </router-link>
    </el-form>

  </div>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref} from 'vue'
import type {FormInstance, FormRules} from 'element-plus'
import {reg} from "@/api/login"
import {userStore} from "@/store/user";
import {useRouter} from 'vue-router'
import {routerStore} from "@/store/router";
import {ElMessage} from 'element-plus'
import {UploadProps} from "element-plus";
import {adduser} from "@/api/user";
import {Plus} from "@element-plus/icons-vue";
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
const file = reactive({
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
    label: t('app.man'),
  },
  {
    value: '1',
    label: t('app.woman'),
  }
]
//表单属性值
const ruleForm = reactive({
  username:'',
  password:'',
  qrpassword:'',
  nickname:'',
  avatar:'',
  sex: '',
  phone:'',
  roleName:0,
});

//校验规则
const rules = reactive<FormRules>({
  userName: [
    {required: true, message: '用户名不能为空', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '密码不能为空', trigger: 'blur'},
  ],
  code: [
    {required: true, message: '验证码不能为空', trigger: 'blur'},
  ],
})
const handleAvatarSuccess: UploadProps['onSuccess'] = (
    response,
    uploadFile
) => {
  ruleForm.avatar = response.data.urlIp
}

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Avatar picture size can not exceed 2MB!')
    return false
  }
  return true
}
const register = () => {
  dialogVisible.value = true
}
//表单提交
const submitForm = () => {
  if (ruleForm.username===''||ruleForm.password===''||ruleForm.nickname===''||ruleForm.avatar===''||ruleForm.sex===''||ruleForm.qrpassword==='') {
    ElMessage({
      type: 'warning',
      message: t('app.fromNoClear')+'！',
    })
    return
  }
  if (ruleForm.password!==ruleForm.qrpassword) {
    ElMessage({
      type: 'warning',
      message: t('app.twoNuPass')+'！',
    })
    return
  }
  reg(ruleForm).then(r => {
    if (r.code===200) {
      ElMessage({
        type: 'success',
        message: t('app.success'),
      })
      router.push('/login')
    }
  })
}

//页面加载后执行
onMounted(() => {
})

</script>

<style scoped>
.cont {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-image: url(../assets/bj.jpg);

  background-repeat: no-repeat;
  overflow: hidden;
  background-size: cover;
}

.login-form {
  border-radius: 6px;
  background: rgb(137 198 255 / 35%);
  width: 300px;
  padding: 25px 25px 5px 25px;
  margin-bottom: 100px;
}

.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-code {
  display: flex;
}

.login-code > img {
  height: 35px;
  margin-left: 10px;
  cursor: pointer;
}

.tips {
  float: right;
  font-size: 14px;
  color: #ffffff;
  margin-bottom: 10px;
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

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 80px;
  height: 80px;
  text-align: center;
}

</style>
