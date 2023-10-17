<template>
<div class="homePage">
  <div class="homeTop">
    <p>统一研发框架</p>
    <p>低代码+无代码开发，让企业应用构建更简单</p>
    <div class="homeBtn">
      <a href="http://udp6hxwiki.devops.com">了解更多</a>
    </div>
  </div>
  <div class="homeBottom">
    <div class="homeForm" @click="homeFormBtn(1)">
      <img src="@/assets/Ellipse 2.svg" alt="">
      <div class="homeFormMes">
        <p>基础表单</p>
        <p>用于承载用户信息录入的文本框， 常用于表单、对话框等场景。</p>
      </div>
    </div>
    <div class="homeForm" @click="homeFormBtn(2)">
      <img src="@/assets/Ellipse 1.svg" alt="">
      <div class="homeFormMes">
        <p>基础列表</p>
        <p>用连续的列来显示多行元素，常用于具 有相同构成的模块批量展示。</p>
      </div>
    </div>
    <div class="homeForm" @click="homeFormBtn(3)">
      <img src="@/assets/Ellipse 3.svg" alt="">
      <div class="homeFormMes">
        <p>基础表单控件</p>
        <p>输入框、选择器、单选框、多选框等控 件组成，用以收集、校验、提交数据。</p>
      </div>
    </div>
  </div>
</div>
</template>
<script>
import MultiRank from './multi-column/index.vue'
import FormTable from './table-demo/index.vue'
import FormGroup from './form-group/index.vue'
import ChangeTheme from '@/views/theme/theme'
export default {
  components: { MultiRank, FormTable, FormGroup },
  data () {
    return {
      isHidden: true,
      timeout: null
    }
  },
  mounted () {
    this.goHidden()
    let theme = JSON.parse(localStorage.getItem('biyi-theme'))
    console.log('theme', theme)
    if (theme) {

    }
  },
  methods: {
    homeFormBtn (num) {
      if (num === 1) {
        this.$router.push({
          path: '/multi-column'
        })
      } else if (num === 2) {
        this.$router.push({
          path: '/table-demo'
        })
      } else if (num === 3) {
        this.$router.push({
          path: '/form-group'
        })
      }
    },
    toggleHidden () {
      this.$store.state.shake.isshake = true
    },
    goHidden () {
      this.timeout = setTimeout(() => {
        this.isHidden = false
        this.$store.state.shake.isshake = true
      }, 1000 * 60 * 2)
    }
  },
  beforeDestroy () {
    clearTimeout(this.timeout)
    this.$store.state.shake.isshake = false
  }
}
</script>
<style scoped lang="less">
.homePage{
  width: 100%;
  height: 100%;
  background: url('@/assets/shouye.png') no-repeat;
  background-size: cover;
  .homeTop{
    padding: 65px 0px 0px 60px;
    p:nth-child(1) {
      font-size: 55px;
      font-weight: bold;
      color: black;
      letter-spacing: 10px
    }
    p:nth-child(2){
      color: grey;
      margin: 20px 0px 20px 0px;
    }
    .homeBtn{
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100px;
      height: 26px;
      background-image: linear-gradient(to right, #4c65ff,#aed4fd);
      border-radius: 15px;
      a{
        color: white;
      }
    }
  }
  .homeBottom{
    display: flex;
    margin: 140px 0px 0px 0px;
    justify-content: space-around;
    .homeForm{
      cursor: pointer;
      display: flex;
      align-items: center;
      width: 300px;
      height: 100px;
      border-radius: 10px;
      background-color: white;
      margin: 0px 20px;
      .homeFormMes{
        margin-right: 10px;
        p:nth-child(1){
          font-weight: bold;
          color: black;
          margin-bottom: 5px;
        }
        p:nth-child(2){
          font-size: 12px;
          color: grey;
        }
      }
    }
  }
}

</style>
