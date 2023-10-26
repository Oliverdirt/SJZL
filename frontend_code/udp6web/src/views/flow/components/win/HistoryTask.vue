<template>
    <Modal v-model="modalVisible" title="查看流转历史" width="900" class-name="vertical-center-modal" footer-hide>
        <div style="height: 400px;overflow: auto">
            <Table border :columns="columns" :data="dataTable"></Table>
        </div>

    </Modal>
</template>

<script>

import Util from '@/libs/util.js'

export default {
  name: 'historyTask',
  data () {
    return {
      modalVisible: false,
      processInstanceId: null,
      dataTable: [],
      columns: [
        {
          type: 'index',
          width: 60,
          align: 'center'
        },
        {
          title: '任务节点',
          key: 'node'
        },
        {
          title: '处理人',
          key: 'user'
        },
        {
          title: '接收时间',
          key: 'time'
        },
        {
          title: '状态',
          key: 'status'
        },
        {
          title: '批注',
          key: 'comment'
        }
      ]
    }
  },
  watch: {
    processInstanceId: function (value) {
      if (value === null) return

      this.listHistory()
    }
  },
  methods: {
    listHistory () {
      let This = this
      Util.http
        .get('/api/activiti/query/getHistoryList/' + This.processInstanceId)
        .then(function (response) {
          This.dataTable = []
          let data = response.data
          let list = data.taskList
          for (let i = 0; i < list.length; i++) {
            let task = list[i]
            if (task.userTask) {
              let map = {
                node: task.name,
                user: task.assigneeName,
                time: task.startTime,
                status: task.active ? '处理中' : '已处理',
                comment: task.comment

              }
              This.dataTable.push(map)
            }
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    }
  }
}
</script>

<style scoped>

</style>
