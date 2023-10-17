/*
 * @Author:  
 * @Date: 2023-02-28 10:31:23
 * @LastEditors:  
 * @LastEditTime: 2023-02-28 15:27:50
 * @FilePath: \udp6web\src\libs\dict\DictConverter.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import DictOptions from './DictOptions'
import DictData from './DictData'

export default function (dict, dictMeta) {
  const label = determineDictField(dict, dictMeta.labelField, ...DictOptions.DEFAULT_LABEL_FIELDS)
  const value = determineDictField(dict, dictMeta.valueField, ...DictOptions.DEFAULT_VALUE_FIELDS)
  return new DictData(dict[label], dict[value], dict)
}

/**
 * 确定字典字段
 * @param {DictData} dict
 * @param  {...String} fields
 */
function determineDictField (dict, ...fields) {
  return fields.find(f => Object.prototype.hasOwnProperty.call(dict, f))
}
