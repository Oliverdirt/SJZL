<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea" >
          <Form ref="queryForm" :model="queryForm" :label-width="80" >
            <Row :gutter="40">
              <Col span="8" style="margin-bottom: 12px">
                <FormItem label="任务名称"  prop="taskName" style="width: 100%">
                  <Input  type="text" :maxlength="100" v-model.trim="queryForm.taskName" placeholder="请输入任务名称" style="width: 100%"></Input>
                </FormItem>
            </Col>
              <Col span="8" style="margin-bottom: 12px">
                <FormItem label="任务状态"  prop="taskStatus" style="width: 100%">
                  <Select v-model.trim="queryForm.taskStatus" placeholder="请选择">
                    <Option value=1>初始化</Option>
                    <Option value=2>导入中</Option>
                    <Option value=3>成功</Option>
                    <Option value=4>失败</Option>
                    <Option value=5>部分成功</Option>
                  </Select>
                  <!-- <Select v-model="queryForm.taskStatus" placeholder="请选择">
                    <Option v-for="item in taskStatusDicGroups" :value="item.itemCode"
                            :key="item.itemCode">{{ item.itemName }}
                    </Option>
                  </Select> -->
                </FormItem>
            </Col>
              <Col span="8" style="margin-bottom: 12px">
                <FormItem label="创建时间"  prop="createdTime" style="width: 100%">
                  <DatePicker type="datetimerange" placement="bottom-end" v-model.trim="queryForm.createdTime"
                    placeholder="请选择日期" style="width: 100%" format="yyyy-MM-dd HH:mm:ss"></DatePicker>
                </FormItem>
            </Col>
              <Col span="8" style="margin-bottom: 12px">
                <FormItem label="处理时间"  prop="dealTime" style="width: 100%">
                  <DatePicker type="daterange" placement="bottom-end" v-model.trim="queryForm.dealTime"
                    placeholder="请选择日期" style="width: 100%" format="yyyy-MM-dd"></DatePicker>
                </FormItem>
            </Col>
              <Col span="8" style="margin-bottom: 12px">
                <FormItem label="完成时间"  prop="finishTime" style="width: 100%">
                  <DatePicker type="daterange" placement="bottom-end" v-model.trim="queryForm.finishTime"
                    placeholder="请选择日期" style="width: 100%" format="yyyy-MM-dd"></DatePicker>
                </FormItem>
            </Col>
              <Col span="8" style="margin-bottom: 12px">
                <FormItem label="任务类型"  prop="taskType" style="width: 100%">
                 
                  <Select v-model="queryForm.taskType" placeholder="请选择">
                    <Option value=1>导入</Option>
                    <!-- <Option value=2>导出</Option> -->
                    <!-- <Option v-for="item in taskTypeDicGroups" :value="item.itemCode"
                            :key="item.itemName">{{ item.itemName }}
                    </Option> -->
                  </Select>
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
          <!-- <Button  icon="md-add"  type="primary"  @click="handleAdd()">新增</Button>
          <Button  icon="ios-trash-outline" type="error"  @click="handleDeleteMultiple()" style="margin-left: 20px">删除</Button> -->
        </div>
        <Table border :columns="columns" :data="data" @on-selection-change="rowSelected($event)" style="">
           <template slot-scope="{ row, index }" slot="action">
           <!-- <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="handleEdit(row,index)">编辑</Button>
            <Button  type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="remove(row, index)">删除</Button>-->
            <Button  type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="exportErrorMsg(row, index)">错误信息下载</Button>
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
    asyncImportExportTaskShow,
    asyncImportExportTaskDel,
    asyncImportExportTaskUpdate,
    asyncImportExportTaskCreate,
    asyncImportExportTaskParams,
    asyncImportExportTaskDelAll
} from '@/api/taskcenter/asyncOperateDataTask'
import {
    getCscpBasicHxItemCode
} from '@/api/dic/listdic'
export default {
  name: 'asyncImportExportTaskManager',
  data () {
    return {
      taskStatusDicGroups: [{"itemCode":1,"itemName":"初始化"},
                            {"itemCode":2,"itemName":"运行中"},
                            {"itemCode":3,"itemName":"成功"},
                            {"itemCode":4,"itemName":"失败"},
                            {"itemCode":5,"itemName":"部分成功"}],
      taskTypeDicGroups: [ {"itemCode":1,"itemName":"导入"}],
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
          title: '任务名称',
          key: 'taskName',
          width: 150
        },
        {
          title: '任务状态',
          key: 'taskStatus',
          width: 100,
          render: (h, params) => {
            return h('span', this.taskStatusTranslate(params.row))
          }
        },
        {
          title: '创建时间',
          key: 'createdTime',
          width: 150
        },
        {
          title: '处理时间',
          key: 'dealTime',
          width: 150
        },
        {
          title: '完成时间',
          key: 'finishTime',
          width: 150
        },
        {
          title: '处理结果',
          key: 'result',
          width: 100
        },
        {
          title: '任务类型',
          key: 'taskType',
          width: 100,
          render: (h, params) => {
            return h('span', this.taskTypeTranslate(params.row))
          }
        },
        {
          title: '操作',
          slot: 'action',
          width: 200,
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
      }
    }
  },
  mounted () {
    this.queryList()
    this.taskStatusGetDicGroups('asyncOperateDataTaskStatus')
    this.taskTypeGetDicGroups('asynOperateDataTaskType')
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
    taskStatusGetDicGroups(asyncOperateDataTaskStatus){
        getCscpBasicHxItemCode({
            'dictCode' : 'asyncOperateDataTaskStatus'
        })
         .then(response => {
            this.taskStatusDicGroups = response.data.data
         })
    },
    taskStatusTranslate(row) {
      if(!row.taskStatus){
        return row.taskStatus
      }
      let realGroupName
      for (let i = 0; i < this.taskStatusDicGroups.length; i++) {
        if (row.taskStatus === this.taskStatusDicGroups[i].itemCode) {
            realGroupName = this.taskStatusDicGroups[i].itemValue
        }
      }
      if(realGroupName){
        return realGroupName
      }else{
        return row.taskStatus == 1 ? '初始化' : row.taskStatus == 2 ? '导入中' : row.taskStatus == 3 ? '成功' : row.taskStatus == 4 ? '失败' : '部分成功';
      }
    },
    taskTypeGetDicGroups(asynOperateDataTaskType){
        getCscpBasicHxItemCode({
            'dictCode' : 'asynOperateDataTaskType'
        })
         .then(response => {
            this.taskTypeDicGroups = response.data.data
         })
    },
    taskTypeTranslate(row) {
      if(!row.taskType){
        return row.taskType
      }
      let realGroupName
      for (let i = 0; i < this.taskTypeDicGroups.length; i++) {
        if (row.taskType === this.taskTypeDicGroups[i].itemCode) {
            realGroupName = this.taskTypeDicGroups[i].itemValue
        }
      }
      if(realGroupName){
        return realGroupName
      }else{
        return row.taskType == 1 ? '导入' : '导出'
      }
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
       asyncImportExportTaskShow(row.taskId)
       .then(res => {
            this.modal = true
            this.detail = res.data.data
        if(res.data.data.taskStatus){
            this.detail.taskStatus = res.data.data.taskStatus
        }
        if(res.data.data.taskType){
            this.detail.taskType = res.data.data.taskType
        }
            this.okFlag = 'update'
            }).catch(() => {
                this.$Message.error('行数据查询失败')
            })
        },

     // 导出
     exportErrorMsg (row) {
        console.log(row);
        let url = this.dynamicApi ? this.dynamicApi : '/api/lowcode/customize/page/template/cscpCustomizeVpages/exportTxt/'
        this.$http.post(url + row.taskId, { responseType: 'blob' }).then(res => {
          let content = res.data // 文件流
          let blob = new Blob([content], { type: 'application/octet-stream' })
          let fileName = row.taskName + '.txt'
          let link = document.createElement('a')
          link.download = fileName
          link.style.display = 'none'
          link.href = URL.createObjectURL(blob)
          document.body.appendChild(link)
          link.click()
          URL.revokeObjectURL(link.href) // 释放URL 对象
          document.body.removeChild(link)
        })
    },
    // 导入
    labelExcelImport () {
      event.preventDefault()
      document.getElementById('uploadEventFile').click()
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
        asyncImportExportTaskDel(row.taskId).then(res => {
            if (res.data.code != 200) {
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
                asyncImportExportTaskUpdate(copyDetail).then(res => {
                this.$Message.success('修改成功')
                this.modal = false
                this.queryList()
                this.$refs['detail'].resetFields()
                }).catch(() => {
                this.$Message.error('修改失败')
                })
            } else if (type === 'add') {
                asyncImportExportTaskCreate(copyDetail).then(res => {
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
    queryList (flag) {
      if (flag) {
        this.pageData.page = 1
      }
      let obj = {}
      obj['taskName.contains'] = this.queryForm['taskName']  ?  this.queryForm['taskName']: null
      obj['taskStatus.equals'] = this.queryForm['taskStatus']  ?  this.queryForm['taskStatus']: null
      obj['createdTime.greaterOrEqualThan'] = this.queryForm['createdTime'][0]  ?  this.$moment(new Date(this.queryForm['createdTime'][0])).format('yyyy-MM-DD HH:mm:ss') : null
      obj['createdTime.lessOrEqualThan'] = this.queryForm['createdTime'][1]  ?  this.$moment(new Date(this.queryForm['createdTime'][1])).format('yyyy-MM-DD HH:mm:ss'): null
      obj['dealTime.greaterOrEqualThan'] = this.queryForm['dealTime'][0]  ?  this.$moment(new Date(this.queryForm['dealTime'][0])).format('yyyy-MM-DD') : null
      obj['dealTime.lessOrEqualThan'] = this.queryForm['dealTime'][1]  ?  this.$moment(new Date(this.queryForm['dealTime'][1])).format('yyyy-MM-DD'): null
      obj['finishTime.greaterOrEqualThan'] = this.queryForm['finishTime'][0]  ?  this.$moment(new Date(this.queryForm['finishTime'][0])).format('yyyy-MM-DD') : null
      obj['finishTime.lessOrEqualThan'] = this.queryForm['finishTime'][1]  ?  this.$moment(new Date(this.queryForm['finishTime'][1])).format('yyyy-MM-DD'): null
      obj['taskType.equals'] = this.queryForm['taskType']  ?  this.queryForm['taskType']: null
      let params = Object.assign(obj, {
        page: this.pageData.page - 1,
        size: this.pageData.size,
      })
      asyncImportExportTaskParams(params)
        .then(res => {
            this.data = res.data.data.data
            this.pageData.total = res.data.data.recordsTotal
        }).catch(() => {
        this.$Message.error('列表查询失败')
        })
    },
    handleDeleteMultiple: function () {
      let arr = this.selectedArr.map(item => item.taskId)
      if (arr.length < 1) {
        return this.$Message.error('删除失败,当前选择列表为空！')
      }
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除勾选的数据？</p>',
        onOk: () => {
        asyncImportExportTaskDelAll(arr).then(res => {
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
