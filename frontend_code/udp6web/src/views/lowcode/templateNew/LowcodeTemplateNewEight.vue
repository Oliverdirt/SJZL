<template>
  <div
    id="muban"
    v-loading="mainLoading"
    element-loading-text="页面加载中..."
  >
    <div class="tableBox">
      <VFormRender ref="pageRender"
                   :form-json="tableJson"
                   :preview-state="true"
                   v-if="isShowPage"
                   @buttonClick="listenBtnClick"
                   @operationButtonClick="tableOpeColumnClick"
                   @dataTablePageSizeChange="dataTablePageSizeChange"
                   @dataTablePageChange="dataTablePageChange"
      >
      </VFormRender>
      <form enctype="multipart/form-data" id="daoru">
        <input
          type="file"
          name="fileup"
          id="uploadEventFile"
          @change="fileChange"
          style="display: none"
        />
      </form>
    </div>
    <!--    新增，编辑弹窗 -->
    <el-dialog
      :title="addAlertTittle"
      :visible.sync="addDialogVisible"
      width="60%"
    >
      <VFormRender ref="preForm"
                   :form-json="pageJson"
                   :preview-state="true"
                   :option-data="testOptionData"
                   v-if="addDialogVisible"
                   :form-data="testFormData"
      >
      </VFormRender>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="getFormData">确 定</el-button>
      </span>
    </el-dialog>
    <!--    子表信息弹窗  -->
    <el-dialog
      title="更多信息"
      :visible.sync="moreInfoDialog"
      :fullscreen="true"
      append-to-body
      :modal="false"
    >
      <LowcodeTemplateNew
        :pageId="childPageId"
        :currentMoreTableItem="currentMoreTableItem"
        :rowParam="baseRowParam"
        :relationField="childrelationField"
        :mainTablePageName="mainTablePageName"
        v-if="moreInfoDialog"
      >
      </LowcodeTemplateNew>
      <span slot="footer" class="dialog-footer">
        <el-button @click="moreInfoDialog = false;clickQueryBtn()">取 消</el-button>
      </span>
    </el-dialog>
    <!--    -->
    <el-dialog title="选择导出字段" :visible.sync="dialogTableVisible">
      <el-table
        :data="gridData"
        ref="columnMultipleTable"
      >
        <el-table-column property="label" label="字段描述" width="260"></el-table-column>
        <el-table-column property="prop" label="字段名称" width="260"></el-table-column>
        <el-table-column property="isExport" label="是否导出">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.isExport"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column property="isQuery" label="显示列">
          <template slot-scope="scope">
            <el-checkbox @change="clickIsQuery" v-model="scope.row.isQuery"></el-checkbox>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import util from '@/libs/util'
import VFormRender from '@/views/lowcode/vformPro/components/form-render/index'
import { getDateTimeFields, getDeptFields, getFieldOptions } from '@/views/lowcode/utils/optionUtil'
import LowcodeTemplateNew from '@/views/lowcode/templateNew/LowcodeTemplateNew'

import QS from 'qs'

