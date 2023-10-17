<template>
  <Modal fullscreen class="newCreateModel" title="新建数据模型" v-model="createModelVisible" :mask-closable="false"
         @on-visible-change="visibleChange" width="1400" @on-cancel="cancel">
    <span class="mTitle">新建数据模型</span>
    <!--    样式改造 -->
    <div class="mine_el_tab">
      <el-tabs :before-leave="beforeLeave" v-model="activeName" @tab-click="handleTabClick">
        <el-tab-pane label="填写基本信息" name='0'>
          <div class="contentContainer">
            <div class="content">
              <div class="step1">
                <div class="topBox">
                  <!--           单表模型 -->
                  <div @click="clicktableType('单表模型')" class="typeItem">
                    <div class="imgBox">
                      <img
                        :src="tableType === '单表模型'?require('../../../../assets/lowcode/dbmxSeleted.svg'):require('../../../../assets/lowcode/dbmx.png')"
                        alt=""
                      >
                      <span v-show="tableType === '单表模型'"></span>
                    </div>
                    <span :style="{ color: tableType === '单表模型' ? '#246AD9' : '' }">单表模型</span>
                  </div>
                  <!--           视图模型 -->
                  <div @click="clicktableType('视图模型')" class="typeItem">
                    <div class="imgBox">
                      <img
                        :src="tableType === '视图模型'?require('../../../../assets/lowcode/stmxSelected.svg'):require('../../../../assets/lowcode/stmx.png')"
                        alt="">
                      <span v-show="tableType === '视图模型'"></span>
                    </div>
                    <span :style="{ color: tableType === '视图模型' ? '#246AD9' : '' }">视图模型</span>
                  </div>
                  <!--           主子表模型 -->
                  <div @click="clicktableType('主子表模型')" class="typeItem">
                    <div class="imgBox">
                      <img
                        :src="tableType === '主子表模型'?require('../../../../assets/lowcode/zzbmxSelected.svg'):require('../../../../assets/lowcode/zzbmx.svg')"
                        alt="">
                      <span v-show="tableType === '主子表模型'"></span>
                    </div>
                    <span :style="{ color: tableType === '主子表模型' ? '#246AD9' : '' }">主子表模型</span>
                  </div>
                  <!--           API模型 -->
                  <div @click="clicktableType('API模型')" class="typeItem">
                    <div class="imgBox">
                      <img
                        :src="tableType === 'API模型'?require('../../../../assets/lowcode/apimxSelected.svg'):require('../../../../assets/lowcode/apimx.svg')"
                        alt="">
                      <span v-show="tableType === 'API模型'"></span>
                    </div>
                    <span :style="{ color: tableType === 'API模型' ? '#246AD9' : '' }">API模型</span>
                  </div>
                </div>
                <div class="bottomBox">
                  <img src="../../../../assets/lowcode/robert.svg" class="robert"/>
                  <div class="descriptionBox">
                    <div class="fengexian"></div>
                    <p style="font-weight: 600; margin-bottom: 15px">{{ tableType }}</p>
                    <span>{{ dataModelDetail }}</span>
                  </div>
                  <div class="modelNameSearch">
                    <Form style="width:100%;padding-top: 25px;: " :model="detailFormData" :label-width="110"
                          :rules="detailFormRules"
                          ref="detailFormRef">
                      <FormItem label="数据模型名称" prop="modelName">
                        <Input v-model="detailFormData.modelName" placeholder="请输入数据模型名称"></Input>
                      </FormItem>
                      <FormItem :label="dbnameLabel" prop="dbname">
                        <Input v-model="detailFormData.dbname" :placeholder="dbnamePlaceholder"></Input>
                      </FormItem>
                      <FormItem :label="dbCommentLabel">
                        <Input v-model="detailFormData.dbComment" :placeholder="dbCommentPlaceholder" type="textarea"
                               :rows="3">
                        </Input>
                      </FormItem>
                      <!--                                        <FormItem label="所属模块" prop="moduleId">-->
                      <!--                                          &lt;!&ndash; <Input v-model="detailFormData.moduleId" disabled></Input> &ndash;&gt;-->
                      <!--                                          <Select v-model="detailFormData.moduleId">-->
                      <!--                                            <Option v-for="item in moduleNameList" :key="item.value" :label="item.label" :value="item.value"></Option>-->
                      <!--                                          </Select>-->
                      <!--                                        </FormItem>-->
                      <!--                      <FormItem v-show="false">-->
                      <!--                        <Button type="primary" @click="handleSubmit('detailFormRef')">提交</Button>-->
                      <!--                      </FormItem>-->
                    </Form>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="编辑数据模型" name='1'>
          <div class="contentContainer">
            <div class="content">
              <div class="step3">
                <div style="padding: 20px 0;display: flex;align-items: center">
                  <el-button v-if="tableData.length < 1" type="primary" size="small" @click="reset(1)"
                             :loading="newLoading">新建
                  </el-button>
                  <span style="margin-right: 25px">是否展示所有默认字段</span>
                  <el-radio-group size="mini" @input="changeSwitchValue" v-model="isShowOtherFields">
                    <el-radio-button :label="true">是</el-radio-button>
                    <el-radio-button :label="false">否</el-radio-button>
                  </el-radio-group>
                </div>
                <el-table :data="tableData" row-key="key" :row-style="{ height: '70px' }" style="width: 100%"
                          :expand-row-keys="expandRows" ref="treeTable"
                          :tree-props="{ children: 'childDto', hasChildren: 'hasChildren' }"
                          @selection-change="handleSelectionChange">
                  <el-table-column align="center" type="selection" width="55"
                                   :selectable="selectAble"></el-table-column>
                  <el-table-column align="center" label="序号" width="80">
                    <template slot-scope="scope">{{ scope.$index + 1 }}</template>
                  </el-table-column>
                  <el-table-column align="center" prop="columnName" label="数据库字段名" width="180"
                                   style="position: relative;">
                    <template v-slot="scope">
                      <el-input v-model="scope.row.columnName" ref="textDom" @blur="blur(scope.row)"
                                style="border-color:red"
                                placeholder="数据库字段名" :disabled="scope.row.isOriginal"></el-input>
                      <div v-show="!scope.row.columnName">
                        <span style="color:red;position: absolute;padding-bottom: 20px;">数据库字段名不能为空</span>
                      </div>
                      <div v-show="scope.row.columnName && scope.row.inValid">
                        <span style="color:red;position: absolute;padding-bottom: 20px;">数据库表名或字段名重复</span>
                      </div>
                      <div v-show="mgc.indexOf(scope.row.columnName) !== -1">
                <span style="color:red;position: absolute;padding-bottom: 20px;">{{
                    scope.row.columnName
                  }}与数据库系统字段重复</span>
                      </div>
                      <div
                        v-show="scope.row.columnName && scope.row.columnType === 25 && !(/^[A-Za-z][A-Za-z_0-9]+$/.test(scope.row.columnName))">
                        <span style="color:red;position: absolute;padding-bottom: 20px;">以字母开头,后跟数字,字母或下划线</span>
                      </div>
                      <div
                        v-show="scope.row.columnName && scope.row.columnType === 25 && (/,*[A-Z]+.*/.test(scope.row.columnName))">
                        <span style="color:red;position: absolute;padding-bottom: 20px;">表名不能含有大写字母！</span>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column align="center" prop="columnComment" label="字段名称" style="width:auto">
                    <template v-slot="scope">
                      <el-input v-model="scope.row.columnComment" placeholder="字段名称" :disabled="scope.row.isOriginal">
                      </el-input>
                    </template>
                  </el-table-column>
                  <el-table-column align="center" prop="columnType" label="字段类型" style="width:auto;position: relative;">
                    <template v-slot="scope">
                      <Select v-model="scope.row.columnType" placeholder="请选择" transfer
                              :disabled="scope.row.isOriginal">
                        <Option v-for="(item, index) in columnTypeArr" :value="item.typeNum" :key="index"
                                @click.native="sonTable(scope.row)">{{ item.typeName }}
                        </Option>
                      </Select>
                      <div v-show="!scope.row.columnType">
                        <span style="color:red;position: absolute;">字段类型不能为空</span>
                      </div>
                      <div
                        v-show="scope.row.parentId && scope.row.columnType && scope.row.isFk === '1' && scope.row.columnType !== 5">
                        <span style="color:red;position: absolute;">必须为bigint类型</span>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column align="center" label="字段长度" prop="columnLength" style="width:auto">
                    <template v-slot="scope">
                      <Input type="number" v-model="scope.row.columnLength" placeholder="字段长度"
                             :disabled="scope.row.isOriginal"></Input>
                    </template>
                  </el-table-column>
                  <el-table-column align="center" label="小数点" prop="pointLength" style="width:auto">
                    <template v-slot="scope">
                      <Input type="number" v-model="scope.row.pointLength" placeholder="小数点位数"
                             :disabled="scope.row.isOriginal"></Input>
                      <div
                        v-show="(scope.row.columnType === 6 || scope.row.columnType===7 || scope.row.columnType===8) && (scope.row.pointLength<1)">
                        <span style="color:red;position: absolute;padding-bottom: 20px;">不能小于1！</span>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column align="center" label="是否为空" prop="isNull" style="width:auto">
                    <template v-slot="scope">
                      <!--                      <RadioGroup v-model="scope.row.isNull">-->
                      <!--                        <Radio label="1" :disabled="scope.row.isOriginal">是</Radio>-->
                      <!--                        <Radio label="0" :disabled="scope.row.isOriginal">否</Radio>-->
                      <!--                      </RadioGroup>-->
                      <el-radio-group size="mini" v-model="scope.row.isNull">
                        <el-radio-button :disabled="scope.row.isOriginal" label="1">是</el-radio-button>
                        <el-radio-button :disabled="scope.row.isOriginal" label="0">否</el-radio-button>
                      </el-radio-group>
                    </template>
                  </el-table-column>
                  <el-table-column align="center" label="是否为主键" prop="isPk" style="width:auto">
                    <template v-slot="scope">
                      <!--                      <RadioGroup v-model="scope.row.isPk">-->
                      <!--                        <Radio label="1" :disabled="scope.row.isOriginal">是</Radio>-->
                      <!--                        <Radio label="0" :disabled="scope.row.isOriginal">否</Radio>-->
                      <!--                      </RadioGroup>-->
                      <el-radio-group size="mini" v-model="scope.row.isPk">
                        <el-radio-button :disabled="scope.row.isOriginal" label="1">是</el-radio-button>
                        <el-radio-button :disabled="scope.row.isOriginal" label="0">否</el-radio-button>
                      </el-radio-group>
                      <div v-show="scope.row.isPk === '1' && isShowIsPkError && !scope.row.isOriginal">
                        <span style="color:red;position: absolute;padding-bottom: 20px;">只能有一个主键</span>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column align="center" label="是否为业务主键" prop="isBk" width="180">
                    <template v-slot="scope">
                      <!--                      <RadioGroup v-model="scope.row.isBk">-->
                      <!--                        <Radio label="1" :disabled="scope.row.isOriginal">是</Radio>-->
                      <!--                        <Radio label="0" :disabled="scope.row.isOriginal">否</Radio>-->
                      <!--                      </RadioGroup>-->
                      <el-radio-group size="mini" v-model="scope.row.isBk">
                        <el-radio-button :disabled="scope.row.isOriginal" label="1">是</el-radio-button>
                        <el-radio-button :disabled="scope.row.isOriginal" label="0">否</el-radio-button>
                      </el-radio-group>
                    </template>
                  </el-table-column>
                  <el-table-column align="center" prop="columnRelation" label="关联主表字段"
                                   style="width:auto;position: relative;">
                    <template v-slot="scope">
                      <Select v-model="scope.row.columnRelation" placeholder="请选择" transfer
                              :disabled="!scope.row.parentId || scope.row.isOriginal">
                        <template v-for="(item, index) in mainTableColumn">
                          <Option v-if="item.columnType == scope.row.columnType" :value="item.columnName" :key="index">
                            {{ item.columnName }}
                          </Option>
                        </template>
                      </Select>
                    </template>
                  </el-table-column>
                  <el-table-column align="center" prop="relateTable" label="关联数据表">
                    <template v-slot="scope">
                      <el-select
                        @visible-change="relateTableVisibleChange(scope.row.columnName)"
                        v-model="scope.row.relateTable"
                        placeholder="请选择"
                        :disabled="scope.row.columnType !== 25"
                      >
                        <el-option
                          v-for="(item,index) in relationDbName"
                          :key='index + "hbw"'
                          :value="item"
                          :label="item">
                        </el-option>
                      </el-select>
                    </template>
                  </el-table-column>
                  <el-table-column align="center" prop="relateType" label="关联类型">
                    <template v-slot="scope">
                      <Select v-model="scope.row.relateType" placeholder="请选择" transfer
                              :disabled="scope.row.columnType !== 25">
                        <Option value="left" key="左关联">左关联</Option>
                        <Option value="right" key="右关联">右关联</Option>
                        <Option value="inner" key="内关联">内关联</Option>
                        <Option value="outer" key="外关联">外关联</Option>
                      </Select>
                    </template>
                  </el-table-column>
                  <el-table-column align="center" label="操作" width="60">
                    <template v-slot="scope">
                      <el-button size="mini" type="text" @click="deleteTable(scope.row)"
                                 :disabled="scope.row.isOriginal">删除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="btnBox">
                  <el-button v-if="tableType === '主子表模型' && selectedRowData.length === 1 && selectedRowData[0].childDto"
                             style="width: 100%;margin: 10px 0;background-color: #246AD9;"
                             @click="newRowData(1)"
                             icon="el-icon-plus" type="primary">
                    新建子集
                  </el-button>
                  <el-button style="width: 100%;margin-left: 0" @click="newRowData(0)" icon="el-icon-plus" plain
                             type="primary">新建
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div slot="footer">
      <Button type="primary" @click="prev" v-show="activeName === '1'" style="width: 72px">上一步</Button>
      <Button type="primary" @click="next" v-show="activeName === '0'" style="width: 72px">下一步</Button>
      <Button type="primary" @click="passData" :loading="createLoading" v-show="activeName === '1'"
              style="width: 72px">创建
      </Button>
      <Button type="default" @click="cancel" style="width: 72px">取消
      </Button>
    </div>
  </Modal>
