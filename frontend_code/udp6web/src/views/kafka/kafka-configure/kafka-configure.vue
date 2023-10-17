<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea" >
          <!--顶部查询条件-->
          <Form :model="cscpPost" >
            <Row :gutter="48">
              <Col span="24" style="text-align: right;">
                <FormItem>
                  <Button type="primary" icon="md-add" shape="circle" @click="create">添加集群</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <Table  :columns="columns" :data="tableData" border>
          <template slot-scope="{ row }" slot="action">
            <Button type="text" size="small" style="margin-right: 5px;color: #2b85e4" @click="deleteRow(row.id)">删除</Button>
          </template>
        </Table>
      </Card>
    </div>
    <Modal class="modal" title="添加kafka地址"
           v-model="isNodeEdited"
           :styles="{top: '30%'}"
           @on-ok="handleSubmit">
      <Form ref="asyncRoute" :model="formItem" :rules="rules" :label-width="100">
        <FormItem prop="name" label="名称：">
          <Input type="text" v-model="formItem.name" placeholder="名称">
          </Input>
        </FormItem>
        <FormItem prop="broker" label="地址：">
          <Input type="text" v-model="formItem.broker" placeholder="地址">
          </Input>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="default" @click="cancel">取消</Button>
        <Button type="primary" @click="handleSubmit">确定</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import {
  delSourceById,
  addSource,
  getSourceInfoByParams
} from '@/api/kafka/kafka'
export default {
  name: 'kafka-configure',
  data () {
    const validateName = (rule, value, callback) => {
      if (value) {
        if (/[\u4E00-\u9FA5]/g.test(value)) {
          callback(new Error('不能输入汉字!'))
        }
        if (!value.replace(/\s*/g, '').length) {
          callback(new Error('不允许输入空值!'))
        }
        if (value.length > 30) {
          callback(new Error('输入长度不能大于30位！'))
        }
      }
      callback()
    }
    return {
      // 分页参数
      pageLength: 6, // 每页条数
      pageTotal: 0, // 总数
      pageOffset: 0, // 页码
      formItem: {},
      // 查询参数
      cscpPost: {
        postCode: null,
        postName: null,
        status: null
      },
      isNodeEdited: false,
      rules: {
        name: [
          { required: true, message: '名称不能为空！', trugger: 'blur' },
          { validator: validateName, trigger: 'blur' }
        ],
        broker: [
          { required: true, message: '地址不能为空！', trugger: 'blur' },
          { validator: validateName, trigger: 'blur' }
        ]
      },
      columns: [
        {
          key: 'name',
          title: '集群名称'
        },
        {
          key: 'broker',
          title: '地址'
        },
        {
          title: '操作',
          slot: 'action',
          width: 300,
          align: 'center'
        }
      ],
      tableData: []
    }
  },
  mounted () {
    this.listTable()
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
    deleteRow (id) {
      let _this = this
      this.$Modal.confirm({
        title: '确定删除？',
        onOk () {
          delSourceById(id)
          // this.$http.post('/api/kafka/deleteSource/' + id)
            .then(res => {
              this.$Message.success('删除成功！')
              _this.handleSearch()
            })
        }
      })
    },
    handleSubmit () {
      this.$refs['asyncRoute'].validate((valid) => {
        if (valid) {
          let data = new FormData()
          data.append('name', this.formItem.name)
          data.append('broker', this.formItem.broker)
          addSource(data)
          // this.$http.post('/api/kafka/add', data)
            .then(res => {
              this.formItem = {}
              this.isNodeEdited = false
              console.log(res)
              if (res.data == 'success') {
                this.$Message.success('新增集群成功！')
              } else {
                this.$Message.success(res.data)
              }
              this.$refs['asyncRoute'].resetFields()
              this.handleSearch()
            })
        } else {
          // this.topicForm = true
        }
      })
    },
    cancel () {
      this.isNodeEdited = false
      this.$refs['asyncRoute'].resetFields()
    },
    // 渲染表格数据
    listTable () {
      // const [url, param] = [
      //   '/api/kafka/getSource', {
      //     params: {
      //       page: this.pageOffset,
      //       size: this.pageLength
      //     }
      //   }
      // ]
      getSourceInfoByParams()
      // this.$http.get(url)
        .then(response => {
          this.tableData = response.data
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    // 新建
    create () {
      this.isNodeEdited = true
    },
    // 查询
    handleSearch () {
      this.pageOffset = 0
      this.listTable()
    },
    handlePageChange (current) {
      this.pageOffset = current - 1
      this.listTable()
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
  ::v-deep .ivu-form-item-content{
    display: flex;
  }
}

</style>
