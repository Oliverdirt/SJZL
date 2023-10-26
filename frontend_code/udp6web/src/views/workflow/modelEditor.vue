<template>
  <div id="app">
    <!-- <my-process-palette /> -->
    <my-process-designer :key="`designer-${reloadIndex}`" :options="{
      taskResizingEnabled: true, eventResizingEnabled: true,
      minimap: { open: false }
    }" v-model="xmlString" v-bind="controlForm" keyboard ref="processDesigner" @element-click="elementClick"
      @element-contextmenu="elementContextmenu" @init-finished="initModeler" @save="save">
    </my-process-designer>
    <my-properties-panel :key="`penal-${reloadIndex}`" :bpmn-modeler="modeler" :prefix="controlForm.prefix"
      class="process-panel" />

    <!-- demo config -->
    <div class="demo-control-bar">
      <div class="open-model-button" @click="controlDrawerVisible = true"><i class="el-icon-setting"></i></div>
    </div>
    <el-drawer :visible.sync="controlDrawerVisible" size="400px" title="偏好设置" append-to-body destroy-on-close>
      <el-form :model="controlForm" size="small" label-width="100px" class="control-form" @submit.native.prevent>
        <el-form-item label="流程ID">
          <el-input v-model="controlForm.processId" @change="reloadProcessDesigner(true)" />
        </el-form-item>
        <el-form-item label="流程名称">
          <el-input v-model="controlForm.processName" @change="reloadProcessDesigner(true)" />
        </el-form-item>
        <el-form-item label="流转模拟">
          <el-switch v-model="controlForm.simulation" inactive-text="停用" active-text="启用"
            @change="reloadProcessDesigner()" />
        </el-form-item>
        <el-form-item label="禁用双击">
          <el-switch v-model="controlForm.labelEditing" inactive-text="停用" active-text="启用"
            @change="changeLabelEditingStatus" />
        </el-form-item>
        <el-form-item label="自定义渲染">
          <el-switch v-model="controlForm.labelVisible" inactive-text="停用" active-text="启用"
            @change="changeLabelVisibleStatus" />
        </el-form-item>
        <el-form-item label="流程引擎">
          <el-radio-group v-model="controlForm.prefix" @change="reloadProcessDesigner()">
            <el-radio label="activiti">activiti</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="工具栏">
          <el-radio-group v-model="controlForm.headerButtonSize">
            <el-radio label="mini">mini</el-radio>
            <el-radio label="small">small</el-radio>
            <el-radio label="medium">medium</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-switch v-model="pageMode" active-text="dark" inactive-text="light" @change="changePageMode"></el-switch>
      </el-form>
    </el-drawer>

<!--    <div class="demo-info-bar">-->
<!--      <div class="open-model-button" @click="infoTipVisible = !infoTipVisible"><i class="el-icon-info"></i></div>-->
<!--      <transition name="zoom-in-right">-->
<!--        <div class="info-tip" v-show="infoTipVisible">-->
<!--          <p><strong>该项目仅作为Bpmn.js的简单演示项目，不涉及过多的自定义Render内容。</strong></p>-->
<!--          <p>注：activiti 好像不支持表单配置，控制台可能会报错</p>-->
<!--          <p>-->
<!--            <span>更多配置请查看源码：</span>-->
<!--            <a href="https://github.com/miyuesc/bpmn-process-designer">MiyueSC/bpmn-process-designer</a>-->
<!--          </p>-->
<!--          <p>-->
<!--            <span>bpmn 官方图标：</span>-->
<!--            <a href="https://cdn.staticaly.com/gh/bpmn-io/bpmn-font/master/dist/demo.html">bpmn-io/bpmn-font</a>-->
<!--          </p>-->
<!--          <p>-->
<!--            <span>疑问请在此留言：</span>-->
<!--            <a-->
<!--              href="https://github.com/miyuesc/bpmn-process-designer/issues/16">MiyueSC/bpmn-process-designer/issues</a>-->
<!--          </p>-->
<!--        </div>-->
<!--      </transition>-->
<!--    </div>-->
  </div>
</template>

<script>
import translations from '@/translations'
// 自定义渲染（隐藏了 label 标签）
import CustomRenderer from './modules/custom-renderer'
// 自定义元素选中时的弹出菜单（修改 默认任务 为 用户任务）
import CustomContentPadProvider from './package/designer/plugins/content-pad'
// 自定义左侧菜单（修改 默认任务 为 用户任务）
import CustomPaletteProvider from './package/designer/plugins/palette'
import Log from './package/Log'
// 任务resize
import resizeTask from 'bpmn-js-task-resize/lib'
// bpmn theme plugin
import sketchyRendererModule from 'bpmn-js-sketchy'
// 小地图
import minimapModule from 'diagram-js-minimap'

import UserSql from './modules/extension/user.json'

