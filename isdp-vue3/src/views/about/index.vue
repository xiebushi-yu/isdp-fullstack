<template>
  <div class="about">
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

      <div v-if="product" class="result-container">
        <el-divider content-position="left">查询结果</el-divider>
        <pre>{{ product }}</pre>
      </div>

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

const store = useCounterStore()
store.count = 3
const double = store.doubleCount
console.log(double)
store.increment()

const input = ref('about page')
const productSn = ref('')
const product = ref<Product | null>(null)
const loading = ref(false)
const searched = ref(false)

const click = () => {
  hello().then((response) => {
    console.log(response)
    input.value = response.data
  })
}

const click_gpbs = async () => {
  if (!productSn.value.trim()) {
    ElMessage.warning('请输入产品SN')
    return
  }

  loading.value = true
  searched.value = true

  try {
    const response = await getProductBySn(productSn.value.trim())
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
