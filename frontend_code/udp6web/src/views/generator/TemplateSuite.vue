<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card
        :bordered="false"
        :dis-hover="true"
        :shadow="false"
        class="formCard"
      >
        <div class="searchArea">
          <Form ref="queryForm" :model="queryForm" :label-width="80">
            <Row :gutter="40">
              <Col span="8" style="margin-bottom: 12px">
                <FormItem label="模板名称" prop="suiteName" style="width: 100%">
                  <Input
                    type="text"
                    :maxlength="100"
                    v-model.trim="queryForm.suiteName"
                    placeholder="请输入模板名称"
                    style="width: 100%"
                  ></Input>
                </FormItem>
              </Col>
              <Col span="24" style="text-align: right; margin-top: 12px">
                <FormItem :label-width="0">
                  <Button
                    type="primary"
                    @click="queryList(true)"
                    style="margin-right: 8px"
                    >查询</Button
                  >
                  <Button type="default" @click="handleReset('queryForm')"
                    >重置</Button
                  >
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
      <Card
        :bordered="false"
        :dis-hover="true"
        :shadow="false"
        class="formCard"
      >
        <div class="tableBtn">
          <Button icon="md-add" type="primary" @click="handleAdd()"
            >新增</Button
          >
          <Button
            icon="ios-trash-outline"
            type="error"
            @click="handleDeleteMultiple()"
            style="margin-left: 20px"
            >删除</Button
          >
        </div>
        <Table border
          :columns="columns"
          :data="data"
          @on-selection-change="rowSelected($event)"
          style=""
        >
          <template slot-scope="{ row, index }" slot="action">
            <Button
              type="text"
              size="small"
              style="margin-right: 5px; color: #0056b5"
              @click="handleEdit(row, index)"
              >编辑</Button
            >
            <Button
              type="text"
              size="small"
              style="margin-right: 5px; color: #0056b5"
              @click="remove(row, index)"
              >删除</Button
            >
          </template>
        </Table>
        <Page
          :total="pageData.total"
          :page-size="pageData.size"
          :current="pageData.page"
          show-elevator
          show-sizer
          show-total
          @on-page-size-change="changePage($event, 'size')"
          @on-change="changePage($event, 'page')"
          style="margin-top: 20px"
        />
      </Card>
    </div>
    <Modal
      v-model="modal"
      :title="title"
      ref="modal"
      @on-cancel="close"
      :width="580"
    >
      <Form
        :model="detail"
        ref="detail"
        :rules="ruleValidate"
        :label-width="120"
        label-position="left"
      >
        <FormItem label="模板名称" prop="suiteName">
          <Input class="input-width" style="width: 80%;" v-model="detail.suiteName"></Input>
            <Tooltip max-width="300" placement="top" content="模板名可以为中文、字母、数字" :disabled="disabled">
              <i class="el-icon-question" style="margin-left: 10px;"></i>
            </Tooltip>
        </FormItem>
        <FormItem label="模板编码" prop="suiteCode">
          <Input class="input-width" style="width: 80%;" v-model="detail.suiteCode"></Input>
          <!-- 以字母开头结尾，多个单词使用“-”连接 -->
            <Tooltip max-width="300" placement="top" content="以字母开头结尾，多个单词使用“-”连接" :disabled="disabled">
              <i class="el-icon-question" style="margin-left: 10px;"></i>
            </Tooltip>
        </FormItem>
        <FormItem label="模板版本" prop="suiteVersion">
          <Input class="input-width" style="width: 80%;" v-model="detail.suiteVersion"></Input>
          <Tooltip max-width="300" placement="top" content="请使用V或v字开头，如V1,v1.2,v1.1.2等" :disabled="disabled">
            <i class="el-icon-question" style="margin-left: 10px;"></i>
          </Tooltip>
        </FormItem>
        <FormItem label="模板类型" prop="suiteType">
          <Select v-model="detail.suiteType" style="width: 80%;" placeholder="请选择">
            <Option :value="0">通用表单</Option>
          </Select>
        </FormItem>
        <FormItem label="保存路径" v-show="detail.id !== null" prop = "suitePath">
          <Input style="width: 80%;"
            class="input-width"
            v-model.trim="detail.suitePath"
            disabled
          ></Input>
        </FormItem>
        <FormItem label="模版文件" prop="mFile" >
          <Upload
            v-model="detail.mFile"
            ref="upload"
            action=""
            :before-upload="handleUpload"
          >
            <Button icon="ios-cloud-upload-outline">选择文件</Button>
          </Upload>
          <div>
            <ul class="ivu-upload-list" v-show="detail.mFile !== null">
              <li class="ivu-upload-list-file ivu-upload-list-file-finish">
                <span
                  ><i class="ivu-icon ivu-icon-ios-document-outline"></i>
                  {{ fileName }}
                </span>
              </li>
            </ul>
            <ul
              class="ivu-upload-list"
              v-show="detail.mFile == null && detail.id !== null"
            >
              <li class="ivu-upload-list-file ivu-upload-list-file-finish">
                <span
                  ><i class="ivu-icon ivu-icon-ios-document-outline"></i>
                  {{ detail.suiteCode }}-{{ detail.suiteVersion }}.zip
                </span>
              </li>
            </ul>
          </div>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button
          type="primary"
          @click="ok(okFlag)"
          :disabled="disabled"
          style="width: 80px"
          >确定</Button
        >
        <Button type="default" @click="close()" style="width: 80px"
          >取消</Button
        >
      </div>
    </Modal>
  </div>
