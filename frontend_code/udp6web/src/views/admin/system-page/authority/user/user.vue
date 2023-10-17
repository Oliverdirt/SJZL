<style lang="less" scoped>
@import "../../../../../views/admin/styles/formStyle.less";
</style>

<template>
  <div class="user-mg" style="height: 100%">
    <Row :gutter="16" style="height: 100%">
      <Col span="4" style="">
        <Card :bordered="false" :dis-hover="true" :shadow="false" style="height: 100%">
          <Input
            placeholder="输入关键字进行过滤"
            v-model="filterText">
          </Input>
          <el-tree :data="deptTree"   ref="tree" :highlight-current="true" :expand-on-click-node="false"  :filter-node-method="filterNode" @node-click="chooseNode($event)" style="margin-top: 10px"></el-tree>
        </Card>
      </Col>
      <Col span="20">
        <div class="formCard-content">
          <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
            <div class="searchArea" >
              <Form ref="userForms" :model="userForm"  :label-width="80">
                <Row :gutter="48">
                  <Col span="8">
                    <FormItem label="用户名">
                      <Input v-model.trim="userForm.usernameLike" :maxlength="100" placeholder="请输入用户名" />
                    </FormItem>
                  </Col>
                  <Col span="8" >
                    <FormItem label="姓名">
                      <Input v-model.trim="userForm.nameLike"  :maxlength="100" placeholder="请输入姓名" />
                    </FormItem>
                  </Col>
                  <Col span="8" >
                    <FormItem label="手机">
                      <Input  v-model.trim="userForm.mobileLike" :maxlength="11" placeholder="请输入手机号" />
                    </FormItem>
                  </Col>
                  <Col span="24">
                    <FormItem style="text-align: right;margin-top: 24px">
                      <Button type="primary"  @click="handleSearch">查询</Button>
                      <Button type="default" style="margin-left:10px" @click="resetQuery('userForms')">重置</Button>
                    </FormItem>
                  </Col>
                </Row>
              </Form>
            </div>
          </Card>
          <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
            <div class="tableBtn">
              <Button icon="md-add" code="cscp.user.add" @click="handleCreateOrEditItem(null, 'user-add')" type="primary">
                新增
              </Button>
            </div>
            <Table border :columns="columns" :data="data" :loading="isDisabled" @on-row-dblclick="showDetail($event.userId)"></Table>
            <Page :total="dataTotal" show-elevator show-total show-sizer :page-size="pageSize"
            @on-change="changePage" @on-page-size-change="changePageSize" :current="currentPageIndex" style="margin-top: 20px"></Page>
          </Card>
        </div>
      </Col>
    </Row>
    <Modal
      v-model="modalDetail"
      title=""
      :footer-hide="!showFlag"
      ref="modalDetail"
      :width="800"
      @on-cancel="hideUserEdit"
    >
      <userDetailEdit ref="mychild"  :deptId="deptId" :showFlag="showFlag"></userDetailEdit>
      <div slot="footer">
        <Button type="primary" @click="modalDetail=false">确定</Button>
      </div>
    </Modal>
    <Modal  v-model="pswModal"
            title="修改密码"
            :footer-hide="true"
            @on-cancel="newPassword=''"
            ref=""
    >
      <Form ref="pswForm" :model="pswObj" :rules="ruleValidate" >
        <FormItem prop="newPassword">
          <div>
            <span slot='label'>新密码</span>
            <Tooltip :content="passwordRule" max-width="320" transfer>
              <Icon type="md-help-circle" color="#ff9900" size="18" />
            </Tooltip>
          </div>
          <Input type="password" v-model="newPassword"  autocomplete="new-password" placeholder="输入新密码" style="margin-right: 8px;">
            <!-- <Icon type="md-lock" size="14" slot="prepend"></Icon> -->
          </Input>

        </FormItem>
        <FormItem prop="currentUserPwd" label="当前登录密码">
          <Input type="password" v-model="pswObj.currentUserPwd" autocomplete="new-password" placeholder="输入当前登录账号的密码">
            <!-- <Icon type="md-lock" size="14" slot="prepend"></Icon> -->
          </Input><span style="color:#ed3f14;" v-if="currentPasswordError">登录密码错误，请重新填写！</span>
        </FormItem>
        <FormItem style="text-align: right">
          <Button type="primary" @click="submitPsw" style="margin-right: 8px">确定</Button>
          <Button type="default" @click="pswModal=false;newPassword='';pswObj={}">取消</Button>
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>

