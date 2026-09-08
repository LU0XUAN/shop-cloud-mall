<template>
  <div class="index" style="margin-top: 50px">
    <div style="width: 100%;display: flex;justify-content: center">
      <div style="width: 60%">
        <h1 style="text-align: center;font-size: 30px;color: #51aeff">{{ $t('app.editPass') }}</h1>
        <el-form  ref="form"
                  :label-position="labelPosition"
                  :label-width="$t('app.width')"
                  :model="ruleForm1" >


          <el-form-item :label="$t('app.oldPass')" prop="oldPassword">
            <el-input v-model="ruleForm1.oldPassword" type="password"/>
          </el-form-item>
          <el-form-item :label="$t('app.newPass')" prop="password">
            <el-input v-model="ruleForm1.password" type="password"/>
          </el-form-item>
          <el-form-item :label="$t('app.oldPass')" prop="qrpassword">
            <el-input v-model="ruleForm1.qrpassword" type="password"/>
          </el-form-item>

        </el-form>
        <div style="text-align: center;width: 100%" >
          <el-button style="text-align: center"  type="primary" @click="submitForm1">{{ $t('app.editPass') }}</el-button>
        </div>
      </div>

    </div>
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
import { ref } from 'vue'
import {getUserById,edituserInfo,pwd} from "@/api/user";
import type { TabsPaneContext } from 'element-plus'
import {Plus} from "@element-plus/icons-vue";
import {onMounted, reactive} from "vue";
import {ElMessage, UploadProps} from "element-plus";
const labelPosition = ref('right');
const activeName = ref('first')
const ruleForm = reactive({
  id:null,
  username:null,
  password:'',
  qrpassword:'',
  oldPassword:'',
  nickname:null,
  avatar:'',
  sex: '',
  roleName:0,
});
const ruleForm1 = reactive({
  password:'',
  qrpassword:'',
  oldPassword:'',
});
const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event)
}
const file =reactive({
  uploadFileUrl: "http://localhost:8081" + '/file/upload',
  headers: {
    Authorization: localStorage.getItem("token"),
  },
})

const sex = [
  {
    value: '0',
    label: t('app.man'),
  },
  {
    value: '1',
    label:t('app.woman'),
  },
]
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
const submitForm = () => {
  edituserInfo(ruleForm).then(res=>{
    ElMessage({
      type: 'success',
      message: t('app.success'),
    })
  })
}
const submitForm1 = () => {
  if (ruleForm1.password==='') {
    ElMessage({
      type: 'info',
      message:  t('app.fromNoClear'),
    })
    return;
  }
  if (ruleForm1.oldPassword==='') {
    ElMessage({
      type: 'info',
      message: t('app.fromNoClear'),
    })
    return;
  }
  if (ruleForm1.password !== ruleForm1.qrpassword) {
    ElMessage({
      type: 'info',
      message: t('app.twoNuPass'),
    })
    return
  }
  pwd(ruleForm1).then(res=>{
    console.log(res)
    if (res.code === 200) {
      ruleForm1.password = ''
      ruleForm1.qrpassword = ''
      ruleForm1.oldPassword = ''
      ElMessage({
        type: 'success',
        message: t('app.success'),
      })
    }
  })
}
const getUser = () => {
  getUserById().then(r => {
    ruleForm.nickname = r.data.nickname
    ruleForm.username = r.data.username
    ruleForm.sex = r.data.sex
    ruleForm.avatar = r.data.avatar
    ruleForm.id = r.data.id
  })
}
onMounted(()=>{
  getUser();
})
</script>

<style scoped>
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
