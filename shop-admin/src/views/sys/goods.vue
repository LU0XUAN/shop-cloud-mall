<template>
  <div>
    <el-form :inline="true" label-width="68px">
      <el-form-item :label="$t('app.goodName')">
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
        <el-table-column prop="name" :label="$t('app.goodName')"/>
        <el-table-column prop="price" :label="$t('app.fPrice')"/>
        <el-table-column prop="type" :label="$t('app.type')">
          <template #default="scope">
            {{getTypeLabel(scope.row.type,dictData)}}
          </template>
        </el-table-column>
        <el-table-column prop="environment" :label="$t('app.environmentalFriendliness')"/>
        <el-table-column :label="$t('app.fImg')">
          <template #default="scope">
            <el-image
                style="width: 50px; height: 50px; border-radius: 25px"
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
        <el-table-column :label="$t('app.upStatus')">
          <template #default="scope">
            <el-switch v-model="scope.row.grounding" @click="updateStatus(scope.row)"/>
          </template>
        </el-table-column>

        <el-table-column :label="$t('app.operation')">
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
          width="1000px" top="20px">
        <el-form ref="form"
                 :rules="rules"
                 :label-position="labelPosition"
                 :label-width="$t('app.width')"
                 :model="ruleForm">

          <el-form-item :label="$t('app.fImg')">
            <div style="width: 100%;">
              <el-upload
                  class="avatar-uploader"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess"
                  :before-upload="beforeAvatarUpload"
                  :headers="file.headers"
                  :action="file.uploadFileUrl"
              >
                <img v-if="ruleForm.img!==''" :src="ruleForm.img" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
              </el-upload>
            </div>
          </el-form-item>
          <el-form-item :label="$t('app.goodName')">
            <el-input v-model="ruleForm.name"/>
          </el-form-item>
          <el-form-item :label="$t('app.fPrice')">
            <el-input v-model="ruleForm.price"/>
          </el-form-item>
          <el-form-item :label="$t('app.environmentalFriendliness')">
            <el-input v-model="ruleForm.environment"/>
          </el-form-item>
          <el-form-item :label="$t('app.type')">
            <el-select
                v-model="ruleForm.type"
                :placeholder="$t('app.pleaseSelType')"
                clearable
            >
              <el-option
                  v-for="item in dictData"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value + ''"
              />
            </el-select>
          </el-form-item>
          <el-form-item
              v-for="(domain, index) in dynamicValidateForm.domains"
              :key="domain.key"
              :label="$t('app.goodAtr') + (index*1 + 1)"
              :prop="'domains.' + index + '.value'"
          >
            <el-input v-model="domain.name" style="width: 20%" :placeholder="$t('app.atr')" />
            <el-input v-model="domain.num" style="width: 20%;margin-left: 66px" :placeholder="$t('app.goodNum')" />
            <el-input v-model="domain.price" style="width: 20%;margin-left: 66px" :placeholder="$t('app.goodPrice')" />
            <el-button class="mt-2" @click.prevent="removeDomain(domain)" style="margin-left: 50px" type="danger">{{ $t('app.del') }}</el-button>
          </el-form-item>
          <el-form-item >
            <el-button @click="addDomain" style="margin-left: 90%" type="success">{{ $t('app.addArt') }}</el-button>
          </el-form-item>
          <el-form-item :label="$t('app.imgs')" prop="numName">
            <el-upload     list-type="picture-card"
                           v-model:file-list="fileList1"
                           :on-success="handleAvatarSuccess1"
                           :before-upload="beforeAvatarUpload"
                           :headers="file.headers"
                           :action="file.uploadFileUrl"
                           :on-remove="handleRemove"
            >
              <el-icon><Plus /></el-icon>
            </el-upload>
          </el-form-item>
          <el-form-item :label="$t('app.remarks')">
            <el-input
                :autosize="{ minRows: 2, maxRows: 4 }"
                type="textarea" v-model="ruleForm.remarks"/>
          </el-form-item>
          <el-form-item :label="$t('app.details')">
            <div style="border: 1px solid #ccc;margin-top: 10px" >
              <Toolbar
                  style="border-bottom: 1px solid #ccc"
                  :editor="editorRef"
                  :defaultConfig="toolbarConfig"
                  :mode="mode"
              />
              <Editor
                  style="height: 70vh; overflow-y: hidden;"
                  v-model="ruleForm.details"
                  :defaultConfig="editorConfig"
                  :mode="mode"
                  @onCreated="handleCreated"
              />
            </div>
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
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
import {Search, Refresh, Plus, Edit, Delete} from '@element-plus/icons-vue'
import {lists,add,edit,del,editStatus} from "@/api/goods";
import {getAll} from "@/api/type";
import Pagination from '@/components/pagination.vue';
import {onBeforeUnmount, onMounted, reactive, ref, shallowRef} from 'vue';
import type {FormRules, UploadFile, UploadUserFile} from 'element-plus'
import {ElMessage, ElMessageBox, ElNotification} from "element-plus";
import {UploadProps} from "element-plus";
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'
import axios from 'axios'
const labelPosition = ref('right');
const type = ref(1);
const list = ref([])
const dictData = ref([])
const total = ref(0);
const loading = ref(false);
const dialogVisible = ref(false);
const title = ref('');
const form = ref(null);
const editorRef = shallowRef()
const mode = ref('simple')
const options1 = ref([
  {
    value: '0',
    label: '否',
  },
  {
    value: '1',
    label: '是',
  }
])
const fileList1 = ref<UploadUserFile[]>([])
const fileList = ref<UploadUserFile[]>([])
const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Avatar picture size can not exceed 2MB!')
    return false
  }
  return true
}
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
const dynamicValidateForm = reactive({
  domains: [
    {
      key: Date.now(),
      price: '',
      name: '',
      num: '',
    },
  ],
})
const removeDomain = (item) => {
  const index = dynamicValidateForm.domains.indexOf(item)
  if (index !== -1) {
    dynamicValidateForm.domains.splice(index, 1)
  }
}
const getTypeLabel = (value,options) => {
  const option = options.find(option => option.value == value);
  return option ? option.label : '';
}
const addDomain = () => {
  dynamicValidateForm.domains.push({
    key: Date.now(),
    price: '',
    name: '',
    num: '',
  })
  console.log(dynamicValidateForm.domains)
}

