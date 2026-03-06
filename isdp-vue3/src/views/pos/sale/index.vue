<template>
  <div class="cashier-container">
    <div class="process-nav">
      <el-steps :active="4" align-center finish-status="success" class="custom-steps">
        <el-step title="MakeNewSale" />
        <el-step title="EnterItem" />
        <el-step title="EndSale" />
        <el-step title="MakePayment" />
      </el-steps>
    </div>

    <div class="layout-container">
      <!-- 左侧区域 -->
      <div class="left-panel">
        <el-card class="card-item">
          <template #header>
            <div class="card-header">商品录入</div>
          </template>
          <el-form :model="productForm" label-width="80px" class="compact-form">
            <el-form-item label="商品编码">
              <el-input
                v-model="productForm.itemSn"
                placeholder="请输入商品编码"
                class="custom-input"
                clearable
                @keyup.enter="handleEnterItem"
              />
            </el-form-item>
            <el-form-item label="订购数量">
              <el-input-number
                v-model="productForm.quantity"
                :min="1"
                controls-position="right"
                class="compact-input-number"
              />
            </el-form-item>
            <div class="form-buttons">
              <el-button
                class="enter-item-btn"
                @click="handleEnterItem"
                :disabled="isEnterItemDisabled"
              >
                ENTER ITEM
              </el-button>
              <el-button class="end-sale-btn" @click="handleEndSale" :disabled="isEndSaleDisabled">
                END SALE
              </el-button>
            </div>
          </el-form>
        </el-card>

        <el-card class="card-item">
          <template #header>
            <div class="card-header">订单支付</div>
          </template>
          <el-form :model="paymentForm" label-width="80px" class="compact-form">
            <el-form-item label="付款金额">
              <el-input v-model="paymentForm.paidAmount" class="custom-input" />
            </el-form-item>
            <el-form-item label="找零">
              <el-input v-model="paymentForm.change" class="custom-input" />
            </el-form-item>
            <div class="form-buttons">
              <el-button
                class="make-payment-btn"
                @click="handleMakePayment"
                :disabled="isMakePaymentDisabled"
              >
                MAKE PAYMENT
              </el-button>
            </div>
          </el-form>
        </el-card>
      </div>

      <!-- 右侧区域 -->
      <div class="right-panel">
        <div class="order-header">
          <div class="header-top">
            <h2 class="header-title">订单信息</h2>
            <el-button
              class="new-sale-button"
              @click="handleMakeNewSale"
              :disabled="isNewSaleDisabled"
            >
              MAKE NEW SALE
            </el-button>
          </div>
          <div class="order-info-container">
            <el-descriptions :column="3" border class="order-descriptions">
              <el-descriptions-item>
                <template #label>
                  <div class="label-content">
                    <el-icon><User /></el-icon>
                    <span>会员</span>
                  </div>
                </template>
                张三
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="label-content">
                    <el-icon><Lock /></el-icon>
                    <span>订单号</span>
                  </div>
                </template>
                so-20241028-001
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="label-content">
                    <el-icon><Wallet /></el-icon>
                    <span>总金额</span>
                  </div>
                </template>
                {{ totalAmount }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="label-content">
                    <el-icon><Grid /></el-icon>
                    <span>总件数</span>
                  </div>
                </template>
                {{ totalQuantity }}
              </el-descriptions-item>
              <el-descriptions-item :span="2">
                <template #label>
                  <div class="label-content">
                    <el-icon><InfoFilled /></el-icon>
                    <span>备注</span>
                  </div>
                </template>
                送货上门
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </div>

        <!-- 商品表格 -->
        <el-card class="table-card">
          <template #header>
            <div class="card-header">订单明细</div>
          </template>
          <el-table
            :data="tableData"
            style="width: 100%"
            class="stripe-table"
            :row-class-name="tableRowClassName"
          >
            <el-table-column type="index" label="序号" align="center" />
            <el-table-column label="商品编码" align="center">
              <template #default="scope">
                {{ scope.row.itemSn }}
              </template>
            </el-table-column>

            <el-table-column label="商品名称" align="center">
              <template #default="scope">
                {{ scope.row.productName }}
              </template>
            </el-table-column>

            <el-table-column label="销售价格" align="center">
              <template #default="scope"> ¥{{ scope.row.price.toFixed(2) }} </template>
            </el-table-column>

            <el-table-column prop="quantity" label="订购数量" align="center">
              <template #default="scope">
                <el-input-number
                  v-model="scope.row.quantity"
                  @change="(value: number) => handleQuantityChange(scope.row.itemSn, value)"
                  :min="1"
                  :max="10"
                  size="small"
                  controls-position="right"
                  class="compact-input-number"
                />
              </template>
            </el-table-column>

            <el-table-column label="操作" align="center">
              <template #default="scope">
                <el-button
                  type="text"
                  size="small"
                  @click="handleDeleteItem(scope.row.itemSn)"
                  class="delete-button"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <template #footer>
            <div class="table-summary">
              总件数: {{ totalQuantity }} 件 | 总金额: {{ totalAmount }} 元
            </div>
          </template>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import {
  makeNewSale,
  enterItem,
  endSale,
  makePayment,
  removeItem,
  updateQuantity,
} from '@/api/sale'
import type { Sale, SaleItem } from '@/types'
import { ElMessage, ElMessageBox } from 'element-plus'

const productForm = reactive({
  itemSn: '',
  quantity: 1,
})

const paymentForm = reactive({
  paidAmount: '',
  change: '0',
})
const tableData = ref<SaleItem[]>([])
const totalAmount = ref(0)
const totalQuantity = ref(0)
const saleStatus = ref({
  hasNewSale: false,
  hasItems: false,
  hasEndedSale: false,
  hasPaid: false,
})

//重置状态
const resetAllStatus = () => {
  saleStatus.value = {
    hasNewSale: false,
    hasItems: false,
    hasEndedSale: false,
    hasPaid: false,
  }

  tableData.value = []
  totalAmount.value = 0
  totalQuantity.value = 0
  productForm.itemSn = ''
  productForm.quantity = 1
  paymentForm.paidAmount = ''
  paymentForm.change = '0'
}

//依据状态看是否禁用按钮
const isNewSaleDisabled = ref(false)
const isEnterItemDisabled = computed(() => !saleStatus.value.hasNewSale)
const isEndSaleDisabled = computed(() => !saleStatus.value.hasItems)
const isMakePaymentDisabled = computed(() => !saleStatus.value.hasEndedSale)

const tableRowClassName = ({ rowIndex }: { rowIndex: number }) => {
  if (rowIndex % 4 === 0) return 'row-orange'
  if (rowIndex % 4 === 1) return 'row-blue'
  if (rowIndex % 4 === 2) return 'row-orange'
  return 'row-green'
}

/**
 * 根据后端返回的Sale对象更新前端所有状态
 * @param sale 后端返回的完整Sale对象
 */
const updateFrontendFromSale = (sale: Sale) => {
  tableData.value = (sale.saleItems || []).map((item: any) => {
    const product = item.product || {}
    return {
      itemSn: product.productSn || '',
      productName: product.productName || '',
      price: product.price || 0,
      quantity: item.quantity || 0,
      originalItem: item,
    }
  })

  totalAmount.value = sale.total || 0
  totalQuantity.value = sale.totalQuantity || 0

  saleStatus.value.hasItems = tableData.value.length > 0
}

// 处理商品录入
const handleEnterItem = async () => {
  if (!productForm.itemSn || productForm.itemSn.trim() === '') {
    ElMessage.error('请输入商品编码')
    return
  }

  if (!productForm.quantity || productForm.quantity <= 0) {
    ElMessage.error('数量必须大于0')
    return
  }

  try {
    const response = await enterItem({
      itemSn: productForm.itemSn,
      quantity: productForm.quantity,
    })

    updateFrontendFromSale(response.data)

    saleStatus.value.hasItems = true

    productForm.quantity = 1

    ElMessage.success('商品录入成功')
  } catch (error: any) {
    console.error('录入商品失败:', error)

    if (error.response?.status === 404) {
      ElMessage.error(`商品编码 "${productForm.itemSn}" 不存在`)
      productForm.itemSn = ''
    } else {
      ElMessage.error('该商品编码不存在，录入商品失败，请重试')
    }

    const { status, data } = error.response

    if (status >= 400 && status < 500) {
      const errorMsg = data?.message || '输入有误，请检查商品编码和数量'
      ElMessage.error(errorMsg)
      return
    }

    if (status >= 500) {
      ElMessage.error('服务器错误，请稍后重试')
      return
    }

    ElMessage.error('录入商品失败，请重试')
  }
}

// 处理结束销售
const handleEndSale = async () => {
  try {
    const response = await endSale()
    const sale = response.data

    updateFrontendFromSale(response.data)

    saleStatus.value.hasEndedSale = true

    ElMessage.success('销售结束，请进行支付')
    console.log('结束销售成功:', sale)
  } catch (error) {
    console.error('结束销售失败:', error)
    ElMessage.error('结束销售失败，请重试')
  }
}

// 处理支付
const handleMakePayment = async () => {
  if (!paymentForm.paidAmount || paymentForm.paidAmount.trim() === '') {
    ElMessage.error('请输入付款金额')
    return
  }

  const paidAmount = parseFloat(paymentForm.paidAmount)
  if (isNaN(paidAmount)) {
    ElMessage.error('请输入有效的数字金额')
    return
  }

  if (paidAmount <= 0) {
    ElMessage.error('付款金额必须大于0')
    return
  }
  //金额不足时
  if (paidAmount < totalAmount.value) {
    ElMessage.error(
      `付款金额不足！总金额为 ${totalAmount.value} 元，还需支付 ${totalAmount.value - paidAmount} 元`,
    )
    return
  }

  const change = paidAmount - totalAmount.value
  if (change > 0) {
    const confirmMessage = `收款：${paidAmount}元，找零：${change}元，确认支付吗？`
    try {
      await ElMessageBox.confirm(confirmMessage, '确认支付', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      })
    } catch {
      ElMessage.info('已取消支付')
      return
    }
  }

  try {
    const response = await makePayment({
      cashTendered: paidAmount,
    })

    const change = response.data

    saleStatus.value.hasPaid = true

    paymentForm.change = change.toString()

    ElMessage.success('支付成功')
    console.log('支付成功，找零:', change)
  } catch (error) {
    console.error('支付失败:', error)
    ElMessage.error('支付失败，请重试')
  }

  resetAllStatus()
}

