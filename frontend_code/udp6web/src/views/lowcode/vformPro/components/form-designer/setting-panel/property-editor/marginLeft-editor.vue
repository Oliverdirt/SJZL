<template>
  <el-form-item label="左边距">
    <el-input-number
      v-model="optionModel.marginLeft"
      :min="0"
      :max="1000"
      label="左边距"
      v-if="!noLabelSetting"
    >
    </el-input-number>&nbsp;px
  </el-form-item>
</template>

<script>
import i18n from '@/views/lowcode/utils/i18n'

export default {
  name: 'marginLeft-editor',
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
