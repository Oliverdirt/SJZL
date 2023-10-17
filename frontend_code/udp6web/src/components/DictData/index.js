/*
 * @Author:
 * @Date: 2023-02-28 10:27:57
 * @LastEditors: guochui 17798502654@189.com
 * @LastEditTime: 2023-03-07 13:54:41
 * @FilePath: \udp6web\src\components\DictData\index.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import Vue from 'vue'
import store from '@/store/store'
import DataDict from '@/libs/dict'
import { getDictsByDictCode } from '@/api/dic/listdic'

function searchDictByKey (dict, key) {
  if (key == null && key == '') {
    return null
  }
  try {
    for (let i = 0; i < dict.length; i++) {
      if (dict[i].key == key) {
        return dict[i].value
      }
    }
  } catch (e) {
    return null
  }
}

function install () {
  Vue.use(DataDict, {
    metas: {
      '*': {
        labelField: 'dictLabel',
        valueField: 'dictValue',
        request (dictMeta) {
          const storeDict = searchDictByKey(store.getters.dict, dictMeta.type)
          if (storeDict) {
            return new Promise(resolve => { resolve(storeDict) })
          } else {
            return new Promise((resolve, reject) => {
              getDictsByDictCode(dictMeta.type).then(res => {
                store.dispatch('dict/setDict', { key: dictMeta.type, value: res.data.data })
                resolve(res.data.data)
              }).catch(error => {
                reject(error)
              })
            })
          }
        }
      }
    }
  })
}

export default {
  install
}
