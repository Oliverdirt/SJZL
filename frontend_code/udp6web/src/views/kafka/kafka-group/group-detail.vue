<template>
  <div>
    <Modal :title="title"
           v-model="show"
           :styles="{top: '30%', width: '800px'}"
           @on-ok="handleSubmit">
      <Table  :columns="columns" border :data="tableData" :span-method="handleSpan" :disabled-hover="true"></Table>
      <div slot="footer">
        <Button type="primary" @click="handleSubmit">确定</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import {
  getGroupDetail
} from '@/api/kafka/kafka'
export default {
  name: 'group-detail',
  data () {
    return {
      show: false,
      formItem: {},
      title: '',
      columns: [
        {
          key: 'topic',
          title: 'topic'
        },
        {
          key: 'partition',
          title: '分区号'
        },
        {
          title: '消费偏移量',
          render: (h, params) => {
            return h('span', params.row.offset)
          }
        },
        {
          key: 'lag',
          title: '未消费消息条数'
        }
      ],
      tableData: [],
      pageTotal: 0,
      pageLength: 0
    }
  },
  methods: {
    handleSubmit () {
      this.show = false
    },
    handleSpan ({ row, column, rowIndex, columnIndex }) {
      // 合并第2列
      if (columnIndex == 0) {
        // 计算合并的行数列数
        let x = row.num == 0 ? 0 : row.num
        let y = row.num == 0 ? 0 : 1
        return [x, y]
      }
    },
    assembleData (data) {
      for (var i = 0; i < data.length; i++) {
        if (data[i].already !== 1) {
          if (data[i + 1]) {
            data[i].num = 1
            for (var a = i + 1; a < data.length; a++) {
              if (data[i].topic === data[a].topic) {
                data[i].num++
                data[a].num = 0
                data[a].already = 1
              } else {
                break
              }
            }
          }
        }
      }
      // 将整理后的数据交给表格渲染
      console.log('data', data)
      this.tableData = data
    },
    handlePageChange () {
    },
    initModel (sourceId, name) {
      let data = new FormData()
      data.append('group', name)
      data.append('sourceId', sourceId)
      getGroupDetail(data)
      // this.$http.post('/api/kafka/group/detail', data)
        .then(res => {
          this.title = 'group:' + name + '消费偏移量详情'
          this.assembleData(res.data.data)
          this.show = true
        })
    }
  }
}
</script>

<style scoped>

</style>