const handleAvatarSuccess1: UploadProps['onSuccess'] = (
    response,
    uploadFile
) => {
  fileList.value.push( {
    name: response.data.fileName,
    url: response.data.urlIp,
  })
}
const handleRemove = (file: UploadFile) => {
  console.log(file.name)
  const index = fileList.value.findIndex(item => item.name === file.name);
  if (index !== -1) {
    fileList.value.splice(index, 1);
  }
  console.log(fileList.value)
}
const ruleForm = reactive({
  id: '',
  title: null,
  details: '',
  attr: '',
  environment: '',
  price: '',
  grounding: '',
  img: '',
  name: '',
  type: '',
  remarks: '',
  imgs: '',
});
const query = reactive({
  page: 1,
  pageSize: 10,
  name: '',
});

const rules = reactive<FormRules>({

})

const updateStatus = (data) => {
  console.log(data.grounding)
  getform(data)
  editStatus(ruleForm).then(r => {
    ElMessage({
      type: 'success',
      message: t('app.success'),
    })
  })
}
// 工具栏配置
const toolbarConfig = {
  toolbarKeys: [
    // 菜单 key
    'headerSelect',
    'bold', // 加粗
    'italic', // 斜体
    'through', // 删除线
    'underline', // 下划线
    'bulletedList', // 无序列表
    'numberedList', // 有序列表
    'color', // 文字颜色
    'insertLink', // 插入链接
    'fontSize', // 字体大小
    'lineHeight', // 行高
    'uploadImage', // 上传图片
    'uploadVideo',//上传视频
    'delIndent', // 缩进
    'indent', // 增进
    'deleteImage',//删除图片
    'divider', // 分割线
    'insertTable', // 插入表格
    'justifyCenter', // 居中对齐
    'justifyJustify', // 两端对齐
    'justifyLeft', // 左对齐
    'justifyRight', // 右对齐
    'undo', // 撤销
    'redo', // 重做
    'clearStyle', // 清除格式
    'fullScreen' // 全屏
  ]
}
//自定义上传图片
const editorConfig = {
  placeholder: t('app.pleaseInp'), // 配置默认提示
  // readOnly: true,
  MENU_CONF: {                // 配置上传服务器地址
    uploadImage: {
      // 小于该值就插入 base64 格式（而不上传），默认为 0
      // base64LimitSize: 5 * 1024, // 5kb
      // 单个文件的最大体积限制，默认为 2M
      // maxFileSize: 1 * 1024 * 1024, // 1M
      // // 最多可上传几个文件，默认为 100
      // maxNumberOfFiles: 5,
      // 选择文件时的类型限制，默认为 ['image/*'] 。如不想限制，则设置为 []
      allowedFileTypes: ['image/*'],
      // 自定义上传
      customUpload(file, insertFn) { // 文件上传
        const formData = new FormData();
        formData.append("file", file); // 必须放在最后
        axios.post("http://localhost:8081" + '/file/upload', formData, {
          headers: {
            Authorization: localStorage.getItem("token"),
          }
        }).then(r => {
          insertFn(r.data.data.urlIp, 'alt', r.data.data.fileName)

          console.log(r)
        })
        // 插入到富文本编辑器中，主意这里的三个参数都是必填的，要不然控制台报错：typeError: Cannot read properties of undefined (reading 'replace')
        // insertFn(result.data.data.url, result.data.data.name, result.data.data.name)
      }
    },
    uploadVideo: {
      async customUpload(file, insertFn) {
        try {
          //自定义上传云储存结束
          const formData = new FormData();
          formData.append("file", file); // 必须放在最后
          axios.post("http://localhost:8081" + '/file/upload', formData, {
            headers: {
              Authorization: localStorage.getItem("token"),
            }
          }).then(r => {
            insertFn(r.data.data.urlIp, r.data.data.fileName)
          })
        } catch (e) {
          ElNotification.error('上传附件失败！');
        }
      }
    }
  }
}
// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor) => {
  editorRef.value = editor // 记录 editor 实例，重要！
}
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
  ruleForm.id = null;
  ruleForm.title = null;
  ruleForm.details=null;
  ruleForm.img='';
  ruleForm.environment='';
  ruleForm.price='';
  ruleForm.name='';
  ruleForm.type='';
  ruleForm.remarks='';
  fileList1.value = []
  fileList.value = []
  dynamicValidateForm.domains = []
  dynamicValidateForm.domains.push(
      {
        key: Date.now(),
        price: '',
        name: '',
        num: '',
      })
}

