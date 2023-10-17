<template>
  <div class="form-area" style="width: 99%;">
    <Form :model="formDetail" :label-width="100" ref="formDetail" :rules="ruleValidate"
          label-position="left" style="margin-top: 20px">
      <Row :gutter="48">
        <Col span="12">
          <FormItem label="表名" prop="tableName">
            <Input class="input-width" v-model="formDetail.tableName"></Input>
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="表单名称" prop="tableContent">
            <Input class="input-width" v-model="formDetail.tableContent"></Input>
          </FormItem>
        </Col>
      </Row>
      <Row :gutter="48">
        <Col span="12">
          <FormItem label="包名" prop="packageName">
            <Input class="input-width" v-model="formDetail.packageName"></Input>
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="表单类型" prop="formType">
            <Select v-model="formDetail.formType">
              <Option :value="1">单表</Option>
              <Option :value="2">树表</Option>
              <Option :value="3">主子表</Option>
            </Select>
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="自定义模板" prop="formSuite">
            <Select v-model="formDetail.formSuite">
              <Option value="1">内置mybatis模板</Option>
              <Option value="2">内置mybatis plus模板</Option>
              <Option :value="suite.id" v-for="suite in suiteList" :key="suite.id">{{ suite.suiteName }}
              </Option>
            </Select>
          </FormItem>
        </Col>
      </Row>
      <Row :gutter="48">
        <Col span="48">
          <FormItem label="生成代码方式" prop="genType">
            <RadioGroup v-model="formDetail.genType">
              <Radio :label="0">zip压缩包</Radio>
              <Radio :label="1">自定义路径</Radio>
            </RadioGroup>
          </FormItem>
        </Col>
      </Row>
      <Row :gutter="48" v-show="formDetail.genType == '1'">
        <Col span="24">
          <FormItem label="生成路径" prop="genPath" :label-width="100">
            <Input class="input-width" v-model="formDetail.genPath"></Input>
          </FormItem>
        </Col>
      </Row>
      <div v-show="formDetail.formType == '2'">
        <h4 class="form-header">其他信息</h4>
        <Row :gutter="40">
          <Col span="12">
            <FormItem label="树编码字段" prop="treeCode">
              <Select v-model="options.treeCode">
                <Option v-for="(i, index) in dataArr" :value="i.columnName" :key="index">{{ i.columnName }}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="树父编码字段" prop="treeParentCode" :label-width=120>
              <Select v-model="options.treeParentCode">
                <Option v-for="(i, index) in dataArr" :value="i.columnName" :key="index">{{ i.columnName }}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="树名称字段" prop="treeName">
              <Select v-model="options.treeName">
                <Option v-for="(i, index) in dataArr" :value="i.columnName" :key="index">{{ i.columnName }}</Option>
              </Select>
            </FormItem>
          </Col>
        </Row>
      </div>
      <div v-show="formDetail.formType == '3'">
        <h4 class="form-header">关联信息</h4>
        <Row :gutter="48">
          <Col span="12">
            <FormItem label="关联子表的表名" prop="subTableName" :label-width=150>
              <Select v-model="formDetail.subTableName" @on-change="changeSubTable">
                <Option v-for="(i, index) in glzbdbmOptions" :text="i.tableName" :value="i.tableName" :key="index">
                  {{ i.tableName }}
                </Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="子表关联的外键名" prop="subTableFkName" :label-width=150>
              <Select v-model="formDetail.subTableFkName">
                <Option v-for="(i, index) in subColumns" :value="i.columnName" :key="index">{{ i.columnName }}</Option>
              </Select>
            </FormItem>
          </Col>
        </Row>
      </div>
    </Form>
    <form-tab ref="formTab" :close-flag="closeFlag" :add-flag="addFlag"></form-tab>
  </div>
</template>
<script>
import FormTab from './FormTab'
import {
  getCustomerSuiteList,
  getSubFormTableInfoByParams,
  getFromColumnInfoByParams,
  validFromTableByParams
} from '@/api/generator/generator'

