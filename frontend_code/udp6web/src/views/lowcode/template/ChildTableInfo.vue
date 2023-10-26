<template>
  <div id="childTableBox">
    <el-tabs
      type="border-card"
      @tab-click="handleTabClick"
    >
      <el-tab-pane
        :label="item.formName"
        v-for="(item,index) in childFormJsonArr"
        :key="index + 'hbw_'"
      >
        <div class="mainBox">
          <Queryform
            ref="queryForm"
            :searchJson="searchJson"
            @clickQueryBtn="clickQueryBtn"
          >
          </Queryform>
          <div class="tableBox">
            <div class="tableBtnBox">
              <el-button @click="clickAddBtn" size="mini" type="primary" icon="el-icon-plus">新增</el-button>
              <el-button @click="multiDelete" icon="el-icon-delete" size="mini" type="danger">删除</el-button>
              <el-button @click="exportMsg" icon="el-icon-plus" size="mini" >导出</el-button>
              <el-button @click="importMsg" icon="el-icon-plus" size="mini" >导入</el-button>
            </div>
            <el-table
              :header-cell-style="tableHeaderStyle"
              @selection-change="handleSelectionChange"
              height="400px"
              width="100%"
              :data="tableData"
            >
              <el-table-column
                type="selection"
                align="center"
                width="55">
              </el-table-column>
              <el-table-column
                v-for="(item,index) in columnData"
                :prop="item.prop"
                :label="item.label"
                :key="index"
                align="center">
                <template slot-scope="scope" >
                  <span v-if="item.options != null">{{optionsShow(item.prop,scope.row[item.prop],JSON.stringify(item.options))}}</span>
                  <span v-else-if="deptFieldsList.indexOf(item.prop) !=-1">{{deptShow(item.prop,scope.row[item.prop])}}</span>
                  <span v-else>{{scope.row[item.prop]}}</span>
                </template>
              </el-table-column>
              <el-table-column
                width="150px"
                align="center"
                label="操作">
                <template slot-scope="scope">
                  <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="handleEdit(scope.row)">编辑</Button>
                  <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="handleDelete(scope.row)">删除</Button>
                  <!-- <el-tooltip content="编辑" placement="top">
                    <el-button @click="handleEdit(scope.row)" size="mini" type="primary" icon="el-icon-edit"
                               circle></el-button>
                  </el-tooltip>
                  <el-tooltip content="删除" placement="top">
                    <el-button @click="handleDelete(scope.row)" size="mini" type="danger" icon="el-icon-delete"
                               circle></el-button>
                  </el-tooltip> -->
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="footer">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="pagerJson.page"
              :page-size="pagerJson.size"
              layout="total, sizes, prev, pager, next, jumper"
              :page-sizes="[3, 5, 10, 20]"
              :total="pagerJson.total">
            </el-pagination>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
    <!--    新增，编辑弹窗 -->
    <el-dialog
      :title="addAlertTittle"
      :visible.sync="addDialogVisible"
      :append-to-body="true"
      width="60%"
    >
      <VFormRender ref="preForm"
                   :form-json="formJson"
                   :preview-state="true"
                   @formChange="handleFormChange"
                   :form-data="testFormData"
                   :option-data="testOptionData"
                   v-if="addDialogVisible"
      >
      </VFormRender>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="getFormData">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import util from '@/libs/util'
import Queryform from '@/views/lowcode/template/components/Queryform'
import moment from 'moment'
import VFormRender from '@/views/lowcode/vformPro/components/form-render/index'
import { getFieldOptions, getDateTimeFields, getDeptFields } from '@/views/lowcode/utils/optionUtil'

