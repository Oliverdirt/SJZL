<template>
  <div>
    <Modal :title="title"
           v-model="show"
           :styles="{top: '30%', width: '800px'}"
           @on-ok="handleSubmit">
      <Table border :columns="columns" :data="tableData"></Table>
      <div slot="footer">
        <Button type="primary" @click="handleSubmit">确定</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import {
  getTopicDetailByData
} from '@/api/kafka/kafka'
export default {
  name: 'topic-detail',
  data () {
    return {
      show: false,
      formItem: {},
      title: '',
      columns: [
        {
          key: 'partition',
          title: '分区号'
        },
        {
          title: 'lender分区',
          render: (h, params) => {
            return h('data-tag',
              {
                props: {
                  left: 'broker',
                  title: params.row.leader.host + ':' + params.row.leader.port,
                  right: params.row.leader.id
                }
              }
            )
          }
        },
        {
          title: '所有副本',
          render: (h, params) => {
            return h('div',
              params.row.replicas.map(item => {
                return h('data-tag',
                  {
                    props: {
                      left: 'broker',
                      title: item.host + ':' + item.port,
                      right: item.id
                    }
                  })
              })
            )
          }
        },
        {
          title: 'isr副本',
          render: (h, params) => {
            return h('div',
              params.row.replicas.map(item => {
                return h('data-tag',
                  {
                    props: {
                      left: 'broker',
                      title: item.host + ':' + item.port,
                      right: item.id
                    }
                  })
              })
            )
          }
        },
        {
          key: 'beginningOffset',
          title: '最小偏移量'
        },
        {
          key: 'endOffset',
          title: '最大偏移量'
        },
        {
          title: '消息数量',
          render: (h, params) => {
            return h('span', params.row.endOffset - params.row.beginningOffset)
          }
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
    handlePageChange () {

    },
    initModel (sourceId, name) {
      let data = new FormData()
      data.append('topic', name)
      data.append('sourceId', sourceId)
      getTopicDetailByData(data)
      // this.$http.post('/api/kafka/getTopicDetail', data)
        .then(res => {
          this.title = res.data.name + ' 分区详情'
          this.tableData = res.data.partitions
          this.show = true
        })
    }
  }
}
</script>

<style scoped>

</style>
