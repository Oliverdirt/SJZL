<template>
<div style="height: 100%">
  <div class="formCard-content">
    <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
      <div class="searchArea" >
        <Form ref="formInline" :model="formInline" :label-width="80" >
          <Row :gutter="48">
            <Col span="6">
              <FormItem label="租户账号"  prop="tenantAccount">
                <Input   type="text" :maxlength="100" v-model.trim="formInline.tenantAccount" placeholder="请输入租户账号"></Input>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="租户名称" prop="tenantName">
                <Input   type="text" :maxlength="100" v-model.trim="formInline.tenantName" placeholder="请输入租户名称"></Input>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="联系人"  prop="contacts">
                <Input   type="text" :maxlength="100" v-model.trim="formInline.contacts" placeholder="请输入联系人"></Input>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="状态"  prop="activeFlag">
                <Select v-model="formInline.activeFlag" placeholder="请选择">
                  <Option value="1" >激活</Option>
                  <Option  value="0" >未激活</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="24" style="text-align: right;margin-top: 24px">
              <FormItem>
                <Button   type="primary" @click="queryList(true)" style="margin-right: 8px">查询</Button>
                <Button  type="default" @click="handleReset('formInline')">重置</Button>
              </FormItem>
            </Col>
          </Row>
        </Form>
      </div>
    </Card>
    <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
      <div class="tableBtn">
        <Button  icon="md-add" type="primary" code="cscp.tenant.add"  @click="handleAdd()" >新增</Button>
        <Button  icon="ios-trash-outline" type="error"   @click="handleDeleteMultiple()" style="margin-left: 20px">删除</Button>
      </div>
      <Table border :columns="columns" :data="data" @on-selection-change="rowSelected($event)" style="">
        <template slot-scope="{ row, index }" slot="action">
          <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="show(row,index,true)">查看</Button>
          <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="show(row,index,false)">编辑</Button>
          <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="remove(row, index)">删除</Button>
        </template>
        <template slot-scope="{ row }" slot="switch">
          <i-switch v-model="row.activeFlag" :true-value="'1'" :false-value="'0'" @on-change="switchChange(row)"></i-switch>
        </template>
      </Table>
      <Page :total="pageData.total" :page-size-opts="[10, 20, 30, 40]" :page-size="pageData.size" :current="pageData.page" show-elevator show-sizer show-total @on-page-size-change="changePage($event, 'size')" @on-change="changePage($event, 'page')" style="margin-top: 20px;"/>
    </Card>
  </div>
  <Modal
    v-model="modal1"
    :title="title"
    ref="modal"
    @on-cancel="close"
    :width="620"
  >
    <Form  ref="detail" :model="detail" :label-width="80" :rules="ruleValidate" style="margin-top: 20px">
      <Row :gutter="50">
        <Col span="12">
          <FormItem label="租户账号"  prop="tenantAccount">
            <Input   type="text" :maxlength="100" v-model.trim="detail.tenantAccount" placeholder="" :disabled="flag||okFlag==='update'" ></Input>
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="租户名称" prop="tenantName">
            <Input   type="text" :maxlength="100" v-model.trim="detail.tenantName" placeholder="" :disabled="flag"></Input>
          </FormItem>
        </Col>
      </Row>
      <Row :gutter="50">
        <Col span="12">
          <FormItem label="联系人"  prop="contacts">
            <Input   type="text" :maxlength="100" v-model.trim="detail.contacts" placeholder="" :disabled="flag"></Input>
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="联系方式"  prop="phone">
            <Input   type="tel" v-model="detail.phone" placeholder="" :disabled="flag"></Input>
          </FormItem>
        </Col>
      </Row>
      <Row :gutter="50">
        <Col span="12">
          <FormItem label="过期时间"  prop="expireTime">
            <DatePicker v-model="detail.expireTime" type="datetime" placeholder="" :disabled="flag" style="width: 100%"></DatePicker>
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="状态"  prop="activeFlag">
            <RadioGroup v-model="detail.activeFlag"   >
              <Radio label="0" :disabled="flag" >未激活</Radio>
              <Radio label="1" :disabled="flag" >激活</Radio>
            </RadioGroup>
          </FormItem>
        </Col>
      </Row>
      <FormItem label="菜单" style="">
        <Button type="default"  @click="handleExpand()" >展开/折叠</Button>
        <Tree ref="menuSelectTree" :data="menuData"  show-checkbox multiple :expand-node="false"
              :disabled="flag"      style="border: 1px solid #e8eaec;line-height: 24px;overflow: auto;height: 320px"></Tree>
      </FormItem>
    </Form>
    <div slot="footer">
      <Button v-if="!flag" type="primary"  @click="ok(okFlag)"  style="width: 80px">确定</Button>
      <Button type="default"  @click="close"  style="width: 80px">取消</Button>
    </div>
  </Modal>
</div>
</template>

