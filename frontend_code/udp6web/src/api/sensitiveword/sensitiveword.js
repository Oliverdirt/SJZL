import Util from '@/libs/util'

// 根据条件查询敏感词列表
export function listSensitiveWordByParams (query) {
  return Util.http({
    url: `/api/system/cscpSensitiveWordsByCriteria`,
    method: 'get',
    params: query
  })
}

// 根据主键id查询敏感词列表
export function querySensitiveWordById (id) {
  return Util.http({
    url: `/api/system/cscpSensitiveWords/${id}`,
    method: 'get'
  })
}

// 根据主键id删除敏感词列表
export function delSensitiveWordById (id) {
  return Util.http({
    url: `/api/system/cscpSensitiveWords/${id}`,
    method: 'delete'
  })
}

// 根据id列表批量删除
export function delSensitiveWordByIds (ids) {
  return Util.http({
    url: `/api/system/cscpSensitiveWords/delAll?ids=${ids}`,
    method: 'delete'
  })
}

// 根据条件更新敏感词列表
export function updateSensitiveWordByData (data) {
  return Util.http({
    url: `/api/system/cscpSensitiveWords`,
    method: 'put',
    data: data
  })
}

// 添加敏感词
export function addSensitiveWordByData (data) {
  return Util.http({
    url: `/api/system/cscpSensitiveWords`,
    method: 'post',
    data: data
  })
}

// 获取敏感词模板
export function getSensitiveWordTemplate (responseType) {
  return Util.http({
    url: '/api/system/downLoadSensitiveWordTemplate',
    method: 'get',
    responseType: responseType
  })
}
