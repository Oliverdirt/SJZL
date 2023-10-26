<template>
  <div class="main" style="height: 100%">
    <div class="flowform">
      <el-card>
        <el-form ref="form" :model="modelData" label-width="80px">
          <el-row>
            <el-col :span="8">
              <el-form-item label="流程标识" label-width="80px" prop="modelKey">
                <el-input placeholder="请输入流程标识" v-model="modelData.modelKey" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="流程名称" label-width="80px" prop="modelName">
                <el-input placeholder="请输入流程名" v-model="modelData.modelName" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24" style="text-align: right">
                <el-button size="media" type="primary" @click="queryList(true)">查询</el-button>
                <el-button size="media" @click="resetFn">重置</el-button>
            </el-col>
          </el-row>
        </el-form>
      </el-card>

      <!--列表-->
      <el-card>
        <div class="formBtn">
          <!-- <el-button @click="addFormFn" icon="el-icon-plus" type="primary" size="medium">快建流程</el-button> -->
          <el-button @click="addForm" icon="el-icon-plus" type="primary" size="medium">新建流程</el-button>
          <!-- <el-button type="info" icon="el-icon-upload2" size="medium" @click="handleImport">导入流程
          </el-button> -->
        </div>
        <div>
          <el-table :data="tableData"  style="font-size: 14px">
            <el-table-column label="流程名称" align="center" prop="name" />

            <el-table-column label="流程标识" align="center" prop="key">
              <template slot-scope="scope">
                <el-button type="text" @click.native="handleDesign(scope.row)" style="font-size: 14px">
                  <span>{{ scope.row.key }}</span>
                </el-button>
              </template>
            </el-table-column>

<!--            <el-table-column label="表单信息" align="center" prop="formType">-->
<!--              <template slot-scope="scope">-->
<!--                <el-button v-if="scope.row.formId" type="text" @click.native="chargeForm(scope.row)"-->
<!--                  style="font-size: 14px">-->
<!--                  <span>{{ scope.row.formName }}</span>-->
<!--                </el-button>-->
<!--                &lt;!&ndash; <label v-else >暂无表单</label> &ndash;&gt;-->
<!--                <el-button v-else type="text"  @click.native="chargeForm(scope.row)" style="font-size: 14px">暂无表单</el-button>-->
<!--              </template>-->
<!--            </el-table-column>-->
            <el-table-column label="创建时间" align="center" prop="createTime" width="180"></el-table-column>
            <el-table-column label="最新部署的流程定义" align="center" prop="processDefinition">
              <el-table-column label="流程版本" align="center" prop="processDefinition.version" width="100">
                <template slot-scope="scope">
                  <el-tag size="medium" v-if="scope.row.processDefinition">v{{ scope.row.processDefinition.version }}
                  </el-tag>
                  <el-tag size="medium" type="warning" v-else>未部署</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="激活状态" align="center" prop="state" width="100">
                <template slot-scope="scope">
                  <el-switch v-if="scope.row.processDefinition" v-model="scope.row.processDefinition.suspensionState"
                    :active-value="1" :inactive-value="2" @change="handleChangeState(scope.row)" />
                </template>
              </el-table-column>
              <el-table-column label="部署时间" align="center" prop="deploymentTime" width="180">
                <template slot-scope="scope">
                  <span v-if="scope.row.processDefinition">{{
                      scope.row.processDefinition.deploymentTime
                  }}</span>
                </template>
              </el-table-column>
            </el-table-column>
<!--            <el-table-column label="是否可终止" align="center" prop="createTime" width="70">-->
<!--              <template slot-scope="scope">-->
<!--                <el-switch v-if="scope.row.processDefinition" v-model="scope.row.backState" :active-value="1"-->
<!--                  :inactive-value="0" @change="changeBackState(scope.row)" />-->
<!--              </template>-->
<!--            </el-table-column>-->
            <el-table-column label="操作" align="center" fixed="right" width="200">
              <template slot-scope="scope">
<!--                <el-button type="text" @click="handleAssignRule(scope.row)" style="font-size: 14px;color: #0056B5">分配规则</el-button>-->
                <el-button type="text" @click="publishDesign(scope.row)" style="font-size: 14px;color: #0056B5">发布流程</el-button>
