import Mock from 'mockjs'
import userMock from './user' // 导入用户Mock配置

console.log('用户Mock配置:', userMock)

/** Mock模拟生成 */
function createMockCategoryList() {
  const mockData = Mock.mock({
    'list|10': [
      {
        'categoryId|+1': 1,
        parentId: 0,
        categoryName: '@ctitle',
      },
    ],
  })
  return mockData.list
}

/** 配置Mock接口 */
const mockData = [
  ...userMock,
  {
    url: '/api/category/listAll',
    method: 'get',
    response: () => {
      console.log('Mock 接口被调用')
      const categoryList = createMockCategoryList()
      return { code: 200, msg: '查询成功', data: categoryList }
    },
  },
]

// 添加 setupMock 函数
function setupMock() {
  Mock.setup({
    timeout: 400, // 模拟延迟
  })

  console.log('开始注册Mock接口，数量:', mockData.length)

  // 注册所有mock接口
  mockData.forEach((item) => {
    Mock.mock(new RegExp(item.url), item.method, item.response)
    console.log(`注册Mock接口: ${item.method} ${item.url}`)
  })

  console.log('Mock接口注册完成')
}

// 默认导出 setupMock 函数
export default setupMock

// 如果需要，也可以保留数组的命名导出
export { mockData }
