<template>
  <div>
    <el-form  :inline="true"  label-width="68px" >
      <el-form-item :label="$t('app.username')">
        <el-input v-model="query.username" placeholder="请输入" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" size="mini" @click="handleQuery">{{ $t('app.select') }}</el-button>
        <el-button :icon="Refresh" size="mini" @click="resetQuery">{{ $t('app.reset') }}</el-button>
        <el-button
            type="primary"
            plain
            :icon="Plus"
            size="mini"
            @click="handleAdd"
        >{{  $t('app.add') }}</el-button>
      </el-form-item>
    </el-form>

    <div style="padding-top: 10px">
      <el-table v-loading="loading" :data="list" :header-cell-style="{background:'#eef1f6',color:'#606266'}">
        <el-table-column prop="username" :label=" $t('app.username')"  />
        <el-table-column prop="nickname" :label=" $t('app.nickname')"  />
        <el-table-column prop="roleName" :label=" $t('app.roleName')">
          <template #default="scope">
            <el-tag :type="scope.row.role===0?'success':'primary'">{{ scope.row.role===0?$t('app.admin'):$t('app.users') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sex" :label="$t('app.sex')"  >
          <template #default="scope">
            <el-tag :type="scope.row.sex==='0'?'success':'primary'">{{ scope.row.sex==='0'?$t('app.man'):$t('app.woman') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('app.avatar')">
          <template #default="scope">
            <img :src="scope.row.avatar" style="width: 40px;height: 40px;border-radius:25px"/>
          </template>
        </el-table-column>
        <el-table-column :label="$t('app.operation')">
          <template #default="scope">
            <el-button size="small"
                       type="success"
                       text
                       :icon="Edit"
                       @click="handleEdit(scope.row)"
            >{{ $t('app.edit') }}</el-button>
            <el-button
                size="small"
                type="danger"
                text
                :icon="Delete"
                @click="handleDelete(scope.row)"
            >{{ $t('app.del') }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <Pagination  :total="total" @pageChange="page"></Pagination>

      <el-dialog
          v-model="dialogVisible"
          :title="title"
          width="500px">
        <el-form  ref="form"
                  :rules="rules"
                  :label-position="labelPosition"
                  label-width="80px"
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
          <el-form-item :label="$t('app.username')" prop="username">
            <el-input v-model="ruleForm.username" :disabled="type!='1'"/>
          </el-form-item>
          <el-form-item :label="$t('app.nickname')" prop="nickname">
            <el-input v-model="ruleForm.nickname" />
          </el-form-item>
          <el-form-item :label="$t('app.password')" prop="password" v-if="type===1">
            <el-input v-model="ruleForm.password"  type="password"/>
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
        </el-form>

        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">{{ $t('app.cancel') }}</el-button>
        <el-button type="primary" @click="submitForm">{{ $t('app.determine') }}</el-button>
      </span>
        </template>
      </el-dialog>

    </div>
  </div>
</template>

<script  lang="ts" setup>
import { Search,Refresh,Plus,Edit,Delete } from '@element-plus/icons-vue'
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
import Pagination from '@/components/pagination.vue';
import { onMounted,reactive, ref } from 'vue';
import {listUser,adduser,edituser,deleteuser} from "@/api/user";
import type { FormInstance, FormRules } from 'element-plus'
import type { UploadProps } from 'element-plus'
import {ElMessage, ElMessageBox} from "element-plus";
const labelPosition = ref('right');
const type = ref(1);
const list = ref([]);
const total = ref(0);
const loading = ref(false);
const dialogVisible = ref(false);
const title = ref('');
const form = ref(null);
const options = ref([]);
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
    label: t('app.woman'),
  },
]
const ruleForm = reactive({
  userId:null,
  username:null,
  password:'123456',
  nickname:null,
  avatar:'',
  sex: '',
  role:'1',
});
const query = reactive({
  pageNum:1,
  pageSize:10,
  username:null,
});

const rules = reactive<FormRules>({

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
const page = (e)=>{
  query.pageNum = e.currentPage;
  query.pageSize = e.pageSize;
  getlist()
}

const handleAdd = () => {
  title.value = t('app.add')
  dialogVisible.value = true
  type.value = 1;
  reset();
}
const reset = ()=>{
  ruleForm.userId = null;
  ruleForm.username = null;
  ruleForm.password = '123456';
  ruleForm.sex = '';
  ruleForm.nickname = null;
  ruleForm.avatar = '';
}

const handleQuery = () => {
  getlist()
}

const resetQuery = () => {
  query.username = null
  getlist()
}

const getlist = () => {
  loading.value = true;
  listUser(query).then(res=>{
    list.value = res.data;
    total.value = res.total;
    loading.value = false;
  })
}

const submitForm = () => {
  form.value.validate(valid=>{
    if (valid){
      if (type.value == 1){
        //添加
        adduser(ruleForm).then(res=>{
          getlist();
          dialogVisible.value = false;
        })
      }else {
        //修改

        edituser(ruleForm).then(res=>{
          getlist();
          dialogVisible.value = false;
        })
      }
    }
  })
}
const handleDelete = (data)=>{
  ElMessageBox.confirm(
      t('app.confirmedDel'),
      t('app.tip'),
      {
        confirmButtonText: t('app.determine'),
        cancelButtonText: t('app.cancel'),
        type: 'warning',
      }
  )
      .then(() => {
        deleteuser(data.userId).then(res=>{
          getlist();
          dialogVisible.value = false;
          ElMessage({
            type: 'success',
            message: t('app.success'),
          })
        })

      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: t('app.cancel'),
        })
      })

}

const handleEdit = (data)=>{
  title.value =  t('app.edit');
  dialogVisible.value = true;
  type.value = 2;
  getform(data)
}

const getform = (data)=>{
  ruleForm.userId = data.userId;
  ruleForm.username = data.username;
  ruleForm.sex = data.sex;
  ruleForm.nickname = data.nickname;
  ruleForm.avatar = data.avatar;
}

const getroles = () =>{

}

onMounted(()=>{
  getlist();
  getroles();
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
