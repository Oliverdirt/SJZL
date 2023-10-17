<template>
  <div class="form-area pageLastStep" style="width: 99%;">
    <img src="../../../../../assets/lowcode/pageLastStep.png" alt="">
    <Form ref="menuForm" :model="menuForm" :label-width="120" :rules="ruleValidate"
          style="width: 40%;margin-left: 29%;margin-top: 2%">
      <FormItem :label="'是否生成菜单：'" prop="genMenuFlag">
        <el-radio-group size="mini" @input="qryMenuByModuleId" v-model="menuForm.genMenuFlag">
          <el-radio-button label="1">是</el-radio-button>
          <el-radio-button label="2">否</el-radio-button>
        </el-radio-group>
        <!--                <Select readonly v-model="menuForm.genMenuFlag" @on-change="qryMenuByModuleId()">-->
        <!--                  <Option value="1" label="是"></Option>-->
        <!--                  <Option value="2" label="否"></Option>-->
        <!--                </Select>-->
      </FormItem>
      <FormItem v-show="this.menuForm.genMenuFlag==='1'" :label="'上层菜单：'" prop="parentId">
        <Treeselect v-model="menuForm.parentId" :options="treeArr" style="width:100%" placeholder="请输入菜单">
        </TreeSelect>
      </FormItem>
      <FormItem v-show="this.menuForm.genMenuFlag==='1'" :label="'类型：'" prop="type">
        <Select readonly v-model="menuForm.type">
          <!--          <Option v-show="!menuForm.parentId" value="menu1" label="一级菜单"></Option>-->
          <Option value="menu2" label="二级菜单（不含三级菜单），三级菜单"></Option>
        </Select>
      </FormItem>
      <FormItem v-show="this.menuForm.genMenuFlag==='1'" :label="'标题：'" prop="title">
        <Input onkeydown="if(event.keyCode==32){return false;}" v-model="menuForm.title" placeholder="请输入标题"/>
      </FormItem>
      <FormItem v-show="this.menuForm.genMenuFlag==='1'" :label="'图标：'" prop="icon">
        <icon-change :icon.sync="menuForm.icon"></icon-change>
      </FormItem>
      <FormItem v-show="this.menuForm.genMenuFlag==='1'" :label="'排序：'" prop="orderby">
        <InputNumber v-model="menuForm.orderby" :min="0" :max="1000000" :precision="0" placeholder=""
                     style="width: 100%"></InputNumber>
      </FormItem>
    </Form>
    <menu-info
        ref="menuInfoRef"
        @updateModule="updateModule"
        :form.sync="form"
        :keyFlag="keyFlag"
        :id="id"
        :parentRow="parentRow"
        :menuForm="moduleMenuForm"
        :treeArr="treeArr"
    ></menu-info>
  </div>
</template>

<script>
import iconChange from '@/views/admin/components/iconChange'
import menuInfo from '@/views/admin/system-page/authority/menus/components/menu-info'

