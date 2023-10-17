<template>
  <div class="addpageContainer">
    <span class="mTitle">新建页面</span>
    <div class="mine_el_tab">
      <el-tabs v-model="activeName" @tab-click="handleTabClick">
        <!--  step1 配置数据表      -->
        <!--        <el-tab-pane style="visibility: hidden" label="页面基本信息" name="first">-->

        <!--        </el-tab-pane>-->
        <!--  新增step2 配置页面基本信息      -->
        <el-tab-pane label="页面配置" name="second">
          <div class="step2" v-if="currentStep === 1">
            <ListConfig ref='listConfig' :formDetail="detail" :editOrCreate="editOrCreate"
                        @ListConfigEmit="ListConfigEmit"/>
          </div>
        </el-tab-pane>
        <!--  step3 表单设计      -->
        <el-tab-pane label="页面设计" name="third">
          <div class="step3" v-if="currentStep === 2">
            <v-form-designer ref="vfDesigner" :designer-config="designerConfig" :field-list-api="fieldListApi">
              <!-- 自定义按钮插槽演示 -->
              <template #customToolButtons>
                <el-button type="text" @click="saveFormJson(false)"><i class="el-icon-finished"/>保存</el-button>
              </template>
            </v-form-designer>
          </div>
        </el-tab-pane>
        <!--  step4 菜单配置      -->
        <el-tab-pane label="菜单配置" name="four">
          <div class="step4" v-if="currentStep === 3">
            <menu-setting ref='menuSetting' :formDetail="detail"></menu-setting>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div class="btnClass">
      <Button type="primary" v-show="currentStep > 1" @click="back" style="width: 80px;margin-right:12px">上一步</Button>
      <Button type="primary" v-show="currentStep < 3" @click="next" style="width: 80px;margin-right:12px"
              :loading="nextLoading">下一步
      </Button>
      <Button type="primary" v-show="currentStep === 3" @click="next" style="width: 80px;margin-right:12px"
              :loading="nextLoading">完成
      </Button>
      <Button type="default" @click="close" style="width: 80px">取消</Button>
    </div>
    <!--    新建页面弹窗   改造后（原第一步操作）-->
    <el-dialog
      :before-close="handleClose"
      :close-on-click-modal="false"
      class="newPageDialog"
      append-to-body
      title="新建页面"
      :visible.sync="buildPageModal"
      width="520px">
      <FormDB
        :pageParam="pageParam"
        ref='formDbRef'
      />
      <span slot="footer" class="dialog-footer">
    <el-button size="small" @click="buildPageModal = false;$emit('addPageCancel')">取 消</el-button>
    <el-button size="small" type="primary" @click="next()">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
import FormDB from '@/views/lowcode/customize/components/steps/formdbDemo.vue'
import ListConfig from '@/views/lowcode/customize/components/steps/listConfigTest'
import menuSetting from '@/views/lowcode/customize/components/steps/menuSettingDemo'
import {getDefaultFormConfig} from '@/views/lowcode/utils/util.js'
import {componentTypes, queryTypes, queryFieldTypes} from '@/views/lowcode/customize/config/types.js'
import {
  basicFields,
  advancedFields,
  customFields
} from '@/views/lowcode/vformPro/components/form-designer/widget-panel/widgetsConfig.js'
import {_debounce} from "@/libs/util";
import {
  SEARCH_BUTTON,
  TABLE_JSON,
  FORM_BUTTON,
  TABLE_OPE_SCRIPTS,
  FORM_BUTTON_SCRIPTS,
  SEARCH_BUTTON_SCRIPTS,
  PAGE_CHANGE_SCRIPTS
} from "@/views/lowcode/tableDesigner/pageManage/buttonJJson";

