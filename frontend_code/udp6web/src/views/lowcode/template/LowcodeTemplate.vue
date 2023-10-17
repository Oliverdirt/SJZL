<template>
  <div id="muban">
    <div class="searchBox">
      <el-form size="small" :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item
          v-for="(item,index) in searchJson"
          :key="index + 'hbw_'"
          :label="item.fieldComment+'：'"
        >
          <!--        文本框  -->
          <el-input v-if="item.controlType === '0'" placeholder="请输入" v-model="formInline[item.fieldName]"></el-input>
          <!--        下拉框 -->
          <el-select filterable v-if="item.controlType === '1'" v-model="formInline[item.fieldName]" placeholder="请选择">
            <el-option
              v-for="(option,index) in item.options"
              :label="option.label"
              :value="option.value"
              :key="index+'opt'"
            >
            </el-option>
          </el-select>
          <el-select multiple filterable v-if="item.controlType === '2'" v-model="formInline[item.fieldName]"
                     placeholder="请选择">
            <el-option
              v-for="(option,index) in item.options"
              :label="option.label"
              :value="option.value"
              :key="index+'opt'"
            >
            </el-option>
          </el-select>
          <!--        时间范围（HH:mm:ss） -->
          <el-time-picker
            is-range
            value-format="HH-mm-ss"
            v-if="item.controlType === '3'"
            arrow-control
            v-model="formInline[item.fieldName]"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            placeholder="选择时间范围">
          </el-time-picker>
          <!--        日期范围 （yyyy-MM-dd HH:mm:ss）  -->
          <el-date-picker
            value-format="yyyy-MM-dd HH-mm-ss"
            v-if="item.controlType === '4'"
            v-model="formInline[item.fieldName]"
            type="datetimerange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          >
          </el-date-picker>
          <!--        日期范围 （yyyy-MM-dd）  -->
          <el-date-picker
            v-if="item.controlType === '5'"
            v-model="formInline[item.fieldName]"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item :key="item" v-for="item in 3-(searchJson.length + 1)%3"></el-form-item>
        <el-form-item>
          <el-button @click="clickQueryBtn" type="primary" icon="el-icon-search">查询</el-button>
          <el-button @click="onReset" icon="el-icon-s-tools">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="tableBox">
      <div class="tableBtnBox">
        <el-button @click="clickAddBtn" size="mini" type="primary" icon="el-icon-plus">新增</el-button>
        <el-button @click="multiDelete" icon="el-icon-delete" size="mini" type="danger">删除</el-button>
        <el-button @click="exportMsg" icon="el-icon-upload2" size="mini" type="warning">导出</el-button>
        <el-button @click="labelExcelImport" icon="el-icon-download" size="mini" type="primary">导入</el-button>
        <form enctype="multipart/form-data" id="daoru">
          <input
            type="file"
            name="fileup"
            id="uploadEventFile"
            @change="fileChange"
            style="display: none"
          />
        </form>
      </div>
      <el-table
        :header-cell-style="tableHeaderStyle"
        @selection-change="handleSelectionChange"
        height="100%"
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
          <template slot-scope="scope">
            <span
              v-if="item.options != null">{{
                optionsShow(item.prop, scope.row[item.prop], JSON.stringify(item.options))
              }}</span>
            <span
              v-else-if="deptFieldsList.indexOf(item.prop) !=-1">{{ deptShow(item.prop, scope.row[item.prop]) }}</span>
            <span v-else>{{ scope.row[item.prop] }}</span>
          </template>
        </el-table-column>
        <el-table-column
          width="200px"
          align="center"
          label="操作">
          <template slot-scope="scope">

            <Button v-if='isMulti' type="text" size="small" style="margin-right: 5px;color: #0056B5"
                    @click="clickMoreBtn(scope.row)">查看更多
            </Button>
            <Button type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="handleEdit(scope.row)">
              编辑
            </Button>
            <Button type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="handleDelete(scope.row)">
              删除
            </Button>
            <!--
                        <el-tooltip content="编辑" placement="top">
                          <el-button @click="handleEdit(scope.row)" size="mini" type="primary" icon="el-icon-edit"
                                     circle></el-button>
                        </el-tooltip>
                        <el-tooltip content="删除" placement="top">
                          <el-button @click="handleDelete(scope.row)" size="mini" type="danger" icon="el-icon-delete"
                                     circle></el-button>
                        </el-tooltip>
                        <el-tooltip content="" placement="top">
                          <el-button @click="clickMoreBtn(scope.row)" size="mini" type="success" icon="el-icon-s-order"
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
    <!--    新增，编辑弹窗 -->
    <el-dialog
      :title="addAlertTittle"
      :visible.sync="addDialogVisible"
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
    <!--    子表信息弹窗  -->
    <el-dialog
      title="更多信息"
      :visible.sync="moreInfoDialog"
      :fullscreen="true"
    >
      <ChildTableInfo
        :formId="formId"
        :currentMoreTableItem="currentMoreTableItem"
      >
      </ChildTableInfo>
      <span slot="footer" class="dialog-footer">
        <el-button @click="moreInfoDialog = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import util from '@/libs/util'
