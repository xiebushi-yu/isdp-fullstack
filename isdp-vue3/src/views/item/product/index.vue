<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true" style="margin-bottom: 16px">
      <el-form-item label="编码" prop="productSn">
        <el-input
          v-model="queryParams.productSn"
          placeholder="请输入编码"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="名称" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入名称"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类别" prop="productCategoryId">
        <el-select
          v-model="queryParams.productCategoryId"
          style="width: 200px"
          placeholder="请选择类别"
        >
          <el-option
            v-for="item in categoryOptions"
            :key="item.categoryId"
            :label="item.categoryName"
            :value="item.categoryId"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10">
      <el-col :span="1.5">
        <el-button type="primary" plain @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="!single" @click="handleUpdate"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="!multiple"
          @click="handleBatchDelete"
          >批量删除</el-button
        >
      </el-col>
      <el-col :span="1.5" v-if="processedProductList.length > 0">
        <el-button type="warning" plain @click="handleExport">导出</el-button>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 10px">
      <el-table
        :data="processedProductList"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="loading"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="productId" label="ID" align="center" width="80"></el-table-column>
        <el-table-column prop="productSn" label="编码" align="center" width="120"></el-table-column>
        <el-table-column prop="productName" label="名称" align="center"></el-table-column>
        <el-table-column
          prop="productCategoryId"
          label="类别ID"
          align="center"
          width="100"
        ></el-table-column>
        <el-table-column
          prop="categoryName"
          label="类别"
          align="center"
          width="100"
        ></el-table-column>
        <el-table-column prop="price" label="价格" align="center" width="100">
          <template #default="scope"> ¥{{ scope.row.price }} </template>
        </el-table-column>
        <!-- 操作列 -->
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
          width="200"
        >
          <template #default="scope">
            <el-button link type="primary" icon="View" size="small" @click="handleView(scope.row)"
              >查看</el-button
            >
            <el-button link type="primary" icon="Edit" size="small" @click="handleUpdate(scope.row)"
              >修改</el-button
            >
            <el-button
              link
              type="primary"
              icon="Delete"
              size="small"
              @click="handleDelete(scope.row)"
              >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 数据展示区：分页加载 -->
      <el-pagination
        style="margin-top: 20px"
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :page-sizes="[5, 10, 20, 30]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      >
      </el-pagination>
    </el-row>

    <!-- 新增/修改对话框 -->
    <el-dialog v-model="dialogOpen" :title="title" width="600px" :before-close="handleCloseDiaglog">
      <product-form :product-id="selectedId" />
    </el-dialog>

    <!-- 查看抽屉 -->
    <el-drawer v-model="drawerOpen" title="查看商品数据" direction="rtl" size="50%">
      <el-descriptions :column="4" :model="viewProduct" border>
        <el-descriptions-item label="商品ID">{{ viewProduct.productId }}</el-descriptions-item>
        <el-descriptions-item label="编码">{{ viewProduct.productSn }}</el-descriptions-item>
        <el-descriptions-item label="名称">{{ viewProduct.productName }}</el-descriptions-item>
        <el-descriptions-item label="价格">¥{{ viewProduct.price }}</el-descriptions-item>
        <el-descriptions-item label="类别ID">{{
          viewProduct.productCategoryId
        }}</el-descriptions-item>
        <el-descriptions-item label="类别">{{ viewProduct.categoryName }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="4">{{
          viewProduct.productDescription || '无描述'
        }}</el-descriptions-item>
        <el-descriptions-item label="主图URL" :span="4">{{
          viewProduct.imageUrl || '无'
        }}</el-descriptions-item>
        <el-descriptions-item label="详情图URL" :span="4">{{
          viewProduct.detailUrl || '无'
        }}</el-descriptions-item>
      </el-descriptions>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import ProductForm from '@/components/ProductForm.vue'
import {
  listProduct,
  getProductById,
  deleteProduct,
  listProductByPage,
  deleteProductBatch,
} from '@/api/product'
import { listCategory } from '@/api/category'
import type { FormInstance } from 'element-plus'
import type { Product } from '@/types'

interface UnifiedProduct {
  productId: number
  productSn: string
  productName: string
  productCategoryId: number
  categoryName: string
  price: number
  productDescription?: string
  imageUrl?: string
  detailUrl?: string
  [key: string]: any
}

const drawerOpen = ref(false)
const loading = ref(false)
const rawProductList = ref<any[]>([])
const parentCategoryOptions = ref<any[]>([])
const categoryOptions = ref<any[]>([])

// 查询参数 - 修改为包含分页参数
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  productSn: '',
  productName: '',
  productCategoryId: undefined as number | undefined,
})
const total = ref<number>(0) // table数据总数
const queryRef = ref()

// 查看抽屉相关
const viewProduct = ref<UnifiedProduct>({} as UnifiedProduct)

onMounted(() => {
  getProductList()
  getCategoryList()
})

// 获取商品列表并进行格式统一处理
// 获取商品列表并进行格式统一处理 - 修改为分页查询
function getProductList() {
  loading.value = true
  listProductByPage(queryParams.value)
    .then((res) => {
      console.log('分页数据:', res.data)
      rawProductList.value = res.data.list || res.data.records || res.data // 适配不同后端返回格式
      total.value = res.data.total
      loading.value = false
    })
    .catch((err) => {
      console.error('获取商品列表失败:', err)
      loading.value = false
    })
}

