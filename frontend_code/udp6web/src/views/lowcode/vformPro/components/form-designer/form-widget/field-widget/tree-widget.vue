<template>
  <form-item-wrapper :designer="designer" :field="field" :design-state="designState"
                     :parent-widget="parentWidget" :parent-list="parentList"
                     :index-of-parent-list="indexOfParentList"
                     :sub-form-row-index="subFormRowIndex" :sub-form-col-index="subFormColIndex"
                     :sub-form-row-id="subFormRowId">
    <el-tree
      :data="data"
      show-checkbox
      node-key="id"
      :default-expanded-keys="[2, 3]"
      :default-checked-keys="[5]"
      :props="defaultProps">
    </el-tree>
  </form-item-wrapper>
</template>

<script>
import FormItemWrapper from './form-item-wrapper'
import emitter from '@/views/lowcode/utils/emitter'
import i18n from '@/views/lowcode/utils/i18n'
import fieldMixin from '@/views/lowcode/vformPro/components/form-designer/form-widget/field-widget/fieldMixin'

export default {
  name: 'tree-widget',
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
  watch: {
    fieldModel (val) {
      this.syncUpdateFormModel(this.fieldModel)
      /* 主动触发表单的单个字段校验，用于清除字段可能存在的校验错误提示 */
      this.dispatch('VFormRender', 'fieldValidation', [this.getPropName()])
    }
  },
  inject: ['refList', 'formConfig', 'globalOptionData', 'globalModel'],
  data () {
    return {
      dialogVisible: false,
      oldFieldValue: null, // field组件change之前的值
      fieldModel: null,
      rules: [],
      data: [
        {
          id: 1,
          label: '一级 1',
          children: [
            {
              id: 4,
              label: '二级 1-1',
              children: [{
                id: 9,
                label: '三级 1-1-1'
              }, {
                id: 10,
                label: '三级 1-1-2'
              }
              ]
            }]
        },
        {
          id: 2,
          label: '一级 2',
          children: [
            {
              id: 5,
              label: '二级 2-1'
            }, {
              id: 6,
              label: '二级 2-2'
            }
          ]
        },
        {
          id: 3,
          label: '一级 3',
          children: [
            {
              id: 7,
              label: '二级 3-1'
            }, {
              id: 8,
              label: '二级 3-2'
            }
          ]
        }
      ],
      defaultProps: {
        children: 'children',
        label: 'label'
      }
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
    this.handleOnMounted()
  },

  beforeDestroy () {
    this.unregisterFromRefList()
  },

  methods: {
    clickChioseUser () {
      this.dialogVisible = true
    },
    clickConfirmeBtn () {
      let temArr = []
      this.$refs.usertable.getLastUserVal().forEach(item => {
        temArr.push(item.username)
      })
      this.fieldModel = temArr.join(',')
      this.dialogVisible = false
    }
  }
}
</script>

<style lang="scss" scoped>
@import "@/styles/global"; //* form-item-wrapper已引入，还需要重复引入吗？ *//
.el-dialog__wrapper {
  .el-dialog__body {
    padding: 30px 20px !important;
  }
}

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
