<template>
  <div>
   <Process></Process>
  </div>
</template>

<script>
// import Process from 'biyi-hx-activiti/src/views/flow/Process'

export default {
  name: 'ProcessList',
  components: {
    Process
  },
  data () {
    return {
      template: ''
    }
  },
  created () {
    this.template = 'Process'
  }
}
</script>

<style scoped>

</style>
