<template>
  <span v-if="accessible" :class="['table-btn',btnClassName(type)]" @click="btnClick">
    <slot></slot>
  </span>
</template>

<script>
export default {
  data () {
    return {
      accessible: true
    }
  },
  props: {
    /**
     * 是否禁用
     */
    disabled: {
      type: Boolean,
      default: false
    },
    type: {
      type: String,
      default: ''
    },
    code: {
      type: [String, Number],
      default: ''
    }
  },
  methods: {
    btnClassName (type) {
      let className = ''
      switch (type) {
        case 'primary':
          className = 'table-btn-primary'
          break
        case 'info':
          className = 'table-btn-info'
          break
        case 'success':
          className = 'table-btn-success'
          break
        case 'warning':
          className = 'table-btn-warning'
          break
        case 'error':
          className = 'table-btn-error'
          break
        case 'diabled':
          className = 'table-btn-disabled'
          break
        default:
          break
      }
      return className
    },
    btnClick (event) {
      this.$emit('click', event)
    },
    init () {
      if (this.code) {
        this.accessible = this.$store.state.app.btnLimitedCodes.includes(this.code)
      } else {
        this.accessible = true
      }
    }
  },
  mounted () {
    this.init()
  }
}
</script>

<style lang='less' scoped>
.table-btn{
  color:#17233d;
  cursor: pointer;
}
.table-btn-primary{
  color:#2d8cf0
}
.table-btn-success{
  color:#19be6b
}
.table-btn-info{
  color:#2db7f5
}
.table-btn-warning{
  color: #ff9900;
}
.table-btn-error{
  color:#ed4014
}
.table-btn-disabled{
  pointer-events: none;
  color:#c5c8ce;
}
</style>
