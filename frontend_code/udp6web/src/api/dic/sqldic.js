import Util from '@/libs/util'

// 检查dicCode是否存在
export function checkSqlDicCodeExistsByParams (params) {
  return Util.http({
    url: '/api/cscpHxSqldicBaseinfos/validateDicExisted',
    method: 'get',
    params
  })
}

// 根据条件查询字典列表
export function getSqlDicByParams (params) {
  return Util.http({
    url: '/api/cscpHxSqldicBaseinfos',
    method: 'get',
    params: params
  })
}

// 根据主键id查询字典
export function querySqlDicById (id) {
  return Util.http({
    url: `/api/cscpHxSqldicBaseinfos/${id}`,
    method: 'get'
  })
}

// 添加字典
export function addSqlDicByData (data) {
  return Util.http({
    url: `/api/cscpHxSqldicBaseinfos`,
    method: 'post',
    data: data
  })
}

// 更新字典
export function updateSqlDicByData (data) {
  return Util.http({
    url: `/api/cscpHxSqldicBaseinfos`,
    method: 'put',
    data: data
  })
}

// 根据主键id删除字典
export function delSqlDicById (id) {
  return Util.http({
    url: `/api/cscpHxSqldicBaseinfos/${id}`,
    method: 'delete'
  })
}

// 根据主键id删除字典
export function exportSqlDic (responseType) {
  return Util.http({
    url: `/api/cscpHxSqldicBaseinfos/export`,
    method: 'post',
    responseType
  })
}

// 预览页面相关接口
// 根据主键id查询字典
export function syncSqlDicById (id) {
  return Util.http({
    url: `/api/syncdic/${id}`,
    method: 'post'
  })
}

// 根据字典id查询字典项列表
export function getSqlDicItemByDicId (dicId) {
  return Util.http({
    url: `/api/selectDicItemByDicCode/${dicId}`,
    method: 'get'
  })
}

// 根据字典id获取执行结果列表
export function getSqlDicItemByDicId2 (dicId) {
  return Util.http({
    url: `/api/selectSqlDic/${dicId}`,
    method: 'get'
  })
}

// // 添加字典
// export function addSqlDicByData (headers,data) {
//   return Util.http({
//     url: `/api/cscpHxSqldicBaseinfos`,
//     method: 'post',
//     headers,
//     data: data
//   })
// }

// // 更新字典
// export function updateSqlDicByData (headers,data) {
//   return Util.http({
//     url: `/api/cscpHxSqldicBaseinfos`,
//     method: 'put',
//     headers,
//     data: data
//   })
// }