</template>

<script>
export default {
  name: 'createModel',
  props: ['createModelDialogVisible', 'moduleIdName', 'moduleNameList', 'moduleParam'],
  watch: {
    createModelDialogVisible(newVal) {
      this.createModelVisible = newVal
    },
    moduleIdName(newVal) {
      this.tempData = newVal
      this.detailFormData.moduleId = this.tempData
    },
    current(newVal) {
      if (newVal === 1) {
        this.reset(1)
      }
    }
  },
  mounted() {
    this.detailFormData.moduleId = this.moduleParam.moduleId
  },
  created() {
    this.getDefaultLinkData()

    // 第三步
    // 1为新建 2为编辑 this.treeArr为编辑的数据
    this.getColumnType()
    if (this.$util.Setting.dataBankType === '0') {
      this.mgc = this.$util.Setting.columnNamesql
    } else if (this.$util.Setting.dataBankType === '1' || this.$util.Setting.dataBankType === '2') {
      this.mgc = this.$util.Setting.columnNamegs
    }
  },
  directives: {
    // 注册一个局部的自定义指令 v-focus
    focus: {
      // 指令的定义
      inserted(el) {
        // 聚焦元素
        if (el.querySelector('input')) {
          el.querySelector('input').focus()
        }
      },
    },
  },
  data() {
    let validateModelName = (rule, value, callback) => {
      // 验证数据模型名称是否重复
      const [url] = [
        `/api/cscpCustomizeModels?modelName.equals=${value}&page=1&size=10`,
      ]
      this.$http
        .get(url)
        .then((response) => {
          if (response.data.data.length > 0) {
            callback(new Error('数据模型名称已重复'))
          } else {
            callback()
          }
        })
        .catch(() => {
          callback(new Error('异步校验出错！'))
        })
    }
    let validateDbName = (rule, value, callback) => {
      let pattern = /^[A-Za-z][A-Za-z_0-9]+$/
      let reg = /,*[A-Z]+.*/
      if (value.length > 30) {
        callback(new Error('长度不可以超过30位'))
      } else if (!pattern.test(value)) {
        callback(new Error('以字母开头，后边可以为数字、字母或下划线'))
      } else if (reg.test(value)) {
        callback(new Error('表名不能含有大写字母！'))
      } else {
        const [url] = [
          `/api/lowcode/modelDesign/validation/cscpHxFormTable?name=${value}`,
        ]
        this.$http
          .get(url)
          .then((response) => {
            if (response.data?.code === 200) {
              callback()
            }
          })
          .catch((err) => {
            if (err?.response?.status === 400) {
              callback(new Error('表名已重复'))
              return
            }
            callback(new Error('异步校验出错！'))
          })
      }
    }
    return {
      selectedRowData: [],
      selectedTRow: {},
      activeName: '0',
      isShowIsPkError: false,
      filterData: [],
      originData: [],
      isShowOtherFields: true,
      createLoading: false,
      tableType: '单表模型', //  单表模型 视图模型 主子表模型
      dataModelDetail: '支持无需审批的简单业务的增加、删除、修改、查询，仅支持包含单个数据库表，如日报填报、健康行程上报等业务都可使用单表模型',
      checkColumnNameCopy: '1',
      expandRows: [],
      // key(row) {
      //   return row.id
      // },
      closeFlag: false,
      // 编辑数据模型
      tableData: [],
      columnTypeArr: [],
      operateType: '',
      mainTableId: null,
      childTableId: [],
      newLoading: false,
      formData: {
        dbType: '1',
      },
      current: 0,
      createModelVisible: false,
      tempData: '',
      detailFormData: {
        modelName: '',
        // id: "",
        dbname: '',
        dbComment: '',
        dbMainName: '',
        dbMainComment: '',
        moduleId: '',
      },
      mainTableColumn: [],
      options: [],
      detailFormRules: {
        modelName: [
          {required: true, message: '数据模型名称不能为空', trigger: 'blur'},
          {required: true, validator: validateModelName, trigger: 'blur'},
        ],
        dbname: [
          {required: true, message: '数据库表名不能为空', trigger: 'blur'},
          {validator: validateDbName, trigger: 'blur'},
        ]
      },
      dbnameLabel: '数据库表名',
      dbCommentLabel: '数据库表注释',
      dbnamePlaceholder: '请输入数据库表名',
      dbCommentPlaceholder: '请输入数据库表注释',
      relationDbName: []
    }
  },
  methods: {
    newRowData(flag) {
      if (flag) {
        // 添加子表字段
        if (this.selectedTRow.childDto) {
          let row = this.selectedTRow.childDto[this.selectedTRow.childDto.length - 1]
          this.peerTable(row)
        } else {
          this.$Message.error('请勾选table类型数据！')
        }
      } else {
        // 添加主表字段
        let row = this.tableData[this.tableData.length - 1]
        this.peerTable(row)
      }
    },
    beforeLeave() {
      // if (this.activeName === '0') {
      //   return this.handleSubmit('detailFormRef')
      // }
    },
    handleTabClick() {
      return false
    },
    relateTableVisibleChange(val) {
      this.relationDbName = []
      let newArr = this.tableData.filter(item => {
        return item.columnType === 25 && item.columnName !== val
      })
      newArr.forEach(item => {
        item.columnName ? this.relationDbName.push(item.columnName) : ''
      })
      this.relationDbName.push(this.detailFormData.dbname) // 主表
    },
    dealWithTableData(tableData) {
      const temp = tableData.filter(item => {
        if (item.childDto && item.childDto.length > 0) {
          const childDtoTemp = item.childDto.filter(child => child.isShow)
          item.childDto = childDtoTemp
        }
        if (item.isShow) {
          return item
        }
      })
      return temp
    },
    changeSwitchValue(isShowOtherFields) {
      this.tableData = JSON.parse(JSON.stringify(this.originData))
      this.filterData = this.dealWithTableData(JSON.parse(JSON.stringify(this.tableData)))
      if (isShowOtherFields) {
        this.tableData = this.tableData
      } else {
        this.tableData = this.filterData
      }
    },
    // 鼠标 键盘事件
    clicktableType(str) {
      this.tableType = str
      if (str === '单表模型') {
        this.dbTypeChange('1')
        this.dataModelDetail = '支持无需审批的简单业务的增加、删除、修改、查询，仅支持包含单个数据库表，如日报填报、健康行程上报等业务都可使用单表模型'
      } else if (str === '主子表模型') {
        this.dbTypeChange('2')
        this.dataModelDetail = '支持无需审批的复杂业务的增加、删除、修改、查询，可包含多个数据库表，如客户档案维护、班级学生等业务都可使用主子表模型'
      }
    },
    // 第三步
    // 新建 or 编辑
    reset(type) {
      this.tableData = []
      this.newLoading = true

      if (type === 1) { // 新建
        this.tableData = [
          {
            id: Math.random() + '',
            key: Math.random() + '',
            columnName: 'id',
            columnComment: '主键id',
            columnType: 5,
            columnLength: null,
            pointLength: null,
            isNull: '0',
            isPk: '1',
            isFk: '0',
            showInput: true,
            inValid: false,
            isOriginal: true,
            isShow: true,
            isBk: '0',
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            columnName: 'tenant_id',
            columnComment: '租户id',
            columnType: 5,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            showInput: true,
            inValid: false,
            isOriginal: true,
            isShow: false,
            isBk: '0',
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            columnName: 'created_time',
            columnComment: '创建时间',
            columnType: 12,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            showInput: true,
            inValid: false,
            isOriginal: true,
            isShow: false,
            isBk: '0',
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            columnName: 'created_userid',
            columnComment: '创建用户',
            columnType: 5,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            showInput: true,
            inValid: false,
            isOriginal: true,
            isShow: false,
            isBk: '0',
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            columnName: 'updated_time',
            columnComment: '更新时间',
            columnType: 12,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            showInput: true,
            inValid: false,
            isOriginal: true,
            isShow: false,
            isBk: '0',
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            columnName: 'updated_userid',
            columnComment: '更新人',
            columnType: 5,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            showInput: true,
            inValid: false,
            isOriginal: true,
            isShow: false,
            isBk: '0',
          }
        ]
        this.tableData = this.flatten(this.tableData, 1)
      }
      if (type === 2) {
        if (this.treeArr.length > 0) {
          this.tableData = this.treeArr
        } else {
          this.tableData = [
            {
              id: Math.random() + '',
              key: Math.random() + '',
              columnName: '',
              columnComment: '',
              columnType: '',
              columnLength: null,
              pointLength: null,
              isNull: '1',
              isPk: '0',
              isFk: '0',
              showInput: true,
              inValid: false,
              isOriginal: false,
              isShow: true,
              isBk: '0',
            },
          ]
        }
        this.tableData = this.flatten(this.tableData, 2)
      }

      this.tableSelected = {}
      this.faTable = {}
      this.tableData.forEach((item) => {
        item.first = 1
      })
      this.originData = this.tableData
      this.newLoading = false
    },
    // 获取列表并保存数据
    passDataSecondStep(params) {
      // 最终数据+++++++++++++++++++++++++++++++++++
      //主表数据加工
      var result = {
        'columns': [],
        'cscpHxFormTables': [],
        'subTableFkName': '',
        'subTableName': '',
        'tableContent': '',
        'tableName': '',
        'tableId': null,
        'formType': '0', //默认单表
      }
      var datas = this.isShowOtherFields ? this.tableData : this.originData
      //子表名用逗号拼接
      var subTableFkName = ''
      //子表外键用逗号拼接
      var subTableName = ''
      //主表字段列表
      var mainColumnArr = []
      //字段是否合法标识
      var validDataFlag = true
      //表名称
      var tableNames = []
      //主表名称放入
      tableNames.push(this.detailFormData.dbname)
      //错误信息提示
      var errMessage = ''
      //主表字段名称
      var mainClumnNames = []
      for (let i = 0; i < datas.length; i++) {
        if (!datas[i].childDto || datas[i].childDto.length == 0) {
          //是主表字段，非子表进行加工
          var columns = {
            'columnComment': datas[i].columnComment.trim(),
            'columnLength': datas[i].columnLength,
            'pointLength': datas[i].pointLength,
            'columnName': datas[i].columnName.trim(),
            'columnType': datas[i].columnType,
            'isNull': datas[i].isNull,
            'isPk': datas[i].isPk,
            'isFk': datas[i].isFk,
            'isBk': datas[i].isBk,
          }
          //校验表明及字段类型是否为空
          if (!datas[i].columnName || !datas[i].columnType) {
            validDataFlag = false
            errMessage = '主表字段名称及字段类型不能为空'
            break
          }
          //字段名称与数据库系统
          if (this.mgc.indexOf(datas[i].columnName) !== -1) {
            validDataFlag = false
            errMessage = '字段名称与数据库系统字段不能重复'
            break
          }
          //校验主表字段名称是否重复
          if (datas[i].columnType != 25 && mainClumnNames.indexOf(datas[i].columnName) > -1) {
            validDataFlag = false
            errMessage = '主表字段名称不能重复'
            break
          }
          if ((datas[i].columnType === 6 || datas[i].columnType === 7 || datas[i].columnType === 8) && (datas[i].pointLength < 1)) {
            validDataFlag = false
            errMessage = '小数点位数不能小于1！'
            break
          }
          mainClumnNames.push(datas[i].columnName)
          mainColumnArr[i] = columns
        } else {
          //如果含有子表进行子表数据加工
          var childResult = {
            'columns': [],
            'cscpHxFormTables': [],
            'subTableFkName': '',
            'subTableName': '',
            'tableContent': '',
            'tableName': '',
            'tableId': null
          }
          var childColumnArr = []
          if (tableNames.indexOf(datas[i].columnName) > -1) {
            validDataFlag = false
            errMessage = '主子表之间、子表之间表名不能重复'
            break
          }
          if (!datas[i].columnName && datas[i].columnType == 25) {
            validDataFlag = false
            errMessage = '主表字段名称及字段类型不能为空'
            break
          }
          if (datas[i].columnName && datas[i].columnType === 25 && !(/^[A-Za-z][A-Za-z_0-9]+$/.test(datas[i].columnName))) {
            validDataFlag = false
            errMessage = '数据表名必须以字母开头,后跟数字,字母或下划线'
            break
          }
          if (datas[i].columnName && datas[i].columnType === 25 && (/,*[A-Z]+.*/.test(datas[i].columnName))) {
            validDataFlag = false
            errMessage = '表名不能含有大写字母！'
            break
          }
          if ((datas[i].columnType === 6 || datas[i].columnType === 7 || datas[i].columnType === 8) && (datas[i].pointLength < 1)) {
            validDataFlag = false
            errMessage = '小数点位数不能小于1！'
            break
          }
          //每个子表外键个数校验
          let fkcount = 0
          //子表字段名称 用于校验子表字段是否重复
          var childClumnNames = []
          for (let j = 0; j < datas[i].childDto.length; j++) {
            var childColumns = {
              'columnComment': datas[i].childDto[j].columnComment,
              'columnLength': datas[i].childDto[j].columnLength,
              'columnName': datas[i].childDto[j].columnName,
              'pointLength': datas[i].childDto[j].pointLength,
              'columnType': datas[i].childDto[j].columnType,
              'isNull': datas[i].childDto[j].isNull,
              'isPk': datas[i].childDto[j].isPk,
              'isFk': datas[i].childDto[j].isFk,
              'isBk': datas[i].childDto[j].isBk,
              'columnRelation': datas[i].childDto[j].columnRelation,
            }
            if (datas[i].childDto[j].columnRelation) {
              fkcount++
            }
            // if (fkcount > 1) {
            //   validDataFlag = false;
            //   errMessage = "子表关联主表id的外键只能有一个";
            // }
            if (!datas[i].childDto[j].columnName || !datas[i].childDto[j].columnType) {
              validDataFlag = false
              errMessage = '子字段名称及字段类型不能为空'
              break
            }
            //校验子表字段名称是否重复
            if (childClumnNames.indexOf(datas[i].childDto[j].columnName) > -1) {
              validDataFlag = false
              errMessage = '子表字段名称不能重复'
              break
            }
            //校验子表字段如果是外键,字段类型必须为bigint
            // if (datas[i].childDto[j].isFk === '1') {
            //   if (datas[i].childDto[j].columnType !== 5) {
            //     validDataFlag = false;
            //     errMessage = "子字段为外键时字段类型必须为bigint";
            //     break;
            //   }
            // }
            childClumnNames.push(datas[i].childDto[j].columnName)
            // 如果含有外键 拼接 外键字段 及表名
            if (datas[i].childDto[j].isFk === '1') {
              if (subTableFkName && subTableFkName.length > 0) {
                subTableFkName = subTableFkName.concat(',' + datas[i].childDto[j].columnName)
                subTableName = subTableName.concat(',' + datas[i].columnName)
              } else {
                subTableFkName = subTableFkName.concat(datas[i].childDto[j].columnName)
                subTableName = subTableName.concat(datas[i].columnName)
              }
            }
            childColumnArr[j] = childColumns
          }
          if (fkcount == 0) {
            validDataFlag = false
            errMessage = '请设置主子表关联字段'
          }
          childResult.columns = childColumnArr
          tableNames.push(datas[i].columnName)
          //子表名
          childResult.tableName = datas[i].columnName
          //子表解释说明
          childResult.tableContent = datas[i].columnComment
          //子表tableId
          if (this.childTableId.length > 0) {
            for (let k = 0; k < this.childTableId.length; k++) {
              if (this.childTableId[k].tableName == datas[i].columnName) {
                childResult.tableId = this.childTableId[k].tableId
              }
            }
          }
          //子表列表
          result.cscpHxFormTables[result.cscpHxFormTables.length] = childResult
          //如果字符非法跳出大循环
          if (!validDataFlag) {
            break
          }
        }
      }
      // 校验一张子表只能有一个主键
      if (result.cscpHxFormTables && result.cscpHxFormTables.length > 0) {
        for (let i = 0; i < result.cscpHxFormTables.length; i++) {
          let tempArr = result.cscpHxFormTables[i].columns
          if (tempArr.filter(item => item.isPk === '1').length > 1) {
            validDataFlag = false
            errMessage = '一张子表只能包含一个主键'
            this.isShowIsPkError = true
            break
          }
        }
      }

      //字段类型和字段名是否为空
      if (validDataFlag) {
        result.columns = mainColumnArr.filter(item => item)
        if (result.columns && result.columns.length > 0) {
          let isPkArr = result.columns.filter(item => item.isPk === '1')
          if (isPkArr.length > 1) {
            this.$Message.error('一张表只能包含一个主键')
            this.createLoading = false
            this.isShowIsPkError = true
            return
          }
        }
        result.subTableFkName = subTableFkName
        result.subTableName = subTableName
        if (result.subTableName && result.subTable !== '') {
          result.formType = '1'
        } else {
          result.formType = '0'
        }
        //主表名
        // 修改
        result.tableName = this.detailFormData.dbname
        //主表tableId
        if (this.mainTableId && this.mainTableId != null) {
          result.tableId = this.mainTableId
        }
        //主表解释说明
        // 修改
        result.tableContent = this.detailFormData.dbComment
        // 验证子表名是否已存在
        const subTableNameArr = result.subTableName.split(',')
        const url = '/api/lowcode/modelDesign/validation/cscpHxFormTable?name='
        let promiseArr = []
        if (subTableNameArr && subTableNameArr.length) {
          for (let i = 0; i < subTableNameArr.length; i++) {
            if (subTableNameArr[i] && subTableNameArr[i] !== '') {
              promiseArr.push(this.$http.get(url + subTableNameArr[i]))
            }

          }
        }
        if (this.formData.dbType !== '1') {
          if (result.cscpHxFormTables.length === 0) {
            this.$Message.error('未设置子表！')
            this.createLoading = false
            return
          }
        }
        Promise.all(promiseArr).then(() => {
          // this.$Message.success('表名校验成功')
          // operateType:  "1" 为修改  其他为新增
          if (this.operateType == '1') {
            this.$http
              .put('/api/lowcode/modelDesign/cscpHxFormTables', result)
              .then((res) => {
                this.closeFlag = true
                this.$parent.createModelDialogVisible = false
                // 重置数据
                this.$refs.formRef.resetFields()
                this.$refs.detailFormRef.resetFields()
                this.tableData = []
                this.current = 0
                this.selectedTableItem = []
                this.$Message.success({
                  background: true,
                  content: '修改成功'
                })
                this.createLoading = false
                this.selectedTableItem = []
                this.$parent.queryList(true)
                // this.getColumns(this.curTableName)
              })
              .catch(() => {
                this.createLoading = false
                this.$Message.error({
                  background: true,
                  content: '修改失败'
                })
              })
          } else {
            this.$http.post('/api/cscpCustomizeModels', params).then(res => {
              if (res.data) {
                // this.$Message.success('基本信息保存成功');
                // 上面先提交第二步的基本信息,只有等成功,才能提交第三步的数据
                this.$http
                  .post('/api/lowcode/modelDesign/cscpHxFormTables', result)
                  .then(() => {
                    this.closeFlag = true
                    // 重置数据
                    this.createLoading = false
                    this.isShowOtherFields = true
                    this.$Message.success({
                      background: true,
                      content: '添加成功'
                    })
                    // this.$refs.formRef.resetFields()
                    this.$refs.detailFormRef.resetFields()
                    this.tableData = []
                    this.current = 0
                    this.selectedTableItem = []
                    this.$parent.createModelDialogVisible = false
                    this.$parent.queryList(true)
                  })
                  .catch(() => {
                    this.$http
                      .delete('api/cscpCustomizeModels/' + res.data.modelId)
                      .then((res) => {
                      })
                      .catch(() => {
                        this.createLoading = false
                      })
                    this.createLoading = false
                    this.$Message.error({
                      background: true,
                      content: '添加失败'
                    })
                  })
                return
              }
            }).catch(() => {
              this.createLoading = false
              this.$Message.error('基本信息保存失败')
              return
            })

          }
        }).catch(() => {
          this.createLoading = false
          this.$Message.error('子表名已重复')
          return
        })
      } else {
        this.createLoading = false
        // this.$Message.error(errMessage);
        this.$message({message: errMessage, type: 'error', customClass: 'war-message'})
      }
    },
    // 删除名称为空的数据
    nullFlatten(arr) {
      arr.map((item) => {
        if (item.childDto !== undefined) {
          for (let i = 0; i < item.childDto.length; i++) {
            if (item.childDto[i].branchName === '') {
              item.childDto = item.childDto.filter((items) => {
                return items.id !== item.childDto[i].id
              })
            }
          }
        }
        if (Array.isArray(item.childDto)) {
          this.nullFlatten(item.childDto)
        }
      })
      return arr
    },
    // 名称不为空切失去焦点时隐藏input
    inputBlur(val) {
      if (val.branchName) {
        val.showInput = false
      }
      // val.showInput = false;
    },
    // 点击编辑名称
    showInput(val) {
      val.showInput = !val.showInput
    },
    // 是否可选
    selectAble(row) {
      // // 给数据手动添加一个新字段 status 默认为1， 如果需要设置disbaled； status 变为0
      // if (row.status === 1) {
      //   return true
      // }
      return true
    },
    // 选中方法
    handleSelectionChange(val) {
      this.selectedRowData = val
      if (val.length > 0) {
        this.selectedTRow = val[0]
        this.tableSelected = val[0]
        this.tableData = this.flattenId(this.tableData, val[0].id)
        this.faTable = val[0]
      } else {
        this.tableSelected = {}
        this.faTable = {}
        this.tableData = this.flatten(this.tableData, 1)
      }
    },
    // 选中状态
    flatten(arr, type) {
      arr.map((item) => {
        this.$set(item, 'status', 1)
        this.$set(item, 'key', Math.random() + '')
        if (type === 1 && item.branchName === '') {
          this.$set(item, 'showInput', true)
        } else {
          this.$set(item, 'showInput', false)
        }
        if (Array.isArray(item.childDto)) {
          this.flatten(item.childDto, type)
        }
      })
      return arr
    },
    // 修改选中其他状态
    flattenId(arr, id) {
      arr.map((item) => {
        if (item.id === id) {
          this.$set(item, 'status', 1)
        } else {
          this.$set(item, 'status', 0)
        }
        if (Array.isArray(item.childDto)) {
          this.flattenId(item.childDto, id)
        }
      })
      return arr
    },
    //
    blur(rowData) {
      if (this.checkColumnName !== rowData.columnName) {
        this.checkColumnName = rowData.columnName
      }
      if (rowData.columnType === 25) {
        this.$http.get(`api/lowcode/modelDesign/validation/cscpHxFormTable?name=${this.checkColumnName}`).then((response) => {
          if (response) {
            // rowData.inValid = false;
          }
        }).catch((err) => {
          if (err?.response?.data?.detail) {
            // this.$message.error(err.response.data.detail)
            rowData.inValid = true
            // this.checkColumnNameCopy = this.checkColumnName
          }
        })
      }
      var datas = this.tableData
      //表名称
      var tableNames = []
      //主表名称放入
      tableNames.push(this.detailFormData.dbname)
      //主表字段名称
      var mainClumnNames = []
      //是否是否合法标识
      var validDataFlag = true
      for (let i = 0; i < datas.length; i++) {
        if (!datas[i].childDto || datas[i].childDto.length == 0) {
          //校验主表字段名称是否重复
          if (datas[i].columnType != 25 && datas[i].columnName && datas[i].columnName != '') {
            if (mainClumnNames.indexOf(datas[i].columnName) > -1) {
              // this.checkColumnNameCopy = this.checkColumnName
              rowData.inValid = true
              validDataFlag = false
              break
            }
            mainClumnNames.push(datas[i].columnName)
          }
        } else {
          //子表字段名称 用于校验子表字段是否重复
          var childClumnNames = []
          if (rowData.columnType === 25 && tableNames.indexOf(datas[i].columnName) > -1) {
            // this.checkColumnNameCopy = this.checkColumnName
            // debugger
            rowData.inValid = true
            validDataFlag = false
            break
          }
          tableNames.push(datas[i].columnName)
          for (let j = 0; j < datas[i].childDto.length; j++) {
            //校验子表字段名称是否重复
            if (rowData.parentId == datas[i].childDto[j].parentId) {
              if (datas[i].childDto[j].columnName &&
                datas[i].childDto[j].columnName != '' &&
                childClumnNames.indexOf(datas[i].childDto[j].columnName) > -1) {
                // this.checkColumnNameCopy = this.checkColumnName
                rowData.inValid = true
                validDataFlag = false
                break
              }
              childClumnNames.push(datas[i].childDto[j].columnName)
            }
          }
          //如果字符非法跳出大循环
          if (!validDataFlag) {
            break
          }
        }
      }
      if (validDataFlag) {
        rowData.inValid = false
      }
    },
    // 新建子节点
    sonTable(row) {
      //如果是子节点新建联动带出主表新增字段做为关联字段选项 columnRelation
      if (row.parentId) {
        this.mainTableColumn = []
        this.tableData.map((item) => {
          if (item && item.columnType != 25
            && (!item.isOriginal || item.columnName == 'id')
            //  && item.columnType == row.columnType
          ) {
            this.mainTableColumn.push(item)
          }
        })
      }
      this.checkColumnName = row.columnName
      if (row.columnType === 25 && this.checkColumnName) {
        this.blur(row)
      }
      if (row.columnType == 25) {
        let arr = [
          {
            id: Math.random() + '',
            key: Math.random() + '',
            parentId: row.id,
            columnName: 'id',
            columnComment: '主键id',
            columnType: 5,
            columnLength: null,
            pointLength: null,
            isNull: '0',
            isPk: '1',
            isFk: '0',
            showInput: true,
            inValid: false,
            isOriginal: true,
            isShow: true,
            isBk: '0',
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            parentId: row.id,
            columnName: 'tenant_id',
            columnComment: '租户id',
            columnType: 5,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            showInput: true,
            inValid: false,
            isOriginal: true,
            isShow: false,
            isBk: '0',
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            parentId: row.id,
            columnName: 'created_time',
            columnComment: '创建时间',
            columnType: 12,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            showInput: true,
            inValid: false,
            isOriginal: true,
            isShow: false,
            isBk: '0',
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            parentId: row.id,
            columnName: 'created_userid',
            columnComment: '创建用户',
            columnType: 5,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            showInput: true,
            inValid: false,
            isOriginal: true,
            isShow: false,
            isBk: '0',
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            parentId: row.id,
            columnName: 'updated_time',
            columnComment: '更新时间',
            columnType: 12,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            showInput: true,
            inValid: false,
            isOriginal: true,
            isShow: false,
            isBk: '0',
          },
          {
            id: Math.random() + '',
            key: Math.random() + '',
            parentId: row.id,
            columnName: 'updated_userid',
            columnComment: '更新人',
            columnType: 5,
            columnLength: null,
            pointLength: null,
            isNull: '1',
            isPk: '0',
            isFk: '0',
            showInput: true,
            inValid: false,
            isOriginal: true,
            isShow: false,
            isBk: '0',
          }
        ]
        this.$set(row, 'childDto', arr)
      } else {
        this.$set(row, 'childDto', [])
      }
    },
    // 新建同级节点
    peerTable(row) {
      // 只能在展示所有列表字段的时候新建
      if (!this.isShowOtherFields) {
        this.$message.error('请在展示所有默认字段的情况下新建字段')
        return
      }
      this.checkColumnName = ''
      var fatherId = null
      if (row) {
        this.tableSelected = row
        if (row.parentId) {
          fatherId = row.parentId
        }
        this.tableData = this.flattenId(this.tableData, row.id)
        this.faTable = row
      } else {
        this.tableSelected = {}
        this.faTable = {}
        this.tableData = this.flatten(this.tableData, 1)
      }
      this.peerLoading = true
      let obj = {
        id: Math.random() + '',
        key: Math.random() + '',
        columnName: '',
        columnComment: '',
        columnType: '',
        columnLength: null,
        pointLength: null,
        isNull: '1',
        isPk: '0',
        isFk: '0',
        isBk: '0',
        showInput: true,
        first: 1,
        inValid: false,
        isOriginal: false,
        isShow: true,
      }
      if (JSON.stringify(this.faTable) !== '{}') {
        if (this.faTable.first === 1) {
          this.tableData.forEach((item) => {
            if (item.status === 1) {
              this.tableData.push(obj)
            }
          })
        } else {
          fatherId = fatherId == null ? this.faTable.id : fatherId
          this.tableData = this.sonFlatten(this.tableData, this.faTable.id, fatherId)
        }
      }
      this.originData = this.tableData
      this.peerLoading = false
    },
    // 子项新建同级节点
    sonFlatten(arr, id, fatherId) {
      let obj = {
        id: Math.random() + '',
        key: Math.random() + '',
        parentId: fatherId,
        branchName: '',
        showInput: true,
        inValid: false,
        isNull: '1',
        isPk: '0',
        isFk: '0',
        isBk: '0',
        isOriginal: false,
        isShow: true,
      }
      arr.map((item) => {
        if (item.childDto !== undefined && item.childDto !== null) {
          for (let i = 0; i < item.childDto.length; i++) {
            if (item.childDto[i].id === id) {
              item.childDto.push(obj)
            }
          }
        }
        if (Array.isArray(item.childDto)) {
          this.sonFlatten(item.childDto, id, fatherId)
        }
      })
      return arr
    },
    // 删除
    deleteTable(val) {
      //如果删除的是父节点字段，子节点关联字段清除
      if (!val.parentId) {
        // var tempTableData = [];
        this.tableData.map(item => {
          if (item.childDto) {
            item.childDto.map(item1 => {
              if (item1.columnRelation == val.columnName) {
                item1.columnRelation = null
              }
            })
          }
        })
        var tempMainTableColumn = []
        this.mainTableColumn.map(item => {
          if (item.columnName != val.columnName) {
            tempMainTableColumn.push(item)
          }
        })
        this.mainTableColumn = tempMainTableColumn
      }
      // 只能在展示所有列表字段的时候删除
      if (!this.isShowOtherFields) {
        this.$message.error('请在展示所有默认字段的情况下删除字段')
        return
      }
      if (val.first === 1) {
        this.tableData = this.tableData.filter((item) => {
          return item.id !== val.id
        })
      } else {
        this.tableData = this.delFlatten(this.tableData, val.id)
      }
      this.originData = this.tableData
    },
    // 子节点删除
    delFlatten(arr, id) {
      arr.map((item) => {
        if (item.childDto !== undefined && item.childDto !== null) {
          for (let i = 0; i < item.childDto.length; i++) {
            if (item.childDto[i].id === id) {
              item.childDto = item.childDto.filter((items) => {
                return items.id !== id
              })
            }
          }
        }
        if (Array.isArray(item.childDto)) {
          this.delFlatten(item.childDto, id)
        }
      })
      return arr
    },
    getColumnType() {
      this.$http.get('/api/schema/getColumnTypeList').then((res) => {
        this.columnTypeArr = res.data
      })
    },
    // 编辑的时候数据回显
    initData(tableId) {
      let url = ''
      if (tableId) {
        url = `/api/lowcode/modelDesign/cscpHxFormTablesAll/${tableId}`
      } else {
        url = `/api/lowcode/modelDesign/cscpHxFormTablesAll`
      }
      this.$http.get(url).then((res) => {
        if (res.data && res.data.columns && res.data.columns.length > 0) {
          var data = []
          var datas = res.data
          //主表column
          for (let i = 0; i < datas.columns.length; i++) {
            var column = {
              id: Math.random() + '',
              key: Math.random() + '',
              columnName: datas.columns[i].columnName,
              columnComment: datas.columns[i].columnComment,
              columnType: datas.columns[i].columnType,
              columnLength: datas.columns[i].columnLength,
              pointLength: datas.columns[i].pointLength,
              isNull: datas.columns[i].isNull + '',
              isPk: datas.columns[i].isPk + '',
              isFk: datas.columns[i].isFk + '',
              childDto: null,
              first: 1,
              showInput: true,
              inValid: false,
            }
            data[i] = column
          }
          //主表tableId
          this.mainTableId = datas.tableId
          if (datas.cscpHxFormTables && datas.subTableFkName && datas.subTableName) {
            for (let j = 0; j < datas.cscpHxFormTables.length; j++) {
              var tableColumn = {
                id: Math.random() + '',
                key: Math.random() + '',
                columnName: datas.cscpHxFormTables[j].tableName,
                columnComment: datas.cscpHxFormTables[j].tableContent,
                columnType: 5,
                columnLength: null,
                pointLength: null,
                isNull: '1',
                isPk: '0',
                isFk: '0',
                first: 1,
                childDto: null,
                showInput: true,
                inValid: false,
              }
              var cDto = []
              for (let k = 0; k < datas.cscpHxFormTables[j].columns.length; k++) {
                var childColumn = {
                  id: Math.random() + '',
                  key: Math.random() + '',
                  columnName: datas.cscpHxFormTables[j].columns[k].columnName,
                  columnComment: datas.cscpHxFormTables[j].columns[k].columnComment,
                  columnType: datas.cscpHxFormTables[j].columns[k].columnType,
                  columnLength: datas.cscpHxFormTables[j].columns[k].columnLength,
                  pointLength: datas.cscpHxFormTables[j].columns[k].pointLength,
                  isNull: datas.cscpHxFormTables[j].columns[k].isNull + '',
                  isPk: datas.cscpHxFormTables[j].columns[k].isPk + '',
                  isFk: datas.cscpHxFormTables[j].columns[k].isFk + '',
                  childDto: null,
                  showInput: true,
                  inValid: false,
                }
                cDto[k] = childColumn
              }
              tableColumn.childDto = cDto
              data[data.length] = tableColumn
              var childTableMesage = {
                'tableName': datas.cscpHxFormTables[j].tableName,
                'tableId': datas.cscpHxFormTables[j].tableId
              }
              this.childTableId[j] = childTableMesage
            }
          }
          this.tableData = data
          this.tableData = this.flatten(this.tableData, 1)
          // 1 为编辑
          this.operateType = '1'
        }
        return res
      })
    },
    dbTypeChange(value) {
      if (value !== '1') {
        this.formData.dbType = '2'
        this.dbnameLabel = '数据库主表名'
        this.dbCommentLabel = '数据库主表注释'
        this.dbnamePlaceholder = '请输入数据库主表名',
          this.dbCommentPlaceholder = '请输入数据库主表注释',
          this.$http.get('/api/schema/getColumnTypeList?type=2').then((res) => {
            this.columnTypeArr = res.data
          })
      } else {
        this.formData.dbType = '1'
        this.dbnameLabel = '数据库表名'
        this.dbCommentLabel = '数据库表注释'
        this.dbnamePlaceholder = '请输入数据库表名',
          this.dbCommentPlaceholder = '请输入数据库表注释',
          this.$http.get('/api/schema/getColumnTypeList').then((res) => {
            this.columnTypeArr = res.data
          })
      }
    },
    handleSubmit(name) {
      let that = this
      let flag = true
      this.$refs[name].validate((valid) => {
        if (valid) {
          that.current = that.current + 1
          this.activeName = '1'
          flag = true
        } else {
          // this.$Message.error('表单校验失败,请检查输入');
          flag = false
        }
      })
      return flag
    },
    handleReset(name) {
      this.$refs[name].resetFields()
    },
    prev() {
      this.$Modal.confirm({
        title: '提示',
        content: '<p>返回上一步会导致当前的操作被重置,是否确认返回?</p>',
        onOk: () => {
          this.activeName = '0'
          this.isShowOtherFields = true
          this.current -= 1
        },
        onCancel: () => {

        },
      })
    },
    // 提交基本信息
    passData() {
      this.createLoading = true
      const params = {}
      this.detailFormData.dbComment = '' // 点击创建 清空数据表注释内容
      if (this.formData.dbType === '1') { // 单表
        params.modelName = this.detailFormData.modelName
        params.modelType = '1'
        params.moduleId = this.detailFormData.moduleId
        // 获取到对应moduleId的moduleName
        params.moduleName = this.moduleNameList.filter(module => module.value === this.detailFormData.moduleId)[0].label
        params.tableDescription = this.detailFormData.dbComment
        params.tableName = this.detailFormData.dbname
      } else { // 主子表
        params.modelName = this.detailFormData.modelName
        params.modelType = '2'
        params.moduleId = this.detailFormData.moduleId
        // 获取到对应moduleId的moduleName
        params.moduleName = this.moduleNameList.filter(module => module.value === this.detailFormData.moduleId)[0].label
        params.tableDescription = this.detailFormData.dbComment
        params.tableName = this.detailFormData.dbname
        params.createMode = '0'
      }
      this.passDataSecondStep(params)
    },
    next() {
      this.handleSubmit('detailFormRef')
    },
    cancel() {
      this.$parent.createModelDialogVisible = false
      this.activeName = '0'
    },

    visibleChange(isShow) {
      if (this.closeFlag) {
        // 通过判断是否创建成功,成功,直接关闭对话框
        this.current = 0
        this.$refs.detailFormRef.resetFields()
        this.formData.dbType = '1'
        return
      }
      if (!isShow && (this.current === 0 || this.current === 1)) {
        this.$Modal.confirm({
          title: '提示',
          content: '退出将不会保存之前的配置, 是否退出?',
          okText: '确定',
          cancelText: '取消',
          onOk: () => {
            this.current = 0
            this.$refs.detailFormRef.resetFields()
            this.formData.dbType = '1'
            this.tableType = '单表模型'
            this.$parent.createModelDialogVisible = false
            this.$Message.success('退出成功')
          },
          onCancel: () => {
            this.$Message.info('已取消')
            this.$parent.createModelDialogVisible = true
          }

        })
        return
      }

    },
    // 数据源信息
    getDefaultLinkData() {
      const url = '/api/lowcode/modelDesign/qryDataBaseInfo'
      this.$http
        .get(url)
        .then((res) => {
          if (res.data) {
            const temp = res.data
            this.formData.ipAddress = temp.ipAddress || ''
            this.formData.port = temp.port || ''
            this.formData.dbName = temp.dbName || ''
            this.formData.username = temp.username || ''
          }
        })
        .catch(() => {
          this.$message.error('获取信息失败')
        })
    },
  }

}
</script>

