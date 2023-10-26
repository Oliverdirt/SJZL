import Util from '@/libs/util'

// 根据id查看job日志详情
export function getJoblogById (id) {
  return Util.http({
    url: `/api/sysJobLogs/${id}`,
    method: 'get'
  })
}

// 根据条件查看job日志详情
export function getJoblogByParams (params) {
  return Util.http({
    url: `/api/sysJobLogs`,
    method: 'get',
    params
  })
}

// 根据id删除job日志
export function delJoblogById (id) {
  return Util.http({
    url: `/api/sysJobLogs/${id}`,
    method: 'delete'
  })
}

// 根据id列表批量删除日志
export function delQuartzByIds (ids) {
  return Util.http({
    url: `/api/sysJobLogs/delAll?jobLogIds=${ids}`,
    method: 'delete'
  })
}

// 添加job日志
export function addJoblogByData (data) {
  return Util.http({
    url: `/api/sysJobLogs`,
    method: 'post',
    data: data
  })
}

// 更新job日志
export function updateJoblogByData (data) {
  return Util.http({
    url: `/api/sysJobLogs`,
    method: 'put',
    data: data
  })
}
