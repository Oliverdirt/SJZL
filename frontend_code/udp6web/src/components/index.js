import DatePicker from './datePicker'
// 列表渲染组件
import Table from './table'

import editCell from './table/editCell'

export default {
  install: app => {
    app.component('hxDatePicker', DatePicker)
    app.component('hxTable', Table)
    app.component('editCell', editCell)
  }
}
