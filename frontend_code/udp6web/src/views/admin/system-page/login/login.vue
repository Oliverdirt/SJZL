<style lang="less">
</style>
<template>
  <div style="width: 100%;height: 100%;position: relative">
    <div class="login" @keydown.enter="handleSubmit2">
      <img style="width: 100%;height: 100%;" :src="imgData" alt="">
      <!-- <div class="burl"></div> -->
      <!--      第三方登录 天翼-->
      <div class="third-login-style" v-if="thirdType === 'tianyi'">
        <iframe width="318px" height="324px" class="iframeBox" ref="iframe" :src="thirdSrc" allowtransparency="true"
                target="_blank" scrolling="no" frameBorder="0"/>
        <span class="loginlink" @click="thirdType = ''; setLoginType()">返回>></span>
      </div>
      <!--       账号密码登录-->
      <div class="login-con" v-if="thirdType === ''">
        <div class="card" style="width: 100%;height: 100%">
          <div class="login-body">
            <!--            <img class="logo" src="./login-logo.png"/>-->
            <span style="color:#4F79E3 ;font-size: 36px;font-weight: 400;;margin-bottom: 25px">{{ systemNameData }}</span>

            <Form ref="loginForm" :model="form" :rules="rules" style="width: 368px;margin-top: 7px">
              <FormItem prop="tenantAccount" v-show="$util.sassModel">
                <i-input v-model="form.tenantAccount" placeholder="请输入租户账号" autocomplete="off"
                         style="width: 368px;height: 40px">
                  <span slot="prepend">
                    <Icon color="#FDA600" :size="16" type="md-cloud"></Icon>
                  </span>
                </i-input>
              </FormItem>
              <FormItem prop="userName">
                <i-input v-model="form.userName" placeholder="请输入用户名" autocomplete="off"
                         style="width: 368px;height: 56px">
                  <span slot="prepend">
                    <Icon color="#FDA600" :size="16" type="md-person"></Icon>
                  </span>
                </i-input>
              </FormItem>
              <FormItem prop="password">
                <i-input type="password" v-model="form.password" placeholder="请输入密码" autocomplete="off"
                         style="width: 368px;height: 56px">
                  <span slot="prepend">
                    <Icon color="#FDA600" :size="16" type="md-lock"></Icon>
                  </span>
                </i-input>
              </FormItem>
              <!-- <FormItem v-if='!Setting.captchaTypeCode'> -->
              <FormItem v-if='!this.captchaTypeCode'>
                <SecurityCode v-show="codeStatus" :value.sync="value" :biyiCaptchaKey.sync="biyiCaptchaKey" width="368"
                              ref="securityCode">
                </SecurityCode>
              </FormItem>
              <Button @click="handleSubmit2" type="primary" long :loading="isLogining"
                      style="height: 56px;background: rgba(0, 107, 255, 0.7);border-radius: 2px 2px 2px 2px;">
                {{ loginBtnText }}
              </Button>
              <FormItem v-if="Setting.isCas">
                <Select v-model="casOrNormal" transfer @on-change='goToCas'>
                  <Option value="normal">普通登录</Option>
                  <Option value="cas">cas登录</Option>
                </Select>
              </FormItem>
              <div class="sfImgBox">
                <div class="leftImg">
                  <span>其它登录方式：</span>
                  <a @click="onThirdLogin('tianyi')" title="天翼"><img style="width:26px;height: 32px"
                                                                     src="@/assets/tianyi.svg"/></a>
                  <a @click="onThirdLogin('wechat_enterprise')" title="企业微信"> <img style="width:24px;height: 28px"
                                                                                   src="@/assets/Enterprise_wechat.svg"/></a>
                </div>
                <div class="rememberMe">
                  <Checkbox v-model="rememberme" @on-change="setRememberMe">记住我</Checkbox>
                </div>
              </div>
            </Form>
          </div>
        </div>
      </div>
      <!--      第三方登录 企业微信-->
      <div class="third-login-style" v-if="thirdType === 'wechat_enterprise'" style="height:470px">
        <iframe width="318px" height="388px" class="iframeBox" ref="iframe" :src="thirdSrc" allowtransparency="true"
                target="_blank" scrolling="no" frameBorder="0"/>
        <span @click="thirdType = ''; setLoginType()" class="rebackBtn">返回>></span>
      </div>
    </div>
    <Verify @success='success' :mode="'pop'" :captchaType="this.captchaTypeCode"
            :imgSize="{ width: '400px', height: '200px' }" ref="verify"></Verify>
  </div>
