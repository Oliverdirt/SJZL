<template>
    <div>
        <Modal
                v-model="modalVisible"
                title="属性设置"
                :mask-closable="false"
                :closable = "false"
                @on-visible-change="change">
            <div style="padding: 20px 30px 20px 0;">
                <Form  ref="formCall"  :model="process" :label-width="100"  :rules="ruleValidate">
                    <FormItem label="环节">
                        <Input v-model="process.businessObject.id" disabled=""  :maxlength="50"></Input>
                    </FormItem>
                    <FormItem label="名称" prop="businessObject.name">
                        <Input v-model="process.businessObject.name"  :maxlength="40"></Input>
                    </FormItem>
                    <FormItem label="关联流程" class="call-append">
                        <Input v-model="subProcessDefinitionName" readonly=""  :maxlength="40">
                            <Button slot="append" icon="md-search" title="选择调用的流程" @click="showProcessList"></Button>
                            <Button slot="append" icon="md-eye" title="查看流程" @click="viewProcess"></Button>
                            <Button slot="append" icon="md-trash" title="清除" @click="clearProcess"></Button>
                        </Input>
                    </FormItem>
                    <!--<FormItem label="多实例">-->
                        <!--<RadioGroup v-model="process.multiInstance">-->
                            <!--<Radio label="false">否</Radio>-->
                            <!--<Radio label="true">是</Radio>-->
                        <!--</RadioGroup>-->
                    <!--</FormItem>-->
                    <!--<FormItem label="执行顺序" v-show="showMultiItem">-->
                        <!--<RadioGroup v-model="process.sequential">-->
                            <!--<Radio label="false">同时进行</Radio>-->
                            <!--<Radio label="true">顺序进行</Radio>-->
                        <!--</RadioGroup>-->
                    <!--</FormItem>-->
                </Form>
            </div>
            <div slot="footer">
                <Button size="large" @click="cancel">取消</Button>
                <Button type="primary" size="large" @click="ok">确定</Button>
            </div>
        </Modal>
        <flow-diagram ref="FlowDiagram"></flow-diagram>
        <process-list-win ref="ProcessListWin" @initProcess="initProcess"></process-list-win>
    </div>

</template>

<script>
import FlowDiagram from './FlowDiagram'
import ProcessListWin from './ProcessListWin'

export default {
  name: 'CallActivityWin',
  components: { ProcessListWin, FlowDiagram },
  data () {
    return {
      modalVisible: false,
      element: null,
      process: {
        businessObject: {
          id: null,
          name: null,
          data: {}
        },
        // attr
        multiInstance: 'false',
        sequential: 'false'

      },
      showMultiItem: false,
      subProcessDefinitionKey: null,
      subProcessDefinitionName: null,
      subProcessDefinitionId: null,
      ruleValidate: {
        'businessObject.name': [
          {
            type: 'string',
            pattern: /^[A-Za-z0-9_\u4e00-\u9fa5]+$/,
            message: '特殊字符只允许下划线',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  computed: {
    multiInstance () {
      return this.process.multiInstance
    }
  },
  watch: {
    element: function (value) {
      if (!this.modalVisible) return
      if (value === null) return
      // this.process.businessObject = value.businessObject;
      this.process.businessObject.id = value.businessObject.id
      this.process.businessObject.name = value.businessObject.name
      this.subProcessDefinitionKey = value.businessObject.data.subProcessDefinitionKey
      this.subProcessDefinitionName = value.businessObject.data.subProcessDefinitionName
      this.subProcessDefinitionId = value.businessObject.data.subProcessDefinitionId
      this.process.multiInstance = value.businessObject.data.multiInstance === 1 ? 'true' : 'false'
      this.process.sequential = value.businessObject.data.sequential === 1 ? 'true' : 'false'
    },
    multiInstance: function (n) {
      if (n === 'false' || n === false) {
        this.showMultiItem = false
      } else {
        this.showMultiItem = true
      }
    }
  },
  methods: {
    showProcessList () {
      if (this.subProcessDefinitionKey !== null) {
        this.$refs.ProcessListWin.checked = { processDefinitionId: this.subProcessDefinitionId }
      }
      this.$refs.ProcessListWin.pageSize = 4
      this.$refs.ProcessListWin.modalVisible = true
    },
    viewProcess () {
      if (this.subProcessDefinitionKey == null) return
      this.$refs.FlowDiagram.processDefinitionKey = this.subProcessDefinitionKey
      this.$refs.FlowDiagram.modalVisible = true
    },
    clearProcess () {
      this.subProcessDefinitionKey = null
      this.subProcessDefinitionName = null
      this.subProcessDefinitionId = null
    },
    initProcess (value) {
      if (value === null) return
      this.subProcessDefinitionKey = value.key
      this.subProcessDefinitionName = value.name
      this.subProcessDefinitionId = value.processDefinitionId
    },
    ok () {
      let This = this
      this.$refs['formCall'].validate((valid) => {
        if (valid) {
          let elements = This.element
          elements.businessObject.id = This.process.businessObject.id
          elements.businessObject.name = This.process.businessObject.name
          elements.businessObject.data.multiInstance = This.process.multiInstance
          elements.businessObject.data.sequential = This.process.sequential
          elements.businessObject.data.subProcessDefinitionKey = This.subProcessDefinitionKey
          elements.businessObject.data.subProcessDefinitionName = This.subProcessDefinitionName
          elements.businessObject.data.subProcessDefinitionId = This.subProcessDefinitionId
          This.$emit('refreshElement', elements)
          This.modalVisible = false
        }
      })
    },
    change (flag) {
      if (!flag) {
        this.element = null
        this.process = {
          businessObject: {
            id: null,
            name: null,
            data: {}
          },
          // attr
          multiInstance: 'false',
          sequential: 'false'

        }
        this.showMultiItem = false
        this.subProcessDefinitionKey = null
        this.subProcessDefinitionName = null
        this.subProcessDefinitionId = null
      }
    },
    cancel () {
      let This = this
      // this.$refs['formCall'].validate((valid) => {
      //     if (valid) {
      This.modalVisible = false
      //     }else {
      //         This.$Message.warning('先处理表单验证错误，才能关闭');
      //     }
      // });
    }
  }
}
</script>

<style scoped>
    .ivu-input-group-append .ivu-btn, .ivu-input-group-prepend .ivu-btn {
        margin: -6px 0;
        border-right: 1px solid #dcdee2;
    }

    .ivu-input-group-append .ivu-btn:last-child {
        border-right: none;
    }
</style>
<style>
    .call-append .ivu-input-group-append {
        padding: 4px 0 !important;
    }
</style>