<style lang="less" scoped>
// tab样式改造
::v-deep .mine_el_tab {
  position: relative;
  height: 100%;

  .el-tabs {
    height: 100%;
    display: flex;
    flex-direction: column;

    .el-tabs__header {
      margin: 0;
      height: 50px;

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
      background-color: rgb(243, 248, 255);
      padding: 16px;

      .el-tab-pane {
        height: 100%;
        background-color: #fff;
        overflow: hidden;
      }
    }
  }
}

// 弹窗样式
::v-deep.newCreateModel {
  .ivu-modal-content {
    .ivu-modal-header {
      display: none !important;
    }

    .ivu-modal-body {
      padding: 0;
      width: 100%;
      bottom: 56.8px;
      top: 0;
    }

    .ivu-modal-footer {
      height: 56.8px;
    }
  }

  .el-table {
    .el-table__header-wrapper {
      .el-table__header {
        thead {
          height: 50px;

          tr {
            th {
              background-color: #D5E0F1 !important;
              font-size: 14px;
              font-family: PingFang SC-Semibold, PingFang SC;
              font-weight: 600;
              color: #000000;
            }
          }
        }
      }
    }

    .el-table__body-wrapper {
      .el-table__body {
        tbody {
          .el-table__row--level-1 {
            td {
              background-color: #D5E0F1;
            }
          }

          .el-table__row {
            height: 60px !important;
          }
        }
      }
    }
  }

  .el-radio-group {
    width: 52px;
    height: 28px;
    display: flex;

    .el-radio-button {
      flex: 1;
      height: 100%;
    }

    .is-active {
      .el-radio-button__inner {
        background-color: #246AD9 !important;
      }
    }
  }
}
</style>
<style scoped lang="scss">
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


