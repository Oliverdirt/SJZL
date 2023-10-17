/*
 * @Author:  
 * @Date: 2023-02-28 10:31:23
 * @LastEditors:  
 * @LastEditTime: 2023-02-28 14:14:06
 * @FilePath: \udp6web\src\libs\dict\index.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import Dict from './Dict'
import { mergeOptions } from './DictOptions'

export default function (Vue, options) {
  mergeOptions(options)
  Vue.mixin({
    data () {
      if (this.$options === undefined || this.$options.dicts === undefined || this.$options.dicts === null) {
        return {}
      }
      const dict = new Dict()
      dict.owner = this
      return {
        dict
      }
    },
    created () {
      if (!(this.dict instanceof Dict)) {
        return
      }
      options.onCreated && options.onCreated(this.dict)
      this.dict.init(this.$options.dicts).then(() => {
        options.onReady && options.onReady(this.dict)
        this.$nextTick(() => {
          this.$emit('dictReady', this.dict)
          if (this.$options.methods && this.$options.methods.onDictReady instanceof Function) {
            this.$options.methods.onDictReady.call(this, this.dict)
          }
        })
      })
    }
  })
}