<!--                <el-button type="text" style="font-size: 14px;color: #0056B5" @click="authorizationManagement(scope.row)">权限管理-->
<!--                </el-button>-->
                <el-button type="text" @click.native="deleteDesign(scope.row)" style="font-size: 14px;color: #0056B5">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :current-page.sync="pageData.pageNum" :page-sizes="[10, 20, 30, 40]" :page-size="5"
          layout="total, sizes, prev, pager, next, jumper" :total="pageData.total"></el-pagination>
      </el-card>

      <!--表单弹框-->
      <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body z-index="1000" @close="cancel">
        <el-form ref="formData" :model="form" label-width="80px" :rules="rules">
          <el-form-item label="流程标识" prop="key">
            <el-input v-model="form.key" placeholder="请输入流标标识" :disabled="formOpen" />
          </el-form-item>
          <el-form-item label="流程名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入流程名称" :disabled="formOpen" clearable />
          </el-form-item>
          <el-form-item label="流程描述" prop="description">
            <el-input v-model="form.description" type="textarea" clearable />
          </el-form-item>
          <el-form-item v-if="formOpen" label="表单类型" prop="formType">
            <el-select v-model="form.formType" clearable placeholder="请选择" @change="formTypeChange">
              <el-option v-for="item in formTypeOpts" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item v-if="formOpen" label="表单选择" prop="formId">
            <el-select v-model="form.formId" clearable placeholder="请选择">
              <el-option v-for="item in options" :key="item.formId" :label="item.formName" :value="item.formId">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="save" type="primary">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
      <!-- 分配规则弹窗 -->
      <el-dialog title="任务分配规则" :visible.sync="Assignvisible" width="800px">
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
                <el-select v-model="formResize.id" placeholder="请选择" multiple>
                  <el-option v-for="(option, index1) in roleOptions" :key="'qx' + index1" :label="option.label"
                    :value="option.value">
                  </el-option>
                </el-select>
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
                      option.label
                  }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
              <el-form-item label="可显示字段" label-width="85px">
                <el-checkbox-group v-model="formResize.showFieldName">
                  <el-checkbox @change="showFieldNameFn" v-for="(option, index) in showFieldOptions" :label="option.value" :key="index">{{
                      option.label
                  }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitResize">确 定</el-button>
          <el-button @click="cancelAssignForm">取 消</el-button>
        </div>
      </el-dialog>

      <!--222权限管理弹框-->
      <el-dialog title="流程发起权限" :visible.sync="authorizationVisible" width="550px" append-to-body>
        <el-form label-width="80px" :model="authpersonnelResize" ref="authpersonnelResize">
          <el-form-item label="流程标识" prop="key">
            <el-input v-model="authpersonnelResize.key" placeholder="请输入流标标识" disabled />
          </el-form-item>
          <el-form-item label="流程名称" prop="name">
            <el-input v-model="authpersonnelResize.name" placeholder="请输入流程名称" disabled />
          </el-form-item>
          <el-form-item label="操作权限" prop="type">
            <el-select v-model="authpersonnelResize.type" @change="authpersonnelResizeTypeChange">
              <el-option v-for="option in authpersonnelOptions" :value="option.value" :key="option.value"
                :label="option.label">
              </el-option>
            </el-select>
          </el-form-item>
          <!--人员-->
          <el-form-item v-show="authpersonnelResize.type === 1" label="指定角色" prop="authroleIds">
            <el-select ref="roleSelect" v-model="authpersonnelResize.authroleIds" multiple>
              <el-option v-for="option in operaterole" :value="option.value" :key="option.value" :label="option.label">
              </el-option>
            </el-select>
          </el-form-item>
          <!--用户-->
          <el-form-item v-show="authpersonnelResize.type === 2" label="指定用户" prop="authpersonneIds">
            <el-select ref="userSelect" v-model="authpersonnelResize.authpersonneIds" multiple filterable>
              <el-option v-for="(option, index) in userOptions" :value="option.value" :key="option.value + index"
                :label="option.label">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitAuthorization">确 定</el-button>
            <el-button @click="cancelAuthorization">取 消</el-button>
        </div>
      </el-dialog>
      <!-- 用户导入对话框 -->
      <el-dialog title="导入流程" :visible.sync="upload.open" width="400px" append-to-body>
        <el-upload ref="upload" :limit="1" accept=".bpmn, .xml, .BPMN, .XML" :headers="upload.headers"
          :action="upload.url" :disabled="upload.isUploading" :on-progress="handleFileUploadProgress"
          :on-success="handleFileSuccess" :on-error="handleFileError" :auto-upload="false" name="bpmnFile"
          :data="upload.form" drag style="margin-left: 4px">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text" style="margin-left: 4px !important">
            将文件拖到此处，或
            <em>点击上传</em>
          </div>
          <div class="el-upload__tip" style="color: red; text-align: center; margin: 0px 0px 15px 0px" slot="tip">
            提示：仅允许导入“bpmn”或“xml”格式文件！
          </div>
          <div class="el-upload__tip" slot="tip">
            <el-form style="margin-right: 5px !important" ref="uploadForm" size="mini" label-width="80px"
              :model="upload.form" @submit.native.prevent>
              <el-form-item label="流程描述" prop="description">
                <el-input type="textarea" v-model="upload.form.description" clearable />
              </el-form-item>
            </el-form>
          </div>
        </el-upload>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="uploadClose">取 消</el-button>
        </div>
      </el-dialog>
    </div>
    <flowformSteps ref="flowformSteps"></flowformSteps>
  </div>
</template>
<script>
import treeselect from '@riophae/vue-treeselect'
import flowformSteps from './flowformSteps.vue'
import request from '@/api/workflow/request'

export default {
  components: {
    treeselect,
    flowformSteps
  },
  data() {
    return {
      intersectionNum: 0,
      personneIdsData: {},
      roleIdsData: {},
      departmentData: {},
      customizeData: {},
      typeChangeValue: [],
      editFlag: false,
      tabsList: [],
      activeName: '',
      valueConsistsOf: 'ALL',
      formResize: {
        taskDefinitionName: '',
        taskDefinitionKey: '',
        taskDefinitionFlag: '',
        id: [],
        formName: '',
        formTable: '',
        fieldName: [],
        showFieldName: [],
        idFlag: '',
        unionFlag: '',
        radio: '0'
      },
      personnelResize: {
        flowTaskAssignRuleBaseList: [],
      },
      authpersonnelResize: {},
      saveModelId: '',
      ids: '',
      fieldId: [],
      modelData: {
        modelKey: '',
        modelName: ''
      },
      pageData: {
        total: 0,
        pageSize: 10,
        pageNum: 1
      },
      userOptions: [],
      operaterole: [],
      customizeSelect: [
        {
          value: '10',
          label: '流程发起人'
        }
      ],
      depTreeData: [],
      intersectionUnion: [
        {
            value: 0,
            label: '交集'
        },
        {
           value: 1,
           label: '并集'
        },
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
      authpersonnelOptions: [
        {
          value: 1,
          label: '角色'
        },
        {
          value: 2,
          label: '用户'
        }
      ],
      // 表单校验
      rules: {
        key: [{ required: true, message: '流程标识不能为空', trigger: 'blur' }],
        name: [{ required: true, message: '流程名称不能为空', trigger: 'blur' }],
        formType: [{ required: true, message: '请选择表单类型', trigger: 'blur' }],
        formId: [{ required: true, message: '请选择表单', trigger: 'blur' }]
      },
      // 流程导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: {
          Authorization: localStorage.token
        },
        // 上传的地址
        url: this.$util.baseUrl + '/api/flow/bpmn/import',
        // 表单
        form: {}
      },
      tableData: [],
      userRuleData: [],
      options: [],
      formTypeOpts: [
        {
          label: '自定义表单',
          value: '1'
        },
        {
          label: '路由表单',
          value: '2'
        }
      ],
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
      editFieldOptions: [],
      showFieldOptions: [],
      modal1: false,
      typeDisabled: false,
      form: {
        formType: '1'
      },
      open: false,
      Assignvisible: false,
      authorizationVisible: false,
      formOpen: false,
      title: '',
      currentTaskDefinitionKey: ''
    }
  },
  created() {
    this.queryList()
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
    submitResize() {
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
      if(arrayRules.indexOf(false) !== -1){
        this.$message.warning('规则类型不能为空')
        return
      }
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
    authpersonnelResizeTypeChange() {
      this.$forceUpdate()
    },
    init() {
      // 人员
      // this.$http
      //   .get('/api/system/cscpUserDetailsOr')
      let params = { page: 1,size: 10000}
      request.getCscpUserDetailList({ params: params})
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
      // 角色
      // const [url, httpConfig] = [
      //   '/api/system/cscpRolessByCriteria',
      //   {
      //     params: { size: 1000 }
      //   }
      // ]
      // this.$http
      //   .get(url, httpConfig)
      request.getCscpRoleList({ params: { size: 1000 } })
        .then((response) => {
          this.operaterole = []
          for (const item of response.data.data) {
            this.operaterole.push({
              value: item.id.toString(),
              label: item.name,
              icon: item.icon
            })
          }
        })
        .catch()
      // 部门
      // this.$http
      //   .get('/api/cscpDepts/treeselect')
      request.getCscpDeptsTreeselectList()
        .then((response) => {
          this.depTreeData = this.getTree(response.data)
        })
        .catch()
    },
    //部门
    getTree(tree) {
      let arr = []
      if (!!tree && tree.length !== 0) {
        tree.forEach((item) => {
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
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    // 表单设计
    formDesign(row) {
      this.$router.push({
        path: '/lowcode-programming',
        query: {
          key: row.formId
        }
      })
    },

    // 222列表
    queryList(inquire) {
      if(inquire){
        this.pageData.pageNum = 1
      }
      let obj = {}
      obj['key'] = this.modelData['modelKey']
        ? this.modelData['modelKey']
        : null
      obj['name'] = this.modelData['modelName']
        ? this.modelData['modelName']
        : null
      let params = Object.assign(obj, {
        page: this.pageData.pageNum,
        size: this.pageData.pageSize,
        category: 'normal'
      })
      // this.$http
      //   .get('/api/flow/bpmn/page', {
      //     params: params
      //   })
      request.getCscpFlowBpmnPageList({ params: params })
        .then((res) => {
          this.tableData = res.data.result.list
          this.pageData.total = res.data.result.total
        })
        .catch(() => {
          this.$Message.error('列表查询失败')
        })
    },
    addFormFn() {
      this.$refs.flowformSteps.flowformStepsVisiable = true
    },
    addForm() {
      this.title = '新建模型'
      this.open = true
      this.reset()
      this.formOpen = false
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.open = true
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true
    },
    // 文件上传失败的处理
    handleFileError(err, file, fileList) {
      const detail = JSON.parse(err.message).detail
      this.$message.error('导入流程失败:' + detail)
      this.uploadClose()
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      if (response.code !== 0) {
        this.$message.error(response.error)
        return
      }
      // 提示，并刷新
      this.$message.success(
        '导入流程成功！请点击【设计流程】按钮，进行编辑保存后，才可以进行【发布流程】'
      )
      // 重置表单

      this.$Message.success('导入成功')
      this.queryList()
      this.uploadClose()
    },
    uploadClose() {
      // 关闭弹窗
      this.upload.open = false
      // 重置上传状态和文件
      this.upload.isUploading = false
      this.$refs.upload.clearFiles()
      // 重置表单
      this.upload.form = {}
    },
    /** 提交上传文件 */
    submitFileForm() {
      this.$refs['uploadForm'].validate((valid) => {
        if (!valid) {
          return
        }
        this.$refs.upload.submit()
      })
    },
    chargeForm(row) {
      this.title = '表单绑定'
      this.open = true
      this.formOpen = true
      this.reset()
      this.form = {
        ...row
      }
      // 查询出设计的流程表列表
      this.formTypeChange()
      this.editFlag = true
    },
    formTypeChange() {
      const formType = this.form.formType
      let obj = {}
      obj['formType'] = formType
      let params = Object.assign(obj)
      if (this.editFlag) {
        this.form.formId = ''
      }
      // this.$http
      //   .get('/api/lowcode/customize/cscpCustomizeVforms/getByFormType', {
      //     params: params
      //   })
      request.getCscpCustomizeVformsList({ params })
        .then((res) => {
          this.options = res.data
        })
        .catch(() => {
          this.$Message.error('表单查询失败')
        })
    },
    //设计流程
    handleDesign(row) {
      console.log(row,'www');
      this.$router.push({
        path: '/modelEditor',
        query: {
          modelId: row.id,
          processId: row.key,
          processName: row.name,
          category: row.category,
        }
      })
    },

    handleAssignRule(row) {
      if (!row.formId) {
        this.$Message.error('请先绑定表单!')
        return
      }
      this.saveModelId = row.id
      this.handleAssignRuleRn = row
      this.formResize.formName = row.formName
      // 调用接口查询
      let obj = {}
      obj['modelId'] = row.id
      obj['processDefinitionId'] = undefined
      let params = Object.assign(obj)
      // this.$http
      //   .get('/api/flow/rule/list', { params: params })
      request.getRuleList({ params })
        .then((res) => {
          res?.data?.result.map((item) => (item.taskDefinitionFlag = row.key))
          this.userRuleData = res.data?.result
          this.tabsList = this.userRuleData?.map((item) => {
            let obj = {}
            // obj.id = item.idradio
            obj.id = item.id
            obj.title = item.taskDefinitionName
            obj.name = item.taskDefinitionKey
            return obj
          })
          // 默认展示第一个tab
          if (this.tabsList && this.tabsList.length) {
            this.activeName = this.tabsList[0].name
            this.handleTabClick({ name: this.activeName })
          }
        })
        .catch(() => {
          this.$Message.error('任务表查询失败')
        })
      this.Assignvisible = true
    },
    handleTabClick(tab) {
      const tabName = tab.name
      let obj = {}
      obj['modelId'] = this.handleAssignRuleRn.id
      obj['processDefinitionId'] = undefined
      let params = Object.assign(obj)
      request.getRuleList({ params })
        .then((res) => {
          res?.data?.result.map((item) => (item.taskDefinitionFlag = this.handleAssignRuleRn.key))
          this.userRuleData = res.data?.result
          this.tabsList = this.userRuleData?.map((item) => {
            let obj = {}
            obj.id = item.id
            obj.title = item.taskDefinitionName
            obj.name = item.taskDefinitionKey
            return obj
          })
          const row = this.userRuleData.filter(
            (item) => item.taskDefinitionKey === tabName
          )
          this.reviseFn(row[0])
        })
        .catch(() => {
          this.$Message.error('任务表查询失败2')
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
            this.formResize.idFlag = response.data.result[0].id
            if (response?.data?.result[0].perms) this.formResize.id = response.data.result[0].perms.split(',')
            const temp = this.formResize.id.filter(item => item)
            this.formResize.id = temp
          }
        })

      // 人员
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
      this.personnelResize.type = typeData
      // 规则类型多选联动
      this.typeChange(this.personnelResize.type)
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
      this.init()
    },
    getKeyName() {
      // this.$http
      //   .get('/api/lowcode/customize/qryTableFieldPerm', {
      //     params: {
      //       taskDefinitionKey: this.currentTaskDefinitionKey.taskDefinitionKey,
      //       taskDefinitionFlag: this.currentTaskDefinitionKey.taskDefinitionFlag
      //     }
      //   })
      request.getTableFieldPerm({
        params: {
          taskDefinitionKey: this.currentTaskDefinitionKey.taskDefinitionKey,
          taskDefinitionFlag: this.currentTaskDefinitionKey.taskDefinitionFlag
        }
      })
        .then((response) => {
          let temp = response.data.data.map((item) => item.fieldId)
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
      // this.$http
      //   .get('/api/getTableOperateFields', { params: params })
      request.getTableOperateFieldsList({ params })
        .then((response) => {
          this.editFieldOptions = []
          this.showFieldOptions = []
          for (const item of response.data) {
            this.editFieldOptions.push({
              value: item.columnName,
              label: item.columnComment
            })
            this.showFieldOptions.push({
              value: item.columnName,
              label: item.columnComment
            })
          }
        })
        .catch()
    },
    cancelAssignForm() {
      this.personnelResize = {}
      if (this.$refs?.formResize && this.$refs?.formResize.length) {
        this.$refs?.formResize[0].resetFields()
      }
      this.Assignvisible = false
      this.formResize.id = []
    },

    // 111权限管理
    authorizationManagement(row) {
      this.init()
      this.authpersonnelResize = {
        ...row,
        authpersonneIds: [],
        authroleIds: [],
        relation: [],
        proDefName: '',
        processId: ''
      }
      if (row.type === 2) {
        this.authpersonnelResize.authpersonneIds.push(...row.relation)
      } else if (row.type === 1) {
        this.authpersonnelResize.authroleIds.push(...row.relation)
      }
      if (this.authpersonnelResize.processDefinition) {
        this.getdata()
      } else {
        this.$alert('流程尚未部署，请部署后进行此操作', '提示', {
          confirmButtonText: '确定'
        })
      }
    },

    getdata() {
      // this.$http
      //   .get(`/api/cscpActs/getAct?id=${this.authpersonnelResize.id}`)
      request.getActsById(this.authpersonnelResize.id)
        .then((response) => {
          this.authpersonnelResize.type = response.data.type
          this.authorizationVisible = true
          if (response.data.type === 1) {
            this.authpersonnelResize.authroleIds = response.data.relation
          } else {
            this.authpersonnelResize.authpersonneIds = response.data.relation
          }
        })
        .catch()
    },

    // 权限管理提交
    submitAuthorization() {
      this.$refs.authpersonnelResize.validate((valid) => {
        if (valid) {
          // 构建表单
          let authpersonnelResize = {
            ...this.authpersonnelResize,
            // taskDefinitionName: undefined,
            processId: this.authpersonnelResize.processDefinition.id
          }
          if (authpersonnelResize.type === 2) {
            authpersonnelResize.relation = authpersonnelResize.authpersonneIds
          } else if (authpersonnelResize.type === 1) {
            authpersonnelResize.relation = authpersonnelResize.authroleIds
          }
          authpersonnelResize.authpersonneIds = undefined
          authpersonnelResize.authroleIds = undefined
          authpersonnelResize.procDefName = authpersonnelResize.name
          authpersonnelResize.procDefId = authpersonnelResize.id
          // this.$http
          //   .put('/api/cscpActs/addAct', authpersonnelResize)
          request.addAct(authpersonnelResize)
            .then((response) => {
              this.$Message.success('修改成功')
            })
            .catch()
          this.authorizationVisible = false
        }
      })
    },
    cancelAuthorization() {
      this.authorizationVisible = false
    },
    // 发布流程
    publishDesign(row) {
      this.$Modal.confirm({
        title: '提示',
        content: '<p>是否发布该流程！</p>',
        onOk: () => {
          // this.$http
          //   .post('/api/flow/bpmn/deploy?id=' + row.id)
          request.publishBpmnById(row.id)
            .then((res) => {
              this.$Message.success('发布成功')
              this.queryList()
            })
            .catch((error) => {
              this.$Message.error('发布失败:' + error.response.data.detail)
            })
        },
        onCancel: () => { }
      })
    },
    /** 更新状态操作 */
    handleChangeState(row) {
      const id = row.id
      let state = row.processDefinition.suspensionState
      let statusState = state === 1 ? '激活' : '挂起'
      this.$Modal.confirm({
        title: '提示',
        content: '<p>是否确认' + statusState + '流程:' + row.name + '?</p>',
        onOk: () => {
          const [url, data] = [
            '/api/flow/bpmn/update-state',
            {
              id: id,
              state: state
            }
          ]
          // this.$http
          //   .put(url, data)
          request.updateState(data)
            .then((res) => {
              if (res.data.code === 0) {
                this.$Message.success(statusState + '成功')
                this.queryList()
              }
            })
            .catch(() => {
              this.$Message.error(statusState + '失败')
            })
          this.reset()
        },
        onCancel: () => { }
      })
    },
    // 更改可终止状态
    changeBackState(row) {
      let statusState = row.backState == 0 ? 0 : 1
      let data = {
        id: row.id,
        name: row.name,
        backState: statusState
      }
      // this.$http
      //   .put('/api/flow/bpmn/update', data)
      request.updateBpmn(data)
        .then((res) => {
          this.$Message.success('更新成功')
          this.queryList()
        })
        .catch((error) => {
          this.$Message.error('更新失败:' + error.response.data.detail)
        })
    },
    // 删除
    deleteDesign(row) {
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除这条数据？</p>',
        onOk: () => {
          let obj = {}
          obj['id'] = row.id
          let params = Object.assign(obj)
          // this.$http
          //   .delete('/api/flow/bpmn/delete', { params: params })
          request.deleteBpmn({ params })
            .then((res) => {
              this.$Message.success('删除成功')
              this.queryList()
            })
            .catch((error) => {
              this.$Message.error(
                `${'删除失败！'}<br/>${error.response.data.detail}`
              )
            })

          this.reset()
        },
        onCancel: () => { }
      })
    },

    save() {
      this.$refs.formData.validate((valid) => {
        if (!valid) return
        this.form.category = 'normal'
        let copyDetail = JSON.parse(JSON.stringify(this.form))
        console.log(copyDetail,'copyDetail');
        if (!this.formOpen) {
          // this.$http
          //   .post('/api/flow/bpmn/create', copyDetail)
          request.createBpmn(copyDetail)
            .then((res) => {
              this.$Message.success('新建模型成功')
              this.queryList()
              this.open = false
            })
            .catch((error) => {
              this.$Message.error('新建模型失败:' + error.response.data.detail)
            })
        } else {
          // this.$http
          //   .put('/api/flow/bpmn/update', copyDetail)
          request.updateBpmn(copyDetail)
            .then((res) => {
              this.$Message.success('更新成功')
              this.open = false
              this.queryList()
            })
            .catch((error) => {
              this.$Message.error('更新失败:' + error.response.data.detail)
            })
        }
        this.editFlag = false
      })
    },
    resetFn() {
      this.$refs.form.resetFields()
      this.pageData.pageNum = 1
      this.queryList()
    },
    cancel() {
      this.open = false
      this.formOpen = false
      // this.$refs.formData.resetFields()
      this.editFlag = false
    },
    reset() {
      this.form = {
        id: undefined,
        key: undefined,
        name: undefined,
        description: undefined
      }
      if (this.$refs.formData !== undefined) this.$refs.formData.resetFields()
    },
    // 分页
    handleSizeChange(val) {
      this.pageData.pageSize = val
      this.queryList()
    },
    handleCurrentChange(val) {
      this.pageData.pageNum = val
      this.queryList()
    }
  }
}
</script>

<style lang="less" scoped>
@import '../admin/styles/formStyle.less';

/deep/ .el-dialog {
  z-index: 1000 !important;
}

::v-deep .el-form-item__content {
  line-height: 21px;
  padding-top: 1px;

  .vue-treeselect__icon {
    color: #515a6e;
  }

  .vue-treeselect__multi-value-item {
    background: #f7f7f7;
    color: #515a6e;
  }
}

.el-form-item {
  margin-bottom: 20px;
}

.el-dropdown {
  margin-right: 14px;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}

.el-icon-arrow-down {
  font-size: 12px;
}

.el-icon--right {
  margin-left: 0px !important;
}

.el-card {
  margin-bottom: 16px;
}

// .el-table__header-wrapper {
//   background-color: #f8f8f9;
// }

.formBtn {
  float: right;
  margin-bottom: 20px;
}

.el-pagination {
  float: right;
  margin-top: 40px;
}

::v-deep .el-dialog__body {
  overflow: visible;
  overflow: auto;
}

.ivu-tabs {
  overflow: visible;
}

.el-button--primary {
  padding: 8px 15px 8px 15px;
}

.el-button--default {
  padding: 8px 15px 8px 15px;
}

.el-button--info {
  padding: 8px 15px 8px 15px;
}

::v-deep .el-card__body {
  padding: 16px;
}

.formItemStyle {
  display: flex;
}

::v-deep .el-upload-dragger {
  height: 150px;
}

.el-icon-upload {
  margin: 26px 0 16px;
}
</style>
