<template>
  <div id="authority" style="height: 100%">
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
              <Col flex="auto" style="text-align: right">
                <Button
                  type="primary"
                  @click="queryList"
                  style="margin-right: 8px"
                >查询
                </Button>
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
        <Table :columns="columns" :data="data" border>
          <template slot-scope="{ row, index }" slot="action">
            <Button type="text" size="small" style="margin-right: 5px; color: #0056b5" @click="share(row, index)">
              分享
            </Button>
          </template>
        </Table>
      </Card>
    </div>
    <Modal title="菜单分享" v-model="shareModal" :width="450">
      <div style="padding: 25px 0">
        <label style="margin-right: 15px" for="roleselect">选择用户：</label>
        <Select multiple clearable id="roleselect" v-model="role" style="width:250px">
          <Option v-for="item in roleList" :value="item.userId" :key="item.userId">{{ item.username }}</Option>
        </Select>
      </div>
      <div slot="footer">
        <Button @click="confirmeShare" type="primary">确 定</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import { getUserDetailByParams } from '@/api/admin/dept'

export default {
  name: 'Authority',
  data () {
    return {
      role: '',
      menus: {
        menuName: ''
      },
      columns: [
        {
          title: '菜单名称',
          key: 'title',
          width: 200,
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
          width: 300,
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
          width: 100,
          align: 'center'
        }
      ],
      pageData: {
        total: 0,
        size: 10,
        page: 1
      },
      shareModal: false,
      data: [
        {
          title: '测试菜单',
          name: '测试数据'
        }
      ],
      roleList: [],
      currentRowId: ''
    }
  },
  mounted () {
    this.queryList(false) // 查询 初始化
    this.handleSearch()
  },
  methods: {
    // 获取用户列表
    handleSearch () {
      getUserDetailByParams({
        sort: 'user_id',
        page: 1,
        size: 10000 // 不分页
      }).then(res => {
        this.roleList = res.data.data
      }).catch(err => {
        throw new Error(err)
      })
    },
    // 分享
    share (row) {
      this.role = []
      this.shareModal = true
      this.currentRowId = row.id // 当前操作的行数据id
      //初始化this.role
      this.$http.get("/api/lowcode/customize/getCscpUserIdsByMenuIds/"+row.id).then(response => {
          console.log(response);
          this.role = response.data
        }).catch(error => {
          if (error.response) {
            this.$Message.error('初始化用户失败！')
          }
        })
    },
    confirmeShare () {
      this.$http.post('/api/addDataPermission', {
        permissionUser: this.role.join(','),
        permissionScopeIds: this.currentRowId
      }).then(res => {
        console.log(res)
        this.shareModal = false
        this.$Message.success('分享成功！')
      }).catch(err => {
        throw new Error(err)
      })
    },
    // 查询
    queryList (flag) {
      if (flag) {
        this.pageData.page = 1
      }
      let obj = {}
      var title = this.menus.menuName == undefined ? null : this.menus.menuName
      let params = Object.assign(obj, {
        page: this.pageData.page - 1,
        size: this.pageData.size
      })
      this.$http
        .get('/api/lowcode/customize/getCscpMenussByPermission?title.contians=' + title, {
          params: params
        })
        .then((res) => {
          this.data = res.data.data
          this.pageData.total = res.data.recordsTotal
        })
        .catch(() => {
          this.$Message.error('列表查询失败')
        })
    }

  }
}
</script>

<style scoped lang="less">
#authority {
  .formCard-content {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;

    .formCard:nth-child(2) {
      margin-top: 16px;
      flex: 1 auto;
    }

    .formCard {
      width: 100%;
      opacity: 1;
      padding: 0 16px;
      position: relative;

      .searchArea {
        .ivu-form-item {
          width: 100%;
        }
      }

      .tableBtn {
        width: 100%;
        text-align: right;
        padding: 0 0 16px;
      }

      .ivu-page {
        flex: 1 auto;
        align-self: flex-end;
        display: flex;
        align-items: flex-end;
        bottom: 16px;
        right: 32px;
      }

      ::v-deep .ivu-card-body {
        display: flex;
        flex-direction: column;
        height: 100%;
      }

      ::v-deep .ivu-form-item {
        margin-bottom: 0;
      }
    }
  }

  .ivu-btn {
    border-radius: 3px;

  }

  .ivu-btn-text:focus {
    box-shadow: 0 0 !important
  }
}
</style>
