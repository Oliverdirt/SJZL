<template>
  <div id="midLoginWin">
    <Spin fix>登录中...</Spin>
    <!--    <div class="load_title">正在登录江苏电信比翼Pro，请耐心等待</div>-->
  </div>
</template>

<script>
export default {
  name: 'thirdauth',
  data () {
    return {
      env: {
        thirdApp: true,
        wxWork: true
      }
    }
  },
  created () {
    this.checkEnv()
    this.doThirdLogin()
  },
  methods: {
    /** 检测当前的环境 */
    checkEnv () {
      // 判断当时是否是企业微信环境
      // if (/wxwork/i.test(navigator.userAgent)) {
      //   this.env.thirdApp = true
      //   this.env.wxWork = true
      // }
      // // 判断当时是否是钉钉环境
      // if (/dingtalk/i.test(navigator.userAgent)) {
      //   this.env.thirdApp = true
      //   this.env.dingtalk = true
      // }
    },

    /** 进行Auth登录操作 */
    doThirdLogin () {
      if (this.env.thirdApp) {
        // 判断是否携带了Token，是就说明登录成功
        if (this.$route.query.thirdAuthLoginToken) {
          this.thirdType = this.$route.query.thirdType
          let token = this.$route.query.thirdAuthLoginToken
          window.localStorage.token = token
          this.$Message.success({
            content: '登录成功！',
            onClose: () => {
              window.parent.location.href = window.location.origin
            }
          })
        }
      }
    }
  }
}
</script>

<style scoped>
.demo-spin-icon-load{
  animation: ani-demo-spin 1s linear infinite;
}
@keyframes ani-demo-spin {
  from { transform: rotate(0deg);}
  50%  { transform: rotate(180deg);}
  to   { transform: rotate(360deg);}
}
.demo-spin-col{
  height: 100px;
  position: relative;
  border: 1px solid #eee;
}
</style>
