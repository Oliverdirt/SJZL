<template>
    <Modal
            v-model="modalVisible"
            title="流程列表"
            :mask-closable="false"
            width="600"
            @on-ok="ok"
            @on-visible-change="change"
            @on-cancel="cancel">
        <div>
            <Form :model="query" inline>
                <FormItem>
                    <Input clearable v-model="query.name" placeholder="流程名称"></Input>
                </FormItem>
                <FormItem>
                    <Input clearable v-model="query.key" placeholder="模型KEY"></Input>
                </FormItem>
                <FormItem>
                    <Button icon="ios-search" shape="circle" @click="querySubmit">查询</Button>
                </FormItem>
            </Form>
            <Table border ref="selection" :columns="columns" :data="tableData"></Table>
            <div class="page-div">
                <Page :total="dataCount" :page-size="pageSize" @on-change="handlePageChange" show-total/>
            </div>
        </div>
    </Modal>
</template>

<script>
import Util from '@/libs/util.js'
export default {
  name: 'ProcessListWin',
  data () {
    return {
      modalVisible: false,
      query: {
        name: '',
        key: ''
      },
      dataCount: 0,
      pageSize: 6,
      page: 0,
      checked: null,
      columns: [
        {
          title: '#',
          key: 'id',
          width: 60,
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h('Checkbox', {
                props: {
                  value: params.row.checkBox
                },
                on: {
                  'on-change': (e) => {
                    if (e) {
                      this.checked = params.row
                    } else {
                      this.checked = null
                    }
                    this.tableData.forEach((items) => { // 先取消所有对象的勾选，checkBox设置为false
                      this.$set(items, 'checkBox', false)
                    })
                    this.tableData[params.index].checkBox = e // 再将勾选的对象的checkBox设置为true
                  }
                }
              })
            ])
          }
        },
        {
          title: '流程名称',
          key: 'name'
        },
        {
          title: '模型KEY',
          key: 'key'
        }
      ],
      tableData: []
    }
  },
  methods: {
    ok () {
      this.$emit('initProcess', this.checked)
    },
    change (flag) {
      if (flag) {
        this.listTable()
      } else {
        this.query = {
          name: '',
          key: ''
        }
        this.dataCount = 0
        this.pageSize = 4
        this.page = 0
        this.checked = null
        this.tableData = []
      }
    },
    listTable () {
      let This = this
      Util.http
        .get('/api/activiti/bpmn/getProcessList', {
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
              processDefinitionId: process.processDefinitionId
            }
            This.tableData.push(map)
          }
          // 设置选中
          if (This.checked !== null) {
            This.tableData.forEach((items) => {
              if (items.processDefinitionId === This.checked.processDefinitionId) {
                This.$set(items, 'checkBox', true)
              }
            })
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    handlePageChange (current) {
      this.page = current - 1
      this.listTable()
    },
    querySubmit () {
      this.listTable()
    },
    cancel () {
    }
  }

}
</script>

<style scoped>
    .page-div {
        display: flex;
        justify-content: flex-end;
        padding-top: 25px;
        padding-right: 30px;
    }
</style>
