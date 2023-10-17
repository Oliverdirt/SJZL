<template>
  <div style="width: 100%; height: 90%" ref="mychart"></div>
</template>

<script>
import * as echarts from 'echarts';
export default {
  props: {
    titleData: {
      type: Object,
      require: true
    }
  },
  data () {
    return {
      chartRef: null
    }
  },
  watch: {
    titleData: {
      immediate: false,
      handler (val) {
        // 容错数据为空
        // const keys = Object.keys(val)
        // if (!keys) return
        this.renderChart()
      }
    }
  },
  methods: {
    renderChart () {
      if (!this.chartRef) {
        this.initMyChart()
      } else {
        const option = this.getOption()
        this.chartRef.setOption(option, true)
      }
    },
    initMyChart () {
      // 基于准备好的dom，初始化echarts实例
      this.chartRef = echarts.init(this.$refs.mychart)
      const option = this.getOption()
      // 指定图表的配置项和数据
      // 使用刚指定的配置项和数据显示图表。
      this.chartRef.setOption(option)
      window.addEventListener('resize', () => {
        if (this.chartRef) this.chartRef.resize()
      })
    },
    getOption () {
      const keyVal = {
        cpu: 'CPU空闲容量',
        mem: '内存空闲容量',
        jvm: 'jvm空闲容量'
      }
      const seriesData = Object.keys(this.titleData).reduce((pre, key) => {
        const itemData = this.titleData[key] // Array
        const data = itemData.map(item => item.free)
        pre.push({
          name: keyVal[key],
          type: 'line',
          // smooth: true,
          areaStyle: {},
          data
        })
        return pre
      }, [])
      const xAxisData = this.titleData.cpu.map(item => item.time)
      const newxAxisData = xAxisData.map((item, i) => {
        return item.substr(5, item.length - 8)
      })
      let option = {
        title: {
          text: ''
        },
        tooltip: {
          trigger: 'axis',
          formatter:
            // '{b0}<br/>{a0}: {c0}%<br/>{a1}: {c1}%<br/>{a2}: {c2}%<br/>'
            ''
        },
        legend: {
          data: [
            'CPU空闲容量',
            '内存空闲容量',
            'jvm空闲容量'
          ]
        },
        grid: {
          left: '4%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis:
          {
            type: 'category',
            boundaryGap: false,
            data: newxAxisData
          },
        yAxis:
          {
            type: 'value'
            // axisLabel: {
            //   show: true,
            //   interval: 'auto',
            //   formatter: '{value} %' // 纵坐标百分比
            // }
          },
        series: seriesData
      }
      return option
    }
  }
}
</script>

<style></style>
