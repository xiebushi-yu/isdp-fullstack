import Mock from 'mockjs'

interface MockRequest {
  body?:
    | string
    | {
        userName?: string
        password?: string
      }
  headers?: {
    token?: string
  }
}

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

export default [
  // {
  //   url: '/api/user/login',
  //   method: 'post',
  //   response: ({ body }: MockRequest) => {
  //     const { userName, password } = body || {}
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

      if (typeof request.body === 'string') {
        try {
          const parsedBody = JSON.parse(request.body)
          userName = parsedBody.userName
          password = parsedBody.password
        } catch (e) {
          console.log('解析JSON失败:', e)
        }
      } else if (typeof request.body === 'object') {
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
    response: (request: MockRequest) => {
      const token = request.headers?.token
      const user = createStaticUserList().find((item) => item.token === token)
      if (!user) {
        return { code: 201, msg: '查询用户信息失败', data: {} }
      }
      return { code: 200, msg: '查询用户成功', data: { user } }
    },
  },
]
