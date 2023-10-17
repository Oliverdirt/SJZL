<style lang="less" scoped>
@import "../../../base.less";
@import "../../../assets/iconfont/iconfont.css";

.theme-header {
  background-color: #515a6e;
}

.ivu-dropdown-item {
  padding: 7px 30px;
}

.header-nav {
  display: flex;
  align-items: center;
  height: 64px;
  color: @title-color;
  padding: 0 @padding;
  justify-content: space-between;

  .bread-crumb {
    flex: 1 1 auto;
    margin-left: 16px;
    text-align: left;
    font-size: 1.25rem;
  }

  // 顶部logo
  .logo-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 100%;
    width:224px;

    img {
      width: 42px;
      height: 42px;
    }

    span {
      flex: 1;
      color: rgb(229, 230, 233);
      font-size: 16px;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }

  // 中间导航
  .top-menu {
    flex: 1;
  }

  // 最右侧配置项
  .config-wrapper {
    line-height: 32px;
    text-align: left;
    display: flex;
    align-items: center;

    a {
      i {
        font-size: 1.25rem;
      }
    }

    .dark-a {
      margin-right: 16px;
    }

    .avator-wrapper {
      flex-grow: 1;
      display: flex;
      justify-content: flex-end;
    }

    .avatar {
      width: 32px;
      height: 32px;
      border-radius: 50%;
      background: #619fe7;
    }

    .setNav {
      margin: 0 5px;
      cursor: pointer;
    }

    .shake {
      animation: leftshake .4s ease-in-out forwards;
    }

    @keyframes leftshake {
      25% {
        transform: translateY(6px);
      }
      50% {
        transform: translateY(-4px);
      }
      75% {
        transform: translateY(2px);
      }
      100% {
        transform: translateY(0);
      }
    }

    .help {
      font-size: 15px;
      font-weight: 700;
      margin-bottom: 8px;
      padding-left: 8px;
    }

    //  抽屉样式
    .choutiBox {
      width: 100%;
      height: 100%;

      .title {
        height: 30px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 25px;

        i {
          height: 1px;
          width: 25%;
          background-color: #ccc;
        }

        span {
          margin: 0 15px;
        }
      }

      .navbox {
        height: 180px;

        .navType {
          display: flex;
          align-items: center;
          justify-content: space-around;

          .navItem {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            cursor: pointer;

            .imgBox {
              position: relative;
              width: 65px;
              height: 55px;

              img {
                width: 65px;
                height: 55px;
              }

              span {
                position: absolute;
                width: 0;
                height: 0;
                border-bottom: 20px solid #13c2c2;
                border-left: 20px solid transparent;
                bottom: 0;
                right: 0;
              }

              i {
                position: absolute;
                bottom: 0;
                right: 0;
                color: #fff;
              }
            }
          }
        }
      }

      .themeColor {
        .colorRadioBox {
          display: flex;
          justify-content: center;
          align-items: center;

          .colorItem {
            display: flex;
            flex-direction: column;
            align-items: center;
            position: relative;
            margin: 0 5px;
            cursor: pointer;

            span:nth-child(1) {
              width: 24px;
              height: 24px;
            }

            span:nth-child(3) {
              font-size: 13px;
              color: #606266;
            }

            i {
              color: white;
              position: absolute;
              top: 5px;
              left: 50%;
              transform: translate(-50%, 0);
              font-weight: bolder;
            }
          }
        }
      }
    }
  }

  li {
    text-align: center;
  }
}

.setting-wrapper {
  header {
    display: flex;
    align-items: center;

    span {
      font-size: .875rem;
      margin-left: 4px;
      font-weight: bold;
    }
  }

  .setting-label {
    font-size: .8125rem;
    margin: 4px 0;
  }
}
</style>

