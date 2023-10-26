import Util from '@/libs/util'

// /api/validation/org
// 合法性校验
export function validOrgByParams (params) {
  return Util.http({
    url: `/api/validation/org`,
    method: 'get',
    params
  })
}

// /api/system/org/userOnlyOne/
// 机构唯一性校验
export function validUserOnlyOne (id) {
  return Util.http({
    url: `/api/system/org/userOnlyOne/${id}`,
    method: 'post'
  })
}

// /api/system/cscpOrgs/all
// 获取机构列表
export function getOrgListByParams (params) {
  return Util.http({
    url: `/api/system/cscpOrgs/all`,
    method: 'get',
    params
  })
}

// /api/system/cscpOrgs/delCscpOrg
// 根据条件删除
export function delOrgByData (data) {
  return Util.http({
    url: `/api/system/cscpOrgs/delCscpOrg`,
    method: 'delete',
    data
  })
}

// /api/system/cscpOrgs/save
// 保存机构
export function saveOrgByData (data) {
  return Util.http({
    url: `/api/system/cscpOrgs/save`,
    method: 'post',
    data
  })
}

// /api/system/cscpOrgs/updateOrg
// 更新机构
export function updateOrgByData (data) {
  return Util.http({
    url: `/api/system/cscpOrgs/save`,
    method: 'post',
    data
  })
}
