<template>
  <div class="index" style="margin-top: 50px">
    <div style="width: 100%;display: flex;justify-content: center">
      <div style="width: 60%">
        <div style="margin: 10px">
          <p style="color: #4d4d4d;margin:0px;font-size: 25px">{{ $t('app.redeemablePoints') }}: <span style="color: #ff4d00;font-weight: bold;font-family: LISU">{{ruleForm.integral}}</span></p>
          <p style="color: #4d4d4d;margin:0px;font-size: 12px">{{ $t('app.pointsDescription') }}</p>
        </div>
        <el-form  ref="form"
                  :label-position="labelPosition"
                  :label-width="$t('app.width')"
                  :model="ruleForm" >

          <div style="width: 100%;display: flex;justify-content: center;align-items: center;padding-bottom: 20px;">
            <el-upload
                class="avatar-uploader"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
                :headers="file.headers"
                :action="file.uploadFileUrl"
            >
              <img v-if="ruleForm.avatar!==''" :src="ruleForm.avatar" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </div>
          <el-form-item :label="$t('app.nickname')" prop="nickname">
            <el-input v-model="ruleForm.nickname" />
          </el-form-item>
          <el-form-item :label="$t('app.username')" prop="username">
            <el-input v-model="ruleForm.username" disabled/>
          </el-form-item>

          <el-form-item :label="$t('app.sex')" prop="sex">
            <el-select v-model="ruleForm.sex" :placeholder="$t('app.selSex')" style="width: 100%">
              <el-option
                  v-for="item in sex"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
          </el-form-item>
          <div style="text-align: center;width: 100%" >
            <el-button style="text-align: center"  type="primary" @click="submitForm">{{ $t('app.update') }}</el-button>
          </div>

        </el-form>
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
const getUser = () => {
  getUserById().then(r => {
    ruleForm.nickname = r.data.nickname
    ruleForm.username = r.data.username
    ruleForm.sex = r.data.sex
    ruleForm.avatar = r.data.avatar
    ruleForm.id = r.data.id
    ruleForm.integral = r.data.integral
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
