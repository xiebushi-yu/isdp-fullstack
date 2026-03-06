// @/api/category.ts
import request from '@/utils/request'
import type { Category } from '@/types' // 假设定义了Category类型

// 1. 根据ID查询类别详情（对应后端 @GetMapping("/{categoryId}")）
export function getCategoryById(categoryId: number) {
  return request({
    url: `/category/${categoryId}`, // 对应后端路径
    method: 'get',
  })
}

// 2. 获取所有类别列表（对应后端 @GetMapping("/listAll")）
export function listCategory() {
  return request({
    url: '/category/listAll', // 修正路径为listAll
    method: 'get',
  })
}

// 3. 新增类别（对应后端 @PostMapping("/add")）
export function addCategory(data: Category) {
  return request({
    url: '/category/add', // 修正路径为add
    method: 'post',
    data, // 传递Category对象
  })
}

// 4. 修改类别（对应后端 @PutMapping("/update")）
export function updateCategory(data: Category) {
  return request({
    url: '/category/update', // 修正路径为update
    method: 'put',
    data, // 传递Category对象
  })
}

// 5. 删除类别（对应后端 @DeleteMapping("/{categoryId}")）
export function deleteCategory(categoryId: number) {
  return request({
    url: `/category/${categoryId}`, // 对应后端路径
    method: 'delete',
  })
}