function getCategoryList() {
  listCategory()
    .then((res) => {
      parentCategoryOptions.value = res.data
      categoryOptions.value = res.data
    })
    .catch((err) => {
      console.error('获取类别列表失败:', err)
    })
}

// 处理数据格式，统一两种数据源的字段
const processedProductList = computed<UnifiedProduct[]>(() => {
  return rawProductList.value
    .map((item) => {
      // 处理编码字段：兼容productCode(Mock)和productSn(后端)
      const productSn = item.productSn || item.productCode || ''

      // 处理类别ID：兼容categoryId(Mock)和productCategoryId(后端)
      const productCategoryId = item.productCategoryId || item.categoryId || 0

      // 处理类别名称：兼容直接字段和嵌套对象
      let categoryName = item.categoryName || ''
      if (!categoryName && item.category && item.category.categoryName) {
        categoryName = item.category.categoryName
      }

      return {
        ...item,
        productSn,
        productCategoryId,
        categoryName,
      }
    })
    .sort((a, b) => a.productId - b.productId)
})

/** ------------------数据展示区：数据选择-------------------- */

const ids = ref<number[]>([]) // 表单勾选的id
const single = ref<boolean>(false) // 勾选1个
const multiple = ref<boolean>(false) // 勾选多个

// 数据展示区--> 勾选数据
function handleSelectionChange(selection: Product[]) {
  ids.value = selection
    .map((item: Product) => item.productId)
    .filter((id) => id !== undefined) as number[]
  single.value = selection.length == 1
  multiple.value = selection.length >= 1
}

/** 新增按钮 */
const dialogOpen = ref(false)
const title = ref('')
const selectedId = ref<number | undefined>()
function handleAdd() {
  title.value = '新增商品'
  selectedId.value = undefined
  dialogOpen.value = true
}

/** 修改按钮 */
function handleUpdate(row: Product) {
  ElMessage.success('修改操作,勾选的数据id为:' + ids.value.join(','))
  title.value = '修改商品'
  selectedId.value = row.productId || ids.value[0]
  dialogOpen.value = true
}

/** 提交表单后关闭对话框——刷新列表 */
function handleCloseDiaglog() {
  dialogOpen.value = false
  getProductList()
}

/** ------------------数据删除操作-------------------- */

/** 删除单条数据 */
function handleDelete(row: Product) {
  ElMessageBox.confirm('是否删除编号为' + row.productId + '的数据?', '警告')
    .then(() => {
      if (row.productId) {
        return deleteProduct(row.productId)
      }
    })
    .then(() => {
      getProductList()
      ElMessage.success('删除id为' + row.productId + '的数据成功!')
    })
}

/** 批量删除按钮 */
function handleBatchDelete() {
  ElMessageBox.confirm('是否删除编号为' + ids.value + '的数据?', '警告')
    .then(() => {
      return deleteProductBatch(ids.value)
    })
    .then(() => {
      getProductList()
      ElMessage.success('批量删除' + ids.value.length + '条数据成功!')
    })
}

function handleExport() {
  ElMessage.info('导出数据')
}

// 查询逻辑 - 分页查询
function handleQuery() {
  queryParams.value.pageNum = 1
  loading.value = true

  listProductByPage(queryParams.value)
    .then((res) => {
      const rawData = res.data.list || res.data.records || res.data

      let processedData = rawData.map((item: any) => {
        return {
          ...item,
          productSn: item.productSn || item.productCode || '',
          productCategoryId: item.productCategoryId || item.categoryId || 0,
          categoryName: item.categoryName || item.category?.categoryName || '',
        }
      })

      if (queryParams.value.productSn) {
        processedData = processedData.filter((item: UnifiedProduct) =>
          item.productSn.includes(queryParams.value.productSn),
        )
      }
      if (queryParams.value.productName) {
        processedData = processedData.filter((item: UnifiedProduct) =>
          item.productName.includes(queryParams.value.productName),
        )
      }
      if (queryParams.value.productCategoryId !== undefined) {
        processedData = processedData.filter(
          (item: UnifiedProduct) => item.productCategoryId === queryParams.value.productCategoryId,
        )
      }

      rawProductList.value = processedData
      total.value = res.data.total
      loading.value = false
    })
    .catch((err) => {
      console.error('查询商品失败:', err)
      loading.value = false
    })
}

function resetQuery() {
  if (queryRef.value) {
    queryRef.value.resetFields()
  }
  // 重置分页参数
  queryParams.value.pageNum = 1
  queryParams.value.pageSize = 10
  getProductList()
}

/** ------------------数据展示区：分页加载-------------------- */
// 分页--> 修改每页数据数（5｜10｜20｜30）
function handleSizeChange(val: number) {
  queryParams.value.pageSize = val
  queryParams.value.pageNum = 1 // 切换页大小时回到第一页
  getProductList()
}

// 分页--> 修改当前页
function handleCurrentChange(val: number) {
  queryParams.value.pageNum = val
  getProductList()
}

// 查看商品详情（统一格式）
async function handleView(row: UnifiedProduct) {
  try {
    const res = await getProductById(row.productId)
    const rawData = res.data

    // 统一详情数据格式
    viewProduct.value = {
      ...rawData,
      productSn: rawData.productSn || rawData.productCode || '',
      productCategoryId: rawData.productCategoryId || rawData.categoryId || 0,
      categoryName: rawData.categoryName || rawData.category?.categoryName || '',
    }
    drawerOpen.value = true
  } catch (err) {
    console.error('查看商品失败:', err)
    ElMessage.error('查看商品失败，请重试')
  }
}
</script>
