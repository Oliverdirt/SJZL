<template>
  <form-item-wrapper :designer="designer" :field="field" :design-state="designState"
                     :parent-widget="parentWidget" :parent-list="parentList"
                     :index-of-parent-list="indexOfParentList"
                     :sub-form-row-index="subFormRowIndex" :sub-form-col-index="subFormColIndex"
                     :sub-form-row-id="subFormRowId">
    <el-transfer
      filterable
      :filter-method="filterMethod"
      filter-placeholder="请输入搜索字符"
      v-model="value"
      :data="data">
    </el-transfer>
  </form-item-wrapper>
</template>

<script>
import FormItemWrapper from './form-item-wrapper'
import emitter from '@/views/lowcode/utils/emitter'
import i18n from '@/views/lowcode/utils/i18n'
import fieldMixin from '@/views/lowcode/vformPro/components/form-designer/form-widget/field-widget/fieldMixin'

export default {
  name: 'transfer-widget',
  componentName: 'FieldWidget', // 必须固定为FieldWidget，用于接收父级组件的broadcast事件
  components: {
    FormItemWrapper
  },
  mixins: [emitter, fieldMixin, i18n],
  props: {
    field: Object,
    parentWidget: Object,
    parentList: Array,
    indexOfParentList: Number,
    designer: Object,

    designState: {
      type: Boolean,
      default: false
    },

    subFormRowIndex: { /* 子表单组件行索引，从0开始计数 */
      type: Number,
      default: -1
    },
    subFormColIndex: { /* 子表单组件列索引，从0开始计数 */
      type: Number,
      default: -1
    },
    subFormRowId: { /* 子表单组件行Id，唯一id且不可变 */
      type: String,
      default: ''
    }

  },
  inject: ['refList', 'globalOptionData', 'globalModel'],
  data () {
    const generateData = _ => {
      const data = []
      const cities = ['上海', '北京', '广州', '深圳', '南京', '西安', '成都']
      const pinyin = ['上海', '北京', '广州', '深圳', '南京', '西安', '成都']
      cities.forEach((city, index) => {
        data.push({
          label: city,
          key: index,
          pinyin: pinyin[index]
        })
      })
      return data
    }
    return {
      fieldModel: [],
      value: [],
      data: generateData(),
      filterMethod (query, item) {
        return item.pinyin.indexOf(query) > -1
      }
    }
  },
  created () {
    /* 注意：子组件mounted在父组件created之后、父组件mounted之前触发，故子组件mounted需要用到的prop
         需要在父组件created中初始化！！ */
    this.initOptionItems()
    this.initFieldModel()
    this.registerToRefList()
    this.initEventHandler()
    this.buildFieldRules()
    this.handleOnCreated()
  },

  mounted () {
    this.handleOnMounted()
  },

  beforeDestroy () {
    this.unregisterFromRefList()
  },
  methods: {}
}
</script>

<style scoped>

</style>
