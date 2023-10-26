<template>
  <Modal
    :title="title"
    v-model="showModal"
    :closable="false"
    :loading="loading"
    :width="900">
    <Form ref="formEquipment" :model="form" :label-width="120" :rules="ruleValidate">
      <FormItem :label="'上层菜单：'" v-if="moduleflag">
        <Treeselect v-model="menuForm.parentId" :options="treeArr" style="width:100%" placeholder="请输入菜单">
        </TreeSelect>
      </FormItem>
      <FormItem :label="'上层菜单：'" v-if="parentRow.title&&keyFlag">
        <Input v-model="parentRow.title" readonly placeholder="请输入标题"/>
      </FormItem>
      <FormItem :label="'类型：'" prop="type">
        <Select @on-change="typeChange" v-model="form.type">
          <Option value="menu1" label="一级菜单"></Option>
          <Option value="menu2" label="二级菜单（不含三级菜单），三级菜单"></Option>
          <Option value="menu2-sp" label="二级菜单（含三级菜单）"></Option>
          <Option value="non-menu" label="非菜单页面"></Option>
          <Option value="button" label="数据/按钮"></Option>
        </Select>
      </FormItem>
      <FormItem :label="'标题：'" prop="title">
        <Input v-model="form.title" placeholder="请输入标题"/>
      </FormItem>
      <FormItem :label="'图标：'" v-if="form.type!=='button'">
        <icon-change :icon.sync="form.icon"></icon-change>
      </FormItem>
      <FormItem prop="roleId" v-show="!$util.sassModel" :label="'角色：'">
        <Select ref="roleSelect" v-model="form.roleId" multiple :disabled="form.flag === 0 ? true : false">
          <Option v-for="option in roleOptions" :value="option.value" :key="option.value">{{ option.label }}
          </Option>
        </Select>
      </FormItem>
      <FormItem :label="'路由名称：'" prop="name" v-if="form.type!=='button'">
        <Input v-model="form.name" placeholder="请输入路由名称"/>
      </FormItem>
      <FormItem :label="'路由路径：'" prop="url" v-if="form.type!=='button'">
        <Input v-model="form.url" placeholder="一级菜单路径必须以 ' / '开头，二级及其他路径可不以 ' / '开头 "/>
      </FormItem>
      <FormItem :label="'vue组件位置：'" prop="component" v-if="form.type!=='button' && form.type!=='menu2-sp'">
        <Input :disabled="isReadonly" v-model="form.component" placeholder="组件位于src下路径，例如：views/main/main.vue"/>
      </FormItem>
      <FormItem :label="'权限码：'" prop="permissionCode">
        <Input v-model="form.permissionCode" placeholder="请输入权限码"/>
      </FormItem>
      <FormItem :label="'排序：'" prop="orderby">
        <InputNumber v-model="form.orderby" :min="0" :max="1000000" :precision="0" placeholder=""
                     style="width: 100%"></InputNumber>
      </FormItem>
    </Form>
    <div slot="footer">
      <Button type="default" @click="cancel">取消</Button>
      <Button type="primary" @click="handleSubmit('formEquipment')">确定</Button>
    </div>
  </Modal>
</template>

<script>
import iconChange from '@/views/admin/components/iconChange'
import {
  getRoleListByParams
} from '@/api/admin/role'
import {
  addOrUpdateMenuByData
} from '@/api/admin/menu'

