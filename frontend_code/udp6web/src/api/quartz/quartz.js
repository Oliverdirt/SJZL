import Util from '@/libs/util'

// 根据主键id查询定时任务
export function queryQuartzById (id) {
  return Util.http({
    url: `/api/jobs/${id}`,
    method: 'get'
  })
}

// 根据条件查询定时任务列表
export function listQuartzByParams (params) {
  return Util.http({
    url: '/api/jobs',
    method: 'get',
    params
  })
}

// 添加定时任务
export function addQuartzByData (data) {
  return Util.http({
    url: `/api/jobs`,
    method: 'post',
    data: data
  })
}

// 更新定时任务
export function updateQuartzByData (data) {
  return Util.http({
    url: `/api/jobs`,
    method: 'put',
    data: data
  })
}

// 更新定时任务状态
export function updateQuartzStatus (data) {
  return Util.http({
    url: `/api/jobs/changeStatus`,
    method: 'put',
    data: data
  })
}

// 执行定时任务
export function execQuartzs (data) {
  return Util.http({
    url: `/api/jobs/run`,
    method: 'put',
    data: data
  })
}

// 根据id删除定时任务
export function delQuartzById (id) {
  return Util.http({
    url: `/api/jobs?jobIds=${id}`,
    method: 'delete'
  })
}

// 导出定时任务列表
export function exportQuartzInfo (data, responseType) {
  return Util.http({
    url: `/api/jobs/export`,
    method: 'post',
    data: data,
    responseType: responseType
  })
}