</template>

<script>

import SecurityCode from './component/securityCode'
// 引入组件
import Verify from '@/views/admin/components/verifition/Verify'
import { isNull } from '@/views/lowcode/utils/util'
import Setting from '@/setting'

import {
  getUserErrorLoginCountByUserName,
  loginByUserInfo,
  getAttempts
} from '@/api/admin/user'
import {
  getSysconfigByCode
} from '@/api/sysconfig/sysconfig'
import {
  getUnifyAccountLoginUrlByParams,
  getQiYeWxCode
} from '@/api/thirdauth/third-login'
import { init } from 'events'

export default {
  props: {
    Setting: Object,
    cas: Object
  },
  data () {
    return {
      systemNameData: '统一研发框架',
      imgData: require('@/assets/background.png'),
      thirdSrc: '',
      thirdType: '',
      isLogining: false,
      loginBtnText: '登录',
      rememberme: null,
      captchaTypeCode: 'Undefined',
      form: {
        userName: 'admin',
        password: 'ctsi@123',
        tenantAccount: this.$util.sassModel ? '' : 'default'
      },
      rules: {
        userName: [{
          required: true,
          message: '账号不能为空',
          trigger: 'blur'
        }],
        tenantAccount: [{
          required: true,
          message: '租户账号不能为空',
          trigger: 'blur'
        }],
        password: [{
          required: true,
          message: '密码不能为空',
          trigger: 'blur'
        }]
      },
      value: '',
      biyiCaptchaKey: '',
      casOrNormal: 'normal',
      codeStatus: false
    }
  },
  components: {
    SecurityCode,
    Verify
  },
  mounted () {
    this.getSysConfigCaptchaTypeCode('setting.captchaTypeCode')
    this.initCodeStatus()
    this.getCaptchaAttempts()
  },
  watch: {
    'form.userName' (v1, v2) {
      if (this.Setting.captchaSwitch) {
        if (v1 == '') {
          this.codeStatus = false
        } else {
          this.getUserName(v1)
        }
      }
    }
  },
  methods: {
    // 浏览器logo及标签
    LoginQuery () {
      this.$http.get('/api/cscpPortalConfigureLogin').then((res) => {
        this.imgData = res.data.data.backgroundUrl ? res.data.data.backgroundUrl : require('@/assets/background.png')
        this.systemNameData = res.data.data.systemName ? res.data.data.systemName : '统一研发框架'
      })
    },
    getCaptchaAttempts () {
      if (this.Setting.captchaSwitch) {
        getAttempts()
          .then(response => {
            if (response.data.captchaAttempts <= 0) {
              this.codeStatus = true
            }
          })
      }
    },
    getUserName (e) {
      if (this.Setting.captchaSwitch) {
        getUserErrorLoginCountByUserName(e)
          .then((response) => {
            console.log(response)
          })
          .catch((error) => {
            if (error.response) {
              switch (error.response.status) {
                case 401:
                  if (error.response.data.attempts != 0) {
                    if (error.response.data.attempts >= error.response.data.captchaAttempts - 1) {
                      this.codeStatus = true
                    }
                  } else {
                    if (error.response.data.attempts < error.response.data.captchaAttempts - 1) {
                      this.codeStatus = false
                    }
                  }
                  break
                default:
                  break
              }
            }
          })
      }
    },
    initCodeStatus () {
      if (!this.Setting.captchaSwitch) {
        this.codeStatus = true
      } else {
        this.codeStatus = false
      }
    },
    handleSubmit2 () {
      // alert("sssss")
      if (this.captchaTypeCode) {
        // alert('sssss')
        this.useVerify()
      } else {
        this.handleSubmit()
      }
    },
    getSysConfigCaptchaTypeCode (code) {
      // let url = '/api/getCscpSysConfigByConfigKey/' + `setting.captchaTypeCode`
      // let url = '/api/getCscpSysConfigByConfigKey/' + code
      getSysconfigByCode(code)
        // this.$util.http.get(url)
        .then(res => {
          if (res.data.code == 200) {
            this.captchaTypeCode = typeof (res.data.data) === 'undefined' ? '' : res.data.data.configValue
            // if (this.captchaTypeCode == this.Setting.captchaTypeCode1 || this.captchaTypeCode == this.Setting.captchaTypeCode2 ) {

            // }
          }
        }).catch(err => {
        this.captchaTypeCode = this.Setting.captchaTypeCode
        console.log(new Error(err))
      })
    },
    onThirdLogin (type) {
      // let url = ''
      let domain = window.location.origin
      if (type === 'tianyi') {
        // url = '/api/system/thirdAuth/tianyi/getUnifyAccountLoginUrl'
        getUnifyAccountLoginUrlByParams({ 'state': domain })
          // this.$util.http.get(url, { params: { 'state': domain } })
          .then(res => {
            this.thirdSrc = res.data
            this.thirdType = 'tianyi'
            this.setLoginType()
          }).catch(err => {
          console.log(new Error(err))
        })
      } else if (type === 'wechat_enterprise') { // 企业微信
        // url = 'api/system/getQrCode'
        getQiYeWxCode()
          // this.$util.http.get(url)
          .then(res => {
            this.thirdType = 'wechat_enterprise'
            this.setLoginType()
            let netData = res.data.data
            let codeUrl = `https://open.work.weixin.qq.com/wwopen/sso/qrConnect?appid=${netData.appid}&agentid=${netData.agentid}&redirect_uri=${netData.redirectUri}&state=${domain}`
            this.thirdSrc = codeUrl
          }).catch(err => {
          console.log(new Error(err))
        })
      }
    },
    setLoginType () {
      window.localStorage.setItem('thirdType', this.thirdType)
    },
    success (params) {
      // params 返回的二次验证参数, 和登录参数一起回传给登录接口，方便后台进行二次验证
      // captchaVerification: "0EgmmvFSvxXkL5WuuZ2WPxeAlRdeSEk4zVxVqPq+1HRUsM4a1rz+DbYD+42DEWl3fIrPfYJhK+fe6IU0G6ZPM/oJHOk+m5BsUombAcgcI7GzagFxd6cwPUBPmnLaJ+Eh"
      // captchaVerification: "A6RH3i1Y8db+GQtz82YTmaix+7jgo8KBZ8EkRjmyz832vVc/nalGSiT7p6CRDCrFmyWLzqnkJGfIqFbIFK8Aanw2Wj3KZT238J7ivryX+pE="
      if (params.captchaVerification) {
        this.handleSubmit(params.captchaVerification)
      }
      // console.log('========>', params)
    },
    useVerify () {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          if (this.codeStatus) {
            this.$refs.verify.show()
          } else {
            this.handleSubmit()
          }
        }
      })
    },
    setRememberMe (value) {
      if (value) {
        localStorage['rememberme'] = 1
      } else {
        localStorage['rememberme'] = 0
      }
    },
    handleSubmit (param) {
      this.$refs.loginForm.validate((valid) => {
        // 默认字符验证码
        if (!this.captchaTypeCode) {
          var reg = new RegExp('[\\u4E00-\\u9FFF]+', 'g')
          if (reg.test(this.value)) {
            this.$Message.error({
              content: `登录失败，验证码不允许存在中文！`
            })
            return
          }
        }
        if (valid) {
          this.isShowModal = true
          this.isLogining = true
          let msg = this.$Message.loading({
            content: '正在登录中',
            duration: 0
          })
          var pswd = this.$util.encryptPassword
            ? this.$util.encryptPassword(this.form.password)
            : this.form.password

          loginByUserInfo({
            biyiCaptchaKey: this.biyiCaptchaKey,
            biyiCaptcha: JSON.stringify({
              code: this.value
            }),
            codeStatus: this.codeStatus,
            captchaVerification: (typeof param) === 'string' ? param : ''
          }, {
            username: this.form.userName,
            password: pswd,
            tenantAccount: this.form.tenantAccount,
            rememberme: localStorage['rememberme']
          }).then((response) => {
              msg()
              this.isLogining = false
              console.log(response, 'respdonse')
              if (
                this.$route.meta.validatePaswd &&
                response.data.passwordStatus
              ) {
                this.$Message.warning({
                  content:
                    response.data.passwordStatus === 1
                      ? '初始密码强度较弱，请修改后再重新登录！'
                      : '密码已使用3个月，请修改后再重新登录！'
                })
                this.$byStoreSet('user-info', this.form)
                this.$router.push({ name: 'update-password' })
              } else {
                localStorage.token = response.data.token
                this.$Message.success({
                  content: '登录成功！',
                  onClose: () => {
                    this.$router.replace('/home')
                  }
                })
              }
            })
            .catch((error) => {
              msg()
              if (!this.captchaTypeCode) {
                this.$refs.securityCode.init()
              }
              this.isLogining = false
              if (error.response) {
                switch (error.response.status) {
                  case 401:
                    if (error.response.data.attempts) {
                      this.$Message.error(
                        `登录失败，您还有 ${error.response.data.attempts} 尝试机会！`
                      )
                      if (error.response.data.badPasswordAttempts - error.response.data.attempts >= error.response.data.captchaAttempts) {
                        this.codeStatus = true
                      }
                    } else {
                      const locktime = this.formatLockedTime(
                        error.response.data.lockoutTime
                      )
                      this.$Message.error({
                        content: `登录失败，您的账号已被锁定，请在<span style="font-weight: bold; color: #2d8cf0;">${locktime}</span>后重试！`
                      })
                    }
                    break
                  default:
                    // this.$Message.error(
                    //   `登录失败，${error.response.data.message}！`
                    // )
                    this.$Message.error(
                      `登录失败，用户名或密码错误！`
                    )
                    break
                }
              }
            })
        }
      })
    },
    formatLockedTime (time) {
      let str = ''
      const d = parseInt(time / (3600 * 24))
      let h = parseInt((time % (3600 * 24)) / 3600)
      let m = parseInt((time % 3600) / 60)
      let s = (time % 3600) % 60
      if (d > 0) {
        str = `${str}${d}天`
      }
      if (h > 0) {
        str = `${str}${h}小时`
      }
      if (m > 0) {
        str = `${str}${m}分钟`
      }
      if (s > 0) {
        str = `${str}${s}秒`
      }
      return str
    },
    goToCas (value) {
      if (value === 'cas') {
        this.cas.login()
      }
    }
  },
  created () {
    this.LoginQuery()
    this.$store.commit('user/logout')
    let dom = document.querySelector('.biyi-style')
    if (dom) {
      dom.remove()
    }
    localStorage.removeItem('biyi-theme')
    if (!localStorage['rememberme']) {
      localStorage['rememberme'] = 0
    }
    this.rememberme = !(localStorage['rememberme'] === '0')
  }
}
</script>
<style>
html,
body {
  width: 100%;
  height: 100%;
  padding: 0;
  margin: 0;
}
</style>
<style lang="less" scoped>
.login {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  position: relative;
  align-items: center;
  text-align: left;
  background-attachment: fixed;
  display: flex;
  justify-content: flex-end;

  .loginlink {
    color: #666;
    position: absolute;
    font-size: 12px;
    bottom: 51px;
    right: 35px;
    cursor: pointer;
    color: #409EFF;
  }

  .third-login-style {
    background-color: #fff;
    position: absolute;
    right: 160px;
    top: 50%;
    transform: translateY(-50%);
    width: 378px;
    height: 390px;
    z-index: 10;

    .rebackBtn {
      color: #409EFF;
      font-size: 13px;
      position: absolute;
      bottom: 10px;
      right: 15px;
      cursor: pointer;
    }
  }

  .iframeBox {
    margin-left: 30px;
    margin-top: 36px;
  }

  &-con {
    position: absolute;
    right: 160px;
    top: 50%;
    transform: translateY(-50%);
    width: 448px;
    height: 510px;
    z-index: 10;
  }

  &-body {
    width: 448px;
    height: 510px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background-color:rgba(217,217,217, 0.1);
    border-radius: 5px 5px 5px 5px;
    opacity: 1;
    border: 2px solid rgba(255,255,255,0.5);

    .logo {
      width: 294px;
      height: 72px;
    }
    .ivu-form-item{
      margin-bottom:14px
    }
  }
}

.card {
  width: 448px;
  height: 510px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 16px 16px 16px 16px;
  opacity: 1;
}

::v-deep .ivu-input-group-prepend {
  width: 40px;
}

::v-deep .ivu-input {
  background: rgba(255, 255, 255, 0.85);
  border-radius: 3px 3px 3px 3px;
  opacity: 1;
  border: 1px solid #d9d9d9;
  height: 100%;
}

.sfImgBox {
  display: flex;
  align-items: center;
  justify-content: space-between;

  .leftImg {
    height: 80px;
    display: flex;
    align-items: center;

    > span {
      font-size: 13px;
      color: #000;
    }

    a {
      margin: 0 5px;
      display: flex;
      align-items: center;
      justify-content: center;
      height: 100%;

      img {
        width: 18px;
        height: 18px;
      }
    }
  }

}

.ivu-checkbox-wrapper {
  margin: 0 !important;
}
</style>
