<template>
    <div>
        <Modal
                v-model="modalVisible"
                title="属性设置"
                :mask-closable="false"
                :closable = "false"
                width="600"
                :styles="{top: '60px'}"
                @on-visible-change="change">
            <div style="padding: 20px 30px 20px 0;max-height: 60vh;overflow: auto">
                <Form ref="formUser" :model="taskForm" :label-width="100" :rules="ruleValidate">
                    <FormItem label="ID">
                        <Input v-model="taskForm.businessObject.id" readonly></Input>
                    </FormItem>
                    <FormItem label="名称" prop="businessObject.name">
                        <Input v-model="taskForm.businessObject.name" :maxlength="40"></Input>
                    </FormItem>
                    <FormItem label="任务类型">
                        <RadioGroup v-model="taskForm.taskType">
                            <Radio label="user">个人任务</Radio>
                            <Radio label="pub">竞争任务</Radio>
                        </RadioGroup>
                    </FormItem>
                    <FormItem label="任务监听" prop="taskListener">
                        <Input v-model="taskForm.taskListener" placeholder="JavaBean" :maxlength="255"></Input>
                    </FormItem>
                    <FormItem label="候选人策略">
                        <CheckboxGroup v-model="taskForm.assignees">
                            <Checkbox v-for="item in assigneesList" :label="item.key">{{item.name}}</Checkbox>
                        </CheckboxGroup>
                    </FormItem>
                    <FormItem label="点击设置规则" v-show="showAssignees">
                        <div style="display: flex; flex-wrap: wrap;">
                            <Button v-for="item in taskForm.assigneeRule" icon="md-contact" shape="circle"
                                    @click="openAssigneeRule(item)">{{
                                item.name }}
                                <Icon v-if="item.value !== null && item.value !== []" type="ios-checkmark-circle"
                                      color="#19be6b"/>
                            </Button>
                        </div>
                    </FormItem>
                    <FormItem label="多实例任务" v-show="showMulti">
                        <RadioGroup v-model="taskForm.multiInstance">
                            <Radio label="false">否</Radio>
                            <Radio label="true">是</Radio>
                        </RadioGroup>
                    </FormItem>
                    <FormItem label="执行顺序" v-show="showMultiItem">
                        <RadioGroup v-model="taskForm.sequential">
                            <Radio label="false">并行</Radio>
                            <Radio label="true">串行</Radio>
                        </RadioGroup>
                    </FormItem>
                    <FormItem label="多实例策略" v-show="showMultiItem">
                        <RadioGroup v-model="taskForm.multiStrategy">
                            <Radio v-for="item in multiList" :label="item.key">{{item.name}}</Radio>
                        </RadioGroup>
                    </FormItem>
                    <FormItem label="决策方式" v-show="showMultiItem">
                        <RadioGroup v-model="taskForm.decisionType">
                            <Radio label="1">通过</Radio>
                            <Radio label="0">拒绝</Radio>
                        </RadioGroup>
                    </FormItem>
                    <FormItem label="投票类型" v-show="showMultiItem">
                        <RadioGroup v-model="taskForm.votingType">
                            <Radio label="1">百分比</Radio>
                            <Radio label="0">绝对票数</Radio>
                        </RadioGroup>
                    </FormItem>
                    <FormItem label="票数或比率" v-show="showMultiItem">
                        <Input v-model="taskForm.votes" :maxlength="30"></Input>
                    </FormItem>
                    <FormItem label="表单URL" prop="formUrl">
                        <Input v-model="taskForm.formUrl" :maxlength="120"></Input>
                    </FormItem>
                </Form>
            </div>
            <div slot="footer">
                <Button size="large" @click="cancel">取消</Button>
                <Button type="primary" size="large" @click="ok">确定</Button>
            </div>
        </Modal>
        <user-rule-set-win ref="UserRuleSetWin" @refreshAssigneeRule="refreshAssigneeRule"></user-rule-set-win>
        <role-rule-set-win ref="RoleRuleSetWin" @refreshAssigneeRule="refreshAssigneeRule"></role-rule-set-win>
    </div>

</template>

<script>
import util from '@/libs/util.js'
import UserRuleSetWin from '../win/UserRuleSetWin'
import RoleRuleSetWin from '../win/RoleRuleSetWin'

