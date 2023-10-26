<template>
  <div id="pageDeContainer" style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea">
          <Form ref="queryForm" :model="queryForm" :label-width="80">
            <Row :gutter="40">
              <Col span="8" style="margin-bottom: 12px">
                <FormItem label="页面名称" prop="pageName" style="width: 100%">
                  <Input type="text" :maxlength="100" v-model.trim="queryForm.pageName" placeholder="请输入页面名称"
                         style="width: 100%"/>
                </FormItem>
              </Col>
              <Col span="16" style="text-align: right">
                <FormItem :label-width="0">
                  <Button type="primary" @click="queryList(true)" style="margin-right: 8px">查询</Button>
                  <Button type="default" @click="handleReset">重置</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
          <div class="tableBtn">
            <Button @click="createPage" type="primary" style="margin-right: 10px" icon="md-add">新建页面</Button>
            <Button v-if="selectedArr.length>1" icon="ios-trash-outline" type="error" @click="handleDelete()"
                    style="margin-left: 5px">批量删除
            </Button>
          </div>
        </div>
      </Card>
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <Table border :columns="columns" :data="data" @on-selection-change="rowSelected($event)" style="">
          <template slot-scope="{ row }" slot="typeAction">
            <Button v-if="row.tableType=='0'" type="success" size="small" style="margin-right: 5px; color: #f1f5f8">单表
            </Button>
            <Button v-else-if="row.tableType=='3'" type="warning" size="small"
                    style="margin-right: 5px; color: #f1f5f8">视图
            </Button>
            <Button v-else type="primary" size="small" style="margin-right: 5px; color: #f1f5f8">多表
            </Button>
          </template>
          <template slot-scope="{ row, index }" slot="action">
            <Button type="text" size="small" style="margin-right: 5px; color: #0056b5"
                    @click="editPageName(row, index)">编辑页名
            </Button>
            <Button type="text" size="small" style="margin-right: 5px; color: #0056b5" @click="editPage(row, index)">
              编辑页面
            </Button>
            <Button type="text" size="small" style="margin-right: 5px; color: red" @click="remove(row, index)">
              删除
            </Button>
          </template>
          <template slot-scope="{ row }" slot="pageType">
            <span>{{ row.pageType === '1' ? '列表' : '表单' }}</span>
          </template>
        </Table>
        <Page :total="pageData.total" :page-size="pageData.size" :current="pageData.page" show-elevator show-sizer
              show-total @on-page-size-change="changePage($event, 'size')" @on-change="changePage($event, 'page')"
              style="margin-top: 20px"/>
      </Card>
    </div>
    <Modal class="editPageNameModule" v-model="editModalVisible" title="编辑页面名称" @on-ok="editPageNameConfirm"
           @on-cancel="editPageNameCancel">
      <Form ref="editFormRef" :model="pageNameDetail" :rules="editFormRules" :label-width="80">
        <FormItem prop="pageName" label="页面名称">
          <Input type="text" v-model="pageNameDetail.pageName" placeholder="请输入页面名称">
          </Input>
        </FormItem>
      </Form>
    </Modal>
    <!--    设计页面  -->
    <Modal fullscreen class="addpageModal" v-model="addPageVisible" title="设计页面" :footer-hide="true">
      <addPage ref="hbwceshi" :pageParam="pageParam" v-if="addPageVisible" @addPageCancel="addPageCancel"></addPage>
    </Modal>
    <!--    编辑页面  -->
    <Modal fullscreen v-model="editPageVisible" title="编辑页面" :footer-hide="true">
      <editPage v-if="editPageVisible" :rowData='rowData' :editPageCancel="editPageCancel"></editPage>
    </Modal>
    <config-page ref="configRef" formId1=''/>
  </div>
</template>

<script>
import ConfigPage from '@/views/lowcode/customize/components/configPageTest'
import addPage from './addPage.vue'
import editPage from './editPage.vue'

