<template>
  <div class="echart">
    <div class="serveDate">
      <el-row>
        <el-col :span="6">
          <div class="grid-content bg-purple">
            <el-date-picker
              v-model="value1"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              @change="dateChangeFn"
            ></el-date-picker>
          </div>
        </el-col>
        <el-col :span="9"
          ><div class="grid-content bg-purple">&nbsp;</div></el-col
        >
        <el-col :span="6"> <div class="grid-content bg-purple"></div></el-col>
      </el-row>
    </div>
    <div class="echarts">
      <div class="echartsUse">
        <EchartsUse :titleData="tableData"></EchartsUse>
      </div>
      <div class="echartsFree">
        <EchartsFree :titleData="tableData"></EchartsFree>
      </div>
    </div>
    <div class="serverTable">
      <div class="tableRam">
        <el-collapse v-model="activeNames" @change="handleChange">
          <el-collapse-item title="磁盘信息" name="1">
            <div>
              <Table border :messageTable="tableDiskData"></Table>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
      <div class="tableMessage">
        <el-collapse v-model="activeNames" @change="handleChange">
          <el-collapse-item title="服务器信息" name="1">
            <div>
              <Table border :messageTable="tableServerData"></Table>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </div>
  </div>
</template>
<script>
import EchartsUse from './components/EchartsUse.vue'
import EchartsFree from './components/EchartsFree.vue'
import Table from './components/Table.vue'
import {
  getSysMonitorMsgUp10,
  getSysMonitorMsgByDateTime

} from '@/api/system/monitor'
export default {
  components: {
    EchartsUse,
    EchartsFree,
    Table
  },
  data () {
    return {
      activeNames: ['1'],
      value1: '',
      tableData: {
        cpu: [],
        jvm: [],
        mem: []
      },
      messageData: {
        cpu: [],
        jvm: [],
        mem: []
      },
      timer: null,
      intervalTime: 6, // 秒
      tableDiskData:[],
      tableServerData:[]
    }
  },
  methods: {
    renderChatWithMoment () {
      this.timer = setInterval(() => {
        this.getChartData()
      }, this.intervalTime * 1000)
    },
    getChartData (option, isSelect = false) {
      let startTime, endTime
      if (!isSelect) {
        getSysMonitorMsgUp10()
        // this.$http
        //   .get("/api/monitor/server/getMsgUp10")
          .then((res) => {
            if (res.data.code === 200) {
              this.tableData = res.data.data
            }
          })
          .catch((err) => {
            console.log(err)
          })
      } else {
        startTime = option.startTime
        endTime = option.endTime
        const queryStr = `startTime=${encodeURIComponent(
          startTime
        )}&endTime=${encodeURIComponent(endTime)}`
        getSysMonitorMsgByDateTime(queryStr)
        // this.$http
        //   .get('/api/monitor/server/getMsg?' + queryStr)
          .then((res) => {
            console.log(res, 'res')
            if (res.data.code === 200) {
              this.tableData = res.data.data
            }
          })
          .catch((err) => {
            console.log(err)
          })
      }
    },
    handleChange (val) {
      console.log(val)
    },
    dateChangeFn (val) {
      console.log(val, 'val')
      if (!val) {
        this.getChartData()
        this.renderChatWithMoment()
      } else {
        const startTime = this.$moment(val[0]).format('YYYY-MM-DD HH:mm:ss')
        const endTime = this.$moment(val[1]).format('YYYY-MM-DD HH:mm:ss')
        clearInterval(this.timer)
        this.timer = null
        this.getChartData({ startTime, endTime }, true)
      }
    },
    getMessageFn () {
      let params = {}
      this.$http.get('/api/monitor/server/getInfo', params).then((res) => {
        if (res.data.code === 200) {
          this.messageData = res.data.data
          this.tableDiskData = [
            {
              date: '磁盘类型',
              name: this.messageData.sysFiles[1].sysTypeName
            },
            {
              date: '磁盘总容量',
              name: this.messageData.sysFiles[1].total
            },
            {
              date: '磁盘空闲容量',
              name: this.messageData.sysFiles[1].free
            },
            {
              date: '磁盘已使用容量',
              name: this.messageData.sysFiles[1].used
            },
            {
              date: '磁盘资源的使用率',
              name: this.messageData.sysFiles[1].usage + '%'
            }
          ]
          this.tableServerData = [
              {
                date: '主机名称',
                name: this.messageData.sys.computerName
              },
              {
                date: '主机IP',
                name: this.messageData.sys.computerIp
              },
              {
                date: '主机类型',
                name: this.messageData.sys.osName
              },
              {
                date: '显卡类型',
                name: this.messageData.sys.osArch
              },
              {
                date: '所在路径',
                name: this.messageData.sys.userDir
              }
          ]
        }
      })
    }
  },
  created () {
    this.getChartData()
    this.renderChatWithMoment()

    this.getMessageFn()
  },
  beforeDestroy () {
    clearInterval(this.timer)
    this.timer = null
  }
}
</script>
<style lang="less" scoped>
@import "../admin/styles/formStyle.less";

::v-deep .el-date-editor {
  margin-top: 10px;
  margin-left: 30px;
}
.echarts {
  height: 330px;
  box-shadow: 0 0 5px 1px #ccc;
  margin: 30px 30px 0 30px;
  display: flex;
  justify-content: space-between;
  .echartsUse {
    width: 45%;
  }
  .echartsFree {
    width: 45%;
    margin-right: 40px;
  }
}
.serverTable {
  display: flex;
  justify-content: space-between;
  .tableRam {
    width: 45%;
    box-shadow: 0 0 5px 1px #ccc;
    margin: 30px 10px 30px 30px;
  }
  .tableMessage {
    width: 45%;
    box-shadow: 0 0 5px 1px #ccc;
    margin: 30px 30px 30px 10px;
  }
  ::v-deep .el-collapse-item__header {
    padding-left: 12px;
    font-size: 20px;
    color: #606266;
    background-color: #f8f8f9;
  }
}
</style>
