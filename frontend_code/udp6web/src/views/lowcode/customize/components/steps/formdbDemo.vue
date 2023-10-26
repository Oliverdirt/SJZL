<template>
  <Form :model="createPageForm" :label-width="120" label-position="right" :rules="createPageFormRules"
        ref="createPageRef">
    <FormItem label="页面名称" prop="pageName">
      <Input v-model="createPageForm.pageName" placeholder="请输入页面名称"></Input>
    </FormItem>
    <FormItem label="页面类型" prop="pageType">
      <Select v-model="createPageForm.pageType" @on-change="pageTypeChange">
        <Option value="1">列表</Option>
        <Option value="0">表单</Option>
      </Select>
    </FormItem>
    <FormItem label="页面属性" prop="tableType">
      <Select v-model="createPageForm.tableType" :disabled="tableTypeDisabled"
              :placeholder="tableTypeDisabled ? '请先选择所属模块' : '请选择'">
        <Option value="0" @click.native="getModelList(createPageForm.tableType)">单表页面</Option>
        <Option value="3" @click.native="getModelList(createPageForm.tableType)">视图页面</Option>
        <Option value="1" @click.native="getModelList(createPageForm.tableType)">主表页面</Option>
        <Option value="2" @click.native="getModelList(createPageForm.tableType)">子表页面</Option>
      </Select>
    </FormItem>
    <FormItem label="绑定数据模型" prop="modelId">
      <Select v-model="createPageForm.modelId" :disabled="modelDisabled">
        <Option v-for="(item, index) in modelList" :value="item.modelId" :key="index"
                @click.native="getTableList(createPageForm.modelId)">{{ item.modelName }}
        </Option>
      </Select>
    </FormItem>
    <FormItem label="绑定数据表" prop="tableName">
      <Select v-model="createPageForm.tableName" :disabled="tableDisabled" :multiple="createPageForm.tableType === '1'">
        <Option v-for="(item, index) in tableNameList" :value="item.tableName" :key="index">{{ item.tableName }}
        </Option>
      </Select>
    </FormItem>
    <!--    选择api 列表显示  -->
    <FormItem label="页面初始化api" prop="api">
      <Select v-model="createPageForm.api">
        <Option v-for="(item, index) in apiList" :value="JSON.stringify(item)" :key="index+'apiName'">{{
            item.name
          }}
        </Option>
      </Select>
    </FormItem>
    <FormItem v-show="createPageForm.pageType === '1'" label="是否下钻" prop="hasChild">
      <Select v-model="createPageForm.hasChild">
        <Option value="1">是</Option>
        <Option value="0">否</Option>
      </Select>
    </FormItem>
    <!--    end   -->
    <FormItem v-show="createPageForm.pageType === '1'&& createPageForm.tableType === '2'" label="选择主表页面"
              prop="mainPageId">
      <Select v-model="createPageForm.mainPageId" clearable>
        <Option v-for="(item, index) in mainPageList" :value="item.pageId" :key="index">{{ item.pageName }}
        </Option>
      </Select>
      <!--      <div v-show="mainPageIdRequired">-->
      <!--        <span style="color: red">-->
      <!--          页面类型为子表页面的列表页时主表页面必填-->
      <!--        </span>-->
      <!--      </div>-->
    </FormItem>
  </Form>
