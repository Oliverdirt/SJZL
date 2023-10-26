import Util from '@/libs/util'

// /api/cscpNotice/getList
// 根据条件获取通知列表
export function getNoticeListByParams (params) {
  return Util.http({
    url: `/api/cscpNotice/getList${params}`,
    method: 'get'
  })
}

// /api/cscpNotices/batchDel?ids=
// 批量删除
export function delNoticeListByIds (ids) {
  return Util.http({
    url: `/api/cscpNotices/batchDel?ids=${ids}`,
    method: 'delete'
  })
}

// /api/cscpNotices/${noticeId}
export function delNoticeListByNoticeId (noticeId) {
  return Util.http({
    url: `/api/cscpNotices/${noticeId}`,
    method: 'delete'
  })
}

// /api/cscpNotices/${noticeId}
export function getNoticeListByNoticeId (noticeId) {
  return Util.http({
    url: `/api/cscpNotices/${noticeId}`,
    method: 'get'
  })
}

// /api/cscpNotices
// 新增
export function addNoticeByData (data) {
  return Util.http({
    url: `/api/cscpNotices`,
    method: 'post',
    data
  })
}

// /api/cscpNotices
// 更新
export function updateNoticeByData (data) {
  return Util.http({
    url: `/api/cscpNotices`,
    method: 'put',
    data
  })
}

// /api/cscpNotice/getList
// 根据条件获取通知列表
export function getNoticeListByParams2 (params) {
  return Util.http({
    url: `/api/cscpNotice/getList`,
    method: 'get',
    params
  })
}
