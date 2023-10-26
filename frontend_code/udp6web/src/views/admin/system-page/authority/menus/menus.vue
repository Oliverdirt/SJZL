<template>
  <div class="main" style="height: 100%">
    <div class="formCard-content">
      <Card
        :bordered="false"
        :dis-hover="true"
        :shadow="false"
        class="formCard"
      >
        <div class="searchArea">
          <Form ref="formEquipment" :model="menus" :label-width="100">
            <Row :gutter="48">
              <Col span="8">
                <FormItem :label="'菜单名称：'">
                  <Input
                    v-model="menus.menuName"
                    clearable
                    placeholder="请输入菜单名称"
                  />
                </FormItem>
              </Col>
              <Col span='24' style="text-align: right">
                <Button
                  type="primary"
                  @click="handleSearch()"
                  style="margin-right: 8px"
                  >查询</Button
                >
                <Button type="default" @click="handleReset()">重置</Button>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
      <Card
        :bordered="false"
        :dis-hover="true"
        :shadow="false"
        class="formCard"
      >
        <div class="tableBtn">
          <Button
            @click="handleCreateOrEditItem('add')"
            type="primary"
            code="cscp.menu.add"
            icon="md-add"
          >
            新增
          </Button>
        </div>
        <Table
          :indent-size="10"
          row-key="id"
          :columns="columns"
          :data="menuList"
        >
          <template slot-scope="{ row }" slot="action">
            <Button
              type="text"
              size="small"
              style="margin-right: 5px; color: #0056b5"
              @click="handleCreateOrEditItem('add', row)"
              >新增
            </Button>
            <Button
              type="text"
              size="small"
              style="margin-right: 5px; color: #0056b5"
              @click="handleCreateOrEditItem('edit', row)"
              >编辑
            </Button>
            <Button
              type="text"
              size="small"
              style="margin-right: 5px; color: #0056b5"
              @click="del(row.id)"
              >删除</Button
            >
          </template>
        </Table>
      </Card>
    </div>
    <menu-info
      ref="menuInfoRef"
      @refreshList="handleSearch"
      :form.sync="form"
      :keyFlag="keyFlag"
      :id="id"
      :parentRow="parentRow"
    ></menu-info>
    <Modal v-model="deleteModal" :width="360">
      <p slot="header" style="color: #f60; text-align: center">
        <Icon type="ios-information-circle"></Icon>
        <span>删除确认</span>
      </p>
      <div style="text-align: center">
        <p>是否确定删除当前菜单？</p>
      </div>
      <div slot="footer">
        <Button type="error" :loading="modal_loading" @click="handleDelete"
          >删除</Button
        >
      </div>
    </Modal>
  </div>
</template>

