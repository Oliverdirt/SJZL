<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard" style="height: 120px">
        <div class="searchArea" >
          <Form :label-width="80" >
            <Row :gutter="48">
              <Col span="8">
                <FormItem label="Kafka" prop="noticeTitle">
                  <kafkaSelect @kafkaChange="getTopics"></kafkaSelect>
                </FormItem>
              </Col>
              <Col span="8">
                <FormItem label="topic" prop="topic">
                  <Select v-model="topic" filterable placeholder="选择topic" clearable>
                    <Option v-for="item in topics" :key="item.name" :label="item.name" :value="item.name"></Option>
                  </Select>
                </FormItem>
              </Col>
              <Col span="8">
                <FormItem label="Group" prop="group">
                  <AutoComplete :disabled="disabled" v-model="group" :data="groupList" @on-search="getGroupByTopic"
                                placeholder="请输入消费group"></AutoComplete>
                </FormItem>
              </Col>
            </Row>
            <Row :gutter="48" style="margin-top: 16px">
              <Col span="8">
                <FormItem label="Message" prop="group">
                  <el-radio v-model="mode" label="earliest" :disabled="disabled">历史消息</el-radio>
                  <el-radio v-model="mode" label="latest" :disabled="disabled">最新消息</el-radio>
                </FormItem>
              </Col>
              <Col span="8">
                <FormItem label="高亮显示" prop="group">
                  <Input v-model="keyword" placeholder="请输入关键字" clearable></Input>
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard" style="height: calc(100% - 120px)">
        <consumer @changeDisabled="changeDisabled" :broker="broker" :sourceId="sourceId" :topic="topic" :group="group" :mode="mode" :keyword="keyword"></consumer>
      </Card>
    </div>
  </div>
</template>
<script>
import kafkaSelect from '../component/kafkaSelect.vue'
import consumer from '../component/consumer.vue'

export default {
  name: 'kafka-consume',
  data () {
    return {
      sourceId: null,
      sources: [],
      topic: null,
      groupList: [],
      disabled: false,
      keyword: null,
      mode: 'earliest',
      topics: [],
      message: null,
      group: null,
      broker: null
    }
  },

  methods: {
    getTopics (sourceId) {
      this.sourceId = sourceId
      let data = new FormData()
      data.append('sourceId', this.sourceId)
      this.$http.post('/api/kafka/getTopics', data).then((response) => {
        if (response.data.success) {
          this.topics = response.data.data
        } else { this.$message.error(response.data.message) }
      }).catch((error) => {
        this.$message.error('查询所有topic失败')
      })

      this.$http.post('/api/kafka/getBroker', data).then((response) => {
        this.broker = response.data
      }).catch((error) => {
        this.$message.error('查询失败')
      })
    },
    changeDisabled (val) {
      this.disabled = val
    },
    getGroupByTopic () {
      let data = new FormData()
      data.append('broker', this.broker)
      data.append('topic', this.topic)
      if (this.broker != null && this.topic != null && this.broker != '' && this.topic != '') {
        this.$http.post('/api/kafka/getGroupByTopic', data).then((response) => {
          if (response.data.success) {
            let arr = response.data.data.map(item => {
              return item.value
            })
            console.log('group', this.group)
            console.log('arr', arr)
            this.groupList = arr.filter(i => i.indexOf(this.group) !== -1)
          } else {
            this.groupList = []
          }
        }).catch((error) => {
          this.groupList = []
        })
      } else {
        this.groupList = []
      }
    }
  },
  components: {
    consumer, kafkaSelect
  }
}
</script>
<style scoped lang="less">
@import "../../admin/styles/formStyle.less";
.query-model {
  width: 170px;
}

.page-area {
  display: flex;
  justify-content: flex-end;
  padding-top: 35px;
  padding-right: 50px;
}

.modal{
  ::v-deep .ivu-form-item-content {
    display: flex;
  }
}
</style>
