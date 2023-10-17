<template>
  <el-form-item label="绑定动态接口">
    <el-input v-model="optionModel.bindInterfaceAddress"></el-input>
  </el-form-item>
</template>

<script>
import i18n from '@/views/lowcode/utils/i18n'
import propertyMixin
  from '@/views/lowcode/vformPro/components/form-designer/setting-panel/property-editor/propertyMixin'

export default {
  name: 'bindInterfaceAddress-editor',
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
