<template>
    <section>
        <Form :model="query" inline>
            <FormItem>
                <Input clearable v-model="query.name" placeholder="流程名称"></Input>
            </FormItem>
            <FormItem>
                <Button icon="ios-search" shape="circle" @click="querySubmit()">查询</Button>
            </FormItem>
            <!--<FormItem>-->
                <!--<Button type="primary" icon="ios-cloud" shape="circle" @click="signTask()">签收任务</Button>-->
            <!--</FormItem>-->
        </Form>
        <Table border :columns="columns" :data="tableData"></Table>
        <div class="page-div">
            <Page :total="dataCount" :page-size="pageSize" @on-change="handlePageChange" show-total/>
        </div>
        <history-task ref="historyTask"></history-task>
        <flow-diagram ref="FlowDiagram"></flow-diagram>
    </section>

</template>

<script>
import Util from '@/libs/util.js'
import HistoryTask from './components/win/HistoryTask'
import FlowDiagram from './components/model/RuntimeFlowDiagram'

export default {
  name: 'TodoList',
  components: { FlowDiagram, HistoryTask },
  data () {
    return {
      query: {
        name: null
      },
      dataCount: 0,
      pageSize: 5,
      page: 0,
      columns: [
        {
          type: 'index',
          title: '序号',
          width: 70,
          align: 'center'
        },
        {
          title: '流程名称',
          key: 'name'
        },
        {
          title: '开始时间',
          key: 'startTime'
        },
        {
          title: '结束时间',
          key: 'endTime'
        },
        {
          title: '操作',
          key: 'id',
          render: (h, params) => {
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
                    this.viewHistory(params.row)
                  }
                }
              }, '流转信息'),
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
                    this.viewProcess(params.row)
                  }
                }
              }, '流程图')
            ])
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
        .get('/api/activiti/query/getEndedProcessList', {
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
              name: process.processDefinitionName,
              processInstanceId: process.processInstanceId,
              startTime: process.startTime,
              endTime: process.endTime,
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
    viewHistory (row) {
      if (row.processInstanceId === null) {
        this.$Notice.error({
          title: 'processInstanceId为空'
        })
        return
      }
      this.$refs.historyTask.modalVisible = true
      this.$refs.historyTask.processInstanceId = row.processInstanceId
    },
    viewProcess (row) {
      if (row.processInstanceId === null) {
        this.$Notice.error({
          title: 'processInstanceId为空'
        })
        return
      }
      this.$refs.FlowDiagram.modalVisible = true
      this.$refs.FlowDiagram.processInstanceId = row.processInstanceId
    },
    refreshTable () {
      this.page = 0
      this.query.name = null
      this.listTable()
    }
  }
}
</script>

<style scoped>
    .page-div {
        display: flex;
        justify-content: flex-end;
        padding-top: 35px;
        padding-right: 50px;
    }
</style>
