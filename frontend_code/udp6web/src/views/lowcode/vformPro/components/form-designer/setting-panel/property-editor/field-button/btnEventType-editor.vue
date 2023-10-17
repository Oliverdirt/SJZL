<template>
  <el-form-item label="绑定事件类型">
    <el-select @change="chioseBtnType" v-model="optionModel.btnEventType">
      <el-option label="查询" value="查询"></el-option>
      <el-option label="新增" value="新增"></el-option>
      <el-option label="删除" value="删除"></el-option>
      <el-option label="导入" value="导入"></el-option>
      <el-option label="导出" value="导出"></el-option>
      <el-option label="字段配置" value="字段配置"></el-option>
      <el-option label="模板下载" value="模板下载"></el-option>
      <el-option label="自定义" value="自定义"></el-option>
    </el-select>
  </el-form-item>
</template>

<script>
import i18n from '@/views/lowcode/utils/i18n'
import propertyMixin
  from '@/views/lowcode/vformPro/components/form-designer/setting-panel/property-editor/propertyMixin'

export default {
  name: 'btnEventType-editor',
  mixins: [i18n, propertyMixin],
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object,
  },
  methods: {
    chioseBtnType () {
      this.optionModel.label = this.optionModel.btnEventType
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
