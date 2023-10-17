<template>
  <div class="form-area" style="width: 99%;">
      <Form ref="subFormRef" :model="subFormDetail" :label-width="120" style="width: 30%;margin-left: 29%;margin-top: 2%" >
        <Row>
          <Col span="22">
            <FormItem label="关联字段" prop="subFormFk">
              <Select v-model="subFormDetail.subFormFk" style="width:100%">
                <Option v-for="item in fields" :value="item.columnName" >{{ item.columnComment ?   item.columnName +"  "+ item.columnComment :item.columnName}}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="22">
            <FormItem label="排序" prop="sortNum">
              <InputNumber  v-model="subFormDetail.sortNum" style="width: 100%"></InputNumber>
            </FormItem>
          </Col>
        </Row>
      </Form>
  </div>
</template>

<script>
export default {
  name: 'subFormConfig',
  data () {
    return {
      subFormDetail: {
        subFormFk: null,
        sortNum: 1
      },
      fields: null
    }
  },
  props: {
    tableName: String,
    formId: String
  },
  methods: {
    cancel () {

    },
    init () {
      Promise.all([this.getColumns(this.tableName)]).then(res => {
        this.getFormInfo(this.formId)
      })
    },
    getFormInfo (formId) {
      this.$http
        .get('/api/lowcode/customize/cscpCustomizeVforms/' + formId)
        .then((res) => {
          this.subFormDetail.subFormFk = res.data.subFormFk
          this.subFormDetail.sortNum = res.data.sortNum
        })
        .catch(() => {
          this.$Message.error('行数据查询失败')
        })
    },
    getColumns (tableName) {
      const [url, param] = [
        '/api/cscpHxFormColums',
        {
          params: {
            tableName: tableName
          }
        }
      ]
      return new Promise((resolve, reject) => {
        this.$http.get(url, param).then((res) => {
          let formArr = res.data
          // 数据处理
          this.fields = []
          if (formArr.length > 0) {
            for (let i = 0; i < formArr.length; i++) {
              let map = {
                columnName: formArr[i].columnName,
                columnComment: formArr[i].columnComment
              }
              this.fields.push(map)
            }
          }
          resolve(res)
        }).catch((err) => {
          reject(err)
          this.$Notice.error({
            title: '操作失败！'
          })
        })
      })
    }
  },
  mounted () {
    this.init()
  }
}
</script>

<style lang="less" scoped>
  @import "~@/views/admin/styles/formStyle.less";

</style>
