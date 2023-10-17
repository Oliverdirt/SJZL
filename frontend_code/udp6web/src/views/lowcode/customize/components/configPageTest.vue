<template>
  <!--  新建页面  编辑页面-->
  <Modal v-model="configModal" title="配置页面" :closable="false" ref="modal" @on-cancel="close" :width="1568"
    :fullscreen="isFullScreen">
    <p slot="header">
      <Row :gutter="40">
        <Col span="20">
        </Col>
        <Col span="4" style="text-align: right">
        <a class="dark-a" href="javascript:void(0)" @click="screenClick">
          <Icon custom="iconfont  icon-FullScreen" v-if="!isFullScreen" :size="23" />
          <Icon custom="iconfont  icon-ExitFullScreen" v-if="isFullScreen" :size="23" />
        </a>
        <a class="dark-a" href="javascript:void(0)" @click="close()">
          <Icon type="md-close" style="margin-left: 6px" :size="20" />
        </a>
        </Col>
      </Row>
    </p>
    <Steps :current="currentStep">
      <Step title="页面基本信息" content="填写页面基本信息" />
      <Step title="页面配置" content="配置页面基本信息" />
      <Step title="页面设计" content="页面设计" />
      <Step title="菜单配置" content="菜单配置" />
    </Steps>
    <br>
    <br>
    <div>
      <!--  step1 配置数据表      -->
      <div class="step1" v-if="currentStep === 0">
        <FormDB ref='formDbRef' :isDisable="isDisable" :tableName.sync="tableName" :tableDesc.sync="tableDesc" @formdbDemoEmit="formdbDemoEmit"
          :pageId1="pageId" />
      </div>
      <!--  新增step2 配置页面基本信息      -->
      <div class="step4" v-if="currentStep === 1">
        <ListConfig ref='listConfig' :formDetail="detail" :editOrCreate="editOrCreate" @ListConfigEmit="ListConfigEmit" />
      </div>
      <!--  step3 表单设计      -->
      <div class="step4" v-if="currentStep === 2">
        <v-form-designer  ref="vfDesigner" :designer-config="designerConfig" :field-list-api="fieldListApi">
          <!-- 自定义按钮插槽演示 -->
          <template #customToolButtons>
            <el-button type="text" @click="saveFormJson(false)"><i class="el-icon-finished" />保存</el-button>
          </template>
        </v-form-designer>
      </div>
      <!--  step4 菜单配置      -->
      <div class="step5" v-if="currentStep === 3">
        <menu-setting ref='menuSetting' :formDetail="detail"></menu-setting>
      </div>
    </div>

    <div slot="footer">
      <Button type="primary" v-show="currentStep > 0" @click="back" style="width: 100px">上一步</Button>
      <!-- <Button type="primary" v-show="currentStep !== 3" @click="next" style="width: 100px" :loading="nextLoading">下一步</Button> -->
      <Button type="primary" v-show="currentStep === 0 && this.formdbDemoEmitVisible" @click="next" style="width: 100px" :loading="nextLoading">下一步
      </Button>
      <Button type="primary" v-show="currentStep === 1 && this.ListConfigEmitVisible" @click="next" style="width: 100px" :loading="nextLoading">下一步
      </Button>
      <Button type="primary" v-show="currentStep === 2" @click="next" style="width: 100px" :loading="nextLoading">下一步
      </Button>
      <Button type="primary" v-show="currentStep === 3" @click="next" style="width: 100px" :loading="nextLoading">完成
      </Button>
      <Button type="default" @click="close()" style="width: 80px">取消</Button>
    </div>
  </Modal>
</template>

