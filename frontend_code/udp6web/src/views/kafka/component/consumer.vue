<template>
  <div class="consumer">
    <div class="consumer-title">
      <div>
        <span>已消费消息条数：</span>
        <span>{{ consumeCount }}</span>
      </div>
      <div class="tableBtn">
        <Button type="primary" @click="start" v-if="!on">接收</Button>
        <Button type="primary" @click="stop" style="margin-left: 20px" v-else>停止</Button>
        <Button type="primary" @click="autoScroll" style="margin-left: 20px" :ghost="autoScrollToBottom">滚动</Button>
        <Button icon="ios-trash-outline" type="error" @click="clear" style="margin-left: 20px">清空</Button>
      </div>
    </div>
    <div class="consumer-body" ref="body">
      <div :key="index" v-for="(item,index) in message">
        <div class="content">
          <div class="content-number">
            <span>{{ index + 1}}</span>
          </div>
          <div class="content-txt" v-html="getContent(item)"></div>
        </div>
      </div>
      <Row v-if="on">
        <Spin style="width: 100%">
          <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
          <div>正在加载中，请稍等</div>
        </Spin>
      </Row>
    </div>
  </div>
</template>

<script>
import {
  getKafkaAddress
} from '@/api/kafka/kafka'
export default {
  name: 'consumer.vue',
  data () {
    return {
      on: false,
      address: null,
      message: [],
      websocket: null,
      disabled: false,
      autoScrollToBottom: true,
      autoBreak: true,
      filter: false,
      consumeCount: 0,
      groupList: []
    }
  },
  created () {
    this.getAddress()
  },
  props: ['topic', 'broker', 'sourceId', 'group', 'mode', 'keyword'],
  watch: {
    message () {
      if (this.autoScrollToBottom) {
        console.log('123123')
        this.$nextTick(() => {
          this.$refs.body.scrollTop = this.$refs.body.scrollHeight
        })
      }
    }
  },
  methods: {
    getContent (item) {
      if (this.keyword != '') {
        const p = item.split(this.keyword).join(`<span style="color: red;font-weight: 700;background-color: #F8B950;padding: 0 0px;">${this.keyword}</span>`)
        // console.log(p)
        return p
      } else {
        return item
      }
    },
    autoScroll () {
      this.autoScrollToBottom = !this.autoScrollToBottom
      if (this.autoScrollToBottom) {
        this.$nextTick(() => {
          this.$refs.body.scrollTop = this.$refs.body.scrollHeight
        })
      }
    },
    autoChangeLine () {
      this.autoBreak = !this.autoBreak
    },
    clear () {
      this.message = []
      this.consumeCount = 0
    },
    start () {
      if (this.broker == null || this.broker == '' || this.topic == null || this.topic == '') {
        this.$message({
          showClose: true,
          message: '请先选择kafka和topic',
          type: 'error'
        })
        return
      }
      if (/[\u4E00-\u9FA5]/g.test(this.group)) {
        this.$message({
          showClose: true,
          message: 'group不允许输入中文字符！',
          type: 'error'
        })
        return
      }
      if (this.group == null || this.group == '' || this.group.trim() == '') {
        this.$message({
          showClose: true,
          message: '请先输入group',
          type: 'error'
        })
        return
      }
      this.$emit('changeDisabled', true)
      this.on = true

      this.consumeCount = 0
      if ('WebSocket' in window) {
        let url = `ws://${this.address}/push/websocket?topic=${this.topic}&broker=${this.broker}&group=${this.group}&offset=${this.mode}`
        console.log(url)
        this.websocket = new WebSocket(url)
        this.initWebSocket()
      } else {
        this.$message({
          showClose: true,
          message: '当前浏览器 不支持',
          type: 'error'
        })
        // alert('当前浏览器 不支持')
      }
    },
    stop () {
      this.$emit('changeDisabled', false)
      this.on = false
      this.websocket.close()
    },
    scroll () {
      this.$nextTick(() => {
        this.$refs.frame.scrollTop = 100000
      })
    },
    getAddress () {
      getKafkaAddress()
      // this.$http.post('/api/kafka/getIp')
        .then((response) => {
          this.address = response.data
        }).catch((error) => {
        })
    },
    initWebSocket () {
      // 连接错误
      this.websocket.onerror = () => {
        console.log('WebSocket连接发生错误   状态码：' + this.websocket.readyState)
      }
      // 连接成功
      this.websocket.onopen = () => {
        console.log('WebSocket连接成功    状态码：' + this.websocket.readyState)
      }
      // 收到消息的回调
      this.websocket.onmessage = (event) => {
        this.consumeCount = this.consumeCount + 1
        // if (this.filter && event.data.indexOf(this.keyword) == -1) {
        //   return
        // }

        this.message.push(event.data)
        if (this.autoScrollToBottom) { this.scroll() }
      }
      // 连接关闭的回调
      this.websocket.onclose = () => {
        console.log('WebSocket连接关闭    状态码：' + this.websocket.readyState)
      }
      // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
      window.onbeforeunload = () => {
        this.websocket.close()
      }
    }
  }
}
</script>

<style scoped lang="less">
.consumer{
  height: 100%;

  &-body{
    height: calc(100% - 48px);
    margin-top: 16px;
    padding-bottom: 5px;
    overflow: auto;
  }
  .scrollbar{
    width: 30px;
    height: 300px;
    margin: 0 auto;

  }
  &-body::-webkit-scrollbar {/*滚动条整体样式*/
    width: 10px;     /*高宽分别对应横竖滚动条的尺寸*/
    height: 1px;
  }
  &-body::-webkit-scrollbar-thumb {/*滚动条里面小方块*/
    border-radius: 10px;
    -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
    background: rgb(81,90,110);
  }
  &-body::-webkit-scrollbar-track {/*滚动条里面轨道*/
    -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
    border-radius: 10px;
    background: #EDEDED;
  }
  &-title{
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .content{
    margin-top: 16px;
    width: 100%;
    height: 54px;
    background: #F9E0C7;
    border-radius: 3px 3px 3px 3px;
    opacity: 1;
    display: flex;
    align-items: center;
    &-number{
      min-width: 20px;
      margin-left: 24px;
      height: 20px;
      background: #F7C797;
      border-radius: 0px 0px 0px 0px;
      text-align: center;
      span{
        font-size: 14px;
        font-family: PingFang SC-Regular, PingFang SC;
        font-weight: 400;
        color: #000000;
        line-height: 22px;
      }
    }
    &-txt{
      margin-left: 12px;
      height: 20px;
      font-size: 14px;
      font-family: PingFang SC-Regular, PingFang SC;
      font-weight: 400;
      color: #000000;
      line-height: 22px;
    }
  }
}

::v-deep .ivu-spin-main{
  display: flex;
  justify-content: center;
  margin-top: 20px;
  .ivu-spin-text{
    display: flex;
    justify-content: center;
    div{
      margin-left: 9px;
    }
  }
}
</style>
