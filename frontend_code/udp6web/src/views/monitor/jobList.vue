<template>
  <div class="app-container" style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea" >
          <Form :model="queryParams" ref="queryForm" :label-width="80">
            <Row :gutter="48">
              <Col span="8">
                <FormItem label="任务名称" prop="jobName">
                  <Input
                    v-model="queryParams.jobName"
                    placeholder="请输入任务名称"
                    clearable
                    style=""
                    @keyup.enter.native="getList(true)"
                  />
                </FormItem>
              </Col>
              <Col span="8">
                <FormItem label="任务组名" prop="jobGroup">
                  <Select v-model="queryParams.jobGroup" placeholder="请选择任务组名" clearable style="">
                    <Option v-for="item in jobGroups" :value="item.itemCode.toLocaleString()"
                            :key="item.itemCode.toLocaleString()">{{ item.itemValue }}
                    </Option>
                  </Select>
                </FormItem>
              </Col>
              <Col span="8">
                <FormItem label="任务状态" prop="status">
                  <Select v-model="queryParams.status" placeholder="请选择任务状态" clearable style="">
                    <Option v-for="item in jobStatuss" :value="item.itemCode.toLocaleString()"
                            :key="item.itemCode.toLocaleString()">{{ item.itemValue }}
                    </Option>
                  </Select>
                </FormItem>
              </Col>
              <Col span="24" style="text-align: right;margin-top: 24px">
                <FormItem>
                  <Button icon="ios-search" type="primary" @click="getList(true)">查询</Button>
                  <Button type="default" style="margin-left: 10px" @click="resetQuery()">重置</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="tableBtn">
          <Button @click="handleJob" type="primary" style="margin-right: 10px;margin-bottom: 5px" icon="md-add" >新增</Button>
          <Button @click="handleDelete" type="error" style="margin-right: 10px;margin-bottom: 5px" >删除</Button>
          <Button @click="handleExport" type="default" style="" >导出</Button>
        </div>
        <Table border highlight-row ref="currentRowTable" :columns="JobColumn" :data="jobDatas"
               @on-selection-change="handleSelectionChange">
          <template slot-scope="{ row }" slot="operator">
            <Button type="text" size="small" style="margin-left: 2px;color: #0056B5;" @click="handleJob(row.id)">
              <Icon type="md-create" size="15"/>
              编辑
            </Button>
            <Button type="text" size="small" style="color: #0056B5" @click="handleDelete(row)">
              <Icon type="md-trash" size="15"/>
              删除
            </Button>
            <!-- <Dropdown :transfer="true" >
              <a href="javascript:void(0)">
                <Icon type="md-menu" />
                更多
              </a>
              <DropdownMenu slot="list" >
                <DropdownItem><Icon type="md-arrow-dropright" size="15" @click="handleRun(row)"  />执行一次</DropdownItem> -->
            <!--   <DropdownItem><Icon type="md-eye" size="15" />任务详情</DropdownItem> -->
            <!-- <DropdownItem ><Icon type="md-options" @click="handleLog(row)" size="15"/>调度日志</DropdownItem> -->
            <!-- </DropdownMenu>
          </Dropdown> -->
          </template>
        </Table>
        <Page :total="pageData.total" :page-size="pageData.size" show-sizer show-total show-elevator :current="pageData.page"
              @on-change="handlePageChange($event, 'page')" @on-page-size-change="handlePageChange($event, 'size')" />
      </Card>
    </div>
  </div>
</template>

<script>

// import Util from '@/libs/util'
import {
  getDicItemsbyParams
} from '@/api/dic/listdic'
import {
  listQuartzByParams,
  updateQuartzStatus,
  execQuartzs,
  delQuartzById,
  exportQuartzInfo
} from '@/api/quartz/quartz'

