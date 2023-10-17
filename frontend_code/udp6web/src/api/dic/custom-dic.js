import Util from '@/libs/util'

// 添加自定义字典
export function addCustomDicByData (data) {
  return Util.http({
    url: `/api/cscpHxDicCustoms`,
    method: 'post',
    data: data
  })
}

// 更新自定义字典
export function updateCustomDicByData (data) {
  return Util.http({
    url: `/api/cscpHxDicCustoms`,
    method: 'put',
    data: data
  })
}

// 根据主键id查询自定义字典
export function queryCustomDicById (dicId) {
  return Util.http({
    url: `/api/cscpHxDicCustoms/${dicId}`,
    method: 'get'
  })
}

// 根据条件查询自定义字典列表
export function getCustomDicByParams (params) {
  return Util.http({
    url: '/api/cscpHxDicCustoms',
    method: 'get',
    params: params
  })
}

// 根据id删除自定义字典
export function delCustomDicById (dicId) {
  return Util.http({
    url: `/api/cscpHxDicCustoms/${dicId}`,
    method: 'delete'
  })
}

// 自定义字典项接口
// 根据主键id删除字典项
export function delCustomDicItemById (itemId) {
  return Util.http({
    url: `/api/dic/cscpHxDicItems/${itemId}`,
    method: 'delete'
  })
}
