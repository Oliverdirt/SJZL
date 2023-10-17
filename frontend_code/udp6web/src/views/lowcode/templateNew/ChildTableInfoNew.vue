<template>
  <div id="childTableBox">
    <el-tabs type="border-card" @tab-click="handleTabClick">
      <el-tab-pane :label="item.pageName" v-for="(item, index) in childFormJsonArr" :key="index + 'hbw_'">
        <div class="mainBox">
          <div class="tableBox">
            <VFormRender :global-dsv="rowParam" ref="pageRender" :form-json="tableJson" :preview-state="true" @buttonClick="listenBtnClick"
                         @operationButtonClick="tableOpeColumnClick" @dataTablePageSizeChange="dataTablePageSizeChange"
                         @dataTablePageChange="dataTablePageChange" v-if="reloadRender">
            </VFormRender>
            <form enctype="multipart/form-data" id="daoru">
              <input type="file" name="fileup" id="uploadEventFile" @change="fileChange" style="display: none"/>
            </form>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
    <!--    新增，编辑弹窗 -->
    <el-dialog :title="addAlertTittle" :visible.sync="addDialogVisible" :append-to-body="true" width="60%">
      <VFormRender
        ref="preForm"
        :form-json="pageJson"
        :preview-state="true"
        :form-data="testFormData"
        :option-data="testOptionData"
        v-if="addDialogVisible"
        @buttonClick="listenAddDialogBtnClick"
      >
      </VFormRender>
<!--      <span slot="footer" class="dialog-footer">-->
<!--              <el-button @click="addDialogVisible = false">取 消</el-button>-->
<!--              <el-button type="primary" @click="getFormData">确 定</el-button>-->
<!--            </span>-->
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
        :rowParam="baseRowParam"
        :mainTablePageName="mainTablePageName"
        :relationField="childrelationField"
        v-if="moreInfoDialog"
      >
      </LowcodeTemplateNew>
      <span slot="footer" class="dialog-footer">
        <el-button @click="moreInfoDialog = false;clickQueryBtn()">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import util from '@/libs/util'
import moment from 'moment'
import VFormRender from '@/views/lowcode/vformPro/components/form-render/index'
import { getFieldOptions, getDateTimeFields, getDeptFields } from '@/views/lowcode/utils/optionUtil'
import LowcodeTemplateNew from '@/views/lowcode/templateNew/LowcodeTemplateNewTwo'