// clickoutside
import clickoutside from 'element-ui/lib/utils/clickoutside'
import RewriteAutoPlace from './modules/auto-place/rewriteAutoPlace'
import './package/theme/index.scss'
import request from '@/api/workflow/request'
export default {
  name: 'App',
  directives: {
    clickoutside: clickoutside
  },
  props: {
    modelEditorData: {
      type: Object
      // required: true
    }
  },
  data () {
    return {
      xmlString: '',
      modeler: null,
      reloadIndex: 0,
      controlDrawerVisible: false,
      infoTipVisible: false,
      pageMode: false,
      translationsSelf: translations,
      controlForm: {
        processId: '',
        processName: '',
        simulation: true,
        labelEditing: false,
        labelVisible: false,
        prefix: 'activiti',
        headerButtonSize: 'mini',
        events: ['element.click', 'element.contextmenu'],
        // additionalModel: []
        moddleExtension: { user: UserSql },
        additionalModel: [
          CustomContentPadProvider,
          CustomPaletteProvider,
          minimapModule,
          {
            __init__: ['autoPlaceSelectionBehavior'],
            autoPlace: ['type', RewriteAutoPlace]
          }
        ]
      },
      addis: {
        CustomContentPadProvider,
        CustomPaletteProvider
      },
      // 流程模型的信息
      model: {}
    }
  },
  created () {
    this.category = this.$route.query.category
    this.$nextTick(() => {
      this.$refs.processDesigner.modelEditorDataFn(this.modelEditorData, this.category)
    })
    // 如果 modelId 非空，说明是修改流程模型
    // const modelId = (this.$route.query && this.$route.query.modelId) ? (this.$route.query && this.$route.query.modelId) : this.modelEditorData
    // console.log(modelId)
    // if (modelId) {
    //   let param = {
    //     id: modelId ? this.modelEditorData : this.modelEditorData.modelId,
    //     key: modelId.processId ? modelId.processId : this.modelEditorData.processId,
    //     name: modelId.processName ? modelId.processName :this.modelEditorData.processName
    //   }
    //   this.$http.get('/api/flow/bpmn/get', {params: param})
    //     .then((res) => {
    //       if (res.data.result.bpmnXml) {
    //         this.xmlString = res.data.result.bpmnXml
    //       }
    //       this.model = {
    //         ...res.data.result,
    //         bpmnXml: undefined, // 清空 bpmnXml 属性
    //       }
    //     })
    //     .catch(() => {
    //       this.$Message.error('模型查询失败')
    //     })
    // }

    if (this.$route.query && this.$route.query.modelId) {
      const modelId = this.$route.query && this.$route.query.modelId
      let param = {
        id: modelId,
        key: modelId.processId,
        name: modelId.processName
      }
      // this.$http.get('/api/flow/bpmn/get', { params: param })
      request.getFlowBpmnList({ params: param })
        .then((res) => {
          if (res.data.result.bpmnXml) {
            this.xmlString = res.data.result.bpmnXml
          }
          this.model = {
            ...res.data.result,
            bpmnXml: undefined // 清空 bpmnXml 属性
          }
        })
        .catch(() => {
          this.$Message.error('模型查询失败')
        })
    } else if (this.modelEditorData) {
      let param = {
        id: this.modelEditorData.id,
        key: this.modelEditorData.key,
        name: this.modelEditorData.name
      }
      // this.$http.get('/api/flow/bpmn/get', { params: param })
      request.getFlowBpmnList({ params: param })
        .then((res) => {
          if (res.data.result.bpmnXml) {
            this.xmlString = res.data.result.bpmnXml
          }
          this.model = {
            ...res.data.result,
            bpmnXml: undefined // 清空 bpmnXml 属性
          }
        })
        .catch(() => {
          this.$Message.error('模型查询失败')
        })
    }
  },
  deactivated () {
    this.$destroy()
  },
  methods: {
    // 初始化加载，传递信息，不可修改
    initModeler (modeler) {
      setTimeout(() => {
        const processId = this.$route.query.processId ? this.$route.query.processId : this.modelEditorData.key
        const processName = this.$route.query.processName ? this.$route.query.processName : this.modelEditorData.name
        this.modeler = modeler
        const canvas = modeler.get('canvas')
        const rootElement = canvas.getRootElement()
        rootElement.businessObject.id = processId
        rootElement.businessObject.name = processName
      }, 10)
    },
    reloadProcessDesigner (notDeep) {
      this.controlForm.additionalModel = []
      for (let key in this.addis) {
        if (this.addis[key]) {
          this.controlForm.additionalModel.push(this.addis[key])
        }
      }
      !notDeep && (this.xmlString = undefined)
      this.reloadIndex += 1
      this.modeler = null // 避免 panel 异常
    },
    changeLabelEditingStatus (status) {
      this.addis.labelEditing = status ? { labelEditingProvider: ['value', ''] } : false
      this.reloadProcessDesigner()
    },
    changeLabelVisibleStatus (status) {
      this.addis.customRenderer = status ? CustomRenderer : false
      this.reloadProcessDesigner()
    },
    elementClick (element) {
      console.log(element)
      this.element = element
    },
    elementContextmenu (element) {
      console.log('elementContextmenu:', element)
    },
    changePageMode (mode) {
      const theme = mode
        ? {
          // dark
          stroke: '#ffffff',
          fill: '#333333'
        }
        : {
          // light
          stroke: '#000000',
          fill: '#ffffff'
        }
      const elements = this.modeler.get('elementRegistry').getAll()
      this.modeler.get('modeling').setColor(elements, theme)
    },
    toggle () {
      console.log(this.modeler)
      console.log(this.modeler.get('toggleMode'))
      this.modeler.get('toggleMode').toggleMode()
    },
    save (bpmnXml) {
      const data = {
        ...this.model,
        bpmnXml: bpmnXml // this.bpmnXml 只是初始化流程图，后续修改无法通过它获得
      }
      console.log(data)
      // 修改的提交
      if (data.id) {
        // this.$http.put('/api/flow/bpmn/update', data)
        request.updateBpmn(data)
          .then((res) => {
            this.$Message.success('保存成功')
            if (!this.category) {
              this.closeMe()
              this.$parent.$parent.flowStepsVisiable = false
              this.$parent.$parent.step = 0
              this.$parent.$parent.$parent.queryList()
            } else {
              this.$router.push({
                path: '/flowform'
              })
            }
          }).catch(() => {
            this.$Message.error('保存失败')
          })
        return
      }
      // 添加的提交
      // this.$http.post('/api/flow/bpmn/create', data)
      request.createBpmn(data)
        .then((res) => {
          this.$Message.success('新增成功')
          console.log(3, '3')
          this.$router.push({
            path: '/mainflow'
          })
        })
        .catch(() => {
          this.$Message.error('新增失败')
        })
    },
    closeMe () {
      this.$store.commit('app/closePage', {
        vm: this,
        fromName: this.$route.name,
        toName: 'mainflow'
      })
      // this.$router.push({
      //   path: '/mainflow'
      // })
    }
  }
}
</script>
<style lang="scss" scoped>
body {
  overflow: hidden;
  margin: 0;
  box-sizing: border-box;
}

