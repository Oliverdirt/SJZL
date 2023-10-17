<template>
  <section>
    <Form :model="query" inline>
      <FormItem>
        <Input clearable v-model="query.name" placeholder="模型名称"></Input>
      </FormItem>
      <FormItem>
        <Input clearable v-model="query.key" placeholder="模型KEY"></Input>
      </FormItem>
      <FormItem>
        <Button icon="ios-search" shape="circle" @click="querySubmit()">查询</Button>
      </FormItem>
      <FormItem>
        <Button type="primary" icon="md-add" shape="circle" @click="newFlow()">新增</Button>
      </FormItem>
    </Form>
    <Table border :columns="columns" :data="tableData"></Table>
    <div class="page-div">
      <Page :total="dataCount" :page-size="pageSize" @on-change="handlePageChange" show-total/>
    </div>
  </section>

</template>

<script>
import Util from '@/libs/util.js'

export default {
  name: 'List',
  data () {
    return {
      query: {
        name: '',
        key: ''
      },
      dataCount: 0,
      pageSize: 6,
      page: 0,
      columns: [
        {
          type: 'index',
          width: 60,
          align: 'center'
        },
        {
          title: '模型名称',
          key: 'name'
        },
        {
          title: '模型KEY',
          key: 'key'
        },
        {
          title: '版本号',
          key: 'version'
        },
        {
          title: '是否部署',
          key: 'deploy'
        },
        {
          title: '创建时间',
          key: 'createTime'
        },
        {
          title: '操作',
          key: 'id',
          render: (h, params) => {
            return h('div', [
              h('Button', {
                props: {
                  type: 'primary',
                  size: 'small'
                },
                style: {
                  marginRight: '5px'
                },
                on: {
                  click: () => {
                    this.edit(params.row.id)
                  }
                }
              }, '编辑'),
              h('Button', {
                props: {
                  type: 'error',
                  size: 'small'
                },
                on: {
                  click: () => {
                    this.delete(params.row)
                  }
                }
              }, '删除')
            ])
          }
        }
      ],
      tableData: []
    }
  },
  mounted () {
    this.listTable()
  },
  methods: {
    listTable () {
      let This = this
      Util.http
        .get('/api/activiti/bpmn/getModelList', {
          params: {
            pageNumber: This.page,
            pageSize: This.pageSize,
            name: This.query.name,
            processDefinitionKey: This.query.key
          }
        })
        .then(function (response) {
          This.tableData = []
          let data = response.data
          This.dataCount = data.recordsTotal
          let list = data.data
          for (let i = 0; i < list.length; i++) {
            let process = list[i]
            let map = {
              id: process.id,
              name: process.name,
              key: process.processDefinitionKey,
              version: process.maxVersion === 1 ? process.processDefinitionId : '', // 未部署的不显示版本号
              deploy: process.maxVersion === 1 ? '是' : '否',
              createTime: process.createTime
            }
            This.tableData.push(map)
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    querySubmit () {
      this.page = 0
      this.listTable()
    },
    newFlow () {
      this.$router.push({ name: 'model-edit' })
    },
    handlePageChange (current) {
      this.page = current - 1
      this.listTable()
    },
    edit (id) {
      this.$router.push({
        name: 'model-edit',
        query: {
          id: id
        }
      })
    },
    delete (row) {
      let This = this
      if (row.deploy === '是') {
        this.$Notice.error({
          title: '已部署的模型无法删除'
        })
      } else {
        this.$Modal.confirm({
          title: '确定删除？',
          onOk () {
            Util.http.post('/api/activiti/bpmn/del/' + row.id).then(response => {
              if (response.data) {
                This.$Notice.success({
                  title: '删除成功'
                })
                This.listTable()
              } else {
                This.$Notice.error({
                  title: '删除失败'
                })
              }
            })
          }
        })
      }
    }
  }
}
</script>

<style scoped>
  .page-div {
    display: flex;
    justify-content: flex-end;
    padding-top: 35px;
    padding-right: 50px;
  }
</style>
