<template>
    <div style="height: 100%">
      <div class="formCard-content">
        <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
          <div class="searchArea" >
            <Form ref="formInline" :model="formInline" :label-width="80" >
              <Row :gutter="48">
                <Col span="8">
                  <FormItem label="岗位编码"  prop="postCode" style="width: 100%">
                    <Input   type="text" :maxlength="100" v-model.trim="formInline.postCode" placeholder="请输入岗位编码" style="width: 100%"></Input>
                  </FormItem>
                  </Col>
                  <Col span="8">
                  <FormItem label="岗位名称"  prop="postName" style="width: 100%">
                    <Input   type="text" :maxlength="100" v-model.trim="formInline.postName" placeholder="请输入岗位名称" style="width: 100%"></Input>
                  </FormItem>
                  </Col>
                  <!-- <Col span="8">
                  <FormItem label="岗位状态"  prop="status">
                    <Select v-model="formInline.postStatus" placeholder="请选择">
                      <Option value="0" >开启</Option>
                      <Option value="1" >关闭</Option>
                    </Select>
                  </FormItem>
                </Col> -->
                <Col span="24" style="text-align: right;">
                  <FormItem :label-width="0">
                    <Button   type="primary" @click="queryList(true)" style="margin-right: 8px">查询</Button>
                    <Button type="default"  @click="handleReset('formInline')">重置</Button>
                  </FormItem>
                </Col>
              </Row>
            </Form>
          </div>
        </Card>
        <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
          <div class="tableBtn">
            <Button  icon="md-add" type="primary"  @click="handleAdd()" style="margin-right:10px">新增</Button>
  <!--          <Button @click="handleExport" type="default" style="" >导出</Button>-->
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
        :width="580"
      >
        <Form  ref="detail" :model="detail" :label-width="80" :rules="ruleValidate" style="margin-top: 20px">
          <Row :gutter="50">
            <Col span="24">
              <FormItem label="岗位编码"  prop="postCode">
                <Input   type="text" :maxlength="50" v-model="detail.postCode" placeholder="" :disabled="flag"></Input>
              </FormItem>
            </Col>
          </Row>
          <Row :gutter="50">
            <Col span="24">
              <FormItem label="岗位名称"  prop="postName">
                <Input   type="text" :maxlength="100" v-model="detail.postName" placeholder="" :disabled="flag"></Input>
              </FormItem>
            </Col>
          </Row>
          <Row :gutter="50">
            <Col span="24">
              <FormItem label="显示排序" prop="postSort">
                <InputNumber v-model="detail.postSort" :min="0" :max="1000000" :precision="0" placeholder="" :disabled="flag" style="width: 100%"></InputNumber>
              </FormItem>
            </Col>
          </Row>
          <!-- <Row :gutter="50">
            <Col span="24">
              <FormItem label="状态"  prop="activeFlag">
                <RadioGroup v-model="detail.status"   >
                  <Radio label="0" :disabled="flag" >正常</Radio>
                  <Radio label="1" :disabled="flag" >关闭</Radio>
                </RadioGroup>
              </FormItem>
            </Col>
          </Row> -->
          <Row :gutter="50">
            <Col span="24">
              <FormItem label="备注"  prop="remark">
                <textarea  type="text" :maxlength="200" v-model="detail.remark" placeholder="" :disabled="flag" style="width: 100%"></textarea>
              </FormItem>
            </Col>
          </Row>
        </Form>
        <div slot="footer">
          <Button type="primary"  @click="ok(okFlag)"  style="width: 80px">确定</Button>
          <Button v-if="!flag" type="default"  @click="close"  style="width: 80px">取消</Button>
        </div>
      </Modal>
    </div>
  </template>
  
  <script>
  import {
    validPostCodeByData,
    validPostNameByData,
    getPostByParams2,
    getPostByParams3,
    delPostByPostId,
    exportPosts,
    getPostByPostId,
    updateOrAddPostByData
  } from '@/api/admin/post'
  export default {
    name: 'postList1',
    data () {
      const validSort = (rule, value, callback) => {
        setTimeout(() => {
          if (!this.detail.postSort) {
            callback(new Error('请输入数字显示排序'))
          } else {
            callback()
          }
        }, 0)
      }
      return {
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
            title: '岗位编码',
            key: 'postCode',
            align: 'center'
          },
          {
            title: '岗位名称',
            key: 'postName',
            align: 'center'
          },
          {
            title: '显示排序',
            key: 'postSort',
            align: 'center'
          },
          // {
          //   title: '状态',
          //   align: 'center',
          //   key: 'status',
          //   render: (h, e) => {
          //     let text = ''
          //     if (e.row.status === '0') {
          //       text = '开启'
          //     } else text = '关闭'
          //     return h('div', [
          //       h('span', text)
          //     ])
          //   }
          // },
          {
            title: '创建时间',
            key: 'createTime',
            align: 'center',
            render: (h, e) => {
              return h('div', [
                h('span', this.$moment(new Date(e.row.createTime)).format('yyyy-MM-DD HH:mm:ss'))
              ])
            }
          },
          {
            title: '操作',
            slot: 'action',
            width: 300,
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
          postCode: [
            { pattern: /^[\u4e00-\u9fa5a-zA-Z]+$/, message: '仅支持中文，英文字母', required: true },
            { max: 20, message: '编码不可以超过20个字符' },
            { trigger: 'blur' }
          ],
          postName: [
            { required: true, message: '请输入岗位名称' },
            { max: 20, message: '名称长度不能超过20个字符' },
            { trigger: 'blur' }
          ],
          postSort: [
            { required: true, validator: validSort, trigger: 'blur' }
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
          if (this.flag) {
            return '查看'
          } else return '编辑'
        }
      }
    },
    methods: {
      close () {
        this.modal1 = false
        this.$refs['detail'].resetFields()
        this.disabled = false
      },
      handleAdd () {
        this.detail = {
          postCode: undefined,
          postSort: undefined,
          postName: undefined
  
        }
        if (this.$refs.detail !== undefined) this.$refs.detail.resetFields()
        this.flag = false
        this.okFlag = 'add'
        this.detail.postSort = 1
        this.modal1 = true
      },
      handleReset (name) {
        this.$refs[name].resetFields()
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
            delPostByPostId(row.postId)
            // this.$http.delete('/api/cscpPosts/' + row.postId)
              .then(res => {
                if (res.data.code == 200) {
                  this.$Message.success('删除成功')
                  this.queryList()
                } else {
                  if (res.data.msg) {
                    this.$Message.error(res.data.msg)
                  } else {
                    this.$Message.error('删除失败')
                  }
                }
              }).catch({
              })
          },
          onCancel: () => {}
        })
      },
      show (row, index, flag) {
        getPostByPostId(row.postId)
        // this.$http.get('/api/cscpPosts/' + row.postId)
          .then(res => {
            this.detail = res.data.data
            this.flag = flag
            this.okFlag = 'update'
            this.modal1 = true
          }).catch(() => {
            this.$message.error('行数据查询失败')
          })
      },
      ok (type) {
        if (this.flag) {
          this.$refs['detail'].resetFields()
          this.modal1 = false
          return
        }
        this.$delete(this.detail, '_index')
        this.$delete(this.detail, '_rowKey')
        if (this.detail.remark && this.detail.remark.length > 5000) {
          this.$Message.warning('公告内容长度不能超过5000')
          return
        }
        let copyDetail = JSON.parse(JSON.stringify(this.detail))
        this.$refs['detail'].validate((valid) => {
          if (valid) {
            if (type === 'update') {
              updateOrAddPostByData('put', copyDetail)
              // this.$http.put('/api/cscpPosts', copyDetail)
                .then(res => {
                  if (res.data.code == 200) {
                    this.$Message.success('修改成功')
                    this.modal1 = false
                    this.queryList()
                  } else {
                    this.$Message.error({
                      content: res.data.msg
                    })
                  }
                }).catch(() => {
                })
            } else if (type === 'add') {
              updateOrAddPostByData('post', copyDetail)
              // this.$http.post('/api/cscpPosts', copyDetail)
                .then(res => {
                  if (res.data.code == 200) {
                    this.$Message.success('新增成功')
                    this.modal1 = false
                    this.queryList()
                  } else {
                    this.$Message.error({
                      content: res.data.msg
                    })
                  }
                }).catch(() => {
                })
            }
          } else {
            this.modal1 = true
          }
        })
      },
      queryList (inquire) {
        if (inquire) {
          this.pageData.page = 1
        }
        let obj = {}
        obj['postCode'] = this.formInline['postCode'] ? this.formInline['postCode'] : null
        obj['postName'] = this.formInline['postName'] ? this.formInline['postName'] : null
        let params = Object.assign(obj, {
          page: this.pageData.page - 1,
          size: this.pageData.size
        })
        getPostByParams3(params)
        // this.$http.get('/api/cscpPost/getList', {
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
   @import "~@/views/admin/styles/formStyle.less";
  ::v-deep .ivu-table-cell{
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  </style>
  