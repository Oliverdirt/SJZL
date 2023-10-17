<template>
  <form-item-wrapper :designer="designer" :field="field" :rules="rules" :design-state="designState"
                     :parent-widget="parentWidget" :parent-list="parentList" :index-of-parent-list="indexOfParentList"
                     :sub-form-row-index="subFormRowIndex" :sub-form-col-index="subFormColIndex" :sub-form-row-id="subFormRowId">

    <div class="ryzjContainer">
      <Treeselect  v-model="fieldModel"  @select="selectValue" :options="treeArr"  placeholder="请输入部门"  autocomplete=“off” ref="depttree"/>
    </div>

  </form-item-wrapper>
</template>

<script>
import FormItemWrapper from './form-item-wrapper'
import emitter from '@/views/lowcode/utils/emitter'
import i18n from '@/views/lowcode/utils/i18n'
import fieldMixin from '@/views/lowcode/vformPro/components/form-designer/form-widget/field-widget/fieldMixin'

export default {
  name: 'dept-widget',
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
  inject: ['refList', 'formConfig', 'globalOptionData', 'globalModel'],
  data () {
    return {
      oldFieldValue: null, // field组件change之前的值
      rules: [],
      treeArr: [],
      fieldModel: null
    }
  },
  computed: {
    allowDefaultFirstOption () {
      return (!!this.field.options.filterable && !!this.field.options.allowCreate)
    }
  },
  beforeCreate () {
    /* 这里不能访问方法和属性！！ */
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
    this.$http.get('/api/cscpDepts/treeselect').then(res => {
      if (this.fieldModel === '') {
        this.fieldModel = null
      }
      this.treeArr = res.data
    })
    this.handleOnMounted()
  },
  methods: {
    selectValue (v) {
      this.fieldModel = v.id
      this.syncUpdateFormModel(this.fieldModel)
    }
  },
  beforeDestroy () {
    this.unregisterFromRefList()
  }

}
</script>

<style lang="less">
.ryzjContainer {
  display: flex;
  align-items: center;

  span {
    width: 80px;
    flex-shrink: 0;
  }

  .vue-treeselect {
    flex: 1;
  }
}
</style>
