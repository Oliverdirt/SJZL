import Util from '@/libs/util'

// 检查dicCode是否存在
export function checkDicCodeExists (data) {
  return Util.http({
    url: '/api/dic/cscpHxDics/checkCscpHxDicCode',
    method: 'post',
    data
  })
}

// 检查dicName是否存在
export function checkDicNameExists (data) {
  return Util.http({
    url: '/api/dic/cscpHxDics/checkCscpHxDicName',
    method: 'post',
    data
  })
}

// 根据主键id查询字典
export function queryDicListById (id) {
  return Util.http({
    url: `/api/dic/cscpHxDics/${id}`,
    method: 'get'
  })
}

// 查询所有基础字典列表
export function queryBasicDicList () {
  return Util.http({
    url: `/api/dic/cscpBasicHxDics`,
    method: 'get'
  })
}

// 根据主键id删除字典
export function delDicListById (id) {
  return Util.http({
    url: `/api/dic/cscpHxDics/${id}`,
    method: 'delete'
  })
}

// 根据id列表批量删除字典
export function delDicListByIds (ids) {
  return Util.http({
    url: `/api/dic/cscpHxDics/delDictByIds?dicIds=${ids}`,
    method: 'delete'
  })
}

// 添加字典
export function addDicListByData (data) {
  return Util.http({
    url: `/api/dic/cscpHxDics`,
    method: 'post',
    data: data
  })
}

// 更新字典
export function updateDicListByData (data) {
  return Util.http({
    url: `/api/dic/cscpHxDics`,
    method: 'put',
    data: data
  })
}

// 根据条件查询字典列表
export function getDicListbyParams (params) {
  return Util.http({
    url: '/api/dic/cscpHxDics',
    method: 'get',
    params: params
  })
}

// 检查dicItemCode是否存在
export function checkDicItemCodeExists (data) {
  return Util.http({
    url: '/api/dic/cscpHxDicItems/checkCscpHxItemCode',
    method: 'post',
    data
  })
}

// 根据id删除字典项
export function delDicItemById (id) {
  return Util.http({
    url: `/api/dic/cscpHxDicItems/${id}`,
    method: 'delete'
  })
}
// 根据id删除树型字典项
export function delTreeDicItemById (id) {
  return Util.http({
    url: `/api/dic/cscpHxDicItems/tree/${id}`,
    method: 'delete'
  })
}
// 根据id列表批量删除字典项
export function delDicItemByIds (ids) {
  return Util.http({
    url: `/api/dic/cscpHxDicItems/delAll?itemIds=${ids}`,
    method: 'delete'
  })
}

// 根据字典id查询字典项列表
export function getDicItemsbyDicId (dicId) {
  return Util.http({
    url: `/api/dic/cscpHxDicItems/query/${dicId}`,
    method: 'get'
  })
}

// 根据条件查询字典项列表
export function getDicItemsbyParams (params) {
  return Util.http({
    url: '/api/dic/cscpHxDicItems/getItems',
    method: 'get',
    params: params
  })
}

// 添加字典项
export function addDicItemByData (data) {
  return Util.http({
    url: `/api/dic/cscpHxDicItems`,
    method: 'post',
    data: data
  })
}

// 更新字典项
export function updateDicItemByData (data) {
  return Util.http({
    url: `/api/dic/cscpHxDicItems`,
    method: 'put',
    data: data
  })
}

// 根据主键id查询字典项
export function queryDicItemById (id) {
  return Util.http({
    url: `/api/dic/cscpHxDicItems?itemId.equals=${id}`,
    method: 'get'
  })
}

// 根据条件查询字典项
export function listDicItemByParams (params) {
  return Util.http({
    url: 'api/dic/cscpHxDicItems',
    method: 'get',
    params
  })
}

// 根据条件查询树状字典项
export function getDicItemByQuery (query) {
  return Util.http({
    url: `/api/dic/cscpHxDicItems/list${query}`,
    method: 'get'
  })
}

// 根据条件查询树状字典项
export function getDicItemByParams (params) {
  return Util.http({
    url: `/api/dic/cscpHxDicItems/list`,
    method: 'get',
    params
  })
}

// 根据条件查询树状字典项
export function getTreeListByDicId (dicId) {
  return Util.http({
    url: `/api/dic/cscpHxDicItems/treeselect/${dicId}`,
    method: 'get'
  })
}
// 根据条件查询字典项
export function getCscpBasicHxItemCode (params) {
  return Util.http({
    url: `/api/dic/cscpHxDicItems/getCscpBasicHxItemCode`,
    method: 'get',
    params
  })
}

// 根据字典编码查询字典数据信息
export function getDictsByDictCode (dictCode) {
  return Util.http({
    url: '/api/dic/cscpHxDicItems/getCscpBasicHxItemCode?dictCode=' + dictCode,
    method: 'get'
  })
}

// 清空字典缓存
export function clearDictCache () {
  return Util.http({
    url: '/api/dic/cscpHxDicItems/clearDictCache',
    method: 'get'
  })
}

// 重置字典缓存
export function resetDictCache () {
  return Util.http({
    url: '/api/dic/cscpHxDicItems/resetDictCache',
    method: 'get'
  })
}
