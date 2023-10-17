<template>
    <Modal
            v-model="modalVisible"
            title="签收任务"
            width="700"
            :mask-closable="false"
            @on-ok="ok"
            @on-visible-change="change"
            @on-cancel="cancel">
        <div>
            <Table border highlight-row @on-current-change="selectRow" :columns="columns" :data="dataTable"></Table>
            <div class="page-div">
                <Page :total="dataCount" :page-size="pageSize" @on-change="handlePageChange" show-total/>
            </div>
        </div>
    </Modal>
</template>

<script>
import util from '@/libs/util.js'

export default {
  name: 'SignTaskWin',
  data () {
    return {
      modalVisible: false,
      checkRow: null,
      dataCount: 0,
      pageSize: 2,
      page: 0,
      dataTable: [],
      columns: [
        {
          type: 'index',
          width: 60,
          align: 'center'
        },
        {
          title: '任务名称',
          key: 'name'
        },
        {
          title: '流程名称',
          key: 'processName'
        },
        {
          title: '时间',
          key: 'time'
        }
      ]
    }
  },
  methods: {
    listTable () {
      let This = this
      util.http.get('/api/activiti/query/getGroupTaskList', {
        params: {
          pageNumber: This.page,
          pageSize: This.pageSize
        }
      }).then(response => {
        This.dataTable = []
        let data = response.data
        This.dataCount = data.recordsTotal
        let list = data.data
        for (let i = 0; i < list.length; i++) {
          let process = list[i]
          let map = {
            id: process.taskId,
            name: process.name,
            processName: process.process.processDefinitionName,
            time: process.startTime
          }
          This.dataTable.push(map)
        }
      })
    },
    ok () {
      if (this.checkRow === null) {
        this.$Notice.warning({
          title: '请选择一个任务'
        })
        return
      }
      let This = this
      util.http.get('/api/activiti/core/claim/' + This.checkRow).then(response => {
        This.$Notice.success({
          title: '签收成功'
        })
        This.$emit('refreshTable')
      })
    },
    change (flag) {
      if (!flag) {
        this.dataTable = []
        this.dataCount = 0
        this.pageSize = 2
        this.page = 0
      } else {
        this.listTable()
      }
    },
    selectRow (currentRow, oldCurrentRow) {
      this.checkRow = currentRow.id
    },
    handlePageChange (current) {
      this.page = current - 1
      this.listTable()
    },
    cancel () {
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
