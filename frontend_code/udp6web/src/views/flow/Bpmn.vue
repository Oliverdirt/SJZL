<template>
    <section class="section-height">
        <div class="content" id="js-drop-zone">
            <div id="canvas"></div>
        </div>
        <div class="io-import-export">
            <ul class="io-import io-control io-control-list io-horizontal">
                <li>
                    <a class="link-btn" title="部署" @click="save(true)">
                        <i class="fa fa fa-cloud-upload"></i>
                    </a>
                </li>
                <li>
                    <a class="link-btn" title="暂存流程" @click="save(false)">
                        <i class="fa fa-upload"></i>
                    </a>
                </li>
                <li>
                    <a class="link-btn" title="查看流程图" @click="openSvg()">
                        <i class="fa fa-eye-slash"></i>
                    </a>
                </li>
                <li>
                    <a class="link-btn" title="下载流程图" @click="downloadSvg()">
                        <i class="fa fa-download"></i>
                    </a>
                </li>
                <li>
                    <a title="返回上一页" @click="back()">
                        <i class="fa fa-long-arrow-left"></i>
                    </a>
                </li>
            </ul>
        </div>
        <div class="io-zoom-controls" style="display: block">
            <ul class="io-import io-control io-control-list io-horizontal">
                <li>
                    <a title="定位到初始位置" @click="resetZoom()">
                        <i class="fa fa-crosshairs"></i>
                    </a>
                </li>
            </ul>
        </div>

        <div class="defined-settings-modal">
            <process-win ref="ProcessWin"></process-win>
            <user-task-win ref="UserTaskWin" @refreshElement="refreshElement"></user-task-win>
            <service-task-win ref="ServiceTaskWin" @refreshElement="refreshElement"></service-task-win>
            <call-activity-win ref="CallActivityWin" @refreshElement="refreshElement"></call-activity-win>
            <sequence-flow-win ref="SequenceFlowWin" @refreshElement="refreshElement"></sequence-flow-win>
            <none-win ref="NoneWin" @refreshElement="refreshElement"></none-win>
            <flow-diagram ref="FlowDiagram"></flow-diagram>
            <process-tem-win ref="ProcessTemWin" @refreshXml="refreshXml"></process-tem-win>
        </div>

    </section>
</template>

<script>
import util from '@/libs/util.js'

// bpmn-js
// import BpmnJS from 'biyi-hx-activiti/src/views/flow/lib/bpmn/bpmn_modeler.js'
// 自定义模态框组件
import ProcessWin from './components/model/ProcessWin'
import UserTaskWin from './components/model/UserTaskWin'
import ServiceTaskWin from './components/model/ServiceTaskWin'
import CallActivityWin from './components/model/CallActivityWin'
import SequenceFlowWin from './components/model/SequenceFlowWin'
import NoneWin from './components/model/NoneWin'
import FlowDiagram from './components/model/FlowDiagram'
import ProcessTemWin from './components/model/ProcessTemWin'

