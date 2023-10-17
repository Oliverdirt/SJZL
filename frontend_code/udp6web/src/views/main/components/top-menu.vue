<template>
  <div class="top-menu">
    <el-menu
      v-resize="menuResize"
      class="el-menu-demo"
      mode="horizontal"
      :background-color="currentColor"
      text-color="#fff"
      @select="menuSlected"
      active-text-color="#ffd04b">
      <template v-for="(menu_1, index) in menuList">
        <el-menu-item
          v-if="!menu_1.children"
          :key="'menu_1_' + index"
          :index="menu_1.name">
          <span>
            <Icon :type="menu_1.icon"/>
          {{ menu_1.title }}
          </span>
        </el-menu-item>
        <el-submenu
          v-else-if="menu_1.children && menu_1.children.length >= 1"
          :index="menu_1.name"
          :key="'menu_1_' + index"
        >
          <template slot="title">
            <span class="menu-item-title">
              <Icon :type="menu_1.icon"/>
              {{ menu_1.title }}
            </span>
          </template>
          <template
            v-for="(menu_2, index) in menu_1.children"
          >
            <el-submenu
              :index="menu_2.name"
              v-if="menu_2.children && menu_2.children.length > 0"
              :key="'menu_2_' + index"
            >
              <template slot="title">
                <span class="menu-item-title">
                  <Icon :type="menu_2.icon"/>
                  {{ menu_2.title }}
                </span>
              </template>
              <el-menu-item
                v-for="(menu_3, index) in menu_2.children"
                :index="menu_3.name"
                :key="'menu_3_' + index"
              >
                <span class="menu-item-title">
                   <Icon :type="menu_3.icon"/>
                    {{ menu_3.title }}
                </span>
              </el-menu-item>
            </el-submenu>
            <el-menu-item
              v-else
              :index="menu_2.name"
              :key="'menu_2_' + index"
            >
               <span class="menu-item-title">
                  <Icon :type="menu_2.icon"/>
                  {{ menu_2.title }}
                </span>
            </el-menu-item>
          </template>
        </el-submenu>
      </template>
    </el-menu>

    <!--    <el-tooltip class="item" effect="dark" content="更多菜单" placement="top-start">-->
    <!--      <span @click="clickMoreBtn" v-show="isShowMoreBtn" class="showMoreMenu el-icon-menu"></span>-->
    <!--    </el-tooltip>-->
    <el-popover
      placement="top-end"
      title="更多菜单"
      width="250"
      trigger="hover"
    >
      <div class="miniMenuBox">
        <el-menu
          :background-color="currentColor"
          text-color="#fff"
          @select="menuSlected"
          active-text-color="#ffd04b">
          <template v-for="(menu_1, index) in miniMenuData">
            <el-menu-item
              v-if="!menu_1.children"
              :key="'menu_1_' + index"
              :index="menu_1.name">
              <Icon :type="menu_1.icon"/>
              {{ menu_1.title }}
            </el-menu-item>
            <el-submenu
              v-else-if="menu_1.children && menu_1.children.length >= 1"
              :index="menu_1.name"
              :key="'menu_1_' + index"
            >
              <template slot="title">
            <span class="menu-item-title">
              <Icon :type="menu_1.icon"/>
              {{ menu_1.title }}
            </span>
              </template>
              <template
                v-for="(menu_2, index) in menu_1.children"
              >
                <el-submenu
                  :index="menu_2.name"
                  v-if="menu_2.children && menu_2.children.length > 0"
                  :key="'menu_2_' + index"
                >
                  <template slot="title">
                <span class="menu-item-title">
                  <Icon :type="menu_2.icon"/>
                  {{ menu_2.title }}
                </span>
                  </template>
                  <el-menu-item
                    v-for="(menu_3, index) in menu_2.children"
                    :index="menu_3.name"
                    :key="'menu_3_' + index"
                  >
                <span class="menu-item-title">
                   <Icon :type="menu_3.icon"/>
                    {{ menu_3.title }}
                </span>
                  </el-menu-item>
                </el-submenu>
                <el-menu-item
                  v-else
                  :index="menu_2.name"
                  :key="'menu_2_' + index"
                >
               <span class="menu-item-title">
                  <Icon :type="menu_2.icon"/>
                  {{ menu_2.title }}
                </span>
                </el-menu-item>
              </template>
            </el-submenu>
          </template>
        </el-menu>
      </div>
      <span v-show="isShowMoreBtn" class="showMoreMenu el-icon-menu" slot="reference"></span>
    </el-popover>
  </div>
