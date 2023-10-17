<template>
  <div id="app">
    <ng-form-design ref="tableDesigner">
      <template slot="controlButton">
        <el-button type="text" size="medium" @click="finish">完成</el-button>
      </template>
    </ng-form-design>
  </div>
</template>

<script>
export default {
  created() {
    console.log("this.$route.params", this.$route.params.moduleId);

  },
  data() {
    return {
      data: {
        json:''
      }
    };
  },
  methods: {
    finish() {
      this.json = this.$refs.tableDesigner.getModel()
      console.log(this.$refs.tableDesigner.getModel);
      console.log('json', this.json);
    },
  },
}
</script>

<style>

</style>