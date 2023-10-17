<template>
  <div class="processStatistics">
    <div class="processTop">
      <div class="statisticsLeft">
        <div class="processModel">
          <span>流程模板</span>
          <span>{{ this.modelCount ? this.modelCount : 0 }}</span>
        </div>
        <div class="processExample">
          <span>流程实例</span>
          <span>{{ this.processInstanceCount ? this.processInstanceCount : 0 }}</span>
        </div>
      </div>
      <div class="statisticsRight">
        <EchartsNum :echartsNumData="echartsNumData" />
      </div>
    </div>
    <el-card style="height: 100%;margin-bottom: 20px">
      <div class="processMiddle">
        <div class="middleLeft">
          <EchartsProcess :echartsProcessData="echartsProcessData" />
        </div>
        <div class="middleRight">
          <EchartsTask :echartsProcessData="echartsProcessData" />
        </div>
      </div>
      <!--列表-->
      <div>
        <el-table :data="tableData" style="width: 100%;margin-bottom: 10px">
          <el-table-column type="index" width="50" label="序号">
          </el-table-column>
          <el-table-column prop="name" label="流程名称" align="center">
          </el-table-column>
          <el-table-column prop="processInstanceDown" label="已完成流程"  align="center">
          </el-table-column>
          <el-table-column prop="processInstanceAll" label="全部流程"  align="center">
          </el-table-column>
          <el-table-column prop="procCountProp"  label="流程完成率" align="center">
          </el-table-column>
          <el-table-column prop="taskDown" label="已完成任务" align="center">
          </el-table-column>
          <el-table-column prop="taskAll" label="全部任务" align="center">
          </el-table-column>
          <el-table-column prop="taskCountProp" label="任务完成率" align="center">
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageData.pageNum"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="5"
          layout="->, total, sizes, prev, pager, next, jumper"
          :total="pageData.total">
        </el-pagination>
      </div>
    </el-card>
    <!-- <el-card style="height: 100%"> -->
      <div class="processBottom">
        <HotProcessAnalyse></HotProcessAnalyse>
      </div>
    <!-- </el-card> -->
  </div>
</template>
<script>
// import EchartsPancake from './processComponents/echartsPancake.vue'
import EchartsProcess from './processComponents/echartsProcess.vue'
import EchartsTask from './processComponents/echartsTask.vue'
import EchartsNum from './processComponents/echartsNum.vue'
import HotProcessAnalyse from './hotProcessAnalyse.vue'
import request from '@/api/workflow/request'

export default {
  components: {
    // EchartsPancake,
    EchartsProcess,
    EchartsNum,
    HotProcessAnalyse,
    EchartsTask
  },
  data () {
    return {
      echartsNumData: {
        dateList: [],
        procInstanceEndList: [],
        procInstanceStartList: [],
        taskEndList: [],
        taskStartList: []
      },
      echartsProcessData: {
        procCountProp: '0',
        processInstanceDown: '0',
        processInstanceTodo: '0',
        taskCountProp: '0',
        taskTodo: '0',
        taskDown: '0'
      },
      pageData: {
        total: 0,
        pageNum: 1,
        pageSize: 5
      },
      modelCount: '',
      processInstanceCount: '',
      tableData: []
    }
  },
  created () {
    this.echartsNum()
    this.echartsProcess()
    this.processMessage()
  },
  methods: {
    // 任务趋势折线图
    echartsNum () {
      // this.$http.get('/api/flow/count/queryTrendMessage')
      request.getTrendMessageList()
        .then((res) => {
          this.echartsNumData = res.data.result
        })
    },
    // 流程、任务完成率仪表盘图
    echartsProcess () {
      this.$http.get('/api/flow/count/queryPropMessage')
      request.getPropMessageList()
        .then((res) => {
          this.echartsProcessData = res.data.result
        })
    },
    // 流程概览统计信息/api/flow/count/queryMessage
    processMessage () {
      let params = {
        page: this.pageData.pageNum,
        size: this.pageData.pageSize
      }
      // this.$http.get('/api/flow/count/queryMessage', { params: params })
      request.getMessageList({ params: params })
        .then((res) => {
          this.modelCount = res.data.result.modelCount
          this.processInstanceCount = res.data.result.processInstanceCount
          this.tableData = res.data.result.flowModelCountMessageList.list
          this.pageData.total = res.data.result.flowModelCountMessageList.total
          console.log(res, 'r3')
        })
    },
    handleSizeChange (val) {
      this.pageData.pageSize = val
      this.processMessage()
    },
    handleCurrentChange (val) {
      this.pageData.pageNum = val
      this.processMessage()
    }
  }
}
</script>
<style lang="less" scoped>
@import "../admin/styles/formStyle.less";
.processStatistics {
  .processTop {
    display: flex;
    margin-bottom: 20px;
    .statisticsLeft {
      width: 320px;
      margin-left: -25px;
      margin-right: 20px;
      .processModel {
        display: flex;
        height: 90px;
        align-items: center;
        justify-content: center;
        background: url(./images/Group1122.svg) no-repeat 100% 100%;
        background-size: 100% 100%;
        margin-bottom: 20px;
        span:nth-child(1){
          color: white;
          font-size: 16px;
        }
        span:nth-child(2) {
          color: white;
          font-size: 28px;
          margin-left: 20px;
        }
      }
      .processExample {
        display: flex;
        height: 90px;
        align-items: center;
        justify-content: center;
        background: url(./images/Group1123.svg) no-repeat;
        background-size: 100% 100%;
        span:nth-child(1){
          color: white;
          font-size: 16px;
        }
        span:nth-child(2) {
          color: white;
          font-size: 28px;
          margin-left: 20px;
        }
      }
    }
    .statisticsRight {
      flex: 1;
      //
    }
  }
  .processMiddle {
    display: flex;
    width: 100%;
    height: 240px;
    margin-bottom: 20px;
    .middleLeft {
      width: 50%;
    }
    .middleRight {
      width: 50%;
    }
  }
  .processBottom {
    // display: flex;
    width: 100%;
    height: 100%;
  }
}
</style>
