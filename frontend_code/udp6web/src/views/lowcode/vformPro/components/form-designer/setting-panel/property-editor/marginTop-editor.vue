<template>
  <div style="margin: 15px 0">
    <el-form-item label="设置上间距">
      <el-input-number v-model="optionModel.marginTop"></el-input-number>  px
    </el-form-item>
  </div>
</template>

<script>
import i18n from '@/views/lowcode/utils/i18n'
import propertyMixin
  from '@/views/lowcode/vformPro/components/form-designer/setting-panel/property-editor/propertyMixin'

export default {
  name: 'marginTop-editor',
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
