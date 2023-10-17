<template>
  <div>
    <el-form-item :label="i18nt('designer.setting.tableWidth')">
      <el-input v-model="optionModel.tableWidth"></el-input>
    </el-form-item>
    <el-form-item :label="i18nt('designer.setting.tableHeight')">
      <el-input v-model="optionModel.tableHeight"></el-input>
    </el-form-item>
    <el-form-item :label="i18nt('designer.setting.customClass')">
      <el-select v-model="optionModel.customClass" multiple filterable allow-create
                 default-first-option>
        <el-option v-for="(item, idx) in cssClassList" :key="idx" :label="item" :value="item"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item :label="i18nt('designer.setting.showIndex')">
      <el-switch v-model="optionModel.showIndex"></el-switch>
    </el-form-item>
    <el-form-item :label="i18nt('designer.setting.showCheckBox')">
      <el-switch v-model="optionModel.showCheckBox"></el-switch>
    </el-form-item>
    <el-form-item :label="i18nt('designer.setting.showPagination')">
      <el-switch v-model="optionModel.showPagination"></el-switch>
    </el-form-item>
    <el-form-item :label="i18nt('designer.setting.smallPagination')">
      <el-switch v-model="optionModel.smallPagination"></el-switch>
    </el-form-item>
    <el-form-item :label="i18nt('designer.setting.showSummary')">
      <el-switch v-model="optionModel.showSummary"></el-switch>
    </el-form-item>
    <el-form-item :label="i18nt('designer.setting.stripe')">
      <el-switch v-model="optionModel.stripe"></el-switch>
    </el-form-item>
    <el-form-item :label="i18nt('designer.setting.rowSpacing')">
      <el-input-number v-model="optionModel.rowSpacing" controls-position="right" :min="0" :max="20"
                       style="width: 100%">
      </el-input-number>
    </el-form-item>
    <!--分页-->
    <el-form-item :label="i18nt('designer.setting.pageWidth')">
      <el-select v-model="optionModel.pageWidth">
        <el-option v-for="item in pagePlaces" :key="item.value" :label="item.label"
                   :value="item.value">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item :label="i18nt('designer.setting.widgetSize')">
      <el-select v-model="optionModel.tableSize" @change="refreshTableLayout">
        <el-option v-for="item in widgetSizes" :key="item.value" :label="item.label"
                   :value="item.value">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item :label="i18nt('designer.setting.tableColEdit')">
      <el-button type="primary" plain round @click="openSetting">{{ i18nt('designer.setting.editAction') }}</el-button>
    </el-form-item>
    <el-form-item v-show="!optionModel.dsEnabled" :label="i18nt('designer.setting.tableDataEdit')">
      <!-- 此处使用v-if指令会报错，原因不明？？ -->
      <el-button type="primary" plain round @click="openTableDataEdit">{{ i18nt('designer.setting.editAction') }}
      </el-button>
    </el-form-item>
    <el-form-item :label="i18nt('designer.setting.dsEnabled')">
      <el-switch v-model="optionModel.dsEnabled"></el-switch>
    </el-form-item>
    <el-form-item v-if="!!optionModel.dsEnabled" :label="i18nt('designer.setting.dsName')">
      <el-select v-model="optionModel.dsName" filterable clearable>
        <el-option v-for="(item, idx) in dataSourceList" :key="idx" :title="item.description"
                   :label="item.uniqueName" :value="item.uniqueName"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item :label="i18nt('designer.setting.showButtonsColumn')">
      <el-switch v-model="optionModel.showButtonsColumn" @change="handleShowButtonsColumnChange"></el-switch>
    </el-form-item>
    <el-form-item :label="i18nt('designer.setting.buttonsColumnEdit')" v-if="!!optionModel.showButtonsColumn">
      <el-button type="primary" plain round @click="editButtonsColumn">{{ i18nt('designer.setting.editAction') }}
      </el-button>
    </el-form-item>

    <el-dialog :title="i18nt('designer.setting.tableDataEdit')" :visible.sync="dataDialogVisible"
               v-if="dataDialogVisible" :show-close="true" class="small-padding-dialog" append-to-body
               v-dialog-drag :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true" width="75%">
      <code-editor :mode="'json'" :readonly="false" v-model="tableDataOptions"></code-editor>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="large" type="primary" @click="saveTableData">{{ i18nt('designer.hint.confirm') }}</el-button>
          <el-button size="large" type="" @click="dataDialogVisible = false">{{ i18nt('designer.hint.cancel') }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog :title="i18nt('designer.setting.tableColEdit')" :visible.sync="dialogVisible"
               v-if="dialogVisible" :show-close="true" class="small-padding-dialog"
               v-dialog-drag :close-on-click-modal="false" :close-on-press-escape="false" append-to-body
               :destroy-on-close="true" width="1220px">
      <el-table :data="optionModel.tableColumns" style="width: 100%" :cell-style="{padding:'1px 0'}"
                height="500" border row-key="columnId" ref="singleTable" stripe>
        <el-table-column type="index" width="35" fixed="left"></el-table-column>
        <el-table-column label="" width="30">
          <i class="iconfont icon-drag drag-option"></i>
        </el-table-column>
        <el-table-column label="columnId" prop="columnId" width="150" v-if="false"></el-table-column>
        <el-table-column :label="i18nt('designer.setting.columnName')" width="150" prop="prop">
          <template slot-scope="scope">
            <el-input  v-model="scope.row.prop"></el-input>
          </template>
        </el-table-column>
        <el-table-column :label="i18nt('designer.setting.columnLabel')" width="150" prop="label">
          <template slot-scope="scope">
            <el-input v-model="scope.row.label"></el-input>
          </template>
        </el-table-column>
        <el-table-column :label="i18nt('designer.setting.columnWidth')" width="100" prop="width">
          <template slot-scope="scope">
            <el-input v-model="scope.row.width"></el-input>
          </template>
        </el-table-column>
        <el-table-column :label="i18nt('designer.setting.visibleColumn')" width="70" prop="show">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.show"></el-switch>
          </template>
        </el-table-column>
        <el-table-column :label="i18nt('designer.setting.sortableColumn')" width="70" prop="sortable">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.sortable"></el-switch>
          </template>
        </el-table-column>
        <el-table-column :label="i18nt('designer.setting.fixedColumn')" width="100" prop="fixed">
          <template slot-scope="scope">
            <el-select v-model="scope.row.fixed">
              <el-option value="">false</el-option>
              <el-option value="left">left</el-option>
              <el-option value="right">right</el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column :label="i18nt('designer.setting.alignTypeOfColumn')" width="100" prop="align">
          <template slot-scope="scope">
            <el-select v-model="scope.row.align">
              <el-option v-for="item in alignOptions" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <!--    自定义选择排序方式    -->
        <el-table-column :label="i18nt('designer.setting.sortType')" width="100" prop="sortType">
          <template slot-scope="scope">
            <el-select v-model="scope.row.sortType">
              <el-option v-for="item in sortOptions" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column :label="i18nt('designer.setting.formatOfColumn')" width="200" prop="formatS">
          <template slot-scope="scope">
            <el-select v-model="scope.row.formatS" clearable>
              <el-option-group :label="i18nt('designer.setting.customRenderGroup')" key="custom-render-group">
                <el-option value="render" label="render"></el-option>
              </el-option-group>
              <el-option-group
                v-for="group in op"
                :key="group.label"
                :label="group.label">
                <el-option
                  v-for="item in group.options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-option-group>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column :label="i18nt('designer.setting.renderFunction')" width="80" fixed="right" align="center">
          <template slot-scope="scope">
            <el-button :disabled="scope.row.formatS !== 'render'" @click="showRenderDialog(scope.row)"
                       size="mini" plain round icon="el-icon-edit"></el-button>
          </template>
        </el-table-column>
        <el-table-column :label="i18nt('designer.setting.actionColumn')" width="100" fixed="right" align="center">
          <template slot-scope="scope">
            <el-button :title="i18nt('designer.setting.addTableColumn')" size="mini" type="" circle
                       @click="addCol" icon="el-icon-plus"></el-button>
            <el-button :title="i18nt('designer.setting.deleteTableColumn')" size="mini" type="" circle
                       @click="handleDelete(scope.$index, scope.row)" icon="el-icon-minus"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="large" type="primary" @click="colSubmit">{{ i18nt('designer.hint.confirm') }}</el-button>
          <el-button size="large" @click="dialogVisible = false">{{ i18nt('designer.hint.cancel') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog :title="i18nt('designer.setting.buttonsColumnEdit')" :visible.sync="showButtonsEditDialog"
               v-if="showButtonsEditDialog"
               :show-close="true" custom-class="drag-dialog small-padding-dialog" append-to-body v-dialog-drag
               :close-on-click-modal="false" :close-on-press-escape="false"
               :destroy-on-close="true" width="1120px">
      <el-form label-width="110px" label-position="left" :model="optionModel">
        <el-row :gutter="8">
          <el-col :span="11">
            <el-form-item prop="buttonsColumnTitle" :label="i18nt('designer.setting.buttonsColumnTitle')">
              <el-input v-model="optionModel.buttonsColumnTitle"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item prop="buttonsColumnWidth" :label="i18nt('designer.setting.buttonsColumnWidth')">
              <el-input-number v-model="optionModel.buttonsColumnWidth"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item prop="buttonsColumnFixed" :label="i18nt('designer.setting.fixedColumn')">
              <el-select v-model="optionModel.buttonsColumnFixed">
                <el-option value="">false</el-option>
                <el-option value="left">left</el-option>
                <el-option value="right">right</el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-divider content-position="left">{{ i18nt('designer.setting.operationButtonsSetting') }}</el-divider>
          </el-col>
        </el-row>
        <draggable tag="div" :list="optionModel.operationButtons"
                   v-bind="{ghostClass: 'ghost', handle: '.drag-handler'}">
          <el-row v-for="(btn, bIdx) in optionModel.operationButtons" :key="bIdx" :gutter="8" class="button-row">
            <el-col :span="1" class="drag-sort-col">
              <i class="iconfont icon-drag drag-handler"></i>
            </el-col>
            <el-col :span="3">
              <el-form-item :prop="'operationButtons.' + bIdx + '.name'" label-width="50px"
                            :label="i18nt('designer.setting.operationButtonName')" :rules="nameRules">
                <el-input v-model="btn.name" @focus="onButtonNameFocus"
                          @change="(value) => onButtonNameChange(value, bIdx,btn)"
                          :placeholder="i18nt('designer.setting.operationButtonName')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item :prop="'operationButtons.' + bIdx + '.type'" label-width="50px"
                            :label="i18nt('designer.setting.operationButtonType')">
                <el-select v-model="btn.type" :placeholder="i18nt('designer.setting.operationButtonType')">
                  <el-option value="text">text</el-option>
                  <el-option value="primary">primary</el-option>
                  <el-option value="success">success</el-option>
                  <el-option value="warning">warning</el-option>
                  <el-option value="danger">danger</el-option>
                  <el-option value="info">info</el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item :prop="'operationButtons.' + bIdx + '.opeType'" label-width="80px"
                            label="事件类型">
                <el-select @change="opeTypeChange(btn)" v-model="btn.opeType">
                  <el-option label="编辑" value="编辑"></el-option>
                  <el-option label="删除" value="删除"></el-option>
                  <el-option label="详情" value="详情"></el-option>
                  <el-option label="查看更多" value="查看更多"></el-option>
                  <el-option label="自定义" value="自定义"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item :prop="'operationButtons.' + bIdx + '.pageId'"
                            label-width="80px"
                            label="绑定表单">
                <el-select v-model="btn.pageId">
                  <el-option
                    v-for="(item,index) in pages"
                    :label="item.pageName"
                    :value="item.pageId"
                    :key="index+'pages'"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="3">
              <el-form-item :prop="'operationButtons.' + bIdx + '.size'" label-width="50px"
                            :label="i18nt('designer.setting.operationButtonSize')">
                <el-select v-model="btn.size" :placeholder="i18nt('designer.setting.operationButtonSize')">
                  <el-option value="medium">medium</el-option>
                  <el-option value="small">small</el-option>
                  <el-option value="mini">mini</el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="3" v-show="btn.opeType === '查看更多'">
              <!--              <el-form-item :prop="'operationButtons.' + bIdx + '.round'" label-width="50px"-->
              <!--                            :label="i18nt('designer.setting.operationButtonRound')">-->
              <!--                <el-switch v-model="btn.round"></el-switch>-->
              <!--              </el-form-item>-->
              <Button @click="clickBzField(btn)" type="primary">绑定关联字段</Button>
            </el-col>
            <el-col :span="1">
              <el-button icon="el-icon-delete" plain circle @click="deleteOperationButton(bIdx)"></el-button>
            </el-col>
          </el-row>
        </draggable>
        <el-row :gutter="0">
          <el-col :span="4">
            <el-button type="primary" size="small" icon="el-icon-plus"
                       plain round @click="addOperationButton">{{ i18nt('designer.setting.addOperationButton') }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="default" @click="showButtonsEditDialog = false">{{ i18nt('designer.hint.closePreview') }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog :title="i18nt('designer.setting.renderFunction')" :visible.sync="showRenderDialogFlag"
               v-if="showRenderDialogFlag" v-dialog-drag append-to-body
               :show-close="true" custom-class="drag-dialog small-padding-dialog"
               :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true">
      <el-alert type="info" :closable="false" title="function customRender(h, params) {"></el-alert>
      <code-editor :mode="'javascript'" :readonly="false" v-model="renderJson" ref="dsResultEditor"></code-editor>
      <el-alert type="info" :closable="false" title="}"></el-alert>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveColumnRender">
          {{ i18nt('designer.hint.confirm') }}
        </el-button>
        <el-button @click="showRenderDialogFlag = false">
          {{ i18nt('designer.hint.cancel') }}
        </el-button>
      </div>
    </el-dialog>

    <!--  绑定关联字段弹窗   -->
    <el-dialog title="绑定关联字段" :visible.sync="showGlzdDialog"
               v-dialog-drag append-to-body
               :show-close="true" custom-class="drag-dialog small-padding-dialog"
               :close-on-click-modal="false" :close-on-press-escape="false">
      <div style="height: 100%">
        <div style="height: 100%">
          <div class="containerBox">
            <div
              v-for="(tableItem,index) in  childTableField"
              :key="index + 'hbwtable'"
              class="tableItemBox"
            >
              <el-table
                :data="tableItem.data"
                style="width: 100%">
                <el-table-column prop="mainTableField" :label="'表一：（'+mainTableFields[0].label+'）'" width="220">
                  <template v-slot="scope">
                    <el-select clearable v-model="scope.row.mainTableField" placeholder="请选择要绑定的字段">
                      <el-option
                        v-for="(item,index) in mainTableFields[0].options"
                        :key="index + 'main66'"
                        :label="item.columnName"
                        :value="item.columnName"
                      >
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="chiTableField" :label="'表二：（'+tableItem.label+'）'" width="220">
                  <template v-slot="scope">
                    <el-select clearable v-model="scope.row.chiTableField" placeholder="请选择要绑定的字段">
                      <el-option
                        v-for="(item,index) in tableItem.options"
                        :key="index + item"
                        :label="item.columnName"
                        :value="item.columnName"
                      >
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column label="操作">
                  <template v-slot="scope">
                    <el-button
                      v-show="scope.$index === (tableItem.data.length-1) && scope.row.chiTableField && scope.row.mainTableField"
                      @click="addItem(tableItem.data)"
                      type="text">
                      添加
                    </el-button>
                    <el-button
                      v-show="scope.$index === (tableItem.data.length-1) && scope.row.chiTableField && scope.row.mainTableField"
                      @click="deleteItem(tableItem.data)"
                      type="text">
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="conFirmBindZd">确 定</el-button>
      </span>
    </el-dialog>
  </div>

</template>

<script>
import i18n from '@/views/lowcode/utils/i18n'
import Draggable from 'vuedraggable'
import { deepClone, generateId } from '@/views/lowcode/utils/util'
import Sortable from 'sortablejs'
import CodeEditor from '@/views/lowcode/vformPro/components/code-editor/index'

export default {
  name: 'data-table-customClass-editor',
  componentName: 'PropertyEditor',
  mixins: [i18n],
  components: {
    Draggable,
    CodeEditor,
  },
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object,
  },
  data () {
    return {
      pages: [],
      dialogVisible: false,
      dataDialogVisible: false,
      showButtonsEditDialog: false,
      oldButtonName: '',
      cssClassList: [],
      tableDataOptions: [],
      widgetSizes: [
        { label: 'default', value: '' },
        { label: 'large', value: 'large' },
        { label: 'medium', value: 'medium' },
        { label: 'small', value: 'small' },
        { label: 'mini', value: 'mini' },
      ],
      pagePlaces: [
        { label: 'left', value: 'flex-start' },
        { label: 'center', value: 'center' },
        { label: 'right', value: 'flex-end' },
      ],
      alignOptions: [
        {
          value: 'left',
          label: 'left'
        }, {
          value: 'center',
          label: 'center'
        }, {
          value: 'right',
          label: 'right'
        },
      ],
      sortOptions: [
        {
          value: 'bpx',
          label: '不排序'
        },
        {
          value: 'asc',
          label: '升序'
        }, {
          value: 'desc',
          label: '降序'
        }
      ],
      fieldTypeOptions: [
        {
          value: 'text',
          label: 'Text'
        }, {
          value: 'number',
          label: 'Number'
        }, {
          value: 'date',
          label: 'Date'
        },
      ],
      op: [
        {
          label: 'Date Format',
          options: [
            { value: 'd1', label: 'yyyy-MM-dd' },
            { value: 'd2', label: 'yyyy/MM/dd' },
            { value: 'd3', label: 'yyyy年MM月dd日' },
            { value: 'd4', label: 'yyyy-MM-dd HH:mm:ss' },
            { value: 'd5', label: 'yyyy-MM-dd hh:mm:ss' },
          ]
        }, {
          label: 'Number Format',
          options: [
            { value: 'n1', label: '###,###,###,##0.######' },
            { value: 'n2', label: '###,###,###,##0.00####' },
            { value: 'n3', label: '###,###,###,##0.000000' },
            { value: 'n4', label: '###,###,###,##0.000' },
            { value: 'n5', label: '###,###,###,##0.00' },
            { value: 'n6', label: '###,###,###,##0' },
            { value: 'n7', label: '###,##0.00##%' },

          ]
        }],
      showRenderDialogFlag: false,
      showGlzdDialog: false,
      childTableField: [],
      mainTableFields: [],

      renderJson: '',
      currentTableColumn: null,
      nameRules: [
        { required: true, trigger: ['blur', 'change'], message: this.i18nt('designer.setting.fieldValueRequired') }
      ]
    }
  },
  computed: {
    dataSourceList () {
      if (!this.designer.formConfig || !this.designer.formConfig.dataSources) {
        return []
      } else {
        return this.designer.formConfig.dataSources
      }
    },

  },
  created () {
    this.cssClassList = deepClone(this.designer.getCssClassList())
    //监听表单css代码改动事件并重新加载！
    this.designer.handleEvent('form-css-updated', (cssClassList) => {
      this.cssClassList = cssClassList
    })
  },
  mounted () {
    // this.dragSort()
    this.$http.get('/api/lowcode/customize/cscpCustomizeVpages', {
      params: {
        page: 0,
        size: 1000
      }
    }).then(res => {
      this.pages = res.data.data
    })
  },
  methods: {
    opeTypeChange (btn) {
      console.log(btn)
      // btn.name = btn.opeType
      // btn.label = btn.opeType
      // if (btn.opeType === '查看更多') {
      //   this.clickBindField(btn.pageId)
      // }
    },
    //表格拖动排序
    dragSort () {
      const el = this.$refs.singleTable.$el.querySelectorAll('.el-table__body-wrapper > table > tbody')[0]
      let tableData = this.optionModel.tableColumns
      this.sortable = Sortable.create(el, {
        ghostClass: 'sortable-ghost',
        setData: function (dataTransfer) {
          dataTransfer.setData('Text', '')
        },
        onEnd: e => {
          //e.oldIndex为拖动一行原来的位置，e.newIndex为拖动后新的位置
          const targetRow = tableData.splice(e.oldIndex, 1)[0]
          tableData.splice(e.newIndex, 0, targetRow)
          let dragId = tableData[e.newIndex].id//拖动行的id
          let oneId, twoId
          //拖动行的前一行
          if (tableData[e.newIndex - 1]) {
            oneId = tableData[e.newIndex - 1].id
          } else {
            oneId = ''
          }
          //拖动行的后一行
          if (tableData[e.newIndex + 1]) {
            twoId = tableData[e.newIndex + 1].id
          } else {
            twoId = ''
          }
        }
      })

    },

    openTableDataEdit () {
      this.dataDialogVisible = true
      this.tableDataOptions = JSON.stringify(this.optionModel.tableData, null, '  ')
    },

    saveTableData () {
      try {
        this.optionModel.tableData = JSON.parse(this.tableDataOptions)
        this.dataDialogVisible = false
      } catch (ex) {
        this.$message.error(this.i18nt('designer.hint.invalidOptionsData') + ex.message)
      }
    },

    openSetting () {
      this.dialogVisible = true
      this.$nextTick(() => {
        this.dragSort()
      })
    },

    // 确认表格列更改
    colSubmit () {
      this.dialogVisible = false
    },

    addCol () {
      let newRow = { columnId: new Date().getTime() }
      this.optionModel.tableColumns.push(newRow)
      this.designer.emitHistoryChange()
    },

    handleDelete (index, row) {
      if (this.optionModel.tableColumns.length === 1) {
        this.$message.warning(this.i18nt('designer.setting.onlyOneColumnCannotBeDeleted'))
        return false
      }
      this.optionModel.tableColumns.splice(index, 1)
    },

    showRenderDialog (tableColumn) {
      this.currentTableColumn = tableColumn
      this.renderJson = tableColumn.render || ''
      this.showRenderDialogFlag = true
    },

    saveColumnRender () {
      this.$set(this.currentTableColumn, 'render', this.renderJson)
      this.showRenderDialogFlag = false
    },

    handleShowButtonsColumnChange (value) { // 刷新表格显示，防止行列显示错位！！
      if (!!value) {
        let dataTableInDesign = this.designer.formWidget.getSelectedWidgetRef()
        if (!!dataTableInDesign && !!dataTableInDesign.refreshLayout) {
          this.$nextTick(() => {
            dataTableInDesign.refreshLayout()
          })
        }
      }
    },

    onButtonNameFocus (event) {
      console.log('test', event)
      this.oldButtonName = event.target.value
    },

    onButtonNameChange (newName, btnIdx, btn) {
      btn.label = btn.name
      let sameNameFlag = false
      this.optionModel.operationButtons.map((tb, tbIdx) => {
        if ((tb.name === newName) && (tbIdx !== btnIdx)) {
          sameNameFlag = true
        }
      })
      if (sameNameFlag) {
        this.$message.error(this.i18nt('designer.setting.operationButtonDuplicatedNameError'))
        this.optionModel.operationButtons[btnIdx].name = this.oldButtonName
      }
    },

    editButtonsColumn () {
      this.showButtonsEditDialog = true
    },

    deleteOperationButton (idx) {
      this.$confirm(this.i18nt('designer.setting.deleteOperationButtonHint'), this.i18nt('render.hint.prompt'), {
        confirmButtonText: this.i18nt('render.hint.confirm'),
        cancelButtonText: this.i18nt('render.hint.cancel')
      }).then(() => {
        this.optionModel.operationButtons.splice(idx, 1)
      }).catch(error => {
        //this.$message.error(error)
      })

    },

    addOperationButton () {
      this.$set(this.optionModel, 'operationButtons', this.optionModel.operationButtons || [])
      this.optionModel.operationButtons.push({
        name: 'btn' + generateId(),
        label: 'new btn',
        type: 'text',
        size: 'small',
        round: false,
        hidden: false,
        disabled: false,
      })
    },

    refreshTableLayout () {
      const dataTableInDesign = this.designer.formWidget.getSelectedWidgetRef()
      if (!!dataTableInDesign && !!dataTableInDesign.refreshLayout) {
        this.$nextTick(() => {
          dataTableInDesign.refreshLayout()
        })
      }
    },

    // 绑定关联字段
    clickBzField (btn) {
      if (!btn.pageId) {
        this.$Message.error('请先绑定表单')
        return false
      }
      if (btn.relationField) { // 已经绑了关联字段
        this.childTableField = btn.childTableField
        this.mainTableFields = btn.mainTableFields
        this.showGlzdDialog = true
      } else {
        this.clickBindField(btn.pageId)
      }
    },
    clickBindField (pageId) {
      // 初始化
      this.childTableField = []
      // 参数
      this.$http
        .get('/api/lowcode/customize/selectDformColumnByPageId/' + pageId)
        .then((res) => {
          for (let item in res.data) {
            let temJson = {
              label: item,
              options: res.data[item],
              data: [{ mainTableField: '', chiTableField: '' }]
            }
            this.childTableField.push(temJson)
          }
          this.getMainTableFields()
        })
        .catch((err) => {
          this.$message.error('获取字段失败！')
        })
    },
    getMainTableFields () {
      // 初始化
      this.mainTableFields = []
      // 参数
      this.$http
        .get('/api/lowcode/customize/selectDformColumnByPageId/' + window.localStorage.getItem('edittingPageId'))
        .then((res) => {
          for (let item in res.data) {
            let temJson = {
              label: item,
              options: res.data[item]
            }
            this.mainTableFields.push(temJson)
          }
          this.showGlzdDialog = true
        })
        .catch((err) => {
          this.$message.error('获取字段失败！')
        })
    },
    addItem (arr) {
      arr.push({ mainTableField: '', chiTableField: '' })
    },
    deleteItem (arr) {
      if (arr.length > 1) {
        arr.pop()
      } else {
        this.$message.error('仅有一条数据不能删除！')
      }
    },
    conFirmBindZd () {
      this.optionModel.operationButtons.map(item => {
        if (item.opeType === '查看更多') {
          let temData = this.childTableField[0].data.map(item => {
            let json = {}
            json.mainTableField = this.mainTableFields[0].label + '.' + item.mainTableField
            json.childTableField = this.childTableField[0].label + '.' + item.chiTableField
            return json
          })
          this.$set(item, 'childTableField', this.childTableField)
          this.$set(item, 'mainTableFields', this.mainTableFields)
          this.$set(item, 'relationField', temData)
        }
      })
      this.showGlzdDialog = false
    }
  }
}
</script>

<style lang="scss" scoped>
li.col-item {
  list-style: none;

  span.col-span-title {
    display: inline-block;
    font-size: 13px;
    width: 120px;
  }

  .col-delete-button {
    margin-left: 6px;
  }
}

.panes-setting {
  ul {
    padding-inline-start: 0;
    padding-left: 0; /* 重置IE11默认样式 */
    margin: 0;
  }

  .drag-option {
    cursor: move;
  }

  li.ghost {
    background: #fff;
    border: 2px dotted $--color-primary;
  }
}

.small-padding-dialog {
  ::v-deep .el-dialog__body {
    padding: 6px 15px 12px 15px;
  }
}

.button-row {
  border-bottom: 1px solid #e1e2e3;
  margin-bottom: 12px;
}

.drag-sort-col {
  padding-top: 8px;
  cursor: move;
}

.containerBox {
  display: flex;
  width: 100%;
  height: 450px;
  flex-wrap: wrap;
  justify-content: space-between;
  overflow-y: auto;
  overflow-x: hidden;

  .tableItemBox {
    width: 100%;
    margin: 15px 0;
  }
}

</style>
