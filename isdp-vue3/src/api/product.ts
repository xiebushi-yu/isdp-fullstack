import request from '@/utils/request'
import type { Product, ProductQueryParams } from '@/types/index'

// 根据ID获取产品
export function getProductById(productId: number) {
  return request({
    url: `/product/${productId}`,
    method: 'get',
  })
}

// 根据产品SN查询产品
export function getProductBySn(productSn: string) {
  return request({
    url: `/product/getBySn/${productSn}`,
    method: 'get',
  })
}

// 获取产品列表
export function listProduct() {
  return request({
    url: '/product/listAll',
    method: 'get',
  })
}

///获取全部产品
export function listAllProduct() {
  return request({
    url: '/product/listAll',
    method: 'get',
  })
}

// 根据条件查询产品列表
export function listProductByParams(params: Partial<Product>) {
  return request({
    url: '/product/listByParams',
    method: 'get',
    params: params,
  })
}

export function listProductByPage(query: ProductQueryParams) {
  return request({
    url: '/product/page',
    method: 'get',
    params: query,
  })
}

// 新增产品
export function addProduct(data: any) {
  return request({
    url: '/product/add',
    method: 'post',
    data: data,
  })
}

// 修改产品
export function updateProduct(data: any) {
  return request({
    url: '/product/update',
    method: 'put',
    data: data,
  })
}

// 删除产品
export function deleteProduct(productId: number) {
  return request({
    url: `/product/delete/${productId}`,
    method: 'delete',
  })
}

export function deleteProductBatch(productIds: number[]) {
  return request({
    url: '/product/deleteBatch/' + productIds,
    method: 'delete',
  })
}
