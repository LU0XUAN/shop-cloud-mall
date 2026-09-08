<template>
  <div class="body">
    <section class="product-list">
      <div class="container">
        <div>
          <el-form :inline="true" label-width="68px">
            <el-form-item :label="$t('app.recipient')">
              <el-input v-model="query.nickname" :placeholder="$t('app.pleaseInp')"/>
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
              <el-table-column prop="nickname" :label="$t('app.recipient')"/>
              <el-table-column prop="phone" :label="$t('app.phone')"/>
              <el-table-column prop="province" :label="$t('app.province')"/>
              <el-table-column prop="city" :label="$t('app.city')"/>
              <el-table-column prop="region" :label="$t('app.region')"/>
              <el-table-column prop="address" :label="$t('app.address')"/>

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
                width="500px">
              <el-form ref="form"
                       :rules="rules"
                       :label-position="labelPosition"
                       :label-width="$t('app.width')"
                       :model="ruleForm">

                <el-form-item :label="$t('app.recipient')">
                  <el-input v-model="ruleForm.nickname"/>
                </el-form-item>
                <el-form-item :label="$t('app.phone')">
                  <el-input v-model="ruleForm.phone"/>
                </el-form-item>
                <el-form-item :label="$t('app.addrs')">
                  <!--默认使用-->
                  <el-input :value="concatenatedAddress(ruleForm)" v-if="ruleForm.id!=null" disabled/>
                  <elui-china-area-dht v-model="ruleForm.addrs" @change="onChange" style="width: 100%"></elui-china-area-dht>
                  <!--带isall参数和leave参数示例-->
                  <!--        <elui-china-area-dht isall :leave="2" @change="onChange" style="width: 100%"></elui-china-area-dht>-->
                </el-form-item>
                <el-form-item :label="$t('app.address')">
                  <el-input v-model="ruleForm.address"/>
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
      </div>
    </section>
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
import {Search, Refresh, Plus, Edit, Delete} from '@element-plus/icons-vue'
import {lists,add,edit,del} from "@/api/addr";
import Pagination from '@/components/pagination.vue';
import { onMounted, reactive, ref} from 'vue';
import type {FormRules} from 'element-plus'
import {ElMessage, ElMessageBox} from "element-plus";
import {UploadProps} from "element-plus";
import { EluiChinaAreaDht } from 'elui-china-area-dht'
const chinaData = new EluiChinaAreaDht.ChinaArea().chinaAreaflat
const labelPosition = ref('right');
const type = ref(1);
const list = ref([])
const total = ref(0);
const loading = ref(false);
const dialogVisible = ref(false);
const title = ref('');
const form = ref(null);
const ruleForm = reactive({
  id: '',
  nickname: null,
  phone: '',
  province: '',
  city: '',
  region: '',
  address: '',
  addrs: [],
});
const query = reactive({
  page: 1,
  pageSize: 10,
  nickname: '',
});

const rules = reactive<FormRules>({

})

const concatenatedAddress = (data) => {
  return data.province +"/"+ data.city +"/"+ data.region
}
// 覆盖区县
const onChange = (e:any) => {
  const province = chinaData[e[0]]
  const city = chinaData[e[1]]
  const area = chinaData[e[2]]
  console.log(province, city, area)
  ruleForm.province = province.label
  ruleForm.city = city.label
  ruleForm.region = area.label
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
  ruleForm.nickname = null;
  ruleForm.phone='';
  ruleForm.province='';
  ruleForm.city='';
  ruleForm.region='';
  ruleForm.address='';
  ruleForm.addrs=[];
}

const handleQuery = () => {
  getlist()
}

const resetQuery = () => {
  query.nickname = null
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

const handleEdit = (data) => {
  title.value =  t('app.edit')
  dialogVisible.value = true;
  type.value = 2;
  getform(data)
}

const getform = (data) => {
  ruleForm.id = data.id;
  ruleForm.nickname = data.nickname;
  ruleForm.phone = data.phone;
  ruleForm.province = data.province;
  ruleForm.city = data.city;
  ruleForm.region = data.region;
  ruleForm.address = data.address;
  ruleForm.addrs = data.addrs;
}


onMounted(() => {
  getlist();
})
</script>

<style scoped>


.body {
  font-family: Arial, sans-serif;
  line-height: 1.6;
  background-color: #ffffff;
  flex-direction: column;
  min-height: 100vh;
  display: flex;
}

.container {
  width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.product-list {
  margin: 0 auto;
  padding: 20px 0;
  background-color: #fff;
}

.product-list .container {
  max-width: 1200px;
  margin: 0 auto;
}
</style>
