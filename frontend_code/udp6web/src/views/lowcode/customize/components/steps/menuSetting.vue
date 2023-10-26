<template>
  <div class="form-area" style="width: 99%;">
    <Form ref="menuFormRef" :model="menuForm" :label-width="120" :rules="ruleValidate"
          style="width: 40%;margin-left: 29%;margin-top: 2%">
      <FormItem :label="'上层菜单：'" prop="parentId">
        <Treeselect v-model="menuForm.parentId" :options="treeArr" style="width:100%" placeholder="请输入父级菜单">
        </TreeSelect>
      </FormItem>
      <FormItem :label="'类型：'" prop="type">
        <Select readonly v-model="menuForm.type">
          <Option v-show="!menuForm.parentId" value="menu1" label="一级菜单"></Option>
          <Option v-show="menuForm.parentId" value="menu2" label="二级菜单（不含三级菜单），三级菜单"></Option>
          <!--          <Option value="menu2-sp" label="二级菜单（含三级菜单）"></Option>-->
          <!--          <Option value="non-menu" label="非菜单页面"></Option>-->
        </Select>
      </FormItem>
      <FormItem :label="'标题：'" prop="title">
        <Input onkeydown="if(event.keyCode==32){return false;}" v-model="menuForm.title" placeholder="请输入标题"/>
      </FormItem>
      <FormItem :label="'图标：'" property="icon">
        <icon-change :icon.sync="menuForm.icon"></icon-change>
      </FormItem>
      <FormItem :label="'路由名称：'" prop="name" v-show="false">
        <Input v-model="menuForm.name" placeholder="请输入路由名称"/>
      </FormItem>
      <FormItem :label="'路由路径：'" prop="url" v-show="false">
        <Input v-model="menuForm.url" placeholder="请输入路由路径"/>
      </FormItem>
      <FormItem :label="'vue组件位置：'" prop="component" v-if="menuForm.type!=='menu2-sp'" v-show="false">
        <Input v-model="menuForm.component" placeholder="请输入vue组件"/>
      </FormItem>
      <FormItem :label="'权限码：'" prop="permissionCode" v-show="false">
        <Input v-model="menuForm.permissionCode" placeholder="请输入权限码"/>
      </FormItem>
      <FormItem :label="'排序：'" prop="orderby">
        <InputNumber v-model="menuForm.orderby" :min="0" :max="1000000" :precision="0" placeholder=""
                     style="width: 100%"></InputNumber>
      </FormItem>
    </Form>
  </div>
</template>

<script>
import iconChange from '@/views/admin/components/iconChange'

