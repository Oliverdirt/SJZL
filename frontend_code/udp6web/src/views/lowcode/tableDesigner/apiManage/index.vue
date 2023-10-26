<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea">
          <Form ref="queryForm" :model="queryForm" :label-width="120">
            <Row :gutter="40">
              <Col span="6" style="margin-bottom: 12px">
                <FormItem label="唯一名称" prop="modelName" style="width: 100%">
                  <Input type="text" :maxlength="100" v-model.trim="queryForm.modelName" placeholder="请输入唯一名称"
                         style="width: 100%"/>
                </FormItem>
              </Col>
              <!-- <Col span="6" style="margin-bottom: 12px">
                <FormItem label="模块名称" prop="moduleName" style="width: 100%">
                  <Input type="text" :maxlength="100" v-model.trim="queryForm.moduleName" placeholder="请输入模块名称"
                         style="width: 100%"/>
                </FormItem>
              </Col> -->
              <Col span="24" style="text-align: right">
                <FormItem :label-width="0">
                  <Button type="primary" @click="queryList(true)" style="margin-right: 8px">查询</Button>
                  <Button type="default" @click="handleReset">重置</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="tableBtn">
          <!-- <Button @click="clickDbBtn" type="primary" style="margin-right: 10px" icon="md-add">
            从数据库中新建数据模型
          </Button> -->
          <Button @click="
  okFlag = 'add';openModalType='add';
openAddModelDialog();
          " type="primary" style="margin-right: 10px" icon="md-add">新建API
          </Button>
          <Button icon="ios-trash-outline" type="error" @click="handleDelete()" style="margin-left: 5px">删除</Button>
        </div>
        <Table border :columns="columns" :data="data" @on-selection-change="rowSelected($event)" style="">
          <template slot-scope="{ row }" slot="opType">
            <span>{{ opTypeMap.get(row.business) }}</span>
          </template>
          <template slot-scope="{ row }" slot="createModeAction">
            <span>{{ typeMap.get(row.requestType) }}</span>
          </template>
          <template slot-scope="{ row, index }" slot="action">
            <Button  type="text" size="small" style="margin-right: 5px; color: #0056b5"
                    @click="
    okFlag = 'update';openModalType='check';
  viewTable(row, index);
              ">查看
            </Button>
            <Button  type="text" size="small" style="margin-right: 5px; color: #0056b5" @click="
  okFlag = 'update';openModalType='edit';
