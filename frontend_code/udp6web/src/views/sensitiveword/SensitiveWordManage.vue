<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea" >
          <Form ref="queryForm" :model="queryForm" :label-width="80" >
            <Row :gutter="40">
              <Col span="8" style="margin-bottom: 12px">
                <FormItem label="分类"  prop="senTypeId" style="width: 100%">
                <Select v-model="queryForm.senTypeId" clearable style="width:170px">
                    <Option v-for="item in senTypeList" :value="item.id" :key="item.id">{{ item.senTypeName }}</Option>
                </Select>
                </FormItem>
            </Col>
              <Col span="8" style="margin-bottom: 12px">
                <FormItem label="敏感词"  prop="senContent" style="width: 100%">
                  <Input  type="text" :maxlength="100" v-model.trim="queryForm.senContent" placeholder="请输入敏感词内容" style="width: 100%"></Input>
                </FormItem>
            </Col>
              <Col span="24" style="text-align: right;margin-top: 12px">
                <FormItem :label-width="0">
                  <Button   type="primary" @click="queryList(true)" style="margin-right: 8px">查询</Button>
                  <Button type="default"  @click="handleReset('queryForm')">重置</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="tableBtn">
          <Button  icon="md-add"  type="primary"  @click="handleAdd()">新增</Button>
          <Button  icon="ios-trash-outline" type="error"  @click="handleDeleteMultiple()" style="margin-left: 20px">删除</Button>
          <!-- <el-button type="info" icon="el-icon-upload2" size="medium" @click="handleImport">批量导入
          </el-button> -->
          <Button type="info" icon="ios-cloud-upload-outline" @click="handleImport" style="margin-left: 20px">批量导入
          </Button>
        </div>
        <!-- <div class="tableBtn">
          <Button type="success" shape="circle" icon="md-cloud-download" style="margin-left:10px;" @click="downloadTemp" :loading="loading">模板下载</Button>
          <Upload :action="baseUrl+'/api/system/uploadSensitiveWordData'" :headers="headers" :show-upload-list="false" :on-success="uploadSuccess">
              <Button type="success" shape="circle" icon="md-cloud-upload" style="margin-left:10px;">批量导入</Button>
          </Upload>
        </div> -->
        <Table border :columns="columns" :data="data" @on-selection-change="rowSelected($event)" style="">
          <template slot-scope="{ row, index }" slot="action">
            <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="handleEdit(row,index)">编辑</Button>
            <Button  type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="remove(row, index)">删除</Button>
          </template>
        </Table>
        <Page :total="pageData.total" :page-size="pageData.size" :current="pageData.page" show-elevator show-sizer show-total @on-page-size-change="changePage($event, 'size')" @on-change="changePage($event, 'page')" style="margin-top: 20px;"/>
      </Card>
    </div>
<el-dialog :title="upload.title" :visible.sync="upload.open" width="400px">
  <!-- :action="upload.url" -->
  <el-upload
    ref="upload"
    :limit="1"
    accept=".xlsx, .xls"
    :headers="upload.headers"
    :action="upload.url"
    :disabled="upload.isUploading"
    :on-progress="handleFileUploadProgress"
    :on-success="handleFileSuccess"
    :on-error="handleFileError"
    :auto-upload="false"
    drag
  >
    <i class="el-icon-upload"></i>
    <div class="el-upload__text">
      将文件拖到此处，或
      <em>点击上传</em>
    </div>
    <div class="el-upload__tip" slot="tip">
      <el-link type="info" style="font-size:12px;color:red" @click="importTemplate">下载模板</el-link>
    </div>
    <div class="el-upload__tip" style="color:#ee8f01" slot="tip">提示：仅允许导入“xls”或“xlsx”格式文件！</div>
  </el-upload>
  <div slot="footer" class="dialog-footer">
    <el-button type="primary" @click="submitFileForm">确 定</el-button>
    <el-button @click="upload.open = false">取 消</el-button>
  </div>
