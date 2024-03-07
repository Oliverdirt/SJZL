import Util from '@/libs/util'

// /api/cscpPostsByCriteria
// 查询岗位列表
export function getPostByParams (params) {
  return Util.http({
    url: `/api/cscpPostsByCriteria`,
    method: 'get',
    params
  })
}

// /api/cscpPosts/checkCscpPostCode
// 岗位编码校验
export function validPostCodeByData (data) {
  return Util.http({
    url: `/api/cscpPosts/checkCscpPostCode`,
    method: 'post',
    data
  })
}

// /api/cscpPosts/checkCscpPostName
// 岗位名校验
export function validPostNameByData (data) {
  return Util.http({
    url: `/api/cscpPosts/checkCscpPostName`,
    method: 'post',
    data
  })
}

// /api/cscpPost/getList url
// 根据条件查询
export function getPostByParams2 (params) {
  return Util.http({
    url: `/api/cscpPost/getList${params}`,
    method: 'get'
  })
}

// /api/cscpPost/getList
// 根据条件查询
export function getPostByParams3 (params) {
  return Util.http({
    url: `/api/cscpPost/getList`,
    method: 'get',
    params
  })
}

// /api/cscpPosts/postId
// 根据id删除
export function delPostByPostId (postId) {
  return Util.http({
    url: `/api/cscpPosts/${postId}`,
    method: 'delete'
  })
}

// /api/cscpPosts/export
// 导出
export function exportPosts (responseType) {
  return Util.http({
    url: `/api/cscpPosts/export`,
    method: 'post',
    responseType
  })
}

// /api/cscpPosts/' + row.postId
// 根据id查询
export function getPostByPostId (postId) {
  return Util.http({
    url: `/api/cscpPosts/${postId}`,
    method: 'get'
  })
}

// /api/cscpPosts
// 更新或者新增
export function updateOrAddPostByData (method, data) {
  return Util.http({
    url: `/api/cscpPosts`,
    method,
    data
  })
}
// /api/systemDics/getSystemList
// 查询系统列表
export function getPostByParams11 () {
  return Util.http({
    url: `/api/systemDics/getSystemList`,
    method: 'get',

  })
}
// /api/systemDics/getSystemList
// 查询模块列表
export function getPostByParams12 () {
  return Util.http({
    url: `/api/dic/getCscpHxDicItemsListByDicCode?dicCode=model`,
    method: 'get',

  })
}// /api/commonFunc/queryPageCommonFunctionByCondition url
// 根据条件查询
export function getPostByParams22 (params) {
  return Util.http({

    url: `/api/commonFunc/queryPageCommonFunctionByCondition${params}`,
    method: 'get'
  })
}
// /api/cscpPost/getList
// 根据条件查询
export function getPostByParams33 (params) {

  return Util.http({
    url: `/api/commonFunc/queryPageCommonFunctionByCondition?${params}`,
    method: 'get'
  })
}// /api/cscpPosts/postId
// 根据id删除
export function delPostByPostId1 (postId) {
  return Util.http({
    url: `/api/commonFunc/deleteCommonFunctionInfo/${postId}`,
    method: 'delete'
  })
}// /api/cscpPosts/' + row.postId
// 根据id编辑
export function getPostByPostId1 (postId) {
  return Util.http({
    url: `/api/commonFunc/queryOne/${postId}`,
    method: 'get'
  })
}// /api/cscpPosts
// 更新或者新增
export function updateOrAddPostByData1 (method, data) {
  return Util.http({
    url: `/api/commonFunc/insertOrUpdateCommonFunctionInfo`,
    method,
    data
  })
}
