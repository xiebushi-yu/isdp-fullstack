export interface Product {
  productId: number | undefined
  productSn: string
  productName: string
  productDescription?: string
  price: number
  productCategoryId?: number
  category?: Category
  imageUrl?: string
  detailUrl?: string
}

export interface Category {
  categoryId: number | undefined
  parentId: number
  categoryName: string
}

export interface ProductQueryParams {
  pageNum: number
  pageSize: number
  productName?: string
  productSn?: string
  productCategoryId?: number
}

export interface LoginUser {
  userName: string
  password: string
}

// 在 @/types 文件中定义
export interface EnterItemForm {
  itemSn: string // 商品编码
  quantity: number // 数量
}

export interface MakePaymentForm {
    cashTendered: number;  // 付款金额
}

export interface Sale {
  saleId?: number
  saleNo?: string
  total: number
  totalQuantity: number // 新增字段
  saleItems: SaleItem[]
  // 其他字段...
}

export interface SaleItem {
  itemSn: string
  productName: string
  price: number
  quantity: number
  // 其他字段...
}
