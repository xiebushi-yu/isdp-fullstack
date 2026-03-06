<template>
  <div class="about">
    <!-- 原有的卡片 -->
    <el-card style="max-width: 480px; margin-bottom: 20px">
      <template #header>
        <div class="card-header">
          <span>{{ input }}</span>
        </div>
      </template>
      <el-input v-model="input" style="width: 240px" placeholder="Please input" />
      <template #footer>
        <el-button type="primary" round @click="click">click me</el-button>
      </template>
    </el-card>

    <!-- 新增的产品查询卡片 -->
    <el-card style="max-width: 480px">
      <template #header>
        <div class="card-header">
          <span>产品查询</span>
        </div>
      </template>

      <div class="search-container">
        <el-input
          v-model="productSn"
          style="width: 240px; margin-right: 10px"
          placeholder="请输入产品SN"
          clearable
          @keyup.enter="click_gpbs"
        />
        <el-button type="primary" round @click="click_gpbs" :loading="loading">
          查询产品
        </el-button>
      </div>

      <!-- 显示查询结果 -->
      <div v-if="product" class="result-container">
        <el-divider content-position="left">查询结果</el-divider>
        <pre>{{ product }}</pre>
      </div>

      <!-- 无数据提示 -->
      <div v-if="searched && !product && !loading" class="empty-container">
        <el-empty description="未找到对应的产品信息" />
      </div>

      <template #footer>
        <div class="card-footer">
          <span>输入产品SN查询产品信息</span>
        </div>
      </template>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { hello } from '@/api/about'
import { getProductBySn } from '@/api/product'
import type { Product } from '@/types'
import { useCounterStore } from '@/stores/counter'

// 可以在组件中的任意位置访问 `store` 变量 ✨
const store = useCounterStore()
store.count = 3
const double = store.doubleCount
console.log(double)
store.increment()

// 原有的响应式变量
const input = ref('about page')

// 新增的产品查询相关变量
const productSn = ref('')
const product = ref<Product | null>(null)
const loading = ref(false)
const searched = ref(false)

// 原有的按钮点击事件
const click = () => {
  hello().then((response) => {
    console.log(response)
    input.value = response.data
  })
}

// 新增的产品查询按钮点击事件
const click_gpbs = async () => {
  if (!productSn.value.trim()) {
    ElMessage.warning('请输入产品SN')
    return
  }

  loading.value = true
  searched.value = true

  try {
    const response = await getProductBySn(productSn.value.trim())

    // 根据你的ApiResult结构处理响应
    // 由于你的响应拦截器已经处理了code !== 200的情况
    // 所以这里直接拿到的是成功的数据
    console.log(response)
    if (response && response.data) {
      product.value = response.data
      ElMessage.success('查询成功')
    } else {
      product.value = null
      ElMessage.warning('未找到产品信息')
    }

    console.log('查询结果:', response.data)
  } finally {
    loading.value = false
  }
}
</script>
<style>
@media (min-width: 1024px) {
  /* .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  } */
}
</style>
