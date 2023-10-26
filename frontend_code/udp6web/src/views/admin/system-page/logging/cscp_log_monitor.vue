<style lang="less">
@import '../../styles/common.less';
@import 'table.less';
</style>
<template>
    <div class="logging">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
         <div class="searchArea" >
            <Row :gutter="48">
              <Col span="26" style="text-align: right;">
                  <Button @click="openSocket" type="primary" style="margin-right:10px"
                  >开启日志</Button
                  >
                  <Button @click="closeSocket" type="default">关闭日志</Button>
              </Col>
            </Row>
        </div>
      </Card>
        <div id="log-container" style="height: 600px; overflow-y: scroll; background: #333; color: #aaa; padding: 10px;margin-top: 16px;" ref="log_container">
            <div ref = 'container' v-html="htmlContent"></div>
        </div>
    </div>

</template>
<script>
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'
import Util from '@/libs/util'
export default {
  data () {
    return {
      stompClient: null,
      container1: '',
      htmlContent: ''
    }
  },
  created () {
    this.$nextTick(() => {
      this.openSocket()
    })
  },
  destroyed () {
    this.closeSocket()
  },

  methods: {
    openSocket () {
      let that = this
      if (this.t) clearTimeout(this.t)
      if (this.stompClient == null) {
        let socket = new SockJS(`${Util.baseUrl}/websocket?token=kl`)
        this.stompClient = Stomp.over(socket)
        this.stompClient.connect({ token: 'kl' }, (frame) => {
          this.stompClient.subscribe('/topic/pullLogger', function (event) {
            let content = JSON.parse(event.body)
            // document.querySelector('#log-container div').innerHTML += content.timestamp + ' ' + content.level + ' --- [' + content.threadName + '] ' + content.className + '   :' + content.body + '<br/>'
            that.htmlContent += content.timestamp + ' ' + content.level + ' --- [' + content.threadName + '] ' + content.className + '   :' + content.body + '<br/>'
          }, {
            token: 'kltoen'
          })
        })
      }
      this.t = setTimeout(() => {
        this.closeSocket()
      }, 10000)
    },
    closeSocket () {
      if (this.stompClient != null) {
        this.stompClient.disconnect()
        this.stompClient = null
      }
    }
  }

}

</script>
<style lang="less" scoped>
@import "../../../admin/styles/formStyle.less";
.btn-group {
  float: right;
}
.logging{
    margin-bottom: 20px;
}
</style>