editTable(row, index);
            ">编辑
            </Button>
            <Button type="text" size="small" style="margin-right: 5px; color: #0056b5" @click="remove(row, index)">
              删除
            </Button>
          </template>
        </Table>
        <Page :total="pageData.total" :page-size="pageData.size" :current.sync="pageData.page" show-elevator show-sizer
              show-total @on-page-size-change="changePage($event, 'size')" @on-change="changePage($event, 'page')"
              style="margin-top: 20px"/>
      </Card>
    </div>
    <Modal v-model="modal1" :title="modalTitle">
      <Form ref="moduleRules" :model="detail" :rules="fieldRuleValidate" :label-width="80" style="margin-top: 20px">
        <FormItem label="模块名称" prop="moduleName">
          <Input type="text" :maxlength="100" v-model="detail.moduleName" placeholder="请输入模块名称"></Input>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="primary" @click="save" style="width: 80px">确定</Button>
        <Button type="default" @click="cancel" style="width: 80px">取消
        </Button>
      </div>
    </Modal>
    <el-dialog title="编辑" :visible.sync="editTableVisible" width="90%" append-to-body>
      <el-table :data="tableData" row-key="key" style="overflow-y:scroll" :row-style="{ height: '70px' }"
                default-expand-all :tree-props="{ children: 'childDto', hasChildren: 'hasChildren' }"
                @selection-change="handleSelectionChange">
        <el-table-column label="序号" hidden style="width:auto" width="100" align="center">
          <template slot-scope="scope">{{ scope.$index + 1 }}</template>
        </el-table-column>
        <el-table-column prop="columnName" label="数据库字段名" width="150" style="position: relative;">
          <template v-slot="scope">
            <el-input v-model="scope.row.columnName" ref="textDom" @blur="blur(scope.row)"
                      :disabled="scope.row.editFlag" placeholder="数据库字段名"></el-input>
            <div v-show="!scope.row.columnName">
              <span style="color:red;position: absolute;padding-bottom: 20px;">数据库字段名不能为空</span>
            </div>
            <div v-show="scope.row.columnName && scope.row.inValid">
              <span style="color:red;position: absolute;padding-bottom: 20px;">数据库表名或字段名重复</span>
            </div>
            <div v-show="mgc.indexOf(scope.row.columnName) !== -1">
              <span style="color:red;position: absolute;padding-bottom: 20px;">{{
                  scope.row.columnName
                }}与数据库系统字段重复</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="columnComment" label="字段名称" style="width:auto">
          <template v-slot="scope">
            <el-input v-model="scope.row.columnComment" placeholder="字段名称" :disabled="scope.row.editFlag"></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="columnType" label="字段类型" style="width:auto">
          <template v-slot="scope">
            <el-select v-model="scope.row.columnType" placeholder="请选择" :disabled="scope.row.editFlag"
                       popper-append-to-body>
              <el-option v-for="item in columnTypeArr" @click.native="sonTable(scope.row)" :key="item.typeNum"
                         :label="item.typeName" :value="item.typeNum">
              </el-option>
            </el-select>
            <div v-show="!scope.row.columnType">
              <span style="color:red;position: absolute;">字段类型不能为空</span>
            </div>
            <div
              v-show="scope.row.parentId && scope.row.columnType && scope.row.isFk === '1' && scope.row.columnType !== 5">
              <span style="color:red;position: absolute;">必须为bigint类型</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="字段长度" prop="columnLength" style="width:auto">
          <template v-slot="scope">
            <Input type="number" v-model="scope.row.columnLength" placeholder="请输入字段长度"></Input>
          </template>
        </el-table-column>
        <el-table-column label="小数点" prop="pointLength" style="width:auto">
          <template v-slot="scope">
            <Input type="number" v-model="scope.row.pointLength" placeholder="请输入小数点"></Input>
            <div
              v-show="(scope.row.columnType === 6 || scope.row.columnType===7 || scope.row.columnType===8) && (scope.row.pointLength<1)">
              <span style="color:red;position: absolute;padding-bottom: 20px;">不能小于1！</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="是否为空" prop="isNull" style="width:auto">
          <template v-slot="scope">
            <RadioGroup v-model="scope.row.isNull" :disabled="scope.row.editFlag">
              <Radio label="1" :disabled="scope.row.editFlag">是</Radio>
              <Radio label="0" :disabled="scope.row.editFlag">否</Radio>
            </RadioGroup>
          </template>
        </el-table-column>
        <el-table-column label="是否为主键" prop="isPk" style="width:auto">
          <template v-slot="scope">
            <RadioGroup v-model="scope.row.isPk" :disabled="scope.row.editFlag">
              <Radio label="1" :disabled="true">是</Radio>
              <Radio label="0" :disabled="true">否</Radio>
            </RadioGroup>
            <div v-show="scope.row.isPk === '1' && isShowIsPkError && !scope.row.editFlag">
              <span style="color:red;position: absolute;padding-bottom: 20px;">只能有一个主键</span>
            </div>
          </template>
        </el-table-column>
        <!-- <el-table-column label="是否为外键" prop="isFk" style="width:auto">
          <template v-slot="scope">
            <RadioGroup v-model="scope.row.isFk" :disabled="scope.row.editFlag">
              <Radio label="1" :disabled="scope.row.editFlag">是</Radio>
              <Radio label="0" :disabled="scope.row.editFlag">否</Radio>
            </RadioGroup>
          </template>
        </el-table-column> -->
        <el-table-column label="是否为业务主键" prop="isBk" style="width:auto">
          <template v-slot="scope">
            <RadioGroup v-model="scope.row.isBk">
              <Radio label="1" :disabled="scope.row.editFlag">是</Radio>
              <Radio label="0" :disabled="scope.row.editFlag">否</Radio>
            </RadioGroup>
          </template>
        </el-table-column>
        <el-table-column prop="columnRlation" label="关联主表字段" style="width:auto">
          <template v-slot="scope">
            <el-select v-model="scope.row.columnRelation" placeholder="请选择"
                       :disabled="scope.row.editFlag || !scope.row.parentId" popper-append-to-body>
              <template v-for="(item, index) in mainTableColumn">
                <el-option v-if="item.columnType == scope.row.columnType" :value="item.columnName" :key="index">{{
                    item.columnName
                  }}
                </el-option>
              </template>
            </el-select>
          </template>
        </el-table-column>

        <el-table-column label="操作" style="width:auto">
          <template v-slot="scope">
            <el-button size="mini" type="text" @click="deleteTable(scope.row)"
                       :disabled="scope.row.editFlag">删除
            </el-button>
            <el-button v-if="tableData.length >= 1" type="text" size="mini" @click="peerTable(scope.row)">新建
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 24px; margin-right: 24px" slot="footer">
        <el-button type="primary" :loading="confirmLoading" size="small" @click="passData">
          确定
        </el-button>
        <el-button type="primary" size="small" @click="passDataCancel">
          取消
        </el-button>
      </div>
    </el-dialog>
    <el-dialog width="80%" title="查看" :visible.sync="viewTableVisible">
      <!-- <div class="step4" v-show="viewTableVisible"> -->
      <div class="tableBox" v-if="(modelType === '1' || modelType === '3') && viewTableVisible">
        <el-table ref="multipleTable" :data="viewTableData" height="100%" tooltip-effect="dark" style="width: 100%">
          <el-table-column label="序号" hidden style="width:auto" width="120" align="center">
            <template slot-scope="scope">{{ scope.$index + 1 }}</template>
          </el-table-column>
          <el-table-column prop="columnName" label="字段名称"></el-table-column>
          <el-table-column prop="columnComment" label="字段描述" show-overflow-tooltip></el-table-column>
          <el-table-column prop="columnType" label="数据类型" show-overflow-tooltip></el-table-column>
          <el-table-column label="是否是主键" show-overflow-tooltip align="center">
            <template slot-scope="scope">
              <span>{{ (scope.row.columnKey === 'PRI' || scope.row.columnKey === 't') ? '是' : '否' }}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="tableBox" v-if="modelType === '2' && viewTableVisible">
        <el-table :data="viewTableData" style="width: 100%;margin-bottom: 20px;" row-key="id" height="100%" border
                  :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
          <el-table-column prop="columnName" label="字段名称"></el-table-column>
          <el-table-column prop="columnComment" label="字段描述" show-overflow-tooltip></el-table-column>
          <el-table-column prop="columnType" label="数据类型" show-overflow-tooltip></el-table-column>
          <el-table-column label="是否是主键" show-overflow-tooltip>
            <template slot-scope="scope">
              <span>{{ (scope.row.columnKey === 'PRI' || scope.row.columnKey === 't') ? '是' : '否' }}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <Button type="primary" @click="viewCancel" v-show="viewTableVisible" style="width: 72px">关闭</Button>
      </div>
      <!-- </div> -->
    </el-dialog>
    <DataSourceModel :createModelDialogVisible="createModelDialogVisible" :openModalType="openModalType" :tempRowData="tempRowData"></DataSourceModel>
    <!-- <CreateModel :createModelDialogVisible="createModelDialogVisible"></CreateModel> -->
    <!--    从数据库中创建数据模型弹窗 -->
    <!-- <addModelFromDb @childCancel="childCancel" v-if="createModelDbVisible" ref="addModelFromDb"></addModelFromDb> -->
  </div>
</template>

<script>
import DataSourceModel from './dataSourceModel.vue'
// import addModelFromDb from '@/views/lowcode/tableDesigner/dataModelMng/addModelFromDb'
import { v4 as uuidv4 } from 'uuid'