.contentContainer {
  width: 100%;
  height: 100%;
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;

  .content {
    overflow: hidden;
    height: 552px;
    margin-bottom: 28px;
    width: 100%;

    .step1 {
      display: flex;
      align-items: center;
      height: 100%;
      width: 100%;
      overflow: hidden;
      flex-direction: column;

      .topBox {
        width: 80%;
        display: flex;
        align-items: center;
        justify-content: space-around;
        flex: 1;

        .typeItem {
          display: flex;
          flex-direction: column;
          align-items: center;
          margin-right: 25px;
          cursor: pointer;

          > span {
            font-size: 20px;
            font-family: PingFang SC-Regular, PingFang SC;
            font-weight: 400;
            color: #333333;
            line-height: 20px;
            -webkit-background-clip: text;
          }

          .imgBox {
            width: 180px;
            height: 180px;
            border: 1px solid #e7e7e7;
            margin-bottom: 15px;
            position: relative;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;

            img {
              width: 200px;
              height: 200px;
            }

            span {
              width: 100%;
              height: 4px;
              background-color: #246AD9;
              position: absolute;
              bottom: 0;
              left: 0;
            }
          }
        }
      }

      .bottomBox {
        flex-shrink: 0;
        width: 80%;
        height: 232px;
        background-image: url("../../../../assets/lowcode/robrtBoxBg.png");
        background-size: cover;
        display: flex;
        border-radius: 16px;

        .robert {
          width: 80px;
          height: 111px;
          margin: 24px 0 0 16px;
        }

        .descriptionBox {
          flex: 1;
          margin: 32px 0 0 16px;
          position: relative;

          .fengexian {
            position: absolute;
            right: -13px;
            height: 168px;
            border-left: 1px dashed #246AD9;
            z-index: 999;
          }
        }

        .modelNameSearch {
          flex: 3;
          height: 100%;
          display: flex;
          align-items: center;
          padding: 0 25px;

          > span {
            width: 140px;
            height: 22px;
            line-height: 22px;
            font-size: 14px;
            font-family: PingFang SC-Regular, PingFang SC;
            font-weight: 400;
            color: #000;
          }
        }
      }
    }

    .step3 {
      width: 100%;
      height: 100%;
      overflow-y: auto;
    }

    .containerBox {
      width: 100%;
      height: 100%;
      overflow-y: auto;
      overflow-x: hidden;

      .tableItemBox {
        width: 100%;
        margin: 0 0 30px 0;
      }
    }
  }
}

::v-deep {

  // 隐藏全选框
  .el-table__header-wrapper .el-checkbox {
    display: none;
  }
}

// 编辑图标样式
.icon {
  color: #66b1ff;
  margin-left: 10px;
  cursor: pointer;
}

.tab-height-div {
  height: calc(100vh - 180px);
  overflow: auto;
}

.ctName {
  height: 115px;
  position: relative;
}

.ctName .ctinpt {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  border-radius: 4px;
}

.ctName .cxname {
  position: absolute;
  bottom: 21px;
}

.cxnameselect {
  position: absolute;
  bottom: 21px;
}

.ivu-select-dropdown-list > .ivu-select-item {
  padding: 5px;
}

.war-message {
  z-index: 3000 !important;
}
</style>
