import Util from '@/libs/util'

// 查询敏感词类型列表
export function listSensitiveWordType (query) {
  return Util.http({
    url: '/api/system/cscpSensitiveWordTypesList',
    method: 'get',
    params: query
  })
}

// 根据条件查询敏感词类型列表(分页)
export function listSensitiveWordTypeByParams (query) {
  return Util.http({
    url: `/api/system/cscpSensitiveWordTypesByCriteria`,
    method: 'get',
    params: query
  })
}

// 根据主键id查询敏感词类型列表
export function querySensitiveWordTypeById (id) {
  return Util.http({
    url: `/api/system/cscpSensitiveWordTypes/${id}`,
    method: 'get'
  })
}

// 根据主键id删除敏感词类型
export function delSensitiveWordTypeById (id) {
  return Util.http({
    url: `/api/system/cscpSensitiveWordTypes/${id}`,
    method: 'delete'
  })
}

// 根据id列表批量删除敏感词类型
export function delSensitiveWordTypeByIds (ids) {
  return Util.http({
    url: `/api/system/cscpSensitiveWordTypes/delAll?ids=${ids}`,
    method: 'delete'
  })
}

// 根据条件更新敏感词列表
export function updateSensitiveWordTypeByData (data) {
  return Util.http({
    url: `/api/system/cscpSensitiveWordTypes`,
    method: 'put',
    data: data
  })
}

// 添加敏感词列表
export function addSensitiveWordTypeByData (data) {
  return Util.http({
    url: `/api/system/cscpSensitiveWordTypes`,
    method: 'post',
    data: data
  })
}
