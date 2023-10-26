<template>
  <div>
    <div>
      <el-tabs v-model="activeName" type="card" @tab-click="handleTabClick">
        <el-tab-pane v-for="(item, index) in tabsList" :label="item.title" :name="item.name"
          :key="index">
          <!-- 放点击修改展示的form表单 -->
          <el-form :model="formResize" ref="formResize">
            <div class="formItemStyle">
              <el-form-item label="任务名称" label-width="85px" prop="taskDefinitionName">
                <el-input v-model="formResize.taskDefinitionName" disabled />
              </el-form-item>
              <el-form-item label="任务标识" label-width="85px" prop="taskDefinitionKey">
                <el-input v-model="formResize.taskDefinitionKey" disabled />
              </el-form-item>
            </div>
            <div class="formItemStyle">
              <el-form-item label="流程标识" label-width="85px" prop="taskDefinitionFlag">
                <el-input v-model="formResize.taskDefinitionFlag" disabled style="width: 160px" />
              </el-form-item>
            </div>
            <el-form-item label="操作权限" prop="name" label-width="85px">
              <Select multiple v-model="formResize.id" transfer>
                <Option v-for="(option, index1) in roleOptions" :value="option.value" :key="'qx' + index1">
                  {{ option.label }}
                </Option>
              </Select>
            </el-form-item>
            <el-form-item v-show="intersectionNum > 1" label="交并集" prop="intersection" label-width="85px">
                  <el-radio v-model="formResize.radio" label="0">交集</el-radio>
                  <el-radio v-model="formResize.radio" label="1">并集</el-radio>
            </el-form-item>
            <el-form-item label="处理人配置" prop="type" label-width="85px">
                <el-select v-model="personnelResize.type" placeholder="请选择" multiple @change="typeChange">
                  <el-option v-for="(option, index2) in personnelOptions" :key="'ry' + index2" :label="option.label"
                    :value="option.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <!--人员-->
              <el-form-item v-show="typeChangeValue.indexOf(1) !== -1" label="指定人员" prop="personneIds" label-width="85px">
                <el-select v-model="personnelResize.personneIds" placeholder="请选择" multiple>
                  <el-option v-for="(option, index) in userOptions" :key="option.value + index" :label="option.label"
                    :value="option.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <!--角色-->
              <el-form-item v-show="typeChangeValue.indexOf(2) !== -1" label="指定角色" prop="roleIds" label-width="85px">
                <el-select ref="roleSelect" v-model="personnelResize.roleIds" placeholder="请选择" multiple>
                  <el-option v-for="option in operaterole" :key="option.value" :label="option.label"
                    :value="option.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <!--部门-->
              <el-form-item v-show="typeChangeValue.indexOf(3) !== -1" label="指定部门" prop="department" label-width="85px">
                <treeselect v-model="personnelResize.department" :options="depTreeData" multiple
                  :value-consists-of="valueConsistsOf" :normalizer="normalizer" placeholder="请选择指定部门" />
              </el-form-item>
              <!--自定义脚本-->
              <el-form-item v-show="typeChangeValue.indexOf(4) !== -1" label="指定脚本" prop="customize" label-width="85px">
                <el-select ref="customizeSelect" v-model="personnelResize.customize" placeholder="请选择" multiple>
                  <el-option v-for="option in customizeSelect" :key="option.value" :label="option.label"
                    :value="option.value">
                  </el-option>
                </el-select>
              </el-form-item>

            <el-form-item label="可编辑字段" label-width="85px">
              <el-checkbox-group v-model="formResize.fieldName">
                <el-checkbox @change="fieldNameFn" v-for="(option, index3) in editFieldOptions" :label="option.value" :key="index3">{{
                option.label }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-form-item label="可显示字段" label-width="85px">
              <el-checkbox-group v-model="formResize.showFieldName">
                <el-checkbox @change="showFieldNameFn" v-for="(option, index) in showFieldOptions" :label="option.value" :key="index">{{
                option.label }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitResize">确 定</el-button>
      <el-button @click="cancelAssignForm">取 消</el-button>
    </div>

  </div>
</template>
<script>
import request from '@/api/workflow/request'

export default {
  data() {
    return {
      intersectionNum: 0,
      editFieldOptions: [],
      showFieldOptions: [],
      editFlag: false,
      tabsList: [],
      activeName: '',
      depTreeData: [],
      valueConsistsOf: 'ALL',
      userRuleData: [],
      page: 1,
      size: 10,
      // resizeVisible: false,
      formResize: {
        taskDefinitionName: '',
        taskDefinitionKey: '',
        taskDefinitionFlag: '',
        id: [],
        formName: '',
        // 注意这个值
        saveModelId: '',
        formTable: '',
        fieldName: [],
        showFieldName: [],
        idFlag: '',
        radio: '0'
      },
      // 注意这个值
      saveModelId: '',
      personnelResize: {
        flowTaskAssignRuleBaseList: [],
        type: ''
      },
      userOptions: [],
      operaterole: [],
      treeArr: [],
      fieldOptions: [],
      roleOptions: [
        {
          value: '10',
          label: '传阅'
        },
        {
          value: '11',
          label: '转办'
        },
        {
          value: '12',
          label: '指定下一节点处理人'
        },
        {
          value: '13',
          label: '终止'
        }
      ],
      customizeSelect: [
        {
          value: '10',
          label: '流程发起人'
        }
      ],
      personnelOptions: [
        {
          value: 1,
          label: '人员'
        },
        {
          value: 2,
          label: '角色'
        },
        {
          value: 3,
          label: '部门'
        },
        {
          value: 4,
          label: '自定义脚本'
        }
      ],
    }
  },
  created() {
    this.queryList()
    this.init()
  },
  methods: {
        // 可编辑字段联动
    fieldNameFn(row,c){
      if(row){
        if(this.formResize.showFieldName.indexOf(c.target.value) == -1){
          this.formResize.showFieldName.push(c.target.value)
        }
      }
    },
    showFieldNameFn(row,b){
      if(!row){
        this.formResize.fieldName = this.formResize.fieldName.filter((item)=> item !== b.target.value)
      }
    },
    typeChange(row){
      this.typeChangeValue = row
      this.intersectionNum = row.length
    },
    queryList() {
      let params = {
        page: 1,
        size: 100000,
      }
      // this.$http
      //   .get('/api/flow/bpmn/page', {
      //     params: params,
      //   })
      request.getCscpFlowBpmnPageList({params: params})
        .then((res) => {
          this.id = res.data.result.list[0].id
          this.key = res.data.result.list[0].key
          this.row = res.data.result.list[0]
          this.formName = res.data.result.list[0].formName
          this.handleAssignRule()
        })
        .catch(() => {
          this.$Message.error('列表查询失败')
        })
    },
    handleAssignRule() {
      this.saveModelId = this.id
      this.handleAssignRuleRn = this.row
      this.formResize.formName = this.formName
      // 调用接口查询
      let obj = {}
      obj['modelId'] = this.id
      obj['processDefinitionId'] = undefined
      let params = Object.assign(obj)
      // this.$http
      //   .get('/api/flow/rule/list', { params: params })
      request.getRuleList({ params: params })
        .then((res) => {
          res?.data?.result.map((item) => (item.taskDefinitionFlag = this.key))
          this.userRuleData = res.data?.result
          this.tabsList = this.userRuleData.map((item) => {
            let obj = {}
            obj.id = item.id
            obj.title = item.taskDefinitionName
            obj.name = item.taskDefinitionKey
            return obj
          })
          // // 默认展示第一个tab
          if (this.tabsList && this.tabsList.length) {
            this.activeName = this.tabsList[0].name
            this.handleTabClick({ name: this.activeName })
          }
        })
        .catch(() => {
          this.$Message.error('任务表查询失败')
        })
    },
    // 点击修改
    reviseFn(row) {
      this.formResize.radio = ''
       if(row.unionFlag){
        this.formResize.radio = row.unionFlag + ''
      }else{
        this.formResize.radio = '0'
      }
      // 操作配置
      this.formResize.taskDefinitionName = row.taskDefinitionName
      this.formResize.taskDefinitionKey = row.taskDefinitionKey
      this.formResize.taskDefinitionFlag = row.taskDefinitionFlag
      this.ids = row.id
      let params = {
        taskDefinitionKey: row.taskDefinitionKey,
        taskDefinitionFlag: row.taskDefinitionFlag
      }
      // 操作权限数据回显
      // this.$http
      //   .get('/api/flow/rule/taskPerm-select', { params: params })
      request.getRuleTaskPermList({ params })
        .then((response) => {
          this.formResize.idFlag = ''
          this.formResize.id = []
          if (response?.data?.result[0]) {
            this.formResize.id = response.data.result[0].perms.split(',')
            const temp = this.formResize.id.filter(item => item)
            this.formResize.id = temp
            this.formResize.idFlag = response.data.result[0].id
          }
        })

      // 人员
      // debugger
      this.personnelResize = {
        ...row,
        personneIds: [],
        roleIds: [],
        department: [],
        customize: [],
        options: [],
      }
      let typeData = this.personnelResize.flowTaskAssignRuleBaseList.map((item)=>item.type).filter((item)=>{
        return String(item) !== 'null'
      })
      if(typeData.length > 0){
        this.personnelResize.type = typeData
      }
      this.typeChange(typeData)
      if(this.personnelResize.flowTaskAssignRuleBaseList.length > 0){
        if(this.personnelResize.flowTaskAssignRuleBaseList[0].type){
            this.personnelResize.personneIds.push(...this.personnelResize.flowTaskAssignRuleBaseList[0].options)
        }
      }
      if(this.personnelResize.flowTaskAssignRuleBaseList.length > 0){
        if(this.personnelResize.flowTaskAssignRuleBaseList[1].type){
          this.personnelResize.roleIds.push(...this.personnelResize.flowTaskAssignRuleBaseList[1].options)
        }
      }
      if(this.personnelResize.flowTaskAssignRuleBaseList.length > 0){
        if(this.personnelResize.flowTaskAssignRuleBaseList[2].type){
          this.personnelResize.department.push(...this.personnelResize.flowTaskAssignRuleBaseList[2].options)
        }
      }
      if(this.personnelResize.flowTaskAssignRuleBaseList.length > 0){
        if(this.personnelResize.flowTaskAssignRuleBaseList[3].type){
          this.personnelResize.customize.push(...this.personnelResize.flowTaskAssignRuleBaseList[3].options)
        }
      }
      // 操作字段权限控制
      let qryTableNameParam = {
        formName: this.formResize.formName ? this.formResize.formName : ''
      }
      // this.$http
      //   .get('/api/lowcode/customize/cscpCustomizeVforms/listOne', {
      //     params: qryTableNameParam
      //   })
      request.getCscpCustomizeVformsListOne({
        params: qryTableNameParam
      })
        .then((response) => {
          this.initField(response.data.formTable)
        })
      // 333操作字段权限查询
      this.currentTaskDefinitionKey = row
      // this.getKeyName()
      this.getFieldKeyName()
      // this.init()
    },
    getKeyName() {
      // this.$http.get('/api/lowcode/customize/qryTableFieldPerm', {
      //   params: {
      //     taskDefinitionKey: this.currentTaskDefinitionKey.taskDefinitionKey,
      //     taskDefinitionFlag: this.currentTaskDefinitionKey.taskDefinitionFlag
      //   }
      // })
      request.getTableFieldPerm({
        params: {
          taskDefinitionKey: this.currentTaskDefinitionKey.taskDefinitionKey,
          taskDefinitionFlag: this.currentTaskDefinitionKey.taskDefinitionFlag
        }
      })
        .then(response => {
        let temp = response.data.data.map(item => item.fieldId)
        this.formResize.fieldName = temp
      })
    },
    getFieldKeyName() {
      // this.$http
      //   .get('/api/queryFieldPerms', {
      //     params: {
      //       taskDefinitionKey: this.currentTaskDefinitionKey.taskDefinitionKey,
      //     }
      //   })
      request.getFieldPermsList({
          params: {
            taskDefinitionKey: this.currentTaskDefinitionKey.taskDefinitionKey,
          }
        })
        .then((res) => {
          console.log('新的接口的返回值', res.data)
          let temp = res.data?.result?.fieldsPerms || []
          const fieldNameArr = temp.map(item => {
            if (item.editFlag === '1') {
              return item.fieldId
            }
          })
          const showFieldNameArr = temp.map(item => {
            if (item.showFlag === '1') {
              return item.fieldId
            }
          })
          this.formResize.fieldName = fieldNameArr
          this.formResize.showFieldName = showFieldNameArr
        })
    },
    // 字段下拉数据
    initField(tableName) {
      let params = {
        tableName: tableName ? tableName : ''
      }
      // this.$http.get('/api/getTableOperateFields', { params: params })
      request.getTableOperateFieldsList({ params: params })
        .then(response => {
        // this.fieldOptions = []
        this.editFieldOptions = []
          this.showFieldOptions = []
        for (const item of response.data) {
          // this.fieldOptions.push({
          //   value: item.columnName,
          //   label: item.columnComment
          // })
          this.editFieldOptions.push({
              value: item.columnName,
              label: item.columnComment
            })
            this.showFieldOptions.push({
              value: item.columnName,
              label: item.columnComment
            })
        }
      }).catch()
    },
    init() {
      // 人员
      // this.$http.get('api/system/cscpUserDetailsOr')
      request.getCscpUserDetailList()
        .then(response => {
        const op = []
        for (let item of response.data.data) {
          op.push({
            value: item.userId,
            label: `${item.username} `
          })
        }
        this.userOptions = op
      }).catch()
      // 角色
      const [url, httpConfig] = [
        '/api/system/cscpRolessByCriteria',
        {
          params: { size: 1000 }
        }
      ]
      // this.$http.get(url, httpConfig)
      request.getCscpRoleList(httpConfig)
        .then(response => {
        this.operaterole = []
        for (const item of response.data.data) {
          this.operaterole.push({
            value: item.id.toString(),
            label: item.name,
            icon: item.icon
          })
        }
      }).catch()
      // 部门
      // this.$http.get('/api/cscpDepts/treeselect')
      request.getCscpDeptsTreeselectList()
        .then(response => {
        this.depTreeData = this.getTree(response.data)
      }).catch()

    },
    //部门
    getTree(tree) {
      let arr = []
      if (!!tree && tree.length !== 0) {
        tree.forEach(item => {
          let obj = {}
          obj.label = item.label
          obj.id = item.id // 其他你想要添加的属性
          obj.expand = true
          obj.selected = false
          obj.children = this.getTree(item.children) // 递归调用
          arr.push(obj)
        })
      }
      return arr
    },
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
    },
    handleTabClick(tab) {
      console.log(tab,'tab')
      const tabName = tab.name
      const row = this.userRuleData.filter(
        (item) => item.taskDefinitionKey === tabName
      )
      // 模拟点击对应行的修改按钮
      this.reviseFn(row[0])
    },
    //确定
    submitResize() {
      console.log(1,'1.1')
      let fieldsPerms = this.editFieldOptions.map(item => {
        let obj = {}
        obj.fieldId = item.value
        obj.editFlag = this.formResize.fieldName.includes(item.value) ? '1' : '0'
        obj.showFlag = this.formResize.showFieldName.includes(item.value) ? '1' : '0'
        return obj
      })
      let that = this
      const params1 = {
        taskDefinitionKey: this.formResize.taskDefinitionKey,
        taskDefinitionName: this.formResize.taskDefinitionName,
        taskDefinitionFlag: this.formResize.taskDefinitionFlag,
        perms: !this.formResize.id ? '' : this.formResize.id.join(','),
        id: this.formResize.idFlag || ''
      }
      // 人员
      if(this.personnelResize.type.indexOf(1) !== -1){
        this.personneIdsData = {options: this.personnelResize.personneIds,type: 1}
        if(this.personneIdsData.options.length == 0){
          this.personneIdsRules = false
        }else{
          this.personneIdsRules = true
        }
      }else{
        this.personneIdsData = {options: [],type: ''}
        this.personneIdsRules = true
      }
      // 角色
      if(this.personnelResize.type.indexOf(2) !== -1){
        this.roleIdsData = {options: this.personnelResize.roleIds,type: 2}
        if(this.roleIdsData.options.length == 0){
          this.roleIdsDataRules = false
        }else{
          this.roleIdsDataRules = true
        }
      }else{
        this.roleIdsData = {options: [],type: ''}
        this.roleIdsDataRules = true
      }
      // 部门
      if(this.personnelResize.type.indexOf(3) !== -1){
        this.departmentData = {options: this.personnelResize.department,type: 3}
        if(this.departmentData.options.length == 0){
          this.departmentDataRules = false
        }else{
          this.departmentDataRules = true
        }
      }else{
        this.departmentData = {options: [],type: ''}
        this.departmentDataRules = true
      }
      //自定义
      if(this.personnelResize.type.indexOf(4) !== -1){
        this.customizeData = {options: this.personnelResize.customize,type: 4}
        if(this.customizeData.options.length == 0){
          this.customizeDataRules = false
        }else{
          this.customizeDataRules = true
        }
      }else{
        this.customizeData = {options: [],type: ''}
        this.customizeDataRules = true
      }
      let flowTaskAssignRuleBaseList = [this.personneIdsData,this.roleIdsData,this.departmentData,this.customizeData]
      let personnelResize = {
        ...this.personnelResize,
        flowTaskAssignRuleBaseList: flowTaskAssignRuleBaseList,
        unionFlag: this.formResize.radio
      }
      let arrayRules = [this.personneIdsRules, this.roleIdsDataRules,this.departmentDataRules,this.customizeDataRules]
      console.log(arrayRules,'arrayRules')
      if(arrayRules.indexOf(false) !== -1){
        this.$message.warning('规则类型不能为空')
        return
      }
      // personnelResize.personneIds = undefined
      // personnelResize.roleIds = undefined
      // personnelResize.department = undefined
      // personnelResize.customize = undefined
      // let promise1 = this.$http.post('/api/flow/rule/taskPerm-update', params1)
      let promise1 = request.updateTaskPerm(params1)
      let promise2 = {}
      const params3 = {
        taskDefinitionKey: this.formResize.taskDefinitionKey,
        taskDefinitionName: this.formResize.taskDefinitionName,
        formId: that.handleAssignRuleRn.formId,
        fieldsPerms: fieldsPerms,
      }
      // 替换成新的接口兼容可显示字段
      // let promise3 = this.$http.post(
      //   '/api/updateFieldsPerm',
      //   params3
      // )
      let promise3 = request.updateFieldsPerm(params3)
      if (!personnelResize.id) {
        personnelResize.modelId = that.saveModelId || ''
        // promise2 = this.$http.post(
        //   '/api/flow/rule/assign-create',
        //   personnelResize
        // )
        promise2 = request.createAssign(personnelResize)
      } else {
        // promise2 = this.$http.put('/api/flow/rule/update', personnelResize)
        promise2 = request.updateAssign(personnelResize)
      }

      Promise.all([promise1, promise2, promise3])
        .then((res) => {
          const resolve2 = res.filter(item => item.config.url === '/api/flow/rule/assign-create')
          if (resolve2 && resolve2.length > 0) {
            let personnelResizeId = resolve2[0].data.result
            this.personnelResize.id = personnelResizeId
          }
          this.$message({
            message: '修改成功',
            type: 'success',
            duration: 3000
          })
        })
        .catch(() => {
          this.$message({
            message: '修改失败',
            type: 'error',
            duration: 3000
          })
        })
    },
    //取消
    cancelAssignForm() {
      this.personnelResize = {}
      // this.resizeVisible = false
      this.$refs.formResize[0].resetFields()
      this.formResize.id = []
    },

  },
}
</script>
<style lang="less" scoped>
::v-deep .el-form-item__content {
  line-height: 21px;
  padding-top: 1px;

  .vue-treeselect__icon {
    color: #515a6e
  }

  .vue-treeselect__multi-value-item {
    background: #f7f7f7;
    color: #515a6e
  }
}
</style>
