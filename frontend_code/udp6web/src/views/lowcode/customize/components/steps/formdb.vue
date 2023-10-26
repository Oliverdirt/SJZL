<template>
  <div class="user-mg" style="height: 100%">
    <Row :gutter="16" style="height: 100%">
      <Col span="4" style="">
      <Card :bordered="false" :dis-hover="true" :shadow="false" style="height: 100%">
        <label>请选择数据表</label>
        <RadioGroup v-model="tableName" vertical @on-change="changeTable(tableName)">
          <Radio :disabled="isDisable" v-for="(item, index) in tableArr" :label="item.table_name" :key="index"></Radio>
        </RadioGroup>
      </Card>
      </Col>
      <Col span="20">
      <div class="formCard-content">
        <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        </Card>
        <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
          <div>
            <Table border :columns="columns" :data="dataArr"></Table>
          </div>
        </Card>
      </div>
      </Col>
    </Row>
  </div>
</template>
<script>
export default {
  name: 'FormDB',
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
    isDisable: Boolean,
    formId1: String,
  },
  data() {
    return {
      tableArr: [],
      dataArr: [],
      detail: {},
      tableName: null,
      tableDesc: null,
      columns: [
        {
          title: '数据库字段名',
          key: 'columnName'
        },
        {
          title: '字段名称',
          key: 'columnComment'
        },
        {
          title: '字段类型',
          width: 150,
          key: 'columnType'
        },
        {
          title: '字段长度',
          key: 'columnLength'
        },
        {
          title: '小数点',
          key: 'pointLength'
        },
        {
          title: '默认值',
          key: 'defaultValue'
        }
      ]
    }
  },

  methods: {
    changeTable(tableName) {
      if (tableName) {
        for (var i = 0; i <= this.tableArr.length; i++) {
          if (this.tableArr[i].table_name == tableName) {
            this.tableDesc = this.tableArr[i].table_comment ? this.tableArr[i].table_comment : this.tableArr[i].table_name
            break
          }
        }
        this.getColumns(tableName)
      }
    },
    getMainTable() {
      console.log('this.formId1', this.formId1)
      if (this.formId1 && this.formId1 != '') {
        // 查询主表信息
        // /api/lowcode/customize/getMainCscpCustomizeVform/{formId}
        this.$http.get('/api/lowcode/customize/getMainCscpCustomizeVform/' + this.formId1).then((res) => {
          this.mainTable = res.data
          console.log('mainTable>>>>>>>>>>>', res.data)
          this.selectDB()
        }).catch(() => {
          this.$Notice.error({
            title: '操作失败！'
          })
        })
      } else {
        this.selectDB()
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
    selectDB() {
      let This = this
      This.$http
        .get('/api/schema/cscpHxTablesInfo')
        .then(function (response) {
          This.tableArr = response.data
          if (This.mainTable) {
            This.tableArr = response.data.filter(t => {
              return t.table_name !== This.mainTable.formTable
            })
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    }
  },
  mounted() {
    this.dataArr = []
    this.getMainTable()
    // this.selectDB()
  }
}
</script>
