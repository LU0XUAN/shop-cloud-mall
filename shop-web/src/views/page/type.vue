<template>
  <div class="body">
    <section class="product-list">
      <div class="container">
        <div class="search-form">
          <input type="text" :placeholder="$t('app.keyword')" v-model="query.name">
          <button type="submit" @click="getlist">{{ $t('app.select') }}</button>
        </div>
      </div>
    </section>

    <section v-for="(category, index) in products" :key="index" class="product-list">
      <div class="types">
        <h1>{{ category.name }}{{ $t('app.types') }}</h1>
      </div>
      <div class="container">
        <div class="product" v-for="product in category.products" :key="product.id" @click="router.push({ path: '/details/'+product.id+'' })">
          <div class="hot-sale-label" v-if="product.type != ''">{{ $t('app.isEcoFriendly') }}</div>
          <img :src="product.img" :alt="product.name">
          <h3 class="ellipsis-text">{{ product.name }}</h3>
          <p>￥{{ product.price }}</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
import {getType} from "@/api/type";
import {onMounted, reactive, ref} from 'vue';
import { useRouter, useRoute } from 'vue-router'
let router = useRouter();
let route = useRoute();
const title = ref('');
const products = ref([]);
const fromData = reactive({
  id: null,

});
const query = reactive({
  name: '',
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
  getType(query).then(r => {
    console.log(r)
    const result = r.data
    const categories = []
    Object.keys(result).forEach(key => {
      const categoryProducts = result[key]
      categoryProducts.forEach(product => {
        if (!product.img || product.img === '商品1' || product.img.startsWith('http://localhost:9300')) {
          product.img = getImageByName(product.name)
        }
      })
      categories.push({
        name: key,
        products: categoryProducts
      })
    })
    products.value = categories
  })
}
onMounted(() => {
  getlist()
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
  background-color: #ffffff;
  flex-direction: column;
  min-height: 100vh;
  display: flex;
}
.search-form {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.search-form input {
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px 0 0 5px;
  width: 70%; /* 调整输入框宽度 */
}

.search-form button {
  padding: 10px 20px;
  font-size: 16px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 0 5px 5px 0;
  cursor: pointer;
}

.search-form button:hover {
  background-color: #0056b3;
}

.container {
  width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}
.search-form {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.hero h2 {
  margin-bottom: 20px;
}

.hero p {
  font-size: 18px;
  margin-bottom: 30px;
}

.product-list {
  margin: 0 auto;
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
.types {
  max-width: 1200px;
  margin: 0 auto;
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
