import Util from '@/libs/util'

//  //
export function getUserDetailByParams (data) {
  return Util.http({
    url: '/api/system/cscpUserDetailsOr',
    method: 'get',
    data
  })
}

// /api/roleDeptTreeselect/${id}
// 查询角色部门树
export function getRoleDeptTreeById (id) {
  return Util.http({
    url: `/api/roleDeptTreeselect/${id}`,
    method: 'get'
  })
}

// /api/system/dataScope
// 更新角色权限
export function updateUserDataScope (data) {
  return Util.http({
    url: `/api/system/dataScope`,
    method: 'put',
    data
  })
}

// /api/cscpDepts/treeselect
// 查询用户部门树
export function getRoleDeptTree2 () {
  return Util.http({
    url: `/api/cscpDepts/treeselect`,
    method: 'get'
  })
}

// /api/cscpDepts?
// 根据条件查询部门列表
export function getDeptByParams (params) {
  return Util.http({
    url: `/api/cscpDepts${params}`,
    method: 'get'
  })
}

// /api/cscpDepts?deptId.equals=
// 根据id查询部门列表
export function getDeptByDeptId (deptId) {
  return Util.http({
    url: `/api/cscpDepts?deptId.equals=${deptId}`,
    method: 'get'
  })
}

// /api/cscpDepts/deptId
// 删除根据id
export function delDeptByDeptId (deptId) {
  return Util.http({
    url: `/api/cscpDepts/${deptId}`,
    method: 'delete'
  })
}

// /api/cscpDepts
// 添加或者新增部门
export function updateOrAddDeptByData (method, data) {
  return Util.http({
    url: `/api/cscpDepts`,
    method,
    data
  })
}

// /api/cscpDepts
// 查询部门列表
export function getDeptList () {
  return Util.http({
    url: `/api/cscpDepts`,
    method: 'get'
  })
}
