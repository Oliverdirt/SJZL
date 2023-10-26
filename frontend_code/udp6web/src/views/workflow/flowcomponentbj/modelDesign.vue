<template>
  <div>
    <el-form ref="addTableName" :model="addTableJson" label-width="120px" :rules="rules" style="margin-bottom: 10px">
     <el-row>
        <el-col :span="12">
            <el-form-item label="数据模型名称" prop="tableName">
                <el-input v-model="addTableJson.tableName" placeholder="请输入数据模型名称" :disabled="formOpen" clearable />
            </el-form-item>
        </el-col>
     </el-row>
     <el-row>
        <el-col :span="12">
            <el-form-item label="数据模型描述" prop="tableContent">
                <el-input v-model="addTableJson.tableContent" placeholder="请输入数据模型描述" :disabled="formOpen" clearable />
            </el-form-item>
        </el-col>
     </el-row>
    </el-form>
    <!--列表-->
    <el-table :data="dataArr" :header-cell-style="{ 'background-color': '#d3e1f7' }" :row-style="{ height: '80px' }" style="width: 100%">
      <el-table-column type="index" label="序号" width="40"></el-table-column>
      <el-table-column prop="columnName" label="数据库字段名" width="170">
        <template v-slot="scope">
            <el-input v-model="scope.row.columnName" @blur="blur(scope.row)" :disabled="scope.row.editFlag" placeholder="数据库字段名">
            </el-input>
            <!-- <div v-show="!scope.row.columnName">
              <span style="color:red;position: absolute;padding-bottom: 20px;">数据库字段名不能为空</span>
            </div> -->
            <div v-show="scope.row.columnName && scope.row.columnNameSame">
              <span style="color:red;position: absolute;padding-bottom: 20px;">数据库字段名不能重复</span>
            </div>
            <div v-show="mgc.indexOf(scope.row.columnName) !== -1">
              <span style="color:red;position: absolute;padding-bottom: 20px;">{{
                  scope.row.columnName
                }}与数据库系统字段重复</span>
            </div>
        </template>
      </el-table-column>
      <el-table-column prop="columnComment" label="字段名称" width="150">
        <template v-slot="scope">
            <el-input v-model="scope.row.columnComment" @blur="blur(scope.row)" :disabled="scope.row.editFlag" placeholder="字段名称">
            </el-input>
            <!-- <div v-show="!scope.row.columnComment">
              <span style="color:red;position: absolute;padding-bottom: 20px;">字段名称不能为空</span>
            </div> -->
        </template>
      </el-table-column>
      <el-table-column prop="columnType" label="字段类型" width="150">
        <template v-slot="scope">
            <Select v-model="scope.row.columnType" placeholder="请选择" transfer  :disabled="scope.row.editFlag">
              <Option v-for="(item, index) in columnTypeArr" :value="item.typeNum" :key="index"
                      >{{ item.typeName }}
              </Option>
            </Select>
            <!-- <div v-show="!scope.row.columnType">
              <span style="color:red;position: absolute;">字段类型不能为空</span>
            </div> -->
          </template>
      </el-table-column>
      <el-table-column prop="columnLength" label="字段长度">
        <template v-slot="scope">
            <Input type="number" v-model="scope.row.columnLength" placeholder="字段长度" :disabled="scope.row.editFlag">
            </Input>
          </template>
      </el-table-column>
      <el-table-column prop="pointLength" label="小数点">
        <template v-slot="scope">
            <Input type="number" v-model="scope.row.pointLength" placeholder="小数点位数" :disabled="scope.row.editFlag"
                   ></Input>
          </template>
      </el-table-column>
      <el-table-column prop="isNull" label="允许空值">
        <template v-slot="scope">
            <RadioGroup v-model="scope.row.isNull" :disabled="scope.row.editFlag" type="button" button-style="solid">
              <Radio label="1" :disabled="scope.row.editFlag">是</Radio>
              <Radio label="0" :disabled="scope.row.editFlag">否</Radio>
            </RadioGroup>
          </template>
      </el-table-column>
      <el-table-column prop="isPk" label="是否为主键">
        <template v-slot="scope">
            <RadioGroup v-model="scope.row.isPk" type="button" button-style="solid">
              <Radio label="1" :disabled="scope.row.editFlag">是</Radio>
              <Radio label="0" :disabled="scope.row.editFlag">否</Radio>
            </RadioGroup>
          </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="100">
        <template slot-scope="scope">
          <!-- <el-button size="mini" type="primary" @click="addFn(scope.row)">添加</el-button> -->
          <el-button size="mini" type="text" @click="deleteFn(scope.row)" :disabled="scope.row.editFlag">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-button style="width: 100%;margin-left: 0" icon="el-icon-plus" plain @click="addFn"
                             type="primary">新建
    </el-button>
  </div>
