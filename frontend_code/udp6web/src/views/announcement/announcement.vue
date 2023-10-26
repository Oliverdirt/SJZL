<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea" >
          <Form ref="formInline" :model="formInline" :label-width="80" >
            <Row :gutter="48">
              <Col span="8">
                <FormItem label="公告标题"  prop="noticeTitle">
                  <Input   type="text" :maxlength="100" v-model.trim="formInline.noticeTitle" placeholder="请输入公告标题"></Input>
                </FormItem>
              </Col>
              <Col span="8">
                <FormItem label="发布人员" prop="createBy">
                  <Input   type="text" :maxlength="100" v-model.trim="formInline.createBy" placeholder="请输入发布人员"></Input>
                </FormItem>
              </Col>
              <Col span="8">
                <FormItem label="类型"  prop="noticeType">
                  <Select v-model="formInline.noticeType" placeholder="请选择">
                    <Option value="1" >通知</Option>
                    <Option value="2" >公告</Option>
                  </Select>
                </FormItem>
              </Col>
              <Col span="24" style="text-align: right;margin-top: 24px">
                <FormItem>
                  <Button  type="primary" @click="handleSearch(true)" style="margin-right: 8px">查询</Button>
                  <Button  type="default"  @click="handleReset('formInline')">重置</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="tableBtn">
          <Button  icon="md-add" type="primary"  @click="handleAdd()" >新增</Button>
          <Button  icon="ios-trash-outline" type="error"   @click="handleDeleteMultiple()" style="margin-left: 20px">删除</Button>
        </div>
        <Table border :columns="columns" :data="data" @on-selection-change="rowSelected($event)" style="">
          <template slot-scope="{ row, index }" slot="action">
            <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="show(row,index,true)">查看</Button>
            <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="show(row,index,false)">编辑</Button>
            <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="remove(row, index)">删除</Button>
          </template>
        </Table>
        <Page :total="pageData.total" :page-size="pageData.size" :current="pageData.page" show-elevator show-sizer show-total @on-page-size-change="changePage($event, 'size')" @on-change="changePage($event, 'page')" style="margin-top: 20px;"/>
      </Card>
    </div>
    <Modal
      v-model="modal1"
      :title="title"
      ref="modal"
      @on-cancel="close"
      :width="800"
    >
      <Form  ref="detail" :model="detail" :label-width="80" :rules="ruleValidate" style="margin-top: 20px">
        <Row :gutter="50">
          <Col span="24">
            <FormItem label="公告标题"  prop="noticeTitle">
              <Input   type="text" :maxlength="50" v-model.trim="detail.noticeTitle" placeholder="" :disabled="flag"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="50">
          <Col span="12">
            <FormItem label="公告类型" prop="noticeType">
              <Select  v-model="detail.noticeType" placeholder="" :disabled="flag">
                <Option value="1" >通知</Option>
                <Option value="2" >公告</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="状态"  prop="activeFlag">
              <RadioGroup v-model="detail.status"   >
                <Radio label="0" :disabled="flag" >正常</Radio>
                <Radio label="1" :disabled="flag" >关闭</Radio>
              </RadioGroup>
            </FormItem>
          </Col>
        </Row>
        <FormItem label="内容"  style="">
          <Editor :textcontent="detail" ref="detailFn" v-if="modal1" :flagFn="flagFn"></Editor>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button  type="primary"  @click="ok(okFlag)"  style="width: 80px">确定</Button>
        <Button v-if="!flag" type="default"  @click="close"  style="width: 80px">取消</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
