<template>
  <el-form-item :label="i18nt('designer.setting.remote')">
    <el-switch v-model="optionModel.remote" @change="onRemoteChange"></el-switch>
  </el-form-item>
</template>

<script>
  import i18n from "@/views/lowcode/utils/i18n"
  import propertyMixin from "@/views/lowcode/vformPro/components/form-designer/setting-panel/property-editor/propertyMixin"

  export default {
    name: "remote-editor",
    mixins: [i18n, propertyMixin],
    props: {
      designer: Object,
      selectedWidget: Object,
      optionModel: Object,
    },

  }
</script>

<style scoped>

</style>
