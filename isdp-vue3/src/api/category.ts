// @/api/category.ts
import request from '@/utils/request'
import type { Category } from '@/types'

// 根据ID查询类别详情
export function getCategoryById(categoryId: number) {
  return request({
    url: `/category/${categoryId}`,
    method: 'get',
  })
}

// 获取所有类别列表
export function listCategory() {
  return request({
    url: '/category/listAll',
    method: 'get',
  })
}

// 新增
export function addCategory(data: Category) {
  return request({
    url: '/category/add',
    method: 'post',
    data,
  })
}

// 修改
export function updateCategory(data: Category) {
  return request({
    url: '/category/update',
    method: 'put',
    data,
  })
}

// 删除
export function deleteCategory(categoryId: number) {
  return request({
    url: `/category/${categoryId}`,
    method: 'delete',
  })
}
