<template>
  <div style="height: 100%">
    <Row :gutter="16" style="height: 100%" ref="rightRow" id="rightRow">
      <Col span="4" style="">
        <Card :bordered="false" :dis-hover="true" :shadow="false" style="height: 100%">
          <span slot="title" style="height: 38px"> 数据表 </span>
          <a slot="extra" @click="addModal = true">
            <Icon type="md-add" size="18" color="green"></Icon>
          </a>
          <div class="searchBox">
            <Input v-model="searchTableName" @input="listenTnameChnage" placeholder="请输入表名" icon="ios-search"
                   style="margin-bottom: 20px"/>
          </div>
          <div style="height: 450px; overflow-y: auto; overflow-x: hidden" @click="handleContextmenu">
            <div class="tableItem" v-for="tableItem in tableList">
              <div :class="
              curTableName === tableItem.table_name ? 'active' : 'normal'
            ">
                <div style="width: 85%; float: left" @click="selectTable(tableItem)">
                  <Tooltip :content="tableItem.table_comment" placement="top">
                  <span data-tooltip="tableItem">{{
                      tableItem.table_name
                    }}</span>
                  </Tooltip>
                </div>
                <div style="width: 15%; float: right">
                  <a @click="delTable(tableItem)">
                    <Icon type="ios-trash" size="22" color="#ed4014"/>
                  </a>
                </div>
              </div>
            </div>
          </div>
        </Card>
      </Col>
      <Col span="20">
        <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard" style="height: 100%">
          <div class="tableBtn">
            <Button @click="clickAddBtn" icon="md-add" type="primary">
              新增
            </Button>
            <Button @click="clickDelBtn" icon="md-trash" type="error">
              删除
            </Button>
          </div>
          <Table border @on-selection-change="tableSelectChange" height="600" :columns="columns"
                 :data="dataArr"></Table>
        </Card>
      </Col>
      <Dropdown transfer placement="right-start" trigger="custom" ref="contextMenu" :visible="currentVisible"
                @on-clickoutside="handleCancel" @on-click="rightClick">
        <DropdownMenu slot="list">
          <DropdownItem name="add">新建数据表</DropdownItem>
          <DropdownItem name="copy">复制数据表</DropdownItem>
        </DropdownMenu>
      </Dropdown>
    </Row>
    <!--     复制表弹窗 -->
    <Modal v-model="copyModal" title="复制数据表" @on-cancel="copyModal = false">
      <Form ref="copyTableNameForm" :model="copyTableData" :rules="ruleValidateCopyModal" :label-width="80">
        <FormItem label="表名" prop="newTableName">
          <Input v-model="copyTableData.newTableName" placeholder="请输入表名"></Input>
        </FormItem>
        <FormItem label="表描述" prop="newTableComment">
          <Input v-model="copyTableData.newTableComment" placeholder="请输入表描述名"></Input>
        </FormItem>
        <FormItem label="目标表" prop="copyTableName">
          <Select v-model="copyTableData.copyTableName" placeholder="请选择">
            <Option v-for="(item, index) in tableList" :value="item.table_name" :key="index">{{ item.table_name }} 【{{
                item.table_comment
              }}】
            </Option>
          </Select>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="primary" @click="confirmCopy" style="width: 80px">确定
        </Button>
        <Button type="default" @click="copyModal = false" style="width: 80px">取消
        </Button>
      </div>
    </Modal>

    <!--     新增表弹窗 -->
    <Modal v-model="addModal" title="新增数据表" @on-cancel="addModal = false">
      <Form ref="addTableName" :model="addTableJson" :rules="ruleValidateAddModal" :label-width="80">
        <FormItem label="表名" prop="tableName">
          <Input v-model="addTableJson.tableName" placeholder="请输入表名"></Input>
        </FormItem>
        <FormItem label="表描述" prop="tableContent">
          <Input v-model="addTableJson.tableContent" placeholder="请输入表描述名"></Input>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="primary" @click="confirmAdd" style="width: 80px">确定
        </Button>
        <Button type="default" @click="addModal = false" style="width: 80px">取消
        </Button>
      </div>
    </Modal>
    <!--     新增字段弹窗 -->
    <Modal v-model="addFieldModal" :title="addDialogTitle" @on-cancel="closeAddFieldModal">
      <Form ref="addFieldJson" :model="addFieldJson" :rules="fieldRuleValidate" :label-width="120">
        <FormItem label="数据库字段名" prop="columnName">
          <Input v-model="addFieldJson.columnName" placeholder="请输入数据库字段名"></Input>
        </FormItem>
        <FormItem label="字段名称" prop="columnComment">
          <Input v-model="addFieldJson.columnComment" placeholder="请输入字段名称"></Input>
        </FormItem>
        <FormItem label="字段类型" prop="columnType">
          <Select v-model="addFieldJson.columnType" placeholder="请选择">
            <Option v-for="(item, index) in columnTypeArr" :value="item.typeNum" :key="index">{{ item.typeName }}
            </Option>
          </Select>
        </FormItem>
        <FormItem label="字段长度" prop="columnLength">
          <Input type="number" v-model="addFieldJson.columnLength" placeholder="请输入字段长度"></Input>
        </FormItem>
        <FormItem label="小数点" prop="pointLength">
          <Input type="number" v-model="addFieldJson.pointLength" placeholder="请输入小数点"></Input>
        </FormItem>
        <FormItem label="是否为空" prop="isNull">
          <RadioGroup v-model="addFieldJson.isNull">
            <Radio label="1">是</Radio>
            <Radio label="0">否</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="是否为主键" prop="isPk">
          <RadioGroup v-model="addFieldJson.isPk">
            <Radio label="1">是</Radio>
            <Radio label="0">否</Radio>
          </RadioGroup>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="primary" @click="clickConfirmBtn" style="width: 80px">确定
        </Button>
        <Button type="default" @click="closeAddFieldModal" style="width: 80px">取消
        </Button>
      </div>
    </Modal>
  </div>
