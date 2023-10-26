<template>
  <Modal v-model="flowStepsVisiable" :width="1568" :fullscreen="isFullScreen" @on-cancel="close">
  <p slot="header">
    <Row :gutter="40">
      <Col span="20">
      <!-- <span v-html="modalTitle" class="modalHeaderClass"></span> -->
      <span>新建流程</span>
      </Col>
      <Col span="4" style="text-align: right">
      <a class="dark-a" href="javascript:void(0)" @click="screenClick">
        <Icon custom="iconfont  icon-FullScreen" v-if="!isFullScreen" :size="20" />
        <Icon custom="iconfont  icon-ExitFullScreen" v-if="isFullScreen" :size="20" />
      </a>
      <a class="dark-a" href="javascript:void(0)">
        <Icon style="margin:0 0 0 26px" :size="20" />
      </a>
      </Col>
    </Row>
  </p>
  <br>
<!--步骤-->
<el-steps :active="step" align-center >
  <el-step title="新建流程"></el-step>
  <el-step title="模型设计"></el-step>
  <!-- <el-step title="表单设计"></el-step> -->
  <el-step title="表单设计"></el-step>
  <el-step title="流程设计"></el-step>
</el-steps>
<br>
  <!--中间内容-->
  <div>
    <!-- step1 新建流程      -->
    <div class="step0" v-if="step == 0" style="display: flex;padding-top: 100px;justify-content: center;align-items: center;">
      <img src="./images/Team work-rafiki (3) 1.svg" alt="">
      <createdModel ref="createdModelRef" @stepParams="stepParams" style="width: 40%"></createdModel>
    </div>
    <!-- step1 模型设计      -->
    <div class="step0" v-if="step == 1" style="display: flex;padding-top: 100px;justify-content: center;align-items: center;">
      <modelDesign ref="modelDesignRef" @tableNameParams="tableNameParams" :formIdParams="formIdParams" style="width: 80%"></modelDesign>
    </div>
    <!-- step3 表单设计      -->
    <div class="step2" v-if="step == 2 && !formDesignTab" style="height: 700px;overflow-y: auto;">
      <ListConfig ref='listConfigRef' :pageIdListConfig="pageIdListConfig"/>
    </div>
    <!-- step1 流程设计      -->
    <div class="step2" v-if="step == 2 && formDesignTab" style="height: 700px;overflow-y: auto;">
      <v-form-designer ref="vfDesigner" :designer-config="designerConfig" :field-list-api="fieldListApi">
        <!-- 自定义按钮插槽演示 -->
        <template #customToolButtons>
          <el-button type="text"><i class="el-icon-finished"/>保存</el-button>
        </template>
      </v-form-designer>
    </div>
    <!-- step1 流程设计      -->
    <div class="step2" v-if="step == 3" style="height: 700px;overflow-y: auto;">
      <ModelEditor ref='ModelEditorRef' :modelEditorData="modelEditorData" />
    </div>
  </div>
  <div slot="footer">
    <Button type="primary" v-if="step == 0" @click="next"><span>保存</span></Button>
    <Button type="primary" v-if="step == 1 && !createdModelSave" @click="next"><span>保存</span></Button>
    <Button type="primary" v-if="step == 2 && formDesignSave" @click="next"><span>保存</span></Button>
    <Button type="primary" v-if="step < 3 && step > 0 && !formDesignSave && !modelDesignSave" @click="next"><span>下一步</span></Button>
    <Button type="primary" v-if="step == 3"  @click="next"><span>完成</span></Button>
  </div>
