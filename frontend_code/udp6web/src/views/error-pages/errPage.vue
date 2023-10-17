<style lang="less" scoped>
.page-404 {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 100%;
  background-color: white;

  span:first-of-type {
    font-size: 11.5rem;
    font-weight: bold;
    text-shadow: 5px 1px 5px rgba(0, 0, 0, 1)
  }

  span:last-of-type {
    font-size: 2rem;
  }

  a {
    font-size: 1.25rem;
  }
}
</style>

<template>
  <div class="page-404">
    <span>{{ code }}</span>
    <span>{{ msg }}</span><br>
    <a href="javascript:void(0)" @click="reback">返回首页</a>
  </div>
</template>

<script>
import {
  tianyiLogout
} from '@/api/admin/captcha'
export default {
  data () {
    return {
      code: '401',
      msg: '错误！',
      type: ''
    }
  },
  mounted () {
    window.parent.location.href = window.location.href
    let cUrl = decodeURIComponent(window.location.href)
    let params = cUrl.split('?')[1].split('&')
    this.code = params[1].split('=')[1]
    this.msg = params[0].split('=')[1]
    this.type = params[2] ? params[2].split('=')[1] : ''
  },
  methods: {
    logOutApi () {
      // let url = '/api/system/thirdAuth/tianyi/logout'
      tianyiLogout({ url: window.location.origin })
      // this.$util.http.get(url, { params: { url: window.location.origin } })
        .then(res => {
          let a = document.createElement('a')
          a.setAttribute('href', res.data)
          document.body.appendChild(a)
          a.style.display = 'none'
          a.click()
          document.body.removeChild(a)
        // this.$router.push('/login')
        }).catch(err => {
          console.log(new Error('function getTargetComponent error!'))
          console.log(err)
        })
    },
    reback () {
      if (this.type !== 'ty') {
        this.$router.push('/home')
      } else {
        this.logOutApi()
      }
    }
  }
}
</script>
