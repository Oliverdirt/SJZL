<template>
  <div
    class="echartsPancake"
    ref="echartsPancake"
    style="height: 100%; width: 100%"
  ></div>
</template>
<script>
import * as echarts from "echarts";
export default {
  data() {
    return {};
  },
  mounted(){
    this.initMychart()
  },
  methods: {
    initMychart() {
      // var value = data.value
      const mychart = echarts.init(this.$refs.echartsPancake);
      let option = {
        color: ["#90f650", "#37a2da", "#5085f6", "#fd666d"],
        title: {
        text: "流程状态分析",
        left: "8%",
        top: "3%",
        textStyle:{
            fontSize: 15,
            color: 'black',
            fontWeight: 'normal',
        }  
        },
        tooltip: {
          trigger: "item",
        },
        legend: {
          bottom: "11%",
          left: "24%",
          itemWidth: 5,
          itemHeight: 5,
          orient: "horizontal",
          // itemGap:25,
          formatter: function(name){
            var index = 0;
            var clientlabels = ["流程正常","流程中止","流程结束","流程挂起"];
            var clientcounts = [40,30,20,10];
            clientlabels.forEach(function(value,i){
              if(value==name){
                index = i;
              }
            });
            return name + "   " +clientcounts[index]
          }
        },
        series: [
          {
            name: "流程分布",
            type: "pie",
            radius: ["40%", "60%"],
            center: ["54%", "45%"],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: "center",
            },
            // emphasis: {
            //   label: {
            //     show: true,
            //     fontSize: "40",
            //     fontWeight: "bold",
            //   },
            // },
            labelLine: {
              show: false,
            },
            data: [
              { value: 40, name: "流程正常" },
              { value: 30, name: "流程中止" },
              { value: 20, name: "流程结束" },
              { value: 10, name: "流程挂起" },
            ],
          },
        ],
      };
      mychart.setOption(option);
      window.addEventListener('resize',function(){
          mychart.resize();
      })
    },
  },
};
</script>
<style scoped></style>
