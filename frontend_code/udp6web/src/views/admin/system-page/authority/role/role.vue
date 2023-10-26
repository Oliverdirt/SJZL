<template>
  <div class="main" style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea">
          <Form  ref="formEquipment" :model="roleSearchForm" :label-width="80">
            <Row :gutter="48">
              <Col span="8">
                <FormItem label="角色名" prop="roleName">
                  <Input v-model="roleSearchForm.roleName" placeholder="请输入角色名称" style=""/>
                </FormItem>
              </Col>
              <Col span="24">
                <FormItem style="text-align: right">
                  <Button  type="primary" @click="handleSearch()">查询</Button>
                  <Button type="default" style="margin-left: 8px" @click="handleReset(roleSearchForm)">重置</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="tableBtn">
          <Button code="cscp.role.add" @click="handleCreateOrEditItem('add')" type="primary" icon="md-add">新增</Button>
        </div>
        <Table border row-key="id" :columns="columns" :data="roleList" @on-selection-change="tableSelectChange"  @on-row-dblclick="showDetail($event.id)">
          <template slot-scope="{ row }" slot="action">
            <Button v-if="row.name !== 'admin'" type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="handleCreateOrEditItem('edit', row)">编辑</Button>
            <Button v-if="row.name !== 'admin'" type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="handleCreateOrEditItem('delete', row)">删除</Button>
            <Button v-if="row.name !== 'admin'" type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="handleCreateOrEditItem('authority', row)">数据权限</Button>
            <Button v-if="row.name !== 'admin'" type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="handleCreateOrEditItem('copy',row)">复制</Button>
            <Button v-if="row.name !== 'admin'" type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="handleCreateOrEditItem('assign',row)">分配用户</Button>
          </template>
        </Table>
        <Page :total="total" show-elevator :current="currentPageIndex" show-total show-sizer @on-change="changePage" @on-page-size-change="changePageSize"/>
      </Card>
    </div>
    <role-info :roleInfo="roleInfo" @refreshList="handleSearch" ref="roleInfoRef" :showDetailFlag="showDetailFlag" :treeList="treeList"></role-info>
    <role-authority :roleInfo="roleInfo" @refreshList="handleSearch" ref="roleAuthorityRef" :showDetailFlag="showDetailFlag" :treeList="treeList"></role-authority>
    <role-assign ref="roleAssignRef" :roleInfo="roleInfo"></role-assign>
  </div>
</template>