</template>

<script>
export default {
  name: 'dbManager',
  watch: {
    addModal(newVal) {
      if (this.$refs.addTableName) {
        this.$refs.addTableName.resetFields()
      }
    },
    copyModal(newVal) {
      if (this.$refs.copyTableNameForm) {
        this.$refs.copyTableNameForm.resetFields()
      }
    }
  },
  data() {
    let validateAddTableName = (rule, value, callback) => {
      let pattern = /^[A-Za-z][A-Za-z_0-9]+$/
      let reg = /,*[A-Z]+.*/
      if (value.length > 30) {
        callback(new Error('长度不可以超过30位'))
      } else if (!pattern.test(value)) {
        callback(new Error('以字母开头，后边可以为数字、字母或下划线'))
      } else if (reg.test(value)) {
        callback(new Error('表名不能含有大写字母！'))
      } else {
        const [url, httpConfig] = [
          '/api/schema/isTableExists',
          {
            params: {tableName: value}
          }
        ]
        this.$http
          .get(url, httpConfig)
          .then((response) => {
            if (response.data.code === -1) {
              callback()
            } else {
              callback(new Error(response.data.msg))
            }
          })
          .catch(() => {
            callback(new Error('异步校验出错！'))
          })
      }
    }
    let isSelected = (rule, value, callback) => {
      if (value > 0) {
        callback()
      } else {
        callback(new Error('字段类型不能为空，请选择字段类型！！'))
      }
    }
    let validateAddFeildName = (rule, value, callback) => {
      let pattern = /^[A-Za-z][A-Za-z_0-9]+$/
      if (value.length > 30) {
        callback(new Error('长度不可以超过30位'))
      } else if (!pattern.test(value)) {
        callback(new Error('以字母开头，后边可以为数字、字母或下划线'))
      } else if (this.mgc.indexOf(value) !== -1) {
        callback(new Error(value + '与数据库系统字段重复'))
      } else {
        let columnList = this.dataArr.map((v) => v.columnName)
        if (this.delIndex === -1) {
          if (columnList.includes(value)) {
            callback(new Error('字段名已存在,请重新命名'))
          } else {
            callback()
          }
        } else {
          if (
            columnList[this.delIndex] !== value &&
            columnList.includes(value)
          ) {
            callback(new Error('字段名已存在,请重新命名'))
          } else {
            callback()
          }
        }
      }
    }
    return {
      posX: 0,
      posY: 0,
      currentVisible: false,
      locator: null,
      addDialogTitle: '新增数据表字段',
      addFieldModal: false,
      ruleValidate: {
        tableName: [
          {required: true, message: '表名不能为空！', trigger: 'blur'}
        ],
        tableContent: [
          {required: true, message: '表描述名不能为空！', trigger: 'blur'}
        ]
      },
      fieldRuleValidate: {
        columnName: [
          {
            required: true,
            message: '数据库字段名不能为空！',
            trigger: 'blur'
          },
          {required: true, validator: validateAddFeildName, trigger: 'blur'}
        ],
        columnComment: [
          {required: true, message: '字段名称不能为空！！', trigger: 'blur'}
        ],
        columnType: [
          {required: true, trigger: 'change', validator: isSelected}
        ]
      },
      ruleValidateAddModal: {
        tableName: [
          {required: true, message: '表名不能为空！', trigger: 'blur'},
          {required: true, validator: validateAddTableName, trigger: 'blur'}
        ],
        tableContent: [
          {required: true, message: '表描述不能为空！！', trigger: 'blur'}
        ]
      },
      ruleValidateCopyModal: {
        newTableName: [
          {required: true, message: '表名不能为空！', trigger: 'blur'},
          {required: true, validator: validateAddTableName, trigger: 'blur'}
        ],
        newTableComment: [
          {required: true, message: '表描述不能为空！！', trigger: 'blur'}
        ],
        copyTableName: [
          {required: true, message: '目标表不能为空！！', trigger: 'change'}
        ]
      },
      addFieldJson: {
        columnName: '',
        columnComment: '',
        columnType: '',
        columnLength: '',
        pointLength: ''
      },
      addTableJson: {
        tableName: '',
        tableContent: ''
      },
      copyTableData: {
        newTableName: '',
        newTableComment: '',
        copyTableName: ''
      },
      addModal: false,
      copyModal: false,
      searchTableName: '',
      tableList: [],
      copyTableList: [],
      dataArr: [],
      curTableName: '',
      curTableContent: '',
      columns: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          type: 'index',
          title: '序号',
          width: 80,
          align: 'center'
        },
        {
          key: 'columnName',
          title: '数据库字段名',
          align: 'center'
        },
        {
          key: 'columnComment',
          title: '字段名称',
          align: 'center'
        },
        {
          key: 'dataType',
          title: '字段类型',
          align: 'center',
        },
        {
          key: 'columnLength',
          title: '字段长度',
          align: 'center',
        },
        {
          key: 'pointLength',
          title: '小数点',
          align: 'center',
        },
        {
          key: 'isNull',
          title: '允许空值',
          align: 'center',
        },
        {
          key: 'isPk',
          title: '是否为主键',
          align: 'center'
        },
        {
          title: '操作',
          align: 'center',
          key: 'handle',
          width: 200,
          fixed: 'right',
          render: (h, params) => {
            return h('div', [
              h(
                'Button',
                {
                  props: {
                    type: 'text',
                    disabled:
                      params.row.columnName === 'id' ||
                      params.row.columnName === 'instance_id'
                  },
                  style: {
                    color: params.row.columnName === 'id' ||
                    params.row.columnName === 'instance_id' ? '' : '#0056B5'
                  },
                  on: {
                    click: () => {
                      this.clickEditBtn(params)
                    }
                  }
                },
                '编辑'
              ),
              h(
                'Button',
                {
                  props: {
                    type: 'text',
                    disabled:
                      params.row.columnName === 'id' ||
                      params.row.columnName === 'instance_id'
                  },
                  style: {
                    color: params.row.columnName === 'id' ||
                    params.row.columnName === 'instance_id' ? '' : '#0056B5'
                  },
                  on: {
                    click: () => {
                      this.clickSingleDelBtn(params)
                    }
                  }
                },
                '刪除'
              )
            ])
          }
        }
      ],
      selectedTableItem: [],
      columnTypeArr: [],
      delIndex: -1,
      DB_TYPE: '0'
    }
  },
  methods: {
    rightClick(name) {
      if (name === 'add') {
        this.addModal = true
      }
      if (name === 'copy') {
        this.copyModal = true
      }
    },

    createLocator() {
      // 获取Dropdown
      const contextmenu = this.$refs.contextMenu
      // 创建locator
      const locator = document.createElement('div')
      locator.style.cssText = `position:fixed;left:${this.posX}px;top:${this.posY}px`
      document.body.appendChild(locator)
      // 将locator绑定到Dropdown的reference上
      contextmenu.$refs.reference = locator
      this.locator = locator
    },
    removeLocator() {
      if (this.locator) document.body.removeChild(this.locator)
      this.locator = null
    },
    handleContextmenu({button, clientX, clientY}) {
      if (this.$route.name.includes('db-manager')) {
        if (button === 2) {
          if (clientX < 450) {
            if (this.posX !== clientX) this.posX = clientX
            if (this.posY !== clientY) this.posY = clientY
            if (this.trigger !== 'custom') {
              this.createLocator()
              this.currentVisible = true
            }
          }
        }
      }
    },
    handleCancel(name) {
      this.currentVisible = false
      this.removeLocator()
    },

    /**
     *  鼠标事件 通用方法
     * */
    selectTable(tableItem) {
      this.curTableName = tableItem.table_name
      this.curTableContent = tableItem.table_comment
      this.getColumns(this.curTableName)
    },
    listenTnameChnage() {
      this.tableList = this.copyTableList.filter((item) => {
        return item.table_name.includes(this.searchTableName)
      })
    },
    tableSelectChange(val) {
      this.selectedTableItem = val
    },
    getColumnType() {
      this.$http.get('/api/schema/getColumnTypeList').then((res) => {
        this.columnTypeArr = res.data
      })
    },
    // 更新数据库表字段
    updateTable(params, type) {
      // type:  1新增字段 2删除字段 3修改
      this.$http
        .post('/api/schema/createOrUpdateFormTable', params)
        .then((res) => {
          let str = ''
          if (type === 1) {
            str = '新增字段成功！'
          } else if (type === 2) {
            str = '删除字段成功！'
          } else if (type === 3) {
            str = '修改字段成功！'
          }
          this.$Message.success({
            background: true,
            content: str
          })
          this.selectedTableItem = []
          this.getColumns(this.curTableName)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    createTable(params) {
      this.$http
        .post('/api/schema/initFormTable', params)
        .then((res) => {
          this.$Message.success({
            background: true,
            content: '新建表成功！'
          })
          this.selectDB()
        })
        .catch((err) => {
          console.log(err)
        })
    },
    /**
     *  表字段相关逻辑
     * */
    clickEditBtn(params) {
      this.delIndex = params.index
      this.addDialogTitle = '编辑数据表字段'
      this.addFieldModal = true
      this.addFieldJson = {
        columnName: params.row.columnName,
        columnComment: params.row.columnComment,
        columnType: params.row.columnType,
        columnLength: params.row.columnLength,
        pointLength: params.row.pointLength,
        isNull: params.row.isNull === 'YES' ? '1' : '0',
        isPk: params.row.isPk === 'YES' ? '1' : '0'
      }
    },
    clickAddBtn() {
      this.addFieldModal = true
      this.addDialogTitle = '新增数据表字段'
      this.addFieldJson = {
        columnName: '',
        columnComment: '',
        columnType: '',
        columnLength: '',
        pointLength: '',
        isNull: '1',
        isPk: '0'
      }
      if (this.$refs.addFieldJson !== undefined) this.$refs.addFieldJson.resetFields()
    },
    clickDelBtn() {
      if (this.selectedTableItem.length) {
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 参数处理
          let temArr = this.selectedTableItem.map((item) => {
            return item.columnName
          })
          let paramArr = this.dataArr.filter((item) => {
            return temArr.indexOf(item.columnName) === -1
          })
          let columns = paramArr.map((item) => {
            let copyitem = JSON.parse(JSON.stringify(item))
            copyitem.pointLength = parseInt(copyitem.pointLength)
            //判断数据库类型
            copyitem.isNull = copyitem.isNull === 'YES' ? 1 : 0
            copyitem.isPk = copyitem.isPk === 'YES' ? 1 : 0
            return copyitem
          })
          if (columns.length <= 0) {
            this.$Message.error({
              background: true,
              content: '删除失败！至少保留一条字段信息！！'
            })
          } else {
            let params = {
              tableName: this.curTableName,
              tableContent: this.curTableContent,
              columns
            }
            this.updateTable(params, 2)
          }
        })
      } else {
        this.$Message.warning({
          background: true,
          content: '请至少勾选一条需要删除的数据！'
        })
      }
    },
    closeAddFieldModal() {
      this.addFieldModal = false
      this.delIndex = -1
      this.$refs.addFieldJson.resetFields()
    },
    clickConfirmBtn() {
      this.$refs.addFieldJson.validate((valid) => {
        if (valid) {
          this.addDialogTitle === '新增数据表字段'
            ? this.confirmFieldAdd()
            : this.confirmFieldEdit()
        } else {
          this.$Message.error({
            background: true,
            content: '检验数据失败！'
          })
          this.addFieldModal = true
        }
      })
    },
    confirmFieldEdit() {
      // 主键校验
      // 校验主键规则
      let pkList = this.dataArr.filter((t) => {
        return t.isPk == '1' || t.isPk == 'YES'
      })
      // 当前为主键，之前不是主键
      if (
        this.addFieldJson.isPk == 1 &&
        this.dataArr[this.delIndex].isPk != 'YES'
      ) {
        if (pkList.length > 0) {
          this.$Notice.error({
            title: '操作失败,主键必须唯一！！'
          })
          return
        } else {
          // 无主键新增主键，主键非空校验
          if (this.addFieldJson.isNull == 1) {
            this.$Notice.error({
              title: '主键不可为空！！'
            })
            return
          }
          // 新增主键，主键类型校验
          if (this.addFieldJson.columnType != 5) {
            this.$Notice.error({
              title: '主键必须为bigint类型！！'
            })
            return
          }
        }
      } else if (
        this.addFieldJson.isPk == 1 &&
        this.dataArr[this.delIndex].isPk == 'YES'
      ) {
        // 当前为主键，之前也为主键
        // 无主键新增主键，主键非空校验
        if (this.addFieldJson.isNull == 1) {
          this.$Notice.error({
            title: '主键不可为空！！'
          })
          return
        }
        // 新增主键，主键类型校验
        if (this.addFieldJson.columnType != 5) {
          this.$Notice.error({
            title: '主键必须为bigint类型！！'
          })
          return
        }
      }
      this.dataArr.splice(this.delIndex, 1)
      let columns = this.dataArr.map((item) => {
        let copyitem = JSON.parse(JSON.stringify(item))
        copyitem.pointLength = parseInt(copyitem.pointLength)

        copyitem.isNull = copyitem.isNull === 'YES' ? 1 : 0
        copyitem.isPk = copyitem.isPk === 'YES' ? 1 : 0
        return copyitem
      })
      this.addFieldJson.isNull = parseInt(this.addFieldJson.isNull)
      this.addFieldJson.isPk = parseInt(this.addFieldJson.isPk)
      columns.splice(this.delIndex, 0, this.addFieldJson)
      let params = {
        tableName: this.curTableName,
        tableContent: this.curTableContent,
        columns
      }
      this.updateTable(params, 3)
      this.addFieldModal = false
    },
    confirmFieldAdd() {
      let columns = this.dataArr.map((item) => {
        let copyitem = JSON.parse(JSON.stringify(item))
        copyitem.pointLength = parseInt(copyitem.pointLength)

        copyitem.isNull = copyitem.isNull === 'YES' ? 1 : 0
        copyitem.isPk = copyitem.isPk === 'YES' ? 1 : 0
        return copyitem
      })
      this.addFieldJson.isNull = parseInt(this.addFieldJson.isNull)
      this.addFieldJson.isPk = parseInt(this.addFieldJson.isPk)

      if (this.addFieldJson.isPk) {
        // 校验主键规则
        let pkList = this.dataArr.filter((t) => {
          return t.isPk == 'YES' || t.isPk == '1'
        })
        if (pkList.length > 0) {
          this.$Notice.error({
            title: '操作失败,主键必须唯一！！'
          })
          return
        } else {
          // 无主键新增主键，主键非空校验
          if (this.addFieldJson.isNull) {
            this.$Notice.error({
              title: '主键不可为空！！'
            })
            return
          }
          // 新增主键，主键类型校验
          if (this.addFieldJson.columnType != 5) {
            this.$Notice.error({
              title: '主键必须为bigint类型！！'
            })
            return
          }
        }
      }

      columns.push(this.addFieldJson)
      let params = {
        tableName: this.curTableName,
        tableContent: this.curTableContent,
        columns
      }
      this.updateTable(params, 1)
      this.addFieldModal = false
      //  this.$refs.addFieldJson.resetFields()
    },
    getColumns(tableName) {
      const [url, param] = [
        '/api/schema/getTableFieldInfoByMap',
        {
          params: {
            tableName: tableName
          }
        }
      ]
      this.$http
        .get(url, param)
        .then((res) => {
          let formArr = res.data
          this.dataArr = []
          if (formArr.length > 0) {
            // 数据处理
            for (let i = 0; i < formArr.length; i++) {
              let map = {
                columnName: formArr[i].columnName,
                columnComment: formArr[i].columnComment
                  ? formArr[i].columnComment
                  : formArr[i].columnName,
                dataType: formArr[i].dataType,
                columnType: formArr[i].dataTypeInt,
                columnLength: formArr[i].length,
                pointLength: formArr[i].pointLength,
                defaultValue: formArr[i].columnDefault,
                isPk: formArr[i].isPk ? 'YES' : 'NO',
                isNull: this.DB_TYPE === '2' ? (formArr[i].isNullable === 't' ? 'NO' : 'YES') : formArr[i].isNullable,
                // 给id和instance_id行添加禁止选中标记_disabled: true
                _disabled:
                  !!(formArr[i].columnName === 'id' ||
                    formArr[i].columnName === 'instance_id')
              }
              this.dataArr.push(map)
            }
          }
        })
        .catch((e) => {
          this.$Notice.error({
            title: '操作失败！'
          })
        })
    },
    clickSingleDelBtn(tableRow) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (this.dataArr.length <= 1) {
          this.$Message.error({
            background: true,
            content: '删除失败！至少保留一条字段信息！！'
          })
        } else {
          // 参数处理
          let temArr = [tableRow.row.columnName]
          let paramArr = this.dataArr.filter((item) => {
            return temArr.indexOf(item.columnName) === -1
          })
          let columns = paramArr.map((item) => {
            let copyitem = JSON.parse(JSON.stringify(item))
            copyitem.pointLength = parseInt(copyitem.pointLength)
            copyitem.isNull = copyitem.isNull === 'YES' ? 1 : 0
            copyitem.isPk = copyitem.isPk === 'YES' ? 1 : 0
            return copyitem
          })
          let params = {
            tableName: this.curTableName,
            tableContent: this.curTableContent,
            columns
          }
          this.updateTable(params, 2)
        }
      })
    },
    /**
     *  表相关逻辑
     * */
    delTable(tableItem) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http
          .post('/api/schema/deleteFormTable/' + tableItem.table_name)
          .then((res) => {
            if (res.data.code === 400) {
              this.$Message.warning({
                background: true,
                content: '删除失败！' + res.data.msg
              })
            } else {
              this.$Message.success({
                background: true,
                content: '删除成功！'
              })
            }

            this.selectDB()
          })
          .catch((err) => {
            console.log(err)
          })
      })
    },
    confirmAdd() {
      this.$refs.addTableName.validate((valid) => {
        if (valid) {
          let params = {
            tableName: this.addTableJson.tableName,
            tableContent: this.addTableJson.tableContent
          }
          this.createTable(params)
          this.addModal = false
        } else {
          // addModal
          this.addModal = true
          this.$Message.error({
            background: true,
            content: '检验数据失败！'
          })
        }
      })
    },
    confirmCopy() {
      this.$refs.copyTableNameForm.validate((valid) => {
        if (valid) {
          this.$http
            .post('/api/schema/copyTable', this.copyTableData)
            .then((res) => {
              this.$Message.success({
                background: true,
                content: '新建表成功！'
              })
              this.selectDB()
              this.copyModal = false
            })
            .catch((err) => {
              console.log(err)
              this.copyModal = false
            })
        } else {
          // copyModal
          this.copyModal = true
          this.$Message.error({
            background: true,
            content: '检验数据失败！'
          })
        }
      })
    },
    selectDB() {
      let This = this
      This.$http
        .get('/api/schema/cscpHxTablesInfo')
        .then(function (response) {
          This.tableList = response.data.map((item) => {
            return item
          })
          This.copyTableList = response.data.map((item) => {
            return item
          })
          This.curTableName = This.tableList[0].table_name
          This.curTableContent = This.tableList[0].table_comment
          This.getColumns(This.curTableName)
        })
        .catch(function (error) {
          console.log(error)
        })
    }
  },
  mounted() {
    this.DB_TYPE = this.$util.Setting.dataBankType
    this.selectDB() // 表列表
    this.getColumnType() // 字段类型下拉菜单
    document.getElementById('rightRow').oncontextmenu = function (e) {
      return false
    }
    document.addEventListener('contextmenu', this.handleContextmenu, true)
    document.addEventListener('mouseup', this.handleContextmenu, true)
    if (this.DB_TYPE === '0') {
      this.mgc = this.$util.Setting.columnNamesql
    } else if (this.DB_TYPE === '1' || this.DB_TYPE === '2') {
      this.mgc = this.$util.Setting.columnNamegs
    }
  },
  destroyed() {
    document.removeEventListener('contextmenu', this.handleContextmenu, true)
    document.removeEventListener('mouseup', this.handleContextmenu, true)
  }
}
</script>
<style lang="less">
.dbBox {
  .ivu-table-wrapper {
    overflow: hidden;

    .ivu-table {
      overflow: scroll;
    }
  }
}
</style>
<style lang="less" scoped>
.ivu-select-dropdown ul {
  max-width: 140px;
  padding-right: 13px;
  padding-left: 23px;
}

