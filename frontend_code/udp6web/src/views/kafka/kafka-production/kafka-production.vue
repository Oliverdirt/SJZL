<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard" style="height: 270px">
        <div class="searchArea">
          <Form :label-width="80">
            <Row :gutter="48">
              <Col span="8">
                <FormItem label="Kafka" prop="noticeTitle">
                  <kafkaSelect @kafkaChange="kafkaChange"></kafkaSelect>
                </FormItem>
              </Col>
              <Col span="8">
                <FormItem label="topic" prop="topic">
                  <Select v-model="topic" filterable placeholder="选择topic" clearable style="margin-left: 5px">
                    <Option v-for="item in topics" :key="item.name" :label="item.name"
                            :value="item.name"></Option>
                  </Select>
                </FormItem>
              </Col>
            </Row>
            <Row :gutter="48" style="margin-top: 16px">
              <Col span="24">
                <FormItem label="Message:" prop="group">
                  <Input type="textarea" v-model="message" size="medium" rows="6" style="margin-top: 10px"
                         placeholder="上键：向上翻输入历史(最多保存10条最近输入记录); 下键：向下翻输入历史; ctrl+enter: 发送消息"
                         :autosize="{ minRows: 6, maxRows: 15 }"
                         @keyup.enter.native="keyDown"
                         @keyup.up.native="scrollUpHistory" @keyup.down.native="scrollDownHistory"
                         show-word-limit>
                  </Input>
                </FormItem>
              </Col>
            </Row>
          </Form>
          <div class="footer">
            <div>
              <el-checkbox v-model="batch" style="margin-top: 10px;margin-left: 32px"></el-checkbox>
              <el-tooltip class="item" effect="dark" content="按回车分割成多条消息来发送，在消费中显示" placement="top-start">
                <span>分割后批量发送</span>
              </el-tooltip>
            </div>
            <div>
              <Button type="primary" @click="produce" style="margin: 10px 0"><i class="iconfont icon-Send"></i> 发送
              </Button>
            </div>
          </div>
        </div>
      </Card>
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard" style="height: calc(100% - 270px)">
        <producer :sourceId="sourceId" ref="produce" :topic="topic" :batch="batch" :message="message" @processHistory="processHistory"></producer>
      </Card>
    </div>
  </div>
</template>

<script>
import producer from '../component/producer.vue'
import kafkaSelect from '../component/kafkaSelect.vue'
import {
  getTopicsByData
} from '@/api/kafka/kafka'
export default {
  name: 'kafka-production',
  data () {
    return {
      sourceId: null,
      sources: [],
      topic: null,
      message: null,
      history: [],
      cursor: null,
      batch: false,
      topics: [],
      maxHistorySize: 10
    }
  },

  methods: {
    kafkaChange (sourceId) {
      this.sourceId = sourceId
      this.getTopics()
    },
    produce () {
      this.$refs.produce.produce()
      this.message = null
    },
    keyDown (e) {
      if (e.ctrlKey && e.keyCode == 13) { // 用户点击了ctrl+enter触发
        this.produce()
      } else { // 用户点击了enter触发

      }
    },
    // 键盘按上键翻滚历史
    scrollUpHistory () {
      this.message = this.history[this.cursor]
      this.cursor--
      if (this.cursor < 0) {
        this.cursor = this.history.length - 1
      }
    },
    scrollDownHistory () {
      this.cursor++
      if (this.cursor >= this.history.length) {
        this.cursor = 0
      }
      this.message = this.history[this.cursor]
    },
    processHistory (message) {
      this.history.push(message)
      if (this.history.length > this.maxHistorySize) {
        this.history = this.history.slice(-this.maxHistorySize)
      }
      this.cursor = this.history.length - 1
    },
    getTopics (sourceId) {
      let data = new FormData()
      data.append('sourceId', this.sourceId)
      getTopicsByData(data)
      // this.$http.post('/api/kafka/getTopics', data)
        .then((response) => {
          if (response.data.success) {
            this.topics = response.data.data
          } else {
            this.$message.error(response.data.message)
          }
        }).catch(error => {
          this.$message.error('查询所有topic失败')
        })
    }

  },
  components: {
    kafkaSelect, producer
  }
}
</script>

<style scoped lang="less">
@import "../../admin/styles/formStyle.less";

.footer {
  display: flex;
  justify-content: space-between;
}
</style>
