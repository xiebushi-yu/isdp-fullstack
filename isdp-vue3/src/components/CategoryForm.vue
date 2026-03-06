<template>
  <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="80px" status-icon>
    <el-form-item label="类别ID" prop="categoryId" v-if="ruleForm.categoryId">
      <el-input v-model="ruleForm.categoryId" disabled />
    </el-form-item>

    <el-form-item label="父级类别" prop="parentId">
      <el-select v-model="ruleForm.parentId" placeholder="请选择父级类别">
        <el-option
          v-for="item in parentCategoryOptions"
          :key="item.categoryId"
          :label="item.categoryName"
          :value="item.categoryId"
        />
      </el-select>
    </el-form-item>

    <el-form-item label="类别名称" prop="categoryName">
      <el-input v-model="ruleForm.categoryName" placeholder="请输入类别名称" />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="submitForm(ruleFormRef)">提交</el-button>
      <el-button @click="resetForm(ruleFormRef)">重置</el-button>
      <el-button @click="$emit('cancel')">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, toRaw, watch, nextTick } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { getCategoryById } from '@/api/category'

// 定义 Props 和 Emits
const props = defineProps<{
  categoryId: number | null
}>()

const emit = defineEmits<{
  success: []
  cancel: []
}>()

// 监听 categoryId 变化
watch(
  () => props.categoryId,
  (newVal, oldVal) => {
    console.log(`categoryId 变化: ${oldVal} -> ${newVal}`)
    if (newVal !== oldVal) {
      init()
    }
  },
  { immediate: true }, // 添加 immediate 确保首次加载也执行
)

onMounted(() => {
  init()
})

// 初始化表单
function init() {
  if (props.categoryId) {
    // 修改模式：根据 categoryId 获取数据
    getCategoryById(props.categoryId)
      .then((res) => {
        ruleForm.categoryId = res.data.categoryId
        ruleForm.parentId = res.data.parentId
        ruleForm.categoryName = res.data.categoryName
      })
      .catch(() => {
        ElMessage.error('获取类别信息失败')
      })
  } else {
    // 新增模式：重置表单
    resetForm()
  }
}

/** 表单数据接口定义 */
interface RuleForm {
  categoryId: number | null
  parentId: number
  categoryName: string
}

const ruleFormRef = ref<FormInstance>()
const ruleForm = reactive<RuleForm>({
  categoryId: null,
  parentId: 0,
  categoryName: '',
})

/** 父级类别下拉框选项列表 */
const parentCategoryOptions = [
  { categoryId: 0, categoryName: '顶级类别' },
  { categoryId: 1, categoryName: '文具' },
  { categoryId: 2, categoryName: '日用品' },
  { categoryId: 3, categoryName: '电子产品' },
]

/** 表单验证规则 */
const rules = reactive<FormRules<RuleForm>>({
  parentId: [{ required: true, message: '请选择父级类别', trigger: 'change' }],
  categoryName: [
    { required: true, message: '请输入类别名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' },
  ],
})

/** 提交表单 */
const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return

  await formEl.validate((valid) => {
    if (valid) {
      const form = toRaw(ruleForm)
      ElMessage.success(`提交数据: ${JSON.stringify(form)}`)
      // 这里可以调用新增或修改的 API
      // if (props.categoryId) {
      //   // 修改操作
      //   updateCategory(form).then(() => {
      //     emit('success')
      //   })
      // } else {
      //   // 新增操作
      //   addCategory(form).then(() => {
      //     emit('success')
      //   })
      // }
      emit('success')
    } else {
      ElMessage.warning('请完善表单信息')
    }
  })
}

/** 重置表单 */
const resetForm = (formEl?: FormInstance) => {
  if (formEl) {
    formEl.resetFields()
  }
  ruleForm.categoryId = null
  ruleForm.parentId = 0
  ruleForm.categoryName = ''
}
</script>
