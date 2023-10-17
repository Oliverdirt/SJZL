<template>
  <div>
    <Modal :title="title"
           v-model="show"
           :styles="{top: '30%', width: '800px'}">
      <Collapse v-model="value1" simple @on-change="onChane" accordion v-if="groupList.length">
          <Panel :name="item.value" :key="index" v-for="(item, index) in groupList">
            {{ item.value }}
            <p slot="content">
              <Row :gutter="24">
                <Col span="24">
                <Table border :columns="columns" :data="tableData" :span-method="handleSpan" :disabled-hover="true"></Table>
                </Col>
              </Row>
            </p>
          </Panel>
      </Collapse>
      <span v-else>暂无数据</span>
      <div slot="footer">
        <Button type="primary" @click="handleSubmit">确定</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import {
  getGroupDetail,
  getGroupsByTopicByData
} from '@/api/kafka/kafka'
export default {
  name: 'topic-consumer',
  data () {
    return {
      show: false,
      formItem: {},
      value1: '',
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
      sourceId: 0,
      pageTotal: 0,
      pageLength: 0,
      groupList: []
    }
  },
  methods: {
    onChane (arr) {
      this.tableData = []
      if (arr.length) {
        let data = new FormData()
        data.append('group', arr[0])
        data.append('sourceId', this.sourceId)
        getGroupDetail(data)
        // this.$http.post('/api/kafka/group/detail', data)
          .then(res => {
            this.assembleData(res.data.data)
          })
      }
    },
    handlePageChange () {

    },
    handleSubmit () {
      this.show = false
    },
    handleSpan ({ row, column, rowIndex, columnIndex }) {
      // 合并第1列
      if (columnIndex == 0) {
        // 计算合并的行数列数
        let x = row.num == 0 ? 0 : row.num
        let y = row.num == 0 ? 0 : 1
        console.log([x, y], '[x, y]')
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
      this.tableData = data
    },
    initModel (sourceId, name) {
      this.sourceId = sourceId
      let data = new FormData()
      data.append('topic', name)
      data.append('sourceId', sourceId)
      getGroupsByTopicByData(data)
      // this.$http.post('/api/kafka/getGroupsByTopic', data)
        .then(res => {
          this.title = '消费 gcTest 的group'
          this.groupList = res.data.data
          this.show = true
        })
    }
  }
}
</script>

<style scoped>

</style>