import VFormRender from '@/views/lowcode/vformPro/components/form-render/index'
import moment from 'moment'
import ChildTableInfo from '@/views/lowcode/template/ChildTableInfo'
import { getDateTimeFields, getDeptFields, getFieldOptions } from '@/views/lowcode/utils/optionUtil'

export default {
  name: 'Template',
  components: {
    VFormRender,
    ChildTableInfo
  },
  data () {
    return {
      isMulti: false,
      moreInfoDialog: false,
      formInline: {},
      addDialogVisible: false,
      addAlertTittle: '新增',
      tableData: [],
      pagerJson: {
        size: 10,
        page: 1,
        total: 0
      },
      formJson: {},
      columnData: [], // 列数组
      searchJson: [], // 搜索
      formId: '',
      testFormData: {},
      testOptionData: {},
      pk: '', // 主键字段
      selectedTable: [],
      listQueryType: {},
      currentMoreTableItem: undefined,
      optiongroup: [], // 下拉单选多选值数组
      dateTimeFields: [], // 时间、日期数组
      deptFields: [], // 部门
      deptArr: []// 获取所有部门集合
    }
  },
  mounted () {
    this.initPage()
  },
  computed: {
    deptFieldsList () {
      let arr = []
      this.deptFields.forEach(t => {
        arr.push(t.name)
      })
      return arr
    }

  },
  watch: {
    '$route': 'initPage'
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
    initPage () {
      if (window.location.href.includes('customize')) {
        if (window.location.href.split('customize/')[1]) {
          // 拿到formId
          this.formId = window.location.href.split('customize/')[1]
          // 拿到表单信息
          this.getFormJson()
          // 清空搜索条件内容
          this.onReset()
        } else {
          this.$message.error('没有拿到formId!')
        }
      }
    },
    handleFormChange (fieldName, newValue, oldValue, formModel) {

    },
    handleSelectionChange (val) {
      this.selectedTable = val
    },
    clickMoreBtn (row) {
      this.moreInfoDialog = true
      this.currentMoreTableItem = row[this.pk]
    },
    // 查询
    clickQueryBtn () {
      this.tableData = []
      // 参数处理
      let copyParam = JSON.parse(JSON.stringify(this.formInline))
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
      util.http.get('/api/lowcode/customize/template/query/' + this.formId, { params: queryParams })
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
    onReset () { // 重置
      for (let item in this.formInline) {
        if (Array.isArray(this.formInline[item])) { // 是数组
          this.formInline[item] = []
        } else {
          this.formInline[item] = ''
        }
      }
      this.clickQueryBtn()
    },
    // 新增 编辑逻辑
    clickAddBtn () {
      this.testFormData = {}
      this.addAlertTittle = '新增'
      this.addDialogVisible = true
    },
    exportMsg () {
      let downUrl = ''
      downUrl = util.baseUrl + '/api/lowcode/customize/template/export?formId=' + this.formId
      window.open(downUrl)
    },
    handleEdit (row) {
      let params = {}
      params[this.pk] = row[this.pk]
      util.http.get('/api/lowcode/customize/template/queryByPk/' + this.formId, { params }).then(res => {
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
      util.http.post('/api/lowcode/customize/template/add/' + this.formId, formData).then(res => {
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
      params[this.pk] = this.testFormData[this.pk]
      util.http.put('/api/lowcode/customize/template/update/' + this.formId, params).then(res => {
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
      params[this.pk] = row[this.pk]
      util.http.delete('/api/lowcode/customize/template/delByPk/' + this.formId, { params }).then(res => {
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
        ids.push(item[this.pk])
      })
      let params = {}
      params[this.pk + 's'] = ids.join(',')
      util.http.delete('/api/lowcode/customize/template/delByPks/' + this.formId, { params }).then(res => {
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
    // 表单信息
    getFormJson () {
      util.http.get('/api/lowcode/customize/cscpCustomizeVforms/' + this.formId)
        .then((res) => {
          this.formJson = JSON.parse(res.data.formJson)
          this.isMulti = res.data.formType == 1
          this.pk = JSON.parse(res.data.formOption).pk // 拿到主键
          this.optiongroup = getFieldOptions(this.formJson.formConfig, this.formJson.widgetList)
          this.dateTimeFields = getDateTimeFields(this.formJson.formConfig, this.formJson.widgetList)
          this.deptFields = getDeptFields(this.formJson.formConfig, this.formJson.widgetList)
          if (this.deptFields) {
            this.$http.get('/api/cscpDepts').then(res => {
              this.deptArr = res.data.data
            }).catch(() => {
              this.$message.error('列表查询失败')
            })
          }
          // 拿到查询信息
          this.getListJson()
        })
        .catch(() => {
          this.$Message.error('查询失败')
        })
    },
    // 列表信息
    getListJson () {
      util.http.get('/api/lowcode/customize/cscpCustomizeVfields/getListByFormId/' + this.formId)
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
                if (item.fieldName === options['name']) {
                  json.options = options['options']
                }
              })
              this.searchJson.push(json)
              // 拿到queryType
              this.listQueryType[item.fieldName] = item.queryType
            }
          })
          // 拿到表格数据
          // this.clickQueryBtn()
        })
        .catch(() => {
          this.$Message.error('查询失败')
        })
    },
    getOptions () {

    },
    tableHeaderStyle () {
      return 'color:#515a6e;background-color: rgb(248,248,249);'
    },
    // 导入
    labelExcelImport () {
      event.preventDefault()
      document.getElementById('uploadEventFile').click()
    },
    fileChange (el) {
      // el.preventDefault(); //取消默认行为
      let uploadEventFile = document.getElementById('uploadEventFile').files
      this.file = el.target.files[0]
      if (uploadEventFile === '') {
        this.$message({
          type: 'warning',
          message: '请选择文件'
        })
      }
      let formData = new FormData(document.getElementById('daoru'))
      // 向 formData 对象中添加文件
      formData.append('file', this.file)
      // const rLoading = this.openLoading();
      this.$axios({
        method: 'post',
        url:
          util.baseUrl +
          '/api/lowcode/customize/template/import?formId=' + this.formId, // 请求接口
        // 请求接口的参数
        data: formData,
        // responseType: "blob",
        headers: {
          'Content-Type': 'multipart/form-data', // 设置请求头请求格式为JSON
          Authorization: window.localStorage.token
        }
      }).then((data) => {
        if (data.data.code == 200) {
          Loading.close()
          this.$notify({
            type: 'error',
            dangerouslyUseHTMLString: true,
            message: data.data.data,
            position: 'top-left',
            duration: 4000
          })
        } else {
        }
        // location.href="http://www.baidu.com"
        this.xxx()//表格接口
        document.getElementById('uploadEventFile').value = ''
      })
    },
    upload_users_excel () {
      util.http.post('/api/lowcode/customize/template/import?formId=' + this.formId)
        .then(res => {
          if (res.data.code === 200) {
            this.clickQueryBtn()
            this.addDialogVisible = false
            this.$message.success('导入成功！')
          } else {
            this.$message.error(res.data.msg)
          }
        }).catch(err => {
      })
    }
  }
}
</script>

<style lang="less">
#muban {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .searchBox {
    background-color: #fff;
    margin-bottom: 15px;

    .el-form {
      padding: 25px 0 15px;
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      flex-wrap: wrap;

      .el-form-item {
        margin: 0;
        width: 33%;
        box-sizing: border-box;
        padding: 0 10px 10px 0;
        display: flex;
        align-items: center;

        .el-form-item__label {
          flex: 1;
          text-overflow: ellipsis;
          overflow: hidden;
          white-space: nowrap;
          flex-shrink: 0;
        }

        .el-form-item__content {
          flex: 3;

          .el-select {
            width: 100%;
          }

          .el-input {
            width: 100%;
          }

          .el-date-editor {
            width: 100%;
          }
        }
      }

      .el-form-item:last-child {
        .el-form-item__content {
          display: flex;
          justify-content: flex-end;
        }
      }
    }
  }

  .tableBox {
    background-color: #fff;
    padding: 38px 10px 10px 10px;
    flex: 1;
    overflow: hidden;

    .tableBtnBox {
      display: flex;
      align-items: center;
      justify-content: flex-end;
      padding-bottom: 10px;
      padding-right: 15px;
      margin-top: -28px;
    }
  }

  .footer {
    background-color: #fff;
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: flex-end;
  }

  .spanStyle {
    cursor: pointer;
  }

  .is-fullscreen {
    display: flex;
    flex-direction: column;

    .el-dialog__header {
      height: 54px;
    }

    .el-dialog__body {
      flex: 1;
    }
  }
}
</style>
