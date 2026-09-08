<template>
  <div>
    <el-form :inline="true" label-width="68px">
      <el-form-item :label="$t('app.typeName')">
        <el-input v-model="query.name" :placeholder="$t('app.pleaseInp')"/>
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
        >{{ $t('app.add') }}
        </el-button>
      </el-form-item>
    </el-form>

    <div style="padding-top: 10px">
      <el-table v-loading="loading" :data="list" :header-cell-style="{background:'#eef1f6',color:'#606266'}">
        <el-table-column prop="name" :label="$t('app.typeName')"></el-table-column>
        <el-table-column prop="createTime" :label="$t('app.createTime')"></el-table-column>
        <el-table-column :label="$t('app.operation')" width="260px">
          <template #default="scope">
            <el-button size="small"
                       type="success"
                       text
                       :icon="Edit"
                       @click="handleEdit(scope.row)"
            >{{ $t('app.edit') }}
            </el-button>
            <el-button
                size="small"
                type="danger"
                text
                :icon="Delete"
                @click="handleDelete(scope.row)"
            >{{ $t('app.del') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <Pagination :total="total" @pageChange="page"></Pagination>

      <el-dialog
          v-model="dialogVisible"
          :title="title"
          width="500px">
        <el-form ref="form" :rules="rules" :label-position="labelPosition" label-width="80px" :model="ruleForm">
          <el-form-item :label="$t('app.typeName')" prop="grjsdj">
            <el-input v-model="ruleForm.name"></el-input>
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

<script lang="ts" setup>
import {Search, Refresh, Plus, Edit, Delete} from '@element-plus/icons-vue'
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
import {lists,add,edit,del} from "@/api/type";
import Pagination from '@/components/pagination.vue';
import {onBeforeUnmount, onMounted, reactive, ref, shallowRef} from 'vue';
import type {FormRules} from 'element-plus'
import {ElMessage, ElMessageBox, ElNotification} from "element-plus";
import {UploadProps} from "element-plus";
import '@wangeditor/editor/dist/css/style.css' // 引入 css
const labelPosition = ref('right');
const type = ref(1);
const list = ref([])
const total = ref(0);
const loading = ref(false);
const dialogVisible = ref(false);
const title = ref('');
const form = ref(null);
const options = ref([])
const file =reactive({
  uploadFileUrl: "http://localhost:8081" + '/file/upload',
  headers: {
    Authorization: localStorage.getItem("token"),
  },
})
const handleAvatarSuccess: UploadProps['onSuccess'] = (
    response,
    uploadFile
) => {
  ruleForm.img = response.data.urlIp
}
const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Avatar picture size can not exceed 2MB!')
    return false
  }
  return true
}
const getTypeLabel1 = (value) => {
  const option = options.value.find(option => option.value == value);
  return option ? option.label : '';
}
const ruleForm = reactive({

});
const query = reactive({
  page: 1,
  pageSize: 10,
  name: '',
});
const resetQuery = () => {
  query.name = ''
  getlist()
}
const rules = reactive<FormRules>({

})


const page = (e) => {
  query.page = e.currentPage;
  query.pageSize = e.pageSize;
  getlist()
}

const handleAdd = () => {
  title.value = t('app.add')
  dialogVisible.value = true
  type.value = 1;
  reset();
}
const reset = () => {
  Object.keys(ruleForm).forEach(key => {
    ruleForm[key] = '';
  });
}

const handleQuery = () => {
  getlist()
}

const getlist = () => {
  loading.value = true;
  lists(query).then(res=>{
    list.value = res.data;
    total.value = res.total;
    loading.value = false;
  })
}

const submitForm = () => {
  form.value.validate(valid => {
    if (valid) {
      if (type.value == 1) {
        //添加
        add(ruleForm).then(res=>{
          getlist();
          dialogVisible.value = false;
        })
      } else {
        edit(ruleForm).then(res=>{
          getlist();
          dialogVisible.value = false;
        })
      }
    }
  })
}
const handleDelete = (data) => {
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
        del(data.id).then(r => {
          getlist();
          dialogVisible.value = false;
          ElMessage({
            type: 'success',
            message:  t('app.success'),
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

const handleEdit = (data) => {
  title.value = t('app.edit');
  dialogVisible.value = true;
  type.value = 2;
  getform(data)
}

const getform = (data) => {
  Object.assign(ruleForm, data);
}

onMounted(() => {
  getlist();
})
</script>

<style scoped>

</style>
<style>
.avatar-uploader .avatar {
  width: 80px;
  height: 80px;
  display: block;
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  border-radius: 25px;
  width: 80px;
  height: 80px;

}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}
</style>