export default {
  name: 'menu-info',
  data () {
    const validateOrder = (rule, value, callback) => {
      setTimeout(() => {
        if (!this.form.orderby) {
          callback(new Error('请输入显示排序'))
        } else {
          callback()
        }
      }, 0)
    }
    return {
      moduleId: '',
      moduleflag: false,
      disabled: false,
      roleOptions: [],
      roleId: [],
      showModal: false,
      title: '菜单',
      loading: false,
      scollshow: false,
      type: 'add',
      ruleValidate: {
        icon: [
          { required: true, message: '图标不能为空!', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '类型不能为空!', trigger: 'blur' }
        ],
        title: [
          { required: true, message: '标题不能为空!', trigger: 'blur' },
          { max: 30, message: '标题不可以超过30位', trigger: 'blur' }
        ],
        orderby: [
          { required: true, validator: validateOrder, trigger: 'blur' }
        ],
        name: [
          { required: true, message: '路由名称不能为空!', trigger: 'blur' },
          { max: 80, message: '路由名称不可以超过80位', trigger: 'blur' }
        ],
        url: [
          { required: true, message: '路由访问路径不能为空!', trigger: 'blur' },
          { max: 80, message: '路由访问路径不可以超过80位', trigger: 'blur' }
        ],
        component: [
          { required: true, message: 'vue组件存放路径不能为空!', trigger: 'blur' },
          { max: 80, message: '路由访问路径不可以超过80位', trigger: 'blur' }
        ]
      },
      isReadonly: false
    }
  },
  components: {
    iconChange
  },
  props: {
    menuForm: Object,
    treeArr: Array,
    form: {
      type: Object
    },
    keyFlag: Boolean,
    parentRow: Object,
    id: String
  },
  created () {
    this.initRoles()
  },
  methods: {
    typeChange (val) {
      if (val === 'menu1') {
        this.form.component = 'views/main/main.vue'
        this.isReadonly = true
      } else {
        this.form.component = ''
        this.isReadonly = false
      }
    },
    initRoles () {
      // const [url, httpConfig] = [
      //   '/api/system/cscpRolessByCriteria',
      //   {
      //     params: { size: 1000 }
      //   }
      // ]
      getRoleListByParams({ size: 1000 })
        // this.$http.get(url, httpConfig)
        .then(response => {
          this.roleOptions = []
          const selectArr = response.data.data.filter((item) => {
            return item.name != 'admin' || item.id != 1
          })
          for (const item of selectArr) {
            this.roleOptions.push({
              value: item.id.toString(),
              label: item.name,
              icon: item.icon
            })
          }
        }).catch()
    },
    cancel () {
      this.$emit('reloadRule')
      this.showModal = false
      this.$refs.formEquipment.resetFields()
    },
    handleSubmit (name) {
      if (this.menuForm) {
        if (this.menuForm.parentId) {
          this.type = 'addModuleMenu'
          this.form.parentId = this.menuForm.parentId
        } else {
          this.type = 'add'
        }
      }
      this.$refs[name].validate(valid => {
        if (valid) {
          const isAppendNode = this.form.id === null
          const msg = this.$Message.loading({
            content: isAppendNode ? '正在添加新节点' : '正在更新节点',
            duration: 0
          })
          // const [url] = [
          //   '/api/system/menu/addMenu'
          // ]
          let data = {
            'type': this.form.type,
            'name': this.form.name,
            'icon': this.form.icon,
            'title': this.form.title,
            'orderby': this.form.orderby,
            'url': this.form.url,
            'component': this.form.component,
            'permissionCode': this.form.permissionCode,
            'parentId': this.type === 'add' ? this.parentRow.id || 0 : this.form.parentId,
            'id': this.form.id,
            'roleId': this.form.roleId
          }

          addOrUpdateMenuByData(data)
            // this.$http.put(url, data)
            .then(response => {
              //console.log(response.data.id)
              msg()
              this.$Message.success(isAppendNode ? '添加菜单成功！' : '更新菜单成功！')
              this.showModal = false
              this.$emit('refreshList')
              let param = { menuId: response.data.id, moduleId: this.moduleId }
              this.$emit('updateModule', param)
            }).catch(() => {
            msg()
            this.$Message.error(isAppendNode ? '添加菜单失败！' : '更新节点失败！')
          })
        }
      })
    },
    scollListClick (item) {
      this.form.icon = item
      this.scollshow = false
    },
    tableShow () {
      this.scollshow = true
    },
    tableHide () {
      this.scollshow = false
    },
    clickFocus (event) {
      event || (event = window.event)
      event.stopPropagation ? event.stopPropagation() : (event.cancelBubble = true)
      this.scollshow ? this.tableHide() : this.tableShow()
      document.addEventListener('click', this.selectHideList, false)
    },
    clickblur () {
      this.scollshow = false
    }
  }
}
</script>

<style scoped lang="less">

</style>