#app {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  // display: inline-grid;
  display: flex;
  grid-template-columns: 100px auto max-content;
}

.demo-info-bar {
  position: fixed;
  right: 8px;
  bottom: 108px;
  z-index: 1;
}

.demo-control-bar {
  position: fixed;
  right: 8px;
  bottom: 48px;
  z-index: 1;
}

.open-model-button {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  font-size: 32px;
  background: rgba(64, 158, 255, 1);
  color: #ffffff;
  cursor: pointer;
}

.zoom-in-right-enter-active,
.zoom-in-right-leave-active {
  opacity: 1;
  transform: scaleY(1) translateY(-48px);
  transition: all 300ms cubic-bezier(0.23, 1, 0.32, 1);
  transform-origin: right center;
}

.zoom-in-right-enter,
.zoom-in-right-leave-active {
  opacity: 0;
  transform: scaleX(0) translateY(-48px);
}

.info-tip {
  position: absolute;
  width: 480px;
  top: 0;
  right: 64px;
  z-index: 10;
  box-sizing: border-box;
  padding: 0 16px;
  color: #333333;
  background: #f2f6fc;
  transform: translateY(-48px);
  border: 1px solid #ebeef5;
  border-radius: 4px;

  &::before,
  &::after {
    content: "";
    width: 0;
    height: 0;
    border-width: 8px;
    border-style: solid;
    position: absolute;
    right: -15px;
    top: 50%;
  }

  &::before {
    border-color: transparent transparent transparent #f2f6fc;
    z-index: 10;
  }

  &::after {
    right: -16px;
    border-color: transparent transparent transparent #ebeef5;
    z-index: 1;
  }
}

.control-form {
  .el-radio {
    width: 100%;
    line-height: 32px;
  }
}

.element-overlays {
  box-sizing: border-box;
  padding: 8px;
  background: rgba(0, 0, 0, 0.6);
  border-radius: 4px;
  color: #fafafa;
}

body,
body * {

  /* 滚动条 */
  &::-webkit-scrollbar-track-piece {
    background-color: #fff;
    /*滚动条的背景颜色*/
    -webkit-border-radius: 0;
    /*滚动条的圆角宽度*/
  }

  &::-webkit-scrollbar {
    width: 10px;
    /*滚动条的宽度*/
    height: 8px;
    /*滚动条的高度*/
  }

  &::-webkit-scrollbar-thumb:vertical {
    /*垂直滚动条的样式*/
    height: 50px;
    background-color: rgba(153, 153, 153, 0.5);
    -webkit-border-radius: 4px;
    outline: 2px solid #fff;
    outline-offset: -2px;
    border: 2px solid #fff;
  }

  &::-webkit-scrollbar-thumb {
    /*滚动条的hover样式*/
    background-color: rgba(159, 159, 159, 0.3);
    -webkit-border-radius: 4px;
  }

  &::-webkit-scrollbar-thumb:hover {
    /*滚动条的hover样式*/
    background-color: rgba(159, 159, 159, 0.5);
    -webkit-border-radius: 4px;
  }
  ::v-deep .my-process-designer .my-process-designer__container .my-process-designer__canvas .bjs-container a svg{
    display: none
  }
  ::v-deep .djs-palette.two-column.open {
    margin-left: -20px;
  }
}
</style>
