<template>
    <Modal
            v-model="modalVisible"
            title="表单列表"
            :mask-closable="false"
            width="600"
            @on-ok="ok"
            @on-visible-change="change"
            @on-cancel="cancel">
        <div>
            <Form :model="query" inline>
                <FormItem>
                    <Input clearable v-model="query.name" placeholder="表单名称"  :maxlength="30"></Input>
                </FormItem>
                <FormItem>
                    <Input clearable v-model="query.class" placeholder="实体名称"  :maxlength="30"></Input>
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
  name: 'FormListWin',
  data () {
    return {
      modalVisible: false,
      query: {
        name: '',
        class: ''
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
          title: '表单名称',
          key: 'name'
        },
        {
          title: '实体名称',
          key: 'class'
        }
      ],
      tableData: []
    }
  },
  methods: {
    ok () {
      this.$emit('initForm', this.checked)
    },
    change (flag) {
      if (flag) {
        this.tableData = []
        // 设置勾选
        let This = this
        if (this.checked !== null) {
          this.tableData.forEach((items) => {
            console.log(This.checked.id)
            if (items.id === This.checked.id) {
              this.$set(items, 'checkBox', true)
            }
          })
        }

        this.listTable()
      } else {
        this.query = {
          name: '',
          class: ''
        }
        this.dataCount = 0
        this.pageSize = 6
        this.page = 0
        this.checked = null
        this.tableData = []
      }
    },
    listTable () {
      let This = this
      Util.http
        .get('/api/formTablesList', {
          params: {
            pageNumber: This.page,
            pageSize: This.pageSize,
            name: This.query.name,
            class: This.query.class
          }
        })
        .then(function (response) {
          This.tableData = []
          let data = response.data
          This.dataCount = 1
          let list = data.data
          for (let i = 0; i < list.length; i++) {
            let map = {

            }
            This.tableData.push(map)
          }
          // 设置勾选
          This.tableData.forEach((items) => {

          })
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