export default {
  name: 'ChildTableInfo',
  components: {
    VFormRender,
    Queryform
  },
  props: {
    formId: {
      type: String,
      default: () => {
        return ''
      }
    },
    currentMoreTableItem: {
      type: String,
      default: () => {
        return ''
      }
    }
  },
  data () {
    return {
      addDialogVisible: false,
      addAlertTittle: '新增',
      childFormJsonArr: undefined,
      columnData: undefined,
      searchJson: undefined,
      tableData: undefined,
      testFormData: {},
      selectedTable: undefined,
      pagerJson: {
        size: 10,
        page: 1,
        total: 0
      },
      formJson: {},
      testOptionData: {},
      currentFormId: '',
      currentPk: '', // 主键
      currentKey: '', // 外键
      currentTabIndex: 0,
      optiongroup: [], // 下拉单选多选值数组
      dateTimeFields: [] // 时间、日期数组
    }
  },
  mounted () {
    // 拿到子表 表单信息
    this.getChildFormId()
  },
  computed: {
    deptFieldsList () {
      let arr = []
      this.deptFields.forEach(t => {
        arr.push(t.name)
      })
      console.log(arr)
      return arr
    }

  },
  methods: {
    deptShow (name, value) {
      let label = ''
      this.deptArr.forEach(dept => {
        if (value == dept.deptId) {
          label = dept.deptName
        }
      })
      return label
    },
    optionsShow (name, value, optionsString) {
      if (!value) {
        return ''
      }
      let options = JSON.parse(optionsString)
      let label = ''
      if (options['type'] === 'select' || options['type'] === 'radio') {
        options['options'].forEach(option => {
          if (option.value == value) {
            label = option.label
          }
        })
      } else if (options['type'] === 'checkbox') {
        let values = value.split('')
        let labels = []
        values.forEach(item => {
          options['options'].forEach(option => {
            if (option.value == item) {
              labels.push(option.label)
            }
          })
          label = labels.toString()
        })
      } else {
        label = value
      }
      return label
    },
    // tab页切换
    handleTabClick (val) {
      let index = val.index
      this.currentTabIndex = index
      this.getListJson(this.childFormJsonArr[index])
      // 初始化
      this.getListJson(this.childFormJsonArr[index]) // 当前列 搜索条件
      this.formJson = JSON.parse(this.childFormJsonArr[index].formJson) // 当前表单
      this.currentFormId = this.childFormJsonArr[index].formId // 当前formId
      this.currentPk = JSON.parse(this.childFormJsonArr[index].formOption).pk // 当前主键
      this.currentKey = this.childFormJsonArr[index].subFormFk // 关联字段
      this.getKeyType()
    },
    // 拿到页面json 和 子表 formId
    getChildFormId () {
      util.http.get('/api/lowcode/customize/childCscpCustomizeVforms/' + this.formId).then(res => {
        this.childFormJsonArr = res.data
        console.log('childFormJsonArr', this.childFormJsonArr)
        // 初始化
        this.getListJson(this.childFormJsonArr[0]) // 当前列 搜索条件
        this.formJson = JSON.parse(this.childFormJsonArr[0].formJson) // 当前表单
        this.currentFormId = this.childFormJsonArr[0].formId // 当前formId
        this.currentPk = JSON.parse(this.childFormJsonArr[0].formOption).pk // 当前主键
        this.currentKey = this.childFormJsonArr[0].subFormFk
        this.getKeyType()
      })
    },
    getKeyType () {
      // 多选单选集合
      this.optiongroup = getFieldOptions(this.formJson.formConfig, this.formJson.widgetList)
      this.dateTimeFields = getDateTimeFields(this.formJson.formConfig, this.formJson.widgetList)
      this.deptFields = getDeptFields(this.formJson.formConfig, this.formJson.widgetList)
      console.log(this.deptFields)
      if (this.deptFields) {
        this.$http.get('/api/cscpDepts').then(res => {
          this.deptArr = res.data.data
        }).catch(() => {
          this.$message.error('列表查询失败')
        })
      }
    },
    // 列表信息
    getListJson (childTableItem) {
      util.http.get('/api/lowcode/customize/cscpCustomizeVfields/getListByFormId/' + childTableItem.formId)
        .then((res) => {
          // 数据处理
          this.columnData = [] // 列
          this.searchJson = [] // 搜索
          this.listQueryType = {}
          let temArr = res.data.data
          temArr.forEach(item => {
            // 表格列处理
            if (item.fieldList === '1') {
              let column = {
                prop: item.fieldName,
                label: item.fieldComment,
                options: null
              }
              this.optiongroup.forEach(options => {
                if (item.fieldName === options['name']) {
                  column['options'] = options
                }
              })
              this.columnData.push(column)
            }
            // 搜索框处理
            if (item.fieldQuery === '1') { // 要展示搜索条件
              let json = {
                controlType: item.controlType,
                fieldComment: item.fieldComment, // label
                fieldName: item.fieldName, // 字段
                options: []
              }
              // 下拉选项数据处理
              this.optiongroup.forEach(options => {
                if (item.fieldName == options['name']) {
                  json.options = options['options']
                }
              })
              this.searchJson.push(json)
              // 拿到queryType
              this.listQueryType[item.fieldName] = item.queryType
            }
          })
          // 拿到表格数据
          this.clickQueryBtn()
        })
        .catch(() => {
          this.$Message.error('查询失败')
        })
    },
    // 删除逻辑
    handleDelete (row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteNetAPI(row)
      })
    },
    deleteNetAPI (row) {
      let params = {}
      params[this.currentPk] = row[this.currentPk]
      util.http.delete('/api/lowcode/customize/template/delByPk/' + this.currentFormId, { params }).then(res => {
        if (res.data.code === 200) {
          this.$message.success('删除成功！')
          this.clickQueryBtn()
        } else {
          this.$message.error('删除失败！！')
        }
      }).catch(err => {
        console.log(err)
      })
    },
    multiDelete () {
      if (this.selectedTable.length) {
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.multiDelApi()
        })
      } else {
        this.$message.warning('请至少勾选一条需要删除的数据！')
      }
    },
    multiDelApi () {
      let ids = []
      this.selectedTable.forEach(item => {
        ids.push(item[this.currentPk])
      })
      let params = {}
      params[this.currentPk + 's'] = ids.join(',')
      util.http.delete('/api/lowcode/customize/template/delByPks/' + this.currentFormId, { params }).then(res => {
        if (res.data.code === 200) {
          this.$message.success('删除成功！')
          this.clickQueryBtn()
        } else {
          this.$message.error('删除失败！！')
        }
      }).catch(err => {
        console.log(err)
      })
    },
    // 新增 编辑逻辑
    clickAddBtn () {
      this.testFormData = {}
      this.addAlertTittle = '新增'
      this.addDialogVisible = true
    },
    exportMsg(){
      let downUrl="";
      downUrl=util.baseUrl+"/api/lowcode/customize/template/export?formId="+this.currentFormId;
      window.open(downUrl)
    },
    handleEdit (row) {
      let params = {}
      params[this.currentPk] = row[this.currentPk]
      util.http.get('/api/lowcode/customize/template/queryByPk/' + this.currentFormId, { params }).then(res => {
        if (res.data.code === 200) {
          let netData = res.data.data
          // 格式化dateTime类型的数据
          this.dateTimeFields.forEach(item => {
            if (item.optionType === 'datetime' && netData[item.name]) {
              netData[item.name] = moment(netData[item.name]).format('YYYY-MM-DD HH:mm:ss')
            }
          })
          // 单选下拉
          this.optiongroup.forEach(option => {
            if (option['type'] === 'select' || option['type'] === 'radio') {
              if (netData[option['name']]) {
                netData[option['name']] = netData[option['name']].toString()
              }
            } else if (option['type'] === 'checkbox') {
              if (netData[option['name']]) {
                netData[option['name']] = netData[option['name']].split(',')
              }
            }
          })
          this.testFormData = netData
          this.addAlertTittle = '编辑'
          this.addDialogVisible = true
        } else {
          this.$message.error('获取详情数据失败！')
        }
      }).catch(err => {
        console.log(err)
      })
    },
    getFormData () {
      this.$refs['preForm'].getFormData().then(formData => {
        let queryParam = JSON.parse(JSON.stringify(formData)) // 深拷贝
        // 参数处理
        for (let item in queryParam) {
          // 为空处理
          if (queryParam[item] === '') {
            queryParam[item] = null
          }
          // 数组
          if (Array.isArray(queryParam[item])) {
            queryParam[item] = queryParam[item].join(',')
          }
        }
        queryParam[this.currentKey] = this.currentMoreTableItem // 外键
        if (this.addAlertTittle === '新增') {
          this.addNetApi(queryParam)
        } else if (this.addAlertTittle === '编辑') {
          this.updateNetApi(queryParam)
        }
      }).catch(error => {
        this.$message.error(error)
      })
    },
    addNetApi (formData) {
      util.http.post('/api/lowcode/customize/template/add/' + this.currentFormId, formData).then(res => {
        if (res.data.code === 200) {
          this.clickQueryBtn()
          this.addDialogVisible = false
          this.$message.success('新增成功！')
        } else {
          this.$message.error(res.data.msg)
        }
      }).catch(err => {
        console.log(err)
      })
    },
    updateNetApi (formData) {
      let params = JSON.parse(JSON.stringify(formData))
      params[this.currentPk] = this.testFormData[this.currentPk]
      util.http.put('/api/lowcode/customize/template/update/' + this.currentFormId, params).then(res => {
        if (res.data.code === 200) {
          this.clickQueryBtn()
          this.addDialogVisible = false
          this.$message.success('修改成功！')
        } else {
          this.$message.error(res.data.msg)
        }
      }).catch(err => {
        console.log(err)
      })
    },
    // 查询
    clickQueryBtn () {
      this.tableData = []
      // 参数处理
      let copyParam = JSON.parse(JSON.stringify(this.$refs.queryForm[this.currentTabIndex].getFormData()))
      let queryValues = []
      let queryFields = []
      let queryTypes = []
      for (let item in copyParam) {
        // 若值是数组
        if (Array.isArray(copyParam[item])) {
          copyParam[item] = copyParam[item].join('#')
        }
        if (copyParam[item]) {
          queryValues.push(copyParam[item])
          queryFields.push(item)
          queryTypes.push(this.listQueryType[item])
        }
      }
      let queryParams = {
        formid: this.formId,
        page: this.pagerJson.page,
        size: this.pagerJson.size
      }
      queryValues.length && (queryParams.queryValues = queryValues + '')
      queryFields.length && (queryParams.queryFields = queryFields + '')
      queryTypes.length && (queryParams.queryTypes = queryTypes + '')
      // 发送请求
      util.http.get('/api/lowcode/customize/template/query/' + this.currentFormId, { params: queryParams })
        .then((res) => {
          this.tableData = res.data.data.map(item => {
            this.dateTimeFields.forEach(innerItem => {
              if (innerItem.optionType === 'datetime' && item[innerItem.name]) {
                item[innerItem.name] = moment(item[innerItem.name]).format('YYYY-MM-DD HH:mm:ss')
              }
            })
            return item
          })
          this.pagerJson.total = parseInt(res.data.recordsTotal)
        })
        .catch(() => {
          this.$Message.error('查询失败')
        })
    },
    // 分页
    handleSizeChange (val) {
      this.pagerJson.page = 1
      this.pagerJson.size = val
      this.clickQueryBtn()
    },
    handleCurrentChange (val) {
      this.pagerJson.page = val
      this.clickQueryBtn()
    },
    // 公共方法
    tableHeaderStyle () {
      return 'color:#515a6e;background-color: rgb(248,248,249);'
    },
    handleSelectionChange (val) {
      this.selectedTable = val
    },
    handleFormChange (fieldName, newValue, oldValue, formModel) {

    }
  }
}
</script>

<style lang="less">
#childTableBox {
  width: 100%;
  height: 100%;
  overflow: hidden;

  .el-tabs {
    overflow: hidden;
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;

    .el-tabs__content {
      flex: 1;
      overflow: hidden;

      .el-tab-pane {
        width: 100%;
        height: 100%;
        overflow: hidden;

        .mainBox {
          width: 100%;
          height: 100%;

          .tableBox {
            flex: 1;
          }

          .footer {
            height: 50px;
          }
        }
      }
    }
  }
}
</style>
