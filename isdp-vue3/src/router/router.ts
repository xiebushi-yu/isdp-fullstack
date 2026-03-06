// src/router/router.ts
export const routerData = [
  {
    path: '/home',
    name: 'home',
    meta: {
      icon: 'House',
      title: '首页',
    },
    component: () => import('@/views/home/index.vue'),
  },
  {
    path: '/item',
    name: 'item',
    redirect: '/item/category',
    meta: {
      icon: 'Box',
      title: '商品管理',
    },
    children: [
      {
        path: '/item/category',
        name: 'category',
        meta: {
          icon: 'Collection',
          title: '分类管理',
        },
        component: () => import('@/views/item/category/index.vue'),
      },
      {
        path: '/item/product',
        name: 'product',
        meta: {
          icon: 'Goods',
          title: '商品管理',
        },
        component: () => import('@/views/item/product/index.vue'),
      },
    ],
  },
  {
    path: '/pos',
    name: 'pos',
    redirect: '/pos/sale',
    meta: {
      icon: 'Shop',
      title: '销售管理',
    },
    children: [
      {
        path: '/pos/sale',
        name: 'sale',
        meta: {
          icon: 'Sell',
          title: '销售',
        },
        component: () => import('@/views/pos/sale/index.vue'),
      },
      {
        path: '/pos/payment',
        name: 'payment',
        meta: {
          icon: 'Wallet',
          title: '支付',
        },
        component: () => import('@/views/pos/payment/index.vue'),
      },
    ],
  },
  {
    path: '/system',
    name: 'system',
    redirect: '/system/user',
    meta: {
      icon: 'Setting',
      title: '系统管理',
    },
    children: [
      {
        path: '/system/user',
        name: 'system-user',
        meta: {
          icon: 'User',
          title: '用户管理',
        },
        component: () => import('@/views/system/user/index.vue'),
      },
      {
        path: '/system/role',
        name: 'system-role',
        meta: {
          icon: 'Lock',
          title: '角色管理',
        },
        component: () => import('@/views/system/role/index.vue'),
      },
      {
        path: '/system/menu',
        name: 'system-menu',
        meta: {
          icon: 'Menu',
          title: '菜单管理',
        },
        component: () => import('@/views/system/menu/index.vue'),
      },
    ],
  },
  {
    path: '/other',
    name: 'other',
    redirect: '/other/case',
    meta: {
      icon: 'Flag',
      title: '扩展案例',
    },
    children: [
      {
        path: '/other/case',
        name: 'case',
        meta: {
          icon: 'ChatRound',
          title: '组件通信',
        },
        component: () => import('@/views/other/case/index.vue'),
      },
      {
        path: '/other/encapsulation',
        name: 'encapsulation',
        meta: {
          icon: 'Files',
          title: '组件封装',
        },
        component: () => import('@/views/other/encapsulation/index.vue'),
      },
      {
        path: '/other/crud',
        name: 'crud',
        meta: {
          icon: 'Search',
          title: '增删改查',
        },
        component: () => import('@/views/other/crud/index.vue'),
      },
    ],
  },
  {
    path: '/about',
    name: 'about',
    meta: {
      icon: 'InfoFilled',
      title: '关于',
    },
    component: () => import('@/views/about/index.vue'),
  },
]
