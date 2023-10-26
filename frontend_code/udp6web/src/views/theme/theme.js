/*
 * @Author: your name
 * @Date: 2021-01-27 14:29:27
 * @LastEditTime: 2021-01-29 17:48:41
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \archetype-resources\src\views\theme\theme.js
 */
function ChangeTheme (theme) {
  this.biyiWhite = 'white'
  this.alartInfo = 'rgba(64, 159, 231, 0.2)'
  this.alartWarning = 'rgba(255, 195, 106, 0.2)'
  this.biyiPrimary = theme.biyiPrimary // 页面主色
  this.biyiHover = theme.biyiHover // 划过的颜色
  this.biyiThead = theme.biyiThead // 表格划过的颜色
  this.biyiSelect = theme.biyiSelect // 表格划过的颜色
  this.biyiWhole = theme.biyiWhole // 整体色
  this.biyiLight = theme.biyiLight // 整体浅色
  this.biyiLightHover = theme.biyiLightHover // 整体浅色
  this.biyiNone = theme.biyiNone // 整体浅色
  this.biyiBorder = theme.biyiBorder // 边框色
  this.biyiFont = theme.biyiFont // 字体色
  this.biyiActive = theme.biyiActive // 选中颜色
  this.biyiSecondMenu = theme.biyiSecondMenu // 展开的二级菜单背景色
  this.biyiText = theme.biyiText || 'white'
  this.url = 'url(' + require('../../assets/bj.png') + ') no-repeat left 0 bottom/80%'
  this.logo = theme.name === 'biyi-light' ? 'url(' + require('../../assets/logo-black.png') + ')' : theme.name === 'biyi-blue' ? 'url(' + require('../../assets/logo-blue.png') + ')' : 'url(' + require('../../assets/logo-white.png') + ')'
  this.logoMin = theme.name === 'biyi-light' ? 'url(' + require('../../assets/logo-min.png') + ')' : theme.name === 'biyi-blue' ? 'url(' + require('../../assets/logo-min-white.png') + ')' : 'url(' + require('../../assets/logo-min.png') + ')'
  this.style = ''
  //  biyiPrimary的元素
  this.biyiPrimaryEle = {
    //  更改bordercolor的元素
    'background-color': [
      '.main-sider, .ivu-menu-dark',
      '.top-menu',
      '.ivu-menu-dark.ivu-menu-vertical .ivu-menu-submenu-title',
      'ivu-menu-submenu-title, .ivu-menu-opened .ivu-menu-submenu',
      '.ivu-menu-dark.ivu-menu-vertical .ivu-menu-opened, .ivu-menu-dark.ivu-menu-vertical .ivu-menu-opened .ivu-menu-submenu-title',
      '.ivu-menu-dark.ivu-menu-vertical .ivu-menu-submenu-title-active:not(.ivu-menu-submenu)',
      '.ivu-page-item-active',
      '.theme-header',
      '.logo-main1'
    ]
  }
  //  biyiHover的元素,浅蓝色
  this.biyiHoverEle = {
    'background-color': [
      '.el-tree-node__content:hover', '.ivu-select-item-selected', '.ivu-select-item:hover',
      '.addclass',
      '.scollListClass:hover',
      '.vue-treeselect__option--selected',
      '.vue-treeselect__option--highlight',
      '.frame .right .history',
      '.frame .right .history .index',
      '.ivu-tree-title-selected',
      '.ivu-tree-title:hover',
      '.el-tree-node:focus'
    ],
    'color': [
    ]
  }
  this.biyiSelectEle = {
    'background-color': [
      '.ivu-menu-dark.ivu-menu-vertical .ivu-menu-item-active:not(.ivu-menu-submenu)',
      '.ivu-menu-dark.ivu-menu-vertical .ivu-menu-item:hover',
      '.ivu-menu-dark.ivu-menu-vertical .ivu-menu-submenu-title:hover',
      '#app .ivu-menu-dark.ivu-menu-vertical .ivu-menu-submenu .ivu-menu-item-active',
      '#app .ivu-menu-dark.ivu-menu-vertical .ivu-menu-submenu .ivu-menu-item-active:hover',
      '#app .ivu-menu-dark.ivu-menu-vertical .ivu-menu-submenu .ivu-menu-item:hover',
      '.ivu-menu-dark.ivu-menu-vertical .ivu-menu-item:hover',
      '.ivu-menu-dark.ivu-menu-vertical .ivu-menu-submenu-title:hover'
    ],
    'color': [
    ]
  }
  if (theme.name === 'biyi-dark') {
    this.biyiPrimaryEle['background-color'] = [...this.biyiPrimaryEle['background-color'],
      ['.ivu-checkbox-checked .ivu-checkbox-inner', '.tags-nav', '.ivu-drawer-body', '.ivu-drawer-header']]
    this.biyiHoverEle['background-color'] = [...this.biyiHoverEle['background-color'],
      ['.ivu-dropdown-item:hover']]
  }
  // 白色
  this.biyiWhiteEle = {
    'background-color': theme.name === 'biyi-dark' ? ['.ivu-tag-primary.ivu-tag-dot .ivu-tag-dot-inner'] : [],
    'color': theme.name === 'biyi-dark' ? ['.ivu-drawer-header-inner'] : []
  }
  this.alartInfoEle = {
    'background-color': theme.name === 'biyi-dark' ? ['.ivu-alert-info'] : []
  }
  this.alartWarningEle = {
    'background-color': theme.name === 'biyi-dark' ? ['.ivu-alert-warning'] : []
  }
  //  table标题色
  this.biyiTheadEle = {
    // 'background-color': ['.ivu-table-row-hover td', '.ivu-table th:hover']
  }
  //  无颜色（透明色）
  this.biyiNoneEle = {
    'border-color': ['.ivu-page-prev', '.ivu-page-next', '.ivu-page-item', '.ivu-table-cell-tree-empty']
  }
  // 字体元素颜色，深色灰色
  this.biyiFontEle = {
    'color': [
      '.settingSpan',
      '.hljs',
      '.ivu-drawer-header-inner',
      '.el-time-panel__btn',
      '.entry',
      '.ace_layer',
      '.el-drawer__header',
      '.el-button--text',
      '#tab-componentLib',
      '#tab-formLib',
      '.el-collapse-item__content',
      '.el-collapse-item__header',
      '.el-upload__tip',
      '.processList',
      '.collectList',
      '.todoList',
      '.doneList ',
      '.transferList',
      '.circulateList',
      '.doneProcess',
      '.el-form-item__label',
      '.el-dialog__title',
      '.ivu-table-cell',
      'body',
      '.ivu-icon-ios-menu-outline',
      'a',
      '.ivu-input',
      '.ivu-tabs-nav',
      '.ivu-tabs-nav .ivu-tabs-tab:hover',
      'h3',
      '.ivu-page-options-elevator input',
      '.ivu-tree-title', '.ivu-form-item-label',
      '.ivu-icon',
      '.history .content',
      '.custom-tree-node',
      '.ivu-tag-text',
      '.ivu-dropdown-item',
      '.ivu-btn-default',
      '.ivu-select-item',
      '.vue-treeselect__single-value',
      '.ivu-table-tip td',
      '.vue-treeselect__menu',
      '.ivu-card-head p',
      '.ivu-btn-text',
      '.ivu-select-selected-value',
      '.ivu-breadcrumb-item-link',
      '.menu-item-title',
      '.el-menu-demo',
      '.titleText',
      '.ivu-modal-confirm-head-title', // 弹出框提示色
      '.ivu-radio-wrapper',
      '.ivu-input-number-input',
      '.ivu-input-group-append .ivu-btn-primary',
      '.img-wrapper',
      '.ivu-checkbox-wrapper',
      '.ivu-modal-header-inner',
      '.ivu-collapse-header',
      '.frame .left i',
      '.ivu-select-input',
      '.ivu-input-word-count',
      '.el-tree-node__label',
      '.ivu-alert-message',
      '.ivu-alert-desc',
      '.cell',
      '.el-checkbox__label'
    ]
  }
  // 暗黑风格鼠标移入色，更浅色黑色
  this.biyiLightHoverEle = {
    'background-color': ['.ivu-table-row-hover', 'tr.ivu-table-row-hover td']
  }
  // 暗黑风格整体深色，深色黑色
  this.biyiWholeEle = {
    'background-color': [
      '.processContentChildMore',
      '.processContentChild',
      '.flowtop',
      '.cardBottom',
      '.cardMiddle',
      '.childBottom',
      '.ivu-table td',
      '.tags-nav a',
      '.processList',
      '.collectList',
      '.ivu-table',
      '.processContentChildren',
      '.ivu-btn-default',
      'hr',
      '.ivu-poptip-inner',
      '.el-table td.el-table__cell',
      '.ivu-btn-text:hover' // 按钮色
    ],
    'border-top-color': ['.ivu-poptip-arrow:after']
  }
  // 暗黑风格整体浅色，浅色黑色
  this.biyiLightEle = {
    'background-color': [
      '.el-message-box',
      '.ProcessDetail',
      '.processTop',
      '.personalPortal',
      '.personalTop',
      '.personalBottom',
      '.ivu-steps-head',
      '.ivu-steps-title',
      '.hljs',
      '.el-tag',
      '.dataArea',
      '.el-picker-panel__footer',
      '.el-picker-panel',
      '.el-time-panel__footer',
      '.el-collapse-item__wrap',
      '.djs-palette-entries',
      '.el-alert',
      '.ace_scroller',
      '.ace_gutter',
      '.el-divider__text',
      '.el-input-number__decrease',
      '.el-input-number__increase',
      '.is-plain',
      '.el-table__cell',
      '.el-aside',
      '.el-radio-button__inner',
      '.left-toolbar',
      '.center-layout-container',
      '.form-widget-container',
      '.field-widget-item',
      '.djs-minimap',
      '.left-toolbar .el-button',
      '.el-drawer',
      '.el-button--default',
      '.el-upload-dragger',
      '.flowform',
      '#LaunchProcess',
      'main',
      '.el-tabs',
      '.el-tabs__header',
      '.el-tabs__item',
      '.el-textarea__inner',
      '.el-dialog__header',
      '.el-dialog__body',
      '.el-dialog__footer',
      '.el-dropdown-link',
      '.el-dropdown-menu',
      '.el-scrollbar',
      '.el-select-dropdown__item',
      '.processContent',
      '.todoList',
      '.doneList ',
      '.transferList',
      '.circulateList',
      '.doneProcess',
      '.el-card',
      '.el-card__body',
      '.btn-next',
      '.btn-prev',
      '.number',
      '.el-pager',
      '.footer',
      '.tableBox',
      '.el-form-item__content',
      '.el-input',
      '.el-input__inner',
      '.el-collapse',
      '.el-collapse-item',
      '.el-collapse-item__header',
      '.el-table',
      '.el-collapse-item__content',
      '.el-date-editor',
      '.el-range-input',
      '.container-widget-item',
      '.main-content',
      '.ivu-table th', '.ivu-page-next, .ivu-page-prev', '.ivu-page-item', '.ivu-page-options-elevator input', '.ivu-input', '.ivu-card', '.ivu-table-wrapper',
      '.ivu-spin-fix', '.ivu-input-group-prepend',
      '.el-tree', '.ivu-col',
      '.ivu-row', '.ivu-select', '.ivu-select-selection', '.vue-treeselect__control', '.vue-treeselect__menu',
      '.ivu-select-dropdown',
      '.el-tree-node__content',
      '.ivu-select-multiple .ivu-tag',
      '.ivu-modal-header',
      '.ivu-tree',
      '.ivu-modal-footer',
      '.ivu-modal-body',
      '.upload-file',
      '.ivu-input-number-controls-outside-btn',
      '.ivu-radio-wrapper',
      '.ivu-input-number-input',
      '.header-nav',
      '.el-menu-demo',
      '.ivu-input-group-append',
      'form',
      '.tag-unslected', // 标签栏
      '.ivu-collapse-simple',
      '.ivu-collapse-content',
      '.frame .right',
      '.frame .left',
      '.ivu-table-cell-tree .ivu-icon',
      '.ivu-input-word-count',
      '.ivu-btn-info[disabled]',
      '.ivu-input-number-disabled',
      '.treeShow',
      '.el-table th',
    ]
  }
  //  active的元素,蓝色
  this.biyiActiveEle = {
    'border-color': [
      '.ivu-input:hover, .ivu-page-item-active, .ivu-select-selection-focused, .ivu-select-selection:hover',
      '.ivu-btn-primary, .ivu-radio-checked .ivu-radio-inner, .ivu-radio-wrapper-checked.ivu-radio-border',
      '.ivu-tabs-nav .ivu-tabs-tab-active',
      '.ivu-checkbox-checked .ivu-checkbox-inner', '.ivu-page-disabled', '.ivu-card-bordered:hover',
      '.ivu-input-group-append:hover',
      '.ivu-input-group-append .ivu-btn-primary:hover',
      '.ivu-page-options-elevator input:hover', '.ivu-page-options-elevator input:focus', '.ivu-btn-default:hover',
      '.ivu-radio-wrapper .ivu-radio-group-item .ivu-radio-wrapper-checked .ivu-radio-default'
    ],
    'color': ['.ivu-btn-default:hover', '.ivu-menu-item-active', '.ivu-btn-text:hover',
      '.ivu-radio-wrapper:hover'],
    'background-color': ['.tag-slected']
  }
  // 边框，浅色灰色
  this.biyiBorderEle = {
    'border-bottom-color': ['.ivu-table td', '.ivu-table th', '.ivu-table-wrapper-with-border', '.ivu-card-head', '.ivu-modal-header'],
    'border-right-color': ['.ivu-table-border td', '.ivu-table-border th', '.ivu-table-wrapper-with-border', '.ivu-input-number-controls-outside-down'],
    'border-left-color': ['.ivu-table-wrapper-with-border', '.ivu-input-number-controls-outside-up'],
    'border-top-color': ['.ivu-table-wrapper-with-border', '.ivu-modal-footer', '.ivu-collapse>.ivu-collapse-item',],
    'border-color': ['.ivu-col', '.ivu-input', '.ivu-card-bordered', '.ivu-page-options-elevator input',
      '.ivu-input-group-prepend', '.ivu-tag-text', '.tag', '.ivu-btn-default', '.ivu-select-selection', '.ivu-tag',
      '.vue-treeselect__control', '.vue-treeselect__menu', '.scollClass',
      '.ivu-radio-group-button .ivu-radio-wrapper',
      '.ivu-input-group-append .ivu-btn-primary',
      '.ivu-input-number-input',
      '.ivu-input-group-append',
      '.ivu-radio-group-button .ivu-radio-wrapper:after',
      '.ivu-collapse-header',
      '.ivu-collapse',
      '.ivu-table-cell-tree .ivu-icon',
      '.ivu-input-number',
      '.ivu-table-cell-tree',
      '.ivu-btn-info[disabled]',
      '.ivu-tree',
      '.el-table td.el-table__cell',
      '.el-table th',
      '.el-table--border',
      '.el-card',
      '.el-input__inner',
      '.doneProcess',
      '.circulateList',
      '.todoList',
      '.doneList',
      '.transferList',
      '.el-textarea__inner',
      '.el-tabs',
      '.el-tabs__nav-wrap',
      '.el-dialog__header',
      '.el-dialog',
      '.el-select-dropdown',
      '.el-dialog__footer',
      '.el-collapse-item__header',
      '.palette',
      '.el-collapse',
      '.dataArea',
      '.ivu-drawer-header',
      '.el-header',
      '.el-tabs__item',
      '.el-tabs__header',
      // '.ivu-modal-body',
    ],
    'background-color': [
      '.ivu-table:before', '.ivu-radio-group-button .ivu-radio-wrapper:before',
      '.el-table::before',
      '.el-table--border::after',
      '.ivu-table-border:after',
    ]
  }
  this.biyiSecondMenuEle = {
    'background-color': ['.ivu-menu-opened .ivu-menu']
  }
  // biyiText的元素
  this.biyiTextEle = {
    'color': [
      '.ivu-page-item-active a, .ivu-page-item-active:hover a'
    ]
  }
  // 图片
  this.biyiImgEle = {
    'background': ['.main-sider']
  }
  // 图片
  this.biyiLogoEle = {
    'background-image': ['.logo-main']
  }
  // 图片
  this.biyiLogoMinEle = {
    'background-image': ['.logo-min']
  }
}
ChangeTheme.prototype.writeStyle = function () {
  var style = ''
  this.setStyle('biyiActiveEle', 'biyiActive')
  this.setStyle('biyiImgEle', 'url')
  this.setStyle('biyiLogoEle', 'logo')
  this.setStyle('biyiLogoMinEle', 'logoMin')
  this.setStyle('biyiPrimaryEle', 'biyiPrimary')
  this.setStyle('biyiHoverEle', 'biyiHover')
  this.setStyle('biyiTheadEle', 'biyiThead')
  this.setStyle('biyiNoneEle', 'biyiNone')
  this.setStyle('biyiWhiteEle', 'biyiWhite')
  this.setStyle('biyiFontEle', 'biyiFont')
  this.setStyle('biyiLightEle', 'biyiLight')
  this.setStyle('biyiLightHoverEle', 'biyiLightHover')
  this.setStyle('biyiBorderEle', 'biyiBorder')
  this.setStyle('biyiWholeEle', 'biyiWhole')
  this.setStyle('biyiSecondMenuEle', 'biyiSecondMenu')
  this.setStyle('biyiTextEle', 'biyiText')
  this.setStyle('alartInfoEle', 'alartInfo')
  this.setStyle('alartWarningEle', 'alartWarning')
  this.setStyle('biyiSelectEle', 'biyiSelect')
  this.creatStyle(style)
}
//  创建style标签 并插入
ChangeTheme.prototype.creatStyle = function (style) {
  let styledom = document.querySelector('.biyi-style')
  if (!styledom) {
    styledom = document.createElement('style')
    styledom.type = 'text/css'
    styledom.className = 'biyi-style'
  }
  styledom.innerHTML = this.style
  document.getElementsByTagName('head').item(0).appendChild(styledom)
}

// 拼接style的方法
ChangeTheme.prototype.setStyle = function (classEle, attr) {
  for (let key in this[classEle]) {
    var len = this[classEle][key].length
    this[classEle][key].forEach((ele, index) => {
      if (index === len - 1) {
        this.style += ele
      } else {
        this.style += ele + ', '
      }
    })
    this.style = this.style + '{' + key + ':' + this[attr] + ' !important}'
  }
}
export default ChangeTheme