::v-deep.ivu-col {
  height: 100%;
  overflow: hidden;
}

::v-deep.ivu-card {
  overflow: hidden;
  height: 100%;

  ::v-deep.ivu-card-body {
    height: 100%;
    display: flex;
    flex-direction: column;
    overflow: hidden;
  }
}

::v-deep .ivu-input-wrapper {
  margin: 0 !important;
}

::v-deep .ivu-card-head {
  border-bottom: 1px solid #e8eaec;
  padding: 14px 16px;
  line-height: 1;
  height: 42px;
}

::v-deep .ivu-card-extra {
  position: absolute;
  right: 16px;
}

.tableItem {
  height: 30px;
  width: 100%;
  margin-top: 10px;
  display: flex;
  justify-content: space-between;

  span {
    display: -webkit-box;
    overflow: hidden;
    margin-left: 5px;
    display: block;
    word-break: break-all;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 1;
    font-size: 15px;
  }

  .normal {
    width: 100%;
  }

  .active {
    background-color: rgba(131, 191, 248, 0.2);
    width: 100%;
    color: black;
  }
}

.formCard {
  width: 100%;
  opacity: 1;
  padding: 0 16px;
  position: relative;

  .tableBtn {
    width: 100%;
    text-align: left;
    padding: 0 0 16px;
  }

  ::v-deep .ivu-page {
    flex: 1 auto;
    align-self: flex-end;
    display: flex;
    align-items: flex-end;
    bottom: 16px;
    right: 32px;
  }

  ::v-deep .ivu-card-body {
    display: flex;
    flex-direction: column;
    height: 100%;
  }

  ::v-deep .ivu-form-item {
    margin-bottom: 0;
  }
}

::v-deep .ivu-btn {
  border-radius: 3px;
  margin-right: 15px;
}

.formCard:nth-child(2) {
  margin-top: 16px;
  flex: 1 auto;
}

.searchBox {
  display: flex;
  align-items: center;
  justify-content: space-between;

  button {
    width: 60px;
    height: 32px;
    border: none;
    cursor: pointer;
  }
}


</style>
