<template>
  <div class="app-container">
    <Form :model="queryParams" ref="queryForm"  v-show="showSearch" :label-width="68">
      <Form style="display: flex" ref="formEquipment" :model="roleSearchForm" :label-width="100">
        <FormItem  label="任务名称" prop="jobName" >
          <Input
            v-model="queryParams.jobName"
            placeholder="请输入任务名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </FormItem >
        <FormItem label="任务组名" prop="jobGroup">
          <Select  v-model="queryParams.jobGroup" placeholder="请选择任务组名" clearable >

          </Select>
        </FormItem>
        <FormItem label="执行状态" prop="status">
          <Select  v-model="queryParams.status" placeholder="请选择任务状态" clearable >

          </Select >
        </FormItem>
        <FormItem label="执行时间" prop="status">
          <DatePicker v-model="queryParams.daterange" type="daterange" placement="bottom-end" placeholder="Select date" style="width: 200px"></DatePicker>
        </FormItem>
        <div style="width: 100%;margin-left: 10px">
          <Button icon="ios-search" type="primary" @click="handleQuery()">查询</Button>
          <Button type="default" style="margin-left: 10px" @click="resetQuery()">重置</Button>
        </div>
      </Form>
    </Form>

    <Row :gutter="24">
      <Col span="24">
        <Button type="error" icon="md-trash"  ghost>删除</Button>
        <Button type="error" icon="md-trash"  style="margin-left:5px"  ghost>清空</Button>
        <Button type="warning" icon="md-download"  style="margin-left:5px"  ghost>导出</Button>
        <Button type="warning" icon="md-close"  style="margin-left:5px"   ghost>关闭</Button>
      </Col>
    </Row>
    <Row :gutter="24">
      <Col span="24" style="top: 20px">
        <Table border highlight-row ref="currentRowTable" :columns="column1" :data="data1">
          <template slot-scope="{ row }" slot="operator">
            <Button type="text" size="small"  style="margin-left: 2px;color: #1890ff;"  @click="handleJob(row)" >
              <Icon type="md-create" size="15" />编辑</Button>
            <Button type="text" size="small"   style="color: #1890ff" >
              <Icon type="md-trash" size="15" />删除</Button>
            <Dropdown :transfer="true" >
              <a href="javascript:void(0)">
                <Icon type="md-menu" />
                更多
              </a>
              <DropdownMenu slot="list" >
                <DropdownItem><Icon type="md-arrow-dropright" size="15" />执行一次</DropdownItem>
                <DropdownItem><Icon type="md-eye" size="15" />任务详情</DropdownItem>
                <DropdownItem ><Icon type="md-options" size="15"/>调度日志</DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </template>
        </Table>
      </Col>
      <Col span="24" style="top: 50px;text-align: right;" >
        <Page :total="100" show-elevator />
      </Col>
    </Row>

  </div>
</template>

<script>

export default {
  name: 'jobList',
  data () {
    return {
      column1: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          key: 'jobId',
          width: 100,
          align: 'center',
          title: '日志标号'
        },
        {
          title: '任务名称',
          key: 'jobName'
        },
        {
          title: '任务组名',
          key: 'jobGroup'
        },
        {
          title: '调用目标字符串',
          key: 'invokeTarget'
        },
        {
          title: '日志信息',
          key: 'cronExpression'
        },
        {
          title: '执行状态',
          key: 'cronExpression'
        },
        {
          title: '执行时间',
          key: 'cronExpression'
        },
        {
          title: '操作',
          slot: 'operator',
          align: 'center'
        }
      ],
      data1: [
        {
          jobId: 1,
          jobName: '系统默认（无参）',
          jobGroup: '默认',
          invokeTarget: 'ryTask.ryNoParams',
          cronExpression: '0/10 * * * * ?',
          status: 1
        },
        {
          jobId: 2,
          jobName: '系统默认（无参）',
          jobGroup: '默认',
          invokeTarget: 'ryTask.ryNoParams',
          cronExpression: '0/15 * * * * ?',
          status: 0
        },
        {
          jobId: 3,
          jobName: '系统默认（无参）',
          jobGroup: '默认',
          invokeTarget: 'ryTask.ryNoParams',
          cronExpression: '0/20 * * * * ?',
          status: 0
        }
      ],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 定时任务表格数据
      jobList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 是否显示详细弹出层
      openView: false,
      // 任务组名字典
      jobGroupOptions: [],
      // 状态字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        jobName: undefined,
        jobGroup: undefined,
        dateRange: undefined
      },
      // 表单参数
      form: {}
      // 表单校验
    }
  },
  created () {
    // this.getList()
  },
  methods: {
    /** 查询定时任务列表 */
    getList () {
      this.loading = true
    },
    // 取消按钮
    cancel () {
      this.open = false
    },
    // 表单重置
    reset () {
      this.form = {
        jobId: undefined,
        jobName: undefined,
        jobGroup: undefined,
        invokeTarget: undefined,
        cronExpression: undefined,
        misfirePolicy: 1,
        concurrent: 1,
        status: '0'
      }
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1
      // this.getList()
    },
    /** 重置按钮操作 */
    resetQuery () {
      // this.resetForm('queryForm')
      // this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange (selection) {
      this.ids = selection.map(item => item.jobId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 任务状态修改
    handleStatusChange (row) {
    },
    /* 立即执行一次 */
    handleRun (row) {
    },
    /** 任务详细信息 */
    handleView (row) {

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
    /** 提交按钮 */
    submitForm: function () {
    },
    /** 删除按钮操作 */
    handleDelete (row) {

    },
    /** 导出按钮操作 */
    handleExport () {
    }
  }
}
</script>
<style scoped>
</style>
