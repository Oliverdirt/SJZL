<template>
  <!-- <el-form-item label="内连样式"> -->
  <el-form-item label="">
    <!-- <el-input type="text" v-model="optionModel.marginStyle" disabled></el-input> -->
  </el-form-item>
</template>

<script>
import i18n from '@/views/lowcode/utils/i18n'
import propertyMixin
  from '@/views/lowcode/vformPro/components/form-designer/setting-panel/property-editor/propertyMixin'

export default {
  name: 'marginStyle-editor',
  mixins: [i18n, propertyMixin],
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object
  }
}
</script>

<style lang="scss" scoped>

</style>
