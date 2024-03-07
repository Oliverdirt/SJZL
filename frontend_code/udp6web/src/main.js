import './public-path'

// import 'babel-polyfill'
import 'classlist-polyfill'
import Vue from 'vue'
import byStore from 'biyi-store'
import iView from 'view-design'
import ElementUI from 'element-ui'
import 'view-design/dist/styles/iview.css'
import App from './app.vue'
import util from './libs/util'
// import router from './router/router'
import store from './store/store'
import { ButtonLimited, dataTag } from './views/admin'
import directive from './directive' // directive
import download from '@/libs/download'
import '@/assets/less/base.css'

// bpmn
import axios from 'axios'
import './views/workflow/package/theme/element-variables.scss'
import { vuePlugin } from './views/workflow/package/highlight'
import 'highlight.js/styles/atom-one-dark-reasonable.css'
import MyPD from './views/workflow/package/index.js'
import 'bpmn-js/dist/assets/diagram-js.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css'
import ModelEditor from '@/views/workflow/modelEditor.vue'

// vform 依赖
import Treeselect from '@riophae/vue-treeselect'
import UserSelect from '@/views/components/user/index.vue'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import '@/views/lowcode/utils/debug-console'
import '@/views/lowcode/utils/directive'
import '@/styles/index.scss'
import './views/lowcode/vformPro/icons'
import './views/lowcode/vformPro/iconfont/iconfont.css'

import VFormRender from './views/lowcode/vformPro/components/form-render/index'
import VFormDesigner from './views/lowcode/vformPro/components/form-designer/index'
import { loadExtension } from './views/lowcode/vformPro/extension/extension-loader'
import Component from '@/components'

import 'default-passive-events'
// import { Tree } from 'element-ui'
import { iconList } from './libs/icon.js'
import moment from 'moment'
Vue.component('VFormRender', VFormRender)
Vue.component('v-form-designer', VFormDesigner)
Vue.component('Treeselect', Treeselect)
Vue.component('UserSelect', UserSelect)

loadExtension()

if (typeof window !== 'undefined') {
  window.axios = axios
}
Vue.prototype.$axios = axios
Vue.use(vuePlugin)
Vue.use(MyPD)

Vue.prototype.$moment = moment
Vue.config.productionTip = false
Vue.prototype.$util = util
Vue.prototype.$http = util.http
Vue.prototype.$iconList = iconList
Vue.prototype.$download = download
Vue.prototype.$bus = new Vue()

// pxtovw, 单位为 vw
Vue.prototype.$pxToVw = width => {
  return ((width / 1920) * 100).toFixed(5)
}
// 单位为px，用于那些不能使用vw单位的地方
Vue.prototype.$px2Vw = width => {
  return ((window.screen.width / 1920) * width).toFixed(5)
}
// pxtovw, 单位为 vw
Vue.prototype.$pxToVh = height => {
  return ((height / 1080) * 100).toFixed(5)
}
// 单位为px，用于那些不能使用vw单位的地方
Vue.prototype.$px2Vh = height => {
  return ((window.screen.height / 1080) * height).toFixed(5)
}

Vue.use(byStore)
// Vue.use(Tree)
Vue.use(ElementUI, { size: 'small' })
Vue.use(directive)
Vue.use(iView)
Vue.use(Component)

Vue.component('Button', ButtonLimited)
Vue.component('dataTag', dataTag)
Vue.component('ModelEditor', ModelEditor)

let instance = null
let router = null
// let store = null
async function render (props = {}) {
  const { container } = props
  if (!router) {
    router = await require('./router/router').default
  }
  if (!store) {
    store = await require('./store/store').default
  }
  instance = new Vue({
    router,
    store,
    render: h => h(App)
  }).$mount(container ? container.querySelector('#app') : '#app')
}

// 独立运行时
if (!window.__POWERED_BY_QIANKUN__) {
  render()
}

export async function bootstrap () {
  console.log('[vue] vue app bootstraped')
}
export async function mount (props) {
  console.log('[vue] props from main framework', props)
  Vue.prototype.$onGlobalStateChange = props.onGlobalStateChange
  Vue.prototype.$setGlobalState = params => {
    console.info('子应用设置数据变化', params)
    props.setGlobalState(params)
  }
  render(props)
  if (store.state.app.menuList.length > 0) {
    console.info(store.state.app.menuList, '挂载菜单')
    props.setGlobalState({
      menuList: store.state.app.menuList
    })
  }
}
export async function unmount () {
  console.log('[vue] vue app unmount')
  instance.$destroy()
  instance.$el.innerHTML = ''
  instance = null
  router = null
  // store = null
}

const vm = instance

export default vm

