<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import request from '@/utils/request.js'
import * as echarts from 'echarts'
import {
  User, OfficeBuilding, SetUp, Document,
  Lightning, Money, Star, Warning
} from '@element-plus/icons-vue'

const stats = ref([
  { title: '用户总数', value: 0, icon: User, color: '#409eff' },
  { title: '充电站点', value: 0, icon: OfficeBuilding, color: '#67c23a' },
  { title: '充电桩数', value: 0, icon: SetUp, color: '#e6a23c' },
  { title: '今日订单', value: 0, icon: Document, color: '#f56c6c' },
])

const recentOrders = ref([])
const mapLoading = ref(true)
const chartLoading = ref(true)

let mapChart = null
let pieChart = null
let barChart = null

const initMap = (stationList) => {
  const chartDom = document.getElementById('china-map')
  if (!chartDom) return
  mapChart = echarts.init(chartDom)

  fetch('https://geo.datav.aliyun.com/areas_v3/bound/100000_full.json')
    .then(res => res.json())
    .then(geoJson => {
      echarts.registerMap('china', geoJson)
      mapLoading.value = false

      const scatterData = stationList
        .filter(s => s.longitude && s.latitude)
        .map(s => ({
          name: s.name,
          value: [s.longitude, s.latitude],
          itemStyle: {
            color: s.status === 1 ? '#00c853' : '#ff5252'
          }
        }))

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: (params) => {
            if (params.seriesType === 'effectScatter') {
              return `<div style="font-weight:600">${params.name}</div>`
            }
            return params.name
          }
        },
        geo: {
          map: 'china',
          roam: true,
          zoom: 1.2,
          label: { show: false },
          itemStyle: {
            areaColor: '#e8f5e9',
            borderColor: '#4caf50',
            borderWidth: 1
          },
          emphasis: {
            itemStyle: {
              areaColor: '#c8e6c9'
            }
          }
        },
        series: [
          {
            type: 'effectScatter',
            coordinateSystem: 'geo',
            data: scatterData,
            symbolSize: 12,
            rippleEffect: {
              brushType: 'stroke',
              scale: 3
            },
            label: {
              show: true,
              formatter: '{b}',
              position: 'right',
              fontSize: 10,
              color: '#333'
            }
          }
        ]
      }
      mapChart.setOption(option)
    })
    .catch(() => {
      mapLoading.value = false
      // 降级为散点图
      const option = {
        title: { text: '充电站分布（经纬度散点）', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: {
          formatter: (p) => `${p.data.name}<br/>经度: ${p.data.value[0]}<br/>纬度: ${p.data.value[1]}`
        },
        xAxis: { name: '经度', min: 70, max: 140 },
        yAxis: { name: '纬度', min: 15, max: 55 },
        series: [{
          type: 'scatter',
          data: stationList.filter(s => s.longitude && s.latitude).map(s => ({
            name: s.name,
            value: [s.longitude, s.latitude],
            itemStyle: { color: s.status === 1 ? '#00c853' : '#ff5252' }
          })),
          symbolSize: 14,
          label: { show: true, formatter: '{b}', position: 'top', fontSize: 9 }
        }]
      }
      mapChart.setOption(option)
    })
}

const initPieChart = (orders) => {
  const chartDom = document.getElementById('pie-chart')
  if (!chartDom) return
  pieChart = echarts.init(chartDom)

  const statusMap = { 0: '充电中', 1: '待结算', 2: '已完成', 3: '已取消', 4: '退款中', 5: '已退款' }
  const statusColors = ['#e6a23c', '#409eff', '#67c23a', '#909399', '#f56c6c', '#ff5252']
  const countMap = {}
  orders.forEach(o => {
    const label = statusMap[o.status] || '其他'
    countMap[label] = (countMap[label] || 0) + 1
  })
  const data = Object.entries(countMap).map(([name, value], i) => ({
    name, value, itemStyle: { color: statusColors[i % statusColors.length] }
  }))

  pieChart.setOption({
    title: { text: '订单状态分布', left: 'center', textStyle: { fontSize: 14 } },
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { orient: 'horizontal', bottom: 2, left: 'center', itemWidth: 10, itemHeight: 10, textStyle: { fontSize: 11 } },
    series: [{
      type: 'pie',
      radius: ['38%', '62%'],
      center: ['50%', '46%'],
      minAngle: 10,
      avoidLabelOverlap: true,
      label: { show: true, position: 'outside', formatter: '{b}\n{c}', fontSize: 11 },
      labelLine: { show: true, length: 14, length2: 12 },
      data
    }]
  })
}

