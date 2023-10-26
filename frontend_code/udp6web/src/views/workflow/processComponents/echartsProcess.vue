<template>
  <div
    class="echartsProcess"
    ref="echartsProcess"
    style="height: 100%; width: 100%"
  ></div>
</template>
<script>
import * as echarts from 'echarts'
export default {
  props: {
    echartsProcessData: {
      type: Object,
      require: true
    }
  },
  watch: {
    echartsProcessData: {
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
      const mychart = echarts.init(this.$refs.echartsProcess)
      var a = this.echartsProcessData.procCountProp
      // procCountProp processInstanceTodo processInstanceDown
      let option = {
        title: [
          {
            text: '流程完成率'
          },
          {
            text: this.echartsProcessData.procCountProp + '%',
            textStyle: {
              color: 'black',
              fontSize: 30
            },
            subtext: '流程完成率',
            subtextStyle: {
              width: '100px',
              color: 'grey',
              align: 'center'
            },
            itemGap: 0, // 主副标题距离
            left: '130px',
            top: '120px'
          }
        ],
        angleAxis: {
          max: 100, // 满分
          clockwise: false, // 逆时针
          // 隐藏刻度线
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            show: false
          },
          splitLine: {
            show: false
          }
        },
        radiusAxis: {
          type: 'category',
          // 隐藏刻度线
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            show: false
          },
          splitLine: {
            show: false
          }
        },
        polar: {
          center: ['160px', '140px'],
          radius: '100%' // 图形大小
        },
        series: [{
          type: 'bar',
          data: [{
            value: a,
            itemStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [{
                  offset: 0,
                  color: '#32a1ff'
                }, {
                  offset: 1,
                  color: '#286edd'
                }])
              }
            }
          }],
          name: '已完成:   ' + this.echartsProcessData.processInstanceTodo + ' 个',
          coordinateSystem: 'polar',
          roundCap: true,
          barWidth: 28,
          barGap: '-100%', // 两环重叠
          z: 2
        }, { // 灰色环
          type: 'bar',
          name: '进行中:   ' + this.echartsProcessData.processInstanceDown + ' 个',
          data: [{
            value: 100,
            itemStyle: {
              color: '#eff4ff',
              shadowColor: 'rgba(0, 0, 0, 0.2)',
              shadowBlur: 15,
              shadowOffsetY: 2
            }
          }],
          coordinateSystem: 'polar',
          roundCap: true,
          barWidth: 20,
          barGap: '-85%', // 两环重叠
          z: 1
        }
        ],
        color: ['#009DFF', '#edf2fd'],
        legend: {
          show: true,
          left: '300',
          top: '95',
          orient: 'vertical',
          itemGap: 50,
          data: ['已完成:   ' + this.echartsProcessData.processInstanceTodo + ' 个', '进行中:   ' + this.echartsProcessData.processInstanceDown + ' 个']
        }

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
