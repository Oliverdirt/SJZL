<template>
  <div>
    <component :is="template"></component>
  </div>
</template>

<script>
// import FlowList from 'biyi-hx-activiti/src/views/flow/FlowList'

export default {
  name: 'FlowListTemplate',
  // components: {
  //   FlowList
  // },
  data () {
    return {
      template: ''
    }
  },
  created () {
    this.template = 'FlowList'
  }
}
</script>

<style scoped>

</style>
