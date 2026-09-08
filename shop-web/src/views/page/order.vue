<template>
  <div class="body">
    <div class="container">
      <section class="orders">
        <h2>{{ $t('app.order') }}</h2>
        <div class="order" v-for="data in list">
          <div class="order-header">
            <div class="order-details">
              <h3>{{ $t('app.orderNumber') }}: #{{ data.numbers }}</h3>
              <p>{{ $t('app.orderTime') }}: {{ data.createTime }}</p>
              <el-dropdown @command="(value) => handleEdit(value, data)" style="margin-left: 20px;color: #e600ff;font-size: 18px;cursor: pointer">
              <span class="el-dropdown-link">
                <span v-if="data.status=='0'">{{ $t('app.orderStatus') }}： {{ $t('app.paid') }}</span>
                <span v-if="data.status=='1'">{{ $t('app.orderStatus') }}： {{ $t('app.shipped') }}</span>
                <span v-if="data.status=='2'">{{ $t('app.orderStatus') }}： {{ $t('app.received') }}</span>
                <span v-if="data.status=='3'">{{ $t('app.orderStatus') }}： {{ $t('app.returned') }}</span>
                <span v-if="data.status=='4'">{{ $t('app.orderStatus') }}： {{ $t('app.pendingReturn') }}</span>
                <el-icon class="el-icon--right">
                  <arrow-down />
                </el-icon>
              </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="2" v-if="data.status=='1'"> {{ $t('app.confirmReceived') }}</el-dropdown-item>
                    <el-dropdown-item command="4" v-if="data.status=='0' || data.status=='1'|| data.status=='2'"> {{ $t('app.applyReturn') }}</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
            <p>{{ $t('app.orderAmount') }}: <strong>￥{{ data.sum }}</strong></p>
            <button class="delete-btn" @click="deleteOrder(data.id)" v-if="data.status=='3'||data.status=='2'">{{ $t('app.del') }}</button>
          </div>

          <div class="order-products">
            <div class="product">
              <img :src="data.img" alt="商品图">
              <div class="hot-sale-label" v-if="data.type != ''">{{ $t('app.isEcoFriendly') }}</div>
              <div class="product-info">
                <h4>{{ data.name }}</h4>
                <p>{{ $t('app.unitPrice') }}: ￥{{ data.price }}</p>
                <p>{{ $t('app.quantity') }}: {{ data.num }}</p>
              </div>
            </div>
            <div>
              <p>{{ $t('app.pointsEarned') }}：<strong style="color: #595959;">{{ data.integral }}</strong></p>
              <p>{{ $t('app.discountAmount') }}：<strong  style="color: #ff7300;">￥{{ data.discount }}</strong></p>
              <p>{{ $t('app.finalPrice') }}：<strong style="color: #ff7300;font-size: 20px">￥{{ data.finalPrice }}</strong></p>
            </div>
          </div>
        </div>
        <Pagination :total="total" @pageChange="page"></Pagination>
      </section>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
import {onMounted, reactive, ref} from 'vue';
import { useRouter, useRoute } from 'vue-router'
import {getMyOrder,del,editStatus} from "@/api/order";
import Pagination from '@/components/pagination.vue';
import {ElMessage, ElMessageBox} from "element-plus";
let router = useRouter();
const total = ref(0);
let route = useRoute();
const title = ref('');
const list = ref([])
import greenFood from '@/assets/img/green_food.png'
import instantNoodles from '@/assets/img/instant_noodles.png'
import greenProduct from '@/assets/img/green_product.png'
import panteneHairMask from '@/assets/img/pantene_hair_mask.png'
import honeysuckleDrink from '@/assets/img/honeysuckle_drink.png'
import yuanmingLiquor from '@/assets/img/yuanming_liquor.png'
import magnetPatch from '@/assets/img/magnet_patch.png'
import decathlonSportShirt from '@/assets/img/decathlon_sport_shirt.png'

const defaultImages = [greenFood, instantNoodles, greenProduct, panteneHairMask, honeysuckleDrink, yuanmingLiquor, magnetPatch, decathlonSportShirt]

const nameToImage = {
  '绿色食物': greenFood,
  '产品名称': greenFood,
  '绿色产品': greenProduct,
  '康师傅方便面': instantNoodles,
  '远明老酒': yuanmingLiquor,
  '潘婷': panteneHairMask,
  '金银花': honeysuckleDrink,
  '磁铁': magnetPatch,
  '迪卡侬': decathlonSportShirt
}

const getImageByName = (name) => {
  if (!name) return defaultImages[0]
  for (const [keyword, image] of Object.entries(nameToImage)) {
    if (name.includes(keyword)) {
      return image
    }
  }
  return defaultImages[0]
}

const fromData = reactive({
  id: null,

});
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
const page = (e) => {
  query.page = e.currentPage;
  query.pageSize = e.pageSize;
  getMyOrders()
}
const query = reactive({
  page: 1,
  pageSize: 10,
  name: '',
});
const deleteOrder = (id) => {
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
        del(id).then(r => {
          getMyOrders();
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
const getMyOrders = () => {
  getMyOrder(query).then(res => {
    list.value = res.data.map((item) => {
      if (!item.img || item.img === '商品1') {
        item.img = getImageByName(item.name)
      }
      return item
    });
    total.value = res.total;
  })
}
onMounted(() => {
  getMyOrders()
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

header {
  background-color: rgb(11, 169, 232);
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.orders {
  margin-top: 20px;
}

.order {
  background-color: #fff;
  border: 1px solid #ddd;
  margin-bottom: 20px;
  padding: 20px;
  position: relative;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.order-details {
  display: flex;
  align-items: center;
}

.order-details h3 {
  margin-right: 20px;
  font-size: 18px;
}

.order-details p {
  color: #007bff;
  font-weight: bold;
}

.order-products {
  margin-top: 10px;
  border-top: 1px solid #ddd;
  padding-top: 10px;
  display: flex;
  justify-content: space-between;
}

.product {
  display: flex;
  margin-bottom: 10px;
}

.product img {
  width: 80px;
  height: auto;
  margin-right: 10px;
  display: block;
}

.product-info {
  flex: 1;
}

.product-info h4 {
  margin: 0;
  font-size: 16px;
}

.product-info p {
  color: #007bff;
  font-weight: bold;
}

.delete-btn {
  position: absolute;
  top: 90px;
  right: 10px;
  background-color: #dc3545;
  color: #fff;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
  border-radius: 5px;
}
.hot-sale-label {
  position: absolute;
  top: 65px;
  left: 80px;
  background-color: #1f9b55;
  color: white;
  font-size: 10px;
  font-weight: bold;
  padding: 2px 5px;
  transform: rotate(45deg);
  z-index: 1;
  border-radius: 5px;
}
</style>
