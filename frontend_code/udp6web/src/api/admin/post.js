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
