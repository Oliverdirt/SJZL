import Util from '@/libs/util'

// /api/cscpTenant/checkTenantAccountExist
// 校验租户是否存在
export function validTenantExistByParams (params) {
  return Util.http({
    url: `/api/cscpTenant/checkTenantAccountExist`,
    method: 'get',
    params
  })
}

// /api/cscpTenants/delAll?ids=
// 根据主键列表删除
export function delTenantByIds (ids) {
  return Util.http({
    url: `/api/cscpTenants/delAll?ids=${ids}`,
    method: 'delete'
  })
}

// /api/cscpTenants/${id}
// 根据主键删除
export function delTenantById (id) {
  return Util.http({
    url: `/api/cscpTenants/${id}`,
    method: 'delete'
  })
}

// /api/cscpTenants/id
// 根据租户主键查询租户
export function getTenantById (id) {
  return Util.http({
    url: `/api/cscpTenants/${id}`,
    method: 'get'
  })
}

// /api/cscpTenants/getMenu/id
// 根据租户主键查询路由
export function getMeunByTenantId (id) {
  return Util.http({
    url: `/api/cscpTenants/getMenu/${id}`,
    method: 'get'
  })
}

// /api/system/tenant/cscpAllMenus
// 获取租户的全部路由
export function getTenantAllMeuns () {
  return Util.http({
    url: `/api/system/tenant/cscpAllMenus`,
    method: 'get'
  })
}

// /api/cscpTenants/changeStatus
// 更新租户状态
export function updateTenantStatusByData (data) {
  return Util.http({
    url: `/api/cscpTenants/changeStatus`,
    method: 'put',
    data
  })
}

// /api/updateTenant
// 更新租户信息
export function updateTenantInfoByData (data) {
  return Util.http({
    url: `/api/updateTenant`,
    method: 'put',
    data
  })
}

// /api/insertTenant
// 新增租户
export function addTenantInfoByData (data) {
  return Util.http({
    url: `/api/insertTenant`,
    method: 'post',
    data
  })
}

// /api/cscpTenants
// 根据条件查询租户信息
export function getTenantInfoByParams (params) {
  return Util.http({
    url: `/api/cscpTenants`,
    method: 'get',
    params
  })
}
