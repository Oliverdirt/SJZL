<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea">
          <Form ref="queryFrom" :model="queryParams">
            <Row :gutter="48">
              <Col span="8">
                <FormItem label="表名称" prop="tableName" style="width: 100%" :label-width="60">
                  <Input clearable v-model="queryParams.tableName" placeholder="请输入表名称"></Input>
                </FormItem>
              </Col>
              <Col span="8">
                <FormItem label="表单类型" prop="formType" style="width: 100%" :label-width="80">
                  <Select style="" v-model="queryParams.formType" clearable placeholder="请选择">
                    <Option value="1">单表</Option>
                    <Option value="2">树表</Option>
                    <Option value="3">主子表</Option>
                  </Select>
                </FormItem>
              </Col>
              <Col span="8">
                <FormItem label="是否同步数据库" prop="isDbSynch" style="width: 100%" :label-width="120">
                  <Select style="" v-model="queryParams.isDbSynch" clearable placeholder="请选择">
                    <Option value="1">已同步</Option>
                    <Option value="0">未同步</Option>
                  </Select>
                </FormItem>
              </Col>
              <Col span="24" style="text-align: right;padding-top: 20px;">
                <FormItem :label-width="0">
                  <Button type="primary" @click="querySubmit()" style="margin-right: 8px">查询</Button>
                  <Button type="default" @click="handleReset('queryFrom')">重置</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="tableBtn">
          <Button @click="add()" type="primary" style="margin-right: 10px;margin-bottom: 5px" icon="md-add">新增</Button>
          <Button icon="ios-trash-outline" type="error" @click="handleDeleteMultiple()"
                  style="margin-right: 10px;margin-bottom: 5px">删除
          </Button>
          <Button @click="dbImport()" type="warning" style="margin-right: 10px;margin-bottom: 5px"
                  icon="md-cloud-upload">数据库导入
          </Button>
        </div>
        <Table v-loading="tableLOading" :columns="columns" :data="tableData" border
               @on-selection-change="selectedRows = $event">
          <template slot-scope="{row}" slot="action">
            <Button type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="handlePreview(row)">预览
            </Button>
            <Button type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="edit(row.tableId)">编辑
            </Button>
            <Button type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="remove(row.tableId)">删除
            </Button>
            <Button v-if="row.isDbSynch != 1" type="text" size="small" style="margin-right: 5px;color: #0056B5"
                    @click="syncDb(row.tableId)">同步数据库
            </Button>
            <Button type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="genWeb(row, row.tableId)">
              生成代码
            </Button>
          </template>
        </Table>
        <Page :total="dataCount" :page-size="pageSize" show-sizer show-total show-elevator :current="page + 1"  @on-change="handlePageChange($event, 'page')"
              @on-page-size-change="handlePageChange($event, 'size')"/>
      </Card>
    </div>
    <Modal
      v-model="formInfoModal"
      ref="formInfoModal"
      :closable="false"
      :width="1568"
      :fullscreen="isFullScreen"
    >
      <p slot="header">
        <Row :gutter="40">
          <Col span="20">
            <span v-html="modalTitle" class="modalHeaderClass"></span>
          </Col>
          <Col span="4" style="text-align:right">
            <a class="dark-a" href="javascript:void(0)" @click="screenClick">
              <Icon custom="iconfont  icon-FullScreen" v-if="!isFullScreen" :size="23"/>
              <Icon custom="iconfont  icon-ExitFullScreen" v-if="isFullScreen" :size="23"/>
            </a>
            <a class="dark-a" href="javascript:void(0)" @click="closeModal()">
              <Icon type="md-close" style="margin-left: 6px;" :size="20"/>
            </a>
          </Col>
        </Row>
      </p>
      <form-detail ref="formDetailWeb" :close-flag="formInfoModal" :add-flag="addOrEdit"
                   :tableData="tableData"></form-detail>
      <div slot="footer">
        <Button type="primary" size="large" @click="save()">确定</Button>
        <Button type="default" size="large" @click="closeModal()">取消</Button>
      </div>
    </Modal>
    <!-- 预览界面 -->
    <el-dialog :title="preview.title" :visible.sync="preview.open" width="80%" top="5vh" append-to-body
               class="scrollbar">
      <el-tabs v-model="preview.activeName">
        <el-tab-pane
          v-for="(value, key) in preview.data"
          :label="getPreName(key)"
          :name="getPreName(key)"
          :key="key"
        >
          <el-link :underline="false" icon="el-icon-document-copy" v-clipboard:copy="value"
                   v-clipboard:success="clipboardSuccess" style="float:right">复制
          </el-link>
          <pre><code class="hljs" v-html="highlightedCode(value, key)"></code></pre>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
    <import-db ref="importDb"></import-db>
  </div>
