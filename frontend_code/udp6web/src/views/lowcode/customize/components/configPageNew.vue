<template>
  <Modal v-model="configModal" :title="title" :closable="false" ref="modal" @on-cancel="close" :width="1200"
    :fullscreen="isFullScreen">
    <Steps :current="step" v-show="false">
      <Step title="配置数据表" content="绑定数据库已有业务表" />
      <Step v-if="formType == '2'" title="关联配置" content="子表关联主表的外键" />
      <Step v-if="formType != '3'" title="列表配置" content="业务表列表设计" />
      <Step title="表单设计" content="业务表表单设计" />
      <Step v-if="formType != '2' && formType != '3'" title="菜单配置" content="菜单配置" />
    </Steps>
    <div>
      <div class="step1" v-if="currentStep == 0">
        <!--  step1 配置数据表      -->
        <FormDB ref='formDbRef' :isDisable="isDisable" :tableName.sync="tableName" :tableDesc.sync="tableDesc" />
      </div>
      <div class="step2" v-if="currentStep == 1&&formType == '2'">
        <!--  step2 关联配置     -->
        <sub-form-config ref='subFormConfig' :tableName="tableName" :formId="formId" />
      </div>
      <div class="step3" v-if="currentStep == 2">
        <!--  step3 列表配置      -->
        <ListConfig ref='listConfig' :formDetail="detail" />
      </div>
      <div class="step4" v-if="currentStep ==3">
        <!--  step4 表单设计      -->
        <div v-show="false">
          <FormDB ref='formDbRef' :isDisable="isDisable" :tableName.sync="tableName" :tableDesc.sync="tableDesc" />
        </div>
        <v-form-designer ref="vfDesigner" :designer-config="designerConfig" :field-list-api="fieldListApi" v-if="isClose">
          <!-- 自定义按钮插槽演示 -->
          <template #customToolButtons>
            <el-button type="text" @click="saveFormJson(false)"><i class="el-icon-finished" />保存</el-button>
          </template>
        </v-form-designer>
      </div>
      <div class="step5" v-if="currentStep == 4&&formType != '2'">
        <!--  step5 菜单配置      -->
        <menu-setting ref='menuSetting' :formDetail="detail"></menu-setting>
      </div>
    </div>
    <div slot="footer">
      <Button type="primary" @click="next" style="width: 100px"
        :loading="nextLoading">
        <span v-if="!nextLoading">确定</span>
        <span v-else>等待...</span>
      </Button>
      <Button type="default" @click="close" style="width: 80px">取消</Button>
    </div>
  </Modal>
</template>

