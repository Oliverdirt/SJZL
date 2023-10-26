import 'babel-polyfill'
import 'classlist-polyfill'
import Vue from 'vue'
import byStore from 'biyi-store'
import iView from 'view-design'
import ElementUI from 'element-ui'
import 'view-design/dist/styles/iview.css'
import App from './app.vue'
import util from './libs/util'
import router from './router/router'
import store from './store/store'
import {ButtonLimited, dataTag} from './views/admin'
import directive from './directive' // directive
import download from '@/libs/download'

// bpmn
import axios from 'axios'
import './views/workflow/package/theme/element-variables.scss'
import {vuePlugin} from './views/workflow/package/highlight'
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
import {loadExtension} from './views/lowcode/vformPro/extension/extension-loader'

// import { Tree } from 'element-ui'
import {iconList} from './libs/icon.js'
import moment from 'moment'

// 数据字典
import DictData from '@/components/DictData'
// 字典标签组件
import DictTag from '@/components/DictTag'
import {
  selectDictLabels
} from '@/libs/util'

Vue.component('VFormRender', VFormRender)
Vue.component('v-form-designer', VFormDesigner)
Vue.component('Treeselect', Treeselect)
Vue.component('UserSelect', UserSelect)
Vue.component('DictTag', DictTag)
DictData.install()

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
Vue.prototype.selectDictLabels = selectDictLabels
Vue.use(byStore)
Vue.use(ElementUI, {size: 'medium'})
Vue.use(directive)
Vue.use(iView)

Vue.component('Button', ButtonLimited)
Vue.component('dataTag', dataTag)
Vue.component('ModelEditor', ModelEditor)


const vm = new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

export default vm