<script>
import roleInfo from './components/role-info'
import roleAuthority from './components/role-authority'
import roleAssign from './components/role-assign'
import {
  validRoleUser,
  delRoleById,
  getRoleListByParams
} from '@/api/admin/role'
export default {
  name: 'role',
  data () {
    return {
      roleSearchForm: {},
      deptSearchForm: {},
      form: {},
      modal10: false,
      columns: [
        {
          title: '角色名',
          key: 'name'
        },
        {
          title: '权限范围',
          render: (h, params) => {
            return h('span', params.row.dataScope === '1' ? '全部数据权限' : params.row.dataScope === '2'
              ? '自定数据权限' : params.row.dataScope === '3' ? '本部门数据权限' : params.row.dataScope === '4'
                ? '本部门及以下数据权限' : params.row.dataScope === '5' ? '仅本人数据权限' : '暂无数据'
            )
          }
        },
        {
          title: '图标',
          width: 150,
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h('Icon', {
                props: {
                  type: params.row.icon,
                  size: '20'
                }
              })
            ])
          }
        },
        {
          title: '操作',
          slot: 'action',
          width: 300,
          align: 'center'
        }
      ],
      roleList: [],
      treeList: [],
      currentPageIndex: 1,
      checkedKeys: [],
      treeOption: [], // 新增编辑使用
      pageNum: 0,
      pageSize: 10,
      selection: [],
      roleInfo: {
        id: null,
        name: '',
        parentId: 0,
        roleExtent: '',
        icon: ''
      },
      total: 0,
      deptId: null,
      pwd: '',
      showDetailFlag: false,
      reset: {}
    }
  },
  components: {
    roleInfo,
    roleAuthority,
    roleAssign
  },
  methods: {
    init () {
      this.getroleList()
    },
    handleSearch () {
      this.currentPageIndex = 1
      this.getroleList()
    },
    handleReset () {
      // this.roleSearchForm = {}
      this.$refs['formEquipment'].resetFields()
      this.currentPageIndex = 1
      this.getroleList()
    },
    showDetail (id) {
      this.$refs.roleInfoRef.init(id)
      this.$refs.roleInfoRef.showModal = true
      this.showDetailFlag = true
    },
    async handleCreateOrEditItem (val, params) {
      this.showDetailFlag = false
      if (val === 'add') {
        this.$refs.roleInfoRef.init()
        this.$refs.roleInfoRef.keyFlag = true
        this.$refs.roleInfoRef.showModal = true
      } else if (val === 'edit') {
        this.$refs.roleInfoRef.init(params.id)
        this.$refs.roleInfoRef.showModal = true
      } else if (val === 'authority') {
        this.$refs.roleAuthorityRef.showModal = true
        this.$refs.roleAuthorityRef.queryTreeList(params)
      } else if (val === 'copy') {
        this.$refs.roleInfoRef.init()
        this.$refs.roleInfoRef.keyFlag = true
        this.$refs.roleInfoRef.showModal = true
        this.roleInfo.name = params.name
        this.roleInfo.icon = params.icon
      } else if (val === 'assign'){
        this.$refs.roleAssignRef.showModal = true
        this.$refs.roleAssignRef.initTreeData()
        this.$refs.roleAssignRef.getUserList()             
        this.$refs.roleAssignRef.initRoleUser(params.id)        
      } else {
        this.$Modal.confirm({
          title: '警告',
          content: `是否删除选择的数据？`,
          onOk: () => {
            this.deleteConfirm(params.id)
          },
          onCancel: () => {

          }
        })
      }
    },
    deleteConfirm (id) {
      validRoleUser(id)
      // this.$http.get(`/api/system/cscpRoleUsers/${id}`)
        .then(res => {
          if (res.data !== '0') {
            this.$Message.warning(`该角色已被 ${res.data} 个账号使用，禁止删除。`)
          } else {
            this.deleteRole(id)
          }
        }).catch(error => {
          if (error.response) {
            this.$Message.error(error.response.data.title ? error.response.data.title : '角色删除失败！')
          }
        })
    },
    deleteRole (id) {
      // const url = `/api/system/cscpRoless/${id}`
      delRoleById(id)
      // this.$http.delete(url)
        .then(() => {
          this.$Message.success('角色删除成功！')
          this.getroleList()
        }).catch(error => {
          if (error.response) {
            this.$Message.error(error.response.data.title ? error.response.data.title : '角色删除失败！')
          }
        })
    },
    treeMap (data) {
      const hasChild = data.children && data.children.length > 0
      // 可以根据实际开发需要返回自己想要的key值
      return {
        id: data.id,
        checked: this.checkedKeys.indexOf(data.id) !== -1,
        label: data.label,
        title: data.title,
        children: hasChild ? data.children.map(i => this.treeMap(i)) : []
      }
    },
    changePageSize (size) {
      this.pageSize = size
      this.getroleList()
    },
    tableSelectChange (selection) {
      this.selection = selection
    },
    changePage (page) {
      this.currentPageIndex = page
      this.getroleList()
    },
    getroleList () {
      // const [url, httpConfig] = [
      //   '/api/system/cscpRolessByCriteria',
      //   {
      //     params: {
      //       'name.contains': this.roleSearchForm.roleName,
      //       page: this.currentPageIndex,
      //       size: this.pageSize
      //     }
      //   }
      // ]
      const queryParams = {
        'name.contains': this.roleSearchForm.roleName,
        page: this.currentPageIndex,
        size: this.pageSize
      }
      getRoleListByParams(queryParams)
      // this.$http.get(url, httpConfig)
        .then(res => {
          console.log(res, 'res')
          this.roleList = res.data.data
          this.total = res.data.recordsTotal
        })
    },
    selectChange (node) {
      this.deptId = node[0] ? node[0].id : null
      this.getroleList()
    }
  },
  created () {
    this.init()
  }
}
</script>

<style scoped lang="less">
@import "../../../../admin/styles/formStyle.less";
.main {
  ::v-deep .vertical-center-modal{
    display: flex;
    align-items: center;
    justify-content: center;

    .ivu-modal{
      top: 0;
    }
  }
  &-top {
    display: flex;
    &-tree{
      max-height: 100%;
      overflow: auto;
    }
  }

  ::v-deep .ivu-form-item{
    margin-bottom: 0;
  }
}

</style>
