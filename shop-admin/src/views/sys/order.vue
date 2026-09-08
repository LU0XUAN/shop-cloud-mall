<template>
  <div>
    <el-form :inline="true" label-width="68px">
      <el-form-item :label="$t('app.goodName')">
        <el-input v-model="query.name" placeholder="请输入"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" size="mini" @click="handleQuery">{{ $t('app.select') }}</el-button>
        <el-button :icon="Refresh" size="mini" @click="resetQuery">{{ $t('app.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <div style="padding-top: 10px">
      <el-table v-loading="loading" :data="list" :header-cell-style="{background:'#eef1f6',color:'#606266'}">
        <el-table-column prop="name" :label="$t('app.goodName')"></el-table-column>
        <el-table-column prop="numbers" :label="$t('app.orderNumber')"></el-table-column>
        <el-table-column prop="price" :label="$t('app.price')"></el-table-column>
        <el-table-column prop="num" :label="$t('app.num')"></el-table-column>
        <el-table-column prop="sum" :label="$t('app.sum')"></el-table-column>
        <el-table-column prop="attr" :label="$t('app.attr')"></el-table-column>
        <el-table-column prop="type" :label="$t('app.type')">
          <template #default="scope">
            {{ getTypeLabel(scope.row.type, options) }}
          </template>
        </el-table-column>
        <el-table-column prop="environment" :label="$t('app.environmentalFriendliness')"></el-table-column>
        <el-table-column prop="username" :label="$t('app.username')"></el-table-column>
        <el-table-column prop="createTime" :label="$t('app.createTime')"></el-table-column>
        <el-table-column :label="$t('app.fImg')">
          <template #default="scope">
            <el-image
                style="width: 38px; height: 38px; border-radius: 25px"
                :src="scope.row.img"
                :zoom-rate="1.2"
                :max-scale="7"
                :min-scale="0.2"
                :preview-teleported="true"
                :preview-src-list="[scope.row.img]"
                :initial-index="4"
                fit="cover"
            />
          </template>
        </el-table-column>
        <el-table-column prop="status" :label="$t('app.orderStatus')">
          <template #default="scope">
            <el-dropdown @command="(value) => handleEdit(value, scope.row)">
              <span class="el-dropdown-link">
                <span v-if="scope.row.status=='0'">{{ $t('app.ordered') }}</span>
                <span v-if="scope.row.status=='1'">{{ $t('app.dispatched') }}</span>
                <span v-if="scope.row.status=='2'">{{ $t('app.consignees') }}</span>
                <span v-if="scope.row.status=='3'">{{ $t('app.returns') }}</span>
                <span v-if="scope.row.status=='4'">{{ $t('app.returnsGood') }}</span>
                <el-icon class="el-icon--right">
                  <arrow-down />
                </el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="1">{{ $t('app.dispatcheds') }}</el-dropdown-item>
                  <el-dropdown-item command="3">{{ $t('app.returns') }}</el-dropdown-item>
                  <el-dropdown-item command="2">{{ $t('app.consignees') }}</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
        <el-table-column :label="$t('app.operation')" width="160px">
          <template #default="scope">
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

    </div>
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
import {Search, Refresh, Plus, Edit, Delete} from '@element-plus/icons-vue'
import {lists, add, edit, del,editStatus} from "@/api/order";
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
import {getAll} from "@/api/type";

const options = ref([])
const file = reactive({
  uploadFileUrl: "http://localhost:8081" + '/file/upload',
  headers: {
    Authorization: localStorage.getItem("token"),
  },
})
const getTypeLabel = (value, options) => {
  const option = options.find(option => option.value == value);
  return option ? option.label : '';
}
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
const ruleForm = reactive({});
const query = reactive({
  page: 1,
  pageSize: 10,
  name: '',
});
const resetQuery = () => {
  query.name = ''
  getlist()
}
const rules = reactive<FormRules>({})


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
  lists(query).then(res => {
    list.value = res.data;
    total.value = res.total;
    loading.value = false;
  })
}
const getByModules = () => {
  getAll().then(r => {
    for (let i = 0; i < r.data.length; i++) {
      options.value.push({
        value: r.data[i].id,
        label: r.data[i].name
      })
    }
  })
}

const submitForm = () => {
  form.value.validate(valid => {
    if (valid) {
      if (type.value == 1) {
        //添加
        add(ruleForm).then(res => {
          getlist();
          dialogVisible.value = false;
        })
      } else {
        edit(ruleForm).then(res => {
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

const handleEdit = (data,row) => {
  editStatus({
    id: row.id,
    status: data
  }).then(r => {
    ElMessage({
      type: 'success',
      message: t('app.success'),
    })
    row.status=data
  })
}

const getform = (data) => {
  Object.assign(ruleForm, data);
}

onMounted(() => {
  getlist();
  getByModules();
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