const handleQuery = () => {
  getlist()
}


const resetQuery = () => {
  query.name = null
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
      if (valid) {
        const img = fileList.value
        let mainPicture = ''
        for (let i = 0; i < img.length; i++) {
          mainPicture += img[i].url + ","
        }
        const mainPictures = mainPicture.substring(0,mainPicture.length-1)
        ruleForm.imgs = mainPictures
        const attr = []
        const datil = dynamicValidateForm.domains
        for (let i = 0; i < datil.length; i++) {
          if (datil[i].name!=='' && datil[i].num !== '' && datil[i].price !== '') {
            attr.push({
              name:datil[i].name,
              num:datil[i].num,
              price:datil[i].price
            })
          }
        }
        ruleForm.attr = JSON.stringify(attr)
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
  ruleForm.id = data.id;
  ruleForm.title = data.title;
  ruleForm.details = data.details;
  ruleForm.grounding = data.grounding;
  ruleForm.img = data.img;
  ruleForm.name = data.name;
  ruleForm.type = data.type;
  ruleForm.environment = data.environment;
  ruleForm.price = data.price;
  ruleForm.remarks = data.remarks;
  const img = data.imgs
  if (img) {
    fileList.value = []
    fileList1.value = []
    const result = img.split(","); // 使用逗号作为分隔符
    for (let i = 0; i < result.length; i++) {
      const filename = result[i].substring(result[i].lastIndexOf('/') + 1);
      fileList1.value.push({
        name:filename,
        url:result[i],
      })
      fileList.value.push({
        name:filename,
        url:result[i],
      })
    }
  }else {
    fileList.value = []
    fileList1.value = []
  }
  const attrs = JSON.parse(data.attr)
  dynamicValidateForm.domains = []
  for (let i = 0; i < attrs.length; i++) {
    dynamicValidateForm.domains.push(
        {
          key: Date.now(),
          price: attrs[i].price,
          name: attrs[i].name,
          num: attrs[i].num,
        })
  }
}

const getByModules = () => {
  getAll().then(r => {
    for (let i = 0; i < r.data.length; i++) {
      dictData.value.push({
        value:r.data[i].id,
        label:r.data[i].name
      })
    }
  })
}

onMounted(() => {
  getlist();
  getByModules();
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
