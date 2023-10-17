<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea" >
          <Form ref="queryForm" :model="queryForm" :label-width="80" >
            <Row :gutter="40">
              <Col span="8" style="margin-bottom: 12px">
                <FormItem label="任务组名"  prop="jobGroup" style="width: 100%">
                  <Select v-model="queryForm.jobGroup" placeholder="请选择任务组名" clearable style="">
                    <Option v-for="item in jobGroups" :value="item.itemCode.toLocaleString()"
                            :key="item.itemCode.toLocaleString()">{{ item.itemValue }}
                    </Option>
                  </Select>
                </FormItem>
            </Col>
              <Col span="8" style="margin-bottom: 12px">
                <FormItem label="执行状态"  prop="status" style="width: 100%" >
                  <Select v-model="queryForm.status" placeholder="请选择" clearable>
                    <Option value="1" >正常</Option>
                    <Option value="0" >失败</Option>
                  </Select>
                </FormItem>
            </Col>
              <Col span="8" style="margin-bottom: 12px">
                <FormItem label="执行时间"  prop="executionTime" style="width: 100%">
                  <DatePicker type="datetimerange" placement="bottom-end" v-model.trim="queryForm.executionTime"
                    placeholder="请选择日期" style="width: 100%" format="yyyy-MM-dd HH:mm:ss"></DatePicker>
                </FormItem>
            </Col>
              <Col span="8" style="margin-bottom: 12px">
                <FormItem label="任务名称"  prop="jobName" style="width: 100%">
                  <Input  type="text" :maxlength="100" v-model.trim="queryForm.jobName" placeholder="请输入任务名称" style="width: 100%"></Input>
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
<!--          <Button  icon="md-add"  type="primary"  @click="handleAdd()">新增</Button>-->
<!--          <Button  icon="ios-trash-outline" type="error"  @click="handleDeleteMultiple()" style="margin-left: 20px">删除</Button>-->
        </div>
        <Table border :columns="columns" :data="data" @on-selection-change="rowSelected($event)" style="">
          <template slot-scope="{ row, index }" slot="action">
            <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="handleEdit(row,index)">查看</Button>
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
      <Form ref="detail" :model="detail" :label-width="115" :rules="ruleValidate" style="margin-top: 20px">
        <FormItem label="日志编号"  prop="jobLogId">
          <Input   type="text" :maxlength="100" v-model.trim="detail.jobLogId" placeholder="" disabled></Input>
        </FormItem>
        <FormItem label="任务名称"  prop="jobName">
          <Input   type="text" :maxlength="100" v-model.trim="detail.jobName" placeholder="" disabled></Input>
        </FormItem>
        <!-- <FormItem label="任务组名" prop="jobGroup">
          <InputNumber type="text" :maxlength="100" v-model="detail.jobGroup"  placeholder="" disabled></InputNumber>
        </FormItem> -->
        <FormItem label="任务组名"  prop="jobGroup" style="width: 100%">
          <Select v-model="detail.jobGroup" placeholder="请选择任务组名" clearable disabled style="">
            <Option v-for="item in jobGroups" :value="item.itemCode.toLocaleString()"
                    :key="item.itemCode.toLocaleString()" disabled>{{ item.itemValue }}
            </Option>
          </Select>
        </FormItem>
        <FormItem label="调用目标字符串" prop="invokeTarget">
          <Input   type="text" :maxlength="100" v-model.trim="detail.invokeTarget" placeholder="" disabled></Input>
        </FormItem>
        <FormItem label="日志信息"  prop="jobMessage">
          <Input   type="text" :maxlength="100" v-model.trim="detail.jobMessage" placeholder="" disabled></Input>
        </FormItem>
        <FormItem label="执行状态"  prop="status" style="width: 100%" >
          <Select v-model="detail.status" placeholder="请选择" clearable disabled>
            <Option value="1">正常</Option>
            <Option value="0">失败</Option>
          </Select>
        </FormItem>
        <FormItem label="执行时间"  prop="executionTime">
          <Input   type="text" :maxlength="100" v-model.trim="detail.executionTime" placeholder="" disabled></Input>
        </FormItem>
        <FormItem label="异常信息"  prop="exceptionInfo" v-if="detail.status === '0'">
          <Input   type="textarea" :autosize="{minRows: 2,maxRows: 10}" v-model.trim="detail.exceptionInfo" placeholder="" disabled></Input>
        </FormItem>
      </Form>
      <div slot="footer">
        <!-- <Button type="primary"  @click="ok(okFlag)" :disabled="disabled"  style="width: 80px">确定</Button> -->
        <Button type="primary"  @click="close()"  style="width: 80px">确定</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import {
  getDicItemsbyParams
} from '@/api/dic/listdic'
import {
  getJoblogById,
  delJoblogById,
  addJoblogByData,
  updateJoblogByData,
  getJoblogByParams,
  delQuartzByIds
} from '@/api/quartz/joblog'
export default {
  name: 'sysJobLogManager',
  data () {
    return {
      modal: false,
      disabled: false,
      selectedArr: [],
      queryForm: {},
      jobGroups: [],
      detail: {
        jobGroups: []
      },
      pageData: {
        total: 0,
        size: 10,
        page: 1
      },
      columns: [
        // {
        //   type: 'selection',
        //   width: 60,
        //   align: 'center'
        // },
        // {
        //   type: 'index',
        //   align: 'center',
        //   title: '序号',
        //   width: 80
        // },
        // {
        //   title: '日志编号',
        //   key: 'jobLogId'
        // },
        {
          title: '任务名称',
          key: 'jobName'
        },
        {
          title: '任务组名',
          key: 'jobGroup',
          render: (h, params) => {
            // return h('span', params.row.jobGroup === '1' ? '系统' : '默认'  )
            return h('span', this.translate(params.row))
          }
        },
        {
          title: '调用目标字符串',
          key: 'invokeTarget'
        },
        {
          title: '日志信息',
          key: 'jobMessage'
        },
        {
          title: '执行状态',
          key: 'status',
          render: (h, params) => {
            if (params.row.status === '1') {
              return h('span', '正常')
            } else if (params.row.status === '0') {
              return h('span', '失败')
            }
          }
        },
        {
          title: '执行时间',
          key: 'executionTime'
        },
        {
          title: '操作',
          slot: 'action',
          align: 'center'
        }
      ],
      data: [
      ],
      okFlag: '',
      modalValid: true,
      ruleValidate: {
      }
    }
  },
  mounted () {
    this.getJobGroup()
    this.queryList()
  },
  computed: {
    title: function () {
      if (this.okFlag === 'add') {
        return '新增'
      } else {
        return '查看'
      }
    }
  },
  methods: {
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
      getJoblogById(row.jobLogId)
      // this.$http.get('/api/sysJobLogs/' + row.jobLogId)
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
          delJoblogById(row.jobLogId)
          // this.$http.delete('/api/sysJobLogs/' + row.jobLogId)
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

      this.$refs['detail'].validate((valid) => {
        if (valid) {
          if (type === 'update') {
            updateJoblogByData(copyDetail)
            // this.$http.put('/api/sysJobLogs', copyDetail)
              .then(res => {
                this.$Message.success('修改成功')
                this.modal = false
                this.queryList()
                this.$refs['detail'].resetFields()
              }).catch(() => {
                this.$Message.error('修改失败')
              })
          } else if (type === 'add') {
            addJoblogByData(copyDetail)
            // this.$http.post('/api/sysJobLogs', copyDetail)
              .then(res => {
                this.$Message.success('新增成功')
                this.modal = false
                this.$refs['queryForm'].resetFields()
                this.queryList()
                this.$refs['detail'].resetFields()
              }).catch(() => {
                this.$Message.error('新增失败')
              })
          }
        } else {
          this.modal = true
        }
      })
    },
    // 获取任务组名数据字典值
    getJobGroup () {
      // const [url, httpConfig] = [
      //   '/api/dic/cscpHxDicItems/getItems',
      //   {
      //     params: {
      //       'dicAttr.equals': 'list',
      //       'dicCode.equals': 'taskName'
      //     }
      //   }
      // ]
      const dicQueryData = {
        'dicAttr.equals': 'list',
        'dicCode.equals': 'taskName'
      }
      getDicItemsbyParams(dicQueryData)
      // this.$http.get(url, httpConfig)
        .then(response => {
          this.jobGroups = response.data.data
          this.detail.jobGroups = response.data.data
        })
    },
    translate (row) {
      let realGroupName
      for (let i = 0; i < this.jobGroups.length; i++) {
        if (row.jobGroup === this.jobGroups[i].itemCode.toLocaleString()) {
          realGroupName = this.jobGroups[i].itemValue.toLocaleString()
        }
      }
      return realGroupName
    },
    queryList (flag) {
      if (flag) {
        this.pageData.page = 1
      }
      let obj = {}
      obj['jobGroup.equals'] = this.queryForm['jobGroup'] ? this.queryForm['jobGroup'] : null
      obj['status.equals'] = this.queryForm['status'] ? this.queryForm['status'] : null
      obj['executionTime.greaterOrEqualThan'] = this.queryForm['executionTime'][0] ? this.$moment(new Date(this.queryForm['executionTime'][0])).format('yyyy-MM-DD HH:mm:ss') : null
      obj['executionTime.lessOrEqualThan'] = this.queryForm['executionTime'][1] ? this.$moment(new Date(this.queryForm['executionTime'][1])).format('yyyy-MM-DD HH:mm:ss') : null
      obj['jobName.contains'] = this.queryForm['jobName'] ? this.queryForm['jobName'] : null
      let params = Object.assign(obj, {
        page: this.pageData.page - 1,
        size: this.pageData.size
      })
      getJoblogByParams(params)
      // this.$http.get('/api/sysJobLogs/', {
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
      let arr = this.selectedArr.map(item => item.jobLogId)
      if (arr.length < 1) {
        return
      }
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除勾选的数据？</p>',
        onOk: () => {
          delQuartzByIds(arr)
          // this.$http.delete('/api/sysJobLogs/delAll?jobLogIds=' + arr)
            .then(res => {
              this.$Message.success('删除成功')
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