export default {
  name: 'pageManage',
  components: {ConfigPage, addPage, editPage},
  data() {
    let validateFormName = (rule, value, callback) => {
      this.isEditPageName = true
      if (this.oldPageName === value) {
        this.isEditPageName = false
        callback()
      } else {
        const [url] = [
          `/api/lowcode/customize/cscpCustomizeVpages?pageName.equals=${value}`,
        ]
        this.$http
          .get(url)
          .then((response) => {
            if (response.data.data.length > 0) {
              callback(new Error('页面名已重复01'))
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
      pageParam: '',
      oldPageName: '',
      isEditPageName: true,
      tableName: '',
      tableDesc: '',
      pageId: '',
      formId: null,
      isFullScreen: true,
      currentStep: -1,
      step: 0,
      configModal: false,
      isDisable: false,
      formJson: null,
      nextLoading: false,
      // 展示表单设计器的宽度
      designerConfig: {
        resetFormJson: false,
        toolbarMaxWidth: 490
      },
      fieldListApi: {
        URL: this.$util.baseUrl + '/api/cscpHxFormColums?tableName=' + this.tableId,
        labelKey: 'columnComment',
        nameKey: 'columnName',
        headers: {Authorization: localStorage.token}
        // options: []
      },
      formTemplate: {}, // 渲染模板
      models: {},  // 填充数据
      // exportModal: false,
      tableList: [],
      modelList: [],
      moduleList: [],
      createPageForm: {
        moduleId: '',
        pageName: '',
        pageType: '1',
        modelId: '',
        tableId: '',
        formType: '0',
      },
      current: 0,
      createPageVisible: false,
      createModelDialogVisible: false,
      editFormRules: {
        pageName: [
          {required: true, message: '页面名称不能为空！', trigger: 'blur'},
          {validator: validateFormName, trigger: 'blur'}
        ]
      },
      isShowTip: false,
      editModalVisible: false,
      addPageVisible: false,
      editPageVisible: false,
      selectedArr: [],
      queryForm: {
        pageName: ''
      },
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
          title: '所属模块',
          key: 'moduleName',
          align: 'center'
        },
        {
          title: '页面名称',
          key: 'pageName',
          align: 'center'
        },
        {
          title: '页面类型',
          key: 'pageType',
          align: 'center',
          slot: 'pageType'
        },
        {
          title: '数据模型名称',
          key: 'modelName',
          align: 'center'
        },
        {
          title: '数据表类型',
          key: 'tableType',
          align: 'center',
          slot: 'typeAction'
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
      pageNameDetail: {
        pageName: '',
      },
      modalTitle: '',
      initData: {},
      getData: {},
      previewModal: false,
      previewModalModels: {},
      previewModalFormTemplate: {},
      tableId: '', // 当选择数据表时,拿到对应的tableId即可
      mainTable: [],  //存放主子表类型的主表信息用来判断所选表是主表还是子表还是单表
      addOrEditFlag: 'add',
      rowData: {}
    }
  },
  watch: {
    rowData(newValue, oldValue) {
      this.rowData = newValue
      this.$refs.configRef.rowData = newValue
    },
    $route: {
      handler(newVal, oldVal) {
        this.queryForm.moduleName = newVal.moduleName
        this.queryList()
      },
      deep: true
    }
  },
  created() {
    this.pageParam = this.$route.query
    this.queryForm.moduleName = this.$route.query.moduleName
  },
  mounted() {
    this.queryList()
  },
  methods: {
    // 新增
    createPage() {
      //this.$router.push({ path: '/addPage', query: { editOrCreate: 'create' } })
      this.addPageVisible = true
      // setTimeout(() => {
      //   this.$refs.hbwceshi.buildPageModal = true
      // }, 0)
    },

    handleEdit(row, index) {
      //this.$router.push({ path: '/editPage' })
      this.editPageVisible = true
      window.localStorage.setItem('rowDataBackup', '')
      this.rowData = row
      const rowDataBackup = JSON.stringify(row)
      window.localStorage.setItem('rowDataBackup', rowDataBackup)
      window.localStorage.setItem('widget__list__backup', '')
      window.localStorage.setItem('form__config__backup', '')
      // 页面状态记录
      let pageJson = JSON.parse(row.pageJson ? row.pageJson : null)
      if (pageJson) {
        window.localStorage.setItem('widget__list__backup', JSON.stringify(pageJson.widgetList))
        window.localStorage.setItem('form__config__backup', JSON.stringify(pageJson.formConfig))
      }
      // 将行数据传递给子组件
      this.pageId = row.pageId
    },

    editPage(row, index) {
      this.handleEdit(row, index)
      this.$refs.configRef.editOrCreate = 'edit'
      window.localStorage.setItem('edittingPageId', row.pageId)
    },
    editPageNameConfirm() {
      const params = {
        moduleId: this.rowData.moduleId,
        pageId: this.rowData.pageId,
        pageJson: this.rowData.pageJson,
        pageName: this.pageNameDetail.pageName,
        pageOption: this.rowData.pageOption,
        pageTable: this.rowData.pageTable,
        pageType: this.rowData.pageType,
        tableType: this.rowData.tableType,
      }
      this.$refs.editFormRef.validate(valid => {
        if (valid) {
          if (!this.isEditPageName) {
            this.editModalVisible = false
            return
          }
          this.$http.put('/api/lowcode/customize/cscpCustomizeVpages', params).then((res) => {
            if (res.data) {
              this.$Message.success('修改成功')
              this.$refs.editFormRef.resetFields()
              this.editModalVisible = false
              this.queryList(true)
            }
          })
        } else {
          this.$refs['editFormRef'].resetFields()
        }
      })
    },
    getModelList() {
      const url = `api/cscpCustomizeModels?page=0&size=1000`
      this.$http.get(url)
        .then((res) => {
          const temp = res.data.data
          this.modelList = []
          for (let index = 0; index < temp.length; index++) {
            let element = temp[index]
            let obj = {}
            obj.label = element.modelName
            obj.value = element.modelId
            this.modelList.push(obj)
          }
        })
        .catch(() => {
          this.$Message.error('列表查询失败')
        })
    },
    prev() {
      if (this.current !== 0) {
        this.current -= 1
      }
    },

    editPageName(row, index) {
      this.oldPageName = row.pageName
      this.rowData = row
      this.pageNameDetail.pageName = row.pageName
      this.editModalVisible = true
    },

    changePage(e, type) {
      if (type === 'size') {
        this.pageData.size = e
      } else if (type === 'page') {
        this.pageData.page = e
      }
      this.queryList()
    },

    handleReset() {
      this.$refs.queryForm.resetFields()
      this.$route.params.moduleName = ''
      this.selectedArr = []
      this.queryList(true)
    },
    rowSelected(e) {
      this.selectedArr = e
    },
    remove(row, index) {
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除这条数据？</p>',
        onOk: () => {
          this.$http
            .delete(`/api/lowcode/customize/cscpCustomizeVpages/${row.pageId}`)
            .then((res) => {
              if (res.data.msg) {
                this.$Message.error(res.data.msg)
                return
              } else {
                this.$Message.success('删除成功')
                this.queryList()
              }
            })
            .catch(() => {
              this.$Message.error('删除失败')
            })
        }
      })
    },
    queryList(flag) {
      if (flag) {
        this.pageData.page = 1
      }
      const page = this.pageData.page - 1
      const size = this.pageData.size
      const pageName = this.queryForm.pageName || ''
      const moduleId = this.pageParam.moduleId || ''
      let url = `api/lowcode/customize/cscpCustomizeVpages?pageName.contains=${pageName}&moduleId.equals=${moduleId}&page=${page}&size=${size}&sort=update_time,desc`
      this.$http
        .get(
          url
        )
        .then((res) => {
          this.data = res.data.data
          this.pageData.total = res.data.recordsTotal
        })
        .catch(() => {
          this.$Message.error('列表查询失败')
        })
    },

    editPageNameCancel() {
      this.$refs['editFormRef'].resetFields()
      this.editModalVisible = false
    },
    addPageCancel() {
      this.addPageVisible = false
      this.queryList()
    },
    editPageCancel() {
      this.editPageVisible = false
    },
    handleDelete() {
      let arr = this.selectedArr.map((item) => item.pageId)
      if (arr.length < 1) {
        return this.$Message.error('删除失败,当前选择列表为空！')
      }
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除勾选的数据？</p>',
        onOk: () => {
          this.$http
            .delete('/api/lowcode/customize/cscpCustomizeVpages/delAll?pageIds=' + arr)
            .then((res) => {
              this.$Message.success('删除成功')
              this.queryList()
              this.selectedArr = []
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
  },
}
</script>
<style lang="less">
.addpageModal {
  .ivu-modal-header {
    display: none !important;
  }

  .ivu-modal-body {
    padding: 0;
    width: 100% !important;
    height: 100% !important;
    position: absolute;
    top: 0 !important;
    left: 0 !important;
  }

  .ivu-modal-close {
    display: none !important;
  }
}

.myTipContainer {
  .ivu-input {
    border-color: red;
  }
}

#pageDeContainer {
  .formCard-content {
    .ivu-table {
      .ivu-table-header {
        thead {
          tr {
            background-color: #D5E0F1;

            th {
              background-color: #D5E0F1;
              color: #000;
              font-size: 14px;
              font-weight: 600;
              font-family: PingFang SC-Semibold, Ping
            }
          }
        }
      }
    }

    .ivu-form .ivu-form-item-label {
      text-align: left;
    }

    .tableBtn {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      padding: 10px 0 0;
    }
  }
}

.editPageNameModule {
  .ivu-modal-content {
    background-image: url("../../../../assets/lowcode/dialogBg.png");
    background-size: cover;
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
  color: #0056b5;
}

.red {
  margin-right: 5px;
  color: red;
}

.moduleBox {
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
  align-items: center;
  width: 100%;
  height: 100%;

  .moduleItem {
    width: 32%;
    box-sizing: border-box;
    height: 104px;
    padding: 12px 8px 2px 16px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 4px 6px #0c1f500a, 0 0 0 1px #e6eaf0;
    cursor: pointer;
    position: relative;
    margin-bottom: 15px;
    margin-right: 15px;

    ul {
      display: flex;
      align-items: center;
      justify-content: flex-start;
      flex-wrap: wrap;

      li {
        list-style: none;
        width: 50%;
      }
    }

    .moreBtn {
      position: absolute;
      right: 15px;
      bottom: 10px;
    }
  }
}
</style>