</template>
<script>
export default {
  name: 'FormDB',
  computed: {
    mainPageIdRequired: function () {
      return this.createPageForm.pageType === '1' && this.createPageForm.tableType === '2' && !this.createPageForm.mainPageId
    }
  },
  watch: {
    tableName(newValue, oldValue) {
      // 每当inputValue的值改变则更新 update:inputValue, 并且把值传过去
      this.$emit('update:tableName', newValue)
    },
    tableDesc(newValue, oldValue) {
      // 每当inputValue的值改变则更新 update:inputValue, 并且把值传过去
      this.$emit('update:tableDesc', newValue)
    }
  },
  props: {
    pageParam: Object
  },
  created() {
    this.createPageForm.moduleId = this.pageParam.moduleId //模块id写死
    this.moduleName = this.pageParam.moduleName  //模块名称写死
    this.resert()
  },
  data() {
    let validateFormName = (rule, value, callback) => {
      const [url] = [
        `/api/lowcode/customize/cscpCustomizeVpages?pageName.equals=${value}`
      ]
      this.$http
        .get(url)
        .then((response) => {
          if (response.data.data.length > 0) {
            callback(new Error('页面名已重复'))
          } else {
            callback()
          }
        })
        .catch(() => {
          callback(new Error('异步校验出错！'))
        })
    }
    let validateTableName = (rule, value, callback) => {
      // 需要对createPageForm.tableName的类型进行判断,如果是字符串,非空就可以;如果是数组,则必须包含主表的tableName项
      if (!value || JSON.stringify(value) === '[]') {
        callback(new Error('数据表选项不能为空'))
      } else if (Array.isArray(value)) {
        // value的数组里必须包含主表的tableName项
        if (!value.includes(this.tableNameList[0].tableName)) {
          callback(new Error('主表页面的数据表可以多选但必须包含主表数据表,即必须选中第一项'))
        } else {
          callback()
        }
      } else {
        callback()
      }
    }
    return {
      apiList: [
        {apiName: '/api/getUserName'},
        {apiName: '/api/getRoleName'}
      ],
      pageType: '',
      moduleId: null,
      moduleList: [],
      pageName: null,
      modelId: null,
      modelList: [],
      tableName: null,
      tableNameList: [],
      tableList: {},
      tableType: null,
      mainPageId: null,
      mainPageList: [],
      createPageForm: {
        moduleId: '',
        pageName: '',
        pageType: '',
        tableType: '',
        modelId: '',
        tableName: '',
        mainPageId: '',
        api: '', // 自定义api
        hasChild: '' // 是否下钻
      },
      tableTypeDisabled: true,
      modelDisabled: true,
      tableDisabled: true,
      subPageFk: null,
      modelName: null,
      moduleName: null,
      mainPageDisabled: false,
      createPageFormRules: {
        tableType: [
          {required: true, message: '页面属性不能为空', trigger: 'change'}
        ],
        moduleId: [
          {required: true, message: '所属模块不能为空', trigger: 'change'}
        ],
        pageName: [
          {required: true, message: '页面名称不能为空', trigger: 'blur'},
          {required: true, validator: validateFormName, trigger: 'blur'}
        ],
        modelId: [
          {required: true, message: '数据模型不能为空', trigger: 'change'}
        ],
        tableName: [
          {required: true, validator: validateTableName, trigger: 'change'}
        ],
        pageType: [
          {required: true, message: '页面类型不能为空', trigger: 'change'}
        ]
        // mainPageId: [
        //   { validator: validateMainPageId, trigger: 'blur' }
        // ]
      }
    }
  },

  methods: {
    pageTypeChange(pageType) {
      if (pageType === '0' && this.createPageForm.tableType === '2') {
        this.createPageForm.mainPageId = ''
        this.mainPageDisabled = true
      }
    },
    initModuleList() {
      this.$http.get('/api/cscpCustomizeModules').then((res) => {
        this.moduleList = res.data.data
      })
    },
    resert() {
      this.tableTypeDisabled = false
      this.modelDisabled = false
      this.tableDisabled = false
      this.mainPageDisabled = false
      this.createPageForm.modelId = ''
      this.createPageForm.tableName = ''
      this.createPageForm.tableType = ''
      this.createPageForm.mainPageId = ''
      this.modelList = []
      this.tableList = []
      this.tableNameList = []
      this.modelName = null
      this.subPageFk = null
    },
    getModelList(tableType, moduleId, modelId, initFlag) {
      if (!tableType) {
        tableType = this.createPageForm.tableType
      }
      this.modelList = []

      if ((this.createPageForm.moduleId && tableType) || (moduleId && tableType)) {
        var tableModelId = tableType === '0' ? '1' : tableType === '3' ? '3' : '2'
        var moid = this.createPageForm.moduleId ? this.createPageForm.moduleId : moduleId
        this.$http.get('/api/cscpCustomizeModels?moduleId.equals=' + moid + '&modelType.equals=' + tableModelId).then((res) => {
          this.modelList = res.data.data
          this.modelDisabled = false
          if (initFlag) {
            this.getTableList(modelId, tableType, res.data.data, initFlag)
          }
        })
      } else {
        this.modelList = []
        this.modelDisabled = true
      }

      if (!initFlag) {
        this.createPageForm.modelId = ''
        this.createPageForm.tableName = ''
        this.subPageFk = null
        this.createPageForm.mainPageId = null
        this.mainPageDisabled = true
      }
      if (tableType === '2') {
        if (this.createPageForm.pageType === '1') {
          this.mainPageDisabled = false
        } else {
          this.mainPageDisabled = true
        }
      }
    },
    getTableList(modelId, tableType, modelList, initFlag) {
      modelId = modelId || this.createPageForm.modelId
      // 设置数据模型名称
      if (this.modelList) {
        for (let k = 0; k < this.modelList.length; k++) {
          if (modelId == this.modelList[k].modelId) {
            this.modelName = this.modelList[k].modelName
            continue
          }
        }
      }
      this.tableList = []
      this.tableNameList = []
      if (modelId) {
        var tableId = null
        var mdlList = (this.modelList && this.modelList.length > 0) ? this.modelList : modelList
        for (let i = 0; i < mdlList.length; i++) {
          if (modelId == mdlList[i].modelId) {
            tableId = mdlList[i].tableId
            continue
          }
        }
        this.$http.get('/api/lowcode/modelDesign/cscpHxFormTablesAll/' + tableId).then((res) => {
          this.tableList = res.data
          if (this.tableList) {
            let tableListData = this.tableList
            this.$emit('formdbDemoEmit', tableListData)
          }
          if (this.tableList) {
            var tbtype = this.createPageForm.tableType ? this.createPageForm.tableType : tableType
            // 子表
            if (tbtype === '2') {
              // var childTableList = this.tableList.subTableName.split(',');
              var childTableList = []
              if (this.tableList.cscpHxFormTables &&
                this.tableList.cscpHxFormTables.length &&
                this.tableList.cscpHxFormTables.length > 0) {
                this.tableList.cscpHxFormTables.map(item => {
                  childTableList.push(item.tableName)
                })
              }
              if (childTableList) {
                for (let j = 0; j < childTableList.length; j++) {
                  this.tableNameList.push({'tableName': childTableList[j]})
                }
              }
              // 主表信息 用于下拉显示
              this.$http.get('/api/lowcode/customize/cscpCustomizeVpages?moduleId.equals=' + this.createPageForm.moduleId + '&modelId.equals=' + modelId + '&tableType.equals=1').then((res) => {
                this.mainPageList = res.data.data
              })
              if (this.createPageForm.pageType === '1') {
                this.mainPageDisabled = false
              } else {
                this.mainPageDisabled = true
              }
            } else {
              // 单表或主表
              this.tableNameList.push({'tableName': this.tableList.tableName})
              if (tbtype === '1') { // 把子表的表名也都push到tableNameList
                var childTableList = this.tableList.cscpHxFormTables.map(item => {
                  return item.tableName
                })
                if (childTableList && childTableList.length > 0) {
                  for (let j = 0; j < childTableList.length; j++) {
                    this.tableNameList.push({'tableName': childTableList[j]})
                  }
                } else {
                  let tempArr = this.tableList.subTableName.split(',')
                  for (let k = 0; k < tempArr.length; k++) {
                    this.tableNameList.push({'tableName': tempArr[k]})
                  }
                }

              }
              this.mainPageDisabled = true
            }
          }
        })
        this.tableDisabled = false
      } else {
        this.tableList = []
        this.tableNameList = []
        this.tableDisabled = true
      }
      if (!initFlag) {
        this.tableName = ''
        this.createPageForm.tableName = ''
      }
    },
    getColumns(tableName) {
      const [url, param] = [
        '/api/cscpHxFormColums',
        {
          params: {
            tableName: tableName
          }
        }
      ]
      this.$http.get(url, param).then((res) => {
        let formArr = res.data
        if (formArr.length > 0) {
          // 找出主键
          let keyArr = formArr.filter(item => {
            return item.isKey
          })
          keyArr.length ? window.localStorage.setItem('keyName', keyArr[0].columnName) : ''
          // 数据处理
          this.dataArr = []
          for (let i = 0; i < formArr.length; i++) {
            let map = {
              columnName: formArr[i].columnName,
              columnComment: formArr[i].columnComment,
              columnLength: formArr[i].length,
              pointLength: formArr[i].pointLength,
              defaultValue: formArr[i].columnDefault,
              columnType: formArr[i].dataType
            }
            this.dataArr.push(map)
          }
        }
      }).catch(() => {
        this.$Notice.error({
          title: '操作失败！'
        })
      })
    },
    getAllApi() {
      // 要查询所有api 所以页容量设置为 10000
      this.$http.post('/api/source/queryListSourceConfig?name=&page=1&size=10000').then(res => {
        console.log(res.data.data.data, 'res.data')
        this.apiList = res.data.data.data
      }).catch(err => {
        console.log(err)
        this.$message.error('获取api列表失败！')
      })
    }
  },
  mounted() {
    this.initModuleList()
    // 拿到所有的api
    this.getAllApi()
  }
}
</script>
