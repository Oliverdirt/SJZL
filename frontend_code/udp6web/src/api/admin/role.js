import Util from '@/libs/util'

// /api/system/cscpRolessByCriteria
// 角色列表查询
export function getRoleListByParams (params) {
  return Util.http({
    url: `/api/system/cscpRolessByCriteria`,
    method: 'get',
    params
  })
}

// /api/validation/role
// 角色名校验
export function validRoleByParams (params) {
  return Util.http({
    url: `/api/validation/role`,
    method: 'get',
    params
  })
}

// /api/system/role/findUsers/${id}
// 根据角色id查询用户列表
export function getUsersByRoleId (id) {
  return Util.http({
    url: `/api/system/role/findUsers/${id}`,
    method: 'get'
  })
}

// /api/system/cscpRoless/${roleId}
// 根据角色id查询角色详情
export function getRoleInfoByRoleId (roleId) {
  return Util.http({
    url: `/api/system/cscpRoless/${roleId}`,
    method: 'get'
  })
}

// /api/system/getMenuItemBean
// 根据角色id查询路由列表
export function getMenuListByRoleId (data) {
  return Util.http({
    url: `/api/system/getMenuItemBean`,
    method: 'post',
    data
  })
}

// /api/system/menu/queryByRoleId
export function getMenuListByRoles (roleId) {
  return Util.http({
    url: `/api/system/menu/queryByRoleId/${roleId}`,
    method: 'post'
  })
}

// /api/system/cscpRoless
// 新增或更新角色
export function updateOrAddRoles (method, data) {
  return Util.http({
    url: `/api/system/cscpRoless`,
    method,
    data
  })
}

// /api/system/role/saveUsers/${roleId}
// 批量保存用户角色
export function updateUserRoles (roleId, data) {
  return Util.http({
    url: `/api/system/role/saveUsers/${roleId}`,
    method: 'post',
    data
  })
}

// /api/system/menu/save
// 保存角色菜单
export function saveRoleMeunsByData (data) {
  return Util.http({
    url: `/api/system/menu/save`,
    method: 'post',
    data
  })
}

// /api/system/cscpRoleUsers/${id}
// 根据角色id查询用户，判断改角色是否绑定用户
export function validRoleUser (id) {
  return Util.http({
    url: `/api/system/cscpRoleUsers/${id}`,
    method: 'get'
  })
}

// /api/system/cscpRoless/${id}
// 根据id删除角色
export function delRoleById (id) {
  return Util.http({
    url: `/api/system/cscpRoless/${id}`,
    method: 'delete'
  })
}
