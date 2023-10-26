import Util from '@/libs/util'

// /api/system/thirdAuth/tianyi/getUnifyAccountLoginUrl
export function getUnifyAccountLoginUrlByParams (params) {
  return Util.http({
    url: `/api/system/thirdAuth/tianyi/getUnifyAccountLoginUrl`,
    method: 'get',
    params
  })
}

// /api/system/thirdAuth/tianyi/logout
// 天翼登出
export function tianyiLogout (params) {
  return Util.http({
    url: `/api/system/thirdAuth/tianyi/logout`,
    method: 'get',
    params
  })
}

// api/system/getQrCode
// 获取企业微信码
export function getQiYeWxCode (params) {
  return Util.http({
    url: `api/system/getQrCode`,
    method: 'get',
    params
  })
}