</template>

<script>
import {
  validTemplateSuitesByParams,
  getCustomerSuiteInfoById,
  delCustomerSuiteInfoById,
  updateOrAddCustomerSuiteByData,
  getCustomerSuitbyParams,
  delCustomerSuiteByIds
} from '@/api/generator/generator'
export default {
  name: 'codeTemplateSuiteManager',

  data () {
    const validateUpload = (rule, value, callback) => {
      if (this.detail.id !== null && this.detail.mFile === null) {
        callback()
      } else {
        if (this.detail.mFile === null) {
          callback(new Error('请上传模板文件'))
        } else {
          let name = this.detail.mFile.name
          let index = name.lastIndexOf('.')
          let type = name.substring(index, name.length)
          if (type !== '.zip') {
            callback(new Error('文件格式只能为zip'))
          } else {
            callback()
          }
        }
      }
    }
    const validatesuiteName = (rule, value, callback) => {
      // 格式校验
      let reg = /^[\u4E00-\u9FA5A-Za-z0-9]+$/
      if (reg.test(value)) {
      // 唯一性校验
        let params = {}
        params['suiteName.equals'] = this.detail.suiteName ? this.detail.suiteName : null
        validTemplateSuitesByParams(params)
        // this.$http.get('/api/codeTemplateSuitesList/', {
        //   params: params
        // })
          .then(res => {
            // console.log('>>>>>>>findTempletBySuiteCodeAndSuiteVersion', res.data)
            // console.log(res.data.data.length, (res.data.data.length > 0), (this.detail.id === null))
            if ((this.detail.id === null && res.data.data.length > 0) || (this.detail.id !== null && res.data.data.length > 0 && this.detail.id !== res.data.data[0]['id'])) {
              // alert('ssss')
              callback(new Error('请修改模板名，该模板名已存在！！'))
            }
            callback()
          })
      } else {
        callback(new Error('模板名格式不正确，请输入正确的格式！！'))
      }
    }
    const validateSuiteCode = (rule, value, callback) => {
      // 格式校验
      let reg = /^[a-zA-Z]+([-]?[a-zA-Z]){0,}$/
      if (reg.test(value)) {
        callback()
      } else {
        callback(new Error('模板编码格式不正确，请输入正确的格式！！'))
      }
    }
    const validateSuiteVersion = (rule, value, callback) => {
      // 格式校验
      let reg = /^[Vv][0-9]+([.]?[0-9]+){0,}$/
      // console.log('>>>>>>>>>>>', reg.test(value))
      if (reg.test(value)) {
        // 唯一性校验
        let params = {}
        params['suiteCode.equals'] = this.detail.suiteCode ? this.detail.suiteCode : null
        params['suiteVersion.equals'] = this.detail.suiteVersion ? this.detail.suiteVersion : null
        validTemplateSuitesByParams(params)
        // this.$http.get('/api/codeTemplateSuitesList/', {
        //   params: params
        // })
          .then(res => {
          // console.log('>>>>>>>findTempletBySuiteCodeAndSuiteVersion', res.data)
          // console.log(res.data.data.length, (res.data.data.length > 0), (this.detail.id === null))
            if ((this.detail.id === null && res.data.data.length > 0) || (this.detail.id !== null && res.data.data.length > 0 && this.detail.id !== res.data.data[0]['id'])) {
            // alert('ssss')
              callback(new Error('请修改版本号，该版本号已存在！！'))
            }
            callback()
          })
      } else {
        callback(new Error('模板版本格式不正确，请输入正确的格式！！'))
      }
    }
    return {
      detail: {
        id: null,
        suiteName: '',
        suiteCode: '',
        suiteVersion: '',
        suitePath: '',
        suiteType: 0,
        mFile: null
      },
      fileName: null,
      modal: false,
      disabled: false,
      selectedArr: [],
      queryForm: {},
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
          width: 80
        },
        {
          title: '模板名称',
          key: 'suiteName'
        },
        {
          title: '模板编码',
          key: 'suiteCode'
        },
        {
          title: '模板版本',
          key: 'suiteVersion'
        },
        {
          title: '操作',
          slot: 'action',
          width: 300,
          align: 'center'
        }
      ],
      data: [
      ],
      okFlag: '',
      modalValid: true,
      ruleValidate: {
        suiteName: [
          { required: true, message: '请输入模板名称', trigger: 'blur' },
          {
            required: true,
            max: 50,
            message: '模板名称最多50个字符',
            trigger: 'blur'
          },
          { required: true, validator: validatesuiteName, trigger: 'blur' }
        ],
        suiteCode: [
          { required: true, message: '请输入模板编号', trigger: 'blur' },
          {
            required: true,
            max: 50,
            message: '模板编号最多50个字符',
            trigger: 'blur'
          },
          { required: true, validator: validateSuiteCode, trigger: 'blur' }

        ],
        suiteVersion: [
          { required: true, validator: validateSuiteVersion, trigger: 'blur' },
          {
            required: true,
            max: 16,
            message: '模板版本最多16个字符',
            trigger: 'blur'
          }

        ],
        suiteType: [
          { required: true, message: '请选择模板类型', trigger: 'change', type: 'number' }
        ],
        mFile: [
          { required: true, validator: validateUpload, trigger: 'change' }
        ]
      }
    }
  },
  mounted () {
    this.queryList()
  },
  computed: {
    title: function () {
      if (this.okFlag === 'add') {
        return '新增'
      } else {
        return '编辑'
      }
    }
  },
  methods: {
    // 文件上传
    handleUpload (file) {
      this.detail.mFile = file
      this.fileName = file.name
      this.$refs['detail'].validate((valid) => {
      })

      return false
    },
    close () {
      this.modal = false
      this.$refs['detail'].resetFields()

      console.log('close>>>>>>>>>>>>>>>>>>')
      this.fileName = ''
      this.okFlag = ''
      this.disabled = false
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
      this.detail = {
        id: null,
        suiteName: '',
        suiteCode: '',
        suiteVersion: '',
        suitePath: '',
        suiteType: 0,
        mFile: null
      }

      this.okFlag = 'add'
      this.modal = true
    },
    handleEdit (row, index) {
      getCustomerSuiteInfoById(row.id)
      // this.$http.get('/api/codeTemplateSuites/' + row.id)
        .then(res => {
          this.modal = true
          this.detail = res.data
          this.detail.mFile = null
          this.okFlag = 'update'
        }).catch(() => {
          this.$Message.error('行数据查询失败')
        })
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
          delCustomerSuiteInfoById(row.id)
          // this.$http.delete('/api/codeTemplateSuites/' + row.id)
            .then(res => {
              if (res.data.msg) {
                this.$Message.error(res.data.msg)
                return
              } else this.$Message.success('删除成功')
              this.queryList()
            }).catch(() => {
              this.$Message.error('删除失败')
            })
        },
        onCancel: () => {}
      })
    },
    ok (type) {
      this.$delete(this.detail, '')
      this.$delete(this.detail, '_rowKey')
      console.log('>>>>>>>>>type', type)

      this.$refs['detail'].validate((valid) => {
        console.log('valid>>>>>>>', valid)
        if (valid) {
          if (type === 'update') {
            let This = this
            let param = new FormData()
            This.saveLoading = true
            param.append('id', This.detail.id)
            param.append('suiteName', This.detail.suiteName)
            param.append('suiteCode', This.detail.suiteCode)
            param.append('suiteVersion', This.detail.suiteVersion)
            param.append('suitePath', This.detail.suitePath)
            param.append('suiteType', This.detail.suiteType)
            param.append('multipartFile', This.detail.mFile)
            // const [url, httpConfig] = [
            //   '/api/codeTemplateSuites/edit',
            //   {
            //     headers: {
            //       'Content-Type': 'multipart/form-data'
            //     }
            //   }
            // ]
            updateOrAddCustomerSuiteByData({
              'Content-Type': 'multipart/form-data'
            }, param)
            // This.$http.put(url, param, httpConfig)
              .then(function (response) {
                This.$Notice.success({
                  title: '更新成功'
                })
                This.modal = false
                This.$refs['queryForm'].resetFields()
                This.queryList()
                This.$refs['detail'].resetFields()
                This.detail.id = null
                This.okFlag = ''
                This.fileName = ''
              })
              .catch(function (error) {
                This.$Notice.error({
                  title: '操作失败！'
                })
                console.log(error)
              })
          } else if (type === 'add') {
            let This = this
            let param = new FormData()
            This.saveLoading = true
            param.append('suiteName', This.detail.suiteName)
            param.append('suiteCode', This.detail.suiteCode)
            param.append('suiteVersion', This.detail.suiteVersion)
            param.append('suitePath', This.detail.suitePath)
            param.append('suiteType', This.detail.suiteType)
            param.append('multipartFile', This.detail.mFile)

            // const [url, httpConfig] = [
            //   '/api/codeTemplateSuites',
            //   {
            //     headers: {
            //       'Content-Type': 'multipart/form-data'
            //     }
            //   }
            // ]
            updateOrAddCustomerSuiteByData({
              'Content-Type': 'multipart/form-data'
            }, param)
            // This.$http.post(url, param, httpConfig)
              .then(function (response) {
                This.$Notice.success({
                  title: '保存成功'
                })
                This.modal = false
                This.$refs['queryForm'].resetFields()
                This.queryList()
                This.$refs['detail'].resetFields()
                This.detail.id = null
                This.okFlag = ''
                This.fileName = ''
              })
              .catch(function (error) {
                This.$Notice.error({
                  title: '操作失败1！'
                })
                console.log(error)
              })
          }
        } else {
          this.modal = true
        }
      })
    },
    queryList (flag) {
      if (flag) {
        this.pageData.page = 1
      }
      let obj = {}
      obj['suiteName.contains'] = this.queryForm['suiteName'] ? this.queryForm['suiteName'] : null
      let params = Object.assign(obj, {
        page: this.pageData.page - 1,
        size: this.pageData.size
      })
      //   this.$http.get('/api/codeTemplateSuites/', {
      getCustomerSuitbyParams(params)
      // this.$http.get('/api/codeTemplateSuitesByCriteria/', {
      //   params: params
      // })
        .then(res => {
          this.data = res.data.data
          this.pageData.total = res.data.recordsTotal
        }).catch(() => {
          this.$Message.error('列表查询失败')
        })
    },
    handleDeleteMultiple: function () {
      if (this.selectedArr == null || this.selectedArr.length < 1) {
        return this.$Message.error('删除失败,当前选择列表为空！')
      }
      let arr = this.selectedArr.map(item => item.id)
      // if (arr.length < 1) {
      // }
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除勾选的数据？</p>',
        onOk: () => {
          delCustomerSuiteByIds(arr)
          // this.$http.delete('/api/codeTemplateSuites/delAll?ids=' + arr)
            .then(res => {
              this.$Message.success('删除成功')
              this.selectedArr = null
              this.queryList()
            }).catch(error => {
              if (error.response) {
                this.$Message.error(error.response.data.title ? error.response.data.title : '删除失败！')
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

<style lang="less" scoped>
  @import "../admin/styles/formStyle.less";
  ::v-deep .ql-editor{
    min-height: 200px;
  }
</style>
