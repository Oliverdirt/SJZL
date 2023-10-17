<template>
    <Modal
            v-model="modalVisible"
            title="用户选择"
            :mask-closable="false"
            width="700"
            @on-ok="ok"
            @on-visible-change="change"
            @on-cancel="cancel">
        <Row>
            <Col span="8" style="height: 350px;overflow: auto">
<!--                <Tree :data="deptArr" @on-select-change="listUser"></Tree>-->
              <el-tree :data="deptArr"   ref="tree" :highlight-current="true" :expand-on-click-node="false"  :filter-node-method="filterNode" @node-click="chooseNode($event)" style="margin-top: 10px"></el-tree>
            </Col>
            <Col span="1">
                <Divider style="height: 350px;" type="vertical"/>
            </Col>
            <Col span="15">
                <div>已选择用户</div>
                <Tag v-for="(item, i) in userCheck" type="dot" color="primary"  closable @on-close="handleTagClose(item.id)" :key="i">{{item.name}}</Tag>
                <Divider/>
                <Table border ref="selection" :columns="columns" :data="dataTable"
                       @on-select="selectTable"
                       @on-select-cancel="cancelSelectTable"
                       @on-select-all = "selectAll"
                       @on-select-all-cancel = "cancelSelectAllUser"
                ></Table>
                <div class="page-div">
                    <Page :total="dataCount" :page-size="pageSize" @on-change="handlePageChange" show-total/>
                </div>
            </Col>
        </Row>
    </Modal>
</template>

<script>
import util from '@/libs/util.js'

export default {
  name: 'UserRuleSetWin',
  data () {
    return {
      isDisabled: false,
      deptId: '',
      modalVisible: false,
      tableId: null,
      dataCount: 0,
      pageSize: 3,
      page: 1,
      user: null,
      userCheck: [],
      deptArr: [],
      dataTable: [],
      columns: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '姓名',
          key: 'name',
          align: 'center'
        }
      ]
    }
  },
  watch: {
    user: function (value) {
      if (value === null) return
      if (typeof (value.value) === 'undefined' || value.value === null) return
      this.userCheck = value.value
    }
  },
  methods: {
    filterNode (value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    initTree () {
      let This = this
      util.http.get('/api/cscpDepts/treeselect').then(response => {
        This.deptArr = this.getTree(response.data)
        if (This.deptArr.length > 0) {
          This.deptArr[0].expand = true
        }
        this.getUserList()
      })
    },
    chooseNode (e) {
      this.deptId = e.id
      this.handleSearch()
    },
    handleSearch () {
      this.page = 1
      this.getUserList()
    },
    getUserList () {
      let msg = this.$Message.loading({
        content: '正在更新数据',
        duration: 0
      })
      this.isDisabled = true
      const [url, httpConfig] = [
        '/api/system/cscpUserDetailsOr',
        {
          params: {
            'deptIdEquals': this.deptId,
            sort: 'user_id',
            page: this.page,
            size: this.pageSize
          }
        }
      ]
      this.$http.get(url, httpConfig).then(response => {
        this.dataTable = []
        let data = response.data
        this.dataCount = response.data.recordsTotal
        let list = data.data
        for (let i = 0; i < list.length; i++) {
          let user = list[i]
          let map = {
            id: user.userId,
            _checked: false,
            name: user.username
          }
          for (let j = 0; j < this.userCheck.length; j++) {
            if (this.userCheck[j].id === user.userId) {
              map._checked = true
            }
          }
          this.dataTable.push(map)
        }

        msg()
        this.isDisabled = false
      }).catch(error => {
        msg()
        this.isDisabled = false
        if (error.response) {
          this.$Message.error('数据获取失败！')
        }
      })
    },
    getTree (tree) {
      let arr = []
      if (!!tree && tree.length !== 0) {
        tree.forEach(item => {
          let obj = {}
          obj.label = item.label
          obj.id = item.id // 其他你想要添加的属性
          obj.expand = true
          obj.selected = false
          obj.children = this.getTree(item.children) // 递归调用
          arr.push(obj)
        })
      }
      return arr
    },
    selectTable (selection, row) {
      this.userCheck.push(row)
    },
    cancelSelectTable (selection, row) {
      for (let i = 0; i < this.userCheck.length; i++) {
        if (this.userCheck[i].id === row.id) {
          this.userCheck.splice(i, 1)
        }
      }
    },
    selectAll (selection) {
      let This = this
      for (let i = 0; i < selection.length; i++) {
        for (let j = 0; j < This.userCheck.length; j++) {
          if (selection[i].id === This.userCheck[j].id) {
            selection.splice(i, 1)
          }
        }
      }

      this.userCheck = this.userCheck.concat(selection)
    },

    cancelSelectAllUser (selection) {
      let This = this
      for (let i = 0; i < This.dataTable.length; i++) {
        for (let j = 0; j < This.userCheck.length; j++) {
          if (This.dataTable[i].id === This.userCheck[j].id) {
            This.userCheck.splice(j, 1)
          }
        }
      }
    },
    handleTagClose (id) {
      let This = this
      for (let j = 0; j < This.userCheck.length; j++) {
        if (id === This.userCheck[j].id) {
          This.userCheck.splice(j, 1)
        }
      }
      this.getUserList()
    },
    ok () {
      console.log(this.userCheck)
      if (this.user !== null) {
        this.user.value = this.userCheck
        this.$emit('refreshAssigneeRule', this.user)
      }
    },
    change (flag) {
      if (!flag) {
        this.user = null
        this.userCheck = []
        this.dataTable = []
        this.tableId = null
        this.dataCount = 0
        this.pageSize = 3
        this.page = 1
        this.deptArr = []
      } else {
        this.initTree()
      }
    },

    handlePageChange (current) {
      this.page = current
      this.getUserList()
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
        padding-top: 35px;
        padding-right: 50px;
    }
</style>
