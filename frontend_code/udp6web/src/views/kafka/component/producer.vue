<template>
  <div class="consumer">
    <div class="consumer-title">
      <div>
        <span>已发送消息条数：</span>
        <span>{{ messages.length }}</span>
      </div>
      <div class="tableBtn">
        <Button icon="ios-trash-outline" type="error" @click="clear" style="margin-left: 20px">清空</Button>
      </div>
    </div>
    <div class="consumer-body" ref="body">
      <div :key="index" v-for="(item,index) in messages">
        <div class="content">
          <div class="content-number">
            <span>{{ index + 1}}</span>
          </div>
          <div class="content-txt">{{ item.content }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  produceMsg
} from '@/api/kafka/kafka'
export default {
  name: 'producer',
  data () {
    return {
      messages: [],
      history: [],
      maxHistorySize: 10,
      cursor: null
    }
  },
  props: ['topic', 'sourceId', 'message', 'batch'],
  methods: {
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
    clear () {
      this.messages = []
    },
    scroll () {
      this.$nextTick(() => {
        this.$refs.body.scrollTop = 10000
      })
    },
    processHistory (message) {
      this.history.push(message)
      if (this.history.length > this.maxHistorySize) {
        this.history = this.history.slice(-this.maxHistorySize)
      }
      this.cursor = this.history.length - 1
    },
    produce () {
      if (this.sourceId == null || this.sourceId == '' || this.topic == null || this.topic == '') {
        this.$message({
          showClose: true,
          message: '请先选择kafka和topic',
          type: 'error'
        })
        return
      }

      if (!this.message || !this.message.replace(/\s*/g, '').length || this.message == null) {
        this.$message({
          showClose: true,
          message: '禁止发送空消息',
          type: 'error'
        })
        return
      }
      const m = this.message
      let data = new FormData()
      data.append('sourceId', this.sourceId)
      data.append('topic', this.topic)
      data.append('message', m)
      data.append('batch', this.batch)
      produceMsg(data)
      // this.$http.post('/api/kafka/produce', data)
        .then((response) => {
          this.messages.push({ content: m, success: true, batch: this.batch })
          this.$emit('processHistory', m)
          this.processHistory(m)
          this.scroll()
        }).catch((error) => {
          this.$message.error('发送失败')
          this.messages.push({ content: m, success: false, batch: this.batch })
        })
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
    height: 32px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .content{
    width: 100%;
    margin-top: 16px;
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
