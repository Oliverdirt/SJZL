<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea">
          <Form ref="queryForm" :model="queryForm" :label-width="80">
            <Row :gutter="40">
              <Col span="6" style="margin-bottom: 12px">
                <FormItem label="表单名称" prop="formName" style="width: 100%">
                  <Input type="text" :maxlength="100" v-model.trim="queryForm.formName" placeholder="请输入表单名称"
                         style="width: 100%"/>
                </FormItem>
              </Col>
              <Col span="6" style="margin-bottom: 12px">
                <FormItem label="数据表" prop="formTable" style="width: 100%">
                  <Input type="text" :maxlength="100" v-model.trim="queryForm.formTable" placeholder="请输入数据表"
                         style="width: 100%"/>
                </FormItem>
              </Col>
              <Col span="6" style="margin-bottom: 12px">
                <FormItem label="表单类型" prop="formType" style="width: 100%">
                  <Select v-model.trim="queryForm.formType" placeholder="请选择">
                    <Option value="1">自定义表单</Option>
                    <Option value="2">路由表单</Option>
                  </Select>
                </FormItem>
              </Col>
              <Col span="24" style="text-align: right; margin-top: 12px">
                <FormItem :label-width="0">
                  <Button type="primary" @click="queryList(true)" style="margin-right: 8px">查询</Button>
                  <Button type="default" @click="handleReset('queryForm')">重置</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="tableBtn">
          <Button @click="okFlag = 'add';openAdd()" type="primary" style="margin-right: 10px;" icon="md-add">新建表单
          </Button>
          <Button icon="ios-trash-outline" type="error" @click="handleDeleteMultiple()" style="margin-left: 5px">删除
          </Button>
        </div>
        <Table border :columns="columns" :data="data" @on-selection-change="rowSelected($event)" style="">

          <template slot-scope="{ row }" slot="dataTable">
            <!-- <Button v-if="row.formTable" type="text" size="small" @click.native="chargeForm(row)">{{row.formTable}}
            </Button>
            <Button v-else type="text" size="small" @click.native="chargeForm(row)">暂无数据表</Button> -->
            <span type="text" v-if="row.formType === '2'">--</span>
            <el-button v-else-if="row.formTable" type="text" @click.native="chargeForm(row)" style="font-size: 14px">
              <span>{{ row.formTable }}</span>
            </el-button>
            <el-button type="text" v-else @click.native="chargeForm(row)" style="font-size: 14px">暂无数据表</el-button>
          </template>
          <template slot-scope="{ row }" slot="typeAction">
            <Button v-if="row.formType=='1'" type="success" size="small" style="margin-right: 5px; color: #f1f5f8">自定义表单
            </Button>
            <Button v-else type="primary" size="small" style="margin-right: 5px; color: #f1f5f8">路由表单</Button>
          </template>
          <template slot-scope="{ row }" slot="formUrlAction">
            <span v-if="row.formUrl">{{ row.formUrl }}
            </span>
            <span type="text" v-else>--</span>
          </template>
          <template slot-scope="{ row, index }" slot="action">
            <Button type="text" size="small" style="margin-right: 5px; color: #0056b5"
                    @click="okFlag = 'update';openAdd(row, index)">编辑
            </Button>
            <!-- </Tooltip> -->
            <Button v-if="row.formType === '1'" type="text" size="small" style="margin-right: 5px; color: #0056b5"
                    @click="beforeHandleEdit(row, index)">
              表单设计
            </Button>
            <Button type="text" size="small" style="margin-right: 5px; color: #0056b5" @click="remove(row, index)">
              删除
            </Button>
          </template>
        </Table>
        <Page :total="pageData.total" :page-size="pageData.size" :current="pageData.page" show-elevator show-sizer
              show-total @on-page-size-change="changePage($event, 'size')" @on-change="changePage($event, 'page')"
              style="margin-top: 20px"/>
      </Card>
    </div>
    <Modal v-model="modal1" :title="modalTitle">
      <Form ref="moduleRules" :model="detail" :rules="fieldRuleValidate" :label-width="80" style="margin-top: 20px">
        <FormItem label="表单名称" prop="formName">
          <Input type="text" :maxlength="100" v-model="detail.formName"
                 placeholder="请输入表单名称"></Input>
        </FormItem>
        <FormItem label="表单类型" prop="formType">
          <Select v-model="detail.formType" placeholder="请选择" :disabled="typeDisabled">
            <Option v-for="item in formTypeOption" :value="item.value.toLocaleString()"
                    :key="item.value.toLocaleString()">{{ item.label }}
            </Option>
          </Select>
        </FormItem>
        <FormItem label="表单路径" prop="formUrl" v-show="detail.formType==='2'">
          <Tooltip max-width="300" placement="top" content="路由表单路径必须是项目中存在于src/views目录下的以/开始的.vue文件路径地址">
            <div style="width: 408px">
              <Input type="text" v-model="detail.formUrl"
                     placeholder="请输入路由表单路径"></Input>
            </div>
          </Tooltip>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="primary" @click="save" style="width: 80px"
        >确定
        </Button
        >
        <Button type="default" @click="cancel" style="width: 80px"
        >取消
        </Button
        >
      </div>
    </Modal>
    <config-page ref="configRef" :formType="formType" formId1=''/>
  </div>
