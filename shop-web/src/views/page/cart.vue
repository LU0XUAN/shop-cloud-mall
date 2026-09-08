<template>
  <div class="body">
    <div class="container">
      <section class="cart">
        <div style="display: flex;justify-content: space-between;align-items: center">
          <h2>{{ $t('app.cart') }}</h2>
          <span style="color: #ff2121;font-size: 18px">{{ $t('app.availablePoints') }} : {{ integral }}</span>
        </div>

        <table>
          <thead>
          <tr>
            <th>{{ $t('app.choose') }}</th>
            <th>{{ $t('app.goods') }}</th>
            <th>{{ $t('app.goodsName') }}</th>
            <th>{{ $t('app.unitPrice') }}</th>
            <th>{{ $t('app.quantity') }}</th>
            <th>{{ $t('app.attribute') }}</th>
            <th>{{ $t('app.subtotal') }}</th>
            <th>{{ $t('app.operation') }}</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="data in myCarts" :key="data.id">
            <td>
              <input type="checkbox" class="styled-checkbox" :id="'checkbox-' + data.id"
                     v-model="selectedItems[data.id]" @change="handleCheckboxChange">
              <label :for="'checkbox-' + data.id" class="styled-label"></label>
            </td>
            <td>
              <div class="product">
                <img :src="data.img" alt="商品图">
                <div class="hot-sale-label" v-if="data.type != ''">{{ $t('app.isEcoFriendly') }}</div>
              </div>
            </td>
            <td><h4>{{ data.name }}</h4></td>
            <td><p>￥{{ data.price }}</p></td>
            <td>
              <el-input-number v-model="data.num" :min="1" :max="getAttrNum(data.attr)"
                               @change="(value) => handleChange(value, data)"/>
            </td>
            <td>{{ getAttrName(data.attr) }}</td>
            <td><p>￥{{ data.sum }}</p></td>
            <td><a @click="rome(data.id)" class="delete-btn">{{ $t('app.del') }}</a></td>
          </tr>
          </tbody>


        </table>
        <div style="width: 1200px; text-align: right;font-weight: bold">
        <div style="display: flex;justify-content: right;align-items: center">
          <span class="label">{{ $t('app.sum') }}：</span>
          <span class="amount"><p style="color: #ff9100; font-size: 23px;margin: 0">￥{{ sums }}</p></span>
        </div>
        <div v-if="integrals"  style="display: flex;justify-content: right;align-items: center">
          <span>
            <label>
              <input type="checkbox" v-model="useIntegrals" @change="handleUseIntegralsChange" v-if="integral!=0"> {{ $t('app.pointsRedeemed') }}：
            </label>
          </span>
          <span class="amount"><p style="color: #ff9100; font-size: 23px;margin: 0">￥{{ integral1 }}</p></span>
        </div>
        <div class="total-row"   style="display: flex;justify-content: right;align-items: center">
          <span class="label">{{ $t('app.amountPaid') }}：</span>
          <span class="amount"><p style="color: #ff9100; font-size: 23px;margin: 0"><span v-if="getSelectedItems().length > 1">({{sum}})</span>￥{{ calculateSum }} </p></span>
        </div>
        </div>

        <button class="continue-shopping" @click="router.push('/')">{{ $t('app.goShop') }}</button>
        <button class="checkout" @click="downOrder">{{ $t('app.orderDown') }}</button>
      </section>
    </div>

    <el-dialog
        v-model="dialogVisible"
        :title="$t('app.selAddr')"
        width="500"
        :before-close="handleClose"
    >
      <el-select
          v-model="address"
          :placeholder="$t('app.selAddr')"
          size="mini"
      >
        <el-option
            v-for="item in myAddr"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">{{ $t('app.cancel') }}</el-button>
          <el-button type="primary" @click="orderDown">
            {{ $t('app.orderDown') }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import {useI18n} from 'vue-i18n';
import {del, editNum, getMyCart} from "@/api/cart";
import {add} from "@/api/order";
import {onMounted, ref} from 'vue';
import {useRoute, useRouter} from 'vue-router'
import {ElMessage, ElMessageBox} from "element-plus";
import {getAll} from "@/api/addr";
import {getUserById} from "@/api/user";

const {t} = useI18n();
let router = useRouter();
let route = useRoute();
const selectedItems = ref([]);
const myCarts = ref([]);
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
//总共积分
const integral = ref(0);
//使用积分抵扣
const integral1 = ref(0);
//实付
const calculateSum = ref(0);
//计算公式
const sum = ref('');
//总价
const sums = ref('0');
//是否勾选折扣
const useIntegrals = ref(false);
//显示折扣
const integrals = ref(false);
const myAddr = ref([]);
const address = ref('')
const dialogVisible = ref(false)
const parseAttr = (attr) => {
  if (!attr) {
    return {}
  }
  if (typeof attr === 'object') {
    return attr
  }
  try {
    return JSON.parse(attr)
  } catch (e) {
    return {name: attr, num: 999}
  }
}
const getAttrName = (attr) => {
  const data = parseAttr(attr)
  return data.name || attr || ''
}
const getAttrNum = (attr) => {
  const data = parseAttr(attr)
  return Number(data.num || 999)
}
const handleClose = (done: () => void) => {
  ElMessageBox.confirm('Are you sure to close this dialog?')
      .then(() => {
        done()
      })
      .catch(() => {
        // catch error
      })
}
const fromData = ref([]);
const handleUseIntegralsChange = () => {
  // 处理复选框状态改变的逻辑
  if (useIntegrals.value) {
    console.log('用户选择使用积分抵扣');
    integrals.value = true
    // 实付
    calculateSum.value = Number((sums.value - integral1.value).toFixed(2))
  } else {
    useIntegrals.value = false
    // 如果复选框未被选中，执行相应操作
    console.log('用户取消使用积分抵扣');
    calculateSum.value = Number(sums.value)
  }
  calculateTotals(sums.value,integral1.value)
}
const orderDown = (data) => {
  if (address.value === '') {
    ElMessage({
      type: 'warning',
      message: t('app.selAddr'),
    })
    return
  }
  const datas = []
  const selectedIds = getSelectedItems();
  const descs = sum.value.split(' + ')
  for (let i = 0; i < selectedIds.length; i++) {
    //根据id查询myCart种的小计
    const cart = myCarts.value.find(item => item.id == selectedIds[i]);
    datas.push({
      cid: selectedIds[i],
      aid: address.value,
      discount: (cart.sum - descs[i]).toFixed(2),
      finalPrice: descs[i],
    })
  }
  add(datas,useIntegrals.value,integral1.value).then(r => {
    ElMessage({
      type: 'success',
      message: t('app.success'),
    })
    getCart()
    getUser()
    integrals.value = false
    dialogVisible.value = false
    //使用积分抵扣
     integral1.value = 0
    //实付
     calculateSum.value = 0
    //计算公式
     sum.value = ''
    //总价
     sums.value = '0'
    selectedItems.value = []
  })
}
const getSelectedItems = () => {
  // 遍历 selectedItems 对象
  const selectedIds = [];
  for (const id in selectedItems.value) {
    if (selectedItems.value[id]) { // 如果值为 true
      selectedIds.push(id); // 将 ID 添加到数组中
    }
  }
  return selectedIds; // 返回选中的商品 ID 数组
}
const handleCheckboxChange = () => {
  //计算总价
  calculateTotal()
  //计算可用积分
  if (sums.value < integral.value) {
    integral1.value = Number(sums.value)
  }else {
    integral1.value = integral.value
  }
  // console.log(selectedItems.value)
  // console.log(`Checkbox with ID ${id} changed. Selected: ${selectedItems.value[id]}`);
  if (getSelectedItems().length == 0) {
    useIntegrals.value = false
    integrals.value = false
  } else {
    integrals.value = true
  }
  //实付
  if (useIntegrals.value) {
    calculateSum.value = Number((Number(sums.value) - Number(integral1.value)).toFixed(2))
  }else {
    calculateSum.value = Number(sums.value)
  }
  calculateTotals(sums.value,integral1.value)
}
const downOrder = () => {
  if (getSelectedItems().length === 0) {
    ElMessage({
      type: 'warning',
      message: t('app.selGood'),
    })
    return
  }
  dialogVisible.value = true
}
const rome = (id) => {
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
          ElMessage({
            type: 'success',
            message: t('app.success'),
          })
          getCart()
        })
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: t('app.cancel'),
        })
      })


}
const handleChange = (number, data) => {
  editNum({
    id: data.id,
    num: number
  }).then(r => {
    ElMessage({
      type: 'success',
      message: t('app.success'),
    })
    getCart()
    handleCheckboxChange()
  })
}
//data 总价格  item优惠价格
const calculateTotals = (totalPrice, discount) => {
  if (!integrals.value) {
    discount = 0
  }
  console.log(useIntegrals.value)
  if (!useIntegrals.value) {
    console.log('狗哟')
    discount = 0
  }
  console.log('抵扣金额：',discount)
  const prices = []
  //myCarts是所有商品
  myCarts.value.forEach(item => {
    if (JSON.stringify(selectedItems.value) === '{}') {
      return
    }
    if (getSelectedItems().includes(item.id.toString())) {
      const price1 = (item.price * item.num).toFixed(2);
      const discount1 = (discount * (price1 / totalPrice)).toFixed(2);
      const price = (price1 - discount1).toFixed(2)
      //勾选的商品
      console.log(price)
      prices.push(price)
    }
  });
  let result: string
  if (prices.length > 1) {
    result = prices.join(' + ');
  } else {
    result = '' + (totalPrice - discount).toFixed(2);
  }
  sum.value = result
}
//总价
const calculateTotal = () => {
  let total: number = 0;
  myCarts.value.forEach(item => {
    // console.log(selectedItems.value)
    if (JSON.stringify(selectedItems.value) === '{}') {
      return
    }
    if (getSelectedItems().includes(item.id.toString())) {
      total += item.price * item.num;
    }
  });
  sums.value = total.toFixed(2);
}
const getCart = () => {
  getMyCart().then(res => {
    myCarts.value = res.data.map((item) => {
      if (!item.img || item.img === '商品1') {
        item.img = getImageByName(item.name)
      }
      return item
    });
  })
}
const getAddrAll = () => {
  getAll().then(r => {
    for (let i = 0; i < r.data.length; i++) {
      myAddr.value.push({
        value: r.data[i].id,
        label: r.data[i].nickname + "/" + r.data[i].phone + "/" + r.data[i].province + "/" + r.data[i].city + "/" + r.data[i].region + "/" + r.data[i].address
      })
    }
  })
}
const getUser = () => {
  getUserById().then(r => {
    integral.value = r.data.integral
  })
}

