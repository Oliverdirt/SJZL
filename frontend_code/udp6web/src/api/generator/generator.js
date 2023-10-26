import Util from '@/libs/util'
//  模板唯一性判断
export function validTemplateSuitesByParams (params) {
  return Util.http({
    url: `/api/codeTemplateSuitesList/`,
    method: 'get',
    params
  })
}

// 获取代码生成模板列表
export function getCustomerSuiteList () {
  return Util.http({
    url: `/api/codeTemplateSuitesList`,
    method: 'get'
  })
}

// 根据主键获取模板信息
export function getCustomerSuiteInfoById (id) {
  return Util.http({
    url: `/api/codeTemplateSuites/${id}`,
    method: 'get'
  })
}

// 更新/添加模板
export function updateOrAddCustomerSuiteByData (headers, data) {
  return Util.http({
    url: `/api/codeTemplateSuites/edit`,
    method: 'post',
    headers,
    data
  })
}

// 根据条件查询字典列表
export function getCustomerSuitbyParams (params) {
  return Util.http({
    url: '/api/codeTemplateSuitesByCriteria/',
    method: 'get',
    params
  })
}

// 根据主键删除模板
export function delCustomerSuiteInfoById (id) {
  return Util.http({
    url: `/api/codeTemplateSuites/${id}`,
    method: 'delete'
  })
}

// 根据id列表批量删除模板
export function delCustomerSuiteByIds (ids) {
  return Util.http({
    url: `/api/codeTemplateSuites/delAll?ids=${ids}`,
    method: 'delete'
  })
}

// 根据主键id查询数据库字段类类型
export function getColumnTypesByParams (params) {
  return Util.http({
    url: `/api/cscpHxColumnTypes`,
    method: 'get',
    params
  })
}

// 根据条件获取子表信息cscpHxAllSubFormTables
export function getSubFormTableInfoByParams (params) {
  return Util.http({
    url: `/api/cscpHxAllSubFormTables`,
    method: 'get',
    params
  })
}

// /api/cscpHxFormColums
// 根据条件获取数据表列信息
export function getFromColumnInfoByParams (params) {
  return Util.http({
    url: `/api/cscpHxFormColums`,
    method: 'get',
    params
  })
}

// /api/validation/cscpHxFormTable
// 根据条件校验表信息
export function validFromTableByParams (params) {
  return Util.http({
    url: `/api/validation/cscpHxFormTable`,
    method: 'get',
    params
  })
}

// /api/cscpHxTables 获取所有的数据库表
export function getDbTableAll () {
  return Util.http({
    url: `/api/cscpHxTables`,
    method: 'get'
  })
}

// /api/cscpHxFormTables
// 根据条件查询表单列表
export function getFormTablesByParams (params) {
  return Util.http({
    url: '/api/cscpHxFormTables',
    method: 'get',
    params: params
  })
}

// /api/cscpHxFormTablesAll/
// 根据主键id查询表单详情
export function queryFormTableInfoById (id) {
  return Util.http({
    url: `/api/cscpHxFormTablesAll/${id}`,
    method: 'get'
  })
}

// 添加数据表
export function addFormTablesByData (headers, data) {
  return Util.http({
    url: `/api/cscpHxFormTables`,
    method: 'post',
    headers,
    data
  })
}

// 更新数据表
export function updateFormTablesByData (headers, data) {
  return Util.http({
    url: `/api/cscpHxFormTables`,
    method: 'put',
    headers,
    data
  })
}

// 根据主键id删除数据表配置
export function delFormTablesById (id) {
  return Util.http({
    url: `/api/cscpHxFormTables/${id}`,
    method: 'delete'
  })
}

// 根据id列表批量删除数据表配置
export function delFormTablesByIds (ids) {
  return Util.http({
    url: `/api/cscpHxFormTables/delAll?ids=${ids}`,
    method: 'delete'
  })
}

// 数据表配置数据库同步
export function syncFormTables (id) {
  return Util.http({
    url: `/api/cscpHxsync/${id}`,
    method: 'put'
  })
}

// 生成代码预览
export function previewGenCode (tableId) {
  return Util.http({
    url: `/api/gen/preview/${tableId}`,
    method: 'get'
  })
}

// 生成代码下载
export function downloadGenCode (id, responseType) {
  return Util.http({
    url: `/api/gen/download/${id}`,
    method: 'get',
    responseType: responseType,
    withCredentials: false
  })
}