<script>
import FormDB from './steps/formdbDemo'
import ListConfig from './steps/listConfigTest'
import menuSetting from './steps/menuSettingDemo'
import { getDefaultFormConfig } from '@/views/lowcode/utils/util.js'
import { componentTypes, queryTypes, queryFieldTypes } from '../config/types.js'
import { basicFields, advancedFields, customFields } from '@/views/lowcode/vformPro/components/form-designer/widget-panel/widgetsConfig.js'
export default {
  name: 'configPage',
  components: { FormDB, ListConfig, menuSetting },
  data() {
    return {
      ListConfigEmitVisible: false,
      formdbDemoEmitVisible: false,
      fieldListApi: {
        URL: this.$util.baseUrl + '/api/lowcode/customize/cscpCustomizeVfields/getListByPageId/' + this.pageId,
        labelKey: 'fieldComment',
        nameKey: 'fieldName',
        headers: { Authorization: localStorage.token }
      },
      // 展示表单设计器的宽度
      designerConfig: {
        resetFormJson: false,
        toolbarMaxWidth: 490
      },
      queryForm: {},
      tableName: '',
      tableDesc: '',
      pageId: '',
      isFullScreen: true,
      currentStep: -1,
      step: 0,
      configModal: false,
      isDisable: false,
      detail: {},
      formJson: null,
      pageJson: null,
      nextLoading: false,
      editOrCreate: '',
      closeFlag: false,
    }
  },
  watch: {
    pageId(newValue, oldValue) {
      this.fieldListApi.URL = this.$util.baseUrl + '/api/lowcode/customize/cscpCustomizeVfields/getListByPageId/' + newValue
    },
    step(newV, oldV) {
      if (newV === 0 && !this.closeFlag) {
        this.$nextTick(() => {
          this.$refs.formDbRef.getColumns(this.tableName)
          if (this.detail.tableName.split(',').length > 1) {
            this.detail.tableName = this.detail.tableName.split(',')
          }
          this.$refs.formDbRef.moduleId = this.detail['moduleId']
          this.$refs.formDbRef.pageName = this.detail['pageName']
          this.$refs.formDbRef.pageType = this.detail['pageType']
          this.$refs.formDbRef.tableType = this.detail['tableType']
          this.$refs.formDbRef.modelId = this.detail['modelId']
          this.$refs.formDbRef.tableName = this.detail['tableName']
          this.$refs.formDbRef.mainPageId = this.detail['mainPageId']

          this.$refs.formDbRef.moduleId = this.detail['moduleId']
          this.$refs.formDbRef.tableName = this.detail['tableDesc']
          this.$refs.formDbRef.subPageFk = this.detail['subPageFk']
          this.$refs.formDbRef.pageId = this.detail['pageId']
          this.$refs.formDbRef.modelName = this.detail['modelName']
          this.$refs.formDbRef.moduleName = this.detail['moduleName']

          this.$refs.formDbRef.moduleList = this.detail['moduleList']
          this.$refs.formDbRef.modelList = this.detail['modelList']
          this.$refs.formDbRef.tableName = this.detail['tableName']
          this.$refs.formDbRef.tableNameList = this.detail['tableNameList']
          this.$refs.formDbRef.tableList = this.detail['tableList']
          this.$refs.formDbRef.mainPageList = this.detail['mainPageList']
          this.$refs.formDbRef.modelDisabled = this.detail['modelDisabled']
          this.$refs.formDbRef.tableDisabled = this.detail['tableDisabled']
          this.$refs.formDbRef.mainPageDisabled = this.detail['mainPageDisabled']
          // 区别createPageForm.moduleId和moduleId
          this.$refs.formDbRef.createPageForm.moduleId = this.detail['moduleId']
          this.$refs.formDbRef.createPageForm.pageName = this.detail['pageName']
          this.$refs.formDbRef.createPageForm.pageType = this.detail['pageType']
          this.$refs.formDbRef.createPageForm.tableType = this.detail['tableType']
          this.$refs.formDbRef.createPageForm.modelId = this.detail['modelId']
          this.$refs.formDbRef.createPageForm.tableName = this.detail['tableName']
          this.$refs.formDbRef.createPageForm.mainPageId = this.detail['mainPageId']
        })
      }
    }
  },
  methods: {
    formdbDemoEmit(data){
      if(data){
        this.formdbDemoEmitVisible = true
      }
    },
    ListConfigEmit(dataArrData){
      if(Array.isArray(dataArrData)){
        this.ListConfigEmitVisible = true
      }
    },
    buildDefaultPage() {
      // 初始化
      window.localStorage.setItem('form__config__backup', '')
      window.localStorage.setItem('widget__list__backup', '')
      // 构建默认页面设计
      if (this.editOrCreate === 'create') {
        // 加上第二步之后,如果用户做了操作,也要用第二步的数据进行渲染,让第二步的操作控制第三步页面的显示
        this.detail.pageType === '0' ? this.buildDefaultFormJson() : this.buildDefaultPageJson() // 1是列表 0是表单
      } else if (this.editOrCreate === 'edit') {
        this.getEditDefaultPageJson()
      }
    },
    buildDefaultPageJson() {
      const url = `/api/lowcode/customize/cscpCustomizeVfields/getListByPageId/${this.pageId}`
      this.$http.get(url).then(res => {
        let widgetJson = []
        res.data.forEach((field) => {
          if (field.fieldQuery === '1') { // 要查询的字段
            if (field.controlType === '0') { // 如果为文本框,渲染为单行输入字段
              basicFields.forEach(basicField => {
                if (basicField.type === 'input') {
                  let varField = JSON.parse(JSON.stringify(basicField))
                  varField.options.label = field.fieldComment ? field.fieldComment : field.fieldName
                  varField.options.name = field.fieldName
                  varField.options.type = 'text'
                  varField.options.queryType = field.queryType
                  varField.id = field.fieldId
                  widgetJson.push(varField)
                }
              })
            } else if (field.controlType === '1') { //单选下拉框
              basicFields.forEach(basicField => {
                if (basicField.type === 'select') {
                  let varField = JSON.parse(JSON.stringify(basicField))
                  varField.options.label = field.fieldComment ? field.fieldComment : field.fieldName
                  varField.options.name = field.fieldName
                  varField.options.multiple = false
                  varField.options.queryType = field.queryType
                  varField.id = field.fieldId
                  widgetJson.push(varField)
                }
              })
            } else if (field.controlType === '2') { //多选下拉框
              basicFields.forEach(basicField => {
                if (basicField.type === 'select') {
                  let varField = JSON.parse(JSON.stringify(basicField))
                  varField.options.label = field.fieldComment ? field.fieldComment : field.fieldName
                  varField.options.name = field.fieldName
                  varField.options.multiple = true
                  varField.options.queryType = field.queryType
                  varField.id = field.fieldId
                  widgetJson.push(varField)
                }
              })
            } else if (field.controlType === '3') { //时间范围
              basicFields.forEach(basicField => {
                if (basicField.type === 'time-range') {
                  let varField = JSON.parse(JSON.stringify(basicField))
                  varField.options.label = field.fieldComment ? field.fieldComment : field.fieldName
                  varField.options.name = field.fieldName
                  varField.options.queryType = field.queryType
                  varField.id = field.fieldId
                  widgetJson.push(varField)
                }
              })
            } else if (field.controlType === '4') { //日期范围带时间
              basicFields.forEach(basicField => {
                if (basicField.type === 'date-range') {
                  let varField = JSON.parse(JSON.stringify(basicField))
                  varField.options.label = field.fieldComment ? field.fieldComment : field.fieldName
                  varField.options.name = field.fieldName
                  varField.options.type = 'datetimerange'
                  varField.options.queryType = field.queryType
                  varField.id = field.fieldId
                  widgetJson.push(varField)
                }
              })
            } else if (field.controlType === '5') { //日期范围
              basicFields.forEach(basicField => {
                if (basicField.type === 'date-range') {
                  let varField = JSON.parse(JSON.stringify(basicField))
                  varField.options.label = field.fieldComment ? field.fieldComment : field.fieldName
                  varField.options.name = field.fieldName
                  varField.options.queryType = field.queryType
                  varField.id = field.fieldId
                  widgetJson.push(varField)
                }
              })
            }
          }
        })
        let json = {
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
            'onPageSizeChange': '',
            'onCurrentPageChange': '',
            'onSelectionChange': '',
            'onHideOperationButton': '',
            'onDisableOperationButton': '',
            'onGetOperationButtonLabel': '',
            'onOperationButtonClick': '',
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
        const tempArr = res.data
        tempArr.forEach((item, index) => {
          if (item.fieldList === '1') {
            let temJson = {
              'columnId': index + 1,
              'prop': item.fieldName,
              'label': item.fieldComment,
              'width': '150',
              'show': true,
              'align': 'right',
              'sortable': true,
              'sortType':'asc',
              'formatS': 'n1'
            }
            json.options.tableColumns.push(temJson)
          } else {
            let temJson = {
              'columnId': index + 1,
              'prop': item.fieldName,
              'label': item.fieldComment,
              'width': '150',
              'show': false,
              'align': 'right',
              'sortable': true,
              'sortType':'asc',
              'formatS': 'n1'
            }
            json.options.tableColumns.push(temJson)
          }
        })
        widgetJson.push(json)
        window.localStorage.setItem('form__config__backup', JSON.stringify(getDefaultFormConfig()))
        window.localStorage.setItem('widget__list__backup', JSON.stringify(widgetJson))
        this.step++
        this.currentStep++
      })
    },
    buildDefaultFormJson() {
      const url = `/api/lowcode/customize/cscpCustomizeVfields/getListByPageId/${this.pageId}`
      this.$http.get(url).then(res => {
        let widgetJson = []
        res.data.forEach((field) => {
          if (field.isForm === '1') { // 要表单展示的字段
            componentTypes.forEach(componentType => {
              if (field.componentType === componentType.value) {
                basicFields.forEach(basicField => {
                  if (basicField.type === componentType.type) {
                    let varField = JSON.parse(JSON.stringify(basicField))
                    varField.options.label = field.fieldComment ? field.fieldComment : field.fieldName
                    varField.options.name = field.fieldName
                    varField.id = field.fieldId
                    widgetJson.push(varField)
                  }
                })
                advancedFields.forEach(advancedField => {
                  if (advancedField.type == componentType.type) {
                    let varField = JSON.parse(JSON.stringify(advancedField))
                    varField.options.label = field.fieldComment ? field.fieldComment : field.fieldName
                    varField.options.name = field.fieldName
                    varField.id = field.fieldId
                    widgetJson.push(varField)
                  }
                })
                customFields.forEach(customField => {
                  if (customField.type == componentType.type) {
                    let varField = JSON.parse(JSON.stringify(customField))
                    varField.options.label = field.fieldComment ? field.fieldComment : field.fieldName
                    varField.options.name = field.fieldName
                    varField.id = field.fieldId
                    widgetJson.push(varField)
                  }
                })
              }
            })
          }
        })
        window.localStorage.setItem('form__config__backup', JSON.stringify(getDefaultFormConfig()))
        window.localStorage.setItem('widget__list__backup', JSON.stringify(widgetJson))
        this.step++
        this.currentStep++
      })
    },
    getEditDefaultPageJson() {
      this.$http.get('/api/lowcode/customize/cscpCustomizeVpages/' + this.pageId).then(res => {
        let widgetList = []
        let formConfig = {}
        if (res.data.pageJson) {
          widgetList = JSON.parse(res.data.pageJson).widgetList
          formConfig = JSON.parse(res.data.pageJson).formConfig
        }
        window.localStorage.setItem('form__config__backup', JSON.stringify(formConfig))
        window.localStorage.setItem('widget__list__backup', JSON.stringify(widgetList))
        this.step++
        this.currentStep++
      })
    },
    // 1.保存表单
    saveFormInfo() {
      // 保存基本信息
      // this.detail['tableName'] = this.$refs.formDbRef.createPageForm.tableName
      if (Array.isArray(this.$refs.formDbRef.createPageForm.tableName)) {
        this.$refs.formDbRef.createPageForm.tableName = this.$refs.formDbRef.createPageForm.tableName.join(',')
      }
      // this.detail['moduleId'] = this.$refs.formDbRef.moduleId
      // this.detail['pageName'] = this.$refs.formDbRef.pageName
      // this.detail['pageType'] = this.$refs.formDbRef.pageType
      // this.detail['tableType'] = this.$refs.formDbRef.tableType
      // this.detail['modelId'] = this.$refs.formDbRef.modelId
      // this.detail['tableName'] = this.$refs.formDbRef.tableName
      // this.detail['mainPageId'] = this.$refs.formDbRef.mainPageId

      // 新增用createPageForm对象给detail对象赋值
      this.detail['moduleId'] = this.$refs.formDbRef.createPageForm.moduleId
      this.detail['pageName'] = this.$refs.formDbRef.createPageForm.pageName
      this.detail['pageType'] = this.$refs.formDbRef.createPageForm.pageType
      this.detail['tableType'] = this.$refs.formDbRef.createPageForm.tableType
      this.detail['modelId'] = this.$refs.formDbRef.createPageForm.modelId
      this.detail['tableName'] = this.$refs.formDbRef.createPageForm.tableName

      this.detail['mainPageId'] = this.$refs.formDbRef.createPageForm.mainPageId

      this.detail['pageTable'] = this.$refs.formDbRef.createPageForm.tableName
      var tableTypes = this.$refs.formDbRef.createPageForm.tableType
      this.detail['tableDesc'] = this.$refs.formDbRef.createPageForm.tableName
      this.detail['subPageFk'] = this.$refs.formDbRef.subPageFk
      this.detail['modelName'] = this.$refs.formDbRef.modelName
      this.detail['moduleName'] = this.$refs.formDbRef.moduleName

      this.detail['moduleList'] = this.$refs.formDbRef.moduleList
      this.detail['modelList'] = this.$refs.formDbRef.modelList

      this.detail['tableNameList'] = this.$refs.formDbRef.tableNameList
      this.detail['tableList'] = this.$refs.formDbRef.tableList
      this.detail['mainPageList'] = this.$refs.formDbRef.mainPageList
      this.detail['modelDisabled'] = this.$refs.formDbRef.modelDisabled
      this.detail['tableDisabled'] = this.$refs.formDbRef.tableDisabled
      this.detail['mainPageDisabled'] = this.$refs.formDbRef.mainPageDisabled

      var tableList = this.$refs.formDbRef.tableList
      // 区别createPageForm.tableName和tableName,一共7个
      var tableNames = this.$refs.formDbRef.createPageForm.tableName
      var tableNameList = this.$refs.formDbRef.tableNameList
      // 设置子表subPageFk
      if (tableTypes && tableTypes == '2' && !this.detail['subPageFk'] && tableList.subTableFkName) {
        var childTableList = tableList.subTableFkName.split(',')
        for (let i = 0; i < tableNameList.length; i++) {
          if (tableNames == tableNameList[i].tableName) {
            this.detail['subPageFk'] = childTableList[i]
            continue
          }
        }
      }

      this.detail['pageId'] = this.pageId
      this.tableName = this.detail.tableName
      // this.detail.pageOption = JSON.stringify({ 'pk': window.localStorage.getItem('keyName') })
      this.detail.pageOption = '{"pk":"id"}'
      let copyDetail = JSON.parse(JSON.stringify(this.detail))
      this.fieldListApi.URL = this.$util.baseUrl + '/api/lowcode/customize/cscpCustomizeVfields/getListByPageId/' + this.pageId
      this.nextLoading = true
      // 新增页面的时候校验,编辑页面时不校验
      if (!(this.$refs.formDbRef.pageId1)) { // 新增
        // 没有pageId1,需要校验
        if (this.$refs.formDbRef.mainPageIdRequired) {
          this.$Message.error('表单校验失败,请检查输入')
          this.nextLoading = false
          return;
        }
        this.$refs.formDbRef.$refs.createPageRef.validate(valid => {
          if (valid) {
            this.$http
              .put('/api/lowcode/customize/cscpCustomizeVpages', copyDetail)
              .then((res) => {
                this.pageId = res.data.pageId
                this.detail['pageId'] = res.data.pageId
                this.nextLoading = false
                this.$Message.success('更新成功')
                // this.buildDefaultPage()
                this.currentStep++
                this.step++

              })
              .catch(() => {
                this.$Message.error('更新失败')
                this.nextLoading = false
              })
          } else {
            this.$Message.error('表单校验失败,请检查输入')
            this.nextLoading = false
            return
          }
        })
      } else {
        // this.buildDefaultPage()
        this.currentStep++
        this.step++
      }
    },

    // 3.保存字段信息
    saveFields() {
      if (this.editOrCreate === 'edit') {
        this.buildDefaultPage()
        this.nextLoading = false
        return
      }
      if (!this.checkForm()) {
        return false
      }
      const flag = this.$refs['listConfig'].addOrEditFlag
      let url = '/api/lowcode/customize/cscpCustomizeVfieldPages/save'
      if (flag === 'edit') {
        url = '/api/lowcode/customize/cscpCustomizeVfieldPages/update'
      }
      let fields = this.$refs['listConfig'].dataArr.map(item => {
        return JSON.parse(JSON.stringify(item))
      })
      for (let i = 0; i < fields.length; i++) {
        fields[i].fieldQuery = fields[i].fieldQuery ? fields[i].fieldQuery === '0' ? '0' : '1' : '0'
        fields[i].fieldList = fields[i].fieldList ? fields[i].fieldList === '0' ? '0' : '1' : '0'
        fields[i].isForm = fields[i].isForm ? fields[i].isForm === '0' ? '0' : '1' : '0'
        fields[i].fieldOrder = i + 1;
      }

      fields.map(item => item.fieldName = item.tableName + '#' + item.fieldName)
      this.nextLoading = true
      this.$util.http.post(url, fields).then(res => {
        this.$Message.success(`操作成功`)
        this.buildDefaultPage()
        this.nextLoading = false
      }).catch(() => {
        this.$Message.error(`操作失败`)
        this.nextLoading = false
      })
    },

    checkForm() {
      let flag = true
      this.$refs['listConfig'].dataArr.forEach(item => {
        if ((item.fieldQuery === '1' || item.fieldQuery === true) && (!item.queryType || !item.controlType)) {
          flag = false
        }
      })
      return flag
    },

    // 4.保存表单json数据
    saveFormJson(nextStep) {
      let formJson = this.$refs.vfDesigner.getFormJson()
      this.detail.pageJson = JSON.stringify(formJson)
      this.pageJson = JSON.stringify(formJson)
      this.nextLoading = true
      this.$http.put('/api/lowcode/customize/cscpCustomizeVpages', this.detail).then(response => {
        this.$message.success('表单已保存！')
        if (nextStep) {
          this.currentStep++
          this.step++
        }
        this.nextLoading = false
      }).catch(err => {
        this.$Message.error(`修改失败：${err.response.data.msg}`)
        this.nextLoading = false
      })
    },
    getButton(jsonObj, buttonArr) {
      if (!jsonObj) {
        return buttonArr
      }
      for (let key in jsonObj) {
        let ele = jsonObj[key]
        if (ele && typeof (ele) === 'object') {
          if (ele && ele.type && ele.type == 'button') {
            buttonArr.push(ele)
            continue
          }
          if (ele && ele.operationButtons && ele.operationButtons.length > 0) {
            ele.operationButtons.map(item => {
              buttonArr.push(item)
            })
            continue;
          }
          this.getButton(ele, buttonArr)
        }
      }
      return buttonArr
    },
    // 5.保存菜单
    saveMenu() {
      let jsonObj = JSON.parse(this.pageJson)
      var buttunArrr = []
      buttunArrr = this.getButton(jsonObj, buttunArrr)
      this.nextLoading = true
      this.$refs['menuSetting'].$refs['menuFormRef'].validate(valid => {
        if (valid) {
          var flag = this.$refs.menuSetting.handleSubmit(buttunArrr, this.pageId, this.detail['pageType'])
          if (flag === 0) {
            this.close()
          }
        }
        this.nextLoading = false
      })
    },
    back() {
      if(this.currentStep === 1){
        this.ListConfigEmitVisible = false
        this.formdbDemoEmitVisible = false
      }
      if (this.currentStep === 2) {
        this.$Modal.confirm({
          title: '提示',
          content: '<p>请先保存当前操作,否则返回上一步将取消当前的操作!</p>',
          onOk: () => {
            this.currentStep--
          },
          onCancel: () => { }
        })
      } else {
        this.currentStep--
      }
    },
    next() {
      if (this.currentStep === 0) {
        this.saveFormInfo()
        this.nextLoading = false
      } else if (this.currentStep === 1) {
        this.saveFields()
        this.nextLoading = false
      } else if (this.currentStep === 2) {
        // 保存表单json数据
        this.saveFormJson(true)
        this.nextLoading = false
      } else if (this.currentStep === 3) {
        // 保存菜单
        this.saveMenu()
      }
    },
    screenClick() {
      if (this.isFullScreen) {
        this.isFullScreen = false
      } else {
        this.isFullScreen = true
      }
    },
    close() {
      window.localStorage.setItem('widget__list__backup', '')
      window.localStorage.setItem('form__config__backup', '')
      this.closeFlag = true
      this.currentStep = -1
      this.step = 0
      this.configModal = false
      this.$parent.queryList()
      this.tableName = ''
      this.pageId = null
      this.formdbDemoEmitVisible = false
    }
  }
}
</script>

<style scoped>
.step1 {
  width: 40%;
  margin: 0 auto;
}
</style>