</template>
<script>
export default {
  props: {
    formIdParams: String
  },
  data () {
    return {
      flag: false,
      mgc: [],
      formOpen: false,
      form: {},
      rowData: {},
      columnNameSame: false,
      addTableJson: {},
      columnTypeArr: [],
      dataArr: [
        {
          columnName: 'id',
          columnComment: '主键id',
          columnType: 5,
          columnLength: '20',
          pointLength: '',
          isPk: '1',
          isNull: '0',
          editFlag: true,
          columnNameSame: false,
        },
        {
          columnName: 'instance_id',
          columnComment: '流程实例ID',
          columnType: 15,
          columnLength: '64',
          pointLength: '',
          isPk: '0',
          isNull: '1',
          editFlag: true,
          columnNameSame: false,
        }
      ],
      DB_TYPE: '0',
      // 表单校验
      rules: {
        tableName: [{ required: true, message: '数据模型名称不能为空', trigger: 'blur' }],
        tableContent: [{ required: true, message: '数据模型描述不能为空', trigger: 'blur' }]
      }
    }
  },
  created () {
    this.getColumnType()
    this.formIdParams ? this.formIdParams : ''
    if (this.formIdParams) {
      this.pageId = this.formIdParams
    }
    if (this.$util.Setting.dataBankType === '0') {
      this.mgc = this.$util.Setting.columnNamesql
    } else if (this.$util.Setting.dataBankType === '1' || this.$util.Setting.dataBankType === '2') {
      this.mgc = this.$util.Setting.columnNamegs
    }
  },
  methods: {
    // 已完成流程点击模型设计
    modelEditFn (row) {
      this.flag = true
      this.$set(this.addTableJson, 'tableName', row.formTable)
      this.$set(this.addTableJson, 'tableContent', row.tableDesc)
      this.formOpen = true
      const [url, param] = [
        '/api/cscpHxFormColums',
        {
          params: {
            tableName: row.formTable
          }
        }
      ]
      this.$http.get(url, param).then((res) => {
        let formArr = res.data
        this.dataArr = []
        if (formArr.length > 0) {
          // 数据处理
          for (let i = 0; i < formArr.length; i++) {
            let map = {
              editFlag: !!(formArr[i].columnName === 'instance_id' || formArr[i].columnName === 'id'),
              columnName: formArr[i].columnName,
              columnComment: formArr[i].columnComment
                ? formArr[i].columnComment
                : formArr[i].columnName,
              columnType: formArr[i].dataTypeInt,
              columnLength: formArr[i].length,
              pointLength: formArr[i].pointLength,
              defaultValue: formArr[i].columnDefault,
              isPk: formArr[i].isPk ? '1' : '0',
              // isNull: this.DB_TYPE === '2' ? (formArr[i].isNullable === 't' ? '0' : '1') : formArr[i].isNullable,
              isNull: formArr[i].isNullable == 'YES' ? '1' : '0',
              // 给id和instance_id行添加禁止选中标记_disabled: true
              _disabled:
                  !!(formArr[i].columnName === 'id' ||
                    formArr[i].columnName === 'instance_id')
            }
            this.dataArr.push(map)
          }
        }
      })
    },
    // 点击25%传值this.pageId
    pageIdFn (row) {
      this.pageId = row
    },
    // 表单绑定模型
    formModelBind () {
      let params = {
        formId: this.pageId,
        formTable: this.addTableJson.tableName
      }
      this.$http.put('/api/lowcode/customize/cscpCustomizeVforms', params).then((res) => {
      })
    },
    // 校验
    blur (rowData) {
      let columnNameArr = []
      let datas = this.dataArr
      var validDataFlag = true
      // 字段校验
      for (let i = 0; i < datas.length; i++) {
        if (datas[i].columnName && datas[i].columnName != '') {
          if (columnNameArr.indexOf(datas[i].columnName) > -1) {
            if (datas[i].columnName == rowData.columnName) {
              rowData.columnNameSame = true
              validDataFlag = false
              break
            }
          }
          columnNameArr.push(datas[i].columnName)
        }
      }
      if (validDataFlag) {
        rowData.columnNameSame = false
      }
    },
    // 创建新的模型
    createdModelFn () {
      return new Promise((resolve) => {
        this.$refs.addTableName.validate((valid) => {
          if (!valid) return
          let columnNameArr = []
          let datas = this.dataArr
          // 字段校验
          for (let i = 0; i < datas.length; i++) {
            if (!datas[i].columnName || !datas[i].columnComment) {
              this.$message.error('数据库字段名及字段名称不能为空')
              return
            }
            if (columnNameArr.indexOf(datas[i].columnName) > -1) {
              this.$message.error('数据库字段名不能重复')
              return
            }
            if (this.mgc.indexOf(datas[i].columnName) !== -1) {
              this.$message.error('数据库字段名与系统字段重复')
              return
            }
            if (!datas[i].columnType) {
              this.$message.error('字段类型不能为空')
              return
            }
            columnNameArr.push(datas[i].columnName)
          }
          this.$emit('tableNameParams', this.addTableJson.tableName)
          const obj = this.dataArr
          let params = {
            columns: this.dataArr,
            tableName: this.addTableJson.tableName,
            tableContent: this.addTableJson.tableContent
          }
          this.$http.post('api/schema/createOrUpdateFormTable', params).then((res) => {
            // resolve(1)
            let params = {
              formId: this.pageId,
              formTable: this.addTableJson.tableName
            }
            if (!this.flag) {
              this.$http.put('/api/lowcode/customize/cscpCustomizeVforms', params).then((res) => {
                resolve(1)
              })
            }
            if (this.flag) {
              // window.location.reload()
              this.$parent.$parent.flowStepsVisiable = false
              this.queryList()
            }
          })
          //
        })
      })
    },
    // 流程列表
    queryList () {
      let params = {
        page: 1,
        size: 100000,
        category: 'lowCode'
      }
      this.$http
        .get('/api/flow/bpmn/page', {
          params: params
        })
        .then((res) => {
        })
        .catch(() => {
          this.$Message.error('列表查询失败')
        })
    },
    // 字段类型下拉数据
    getColumnType () {
      this.$http.get('/api/schema/getColumnTypeList').then((res) => {
        console.log(res, 'resresres')
        this.columnTypeArr = res.data
      })
    },
    // 新增模型字段
    addFn (val) {
      console.log(val, 'addFn')
      let mapKong = {
        key: Math.random() + '',
        columnName: '',
        columnComment: '',
        columnType: '',
        columnLength: '',
        pointLength: '',
        isPk: '0',
        isNull: '1',
        columnNameSame: false
      }
      this.dataArr.push(mapKong)
    },
    // 删除模型字段
    deleteFn (val) {
      console.log(val, 'deleteFn')
      this.dataArr = this.dataArr.filter((item) => {
        if (item.key) {
          return item.key != val.key
        } else {
          return item.columnName != val.columnName
        }
      })
    },
    getColumns (tableName) {
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
                columnType: formArr[i].dataTypeInt,
                columnLength: formArr[i].length,
                pointLength: formArr[i].pointLength,
                defaultValue: formArr[i].columnDefault,
                isPk: formArr[i].isPk ? '1' : '0',
                // isNull: this.DB_TYPE === '2' ? (formArr[i].isNullable === 't' ? '0' : '1') : formArr[i].isNullable,
                isNull: formArr[i].isNullable == 'YES' ? '1' : '0',
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
    }
  }
}
</script>
  <style scoped>

  </style>
