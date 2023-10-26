<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea" >
          <Form ref="queryForm" :model="queryForm" :label-width="80" >
            <Row :gutter="40">
              <Col span="8" style="margin-bottom: 12px">
                <FormItem label="参数名称"  prop="configName" style="width: 100%">
                  <Input  type="text" :maxlength="100" v-model.trim="queryForm.configName" placeholder="请输入参数名称" style="width: 100%"></Input>
                </FormItem>
            </Col>
              <Col span="8" style="margin-bottom: 12px">
                <FormItem label="参数键名"  prop="configKey" style="width: 100%">
                  <Input  type="text" :maxlength="100" v-model.trim="queryForm.configKey" placeholder="请输入参数键名" style="width: 100%"></Input>
                </FormItem>
            </Col>
            <Col span="8" style="margin-bottom: 12px">
                <FormItem label="是否启用"  prop="configStatus" style="width: 100%">
                  <Select v-model="queryForm.configStatus" placeholder="请选择">
                    <Option value="1" >是</Option>
                    <Option value="0" >否</Option>
                  </Select>
                </FormItem>
            </Col>
              <Col span="8" style="margin-bottom: 12px">
                <FormItem label="创建时间"  prop="createTime" style="width: 100%">
                  <DatePicker type="datetimerange" placement="bottom-end" v-model.trim="queryForm.createTime"
                    placeholder="请选择日期" style="width: 100%" format="yyyy-MM-dd HH:mm:ss"></DatePicker>
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
        </div>
        <Table border :columns="columns" :data="data" @on-selection-change="rowSelected($event)" style="">
          <template slot-scope="{ row, index }" slot="action">
            <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="handleEdit(row,index)">编辑</Button>
            <Button  type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="remove(row, index)">删除</Button>
          </template>
        </Table>
        <Page :total="pageData.total" :page-size="pageData.size" :current="pageData.page" show-elevator show-sizer show-total @on-page-size-change="changePage($event, 'size')" @on-change="changePage($event, 'page')" style="margin-top: 20px;"/>
      </Card>
    </div>
    <Modal
      v-model="modal"
      :title="title"
      ref="modal"
      @on-cancel="close"
      :width="580"
    >
      <Form ref="detail" :model="detail" :label-width="80" :rules="ruleValidate" style="margin-top: 20px">
        <FormItem label="参数名称"  prop="configName">
            <Input type="text" :maxlength="20" v-model="detail.configName" placeholder="请输入参数名称" ></Input>
        </FormItem>
        <FormItem label="参数键名"  prop="configKey">
            <Input type="text" :maxlength="100" v-model="detail.configKey" placeholder="请输入参数键名" ></Input>
        </FormItem>
        <FormItem label="参数键值"  prop="configValue">
            <Input type="text" :maxlength="100" v-model="detail.configValue" placeholder="请输入参数键值" ></Input>
        </FormItem>
        <FormItem label="是否启用"  prop="configStatus">
          <RadioGroup v-model="detail.configStatus">
              <Radio :label="1">是</Radio>
              <Radio :label="0">否</Radio>
          </RadioGroup>
       </FormItem>
        <FormItem label="备注"  prop="remark">
            <Input type="textarea" :maxlength="500" :autosize="{minRows: 2,maxRows: 5}" v-model.trim="detail.remark" placeholder="请输入备注" ></Input>
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
  listSysconfig,
  querySysconfigById,
  delSysconfigById,
  delSysconfigByIds,
  updateSysconfigByData,
  addSysconfigByData
} from '@/api/sysconfig/sysconfig'
export default {
  name: 'SysConfigManager',
  data () {
    return {
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
          title: '参数名称',
          key: 'configName'
        },
        {
          title: '参数键名',
          key: 'configKey'
        },
        {
          title: '参数键值',
          key: 'configValue'
        },
        {
          title: '是否启用',
          key: 'configStatus',
          // render: (h, params) => {
          // if (params.row.configStatus == 1) {
          //   return h('span', '是')
          // } else if (params.row.configStatus == 0) {
          //   return h('span', '否')
          // }
          // }
          render: (h, params) => {
            return h('i-switch', {
              props: {
                type: 'primary',
                value: params.row.configStatus === 1
              },
              style: {
                marginRight: '5px'
              },
              on: {
                'on-change': (value) => {
                  this.changeStatus(params.row, value)
                }
              }
            })
          }
        },
        {
          title: '创建时间',
          key: 'createTime'
        },
        // {
        //   title: '备注',
        //   key: 'remark'
        // },
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
        configName: [
          { required: true, message: '参数名称不能为空', trigger: 'blur' },
          { type: 'string', max: 20, message: '参数名称长度不可大于20', trigger: 'blur' },
          { validator: (rule, value, callback) => {
            if (!/^[\u4e00-\u9fa5]+([a-zA-Z0-9]?)+$/.test(value)) {
              callback(new Error('参数名称必须以中文开头，后可跟字母、数字！！！'))
            } else {
              callback()
            }
          },
          trigger: 'blur' }
        ],
        configKey: [
          { required: true, message: '参数键名不能为空', trigger: 'blur' },
          { type: 'string', max: 100, message: '参数键名长度不可大于100', trigger: 'blur' },
          { validator: (rule, value, callback) => {
            if (!/^[a-zA-Z][a-zA-Z0-9._-]+$/.test(value)) {
              callback(new Error('以字母开头，后边可跟字母、数字、下划线、短中线和小数点'))
            } else {
              callback()
            }
          },
          trigger: 'blur' }
        ],
        configValue: [
          { required: true, message: '参数键值不能为空', trigger: 'blur' },
          { type: 'string', max: 100, message: '参数键值长度不可大于100', trigger: 'blur' },
          { validator: (rule, value, callback) => {
            if (!/^[a-zA-Z][a-zA-Z0-9._-]+$/.test(value)) {
              callback(new Error('以字母开头，后边可跟字母、数字、下划线、短中线和小数点'))
            } else {
              callback()
            }
          },
          trigger: 'blur' }
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
    changeStatus (row, flag) {
      console.log('+++++++++++++', row, flag)
      row.configStatus = flag ? 1 : 0
      let copyDetail = JSON.parse(JSON.stringify(row))
      updateSysconfigByData(copyDetail)
      // this.$http.put('/api/cscpSysConfigs', copyDetail)
        .then(res => {
          if (res.data.code !== 200) {
            this.$Message.error(res.data.msg)
          } else {
            this.$Message.success('状态修改成功')
          }
        }).catch(() => {
          this.$Message.error('状态修改失败')
        })
    },
    close () {
      this.$refs['detail'].resetFields()
      this.modal = false
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
      querySysconfigById(row.configId)
      // this.$http.get('/api/cscpSysConfigs/' + row.configId)
        .then(res => {
          if (res.data.code !== 200) {
            this.$Message.error('更新失败!' + res.data.msg)
          } else {
            this.modal = true
            this.detail = res.data.data
            console.log('>>>>>>>>>>>>>>>>>>>>>>>>', this.detail)
            this.okFlag = 'update'
          }
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
          delSysconfigById(row.configId)
          // this.$http.delete('/api/cscpSysConfigs/' + row.configId)
            .then(res => {
              if (res.data.code !== 200) {
                this.$Message.error(res.data.msg)
              } else {
                this.$Message.success('删除成功')
                this.queryList()
              }
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

      this.$refs['detail'].validate((valid) => {
        if (valid) {
          if (type === 'update') {
            updateSysconfigByData(copyDetail)
            // this.$http.put('/api/cscpSysConfigs', copyDetail)
              .then(res => {
                if (res.data.code !== 200) {
                  this.$Message.error(res.data.msg)
                } else {
                  this.$Message.success('修改成功')
                  this.modal = false
                  this.queryList()
                  this.$refs['detail'].resetFields()
                }
              }).catch(() => {
                this.$Message.error('修改失败')
              })
          } else if (type === 'add') {
            addSysconfigByData(copyDetail)
            // this.$http.post('/api/cscpSysConfigs', copyDetail)
              .then(res => {
                if (res.data.code !== 200) {
                  this.$Message.error('新增失败!' + res.data.msg)
                } else {
                  this.$Message.success('新增成功')
                  this.modal = false
                  this.$refs['queryForm'].resetFields()
                  this.queryList()
                  this.$refs['detail'].resetFields()
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
      obj['configName.contains'] = this.queryForm['configName'] ? this.queryForm['configName'] : null
      obj['configKey.contains'] = this.queryForm['configKey'] ? this.queryForm['configKey'] : null
      obj['configStatus.equals'] = this.queryForm['configStatus'] ? this.queryForm['configStatus'] : null
      obj['createTime.greaterOrEqualThan'] = this.queryForm['createTime'][0] ? this.$moment(new Date(this.queryForm['createTime'][0])).format('yyyy-MM-DD HH:mm:ss') : null
      obj['createTime.lessOrEqualThan'] = this.queryForm['createTime'][1] ? this.$moment(new Date(this.queryForm['createTime'][1])).format('yyyy-MM-DD HH:mm:ss') : null
      let params = Object.assign(obj, {
        page: this.pageData.page - 1,
        size: this.pageData.size
      })
      listSysconfig(params)
      // this.$http.get('/api/cscpSysConfigs/', {
      //   params: params
      // })
        .then(res => {
          if (res.data.code !== 200) {
            this.$Message.error('新增失败!' + res.data.msg)
          } else {
            this.data = res.data.data.data
            this.pageData.total = res.data.data.recordsTotal
          }
        }).catch(() => {
          this.$Message.error('列表查询失败')
        })
    },
    handleDeleteMultiple: function () {
      let arr = this.selectedArr.map(item => item.configId)
      if (arr.length < 1) {
        return this.$Message.error('删除失败,当前选择列表为空！')
      }
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除勾选的数据？</p>',
        onOk: () => {
          delSysconfigByIds(arr)
          // this.$http.delete('/api/cscpSysConfigs/delAll?configIds=' + arr)
            .then(res => {
              if (res.data.code !== 200) {
                this.$Message.error('新增失败!' + res.data.msg)
              } else {
                this.$Message.success('删除成功')
                this.selectedArr = []
                this.queryList()
              }
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
