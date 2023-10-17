<!--
 * @Author: your name
 * @Date: 2021-01-27 09:20:49
 * @LastEditTime: 2021-01-28 08:57:51
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \archetype-resources\src\app.vue
-->
<template>
  <div id="app">
    <title>{{ name }}</title>
    <router-view/>
  </div>
</template>
<script>

export default {
  data () {
    return {
      name: ''
    }
  },
  methods: {
    // 浏览器logo及标签
    LoginQuery () {
      this.$http.get('/api/cscpPortalConfigureLogin').then((res) => {
        this.name = res.data.data.lebalName ? res.data.data.lebalName : '统一开发平台研发框架'
        var link = document.querySelector('link[rel*="icon"]')
        link.href = res.data.data.logoUrl ? res.data.data.logoUrl : 'http://10.2.16.78:31246/images/2013190292logo-min.png'
      })
    }
  },
  created () {
    this.LoginQuery()
  }
}
</script>
<style lang="less">
@import './base.less';

html,
body {
  width: 100%;
  height: 100%;
  background: #f0f0f0;
  overflow: auto;
  word-break: break-all;
  word-wrap: break-word;
}

#app {
  width: 100%;
  height: 100%;
}

.ivu-select-dropdown {
  ul {
    max-width: 120px;
    padding-top: 8px;

    li {
      width: 100%;
      height: 100%;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap; /**不换行**/
      display: inline-block;
    }
  }
}
</style>