onMounted(() => {
  getCart()
  getAddrAll()
  getUser();
})
</script>

<style scoped>
.product {
  position: relative;
  width: 100%;
}

.product img {
  width: 100%;
  height: auto;
  display: block;
}

.hot-sale-label {
  position: absolute;
  top: -10px;
  right: 50px;
  background-color: #1f9b55;
  color: white;
  font-size: 10px;
  font-weight: bold;
  padding: 2px 5px;
  transform: rotate(45deg);
  z-index: 1;
  border-radius: 5px;
}

.body {
  font-family: Arial, sans-serif;
  line-height: 1.6;
  background-color: #f0f0f0;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.container {
  width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.hero h2 {
  margin-bottom: 20px;
}

.hero p {
  font-size: 18px;
  margin-bottom: 30px;
}


.product-list .container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}


.product img {
  width: 100%;
  height: auto;
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

.cart {
  margin-top: 20px;
}

.cart table {
  width: 100%;
  border-collapse: collapse;
  background-color: #fff;
  margin-bottom: 20px;
  border-radius: 20px;
}

.cart table th,
.cart table td {
  padding: 10px;
  text-align: center;
}

.cart table th {
  background-color: #499bf1;
  color: #fff;
}

.cart table td img {
  max-width: 80px;
  height: auto;
  vertical-align: middle;
}

.cart table td h4 {
  font-size: 18px;
  margin: 10px 0;
}

.cart table td p {
  color: #007bff;
  font-weight: bold;
}

.cart .total {
  text-align: right;
}

.cart .total p {
  font-size: 18px;
  margin-top: 10px;
}

.continue-shopping {
  background-color: #4CAF50;
  color: white;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  border-radius: 5px;
  font-size: 16px;
}

.continue-shopping:hover {
  background-color: #45a049;
}

.checkout {
  background-color: #008CBA;
  color: white;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  border-radius: 5px;
  font-size: 16px;
  margin-left: 10px;
}

.checkout:hover {
  background-color: #0073e6;
}

.delete-btn {
  background-color: #dc3545;
  color: #fff;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
  text-decoration: none;
  display: inline-block;
}

.delete-btn:hover {
  background-color: #c82333;
}

.styled-checkbox {
  display: none; /* 隐藏原生复选框 */
}

.styled-label {
  position: relative;
  display: inline-block;
  width: 20px;
  height: 20px;
  background-color: #fff;
  border: 1px solid #ccc;
  cursor: pointer;
}

.styled-label::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 12px;
  height: 12px;
  background-color: #fdfdff;
  border: 1px solid #ccc;
  transition: background-color 0.3s ease;
}

.styled-checkbox:checked + .styled-label::before {
  background-color: #4CAF50;
}

tfoot p {
  margin: 5px 0; /* 调整段落的上下外边距 */
  line-height: 1; /* 设置段落的行高为1，使其更加紧凑 */
}

</style>
