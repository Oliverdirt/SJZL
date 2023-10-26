<template>
  <div id="addModelFromDb">
    <Modal
      class="addModelFromDb"
      title="从数据库中新建数据模型"
      v-model="createModelDbVisible"
      :mask-closable="false"
      fullscreen
      @on-cancel="cancel"
    >
      <span class="mTitle">从数据库中新建数据模型</span>
      <div class="mine_el_tab">
        <el-tabs v-model="activeName" @tab-click="handleTabClick">
          <el-tab-pane label="编辑数据信息" name="first">
            <!--        选择数据类型 -->
            <div class="contentContainer">
              <div class="content">
                <div class="step1">
                  <div class="topBox">
                    <!--           单表模型 -->
                    <div @click="clicktableType('单表模型')" class="typeItem">
                      <div class="imgBox">
                        <img
                          :src="tableType === '单表模型'?require('../../../../assets/lowcode/dbmxSeleted.svg'):require('../../../../assets/lowcode/dbmx.png')"
                          alt="">
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
                      <span><span style="color: red">*</span>数据模型名称：</span>
                      <el-input v-model="infoForm.modelName" placeholder="请输入数据模型名称"></el-input>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="选择新建模型" name="second">
            <div class="contentContainer">
              <div class="content">
                <!--        选择新建模型 -->
                <div class="step2">
                  <!--          第二步选了  单表模型-->
                  <div
                    v-show="tableType === '单表模型' || tableType === '视图模型'"
                    class="sigleTableBox"
                  >
                    <div class="left">
                      <div class="searchBox">
                        <el-input
                          @input="emitSearchEvent"
                          :placeholder="'请输入' + tableType + '名称'"
                          prefix-icon="el-icon-search"
                          size="small"
                          style="width: 200px"
                          v-model="searchStr"
                        >
                        </el-input>
                      </div>
                      <div class="contentBox">
                        <div
                          class="tableItem"
                          v-for="(item, index) in tableDataArr"
                          :key="index + 'hbw_'"
                          @click="clickTableItem(item, 0)"
                          :class="checkTable === item.table_name ? 'active check' : ''"
                        >
                          <img class="tMiniLogoImg" src="../../../../assets/lowcode/tMiniLogo.png" alt="">
                          <div class="tMiniLogoContent">
                            <span>{{ item.table_name }}</span>
                            <span v-show="tableType === '单表模型'">{{ item.table_comment }}</span>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="right">
                      <p style="font-weight: 600; margin-bottom: 15px">
                        {{ checkTable }}
                      </p>
                      <div
                        v-for="(item, index) in checkTableDetail"
                        :key="index + 'checkTableDetail'"
                        class="tableFildBox"
                      >
                        <span>{{ item.columnComment + '：' }}</span>
                        <span>{{ item.columnName }}</span>
                      </div>
                    </div>
                  </div>
                  <!--          第二步选了  主子表模型-->
                  <div v-show="tableType === '主子表模型'" class="sigleTableBox">
                    <div class="left">
                      <div class="searchBox">
                        <el-input
                          :placeholder="'请输入' + tableType + '名称'"
                          prefix-icon="el-icon-search"
                          size="small"
                          @input="emitSearchEvent"
                          v-model="searchStr"
                          style="width: 198px"
                        >
                        </el-input>
                      </div>
                      <div class="contentBox">
                        <div
                          class="tableItem"
                          v-for="(item, index) in tableDataArr"
                          :key="index + 'hbw_'"
                          @click="clickTableItem(item, 1)"
                          :class="checkedMulItem.indexOf(item.table_name) !== -1? (item.table_name === checkedMulItem[0]?'active check':'active checkChild'): ''"
                        >
                          <img class="tMiniLogoImg" src="../../../../assets/lowcode/tMiniLogo.png" alt="">
                          <div class="tMiniLogoContent">
                            <span>{{ item.table_name }}</span>
                            <span>{{ item.table_comment }}</span>
                            <div class="sBtnBox">
                              <button class="fBtn" v-show="item.table_name === checkedMulItem[0]">主表</button>
                              <button class="cBtn" v-show="checkedMulItem.slice(1).indexOf(item.table_name) !== -1">子表
                              </button>
                            </div>
                          </div>
                        </div>
                        <div class="tableItem" style="visibility: hidden"
                             v-for="item in (4 - (tableDataArr.length%4))"
                             :key="item+'tableDataArr'"
                        ></div>
                      </div>
                    </div>
                    <div class="right" v-loading="collapseLoading">
                      <el-collapse @change="elCollapseChange" accordion>
                        <el-collapse-item
                          v-for="(dataTable,index) in checkedMulItem"
                          :key="dataTable"
                          :name="dataTable"
                        >
                          <template slot="title">
                            <div class="colTnameBox">
                              <i></i>
                              <span>{{ dataTable }}</span>
                            </div>
                            <span v-if="index===0">主表</span>
                            <span v-else style="font-weight: 500">子表</span>
                          </template>
                          <div
                            v-for="(item, index) in showingTableDetail"
                            :key="index + 'showingTableDetail'"
                            class="tableFildBox"
                          >
                            <span>{{ item.columnComment + '：' }}</span>
                            <span>{{ item.columnName }}</span>
                          </div>
                        </el-collapse-item>
                      </el-collapse>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="绑定关联字段" name="third" :disabled="tableType !== '主子表模型'|| checkedMulItem.length <= 1">
            <div class="contentContainer">
              <div class="content">
                <!--        绑定关联字段 -->
                <div style="height: 100%">
                  <div style="height: 100%" v-if="tableType === '主子表模型'">
                    <div class="containerBox">
                      <div
                        v-for="(tableItem,index) in  childTableField"
                        :key="index + 'hbwtable'"
                        class="tableItemBox"
                      >
                        <div class="qxzSbox">
                          <el-select placeholder="请选择主子表关联类型" v-model="tableItem.relateType">
                            <el-option label="左关联" value="left"></el-option>
                            <el-option label="右关联" value="right"></el-option>
                            <el-option label="内关联" value="inner"></el-option>
                            <el-option label="外关联" value="outer"></el-option>
                          </el-select>
                        </div>
                        <el-table
                          :data="tableItem.data"
                          :header-cell-style="{backgroundColor:'#D5E0F1'}"
                          style="width: 100%">
                          <el-table-column width="400" label="请选择主子表关联类型">
                            <template v-slot="scope">

                            </template>
                          </el-table-column>
                          <el-table-column prop="mainTableField" :label="'主表字段（'+mainTableFields[0].label+'）'"
                                           width="400">
                            <template v-slot="scope">
                              <el-select clearable v-model="scope.row.mainTableField" placeholder="请选择要绑定的主表字段">
                                <el-option
                                  v-for="(item,index) in mainTableFields[0].options"
                                  :key="index + 'main66'"
                                  :label="item.columnName+'（'+item.dataType+')'"
                                  :value="item.columnName"
                                >
                                </el-option>
                              </el-select>
                            </template>
                          </el-table-column>
                          <el-table-column prop="chiTableField" :label="'子表表字段（'+tableItem.label+'）'" width="400">
                            <template v-slot="scope">
                              <el-select clearable v-model="scope.row.chiTableField" placeholder="请选择要绑定的子表字段">
                                <el-option
                                  v-for="(item,index) in tableItem.options"
                                  :key="index + item"
                                  :label="item.columnName+'（'+item.dataType+')'"
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
                                style="color: red"
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
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div slot="footer">
        <Button type="primary" @click="createModel" style="width: 72px">创建</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
