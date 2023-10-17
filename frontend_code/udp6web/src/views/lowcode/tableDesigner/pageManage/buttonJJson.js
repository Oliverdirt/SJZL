export const SEARCH_BUTTON = function (str = '') {
  return {
    type: 'button',
    icon: 'button',
    formItemFlag: false,
    options: {
      name: 'button107686',
      label: '查询',
      columnWidth: '200px',
      size: '',
      displayStyle: 'inline',
      disabled: false,
      hidden: false,
      type: 'primary',
      plain: false,
      round: false,
      circle: false,
      icon: null,
      btnEventType: '查询',
      linkFormPage: '',
      bindInterfaceAddress: '',
      bindResponse: '',
      marginStyle: 0,
      marginLeft: 0,
      marginRight: 0,
      customClass: '',
      onCreated: '',
      onMounted: '',
      onClick: str
    },
    id: 'button107686'
  }
}

export const TABLE_JSON = function (param = {}) {
  return {
    'type': 'data-table',
    'category': 'container',
    'icon': 'data-table',
    'widgetList': [],
    'options': {
      'name': 'datatable28677',
      'label': 'data-table',
      'hidden': false,
      'rowSpacing': 8,
      'tableHeight': '300px',
      'tableWidth': '100%',
      'customClass': [],
      'stripe': true,
      'showIndex': false,
      'showCheckBox': true,
      'showPagination': true,
      'smallPagination': false,
      'showSummary': false,
      'border': true,
      'tableSize': 'medium',
      'tableColumns': [],
      'showButtonsColumn': true,
      'buttonsColumnFixed': 'right',
      'buttonsColumnTitle': '操作',
      'buttonsColumnWidth': 120,
      'operationButtons': [
        {
          name: '编辑',
          label: '编辑',
          opeType: '编辑',
          type: 'text',
          size: 'small',
          round: false,
          hidden: false,
          disabled: false,
          btnEventType: '编辑'
        },
        {
          name: '删除',
          label: '删除',
          opeType: '删除',
          type: 'text',
          size: 'small',
          round: false,
          hidden: false,
          disabled: false,
          btnEventType: '删除'
        },
        { // 操作按钮配置
          name: '查看更多',
          label: '查看更多',
          opeType: '查看更多',
          type: 'text',
          size: 'small',
          round: false,
          hidden: false,
          disabled: false,
          btnEventType: '查看更多'
        }],
      'pagination': {
        'currentPage': 1,
        'pageSizes': [10, 15, 20, 30, 50, 100, 200],
        'pageSize': 10,
        'total': 0
      },
      'dsEnabled': false,
      'dsName': '',
      'tableData': [],
      'onCreated': '',
      'onMounted': '',
      'onPageSizeChange': param.onPageSizeChange,
      'onCurrentPageChange': param.onPageSizeChange,
      'onSelectionChange': '',
      'onHideOperationButton': '',
      'onDisableOperationButton': '',
      'onGetOperationButtonLabel': '',
      'onOperationButtonClick': param.onOperationButtonClick,
      'onHeaderClick': '',
      'onRowClick': '',
      'onRowDoubleClick': '',
      'onCellClick': '',
      'onCellDoubleClick': '',
      'onGetRowClassName': '',
      'onGetSpanMethod': ''
    },
    'id': 'datatable28677'
  }
}

export const FORM_BUTTON = function (str = '') {
  return [
    {
      "type": "button",
      "icon": "button",
      "formItemFlag": false,
      "options": {
        "name": "button57194",
        "label": "确定",
        "columnWidth": "200px",
        "size": "",
        "displayStyle": "inline",
        "disabled": false,
        "hidden": false,
        "type": "primary",
        "plain": false,
        "round": false,
        "circle": false,
        "icon": null,
        "btnEventType": "自定义",
        "linkFormPage": "",
        "bindInterfaceAddress": "",
        "bindResponse": "",
        "marginStyle": 0,
        "marginLeft": 0,
        "marginRight": 25,
        "customClass": [],
        "onCreated": "",
        "onMounted": "",
        "onClick": str
      },
      "id": "button57194"
    },
    {
      "type": "button",
      "icon": "button",
      "formItemFlag": false,
      "options": {
        "name": "button39755",
        "label": "取消",
        "columnWidth": "200px",
        "size": "",
        "displayStyle": "inline",
        "disabled": false,
        "hidden": false,
        "type": "",
        "plain": false,
        "round": false,
        "circle": false,
        "icon": null,
        "btnEventType": "自定义",
        "linkFormPage": "",
        "bindInterfaceAddress": "",
        "bindResponse": "",
        "marginStyle": 0,
        "marginLeft": 0,
        "marginRight": 0,
        "customClass": "",
        "onCreated": "",
        "onMounted": "",
        "onClick": ""
      },
      "id": "button39755"
    }
  ]
}


// 删除 编辑 查看更多脚本
export const TABLE_OPE_SCRIPTS = function () {
  return `if (buttonName.label === '查看更多') {
          this.getFormRef().$emit('edit', row, buttonName)
        } else if (buttonName.label === '删除') {
          axios.get('http://10.2.14.210:8091/delete/' + row.fsiteId).then(res => {
            alert('删除成功！')
          })
        } else if (buttonName.label === '编辑') {
          this.getFormRef().$emit('edit', row, buttonName)
        }`
}

// 表单的确认取消脚本
export const FORM_BUTTON_SCRIPTS = function (api = {}) {
  let {url, params, requestType} = {...api}
  let method = requestType === 0 ? 'post' : 'get'
  return `axios.${method}('${url}',
  ${params}
  ).then(res=>{
    if(res.data.code===200){
      this.$Message.success(res.data.msg);
      // refreshTableFN(params,tableDom);
      document.getElementsByClassName("el-dialog__headerbtn")[0].click();
      // window.location.reload();
      this.$router.replace('/customize/page/1651035178455343106?test='+Date.now());
    }
  })`
}

// 搜索按钮的初始化脚本
export const SEARCH_BUTTON_SCRIPTS = function (api = {}, tableName) {
  let {url, params, requestType} = {...api}
  let method = requestType === 0 ? 'post' : 'get'
  if (typeof params === 'object') {
    params.curPage = 1
    params.pageSize = 10
  }
  return `let tableDom = this.getWidgetRef('datatable28677')
  axios.${method}('${url}', ${params}).then(res => {
    let a = ''
    let newData = []
    res.data.forEach((item,index)=>{
            a = Object.keys(item).length
      let obj = {}
      for(let i=0; i<a; i++){
              obj['${tableName}#'+Object.keys(item)[i]] = item[Object.keys(item)[i]]
      }
      newData.push(obj)
    })
    tableDom.setTableData(newData)
    tableDom.setPagination({
      total: res.data.recordsTotal
    })
  })`
}
// 分页搜索初始化脚本
export const PAGE_CHANGE_SCRIPTS = function (api = '', fieldNames = {}, pageSize = 10, currentPage = 1) {
  let param = {
    ...fieldNames,
    curPage: currentPage,
    pageSize: pageSize
  }
  return `let tableDom = this.getWidgetRef('datatable28677')
  axios.post('${api}', ${JSON.stringify(param)}).then(res => {
    tableDom.setTableData(res.data.data.rows)
    tableDom.setPagination({
      total: res.data.recordsTotal
    })
  })`
}
