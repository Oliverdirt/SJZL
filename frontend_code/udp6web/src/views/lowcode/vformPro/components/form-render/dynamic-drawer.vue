<template>
  <el-drawer ref="drawerRef" :visible.sync="drawerVisible" class="dynamic-drawer" append-to-body destroy-on-close
             :size="options.size" :modal="options.showModal" :direction="options.direction"
             :show-close="options.showClose" :wrapper-closable="options.closeOnClickModal"
             :close-on-press-escape="options.closeOnPressEscape"
             :before-close="handleBeforeClose" @close="handleCloseEvent" @opened="handleOpenedEvent">
    <template #title>
      <div>{{ options.title }}</div>
      <div style="float: right">
        <el-button v-if="!options.cancelButtonHidden" @click="handleCancelClick">
          {{ cancelBtnLabel }}
        </el-button>
        <el-button v-if="!options.okButtonHidden" type="primary" @click="handleOkClick">
          {{ okBtnLabel }}
        </el-button>
      </div>
    </template>
    <VFormRender ref="dFormRef" :form-json="formJson" :form-data="formData"
                 :option-data="optionData" :global-dsv="globalDsv" :parent-form="parentFormRef"
                 :disabled-mode="options.disabledMode" :dynamic-creation="true">
    </VFormRender>
  </el-drawer>
</template>

<script>
import i18n from '@/views/lowcode/utils/i18n'

export default {
  name: 'dynamic-drawer',
  mixins: [i18n],
  props: {
    options: {
      type: Object,
      default: () => ({})
    },
    formJson: {
      type: Object,
    },
    formData: {
      type: Object,
      default: () => ({})
    },
    optionData: { //prop传入的选项数据
      type: Object,
      default: () => ({})
    },
    globalDsv: { // 全局数据源变量
      type: Object,
      default: () => ({})
    },
    parentFormRef: {
      type: Object,
      default: null
    },
    extraData: {
      type: Object,
      default: () => ({})
    },
  },
  data () {
    return {
      drawerVisible: false,
    }
  },
  computed: {
    cancelBtnLabel () {
      return this.options.cancelButtonLabel || this.i18nt('designer.hint.cancel')
    },

    okBtnLabel () {
      return this.options.okButtonLabel || this.i18nt('designer.hint.confirm')
    }

  },
  mounted () {
    //
  },
  beforeDestroy () {
    this.parentFormRef.setChildFormRef(null)
  },
  methods: {
    show () {
      this.drawerVisible = true

      //设置readMode模式
      this.$nextTick(() => {
        if (!!this.options.readMode) {
          this.$refs['dFormRef'].setReadMode(true)
        }

        this.$refs['dFormRef'].setDialogOrDrawerRef(this)
        this.parentFormRef.setChildFormRef(this.$refs['dFormRef'])
      })
    },

    close () {
      if (!!this.options.onDrawerBeforeClose) {
        let Fn = Function
        let customFn = new Fn(this.options.onDrawerBeforeClose)
        let closeResult = customFn.call(this)
        if (closeResult === false) {
          return
        }
      }

      this.drawerVisible = true
      this.$refs['drawerRef'].closeDrawer()
    },

    handleBeforeClose (done) {
      if (!!this.options.onDrawerBeforeClose) {
        let Fn = Function
        let customFn = new Fn(this.options.onDrawerBeforeClose)
        let closeResult = customFn.call(this)
        return (closeResult === false) ? closeResult : done()
      }

      return done()
    },

    handleCloseEvent () {
      //this.$destroy()
      this.$el.parentNode.removeChild(this.$el)
    },

    handleOpenedEvent () {
      if (!!this.options.onDrawerOpened) {
        let Fn = Function
        let customFn = new Fn(this.options.onDrawerOpened)
        customFn.call(this)
      }
    },

    handleCancelClick () {
      if (!!this.options.onCancelButtonClick) {
        let Fn = Function
        let customFn = new Fn(this.options.onCancelButtonClick)
        let clickResult = customFn.call(this)
        if (clickResult === false) {
          return
        }
      }

      this.$destroy()
    },

    handleOkClick () {
      if (!!this.options.onOkButtonClick) {
        let Fn = Function
        let customFn = new Fn(this.options.onOkButtonClick)
        let clickResult = customFn.call(this)
        if (clickResult === false) {
          return
        }
      }

      this.$destroy()
    },

    getParentFormRef () {
      return this.parentFormRef
    },

    getFormRef () {
      return this.$refs['dFormRef']
    },

    getWidgetRef (widgetName, showError = false) {
      return this.$refs['dFormRef'].getWidgetRef(widgetName, showError)
    },

    getExtraData () {
      return this.extraData
    },

  }
}
</script>

<style lang="scss" scoped>
.dynamic-drawer {
  ::v-deep .el-drawer__header {
    margin-bottom: 10px;
  }

  ::v-deep .el-drawer__body {
    padding: 15px;
  }
}

</style>
