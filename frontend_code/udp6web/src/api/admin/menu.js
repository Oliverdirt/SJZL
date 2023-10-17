import Util from '@/libs/util'

// /api/system/menu/addMenu
// 添加或者更新路由
export function addOrUpdateMenuByData (data) {
  return Util.http({
    url: `/api/system/menu/addMenu`,
    method: 'put',
    data
  })
}

// /api/system/cscpMenuss/${id}
// 根据主键获取菜单路由详情
export function getMenuById (id) {
  return Util.http({
    url: `/api/system/cscpMenuss/${id}`,
    method: 'get'
  })
}

// /api/system/cscpAllMenus
// 根据条件获取菜单路由详情
export function getMenuByParams (params) {
  return Util.http({
    url: `/api/system/cscpAllMenus`,
    method: 'get',
    params
  })
}

// /system/menu/${this.deleteId}
// 根据主键删除菜单路由
export function delMenuById (id) {
  return Util.http({
    url: `/system/menu/${id}`,
    method: 'delete'
  })
}

// /api/system/menu/deleteMenu/${id}
// 根据主键删除菜单路由,和上面逻辑一致
export function delMenussById (id) {
  return Util.http({
    url: `/api/system/menu/deleteMenu/${id}`,
    method: 'delete'
  })
}

// /api/system/menu/getMenu
// 根据父id查询菜单路由
export function getMenussByParentId (params) {
  return Util.http({
    url: `/api/system/menu/getMenu`,
    method: 'get',
    params
  })
}

// /api/system/menu/updateMenu
// 更新拖拽后菜单
export function updateMenuByData (data) {
  return Util.http({
    url: `/api/system/menu/updateMenu`,
    method: 'put',
    data
  })
}
