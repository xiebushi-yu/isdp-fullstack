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

export interface EnterItemForm {
  itemSn: string
  quantity: number
}

export interface MakePaymentForm {
  cashTendered: number
}

export interface Sale {
  saleId?: number
  saleNo?: string
  total: number
  totalQuantity: number
  saleItems: SaleItem[]
}

export interface SaleItem {
  itemSn: string
  productName: string
  price: number
  quantity: number
}
