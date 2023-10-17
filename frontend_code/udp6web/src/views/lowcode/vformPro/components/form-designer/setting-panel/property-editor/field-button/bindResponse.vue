<template>
  <el-form-item label="绑定接口返回值字段">
    <!-- 此字段用来获取指定绑定动态接口的返回值字段.如果输入data(有输入值默认前缀为res.data.),则相当于列表回显数据来自于res.data.data;如果用户未输入,则默认获取res.data.data -->
    <el-input v-model="optionModel.bindResponse"></el-input>
  </el-form-item>
</template>

<script>
import i18n from '@/views/lowcode/utils/i18n'
import propertyMixin
  from '@/views/lowcode/vformPro/components/form-designer/setting-panel/property-editor/propertyMixin'

export default {
  name: 'bindResponse-editor',
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
