<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea" >
          <!--顶部查询条件-->
          <Form :model="topicForm">
            <Row :gutter="48">
              <Col span="8">
                <FormItem>
                  <Select v-model="topicForm.status" placeholder="选择kafka环境" class="query-model" style="width: 100%" clearable @on-change="search">
                    <Option v-for="option in kafkaOptions" :value="option.id" :key="option.id">
                      {{option.name}}</Option>
                  </Select>
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <Table  :columns="columns" :data="tableData" border></Table>
      </Card>
    </div>
    <!--顶部查询条件-->

  </div>
</template>
<script>
import {
  createTopic,
  getSourceInfoByParams,
  getClusterInfo,
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
      columns: [
        {
          title: 'broker id',
          render: (h, params) => {
            return h('div', [h('span', params.row.idStr),
              h('tag', {
                props: {
                  color: 'error',
                  size: 'small'
                },
                style: {
                  marginLeft: '5px',
                  display: params.row.controller ? '' : 'none'
                }
              // }, params.row.controller ? 'controller' : 'controller')
              }, 'controller')
            ])
          }
        },
        {
          key: 'host',
          title: 'host'
        },
        {
          key: 'port',
          title: '端口'
        }
      ],
      tableData: []
    }
  },
  mounted () {
    this.initOptions()
  },
  methods: {
    formatTime (UTCDateString) {
      if (!UTCDateString) {
        return '-'
      }

      function formatFunc (str) { // 格式化显示
        return str > 9 ? str : '0' + str
      }

      var date2 = new Date(UTCDateString) // 这步是关键
      var year = date2.getFullYear()
      var mon = formatFunc(date2.getMonth() + 1)
      var day = formatFunc(date2.getDate())
      var hour = date2.getHours()
      var min = formatFunc(date2.getMinutes())
      var second = date2.getSeconds()
      var dateStr = year + '-' + mon + '-' + day + ' ' + hour + ':' + min + ':' + second
      return dateStr
    },
    handleSubmit () {
      if (!this.topicForm.status) {
        this.$Message.error('请先选择！！')
        this.isNodeEdited = false
      }
      let data = new FormData()
      data.append('name', this.formItem.name)
      data.append('partition', this.formItem.partition)
      data.append('replica', this.formItem.replica)
      data.append('sourceId', this.topicForm.status || '')
      createTopic(data)
      // this.$http.post('/api/kafka/createTopic', data)
        .then(res => {
          this.formItem = {
            'partition': 1,
            'replica': 1
          }
          this.isNodeEdited = false
          this.$Message.success('新增集群成功！')
          this.search()
        })
    },
    cancel () {
      this.isNodeEdited = false
    },
    initOptions () {
      // const [url, param] = [
      //   '/api/kafka/getSource', {
      //     params: {
      //       page: this.pageOffset,
      //       size: this.pageLength
      //     }
      //   }
      // ]
      getSourceInfoByParams({})
      // this.$http.get(url)
        .then(response => {
          this.kafkaOptions = response.data
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    // 渲染表格数据
    listTable () {
      // let url = '/api/kafka/cluster/info'
      let data = new FormData()
      data.append('sourceId', this.topicForm.status || '')
      getClusterInfo(data)
      // this.$http.post(url, data)
        .then(response => {
          if (response.data.success) {
            this.tableData = response.data.data
            return
          }
          this.$Message.error('查询失败！请检查kafka环境')
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    // 新建
    create () {
      this.isNodeEdited = true
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

.modal{
  ::v-deep .ivu-form-item-content {
    display: flex;
  }
}

</style>
