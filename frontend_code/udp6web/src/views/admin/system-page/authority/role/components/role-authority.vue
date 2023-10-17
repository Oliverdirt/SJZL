<template>
  <Modal
    :title="title"
    v-model="showModal"
    :closable="false"
    :loading="loading"
    :width="700">
    <Form ref="formEquipment" class="content" :model="form" :label-width="100" :rules="ruleValidate">
      <FormItem :label="'角色名称：'" prop="roleName">
        <Input v-model="form.roleName" disabled readonly placeholder="请输入角色名称"/>
      </FormItem>
      <FormItem :label="'权限范围：'" prop="dataScope">
        <Select v-model="form.dataScope" style="width:200px" @on-change="onChange">
          <Option v-for="item in rangeList" :value="item.value" :key="item.value">{{ item.label }}</Option>
        </Select>
      </FormItem>
      <FormItem :label="'权限菜单：'" v-if="showTree">
        <Tree ref="tree" :data="treeList" show-checkbox></Tree>
      </FormItem>
    </Form>
    <div slot="footer">
      <Button type="default" @click="cancel">取消</Button>
      <Button type="primary" @click="handleSubmit">确定</Button>
    </div>
  </Modal>
</template>

<script>

import lodash from 'lodash'
import {
  getRoleDeptTreeById,
  updateUserDataScope
} from '@/api/admin/dept'
export default {
  name: 'menu-info',
  data () {
    const validatePhone = (rule, value, callback) => {
      if (!value) {
        callback()
      }
      if (!(/^[1][3,4,5,7,8,9][0-9]{9}$/.test(value))) {
        callback(new Error('请输入正确的手机号！'))
      } else {
        callback()
      }
    }
    return {
      showModal: false,
      title: '分配数据权限',
      loading: false,
      showTree: false,
      form: {},
      scollshow: false,
      chooseDept: {},
      chooseDeptName: '',
      dataCode: '1',
      role: {},
      department: {},
      visible: false,
      postIds: [],
      treeList: [],
      roleIds: [],
      rangeList: [
        {
          label: '全部数据权限',
          value: '1'
        },
        {
          label: '自定数据权限',
          value: '2'
        },
        {
          label: '本部门数据权限',
          value: '3'
        },
        {
          label: '本部门及以下数据权限',
          value: '4'
        },
        {
          label: '仅本人数据权限',
          value: '5'
        }
      ],
      roleList: [],
      postList: [],
      keyFlag: false,
      ruleValidate: {
        roleSort: [
          { required: true, message: '角色顺序不能为空!', trigger: 'blur' }
        ],
        roleName: [
          { required: true, message: '角色名不能为空!', trigger: 'blur' }
        ],
        phonenumber: [
          { validator: validatePhone, trigger: 'blur' }
        ],
        dataScope: [
          { required: true, message: '权限字符不能为空!', trigger: 'blur' }
        ]
      }
    }
  },
  props: {
    parentRow: Object,
    deptOptions: Array,
    roleInfo: Object,
    showDetailFlag: Boolean
  },
  methods: {
    cancel () {
      this.showModal = false
      this.$refs.formEquipment.resetFields()
    },
    onChange () {
      console.log('this.form.dataScope', this.form.dataScope)
      this.showTree = this.form.dataScope == '2'
      console.log('this.showTree', this.showTree)
    },
    getTree (tree, select) {
      console.log('select', select)
      let arr = []
      if (!!tree && tree.length !== 0) {
        tree.forEach(item => {
          console.log('item.id', item.id)
          let obj = {}
          obj.title = item.label
          obj.id = item.id // 其他你想要添加的属性
          obj.expand = false
          obj.selected = false
          obj.checked = select.includes(item.id)
          obj.children = this.getTree(item.children, select) // 递归调用
          arr.push(obj)
        })
      }
      return arr
    },
    queryTreeList (param) {
      this.role = lodash.cloneDeep(param)
      this.form = {
        'roleName': param.name,
        'dataScope': param.dataScope
      }
      this.showTree = this.form.dataScope == '2'
      getRoleDeptTreeById(param.id)
      // this.$http.get(`/api/roleDeptTreeselect/${param.id}`)
        .then(res => {
          this.treeList = this.getTree(res.data.depts, res.data.checkedKeys.split(','))
        })
    },
    handleSubmit () {
      this.$refs['formEquipment'].validate(valid => {
        if (valid) {
          this.loading = true
          let menuIds = []
          if (this.form.dataScope === '2') {
            this.$refs.tree.getCheckedNodes().forEach(item => {
              menuIds.push(item.id)
            })
          } else {
            menuIds = []
          }
          let params = lodash.cloneDeep(this.role)
          params.dataScope = this.form.dataScope
          params.deptIds = menuIds
          updateUserDataScope(params)
          // this.$http.put('/api/system/dataScope', params)
            .then(res => {
              if (res.data.code === 200) {
                this.$Message.success('编辑成功！')
                this.showModal = false
                this.$emit('refreshList')
              } else {
                this.$Message.error('编辑失败！' + res.data.msg)
                this.loading = false
              }
            })
        } else {
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
    },
    init () {
      this.keyFlag = false
      this.form = {}
      this.chooseDept = {}
      this.chooseDeptName = ''
      this.postIds = []
      this.roleIds = []
    }
  },
  watch: {
    roleInfo: {
      immediate: true,
      handler (val) {
        this.form = val
      }
    },
    treeList: {
      immediate: true,
      handler (val) {
        console.log('treeList', val)
      }
    }
  }
}
</script>

<style scoped lang="less">
::v-deep .ivu-poptip {
  width: 100%;

  .ivu-poptip-rel {
    width: 100%;
  }
  .ivu-poptip-body{
    width: 200px;
  }
}

/*修改滚动条样式*/
div::-webkit-scrollbar{
  width:10px;
  height:10px;
  /**/
}
div::-webkit-scrollbar-track{
  background: rgb(239, 239, 239);
  border-radius:2px;
}
div::-webkit-scrollbar-thumb{
  background: #bfbfbf;
  border-radius:10px;
}
div::-webkit-scrollbar-thumb:hover{
  background: #333;
}
div::-webkit-scrollbar-corner{
  background: #179a16;
}

</style>