export default {
  name: 'BpmnH',
  components: { ProcessTemWin, FlowDiagram, NoneWin, SequenceFlowWin, CallActivityWin, UserTaskWin, ProcessWin, ServiceTaskWin },
  data () {
    return {
      processDefinitionKey: null,
      processName: null,
      processElement: null,
      id: null,
      index: 0,
      process: null,
      nodes: null,
      lines: null,
      bpmnModeler: null,
      bpmnXML: null
    }
  },
  mounted () {
    window.patrn = /[`~!@#$%^&*()_\-+=<>?:"{}|,.\/;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]/im

    if (typeof (this.$route.query.id) !== 'undefined' && this.$route.query.id !== null && this.$route.query.id !== '') {
      this.id = this.$route.query.id
    }
    // this.bpmnModeler = new BpmnJS({
    //   container: '#canvas',
    //   keyboard: {
    //     bindTo: window
    //   }
    // })
    if (this.id === null) {
      let This = this
      util.http.get('/api/activiti/bpmn/createProcessDefinitionKey').then(response => {
        This.processDefinitionKey = response.data
        This.$refs.ProcessTemWin.modalVisible = true
        This.$refs.ProcessTemWin.process.processDefinitionKey = This.processDefinitionKey
        This.$refs.ProcessTemWin.process.name = This.processName
      })
    } else {
      this.initProcess()
    }
  },
  methods: {

    refreshXml (value) {
      this.processDefinitionKey = value.processDefinitionKey
      this.processName = value.name
      this.initXml()
    },

    initXml () {
      let This = this
      if (This.bpmnXML === '' || This.bpmnXML === null) {
        This.bpmnXML = '<?xml version="1.0" encoding="UTF-8"?>\n' +
                        '<bpmn2:definitions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:bpmn2="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" xmlns:di="http://www.omg.org/spec/DD/20100524/DI" xsi:schemaLocation="http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd" id="sample-diagram" targetNamespace="http://bpmn.io/schema/bpmn">\n' +
                        '  <bpmn2:process id="' + This.processDefinitionKey + '">\n' +
                        '  </bpmn2:process>\n' +
                        '  <bpmndi:BPMNDiagram id="BPMNDiagram_' + This.processDefinitionKey + '">\n' +
                        '    <bpmndi:BPMNPlane id="BPMNPlane_' + This.processDefinitionKey + '" bpmnElement="' + This.processDefinitionKey + '">\n' +
                        '    </bpmndi:BPMNPlane>\n' +
                        '  </bpmndi:BPMNDiagram>\n' +
                        '</bpmn2:definitions>'
        // 导入绘制
        This.openDiagram()
      }
    },

    // 初始化流程数据
    initProcess () {
      let This = this
      util.http.get('/api/activiti/bpmn/getFullProcessData/' + This.id).then(response => {
        This.process = response.data.process
        This.nodes = response.data.nodes
        This.lines = response.data.lines
        This.bpmnXML = response.data.process.xml
        This.processName = This.process.name
        This.processDefinitionKey = This.process.processDefinitionKey
        // 导入绘制
        This.openDiagram()
      })
    },

    // 绘制流程图
    openDiagram () {
      let modalElement = null
      let This = this
      This.bpmnModeler.importXML(This.bpmnXML, function (err) {
        if (err) {
          return console.error('could not import BPMN 2.0 diagram', err)
        }
        let canvas = This.bpmnModeler.get('canvas')
        let processEle = canvas.getRootElement()
        processEle.businessObject.data = This.initProcessData(processEle.businessObject)
        let flowElements = processEle.businessObject.flowElements
        // 初始化元素data
        if (flowElements != null) {
          for (let i = 0; i < flowElements.length; i++) {
            let eleBo = flowElements[i]
            eleBo.data = This.initElementData(eleBo)
          }
        }
        // 绑定事件
        let eventBus = This.bpmnModeler.get('eventBus')
        eventBus.on('shape.added', function (evt, ele) {
          let element = ele.element
          if (element.type !== 'bpmn:StartEvent' &&
                            element.type !== 'bpmn:ExclusiveGateway' &&
                            element.type !== 'bpmn:ParallelGateway' &&
                            element.type !== 'bpmn:EndEvent') {
            This.index++
          }
          // element.height = 50;
          let bo = element.businessObject

          // 初始化信息
          let data = null
          // 人工任务
          if (This.isUserTask(bo)) {
            data = {
              taskType: 'user',
              taskListener: null,

              assignees: [],
              assigneeRule: [],

              multiInstance: 'false',
              sequential: 'false',
              multiStrategy: null,
              decisionType: '1',
              votingType: '1',
              votes: '100',
              sendMail: 'false',
              formUrl: '',
              formAuth: {}
            }
            bo.name = '人工任务' + This.index
          }
          // 调用流程
          if (This.isCallActivity(bo)) {
            data = {
              multiInstance: 'false',
              sequential: 'false',
              subProcessDefinitionKey: null,
              subProcessDefinitionName: null,
              subProcessDefinitionId: null
            }
            bo.name = '调用子流程' + This.index
          }
          // 子流程
          if (This.isSubProcess(bo)) {
            bo.name = '子流程' + This.index
          }
          // 服务任务
          if (This.isServiceTask(bo)) {
            data = {
              taskListener: null
            }
            bo.name = '服务任务' + This.index
          }
          bo.data = data
        })
        eventBus.on('connection.added', function (evt, ele) {
          ele.element.businessObject.data = {
            expression: null
          }
        })
        eventBus.on('element.dblclick', function (evt, ele) {
          let element = ele.element
          switch (element.type) {
            case 'bpmn:Process':
              This.openProcessWin(element)
              break
            case 'bpmn:UserTask':
              This.openUserTaskWin(element)
              break
            case 'bpmn:ServiceTask':
              This.openServiceTaskWin(element)
              break
            case 'bpmn:CallActivity':
              This.openCallActivityWin(element)
              break
            case 'bpmn:SequenceFlow':
              This.openSequenceFlowWin(element)
              break
            default:
              This.openNoneWin(element)
              break
          }
        })
      })
    },

    // 工具栏
    // 保存及部署
    save (isDeployment) {
      let This = this
      This.bpmnModeler.saveXML({ format: true }, function (err, xml) {
        if (err) {
          return console.error('无法保存 BPMN 2.0 流程图', err)
        }
        let canvas = This.bpmnModeler.get('canvas')

        // 获取根节点
        let processEle = canvas.getRootElement()
        let flowElements = processEle.businessObject.flowElements
        let elements = {
          nodes: [],
          lines: []
        }
        if (typeof (flowElements) === 'undefined' || flowElements === null) {
          This.$Notice.error({
            title: '至少含有一个流程元素，无法部署保存'
          })
          return
        }
        for (let i = 0; i < flowElements.length; i++) {
          let ele = flowElements[i]
          let node = {}
          node.type = ele.$type.replace('bpmn:', '')
          node.key = ele.id
          node.name = ele.name == null ? null : ele.name
          node.data = ele.data == null ? null : ele.data

          if (ele.flowElements != null) {
            for (let j = 0; j < ele.flowElements.length; j++) {
              let subEle = ele.flowElements[i]
              let subNode = {}
              node.subElements = {
                nodes: [],
                lines: []
              }

              subNode.type = subEle.$type.replace('bpmn:', '')
              subNode.key = subEle.id
              subNode.name = subEle.name == null ? null : subEle.name
              subNode.data = subEle.data == null ? null : subEle.data

              if (This.isSequence(subEle)) {
                subNode.sourceKey = subEle.sourceRef.id
                subNode.targetKey = subEle.targetRef.id
                node.subElements.lines.push(subNode)
              } else {
                node.subElements.nodes.push(subNode)
              }
            }
          }

          if (This.isSequence(ele)) {
            node.sourceKey = ele.sourceRef.id
            node.targetKey = ele.targetRef.id
            elements.lines.push(node)
          } else {
            elements.nodes.push(node)
          }
        }
        This.bpmnModeler.saveSVG({ format: true }, function (err, svg) {
          let processData = processEle.businessObject.data
          if (typeof (processData) === 'undefined' || processData === null) {
            This.$Notice.error({
              title: '流程数据为空，无法部署保存'
            })
            return
          }
          let param = {
            process: {
              processDefinitionKey: processData.processDefinitionKey,
              name: processData.name === null ? This.processName : processData.name,
              processListener: processData.processListener,
              xml: xml,
              svg: svg
            },
            formList: processData.forms,
            elements: elements
          }
          if (isDeployment) {
            // 部署流程
            util.http.post('/api/activiti/bpmn/deployment', JSON.stringify(param), {
              headers: {
                'Content-Type': 'application/json;charset=UTF-8'
              }
            }).then(response => {
              if (response.data) {
                This.$Notice.success({
                  title: '部署成功'
                })
                // This.$router.push({
                //   name: 'model-management'
                // })
                This.closeMe()
              } else {
                This.$Notice.error({
                  title: '部署失败'
                })
              }
            }).catch((error) => {
              This.$Notice.error({
                title: '部署失败'
              })
            })
          } else {
            // 保存流程
            util.http.post('/api/activiti/bpmn/save', JSON.stringify(param), {
              headers: {
                'Content-Type': 'application/json;charset=UTF-8'
              }
            }).then(response => {
              if (response.data) {
                This.$Notice.success({
                  title: '保存成功'
                })
                // This.$router.push({
                //   name: 'model-management'
                // })
                This.closeMe()
                // This.$router.go(-1);
              } else {
                This.$Notice.error({
                  title: '保存失败'
                })
              }
            }).catch((error) => {
              This.$Notice.error({
                title: '保存失败'
              })
            })
          }
        })
      })
    },
    closeMe () {
      this.$store.commit('app/closePage', {
        vm: this,
        fromName: this.$route.name,
        toName: 'model-management'
      })
    },
    // 打开流程图
    openSvg () {
      let This = this
      this.bpmnModeler.saveSVG({ format: true }, function (err, svg) {
        This.$refs.FlowDiagram.modalVisible = true
        This.$refs.FlowDiagram.svg = svg
      })
    },
    // 下载流程图
    downloadSvg () {
      let This = this
      This.bpmnModeler.saveSVG({ format: true }, function (err, svg) {
        let canvas = This.bpmnModeler.get('canvas')
        let processEle = canvas.getRootElement()
        let data = new Blob([svg], { type: 'text/plain;charset=UTF-8' })
        let downloadUrl = window.URL.createObjectURL(data)
        let anchor = document.createElement('a')
        anchor.href = downloadUrl
        anchor.download = processEle.id + '.svg'
        anchor.click()
        window.URL.revokeObjectURL(data)
      })
    },

    // 流程属性
    openProcessWin (element) {
      this.$refs.ProcessWin.modalVisible = true
      if (element.businessObject.data.name === null) {
        element.businessObject.data.name = this.processName
      }
      this.$refs.ProcessWin.element = element
    },

    // 打开配置窗口：人工任务
    openUserTaskWin (element) {
      let formList = []
      // 获取表单列表
      let canvas = this.bpmnModeler.get('canvas')
      let processEle = canvas.getRootElement()
      let processData = processEle.businessObject.data
      if (typeof (processData) !== 'undefined') {
        let forms = processData.forms
        if (forms != null) {
          for (let i = 0; i < forms.length; i++) {
            let formItem = forms[i]
            formList.push({
              name: formItem,
              formId: formItem
            })
          }
        }
      }

      this.$refs.UserTaskWin.modalVisible = true
      this.$refs.UserTaskWin.element = element
      this.$refs.UserTaskWin.formList = formList
    },

    openServiceTaskWin (element) {
      this.$refs.ServiceTaskWin.modalVisible = true
      this.$refs.ServiceTaskWin.element = element
    },

    // 打开配置窗口：调用流程
    openCallActivityWin (element) {
      this.$refs.CallActivityWin.modalVisible = true
      this.$refs.CallActivityWin.element = element
    },

    // 打开配置窗口：连接线
    openSequenceFlowWin (element) {
      this.$refs.SequenceFlowWin.modalVisible = true
      this.$refs.SequenceFlowWin.element = element
    },

    // 打开配置窗口：无属性
    openNoneWin (element) {
      this.$refs.NoneWin.modalVisible = true
      this.$refs.NoneWin.element = element
    },

    // 其他相关
    initProcessData (processBo) {
      let This = this
      if (processBo.data !== null && JSON.stringify(processBo.data) !== '{}' && typeof (processBo.data) !== 'undefined') {
        return processBo.data
      }
      // 查找是否已存在数据
      if (This.process === null) {
        return {
          processDefinitionKey: processBo.id,
          name: null,
          processListener: null,
          forms: []
        }
      }

      let data = {
        processDefinitionKey: This.process.processDefinitionKey,
        name: This.process.name,
        processListener: This.process.processListener
      }
      let forms = []

      // util.ajax.get('/framework/bpm/getFormIdByProcessDefinitionId/' + This.process.processDefinitionId).then(response => {
      //     if (response.success && response.datas != null && response.datas.length > 0) {
      //         forms = response.datas;
      //     }
      // });
      data.forms = forms
      return data
    },
    initElementData (elementBo) {
      let This = this
      if (elementBo.data != null && JSON.stringify(elementBo.data) !== '{}') {
        return elementBo.data
      }
      // 查找是否已存在该环节数据
      if (This.process == null) {
        return {}
      }

      if (This.isStartEvent(elementBo) || This.isEndEvent(elementBo) || This.isGateway(elementBo)) {
        return {}
      }

      // 连接线
      if (This.isSequence(elementBo)) {
        for (let i = 0; i < This.lines.length; i++) {
          if (This.lines[i].lineKey === elementBo.id) {
            let line = This.lines[i]
            return {
              expression: line.expression
            }
          }
        }
      }
      // 人工任务
      else if (This.isUserTask(elementBo)) {
        for (let i = 0; i < This.nodes.length; i++) {
          if (This.nodes[i].nodeKey === elementBo.id) {
            let node = This.nodes[i]
            let data = {
              taskType: node.taskType == null ? 'user' : node.taskType,
              taskListener: node.taskListener,

              assignees: [],
              assigneeRule: [],

              multiInstance: node.multiInstance == null ? 'false' : node.multiInstance,
              sequential: node.sequential == null ? 'false' : node.sequential,
              multiStrategy: node.multiStrategy,
              decisionType: node.decisionType == null ? '1' : node.decisionType,
              votingType: node.votingType == null ? '1' : node.votingType,
              votes: node.votes == null ? '100' : node.votes,

              sendMail: node.sendMail == null ? 'false' : node.sendMail,
              formUrl: node.formUrl,
              formAuth: node.formAuth == null ? {} : node.formAuth
            }
            // assignees
            let assignees = node.assignees
            let assigneeRule = node.assigneeRule
            if (assignees != null) {
              // data.assignees = eval('(' + assignees + ')');
              data.assignees = JSON.parse(assignees)
            }
            if (assigneeRule != null) {
              // data.assigneeRule = eval('(' + assigneeRule + ')');
              data.assigneeRule = JSON.parse(assigneeRule)
            }
            return data
          }
        }
      }
      // 调用子流程
      else if (This.isCallActivity(elementBo)) {
        for (let i = 0; i < This.nodes.length; i++) {
          if (This.nodes[i].nodeKey === elementBo.id) {
            let node = This.nodes[i]
            return {
              multiInstance: node.multiInstance == null ? 'false' : node.multiInstance,
              sequential: node.sequential == null ? 'false' : node.sequential,
              subProcessDefinitionKey: node.subProcessDefinitionKey,
              subProcessDefinitionName: node.subProcessDefinitionName,
              subProcessDefinitionId: node.subProcessDefinitionId
            }
          }
        }
      }
      // 服务任务
      else if (This.isServiceTask(elementBo)) {
        for (let i = 0; i < This.nodes.length; i++) {
          if (This.nodes[i].nodeKey === elementBo.id) {
            let node = This.nodes[i]
            return {
              taskListener: node.taskListener
            }
          }
        }
      }
      return {}
    },
    isSubProcess (eleBusinessObj) {
      if (eleBusinessObj.$type === 'bpmn:SubProcess') {
        return true
      }
      return false
    },
    isStartEvent (eleBusinessObj) {
      if (eleBusinessObj.$type === 'bpmn:StartEvent') {
        return true
      }
      return false
    },
    isEndEvent (eleBusinessObj) {
      if (eleBusinessObj.$type === 'bpmn:EndEvent') {
        return true
      }
      return false
    },
    isGateway (eleBusinessObj) {
      let This = this
      if (This.isExclusiveGateway(eleBusinessObj) || This.isParallelGateway(eleBusinessObj)) {
        return true
      }
      return false
    },
    isExclusiveGateway (eleBusinessObj) {
      if (eleBusinessObj.$type === 'bpmn:ExclusiveGateway') {
        return true
      }
      return false
    },
    isParallelGateway (eleBusinessObj) {
      if (eleBusinessObj.$type === 'bpmn:ParallelGateway') {
        return true
      }
      return false
    },
    isUserTask (eleBusinessObj) {
      if (eleBusinessObj.$type === 'bpmn:UserTask') {
        return true
      }
      return false
    },
    isCallActivity (eleBusinessObj) {
      if (eleBusinessObj.$type === 'bpmn:CallActivity') {
        return true
      }
      return false
    },
    isServiceTask (eleBusinessObj) {
      if (eleBusinessObj.$type === 'bpmn:ServiceTask') {
        return true
      }
      return false
    },
    isSequence (eleBusinessObj) {
      if (eleBusinessObj.$type === 'bpmn:SequenceFlow') {
        return true
      }
      return false
    },
    resetZoom () {
      let canvas = this.bpmnModeler.get('canvas')
      canvas.zoom('fit-viewport')
    },
    back () {
      this.$router.push({
        name: 'model-management'
      })
    },
    refreshElement (element) {
      let directEditing = this.bpmnModeler.get('directEditing')
      directEditing.activate(element)
      directEditing.complete()
    }

  }
}
</script>

<style scoped>
    /*左边工具栏以及编辑节点的样式*/
    @import './lib/bpmn/diagram-js.css';
    @import './lib/bpmn/bpmn.css';
    @import './lib/bpmn/bpmn_font.css';
    @import "https://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css";

    .section-height {
        height: calc(100vh - 130px);
    }

    div.djs-context-pad.open .entry {
        color: #00bbff;
    }

    a.link-btn:hover {
        color: #00bbff;
    }

    .content {
        background: #f2f2f2;
    }

    .io-help, .io-legal, .io-import-export {
        top: 130px;
    }
</style>
