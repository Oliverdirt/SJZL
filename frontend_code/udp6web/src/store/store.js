import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import user from './modules/user'
import shake from './modules/shake'
import dict from './modules/dict'
import tabdata from './modules/tabdata'
import modelTableList from './modules/modelTableList'

import getters from './getters'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    app,
    user,
    shake,
    tabdata,
    modelTableList,
    dict
  },
  getters
})
