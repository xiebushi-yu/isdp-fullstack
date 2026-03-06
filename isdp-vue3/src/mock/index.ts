import Mock from 'mockjs'
import userMock from './user'

console.log('用户Mock配置:', userMock)

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

function setupMock() {
  Mock.setup({
    timeout: 400, // 模拟延迟
  })

  console.log('开始注册Mock接口，数量:', mockData.length)

  mockData.forEach((item) => {
    Mock.mock(new RegExp(item.url), item.method, item.response)
    console.log(`注册Mock接口: ${item.method} ${item.url}`)
  })

  console.log('Mock接口注册完成')
}

export default setupMock

export { mockData }
