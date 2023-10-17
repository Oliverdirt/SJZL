// /**
//  * 此处可直接引用自己项目封装好的 axios 配合后端联调
//  */

// import { lib } from 'crypto-js'
// // import request from './../utils/axios' // 组件内部封装的axios
// // import request from "@/api/axios.js"       //调用项目封装的axios
// import util from '../../../../../libs/util'

// // export function reqGet(data) {
// //   return axios({
// //     url: 'captcha/get',
// //     method: 'post',
// //     data
// //   })
// // }

// // 获取验证图片  以及token
// export function reqGet (data) {
//   const url = '/api/captcha/get'
//   // let result = null
//   return util.http.post(url, data)
//   // .then((res) => {
//   //   result = res.data
//   // })

//   // return result
//   // return this.$http.post(url, data)
// }
// // 滑动或者点选验证
// export function reqCheck (data) {
//   const url = '/api/captcha/check'
//   // let result
//   return util.http.post(url, data)

//   // return result
// }

// // export function reqCheck(data) {
// //   return request({
// //     url: 'captcha/check',
// //     method: 'post',
// //     data
// //   })
// // }