export default {
  name: 'UserTaskWin',
  components: { RoleRuleSetWin, UserRuleSetWin },
  data () {
    return {
      modalVisible: false,
      processDefinitionKey: null,
      element: null,
      formList: [], // 表单列表
      formAuth: {}, // 表单配置数据

      assigneesList: null,
      multiList: null,
      defaultMultiStrategy: null,
      showAssignees: false,
      showMulti: true,
      showMultiItem: false,
      taskForm: {
        businessObject: {
          id: null,
          name: null,
          data: {}
        },
        // attrs
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
        formUrl: '',
        assigneesWatch: []
      },
      ruleValidate: {
        'businessObject.name': [
          {
            type: 'string',
            pattern: /^[A-Za-z0-9_\u4e00-\u9fa5]+$/,
            message: '特殊字符只允许下划线',
            trigger: 'blur'
          }
        ],
        formUrl: [
          { required: true, message: '表单URL不能为空', trigger: 'change' },
          {
            type: 'string',
            pattern: /^[A-Za-z0-9\/]+$/,
            message: '只允许字母数字和斜杠',
            trigger: 'blur'
          }
        ],
        taskListener: [
          {
            max: 100,
            message: '最多允许100个字符',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  computed: {
    taskType () {
      return this.taskForm.taskType
    },
    assignees () {
      return this.taskForm.assignees
    },
    multiInstance () {
      return this.taskForm.multiInstance
    },
    businessObject () {
      return this.taskForm.businessObject
    }

  },
  watch: {
    processDefinitionKey: function (value) {
      if (!this.modalVisible) return
      let This = this
      // 获取候选人策略
      util.http.get('/api/activiti/query/getCandidateStrategyList').then(response => {
        This.assigneesList = response.data
        This.taskForm.assignees = This.taskForm.assigneesWatch
        // This.taskForm.assignees = This.taskForm.businessObject.data.assignees;
      })

      // 获取多实例策略
      util.http.get('/api/activiti/query/getMultiInstanceStrategyList').then(response => {
        This.multiList = response.data
        if (response.data.length > 0 && This.taskForm.multiStrategy === null) {
          This.taskForm.multiStrategy = response.data[0].key
        }
      })
    },
    element: function (value) {
      if (!this.modalVisible) return
      if (value === null) return
      this.getDefaultMultiStrategy(value)

      // this.taskForm.taskType = value.businessObject.data.taskType;
      this.taskForm.taskListener = value.businessObject.data.taskListener
      this.taskForm.assigneeRule = value.businessObject.data.assigneeRule
      if (value.businessObject.data.multiInstance === 1 || value.businessObject.data.multiInstance === 0) {
        this.taskForm.multiInstance = value.businessObject.data.multiInstance === 1 ? 'true' : 'false'
      } else {
        this.taskForm.multiInstance = value.businessObject.data.multiInstance
      }

      if (value.businessObject.data.sequential === 1 || value.businessObject.data.sequential === 0) {
        this.taskForm.sequential = value.businessObject.data.sequential === 1 ? 'true' : 'false'
      } else {
        this.taskForm.sequential = value.businessObject.data.sequential
      }
      this.taskForm.multiStrategy = value.businessObject.data.multiStrategy
      this.taskForm.decisionType = value.businessObject.data.decisionType.toString()
      this.taskForm.votingType = value.businessObject.data.votingType.toString()
      this.taskForm.votes = value.businessObject.data.votes
      this.taskForm.formUrl = value.businessObject.data.formUrl
      this.formAuth = value.businessObject.data.formAuth
      // this.taskForm.businessObject = value.businessObject;
      this.processDefinitionKey = value.businessObject.$parent.id
      this.taskForm.businessObject.id = value.businessObject.id
      this.taskForm.businessObject.name = value.businessObject.name
      this.taskForm.assigneesWatch = value.businessObject.data.assignees === null || value.businessObject.data.assignees.length === 0 ? [] : value.businessObject.data.assignees
      this.taskForm.assigneeRule = value.businessObject.data.assigneeRule === null || value.businessObject.data.assigneeRule.length === 0 ? [] : value.businessObject.data.assigneeRule
    },
    businessObject: function (value) {
      if (!this.modalVisible) return
      this.processDefinitionKey = value.$parent.id
    },
    taskType: function (n) {
      if (!this.modalVisible) return
      if (n === 'pub') {
        this.showMulti = false
        this.taskForm.multiInstance = 'false'
        this.taskForm.multiStrategy = this.defaultMultiStrategy.key
        this.taskForm.sequential = 'false'
        this.taskForm.decisionType = '1'
        this.taskForm.votingType = '1'
        this.taskForm.votes = '100'
      } else {
        this.showMulti = true
      }
    },
    assignees: function (n) {
      if (!this.modalVisible) return
      if (n.length === 0) {
        this.showAssignees = false
      } else {
        this.showAssignees = true
        // 设置规则项
        let ruleArray = []
        for (let i in n) {
          let assignee = this.getAssignee(n[i])
          let rule = this.getExistedRule(assignee)
          if (rule != null) {
            ruleArray.push(rule)
          } else {
            ruleArray.push(this.getBlankRule(assignee))
          }
        }
        this.taskForm.assigneeRule = ruleArray
      }
    },
    multiInstance: function (n) {
      if (!this.modalVisible) return
      if (n === 'false' || n === false) {
        this.showMultiItem = false
      } else {
        this.showMultiItem = true
      }
    }
  },
  methods: {
    getDefaultMultiStrategy (value) {
      let This = this
      util.http.get('/api/activiti/query/getDefaultMultiInstanceStrategy').then(response => {
        This.defaultMultiStrategy = response.data
        This.taskForm.taskType = value.businessObject.data.taskType
      })
    },
    getAssignee (key) {
      for (let i in this.assigneesList) {
        let item = this.assigneesList[i]
        if (item.key === key) {
          return item
        }
      }
      return null
    },
    getBlankRule (item) {
      return {
        key: item.key,
        name: item.name,
        url: item.url,
        value: null
      }
    },
    getExistedRule (item) {
      if (item === null) return null
      if (this.taskForm.assigneeRule.length === 0) return null
      for (let i in this.taskForm.assigneeRule) {
        let rule = this.taskForm.assigneeRule[i]
        if (rule.key === item.key) {
          return rule
        }
      }
      return null
    },
    openAssigneeRule (rule) {
      if (rule.key === 'com.ctsi.activiti.candidate.strategy.impl.UserCandidateStrategy') {
        this.$refs.UserRuleSetWin.modalVisible = true
        this.$refs.UserRuleSetWin.user = rule
      }
      if (rule.key === 'com.ctsi.activiti.candidate.strategy.impl.RoleCandidateStrategy') {
        this.$refs.RoleRuleSetWin.modalVisible = true
        this.$refs.RoleRuleSetWin.role = rule
      }
    },
    refreshAssigneeRule (value) {
      console.log(this.taskForm.assigneeRule)
    },
    ok () {
      let This = this
      this.$refs['formUser'].validate((valid) => {
        if (valid) {
          let elements = This.element
          elements.businessObject.name = This.taskForm.businessObject.name
          elements.businessObject.data.taskType = This.taskForm.taskType
          elements.businessObject.data.taskListener = This.taskForm.taskListener

          elements.businessObject.data.assignees = This.taskForm.assignees === null ? [] : This.taskForm.assignees
          elements.businessObject.data.assigneeRule = This.taskForm.assigneeRule == null ? [] : This.taskForm.assigneeRule

          elements.businessObject.data.multiInstance = This.taskForm.multiInstance
          elements.businessObject.data.sequential = This.taskForm.sequential
          elements.businessObject.data.multiStrategy = This.taskForm.multiStrategy
          elements.businessObject.data.decisionType = This.taskForm.decisionType
          elements.businessObject.data.votingType = This.taskForm.votingType
          elements.businessObject.data.votes = This.taskForm.votes
          elements.businessObject.data.formUrl = This.taskForm.formUrl
          This.$emit('refreshElement', elements)
          This.modalVisible = false
        }
      })
    },
    change (flag) {
      if (!flag) {
        this.processDefinitionKey = null
        this.element = null
        this.formList = [] // 表单列表
        this.formAuth = {} // 表单配置数据

        this.assigneesList = null
        this.multiList = null
        this.defaultMultiStrategy = null
        this.showAssignees = false
        this.showMulti = true
        this.showMultiItem = false
        this.taskForm = {
          businessObject: {
            id: null,
            name: null,
            data: {}
          },
          // attrs
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
          formUrl: ''
        }
      }
    },
    cancel () {
      let This = this
      // this.$refs['formUser'].validate((valid) => {
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

</style>
