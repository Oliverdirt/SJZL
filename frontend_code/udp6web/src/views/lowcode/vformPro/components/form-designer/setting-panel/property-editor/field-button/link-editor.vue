<template>
  <el-form-item label="绑定表单页面">
    <el-select v-model="optionModel.linkFormPage">
      <el-option
        v-for="(item,index) in pages"
        :label="item.pageName"
        :value="item.pageId"
        :key="index+'pages'"
      >
      </el-option>
    </el-select>
  </el-form-item>
</template>

<script>
import i18n from '@/views/lowcode/utils/i18n'
import propertyMixin
  from '@/views/lowcode/vformPro/components/form-designer/setting-panel/property-editor/propertyMixin'

export default {
  name: 'link-editor',
  mixins: [i18n, propertyMixin],
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object
  },
  data () {
    return {
      pages: undefined
    }
  },
  mounted () {
    this.$http.get('/api/lowcode/customize/cscpCustomizeVpages', {
      params: {
        page: 0,
        size: 1000
      }
    }).then(res => {
      this.pages = res.data.data

    })
  }
}
</script>

<style lang="scss" scoped>

</style>
