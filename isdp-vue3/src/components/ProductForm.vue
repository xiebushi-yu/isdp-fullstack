<template>
  <el-form
    ref="ruleFormRef"
    style="max-width: 600px"
    :model="form"
    :rules="rules"
    label-width="90px"
  >
    <el-form-item label="产品名称" prop="productName">
      <el-input v-model="form.productName" placeholder="请输入商品名称" />
    </el-form-item>
    <el-form-item label="商品编号" prop="productSn">
      <el-input v-model="form.productSn" placeholder="请输入商品编号" />
    </el-form-item>
    <el-form-item label="商品价格" prop="price">
      <el-input-number
        v-model="form.price"
        :step="1"
        :min="0"
        style="width: 100%"
        placeholder="请输入商品价格"
      />
    </el-form-item>
    <el-form-item label="商品分类" prop="productCategoryId">
      <el-select v-model="form.productCategoryId" placeholder="请选择商品分类" style="width: 100%">
        <el-option
          v-for="category in categoryOptions"
          :key="category.categoryId"
          :label="category.categoryName"
          :value="category.categoryId"
        />
      </el-select>
    </el-form-item>

    <!-- 商品描述 -->
    <el-form-item label="商品描述" prop="productDescription">
      <el-input
        v-model="form.productDescription"
        type="textarea"
        :rows="3"
        placeholder="请输入商品描述"
      />
    </el-form-item>

    <!-- 主图URL -->
    <el-form-item label="主图URL" prop="imageUrl">
      <el-input v-model="form.imageUrl" placeholder="请输入图片URL">
        <template #append>
          <el-button @click="uploadImage('image')">上传</el-button>
        </template>
      </el-input>
    </el-form-item>

    <!-- 详情图URL -->
    <el-form-item label="详情图URL" prop="detailUrl">
      <el-input v-model="form.detailUrl" placeholder="请输入详情图URL">
        <template #append>
          <el-button @click="uploadImage('detail')">上传</el-button>
        </template>
      </el-input>
    </el-form-item>

    <!-- 提交与重置按钮 -->
    <el-form-item>
      <el-button type="primary" @click="submitForm(ruleFormRef)">确定</el-button>
      <el-button @click="resetForm(ruleFormRef)">重置</el-button>
      <el-button @click="close()">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, toRef, watch } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { listCategory } from '@/api/category'
import { getProductById, addProduct, updateProduct } from '@/api/product'
import type { Product, Category } from '@/types'

const props = defineProps<{
  productId?: number
}>()

const emit = defineEmits(['close'])

const product = reactive<Product>({
  productId: undefined,
  productSn: '',
  productName: '',
  productDescription: '',
  price: 0,
  productCategoryId: undefined,
  category: {
    categoryId: undefined,
    parentId: 0,
    categoryName: '',
  },
  imageUrl: '',
  detailUrl: '',
})
const form = toRef(product)
const ruleFormRef = ref<FormInstance>()
const categoryOptions = ref<Category[]>([])

// 表单校验规则
const rules = reactive<FormRules<Product>>({
  productName: [
    { required: true, message: '请输入商品名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符之间', trigger: 'blur' },
  ],
  productSn: [
    { required: true, message: '请输入商品编号', trigger: 'blur' },
    { min: 3, max: 20, message: '编号长度在 3 到 20 之间', trigger: 'blur' },
  ],
  price: [
    { required: true, message: '请输入商品价格', trigger: 'blur' },
    { type: 'number', min: 0, message: '价格必须大于 0', trigger: 'blur' },
  ],
  productCategoryId: [{ required: true, message: '请选择商品分类', trigger: 'change' }],
})

// 重置表单
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}

onMounted(() => {
  getCategoryOptions()
})

watch(
  () => props.productId,
  (newVal) => {
    resetForm(ruleFormRef.value)
    if (newVal) {
      getProductById(newVal).then((res) => {
        form.value = res.data
      })
    }
  },
  { immediate: true },
)

// 获取类别下拉框列表
const getCategoryOptions = () => {
  listCategory().then((res) => {
    categoryOptions.value = res.data
  })
}

// 提交表单
const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (form.value.productId != undefined) {
        updateProduct(form.value).then((response) => {
          ElMessage({ type: 'success', message: '修改数据成功' })
          close()
        })
      } else {
        addProduct(form.value).then((response) => {
          ElMessage({ type: 'success', message: '新增数据成功' })
          close()
        })
      }
    } else {
      console.log('表单验证失败:', fields)
    }
  })
}

function close() {
  emit('close')
}

// 上传图片
const uploadImage = (type: 'image' | 'detail') => {}
</script>

<style scoped>
.el-input,
.el-select,
.el-input-number {
  width: 100%;
}
</style>
