import Util from '@/libs/util'

// /api/system/cscpUserPassword
// 更新用户密码
export function updateUserPasswordByData (data) {
  return Util.http({
    url: `/api/system/cscpUserPassword`,
    method: 'put',
    data
  })
}

// /api/system/cscpUserPasswordRule
// 获取用户密码规则
export function getUserPasswordRule () {
  return Util.http({
    url: `/api/system/cscpUserPasswordRule`,
    method: 'get'
  })
}

// /api/system/cscpUserDetailsOr
// 根据条件查询用户详情
export function getUserDetailByParams (params) {
  return Util.http({
    url: `/api/system/cscpUserDetailsOr`,
    method: 'get',
    params
  })
}
// /api/system/deblockingAccount
// 解锁用户登录锁定
export function deblockingAccount (params) {
  return Util.http({
    url: `/api/system/deblockingAccount/${params}`,
    method: 'get',
  })
}

// /api/system/cscpUserDetailsOrTwo
export function getUserDetailOrTwoByParams (params) {
  return Util.http({
    url: `/api/system/cscpUserDetailsOrTwo`,
    method: 'get',
    params
  })
}

// /api/system/cscpUsers/${id}
// 根据id删除
export function delUserById (id) {
  return Util.http({
    url: `/api/system/cscpUsers/${id}`,
    method: 'delete'
  })
}

// /api/system/updateCscpUserPwd/0
// 更新用户密码
export function updateCscpUserPwdByType (type, data) {
  return Util.http({
    url: `/api/system/updateCscpUserPwd/${type}`,
    method: 'put',
    data
  })
}

// /api/system/cscpUserExistByUsername
// 检验用户名
export function validUserExistByUserName (params) {
  return Util.http({
    url: `/api/system/cscpUserExistByUsername`,
    method: 'get',
    params
  })
}

// /api/system/cscpUserDetailsByUserId
//
export function getUserDetailsByUserId (params) {
  return Util.http({
    url: `/api/system/cscpUserDetailsByUserId`,
    method: 'get',
    params
  })
}

// /api/system/cscpUserDetails/${type}
// 更新用户
export function updateUserDetailsByType (type, data) {
  return Util.http({
    url: `/api/system/cscpUserDetails/${type}`,
    method: 'put',
    data
  })
}

// /api/system/cscpUserDetails
// 新增用户
export function addUserDetailsByData (data) {
  return Util.http({
    url: `/api/system/cscpUserDetails`,
    method: 'post',
    data
  })
}

// // /api/system/getCountKeyValue/' + e
// 获取用户错误次数
export function getUserErrorLoginCountByUserName (userName) {
  return Util.http({
    url: `/api/system/getCountKeyValue/${userName}`,
    method: 'get'
  })
}

// /api/system/getCaptchaAttempts
// 获取密码错误次数设定
export function getAttempts () {
  return Util.http({
    url: `/api/system/getCaptchaAttempts`,
    method: 'get'
  })
}

// /api/system/login
// 用户登录
export function loginByUserInfo (headers, data) {
  return Util.http({
    url: `/api/system/login`,
    method: 'post',
    headers,
    data
  })
}

// /api/system/cscpCurrentUserDetails
// 获取当前用户详情
export function getCurrentUserDetails () {
  return Util.http({
    url: `/api/system/cscpCurrentUserDetails`,
    method: 'get'
  })
}

// /api/system/refreshToken?rememberme=
// 刷新token
export function refreshTokenByRememberme (rememberme) {
  return Util.http({
    url: `/api/system/refreshToken?rememberme=${rememberme}`,
    method: 'get'
  })
}
