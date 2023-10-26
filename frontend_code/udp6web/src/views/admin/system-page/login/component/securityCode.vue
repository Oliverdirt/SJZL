<style>
.security-code-wrapper {
  display: flex;
  align-items: center;
}
.security-code-wrapper > img {
  margin-left: 20px;
  height: 36px;
  cursor: pointer;
}
</style>

<template>
  <div class="security-code-wrapper" :style="{width: `${width}px`}">
    <Input
      v-model="securityCode"
      size="large"
      placeholder="请输入验证码"
      style="width: 220px;height: 40px"
      @on-change="$emit('update:value', securityCode)"
    />
    <img style="width: 125px;height: 40px;" :src="imgUrl" alt="验证码图片加载错误" title="点击刷新验证码" @click="init">
  </div>
</template>

<script>
import {
  getDigitalCaptcha
} from '@/api/admin/captcha'
export default {
  name: 'VerificationCode',
  data () {
    return {
      imgUrl: '',
      securityCode: '',
      isDisabled: true
    }
  },
  props: {
    width: {
      type: [String, Number],
      default: '300'
    },
    value: {
      type: String,
      default: ''
    },
    biyiCaptchaKey: {
      type: String,
      default: ''
    }
  },
  methods: {
    init () {
      this.getSecurityCode()
    },
    getSecurityCode () {
      getDigitalCaptcha()
      // this.$http.get('/api/catpcha/digitalCaptcha')
        .then(response => {
          if (response) {
            let key = Object.keys(response.data)[0]
            this.imgUrl = response.data[key]
            this.$emit('update:biyiCaptchaKey', Object.keys(response.data)[0])
          }
        }).catch()
    }
  },
  created () {
    this.init()
  }
}
</script>
