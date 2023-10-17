import Util from '@/libs/util'

// 查询系统参数列表
export function listSysconfig (query) {
  return Util.http({
    url: '/api/cscpSysConfigs/',
    method: 'get',
    params: query
  })
}

// 根据主键id查询系统参数
export function querySysconfigById (id) {
  return Util.http({
    url: `/api/cscpSysConfigs/${id}`,
    method: 'get'
  })
}

// 根据主键id删除系统参数
export function delSysconfigById (id) {
  return Util.http({
    url: `/api/cscpSysConfigs/${id}`,
    method: 'delete'
  })
}

// 根据id列表批量删除系统参数
export function delSysconfigByIds (ids) {
  return Util.http({
    url: `/api/cscpSysConfigs/delAll?configIds=${ids}`,
    method: 'delete'
  })
}

// 根据条件更新系统参数
export function updateSysconfigByData (data) {
  return Util.http({
    url: `/api/cscpSysConfigs`,
    method: 'put',
    data: data
  })
}

// 添加系统参数配置
export function addSysconfigByData (data) {
  return Util.http({
    url: `/api/cscpSysConfigs`,
    method: 'post',
    data: data
  })
}

// /api/getCscpSysConfigByConfigKey/${code}
// 根据code获取系统参数
export function getSysconfigByCode (code) {
  return Util.http({
    url: `/api/getCscpSysConfigByConfigKey/${code}`,
    method: 'get'
  })
}
