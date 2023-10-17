import Util from '@/libs/util'

// /api/system/cscpLogLoginsByCriteria
// 根据条件查询登录日志
export function getLogLoginsByParams (params) {
  return Util.http({
    url: `/api/system/cscpLogLoginsByCriteria`,
    method: 'get',
    params
  })
}

// /api/system/cscpLogOperationsByCriteria
// 根据条件查询操作日志
export function getLogOperationsByParams (params) {
  return Util.http({
    url: `/api/system/cscpLogOperationsByCriteria`,
    method: 'get',
    params
  })
}