export default {
  name: 'addModelFromDb',
  props: ['moduleParam'],
  watch: {
    activeName(newVal, oldVal) {
      if (newVal === 'second') { // 第二步选择新建模型
        this.tableType === '视图模型' ? this.getDbViewList() : this.getDbTableList()
      } else if (newVal === 'third') { // 第二步绑定关联字段
        this.clickBindField()
      }
    }
  },
  data() {
    let validateModelName = (rule, value, callback) => {
      // 验证数据模型名称是否重复
      const [url] = [
        `/api/cscpCustomizeModels?modelName.equals=${value}&page=1&size=10`
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
    return {
      showingTableDetail: undefined,
      collapseLoading: false,
      activeName: 'first',
      createModelDbVisible: true,
      current: 0,
      tableType: '单表模型', //  单表模型 视图模型 主子表模型
      dataModelDetail:
        '支持无需审批的简单业务的增加、删除、修改、查询，仅支持包含单个数据库表，如日报填报、健康行程上报等业务都可使用单表模型',
      searchStr: '',
      tableDataArr: [],
      copyTableDataArr: [],
      checkTable: '',
      checkTableDetail: undefined,
      infoForm: {
        modelName: '',
        tableName: '',
        dbDetail: '',
        moduleId: ''
      },
      rules: {
        modelName: [
          {required: true, message: '数据模块名称不能为空', trigger: 'blur'},
          {required: true, validator: validateModelName, trigger: 'blur'}
        ],
        moduleId: [
          {required: true, message: '所属模块不能为空', trigger: 'blur'}
        ]
      },
      checkedMulItem: [],
      relationForm: {},
      tableData: [],
      childTableField: [],
      mainTableFields: [],
      tableId: '',
      loading: true,
    }
  },
  mounted() {
    window.localStorage.setItem('moduleId', '')
  },
  methods: {
    handleTabClick(tab) {

    },
    createModel() {
      // 验证模型名称不能为空
      if (!this.infoForm.modelName) {
        this.activeName = 'first'
        this.$message.error('请输入模型名称！')
        return
      }
      // 验证表是否勾选
      if ((this.tableType === '单表模型' || this.tableType === '视图模型') && !this.checkTable) {
        this.$message.error('请选择要导入的模型！')
        this.activeName = 'second'
        return
      }
      if (this.tableType === '主子表模型' && this.checkedMulItem.length <= 1) {
        this.$message.error('请至少选择两个数据库表！')
        this.activeName = 'second'
        return
      }
      let mainField = this.childTableField.length ? this.childTableField[0].data[0].mainTableField : ''
      let childField = this.childTableField.length ? this.childTableField[0].data[0].chiTableField : ''
      if (this.tableType === '主子表模型' && (!mainField || !childField)) {
        this.$message.error('请绑定关联字段！')
        this.activeName = 'third'
        return
      }
      this.judgeTableChcNum()
    },
    // 数据表勾选校验
    judgeTableChcNum() {
      if (this.tableType === '单表模型' || this.tableType === '视图模型') {
        this.goFourStep(0)
      } else if (this.tableType === '主子表模型') {
        this.goFourStep(1)
      }
    },
    goFourStep(flag) {
      flag ? (this.infoForm.tableName = this.checkedMulItem[0]) : (this.infoForm.tableName = this.checkTable)
      flag
        ? (this.infoForm.dbDetail = this.getTableContent(
        this.checkedMulItem[0]
        ).table_comment)
        : (this.infoForm.dbDetail = this.getTableContent(
        this.checkTable
        ).table_comment)
      this.save()
    },
    getTableContent(table_name) {
      return this.tableDataArr.find((item) => {
        return item.table_name === table_name
      })
    },
    // 弹窗关闭回调
    cancel() {
      this.$emit('childCancel')
      this.$parent.queryList()
    },
    //  获取数据库表列表
    getDbTableList() {
      this.$http
        .get('/api/schema/cscpHxTablesInfo')
        .then((res) => {
          this.copyTableDataArr = res.data.map((item) => {
            return item
          })
          this.tableDataArr = res.data
          this.current += 1
        })
        .catch((err) => {
          this.$message.error('获取数据库表列表失败！')
        })
    },
    // 获取数据库视图列表
    getDbViewList() {
      this.$http
        .get('/api/schema/cscpHxViewsInfo')
        .then((res) => {
          this.copyTableDataArr = res.data.map((item) => {
            return item
          })
          this.tableDataArr = res.data
          this.current += 1
        })
        .catch((err) => {
          this.$message.error('获取数据库视图列表失败！')
        })
    },
    // 拿到数据表或视图信息
    getTableOrViewInfo(tableName) {
      this.$http
        .get('/api/cscpHxFormColums', {params: {tableName}})
        .then((res) => {
          this.checkTableDetail = res.data
        }).catch(err => {
        this.collapseLoading = false
        console.log(err);
      })
    },
    getModelType(str) {
      let flag = ''
      switch (str) {
        case '单表模型':
          flag = 1
          break
        case '主子表模型':
          flag = 2
          break
        case '视图模型':
          flag = 3
          break
      }
      return flag
    },
    // 数据表中的选中处理逻辑
    mulChecked(item) {
      this.getTableOrViewInfo(item.table_name)
      this.checkTable = item.table_name
    },
    // 数据表中的取消选中逻辑
    mulCancelChecked(item) {
      let index = this.checkedMulItem.indexOf(item.table_name)
      this.checkedMulItem.splice(index, 1)
      this.checkTable = this.checkedMulItem[0]
      this.getTableOrViewInfo(this.checkedMulItem[0])
    },
    elCollapseChange(tableName) {
      if (tableName) {
        this.collapseLoading = true
        this.$http
          .get('/api/cscpHxFormColums', {params: {tableName}})
          .then((res) => {
            this.showingTableDetail = res.data
            setTimeout(() => {
              this.collapseLoading = false
            }, 100)
          }).catch(err => {
          this.collapseLoading = false
          console.log(err);
        })
      }
    },
    // 保存也分两种情况，主子表时进行一部分，下一部分在下一步骤进行，再发起请求
    save() {
      // 接口调用
      let queryParams = {}
      if (this.tableType === '主子表模型') {
        //设置子表列表
        let cscpHxFormTables = []
        //设置主表名称
        queryParams.tableName = this.mainTableFields[0].label
        for (let item in this.childTableField) {
          let subCscpHxFormTable = {}
          //设置子表名称
          subCscpHxFormTable.tableName = this.childTableField[item].label
          //设置关联类型
          subCscpHxFormTable.relateType = this.childTableField[item].relateType
          //关联字段
          let columns = []
          //设置子表关联字段
          for (let citem in this.childTableField[item].data) {
            let column = {}
            //子表本身字段
            column.columnName = this.childTableField[item].data[citem].chiTableField
            //子表和主表关联字段
            column.columnRelation = this.childTableField[item].data[citem].mainTableField
            columns.push(column)
          }
          //设置子表关联字段
          subCscpHxFormTable.columns = columns
          //设置单个子表
          cscpHxFormTables.push(subCscpHxFormTable)
        }
        //设置子表列表
        queryParams.cscpHxFormTables = cscpHxFormTables
        //创建数据模型通过已有数据表
        queryParams.createFlag = '1'
        this.$http
          .post('/api/lowcode/modelDesign/cscpHxFormTables', queryParams)
          .then((res) => {
            this.tableId = res.data
            this.addOrEditBasicInfo()
          })
          .catch((err) => {
            this.$message.error('获取列表数据失败！')
          })
      } else if (this.tableType === '单表模型' || this.tableType === '视图模型') {
        queryParams.tableName = this.checkTable
        queryParams.tableContent = this.getTableContent(
          this.checkTable
        ).table_comment
        this.$http
          .post('/api/lowcode/modelDesign/cscpHxFormTablesMesage', queryParams)
          .then((res) => {
            this.tableId = res.data
            this.addOrEditBasicInfo()
          })
          .catch((err) => {
            this.$message.error('获取列表数据失败！')
          })
      }
    },
    // 基本信息的新增和修改
    addOrEditBasicInfo() {
      let queryParams = {
        modelName: this.infoForm.modelName,
        tableName: this.infoForm.tableName,
        modelType: this.getModelType(this.tableType),
        tableId: this.tableId,
        createMode: '1',
        moduleId: this.moduleParam.moduleId,
        moduleName: this.moduleParam.moduleName
      }
      // 新增
      this.$http
        .post('/api/cscpCustomizeModels', queryParams)
        .then((res) => {
          window.localStorage.setItem('moduleId', res.data.moduleId)
          this.cancel()
          this.$message.success('创建成功！')
        })
        .catch((err) => {
          this.$message.error('创建失败！')
        })
    },
    // 鼠标 键盘事件
    clicktableType(str) {
      this.tableType = str
      if (str === '单表模型') {
        this.dataModelDetail =
          '支持无需审批的简单业务的增加、删除、修改、查询，仅支持包含单个数据库表，如日报填报、健康行程上报等业务都可使用单表模型'
      } else if (str === '视图模型') {
        this.dataModelDetail =
          '支持无需审批的业务查询，仅支持数据库视图，如省内各公司业务情况、市场营销执行情况等业务都可使用视图模型'
      } else if (str === '主子表模型') {
        this.dataModelDetail =
          '支持无需审批的复杂业务的增加、删除、修改、查询，可包含多个数据库表，如客户档案维护、班级学生等业务都可使用主子表模型'
      }
    },
    clickTableItem(item, flag) {
      if (flag) {
        // 多表
        if (this.checkedMulItem.indexOf(item.table_name) !== -1) {
          //取消选中
          this.mulCancelChecked(item)
        } else {
          this.mulChecked(item)
          this.checkedMulItem.push(item.table_name)
        }
      } else {
        this.mulChecked(item)
      }
    },
    emitSearchEvent() {
      this.tableDataArr = this.copyTableDataArr.filter((item) => {
        return item.table_name.includes(this.searchStr)
      })
    },
    clickBindField() {
      // 初始化
      this.childTableField = []
      // 参数
      let temArr = this.checkedMulItem.slice(1, this.checkedMulItem.length)
      this.$http
        .get('/api/cscpHxFormColumsBatch', {
          params: {tableName: temArr.join(',')}
        })
        .then((res) => {
          for (let item in res.data) {
            let temJson = {
              label: item,
              options: res.data[item],
              data: [{mainTableField: '', chiTableField: ''}]
            }
            this.childTableField.push(temJson)
          }
          this.getMainTableFields()
        })
        .catch((err) => {
          this.$message.error('获取子表字段失败！')
        })
    },
    getMainTableFields() {
      // 初始化
      this.mainTableFields = []
      // 参数
      this.$http
        .get('/api/cscpHxFormColumsBatch', {
          params: {tableName: this.checkedMulItem[0]}
        })
        .then((res) => {
          for (let item in res.data) {
            let temJson = {
              label: item,
              options: res.data[item]
            }
            this.mainTableFields.push(temJson)
          }
        })
        .catch((err) => {
          this.$message.error('获取子表字段失败！')
        })
    },
    addItem(arr) {
      arr.push({mainTableField: '', chiTableField: ''})
    },
    deleteItem(arr) {
      if (arr.length > 1) {
        arr.pop()
      } else {
        this.$message.error('仅有一条数据不能删除！')
      }
    }
  }
}
</script>

<style lang="less" scoped>
.mine_el_tab {
  position: relative;
  height: 100%;
}

::v-deep .el-tabs {
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

  ::v-deep .el-tabs__content {
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


.addModelFromDb {
  ::v-deep .ivu-modal-content {
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

  ::v-deep.el-collapse-item__header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    > span {
      font-size: 14px;
      font-family: PingFang SC-Semibold, PingFang SC;
      font-weight: 600;
      color: #246AD9;
    }

    .colTnameBox {
      height: 100%;
      display: flex;
      align-items: center;

      i {
        display: inline-block;
        width: 15px;
        height: 15px;
        background: rgba(0, 73, 218, 0.28);
        border-radius: 50%;
        margin-right: 15px;
      }

      span {
        font-size: 16px;
        font-family: PingFang SC-Medium, PingFang SC;
        font-weight: 500;
        color: #000000;
      }
    }

    ::v-deep .el-collapse-item__arrow {
      display: none !important;
    }
  }
}


</style>
<style lang="less" scoped>
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
          padding: 25px;

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

    .step2 {
      height: 100%;
      width: 100%;

      .left {
        flex: 4;
        height: 100%;
        padding: 16px;
        border-right: 1px dashed #246AD9;
      }

      .right {
        flex: 1;
        height: 100%;
        padding: 25px;
      }

      .sigleTableBox {
        height: 100%;
        width: 100%;
        display: flex;
        overflow-x: hidden;
        overflow-y: auto;

        .left {
          display: flex;
          flex-direction: column;

          .searchBox {
            height: 40px;
            display: flex;
            align-items: center;
            width: 400px;
            padding-bottom: 16px;

            .bdglzdBtn {
              width: 180px;
              height: 32px;
              background: #eff6fd;
              border: 1px solid #aed2f3;
              border-radius: 2px;
              color: #348fe0;
              margin-left: 8px;
              text-align: center;
              cursor: pointer;
              padding: 0 4px;
            }
          }

          .contentBox {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            justify-content: space-around;

            .check {
              background-color: #3066BC !important;

              span {
                font-size: 14px !important;
                font-family: PingFang SC-Regular, PingFang SC;
                font-weight: 400 !important;
                color: #FFFFFF !important;
              }
            }

            .checkChild {
              background-color: #ADD2F3 !important;

              span {
                font-size: 14px !important;
                font-family: PingFang SC-Medium, PingFang SC;
                font-weight: 500 !important;
                color: #000000 !important;
              }
            }

            .tableItem {
              width: 24%;
              height: 88px;
              margin: 0 0 16px 0;
              border: 1px solid #e7e7e7;
              cursor: pointer;
              position: relative;
              display: flex;
              justify-content: center;
              background: #E4F1FD;
              border-radius: 8px;
              opacity: 1;
              align-items: center;


              .sBtnBox {
                position: absolute;
                top: 50%;
                right: 0;
                transform: translate(0, -50%);

                button {
                  width: 40px;
                  height: 20px;
                  background: #BFD8FF;
                  border-radius: 12px 0 0 12px;
                  color: #348fe0;
                  margin-left: 8px;
                  text-align: center;
                  cursor: pointer;
                  font-size: 12px;
                  border: 1px solid #246AD9;
                }

                .cBtn {
                  background-color: #fff;
                }
              }

              .tMiniLogoContent {
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;

                span {
                  font-size: 12px;
                  color: #545672;
                  width: 140px;
                  overflow: hidden;
                  white-space: nowrap;
                  text-overflow: ellipsis;
                }
              }

              .tMiniLogoImg {
                width: 32px;
                height: 32px;
              }
            }
          }
        }

        .tableFildBox {
          margin-bottom: 16px;
          display: flex;

          > span {
            font-size: 12px;
            color: #333651;
          }

          > span:nth-child(1) {
            width: 100px;
            display: inline-block;
          }
        }
      }
    }

    .containerBox {
      width: 100%;
      height: 100%;
      overflow-y: auto;
      overflow-x: hidden;

      .tableItemBox {
        position: relative;
        width: 100%;
        margin: 0 0 30px 0;

        .qxzSbox {
          width: 400px;
          height: 57px;
          overflow: hidden;
          position: absolute;
          top: 44px;
          left: 0;
          z-index: 99;
          display: flex;
          align-items: center;
          justify-content: center;
        }
      }
    }
  }
}
</style>