<script>
import dateMixin from '../../../mixins/date.js'
import userDetailEdit from './userEdit'
import {
  getUserPasswordRule,
  getUserDetailByParams,
  delUserById,
  updateCscpUserPwdByType,
  deblockingAccount
} from '@/api/admin/user'
import {
  getRoleDeptTree2
} from '@/api/admin/dept'
import { size } from 'min-dash'
export default {
  mixins: [dateMixin],
  watch: {
    filterText (val) {
      this.$refs.tree.filter(val)
    },
    modalDetail (val) {
      if (val) {
        this.$refs.mychild.isSubmitDisabled = false
      }
    }
  },
  components: {
    userDetailEdit
  },
  data () {
    return {
      modalDetail: false,
      pswModal: false,
      passwordRule: '',
      newPassword: '',
      currentPasswordError: false,
      showFlag: false,
      isDisabled: false,
      editType: '',
      deptId: '',
      query: '',
      dataTotal: 0,
      currentPageIndex: 1,
      pageSize: 10,
      data: [],
      pswObj: {},
      filterText: '',
      deptTree: [],
      userForm: {},
      currentRowData: {},
      columns: [
        // {
        //   key: 'userId',
        //   title: 'ID'
        // },
        {
          key: 'username',
          title: '用户名',
          align: 'center'
        },
        {
          key: 'name',
          title: '姓名',
          align: 'center'
        },
        {
          key: 'mobile',
          title: '手机',
          align: 'center'
        },
        {
          key: 'email',
          title: '电子邮件'
        },
        {
          key: 'lastLogin',
          title: '最后登录时间',
          align: 'center',
          overflow: 'none',
          textOverflow: 'ellipsis',
          whiteSpace: 'nowrap'
        },
        {
          title: '操作',
          align: 'center',
          key: 'handle',
          width: 350,         
          render: (h, params) => {
            return h('div', [
              h(
                'Button',
                {
                  props: {
                    code: 'cscp.user.edit',
                    type: 'text',
                    size: 'small'
                  },
                  style: {                  
                    display: params.row.adminFlag !== '0' ? 'none' : '',
                    color: '#0056B5'                  
                  },
                  on: {
                    click: () => {
                      this.currentRowData.id = params.row.id
                      this.currentRowData.userId = params.row.userId
                      this.pswModal = true
                      if (params.row.userId) {
                        let id = params.row.userId
                        this.$byStoreSet('user-edit', { id })
                      }
                      this.getPasswordRule()
                    }
                  }
                },
                '修改密码'
              ),
              h(
                'Button',
                {
                  props: {
                    code: 'cscp.user.edit',
                    type: 'text',
                    size: 'small'
                  },
                  style: {                    
                    display: params.row.adminFlag !== '0' ? 'none' : '',
                    color: '#0056B5'
                  },
                  on: {
                    click: () => {
                      this.handleCreateOrEditItem(params.row.userId, 'user-edit')
                    }
                  }
                },
                '编辑'
              ),
              h(
                'Poptip',
                {
                  props: {
                    confirm: true,
                    title: '删除前请确认是否已经移除该人员的流程配置，确定删除这条数据吗？',
                    transfer: true
                  },
                  on: {
                    'on-ok': () => {
                      this.deleteUser(params.row.userId)
                    }
                  }
                },
                [
                  h(
                    'Button',
                    {
                      style: {
                        display: params.row.adminFlag !== '0' ? 'none' : '',
                        color: '#0056B5'
                      },
                      props: {
                        code: 'cscp.user.del',
                        type: 'text',
                        size: 'small',
                        placement: 'top'
                      }
                    },
                    '删除'
                  )
                ]
              ),
              h(
                'Poptip',
                {
                  props: {
                    confirm: true,
                    title: '确定解锁此账号？',
                    transfer: true
                  },
                  on: {
                    'on-ok': () => {
                      this.deblocking(params.row.username, 'cscp.user.deblocking')
                    }
                  }
                },
                [
                  h(
                    'Button',
                    {
                      style: {                   
                        display: params.row.adminFlag !== '0' || !params.row.lockStatus ? 'none' : '',
                        color: '#0056B5',
                        marginRight: params.row.adminFlag !== '0' || !params.row.lockStatus ? '0px' : '-63px'
                      },
                      props: {
                        code: 'cscp.user.deblocking',
                        type: 'text',
                        size: 'small',
                        placement: 'top'
                      }
                    },
                    '解锁'
                  )
                ]
              )
            ])
          }
        }
      ],
      ruleValidate: {
        currentUserPwd: [{ required: true, message: '当前登录密码不能为空', trigger: 'blur' },
          { max: 15, message: '密码长度不能超过15位' }]
      }
    }
  },
  mounted () {
    this.queryTreeList()
    this.$bus.$on('modalShow', (flag) => {
      this.modalDetail = flag
      this.$refs.mychild.$refs.depttree.clear()
      this.init()
    })
  },
  methods: {
    deblocking (username) {
      deblockingAccount(username)
        .then(res => {
          this.getUserList()
          this.$Message.success('解锁成功！')
        })
    },
    init () {
      this.currentPageIndex = 1
      this.getUserList()
    },
    filterNode (value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    getPasswordRule () {
      getUserPasswordRule()
        .then(res => {
          this.passwordRule = res.data
        })
    },
    queryTreeList () {
      getRoleDeptTree2()
        .then(res => {
          this.deptTree = this.getTree(res.data)
        }).catch(() => {
          this.$message.error('部门树查询失败')
        })
    },
    handleSearch () {
      this.init()
    },
    chooseNode (e) {
      this.deptId = e.id
      this.handleSearch()
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
    getUserList () {
      let msg = this.$Message.loading({
        content: '正在更新数据',
        duration: 0
      })
      this.isDisabled = true

      getUserDetailByParams({
        'usernameLike': this.userForm.usernameLike || null,
        'nameLike': this.userForm.nameLike || null,
        'mobileLike': this.userForm.mobileLike || null,
        'deptIdEquals': this.deptId,
        sort: 'user_id',
        page: this.currentPageIndex,
        size: this.pageSize
      }).then(response => {
          this.data = response.data.data.map(el => {
            el.lastLogin = el.lastLogin ? this.formatDate(new Date(el.lastLogin)) : null
            el.registerTime = el.registerTime ? this.formatDate(new Date(el.registerTime)) : null
            return el
          })
          this.dataTotal = response.data.recordsTotal
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
    handleCreateOrEditItem (id, type) {
      if (id) {
        this.$byStoreSet('user-edit', { id })
      }
      this.showFlag = false
      this.modalDetail = true
      this.$forceUpdate()
      this.$refs.mychild.init(type)
    },
    showDetail (id) {
      if (id) {
        this.$byStoreSet('user-edit', { id })
      }
      this.showFlag = true
      this.modalDetail = true
      this.$refs.mychild.init('user-edit')
    },
    changePage (page) {
      this.currentPageIndex = page
      this.getUserList()
    },
    changePageSize (size) {
      this.pageSize = size
      this.getUserList()
    },
    deleteUser (id) {
      delUserById(id)
        .then(resposne => {
          this.init()
          this.$Message.success('删除成功！')
        }).catch(error => {
          if (error.response) {
            this.$Message.error(error.response.data.title ? error.response.data.title : '删除失败！')
          }
        })
    },
    /** 重置按钮操作 */
    resetQuery (name) {
      this.userForm = {}
      this.init()
    },
    submitPsw () {
      if (this.$byStoreGet('user-edit').id !== null) {
        this.$refs['pswForm'].validate(valid => {
          if (valid) {
            const params = Object.assign({}, this.pswObj)
            let data = {
              cscpUserDetailDTO: this.currentRowData,
              currentUserPwd: this.$util.encryptPassword(params.currentUserPwd),
              newPassword: this.$util.encryptPassword(this.newPassword)
            }
            this.currentPasswordError = false
            updateCscpUserPwdByType(0, data)
              .then(resposne => {
                this.$Message.success({
                  content: '密码修改成功',
                  onClose: () => {
                    this.pswModal = false
                    this.pswObj = {}
                    this.newPassword = ''
                    this.currentRowData = {}
                  }
                })
              }).catch(error => {
                this.isSubmitDisabled = false
                if (error.response) {
                  if (error.response.data.detail === '管理员密码不正确！') {
                    this.currentPasswordError = true
                    this.$Message.error('当前登录密码填写错误！')
                  } else {
                    this.$Message.error(error.response.data.detail)
                  }
                }
              })
          }
        })
      }
    },
    hideUserEdit () {
      this.$refs.mychild.$refs.depttree.clear()
    }
  },
  created () {
    this.init()
  }
}
</script>
