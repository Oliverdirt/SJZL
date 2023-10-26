<template>
  <div
    class="echartsNum"
    ref="echartsNum"
    style="height: 100%; width: 100%"
  ></div>
</template>
<script>
import * as echarts from 'echarts'
export default {
  props: {
    echartsNumData: {
      type: Object,
      require: true
    }
  },
  watch: {
    echartsNumData: {
      // deep:true,
      immediate: false,
      handler (val) {
        this.initMychart()
      }
    }
  },
  data () {
    return {}
  },
  mounted () {
    this.initMychart()
  },
  methods: {
    initMychart () {
      const mychart = echarts.init(this.$refs.echartsNum)
      let option = {
        title: {
          text: '任务趋势',
          x: 'left'
        },
        tooltip: {
          trigger: 'axis',
          backgroundColor: 'white',
          textStyle: {
            color: '#000'
          }
        },
        legend: {
          icon: 'roundRect',
          right: 10
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [{
          name: '',
          type: 'category',
          axisPointer: {
            type: 'shadow',
            shadowStyle: {
              color: 'blue',
              opacity: 0.05

            }
          },
          boundaryGap: false,
          data: this.echartsNumData.dateList
        }],
        yAxis: [{
          name: '',
          type: 'value',
          splitLine: {
            show: true,
            lineStyle: {
              type: 'dashed'

            }

          }
        }],
        series: [{
          name: '发起流程',
          type: 'line',
          color: '#8cd573',
          symbol: 'circle',
          symbolSize: 6,
          areaStyle: {
            // opacity: 0.1,
            normal: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: '#8cd573'
              },
              {
                offset: 0.8,
                color: 'white'
              }
              ], false)
            }
          },
          data: this.echartsNumData.procInstanceStartList
        },
        {
          name: '结束流程',
          type: 'line',
          // smooth: true,
          color: '#ffab61',
          symbolSize: 6,
          symbol: 'circle',
          areaStyle: {
            // opacity: 0.1,
            normal: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: '#ffab61'
              },
              {
                offset: 0.8,
                color: 'white'
              }
              ], false)
            }
          },
          data: this.echartsNumData.procInstanceEndList
        },
        {
          name: '新增任务',
          type: 'line',
          color: 'rgba(10,219,250,1)',
          symbolSize: 6,
          symbol: 'circle',
          areaStyle: {
            // opacity: 0.1,
            normal: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: 'rgba(10,219,250,1)'
              },
              {
                offset: 0.8,
                color: 'white'
              }
              ], false)
            }
          },
          data: this.echartsNumData.taskStartList
        },
        {
          name: '结束任务',
          type: 'line',
          // smooth: true,
          color: 'rgba(25,163,223,1)',
          symbolSize: 6,
          symbol: 'circle',
          areaStyle: {
            // opacity: 0.1,
            normal: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: 'rgba(25,163,223,1)'
              },
              {
                offset: 0.8,
                color: 'white'
              }
              ], false)
            }
          },
          data: this.echartsNumData.taskEndList
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
<style scoped></style>
