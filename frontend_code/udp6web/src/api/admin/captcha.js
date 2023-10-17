import Util from '@/libs/util'

// /api/catpcha/digitalCaptcha
// 获取验证码
export function getDigitalCaptcha () {
  return Util.http({
    url: `/api/catpcha/digitalCaptcha`,
    method: 'get'
  })
}

// /api/captcha/get
// 获取行为验证码图片
export function getCaptchaImg (data) {
  return Util.http({
    url: `/api/captcha/get`,
    method: 'post',
    data
  })
}

// /api/captcha/check
// 行为验证
export function checkCaptchaRes (data) {
  return Util.http({
    url: `/api/captcha/check`,
    method: 'post',
    data
  })
}
