<template>
  <section class="hero">
    <div class="container">
      <h2>{{ $t('app.wellcome') }}</h2>
      <p>{{ $t('app.discoveries') }}</p>
      <a href="#" class="btn">{{ $t('app.view') }}</a>
    </div>
  </section>
  <section class="product-list">
    <div style="width: 100%;text-align: center">
      <el-form :inline="true" label-width="68px">
        <el-form-item :label="$t('app.goodsName')">
          <el-input v-model="query.name" :placeholder="$t('app.pleaseInp')" clearable/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" size="mini" @click="handleQuery">{{ $t('app.select') }}</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="container">

      <div v-for="item in list" class="product" @click="router.push({ path: '/details/'+item.id+'' })">
        <div class="hot-sale-label" v-if="item.type != ''">{{ $t('app.isEcoFriendly') }}</div>
        <img :src="item.img" alt="商品1">
        <h3 class="ellipsis-text" :title="item.name">{{ item.name }}</h3>
        <p>￥{{ item.price }}</p>
      </div>
    </div>
    <Pagination :total="total" @pageChange="page"></Pagination>
  </section>
</template>
<script lang="ts" setup>
import {getAll} from "@/api/index";
import {onMounted, reactive, ref} from 'vue';
import {useRouter, useRoute} from 'vue-router'
import Pagination from '@/components/pagination.vue';
import { useI18n } from 'vue-i18n';
import {Search} from "@element-plus/icons-vue";
const { t } = useI18n();
const list = ref([])
const query = reactive({
  page: 1,
  pageSize: 10,
  name: '',
});
const total = ref(0);
const page = (e) => {
  query.page = e.currentPage;
  query.pageSize = e.pageSize;
  getlist()
}
const handleQuery = () => {
  getlist()
}
let router = useRouter();
let route = useRoute();
const title = ref('');
const fromData = reactive({
  id: null,

});

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

const getlist = () => {
  getAll(query).then(res => {
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
  getlist()
})
</script>
<style scoped>

.hero {
  background-color: #007bff;
  color: #fff;
  padding: 100px 0;
  text-align: center;
}

.hero h2 {
  margin-bottom: 20px;
}

.hero p {
  font-size: 18px;
  margin-bottom: 30px;
}

.btn {
  display: inline-block;
  background-color: #fff;
  color: #007bff;
  border: 2px solid #007bff;
  padding: 10px 20px;
  text-decoration: none;
  font-weight: bold;
  border-radius: 5px;
}

.btn:hover {
  background-color: #007bff;
  color: #fff;
}

.product-list {
  padding: 20px 0;
  background-color: #fff;
}

.product-list .container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: start;
}
.product {
  position: relative;
  width: 17%;
  margin-left: 13px;
  margin-bottom: 20px;
  padding: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  cursor: pointer;
}

.product img {
  width: 100%;
  height: 180px;
  object-fit: cover;
}

.product h3 {
  margin-top: 10px;
  font-size: 16px;
}

.product p {
  color: #007bff;
  font-weight: bold;
  font-size: 18px;
  margin-top: 5px;
}

.ellipsis-text {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.hot-sale-label {
  position: absolute;
  top: 16px;
  right: 16px;
  background-color: #267912;
  color: white;
  font-size: 10px;
  font-weight: bold;
  padding: 5px 5px;
  border-radius: 5px;
  transform: translate(50%, -50%) rotate(45deg);
  z-index: 1;
}

</style>
