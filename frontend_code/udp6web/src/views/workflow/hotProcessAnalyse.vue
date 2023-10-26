<template>
  <div class="hotProcessAnalyse" style="height: 450px;">
    <el-card style="height: 100%; width: 100%">
      <div>
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="当日热门流程" name="first"></el-tab-pane>
          <el-tab-pane label="当月热门流程" name="second"></el-tab-pane>
          <el-tab-pane label="当季热门流程" name="third"></el-tab-pane>
          <el-tab-pane label="当年热门流程" name="fourth"></el-tab-pane>
        </el-tabs>
      </div>
      <div style="display:flex;position: relative;">
        <div style="padding-right:10px">
          <el-date-picker clearable v-model="dateVal" type="datetimerange" range-separator="至" start-placeholder="开始日期"
            end-placeholder="结束日期"></el-date-picker>
        </div>
        <div class="grid-content bg-purple">
          <el-form :model="pageData">
            <el-row>
              <el-col :span="18" style="padding-right:20px">
                <el-form-item>
                  <el-select v-model="pageData.pageSize" placeholder="请选择" @change="changeFn">
                    <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <div style="position: absolute;right: 0px">
          <!-- <el-button type="primary" @click.native="dateChangeFn">查询</el-button>
          <el-button type="default" @click="resetFn">重置</el-button> -->
        </div>
      </div>
      <div style="display: flex">
        <div class="echartsPancake" ref="echartsPancake" style="height: 300px; width: 50%"></div>
        <div class="echartsTable" style="width: 50%;">
          <el-table :data="tableData" style="width: 100%" max-height="300">
            <el-table-column type="index" width="50" label="序号">
            </el-table-column>
            <el-table-column prop="processName" label="流程名称" align="center">
            </el-table-column>
            <el-table-column prop="count" label="实例数" align="center">
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script>
import * as echarts from 'echarts'
import moment from 'moment'
import request from '@/api/workflow/request'

export default {
  data () {
    return {
      tableData: [],
      activeName: 'third',
      options: [
        {
          value: 3,
          label: '前3名'
        },
        {
          value: 5,
          label: '前5名'
        },
        {
          value: 10,
          label: '前10名'
        },
        {
          value: 15,
          label: '前15名'
        },
        {
          value: 30,
          label: '前30名'
        }
      ],
      pageData: {
        pageNum: 1,
        pageSize: 5,
        startTime: '2022-08-01 00:00:00',
        endTime: '2022-09-08 00:00:00'
      },
      dateVal: '',
      seriesData: []
    }
  },
  created () {
    this.hotProcessAnalyse()
  },
  methods: {
    changeFn (row) {
      console.log(row, 'row')
      console.log(this.dateVal,'this.dateVal');
      this.pageData.pageSize = row
      const startTime = this.dateVal ? this.$moment(this.dateVal[0]).format('YYYY-MM-DD HH:mm:ss') : ''
      const endTime = this.dateVal ? this.$moment(this.dateVal[1]).format('YYYY-MM-DD HH:mm:ss') : ''
      this.hotProcessAnalyse({ startTime, endTime }, Boolean(startTime && endTime))
    },
    resetFn () {
      window.location.reload()
    },
    handleClick () {
      let params = {
        'pageParam.page': this.pageData.pageNum,
        'pageParam.size': this.pageData.pageSize
      }
      if (this.activeName === 'first') {
        params.startTime = moment(moment().startOf('day').valueOf()).format('YYYY-MM-DD HH:mm:ss')
        params.endTime = moment(moment().valueOf()).format('YYYY-MM-DD HH:mm:ss')
      } else if (this.activeName === 'second') {
        params.startTime = moment(moment().month(moment().month()).startOf('month').valueOf()).format('YYYY-MM-DD HH:mm:ss')
        params.endTime = moment(moment().month(moment().month()).endOf('month').valueOf()).format('YYYY-MM-DD HH:mm:ss')
      } else if (this.activeName === 'third') {
        params.startTime = moment(moment().quarter(moment().quarter()).startOf('quarter').valueOf()).format('YYYY-MM-DD HH:mm:ss')
        params.endTime = moment(moment().quarter(moment().quarter()).endOf('quarter').valueOf()).format('YYYY-MM-DD HH:mm:ss')
      } else if (this.activeName === 'fourth') {
        params.startTime = moment(moment().year(moment().year()).startOf('year').valueOf()).format('YYYY-MM-DD HH:mm:ss')
        params.endTime = moment(moment().year(moment().year()).endOf('year').valueOf()).format('YYYY-MM-DD HH:mm:ss')
      }
      // this.$http.get('/api/flow/count/hotFlow', { params })
      request.getHotFlowList({ params })
        .then((res) => {
          this.tableData = res.data.result.flowHotProcList.list
          this.initMychart()
        })
    },
    hotProcessAnalyse (option, isselect = false) {
      if (!isselect) { // 没选日期根据tab来
        this.handleClick()
      } else { // 选了日期以日期为准
        let params = {
          'pageParam.page': this.pageData.pageNum,
          'pageParam.size': this.pageData.pageSize,
          startTime: option.startTime,
          endTime: option.endTime
        }
        // this.$http
        //   .get('/api/flow/count/hotFlow', { params: params })
        request.getHotFlowList({ params })
          .then((res) => {
            this.tableData = res.data.result.flowHotProcList.list
            this.initMychart()
          })
      }
    },
    // 时间
    dateChangeFn () {
      // const startTime = this.dateVal.length ? this.dateVal[0] : ''
      // const endTime = this.dateVal.length ? this.dateVal[1] : ''
      const startTime = this.dateVal.length ? this.$moment(this.dateVal[0]).format('YYYY-MM-DD HH:mm:ss') : ''
      const endTime = this.dateVal.length ? this.$moment(this.dateVal[1]).format('YYYY-MM-DD HH:mm:ss') : ''
      this.hotProcessAnalyse({ startTime, endTime }, Boolean(startTime && endTime))
    },
    initMychart () {
      console.log(this.tableData, 'this.tableData')
      this.seriesData = []
      // for (const item of this.tableData) {
      //   this.seriesData.push({
      //     value: item.count,
      //     name: item.processName
      //   })
      // }
      this.seriesDataX = this.tableData.map(item => item.processName)
      this.seriesDataY = this.tableData.map(item => item.count)
      console.log(this.seriesDataX, 'this.seriesDataX')
      console.log(this.seriesData, 'this.seriesData')
      const mychart = echarts.init(this.$refs.echartsPancake)
      let option = {
        title: {
          text: '热门流程'
        },
        xAxis: {
          type: 'category',
          data: this.seriesDataX,
          axisLabel: {
            show: true,
            align: 'left',
            interval: 0,
            rotate: -25
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: this.seriesDataY,
            type: 'bar',
            barWidth: 40,
            itemStyle: {
              barBorderRadius: [5, 5, 2, 2], // 柱体圆角
              color: new echarts.graphic.LinearGradient(
                0, 0, 0, 1, [{ // 代表渐变色从正上方开始
                  offset: 0, // offset范围是0~1，用于表示位置，0是指0%处的颜色
                  color: '#9f2cf0'
                }, // 柱图渐变色
                {
                  offset: 0.6, // 指100%处的颜色
                  color: '#287adf'
                }
                ]
              )
            }
          }
        ]
      }
      mychart.setOption(option)
      window.addEventListener('resize', function () {
        mychart.resize()
      })
    }
  }
}
</script>
<style scoped>
.el-button--primary,
.el-button--default {
  padding: 8px 15px 8px 15px;
}
</style>
