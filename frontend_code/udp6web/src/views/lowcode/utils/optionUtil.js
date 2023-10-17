import { isNotNull, traverseContainWidgets, traverseFieldWidgets } from '@/views/lowcode/utils/util'
import { translate } from '@/views/lowcode/utils/i18n'
import FormValidators, { getRegExp } from '@/views/lowcode/utils/validators'
import de from 'element-ui/src/locale/lang/de'

export function buildDefaultValueListFn (formConfig, widgetList, resultList) {
  return function (fieldWidget) {
    const fop = fieldWidget.options
    const fd = fop.defaultValue
    if (isNotNull(fd)) {
      resultList.push(`${fop.name}: ${JSON.stringify(fd)},`)
    } else {
      resultList.push(`${fop.name}: null,`)
    }
  }
}

export function buildRulesListFn (formConfig, widgetList, resultList) {
  return function (fieldWidget) {
    const fop = fieldWidget.options
    let fieldRules = []
    if (fop.required) {
      fieldRules.push(`{
        required: true,
        message: '${translate('render.hint.fieldRequired')}',
      }`)
    }

    if (fop.validation) {
      let vldName = fop.validation
      if (FormValidators[vldName]) {
        fieldRules.push(`{
          pattern: ${getRegExp(vldName)},
          trigger: ['blur', 'change'],
          message: '${fop.validationHint}'
        }`)
      } else {
        fieldRules.push(`{
          pattern: '${vldName}',
          trigger: ['blur', 'change'],
          message: '${fop.validationHint}'
        }`)
      }
    }

    // TODO: 自定义校验函数

    fieldRules.length > 0 && resultList.push(`${fop.name}: [${fieldRules.join(',')}],`)
  }
}

export function buildFieldOptionsFn (formConfig, widgetList, resultList, type) {
  return function (fieldWidget) {
    const fop = fieldWidget.options
    const ft = fieldWidget.type
    const name = fieldWidget.options.name
    let resultJson = {}

    if (type === 'option' && ((ft === 'radio') || (ft === 'checkbox') || (ft === 'select') || (ft === 'cascader'))) {
      resultJson['type'] = ft
      resultJson['name'] = name
      resultJson['options'] = fop.optionItems
      resultList.push(resultJson)
    }
    if (type === 'datetime' && ((ft === 'time') || (ft === 'time-range') || (ft === 'date') || (ft === 'date-range'))) {
      resultJson['type'] = ft
      resultJson['name'] = name
      resultJson['optionType'] = fop.type
      resultList.push(resultJson)
    }
    if (type === 'dept' && ((ft === 'dept'))) {
      resultJson['type'] = ft
      resultJson['name'] = name
      // resultJson['options'] = fop.optionItems
      resultList.push(resultJson)
    }
  }
}

export function buildUploadDataFn (formConfig, widgetList, resultList) {
  return function (fieldWidget) {
    const fop = fieldWidget.options
    const ft = fieldWidget.type
    if ((ft === 'picture-upload') || (ft === 'file-upload')) {
      resultList.push(`${fop.name}FileList: [],`)
      resultList.push(`${fop.name}UploadHeaders: {},`)
      resultList.push(`${fop.name}UploadData: {},`)
    }
  }
}

export function buildActiveTabs (formConfig, widgetList) {
  let resultList = []
  const handlerFn = function (cw) {
    const cop = cw.options
    const ct = cw.type
    if (ct === 'tab') {
      cw.tabs.length > 0 && resultList.push(`'${cop.name}ActiveTab': '${cw.tabs[0].options.name}',`)
    }
  }
  traverseContainWidgets(widgetList, handlerFn)

  return resultList
}

export const getFieldOptions = function (formConfig, widgetList) {
  const type = 'option'
  let resultList = []
  traverseFieldWidgets(widgetList, (widget) => {
    buildFieldOptionsFn(formConfig, widgetList, resultList, type)(widget)
  })
  return resultList
}

export const getDateTimeFields = function (formConfig, widgetList) {
  const type = 'datetime'
  let resultList = []
  traverseFieldWidgets(widgetList, (widget) => {
    buildFieldOptionsFn(formConfig, widgetList, resultList, type)(widget)
  })
  return resultList
}

export const getDeptFields = function (formConfig, widgetList) {
  const type = 'dept'
  let resultList = []
  traverseFieldWidgets(widgetList, (widget) => {
    buildFieldOptionsFn(formConfig, widgetList, resultList, type)(widget)
  })
  return resultList
}
