import request from '@/utils/request'
import type { EnterItemForm, MakePaymentForm, Sale } from '@/types'
// 开始新的销售
export function makeNewSale() {
  return request({
    url: '/sale/makeNewSale',
    method: 'get',
  })
}
// 录入订单明细 - 返回完整的Sale对象
export function enterItem(params: EnterItemForm) {
  return request<Sale>({
    // 添加泛型类型
    url: '/sale/enterItem',
    method: 'post',
    params: params,
  })
}
// 结束录入 - 返回完整的Sale对象
export function endSale() {
  return request<Sale>({
    // 添加泛型类型
    url: '/sale/endSale',
    method: 'get',
  })
}

// 确认支付
export function makePayment(params: MakePaymentForm) {
  return request({
    url: '/sale/makePayment',
    method: 'put',
    params: params,
  })
}

// 删除订单明细
export function removeItem(itemSn: string) {
  return request<Sale>({
    url: '/sale/removeItem',
    method: 'delete',
    params: { itemSn },
  })
}

// 更新订单明细数量
export function updateQuantity(params: { itemSn: string; quantity: number }) {
  return request<Sale>({
    url: '/sale/updateQuantity',
    method: 'put',
    params: params,
  })
}