<script>
import FormDB from './steps/formdb'
import ListConfig from './steps/listConfig'
import menuSetting from './steps/menuSetting'
import SubFormConfig from './steps/subFormConfig'
import { componentTypes } from '../config/types.js'
import { getDefaultFormConfig } from '@/views/lowcode/utils/util.js'
import { basicFields, advancedFields, customFields } from '@/views/lowcode/vformPro/components/form-designer/widget-panel/widgetsConfig.js'
export default {
  name: 'configPage',
  components: { FormDB, ListConfig, menuSetting, SubFormConfig },
  data() {
    return {
      isClose: true,
      fieldListApi: {
        URL: this.$util.baseUrl + '/api/cscpHxFormColums?tableName=' + '',
        labelKey: 'columnComment',
        nameKey: 'columnName',
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
      formId: null,
      isFullScreen: '',
      currentStep: -1,
      step: 0,
      configModal: false,
      isDisable: false,
      detail: {},
      formJson: null,
      nextLoading: false,
      title: ''
    }
  },
  props: {
    formType: String
  },
  mounted() {

  },
  watch: {
    tableName(newValue, oldValue) {
      // 每当inputValue的值改变则更新 update:inputValue, 并且把值传过去
      this.fieldListApi.URL = this.$util.baseUrl + '/api/cscpHxFormColums?tableName=' + newValue
    },
    step(newV, oldV) {
      if (newV == 0) {
        this.$nextTick(() => {
          this.$refs.formDbRef.tableName = this.tableName
          this.$refs.formDbRef.getColumns(this.tableName)
        })
      }
    }
  },
  methods: {
    widgetInit() {
      if (this.formId && !(this.formJson && (this.formJson ? JSON.parse(this.formJson ? this.formJson : null).widgetList.length > 0 : null))) {
        window.localStorage.setItem('form__config__backup', '')
        window.localStorage.setItem('widget__list__backup', '')
        const url = `/api/lowcode/customize/cscpCustomizeVfields/getListByFormId/${this.formId}`
        this.nextLoading = true
        this.$http.get(url).then(res => {
          const fields = res.data.data
          if (fields.length > 0) {
            let widgetJson = []
            fields.forEach((field) => {
              if (field.isForm == 1) {
                componentTypes.forEach(componentType => {
                  if (field.componentType== componentType.value) {
                    basicFields.forEach(basicField => {
                      if (basicField.type == componentType.type) {
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
            this.currentStep++
            this.step++
            this.nextLoading = false
          }
        }).catch(() => {
          this.nextLoading = false
          this.$Notice.error({
            title: '操作失败！'
          })
        })
      } else {
        this.currentStep++
        this.step++
      }
    },
    // 1.保存表单
    saveFormInfo() {
      // 保存数据表信息
      if (this.tableName) {
        this.detail = {}
        this.detail['formTable'] = this.tableName
        this.detail['formId'] = this.formId
        this.detail['tableDesc'] = this.tableDesc
        this.detail.formOption = JSON.stringify({ 'pk': window.localStorage.getItem('keyName') })
        let copyDetail = JSON.parse(JSON.stringify(this.detail))
        this.fieldListApi.URL = this.$util.baseUrl + '/api/cscpHxFormColums?tableName=' + this.tableName
        this.nextLoading = true
        this.$http
          .put('/api/lowcode/customize/cscpCustomizeVforms', copyDetail)
          .then((res) => {
            if (this.formType == '2') {
              this.currentStep++
            } else if (this.formType == '1') {
              this.currentStep += 3
              this.$parent.queryList()
            } else {
              this.currentStep += 2
            }
            this.step++
            this.nextLoading = false
            this.$Message.success('更新成功')
          })
          .catch(() => {
            this.$Message.error('更新失败')
            this.nextLoading = false
          })
      }
    },
    // 2.关联配置
    saveSubConfig() {
      // 保存数据表信息
      if (this.tableName) {
        this.detail['subFormFk'] = this.$refs['subFormConfig'].subFormDetail.subFormFk
        this.detail['sortNum'] = this.$refs['subFormConfig'].subFormDetail.sortNum
        let copyDetail = JSON.parse(JSON.stringify(this.detail))
        this.nextLoading = true
        this.$http
          .put('/api/lowcode/customize/cscpCustomizeVforms', copyDetail)
          .then((res) => {
            this.$Message.success('更新成功')
            this.currentStep++
            this.step++
            this.nextLoading = false
          })
          .catch(() => {
            this.$Message.error('更新失败')
            this.nextLoading = false
          })
      }
    },
    // 3.保存字段信息
    saveFields() {
      if (!this.checkForm()) {
        return false
      }
      const flag = this.$refs['listConfig'].addOrEditFlag
      let url = '/api/lowcode/customize/cscpCustomizeVfields/save'
      if (flag === 'edit') {
        url = '/api/lowcode/customize/cscpCustomizeVfields/update'
      }
      let fields = this.$refs['listConfig'].dataArr.map(item => {
        return JSON.parse(JSON.stringify(item))
      })
      for (let i = 0; i < fields.length; i++) {
        fields[i].fieldQuery = fields[i].fieldQuery ? fields[i].fieldQuery === '0' ? '0' : '1' : '0'
        fields[i].fieldList = fields[i].fieldList ? fields[i].fieldList === '0' ? '0' : '1' : '0'
        fields[i].isForm = fields[i].isForm ? fields[i].isForm === '0' ? '0' : '1' : '0'
        fields[i].fieldOrder = i+1;
      }
      this.nextLoading = true
      this.$util.http.post(url, fields).then(res => {
        this.widgetInit()
        this.nextLoading = false
      }).catch(() => {
        this.$Message.error(`操作失败`)
        this.nextLoading = false
      })
    },
    // 4.保存表单json数据
    saveFormJson(nextStep) {
      if (this.tableName) {
        this.detail = {}
        this.detail['formTable'] = this.tableName
        this.detail['formId'] = this.formId
        this.detail['tableDesc'] = this.tableDesc
        this.detail.formOption = JSON.stringify({ 'pk': 'id'})
        console.log('this.detail', this.detail)
        let copyDetail = JSON.parse(JSON.stringify(this.detail))
        this.fieldListApi.URL = this.$util.baseUrl + '/api/cscpHxFormColums?tableName=' + this.tableName
      }
      let formJson = this.$refs.vfDesigner.getFormJson()
      this.detail.formJson = JSON.stringify(formJson)
      this.formJson = JSON.stringify(formJson)
      this.nextLoading = true
      this.$http.put('/api/lowcode/customize/cscpCustomizeVforms', this.detail).then(response => {
        this.$message.success('表单已保存！')
        this.close()
        this.nextLoading = false
      }).catch(err => {
        this.$Message.error(`修改失败：${err.response.data.msg}`)
        this.nextLoading = false
      })
    },
    checkForm() {
      let flag = true
      this.$refs['listConfig'].dataArr.forEach(item => {
        console.log(item.fieldQuery, item.queryType, item.controlType)
        if ((item.fieldQuery === '1' || item.fieldQuery === true) && (!item.queryType || !item.controlType)) {
          flag = false
        }
      })
      return flag
    },
    // 5.保存菜单
    saveMenu() {
      this.nextLoading = true
      this.$refs['menuSetting'].$refs['menuFormRef'].validate(valid => {
        if (valid) {
          this.$refs.menuSetting.handleSubmit()
          this.close()
        }
        this.nextLoading = false
      })
    },
    back() {
      if (this.currentStep <= 0 || this.formType == '3') {
        this.step = 0
        this.currentStep = 0
      } else {
        if (this.formType != '2' && this.currentStep == 2) {
          this.currentStep -= 2
        } else {
          this.currentStep--
        }
        this.step--
      }
    },
    next() {
      console.log('this.currentStep', this.currentStep)
      if (this.currentStep == 0) {
        // 保存数据表信息
        if (!this.tableName) {
          this.$message.error('请先勾选数据表！')
          return
        }
        this.saveFormInfo()
        // 关闭对话框
        this.close()
      } else if (this.currentStep == 1 && this.formType == '2') {
        this.saveSubConfig()
      } else if (this.currentStep == 2) {
        // 展示列表
        this.saveFields()
      } else if (this.currentStep == 3) {
        // 保存表单json数据
        // 由于这里的数据依赖于第一步的数据,所以要移植第一步的操作

        this.saveFormJson(true)
        this.close()
      } else if (this.currentStep == 4 && this.formType != '2') {
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
      this.configModal = false
      this.isClose = false
      this.$parent.queryList()
      this.formId = null
      this.$refs.formDbRef.tableName = ''
    }
  }
}
</script>

<style scoped>

</style>