<script>
import menuInfo from './components/menu-info'
import {
  getMenuById,
  delMenuById,
  delMenussById,
  getMenuByParams
} from '@/api/admin/menu'
export default {
  name: 'menus',
  data () {
    return {
      menus: {
        menuName: ''
      },
      deleteModal: false,
      parentRow: {},
      form: {},
      keyFlag: false,
      modal_loading: false,
      columns: [
        {
          title: '菜单名称',
          key: 'title',
          // width: 300,
          tree: true
        },
        {
          title: '图标',
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
          title: '类型',
          // width: 300,
          align: 'center',
          render: (h, params) => {
            return h(
              'span',
              params.row.type === 'menu1'
                ? '一级菜单'
                : params.row.type === 'menu2'
                  ? '二级菜单（不含三级菜单），三级菜单'
                  : params.row.type === 'menu2-sp'
                    ? '二级菜单（含三级菜单）'
                    : params.row.type === 'non-menu'
                      ? '非菜单页面'
                      : params.row.type === 'non-button' ||
                  params.row.type === 'button'
                        ? '数据/按钮'
                        : '暂无数据'
            )
          }
        },
        {
          title: '权限码',
          key: 'perms'
        },
        {
          title: '路由名称',
          key: 'name'
        },
        {
          title: '访问路径',
          key: 'url'
        },
        {
          title: '操作',
          slot: 'action',
          width: 300,
          align: 'center'
        }
      ],
      treeList: [],
      id: '',
      showModal: false,
      menuList: [],
      deleteId: null,
      operateType: ''
    }
  },
  components: {
    menuInfo
  },
  methods: {
    handleCreateOrEditItem (type, row) {
      this.parentRow = row || {}
      this.operateType = type
      if (type === 'edit') {
        this.keyFlag = false
        getMenuById(row.id)
        // this.$http.get(`/api/system/cscpMenuss/${row.id}`)
          .then((res) => {
            this.form = res.data
            this.id = row.id
          })
      } else {
        this.keyFlag = true
        this.form = {
          orderby: 1
        }
      }
      this.$refs.menuInfoRef.showModal = true
      this.$refs.menuInfoRef.type = type
    },
    handleDelete () {
      this.modal_loading = true
      delMenuById(this.deleteId)
      // this.$http.delete(`/system/menu/${this.deleteId}`)
        .then((res) => {
          if (res.data.code === 200) {
            this.$Message.success('删除成功！')
            this.modal_loading = false
            this.deleteModal = false
            this.getMeunList()
          } else {
            this.$Message.error(res.data.msg)
            this.modal_loading = false
            this.deleteModal = false
          }
        })
    },
    del (id) {
      this.$Modal.confirm({
        title: '警告',
        content: '确定删除该节点（包括子节点）数据？',
        onOk: () => {
          // const url = `/api/system/menu/deleteMenu/${id}`
          delMenussById(id)
          // this.$http
          //   .delete(url)
            .then(() => {
              this.$Message.success('删除成功！')
              this.getMeunList()
            })
            .catch(() => {
              this.$Message.error('删除失败！')
            })
        }
      })
    },
    handleSearch () {
      this.getMeunList()
    },
    handleReset () {
      this.menus = {}
      this.getMeunList()
    },
    getMeunList () {
      // let url = '/api/system/cscpAllMenus'
      // let param = {
      //   params: {
      //     title: this.menus.menuName
      //   }
      // }
      getMenuByParams({
        title: this.menus.menuName
      })
      // this.$http.get(url, param)
        .then((res) => {
          this.menuList = this.handleTree(res.data, 'menuId')
        })
    },
    handleTree (data, id, parentId, children, rootId) {
      id = id || 'id'
      parentId = parentId || 'parentId'
      children = children || 'children'
      rootId =
        rootId ||
        Math.min.apply(
          Math,
          data.map((item) => {
            return item[parentId]
          })
        ) ||
        0
      let rootArr = []
      // 对源数据深度克隆
      const cloneData = JSON.parse(JSON.stringify(data))
      cloneData.forEach((item) => {
        let typeArr = cloneData.filter((i) => {
          return i.id === item.parentId
        })
        if (!typeArr.length && !rootArr.includes(item.parentId)) {
          rootArr.push(item.parentId)
        }
      })
      // 循环所有项
      // console.log(cloneData);
      // console.log(this.parentRow);
      let temItem = ''
      const treeData = cloneData.filter((father) => {
        // 默认展开
        father._showChildren = false
        if (
          this.parentRow.parentId === father.id ||
          this.parentRow.id === father.id
        ) {
          // 展开父节点和本身
          father._showChildren = true
          temItem = father
        }
        if (temItem.parentId === father.id) {
          father._showChildren = true
        }
        let branchArr = cloneData.filter((child) => {
          // 返回每一项的子级数组
          return father.id == child[parentId]
        })
        // eslint-disable-next-line no-unused-expressions
        branchArr.length > 0 ? (father.children = branchArr) : []
        // 返回第一层
        return rootArr.includes(father[parentId])
      })
      // eslint-disable-next-line eqeqeq
      return treeData != '' ? treeData : data
    }
  },
  created () {
    // this.getMenuTree()
    this.getMeunList()
  }
}
</script>

<style scoped lang="less">
@import "../../../styles/formStyle";

.main {
  ::v-deep .ivu-form-item {
    margin-bottom: 0;
  }

  &-top {
    display: flex;

    &-tree {
      max-height: 100%;
      overflow: auto;
    }
  }
}

// 滚动条样式
.menu-scrollbar::-webkit-scrollbar,
.common-scrollbar::-webkit-scrollbar {
  /*滚动条整体样式*/
  width: 11px;
  /*高宽分别对应横竖滚动条的尺寸*/
  height: 1px;
}

// 滚动条样式1
.menu-scrollbar::-webkit-scrollbar-thumb {
  /*滚动条里面小方块*/
  border-radius: 2px;
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  background: #808695;
}

// 滚动条样式2
.common-scrollbar::-webkit-scrollbar-thumb {
  /*滚动条里面小方块*/
  border-radius: 2px;
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  background: #dcdee2;
}

::v-deep .ivu-table-cell {
  .ivu-table-cell-tree {
    cursor: pointer;
  }
}
</style>
