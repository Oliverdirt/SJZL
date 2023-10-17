import Util from '@/libs/util'

// 查询最近10条系统监控数据
export function getSysMonitorMsgUp10 (query) {
  return Util.http({
    url: '/api/monitor/server/getMsgUp10',
    method: 'get',
    params: query
  })
}

// 根据时间区间查询系统监控数据
export function getSysMonitorMsgByDateTime (query) {
  return Util.http({
    url: `/api/monitor/server/getMsg?${query}`,
    method: 'get'
  })
}
