import util from '../libs/util'
import { login, updatePaswd } from '../views/admin'
import Setting from '@/setting'
import cas from '@/libs/util-cas'

/**
 * 配置静态路由
 * 分为两种路由：
 * 1.路由出口在App.vue
 * 2.路由出口在main.vue
 * 注意：为避免不必要的错误，每个路由需设置name属性！！！
 * 注意：为避免不必要的错误，每个路由需设置name属性！！！
 * 注意：为避免不必要的错误，每个路由需设置name属性！！！
 */

// 1.路由出口在App.vue
const singleRoutes = [
  {
    // 设置初始页面
    path: '',
    redirect: '/home'
  },
  {
    path: '/login',
    name: 'login',
    component: login,
    props: { Setting: Setting, cas: cas },
    meta: {
      // 密码规则校验开关
      validatePaswd: true
    }
  },
  {
    path: '/update-password',
    name: 'update-password',
    component: updatePaswd
  },
  {
    path: '/thirdAuthLogin',
    name: '第三方登录',
    component: () => import('../views/admin/system-page/login/thirdAuthLogin.vue')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/error-pages/404.vue')
  },
  {
    path: '/errPage',
    name: 'errPage',
    component: () => import('../views/error-pages/errPage.vue')
  },
  {
    path: '/formDesigner',
    name: 'formDesigner',
    component: () => import('../views/lowcode/vformPro/components/form-designer/index')
  },
  {
    path: '/addPage',
    name: 'addPage',
    component: () => import('../views/lowcode/tableDesigner/pageManage/addPage.vue')
  },
  {
    path: '/formDesign',
    name: 'formDesign',
    component: () => import('../views/lowcode/customize/formDesign.vue')
  },
  {
    path: '/editPage',
    name: 'editPage',
    component: () => import('../views/lowcode/tableDesigner/pageManage/editPage.vue')
  },
  {
    path: '/self-edit',
    component: () => import('../views/main/main.vue'),
    children: [
      {
        path: '/self-edit',
        name: 'self-edit',
        meta: {
          title: '个人中心',
        },
        title: '个人中心',
        type: 'menu',
        component: () => import('../views/admin/system-page/own-space/self-edit.vue')
      }
    ]
  }
]

/**
 * 路由出口在main.vue，包括了菜单项和非菜单页面
 * 若该页面属于菜单页面，需要添加type: 'menu'属性
 */
const staticTree = [
  {
    path: '/main',
    name: 'main-home',
    title: '首页',
    icon: 'ios-paper',
    type: 'menu',
    component: () => import('../views/main/main.vue'),
    children: [
      {
        path: '/home',
        name: 'home',
        title: '首页',
        icon: 'ios-paper',
        component: () => import('../views/form-demo/index.vue'),
        children: [
          {
            path: '/form-group',
            name: 'form-group',
            title: '表单控件',
            icon: 'ios-paper',
            type: '',
            component: () => import('../views/form-demo/form-group/index.vue')
          },
          {
            path: '/multi-column',
            name: 'multi-column',
            title: '基础表单',
            icon: 'ios-paper',
            type: '',
            component: () => import('../views/form-demo/multi-column/index.vue')
          },
          {
            path: '/table-demo',
            name: 'table-demo',
            title: '表格',
            icon: 'ios-paper',
            type: '',
            component: () => import('../views/form-demo/table-demo/index.vue')
          },
        ]
      },
    ]
  }
]
const staticMenuList = util.getMenuList(staticTree)
const staticMainRoutes = util.getMenuRoutes(staticTree)

// 汇总所有的路由
const staticRoutes = [...staticMainRoutes, ...singleRoutes]

export { staticRoutes, staticMenuList, staticTree }
