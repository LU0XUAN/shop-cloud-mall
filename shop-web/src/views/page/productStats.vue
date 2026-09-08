<template>
  <div style="display: flex; justify-content: center; align-items: center; height: 100vh;width: 100%;">
    <div ref="charts" class="echarts-chart"></div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import * as echarts from 'echarts';
import {productStats} from "@/api/order";
const charts = ref(null);
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
const renderPieChart = () => {
  productStats().then(r => {
    let datas = r.data
    if (localStorage.getItem('localeLang') == 'en') {
      for (let i = 0; i < datas.length; i++) {
        if (datas[i].name == '环保商品') {
          datas[i].name = t('app.ecoFriendlyProduct')
        }
        if (datas[i].name == '普通商品') {
          datas[i].name = t('app.regularProduct')
        }
      }

    }
    console.log()
    const chartInstance = echarts.init(charts.value);

    const option = {
      title: {
        text:   t('app.ecoFriendlyProductCount'),
        subtext: t('app.productCount'),
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'top'
      },
      series: [
        {
          name: t('app.productCount'),
          type: 'pie',
          radius: '60%',
          data: datas,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };
    chartInstance.setOption(option);
  })

};

onMounted(() => {
  renderPieChart();
});
</script>

<style scoped>
.echarts-chart {
  width: 400px;
  height: 800px;
}
</style>