</el-dialog>

    <Modal
      v-model="modal"
      :title="title"
      ref="modal"
      @on-cancel="close"
      :width="580"
    >
      <Form ref="detail" :model="detail" :label-width="80" :rules="ruleValidate" style="margin-top: 20px">
       <FormItem label="分类"  prop="senTypeId">
            <Select v-model="detail.senTypeId" clearable >
                <Option v-for="item in senTypeList" :value="item.id" :key="item.id">{{ item.senTypeName }}</Option>
            </Select>
       </FormItem>

        <FormItem label="敏感词"  prop="senContent">
            <Input type="text" :maxlength="100" v-model="detail.senContent" placeholder="请输入敏感词内容" ></Input>
        </FormItem>
        <FormItem label="替换内容"  prop="senReplace">
            <Input type="text" :maxlength="100" v-model="detail.senReplace" placeholder="请输入敏感词替换" ></Input>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="primary"  @click="ok(okFlag)" :disabled="disabled"  style="width: 80px">确定</Button>
        <Button type="default"  @click="close()"  style="width: 80px">取消</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import {
  listSensitiveWordByParams,
  delSensitiveWordByIds,
  querySensitiveWordById,
  delSensitiveWordById,
  updateSensitiveWordByData,
  addSensitiveWordByData,
  getSensitiveWordTemplate
} from '@/api/sensitiveword/sensitiveword'
import { listSensitiveWordType } from '@/api/sensitiveword/sensitiveword-type'
export default {
  name: 'SensitiveWordManager',
  data () {
    return {
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: '',
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: { Authorization: localStorage.token },
        // 上传的地址
        url: this.$util.baseUrl + '/api/system/uploadSensitiveWordData'
      },
      // baseUrl: this.$util.baseUrl,
      // headers: { Authorization: localStorage.token },
      modal: false,
      disabled: false,
      selectedArr: [],
      senTypeList: [],
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
        { key: 'senTypeName', title: '敏感词分类', align: 'center' },
        {
          title: '敏感词内容',
          key: 'senContent'
        },
        {
          title: '敏感词替换',
          key: 'senReplace'
        },
        {
          title: '更新时间',
          key: 'updateTime'
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
      detail: {
      },
      okFlag: '',
      modalValid: true,
      ruleValidate: {
        'senTypeId': [
          { required: true, message: '分类不能为空', trigger: 'blur' }
        ],
        'senContent': [
          { required: true, message: '敏感词不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  mounted () {
    this.queryList()
    this.getTypeList()
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
    // 打开导入面板
    handleImport () {
      this.upload.title = '敏感词导入'
      this.upload.open = true
    },
    // 下载模板
    importTemplate () {
      let self = this
      self.loading = true
      getSensitiveWordTemplate('blob')
      // this.$http.get('/api/system/downLoadSensitiveWordTemplate', { responseType: 'blob' })
        .then(response => {
          let fileName = '敏感词批量导入模板.xlsx'
          // 由于ie不支持download属性，故需要做兼容判断
          if (navigator.appVersion.toString().indexOf('.NET') > 0) {
          // ie独有的msSaveBlob属性，data.data为Blob文件流
            window.navigator.msSaveBlob(response.data, fileName)
          } else {
          // 下载流程
            let url = window.URL.createObjectURL(response.data)
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.download = fileName
            document.body.appendChild(link)
            link.click()
            window.URL.revokeObjectURL(link.href)
          }
          self.loading = false
        }).catch(error => {
          if (error.response.data.message) {
            self.$Notice.error({ title: '提示', desc: error.response.data.message })
          }
        })
    },
    // 文件上传中处理
    handleFileUploadProgress (event, file, fileList) {
      this.upload.isUploading = true
    },
    // 文件上传成功处理
    handleFileSuccess (response, file, fileList) {
      this.upload.open = false
      this.upload.isUploading = false
      this.$refs.upload.clearFiles()
      this.$Message.success({
        content: '数据导入成功',
        duration: 3
      })
      this.queryList()
    },
    handleFileError (err, file, fileList) {
      this.upload.open = false
      this.upload.isUploading = false
      this.$refs.upload.clearFiles()
      console.log('err>>>>', err, '>>>>>>', err.message)
      this.$Message.error({
        content: err.message,
        duration: 3
      })
      this.queryList()
    },
    // 提交上传文件
    submitFileForm () {
      this.$refs.upload.submit()
    },
    // getTypeList () {
    //   let self = this
    //   this.$http.get('/api/system/cscpSensitiveWordTypesList', { params: {} }).then(function (response) {
    //     self.senTypeList = response.data.data
    //   }).catch(function (error) {
    //     console.log(error)
    //   })
    // },
    getTypeList () {
      let self = this
      listSensitiveWordType('{}').then(response => {
        self.senTypeList = response.data.data
      }).catch(function (error) {
        console.log(error)
      })
    },
    close () {
      this.modal = false
      this.$refs['detail'].resetFields()
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
      this.detail = {}
      this.okFlag = 'add'
      this.modal = true
    },
    handleEdit (row, index) {
      querySensitiveWordById(row.id)
      // this.$http.get(`/api/system/cscpSensitiveWords/${row.id}`)
        .then(res => {
          this.modal = true
          this.detail = res.data
          this.okFlag = 'update'
        }).catch(() => {
          this.$Message.error('行数据查询失败')
        })
    },
    handleReset (name) {
      this.$refs[name].resetFields()
      this.pageData.page = 1
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
          delSensitiveWordById(row.id)
          // this.$http.delete('/api/system/cscpSensitiveWords/' + row.id)
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
      let copyDetail = JSON.parse(JSON.stringify(this.detail))
      if (this.detail.updateTime) {
        copyDetail.updateTime = this.$moment(new Date(this.detail.updateTime)).format('yyyy-MM-DD HH:mm:ss')
      }

      this.$refs['detail'].validate((valid) => {
        if (valid) {
          if (type === 'update') {
            updateSensitiveWordByData(copyDetail)
            // this.$http.put('/api/system/cscpSensitiveWords', copyDetail)
              .then(res => {
                if (res.data.code == 200) {
                  this.$Message.success('修改成功')
                  this.modal = false
                  this.queryList()
                  this.$refs['detail'].resetFields()
                } else {
                  this.$Message.error({
                    content: res.data.msg,
                    duration: 3
                  })
                }
              }).catch(() => {
              // this.$Message.error('修改失败')
              })
          } else if (type === 'add') {
            addSensitiveWordByData(copyDetail)
            // this.$http.post('/api/system/cscpSensitiveWords', copyDetail)
              .then(res => {
                if (res.data.code == 200) {
                  this.$Message.success('新增成功')
                  this.modal = false
                  this.$refs['queryForm'].resetFields()
                  this.queryList()
                  this.$refs['detail'].resetFields()
                } else {
                  this.$Message.error({
                    content: res.data.msg,
                    duration: 3
                  })
                }
              }).catch(() => {
              // this.$Message.error('新增失败')
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
      obj['senTypeId.equals'] = this.queryForm['senTypeId'] ? this.queryForm['senTypeId'] : null
      obj['senContent.contains'] = this.queryForm['senContent'] ? this.queryForm['senContent'] : null
      let params = Object.assign(obj, {
        page: this.pageData.page - 1,
        size: this.pageData.size
      })

      listSensitiveWordByParams(params)
      // this.$http.get('/api/system/cscpSensitiveWordsByCriteria', {
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
      let arr = this.selectedArr.map(item => item.id)
      if (arr.length < 1) {
        return this.$Message.error('删除失败,当前选择列表为空！')
      }
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除勾选的数据？</p>',
        onOk: () => {
          delSensitiveWordByIds(arr)
          // this.$http.delete('/api/system/cscpSensitiveWords/delAll?ids=' + arr)
            .then(res => {
              this.$Message.success('删除成功')
              this.selectedArr = []
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
   @import "~@/views/admin/styles/formStyle.less";
</style>
