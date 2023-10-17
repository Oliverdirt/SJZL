import { isNotNull, traverseContainWidgets, traverseFieldWidgets } from '@/views/lowcode/utils/util'
import { translate } from '@/views/lowcode/utils/i18n'
import FormValidators, { getRegExp } from '@/views/lowcode/utils/validators'
import de from 'element-ui/src/locale/lang/de'

export function buildDefaultValueListFn (formConfig, widgetList, resultList) {
  return function (fieldWidget) {
    const fop = fieldWidget.options
    const fd = fop.defaultValue
    const ft = fieldWidget.type
    if (ft === 'dept') {
      resultList.push(`${fop.name}: null,`)
    } else {
      if (isNotNull(fd)) {
        resultList.push(`${fop.name}: ${JSON.stringify(fd)},`)
      } else {
        resultList.push(`${fop.name}: null,`)
      }
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

export function buildFieldOptionsFn (formConfig, widgetList, resultList) {
  return function (fieldWidget) {
    const fop = fieldWidget.options
    const ft = fieldWidget.type
    if ((ft === 'radio') || (ft === 'checkbox') || (ft === 'select') || (ft === 'cascader')) {
      resultList.push(`${fop.name}Options: ${JSON.stringify(fop.optionItems)},`)
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
export function buildDeptFn (formConfig, widgetList, deptDataJson) {
  return function (fieldWidget) {
    const ft = fieldWidget.type
    if (ft === 'dept') {
      deptDataJson['deptArr'] = 'deptArr: [],'
      deptDataJson['deptApi'] = 'this.$http.get(\'/api/cscpDepts/treeselect\').then(res => {\n' +
        '      this.deptArr = res.data\n' +
        '    })\n'
    }
  }
}
export const genVue2JS = function (formConfig, widgetList) {
  let defaultValueList = []
  let rulesList = []
  let fieldOptions = []
  let uploadData = []
  let deptDataJson = {}
  traverseFieldWidgets(widgetList, (widget) => {
    buildDefaultValueListFn(formConfig, widgetList, defaultValueList)(widget)
    buildRulesListFn(formConfig, widgetList, rulesList)(widget)
    buildFieldOptionsFn(formConfig, widgetList, fieldOptions)(widget)
    buildUploadDataFn(formConfig, widgetList, uploadData)(widget)
    buildDeptFn(formConfig, widgetList, deptDataJson)(widget)
  })
  const activeTabs = buildActiveTabs(formConfig, widgetList)

  const v2JSTemplate =
`  export default {
    components: {},
    props: {},
    data() {
      return {
        ${formConfig.modelName}: {
          ${defaultValueList.join('\n')}
        },
        ${deptDataJson['deptArr']}
        ${formConfig.rulesName}: {
          ${rulesList.join('\n')}
        },

        ${activeTabs.join('\n')}

        ${fieldOptions.join('\n')}

        ${uploadData.join('\n')}
      }
    },
    computed: {},
    watch: {},
    created() {
    },
    mounted() {
        ${deptDataJson['deptApi']}
    },
    methods: {
      submitForm() {
        this.$refs['vForm'].validate(valid => {
          if (!valid) return

          //TODO: 提交表单
        })
      },

      resetForm() {
        this.$refs['vForm'].resetFields()
      }
    }
  }`

  return v2JSTemplate
}