export default {
  name: 'jobList',
  data () {
    return {
      JobColumn: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          type: 'index',
          title: '任务编号',
          align: 'center'
        },
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
          title: 'cron执行表达式',
          key: 'cronExpression'
        },
        {
          title: '状态',
          key: 'status',
          align: 'center',
          render: (h, params) => {
            return h('i-switch', {
              props: {
                type: 'primary',
                value: params.row.status === '1' // 控制开关的打开或关闭状态，官网文档属性是value
              },
              style: {
                marginRight: '5px'
              },
              on: {
                'on-change': (value) => { // 触发事件是on-change,用双引号括起来，
                  // 参数value是回调值，并没有使用到
                  this.switch(params.row, value) // params.index是拿到table的行序列，可以取到对应的表格值
                }
              }
            })
          }
        },
        {
          title: '操作',
          slot: 'operator',
          align: 'center'
        }
      ],
      jobGroups: [],
      jobStatuss: [],
      jobDatas: [],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 定时任务表格数据
      jobList: [],
      // 任务组名字典
      jobGroupOptions: [],
      // 查询参数
      queryParams: {
        jobName: null,
        jobGroup: [],
        status: []
      },
      pageData: {
        total: 0,
        size: 10,
        page: 1
      }
    }
  },
  created () {
    this.getJobGroup(9)
    this.getList()
    this.getDicStaus(8)
  },
  methods: {
    /** 查询定时任务列表 */
    getList (inquire) {
      this.loading = true
      if (inquire) {
        this.pageData.page = 1
      }
      let obj = {}
      obj['jobName'] = this.queryParams['jobName'] ? this.queryParams['jobName'] : null
      obj['jobGroup'] = this.queryParams['jobGroup'] ? this.queryParams['jobGroup'] : null
      obj['status'] = this.queryParams['status'] ? this.queryParams['status'] : null
      let params = Object.assign(obj, {
        page: this.pageData.page - 1,
        size: this.pageData.size
      })
      listQuartzByParams(params)
      // this.$util.http
      //   .get(url, httpConfig)
        .then(res => {
          this.jobDatas = res.data.data
          this.pageData.total = res.data.recordsTotal
        }).catch(function (error) {
          console.log(error)
        })
    },
    // 获取任务状态字典值
    getDicStaus (dicType) {
      // const [url, httpConfig] = [
      //   '/api/dic/cscpHxDicItems/getItems',
      //   {
      //     params: {
      //       'dicAttr.equals': 'list',
      //       'dicCode.equals': 'status'
      //     }
      //   }
      // ]
      const dicParams = {
        'dicAttr.equals': 'list',
        'dicCode.equals': 'status'
      }
      getDicItemsbyParams(dicParams)
      // this.$http.get(url, httpConfig)
        .then(response => {
          this.jobStatuss = response.data.data
        })
    },
    // 获取任务组名数据字典值
    getJobGroup (dicType) {
      // const [url, httpConfig] = [
      //   '/api/dic/cscpHxDicItems/getItems',
      //   {
      //     params: {
      //       'dicAttr.equals': 'list',
      //       'dicCode.equals': 'taskName'
      //     }
      //   }
      // ]
      const dicParams = {
        'dicAttr.equals': 'list',
        'dicCode.equals': 'taskName'
      }
      getDicItemsbyParams(dicParams)
      // this.$http.get(url, httpConfig)
        .then(response => {
          this.jobGroups = response.data.data
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
    // 取消按钮
    cancel () {
      this.open = false
    },
    // 表单重置
    reset () {
      this.resetQuery()
    },
    // 状态开关按钮
    switch (row, flag) {
      if (flag) {
        row.status = 1
      } else {
        row.status = 2
      }
      let text = row.status === 1 ? '恢复' : '暂停'
      // const [url, data] = [
      //   '/api/jobs/changeStatus',
      //   {
      //     id: row.id,
      //     status: row.status
      //   }
      // ]
      const StatusData = {
        id: row.id,
        status: row.status
      }
      updateQuartzStatus(StatusData)
      // this.$http.put(url, data)
        .then(response => {
          this.$Message.success(text + '任务成功')
        }).catch(() => {
          this.$Message.error(text + '任务失败')
          row.status = row.status === 1 ? 2 : 1
        })
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.$refs['queryForm'].resetFields()
      this.getList(true)
    },
    // 多选框选中数据
    handleSelectionChange (selection) {
      this.ids = selection.map(item => item.id)
    },
    // 任务状态修改
    handleStatusChange (row) {
    },
    /* 立即执行一次 */
    handleRun (row) {
      // const [url, data] = [
      //   '/api/jobs/run',
      //   {
      //     id: row.id,
      //     jobGroup: row.jobGroup
      //   }
      // ]
      const jobMsg = {
        id: row.id,
        jobGroup: row.jobGroup
      }

      execQuartzs(jobMsg)
      // this.$http.put(url, data)
        .then(response => {
          if (response.status === 200) {
            this.$Message.success('执行成功！')
          }
        }).catch(() => {
          this.$Message.success('执行失败！')
        })
    },
    /** 任务详细信息 */
    handleView (row) {

    },
    /** 任务日志列表查询 */
    handleJobLog () {
    },
    /** 新增按钮操作 */
    handleJob (id) {
      if (id) {
        this.$byStoreSet('job-edit', { id })
        this.$router.push({
          name: 'job-edit'
        })
      } else {
        this.$router.push({
          name: 'job-add'
        })
      }
    },
    /** 任务日志列表查询 */
    handleLog (id) {
      if (!id) {
        id = 0
      }
      this.$byStoreSet('job-log', 0)
      this.$router.push({
        name: 'job-log'
      })
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      let jobIds = null
      if (!row) {
        if (this.ids.length > 0) {
          jobIds = this.ids
        } else {
          return this.$Message.error('请勾选列表中的数据进行删除!')
        }
      } else {
        jobIds = row.id
      }
      this.$Modal.confirm({
        title: '警告',
        content: '确定删除所选中的任务？',
        onOk: () => {
          this.deleteJob(jobIds)
        }
      })
    },
    deleteJob (jobIds) {
      delQuartzById(jobIds)
      // this.$http.delete(`/api/jobs?jobIds=${jobIds}`)
        .then(resposne => {
          this.$Message.success('删除成功！')
          this.getList()
        }).catch(error => {
          if (error.response) {
            this.$Message.error(error.response.data.title ? error.response.data.title : '删除失败！')
          }
        })
    },
    /** 导出按钮操作 */
    handleExport () {
      this.$Modal.confirm({
        title: '是否确认导出所有数据项？',
        onOk () {
          this.$http.defaults.timeout = 15000 * 60
          exportQuartzInfo({}, 'blob')
          // this.$http.post(`/api/jobs/export`, {}, {
          //   responseType: 'blob'
          // })
            .then(res => {
              const link = document.createElement('a')
              const blob = new Blob([res.data], { type: 'application/vnd.ms-excel' })
              link.style.display = 'none'
              link.href = URL.createObjectURL(blob)
              document.body.appendChild(link)
              link.click()
              URL.revokeObjectURL(link.href)
              document.body.removeChild(link)
              this.$Notice.success({
                title: '导出数据成功'
              })
            }).catch(() => {
              this.$Notice.error({
                title: '导出数据失败'
              })
            })
        }
      })
    },
    handlePageChange (e, type) {
      if (type === 'size') {
        this.pageData.size = e
      } else if (type === 'page') {
        this.pageData.page = e
      }
      this.getList()
    }
  }
}
</script>
<style lang="less" scoped>
@import "../admin/styles/formStyle.less";
</style>