export default {
  name: 'formDetail',
  components: { FormTab },
  props: {
    closeFlag: {
      type: Boolean,
      default: false
    },
    addFlag: {
      type: String,
      default: 'add'
    },
    tableData: {
      type: Array
    }
  },
  mounted () {
    // 获取自定义模板列表
    this.getCustomerSuite()
  },
  watch: {
    closeFlag: function (val) {
      if (!val) {
        this.formName = ''
        this.subColumns = []
        this.options = {
          treeCode: '',
          treeName: '',
          treeParentCode: ''
        }
        this.formDetail = {
          tableName: null,
          tableContent: null,
          packageName: null,
          genType: 0,
          genPath: null,
          formType: 1,
          formSuite: 1,
          subTableName: null,
          subTableFkName: null

        }
      }
    },
    currentData (v) {
      setTimeout(() => {
        this.dataArr = this.$store.state.tabdata.info
        // this.isLoading = false;
      }, 20)
    },
    'formDetail.formType': function (val) {
      if (val === 2) {
        this.formDetail.options = this.options
      } else {
        this.$delete(this.formDetail, 'options')
      }
      if (val === 3 && this.formDetail.subTableName) {
        this.changeSubTable(this.formDetail.subTableName)
      }
      if (val === 3) {
        this.listTable()
      }
    }
  },
  computed: {
    currentData () {
      return this.$store.state.tabdata.info
    }
  },
  data () {
    let validateGenPath = (rule, value, callback) => {
      if (this.formDetail.genType === 0) {
        callback()
      } else {
        if (!value) {
          callback(new Error('生成路径不能为空'))
        } else {
          callback()
        }
      }
    }
    let validateSubTableName = (rule, value, callback) => {
      if (this.formDetail.formType === 1 || this.formDetail.formType === 2) {
        callback()
      } else {
        if (!value) {
          callback(new Error('子表名不能为空'))
        } else {
          callback()
        }
      }
    }
    let validateSubTableFkName = (rule, value, callback) => {
      if (this.formDetail.formType === 1 || this.formDetail.formType === 2) {
        callback()
      } else {
        if (!value) {
          callback(new Error('子表外键不能为空'))
        } else {
          callback()
        }
      }
    }

    let validateTreeCode = (rule, value, callback) => {
      if (this.formDetail.formType === 1 || this.formDetail.formType === 3) {
        callback()
      } else {
        if (!this.options.treeCode) {
          callback(new Error('树编码字段不能为空'))
        } else {
          callback()
        }
      }
    }
    let validateTreeParentCode = (rule, value, callback) => {
      if (this.formDetail.formType === 1 || this.formDetail.formType === 3) {
        callback()
      } else {
        if (!this.options.treeParentCode) {
          callback(new Error('树父编码字段不能为空'))
        } else {
          callback()
        }
      }
    }

    let validateTreeName = (rule, value, callback) => {
      if (this.formDetail.formType === 1 || this.formDetail.formType === 3) {
        callback()
      } else {
        if (!this.options.treeName) {
          callback(new Error('树名称字段不能为空'))
        } else {
          callback()
        }
      }
    }

    let validateTableName = (rule, value, callback) => {
      let pattern = /^[_A-Za-z0-9]+$/
      let reg = /,*[A-Z]+.*/
      if (value.length > 30) {
        callback(new Error('表名不可以超过30位'))
        return
      }
      if (!pattern.test(value)) {
        callback(new Error('表名必须是数字或字母或下划线'))
        return
      }
      if (reg.test(value)) {
        callback(new Error('表名不能含有大写字母！'))
      }
      // const [url, httpConfig] = [
      //   '/api/validation/cscpHxFormTable',
      //   {
      //     params: { name: value }
      //   }
      // ]
      if (value !== this.formName) {
        validFromTableByParams({ name: value })
          // this.$http.get(url, httpConfig)
          .then(response => {
            if (response.data.code === -1) {
              callback(new Error(response.data.msg))
            } else {
              callback()
            }
          }).catch(() => {
          callback(new Error('异步校验出错！'))
        })
      } else {
        callback()
      }
    }
    return {
      glzbdbmOptions: [],
      formName: '',
      dataArr: [],
      subColumns: [],
      suiteList: [],
      options: {
        treeCode: '',
        treeName: '',
        treeParentCode: ''
      },
      formDetail: {
        tableName: null,
        tableContent: null,
        packageName: null,
        genType: 0,
        formSuite: 1,
        genPath: null,
        formType: 1,
        subTableName: null,
        subTableFkName: null
      },
      ruleValidate: {
        tableName: [
          { required: true, message: '请输入表名', trigger: 'blur' },
          { validator: validateTableName, trigger: 'blur' }
        ],
        tableContent: [
          { required: true, message: '请输入表单名称', trigger: 'blur' },
          { max: 50, message: '表单名称不可以超过50位', trigger: 'blur' }
        ],
        packageName: [
          { required: true, message: '请输入包名', trigger: 'blur' },
          { max: 50, message: '包名长度不可以超过50位', trigger: 'blur' }
        ],
        formType: [
          { required: true, message: '请选择表单类型', trigger: 'change', type: 'number' }
        ],
        genPath: [
          { required: true, validator: validateGenPath, trigger: 'blur' }
        ],
        subTableName: [
          { required: true, validator: validateSubTableName, trigger: 'change' }
        ],
        subTableFkName: [
          { required: true, validator: validateSubTableFkName, trigger: 'change' }
        ],
        treeCode: [
          { required: true, validator: validateTreeCode, trigger: 'change' }
        ],
        treeParentCode: [
          { required: true, validator: validateTreeParentCode, trigger: 'change' }
        ],
        treeName: [
          { required: true, validator: validateTreeName, trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    getCustomerSuite () {
      getCustomerSuiteList()
        // this.$http.get('/api/codeTemplateSuitesList')
        .then(res => {
          this.suiteList = res.data.data
        }).catch(function (error) {
        console.log(error)
      })
    },
    listTable () {
      let This = this
      // const [url, httpConfig] = [
      //   '/api/cscpHxAllSubFormTables',
      //   {
      //     params: {
      //       tableName: This.formDetail.tableName
      //     }
      //   }
      // ]
      const queryParams = {
        tableName: This.formDetail.tableName
      }
      getSubFormTableInfoByParams(queryParams)
        // This.$http.get(url, httpConfig)
        .then(function (response) {
          This.glzbdbmOptions = response.data.data
        }).catch(function (error) {
        console.log(error)
      })
    },
    initFormTableName (tableName) {
      if (tableName) {
        this.formDetail.tableName = tableName
        // const [url, param] = [
        //   '/api/cscpHxFormColums', {
        //     params: {
        //       tableName: tableName
        //     }
        //   }
        // ]
        const queryParams = {
          tableName: tableName
        }
        getFromColumnInfoByParams(queryParams)
          // this.$http.get(url, param)
          .then(res => {
            let formArr = res.data
            if (formArr.length > 0) {
              this.dataArr = []
              for (let i = 0; i < formArr.length; i++) {
                let map = {
                  columnName: formArr[i].columnName,
                  columnComment: formArr[i].columnComment,
                  columnLength: formArr[i].length,
                  pointLength: formArr[i].pointLength,
                  defaultValue: formArr[i].columnDefault,
                  columnType: formArr[i].dataTypeInt,
                  isNull: formArr[i].isNullableInt === 1,
                  isPk: formArr[i].isKey === 1,
                  orderNum: formArr[i].ordinalPositionInt,
                  isForm: false,
                  isList: false,
                  controlLength: null,
                  isQuery: false,
                  queryType: null,
                  showType: null
                }
                this.dataArr.push(map)

                if (map.isPk) {
                  this.$refs['formTab'].$refs['FormData'].editDisabled = false
                }
              }
              this.$store.commit('setInfo', this.dataArr)
            }
          }).catch((error) => {
          this.$Notice.error({
            title: '操作失败！'
          })
          console.log(error)
        })
      }
    },
    initData (data) {
      this.formName = data.tableName
      this.formDetail = data
      if (this.formDetail.options) {
        this.options = JSON.parse(this.formDetail.options) || {
          treeCode: '',
          treeName: '',
          treeParentCode: ''
        }
      }
    },
    changeSubTable (val) {
      if (!val) {
        return
      }
      getFromColumnInfoByParams({
        tableName: val
      })
        // this.$http.get('/api/cscpHxFormColums', {
        //   params: {
        //     tableName: val
        //   }
        // })
        .then((response) => {
          this.subColumns = response.data
        })
    }
  }
}
</script>
<style lang="less" scoped>
@import "../../admin/styles/formStyle.less";
/** 表单布局 **/
.form-header {
  font-size: 15px;
  color: #6379bb;
  border-bottom: 1px solid #ddd;
  margin: 8px 0px 25px 0px;
  padding-bottom: 5px
}
</style>