const initBarChart = (chargers) => {
  const chartDom = document.getElementById('bar-chart')
  if (!chartDom) return
  barChart = echarts.init(chartDom)

  const statusMap = { 0: '故障', 1: '正常', 2: '维护中' }
  const statusColorMap = { '故障': '#f56c6c', '正常': '#67c23a', '维护中': '#e6a23c' }
  const countMap = { '故障': 0, '正常': 0, '维护中': 0 }
  chargers.forEach(c => {
    const label = statusMap[c.status] || '其他'
    countMap[label] = (countMap[label] || 0) + 1
  })

  barChart.setOption({
    title: { text: '充电桩状态统计', left: 'center', textStyle: { fontSize: 14 } },
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '4%', bottom: '3%', top: '18%', containLabel: true },
    xAxis: { type: 'category', data: Object.keys(countMap), axisTick: { alignWithLabel: true } },
    yAxis: { type: 'value' },
    series: [{
      type: 'bar',
      barWidth: '50%',
      data: Object.entries(countMap).map(([name, value]) => ({
        name, value, itemStyle: { color: statusColorMap[name] || '#909399' }
      })),
      label: { show: true, position: 'top' }
    }]
  })
}

const handleResize = () => {
  mapChart?.resize()
  pieChart?.resize()
  barChart?.resize()
}

onMounted(async () => {
  try {
    const [userRes, stationRes, chargerRes, orderRes] = await Promise.all([
      request.get('/user/list', { params: { page: 1, limit: 1 } }),
      request.get('/station/list', { params: { page: 1, limit: 100 } }),
      request.get('/charger/list', { params: { page: 1, limit: 100 } }),
      request.get('/chargingOrder/list', { params: { page: 1, limit: 100 } }),
    ])

    stats.value[0].value = userRes.data?.total || 0
    stats.value[1].value = stationRes.data?.total || 0
    stats.value[2].value = chargerRes.data?.total || 0
    stats.value[3].value = orderRes.data?.total || 0

    const stations = stationRes.data?.records || []
    const chargers = chargerRes.data?.records || []
    const orders = orderRes.data?.records || []

    recentOrders.value = orders.slice(0, 5)

    initMap(stations)
    initPieChart(orders)
    initBarChart(chargers)
    chartLoading.value = false

    window.addEventListener('resize', handleResize)
  } catch (e) {
    console.error(e)
    mapLoading.value = false
    chartLoading.value = false
  }
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  mapChart?.dispose()
  pieChart?.dispose()
  barChart?.dispose()
})
</script>

<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="16">
      <el-col :xs="24" :sm="12" :md="6" v-for="item in stats" :key="item.title">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon-box" :style="{ background: item.color + '15' }">
              <el-icon :size="28" :color="item.color">
                <component :is="item.icon" />
              </el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-title">{{ item.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 地图 + 饼图 -->
    <el-row :gutter="16" class="mt-16">
      <el-col :xs="24" :lg="16">
        <el-card class="chart-card" v-loading="mapLoading">
          <template #header>
            <div class="card-header">
              <el-icon color="#00c853"><OfficeBuilding /></el-icon>
              <span>充电站分布地图</span>
              <el-tag size="small" type="success" style="margin-left:auto">绿色=运营中</el-tag>
              <el-tag size="small" type="danger" style="margin-left:8px">红色=维护中</el-tag>
            </div>
          </template>
          <div id="china-map" style="width:100%;height:420px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="8">
        <el-card class="chart-card" v-loading="chartLoading">
          <div id="pie-chart" style="width:100%;height:420px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 柱状图 + 最近订单 -->
    <el-row :gutter="16" class="mt-16">
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card" v-loading="chartLoading">
          <div id="bar-chart" style="width:100%;height:320px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <el-icon color="#f56c6c"><Document /></el-icon>
              <span>最近充电订单</span>
            </div>
          </template>
          <el-table :data="recentOrders" size="small" stripe>
            <el-table-column prop="orderNo" label="订单号" width="150" show-overflow-tooltip />
            <el-table-column prop="userId" label="用户ID" width="70" />
            <el-table-column prop="amount" label="金额" width="80">
              <template #default="{ row }">
                <span style="color:#f56c6c;font-weight:500">¥{{ row.amount }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="80">
              <template #default="{ row }">
                <el-tag size="small" :type="['warning','primary','success','info','danger','danger'][row.status] || 'info'">
                  {{ ['充电中','待结算','已完成','已取消','退款中','已退款'][row.status] || '未知' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.dashboard {
  padding: 8px;
}
.stat-card {
  border-radius: 10px;
  margin-bottom: 16px;
}
.stat-content {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 6px 4px;
}
.stat-icon-box {
  width: 52px;
  height: 52px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.stat-value {
  font-size: 26px;
  font-weight: 700;
  color: #1a1a1a;
  line-height: 1.2;
}
.stat-title {
  font-size: 13px;
  color: #888;
  margin-top: 2px;
}
.mt-16 {
  margin-top: 16px;
}
.chart-card {
  border-radius: 10px;
}
.chart-card :deep(.el-card__header) {
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
}
.chart-card :deep(.el-card__body) {
  padding: 12px;
}
.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  font-size: 14px;
  color: #333;
}
</style>
