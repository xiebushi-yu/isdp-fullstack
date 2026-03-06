<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5" v-if="categoryList.length > 0">
        <el-button type="warning" plain icon="Download" @click="handleExport">导出</el-button>
      </el-col>
    </el-row>
    <el-divider />
    <el-row :gutter="20">
      <el-table :data="categoryList" style="width: 100%">
        <el-table-column
          prop="categoryId"
          label="类别ID"
          align="center"
          width="200"
        ></el-table-column>
        <el-table-column
          prop="parentId"
          label="上级ID"
          align="center"
          width="200"
        ></el-table-column>
        <el-table-column prop="categoryName" label="类别名称" align="center"></el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button link type="primary" icon="View" size="small" @click="handleUpdate(scope.row)"
              >修改</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-row>

    <el-dialog v-model="dialogOpen" :title="title" width="500">
      <!-- 也可以写成 <CateogryForm /> -->
      <category-form :category-id="categoryId" />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ElMessage, ElMessageBox } from 'element-plus'
import { ref } from 'vue'
import { onMounted } from 'vue'
import CategoryForm from '@/components/CategoryForm.vue'
// 导入api接口
import { listCategory } from '@/api/category'
onMounted(() => {
  getCategoryList()
})

interface Category {
  categoryId: number
  parentId: number
  categoryName: string
}
const categoryList = ref<Category[]>([])

const dialogOpen = ref(false) // 对话框 v-model
const title = ref('') // 对话框 v-bind

const categoryId = ref(0)

/** 获取类别列表 */
function getCategoryList() {
  listCategory().then((res) => {
    categoryList.value = res.data
  })
}

/** 新增按钮 */
function handleAdd() {
  dialogOpen.value = true
  title.value = '新增类别'
}

/** 修改按钮 */
function handleUpdate(row: Category) {
  categoryId.value = row.categoryId
  dialogOpen.value = true
  title.value = '修改类别'
}

/** 删除按钮 */
function handleDelete() {
  ElMessageBox.confirm('是否删除数据?', '警告', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    type: 'warning',
  })
    .then(() => {
      ElMessage({ type: 'success', message: '删除成功' })
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消删除',
      })
    })
}

/** 导出按钮 */
function handleExport() {
  ElMessage({ type: 'info', message: '导出数据' })
}
</script>

<style>
@media (min-width: 1024px) {
}
</style>