// import 'quill/dist/quill.core.css'
// import 'quill/dist/quill.snow.css'
// import 'quill/dist/quill.bubble.css'
// import { quillEditor } from 'vue-quill-editor'
import Editor from './components/editor.vue'
import {
  getNoticeListByParams,
  delNoticeListByIds,
  delNoticeListByNoticeId,
  getNoticeListByNoticeId,
  addNoticeByData,
  updateNoticeByData,
  getNoticeListByParams2
} from '@/api/admin/notice'
export default {
  name: 'announcement',
  components: {
    Editor
  },
  data () {
    return {
      flagFn: '',
      modal1: false,
      editorOption: {
        placeholder: '请输入文本...',
        modules: {
          toolbar: '#toolbar'
        }
      },
      formInline: {},
      columns: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          type: 'index',
          title: '序号',
          width: 80,
          align: 'center'
        },
        {
          title: '公告标题',
          align: 'center',
          key: 'noticeTitle'
        },
        {
          title: '公告类型',
          align: 'center',
          key: 'noticeType',
          render: (h, e) => {
            let text = ''
            if (e.row.noticeType === '1') {
              text = '通知'
            } else text = '公告'
            return h('div', [
              h('span', text)
            ])
          }
        },
        {
          title: '状态',
          align: 'center',
          key: 'status',
          render: (h, e) => {
            let text = ''
            if (e.row.status === '0') {
              text = '正常'
            } else text = '关闭'
            return h('div', [
              h('span', text)
            ])
          }
        },
        {
          title: '发布人员',
          align: 'center',
          key: 'createBy'
        },
        {
          title: '创建时间',
          key: 'createTime',
          align: 'center',
          render: (h, e) => {
            return h('div', [
              h('span', this.$moment(new Date(e.row.createTime)).format('yyyy-MM-DD HH:mm'))
            ])
          }
        },
        {
          title: '操作',
          slot: 'action',
          width: 200,
          align: 'center'
        }
      ],
      selectedArr: [],
      data: [
      ],
      pageData: {
        total: 0,
        size: 10,
        page: 1
      },
      detail: {},
      flag: false,
      modalValid: true,
      okFlag: '',
      ruleValidate: {
        noticeTitle: [
          { required: true, message: '请输入标题', trigger: 'blur' }
        ],
        noticeType: [
          { required: true, message: '请选择消息类型', trigger: 'blur' }
        ]
      }
    }
  },
  mounted () {
    this.queryList()
    this.handleSearch()
  },
  computed: {
    // editor () {
    //   return this.$refs.myQuillEditor.quill
    // },
    title: function () {
      if (this.okFlag === 'add') {
        return '新增'
      } else {
        if (this.flag) {
          return '查看'
        } else return '编辑'
      }
    }
  },
  methods: {
    // onEditorBlur (quill) {
    //   console.log('editor blur!', quill)
    // },
    // onEditorFocus (quill) {
    //   console.log('editor focus!', quill)
    // },
    // onEditorReady (quill) {
    //   console.log('editor ready!', quill)
    // },
    // onEditorChange ({ quill, html, text }) {
    //   console.log('editor change!', quill, html, text)
    //   this.content = html
    // },
    close () {
      this.modal1 = false
      this.$refs['detail'].resetFields()
    },
    handleSearch (inquire) {
      if (inquire) {
        this.pageData.page = 1
      }
      let url = '?'
      let arr = []
      for (let key in this.formInline) {
        arr.push(key + '=' + (this.formInline[key] === undefined ? '' : this.formInline[key]))
      }
      arr.push('page=' + (this.pageData.page - 1))
      arr.push('size=' + this.pageData.size)
      url += arr.join('&')
      getNoticeListByParams(url)
      // this.$http.get('/api/cscpNotice/getList' + url)
        .then(res => {
          this.data = res.data.data
          this.pageData.total = res.data.recordsTotal
        }).catch(() => {
          this.$message.error('列表查询失败')
        })
    },
    handleDeleteMultiple () {
      let arr = this.selectedArr.map(item => item.noticeId)
      if (arr.length < 1) {
        return this.$Message.error('删除失败,当前选择列表为空！')
      }
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除勾选的数据？</p>',
        onOk: () => {
          delNoticeListByIds(arr)
          // this.$http.delete('/api/cscpNotices/batchDel?ids=' + arr)
            .then(res => {
              this.$message.success('删除成功')
              // this.queryList()
              this.handleSearch()
            }).catch(error => {
              if (error.response) {
                this.$Message.error(error.response.data.title ? error.response.data.title : '删除失败！')
              }
            })
        },
        onCancel: () => {}
      })
    },
    handleAdd () {
      this.flagFn = '2'
      this.detail = {}
      this.detail.status = '0'
      this.flag = false
      this.okFlag = 'add'
      this.modal1 = true
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
          delNoticeListByNoticeId(row.noticeId)
          // this.$http.delete('/api/cscpNotices/' + row.noticeId)
            .then(res => {
              this.$message.success('删除成功')
              // this.queryList()
              this.handleSearch()
            }).catch(error => {
              if (error.response) {
                this.$Message.error(error.response.data.title ? error.response.data.title : '删除失败！')
              }
            })
        },
        onCancel: () => {}
      })
    },
    show (row, index, flag) {
      if (flag) {
        this.flagFn = '1'
      } else {
        this.flagFn = '2'
      }
      getNoticeListByNoticeId(row.noticeId)
      // this.$http.get('/api/cscpNotices/' + row.noticeId)
        .then(res => {
          console.log(res, 'res11')
          this.detail = res.data
          this.flag = flag
          this.okFlag = 'update'
          this.modal1 = true
        }).catch(() => {
          this.$message.error('行数据查询失败')
        })
    },
    ok (type) {
      if (this.flag) {
        this.modal1 = false
        return
      }
      this.$delete(this.detail, '_index')
      this.$delete(this.detail, '_rowKey')
      // if (this.detail.noticeContent && this.detail.noticeContent.length > 30000) {
      //   this.$Message.warning('公告内容长度不能超过5000')
      //   return
      // }

      let copyDetail = JSON.parse(JSON.stringify(this.detail))
      console.log(copyDetail, 'copyDetail')
      this.$refs['detail'].validate((valid) => {
        if (valid) {
          if (type === 'update') {
            updateNoticeByData(copyDetail)
            // this.$http.put('/api/cscpNotices', copyDetail)
              .then(res => {
                this.$Message.success('修改成功')
                this.modal1 = false
                this.queryList()
              }).catch(() => {
                this.$Message.error('修改失败')
              })
          } else if (type === 'add') {
            addNoticeByData(copyDetail)
            // this.$http.post('/api/cscpNotices', copyDetail)
              .then(res => {
                this.$Message.success('新增成功')
                this.modal1 = false
                this.queryList()
              }).catch(() => {
                this.$Message.error('新增失败')
              })
          }
        } else {
          this.modal1 = true
        }
      })
    },
    queryList () {
      getNoticeListByParams2({
        page: this.pageData.page - 1,
        size: this.pageData.size
      })
      // this.$http.get('/api/cscpNotice/getList', {
      //   params: {
      //     page: this.pageData.page - 1,
      //     size: this.pageData.size
      //   }
      // })
        .then(res => {
          this.data = res.data.data
          this.pageData.total = res.data.recordsTotal
        }).catch(() => {
          this.$message.error('列表查询失败')
        })
    },
    changePage (e, type) {
      if (type === 'size') {
        this.pageData.size = e
      } else if (type === 'page') {
        this.pageData.page = e
      }
      this.queryList()
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
