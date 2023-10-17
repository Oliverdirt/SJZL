<style lang="less" scoped>
@import "./main.less";
@import "../admin/styles/elementStyleOverwrite.less";
</style>

<template>
  <div class="main">
    <div
      v-show="isShowSlideMenu"
      class="main-sider menu-scrollbar"
      style="top: 64px;opacity:0.8"
      :style="siderStyles"
    >
      <sider-menu-shrinked
        v-if="shrink"
        :menuList="menuList"
      ></sider-menu-shrinked>
      <sider-menu
        v-else
        :menuList="menuList"
        :openNames="openNames"
      ></sider-menu>
    </div>
    <div class="main-header">
      <header-nav
        :currentPath="currentPath"
        :shrink="shrink"
        @toggleShrink="toggleShrink"
        @controlSlideMenu="controlSlideMenu"
      ></header-nav>
    </div>
    <div style="top:64px;position:absolute"   :style="tagStyle">
      <tags-nav
        :pageOpenedList="pageOpenedList"
        @closeTags="closeTags"
        @tagSelected="tagSelected"
      ></tags-nav>
    </div>
    <div class="main-content common-scrollbar" :style="otherStyles">
      <router-view />
    </div>
  </div>
</template>

<script>
import { refreshTokenByRememberme } from "@/api/admin/user";

export default {
  name: "Main",
  data() {
    return {
      shrink: false,
      isShowSlideMenu: true,
    };
  },
  components: {
    "sider-menu": () => import("./components/sider-menu"),
    "sider-menu-shrinked": () => import("./components/sider-menu-shrinked"),
    "header-nav": () => import("./components/header-nav"),
    "tags-nav": () => import("./components/tags-nav"),
  },
  computed: {
    logoImgStyles() {
      return {
        backgroundImage: `url(${
          this.shrink
            ? require("../../assets/logo-min.png")
            : require("../../assets/logo-white.png")
        })`,
        padding: this.shrink ? "11px" : "4px 10px 4px 4px",
        // backgroundSize: '85%'
      };
    },
    cache() {
      return this.$store.state.app.cacheList;
    },
    tagStyle(){ 
      return {
        width: this.isShowSlideMenu ? 'calc(100% - 224px)' : "100%",
        left: this.isShowSlideMenu ? '224px' : "0"
      }
    },
    siderStyles() {
      return {
        width: this.shrink ? "64px" : "224px",
        overflow: this.shrink ? "visible" : "auto",
      };
    },
    otherStyles() {
      return {
        left: this.isShowSlideMenu ? (this.shrink ? "64px" : "224px") : "0",
      };
    },
    menuList() {
      return this.$store.state.app.menuList;
    },
    currentPath() {
      return this.$store.state.app.currentPath;
    },
    pageOpenedList() {
      return this.$store.state.app.pageOpenedList;
    },
    openNames() {
      return this.$store.state.app.currentMenuOpenNames;
    },
  },
  watch: {
    $route(to) {
      // 路由变化，更新PageOpenedList
      let index = this.$util.indexOfCurrentPageOpened(
        to.name,
        this.$store.state.app.pageOpenedList
      );
      this.$store.commit("app/setPageOpenedList", { route: to, index });
      // 设置header的面包屑路径
      let currentPathArr = this.$util.initCurrentPath(
        to,
        this.$store.state.app.routesTree
      );
      this.$store.commit("app/setCurrentPath", currentPathArr);
      // 设置左侧菜单的选中项
      this.$store.commit("app/setCurrentMenuOpenNames", to.matched);
      console.log(this.pageOpenedList, "1111");
      console.log(to, index, "2222");
    },
  },
  methods: {
    init() {
      this.initWebConfig();
      this.timingRefreshToken();
    },
    initWebConfig() {
      // 初始化PageOpenedList
      this.$store.commit("app/setPageOpenedList");
      // 设置header的面包屑路径
      let currentPathArr = this.$util.initCurrentPath(
        this.$route,
        this.$store.state.app.routesTree
      );
      this.$store.commit("app/setCurrentPath", currentPathArr);
      // 设置左侧菜单的选中项
      this.$store.commit("app/setCurrentMenuOpenNames", this.$route.matched);
    },
    timingRefreshToken() {
      // 定时每1min刷新token，如果时间间隔为半小时，停止刷新token
      if (!sessionStorage.lastHttpRequestTime) {
        sessionStorage.lastHttpRequestTime = +new Date();
      }
      this.tokenInterval = setInterval(() => {
        if (+new Date() - sessionStorage.lastHttpRequestTime > 30 * 60 * 1000) {
          clearInterval(this.tokenInterval);
        } else {
          this.refreshToken();
        }
      }, 60000);
    },
    closeTags(action) {
      let isRemoveSelected;
      if (typeof action === "number") {
        isRemoveSelected =
          this.$store.state.app.pageOpenedList[action].selected;
      }
      this.$store.commit("app/removePageOpenedList", {
        route: this.$route,
        action,
      });
      if (isRemoveSelected) {
        // 移除单个tag，导航到最后一个tag的页面
        this.$router.push({
          name: this.$store.state.app.pageOpenedList[
            this.$store.state.app.pageOpenedList.length - 1
          ].name,
        });
      } else if (action === "closeAll") {
        this.$router.push("/home");
      }
    },
    tagSelected(index) {
      if (
        this.$route.name !== this.$store.state.app.pageOpenedList[index].name
      ) {
        this.$router.push({
          name: this.$store.state.app.pageOpenedList[index].name,
        });
      }
    },
    refreshToken() {
      // const url = `/api/system/refreshToken?rememberme=${localStorage['rememberme']}`
      refreshTokenByRememberme(localStorage["rememberme"])
        // this.$http.get(url)
        .then((response) => {
          localStorage.token = response.data.token;
        })
        .catch();
    },
    toggleShrink() {
      this.shrink = !this.shrink;
    },
    controlSlideMenu(val) {
      this.isShowSlideMenu = val;
    },
  },
  created() {
    this.init();
  },
  beforeDestroy() {
    clearInterval(this.tokenInterval);
  },
};
</script>
<style scoped lang="less">
.main {
  .logo-main1 {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 224px;

    img {
      width: 42px;
      height: 42px;
    }

    span {
      flex: 1;
      color: rgb(229, 230, 233);
      font-size: 16px;
      margin-left: 8px;
    }
  }
}
</style>
