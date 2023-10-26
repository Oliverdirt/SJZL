<template>
  <Modal
    :title="title"
    v-model="showModal"
    :closable="false"
    :loading="loading"
    :width="900">
    <Row :gutter="16" type="flex" justify="center" align="top">
      <Col span="24" style="border: #fff 1px solid; margin:5px;">
        <Form ref="roleForm" :model="role" :rules="ruleValidate" :disabled="showDetailFlag">
          <Row type="flex" justify="center" :gutter="16">
            <Col span="22">
              <FormItem prop="id" label="ID" style="display:none">
                <Input type="text" v-model="role.id" placeholder="主键" disabled>
                </Input>
              </FormItem>
            </Col>
            <Col span="22">
              <FormItem prop="name" label="名称" style="">
                <Input type="text" v-model.trim="role.name" placeholder="名称">
                </Input>
              </FormItem>
            </Col>
            <Col span="22">
              <FormItem prop="roleExtent" label="扩展属性" style="">
                <Input type="text" v-model.trim="role.roleExtent" placeholder="扩展属性">
                </Input>
              </FormItem>
            </Col>
            <Col span="22">
              <FormItem prop="parentId" label="父ID" style="display:none">
                <Input type="text" v-model="role.parent_id" placeholder="父ID" disabled>
                </Input>
              </FormItem>
            </Col>
            <Col span="22">
              <FormItem prop="icon" label="图标" style="">
                <icon-change :icon.sync="role.icon" @on-change="onChange"></icon-change>
              </FormItem>
            </Col>
            <Col span="22">
              <FormItem prop="menus" label="菜单" style="">
                <Tree ref="menuSelectTree" :data="menuData" show-checkbox multiple :expand-node="false"
                      style="border: 1px solid #e8eaec;line-height: 24px;padding:10px"></Tree>
              </FormItem>
            </Col>
          </Row>
        </Form>
      </Col>
    </Row>
    <div slot="footer">
      <Button type="primary" @click="cancel" v-if="showDetailFlag">确定</Button>
      <Button type="default" @click="cancel" v-if="!showDetailFlag">取消</Button>
      <Button type="primary" @click="handleSubmit('roleForm')" v-if="!showDetailFlag" :disabled="isSubmitDisabled">确定</Button>
    </div>
  </Modal>
</template>

