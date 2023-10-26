<template>
    <section style="height: 100%">
      <div class="formCard-content">
        <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
          <div class="searchArea" >
            <Form :model="query">
              <Row :gutter="48">
                <Col span="8">
                  <FormItem>
                    <Input clearable v-model="query.name" placeholder="流程名称"></Input>
                  </FormItem>
                </Col>
                <Col span="16" style="text-align: right">
                  <FormItem>
                    <Button icon="ios-search" type="primary" @click="querySubmit()">查询</Button>
                  </FormItem>
                </Col>
              </Row>
              <!--<FormItem>-->
              <!--<Button type="primary" icon="ios-cloud" shape="circle" @click="signTask()">签收任务</Button>-->
              <!--</FormItem>-->
            </Form>
          </div>
        </Card>
        <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
          <Table border :columns="columns" :data="tableData"></Table>
          <Page :total="dataCount" :page-size="pageSize" @on-change="handlePageChange" show-total/>
        </Card>
      </div>
        <sign-task-win ref="SignTaskWin" @refreshTable="refreshTable"></sign-task-win>
    </section>

</template>

<script>
import Util from '@/libs/util.js'
import SignTaskWin from './components/win/SignTaskWin'

export default {
  name: 'TodoList',
  components: { SignTaskWin },
  data () {
    return {
      query: {
        name: null
      },
      dataCount: 0,
      pageSize: 6,
      page: 0,
      columns: [
        {
          type: 'index',
          width: 60,
          align: 'center'
        },
        {
          title: '流程名称',
          key: 'name'
        },
        {
          title: '当前节点',
          key: 'key'
        },
        {
          title: '接收时间',
          key: 'time'
        },
        {
          title: '操作',
          key: 'id',
          render: (h, params) => {
            if (params.row.assignee == null || typeof (params.row.assignee) === 'undefined') {
              return h('div', [
                h('Button', {
                  props: {
                    type: 'primary',
                    size: 'small'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.signTask(params.row)
                    }
                  }
                }, '签收')
              ])
            } else {
              return h('div', [
                h('Button', {
                  props: {
                    type: 'primary',
                    size: 'small'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.edit(params.row)
                    }
                  }
                }, '处理')
              ])
            }
          }
        }
      ],
      tableData: []
    }
  },
  mounted () {
    this.listTable()
  },
  methods: {
    listTable () {
      let This = this
      Util.http
        .get('/api/activiti/query/getActivelyTaskList', {
          params: {
            pageNumber: This.page,
            pageSize: This.pageSize,
            processName: This.query.name

          }
        })
        .then(function (response) {
          This.tableData = []
          let data = response.data
          This.dataCount = data.recordsTotal
          let list = data.data
          for (let i = 0; i < list.length; i++) {
            let process = list[i]
            let map = {
              id: process.id,
              name: process.process.processDefinitionName,
              key: process.name,
              processInstanceId: process.processInstanceId,
              time: process.startTime,
              taskId: process.taskId,
              formUrl: process.formUrl,
              assignee: process.assignee

            }
            This.tableData.push(map)
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    querySubmit () {
      this.page = 0
      this.listTable()
    },
    handlePageChange (current) {
      this.page = current - 1
      this.listTable()
    },
    edit (row) {
      this.$router.push({
        name: row.formUrl,
        query: {
          processInstanceId: row.processInstanceId,
          taskId: row.taskId
        }
      })
    },
    signTask (row) {
      // this.$refs.SignTaskWin.modalVisible = true;
      let This = this
      Util.http.get('/api/activiti/core/claim/' + row.taskId).then(response => {
        This.$Notice.success({
          title: '签收成功'
        })
        This.refreshTable()
        // This.$emit('refreshTable');
      })
    },
    refreshTable () {
      this.page = 0
      this.query.name = null
      this.listTable()
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
</style>