export default {
  name: 'menuSetting',
  components: {
    iconChange
  },
  props: {
    formDetail: Object
  },
  data () {
    const validateOrder = (rule, value, callback) => {
      setTimeout(() => {
        if (!this.menuForm.orderby) {
          callback(new Error('请输入显示排序'))
        } else {
          callback()
        }
      }, 0)
    }

    return {
      menuForm: {},
      treeArr: [],
      ruleValidate: {
        type: [
          { required: true, message: '类型不能为空!', trigger: 'blur' }
        ],
        title: [
          { required: true, message: '标题不能为空!', trigger: 'blur' },
          { max: 30, message: '标题不可以超过30位', trigger: 'blur' }
        ],
        orderBy: [
          { required: true, validator: validateOrder, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    queryTreeList () {
      return new Promise((resolve, reject) => {
        this.$http.get('/api/system/menu/treeselect').then(res => {
          this.treeArr = res.data
          resolve(res)
        }).catch((err) => {
          reject(err)
          this.$message.error('菜单树查询失败')
        })
      })
    },
    getMenuInfo () {
      this.menuForm.url = '/customize/' + this.formDetail.formId
      this.menuForm.name = this.formDetail.formTable + '_' + this.formDetail.formId
      this.menuForm.component = 'views/lowcode/template/LowcodeTemplate.vue'
      const [url, httpConfig] = [
        '/api/system/menu/getMenuByUrl',
        {
          params: {
            url: this.menuForm.url
          }
        }
      ]
      this.$http.get(url, httpConfig).then(res => {
        if (res.data.data) {
          this.menuForm = res.data.data
        }
      }).catch(() => {
        this.$Message.error('获取菜单失败')
      })
    },
    init () {
      // 构建菜单树
      Promise.all([this.queryTreeList()]).then(res => {
        this.getMenuInfo()
      })
    },
    handleSubmit (buttonArr,pageId,pageType) {
      if (this.menuForm.genMenuFlag =='2' && this.menuForm.id !== undefined) {
        // 删除菜单
        const url = `/api/system/menu/deleteMenu/${this.menuForm.id}`
        var flag = 0;
        this.$http
            .delete(url)
            .then(() => {
              this.$Message.success('删除成功！')
              flag = 0;
            })
            .catch(() => {
              this.$Message.error('删除失败！')
              flag = 1;
            })
            return flag;
      }else if (this.menuForm.genMenuFlag =='2') {
        var flag = 0;
        return flag;
      }else{
        const isAppendNode = this.menuForm.id === null
        const [url] = [
          '/api/lowcode/customize/addMenuAndSetDataPermssion'
        ]
        let data = {
          'type': this.menuForm.type,
          'name': this.menuForm.name,
          'icon': this.menuForm.icon,
          'title': this.menuForm.title,
          'orderby': this.menuForm.orderby,
          'url': this.menuForm.url,
          'component': this.menuForm.component,
          'permissionCode': this.menuForm.permissionCode,
          'parentId': this.menuForm.parentId ? this.menuForm.parentId : 0,
          'id': this.menuForm.id
        }
        var flag = 0;
        if(data.id === data.parentId){
            this.$Message.error('更新菜单失败,父级菜单不能选择本身')
            flag =  1;
        }else{
          const msg = this.$Message.loading({
            content: isAppendNode ? '正在添加新节点' : '正在更新节点',
            duration: 0
          })
          //如果是列表 且有按钮 设置按钮菜单
          if(pageType && pageType == 1 && buttonArr && buttonArr.length > 0){
            var childMenu = [];
            buttonArr.map(item=>{
              var order = item.options.label == '新增'  ? 1 :
                          item.options.label == '删除'  ? 2 :
                          item.options.label == '修改'  ? 3 :
                          item.options.label == '查询'  ? 4 :
                          item.options.label == '导入'  ? 5 :
                          item.options.label == '导出'  ? 6 : 7;
              var url =   item.options.label == '新增'  ? '/api/lowcode/customize/page/template/add/'+pageId :
                          item.options.label == '删除'  ? '/api/lowcode/customize/page/template/delByPks/'+pageId :
                          item.options.label == '修改'  ? '/api/lowcode/customize/page/template/update/'+pageId :
                          item.options.label == '查询'  ? '/api/lowcode/customize/page/template/query/'+pageId :
                          item.options.label == '导入'  ? '/api/lowcode/customize/page/template/vpage/import/'+pageId :
                          item.options.label == '导出'  ? '/api/lowcode/customize/page/template/cscpCustomizeVpages/exportExcel'+pageId : null;   
              var httpMethod =  item.options.label == '新增'  ? 'POST' :
                                item.options.label == '删除'  ? 'DELETE':
                                item.options.label == '修改'  ? 'UPDATE' :
                                item.options.label == '查询'  ? 'GET' :
                                item.options.label == '导入'  ? 'POST':
                                item.options.label == '导出'  ? 'POST': null;          
              let childMenuDto = {
                'type': 'button',
                'title': item.options.label,
                'orderby': order,
                'url': url,
                'permissionCode': pageId+item.id,
                'httpMethod' : httpMethod
              }
              childMenu.push(childMenuDto);
            })
          }
          data.childMenusList = childMenu;
          this.$http.put(url, data).then(response => {
            msg()
            this.$Message.success(isAppendNode ? '添加菜单成功！' : '更新菜单成功！')
          }).catch(() => {
          msg()
          this.$Message.error(isAppendNode ? '添加菜单失败！' : '更新节点失败！')
        })
      }
      return flag;
      }
    }
  },
  created () {
    this.init()
  },
  watch: {
    'menuForm.parentId': {
      handler (newVal, oldVal) {
        newVal ? this.menuForm.type = 'menu2' : this.menuForm.type = 'menu1'
      }
    }
  }
}
</script>

<style scoped>

</style>
