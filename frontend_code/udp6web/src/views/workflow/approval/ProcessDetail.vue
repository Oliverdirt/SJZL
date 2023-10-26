<template>
  <div class="ProcessDetail">
    <div>
      <div v-if="showVueComponent">
        <!-- 这里直接展示formUrl参数指向的vue文件 -->
        <component
          :processDefinitionId="processDefinitionId" :parentProcessInstanceId="parentProcessInstanceId" :taskId="taskId" :processInstanceId="processInstanceId"
          :is="testComponent"
          @closed="closed"
        />
      </div>
      <div v-else class="processTop">
        <!--    表单    -->
        <div class="applyForm">
          <p class="title">申请信息</p>
          <div class="formBox">
            <VFormRender ref="preForm" :form-json="formJson" :preview-state="true" :form-data="testFormData"
                        v-if="addDialogVisible">
            </VFormRender>
          </div>
        </div>
        <!--    审批任务 -->
        <div v-if="pageType === '流程处理'" class="taskBox">
          <p class="title">审批任务</p>
          <div class="spFromBox">
            <el-form :model="approvalJson" :rules="rules" ref="approvalJson" size="small">
              <el-form-item label="处理意见" prop="advice">
                <el-input type="textarea" v-model="approvalJson.advice" placeholder="请输入处理意见"></el-input>
              </el-form-item>
              <el-form-item>
                <div class="buttonAll">
                  <el-button type="primary" v-for="(item, i) in exitRoute" :key="'exitRoute' + i"
                            @click="clickHandleBtnFn(i)">{{ item.name }}
                  </el-button>
                  <el-button type="primary" v-for="(item, index) of this.dataArr" @click="buttonHandle(item)" :key="index">{{ item.operationName }}</el-button>
                </div>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <div v-else>
          <img style="width: 330px;margin-left: 50px;" src="../images/image 29.svg" alt="">
        </div>
      </div>
    </div>
    <!--审批-->
    <div>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="审批记录" name="first">
          <div>
            <p style="font-weight: bold;padding: 0px 15px 10px 15px;">审批记录:</p>
            <el-table :data="approveRecords" style="width: 100%" max-height="300">
                <el-table-column prop="name" label="任务" align="center">
                </el-table-column>
                <el-table-column prop="assigneeUser.nickname" label="审批人" align="center">
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" align="center">
                </el-table-column>
                <el-table-column prop="endTime" label="结束时间：" align="center">
                </el-table-column>
                <el-table-column prop="reason" label="审批意见" align="center">
                </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
        <el-tab-pane label="流程图" name="second">
          <p style="font-weight: bold;padding: 0px 15px;">流程图:</p>
          <div>
            <ProcessViewer :processInstanceData="processInstance" key="designer" v-model="bpmnXML"
                          v-bind="bpmnControlForm" :activityData="activityList" :taskData="tasks"/>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    <!--转办指派弹框-->
    <el-dialog :title="dealDialogTitle" :visible.sync="dialogVisible" :append-to-body="true" width="30%">
      <div style="display: flex; align-items: center; padding: 25px 15px">
        <el-form :model="chiosePerson" :rules="personRules" ref="chiosePerson" size="small" label-width="100px">
          <el-form-item label="选择人员" prop="personVal">
            <el-select ref="userSelect" v-model="chiosePerson.personVal" filterable>
              <el-option v-for="(option, index) in peroptions" :value="option.value" :key="option.value + index"
                         :label="option.label"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmeZbCy">确 定</el-button>
      </span>
    </el-dialog>
    <!--跳转指定弹框-->
    <el-dialog :title="dealDialogTitle" :visible.sync="jumpVisible" :append-to-body="true" width="30%">
      <div style="display: flex; align-items: center; padding: 25px 15px">
        <el-form :model="chioseTask" :rules="personRules" ref="chioseTask" size="small" label-width="100px">
          <el-form-item label="选择节点" prop="taskVal">
            <el-select ref="userSelect" v-model="chioseTask.taskVal" filterable>
              <el-option v-for="(option, index) in tabsList" :value="option.value" :key="option.value + index"
                         :label="option.label"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="jumpVisible = false">取 消</el-button>
        <el-button type="primary" @click="jumpSave">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import VFormRender from '@/views/lowcode/vformPro/components/form-render/index'