export default {
  name: 'addPage',
  props: ['pageParam'],
  components: {FormDB, ListConfig, menuSetting},
  data() {
    return {
      buildPageModal: true,
      activeName: 'first',
      title: '',
      ListConfigEmitVisible: false,
      fieldListApi: {
        URL: this.$util.baseUrl + '/api/lowcode/customize/cscpCustomizeVfields/getListByPageId/' + this.pageId,
        labelKey: 'fieldComment',
        nameKey: 'fieldName',
        headers: {Authorization: localStorage.token}
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
      detail: {},
      formJson: null,
      pageJson: null,
      nextLoading: false,
      editOrCreate: 'create',
      closeFlag: false,
      copyPageJson: undefined // 最后一步回到第三步时 需要保存之前的页面数据
    }
  },
  mounted() {
    this.buildPageModal = true
    this.title = '新建页面'
    window.localStorage.setItem('widget__list__backup', '')
    window.localStorage.setItem('form__config__backup', '')
    this.currentStep = 0
    this.configModal = true
    this.pageId = ''
    this.tableName = ''
    this.tableDesc = ''
  },
  watch: {
    pageId(newValue, oldValue) {
      this.fieldListApi.URL = this.$util.baseUrl + '/api/lowcode/customize/cscpCustomizeVfields/getListByPageId/' + newValue
    }
  },
  methods: {
    handleClose() {
      this.$emit('addPageCancel')
    },
    handleTabClick() {

    },
    ListConfigEmit(dataArrData) {
      if (Array.isArray(dataArrData)) {
        this.ListConfigEmitVisible = true
      }
    },
    buildDefaultPage() {
      // 初始化
      window.localStorage.setItem('form__config__backup', '')
      window.localStorage.setItem('widget__list__backup', '')
      // 加上第二步之后,如果用户做了操作,也要用第二步的数据进行渲染,让第二步的操作控制第三步页面的显示
      this.detail.pageType === '0' ? this.buildDefaultFormJson() : this.buildDefaultPageJson() // 1是列表 0是表单
    },
    buildDefaultPageJson() {
      const url = `/api/lowcode/customize/cscpCustomizeVfields/getListByPageId/${this.pageId}`
      let api = this.$refs.formDbRef.createPageForm.api
      let tableName = this.$refs.formDbRef.createPageForm.tableName
      this.$http.get(url).then(res => {
        let widgetJson = []
        let gridJson = {
          'type': 'grid',
          'category': 'container',
          'icon': 'grid',
          'options': {
            'name': 'grid23187',
            'hidden': false,
            'gutter': 12,
            'colHeight': '50',
            'customClass': []
          },
          'id': 'grid23187',
          'cols': []
        }
        let tempWidget = []
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
                  // widgetJson.push(varField)
                  tempWidget.push(varField)
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
                  // widgetJson.push(varField)
                  tempWidget.push(varField)
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
                  // widgetJson.push(varField)
                  tempWidget.push(varField)
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
                  // widgetJson.push(varField)
                  tempWidget.push(varField)
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
                  // widgetJson.push(varField)
                  tempWidget.push(varField)
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
                  // widgetJson.push(varField)
                  tempWidget.push(varField)
                }
              })
            }
          }
        })
        // 拼凑搜索条件json对象
        for (let i = 0; i <= tempWidget.length; i++) {
          let gridColJson = {
            'type': 'grid-col',
            'category': 'container',
            'icon': 'grid-col',
            'internal': true,
            'widgetList': [],
            'options': {
              'name': '',
              'hidden': false,
              'span': 8,
              'offset': 0,
              'push': 0,
              'pull': 0,
              'responsive': false,
              'md': 12,
              'sm': 12,
              'xs': 12,
              'customClass': ''
            },
            'id': ''
          }
          let num = Math.floor(Math.random() * 100000)
          gridColJson.options.name = `gridCol${num}`
          gridColJson.id = `grid-col-${num}`
          if (!tempWidget[i]) {
            api ? gridColJson.widgetList.push(SEARCH_BUTTON(SEARCH_BUTTON_SCRIPTS(JSON.parse(api), tableName))) : gridColJson.widgetList = [] // 是否绑定了自定义API
          } else {
            gridColJson.widgetList.push(tempWidget[i])
          }
          gridJson.cols.push(gridColJson)
        }
        // 拼凑列表json
        let hasChild = this.$refs.formDbRef.createPageForm.hasChild
        let FORM_CREATED_SCRIPTS = api ? SEARCH_BUTTON_SCRIPTS(JSON.parse(api), tableName) : "this.$emit('initQuery')" // formCreated初始化脚本
        // 判断有没有选择api
        let json = ''
        if (api) {
          json = hasChild === '1' ? TABLE_JSON({
            onOperationButtonClick: TABLE_OPE_SCRIPTS(),
            onPageSizeChange: SEARCH_BUTTON_SCRIPTS(JSON.parse(api), tableName)
          }) : TABLE_JSON({
            onPageSizeChange: SEARCH_BUTTON_SCRIPTS(JSON.parse(api), tableName)
          }) // 是否下钻
        } else {
          json = TABLE_JSON()
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
              'formatS': 'render',
              'sortType': 'bpx',
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
              'formatS': 'n1',
              'sortType': 'bpx'
            }
            json.options.tableColumns.push(temJson)
          }
        })

        widgetJson.push(gridJson) // 搜索条件
        widgetJson.push(json) // 表格
        this.copyPageJson = JSON.stringify(widgetJson)
        window.localStorage.setItem('form__config__backup', JSON.stringify(getDefaultFormConfig(FORM_CREATED_SCRIPTS)))
        window.localStorage.setItem('widget__list__backup', JSON.stringify(widgetJson))
        this.step++
        this.currentStep++
        this.activeName = 'third'
      })
    },
    buildDefaultFormJson() {
      const url = `/api/lowcode/customize/cscpCustomizeVfields/getListByPageId/${this.pageId}`
      this.$http.get(url).then(res => {
        let widgetJson = []
        res.data.forEach((field) => {
          if (field.isForm === '1') { // 要表单展示的字段
            componentTypes.forEach(componentType => {
              if (field.componentType.trim() === componentType.value) {
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
        let api = this.$refs.formDbRef.createPageForm.api
        widgetJson = api ? widgetJson.concat(FORM_BUTTON(FORM_BUTTON_SCRIPTS(JSON.parse(api)))) : widgetJson.concat(FORM_BUTTON()) //若填了api则初始化查询API
        this.copyPageJson = JSON.stringify(widgetJson)
        window.localStorage.setItem('form__config__backup', JSON.stringify(getDefaultFormConfig()))
        window.localStorage.setItem('widget__list__backup', JSON.stringify(widgetJson))
        this.step++
        this.currentStep++
        this.activeName = 'third'
      })
    },
    // 1.保存表单
    saveFormInfo() {
      // 保存基本信息
      let tempTableName = this.$refs.formDbRef.createPageForm.tableName
      if (Array.isArray(this.$refs.formDbRef.createPageForm.tableName)) {
        tempTableName = this.$refs.formDbRef.createPageForm.tableName.join(',')
      }
      this.detail['moduleId'] = this.$refs.formDbRef.createPageForm.moduleId
      this.detail['pageName'] = this.$refs.formDbRef.createPageForm.pageName
      this.detail['pageType'] = this.$refs.formDbRef.createPageForm.pageType
      this.detail['tableType'] = this.$refs.formDbRef.createPageForm.tableType
      this.detail['modelId'] = this.$refs.formDbRef.createPageForm.modelId
      this.detail['tableName'] = tempTableName

      this.detail['mainPageId'] = this.$refs.formDbRef.createPageForm.mainPageId

      this.detail['pageTable'] = tempTableName
      var tableTypes = this.$refs.formDbRef.createPageForm.tableType
      this.detail['tableDesc'] = tempTableName
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
      var tableNames = tempTableName
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

      this.detail.pageOption = '{"pk":"id"}'
      let copyDetail = JSON.parse(JSON.stringify(this.detail))
      this.fieldListApi.URL = this.$util.baseUrl + '/api/lowcode/customize/cscpCustomizeVfields/getListByPageId/' + this.pageId
      this.nextLoading = true
      // 新增页面的时候校验,编辑页面时不校验
      if (!this.$refs.formDbRef.pageId1) { // 新增
        // 没有pageId1,需要校验
        if (this.$refs.formDbRef.mainPageIdRequired) {
          this.$Message.error('表单校验失败,请检查输入')
          this.nextLoading = false
          return
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
                this.currentStep++
                this.step++
                this.activeName = 'second'
                this.buildPageModal = false
              })
              .catch(() => {
                this.$Message.error('更新失败')
                this.nextLoading = false
              })
          } else {
            // this.$Message.error('表单校验失败,请检查输入')
            this.nextLoading = false
            return
          }
        })
      } else {
        this.currentStep++
        this.step++
        this.activeName = 'second'
        this.buildPageModal = false
      }
    },

    // 3.保存字段信息
    saveFields() {
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
        fields[i].fieldOrder = i + 1
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
          this.activeName = 'four'
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
            continue
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
      if (this.$refs.menuSetting.menuForm.genMenuFlag === '2') { //不需要校验
        var flag = this.$refs.menuSetting.handleSubmit(buttunArrr, this.pageId, this.detail['pageType'])
        if (flag === 0) {
          this.close()
        }
        this.nextLoading = false
      } else {
        this.$refs['menuSetting'].$refs['menuForm'].validate(valid => {
          if (valid) {
            var flag = this.$refs.menuSetting.handleSubmit(buttunArrr, this.pageId, this.detail['pageType'])
            if (flag === 0) {
              this.close()
            }
          }
          this.nextLoading = false
        })
      }
    },
    back: _debounce(function () {
      // if (this.currentStep === 1) {
      //   this.ListConfigEmitVisible = false
      //   this.activeName = 'first'
      // }
      if (this.currentStep === 2) {
        this.$Modal.confirm({
          title: '提示',
          content: '<p>请先保存当前操作,否则返回上一步将取消当前的操作!</p>',
          onOk: () => {
            this.currentStep--
            this.activeName = 'second'
          }
        })
      }
      if (this.currentStep === 3) {
        window.localStorage.setItem('form__config__backup', JSON.stringify(getDefaultFormConfig()))
        window.localStorage.setItem('widget__list__backup', this.copyPageJson)
        this.activeName = 'third'
        this.currentStep--
      }
    }, 200),
    next: _debounce(function () {
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
    }, 200),
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
      this.tableName = ''
      this.pageId = null
      this.$emit('addPageCancel')
    }
  }
}
</script>
<style lang="less">
// 新建页面弹窗样式
.newPageDialog {
  .el-dialog {
    background-image: url("../../../../assets/lowcode/dialogBg.png") !important;
    background-size: cover;
  }

  .el-dialog__footer {
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .el-dialog__body {
    padding: 25px 40px 25px 25px;
  }

  .el-dialog__header {
    .el-dialog__title {
      font-size: 16px;
      font-family: PingFang SC-Semibold, PingFang SC;
      font-weight: 600;
      color: rgba(0, 0, 0, 0.9);
    }
  }
}
</style>
<style lang="less" scoped>
.addpageContainer {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  // 新建页面标题样式
  .mTitle {
    position: absolute;
    top: 14px;
    left: 15px;
    font-size: 14px;
    font-family: PingFang SC-Semibold, PingFang SC;
    font-weight: 600;
    color: rgba(0, 0, 0, 0.9);
    -webkit-background-clip: text;
  }

  .btnClass {
    width: 100%;
    display: flex;
    height: 50px;
    align-items: center;
    justify-content: end;
  }
}

// tab弹窗样式改造
::v-deep.addpageContainer {
  .mine_el_tab {
    width: 100%;
    flex: 1;
    overflow: hidden;

    .el-tabs {
      display: flex;
      flex-direction: column;
      width: 100%;
      height: 100%;
      overflow: hidden;

      .el-tabs__header {
        .el-tabs__nav-wrap {
          .el-tabs__nav-scroll {
            display: flex;
            justify-content: center;
            align-items: center;

            .el-tabs__item {
              height: 50px;
              line-height: 50px;
            }
          }
        }
      }

      .el-tabs__content {
        flex: 1;
        overflow-y: auto;
        overflow-x: hidden;

        .el-tab-pane {
          width: 100%;
          height: 100%;

          .step1 {
            width: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;

            .ivu-form {
              width: 520px;
              height: 514px;
            }
          }

          .step4 {
            width: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            //.pageLastStep{
            //  width: 520px;
            //  height: 514px;
            //}
          }
        }
      }
    }
  }
}

.searchBoxHbw {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #e8eaec;
  padding: 14px 16px;
  line-height: 1;
  height: 43px;
  margin-top: 16px;
  flex: 1 auto;
  overflow: hidden;

  button {
    width: 61px;
    border: none;
    cursor: pointer;
    border-bottom: 1px solid #e8eaec;
    padding: 15px 17px;
    line-height: 1;
    height: 42px;
    margin-top: 16px;
    flex: 1 auto;
  }

  span {
    width: 60px;
    border: none;
    cursor: pointer;
    border-bottom: 1px solid #e8eaec;
    padding: 14px 16px;
    line-height: 1;
    height: 42px;
    margin-top: 16px;
    flex: 1 auto;
  }

  div {
    width: 60px;
    border: none;
    cursor: pointer;
    border-bottom: 1px solid #e8eaec;
    padding: 14px 16px;
    line-height: 1;
    height: 42px;
    margin-top: 16px;
    flex: 1 auto;
  }

  p {
    width: 60px;
    border: none;
    cursor: pointer;
    border-bottom: 1px solid #e8eaec;
    padding: 14px 16px;
    line-height: 1;
    height: 42px;
    margin-top: 16px;
    flex: 1 auto;
  }

  a {
    width: 60px;
    border: none;
    cursor: pointer;
    border-bottom: 1px solid #e8eaec;
    padding: 14px 16px;
    line-height: 1;
    height: 42px;
    margin-top: 16px;
    flex: 1 auto;
  }

  ul {
    width: 60px;
    border: none;
    cursor: pointer;
    border-bottom: 1px solid #e8eaec;
    padding: 14px 16px;
    line-height: 1;
    height: 42px;
    margin-top: 16px;
    flex: 1 auto;
  }

  li {
    width: 60px;
    border: none;
    cursor: pointer;
    border-bottom: 1px solid #e8eaec;
    padding: 14px 16px;
    line-height: 1;
    height: 42px;
    margin-top: 16px;
    flex: 1 auto;
  }

  th {
    width: 60px;
    border: none;
    cursor: pointer;
    border-bottom: 1px solid #e8eaec;
    padding: 14px 16px;
    line-height: 1;
    height: 42px;
    margin-top: 16px;
    flex: 1 auto;
  }

  td {
    width: 60px;
    border: none;
    cursor: pointer;
    border-bottom: 1px solid #e8eaec;
    padding: 14px 16px;
    line-height: 1;
    height: 42px;
    margin-top: 16px;
    flex: 1 auto;
  }

  tr {
    width: 60px;
    border: none;
    cursor: pointer;
    border-bottom: 1px solid #e8eaec;
    padding: 14px 16px;
    line-height: 1;
    height: 42px;
    margin-top: 16px;
    flex: 1 auto;
  }
}
</style>
