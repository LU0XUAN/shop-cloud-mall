<template>
  <div class="body" v-if="good!=null">
    <div class="container">
      <section class="product-details">
        <div class="product-gallery">
          <el-carousel height="400px">
            <el-carousel-item v-for="item in good.imgs.split(',')" :key="item">
              <img :src="item" alt="商品图片" style="height: 400px;object-fit: cover">
            </el-carousel-item>
          </el-carousel>
        </div>

        <div class="product-info">
          <h2 class="product-title">{{ good.name }}</h2>
          <p class="product-price">￥{{ good.price }}</p>
          <div class="product-description">
            <p>{{ good.remarks}}</p>
          </div>

          <div class="product-attributes">
            <h3>{{ $t('app.goodAttr') }}</h3>
            <ul>
              <el-radio-group v-model="radio" @change="attr">
                <el-radio-button  v-for="(data,index) in JSON.parse(good.attr)" :key="data.name" :label="data.name">
                  {{ $t('app.attributeName') }}：{{ data.name }}；{{ $t('app.attributeQuantity') }}：{{ data.num }}{{ $t('app.one') }}；
                  {{ $t('app.attributePrice') }}：{{ data.price }}{{ $t('app.currency') }}</el-radio-button>
              </el-radio-group>
<!--              <li v-for="(data,index) in JSON.parse(good.attr)">-->
<!--                <strong>属性{{index+1}}:</strong> {{ data }}-->
<!--              </li>-->
            </ul>
          </div>
          <a @click="addCarts" class="btn">{{ $t('app.addToCart') }}</a>

        </div>

      </section>

    </div>
    <div  style="width: 1200px;margin: 0 auto;">
      <div v-html="good.details" class="htmls"></div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
import {getById} from "@/api/details";
import {addCart} from "@/api/cart";
import {onMounted, reactive, ref} from 'vue';
import { useRouter, useRoute } from 'vue-router'
import {ElMessage} from "element-plus";
let router = useRouter();
let route = useRoute();
const good = ref(null);
const radio = ref(null)
const att = ref('')

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

const attr = () => {
  if (!good.value || !good.value.attr) return
  const attrList = JSON.parse(good.value.attr)
  const selected = attrList.find(item => item.name === radio.value)
  if (selected) {
    good.value.price = selected.price
    att.value = selected.name
  }
}
const getId = (id) => {
  getById(id).then(r=> {
    good.value = r.data
    if (!good.value.imgs || good.value.imgs === '商品1') {
      const matchedImage = getImageByName(good.value.name)
      good.value.imgs = matchedImage
    }
  })
}
const addCarts = () => {
  if (!radio.value) {
    ElMessage({
      type: 'error',
      message: t('app.selectAttributes'),
    })
    return
  }
  addCart({
    pid: route.params.id,
    attr: radio.value,
  }).then(r => {
    if (r.code === 200) {
      ElMessage({
        type: 'success',
        message: t('app.success'),
      })
    } else {
      ElMessage({
        type: 'error',
        message: t('app.err'),
      })
    }
  })
}
onMounted(() => {
  getId(route.params.id)
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
.product-details {
  display: flex;
  margin-top: 20px;
}

.product-gallery {
  flex: 1;
  margin-right: 20px;
}

.product-gallery img {
  width: 100%;
  display: block;
  margin-bottom: 20px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.product-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #fff;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.product-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}

.product-price {
  color: #007bff;
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
}

.product-description {
  margin-bottom: 20px;
}

.product-description p {
  line-height: 1.8;
}

.product-attributes {
  margin-top: auto;
  background-color: #f9f9f9;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.product-attributes h3 {
  font-size: 18px;
  margin-bottom: 10px;
  color: #333;
}

.product-attributes ul {
  list-style-type: none;
  padding: 0;
}

.product-attributes ul li {
  margin-bottom: 5px;
}

.btn {
  display: inline-block;
  background-color: #007bff;
  color: #fff;
  border: none;
  margin-top: 10px;
  padding: 10px 20px;
  font-size: 16px;
  text-decoration: none;
  border-radius: 4px;
  cursor: pointer;
  text-align: center;

}

.btn:hover {
  background-color: #0056b3;
  text-align: center;
}
.el-radio-button, .el-radio-button__inner {
  margin-bottom: 10px !important;
}

</style>
<style>
.htmls img {
  width: 1200px !important;
}
</style>
