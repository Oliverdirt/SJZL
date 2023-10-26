<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div  class="tableBtn">
          <Button @click="openAdd(null,null,'add')" type="primary" style="margin-right: 10px;" icon="md-add">新增</Button>
          <Button icon="ios-trash-outline" type="error" @click="handleDeleteMultiple()" style="margin-left: 5px">删除</Button>
        </div>
        <Table border :columns="columns" :data="data" @on-selection-change="rowSelected($event)" style="">
          <template slot-scope="{ row, index }" slot="action">
            <Button type="text" size="small" style="margin-right: 5px; color: #0056b5" @click="openAdd(row, index,'update')">编辑
            </Button>
            <Button  type="text" size="small" style="margin-right: 5px; color: #0056b5" @click="handleEdit(row, index)">
              设计
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
    <config-page ref="configRef" :formType="formType" :formId1='formId'/>

    <Modal v-model="subModal" :title="subModalTitle">
      <Form ref="moduleRules" :model="subFormDetail" :rules="fieldRuleValidate" :label-width="100" style="margin-top: 20px">
        <FormItem label="子模块名称" prop="formName">
          <Input type="text" :maxlength="100" v-model="subFormDetail.formName"
                 placeholder="请输入子模块名称"></Input>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="primary" @click="save" style="width: 80px"
          >确定</Button
        >
        <Button type="default" @click="cancel" style="width: 80px"
          >取消</Button
        >
      </div>
    </Modal>
  </div>
</template>

