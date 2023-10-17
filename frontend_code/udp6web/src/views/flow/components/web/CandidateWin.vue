<template>
    <Modal
            v-model="modalVisible"
            title="处理人选择"
            width="700"
            @on-visible-change="change">
        <div style="max-height: 400px;overflow: auto">
            <Table border ref="selection" :columns="columns" :data="dataTable"
                   @on-select="selectTable"
                   @on-select-all="selectAll"
                   @on-select-all-cancel="cancelSelectAll"
                   @on-select-cancel="cancelSelectTable">

            </Table>
        </div>

        <div slot="footer">
            <Button size="large" @click="cancel">返回</Button>
            <Button type="primary" size="large" @click="ok">确定</Button>
        </div>
    </Modal>
</template>

<script>
import util from '@/libs/util.js'

export default {
  name: 'CandidateWin',
  data () {
    return {
      modalVisible: false,
      userCheck: [],
      dataTable: [],
      lineKey: null,
      processDefinitionId: null,
      processInstanceId: null,
      columns: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '候选人姓名',
          key: 'name',
          align: 'center'
        }
      ]
    }
  },
  methods: {
    selectTable (selection, row) {
      this.userCheck.push(row.id)
    },
    cancelSelectTable (selection, row) {
      for (let i = 0; i < this.userCheck.length; i++) {
        if (this.userCheck[i] === row.id) {
          this.userCheck.splice(i, 1)
        }
      }
    },
    cancelSelectAll () {
      this.userCheck = []
    },
    selectAll (selection) {
      this.userCheck = []
      for (let i = 0; i < selection.length; i++) {
        this.userCheck.push(selection[i].id)
      }
    },
    ok () {
      if (this.userCheck.length > 0) {
        this.$emit('refreshCandidate', this.userCheck)
      } else {
        this.$Notice.error({
          title: '至少选择一个处理人'
        })
      }
    },
    listTable () {
      let This = this
      util.http.get('/api/activiti/query/getCandidateList/' + This.processInstanceId + '/' + This.processDefinitionId + '/' + This.lineKey).then(response => {
        let arr = response.data
        for (let i = 0; i < arr.length; i++) {
          let canArr = arr[i].candidates
          for (let j = 0; j < canArr.length; j++) {
            let map = {
              id: canArr[j].id,
              name: canArr[j].username
            }
            This.dataTable.push(map)
          }
        }
      })
    },
    change (flag) {
      if (!flag) {
        this.userCheck = []
        this.dataTable = []
      } else {
        this.listTable()
      }
    },
    cancel () {
      this.modalVisible = false
    }
  }
}
</script>

<style scoped>

</style>
