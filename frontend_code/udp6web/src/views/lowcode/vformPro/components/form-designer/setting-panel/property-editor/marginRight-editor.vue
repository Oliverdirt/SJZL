<template>
  <el-form-item label="右边距">
    <el-input-number
      v-model="optionModel.marginRight"
      :min="0"
      :max="1000"
      label="右边距"
      v-if="!noLabelSetting"
    >
    </el-input-number>&nbsp;px
  </el-form-item>
</template>

<script>
import i18n from '@/views/lowcode/utils/i18n'

export default {
  name: 'marginRight-editor',
  mixins: [i18n],
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object
  },
  computed: {
    noLabelSetting () {
      return (this.selectedWidget.type === 'static-text') || (this.selectedWidget.type === 'html-text')
    }
  }
}
</script>

<style scoped>

</style>
