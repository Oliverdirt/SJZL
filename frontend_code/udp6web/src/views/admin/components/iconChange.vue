<template>
  <div ref="div">
    <Input :value="icon" :prefix="icon" :icon="arrType" placeholder="请选择图标"
           ref="input" @on-change="query" @click.native="clickFocus($event)"></Input>
    <transition name="fade">
      <Row class="scollClass" v-if="scollshow" ref="rowList">
        <div v-for="(item,index) in iconList" :key="index" @mousedown="scollListClick(item)"
             class="scollListClass" :class="active == index ? 'addclass' : ''">
          <div>
            <Icon :type="item" size="30"/>
            <span>{{ item }}</span>
          </div>
        </div>
      </Row>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'iconChange',
  data () {
    return {
      scollshow: false,
      iconTotal: 0,
      iconList: [],
      active: 0,
      queryList: [],
      queryFlag: false,
      arrType: 'ios-arrow-down',
      change: false
    }
  },
  props: {
    icon: {
      type: String
    }
  },
  mounted () {
    this.init()
  },
  watch: {
    iconList () {
      this.active = this.iconList.indexOf(this.icon)
    },
    scollshow (val) {
      this.arrType = val ? 'ios-arrow-up' : 'ios-arrow-down'
      if (!val) {
        this.$emit('update:icon', this.$refs.input.currentValue)
      }
    }
  },
  methods: {
    init () {
      this.iconList = this.$iconList
      this.iconTotal = this.$iconList.length
    },
    scollListClick (item) {
      this.$emit('update:icon', item)
      this.$emit('on-change')
      this.$refs.input.currentValue = item
      this.scollshow = false
    },
    tableShow () {
      this.scollshow = true
    },
    tableHide () {
      this.scollshow = false
    },
    clickFocus (event) {
      this.queryFlag = false
      this.iconTotal = this.$iconList.length
      this.active = this.iconList.indexOf(this.icon)
      event.stopPropagation ? event.stopPropagation() : (event.cancelBubble = true)
      this.scollshow ? this.tableHide() : this.tableShow()
      document.addEventListener('click', this.selectHideList, false)
    },
    clickblur () {
      this.scollshow = false
    },
    // 点击空白处关闭
    selectHideList (e) {
      this.$nextTick(() => {
        if (this.$refs.rowList && !this.$refs.rowList.$el.contains(e.target) &&
            !this.$refs.input.$el.contains(e.target) && !this.change) {
          this.scollshow = false
        }
        this.change = false
      })
    },
    query (item) {
      this.scollshow = true
      let arr = new Array()
      this.$iconList.forEach((item) => {
        if (item.includes(this.$refs.input.currentValue)) {
          arr.push(item)
        }
      })
      this.queryFlag = true
      this.queryList = JSON.parse(JSON.stringify(arr))
      this.iconTotal = arr.length
      this.iconList = arr
    }
  }
}
</script>

<style lang="less" scoped>
  .scollClass {
    position: absolute;
    background: white;
    box-shadow: 0 1px 6px rgba(0,0,0,.2);
    box-sizing: border-box;
    z-index: 3;
    padding-left: 8px;
    padding-right: 8px;
    max-width: 100%;
    min-height: 160px;
    max-height: 400px;
    overflow-x: hidden;
    overflow-y: auto;
    border: 1px solid #dddee1;
    div {
      height: 30px;
      line-height: 30px;
      margin-bottom: -5px;
      cursor: pointer;
      max-width: 210px;
      float: left;
    }
    border-radius: 4px;
    ::-webkit-scrollbar {
      width: 6px;
      height: 16px;
      background-color: #F5F5F5;
    }

    /*定义滚动条轨道 内阴影+圆角*/
    ::-webkit-scrollbar-track {
      // -webkit-box-shadow: inset 0 0 6px #e9eaec;
      border-radius: 3px;
      background-color: #f5f7f9;
    }

    /*定义滑块 内阴影+圆角*/
    ::-webkit-scrollbar-thumb {
      border-radius: 3px;
      // -webkit-box-shadow: inset 0 0 6px #e9e9e9;
      background-color: #ccc;
    }
  }

  .scollListClass {
    margin: 5px 0;
    line-height: normal;
    width: 100%;
    padding: 7px 16px;
    font-size: 14px;
    /* clear: both; */
    /* color: #515a6e; */
    /* font-size: 14px!important; */
    white-space: nowrap;
    list-style: none;
    cursor: pointer;
    transition: background .2s ease-in-out;
  }

  .scollListClass:hover {
    color: #2d8cf0;
  }

  .fade-enter-active, .fade-leave-active {
    transition: opacity .5s;
  }

  .fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
    opacity: 0;
  }

  .addclass {
    /*background-color: #f3f3f3;*/
    color: #2d8cf0;
  }
</style>
