import Vue from 'vue'
// eslint-disable-next-line import/no-duplicates
import iView from 'view-design'
import Router from 'vue-router'
// eslint-disable-next-line import/no-duplicates
import { Message } from 'view-design'
import { staticRoutes, staticMenuList, staticTree } from './static-routes'
import store from '../store/store'
import Setting from '@/setting'
import cas from '@/libs/util-cas.js'
import util from '../libs/util'
import initAsyncRouter from './async-router.js'

// Uncaught (in promise) NavigationDuplicated解决方法
const originalPush = Router.prototype.push
Router.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

Vue.use(Router)

const routerConfig = {
  routes: [
    ...staticRoutes
  ]
}

const createRouter = () => new Router(routerConfig)
const router = createRouter()

export default router

router.beforeEach((to, from, next) => {
  iView.LoadingBar.start()
  getTargetComponent(to.matched[0]).then(target => {
    navigate(target, to, next)
  }).catch(err => {
    console.log(new Error('function getTargetComponent error!'))
    console.log(err)
  })
})

router.afterEach(() => {
  iView.LoadingBar.finish()
})

function resetRouter () {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

function getTargetComponent (argu) {
  return new Promise((resolve, reject) => {
    try {
      if (!argu) {
        resolve(null)
      }
      if (typeof argu.components.default === 'function') {
        argu.components.default().then(res => {
          resolve(res.default.name)
        }).catch(error => {
          reject(error)
        })
      } else {
        resolve(argu.components.default.name)
      }
    } catch (error) {
      reject(error)
    }
  })
}

async function navigate (target, to, next) {
  if ((target === 'Main' || target === null) && !store.state.app.asyncRoutesCompleted) {

    // 初始化静态菜单
    store.commit('app/setMenuList', staticMenuList || [])
    console.log('staticMenuList>>>>>>>>>', staticMenuList)
    // 保存静态路由数据原始的关系树
    store.commit('app/setRoutesTree', staticTree || [])
    // 重新获取异步菜单和权限数据
    if (Setting.isCas && handleTicketParam()) {
      try {
        let token = await getToken()
        localStorage.setItem('token', token.data.token)
        if (location.href.includes('ticket=')) {
          window.history.replaceState({}, '', cas.removeTicket())
        }
      } catch (error) {
        Message.error('认证失败，请查看后端服务')
        return
      }
    }
    initAsyncRouter().then(menus => {
      next(router.resolve(to).route.fullPath)
    }).catch(() => {
      next('/login')
    })
  } else {
    if (to.matched.length > 0) {
      if (to.path === '/login') {
        resetRouter()
      }
      next()
    } else {
      next('/404')
    }
  }
}

/**
 *
 */
async function getToken () {
  let ticket = handleTicketParam()
  if (ticket) {
    let data = new FormData()
    data.append('ticket', ticket)
    data.append('serviceUrl', cas.removeTicket())
    // eslint-disable-next-line no-return-await
    return await util.http.post('/api/system/loginByCas', data)
  }
}

function handleTicketParam () {
  const searchs = location.search.slice(1).split('&')
  for (let i = 0; i < searchs.length; i++) {
    if (searchs[i].includes('ticket=')) {
      return searchs[i].slice(7)
    }
  }
  return null
}