<script>
import {
  validTenantExistByParams,
  delTenantByIds,
  delTenantById,
  getTenantById,
  getMeunByTenantId,
  updateTenantStatusByData,
  updateTenantInfoByData,
  addTenantInfoByData,
  getTenantInfoByParams,
  getTenantAllMeuns
} from '@/api/admin/tenant'
export default {
  name: 'tenantManager',
  data () {
    // 自定义校验
    const validateTenantAccount = (rule, value, callback) => {
      if (this.okFlag === 'update') {
        callback()
        return
      }
      if (value.length > 20) {
        callback(new Error('租户账号不能超过20位！'))
      }
      // 检查用户名是否存在
      validTenantExistByParams({ tenantAccount: this.detail.tenantAccount })
        .then(response => {
          if (response.data.success) {
            callback(new Error('当前租户账号已存在！'))
          } else {
            callback()
          }
        }).catch(() => {
          callback(new Error('异步校验出错！'))
        })
    }
    return {
      modal1: false,
      formInline: {},
      selectedArr: [],
      menuData: [],
      showChildren: true,
      columns: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '租户账号',
          align: 'center',
          key: 'tenantAccount'
        },
        {
          title: '租户名称',
          align: 'center',
          key: 'tenantName'
        },
        {
          title: '联系人',
          align: 'center',
          key: 'contacts'
        },
        {
          title: '联系电话',
          align: 'center',
          key: 'phone'
        },
        {
          title: '状态',
          align: 'center',
          slot: 'switch'
        },
        {
          title: '过期时间',
          key: 'expireTime',
          render: (h, e) => {
            return h('div', [
              h('span', this.$moment(new Date(e.row.expireTime)).format('yyyy-MM-DD HH:mm:ss'))
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
      data: [
      ],
      pageData: {
        total: 0,
        size: 10,
        page: 1
      },
      detail: {},
      flag: false,
      modalValid: true,
      okFlag: '',
      ruleValidate: {
        tenantAccount: [
          { required: true, message: '请输入租户账号', trigger: 'blur' },
          { validator: validateTenantAccount, trigger: 'blur' }
        ],
        tenantName: [
          { required: true, message: '请输入租户名称', trigger: 'blur' },
          { max: 20, message: '租户名称不能超过20位' }
        ],
        contacts: [
          { required: true, message: '请输入联系人', trigger: 'blur' },
          { max: 20, message: '联系人不能超过20位' }
        ],
        phone: [
          { required: true, message: '请输入联系方式', trigger: 'blur' },
          { pattern: /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/, message: '请输入正确的手机号' }
        ],
        expireTime: [
          { required: true, type: 'date', message: '请选择过期时间', trigger: 'change' },
          { validator: (rule, value, callback) => {
            if ((new Date(value).getTime() < new Date().getTime()) && !this.flag) {
              return new Error(`过期时间不能在当前时间之前`)
            } else if ((new Date(value).getTime() < new Date().getTime()) && this.flag) {
              return new Error(`已过期`)
            } else return true
          }
          }
        ]
      }
    }
  },
  mounted () {
    this.queryList()
  },
  computed: {
    title: function () {
      if (this.okFlag === 'add') {
        return '新增'
      } else {
        if (this.flag) {
          return '查看'
        } else return '编辑'
      }
    }
  },
  methods: {
    close () {
      this.modal1 = false
      this.$refs['detail'].resetFields()
      this.menuData = []
    },
    handleDeleteMultiple () {
      let arr = this.selectedArr.map(item => item.id)
      if (arr.length < 1) {
        return
      }
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除勾选的数据？</p>',
        onOk: () => {
          delTenantByIds(arr)
            .then(res => {
              this.$message.success('删除成功')
              this.queryList()
            }).catch(error => {
              if (error.response) {
                this.$Message.error(error.response.data.title ? error.response.data.title : '删除失败！')
              }
            })
        },
        onCancel: () => {}
      })
    },
    handleAdd () {
      this.detail = {}
      this.detail.activeFlag = '1'
      this.flag = false
      this.okFlag = 'add'
      this.getMenus(this.flag)
    },
    handleReset (name) {
      this.$refs[name].resetFields()
      this.pageData.page = 1
      this.queryList()
    },
    rowSelected (e) {
      this.selectedArr = e
    },
    remove (row, index) {
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除这条数据？</p>',
        onOk: () => {
          delTenantById(row.id)
            .then(res => {
              this.$message.success('删除成功')
              this.queryList()
            }).catch(error => {
              if (error.response) {
                this.$Message.error(error.response.data.title ? error.response.data.title : '删除失败！')
              }
            })
        },
        onCancel: () => {}
      })
    },
    show (row, index, flag) {
      getTenantById(row.id)
        .then(res => {
          this.detail = res.data
          this.detail.expireTime = new Date(this.detail.expireTime)
          this.flag = flag
          this.okFlag = 'update'
          getMeunByTenantId(row.id)
            .then(data => {
              this.getMenus(flag, data.data)
            }).catch(() => {
              this.$message.error('菜单查询失败')
            })
        }).catch(() => {
          this.$message.error('行数据查询失败')
        })
    },
    switchChange (row) {
      let obj = {
        id: row.id,
        activeFlag: row.activeFlag
      }
      updateTenantStatusByData(obj)
        .then(res => {
          let text = ''
          if (row.activeFlag === '0') {
            text = '停用租户成功'
          } else text = '激活租户成功'
          this.$Message.success(text)
        }).catch(() => {
          this.$Message.error({
            content: '修改失败',
            onClose () {
              if (row.activeFlag === '0') {
                row.activeFlag = '1'
              } else {
                row.activeFlag = '0'
              }
            }
          })
        })
    },
    ok (type) {
      if (this.flag) {
        this.modal1 = false
        return
      }
      this.menuCheck()
      this.$delete(this.detail, '_index')
      this.$delete(this.detail, '_rowKey')
      let copyDetail = JSON.parse(JSON.stringify(this.detail))
      copyDetail.expireTime = this.$moment(new Date(this.detail.expireTime)).format('yyyy-MM-DD HH:mm:ss')
      this.$refs['detail'].validate((valid) => {
        if (valid) {
          if (type === 'update') {
            updateTenantInfoByData(copyDetail)
              .then(res => {
                this.$Message.success('修改成功')
                this.modal1 = false
                this.queryList()
              }).catch(() => {
                this.$Message.error('修改失败')
              })
          } else if (type === 'add') {
            addTenantInfoByData(copyDetail)
              .then(res => {
                if (res.data.ajax.success) {
                  this.$Message.success(res.data.ajax.msg)
                } else {
                  this.$Message.error(res.data.ajax.msg)
                }
                this.modal1 = false
                this.queryList()
              }).catch(() => {
                this.$Message.error('新增失败')
              })
          }
        } else {
          this.modal1 = true
        }
      })
    },
    queryList (flag) {
      if (flag) {
        this.pageData.page = 1
      }
      let obj = {}
      for (let key in this.formInline) {
        obj[key + '.contains'] = this.formInline[key] === undefined ? null : this.formInline[key]
      }
      let params = Object.assign(obj, {
        page: this.pageData.page - 1,
        size: this.pageData.size
      })
      getTenantInfoByParams(params)
        .then(res => {
          this.data = res.data.data
          this.pageData.total = res.data.recordsTotal
        }).catch(() => {
          this.$message.error('列表查询失败')
        })
    },
    changePage (e, type) {
      if (type === 'size') {
        this.pageData.size = e
      } else if (type === 'page') {
        this.pageData.page = e
      }
      this.queryList()
    },
    getMenus (flag, data) {
      getTenantAllMeuns()
        .then(response => {
          let copyResultData = JSON.parse(JSON.stringify(response.data))
          for (let n = 0; n < copyResultData.length; n++) {
            copyResultData[n].expand = true
            if (flag) {
              copyResultData[n].disabled = true
            } else if (flag === false && this.okFlag === 'update') {
              copyResultData[n].disabled = false
            }
            if (data === undefined) continue
            for (let i = 0; i < data.length; i++) {
              if (data[i].id === copyResultData[n].id) {
                copyResultData[n].checked = true
                break
              }
            }
          }
          const menuTree = this.formatToTree(copyResultData)
          this.menuData = menuTree
          this.modal1 = true
        }).catch(() => {
          this.$message.error('菜单查询失败')
        })
    },
    menuCheck (e) {
      let nodes = this.$refs.menuSelectTree.getCheckedAndIndeterminateNodes()
      this.detail.menus = nodes.map(item => {
        return item.id
      }).join(',')
    },
    formatToTree (ary, pid) {
      return ary
        .filter((item) =>
          // 如果没有父id（第一次递归的时候）将所有父级查询出来
          // 这里认为 item.parentId === 1 就是最顶层 需要根据业务调整
          pid === undefined ? item.parentId === '0' : item.parentId === pid
        )
        .map((item) => {
          // 通过父节点ID查询所有子节点
          item.children = this.formatToTree(ary, item.id)
          return item
        })
    },
    handleExpand () {
      this.showChildren = !this.showChildren
      let checkedNodes = this.$refs.menuSelectTree.getCheckedNodes()
      getTenantAllMeuns()
        .then(response => {
          response.data.forEach(item => {
            item.expand = this.showChildren
            if (this.flag) {
              item.disabled = true
            }
            checkedNodes.forEach(i => {
              if (i.id === item.id) {
                item.checked = true
              }
            })
          })
          const menuTree = this.formatToTree(response.data)
          this.menuData = menuTree
        }).catch(() => {
          this.$message.error('菜单查询失败')
        })
    }
  }
}
</script>

<style lang="less" scoped>
  @import "../admin/styles/formStyle.less";
</style>