export default {
  name: 'apiManage',
  components: { DataSourceModel},
  data () {
    let validateFormName = (rule, value, callback) => {
      if (value.length > 15) {
        callback(new Error('模块名长度不能超过15个字符'))
      } else if (value.length <= 15) {
        const [url, httpConfig] = [
          '/api/getCscpCustomizeModuleByName',
          {
            params: { moduleName: value },
          },
        ]
        this.$http
          .get(url, httpConfig)
          .then((response) => {
            if (response.data.length > 0) {
              callback(new Error('模块名已重复'))
            } else {
              callback()
            }
          })
          .catch(() => {
            callback(new Error('异步校验出错！'))
          })
      }
    }
    return {
      tempRowData:{},
      typeMap:[],
      opTypeMap:[],
      openModalType:'add',
      isShowIsPkError: false,
      confirmLoading: false,
      checkColumnNameCopy: '1',
      mainTableId: '',
      childTableId: [],
      rowData: {},
      editModelVisible: false,
      moduleIdName: '',
      createModelDialogVisible: false,
      mainTableName: undefined,
      fieldRuleValidate: {
        moduleName: [
          { required: true, message: '模块名称不能为空！', trigger: 'blur' },
          { required: true, validator: validateFormName, trigger: 'blur' },
        ],
      },
      formTypeOption: [
        { value: '0', label: '单表' },
        { value: '1', label: '多表' },
        { value: '3', label: '流程表' },
      ],
      isShowTip: false,
      modal1: false,
      selectedArr: [],
      queryForm: {},
      formId: null,
      formType: '0',
      mgc: [],
      pageData: {
        total: 0,
        size: 10,
        page: 1,
      },
      columns: [
        {
          type: 'selection',
          width: 60,
          align: 'center',
        },
        // {
        //   type: 'index',
        //   align: 'center',
        //   title: '序号',
        //   width: 70,
        // },
        {
          title: '唯一名称',
          key: 'name',
          align: 'center',
        },
        {
          title: '请求地址',
          key: 'url',
          align: 'center',
          tooltip:true
        },
        {
          title: '描述信息',
          key: 'desc',
          align: 'center',
          tooltip:true
        },
        {
          title: '操作类型',
          key: 'business',
          align: 'center',
          slot: 'opType',
        },
        {
          title: '请求方法',
          key: 'requestType',
          align: 'center',
          slot: 'createModeAction',
        },

        {
          title: '创建时间',
          key: 'createTime',
          align: 'center',
        },
        {
          title: '操作',
          slot: 'action',
          width: 200,
          align: 'center',
        },
      ],
      data: [],
      detail: {},
      okFlag: 'add',
      modalValid: true,
      ruleValidate: {},
      modalTitle: '',
      typeDisabled: false,
      tagetRow: '',
      moduleName: '',
      moduleId: '',
      editTableVisible: false,
      viewTableVisible: false,
      tableData: [],
      viewTableData: [],
      columnTypeArr: [],
      operateType: '1',
      createModelDbVisible: false,
      modelType: null,
      mainTableColumn: [],
    }
  },
  created(){
   this.typeMap =new Map([
        [0,'get'],
        [1,'post'],
        [2,'put'],
        [3,'delete']
      ]);
    this.opTypeMap = new Map([
        [0,'新增'],
        [1,'删除'],
        [2,'编辑'],
        [3,'查找']
      ]);
  },
  mounted () {
    const moduleName = this.$route.params.moduleName
    this.queryForm.moduleName = moduleName
    this.queryList()
    // this.getColumnType()
    if (this.$util.Setting.dataBankType === '0') {
      this.mgc = this.$util.Setting.columnNamesql
    } else if (this.$util.Setting.dataBankType === '1' || this.$util.Setting.dataBankType === '2') {
      this.mgc = this.$util.Setting.columnNamegs
    }
  },
  methods: {
    // 第三步
    // 新建 or 编辑
    reset (type) {
      this.tableData = []
      this.newLoading = true

      if (type === 1) { // 新建
        this.tableData = [
          {
            id: Math.random() + '',
            key: Math.random() + '',
            columnName: 'id',
            columnComment: '主键id',
            columnType: 5,
            columnLength: null,
            pointLength: null,
            isNull: '0',
            isPk: '1',
            isFk: '0',
            isBk: '0',
            showInput: true,
            inValid: false,
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            columnName: 'tenant_id',
            columnComment: '租户id',
            columnType: 5,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            isBk: '0',
            showInput: true,
            inValid: false,
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            columnName: 'created_time',
            columnComment: '创建时间',
            columnType: 12,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            isBk: '0',
            showInput: true,
            inValid: false,
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            columnName: 'created_userid',
            columnComment: '创建用户',
            columnType: 5,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            isBk: '0',
            showInput: true,
            inValid: false,
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            columnName: 'updated_time',
            columnComment: '更新时间',
            columnType: 12,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            isBk: '0',
            showInput: true,
            inValid: false,
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            columnName: 'updated_userid',
            columnComment: '更新人',
            columnType: 5,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            isBk: '0',
            showInput: true,
            inValid: false,
          }
        ]
        this.tableData = this.flatten(this.tableData, 1)
      }
      if (type === 2) { // 编辑
        if (this.treeArr.length > 0) {
          this.tableData = this.treeArr
        } else {
          this.tableData = [
            {
              id: Math.random() + '',
              key: Math.random() + '',
              columnName: '',
              columnComment: '',
              columnType: '',
              columnLength: null,
              pointLength: null,
              isNull: '1',
              isPk: '0',
              isFk: '0',
              isBk: '0',
              showInput: true,
              inValid: false,
            },
          ]
        }
        this.tableData = this.flatten(this.tableData, 2)
      }

      this.tableSelected = {}
      this.faTable = {}
      this.tableData.forEach((item) => {
        item.first = 1
      })
      this.newLoading = false
    },
    // 获取列表并保存数据
    passData () {
      // 最终数据+++++++++++++++++++++++++++++++++++
      //主表数据加工
      this.confirmLoading = true
      var result = {
        'columns': [],
        'cscpHxFormTables': [],
        'subTableFkName': '',
        'subTableName': '',
        'tableContent': '',
        'tableName': '',
        'tableId': null
      }
      var datas = this.tableData
      //子表名用逗号拼接
      var subTableFkName = ''
      //子表外键用逗号拼接
      var subTableName = ''
      //字段是否合法标识
      var validDataFlag = true
      //表名称
      var tableNames = []
      //主表名称放入
      tableNames.push(this.mainTableName)
      //错误信息提示
      var errMessage = ''
      //主表字段名称
      var mainClumnNames = []
      //主表字段列表
      var mainColumnArr = []
      for (let i = 0; i < datas.length; i++) {
        if (!datas[i].childDto) {
          //是主表字段，非子表进行加工
          var columns = {
            'columnComment': datas[i].columnComment !== null ? datas[i].columnComment.trim() : datas[i].columnComment,
            'columnLength': datas[i].columnLength,
            'columnName': datas[i].columnName !== null ? datas[i].columnName.trim() : datas[i].columnName,
            'pointLength': datas[i].pointLength,
            'columnType': datas[i].columnType,
            'isNull': datas[i].isNull,
            'isPk': datas[i].isPk,
            'isFk': datas[i].isFk,
            'isBk': datas[i].isBk,
          }
          //校验表明及字段类型是否为空
          if (!datas[i].columnName || !datas[i].columnType) {
            validDataFlag = false
            errMessage = '主表字段名称及字段类型不能为空'
            break
          }
          // if(!datas[i].columnName && datas[i].columnType == 25){
          //   validDataFlag = false;
          //   errMessage = "主表字段名称及字段类型不能为空";
          //   break;
          // }
          //字段名称与数据库系统
          if (this.mgc.indexOf(datas[i].columnName) !== -1) {
            validDataFlag = false
            errMessage = '字段名称与数据库系统字段不能重复'
            break
          }
          //校验主表字段名称是否重复
          if (datas[i].columnType != 25 && mainClumnNames.indexOf(datas[i].columnName) > -1) {
            validDataFlag = false
            errMessage = '主表字段名称不能重复'
            break
          }
          if ((datas[i].columnType === 6 || datas[i].columnType === 7 || datas[i].columnType === 8) && (datas[i].pointLength < 1)) {
            validDataFlag = false
            errMessage = '小数点位数不能小于1！'
            break
          }
          mainClumnNames.push(datas[i].columnName)
          mainColumnArr[i] = columns
        } else {
          //如果含有子表进行子表数据加工
          var childResult = {
            'columns': [],
            'cscpHxFormTables': [],
            'subTableFkName': '',
            'subTableName': '',
            'tableContent': '',
            'tableName': '',
            'tableId': null
          }
          var childColumnArr = []
          if (tableNames.indexOf(datas[i].columnName) > -1) {
            validDataFlag = false
            errMessage = '主子表之间、子表之间表名不能重复'
            break
          }
          if (!datas[i].columnName && datas[i].columnType == 25) {
            validDataFlag = false
            errMessage = '主表字段名称及字段类型不能为空'
            break
          }
          //子表字段名称 用于校验子表字段是否重复
          var childClumnNames = []
          for (let j = 0; j < datas[i].childDto.length; j++) {
            var childColumns = {
              'columnComment': datas[i].childDto[j].columnComment,
              'columnLength': datas[i].childDto[j].columnLength,
              'columnName': datas[i].childDto[j].columnName,
              'pointLength': datas[i].childDto[j].pointLength,
              'columnType': datas[i].childDto[j].columnType,
              'isNull': datas[i].childDto[j].isNull,
              'isPk': datas[i].childDto[j].isPk,
              'isFk': datas[i].childDto[j].isFk,
              'isBk': datas[i].childDto[j].isBk,
              'columnRelation': datas[i].childDto[j].columnRelation,
            }
            if (!datas[i].childDto[j].columnName || !datas[i].childDto[j].columnType) {
              validDataFlag = false
              errMessage = '子字段名称及字段类型不能为空'
              break
            }
            //校验子表字段名称是否重复
            if (childClumnNames.indexOf(datas[i].childDto[j].columnName) > -1) {
              validDataFlag = false
              errMessage = '子表字段名称不能重复'
              break
            }
            if ((datas[i].columnType === 6 || datas[i].columnType === 7 || datas[i].columnType === 8) && (datas[i].pointLength < 1)) {
              validDataFlag = false
              errMessage = '小数点位数不能小于1！'
              break
            }
            //校验子表字段如果是外键,字段类型必须为bigint
            // if (datas[i].childDto[j].isFk === '1') {
            //   if (datas[i].childDto[j].columnType !== 5) {
            //     validDataFlag = false;
            //     errMessage = "子字段为外键时字段类型必须为bigint";
            //     break;
            //   }
            // }
            // 如果含有外键 拼接 外键字段 及表名
            if (datas[i].childDto[j].isFk === '1') {
              if (subTableFkName.includes(',')) {
                subTableFkName = subTableFkName.concat(',' + datas[i].childDto[j].columnName)
                subTableName = subTableName.concat(',' + datas[i].columnName)
              } else {
                subTableFkName = subTableFkName.concat(datas[i].childDto[j].columnName)
                subTableName = subTableName.concat(datas[i].columnName)
              }

            }
            childColumnArr[j] = childColumns
          }
          tableNames.push(datas[i].columnName)
          childResult.columns = childColumnArr
          //子表名
          childResult.tableName = datas[i].columnName
          //子表解释说明
          childResult.tableContent = datas[i].columnComment
          //子表tableId
          if (this.childTableId.length > 0) {
            for (let k = 0; k < this.childTableId.length; k++) {
              if (this.childTableId[k].tableName === datas[i].columnName) {
                childResult.tableId = this.childTableId[k].tableId
              }
            }
          }
          //子表列表
          result.cscpHxFormTables[result.cscpHxFormTables.length] = childResult
          if (!validDataFlag) {
            break
          }
        }
      }
      // 校验一张子表只能有一个主键
      if (result.cscpHxFormTables && result.cscpHxFormTables.length > 0) {
        for (let i = 0; i < result.cscpHxFormTables.length; i++) {
          let tempArr = result.cscpHxFormTables[i].columns
          if (tempArr.filter(item => item.isPk === '1').length > 1) {
            this.$message.error('一张子表只能包含一个主键')
            this.confirmLoading = false
            this.isShowIsPkError = true
            return
          }
        }
      }
      result.columns = mainColumnArr.filter(item => item)
      if (result.columns && result.columns.length > 0) {
        let isPkArr = result.columns.filter(item => item.isPk === '1')
        if (isPkArr.length > 1) {
          this.$message.error('一张表只能包含一个主键')
          this.confirmLoading = false
          this.isShowIsPkError = true
          return
        }
      }
      result.subTableFkName = subTableFkName
      result.subTableName = subTableName
      //主表名
      // 修改
      result.tableName = this.rowData.tableName
      //主表tableId
      if (this.mainTableId && this.mainTableId != null) {
        result.tableId = this.mainTableId
      }
      //主表解释说明
      // 修改
      result.tableContent = this.rowData.tableDescription

      // operateType:  "1" 为修改  其他为新增
      if (validDataFlag) {
        if (this.operateType === '1') {
          this.$http
            .put('/api/lowcode/modelDesign/cscpHxFormTables', result)
            .then((res) => {
              // 重置数据
              this.tableData = []
              this.$Message.success({
                background: true,
                content: '修改成功'
              })
              this.editTableVisible = false
              this.confirmLoading = false
              this.isShowIsPkError = false
              this.queryList(true)
            })
            .catch(() => {
              this.$Message.error({
                background: true,
                content: '修改失败'
              })
              this.confirmLoading = false
            })
        }
      } else {
        this.confirmLoading = false
        // this.$Message.error(errMessage);
        this.$message({ message: errMessage, type: 'error', customClass: 'war-message' })
      }
    },
    // 删除名称为空的数据
    nullFlatten (arr) {
      arr.map((item) => {
        if (item.childDto !== undefined) {
          for (let i = 0; i < item.childDto.length; i++) {
            if (item.childDto[i].branchName === '') {
              item.childDto = item.childDto.filter((items) => {
                return items.id !== item.childDto[i].id
              })
            }
          }
        }
        if (Array.isArray(item.childDto)) {
          this.nullFlatten(item.childDto)
        }
      })
      return arr
    },
    // 名称不为空切失去焦点时隐藏input
    inputBlur (val) {
      if (val.branchName) {
        val.showInput = false
      }
      // val.showInput = false;
    },
    // 点击编辑名称
    showInput (val) {
      val.showInput = !val.showInput
    },
    // 选中方法
    handleSelectionChange (val) {
      if (val.length > 0) {
        this.tableSelected = val[0]
        this.tableData = this.flattenId(this.tableData, val[0].id)
        this.faTable = val[0]
      } else {
        this.tableSelected = {}
        this.faTable = {}
        this.tableData = this.flatten(this.tableData, 1)
      }
    },
    // 选中状态
    flatten (arr, type) {
      arr.map((item) => {
        this.$set(item, 'status', 1)
        this.$set(item, 'key', Math.random() + '')
        if (type === 1 && item.branchName === '') {
          this.$set(item, 'showInput', true)
        } else {
          this.$set(item, 'showInput', false)
        }
        if (Array.isArray(item.childDto)) {
          this.flatten(item.childDto, type)
        }
      })
      return arr
    },
    // 修改选中其他状态
    flattenId (arr, id) {
      arr.map((item) => {
        if (item.id === id) {
          this.$set(item, 'status', 1)
        } else {
          this.$set(item, 'status', 0)
        }
        if (Array.isArray(item.childDto)) {
          this.flattenId(item.childDto, id)
        }
      })
      return arr
    },

    blur (rowData) {
      if (this.checkColumnName !== rowData.columnName) {
        this.checkColumnName = rowData.columnName
      }
      if (rowData.columnType === 25 && !rowData.editFlag) {
        this.$http.get(`api/lowcode/modelDesign/validation/cscpHxFormTable?name=${this.checkColumnName}`).then((response) => {
          // if(response){
          // }
        }).catch((err) => {
          if (err.response.data.detail) {
            rowData.inValid = true
          }
        })
      }
      var datas = this.tableData
      //表名称
      var tableNames = []
      //主表名称放入
      if (this.mainTableName) {
        tableNames.push(this.mainTableName)
      }
      //主表字段名称
      var mainClumnNames = []
      //是否是否合法标识
      var validDataFlag = true
      for (let i = 0; i < datas.length; i++) {
        if (!datas[i].childDto || datas[i].childDto.length == 0) {
          //校验主表字段名称是否重复
          if (datas[i].columnType != 25 && datas[i].columnName && datas[i].columnName != '') {
            if (mainClumnNames.indexOf(datas[i].columnName) > -1) {
              rowData.inValid = true
              validDataFlag = false
              break
            }
            mainClumnNames.push(datas[i].columnName)
          }
        } else {
          //子表字段名称 用于校验子表字段是否重复
          var childClumnNames = []
          if (rowData.columnType === 25 && tableNames.indexOf(datas[i].columnName) > -1) {
            // this.checkColumnNameCopy = this.checkColumnName
            this.$set(rowData, 'inValid', true)
            validDataFlag = false
            break
          }
          tableNames.push(datas[i].columnName)
          for (let j = 0; j < datas[i].childDto.length; j++) {
            //校验子表字段名称是否重复
            if (rowData.parentId == datas[i].childDto[j].parentId) {
              if (datas[i].childDto[j].columnName &&
                datas[i].childDto[j].columnName != '' &&
                childClumnNames.indexOf(datas[i].childDto[j].columnName) > -1) {
                // this.checkColumnNameCopy = this.checkColumnName
                rowData.inValid = true
                validDataFlag = false
                break
              }
              childClumnNames.push(datas[i].childDto[j].columnName)
            }
          }
          //如果字符非法跳出大循环
          if (!validDataFlag) {
            break
          }
        }
      }
      if (validDataFlag) {
        rowData.inValid = false
      }
    },
    // 新建子节点
    sonTable (row) {
      if (row.parentId) {
        this.mainTableColumn = []
        this.tableData.map((item) => {
          if (item && item.columnType != 25
            //  && (!item.isOriginal || item.columnName == 'id')
            //  && item.columnType == row.columnType
          ) {
            this.mainTableColumn.push(item)
          }
        })
      }
      this.checkColumnName = row.columnName
      if (row.columnType === 25 && this.checkColumnName) {
        this.blur(row)
      }
      if (row.columnType == 25) {
        let arr = [
          {
            id: Math.random() + '',
            key: Math.random() + '',
            parentId: row.id,
            columnName: 'id',
            columnComment: '主键id',
            columnType: 5,
            columnLength: null,
            pointLength: null,
            isNull: '0',
            isPk: '1',
            isFk: '0',
            isBk: '0',
            showInput: true,
            inValid: false,
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            parentId: row.id,
            columnName: 'tenant_id',
            columnComment: '租户id',
            columnType: 5,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '1',
            isBk: '0',
            showInput: true,
            inValid: false,
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            parentId: row.id,
            columnName: 'created_time',
            columnComment: '创建时间',
            columnType: 12,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            isBk: '0',
            showInput: true,
            inValid: false,
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            parentId: row.id,
            columnName: 'created_userid',
            columnComment: '创建用户',
            columnType: 5,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            isBk: '0',
            showInput: true,
            inValid: false,
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            parentId: row.id,
            columnName: 'updated_time',
            columnComment: '更新时间',
            columnType: 12,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            isBk: '0',
            showInput: true,
            inValid: false,
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            parentId: row.id,
            columnName: 'updated_userid',
            columnComment: '更新人',
            columnType: 5,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            isBk: '0',
            showInput: true,
            inValid: false,
          }
        ]
        this.$set(row, 'childDto', arr)
      } else {
        this.$set(row, 'childDto', null)
      }
    },
    // 新建同级节点
    peerTable (row) {
      this.checkColumnName = ''
      if (row) {
        this.tableSelected = row
        this.tableData = this.flattenId(this.tableData, row.id)
        this.faTable = row
      } else {
        this.tableSelected = {}
        this.faTable = {}
        this.tableData = this.flatten(this.tableData, 1)
      }
      this.peerLoading = true
      let obj = {
        id: Math.random() + '',
        key: Math.random() + '',
        columnName: '',
        columnComment: '',
        columnType: '',
        columnLength: null,
        pointLength: null,
        isNull: '1',
        isPk: '0',
        isFk: '0',
        isBk: '0',
        showInput: true,
        first: 1,
        inValid: false,
      }
      if (JSON.stringify(this.faTable) !== '{}') {
        if (this.faTable.first === 1) {
          this.tableData.forEach((item) => {
            if (item.status === 1) {
              this.tableData.push(obj)
            }
          })
        } else {
          this.tableData = this.sonFlatten(this.tableData, this.faTable.id)
        }
      }
      this.peerLoading = false
    },
    // 子项新建同级节点
    sonFlatten (arr, id) {
      let obj = {
        id: Math.random() + '',
        key: Math.random() + '',
        parentId: this.faTable.id,
        branchName: '',
        columnLength: null,
        pointLength: null,
        isNull: '1',
        isPk: '0',
        isFk: '0',
        isBk: '0',
        showInput: true,
        inValid: false,
      }
      arr.map((item) => {
        if (item.childDto !== undefined && item.childDto !== null) {
          for (let i = 0; i < item.childDto.length; i++) {
            if (item.childDto[i].id === id) {
              item.childDto.push(obj)
            }
          }
        }
        if (Array.isArray(item.childDto)) {
          this.sonFlatten(item.childDto, id)
        }
      })
      return arr
    },
    // 删除
    deleteTable (val) {
      //如果删除的是父节点字段，子节点关联字段清除
      if (!val.parentId) {
        // var tempTableData = [];
        this.tableData.map(item => {
          if (item.childDto) {
            item.childDto.map(item1 => {
              if (item1.columnRelation == val.columnName) {
                item1.columnRelation = null
              }
            })
          }
        })
        var tempMainTableColumn = []
        this.mainTableColumn.map(item => {
          if (item.columnName != val.columnName) {
            tempMainTableColumn.push(item)
          }
        })
        this.mainTableColumn = tempMainTableColumn
      }
      if (val.first === 1) {
        this.tableData = this.tableData.filter((item) => {
          return item.id !== val.id
        })
      } else {
        this.tableData = this.delFlatten(this.tableData, val.id)
      }
    },
    // 子节点删除
    delFlatten (arr, id) {
      arr.map((item) => {
        if (item.childDto !== undefined && item.childDto !== null) {
          for (let i = 0; i < item.childDto.length; i++) {
            if (item.childDto[i].id === id) {
              item.childDto = item.childDto.filter((items) => {
                return items.id !== id
              })
            }
          }
        }
        if (Array.isArray(item.childDto)) {
          this.delFlatten(item.childDto, id)
        }
      })
      return arr
    },
    getColumnType () {
      this.$http.get('/api/schema/getColumnTypeList').then((res) => {
        this.columnTypeArr = res.data
      })
    },
    // 编辑的时候数据回显
    initData (tableId) {
      this.operateType = '1'
      let url = ''
      if (tableId) {
        url = `/api/lowcode/modelDesign/cscpHxFormTablesAll/${tableId}`
      } else {
        url = `/api/lowcode/modelDesign/cscpHxFormTablesAll`
      }
      this.$http.get(url).then((res) => {
        if (res.data && res.data.columns && res.data.columns.length > 0) {
          if (res.data) {
            var data = []
            var datas = res.data
            this.mainTableName = res.data.tableName
            this.mainTableColumn = []
            //主表column
            for (let i = 0; i < datas.columns.length; i++) {
              this.mainTableColumn.push(datas.columns[i])
              var column = {
                id: Math.random() + '',
                key: Math.random() + '',
                columnName: datas.columns[i].columnName,
                columnComment: datas.columns[i].columnComment,
                columnType: datas.columns[i].columnType,
                columnLength: datas.columns[i].columnLength,
                pointLength: datas.columns[i].pointLength,
                isNull: datas.columns[i].isNull + '',
                isPk: datas.columns[i].isPk + '',
                isFk: datas.columns[i].isFk + '',
                isBk: datas.columns[i].isBk + '',
                childDto: null,
                first: 1,
                showInput: true,
                editFlag: true,
                inValid: false,
              }
              data[i] = column
            }
            //主表tableId
            this.mainTableId = datas.tableId
            if (datas.cscpHxFormTables) {
              for (let j = 0; j < datas.cscpHxFormTables.length; j++) {
                var tableColumn = {
                  id: Math.random() + '',
                  key: Math.random() + '',
                  columnName: datas.cscpHxFormTables[j].tableName,
                  columnComment: datas.cscpHxFormTables[j].tableContent,
                  columnType: 25,
                  columnLength: null,
                  pointLength: null,
                  isNull: '1',
                  isPk: '0',
                  isFk: '0',
                  isBk: '0',
                  first: 1,
                  childDto: null,
                  showInput: true,
                  editFlag: true,
                  inValid: false,
                }
                var cDto = []
                for (let k = 0; k < datas.cscpHxFormTables[j].columns.length; k++) {
                  var childColumn = {
                    id: Math.random() + '',
                    key: Math.random() + '',
                    columnName: datas.cscpHxFormTables[j].columns[k].columnName,
                    columnComment: datas.cscpHxFormTables[j].columns[k].columnComment,
                    columnType: datas.cscpHxFormTables[j].columns[k].columnType,
                    columnLength: datas.cscpHxFormTables[j].columns[k].columnLength,
                    pointLength: datas.cscpHxFormTables[j].columns[k].pointLength,
                    isNull: datas.cscpHxFormTables[j].columns[k].isNull + '',
                    isPk: datas.cscpHxFormTables[j].columns[k].isPk + '',
                    isFk: datas.cscpHxFormTables[j].columns[k].isFk + '',
                    isBk: datas.cscpHxFormTables[j].columns[k].isBk + '',
                    columnRelation: datas.cscpHxFormTables[j].columns[k].columnRelation,
                    childDto: null,
                    showInput: true,
                    editFlag: true,
                    inValid: false,
                  }
                  cDto[k] = childColumn
                }
                tableColumn.childDto = cDto
                data[data.length] = tableColumn
                var childTableMesage = {
                  'tableName': datas.cscpHxFormTables[j].tableName,
                  'tableId': datas.cscpHxFormTables[j].tableId
                }
                this.childTableId[j] = childTableMesage
              }
            }
            this.tableData = data
            this.tableData = this.flatten(this.tableData, 1)
            // 1 为编辑
            this.operateType = '1'
          }
          return res
        }
      })

    },
    passDataCancel () {
      this.tableData = []
      this.editTableVisible = false
    },
    viewCancel () {
      // this.tableData = []
      this.viewTableVisible = false
    },
    editModelClose () {
    },
    openAddModelDialog () {
      this.createModelDialogVisible = true
    },
    getModelDataById () {
    },
    createPage () {
    },
    handleTableChild (row) {
      this.$byStoreSet('mainForm', row)
      this.$router.push({
        name: 'sub-lowcode-programming',
      })
    },
    // 表单名称是否重复 验证
    judgeFormNameRepeat () {
      let params = { formName: this.detail.formName }
      this.$http
        .get('/api/lowcode/customize/getCscpCustomizeVformByName', { params })
        .then((res) => {
          res.data.length ? (this.isShowTip = true) : (this.isShowTip = false)
        })
    },
    editTable (row) {
      this.createModelDialogVisible = true
      this.tempRowData={...row}
      // if (row.modelType === '1') {
      //   this.$http.get('/api/schema/getColumnTypeList').then((res) => {
      //     this.columnTypeArr = res.data
      //   })
      // } else if (row.modelType === '2') {
      //   this.$http.get('/api/schema/getColumnTypeList?type=2').then((res) => {
      //     this.columnTypeArr = res.data
      //   })
      // }
      // this.rowData = row
      // // 展示编辑数据模型对应的表的表格
      // this.initData(row.tableId)
      // this.editTableVisible = true
    },
    viewTable (row,index) {
      // console.log('sjsjee',row,index)
      this.createModelDialogVisible = true
      this.tempRowData={...row}
      // this.rowData = row
      // this.modelType = row.modelType
      // // 展示编辑数据模型对应的表的表格
      // // this.initData(row.tableId)
      // this.getViewTableList(row)
    },
    // 获取列表数据
    getViewTableList (row) {
      this.viewTableData = []
      this.$http.get('/api/lowcode/modelDesign/cscpHxFormTablesAll/' + row.tableId).then(res => {
        //主表表名
        var mainTableName = res.data.tableName
        //子表表名
        var subTableNames = ''
        //设置子表用逗号隔开
        if (res.data.cscpHxFormTables && res.data.cscpHxFormTables.length > 0) {
          for (let item in res.data.cscpHxFormTables) {
            if (item == 0) {
              subTableNames = subTableNames + res.data.cscpHxFormTables[item].tableName
            } else {
              subTableNames = subTableNames + ',' + res.data.cscpHxFormTables[item].tableName
            }
          }
        }
        //设置tableName 用逗号隔开
        var tableNames = row.modelType === '2' ? mainTableName + ',' + subTableNames : mainTableName
        this.$http.get('/api/cscpHxFormColumsBatch', { params: { tableName: tableNames } }).then(res1 => {
          //主子表
          if (row.modelType === '2') {
            for (let i = 0; i < res1.data[mainTableName].length; i++) {
              res1.data[mainTableName].id = uuidv4()
              this.viewTableData.push(res1.data[mainTableName][i])
            }
            var subTableNameList = subTableNames.split(',')
            for (let j = 0; j < subTableNameList.length; j++) {
              if (subTableNameList[j] == null || subTableNameList[j] == '') {
                continue
              }
              this.viewTableData.push({
                columnComment: '',
                columnName: subTableNameList[j],
                columnType: 'table',
                children: res1.data[subTableNameList[j]].map(item => {
                  item.id = uuidv4()
                  return item
                }),
                id: uuidv4()
              })
            }
          } else if (row.modelType === '1' || row.modelType === '3') {
            this.viewTableData = res1.data[mainTableName]
          }
          this.viewTableVisible = true
        })
      })
    },
    save () {
      this.$refs.moduleRules.validate((valid) => {
        if (valid) {
          if (this.isShowTip) return false
          if (this.okFlag === 'update') {
            this.$http
              .put('api/cscpCustomizeModules', this.detail)
              .then((res) => {
                this.$Message.success('修改成功')
                // this.modal1 = false
                this.queryList()
                this.$refs['moduleRules'].resetFields()
              })
              .catch(() => {
                this.$Message.error('修改失败')
              })
            this.modal1 = false
          } else if (this.okFlag === 'add') {
            if (this.detail.moduleName) {
              let copyDetail = JSON.parse(JSON.stringify(this.detail))
              this.$http
                .post('/api/cscpCustomizeModules', copyDetail)
                .then((res) => {
                  this.$Message.success('新建模块成功')
                  this.queryList()
                })
                .catch(() => {
                  this.$Message.error('新建模块失败')
                })
              this.modal1 = false
            }
          }
        } else {
          this.modal1 = true
          this.$Message.error({
            background: true,
            content: '检验数据失败！',
          })
        }
      })
    },
    changePage (e, type) {
      if (type === 'size') {
        this.pageData.size = e
      } else if (type === 'page') {
        this.pageData.page = e
      }
      this.queryList()
    },
    handleReset () {
      this.$refs.queryForm.resetFields()
      this.$route.params.moduleName = ''
      this.selectedArr = []
      this.queryList(true)
    },
    rowSelected (e) {
      this.selectedArr = e
    },
    remove (row, index) {
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除这条数据？</p>',
        onOk: () => {
          this.$http
            .post('/api/source/deleteSourceConfig' , [row.id])
            .then((res) => {
              if (res.data.code === 200) {
                this.$Message.success(res.data.msg)
              } else {
                this.$Message.error(res.data.msg)
              }
              this.queryList()
            })
            .catch(() => {
              this.$Message.error('删除失败')
            })
        },
        onCancel: () => {
        },
      })
    },
    ok (type) {
      this.$delete(this.detail, '')
      this.$delete(this.detail, '_rowKey')
      let copyDetail = JSON.parse(JSON.stringify(this.detail))
      if (this.detail.createdTime) {
        copyDetail.createdTime = this.$moment(
          new Date(this.detail.createdTime)
        ).format('yyyy-MM-DD HH:mm:ss')
      }

      this.$refs['detail'].validate((valid) => {
        if (valid) {
          if (type === 'update') {
            this.$http
              .put('/api/customizeVforms', copyDetail)
              .then((res) => {
                this.$Message.success('修改成功')
                this.queryList()
                this.$refs['moduleRules'].resetFields()
              })
              .catch(() => {
                this.$Message.error('修改失败')
              })
          } else if (type === 'add') {
            this.$http
              .post('/api/customizeVforms', copyDetail)
              .then((res) => {
                this.$Message.success('新增成功')
                this.$refs['queryForm'].resetFields()
                this.queryList()
                this.$refs['moduleRules'].resetFields()
              })
              .catch(() => {
                this.$Message.error('新增失败')
              })
          }
        } else {
          // this.modal = true
        }
      })
    },
    queryList (flag) {
      if (flag) {
        this.pageData.page = 1
      }
      const page = this.pageData.page
      const size = this.pageData.size
      // 这里的模块名称是来自于页面跳转携带的参数还是取自查询输入框
      const moduleName = this.queryForm.moduleName || this.$route.params.moduleName || ''
      const modelName = this.queryForm.modelName || ''
      // let url = ''
      // if (moduleName && modelName) {
      //   url = `api/cscpCustomizeModels?moduleName.contains=${moduleName}&modelName.contains=${modelName}&page=${page}&size=${size}&sort=create_time,desc`
      // } else if (!moduleName && modelName) {
      //   url = `api/cscpCustomizeModels?modelName.contains=${modelName}&page=${page}&size=${size}&sort=create_time,desc`
      // } else if (moduleName && !modelName) {
      //   url = `api/cscpCustomizeModels?moduleName.contains=${moduleName}&page=${page}&size=${size}&sort=create_time,desc`
      // } else {
      //   url = `api/cscpCustomizeModels?page=${page}&size=${size}&sort=create_time,desc`
      // }
      this.$http.post(`/api/source/queryListSourceConfig?name=${modelName}&page=${page}&size=${size}`).then((res) => {
        this.data = res.data.data.data
        this.pageData.total = res.data.data.recordsTotal
      })
        .catch(() => {
          this.$Message.error('列表查询失败')
        })
    },
    cancel () {
      this.modal1 = false
      this.$refs['moduleRules'].resetFields()
    },
    handleDelete () {
      let arr = this.selectedArr.map((item) => item.id)
      if (arr.length < 1) {
        return this.$Message.error('删除失败,当前选择列表为空！')
      }
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除勾选的数据？</p>',
        onOk: () => {
          this.$http
            .post('/api/source/deleteSourceConfig' , arr)
            .then((res) => {
              if (res.data.code === 500) {
                this.$Message.error(res.data.msg)
                return
              }
              this.$Message.success('删除成功')
              this.selectedArr = []
              this.queryList()
            })
            .catch((error) => {
              if (error.response) {
                this.$Message.error(
                  error.response.data.title
                    ? error.response.data.title
                    : '删除失败！'
                )
              }
            })
        },
        onCancel: () => {
        },
      })
    },
    clickDbBtn () {
      this.createModelDbVisible = true
      setTimeout(() => {
        this.$refs.addModelFromDb.createModelDbVisible = true
      }, 0)
    },
    childCancel () {
      this.createModelDbVisible = false
    }
  },
}
</script>
<style lang="less">
.myTipContainer {
  .ivu-input {
    border-color: red;
  }
}
</style>
<style lang="less" scoped>
@import "~@/views/admin/styles/formStyle.less";

.war-message {
  z-index: 3000 !important;
}

.modalHeaderClass {
  position: relative;
  color: #2d8cf0;
  font-size: 16px;
}

.normal {
  margin-right: 5px;
  color: #0056b5;
}

.red {
  margin-right: 5px;
  color: red;
}

.tableBox {
  height: 400px;
}
</style>
