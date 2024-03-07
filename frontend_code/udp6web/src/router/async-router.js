/**
 * 实现biyi异步的路由数据加载，数据分为两步进行处理：
 * 1.属于biyi-admin组件的vue文件对应的数据的component属性不需要带值，这里采用组件引入的方式引用
 * 2.属于用户自定义的vue文件（不属于biyi-admin的文件）对应的数据的component属性必须带该问建的相对src的绝对路径
 * 例如：component: 'views/home/home.vue'，实际对应的文件地址为'src/view/home/home.vue，即该vue必须放在src文件夹下某个路径
 */
import store from '@/store/store'
// import util from '@/libs/util'
import {requestMethod} from '@/libs/util'

const routeMap = {}

async function initAsyncRouter() {
  try {
    const menus = await getDynamicMenus()
    const asyncMenuRes = initMenuTree(menus)
    console.log('asyncMenuRes', asyncMenuRes)
    // 添加动态获取的菜单
    store.commit('app/setMenuList', asyncMenuRes.menuList || [])
    // 添加动态路由
    store.commit('app/updateAppRouter', asyncMenuRes.menuRoutes || [])
    return new Promise((resolve, reject) => {
      resolve(menus)
    })
  } catch (error) {
    return new Promise((resolve, reject) => {
      reject(error)
    })
  }
}

function getDynamicMenus() {
  return new Promise((resolve, reject) => {
    requestMethod.get('/api/system/cscpMenus').then(response => {
      console.log(response,'opop')
      if (response && response.data) {
        resolve(response.data)
      } else {
        reject(new Error('getDynamicMenus error!'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

function initMenuTree(menus) {
  menus = cleanData(menus)
  // map映射
  const menusMap = new Map()
  for (let i = 0; i < menus.length; i++) {
    menusMap.set(menus[i].id, true)
  }
  const tree = []
  let leaves = []
  for (let i = 0; i < menus.length; i++) {
    if (menusMap.has(menus[i].parentId)) {
      // 叶节点
      leaves.push(menus[i])
    } else {
      // 根节点
      tree.push(menus[i])
    }
  }
  buildTree(tree, leaves)
  // 保存路由数据原始的关系树
  // store.commit('app/setRoutesTree', tree || [])
  store.commit('app/setRoutesTree', tree)
  const menuList = getMenuList(tree)
  const menuRoutes = getMenuRoutes(tree)
  return {
    menuList,
    menuRoutes
  }
}

// 保存按钮权限 & 清理无用数据
function cleanData(menus) {
  const menusCleaned = []
  const btnPermissionCode = []
  for (let i = 0; i < menus.length; i++) {
    if (menus[i].permissionCode) {
      btnPermissionCode.push(menus[i].permissionCode)
    }
    if (menus[i].type !== 'button') {
      menusCleaned.push(menus[i])
    }
  }
  // 保存按钮权限码
  store.commit('app/setBtnLimitedCodes', btnPermissionCode)
  return menusCleaned
}

function buildTree(tree, leaves) {
  leaves = deleteUselessElement(leaves)
  while (leaves.length > 0) {
    leaves.forEach((leaf, index) => {
      if (leaf) {
        addLeaf(tree, leaf, index, leaves)
      }
    })
    leaves = deleteUselessElement(leaves)
  }
}

function addLeaf(parents, leaf, index, leaves) {
  for (const parent of parents) {
    if (parent.id === leaf.parentId) {
      if (parent.children && Array.isArray(parent.children)) {
        parent.children.push(leaf)
      } else {
        parent.children = []
        parent.children.push(leaf)
      }
      // 将leaf标记为null
      leaves[index] = null
      break
    } else {
      if (parent.children && Array.isArray(parent.children)) {
        addLeaf(parent.children, leaf, index, leaves)
      }
    }
  }
}

function deleteUselessElement(array) {
  let temp = []
  array.forEach(ele => {
    if (ele) {
      temp.push(ele)
    }
  })
  return temp
}

function getMenuList(menus) {
  const tempList = []
  menus.forEach(ele => {
    if (ele.type && ele.type.startsWith('menu')) {
      const tempObj = {}
      if (ele.children) {
        tempObj.children = getMenuList(ele.children)
      }
      tempObj.name = ele.name
      tempObj.icon = ele.icon || 'ios-cube-outline'
      tempObj.title = ele.title
      tempObj.orderby = ele.orderby
      tempList.push(tempObj)
    }
  })
  tempList.sort((a, b) => {
    if (a.orderby > b.orderby) return 1
    if (a.orderby < b.orderby) return -1
    return 0
  })
  return tempList
}

function getMenuRoutes(tree) {
  const menuRoutes = buildMenuRoutesStep1(tree)
  // 将三级路由结构改为二级
  buildMenuRoutesStep2(menuRoutes)
  // 删除无用的路由数据
  buildMenuRoutesStep3(menuRoutes)
  return menuRoutes
}

function buildMenuRoutesStep1(menus) {
  const tempRoutes = []
  menus.forEach(ele => {
    const tempObj = {}
    if (ele.children) {
      tempObj.children = buildMenuRoutesStep1(ele.children)
    }
    tempObj.parentId = ele.parentId
    tempObj.path = ele.url
    tempObj.name = ele.name
    tempObj.meta = {
      title: ele.title
    }
    if (ele.url) {
      if (routeMap[ele.name]) {
        tempObj.component = routeMap[ele.name]
      } else if (typeof ele.component === 'string' && ele.component.length > 0) {
        // tempObj.component = () => import(`@/${ele.component}`)
        tempObj.component = () => import('@/' + ele.component)
      }
    } else {
      tempObj.component = ele.component
    }
    tempRoutes.push(tempObj)
  })
  return tempRoutes
}

function buildMenuRoutesStep2(routes, root) {
  routes.forEach(ele => {
    if (ele.children) {
      root = ele.parentId === '0' ? ele : root
      buildMenuRoutesStep2(ele.children, root)
      if (ele.parentId !== '0') {
        if (!ele.component) {
          ele.children.forEach(item => {
            // 将二级菜单的信息放入子路由中
            item.meta.parentName = ele.name
          })
        }
        root.children = root.children.concat(ele.children)
        delete ele.children
      }
    }
  })
}

function buildMenuRoutesStep3(routes) {
  const temp = []
  routes.forEach((ele, index) => {
    if (ele.children) {
      buildMenuRoutesStep3(ele.children)
    }
    if (!ele.component || !ele.path) {
      routes[index] = undefined
    } else {
      temp.push(routes[index])
    }
  })
  routes.splice(0, routes.length)
  temp.forEach(el => {
    routes.push(el)
  })
}

export default initAsyncRouter
