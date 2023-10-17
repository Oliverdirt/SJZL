<template>
  <el-form-item label="绑定查询类型" v-if="!noLabelSetting">
    <el-select v-model="optionModel.queryType">
      <el-option label="=" value="0"></el-option>
      <el-option label=">=" value="1"></el-option>
      <el-option label="<=" value="2"></el-option>
      <el-option label="like" value="3"></el-option>
      <el-option label="between" value="4"></el-option>
    </el-select>
  </el-form-item>
</template>

<script>
import i18n from '@/views/lowcode/utils/i18n'

export default {
  name: 'queryType-editor',
  mixins: [i18n],
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object,
  },
  computed: {
    noLabelSetting () {
      return (this.selectedWidget.type === 'static-text') || (this.selectedWidget.type === 'html-text')
      //|| (this.selectedWidget.type === 'divider')
    },

  }
}
</script>

<style lang="scss" scoped>

</style>
