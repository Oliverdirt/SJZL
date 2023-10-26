<template>

    <div class="ds-list">
      <!-- <template v-if="!!formConfig.dataSources && (formConfig.dataSources.length > 0)">
        <el-descriptions v-for="(ds, dsIdx) in formConfig.dataSources" :key="dsIdx"
                         :column="1" size="small" border>
          <template #title>
            <span :title="ds.description">{{ds.uniqueName}}</span>
          </template>
          <template #extra>
            <el-button type="primary" icon="el-icon-edit" plain circle
                       size="mini" @click="editDataSource(dsIdx)"></el-button>
            <el-button type="danger" icon="el-icon-delete" plian circle
                       size="mini" @click="deleteDataSource(dsIdx)"></el-button>
          </template>
          <el-descriptions-item>
            <template slot="label">
              <i :title="ds.requestURL" class="el-icon-s-platform"></i>
            </template>
            {{ds.requestURL}}
          </el-descriptions-item>
        </el-descriptions>
      </template>
      <template v-else>
        <el-empty :description="i18nt('designer.setting.noDataSource')"></el-empty>
      </template> -->
  
      <!-- <div class="ds-button-wrapper">
        <el-button-group>
          <el-button type="primary" icon="el-icon-plus" plain @click="addDataSource">
            {{i18nt('designer.setting.addDataSource')}}</el-button>
          <el-button icon="el-icon-bottom-left" plain :title="i18nt('designer.setting.importDataSource')"
                     @click="importDataSource"></el-button>
          <el-button icon="el-icon-top-right" plain :title="i18nt('designer.setting.exportDataSource')"
                     @click="exportDataSource"></el-button>
        </el-button-group>
      </div> -->
  
      <el-drawer direction="rtl" :modal="true" :size="820" :show-close="false"
                 :visible.sync="showDataSourceDialogFlag" v-if="showDataSourceDialogFlag"
                 :destroy-on-close="true" :append-to-body="true" :wrapperClosable="false" :close-on-press-escape="false"
                 custom-class="ds-setting-drawer header-small-mb">
        <template #title>
          <div>API管理设置</div>
          <div style="float: right">
            <el-button type="primary" plain @click="testDataSource" :disabled="openModalType=='check'">
              测试API</el-button>
            <el-button type="primary" @click="saveDataSource" :disabled="openModalType=='check'">
              保存API</el-button>
            <el-button @click="handleClose">
              {{i18nt('designer.hint.cancel')}}</el-button>
          </div>
        </template>
        <template #default>
          <el-form :model="dsModel" :rules="formRules" ref="dsForm"
                   label-width="160px" label-position="left" class="ds-form">
            <el-form-item :label="i18nt('designer.setting.dsUniqueName')" prop="uniqueName">
              <el-input v-model="dsModel.uniqueName" :disabled="openModalType=='check'"></el-input>
            </el-form-item>
            <el-row style="width: 100%">
              <el-col :span="24">
                <el-form-item :label="i18nt('designer.setting.dsRequestURL')" prop="requestURL">
                  <el-input v-model="dsModel.requestURL" :disabled="openModalType=='check'"></el-input>
                </el-form-item>
              </el-col>
              <!-- <el-col :span="4">
                <el-form-item label-width="0" prop="requestURLType" class="no-left-margin">
                  <el-select v-model="dsModel.requestURLType" :placeholder="i18nt('designer.setting.dsRequestURLType')">
                    <el-option :label="i18nt('designer.setting.dsURLStringType')" value="String"></el-option>
                    <el-option :label="i18nt('designer.setting.dsURLVariableType')" value="Variable"></el-option>
                  </el-select>
                </el-form-item>
              </el-col> -->
            </el-row>
            <el-form-item :label="i18nt('designer.setting.dsDescription')" prop="description">
              <el-input type="textarea" v-model="dsModel.description" :rows="2" :disabled="openModalType=='check'"></el-input>
            </el-form-item>
            <el-form-item label="操作类型" prop="operationType">
                <el-select v-model="dsModel.operationType" placeholder="请选择操作类型" :disabled="openModalType=='check'">
                      <el-option label="新增" :value="0"></el-option>
                      <el-option label="删除" :value="1"></el-option>
                      <el-option label="编辑" :value="2"></el-option>
                      <el-option label="查找" :value="3"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item :label="i18nt('designer.setting.dsRequestMethod')" prop="requestMethod">
              <el-radio-group v-model="dsModel.requestMethod" :disabled="openModalType=='check'">
                <el-radio-button :label="0">get</el-radio-button>
                <el-radio-button :label="1">post</el-radio-button>
                <el-radio-button :label="2">put</el-radio-button>
                <el-radio-button :label="3">delete</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="i18nt('designer.setting.dsRequestHeaders')">
              <el-row v-for="(rh, hIdx) in dsModel.headers" :key="hIdx" class="rh-row" :gutter="8">
                <el-col :span="7">
                  <el-form-item :prop="'headers.' + hIdx + '.key'" :rules="nameRules" label-width="0">
                    <el-input v-model="rh.key" :placeholder="i18nt('designer.setting.dsRequestNameInputPlaceholder')" :disabled="openModalType=='check'"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="5">
                  <el-form-item :prop="'headers.' + hIdx + '.valueType'" label-width="0">
                    <el-select v-model="rh.valueType" :placeholder="i18nt('designer.setting.dsRequestTypeInputPlaceholder')" :disabled="openModalType=='check'">
                      <el-option :label="i18nt('designer.setting.dsRequestValueStringType')" :value="0"></el-option>
                      <el-option :label="i18nt('designer.setting.dsRequestValueNumberType')" :value="1"></el-option>
                      <el-option :label="i18nt('designer.setting.dsRequestValueBooleanType')" :value="2"></el-option>
                      <el-option :label="i18nt('designer.setting.dsRequestValueVariableType')" :value="3"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item :prop="'headers.' + hIdx + '.value'" :rules="valueRules" label-width="0">
                    <el-input v-model="rh.value" :placeholder="i18nt('designer.setting.dsRequestValueInputPlaceholder')" :disabled="openModalType=='check'"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="3">
                  <el-button icon="el-icon-delete" plain circle @click="deleteRequestHeader(hIdx)" :disabled="openModalType=='check'"></el-button>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-button type="text" icon="el-icon-plus" @click="addRequestHeader" :disabled="openModalType=='check'">
                    {{i18nt('designer.setting.addRequestHeader')}}</el-button>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item :label="i18nt('designer.setting.dsRequestParams')">
              <el-row v-for="(rp, pIdx) in dsModel.params" :key="pIdx" class="rd-row" :gutter="8">
                <el-col :span="7">
                  <el-form-item :prop="'params.' + pIdx + '.key'" :rules="nameRules" label-width="0">
                    <el-input v-model="rp.key" :placeholder="i18nt('designer.setting.dsRequestNameInputPlaceholder')" :disabled="openModalType=='check'"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="5">
                  <el-form-item :prop="'params.' + pIdx + '.valueType'" label-width="0">
                    <el-select v-model="rp.valueType" :placeholder="i18nt('designer.setting.dsRequestTypeInputPlaceholder')" :disabled="openModalType=='check'">
                      <el-option :label="i18nt('designer.setting.dsRequestValueStringType')" :value="0"></el-option>
                      <el-option :label="i18nt('designer.setting.dsRequestValueNumberType')" :value="1"></el-option>
                      <el-option :label="i18nt('designer.setting.dsRequestValueBooleanType')" :value="2"></el-option>
                      <el-option :label="i18nt('designer.setting.dsRequestValueVariableType')" :value="3"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item :prop="'params.' + pIdx + '.value'" :rules="valueRules" label-width="0">
                    <el-input v-model="rp.value" :placeholder="i18nt('designer.setting.dsRequestValueInputPlaceholder')" :disabled="openModalType=='check'"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="3">
                  <el-button icon="el-icon-delete" plain circle @click="deleteRequestParam(pIdx)" :disabled="openModalType=='check'"></el-button>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-button type="text" icon="el-icon-plus" @click="addRequestParam" :disabled="openModalType=='check'">
                    {{i18nt('designer.setting.addRequestParam')}}</el-button>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item :label="i18nt('designer.setting.dsRequestData')">
              <el-row v-for="(rd, dIdx) in dsModel.data" :key="dIdx" class="rd-row" :gutter="8">
                <el-col :span="7">
                  <el-form-item :prop="'data.' + dIdx + '.key'" :rules="nameRules" label-width="0">
                    <el-input v-model="rd.key" :placeholder="i18nt('designer.setting.dsRequestNameInputPlaceholder')" :disabled="openModalType=='check'"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="5">
                  <el-form-item :prop="'data.' + dIdx + '.valueType'" label-width="0">
                    <el-select v-model="rd.valueType" :placeholder="i18nt('designer.setting.dsRequestTypeInputPlaceholder')" :disabled="openModalType=='check'">
                      <el-option :label="i18nt('designer.setting.dsRequestValueStringType')" :value="0"></el-option>
                      <el-option :label="i18nt('designer.setting.dsRequestValueNumberType')" :value="1"></el-option>
                      <el-option :label="i18nt('designer.setting.dsRequestValueBooleanType')" :value="2"></el-option>
                      <el-option :label="i18nt('designer.setting.dsRequestValueVariableType')" :value="3"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item :prop="'data.' + dIdx + '.value'" :rules="valueRules" label-width="0">
                    <el-input v-model="rd.value" :placeholder="i18nt('designer.setting.dsRequestValueInputPlaceholder')" :disabled="openModalType=='check'"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="3">
                  <el-button icon="el-icon-delete" plain circle @click="deleteRequestData(dIdx)" :disabled="openModalType=='check'"></el-button>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-button type="text" icon="el-icon-plus" @click="addRequestData" :disabled="openModalType=='check'">
                    {{i18nt('designer.setting.addRequestData')}}</el-button>
                </el-col>
              </el-row>
            </el-form-item>
  
            <!-- <el-tabs v-model="activeNames" type="border-card">
              <el-tab-pane name="1" :label="i18nt('designer.setting.dsConfigHandlerTitle')">
                <el-alert type="info" :closable="false" title="(config, isSandbox, DSV, VFR) => {"></el-alert>
                <code-editor :mode="'javascript'" :readonly="false" v-model="dsModel.configHandlerCode" ref="chEditor"></code-editor>
                <el-alert type="info" :closable="false" title="}"></el-alert>
              </el-tab-pane>
  
              <el-tab-pane name="2" :label="i18nt('designer.setting.dsDataHandlerTitle')">
                <el-alert type="info" :closable="false" title="(result, isSandbox, DSV, VFR) => {"></el-alert>
                <code-editor :mode="'javascript'" :readonly="false" v-model="dsModel.dataHandlerCode" ref="dhEditor"></code-editor>
                <el-alert type="info" :closable="false" title="}"></el-alert>
              </el-tab-pane>
  
              <el-tab-pane name="3" :label="i18nt('designer.setting.dsErrorHandlerTitle')">
                <el-alert type="info" :closable="false" title="(error, isSandbox, DSV, $message, VFR) => {"></el-alert>
                <code-editor :mode="'javascript'" :readonly="false" v-model="dsModel.errorHandlerCode" ref="ehEditor"></code-editor>
                <el-alert type="info" :closable="false" title="}"></el-alert>
              </el-tab-pane>
  
              <el-tab-pane name="4" :label="i18nt('designer.setting.dataSetSettingTitle')">
                <el-form-item :label="i18nt('designer.setting.dataSetEnabled')">
                  <el-switch v-model="dsModel.dataSetEnabled"></el-switch>
                </el-form-item>
                <el-form-item :label="i18nt('designer.setting.dataSetSetting')" v-if="dsModel.dataSetEnabled">
                  <el-row v-for="(dSet, dIdx) in dsModel.dataSets" :key="dIdx" class="rd-row" :gutter="8">
                    <el-col :span="7">
                      <el-form-item :prop="'dataSets.' + dIdx + '.name'" :rules="nameRules" label-width="0">
                        <el-input v-model="dSet.name" :placeholder="i18nt('designer.setting.dsRequestNameInputPlaceholder')"></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :span="8">
                      <el-form-item :prop="'dataSets.' + dIdx + '.remark'" label-width="0">
                        <el-input v-model="dSet.remark" :placeholder="i18nt('designer.setting.dataSetRemarkInputPlaceholder')"></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :span="3">
                      <el-button icon="el-icon-delete" plain circle @click="deleteDataSet(dIdx)"></el-button>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="6">
                      <el-button type="text" icon="el-icon-plus" @click="addDataSet">
                        {{i18nt('designer.setting.addDataSet')}}</el-button>
                    </el-col>
                  </el-row>
                </el-form-item>
              </el-tab-pane>
            </el-tabs> -->
  
          </el-form>
        </template>
        <template #footer>
          <div class="dialog-footer">
          </div>
        </template>
      </el-drawer>
  
      <el-dialog :title="i18nt('designer.setting.testDataSource')" :visible.sync="showTestDataSourceDialogFlag"
                 v-if="showTestDataSourceDialogFlag" :show-close="true" custom-class="drag-dialog small-padding-dialog"
                 append-to-body :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true">
        <!-- <el-alert type="info" :closable="false" :title="i18nt('designer.setting.dsvTitle')"></el-alert>
        <code-editor :mode="'json'" :readonly="false" v-model="dsvJson" class="dsv-json-editor"></code-editor>
        <div class="footer-button">
          <el-button type="primary" @click="doDataSourceRequest">{{i18nt('designer.setting.executeDataSource')}}</el-button>
          <el-button type="primary" plain @click="clearRequestResult">{{i18nt('designer.setting.clearRequestResult')}}</el-button>
          <el-button  @click="showTestDataSourceDialogFlag = false">
            {{i18nt('designer.hint.closePreview')}}</el-button>
        </div> -->
        <el-alert type="info" :closable="false" :title="i18nt('designer.setting.dsRequestResult')"></el-alert>
        <code-editor :mode="'json'" :readonly="true" v-model="dsResultJson" ref="dsResultEditor"></code-editor>
      </el-dialog>
  
      <el-dialog :title="i18nt('designer.setting.importDataSource')" :visible.sync="showImportDSDialogFlag"
                 v-if="showImportDSDialogFlag" :show-close="true" class="small-padding-dialog" center v-dialog-drag
                 append-to-body :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true">
        <el-alert type="info" :title="i18nt('designer.hint.importDSHint')" show-icon class="alert-padding"></el-alert>
        <code-editor :mode="'json'" :readonly="false" v-model="importDSTemplate"></code-editor>
        <el-switch v-model="clearOldDSFlag" style="margin-top: 10px"
                   :active-text="i18nt('designer.setting.clearExistingDataSource')"
                   :inactive-text="i18nt('designer.setting.remainExistingDataSource')"></el-switch>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="doImportDataSource">
            {{i18nt('designer.hint.import')}}</el-button>
          <el-button @click="showImportDSDialogFlag = false">
            {{i18nt('designer.hint.cancel')}}</el-button>
        </div>
      </el-dialog>
  
      <el-dialog :title="i18nt('designer.setting.exportDataSource')" :visible.sync="showExportDSDialogFlag" append-to-body
                 v-if="showExportDSDialogFlag" :show-close="true" class="small-padding-dialog" center v-dialog-drag
                 :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true">
        <el-tabs type="border-card" class="no-box-shadow no-padding" v-model="activeExportTab" @tab-click="handleExportTabClick">
          <el-tab-pane :label="i18nt('designer.setting.selectDataSourceForExport')" name="setting">
            <div v-if="exportDSArray && (exportDSArray.length > 0)" class="export-ds-list">
              <el-descriptions v-for="(ds, dsIdx) in exportDSArray" :key="dsIdx"
                               :column="1" size="small" border>
                <template #title>
                  <span :title="ds.description">{{ds.uniqueName}}</span>
                </template>
                <template #extra>
                  <el-checkbox v-model="ds.checked" @change="handleExportDSChange">{{i18nt('designer.setting.dataSourceChecked')}}</el-checkbox>
                </template>
                <el-descriptions-item>
                  <template slot="label">
                    <i :title="ds.requestURL" class="el-icon-s-platform"></i>
                  </template>
                  {{ds.requestURL}}
                </el-descriptions-item>
              </el-descriptions>
            </div>
            <template v-else>
              <el-empty :description="i18nt('designer.setting.noDataSource')"></el-empty>
            </template>
          </el-tab-pane>
          <el-tab-pane :label="i18nt('designer.setting.previewDataSourceExportResult')" name="result">
            <code-editor :mode="'json'" :readonly="true" v-model="dsExportContent" ref="exportDSEditor"></code-editor>
          </el-tab-pane>
        </el-tabs>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" class="copy-json-btn" :data-clipboard-text="dsRawExportContent" @click="copyDataSourceJson">
            {{i18nt('designer.hint.copyJson')}}</el-button>
          <el-button type="" @click="showExportDSDialogFlag = false">
            {{i18nt('designer.hint.closePreview')}}</el-button>
        </div>
      </el-dialog>
  
    </div>
  </template>
  
  <script>
    import i18n from "@/views/lowcode/utils/i18n"
    import CodeEditor from "@/views/lowcode/vformPro/components/code-editor/index"
    import {copyToClipboard, deepClone, generateId, runDataSourceRequest} from "@/views/lowcode/utils/util"
  
    export default {
      name: "dataSourceModel",
      mixins: [i18n],
      components: {
        CodeEditor,
      },
      props: {
        designer: Object,
        formConfig: Object,
        createModelDialogVisible:{
            type:Boolean,
            default: false
        },
        openModalType:{
          type:String,
          default:'add'
        },
        tempRowData:Object
      },
      watch: {
        createModelDialogVisible (newVal) {
            // console.log('skskls',newVal)
           this.showDataSourceDialogFlag = newVal
        },
        openModalType(newVal){
          // console.log('tempRowDatalllll',this.tempRowData)
          // console.log('类型',newVal)
          if(newVal=='add'){
            this.dsModel.uniqueName='';
            this.dsModel.requestURL='';
            this.dsModel.description='';
            this.dsModel.operationType=0;
            this.dsModel.requestMethod=0;
            this.dsModel.headers=[];
            this.dsModel.params=[];
            this.dsModel.data=[];
          }
        },
        tempRowData:{
           handler(newVal,oldVal){
            //console.log('上课就上课就饿',newVal,this.openModalType)
            if(this.openModalType!='add'){
              this.dsModel.uniqueName=newVal.name;
              this.dsModel.requestURL=newVal.url;
              this.dsModel.description=newVal.desc;
              this.dsModel.operationType=newVal.business;
              this.dsModel.requestMethod=newVal.requestType;
              this.dsModel.headers=newVal.fieldList.filter(item=>item.paramType==0);
              this.dsModel.params=newVal.fieldList.filter(item=>item.paramType==1);
              this.dsModel.data=newVal.fieldList.filter(item=>item.paramType==2);
            }
           },
           deep:true
        }
      },
      data() {
        return {
          //activeNames: ['2'],
          activeNames: '2',
  
          dsModel: {
            dataSourceId: null,
            uniqueName: '',
            requestURL: '',
            requestURLType: 'String',
            description: '',
            headers: [
            ],
            params: [
            ],
            data: [
            ],
            operationType: 0,
            requestMethod: 0,
            configHandlerCode: '  return config',
            dataHandlerCode: '  return result.data.data;',
            errorHandlerCode: '  $message.error(error.message);',
            dataSetEnabled: false,  // 是否开启多数据集
            dataSets: [
            ],
          },
          curEditDSIdx: -1,
  
          formRules: {
            uniqueName: [
              { required: true, trigger: ['blur', 'change'], message: this.i18nt('designer.setting.fieldValueRequired')}
            ],
            requestURL: [
              { required: true, trigger: ['blur', 'change'], message: this.i18nt('designer.setting.fieldValueRequired') }
            ],
            requestMethod: [
              { required: true, trigger: ['blur', 'change'], message: this.i18nt('designer.setting.fieldValueRequired') }
            ],
            operationType: [
            { required: true, message: '请选择操作类型', trigger: 'change' }
          ],
          },
          nameRules: [
            { required: true, trigger: ['blur', 'change'], message: this.i18nt('designer.setting.fieldValueRequired') },
          ],
          valueRules: [
            { required: true, trigger: ['blur', 'change'], message: this.i18nt('designer.setting.fieldValueRequired') },
            { validator: this.validateValueInput, trigger: ['blur', 'change'] }
          ],
  
          showDataSourceDialogFlag: false,
  
          dsvJson: '{\n  \n}',
          dsResultJson: '',
          showTestDataSourceDialogFlag: false,
  
          showImportDSDialogFlag: false,
          importDSTemplate: '',
          clearOldDSFlag: false,  //导入后是否清空原有数据源
  
          showExportDSDialogFlag: false,
          activeExportTab: 'setting',
          exportDSArray: [],
          dsExportContent: '',
          dsRawExportContent: '',
        }
      },
      methods: {
        validateValueInput(rule, value, callback) {
          let ruleField = rule.field
          let fieldType = ruleField.substring(0, ruleField.indexOf('.'))
          let fieldIdx = Number(ruleField.substring(ruleField.indexOf('.') + 1, ruleField.lastIndexOf('.')))
          let valueType = this.dsModel[fieldType][fieldIdx].type
          if (valueType === 'Number') {
            if (isNaN(value)) {
              callback(new Error(this.i18nt('designer.setting.dsRequestNumberTypeError')))
            } else {
              callback()
            }
          } else if (valueType === 'Boolean') {
            if ((value.toLowerCase() === 'true') || (value.toLowerCase() === 'false') || (value.toLowerCase() === 'null') ||
               (value === '1') || (value === '0')) {
              callback()
            } else {
              callback(new Error(this.i18nt('designer.setting.dsRequestBooleanTypeError')))
            }
          } else {
            callback()
          }
        },
  
        addDataSource() {
          this.curEditDSIdx = -1
          this.dsModel = deepClone(this.dsModel)
          this.dsModel.dataSourceId = null
          this.dsModel.uniqueName = ''
          this.dsModel.requestURL = ''
          this.dsModel.requestURLType = 'String'
          this.dsModel.requestMethod = 0
          this.dsModel.description = ''
          this.dsModel.headers.splice(0, this.dsModel.headers.length)
          this.dsModel.params.splice(0, this.dsModel.params.length)
          this.dsModel.data.splice(0, this.dsModel.data.length)
          this.dsModel.configHandlerCode = '  return config'
          this.dsModel.dataHandlerCode = '  return result.data.data;'
          this.dsModel.errorHandlerCode = '  $message.error(error.message);'
          this.dsModel.dataSetEnabled = false
          if (!this.dsModel.hasOwnProperty('dataSets')) { //补齐dataSets属性
            this.$set(this.dsModel, 'dataSets', [])
          } else {
            this.dsModel.dataSets.splice(0, this.dsModel.dataSets.length)
          }
  
          this.showDataSourceDialogFlag = true
        },
  
        editDataSource(dsIdx) {
          this.dsModel = deepClone(this.formConfig.dataSources[dsIdx])
          if (!this.dsModel.hasOwnProperty('dataSets')) { //补齐dataSets属性
            this.$set(this.dsModel, 'dataSets', [])
          }
          this.curEditDSIdx = dsIdx
          this.showDataSourceDialogFlag = true
        },
        //关闭弹窗
        handleClose(){
            this.$parent.createModelDialogVisible  = false;
        },
  
        saveDataSource() {
          //console.log('dsModel is: ', this.dsModel)
  
          this.$refs['dsForm'].validate((valid, fields) => {
            if (!valid) {
              this.$message.error(this.i18nt('designer.setting.dsValidationError'))
              return
            }
            // console.log('ususy800',this.dsModel)
            const tempHeaders={};
            let tempParams='';
            const tempData={};
            for(let i=0; i<this.dsModel.headers.length;i++){
              tempHeaders[this.dsModel.headers[i].key]= this.dsModel.headers[i].value
            }
            for(let i=0; i<this.dsModel.params.length;i++){
              tempParams+=`${this.dsModel.params[i].key}=${this.dsModel.params[i].value}${i==this.dsModel.params.length-1?'':'&'}`
            }
            for(let i=0; i<this.dsModel.data.length;i++){
              tempData[this.dsModel.data[i].key]= this.dsModel.data[i].value
            }
            const configParams={
              id:this.openModalType=='add'?undefined:this.tempRowData.id,
              name:this.dsModel.uniqueName,
              url:this.dsModel.requestURL,
              desc:this.dsModel.description,
              business:this.dsModel.operationType,
              requestType:this.dsModel.requestMethod,
              headers:JSON.stringify(tempHeaders),
              params:tempParams,
              data:JSON.stringify(tempData),
              status:0,
              fieldList:[
                ...this.dsModel.headers,
                ...this.dsModel.params,
                ...this.dsModel.data,
              ]
            };
            this.$http
            .post('/api/source/addSourceConfig' , configParams)
            .then((res) => {
              if (res.data.code === 500) {
                this.$message.error(res.data.msg)
                return
              }
              
              this.$parent.createModelDialogVisible  = false;
              this.$refs['dsForm'].resetFields();
              this.dsModel.headers=[];
              this.dsModel.params=[];
              this.dsModel.data=[];
              this.$parent.queryList();
              this.$message.success(res.data.msg)
            })
            .catch((error) => {
              
            })
            
          })
        },
  
        deleteDataSource(dsIdx) {
          this.$confirm(this.i18nt('designer.setting.deleteDataSourceHint'), this.i18nt('render.hint.prompt'), {
            confirmButtonText: this.i18nt('render.hint.confirm'),
            cancelButtonText: this.i18nt('render.hint.cancel')
          }).then(() => {
            this.formConfig.dataSources.splice(dsIdx, 1)
          }).catch(error => {
            console.error(error)
          })
        },
  
        addRequestHeader() {
          this.dsModel.headers.push({
            paramType: 0,
            key: '',
            valueType: 0,
            value: ''
          })
        },
  
        deleteRequestHeader(idx) {
          this.dsModel.headers.splice(idx, 1)
        },
  
        addRequestParam() {
          this.dsModel.params.push({
            paramType: 1,
            key: '',
            valueType: 0,
            value: ''
          })
        },
  
        deleteRequestParam(idx) {
          this.dsModel.params.splice(idx, 1)
        },
  
        addRequestData() {
          this.dsModel.data.push({
            paramType: 2,
            key: '',
            valueType: 0,
            value: ''
          })
        },
  
        deleteRequestData(idx) {
          this.dsModel.data.splice(idx, 1)
        },
  
        testDataSource() {
        //   let globalDsv = this.getGlobalDsv() || {}
        //   this.dsvJson = JSON.stringify(globalDsv, null, '  ')
        this.$refs['dsForm'].validate((valid, fields) => {
            if (!valid) {
              this.$message.error(this.i18nt('designer.setting.dsValidationError'))
              return
            }
            this.showTestDataSourceDialogFlag = true
            const tempHeaders={};
            let tempParams='';
            const tempData={};
            for(let i=0; i<this.dsModel.headers.length;i++){
              tempHeaders[this.dsModel.headers[i].key]= this.dsModel.headers[i].value
            }
            for(let i=0; i<this.dsModel.params.length;i++){
              tempParams+=`${this.dsModel.params[i].key}=${this.dsModel.params[i].value}${i==this.dsModel.params.length-1?'':'&'}`
            }
            for(let i=0; i<this.dsModel.data.length;i++){
              tempData[this.dsModel.data[i].key]= this.dsModel.data[i].value
            }
            const configParams={
              name:this.dsModel.uniqueName,
              url:this.dsModel.requestURL,
              desc:this.dsModel.description,
              business:this.dsModel.operationType,
              requestType:this.dsModel.requestMethod,
              headers:JSON.stringify(tempHeaders),
              params:tempParams,
              data:JSON.stringify(tempData),
              status:0,
              fieldList:[
                ...this.dsModel.headers,
                ...this.dsModel.params,
                ...this.dsModel.data,
              ]
            };
            this.$http
            .post('/api/source/testSourceConfig' , configParams)
            .then((res) => {
              if (res.data.code === 500) {
                this.$message.error(res.data.msg)
                return
              }
              this.$refs.dsResultEditor.setValue( JSON.stringify(res.data.data, null, '  ') )           
              this.$message.success(res.data.msg)
            })
            .catch((error) => {
              
            })
            
          })
         
        },
  
        async doDataSourceRequest() {
          let dsvObj = JSON.parse(this.dsvJson)
          let dsResult = await runDataSourceRequest(this.dsModel, dsvObj, null, true, this.$message)
          this.$refs.dsResultEditor.setValue( JSON.stringify(dsResult, null, '  ') )
        },
  
        clearRequestResult() {
          this.$refs.dsResultEditor.setValue('')
        },
  
        addDataSet() {
          this.dsModel.dataSets.push({
            name: '',
            remark: ''
          })
        },
  
        deleteDataSet(idx) {
          this.dsModel.dataSets.splice(idx, 1)
        },
  
        importDataSource() {
          this.importDSTemplate = ''
          this.showImportDSDialogFlag = true
        },
  
        doImportDataSource() {
          try {
            let importDSArray = JSON.parse(this.importDSTemplate)
            if (!!this.clearOldDSFlag) {
              this.formConfig.dataSources.splice(0, this.formConfig.dataSources.length)
            }
            this.formConfig.dataSources = this.formConfig.dataSources.concat(importDSArray)
            this.$message.success(this.i18nt('designer.hint.importJsonSuccess'))
            this.designer.emitHistoryChange()
            this.showImportDSDialogFlag = false
          } catch (ex) {
            this.$message.error(ex.message)
          }
        },
  
        exportDataSource() {
          this.dsExportContent = ''
          this.dsRawExportContent = ''
          this.exportDSArray.splice(0, this.exportDSArray.length)
          if (!!this.formConfig.dataSources && (this.formConfig.dataSources.length > 0)) {
            this.formConfig.dataSources.forEach(ds => {
              let newDS = deepClone(ds)
              this.$set(newDS, 'checked', false)
              this.exportDSArray.push(newDS)
            })
          }
  
          this.showExportDSDialogFlag = true
        },
  
        copyDataSourceJson(e) {
          copyToClipboard(this.dsRawExportContent, e,
              this.$message,
              this.i18nt('designer.hint.copyJsonSuccess'),
              this.i18nt('designer.hint.copyJsonFail')
          )
        },
  
        handleExportDSChange(val) {
          let selectedDSArray = []
          this.exportDSArray.forEach(ds => {
            if (!!ds.checked) {
              let selectedDs = deepClone(ds)
              delete selectedDs['checked']
              selectedDSArray.push(selectedDs)
            }
          })
  
          this.dsExportContent = JSON.stringify(selectedDSArray, null, '  ')
          this.dsRawExportContent = JSON.stringify(selectedDSArray)
        },
  
        handleExportTabClick() {
          this.$nextTick(() => {
            this.$refs.exportDSEditor.setValue(this.dsExportContent)
          })
        },
  
      },
    }
  </script>
  
  <style lang="scss" scoped>
    .ds-list {
      ::v-deep .el-descriptions {
        width: 280px;
        overflow-x: hidden;
        margin-bottom: 15px;
        padding: 8px;
        background: #f5f7fa;
      }
  
      ::v-deep .el-descriptions__title {
        font-weight: normal;
      }
  
      ::v-deep .el-descriptions-item__label {
        width: 36px;
      }
    }
  
    .ds-button-wrapper {
      text-align: center;
      margin-top: 12px;
    }
  
    .ds-form {
      ::v-deep .el-form-item.is-required>.el-form-item__label:before {
        content: '' !important;
        margin-right: 0 !important;
      }
  
      ::v-deep .el-form-item.is-required>.el-form-item__label:after {
        content: '*' !important;
        color: var(--el-color-danger);
        font-weight: bold;
      }
    }
  
    .no-left-margin ::v-deep .el-form-item__content {
      margin-left: 0 !important;
    }
  
    .rh-row, .rd-row {
      ::v-deep .el-col {
        margin: 5px 0;
      }
    }
  
    .ch-collapse ::v-deep .ace-editor {
      border: 1px solid #e1e2e3;
      min-height: 120px;
    }
  
    .dh-collapse ::v-deep .ace-editor {
      border: 1px solid #e1e2e3;
      min-height: 220px;
    }
  
    .eh-collapse ::v-deep .ace-editor {
      border: 1px solid #e1e2e3;
      min-height: 120px;
    }
  
    .dsv-json-editor {
      margin-bottom: 12px;
    }
  
    .dsv-json-editor ::v-deep .ace-editor {
      border: 1px solid #e1e2e3;
      min-height: 120px;
    }
  
    ::v-deep .ace-editor {
      border: 1px solid #e1e2e3;
    }
  
    .footer-button {
      float: right;
      margin-bottom: 12px;
    }
  
    .export-ds-list {
      ::v-deep .el-descriptions {
        overflow-x: hidden;
        margin-bottom: 15px;
        padding: 8px;
        background: #f5f7fa;
      }
  
      ::v-deep .el-descriptions__title {
        font-weight: normal;
      }
  
      ::v-deep .el-descriptions-item__label {
        width: 36px;
      }
    }
  
  </style>
  