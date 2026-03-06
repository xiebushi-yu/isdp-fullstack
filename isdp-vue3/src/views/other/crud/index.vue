<template>
  <div class="app-container">
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
import { ref, reactive, toRef, onMounted } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'

onMounted(() => {
  getList()
})

//数据查询区
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
const queryParams = toRef(query)
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}
const resetQuery = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
  getList()
}

//数据展示区
interface TableData {
  id: number
  name: string
}
const loading = ref<boolean>(false)
const total = ref(0)
const dataList = ref([] as TableData[])
function getList() {
  loading.value = true
  /**

    listXxxByPage(queryParams.value).then(response => {
      dataList.value = response.data.list
      total.value = response.data.total
      loading.value = false
    })

    */

  // 测试
  const arr: TableData[] = [
    { id: 1, name: '测试1' },
    { id: 2, name: '测试2' },
  ]
  dataList.value = arr
  total.value = arr.length
  loading.value = false
}

const ids = ref<number[]>([])
const single = ref<boolean>(false)
const multiple = ref<boolean>(false)

function handleSelectionChange(selection: TableData[]) {
  ids.value = selection.map((item: TableData) => item.id)
  single.value = selection.length == 1
  multiple.value = selection.length >= 1
}
function handleSizeChange(val: number) {
  queryParams.value.pageSize = val
  getList()
}

function handleCurrentChange(val: number) {
  queryParams.value.pageNum = val
  getList()
}

//数据编辑区
const formRef = ref<FormInstance>()
interface RuleForm {
  id: number | undefined
  name: string
}
const formData = reactive<RuleForm>({
  id: undefined,
  name: '',
})
const form = toRef(formData)
const rules = reactive<FormRules<RuleForm>>({
  name: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
})

const title = ref('')
const open = ref(false)

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

      // 测试
      ElMessage.success(JSON.stringify(form.value))
    } else {
      console.log('表单验证未通过', fields)
    }
  })
}

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
function cancel() {
  resetForm(formRef.value)
  open.value = false
}

//数据操作区
function handleAdd() {
  resetForm(formRef.value)
  title.value = '新增数据'
  open.value = true
}

function handleUpdate(row: TableData) {
  resetForm(formRef.value)
  const id = row.id || ids.value[0]
  title.value = '修改数据[' + id + ']'
  dataList.value.forEach((item: TableData) => {
    if (item.id == id) {
      form.value.id = id
      form.value.name = item.name
    }
  })
  open.value = true
}

function handleDelete(row: TableData) {
  const id = row.id
  ElMessageBox.confirm('是否删除编号为' + id + '的数据?', '警告').then(() => {
    getList()
    ElMessage({ type: 'success', message: '删除成功' })
  })
}
function handleDeleteBatch() {
  ElMessageBox.confirm('是否删除编号为' + ids.value + '的数据?', '警告').then(() => {
    getList()
    ElMessage({ type: 'success', message: '批量删除成功' })
  })
}
</script>
