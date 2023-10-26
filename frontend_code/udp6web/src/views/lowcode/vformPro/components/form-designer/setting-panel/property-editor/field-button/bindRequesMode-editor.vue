<template>
  <el-form-item label="绑定接口方式">
    <el-select v-model="optionModel.bindRequestMode">
      <el-option label="get" value="get"></el-option>
      <el-option label="post" value="post"></el-option>
      <el-option label="put" value="put"></el-option>
      <el-option label="delete" value="delete"></el-option>
      <el-option label="head" value="head"></el-option>
      <el-option label="patch" value="patch"></el-option>
      <el-option label="trace" value="trace"></el-option>
      <el-option label="connect" value="connect"></el-option>
    </el-select>
  </el-form-item>
</template>

<script>
import i18n from '@/views/lowcode/utils/i18n'
import propertyMixin
  from '@/views/lowcode/vformPro/components/form-designer/setting-panel/property-editor/propertyMixin'

export default {
  name: 'bindRequestMode-editor',
  mixins: [i18n, propertyMixin],
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object,
  }
}
</script>

<style lang="scss" scoped>

</style>