import ProcessViewer from '@/views/workflow/approval/ProcessViewer'
import request from '@/api/workflow/request'

const loadComponent = url => import(`@/views${url}`)

export default {
  name: 'ProcessDetail',
  components: {
    VFormRender,
    ProcessViewer
  },
  props: {
    detaileQueryParam: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  watch: {
    $route: {
      handler (to, from) {
        from.path === '/person' ? this.initPage() : ''
      }
    },
    // 监控变量的变化来动态引入组件
    activeUrl: {
      handler (value) {
        loadComponent(value).then(component => {
          this.testComponent = component.default
        })
      },
      immediate: true
    }
  },
  data () {
    return {
      activeName: 'first',
      dataArr: [],
      jumpVisible: false,
      isShowHandleBtn: false,
      showVueComponent: false,
      activeUrl: '',
      testComponent: null,
      approvalJson: {
        advice: '',
        personneIds: ''
      },
      tasks: [],
      userOptions: [],
      exitRouteOne: '',
      exitRouteTwo: '',
      exitRouteThree: '',
      bpmnXML: '',
      formJson: {},
      addDialogVisible: false,
      testFormData: {},
      formId: '',
      processInstanceId: '',
      processDefinitionId: '',
      parentProcessInstanceId: '',
      taskDefinitionKey: '',
      isDisableFields: [],
      pageType: '',
      activityList: [],
      taskId: '',
      approveRecords: [],
      dialogVisible: false,
      chiosePerson: {
        personVal: ''
      },
      chioseTask: {
        taskVal: ''
      },
      tabsList: [],
      peroptions: [{ label: '张三', value: '1' }],
      dealDialogTitle: '转办',
      processName: '',
      createTime: '',
      bpmnControlForm: {
        prefix: 'activiti'
      },
      rules: {
        advice: [
          {
            required: true,
            message: '请输入处理意见',
            trigger: 'blur',
            transform: (value) => value && value.trim()
          }
        ]
      },
      personRules: {
        personVal: [{ required: true, message: '请选择人员', trigger: 'change' }],
        taskVal: [{ required: true, message: '请选择节点', trigger: 'change' }]
      },
      processDefinitionKey: '',
      isShowZbBtn: false,
      isShowCyBtn: false,
      isShowZzBtn: false,
      isShowClrBtn: false,
      processInstance: {}
    }
  },
  mounted () {
    this.initPage()
    // this.handleClick({ name: 'one' })
  },
  created () {
    this.exitRoute()
    this.personFn()
    // this.$http
    //   .get('/api/lowcode/customize/cscpCustomizeVforms/' + this.detaileQueryParam.formId)
    request.getDataByFormId(this.detaileQueryParam.formId)
      .then((res) => {
        if (res.data.formType === '2') {
          this.activeUrl = res.data?.formUrl
          this.showVueComponent = true
        }
      })
      .catch(() => {
        this.$message({
                message: '行数据查询失败!',
                type: 'error',
                customClass: 'messageIndex'
          })
      })
    // this.$http.get(`/api/flow/multi/isMulti?taskKey=${this.detaileQueryParam.taskCode}`)
    request.isMultiBytaskKey(this.detaileQueryParam.taskCode)
      .then((res) => {
        if (res.data) { // 是多实例流程
          this.isShowHandleBtn = res.data.result
        }
      }).catch(() => {
      this.$message({
                message: '查询失败!',
                type: 'error',
                customClass: 'messageIndex'
          })
    })
  },

  methods: {
    queryList () {
    let params = {
      page: 1,
      size: 10000
    }
    request.getCscpFlowBpmnPageList({ params: params })
      .then((res) => {
        this.modelId = res.data.result.list.filter(item => item.name == this.processName)[0].id
        this.jumpFn(this.modelId)
      })
      .catch(() => {
        this.$Message.error('列表查询失败')
      })
  },
    // 跳转指定节点
    jumpFn(row){
      let params = {modelId : row}
      request.getRuleList({ params })
        .then((res) => {
          res?.data?.result.map((item) => (item.taskDefinitionFlag = row.key))
          this.userRuleData = res.data?.result
          this.tabsList = this.userRuleData?.map((item) => {
            let obj = {}
            obj.id = item.id
            obj.label = item.taskDefinitionName
            obj.value = item.taskDefinitionKey
            return obj
          })
        })
        .catch(() => {
          this.$Message.error('任务表查询失败')
        })
    },
    // 跳转指定弹框
    jumpSave () {
      this.$refs.chioseTask.validate((valid) => {
        if (!valid) return
        this.params.userTaskKey = this.chioseTask.taskVal
        this.$http.post('/api/flowHandle/operate',this.params).then((res)=>{
            this.jumpVisible = false
            this.$emit('reloadPage')
          }).catch(()=>{
            this.$Message.error('操作失败')
          })
      })
    },
    // 转办或指派
    confirmeZbCy () {
      this.$refs.chiosePerson.validate((valid) => {
        if (!valid) return
        this.params.userId = this.chiosePerson.personVal
        this.params.nextHandler = this.chiosePerson.personVal
        this.$http.post('/api/flowHandle/operate',this.params).then((res)=>{
            this.dialogVisible = false
            this.$emit('reloadPage')
          }).catch(()=>{
            this.$Message.error('操作失败')
          })
      })
    },
    buttonHandle(row){
      // 同意、拒绝、办理 approve  refuse  submit
      // 跳转指定节点 jumpWantTask
      // 委托、转办、指派 delegateTask  transferTask  setNextTaskUser 
      // 退回上一步 rejectPreTask 
      this.$refs.approvalJson.validate((valid) => {
        if (!valid) return
        if(row.operationCode == 'approve' || row.operationCode == 'refuse' || row.operationCode == 'submit'){
          // 同意、拒绝、办理
          this.params = {
            operationCode: row.operationCode,
            operationName: row.operationName,
            taskId: this.taskId,
            reason: this.approvalJson.advice,
            flowMap: this.dealWithRequestFields()
          }
          this.$http.post('/api/flowHandle/operate',this.params).then((res)=>{
            this.$emit('reloadPage')
          }).catch(()=>{
            this.$Message.error('操作失败')
          })
        }else if(row.operationCode == 'delegateTask' || row.operationCode == 'transferTask' || row.operationCode == 'setNextTaskUser'){
          // 委托、转办、指派
          this.dialogVisible = true
          if(row.operationCode == 'delegateTask'){
            this.dealDialogTitle = '委托'
            this.$set(this.chiosePerson, 'personVal', '')
            if (this.$refs.chiosePerson !== undefined) this.$refs.chiosePerson.resetFields()
          }else if(row.operationCode == 'transferTask'){
            this.dealDialogTitle = '转办'  
            if (this.$refs.chiosePerson !== undefined) this.$refs.chiosePerson.resetFields()
            this.$set(this.chiosePerson, 'personVal', '')
          }else if(row.operationCode == 'setNextTaskUser'){
            this.dealDialogTitle = '指派'
            this.$set(this.chiosePerson, 'personVal', '')
            if (this.$refs.chiosePerson !== undefined) this.$refs.chiosePerson.resetFields()
          }
          this.params = {
            operationCode: row.operationCode,
            taskId: this.taskId,
            userId: this.chiosePerson.personVal,
            flowMap: this.dealWithRequestFields(),
            reason: this.approvalJson.advice,
            nextHandler: this.chiosePerson.personVal,
            operationName: row.operationName
          }
        }else if(row.operationCode == 'jumpWantTask'){
          // 跳转指定节点
          this.jumpVisible = true
          this.dealDialogTitle = '跳转指定节点'
          this.params = {
            operationCode: row.operationCode,
            taskId: this.taskId,
            reason: this.approvalJson.advice,
            userTaskKey: '',
            operationName: row.operationName
          }
          this.$set(this.chioseTask, 'taskVal', '')
          if (this.$refs.chioseTask !== undefined) this.$refs.chioseTask.resetFields()
          this.queryList()
        }else if(row.operationCode == 'rejectPreTask'){
          // 退回上一步
          this.params = {
            operationCode: row.operationCode,
            taskId: this.taskId,
            operationName: row.operationName
          }
          this.$http.post('/api/flowHandle/operate',this.params).then((res)=>{
            this.$emit('reloadPage')
          }).catch(()=>{
            this.$Message.error('操作失败')
          })
        }
      })
    },
    buttonQuery(){
      let param = this.taskDefinitionKey
      this.$http.post(`/api/flowNodeOperations/findNodeOperation?nodeKey=${param}`).then((res)=>{
      this.dataArr = res.data.result
    })
    },
    handleClick(e){
      if(e.name == 'activeName'){
        this.activeName = 'first'
      }else{
        this.activeName = 'second'
          this.processInstanceId = this.detaileQueryParam.processInstanceId
          this.getLastData()
      }
    },
    closed () {
      // this.showVueComponent = false
      window.location.reload()
    },
    personFn () {
      // 人员
      // this.$http
      //   .get('api/system/cscpUserDetailsOr')
      request.getCscpUserDetailList()
        .then((response) => {
          const op = []
          for (let item of response.data.data) {
            op.push({
              value: item.userId,
              label: `${item.username} `
            })
          }
          this.userOptions = op
        })
        .catch()
    },
    exitRoute () {
      let params = {
        // taskId: 'bd853081-299d-11ed-b3f2-00ff0537aa2b'
        taskId: this.detaileQueryParam.id
      }
      // this.$http
      //   .get('/api/flow/core/get-out-line', { params: params })
      request.getOutlineData({ params: params })
        .then((res) => {
          this.exitRoute = res.data.result
        })
    },
    // 初始化
    initPage () {
      this.formId = this.detaileQueryParam.formId
      this.processInstanceId = this.detaileQueryParam.processInstanceId
      this.processDefinitionId = this.detaileQueryParam.processDefinitionId
      this.parentProcessInstanceId = this.detaileQueryParam.parentProcessInstanceId
      this.taskDefinitionKey = this.detaileQueryParam.taskCode
      this.pageType = this.detaileQueryParam.type
      this.taskId = this.detaileQueryParam.id
      this.processName = this.detaileQueryParam.processName
      this.createTime = this.detaileQueryParam.createTime
      this.processDefinitionKey = this.detaileQueryParam.processDefinitionKey
      // 拿到总审批记录
      this.getRecordList()
      // 高亮
      this.highLightBpmn()
      // 默认值
      this.getFormDefaultData()
      // 人员
      this.pageType === '流程处理' ? this.getUserList() : ''
      // 按钮权限控制
      this.pageType === '流程处理' ? this.buttonQuery() : ''
      //
      this.getLastData()
    },
    getLastData () {
      // this.$http
      //   .get('/api/flow/process/get', {
      //     params: { id: this.processInstanceId }
      //   })
      request.getFlowProcessData({
        params: { id: this.processInstanceId }
      })
        .then((res) => {
          this.processInstance = res.data.result
          this.bpmnXML = res.data.result.processDefinition.bpmnXml
        })
    },
    controlBtn () {
      // this.$http
      //   .get('/api/flow/rule/taskPerm-select', {
      //     params: {
      //       taskDefinitionKey: this.taskDefinitionKey,
      //       taskDefinitionFlag: this.processDefinitionKey
      //     }
      //   })
      request.getRuleTaskPermList({
        params: {
          taskDefinitionKey: this.taskDefinitionKey,
          taskDefinitionFlag: this.processDefinitionKey
        }
      })
        .then((res) => {
          let perm = res.data?.result[0].perms ? res.data.result[0].perms : ''
          // 控制按钮显隐 10:传阅  11：转办 12：指定下一节点处理人  13:终止
          perm.includes('10')
            ? (this.isShowCyBtn = true)
            : (this.isShowCyBtn = false)
          perm.includes('11')
            ? (this.isShowZbBtn = true)
            : (this.isShowZbBtn = false)
          perm.includes('12')
            ? (this.isShowClrBtn = true)
            : (this.isShowClrBtn = false)
          perm.includes('13')
            ? (this.isShowZzBtn = true)
            : (this.isShowZzBtn = false)
        })
    },
    // 处理发送请求时的表单数据参数
    dealWithRequestFields () {
      let formData = this.testFormData
      let formDataModel = this.$refs.preForm.formDataModel
      formDataModel = {
        ...formData,
        ...formDataModel,
      }
      return formDataModel
    },
    //按钮操作
    clickHandleBtnFn (index) {
      this.$refs.approvalJson.validate((valid) => {
        if (valid) {
          let params = {
            key: this.exitRoute[index].key,
            value: this.exitRoute[index].value,
            flowMap: this.dealWithRequestFields(),
            id: this.taskId,
            reason: this.approvalJson.advice,
            nextHandler: this.approvalJson.personneIds
          }
          // this.$http
          //   .put('/api/flow/core/approve', params)
          request.putApproveData(params)
            .then((res) => {
              this.$Message.success('处理成功！')
              this.$emit('reloadPage')
            })
            .catch((err) => {
              this.$message({
                message: '服务器错误!',
                type: 'error',
                customClass: 'messageIndex'
              })
            })
        }
      })
    },
    // 人员
    getUserList () {
      // this.$http
      //   .get('api/system/cscpUserDetailsOr')
      request.getCscpUserDetailList()
        .then((response) => {
          const op = []
          for (let item of response.data.data) {
            op.push({
              value: item.userId,
              label: `${item.username} `
            })
          }
          this.peroptions = op
        })
        .catch()
    },
    getFormData () {
      this.$refs['preForm'].getFormData().then((res) => {
      })
    },
    // 按钮控制

    // 获取formJson
    getFormJson () {
      this.addDialogVisible = false
      // this.$http
      //   .get('/api/lowcode/customize/cscpCustomizeVforms/' + this.formId)
      request.getDataByFormId(this.formId)
        .then((res) => {
          if (this.pageType === '流程处理') {
            // 权限控制
            this.accessControl().then((innerres) => {
              this.isDisableFields = []
              this.isShowFields = []
              innerres.data.result.fieldsPerms.map((item) => {
                if (item.editFlag === '0') {
                  // 可以编辑
                  this.isDisableFields.push(item.fieldId)
                }
                if (item.showFlag === '0') {
                  // 可以编辑
                  this.isShowFields.push(item.fieldId)
                }
              })
              // 处理formJson
              this.formJson = JSON.parse(res.data.formJson)
              this.formJson.widgetList.map((item) => {
                  // 是否可显示
                if (this.isShowFields.indexOf(item.options.name) !== -1) {
                  item.options.hidden = true
                } else {
                  item.options.hidden = false
                }
                // 判断type为grid栅格情况
                if(item.type == 'grid'){
                  item.cols.map((mm) => {
                    if(mm.widgetList.length > 0){
                      // 是否可显示
                      if(this.isShowFields.indexOf(mm.widgetList[0].options.name) !== -1){
                        mm.options.hidden = true
                      }else{
                        mm.options.hidden = false
                      }
                      // 是否可编辑
                      if (this.isDisableFields.indexOf(mm.widgetList[0].options.name) !== -1) {
                        mm.widgetList[0].options.disabled = true
                      } else {
                        mm.widgetList[0].options.disabled = false
                        // item.options.hidden = false
                      }
                    }
                  })
                }
                // 是否可编辑
                if (this.isDisableFields.indexOf(item.options.name) !== -1) {
                  item.options.disabled = true
                } else {
                  item.options.disabled = false
                  item.options.hidden = false
                }
              })
              this.addDialogVisible = true
            })
          } else if (this.pageType === '流程详情') {
              // 处理formJson
              this.formJson = JSON.parse(res.data.formJson)
              this.formJson.widgetList.map(item => {
                //不可编辑
                  item.options.disabled = true
                  if(item.type == 'grid'){
                    item.cols.map((mm) => {
                      // 不可编辑
                      if(mm.widgetList.length > 0){
                        mm.widgetList[0].options.disabled = true
                      }
                    })
                  }
              })
              this.addDialogVisible = true
          }
        })
    },
    // 拿到总审批记录
    getRecordList () {
      this.approveRecords = []
      let params = {
        processInstanceId: this.processInstanceId
      }
      // this.$http
      //   .get('/api/flow/core/list-by-process-instance-id', { params })
      request.getListByInstanceId({ params })
        .then((res) => {
          this.approveRecords = res.data.result.filter(item => item.endTime != null)
          // log
          let response = res.data.result
          // 审批记录
          this.tasks = []
          // 移除已取消的审批
          response.forEach((task) => {
            if (task.result !== 4) {
              this.tasks.push(task)
            }
          })
          // 排序，将未完成的排在前面，已完成的排在后面；
          this.tasks.sort((a, b) => {
            // 有已完成的情况，按照完成时间倒序
            if (a.endTime && b.endTime) {
              return b.endTime - a.endTime
            } else if (a.endTime) {
              return 1
            } else if (b.endTime) {
              return -1
              // 都是未完成，按照创建时间倒序
            } else {
              return b.createTime - a.createTime
            }
          })
        })
        .catch((err) => {
        })
    },
    // 表单权限
    accessControl () {
      // return this.$http.get('api/queryFieldPerms', {
      //   params: {
      //     taskDefinitionKey: this.taskDefinitionKey,
      //   }
      // })
      return request.getFieldPermsList({
        params: {
          taskDefinitionKey: this.taskDefinitionKey,
        }
      })
    },
    // 流程图高亮
    highLightBpmn () {
      // this.$http
      //   .get('/api/flow/process/list', {
      //     params: { processInstanceId: this.processInstanceId }
      //   })
      request.getProcessList({
        params: { processInstanceId: this.processInstanceId }
      })
        .then((res) => {
          this.activityList = res.data.result
          // 排序，将未完成的排在前面，已完成的排在后面；
          this.activityList.sort((a, b) => {
            // 有已完成的情况，按照完成时间倒序
            if (a.endTime && b.endTime) {
              return b.endTime - a.endTime
            } else if (a.endTime) {
              return 1
            } else if (b.endTime) {
              return -1
              // 都是未完成，按照创建时间倒序
            } else {
              return b.startTime - a.startTime
            }
          })
        })
    },
    // 表单默认值
    getFormDefaultData () {
      request.getFieldPermsList({
        params: {
          taskDefinitionKey: this.taskDefinitionKey
        }
      }).then((res) => {
        if(!res.data.result){
          this.testFormDataFn()
        }else{
          if(res.data.result.formName){
          this.formId = res.data.result.formId
          }
          this.testFormDataFn()
        }
      }).catch(()=>{
        this.$message.error('查询失败')
      })
    },
    testFormDataFn(){
      let params = {
            instance_id: this.parentProcessInstanceId ? this.parentProcessInstanceId : this.processInstanceId
          }
          request.queryListByInstanceId(this.formId, { params })
            .then((res) => {
              this.testFormData = res.data.data
              // 拿到formJson
              this.getFormJson()
        })
    }
  },
}
</script>

<style lang="less" scoped>
.ProcessDetail {
  width: 100%;
  height: 100%;
  background-color: #fff;
  .processTop{
    display: flex;
    border-bottom: 1px dashed grey;
    padding-bottom: 10px;
    .applyForm{
      padding-right: 20px;
      width: 65%;
      border-right: 1px dashed grey;
      .formBox{
        height: 290px;
        margin-left: 25px;
        overflow: auto;
        .el-form{
          width: 99%;
        }
      }
      .formBox::-webkit-scrollbar {
        display: none;
      }
    }
    .taskBox{
      width: 35%;
      .spFromBox{
        margin-left: 30px;
        width: 85%;
        .buttonAll{
          height: 130px;
          overflow-y: auto;
          display: flex;
          flex-direction: column;
          .el-button{
            margin-top: 10px;
            margin-left: 0px !important;
          }
          .el-button:hover{
            background-color: #46a6ff;
            color: white;
          }
          .el-button--primary{
            background-color: #EDF2FD;
            color: #246AD9;
            border-color: #EDF2FD;
          }
          
        }
      }
    }
  }

  .title {
    padding: 15px;
    margin-bottom: 15px;
    font-weight: bold;
    width: 100%;
  }
}
.messageIndex {
  z-index: 3000 !important;
}
::v-deep .el-textarea__inner{
  min-height: 110px !important;
}
</style>