<script>
import ConfigPage from '@/views/lowcode/customize/components/configPage'
export default {
  name: 'subVformManager',
  components: { ConfigPage },
  data () {
    let validateFormName = (rule, value, callback) => {
      if (value.length > 15) {
        callback(new Error('模块名长度小于等于15'))
      } else if (value.length <= 15) {
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
      fieldRuleValidate: {
        formName: [
          { required: true, message: '字段名称不能为空！！', trigger: 'blur' },
          { required: true, validator: validateFormName, trigger: 'blur' }

        ]
      },
      isShowTip: false,
      selectedArr: [],
      queryForm: {},
      formId: null,
      mainForm: null,
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
          title: '子模块名称',
          key: 'formName',
          align: 'center'
        },
        {
          title: '数据表',
          key: 'formTable',
          align: 'center'
        },
        {
          title: '关联字段',
          key: 'subFormFk',
          align: 'center'
        },
        {
          title: '排序',
          key: 'sortNum',
          align: 'center'
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
      subFormDetail: {},
      subModal: false,
      okFlag: 'add',
      modalValid: true,
      ruleValidate: {},
      subModalTitle: ''
    }
  },
  mounted () {
    // 获取mainFormId
    this.mainForm = this.$byStoreGet('mainForm')
    this.queryList()
  },
  methods: {
    openAdd (row, index, okFlag) {
      this.okFlag = okFlag
      this.subModal = true
      this.subFormDetail = {}
      if (this.okFlag == 'update') {
        this.subModalTitle = '编辑'
        this.$http
          .get('/api/lowcode/customize/cscpCustomizeVforms/' + row.formId)
          .then((res) => {
            this.subFormDetail = res.data
          })
          .catch(() => {
            this.$Message.error('行数据查询失败')
          })
      } else {
        this.subModalTitle = '新增'
      }
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
      this.subFormDetail = {}
      this.okFlag = 'add'
    },
    handleEdit (row, index) {
      window.localStorage.setItem('widget__list__backup', '')
      window.localStorage.setItem('form__config__backup', '')
      // 页面状态记录
      let pageJson = JSON.parse(row.formJson ? row.formJson : null)
      if (pageJson) {
        window.localStorage.setItem('widget__list__backup', JSON.stringify(pageJson.widgetList))
        window.localStorage.setItem('form__config__backup', JSON.stringify(pageJson.formConfig))
      }
      // 将行数据传递给子组件
      this.formId = row.formId
      this.$refs.configRef.currentStep = 0
      this.$refs.configRef.configModal = true
      this.$http
        .get('/api/lowcode/customize/cscpCustomizeVforms/' + row.formId)
        .then((res) => {
          this.subFormDetail = res.data
          this.formType = res.data.formType
          this.$refs.configRef.tableName = res.data.formTable
          this.$refs.configRef.formId = res.data.formId
          this.$refs.configRef.tableDesc = res.data.tableDesc
          this.$refs.configRef.formJson = res.data.formJson
          this.okFlag = 'update'
          if (res.data.formTable) {
            this.$refs.configRef.isDisable = true
            this.$refs.configRef.$refs.formDbRef.getColumns(res.data.formTable)
            this.$refs.configRef.$refs.formDbRef.tableName = res.data.formTable
            this.$refs.configRef.$refs.formDbRef.tableDesc = res.data.tableDesc
          } else {
            this.$refs.configRef.isDisable = false
            //this.mainForm.formId
            // this.$refs.configRef.mainFormId = this.mainForm.formId
          }
        })
        .catch(() => {
          this.$Message.error('行数据查询失败')
        })
    },
    handleReset (name) {
      this.$refs[name].resetFields()
      this.selectedArr = []
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
              if (res.data.msg) {
                this.$Message.error(res.data.msg)
                return
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
    save () {
      this.$refs.moduleRules.validate((valid) => {
        if (valid) {
          if (this.okFlag === 'update') {
            this.$http
              .put('/api/lowcode/customize/cscpCustomizeVforms', this.subFormDetail)
              .then((res) => {
                this.$Message.success('修改成功')

                this.queryList()
              })
              .catch(() => {
                this.$Message.error('修改失败')
              })
            this.subModal = false
          } else if (this.okFlag === 'add') {
            this.subFormDetail.mainFormId = this.mainForm.formId
            this.subFormDetail.formType = '2'
            if (this.subFormDetail.formName) {
              let copyDetail = JSON.parse(JSON.stringify(this.subFormDetail))
              this.$http
                .post('/api/lowcode/customize/cscpCustomizeVforms', copyDetail)
                .then((res) => {
                  this.$Message.success('新增成功')
                  this.queryList()
                })
                .catch(() => {
                  this.$Message.error('新增失败')
                })
              this.subModal = false
            }
          }
        } else {
          this.subModal = true
          this.$Message.error({
            background: true,
            content: '检验数据失败！'
          })
        }
      })
    },
    cancel () {
      this.subModal = false
      this.$refs['moduleRules'].resetFields()
    },
    // 表单名称是否重复 验证
    judgeFormNameRepeat () {
      let params = { formName: this.subFormDetail.formName }
      this.$http.get('/api/lowcode/customize/getCscpCustomizeVformByName', { params }).then(res => {
        res.data.length ? this.isShowTip = true : this.isShowTip = false
      })
    },
    ok (type) {
      this.$delete(this.subFormDetail, '')
      this.$delete(this.subFormDetail, '_rowKey')
      let copyDetail = JSON.parse(JSON.stringify(this.subFormDetail))
      if (this.subFormDetail.createdTime) {
        copyDetail.createdTime = this.$moment(
          new Date(this.subFormDetail.createdTime)
        ).format('yyyy-MM-DD HH:mm:ss')
      }

      this.$refs['subFormDetail'].validate((valid) => {
        if (valid) {
          if (type === 'update') {
            this.$http
              .put('/api/customizeVforms', copyDetail)
              .then((res) => {
                this.$Message.success('修改成功')
                this.queryList()
              })
              .catch(() => {
                this.$Message.error('修改失败')
              })
          } else if (type === 'add') {
            this.$http
              .post('/api/customizeVforms', copyDetail)
              .then((res) => {
                this.$Message.success('新增成功')
                this.queryList()
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
      let params = Object.assign(obj, {
        page: this.pageData.page - 1,
        size: this.pageData.size,
        'formType.equals': 2,
        'mainFormId.equals': this.mainForm.formId
      })
      this.$http
        .get('/api/lowcode/customize/cscpCustomizeVforms/', {
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
              this.$Message.success('删除成功')
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
        }
      })
    }
  }
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

</style>