</template>

<script>
import resize from '../../../directive/listensize'

export default {
  data () {
    return {
      isShowMoreBtn: false,
      startMenuIndex: 0,
      endMenuIndex: 0,
      miniMenuData: []
    }
  },
  directives: {
    resize
  },
  props: {
    menuList: {
      type: Array
    },
    openNames: {
      type: Array
    },
    mode: {
      type: String,
      default: () => {
        return 'vertical'
      }
    },
    currentColor: {
      type: String,
      default: () => {
        return ''
      }
    }
  },
  computed: {
    activeName () {
      return this.$route.name
    }
  },
  methods: {
    menuSlected (name) {
      if (!!name && this.$route.name !== name) {
        this.$router.push({ name })
      }
    },
    openChange (openNames) {
      this.$store.commit('app/setCurrentMenuOpenNames', openNames)
    },
     // 更多逻辑
     menuResize () {
      let sumWidth = 0
      this.isShowMoreBtn = false
      let filterTitle = []
      // 获取导航UL的宽度
      let ulDom = document.getElementsByClassName('el-menu--horizontal')[0]
      let ulWidth = ulDom.offsetWidth
      // 获取导航li标签的宽度
      let liDom = ulDom.getElementsByClassName('el-submenu')  //含有孩子的导航分支
      let targetLiDom = Array.prototype.slice.call(liDom).filter(item => item.parentNode.className.indexOf('el-menu--horizontal') !== -1)
      let liDomNoChild = ulDom.getElementsByClassName('el-menu-item') //不含有孩子节点的导航分支
      let targetLiDomNoChild = Array.prototype.slice.call(liDomNoChild).filter(item => item.parentNode.className.indexOf('el-menu--horizontal') !== -1)
      let totalTargetLiDom = targetLiDom.concat(targetLiDomNoChild) //加在一起共同计算宽度
      for (let i = 0; i < totalTargetLiDom.length; i++) {
        totalTargetLiDom[i].style.display = 'block'
        sumWidth += totalTargetLiDom[i].offsetWidth
        if (sumWidth > ulWidth) {
          filterTitle.push(totalTargetLiDom[i].innerText.trim())
          this.startMenuIndex = i
          totalTargetLiDom[i].style.display = 'none'
          this.isShowMoreBtn = true
        }
      }
      this.getMiniMenu(filterTitle)
    },
    getMiniMenu (filterTitle) {
      this.miniMenuData = this.menuList.filter(item => {
        if (filterTitle.indexOf(item.title) !== -1) {
          return item
        }
      })
    },
    clickMoreBtn () {

    }
  },
  mounted () {
    console.log('菜单')
    console.log(this.menuList)
  }
}
</script>
<style lang="less">
.top-menu {
  height: 64px;
  position: relative;
  // iview导航样式
  .ivu-menu {
    width: 100%;
    height: 100%;
    display: flex;
    flex-wrap: nowrap;

    > li {
      height: 100%;
      flex-shrink: 0;

      div.ivu-menu-submenu-title {
        height: 100%;
        display: flex;
        align-items: center;
      }

      div.ivu-select-dropdown {
        padding: 0 !important;
        margin: 0 !important;
        width: 200px !important;

        ul.ivu-menu-drop-list {
          width: 200px !important;

          li {
            width: 200px !important;
          }
        }
      }
    }
  }

  .showMoreMenu {
    position: absolute;
    top: 50%;
    transform: translate(0, -50%);
    right: 10px;
    cursor: pointer;
    color: #fff;
    z-index: 9999;
    font-size: 16px;
  }

  //  elementUi导航样式
  .el-menu-demo {
    height: 64px;

    > li {
      height: 100%;
      line-height: 64px;

      > div.el-submenu__title {
        height: 64px;
        line-height: 64px;
      }
    }
  }

  .ivu-icon {
    color: #fff !important;
  }
}

.miniMenuBox {
  max-height: 425px;
  overflow-x: hidden;
  overflow-y: auto;
}

.miniMenuBox::-webkit-scrollbar {
  display: none;
}
</style>