// 处理新帐单
const handleMakeNewSale = async () => {
  try {
    const response = await makeNewSale()
    const sale = response.data

    resetAllStatus()

    saleStatus.value = {
      hasNewSale: true,
      hasItems: false,
      hasEndedSale: false,
      hasPaid: false,
    }

    ElMessage.success('新建销售成功')
    console.log('新建销售成功:', sale)
  } catch (error) {
    console.error('新建销售失败:', error)
    ElMessage.error('新建销售失败，请重试')
  }
}

// 处理删除商品
const handleDeleteItem = async (itemSn: string) => {
  console.log('=== 开始删除操作 ===')
  console.log('要删除的商品编码:', itemSn)
  console.log('当前表格数据:', tableData.value)

  if (!itemSn) {
    ElMessage.warning('商品编码无效')
    return
  }

  try {
    await ElMessageBox.confirm(`确定要删除商品编码为 ${itemSn} 的商品吗？`, '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    console.log('用户确认删除')
  } catch {
    ElMessage.info('已取消删除')
    console.log('用户取消删除')
    return
  }

  try {
    console.log('开始调用删除API...')
    const response = await removeItem(itemSn)
    console.log('删除API响应:', response)
    const sale = response.data
    console.log('删除后返回的sale对象:', sale)

    updateFrontendFromSale(sale)

    ElMessage.success('删除成功')
    console.log('删除操作完成')
  } catch (error: any) {
    console.error('=== 删除失败详情 ===')
    console.error('完整错误对象:', error)
    console.error('响应状态码:', error.response?.status)
    console.error('响应数据:', error.response?.data)
    console.error('响应消息:', error.response?.data?.message)
    console.error('错误消息:', error.message)

    if (error.response?.status === 404) {
      ElMessage.error(`商品编码 "${itemSn}" 不存在或已被删除`)
    } else if (error.message) {
      ElMessage.error(error.message)
    } else {
      ElMessage.error('删除失败，请重试')
    }
  }
}

const handleQuantityChange = async (itemSn: string, newQuantity: number) => {
  if (!itemSn || newQuantity < 1) return

  try {
    const response = await updateQuantity({
      itemSn: itemSn,
      quantity: newQuantity,
    })

    updateFrontendFromSale(response.data)
  } catch (error: any) {
    console.error('更新数量失败:', error)

    ElMessage.error('数量修改失败')

    const item = tableData.value.find((item) => item.itemSn === itemSn)
    if (item) {
    }
  }
}
</script>

<style scoped>
.cashier-container {
  padding: 20px;
  background-color: white;
  min-height: 100vh;
  font-size: 20px;
}

.process-nav {
  margin-bottom: 20px;
  padding: 20px;
  border-bottom: 1px solid #dedede;
}

:deep(.custom-steps .el-step__head.is-finish) {
  color: #67c23a;
  border-color: #67c23a;
}

:deep(.custom-steps .el-step__title.is-finish) {
  color: #67c23a;
}

:deep(.custom-steps .el-step__line) {
  background-color: #67c23a;
}

:deep(.custom-steps .el-step__icon) {
  background-color: #67c23a;
  border-color: #67c23a;
  color: white;
}

.layout-container {
  display: flex;
  gap: 20px;
  max-width: 100%;
}

/* 左侧面板样式 */
.left-panel {
  flex: 0 0 300px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.card-item {
  flex: 1;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
}

.card-header {
  color: #333;
  font-size: 20px;
}

.compact-form {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.compact-form .el-form-item {
  margin-bottom: 16px;
}

.form-buttons {
  margin-top: auto;
  margin-bottom: 0 !important;
  display: flex;
  justify-content: flex-start;
  gap: 5px;
}

.custom-select,
.custom-input {
  width: 100%;
}

.compact-input-number {
  width: 120px;
}

:deep(.compact-input-number .el-input-number__decrease),
:deep(.compact-input-number .el-input-number__increase) {
  width: 24px;
  background-color: #f5f7fa;
  border-left: 1px solid #dcdfe6;
}

.enter-item-btn {
  background-color: #a0cfff;
  border-color: #a0cfff;
  color: #ffffff;
}

.enter-item-btn:hover {
  background-color: #8bc2ff;
  border-color: #8bc2ff;
}

.end-sale-btn {
  background-color: #e6a23c;
  border-color: #e6a23c;
  color: #ffffff;
}

.end-sale-btn:hover {
  background-color: #e29c2f;
  border-color: #e29c2f;
}

.make-payment-btn {
  background-color: #f78989;
  border-color: #f78989;
  color: #ffffff;
}

.make-payment-btn:hover {
  background-color: #f56c6c;
  border-color: #f56c6c;
}

/* 右侧面板样式 */
.right-panel {
  flex: 1;
}

.order-header {
  display: flex;
  gap: 20px;
  flex-direction: column;
  padding-bottom: 20px;
  border-bottom: 1px solid #dedede;
}

.header-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.header-title {
  font-size: 20px;
  font-weight: 500;
  color: #000;
  margin: 0;
}

.order-descriptions {
  flex: 1;
}

:deep(.desc-label) {
  background-color: #f0f2f5 !important;
  color: #606266;
  font-weight: 500;
}

:deep(.desc-content) {
  background-color: #fafafa !important;
  color: #303133;
}

.new-sale-button {
  background-color: #5cb85c;
  border-color: #5cb85c;
  color: white;
  font-weight: bold;
  white-space: nowrap;
}

.new-sale-button:hover {
  background-color: #4cae4c;
  border-color: #4cae4c;
}

.new-sale-button:disabled {
  opacity: 0.6;
}

.label-content {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 0;
}

.table-card {
  margin-top: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
}

:deep(.row-orange) {
  background-color: #fff9f0 !important;
}

:deep(.row-blue) {
  background-color: #f0f9ff !important;
}

:deep(.row-green) {
  background-color: #f0fff0 !important;
}

:deep(.stripe-table .el-table__row:hover) {
  background-color: #ecf5ff !important;
}

.delete-button {
  color: #409eff;
  padding: 4px 8px;
}

.delete-button:hover {
  color: #66b1ff;
  background-color: transparent;
}

.table-summary {
  background-color: white;
  border-radius: 4px;
  color: #303133;
}

/* 小屏幕适配 */
@media (max-width: 768px) {
  .layout-container {
    flex-direction: column;
  }

  .left-panel {
    flex: none;
    width: 100%;
  }

  .order-header {
    flex-direction: column;
  }

  .new-sale-button {
    align-self: flex-end;
  }
}
</style>
