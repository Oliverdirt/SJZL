<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea" >
          <!--顶部查询条件-->
          <Form :model="topicForm" >
            <Row :gutter="48">
              <Col span="24" style="text-align: right;">
                <FormItem>
                  <Button type="primary" @click="save">保存</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <el-table :data="tableData" stripe >
          <el-table-column prop="name" label="环境"></el-table-column>
          <el-table-column label="权限">
            <template slot-scope="scope">
              <el-checkbox label="新增" v-model="scope.row.auth.add"></el-checkbox>
              <el-checkbox label="删除" v-model="scope.row.auth.remove"></el-checkbox>
              <el-checkbox label="查询" checked disabled></el-checkbox>
            </template>
          </el-table-column>
        </el-table>
      </Card>
    </div>
  </div>
</template>
<script>
import {
  operationAuthByData,
  getAllSourceAuth,
  deleteTopicByData,
  exportTopicByData
} from '@/api/kafka/kafka'
export default {
  name: 'kafka-colony',

  data () {
    return {
      // 分页参数
      pageLength: 6, // 每页条数
      pageTotal: 0, // 总数
      pageOffset: 0, // 页码
      formItem: {
        'partition': 1,
        'replica': 1
      },
      kafkaOptions: [],
      // 查询参数
      topicForm: {
        postCode: null,
        postName: null,
        status: null
      },
      isNodeEdited: false,
      rules: {},
      tableData: []
    }
  },
  mounted () {
    this.initTable()
  },
  methods: {
    // 新建
    save () {
      let p = {}
      for (let item of this.tableData) {
        p[item.id] = item.auth
      }
      let data = new FormData()
      data.append('param', JSON.stringify(p))
      operationAuthByData(data)
      // this.$http.post('/api/kafka/auth', data)
        .then((response) => {
          this.$Message.success('授权成功')
          this.initTable()
        }).catch((error) => {
          console.log(error)
          this.$Message.error('授权失败')
        })
    },
    initTable () {
      getAllSourceAuth()
      // this.$http.post('/api/kafka/getAllSourceAuth')
        .then(res => {
          this.tableData = res.data
        })
    },
    // 编辑
    edit (id) {
      this.$router.push({
        name: 'kafka-configure-form',
        query: {
          id: id
        }
      })
    },
    // 删除
    delete (name) {
      let _this = this
      let data = new FormData()
      data.append('topic', name)
      data.append('sourceId', this.topicForm.status || '')
      this.$Modal.confirm({
        title: '确定删除？',
        onOk () {
          deleteTopicByData(data)
          // _this.$http.post('/api/kafka/deleteTopic', data)
            .then(response => {
              if (response.status === 200) {
                _this.$Notice.success({
                  title: '删除成功'
                })
                _this.listTable()
              } else {
                _this.$Notice.error({
                  title: '删除失败'
                })
              }
            })
        }
      })
    },
    // 查询
    search () {
      this.pageOffset = 0
      this.listTable()
    },
    handlePageChange (current) {
      this.pageOffset = current - 1
      this.listTable()
    },
    // 导出按钮
    handleExport () {
      this.$Modal.confirm({
        title: '是否确认导出所有数据项？',
        onOk () {
          this.$http.defaults.timeout = 15000 * 60
          exportTopicByData('blob')
          // this.$http.post(`/api/topicForms/export`, {}, {
          //   responseType: 'blob'
          // })
            .then(res => {
              const link = document.createElement('a')
              const blob = new Blob([res.data], { type: 'application/vnd.ms-excel' })
              link.style.display = 'none'
              link.href = URL.createObjectURL(blob)
              document.body.appendChild(link)
              link.click()
              URL.revokeObjectURL(link.href)
              document.body.removeChild(link)
              this.$Notice.success({
                title: '导出数据成功'
              })
            }).catch(() => {
              this.$Notice.error({
                title: '导出数据失败'
              })
            })
        }
      })
    }
  }
}
</script>
<style scoped lang="less">
@import "../../admin/styles/formStyle.less";
.query-model {
  width: 170px;
}

.page-area {
  display: flex;
  justify-content: flex-end;
  padding-top: 35px;
  padding-right: 50px;
}

</style>