<template>
  <div class="header-nav" :class="navTypeVal === '顶部导航'?'theme-header':''"  :style="{backgroundColor:currentColor}">
    <div class="logo-header">
      <img src="../../../assets/header-icon.png" alt="">
      <span class="titleText"> 统一研发框架</span>
    </div>
    <!-- <Icon v-show="navTypeVal === '侧边导航'" type="ios-menu-outline" style="cursor: pointer;" size="32" :style="styles"
          @click="toggleShrink"/> -->
    <!-- <div v-show="navTypeVal === '侧边导航'" class="bread-crumb">
      <Breadcrumb>
        <BreadcrumbItem
          v-for="(path, index) in currentPath"
          :key="index"
          style="font-size: .875rem;"
        >{{ path }}
        </BreadcrumbItem>
      </Breadcrumb>
    </div> -->
    <!-- <div v-show="navTypeVal === '顶部导航'" class="logo-main1">
      <img src="../../../assets/logo-min.png" alt="">
      <span class="titleText">统一研发框架</span>
    </div> -->
    <top-menu :currentColor="currentColor" :mode="'horizontal'" v-show="navTypeVal === '顶部导航'" :menuList="menuList"
              :openNames="openNames"></top-menu>
    <div class="config-wrapper clear-float">
      <a class="dark-a" href="http://udp6hxwiki.devops.com" target="_blank" tip="开发文档" rel="noopenner noreferrer">
        <Tooltip content="开发文档" placement="bottom">
          <!--          <Icon type="ios-bulb-outline" :size="20"></Icon>-->
          <Icon :style="{'color': navTypeVal === '顶部导航'?'#fff':'#fff'}" type="ios-help-circle-outline" :size="20"></Icon>
        </Tooltip>
      </a>
      <a class="dark-a" href="javascript:void(0)" @click="toggleFullScreen">
        <Tooltip :content="isFullScreen ? '退出全屏' : '全屏'" placement="bottom">
          <Icon :style="{'color': navTypeVal === '顶部导航'?'#fff':'#fff'}" custom="iconfont  icon-FullScreen"
                v-if="!isFullScreen" :size="23"/>
          <Icon :style="{'color': navTypeVal === '顶部导航'?'#fff':'#fff'}" custom="iconfont  icon-ExitFullScreen"
                v-if="isFullScreen" :size="23"/>
        </Tooltip>
      </a>
      <!--      <Dropdown @on-click="setTheme" style="margin-left:3px">-->
      <!--        <a href="javascript:void(0)">-->
      <!--          主题-->
      <!--          <Icon type="md-arrow-dropdown"/>-->
      <!--        </a>-->
      <!--        <DropdownMenu slot="list">-->
      <!--          <DropdownItem v-for='item in themeGroup' :key='item.id' :name="item.id">-->
      <!--            <Icon v-if='item.id==theme' style="margin-left:-12%" color='green' type="md-checkmark"/>-->
      <!--            <span>{{ item.name }}</span>-->
      <!--          </DropdownItem>-->
      <!--        </DropdownMenu>-->
      <!--      </Dropdown>-->
      <Dropdown class="avator-wrapper" trigger="click" style="margin-left: 20px" @on-click="handleUserAction" transfer>
        <a :style="{'color':navTypeVal === '顶部导航'?'#fff':''}" href="javascript:void(0)">
          {{ userName }}
          <Icon type="md-arrow-dropdown"/>
        </a>
        <DropdownMenu slot="list">
          <DropdownItem name="userCenter">{{ '个人中心' }}</DropdownItem>
          <DropdownItem name="logout">{{ '退出登录' }}</DropdownItem>
        </DropdownMenu>
      </Dropdown>
      <img class="avatar" :src="avatorUrl" alt="">
      <!--      切换导航 -->
      <svg :style="{'color': navTypeVal === '顶部导航'?'#fff':''}" @click="drawerVisible = true" t="1661131999732"
           class="setNav" viewBox="0 0 1024 1024" version="1.1"
           xmlns="http://www.w3.org/2000/svg"
           p-id="2150" width="18" height="18">
        <path
          d="M512 64c31.44 0 58.07 10.92 79.91 32.75 21.83 21.83 32.75 48.47 32.75 79.91 0 31.44-10.92 58.07-32.75 79.9-21.83 21.83-48.47 32.75-79.91 32.75-31.44 0-58.07-10.92-79.91-32.75-21.83-21.83-32.75-48.47-32.75-79.9 0-31.44 10.92-58.08 32.75-79.91C453.93 74.92 480.56 64 512 64z m0 335.35c31.44 0 58.07 10.92 79.91 32.75 21.83 21.83 32.75 48.47 32.75 79.91s-10.92 58.07-32.75 79.91c-21.83 21.83-48.47 32.75-79.91 32.75-31.44 0-58.07-10.91-79.91-32.75-21.83-21.83-32.75-48.47-32.75-79.91s10.92-58.07 32.75-79.91c21.84-21.84 48.47-32.75 79.91-32.75z m0 335.34c31.44 0 58.07 10.92 79.91 32.75 21.83 21.83 32.75 48.47 32.75 79.91 0 31.44-10.92 58.07-32.75 79.91C570.07 949.08 543.44 960 512 960c-31.44 0-58.07-10.92-79.91-32.75-21.83-21.83-32.75-48.47-32.75-79.91 0-31.44 10.92-58.08 32.75-79.91 21.84-21.82 48.47-32.74 79.91-32.74z"
          p-id="2151" :fill="(navTypeVal === '顶部导航' && themeColorVal !== 'biyi-light')?'#fff':'#8a8a8a'"></path>
      </svg>
      <el-drawer
        :withHeader="false"
        :visible.sync="drawerVisible"
        direction="rtl"
        size="20%"
      >
        <div class="choutiBox">
          <div class="navbox">
            <div class="title">
              <i></i>
              <span class="settingSpan">导航模式</span>
              <i></i>
            </div>
            <div class="navType">
              <div @click="clickNavType('侧边导航')" class="navItem">
                <div class="imgBox">
                  <img src="../../../assets/nav/leftnav.png" alt="">
                  <span v-show="navTypeVal === '侧边导航'"></span>
                  <i v-show="navTypeVal === '侧边导航'" class="el-icon-check"></i>
                </div>
                <span class="settingSpan">侧边导航</span>
              </div>
              <div @click="clickNavType('顶部导航')" class="navItem">
                <div class="imgBox">
                  <img src="../../../assets/nav/topnav.png" alt="">
                  <span v-show="navTypeVal === '顶部导航'"></span>
                  <i v-show="navTypeVal === '顶部导航'" class="el-icon-check"></i>
                </div>
                <span class="settingSpan">顶部导航</span>
              </div>
            </div>
          </div>
          <div class="themeColor">
            <div class="title">
              <i></i>
              <span class="settingSpan">主题颜色</span>
              <i></i>
            </div>
            <div class="colorRadioBox">
              <div
                class="colorItem"
                v-for="(item,index) in colorJson"
                :key="index"
                @click="clickColorType(item)"
              >
                <span
                  :style="{'background-color': item.color,border: '1px solid #000000'}"
                ></span>
                <i
                  v-show="themeColorVal === item.id"
                  class="el-icon-check"
                  :style="{'color': item.id === 'biyi-light'?'#000':'#fff'}"
                ></i>
                <span class="settingSpan">{{ item.text }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-drawer>
    </div>
  </div>
</template>

<script>
import ChangeTheme from '../../theme/theme.js'
import Setting from '@/setting'
import cas from '@/libs/util-cas.js'

export default {
  data () {
    return {
      theme: 'biyi-normal',
      browserKernel: '',
      canFullScreen: false,
      isFullScreen: false,
      currentColor: '',
      themeConfig: {
        'biyi-ocean': {
          name: 'biyi-blue',
          biyiPrimary: '#13c2c2',
          biyiHover: '#E7F7FF',
          biyiSelect: '#13c2c2',
          biyiThead: '#ebf7ff',
          biyiSecondMenu: '#13c2c2'
        },
        'biyi-life': {
          name: 'biyi-blue',
          biyiPrimary: '#65bd41',
          biyiHover: '#E7F7FF',
          biyiSelect: '#65bd41',
          biyiThead: '#ebf7ff',
          biyiSecondMenu: '#65bd41'
        },
        'biyi-blue': {
          name: 'biyi-blue',
          biyiPrimary: '#457CE0',
          biyiHover: '#E7F7FF',
          biyiSelect: '#2166CB',
          biyiThead: '#ebf7ff',
          biyiSecondMenu: '#457CE0'
        },
        'biyi-light': {
          name: 'biyi-light',
          biyiPrimary: '#ffffff',
          biyiHover: '#E7F7FF',
          biyiSelect: '#E7F7FF',
          biyiFont: '#000000',
          biyiThead: '#ebf7ff',
          biyiSecondMenu: '#ffffff'
        },
        'biyi-dark': {
          name: 'biyi-dark',
          biyiPrimary: '#242525',
          biyiHover: '#113545',
          biyiSelect: '#113545',
          biyiFont: '#E5E0D8D9',
          biyiWhole: '#242525', // 路由页面整体色 深色
          biyiThead: '#2A2C2C',
          biyiBorder: 'rgb(62, 65, 65)',
          biyiActive: '#4097e7',
          biyiNone: 'rgba(62, 65, 65, 0)',
          biyiSecondMenu: '#242525',
          biyiLight: '#2B2D2B', // 路由页面整体色 浅色
          biyiLightHover: '#272827',
          biyiOpacity: 0.2
        }
      },
      themeGroup: [
        {
          id: 'biyi-normal',
          name: '默认'
        },
        {
          name: '雅蓝',
          id: 'biyi-blue'
        },
        {
          name: '暗黑',
          id: 'biyi-dark'
        },
        {
          name: '高亮',
          id: 'biyi-light'
        }
      ],
      drawerVisible: false,
      navTypeVal: '侧边导航',
      themeColorVal: 'biyi-normal',
      colorJson: [
        { color: '#515a6e', text: '默认', id: 'biyi-normal' },
        { color: '#457CE0', text: '雅蓝', id: 'biyi-blue' },
        { color: '#13c2c2', text: '海洋', id: 'biyi-ocean' },
        { color: '#65bd41', text: '生机', id: 'biyi-life' },
        { color: '#242525', text: '暗黑', id: 'biyi-dark' },
        { color: '#fff', text: '高亮', id: 'biyi-light' }
      ],
      isShowSlideMenu: false
    }
  },
  components: {
    'top-menu': () => import('./top-menu')
  },
  props: {
    currentPath: {
      type: Array,
      default: () => ['首页']
    },
    shrink: {
      type: Boolean,
      default: false
    },
  },
  computed: {
    userName () {
      if (this.$store.state.user.userInfo !== null) {
        return `${this.$store.state.user.userInfo.name}`
      } else {
        return ''
      }
    },
    avatorUrl () {
      if (this.$store.state.user.userInfo !== null && this.$store.state.user.userInfo.imgPath) {
        return this.$store.state.user.userInfo.imgPath
      } else {
        return require('@/assets/header.png')
      }
    },
    styles () {
      return {
        transform: this.shrink ? 'rotate(90deg)' : 'rotate(0deg)',
        transition: 'all 0.3s ease-in-out'
      }
    },
    openNames () {
      return this.$store.state.app.currentMenuOpenNames
    },
    menuList () {
      return this.$store.state.app.menuList
    }
  },
  methods: {
    init () {
      this.getUserInfo()
    },
    getUserInfo () {
      this.$http.get('/api/system/cscpCurrentUserDetails').then(response => {
        this.$store.commit('user/setUserInfo', response.data)
      }).catch()
    },
    openSetting () {
      this.isSettingOpened = true
    },
    setLang (lang) {
      this.$i18n.locale = lang
    },
    setTheme (theme) {
      this.theme = theme
      if (theme === 'biyi-normal') {
        let dom = document.querySelector('.biyi-style')
        if (dom) {
          dom.remove()
        }
        localStorage.removeItem('biyi-theme')
        return false
      } else {
        let t = new ChangeTheme(this.themeConfig[theme])
        t.writeStyle()
        localStorage.setItem('biyi-theme', JSON.stringify(this.themeConfig[theme]))
      }
    },
    toggleFullScreen () {
      if (this.canFullScreen) {
        if (this.isFullScreen) {
          // 关闭全屏
          this.exitFullScreen()
          this.isFullScreen = false
        } else {
          // 打开全屏
          this.requestFullScreen(document.body)
          this.isFullScreen = true
        }
      } else {
        this.$Message.warning({
          content: '当前浏览器暂不支持全屏模式，请切换浏览器后重新尝试！',
          duration: 3
        })
      }
    },
    requestFullScreen (element) {
      // 判断各种浏览器，找到正确的方法
      const requestMethod = element.requestFullScreen || // W3C
        element.webkitRequestFullScreen || // Chrome, safari
        element.mozRequestFullScreen || // FireFox
        element.msRequestFullscreen // IE11
      if (requestMethod) {
        requestMethod.call(element)
      }
    },
    exitFullScreen () {
      var exitMethod = document.exitFullscreen || // W3C
        document.mozCancelFullScreen || // FireFox
        document.webkitExitFullscreen || // Chrome等
        document.msExitFullscreen // IE11
      if (exitMethod) {
        exitMethod.call(document)
      }
    },
    addFullScreenListener () {
      const self = this
      document.onkeydown = function (e) {
        if (e && e.keyCode === 122) { // 捕捉F11键盘动作
          e.preventDefault() // 阻止F11默认动作
          self.toggleFullScreen()
        }
      }
      // 监听不同浏览器的全屏事件，并件执行相应的代码
      switch (self.browserKernel) {
        case 'webkit':
          document.onwebkitfullscreenchange = function () {
            if (document.webkitIsFullScreen) {
              self.isFullScreen = true
            } else {
              self.isFullScreen = false
            }
          }
          break
        case 'gecko':
          document.onmozfullscreenchange = function () {
            if (document.mozFullScreen) {
              self.isFullScreen = true
            } else {
              self.isFullScreen = false
            }
          }
          break
        case 'trident':
          document.onmsfullscreenchange = function () {
            if (document.msFullscreenElement) {
              self.isFullScreen = true
            } else {
              self.isFullScreen = false
            }
          }
          break
        case 'others':
          document.onfullscreenchange = function () {
            if (document.fullscreen) {
              self.isFullScreen = true
            } else {
              self.isFullScreen = false
            }
          }
          break
        default:
          break
      }
    },
    handleUserAction (action) {
      switch (action) {
        case 'logout':
          this.logOutApi()
          // cas退出
          if (Setting.isCas) {
            cas.logout()
            this.$router.push('/login')
          }
          break
        case 'userCenter':
          this.$router.push({ name: 'self-edit' })
          break
        default:
          break
      }
    },
    // 退出登录
    logOutApi () {
      let thirdType = window.localStorage.getItem('thirdType')
      if (thirdType === 'tianyi') {
        let url = '/api/system/thirdAuth/tianyi/logout'
        this.$util.http.get(url, { params: { url: window.location.origin } }).then(res => {
          let a = document.createElement('a')
          a.setAttribute('href', res.data)
          document.body.appendChild(a)
          a.style.display = 'none'
          a.click()
          document.body.removeChild(a)
          this.$router.push('/login')
        }).catch(err => {
          console.log(new Error('function getTargetComponent error!'))
          console.log(err)
        })
      } else {
        let url = '/api/system/logout'
        this.$util.http.post(url).then(res => {
          localStorage.removeItem('token')
          this.$router.push('/login')
        })
      }
    },
    toggleShrink () {
      this.$emit('toggleShrink', this.isShowSlideMenu)
    },
    // 导航配置
    clickNavType (str) {
      this.navTypeVal = str
      str === '顶部导航' ? (this.isShowSlideMenu = false) : (this.isShowSlideMenu = true)
      this.$emit('controlSlideMenu', this.isShowSlideMenu)
    },
    clickColorType (item) {
      this.themeColorVal = item.id
      window.localStorage.setItem('configJson', JSON.stringify(item))
      this.setTheme(item.id)
      this.currentColor = item.color
    }
  },
  created () {
    // 默认颜色
    let configJson = JSON.parse(window.localStorage.getItem('configJson')) || {
      color: '#515a6e',
      text: '默认',
      id: 'biyi-normal'
    }
    this.clickColorType(configJson)
    console.log(this.currentColor,'1111')
    this.init()
    // 检查浏览器是否支持全屏
    this.canFullScreen = document.fullscreenEnabled ||
      document.webkitFullscreenEnabled ||
      document.mozFullScreenEnabled ||
      document.msFullscreenEnabled
    if (document.webkitFullscreenEnabled) {
      this.browserKernel = 'webkit'
    } else if (document.mozFullScreenEnabled) {
      this.browserKernel = 'gecko'
    } else if (document.msFullscreenEnabled) {
      this.browserKernel = 'trident'
    } else if (document.fullscreenEnabled) {
      this.browserKernel = 'others'
    }
    if (this.canFullScreen) {
      this.addFullScreenListener()
    }
  },
  mounted () {
    let theme = JSON.parse(localStorage.getItem('biyi-theme'))
    if (theme) {
      this.$nextTick(() => {
        this.theme = theme.name
      })
      let t = new ChangeTheme(theme)
      t.writeStyle()
    }
  },
  destroyed () {
    document.onkeydown = null
    switch (this.browserKernel) {
      case 'webkit':
        document.onwebkitfullscreenchange = null
        break
      case 'gecko':
        document.onmozfullscreenchange = null
        break
      case 'trident':
        document.onmsfullscreenchange = null
        break
      case 'others':
        document.onfullscreenchange = null
        break
      default:
        break
    }
  }
}
</script>