</Modal>
</template>
<script>
import ListConfig from './flowcomponentbj/listConfig.vue'
import createdModel from './flowcomponentbj/createdModel.vue'
import modelDesign from './flowcomponentbj/modelDesign.vue'
import request from '@/api/workflow/request'
import { getDefaultFormConfig } from '@/views/lowcode/utils/util.js'
import { componentTypes } from '@/views/lowcode/customize/config/types.js'
import {
  basicFields,
  advancedFields,
  customFields
} from '@/views/lowcode/vformPro/components/form-designer/widget-panel/widgetsConfig.js'
export default {
  components: {
    ListConfig,
    createdModel,
    modelDesign
  },
  props: {
    formChooseData: Object
  },
  data () {
    return {
      createdModelSave: false,
      modelDesignSave: false,
      formDesignSave: false,
      formDesignTab: false,
      flowStepsVisiable: false,
      isFullScreen: true,
      step: 0,
      page: 1,
      size: 10,
      NextStep: true,
      modelEditorData: {
        // modelId:'',
        // processId: '',
        // processName:''
      },
      // 表单设计
      currentStep: 0,
      tableName: '',
      pageId: '',
      detail: {},
      detailObj: {},
      // 展示表单设计器的宽度
      designerConfig: {
        resetFormJson: false,
        toolbarMaxWidth: 490
      }
      // fieldListApi: {
      //   URL: this.$util.baseUrl + '/api/cscpHxFormColums?tableName=' + localStorage.getItem('workFlowFormTable'),
      //   labelKey: 'columnComment',
      //   nameKey: 'columnName',
      //   headers: { Authorization: localStorage.token }
      // }
    }
  },
  // watch: {
  //   tableNameParams (newValue, oldValue) {
  //     console.log(newValue, oldValue,'ff');
  //     // 每当inputValue的值改变则更新 update:inputValue, 并且把值传过去
  //     this.fieldListApi.URL = this.$util.baseUrl + '/api/cscpHxFormColums?tableName=' + newValue
  //   }
  // },
  methods: {
    // 25
    createdModelSaveFn () {
      this.createdModelSave = true
    },
    // 已完成流程模型修改
    modelDesignFn (flag) {
      this.modelDesignSave = true
    },
    // 75 传行数据
    modelRowFn (row) {
      console.log(row, 'wwwweee')
      this.modelEditorData = row
    },
    // 25传行数据
    pageIdSon (row) {
      console.log(row, 'www')
      this.pageIdListConfig = row.formId
      this.pageId = row.formId
      this.formName = row.formName
      this.formIdParams = row.formId
    },
    // 50%数据
    formDataFn (row) {
      console.log(row, '50')
      this.pageId = row.formId
      this.formName = row.formName
      this.formIdParams = row.formId
      this.tableNameParams = row.formTable
    },
    close () {
      // window.location.reload()
      this.flowStepsVisiable = false
      this.$parent.queryList()
      this.step = 0
    },
    stepParams (num) {
      this.numStep = num
    },
    // 流程id
    flowIdFn (row) {
      this.rowObj = row
    },
    // 模型名称
    tableNameParams (num) {
      console.log(num, '111')
      this.tableNameParams = num
    },
    // 表单formId
    formIdParams (row) {
      console.log(row, '222')
      this.formIdParams = row.formId
      this.formName = row.formName
    },

    async next () {
      if (this.step == 0) {
        await this.$refs.createdModelRef.modelSave()
      } else if (this.step == 1) {
        if (this.modelDesignSave) {
          this.$refs.modelDesignRef.createdModelFn()
        } else {
          await this.$refs.modelDesignRef.createdModelFn()
          this.step += 1
        }
      } else if (this.step == 2) {
        if (!this.formDesignTab) {
          this.saveFields()
        } else {
          this.saveFormJson()
          // this.flowFormBinding()
        }
      } else if (this.step == 3) {
        this.$refs.ModelEditorRef.$refs.processDesigner.processSave()
      }
    },
    fieldListApiFn () {
      console.log(1, '11')
      console.log(this.tableNameParams, 'this.tableNameParams')
      this.fieldListApi = {
        URL: this.$util.baseUrl + '/api/cscpHxFormColums?tableName=' + this.tableNameParams,
        labelKey: 'columnComment',
        nameKey: 'columnName',
        headers: { Authorization: localStorage.token }
      }
    },
    // 流程列表
    queryList (flag) {
      return new Promise((resolve) => {
        let params = {
          page: 1,
          size: 5,
          category: 'lowCode'
        }
        // this.$http
        //   .get('/api/flow/bpmn/page', {
        //     params: params
        //   })
        request.getCscpFlowBpmnPageList({ params })
          .then((res) => {
            this.modelEditorData = res.data.result.list.filter((item) => item.formId == this.pageId)[0]
            resolve(1)
            if (!flag) {
              this.step += 1
            }
          })
          .catch(() => {
            this.$Message.error('列表查询失败')
          })
      })
    },
    screenClick () {
      if (this.isFullScreen) {
        this.isFullScreen = false
      } else {
        this.isFullScreen = true
      }
    },
    // 表单设计
    // 3.保存字段信息
    saveFields () {
      let dataArray = this.$refs['listConfigRef'].dataArr
      for (let i = 0; i < dataArray.length; i++) {
        if (dataArray[i].isForm == 1 && dataArray[i].componentType == null) {
          this.$message.warning('表单类型不能为空')
          return
        }
      }

      // const flag = this.$refs['listConfigRef'].addOrEditFlag
      let url = '/api/lowcode/customize/cscpCustomizeVfieldPages/save'
      // if (flag === 'edit') {
      //   url = '/api/lowcode/customize/cscpCustomizeVfieldPages/update'
      // }
      let fields = this.$refs['listConfigRef'].dataArr.map(item => {
        return JSON.parse(JSON.stringify(item))
      })
      for (let i = 0; i < fields.length; i++) {
        fields[i].fieldQuery = fields[i].fieldQuery ? fields[i].fieldQuery === '0' ? '0' : '1' : '0'
        fields[i].fieldList = fields[i].fieldList ? fields[i].fieldList === '0' ? '0' : '1' : '0'
        fields[i].isForm = fields[i].isForm ? fields[i].isForm === '0' ? '0' : '1' : '0'
        fields[i].fieldOrder = i + 1
      }
      // fields.map(item => (item.fieldName = item.tableName + '.' + item.fieldName))
      this.nextLoading = true
      this.pageId = fields[0].pageId
      this.$util.http.post(url, fields).then(res => {
        this.$Message.success(`操作成功`)
        this.buildDefaultPage()
        this.nextLoading = false
      }).catch(() => {
        this.$Message.error(`操作失败`)
        this.nextLoading = false
      })
    },
    buildDefaultPage () {
      // 初始化
      window.localStorage.setItem('form__config__backup', '')
      window.localStorage.setItem('widget__list__backup', '')
      this.buildDefaultFormJson()
    },
    buildDefaultFormJson () {
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
                  if (advancedField.type === componentType.type) {
                    let varField = JSON.parse(JSON.stringify(advancedField))
                    varField.options.label = field.fieldComment ? field.fieldComment : field.fieldName
                    varField.options.name = field.fieldName
                    varField.id = field.fieldId
                    widgetJson.push(varField)
                  }
                })
                customFields.forEach(customField => {
                  if (customField.type === componentType.type) {
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
        // this.step += 1
        this.formDesignTab = true
        this.fieldListApiFn()
      })
    },
    // 4.保存表单json数据
    saveFormJson () {
      let formJson = this.$refs.vfDesigner.getFormJson()
      this.detailObj.formJson = JSON.stringify(formJson)
      this.detailObj.formName = this.formName
      this.detailObj.formId = this.formIdParams
      this.detailObj.formTable = this.tableNameParams
      this.detailObj.formOption = JSON.stringify({ 'pk': 'id' })
      this.formJson = JSON.stringify(formJson)
      this.$http.put('/api/lowcode/customize/cscpCustomizeVforms', this.detailObj).then(response => {
        this.$message.success('表单已保存！')
        window.localStorage.setItem('widget__list__backup', '')
        window.localStorage.setItem('form__config__backup', '')
        // this.formId = null
        // this.queryList()
        if (this.formDesignSave) {
          this.flowStepsVisiable = false
          this.step = 0
          // window.location.reload()
          this.queryList(this.formDesignSave)
        } else {
          this.queryList()
        }
      }).catch(err => {
        this.$Message.error(`修改失败：${err.response.data.msg}`)
      })
    },
    // 表单绑定流程
    flowFormBinding () {
      let copyDetail = { ...this.rowObj, formId: this.formIdParams }
      this.$http
        .post('/api/flow/bpmn/updateNew', copyDetail)
        .then((res) => {
          this.$Message.success('新增成功')
          this.queryList()
        })
        .catch(() => {
          this.$Message.error('新增失败')
        })
    },
    checkForm () {
      let flag = true
      this.$refs['listConfigRef'].dataArr.forEach(item => {
        if ((item.fieldQuery === '1' || item.fieldQuery === true) && (!item.queryType || !item.controlType)) {
          flag = false
        }
      })
      return flag
    },
    initSecondStep (row) {
      this.tableNameParams = row.formTable
      console.log(this.tableNameParams, 'this.tableNameParams')
      this.formName = row.formName
      this.formIdParams = row.formId
      this.fieldListApiFn()
      this.formDesignSave = true
      this.formDesignTab = true
      this.detail = row
      this.currentStep = 1
      this.tableName = this.detail.formTable
      window.localStorage.setItem('widget__list__backup', '')
      window.localStorage.setItem('form__config__backup', '')
      let pageJson = JSON.parse(this.detail.formJson ? this.detail.formJson : null)
      if (pageJson) {
        window.localStorage.setItem('widget__list__backup', JSON.stringify(pageJson.widgetList))
        window.localStorage.setItem('form__config__backup', JSON.stringify(pageJson.formConfig))
      }
    }
  },
  created () {

  }

}
</script>
<style lang="less" scoped>
::v-deep .el-step__head{
  // display: none !important;
  color: grey !important;
  // border-color: black !important;
}
::v-deep .el-step__icon{
  color: grey !important;
  // border-color: black !important;
}
// ::v-deep .el-steps{
//   justify-content: center;
// }
// ::v-deep .el-step__description.is-process{
//   display: none;
// }
// ::v-deep .el-step__main{
//   display: flex;
//   justify-content: center;
//   width: 80px;
//   // margin: 0px 5px;
// }
::v-deep .is-process{
  color: #457ce0;
  border-color: #457ce0 !important;
  font-weight: normal !important;
  .is-text{
    color: #457ce0 !important;
  }
}
::v-deep .is-finish{
  color: grey;
  border-color: grey !important;
}
::v-deep .is-wait{
  color: grey;
  border-color: grey !important;
}
</style>