export default {
  name: 'menuSetting',
  components: {
    menuInfo, iconChange
  },
  props: {
    formDetail: Object
  },
  data() {
    return {
      parentRow: {},
      form: {},
      id: '',
      keyFlag: false,
      pageId: '',
      moduleId: '',
      menuForm: {
        genMenuFlag: '2',
        type: '',
        title: ''
      },
      treeArr: [],
      moduleMenuForm: {},
      ruleValidate: {
        type: [{required: true, message: '类型不能为空!', trigger: 'blur'}],
        title: [{required: true, message: '标题不能为空!', trigger: 'blur'}]
      }
    }
  },
  methods: {
    updateModule(param) {
      const [url] = [
        '/api/lowcode/modelDesign/updateModuleByModuleId'
      ]
      let data = {
        'menuId': param.menuId,
        'moduleId': param.moduleId
      }
      this.$http.put(url, data).then(res => {
        console.log(res)
      })
      this.queryTreeList()
      this.menuForm.parentId = param.menuId
    },
    qryMenuByModuleId() {
      this.menuForm.parentId = ''
      this.menuForm.type = ''
      this.menuForm.title = ''
      this.menuForm.orderby = ''
    },
    handleCreateOrEditItem(type) {
      this.$refs.menuInfoRef.moduleflag = true
      this.$refs.menuInfoRef.moduleId = this.moduleId
      this.$refs.menuInfoRef.showModal = true
      this.$refs.menuInfoRef.type = type
      this.form = {orderby: 1}
    },
    queryTreeList() {
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
    getMenuInfo() {
      this.menuForm.url = '/customize/page/' + this.formDetail.pageId
      this.menuForm.name = this.formDetail.pageTable.split(',')[0] + '_' + this.formDetail.pageId
      this.menuForm.component = 'views/lowcode/templateNew/LowcodeTemplateNew.vue'
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
          this.menuForm.genMenuFlag = '1'
        }

      }).catch(() => {
        this.$Message.error('获取菜单失败')
      })
    },
    init() {
      this.menuForm.genMenuFlag = '2'
      this.pageId = this.formDetail.pageId
      // 构建菜单树
      Promise.all([this.queryTreeList()]).then(res => {
        this.getMenuInfo()
      })
      this.$http.get('/api/lowcode/customize/cscpCustomizeVpages?pageId.equals=' + this.pageId).then((res) => {
        if (res.data.data && res.data.data.length > 0) {
          this.moduleId = res.data.data[0].moduleId
        }
      })
    },
    handleSubmit(buttonArr, pageId, pageType) {
      if (this.menuForm.genMenuFlag == '2' && this.menuForm.id !== undefined) {
        // 删除菜单
        const url = `/api/system/menu/deleteMenu/${this.menuForm.id}`
        var flag = 0
        this.$http
            .delete(url)
            .then(() => {
              this.$Message.success('删除成功！')
              flag = 0
            })
            .catch(() => {
              this.$Message.error('删除失败！')
              flag = 1
            })
        return flag
      } else if (this.menuForm.genMenuFlag == '2') {
        var flag = 0
        return flag
      } else {
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
        var flag = 0
        if (data.id === data.parentId) {
          this.$Message.error('更新菜单失败,父级菜单不能选择本身')
          flag = 1
        } else {
          const msg = this.$Message.loading({
            content: isAppendNode ? '正在添加新节点' : '正在更新节点',
            duration: 0
          })
          //如果是列表 且有按钮 设置按钮菜单
          if (pageType && pageType == 1 && buttonArr && buttonArr.length > 0) {
            var childMenu = []
            buttonArr.map(item => {
              var buttonName = item.options ? item.options.label : item.name
              var order = buttonName == '新增' ? 1 :
                  buttonName == '删除' ? 2 :
                      buttonName == '编辑' ? 3 :
                          buttonName == '查询' ? 4 :
                              buttonName == '导入' ? 5 :
                                  buttonName == '导出' ? 6 : 7
              var url = buttonName == '新增' ? '/api/lowcode/customize/page/template/add/' + item.options.linkFormPage :
                  buttonName == '删除' ? '/api/lowcode/customize/page/template/delByPks/' + pageId :
                      buttonName == '编辑' ? '/api/lowcode/customize/page/template/update/' + item.pageId :
                          buttonName == '查询' ? '/api/lowcode/customize/page/template/query/' + pageId :
                              buttonName == '导入' ? '/api/lowcode/customize/page/template/vpage/import/' + pageId :
                                  buttonName == '导出' ? '/api/lowcode/customize/page/template/cscpCustomizeVpages/exportExcel/' + pageId : null

              var httpMethod = buttonName == '新增' ? 'POST' :
                  buttonName == '删除' ? 'DELETE' :
                      buttonName == '编辑' ? 'UPDATE' :
                          buttonName == '查询' ? 'GET' :
                              buttonName == '导入' ? 'POST' :
                                  buttonName == '导出' ? 'POST' : null
              if (url != null) {
                let childMenuDto = {
                  'type': 'button',
                  'title': buttonName,
                  'orderby': order,
                  'url': url,
                  'permissionCode': pageId + item.id,
                  'httpMethod': httpMethod
                }
                childMenu.push(childMenuDto)
              }
            })
          }
          data.childMenusList = childMenu
          this.$http.put(url, data).then(response => {
            msg()
            this.$Message.success(isAppendNode ? '添加菜单成功！' : '更新菜单成功！')
          }).catch(() => {
            msg()
            this.$Message.error(isAppendNode ? '添加菜单失败！' : '更新节点失败！')
          })
        }
        return flag
      }
    }
  },
  created() {
    this.init()
  },
  watch: {
    'menuForm.parentId': {
      handler(newVal, oldVal) {
        newVal ? this.menuForm.type = 'menu2' : this.menuForm.type = 'menu1'
      }
    }
  }
}
</script>

<style lang="less">
.pageLastStep {
  .ivu-form {
    margin: 0 !important;
  }
}
</style>
<style scoped lang="less">
.form-area {
  display: flex;
  align-items: center;
  justify-content: center;

  > img {
    width: 400px;
    height: 400px;
  }
}
</style>