<script>
import iconChange from '@/views/admin/components/iconChange'
import {
  validRoleByParams,
  getUsersByRoleId,
  getRoleInfoByRoleId,
  getMenuListByRoleId,
  getMenuListByRoles,
  updateOrAddRoles,
  updateUserRoles,
  saveRoleMeunsByData
} from '@/api/admin/role'
import {
  getUserDetailByParams
} from '@/api/admin/user'
export default {
  name: 'roleEdit',
  data () {
    const validateRoleName = (rule, value, callback) => {
      let pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>《》/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？ ]")
      if (pattern.test(value)) {
        callback(new Error('角色名称不允许存在特殊字符！'))
        return
      }
      if (value.length > 50) {
        callback(new Error('角色名称长度不允许超过50位！'))
        return
      }
      // 检查角色名是否存在
      // const [url, httpConfig] = [
      //   '/api/validation/role',
      //   {
      //     params: { name: this.role.name }
      //   }
      // ]
      if (value !== this.roleName) {
        validRoleByParams({ name: this.role.name })
        // this.$http.get(url, httpConfig)
          .then(response => {
            if (response.data.code === -1) {
              callback(new Error(response.data.msg))
            } else {
              callback()
            }
          }).catch(() => {
            callback(new Error('异步校验出错！'))
          })
      } else {
        callback()
      }
    }
    return {
      isSubmitDisabled: false,
      title: '角色',
      loading: false,
      showModal: false,
      roleName: null,
      disabled: false,
      role: { id: null, name: '', roleExtent: '', parentId: 0, icon: '' },
      ruleValidate: {
        name: [
          { required: true, message: '名称不能为空!', trigger: 'blur' },
          { validator: validateRoleName, trigger: 'blur' }
        ],
        icon: [
          { required: true, message: '图标不能为空!', trigger: 'blur' }
        ],
        roleExtent: [
          { type: 'string', max: 50, message: '扩展属性最大不可以超过50位！', trigger: 'blur' }
        ]
      },
      form: {
        tableId: 6
      },
      menuData: []
    }
  },
  // props: [ 'showDetailFlag','roleInfo' ],
  props: {
    showDetailFlag: {
      type: Boolean
    },
    roleInfo: {
      type: Object,
      required: true
    }
  },
  components: {
    iconChange
  },

  methods: {
    init (id) {
      this.id = id
      this.handleReset(id)
    },
    cancel () {
      this.showModal = false
      this.$refs['roleForm'].resetFields()
    },
    remoteUserMethod (query) {
      const queryParams = {
        page: 0,
        size: 1000,
        username: query,
        familyName: query,
        name: query,
        mobile: query,
        email: query,
        discTitle: query,
        discDetail: query
      }
      getUserDetailByParams(queryParams)
      // this.$http.get(url, httpConfig)
        .then(response => {
          const op = []
          for (let item of response.data.data) {
            op.push({
              value: item.userId,
              label: `${item.username} `
            })
          }
          this.userOptions = op
        }).catch()
    },
    handleReset (id) {
      if (id) {
        this.getData(id)
      } else {
        this.role = { id: null, name: '', roleExtent: '', parentId: 0, icon: '' }
        this.getMenusByRole(0)
        // this.getMenusByRole2(null)
      }
    },
 
    getData (roleId) {
      const msg = this.$Message.loading({
        content: '正在初始化角色数据',
        duration: 0
      })
      // const url = `/api/system/cscpRoless/${roleId}`
      getRoleInfoByRoleId(roleId)
      // this.$http.get(url)
        .then(response => {
          this.role = response.data
          this.roleName = this.role.name
          this.getMenusByRole(this.role.id, msg)
        // this.getMenusByRole2(this.role.id, msg)
        }).catch(error => {
          msg()
          if (error.response) {
            this.$Message.error('角色数据初始化失败！')
          }
        })
    },
    getMenusByRole2 (roleId, msg) {
      // const url = '/api/system/getMenuItemBean'
      // const data = this.$util.formUrlencoded({ roleId })
      let params = new URLSearchParams()
      params.append('roleId', roleId)
      getMenuListByRoleId(params)
      // this.$http.post(url, data)
        .then(response => {
          if (response && response.data) {
            const menuTree = this.initTreeData(response.data.items)
            this.menuData = menuTree.children
          }
          if (msg) {
            msg()
            this.$Message.success('角色数据初始化成功！')
          }
        }).catch(er => {
          if (msg) {
            msg()
            if (er.response) {
              this.$Message.error('角色数据初始化失败！')
            }
          }
        })
    },
    getMenusByRole (roles, msg) {
      getMenuListByRoles(roles)
      // this.$http.post(url, data)
        .then(response => {
          const menuTree = this.initTreeData(response.data.items)
          this.menuData = menuTree.children
          if (msg) {
            msg()
            this.$Message.success('角色数据初始化成功！')
          }
        }).catch(error => {
          if (msg) {
            msg()
            if (error.response) {
              this.$Message.error('角色数据初始化失败！')
            }
          }
        })
    },
    initTreeData (items) {
      const children = []
      let selected = false
      let checked = true
      for (let item of items) {
        const obj = {
          id: item.id,
          title: item.title,
          checked: item.checked
        }
        if (item.items) {
          obj.expand = false
          const tempRes = this.initTreeData(item.items)
          obj.selected = false
          obj.checked = tempRes.checked
          obj.children = tempRes.children
        }
        selected = (selected || obj.checked)
        checked = (checked && obj.checked)
        children.push(obj)
      }
      return {
        selected,
        checked,
        children
      }
    },
    onChange () {
      this.$refs['roleForm'].validate((valid) => {
        if (valid) {
        }
      })
    },
    handleSubmit (name) {
      // this.$refs.roleInfoRef.init()
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.isSubmitDisabled = true
          const msg = this.$Message.loading({
            content: this.role.id === null ? '正在创建新角色' : '正在更新角色数据',
            duration: 0
          })
          // const httpConfig = {
          //   method: this.role.id === null ? 'post' : 'put',
          //   url: '/api/system/cscpRoless',
          //   data: this.role
          // }
          const queryMerhod = this.role.id === null ? 'post' : 'put'
          updateOrAddRoles(queryMerhod, this.role)
          // this.$http(httpConfig)
            .then(response => {
              this.saveRoleMenus(response.data.id, msg)
            }).catch(error => {
              msg()
              this.isSubmitDisabled = false
              if (error.response) {
                this.$Message.error(this.role.id === null ? '创建新角色失败！' : '更新角色数据失败！')
              }
            })
        }
      })
    },
    
    saveRoleMenus (roleId, msg) {
      const checkedNodes = this.$refs.menuSelectTree.getCheckedAndIndeterminateNodes()
      let menus = []
      for (let item of checkedNodes) {
        menus.push(item.id)
      }
      menus = menus.join()
      // const url = '/api/system/menu/save'
      let params = new URLSearchParams()
      params.append('roles', roleId)
      params.append('menus', menus)
      params.append('permissions', '')

      // const data = this.$util.formUrlencoded({ roles: roleId, menus, permissions: '' })
      saveRoleMeunsByData(params)
      // this.$http.post(url, data)
        .then(response => {
          msg()
          this.$Message.success({
            content: this.role.id === null ? '创建新角色成功！' : '更新角色数据成功！',
            onClose: () => {
              this.closeMe()
            }
          })
        }).catch(error => {
          msg()
          this.isSubmitDisabled = false
          if (error.response) {
            this.$Message.error(this.role.id === null ? '创建新角色失败！' : '更新角色数据失败！')
          }
        })
    },
    getSelectItems (items, idArr) {
      for (let item of items) {
        if (item.children && item.children.length > 0) {
          this.getSelectItems(item.children, idArr)
        }
        if (item.selected) {
          idArr.push(item.id)
        } else if (item.checked) {
          idArr.push(item.id)
        }
      }
    },
    closeMe () {
      this.isSubmitDisabled = false
      this.showModal = false
      this.$refs.roleForm.resetFields()
      this.$emit('refreshList')
    }
  },
  created () {
    this.init()
  },
  watch: {
    showModal (val) {
      if (!val) {
        this.role = {}
        this.menuData = []
        this.userIds = []
      }
    },

    roleInfo: {

      immediate: true,
      handler (newData) {
        this.role = newData
        // console.log(newData,'sssssss')
      },
      deep: true
    }

  }
}
</script>
<style scoped lang="less">
::v-deep .ivu-form-item{
  //margin-bottom: 5px;
  display: flex;
  .ivu-form-item-content{
    width: 100%;
  }
}

::v-deep .ivu-form .ivu-form-item-label{
  width: 110px;
}
</style>
