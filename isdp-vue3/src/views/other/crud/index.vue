<template>
  <div class="app-container">
    <!-- 数据查询区：表单 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery(queryRef)">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据操作区：按钮 -->
    <el-row :gutter="10">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" @click="handleUpdate" :disabled="!single"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          @click="handleDeleteBatch"
          :disabled="!multiple"
          >批量删除</el-button
        >
      </el-col>
    </el-row>

    <!-- 数据展示区：表格 -->
    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" size="small" @click="handleUpdate(scope.row)"
            >修改</el-button
          >
          <el-button link type="primary" icon="Delete" size="small" @click="handleDelete(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 数据展示区：分页加载 -->
    <el-pagination
      style="margin-top: 20px"
      v-model:current-page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    ></el-pagination>

    <!-- 数据编辑区：新增/修改对话框 默认不显示 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm(formRef)">确 定</el-button>
          <el-button @click="resetForm(formRef)" v-show="form.id === undefined">重 置</el-button>
          <el-button @click="cancel()">取 消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
/** ------------------全局导入区-------------------- */
import { ref, reactive, toRef, onMounted } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
// import { listXxxByPage, getXxxById, addXxx, updateXxx, deleteXxx } from '@/api/xxx';

onMounted(() => {
  // 挂载后加载数据
  getList()
})

/** ------------------数据查询区——表单-------------------- */
const queryRef = ref<FormInstance>()

interface QueryParams {
  pageNum: number
  pageSize: number
  name?: string
}

const query = reactive<QueryParams>({
  pageNum: 1,
  pageSize: 10,
  name: undefined,
})
const queryParams = toRef(query) // 推荐统一使用ref

// 数据查询区--> 查询按钮
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}
// 数据查询区--> 重置按钮
const resetQuery = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
  getList()
}

/** ------------------数据展示区——表格-------------------- */
interface TableData {
  id: number
  name: string
}

const loading = ref<boolean>(false) // table数据加载遮罩
const total = ref(0) // table数据总数
const dataList = ref([] as TableData[]) // table数据

// 数据展示区--> 数据加载
function getList() {
  loading.value = true
  /** 调用后端分页查询接口 */
  /** 
     
    listXxxByPage(queryParams.value).then(response => {
      dataList.value = response.data.list
      total.value = response.data.total
      loading.value = false
    })
  
    */

  // 以下为测试代码
  const arr: TableData[] = [
    { id: 1, name: '测试1' },
    { id: 2, name: '测试2' },
  ]
  dataList.value = arr
  total.value = arr.length
  loading.value = false
}

/** ------------------数据展示区：数据选择-------------------- */

const ids = ref<number[]>([]) // 表单勾选的id
const single = ref<boolean>(false) // 勾选1个
const multiple = ref<boolean>(false) // 勾选多个

// 数据展示区--> 勾选数据
function handleSelectionChange(selection: TableData[]) {
  ids.value = selection.map((item: TableData) => item.id)
  single.value = selection.length == 1
  multiple.value = selection.length >= 1
}

/** ------------------数据展示区：分页加载-------------------- */
// 分页--> 修改每页数据数 10 ｜ 20 ｜ 50 ｜ 100
function handleSizeChange(val: number) {
  queryParams.value.pageSize = val
  getList()
}

//分页--> 修改当前页
function handleCurrentChange(val: number) {
  queryParams.value.pageNum = val
  getList()
}

/** ------------------数据编辑区——对话框+表单-------------------- */
// 表单数据
const formRef = ref<FormInstance>()
interface RuleForm {
  id: number | undefined
  name: string
}
const formData = reactive<RuleForm>({
  // 初始化表单数据
  id: undefined,
  name: '',
})
const form = toRef(formData)
const rules = reactive<FormRules<RuleForm>>({
  // 表单验证规则
  name: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
})

// 对话框数据
const title = ref('') // 对话框标题
const open = ref(false) //对话框打开

// 数据编辑区-->提交按钮
const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      /** 调用后端新增和修改接口 */
      /** 
            if (form.value.id != undefined) {
              updateXxx(form.value).then((response) => {
                ElMessage({type: 'success',message: '修改数据成功'})
                open.value = false
                getList()
              })
            } else {
              addXxx(form.value).then((response) => {
                ElMessage({type: 'success',message: '新增数据成功'})
                open.value = false
                getList()
              })
            }
  
            */

      // 以下为测试代码
      ElMessage.success(JSON.stringify(form.value))
    } else {
      console.log('表单验证未通过', fields) // 前台会自动提示errors
    }
  })
}

// 数据编辑区-->重置按钮
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
// 数据编辑区-->取消按钮
function cancel() {
  resetForm(formRef.value)
  open.value = false
}

/** ------------------数据操作区——按钮-------------------- */

// 数据操作区--> 新增按钮
function handleAdd() {
  resetForm(formRef.value)
  title.value = '新增数据'
  open.value = true
}

// 数据操作区--> 修改按钮
function handleUpdate(row: TableData) {
  resetForm(formRef.value)
  const id = row.id || ids.value[0]
  /** 调用后端id查询接口*/
  /** 
     
    getXXX(id).then((response) => {
      form.value = response.data
      title.value = '修改数据['+ id +']'
      open.value = true
    })
  
    */
  title.value = '修改数据[' + id + ']'
  dataList.value.forEach((item: TableData) => {
    if (item.id == id) {
      form.value.id = id
      form.value.name = item.name
    }
  })
  open.value = true
}

// 数据操作区--> 删除按钮
function handleDelete(row: TableData) {
  const id = row.id
  ElMessageBox.confirm('是否删除编号为' + id + '的数据?', '警告')
    .then(() => {
      /** 调用后端删除接口 */
      // return deleteXxx(id)
    })
    .then(() => {
      getList()
      ElMessage({ type: 'success', message: '删除成功' })
    })
}
// 数据操作区--> 批量删除
function handleDeleteBatch() {
  ElMessageBox.confirm('是否删除编号为' + ids.value + '的数据?', '警告')
    .then(() => {
      /** 调用后端批量删除接口 */
      // return deleteXxxBatch(ids.value)
    })
    .then(() => {
      getList()
      ElMessage({ type: 'success', message: '批量删除成功' })
    })
}
</script>