</template>

<script>
import ConfigPage from '@/views/lowcode/customize/components/configPageNew'

export default {
  name: 'customizeVformManager',
  components: { ConfigPage },
  data () {
    let validateFormName = (rule, value, callback) => {
      if (value.length > 15) {
        callback(new Error('表单名长度小于等于15'))
      } else if (value.length <= 15) {
        if (value === this.oldFormName) {
          callback()
          return
        }
        const [url, httpConfig] = [
          '/api/lowcode/customize/getCscpCustomizeVformByName',
          {
            params: { formName: value }
          }
        ]
        this.$http
          .get(url, httpConfig)
          .then((response) => {
            console.log('response.data.length > 0', response.data.length > 0)
            if (response.data.length > 0) {
              callback(new Error('表单名已重复'))
            } else {
              callback()
            }
          })
          .catch(() => {
            callback(new Error('异步校验出错！'))
          })
      }
    }
    let validateFormUrl = (rule, value, callback) => {
      if (this.detail.formType === '2') {
        if (!value) {
          callback(new Error('路由表单地址不能为空'))
        } else if (!(value.length > 5) || !(value.startsWith('/')) || !(value.endsWith('.vue'))) {
          callback(new Error('路由表单地址不合法'))
        } else {
          callback()
        }
      } else {
        callback()
      }
    }
    return {
      oldFormName: '',
      tableArr: [],
      tableColumns: [
        {
          title: '数据库字段名',
          key: 'columnName'
        },
        {
          title: '字段名称',
          key: 'columnComment'
        },
        {
          title: '字段类型',
          width: 150,
          key: 'columnType'
        },
        {
          title: '字段长度',
          key: 'columnLength'
        },
        {
          title: '小数点',
          key: 'pointLength'
        },
        {
          title: '默认值',
          key: 'defaultValue'
        }
      ],
      dataArr: [],
      isDisable: false,
      tableName: '',
      tableDesc: '',
      dbFormVisible: false,
      dbForm: {},
      formOpen: false,
      // 表单校验
      rules: {
        key: [{ required: true, message: '流程标识不能为空', trigger: 'blur' }],
        name: [{ required: true, message: '流程名称不能为空', trigger: 'blur' }],
        formType: [{ required: true, message: '请选择表单类型', trigger: 'blur' }],
        formId: [{ required: true, message: '请选择表单', trigger: 'blur' }]
      },
      fieldRuleValidate: {
        formName: [
          { required: true, message: '表单名称不能为空！', trigger: 'blur' },
          { required: true, validator: validateFormName, trigger: 'blur' }
        ],
        formType: [
          { required: true, message: '表单类型不能为空！', trigger: 'blur' },
        ],
        formUrl: [
          { required: true, validator: validateFormUrl, trigger: 'blur' },
        ],
      },
      formTypeOption: [
        { value: '1', label: '自定义表单' },
        { value: '2', label: '路由表单' }
      ],
      isShowTip: false,
      modal1: false,
      selectedArr: [],
      queryForm: {},
      formId: '',
      formType: '0',
      pageData: {
        total: 0,
        size: 10,
        page: 1
      },
      columns: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          type: 'index',
          align: 'center',
          title: '序号',
          width: 70
        },
        {
          title: '表单名称',
          key: 'formName',
          align: 'center'
        },
        {
          title: '数据表',
          key: 'formTable',
          align: 'center',
          slot: 'dataTable'
        },
        {
          title: '表单类型',
          key: 'formType',
          align: 'center',
          slot: 'typeAction'
        },
        {
          title: '路由地址',
          key: 'formUrl',
          align: 'center',
          slot: 'formUrlAction'
        },
        {
          title: '创建时间',
          key: 'createdTime',
          align: 'center'
        },
        {
          title: '操作',
          slot: 'action',
          width: 300,
          align: 'center'
        }
      ],
      data: [],
      detail: {},
      okFlag: 'add',
      modalValid: true,
      ruleValidate: {},
      modalTitle: '',
      typeDisabled: false,
      tagetRow: '',
      moduleName: ''
    }
  },
  watch: {
    currentTableName (newValue, oldValue) {
      // 每当inputValue的值改变则更新 update:inputValue, 并且把值传过去
      this.fieldListApi.URL = this.$util.baseUrl + '/api/cscpHxFormColums?tableName=' + newValue
    }
  },
  mounted () {
    this.queryList()
  },
  methods: {
    getMainTable () {
      if (this.formId1 && this.formId1 != '') {
        // 查询主表信息
        // /api/lowcode/customize/getMainCscpCustomizeVform/{formId}
        this.$http.get('/api/lowcode/customize/getMainCscpCustomizeVform/' + this.formId1).then((res) => {
          this.mainTable = res.data
          this.selectDB()
        }).catch(() => {
          this.$Notice.error({
            title: '操作失败！'
          })
        })
      } else {
        this.selectDB()
      }
    },
    selectDB () {
      let This = this
      This.$http
        .get('/api/schema/cscpHxTablesInfo')
        .then(function (response) {
          This.tableArr = response.data
          if (This.mainTable) {
            This.tableArr = response.data.filter(t => {
              return t.table_name !== This.mainTable.formTable
            })
          }

        })
        .catch(function (error) {
          console.log(error)
        })
    },
    getColumns (tableName) {
      const [url, param] = [
        '/api/cscpHxFormColums',
        {
          params: {
            tableName: tableName
          }
        }
      ]
      this.$http.get(url, param).then((res) => {
        let formArr = res.data
        if (formArr.length > 0) {
          // 找出主键
          let keyArr = formArr.filter(item => {
            return item.isKey
          })
          keyArr.length ? window.localStorage.setItem('keyName', keyArr[0].columnName) : ''
          // 数据处理
          this.dataArr = []
          for (let i = 0; i < formArr.length; i++) {
            let map = {
              columnName: formArr[i].columnName,
              columnComment: formArr[i].columnComment,
              columnLength: formArr[i].length,
              pointLength: formArr[i].pointLength,
              defaultValue: formArr[i].columnDefault,
              columnType: formArr[i].dataType
            }
            this.dataArr.push(map)
          }
        }
      }).catch(() => {
        this.$Notice.error({
          title: '操作失败！'
        })
      })
    },
    changeTable (tableName) {
      if (tableName) {
        for (var i = 0; i <= this.tableArr.length; i++) {
          if (this.tableArr[i].table_name == tableName) {
            this.tableDesc = this.tableArr[i].table_comment ? this.tableArr[i].table_comment : this.tableArr[i].table_name
            break
          }
        }
        this.getColumns(tableName)
      }
    },
    handleTableChild (row) {
      this.$byStoreSet('mainForm', row)
      this.$router.push({
        name: 'sub-lowcode-programming'
      })
    },
    // 表单名称是否重复 验证
    judgeFormNameRepeat () {
      let params = { formName: this.detail.formName }
      this.$http.get('/api/lowcode/customize/getCscpCustomizeVformByName', { params }).then(res => {
        res.data.length ? this.isShowTip = true : this.isShowTip = false
      })
    },
    openAdd (row, index) {
      this.isShowTip = false
      this.modal1 = true
      this.detail = { formType: 0 }
      if (this.okFlag == 'update') {
        this.modalTitle = '编辑表单'
        this.oldFormName = row.formName
        this.$http
          .get('/api/lowcode/customize/cscpCustomizeVforms/' + row.formId)
          .then((res) => {
            this.detail = res.data
            this.typeDisabled = true
          })
          .catch(() => {
            this.$Message.error('行数据查询失败')
          })
      } else {
        this.modalTitle = '新增表单'
        this.typeDisabled = false
      }
    },
    chargeForm (row) {
      this.$refs.configRef.currentStep = 0
      this.$refs.configRef.isFullScreen = false
      this.$refs.configRef.title = '配置数据表'
      window.localStorage.setItem('widget__list__backup', '')
      window.localStorage.setItem('form__config__backup', '')
      let pageJson = JSON.parse(row.formJson ? row.formJson : null)
      if (pageJson) {
        window.localStorage.setItem('widget__list__backup', JSON.stringify(pageJson.widgetList))
        window.localStorage.setItem('form__config__backup', JSON.stringify(pageJson.formConfig))
      }
      this.$refs['moduleRules'].resetFields()
      this.formId = row.formId
      this.$refs.configRef.configModal = true
      this.$http
        .get('/api/lowcode/customize/cscpCustomizeVforms/' + row.formId)
        .then((res) => {
          this.detail = res.data
          this.formType = res.data.formType
          this.$refs.configRef.tableName = res.data.formTable
          this.$refs.configRef.tableDesc = res.data.tableDesc
          this.$refs.configRef.formId = res.data.formId
          this.$refs.configRef.formJson = res.data.formJson
          this.okFlag = 'update'
          if (res.data.formTable) {
            this.$refs.configRef.isDisable = false
            this.$refs.configRef.$refs.formDbRef.getColumns(res.data.formTable)
            this.$refs.configRef.$refs.formDbRef.tableName = res.data.formTable
            this.$refs.configRef.$refs.formDbRef.tableDesc = res.data.tableDesc
          } else {
            this.$refs.configRef.isDisable = false
          }
        })
        .catch(() => {
          this.$Message.error('行数据查询失败')
        })
    },
    save () {
      this.$refs.moduleRules.validate((valid) => {
        if (valid) {
          if (this.isShowTip) return false
          if (this.okFlag === 'update') {
            this.$http
              .put('/api/lowcode/customize/cscpCustomizeVforms', this.detail)
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
            if (this.detail.formName) {
              let copyDetail = JSON.parse(JSON.stringify(this.detail))
              this.$http
                .post('/api/lowcode/customize/cscpCustomizeVforms', copyDetail)
                .then((res) => {
                  this.$Message.success('新增成功')

                  this.queryList()
                })
                .catch(() => {
                  this.$Message.error('新增失败')
                })
              this.modal1 = false
            }
          }
        } else {
          this.modal1 = true
          this.$Message.error({
            background: true,
            content: '检验数据失败！'
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
    handleAdd () {
      this.detail = {}
      this.okFlag = 'add'
    },
    beforeHandleEdit (row) {
      if (!row.formTable) {
        this.$Message.error('请先进行配置数据表操作')
        return
      }
      window.localStorage.setItem('workFlowFormTable', row.formTable)
      if (row.formJson) {
        this.$router.push({ path: '/formDesign', query: { formRow: row, routeFlag: 1 } })
      } else {
        this.$router.push({ path: '/formDesign', query: { formRow: row, routeFlag: 0 } })
      }
    },
    handleReset (name) {
      this.$refs[name].resetFields()
      this.selectedArr = []
      this.pageData.page = 1
      this.queryList()
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
            .delete('/api/lowcode/customize/cscpCustomizeVforms/' + row.formId)
            .then((res) => {
              if (res.data && res.data.code === 200 && res.data.msg === '删除成功') {
                this.$Message.success('删除成功')
              } else if (res.data.msg) {
                this.$Message.error(res.data.msg)
              } else {
                this.$Message.success('删除成功')
              }
              this.queryList()
            })
            .catch(() => {
              this.$Message.error('删除失败')
            })
        },
        onCancel: () => {
        }
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
      let obj = {}
      obj['formName'] = this.queryForm['formName'] ? this.queryForm['formName'] : null
      obj['formTable'] = this.queryForm['formTable'] ? this.queryForm['formTable'] : null
      if (this.queryForm['formType']) {
        obj['formType'] = this.queryForm['formType']
      }

      let params = Object.assign(obj, {
        page: this.pageData.page - 1,
        size: this.pageData.size
      })
      this.$http
        .get('/api/lowcode/customize/cscpCustomizeVforms/getList', {
          params: params
        })
        .then((res) => {
          this.data = res.data.data
          this.pageData.total = res.data.recordsTotal
        })
        .catch(() => {
          this.$Message.error('列表查询失败')
        })
    },
    cancel () {
      this.modal1 = false
      this.$refs['moduleRules'].resetFields()
    },
    refreshPage () {
      window.location.reload()
    },
    handleDeleteMultiple: function () {
      let arr = this.selectedArr.map((item) => item.formId)
      if (arr.length < 1) {
        return this.$Message.error('删除失败,当前选择列表为空！')
      }
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除勾选的数据？</p>',
        onOk: () => {
          this.$http
            .delete('/api/lowcode/customize/cscpCustomizeVforms/delAll?formIds=' + arr)
            .then((res) => {
              if (res.data.code === 400) {
                this.$Message.error(res.data.msg)
              }else {
                this.$Message.success('删除成功')
                this.queryList()
                this.selectedArr = []
              }
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
        }
      })
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

.modalHeaderClass {
  position: relative;
  color: #2d8cf0;
  font-size: 16px;
}

.normal {
  margin-right: 5px;
  color: #0056b5
}

.red {
  margin-right: 5px;
  color: red
}

</style>