export default {
  name: 'TemplateTwo',
  components: {
    VFormRender,
    LowcodeTemplateNew
  },
  props: {
    pageId: {
      type: String,
      default: () => {
        return ''
      }
    },
    // cPageId: {
    //   type: String,
    //   default: () => {
    //     return ''
    //   }
    // },
    // currentMoreTableItem: {
    //   type: String,
    //   default: () => {
    //     return ''
    //   }
    // },
    rowParam: {
      type: Object,
      default: () => {
        return ''
      }
    },
    relationField: {
      type: Object,
      default: () => {
        return ''
      }
    },
    // mainTablePageName: {
    //   type: String,
    //   default: () => {
    //     return ''
    //   }
    // },
  },
  data () {
    return {
      gridData: [],
      dialogTableVisible: false,
      mainTablePageName: '',
      file: '',
      cPageId: undefined,
      childPageId: undefined,
      moreInfoDialog: false,
      baseRowParam: undefined,
      childrelationField: [],
      mainLoading: true,
      addDialogVisible: false,
      addAlertTittle: '新增',
      pagerJson: {
        size: 10,
        page: 1,
        total: 0
      },
      pageJson: {},
      tableJson: {},
      isShowPage: false,
      pageId: '',
      testFormData: {},
      testOptionData: {},
      pk: '', // 主键字段
      currentMoreTableItem: undefined,
      rowParam: null,
      dateTimeFields: [], // 时间、日期数组
      deptFields: [], // 部门
      deptArr: [], // 获取所有部门集合
      dicArr: [],
      fileName: '',
      formId: '',
      mainTableName: '',
      dynamicApi: '', // 动态接口
      checkedTable: [],
      orderByClause: []
    }
  },
  mounted () {
    this.fileName = this['$route'].meta.title
    this.queryDic()
    this.initPage()
  },
  watch: {
    '$route': 'initPage'
  },
  methods: {
    // 初始化
    initPage (to) {
      to ? this.fileName = to.meta.title : ''
      if (window.location.href.includes('customize/page/')) {
        if (window.location.href.split('customize/page/')[1]) {
          // 拿到pageId
          this.pageId = (this.pageId == undefined || this.pageId == '') ? window.location.href.split('customize/page/')[1] : this.pageId
          // 可编辑列表需要用到pageId,通过localStorage存储
          window.localStorage.setItem('pageId', this.pageId)
          // 通过pageId拿到所有字段,取到主表的表名
          util.http.get('/api/lowcode/customize/cscpCustomizeVfields/getListByPageId/' + this.pageId).then(res => {
            let mainTableName = res.data[0].fieldName.split('#')[0]
            this.mainTableName = mainTableName
            // 拿到表单信息
            this.isShowPage = false
            this.mainLoading = true
            this.getPageJson()
          })
        } else {
          this.$message.error('没有拿到pageId!')
        }
      }
    },
    // 按钮权限
    controlBtnIsShow () {
      let buttonArr = []
      let disableArr = []
      this.getAllButton(this.tableJson, buttonArr)
      this.$http.get('/api/system/menu/getButtonMenus').then(res => {
        buttonArr.forEach(item => {
          if (res.data.indexOf(this.pageId + item.id) === -1) {
            disableArr.push(item)
          }
        })
        disableArr.forEach(item => {
          this.$refs.pageRender.hideWidgets(item.id)
        })
      })
    },
    getAllButton (jsonObj, buttonArr = []) {
      if (!jsonObj) {
        return buttonArr
      }
      for (let key in jsonObj) {
        let ele = jsonObj[key]
        if (ele && typeof (ele) === 'object') {
          if (ele && ele.type && ele.type === 'button') {
            buttonArr.push(ele)
            continue
          }
          this.getAllButton(ele, buttonArr)
        }
        if (Array.isArray(ele) && ele.length.length > 0) {
          ele.map(item => {
            return this.getAllButton(item, buttonArr)
          })
        }
      }
    },
    // 表单信息
    getPageJson () {
      util.http.get('/api/lowcode/customize/cscpCustomizeVpages/' + this.pageId)
        .then((res) => {
          this.mainLoading = false
          this.pk = JSON.parse(res.data.pageOption).pk // 拿到主键
          // 拿到主表名给子表列表页面用
          this.mainTablePageName = res.data.pageTable.split(',')[0]
          this.tableJson = JSON.parse(res.data.pageJson)

          // 先获取到查询按钮的绑定动态接口值和绑定返回值的值
          // let queryBtnBindInterfaceAddress = this.tableJson
          // 拿到查询按钮对应的options里的bindInterfaceAddress和bindResponse
          // 默认查询按钮放在最后一个栅格里
          // let optionArr = queryBtnBindInterfaceAddress?.widgetList[0]?.cols[queryBtnBindInterfaceAddress?.widgetList[0]?.cols?.length - 1].widgetList
          // let targetArr = optionArr.filter(item =>
          //   item.type === 'button' && item.options.btnEventType === '查询')
          // 获取到动态接口
          // this.dynamicApi = targetArr[0].options.bindInterfaceAddress

          this.isShowPage = true
          setTimeout(() => {
            this.getColumns()
            this.clickQueryBtn()
            // this.controlBtnIsShow()
          }, 0)
        }).catch(() => {
        this.mainLoading = false
        this.$Message.error('页面获取失败')
      })
    },
    // 编辑
    getFormJson () {
      util.http.get('/api/lowcode/customize/cscpCustomizeVpages/' + this.formId)
        .then((res) => {
          this.pageJson = JSON.parse(res.data.pageJson)
          this.addDialogVisible = true
          if (this.addAlertTittle === '详情') {
            this.$nextTick(() => {
              this.$refs['preForm'].disableForm()
            })
          }
        }).catch(() => {
        this.mainLoading = false
        this.$Message.error('页面获取失败')
      })
    },
    // formrender的点击事件
    listenBtnClick (buttonWidget) {
      let btnAttr = buttonWidget._props.field.options
      this.dynamicApi = btnAttr.bindInterfaceAddress
      if (btnAttr.btnEventType === '查询') {
        this.clickQueryBtn()
      } else if (btnAttr.btnEventType === '新增') {
        this.formId = btnAttr.linkFormPage
        this.testFormData = {}
        this.addAlertTittle = '新增'
        this.getFormJson()
      } else if (btnAttr.btnEventType === '删除') {
        this.multiDelete()
      } else if (btnAttr.btnEventType === '导出') {
        this.exportMethod()
      } else if (btnAttr.btnEventType === '导入') {
        this.labelExcelImport()
      } else if (btnAttr.btnEventType === '字段配置') {
        this.dialogTableVisible = true
      } else if (btnAttr.btnEventType === '模板下载') {
        this.downloadTemplate()
      } else { // 自定义事件
        let Fn = Function
        let customFn = new Fn(buttonWidget._props.field.options.onClick)
        customFn.call(this)
      }
    },
    // 表格操作列按钮的点击
    tableOpeColumnClick (dataTable, buttonName, rowIndex, row, onOperationButtonClick) {
      if (buttonName.opeType === '编辑') {
        this.addAlertTittle = '编辑'
        this.formId = buttonName.pageId
        for (let item in row) {
          if (item.startsWith('pictureupload')) {
            if (!Array.isArray(row[item])) {
              row[item] = JSON.parse(row[item])
            }
          }
        }
        this.testFormData = row
        this.getFormJson()
      } else if (buttonName.opeType === '删除') {
        this.handleDelete(row)
      } else if (buttonName.opeType === '详情') {
        this.addAlertTittle = '详情'
        this.formId = buttonName.pageId
        for (let item in row) {
          if (item.startsWith('pictureupload')) {
            if (!Array.isArray(row[item])) {
              row[item] = JSON.parse(row[item])
            }
          }
        }
        this.testFormData = row
        this.getFormJson()
      } else if (buttonName.opeType === '查看更多') {
        this.moreInfoDialog = true
        this.currentMoreTableItem = row[this.pk]
        this.childPageId = buttonName.pageId
        this.childrelationField = buttonName.relationField
      } else { // 自定义按钮操作
        let Fn = Function
        let customFn = new Fn('buttonName', 'row', onOperationButtonClick)
        customFn.call(this, buttonName, row)
      }
      this.baseRowParam = row
    },
    // 页面大小  页码改变回调
    dataTablePageSizeChange (dataTable, pageSize, currentPage) {
      this.pagerJson.size = pageSize
      this.pagerJson.page = currentPage
      this.clickQueryBtn()
    },
    dataTablePageChange (dataTable, pageSize, currentPage) {
      this.pagerJson.size = pageSize
      this.pagerJson.page = currentPage
      this.clickQueryBtn()
    },
    // 查询
    clickQueryBtn () {
      this.$refs.pageRender.getFormData().then(formInline => { // 拿到搜索条件数据
        // 参数处理
        let copyParam = JSON.parse(JSON.stringify(formInline))
        let queryValues = []
        let queryFields = []
        let queryTypes = []
        let queryParams = {
          pageid: this.pageId,
          page: this.pagerJson.page,
          size: this.pagerJson.size
        }
        // 处理 key和value
        for (let item in copyParam) {
          // 若值是数组
          if (Array.isArray(copyParam[item])) {
            copyParam[item] = copyParam[item].join('#')
          }
          if (copyParam[item]) {
            queryValues.push(copyParam[item])
            queryFields.push(item)
          }
        }
        //获取关联字段
        this.$http.get('/api/lowcode/customize/selectRelationColumnByPageId?pageId=' + this.pageId).then((res) => {
          if (res.data && res.data.length > 0) {
            // 由于主表新增数据后,主表列列表的row发生变化,导致获取的tableName发生变化
            // const temp = Object.keys(this.rowParam)
            // let tableName = temp[0].split(".")[0]
            let tableName = this.mainTablePageName
            res.data.map(item => {
              if (!queryFields[item.columnName]) {
                queryFields.push(item.columnName)
                queryValues.push(this.rowParam[tableName + '.' + item.columnRelation])
                queryTypes.push('0')
              }
            })
            // this.$Message.error('主子表没有关联字段，请确认')
          } else if (this.relationField.length) {
            this.relationField.forEach(item => {
              queryFields.push(item.childTableField)
              queryValues.push(this.rowParam[item.mainTableField])
              queryTypes.push('0')
            })
            // 处理查询类型
            this.$refs.pageRender.getFieldWidgets(true).forEach(item => {
              queryTypes.push(item.field.options.queryType)
            })
            // queryTypes.length && (queryParams.queryTypes = queryTypes + '')
            // 排序方式
            queryParams.orderList = JSON.stringify(this.orderByClause)
            queryParams.queryFields = queryFields + ''
            queryParams.queryValues = queryValues + ''
            queryParams.queryTypes = queryTypes + ''
            // 发送请求
            let urlApi = this.dynamicApi ? this.dynamicApi : '/api/lowcode/customize/page/template/query/'
            util.http.get(
              urlApi + this.pageId,
              {
                params: queryParams,
                paramsSerializer: function (params) {
                  return QS.stringify(params, { arrayFormat: 'repeat' })
                }
              })
              .then((res) => {
                let tableData = res.data.data
                this.getTableDom().setTableData(tableData)
                this.getTableDom().setPagination({
                  total: res.data.recordsTotal
                })
                // 格式化数据字典字段
                tableData.length ? this.formatDicData(tableData) : ''
              }).catch(() => {
              this.$Message.error('查询失败')
            })
          }
        })

      })
    },
    // 新增 编辑逻辑
    getFormData () {
      this.$refs['preForm'].getFormData().then(formData => {
        let queryParam = JSON.parse(JSON.stringify(formData))
        // 参数处理
        for (let item in queryParam) {
          // 为空处理
          if (queryParam[item] === '') {
            queryParam[item] = null
          }
          // 数组
          if (Array.isArray(queryParam[item])) {
            queryParam[item] = JSON.stringify(queryParam[item])
          }
        }
        if (this.addAlertTittle === '新增') {
          this.addNetApi(queryParam)
        } else if (this.addAlertTittle === '编辑') {
          this.updateNetApi(queryParam)
        }
      }).catch(error => {
        this.$message.error(error)
      })
    },
    addNetApi (formData) {
       //获取关联字段
       this.$http.get('/api/lowcode/customize/selectRelationColumnByPageId?pageId=' + this.formId).then((res) => {
        if (res.data && res.data.length && res.data.length > 0) {
          let tableName = this.mainTablePageName
          res.data.map(item => {
          formData[item.columnName] = this.rowParam[tableName + '.' + item.columnRelation]
          })
        }else if(this.relationField && this.relationField.length > 0){
            this.relationField.forEach(item => {
            formData[item.childTableField] = this.rowParam[item.mainTableField]})
        }
        let urlApi = this.dynamicApi ? this.dynamicApi : '/api/lowcode/customize/page/template/add/'
        util.http.post(urlApi + this.formId, formData).then(res => {
          this.addDialogVisible = false
          this.$message.success('新增成功！')
          this.clickQueryBtn()
        }).catch(err => {
          this.$message.error('新增失败！')
          console.log(err)
        })
      })
    },
    updateNetApi (formData) {
      let params = JSON.parse(JSON.stringify(formData))
      const tempStr = this.pk
      params[this.pk] = this.testFormData[this.mainTableName + '.' + tempStr]
      util.http.put('/api/lowcode/customize/page/template/update/' + this.formId, params).then(res => {
        if (res.data.code === 200) {
          this.clickQueryBtn()
          this.addDialogVisible = false
          this.$message.success('修改成功！')
        } else {
          this.$message.error(res.data.msg)
        }
      }).catch(err => {
        console.log(err)
      })
    },
    // 删除逻辑
    handleDelete (row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteNetAPI(row)
      })
    },
    deleteNetAPI (row) {
      let params = {}
      const tempStr = this.pk
      const tempArr = Object.keys(row)
      const tempsStr1 = tempArr
        .filter(item =>
          item === this.mainTablePageName + '.' + tempStr
        )[0]
      params[this.pk] = row[tempsStr1]
      util.http.delete('/api/lowcode/customize/page/template/delByPk/' + this.pageId, { params }).then(res => {
        if (res.data.code === 200) {
          this.$message.success('删除成功！')
          this.clickQueryBtn()
        } else {
          this.$message.error('删除失败！')
        }
      }).catch(err => {
        this.$message.error('删除失败！')
      })
    },
    multiDelete () {
      if (this.getTableDom().getSelectedRow().length) {
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.multiDelApi()
        })
      } else {
        this.$message.warning('请至少勾选一条需要删除的数据！')
      }
    },
    multiDelApi () {
      let ids = []
      this.getTableDom().getSelectedRow().forEach(item => {
        const tempStr = this.pk
        const tempArr = Object.keys(item)
        const tempsStr1 = tempArr
          .filter(item1 =>
            item1 === this.mainTablePageName + '.' + tempStr
          )[0]
        // params[this.pk] = row[tempsStr1]
        ids.push(item[tempsStr1])
      })
      let params = {}
      params[this.pk + 's'] = ids.join(',')
      let urlApi = this.dynamicApi ? this.dynamicApi : '/api/lowcode/customize/page/template/delByPks/'
      util.http.delete(urlApi + this.pageId, { params }).then(res => {
        if (res.data.code === 200) {
          this.$message.success('删除成功！')
          this.clickQueryBtn()
        } else {
          this.$message.error('删除失败！！')
        }
      }).catch(err => {
        console.log(err)
      })
    },
    // 拿到数据表格实例
    getTableDom () {
      let uniqueTable = this.$refs.pageRender.getContainerWidgets().filter(item => { // 表格的唯一名称
        return item.type === 'data-table'
      })
      let tableDom = this.$refs.pageRender.getWidgetRef(uniqueTable[0].name) // 表格实例
      return tableDom
    },
    // 字段配置弹窗表格数据获取
    getColumns () {
      this.orderByClause = []
      this.gridData = this.getTableDom().getTableColumns().filter(item => item.show)
      // 默认全选
      this.gridData.forEach(item => {
        this.$set(item, 'isExport', true)
        this.$set(item, 'isQuery', true)
        // 排序方式
        if (item.sortType !== 'bpx') {
          let obj = item.prop + ' ' + item.sortType
          this.orderByClause.push(obj)
        }
      })

    },
    clickIsQuery () {
      let temArr = this.getTableDom().getTableColumns()
      temArr.map(item => {
        this.gridData.forEach(innerItem => {
          if (item.prop === innerItem.prop) {
            item.show = innerItem.isQuery
          }
        })
      })
      this.getTableDom().setTableColumns(temArr)
    },
    // 导出
    exportMethod () {
      if (this.getTableDom().getTableData().length) {
        let url = this.dynamicApi ? this.dynamicApi : '/api/lowcode/customize/page/template/cscpCustomizeVpages/exportExcel/'
        // let columns = this.getTableDom().getTableColumns()
        let temJson = {}
        this.gridData.forEach(item => {
          if (item.show && item.isExport) {
            temJson[item.prop] = item.label
          }
        })
        this.$http.post(url + this.pageId, temJson, { responseType: 'blob' }).then(res => {
          let content = res.data // 文件流
          let blob = new Blob([content], { type: 'application/octet-stream' })
          let fileName = this.fileName + '.xlsx'
          let link = document.createElement('a')
          link.download = fileName
          link.style.display = 'none'
          link.href = URL.createObjectURL(blob)
          document.body.appendChild(link)
          link.click()
          URL.revokeObjectURL(link.href) // 释放URL 对象
          document.body.removeChild(link)
        })
      } else {
        this.$message.error('表格数据不能为空！')
      }
    },
    // 导入模板下载
    downloadTemplate () {
      if (this.getTableDom().getTableData().length) {
        let url = this.dynamicApi ? this.dynamicApi : '/api/lowcode/customize/page/template/cscpCustomizeVpages/exportExcelTemplate/'
        // let columns = this.getTableDom().getTableColumns()
        let temJson = {}
        this.gridData.forEach(item => {
          if (item.show && item.isExport) {
            temJson[item.prop] = item.label
          }
        })
        this.$http.post(url + this.pageId, temJson, { responseType: 'blob' }).then(res => {
          let content = res.data // 文件流
          let blob = new Blob([content], { type: 'application/octet-stream' })
          let fileName = this.fileName + '.xlsx'
          let link = document.createElement('a')
          link.download = fileName
          link.style.display = 'none'
          link.href = URL.createObjectURL(blob)
          document.body.appendChild(link)
          link.click()
          URL.revokeObjectURL(link.href) // 释放URL 对象
          document.body.removeChild(link)
        })
      } else {
        this.$message.error('表格数据不能为空！')
      }
    },
    // 导入
    labelExcelImport () {
      event.preventDefault()
      document.getElementById('uploadEventFile').click()
    },
    fileChange (el) {
      let uploadEventFile = document.getElementById('uploadEventFile').files
      // 表格字段
      let columns = this.getTableDom().getTableColumns()
      let temJson = {}
      columns.forEach(item => {
        if (item.show) {
          temJson[item.label] = item.prop
        }
      })

      this.file = el.target.files[0]
      if (uploadEventFile === '') {
        this.$message({
          type: 'warning',
          message: '请选择文件'
        })
      }
      let formData = new FormData(document.getElementById('daoru'))
      // 向 formData 对象中添加文件
      formData.append('file', this.file)
      let urlApi = this.dynamicApi ? this.dynamicApi : '/api/lowcode/customize/page/template/vpage/import/'
      this.$axios({
        method: 'post',
        url: util.baseUrl + urlApi + this.pageId + '?pageId=' + this.pageId + '&queryStr=' + encodeURI(JSON.stringify(temJson)), // 请求接口
        data: formData, // 请求接口的参数
        // responseType: "blob",
        headers: {
          'Content-Type': 'multipart/form-data', // 设置请求头请求格式为JSON
          Authorization: window.localStorage.token
        }
      }).then((data) => {
        if (data.data.code === 500) {
          this.$notify({
            type: 'error',
            dangerouslyUseHTMLString: true,
            message: data.data.msg,
            position: 'top-left',
            duration: 4000
          })
        } else if (data.data.code === 200) {
          this.$notify({
            type: 'success',
            dangerouslyUseHTMLString: true,
            message: data.data.msg,
            position: 'top-left',
            duration: 4000
          })
        }
        this.clickQueryBtn() // 表格接口
        document.getElementById('uploadEventFile').value = ''
      })
    },
    // 数据字典
    queryDic () {
      this.$http.get('/api/dic/cscpBasicHxDics', {}).then((response) => {
        this.dicArr = response.data.data.map(item => {
          return item.dicCode
        })
      }).catch(function (error) {
        console.log(error)
      })
    },
    formatDicData (tableData) {
      let temArr = []
      // 拿到是数据字典的字段
      for (let key in tableData[0]) {
        this.dicArr.indexOf(key) !== -1 ? temArr.push(key) : ''
      }
      temArr.forEach(item => {
        this.queryDicItem(item, tableData)
      })
    },
    async queryDicItem (dicCode, tableData) {
      await this.$http.get('/api/dic/cscpHxDicItems/getItems', {
        params: {
          'dicCode.equals': dicCode
        }
      }).then(res => {
        tableData.forEach(item => {
          let temJson = res.data.data.filter(innerItem => {
            return item[dicCode] === innerItem.itemCode
          })
          this.$set(item, dicCode, temJson[0].itemValue)
        })
      }).catch(function (error) {
        console.log(error)
      })
    }
  }
}
</script>

<style lang="less">
#muban {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background-color: #fff;
  padding: 15px;

  .tableBox {
    height: 100%;
    width: 100%;
    overflow-y: auto;
    overflow-x: hidden;
  }

  .is-fullscreen {
    display: flex;
    flex-direction: column;

    .el-dialog__header {
      height: 54px;
    }

    .el-dialog__body {
      flex: 1;
    }
  }
}
</style>