</template>

<script>
import FormDetail from './components/FormDetail'
import ImportDb from './components/ImportDb'
import hljs from 'highlight.js/lib/index.js'
import {
  previewGenCode,
  getFormTablesByParams,
  queryFormTableInfoById,
  addFormTablesByData,
  updateFormTablesByData,
  delFormTablesById,
  delFormTablesByIds,
  syncFormTables,
  downloadGenCode

} from '@/api/generator/generator'
import 'highlight.js/scss/github-gist.scss'
import { saveAs } from 'file-saver'
import axios from 'axios'
//
hljs.registerLanguage('java', require('highlight.js/lib/languages/java'))
hljs.registerLanguage('xml', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('html', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('vue', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('javascript', require('highlight.js/lib/languages/javascript'))
hljs.registerLanguage('sql', require('highlight.js/lib/languages/sql'))

export default {
  components: { FormDetail, ImportDb },
  data () {
    return {
      tableLOading: false,
      // 预览参数
      preview: {
        open: false,
        title: '代码预览',
        data: {},
        activeName: 'Controller.java'
      },
      selectedRows: [],
      modalTitle: '新增',
      isFullScreen: true,
      addOrEdit: 'add',
      editData: {},
      selectedArr: [],
      formDetail: {},
      formInfoModal: false,
      name: 'FormList',
      queryParams: {
        tableName: null,
        formType: null,
        isDbSynch: null
      },
      dataCount: 0,
      pageSize: 10,
      page: 0,
      columns: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          type: 'index',
          width: 80,
          align: 'center',
          title: '序号'
        },
        {
          title: '表名称',
          key: 'tableName',
          align: 'center'
        },
        {
          title: '表单名称',
          key: 'tableContent',
          align: 'center'
        },
        {
          title: '表单类型',
          key: 'formType',
          align: 'center',
          render: (h, e) => {
            let text = ''
            if (e.row.formType === 1) {
              text = '单表'
            } else if (e.row.formType === 2) {
              text = '树表'
            } else if (e.row.formType === 3) {
              text = '主子表'
            }
            return h('div', [
              h('span', text)
            ])
          }

        },
        {
          title: '同步数据库',
          key: 'isDbSynch',
          align: 'center',
          render: (h, e) => {
            let text = ''
            if (e.row.isDbSynch === 1) {
              text = '已同步'
            } else text = '未同步'
            return h('div', [
              h('span', text)
            ])
          }
        },

        {
          title: '操作',
          slot: 'action',
          align: 'center',
          width: 360
        }
      ],
      tableData: []
    }
  },
  mounted () {
    this.listTable()
  },
  methods: {
    getPreName (fileName) {
      let newFileName = fileName.replaceAll('\\', '/')
      return newFileName.substring(newFileName.lastIndexOf('/') + 1, newFileName.indexOf('.vm'))
    },
    /** 预览按钮 */
    handlePreview (row) {
      previewGenCode(row.tableId)
      // this.$http.get(`/api/gen/preview/${row.tableId}`)
        .then(response => {
          this.preview.data = response.data
          this.preview.open = true
          this.preview.activeName = 'Controller.java'
        })
    },
    /** 高亮显示 */
    highlightedCode (code, key) {
      // console.log(code, key)
      // const vmName = key.substring(key.lastIndexOf('/') + 1, key.indexOf('.vm'))
      // var language = vmName.substring(vmName.indexOf('.') + 1, vmName.length)
      var language = key.indexOf('java') !== -1 ? 'java' : key.indexOf('vue') !== -1 ? 'vue' : key.indexOf('js') !== -1 ? 'js' : null
      const result = hljs.highlight(language, code || '', true)
      return result.value || '&nbsp;'
    },
    /** 复制代码成功 */
    clipboardSuccess () {
      this.$message({ message: '复制成功', type: 'success', customClass: '' })
    },
    rowSelected (e) {
      this.selectedArr = e
    },
    handleReset (name) {
      this.$refs[name].resetFields()
      this.selectedRows = []
      this.page = 0
      this.listTable()
    },
    dbImport () {
      this.$refs.importDb.modalVisible = true
    },
    listTable () {
      let This = this
      // const [url, httpConfig] = [
      //   '/api/cscpHxFormTables',
      //   {
      //     params: {
      //       page: This.page,
      //       size: This.pageSize,
      //       'tableName.contains': This.queryParams.tableName,
      //       'isDbSynch.equals': This.queryParams.isDbSynch,
      //       'formType.equals': This.queryParams.formType
      //     }
      //   }
      // ]
      const queryParams = {
        page: This.page,
        size: This.pageSize,
        'tableName.contains': This.queryParams.tableName,
        'isDbSynch.equals': This.queryParams.isDbSynch,
        'formType.equals': This.queryParams.formType
      }
      getFormTablesByParams(queryParams)
      // This.$http.get(url, httpConfig)
        .then(function (response) {
          This.tableData = response.data.data
          This.dataCount = response.data.recordsTotal
        }).catch(function (error) {
          console.log(error)
        })
    },
    edit (tableId) {
      this.addOrEdit = 'edit'
      this.modalTitle = '修改'
      this.initFormDetail(tableId)
      // ID存在渲染表单
    },
    add () {
      this.addOrEdit = 'add'
      this.modalTitle = '新增'
      this.formInfoModal = true
      this.$store.state.tabdata.info = [
        {
          columnName: '',
          columnComment: '',
          columnLength: '',
          pointLength: '',
          defaultValue: '',
          columnType: '',
          isNull: false,
          isPk: false,
          orderNum: 1,
          isForm: false,
          isList: false,
          controlLength: null,
          isQuery: false,
          queryType: null,
          showType: null
        }
      ]
    },
    initFormDetail (tableId) {
      if (tableId) {
        let This = this
        This.tableLOading = true
        // const url = '/api/cscpHxFormTablesAll/' + tableId
        queryFormTableInfoById(tableId)
        // This.$http.get(url)
          .then(function (response) {
            This.tableLOading = false
            This.$refs.formDetailWeb.$refs['formTab'].$refs['FormData'].editDisabled = false
            let form = response.data
            // console.log(response.data)
            // console.log(form)
            This.editData = form
            This.editData.tableId = form.tableId
            This.editData.tableName = form.tableName
            This.editData.tableContent = form.tableContent
            This.editData.packageName = form.packageName
            This.editData.formType = form.formType
            This.editData.dbType = form.dbType
            // This.editData.formSuite = form.cscpHxFormSuite.id
            This.editData.formSuite = form.cscpHxFormSuite ? form.cscpHxFormSuite.id : form.formSuite
            This.$refs['formDetailWeb'].initData(This.editData)
            if (form.columns.length > 0) {
              for (let i = 0; i < form.columns.length; i++) {
                form.columns[i].isNull = form.columns[i].isNull !== 0
                form.columns[i].isForm = form.columns[i].isForm !== 0
                form.columns[i].isList = form.columns[i].isList !== 0
                form.columns[i].isQuery = form.columns[i].isQuery !== 0
                form.columns[i].isPk = form.columns[i].isPk !== 0
              }
              This.dataArr = form.columns
            } else {
              This.dataArr = []
            }
            This.$store.commit('setInfo', This.dataArr)
            This.formInfoModal = true
          }).catch(function (error) {
            This.tableLOading = false
            console.log(error)
          })
      }
    },
    closeModal () {
      this.formInfoModal = false
      this.editData = {}
      this.$refs['formDetailWeb'].$refs['formDetail'].resetFields()
      this.$store.state.tabdata.info = []
    },
    save () {
      let _this = this
      this.$refs['formDetailWeb'].$refs['formDetail'].validate((valid) => {
        if (valid) {
          let keyFlag = false
          let arr = this.$store.state.tabdata.info
          let primaryKeyIndex = 0
          for (let i = 0; i < arr.length; i++) {
            if (arr[i].columnName.trim().length === 0) {
              continue
            } else {
              if (arr[i].columnType == null || arr[i].columnType === '') {
                _this.$Notice.error({
                  title: '数据属性异常',
                  desc: '数据属性中的字段类型未选择！'
                })
                return
              }
            }
            let isQuery = arr[i].isQuery
            let isShow = arr[i].isForm
            if (isQuery) {
              if (arr[i].queryType == null || arr[i].queryType === '') {
                _this.$Notice.error({
                  title: '页面属性异常',
                  desc: '页面属性勾选了是否查询的字段必须选择查询类型！'
                })
                return
              }
            }
            if (isQuery || isShow) {
              if (arr[i].showType == null || arr[i].showType === '') {
                _this.$Notice.error({
                  title: '页面属性异常',
                  desc: '页面属性勾选了是否查询或者表单显示的的字段必须选择控件类型！'
                })
                return
              }
            }

            if (arr[i].columnType === 6 || arr[i].columnType === 7 || arr[i].columnType === 8) {
              if (arr[i].columnLength == null || arr[i].pointLength == null || arr[i].pointLength === '' || arr[i].columnLength === '') {
                _this.$Notice.error({
                  title: '数据属性异常',
                  desc: '数据属性字段类型为float、double或者decimal的字段必须填写字段长度和小数点'
                })
                return
              }
            }
            if (arr[i].isPk) {
              keyFlag = true
              if (arr[i].isNull) {
                _this.$Notice.error({
                  title: '数据属性异常',
                  desc: '请检查主键字段，主键字段不能为空！！！'
                })
                return
              }
              primaryKeyIndex = i
            }

            // 树表的父id 不可为null
            if (_this.editData.formType === 2) {
              if (arr[i].columnName === _this.editData.options.treeParentCode && arr[i].isNull) {
                _this.$Notice.error({
                  title: '页面属性异常',
                  desc: '树表父id不可为空'
                })
                return
              }
            }
          }
          let feilds = JSON.parse(JSON.stringify(arr))
          for (let i = 0; i < feilds.length; i++) {
            if (feilds[i].columnName.trim().length === 0) {
              feilds.splice(i, 1)
              continue
            }
            feilds[i].isNull = feilds[i].isNull ? 1 : 0
            feilds[i].isForm = feilds[i].isForm ? 1 : 0
            feilds[i].isList = feilds[i].isList ? 1 : 0
            feilds[i].isQuery = feilds[i].isQuery ? 1 : 0
            feilds[i].isPk = feilds[i].isPk ? 1 : 0
            feilds[i].controlLength = feilds[i].controlLength === '' ? null : feilds[i].controlLength
          }

          // 字段类型中必须有一个字段设置为主键
          if (!keyFlag) {
            _this.$Notice.error({
              title: '数据属性异常',
              desc: '数据属性中的主键未设置！'
            })
            return
          }
          if (arr[primaryKeyIndex].columnType !== 5) {
            _this.$Notice.error({
              title: '数据属性异常',
              desc: '数据属性中的主键数据类型应选择bigint！'
            })
            return
          }
          if (_this.addOrEdit === 'add') {
            _this.create(feilds)
          } else {
            _this.put(feilds)
          }
        }
      })
    },
    create (feilds) {
      let formR = this.$refs['formDetailWeb'].$refs['formDetail'].model
      if (formR.genType == 0) {
        formR.genPath = null
      }
      formR.columns = feilds
      if (formR.options) {
        formR.options = JSON.stringify(formR.options)
      }
      let This = this
      This.saveLoading = true
      // const [url, httpConfig] = [
      //   '/api/cscpHxFormTables',
      //   {
      //     headers: {
      //       'Content-Type': 'application/json;charset=UTF-8'
      //     }
      //   }
      // ]
      addFormTablesByData({
        'Content-Type': 'application/json;charset=UTF-8'
      }, formR)
      // This.$http.post(url, formR, httpConfig)
        .then(function (response) {
          This.$Notice.success({
            title: '保存成功'
          })
          // This.$router.push({
          //   name: 'defined-form-list'
          // })
          This.closeModal()
          This.listTable()
        }).catch(() => {
          This.$Notice.error({
            title: '操作失败！'
          })
          This.saveLoading = false
        })
    },
    put (feilds) {
      let formR = this.$refs['formDetailWeb'].$refs['formDetail'].model
      if (formR.genType == 0) {
        formR.genPath = null
      }
      formR.columns = feilds
      if (formR.options) {
        formR.options = JSON.stringify(formR.options)
      }

      let This = this
      This.saveLoading = true
      // const [url, httpConfig] = [
      //   '/api/cscpHxFormTables',
      //   {
      //     headers: {
      //       'Content-Type': 'application/json;charset=UTF-8'
      //     }
      //   }
      // ]
      updateFormTablesByData({
        'Content-Type': 'application/json;charset=UTF-8'
      }, formR)
      // This.$http.put(url, formR, httpConfig)
        .then(function (response) {
          This.$Notice.success({
            title: '保存成功'
          })
          This.closeModal()
          This.listTable()
        })
        .catch(() => {
          This.$Notice.error({
            title: '操作失败！'
          })
          This.saveLoading = false
        })
    },
    remove (id) {
      let This = this
      this.$Modal.confirm({
        title: '确定删除？',
        content: '提示：该删除不会删除数据库表',
        onOk () {
          delFormTablesById(id)
          // This.$http.delete('/api/cscpHxFormTables/' + id)
            .then(response => {
              if (response.status === 200) {
                This.$Notice.success({
                  title: '删除成功'
                })
                This.listTable()
              } else {
                This.$Notice.error({
                  title: '删除失败'
                })
              }
            })
        }
      })
    },
    genWeb (row) {
      console.info('row', row)
      if (!row.isDbSynch) {
        this.$Message.error('相关配置信息发生改变，请先点击"同步数据库"，再点击"代码生成"按钮！！！')
        return
      }
      // const url = this.$util.baseUrl + '/api/gen/download/' + row.tableId
      if (row.genType === 1) {
        downloadGenCode(row.tableId)
        // this.$http.get(url)
          .then(() => {
            this.$Message.success('生成代码成功！')
          }).catch(() => {
            this.$Message.error('获取数据失败！')
          })
      } else {
        let This = this
        downloadGenCode(row.tableId, 'blob')
        // axios({
        //   method: 'get',
        //   url: url,
        //   responseType: 'blob',
        //   headers: { 'Authorization': localStorage.token }
        // })
          .then(async (res) => {
            console.log(res)
            const blob = new Blob([res.data], { type: 'application/zip' })
            saveAs(blob, 'udp')
          }).catch(function (error) {
            This.$Notice.error({
              title: '操作失败'
            })
            console.log(error)
          })
      }
    },
    syncDb (id) {
      let This = this
      syncFormTables(id)
      // This.$http.put('/api/cscpHxsync/' + id)
        .then(function (response) {
          This.$Notice.success({
            title: '同步成功！'
          })
          This.listTable()
        })
        .catch(function (error) {
          This.$Notice.error({
            title: '同步失败！'
          })
          console.log(error)
        })
    },

    handleDeleteMultiple: function () {
      let arr = this.selectedRows.map(item => item.tableId)
      if (arr.length > 0) {
        // this.$http.delete('/api/cscpHxFormTables/delAll?ids=' + arr).then((response) => {
        //   this.listTable()
        //   this.$Notice.success({
        //     title: '删除成功！'
        //   })
        // })

        this.$Modal.confirm({
          title: '警告',
          content: '<p>确定删除勾选的数据？</p>',
          onOk: () => {
            delFormTablesByIds(arr)
            // this.$http.delete('/api/cscpHxFormTables/delAll?ids=' + arr)
              .then(res => {
                this.$Notice.success({
                  title: '删除成功！'
                })
                this.listTable()
              }).catch(error => {
                if (error.response) {
                  this.$Message.error(error.response.data.title ? error.response.data.title : '删除失败！')
                }
              })
          },
          onCancel: () => {
          }
        })
      } else {
        this.$Message.error('删除失败,当前选择列表为空！！')
      }
    },
    querySubmit () {
      this.page = 0
      this.listTable()
    },
    handlePageChange (e, type) {
      if (type === 'size') {
        this.pageSize = e
      } else if (type === 'page') {
        this.page = e - 1
      }
      this.listTable()
    },
    screenClick () {
      if (this.isFullScreen) {
        this.isFullScreen = false
      } else {
        this.isFullScreen = true
      }
    }
  }

}
</script>

<style lang="less" scoped>
@import "../admin/styles/formStyle.less";

.page-div {
  display: flex;
  justify-content: flex-end;
  padding-top: 35px;
  padding-right: 50px;
}

.modalHeaderClass {
  position: relative;
  color: #2d8cf0;
  font-size: 16px;
}
::v-deep pre {
  margin: 0;
  height: 100%;
  overflow: hidden;
  max-height: calc(80vh - 32px);
  overflow-y: auto;
}
::v-deep pre::-webkit-scrollbar {
  display: none;
}
/*::v-deep .el-dialog__body::-webkit-scrollbar {*/
/*  display: none;*/
/*}*/
::v-deep .el-dialog__body {
  padding: 16px;
  max-height: 143vh;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  overflow-y: auto;
}
</style>
