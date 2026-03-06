import Mock from 'mockjs'

// 定义 Mock 请求的参数类型（包含 body、headers 等）
interface MockRequest {
  body?:
    | string
    | {
        // POST 请求的 body 数据
        userName?: string
        password?: string
      }
  headers?: {
    // 请求头
    token?: string
  }
}

/** 静态指定mock数据 */
function createStaticUserList() {
  return [
    {
      avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
      userName: 'admin',
      password: '123456',
      desc: '系统管理员',
      roles: ['admin'],
      token: 'admin_token',
    },
    {
      avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
      userName: 'user',
      password: '123456',
      desc: '普通用户',
      roles: ['common'],
      token: 'user_token',
    },
  ]
}

/** 配置Mock接口 */
export default [
  // {
  //   url: '/api/user/login',
  //   method: 'post',
  //   // 为 body 添加类型注解（从 MockRequest 中解构）
  //   response: ({ body }: MockRequest) => {
  //     const { userName, password } = body || {} // 避免 body 为 undefined
  //     console.log('登录请求数据:', { userName, password })

  //     const user = createStaticUserList().find(
  //       (item) => item.userName === userName && item.password === password,
  //     )
  //     console.log('找到的用户:', user) // 查看是否找到了匹配的用户

  //     if (!user) {
  //       return { code: 401, msg: '认证失败，无法访问系统资源', data: {} }
  //     }
  //     const { token } = user
  //     return { code: 200, msg: '用户登录成功', data: { token, userName } }
  //   },
  // },

  {
    url: '/api/user/login',
    method: 'post',
    response: (request: MockRequest) => {
      let userName, password

      // 尝试不同的方式解析 body 数据
      if (typeof request.body === 'string') {
        // 如果 body 是字符串，尝试解析 JSON
        try {
          const parsedBody = JSON.parse(request.body)
          userName = parsedBody.userName
          password = parsedBody.password
        } catch (e) {
          console.log('解析JSON失败:', e)
        }
      } else if (typeof request.body === 'object') {
        // 如果 body 已经是对象
        userName = request.body?.userName
        password = request.body?.password
      }

      if (!userName || !password) {
        return { code: 400, msg: '用户名或密码不能为空', data: {} }
      }

      const user = createStaticUserList().find(
        (item) => item.userName === userName && item.password === password,
      )

      if (!user) {
        return { code: 401, msg: '认证失败，无法访问系统资源', data: {} }
      }

      const { token } = user
      return { code: 200, msg: '用户登录成功', data: { token, userName } }
    },
  },

  {
    url: '/api/user/info',
    method: 'get',
    // 为 request 添加类型注解
    response: (request: MockRequest) => {
      const token = request.headers?.token // 使用可选链避免 headers 为 undefined
      const user = createStaticUserList().find((item) => item.token === token)
      if (!user) {
        return { code: 201, msg: '查询用户信息失败', data: {} }
      }
      return { code: 200, msg: '查询用户成功', data: { user } }
    },
  },
]
