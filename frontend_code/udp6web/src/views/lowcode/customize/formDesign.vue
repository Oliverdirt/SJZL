<template>
  <div>
    <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
      <div style="display: flex;justify-content:space-between">
        <h3>表单设计</h3>
        <div class="closeBtn">
          <Icon type="ios-close" @click="close" size="36"/>
        </div>
      </div>
      <Steps :current="currentStep" style="margin-bottom:20px">
        <Step title="页面配置" content="配置页面基本信息"/>
        <Step title="页面设计" content="页面设计"/>
      </Steps>
      <div>
        <!--  新增step1 配置页面基本信息      -->
        <div class="step4" v-if="currentStep === 0">
          <ListConfig ref='listConfig' :formDetail="detail"/>
        </div>
        <!--  step2 表单设计      -->
        <div class="step4" v-if="currentStep === 1">
          <v-form-designer ref="vfDesigner" :designer-config="designerConfig" :field-list-api="fieldListApi">
            <!-- 自定义按钮插槽演示 -->
            <template #customToolButtons>
              <el-button type="text" @click="saveFormJson(false)"><i class="el-icon-finished"/>保存</el-button>
            </template>
          </v-form-designer>
        </div>
      </div>
      <div style="height:56px;"></div>
      <div class="btnClass">
        <Button type="primary" v-show="currentStep > 0 && this.$route.query.routeFlag == 0" @click="back" style="width: 80px;margin-right:12px">上一步</Button>
        <Button type="primary" v-show="currentStep === 0" @click="next" style="width: 80px;margin-right:12px">下一步
        </Button>
        <Button type="primary" v-show="currentStep === 1" @click="next" style="width: 80px;margin-right:12px">完成
        </Button>
        <Button type="default" @click="close" style="width: 80px">取消</Button>
      </div>
    </Card>
  </div>
</template>
<script>
import ListConfig from '@/views/lowcode/customize/components/steps/listConfigTestAdd'
import { getDefaultFormConfig } from '@/views/lowcode/utils/util.js'
import { componentTypes } from '@/views/lowcode/customize/config/types.js'
import {
  basicFields,
  advancedFields,
  customFields
} from '@/views/lowcode/vformPro/components/form-designer/widget-panel/widgetsConfig.js'

export default {
  components: { ListConfig },
  data () {
    return {
      currentStep: 0,
      tableName: '',
      pageId: '',
      detail: {},
      // 展示表单设计器的宽度
      designerConfig: {
        resetFormJson: false,
        toolbarMaxWidth: 490
      },
      fieldListApi: {
        URL: this.$util.baseUrl + '/api/cscpHxFormColums?tableName=' + localStorage.getItem('workFlowFormTable'),
        labelKey: 'columnComment',
        nameKey: 'columnName',
        headers: { Authorization: localStorage.token }
      }
    }
  },
  watch: {
    tableName (newValue, oldValue) {
      // 每当inputValue的值改变则更新 update:inputValue, 并且把值传过去
      this.fieldListApi.URL = this.$util.baseUrl + '/api/cscpHxFormColums?tableName=' + newValue
    }
  },
  methods: {
    next () {
      if (this.currentStep === 0) {
        this.saveFields()
      } else if (this.currentStep === 1) {
        // 保存表单json数据
        this.saveFormJson(true)
      }
    },
    back () {
      if (this.currentStep === 1) {
        this.$Modal.confirm({
          title: '提示',
          content: '<p>请先保存当前操作,否则返回上一步将取消当前的操作!</p>',
          onOk: () => {
            this.currentStep--
          },
          onCancel: () => {
          }
        })
      } else {
        this.currentStep--
      }
    },
    // 3.保存字段信息
    saveFields () {
      let dataArray = this.$refs['listConfig'].dataArr
      for(let i = 0; i < dataArray.length; i++){
        if(dataArray[i].isForm == 1 && dataArray[i].componentType == null){
          this.$message.warning('表单类型不能为空')
          return
        }
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
        this.step++
        this.currentStep++
      })
    },
    // 4.保存表单json数据
    saveFormJson (nextStep) {
      let formJson = this.$refs.vfDesigner.getFormJson()
      this.detail.formJson = JSON.stringify(formJson)
      this.formJson = JSON.stringify(formJson)
      this.$http.put('/api/lowcode/customize/cscpCustomizeVforms', this.detail).then(response => {
        this.$message.success('表单已保存！')
        if (nextStep) {
          this.currentStep++
          this.step++
        }
        window.localStorage.setItem('widget__list__backup', '')
        window.localStorage.setItem('form__config__backup', '')
        this.formId = null
        this.$router.push('/lowcode-programming')
      }).catch(err => {
        this.$Message.error(`修改失败：${err.response.data.msg}`)
        this.nextLoading = false
      })
    },
    checkForm () {
      let flag = true
      this.$refs['listConfig'].dataArr.forEach(item => {
        if ((item.fieldQuery === '1' || item.fieldQuery === true) && (!item.queryType || !item.controlType)) {
          flag = false
        }
      })
      return flag
    },
    close () {
      this.$router.push('/lowcode-programming')
    },
    initSecondStep () {
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
    this.detail = this.$route.query.formRow
    this.$route.query.routeFlag ? this.initSecondStep() : ''
  }
}
</script>

<style scoped>
.btnClass {
  display: flex;
  justify-content: right;
  position: fixed;
  right: 24px;
  bottom: 24px
}

.closeBtn {
  cursor: pointer;
}

.formCard {
  height: 100%
}
</style>