export default {
  name: 'ChildTableInfo',
  components: {
    VFormRender,
    LowcodeTemplateNew
  },
  props: {
    relationField: {
      type: Object,
      default: () => {
        return ''
      }
    },
    pageId: {
      type: String,
      default: () => {
        return ''
      }
    },
    cPageId: {
      type: String,
      default: () => {
        return ''
      }
    },
    currentMoreTableItem: {
      type: String,
      default: () => {
        return ''
      }
    },
    rowParam: {
      // type: Json,
      default: () => {
        return ''
      }
    },
    mainTablePageName: {
      type: String,
      default: () => {
        return ''
      }
    },
  },
  data () {
    return {
      currentPageId: '',
      isShowPage: false,
      tableJson: {},
      pageJson: {},
      addDialogVisible: false,
      addAlertTittle: '新增',
      childFormJsonArr: undefined,
      columnData: undefined,
      moreInfoDialog: false,
      childPageId: undefined,
      testFormData: {},
      selectedTable: undefined,
      baseRowParam: undefined,
      childrelationField: [],
      pagerJson: {
        size: 10,
        page: 1,
        total: 0
      },
      listQueryType: {},
      testOptionData: {},
      currentTableId: '',
      currentPk: '', // 主键
      currentKey: '', // 外键
      currentTabIndex: 0,
      optiongroup: [], // 下拉单选多选值数组
      reloadRender: true
    }
  },
  mounted () {
    // 拿到子表 表单信息
    this.getChildFormId()
  },
  methods: {
    // tab页切换
    handleTabClick (val) {
      this.currentTabIndex = Number(val.index)
      this.reloadRender = false
      this.getTableJson()
    },
    // 拿到页面json 和 子表 pageId
    getChildFormId () {
      if (this.cPageId != undefined && this.cPageId != '') {
        //页面绑定了表单
        util.http.get('/api/lowcode/customize/cscpCustomizeVpages/' + this.cPageId)
          .then((res) => {
            this.childFormJsonArr = []
            this.childFormJsonArr[0] = res.data
            this.currentTabIndex = 0
            this.getTableJson()
          }).catch(() => {
          this.$Message.error('页面获取失败')
        })
      } else {
        //没有绑定表单
        util.http.get('/api/lowcode/customize/childCscpCustomizeVpage/' + this.pageId).then(res => {
          this.childFormJsonArr = res.data
          this.currentTabIndex = 0
          this.getTableJson()
        })
      }
    },
    // 列表json
    getTableJson () {
      this.tableJson = JSON.parse(this.childFormJsonArr[this.currentTabIndex].pageJson)
      this.currentTableId = this.childFormJsonArr[this.currentTabIndex].pageId // 当前formId
      this.currentPk = JSON.parse(this.childFormJsonArr[this.currentTabIndex].pageOption).pk // 当前主键
      this.currentKey = this.childFormJsonArr[this.currentTabIndex].subPageFk // 关联字段
      setTimeout(() => {
        this.reloadRender = true
        this.clickQueryBtn()
      }, 0)
    },
    // 表单JSON
    getPageJson () {
      util.http.get('/api/lowcode/customize/cscpCustomizeVpages/' + this.currentPageId)
        .then((res) => {
          this.pageJson = JSON.parse(res.data.pageJson)
          this.addDialogVisible = true
        })
        .catch(() => {
          this.$Message.error('页面获取失败')
        })
    },
    // formrender的点击事件
    listenBtnClick (buttonWidget) {
      let btnAttr = buttonWidget._props.field.options
      if (btnAttr.btnEventType === '查询') {
        this.clickQueryBtn()
      } else if (btnAttr.btnEventType === '新增') {
        this.currentPageId = btnAttr.linkFormPage
        this.testFormData = {}
        this.addAlertTittle = '新增'
        this.getPageJson()
      } else if (btnAttr.btnEventType === '删除') {
        this.multiDelete()
      } else if (btnAttr.btnEventType === '导出') {
        this.exportMethod()
      } else if (btnAttr.btnEventType === '导入') {
        this.labelExcelImport()
      }
    },
    // 表格操作列按钮的点击
    tableOpeColumnClick (dataTable, button, rowIndex, row) {
      if (button.name === '编辑') {
        this.addAlertTittle = '编辑'
        this.currentPageId = button.pageId
        this.getPageJson()
        this.testFormData = row
      } else if (button.name === '删除') {
        this.handleDelete(row)
      } else if (button.opeType === '详情') {
        this.addAlertTittle = '详情'
        this.formId = button.pageId
        for (let item in row) {
          if (item.startsWith('pictureupload')) {
            if (!Array.isArray(row[item])) {
              row[item] = JSON.parse(row[item])
            }
          }
        }
        this.testFormData = row
        this.getFormJson()
      } else if (button.opeType === '查看更多') {
        this.moreInfoDialog = true
        // this.currentMoreTableItem = row[this.pk]
        this.childPageId = button.pageId
        this.childrelationField = button.relationField
      } else { // 自定义按钮操作
        let Fn = Function
        let customFn = new Fn('button', 'row', onOperationButtonClick)
        customFn.call(this, button, row)
      }
      this.baseRowParam = row
      // this.rowParam = row
    },
    // 表单弹窗按钮事件
    listenAddDialogBtnClick (buttonWidget) {
      let btnAttr = buttonWidget._props.field.options
      this.dynamicApi = btnAttr.bindInterfaceAddress
      if (btnAttr.label === '确定') {
        this.getFormData()
      } else if (btnAttr.label === '取消') {
        this.addDialogVisible = false
      }
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
      let tempRow = JSON.parse(JSON.stringify(row))
      delete tempRow['index']
      const tableName = Object.keys(tempRow)[0].split('.')[0]
      params[this.currentPk] = row[tableName + '.' + this.currentPk]
      util.http.delete('/api/lowcode/customize/page/template/delByPk/' + this.currentTableId, { params }).then(res => {
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
      const tableName = Object.keys(this.getTableDom().getSelectedRow()[0])[0].split('.')[0]
      this.getTableDom().getSelectedRow().forEach(item => {
        ids.push(item[tableName + '.' + this.currentPk])
      })
      let params = {}
      params[this.currentPk + 's'] = ids.join(',')
      util.http.delete('/api/lowcode/customize/page/template/delByPks/' + this.currentTableId, { params }).then(res => {
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
    // 新增 编辑逻辑
    getFormData () {
      this.$refs['preForm'].getFormData().then(formData => {
        let queryParam = JSON.parse(JSON.stringify(formData)) // 深拷贝
        // 参数处理
        for (let item in queryParam) {
          // 为空处理
          if (queryParam[item] === '') {
            queryParam[item] = null
          }
          // 数组
          if (Array.isArray(queryParam[item])) {
            queryParam[item] = queryParam[item].join(',')
          }
        }
        if (this.currentKey) {
          queryParam[this.currentKey] = this.currentMoreTableItem // 外键
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
       this.$http.get('/api/lowcode/customize/selectRelationColumnByPageId?pageId=' + this.currentTableId).then((res) => {
        if (res.data && res.data.length && res.data.length > 0) {
          let tableName = this.mainTablePageName
          res.data.map(item => {
          formData[item.columnName] = this.rowParam[tableName + '#' + item.columnRelation]
          })
        }else if(this.relationField){
            this.relationField.forEach(item => {
            formData[item.childTableField] = this.rowParam[item.mainTableField]})
        }else{
          this.$Message.error('主子表没有关联字段，请确认')
        }
        util.http.post('/api/lowcode/customize/page/template/add/' + this.currentPageId, formData).then(res => {
          this.addDialogVisible = false
          this.$message.success('新增成功！')
          this.clickQueryBtn()
        }).catch(err => {
          this.$message.error('新增失败！')
        })
      })
    },
    updateNetApi (formData) {
      let params = JSON.parse(JSON.stringify(formData))
      const tableName = Object.keys(this.testFormData)[0].split('#')[0]
      params[this.currentPk] = this.testFormData[tableName + '#' + this.currentPk]
      util.http.put('/api/lowcode/customize/page/template/update/' + this.currentPageId, params).then(res => {
        if (res.data.code === 200) {
          this.clickQueryBtn()
          this.addDialogVisible = false
          this.$message.success('修改成功！')
        } else {
          this.$message.error(res.data.msg)
        }
      }).catch(err => {
        this.$message.error('修改失败！')
      })
    },
    // 查询
    clickQueryBtn () {
      this.$refs.pageRender[this.currentTabIndex].getFormData().then(formInline => {
        // 参数处理
        let copyParam = JSON.parse(JSON.stringify(formInline))
        let queryValues = []
        let queryFields = []
        let queryTypes = []
        let queryParams = {
          pageid: this.currentTableId,
          page: this.pagerJson.page,
          size: this.pagerJson.size
        }
        //  处理 key和value
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
        queryValues.length && (queryParams.queryValues = queryValues + '')
        queryFields.length && (queryParams.queryFields = queryFields + '')
        // 处理查询类型
        this.$refs.pageRender[this.currentTabIndex].getFieldWidgets(true).forEach(item => {
          queryTypes.push(item.field.options.queryType)
        })
        //处理查询类型个数 根据入参个数传入
        var tempQueryType = []
        for (let i in queryFields) {
          tempQueryType.push(queryTypes[i])
        }
        queryTypes = tempQueryType
        queryTypes.length && (queryParams.queryTypes = queryTypes + '')
        //获取关联字段
        this.$http.get('/api/lowcode/customize/selectRelationColumnByPageId?pageId=' + this.currentTableId).then((res) => {
          if (res.data && res.data.length > 0) {
            // 由于主表新增数据后,主表列列表的row发生变化,导致获取的tableName发生变化
            // const temp = Object.keys(this.rowParam)
            // let tableName = temp[0].split(".")[0]
            let tableName = this.mainTablePageName
            res.data.map(item => {
              if (!queryFields[item.columnName]) {
                queryFields.push(item.columnName)
                queryValues.push(this.rowParam[tableName + '#' + item.columnRelation])
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
          }

          queryParams.queryFields = queryFields + ''
          queryParams.queryValues = queryValues + ''
          queryParams.queryTypes = queryTypes + ''
          // 发送请求
          this.$http.get('/api/lowcode/customize/page/template/query/' + this.currentTableId, { params: queryParams })
            .then((res) => {
              let tableData = res.data.data
              this.getTableDom().setTableData(tableData)
              this.getTableDom().setPagination({
                total: res.data.recordsTotal
              })
              this.pagerJson.total = parseInt(res.data.recordsTotal)
            })
            .catch(() => {
              this.$Message.error('查询失败')
            })
        })
      })
    },
    // 拿到数据表格实例
    getTableDom () {
      let uniqueTable = this.$refs.pageRender[this.currentTabIndex].getContainerWidgets().filter(item => { // 表格的唯一名称
        return item.type === 'data-table'
      })
      let tableDom = this.$refs.pageRender[this.currentTabIndex].getWidgetRef(uniqueTable[0].name) // 表格实例
      return tableDom
    },
    // 导出
    exportMethod () {
      if (this.getTableDom().getTableData().length) {
        let url = '/api/lowcode/customize/page/template/cscpCustomizeVpages/exportExcel/' + this.currentTableId
        let columns = this.getTableDom().getTableColumns()
        let temJson = {}
        columns.forEach(item => {
          if (item.show) {
            temJson[item.prop] = item.label
          }
        })
        this.$http.post(url, temJson, { responseType: 'arraybuffer' }).then(res => {
          let content = res.data // 文件流
          let blob = new Blob([content], { type: 'application/octet-stream' })
          let fileName = 'filename.xlsx'
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
      this.$axios({
        method: 'post',
        url: util.baseUrl + '/api/lowcode/customize/page/template/vpage/import/' + this.currentTableId + '?pageId=' + this.currentTableId + '&queryStr=' + encodeURI(JSON.stringify(temJson)), // 请求接口
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
        }
        if (data.data.code === 200) {
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
#childTableBox {
  width: 100%;
  height: 100%;
  overflow: hidden;

  .el-tabs {
    overflow: hidden;
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;

    .el-tabs__content {
      flex: 1;
      overflow: hidden;

      .el-tab-pane {
        width: 100%;
        height: 100%;
        overflow: hidden;

        .mainBox {
          width: 100%;
          height: 100%;
        }
      }
    }
  }
}
</style>
