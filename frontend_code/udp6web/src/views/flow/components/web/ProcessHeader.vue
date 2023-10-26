<template>
    <div>
        <Row>
            <Col span="24">
                <div class="action-btn">
                    <div>
                        <div v-if="buttonFlag" style="display: inline-block">
                            <Button style="margin-left: 5px" type="primary" v-for="item in actionsButtons"
                                    v-if="item.nodeKey === currentNodeKey"
                                    @click="submitTask(item)">{{item.name}}
                            </Button>
                        </div>
                        <div v-else style="display: inline-block">
                            <Button style="margin-left: 5px" type="primary" v-for="item in buttons"
                                    @click="submitTask(item)">{{item.name}}
                            </Button>
                        </div>

                        <Button @click="viewHistory">流转信息</Button>
                        <Button @click="viewProcess">查看流程图</Button>
                    </div>
                </div>
            </Col>

        </Row>
        <history-task ref="historyTask"></history-task>
        <flow-diagram ref="FlowDiagram"></flow-diagram>

        <candidate-win ref="CandidateWin" @refreshCandidate="refreshCandidate"></candidate-win>
    </div>

</template>

<script>
import comment from './eventComment'
import Util from '@/libs/util.js'
import HistoryTask from '../win/HistoryTask'
import FlowDiagram from '../model/RuntimeFlowDiagram'

// 下一节点为串行多实例节点时，要指定处理人引用此选人组件
import CandidateWin from '../web/CandidateWin'

export default {
  name: 'processHeader',
  components: { FlowDiagram, HistoryTask, CandidateWin },
  data () {
    return {
      buttonFlag: false,
      currentNodeKey: null,
      buttons: [],
      processInstanceId: null,
      taskId: null,
      nextHandlers: [],
      lineKey: null,
      callActivityOutLineKey: null,
      comment: '',
      expressionMap: null
    }
  },
  props: {
    // 设定任务节点key，提交前打开人员选择框，进行指定nextHandle提交
    userNodeKey: {
      type: Array,
      default () {
        return []
      }
    },

    // 自定义参数（会持久化）
    persistentMap: {
      type: Object,
      default () {
        return null
      }
    },

    // 流程操作按钮
    actionsButtons: {
      type: Array,
      default () {
        return []
      }
    },

    // 流程提交之前的钩子，若返回 false 则停止提交流程
    beforeSubmit: Function,

    // 流程提交之后钩子
    afterSubmit: {
      type: Function,
      default () {
        return {}
      }
    },

    // 自定义操作成功返回路由地址，采用路由name名称去匹配，默认是返回上一页
    backPathName:
        {
          type: String,
          default () {
            return null
          }
        }

  },
  watch: {
    actionsButtons (arr) {
      let This = this
      if (this.currentNodeKey !== null) {
        for (let node of arr) {
          if (node.nodeKey === this.currentNodeKey) {
            This.buttonFlag = true
          }
        }
      }
    }
  },
  mounted () {
    if (typeof (this.$route.query.processInstanceId) !== 'undefined' && this.$route.query.processInstanceId !== null && this.$route.query.processInstanceId !== '') {
      this.processInstanceId = this.$route.query.processInstanceId
    }
    if (typeof (this.$route.query.taskId) !== 'undefined' && this.$route.query.taskId !== null && this.$route.query.taskId !== '') {
      this.taskId = this.$route.query.taskId
    }

    if (this.processInstanceId !== null && this.taskId !== null) {
      this.getButtons()

      let This = this
      Util.http.get('/api/activiti/query/getTaskById/' + This.taskId).then(response => {
        let node = response.data
        This.currentNodeKey = node.key
        for (let node of This.actionsButtons) {
          if (node.nodeKey === This.currentNodeKey) {
            This.buttonFlag = true
          }
        }
      })
    }
    comment.$on('commentChange', (comment) => {
      this.comment = comment
    })
  },
  methods: {
    getButtons () {
      let This = this
      Util.http.get('/api/activiti/query/getLineList/' + this.taskId)
        .then(function (response) {
          This.buttons = response.data
        })
        .catch(function (error) {
          console.log(error)
        })
    },

    submitTask (item) {
      // 执行流程提交之前的钩子函数，如果返回false则终止提交
      if (this.beforeSubmit) {
        const before = this.beforeSubmit()
        if (before !== false) {
          this.executeSubmitTask(item)
        }
      } else {
        this.executeSubmitTask(item)
      }
    },

    executeSubmitTask (item) {
      this.lineKey = typeof (item.lineKey) === 'undefined' ? null : item.lineKey
      this.callActivityOutLineKey = typeof (item.callActivityOutLineKey) === 'undefined' ? null : item.callActivityOutLineKey
      this.expressionMap = item.expressionMap

      let This = this
      Util.http.get('/api/activiti/query/getTaskById/' + This.taskId).then(response => {
        let node = response.data
        if (This.userNodeKey.length > 0) {
          for (let key of This.userNodeKey) {
            if (node.key === key) {
              This.$refs.CandidateWin.lineKey = item.lineKey
              This.$refs.CandidateWin.processDefinitionId = item.processDefinitionId
              This.$refs.CandidateWin.processInstanceId = This.processInstanceId
              This.$refs.CandidateWin.modalVisible = true
            } else {
              This.completeTask()
            }
          }
        } else {
          This.completeTask()
        }
      })
    },

    refreshCandidate (arr) {
      this.nextHandlers = arr
      if (this.nextHandlers.length > 0) {
        this.completeTask()
      }
    },

    completeTask () {
      let This = this
      let param = {
        data: {
          taskId: This.taskId,
          nextHandlers: This.nextHandlers,
          lineKey: This.lineKey,
          callActivityOutLineKey: This.callActivityOutLineKey,
          comment: This.comment,
          formDataList: [],
          persistentMap: This.persistentMap,
          expressionMap: This.expressionMap
        }

      }

      Util.http.post('/api/activiti/core/complete', JSON.stringify(param), {
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      }).then(response => {
        if (response.data) {
          // 执行流程提交完成之后钩子
          This.afterSubmit()

          This.$Notice.success({
            title: '操作成功'
          })
          // if (This.backPathName !== null) {
          //   This.$router.push({
          //     name: This.backPathName
          //   })
          // } else {
          //   This.$router.go(-1)
          // }
          this.$store.commit('app/closePage', {
            vm: this,
            fromName: this.$route.name,
            toName: 'todo-list'
          })
        } else {
          This.$Notice.error({
            title: '操作失败'
          })
        }
      }).catch((error) => {
        This.$Notice.error({
          title: '操作失败'
        })
      })
    },
    viewProcess () {
      if (this.processInstanceId === null) {
        this.$Notice.error({
          title: 'processInstanceId为空'
        })
        return
      }
      this.$refs.FlowDiagram.modalVisible = true
      this.$refs.FlowDiagram.processInstanceId = this.processInstanceId
    },
    viewHistory () {
      if (this.processInstanceId === null) {
        this.$Notice.error({
          title: 'processInstanceId为空'
        })
        return
      }
      this.$refs.historyTask.modalVisible = true
      this.$refs.historyTask.processInstanceId = this.processInstanceId
    }
  }
}
</script>

<style scoped>
    .action-btn {
        display: flex;
        justify-content: flex-end;
    }
</style>
