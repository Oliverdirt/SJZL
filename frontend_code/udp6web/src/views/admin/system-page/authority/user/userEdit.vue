<template>
  <div style="display: flex;justify-content: center">
    <Row :gutter="16" type="flex" justify="center" align="top" style="width: 100%">
      <Col span="24" style="border: #fff 1px solid; margin:5px;">
        <Form ref="userDetailForm" :model="userDetail" :rules="ruleValidate" :disabled="showFlag">
          <Row type="flex" justify="center" :gutter="16">
            <Col span="9">
              <FormItem prop="username" label="用户名">
                <Input type="text" v-model.trim="userDetail.username" placeholder="用户名" :disabled="!usernameEditable">
                  <!-- <Icon type="md-person" size="14" slot="prepend"></Icon> -->
                </Input>
              </FormItem>
            </Col>
            <Col span="9">
              <FormItem prop="name" label="姓名">
                <Input type="text" v-model.trim="userDetail.name" placeholder="姓名">
                  <!-- <Icon type="md-person" size="14" slot="prepend"></Icon> -->
                </Input>
              </FormItem>
            </Col>
            <Col span="9">
              <FormItem prop="deptId" label="部门">
                <treeselect
                  v-model="userDetail.deptId"
                  @select="hideError"
                  ref="depttree"
                  :options="treeArr"
                  :disabled="showFlag"
                  autocomplete=“off”
                  placeholder="选择部门" />
              </FormItem>
            </Col>
            <Col span="9">
              <FormItem prop="postIds" label="岗位">
                <Select ref="postSelect" v-model="posts" multiple>
                  <Option v-for="option in postOptions" :value="option.value" :key="option.value">{{ option.label }}
                  </Option>
                </Select>
              </FormItem>
            </Col>
            <!--            <Col span="6">
                          <FormItem prop="name" label="名">
                            <Input type="text" v-model="userDetail.name" placeholder="名">
                              &lt;!&ndash; <Icon type="md-person" size="14" slot="prepend"></Icon> &ndash;&gt;
                            </Input>
                          </FormItem>
                        </Col>-->
            <Col span="9">
              <FormItem prop="roleIds" label="角色" >
                <Select ref="roleSelect" v-model="roles" multiple >
                  <Option v-for="option in roleOptions" :value="option.value" :key="option.value">{{ option.label }}
                  </Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="9">
              <FormItem prop="mobile" label="手机号码">
                <Input type="text" v-model="userDetail.mobile" placeholder="手机">
                  <!-- <Icon type="md-call" size="14" slot="prepend"></Icon> -->
                </Input>
              </FormItem>
            </Col>
            <Col span="18">
              <FormItem prop="email" label="电子邮件">
                <Input type="text" v-model="userDetail.email" placeholder="电子邮件">
                  <!-- <Icon type="md-mail" size="14" slot="prepend"></Icon> -->
                </Input>
              </FormItem>
            </Col>
            <Col span="18" >
              <FormItem prop="imgPath" label="头像">
                <Input type="text" v-model="userDetail.imgPath" placeholder="头像" style="display:none">
                </Input>
                <Row type="flex" justify="start" align="middle">
                  <img :src="userDetail.imgPath?userDetail.imgPath:require('@/assets/header.png')"
                       style="width:32px;height:32px;background: #619fe7;margin: 0 10px;border-radius:50%;">
                  <Button type="primary" shape="circle" icon="md-add" @click="showModal()"></Button>
                </Row>
              </FormItem>
            </Col>
            <Col span="18">
              <FormItem prop="discTitle" label="个人描述标题">
                <Input type="text" v-model.trim="userDetail.discTitle" placeholder="个人描述标题">
                  <!-- <Icon type="md-create" size="14" slot="prepend"></Icon> -->
                </Input>
              </FormItem>
            </Col>
            <Col span="18">
              <FormItem prop="discDetail" label="个人描述">
                <Input type="textarea" :autosize="{minRows: 2,maxRows: 10}" v-model="userDetail.discDetail"
                       placeholder="个人描述">
                </Input>
              </FormItem>
            </Col>
            <Col span="9">
              <FormItem prop="registerTime" label="注册时间">
                <Input type="text" :value="formatTime(userDetail.registerTime)" placeholder="注册时间" disabled>
                  <!-- <Icon type="md-time" size="14" slot="prepend"></Icon> -->
                </Input>
              </FormItem>
            </Col>
            <Col span="9">
              <FormItem prop="lastLogin" label="最后登录时间">
                <Input type="text" :value="formatTime(userDetail.lastLogin)" placeholder="最后登录时间" disabled>
                  <!-- <Icon type="md-time" size="14" slot="prepend"></Icon> -->
                </Input>
              </FormItem>
            </Col>
            <hr style="margin-bottom:15px;">
            </hr>
            <hr style="margin-bottom:15px;">
            </hr>
          </Row>
          <Row v-if="!showFlag" type="flex" justify="center" style="margin-top: 16px;">
            <Button type="primary" @click="handleSubmit('userDetailForm')" :disabled="isSubmitDisabled">提交</Button>
            <Button type="default" @click="handleReset1" style="margin: 0 16px">重置</Button>
          </Row>
        </Form>
      </Col>
    </Row>
    <Modal title="设置头像" v-model="modalShow" :mask-closable="false" :closable="false"
           class-name="vertical-center-modal" width="80%">
      <Row :gutter="10" class="image-editor" v-if="isShow">
        <Col span="14" class="image-editor-con1">
          <vueCropper
            ref="cropper"
            :img="cropper1.img"
            :outputSize="cropper1.size"
            :outputType="cropper1.outputType"
            :autoCrop="true"
            :fixed="true"
            :fixedNumber="[1,1]"
            :autoCropWidth="200"
            :autoCropHeight="200"
            :mode="'cover'"
            @realTime="realTime($event)"
          ></vueCropper>
        </Col>
        <Col span="10" class="image-editor-con1">
          <Row type="flex" justify="center" align="middle" class="image-editor-con1-preview-con" style="overflow: hidden ">
            <div class="show-preview">
              <div :style="previews.div" style="overflow: hidden">
                <img :src="cropper1.img" :style="previews.img" >
              </div>
            </div>
          </Row>
          <div class="image-editor-con1-btn-con margin-top-10">
            <input type="file" accept="image/png, image/jpeg, image/gif, image/jpg" @change="handleChange1"
                   id="fileinput1" class="fileinput"/>
            <label class="filelabel" for="fileinput1">
              <Icon type="md-image" size="20"></Icon>&nbsp;选择图片
            </label>
            <ButtonGroup style="margin-left: 16px;">
              <Button @click="$refs.cropper.changeScale(0.1)" type="primary">
                <Icon :size="14" type="md-add"></Icon>
              </Button>
              <Button @click="$refs.cropper.changeScale(-0.1)" type="primary">
                <Icon :size="14" type="md-remove"></Icon>
              </Button>
            </ButtonGroup>
          </div>
        </Col>
      </Row>
      <div slot="footer">
        <Row type="flex" justify="end">
          <Button type="default" @click="handleCancelCrop">取消</Button>
          <Button type="primary" @click="handlecrop1">确定</Button>
        </Row>
      </div>
    </Modal>
  </div>
</template>

<script>
import moment from 'moment'
import { VueCropper } from 'vue-cropper'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {
  validUserExistByUserName,
  getUserDetailsByUserId,
  updateUserDetailsByType,
  addUserDetailsByData
} from '@/api/admin/user'
import {
  getRoleDeptTree2
} from '@/api/admin/dept'

import {
  getPostByParams
} from '@/api/admin/post'
import {
  getRoleListByParams
} from '@/api/admin/role'
export default {
  name: 'userDetailEdit',
  components: {
    Treeselect,
    VueCropper
  },
  props: ['deptId', 'showFlag'],
  data () {
    // 自定义校验
    const validateUserName = (rule, value, callback) => {
      if (!this.usernameEditable) {
        callback()
        return
      }
      let pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>《》/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？ ]")
      if (pattern.test(value)) {
        callback(new Error('用户名称不允许存在特殊字符！'))
        return
      }
      if (value.length > 45) {
        callback(new Error('用户名称长度不允许超过45位！'))
        return
      }
      // 检查用户名是否存在
      validUserExistByUserName({ username: this.userDetail.username })
        .then(response => {
          if (response.data.success) {
            callback(new Error('当前账号已存在，请重新输入！'))
          } else {
            callback()
          }
        }).catch(() => {
          callback(new Error('异步校验出错！'))
        })
    }
    const validateDept = (rule, value, callback) => {
      if (this.userDetail.deptId === undefined) {
        callback(new Error('请选择部门！'))
      } else return true
    }
    return {
      editType: '',
      curTypeNode: null,
      treeArr: [],
      disabled: false,
      // 机构树数据
      treeoOrgData: [],
      userId: null,
      usernameEditable: true,
      isSubmitDisabled: false,
      userDetail: {},
      roles: [],
      posts: [],
      isShow: false,
      roleOptions: [],
      postOptions: [],
      groupOptions: [],
      ruleValidate: {
        username: [
          { required: true, message: '用户名不能为空！', trugger: 'blur' },
          { validator: validateUserName, trigger: 'blur' }
        ],
        /*   familyName: [{ required: true, message: '姓不能为空！', trigger: 'blur' },
          { max: 30, trigger: 'blur', message: '姓长度不可以超过30位！' }], */
        name: [{ required: true, message: '姓名不能为空！', trigger: 'blur' },
          { max: 30, trigger: 'blur', message: '姓名长度不可以超过30位！' }],
        mobile: [{ required: false, message: '请输入正确的手机号码', pattern: /^1[0-9]{10}$/ }],
        deptId: [
          {
            required: true, validator: validateDept
          }
        ],
        email: [{
          required: false,
          message: '请输入正确的邮箱',
          pattern: /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/
        },
        { max: 45, message: '邮箱长度不能超过45位' }],
        discTitle: [{ max: 45, message: '个人描述标题不能超过45位' }],
        discDetail: [{ max: 500, message: '个人描述不能超过500位' }],
        orgId: [{ required: true, message: '机构不能为空！', trugger: 'blur' }]
      },
      modalShow: false,
      cropper1: {},
      previews: {},
      previewStyle: {},
      option1: {
        showCropedImage: false,
        cropedImg: ''
      },
      img1: null,
      cropdata1: {
        x: '',
        y: '',
        w: '',
        h: '',
        deg: '',
        scaleX: '',
        scaleY: ''
      }
    }
  },
  watch: {
    modalShow (val) {
      if (val) {

      }
    }
  },
  mounted () {
  },
  methods: {
    init (type) {
      this.isShow = true
      this.editType = type
      this.queryTreeList()
      this.initRoles()
      this.initPosts()
      this.handleReset()
    },
    formatTime (time) {
      if (time) {
        return moment(time).format('YYYY-MM-DD HH:mm:ss')
      }
    },
    queryTreeList () {
      getRoleDeptTree2()
        .then(res => {
          this.treeArr = res.data
        })
    },
    orgClick (node, instanceId) {
      this.curTypeNode = node
    },
    /**
     * 构造树型结构数据
     * @param {*} data 数据源
     * @param {*} id id字段 默认 'id'
     * @param {*} parentId 父节点字段 默认 'parentId'
     * @param {*} children 孩子节点字段 默认 'children'
     * @param {*} rootId 根Id 默认 0
     */
    handleTree (data, id, parentId, children, rootId) {
      id = id || 'id'
      parentId = parentId || 'parentId'
      children = children || 'children'
      rootId = rootId || Math.min.apply(Math, data.map(item => {
        return parseInt(item[parentId])
      })) || 0
      // 对源数据深度克隆
      const cloneData = JSON.parse(JSON.stringify(data))
      // 循环所有项
      const treeData = cloneData.filter(father => {
        let branchArr = cloneData.filter(child => {
          // 返回每一项的子级数组
          return father[id] === child[parentId]
        })
        // eslint-disable-next-line no-unused-expressions
        branchArr.length > 0 ? father.children = branchArr : ''
        // 返回第一层
        return parseInt(father[parentId]) === rootId
      })
      return treeData !== '' ? treeData : data
    },
    /** 转换类型数据结构 */
    normalizer (node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      this.curTypeNode = node
      return {
        id: node.id,
        label: node.orgName,
        children: node.children
      }
    },
    handleSubmit: function (name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          this.isSubmitDisabled = true
          const params = Object.assign({}, this.userDetail)
          params.roles = this.roles.join()
          params.posts = this.posts.join()
          delete params.roleIds
          delete params.roleNames
          delete params.rolesList
          delete params.postIds
          delete params.postNames
          delete params.postsList
          let data = null
          if (this.userId !== null) {
            data = {
              cscpUserDetailDTO: params
            }
            delete params.currentUserPwd
          } else {
            data = params
          }

          this.submitUserDetails(this.userId, data)
        }
      })
    },
    submitUserDetails (userId, data) {
      this.isShow = false
      this.cropdata1 = {
        x: '',
        y: '',
        w: '',
        h: '',
        deg: '',
        scaleX: '',
        scaleY: ''
      }
      this.cropper1 = {}
      let msg = this.$Message.loading({
        content: (this.userId !== null) ? '正在更新用户' : '正在创建新用户',
        duration: 0
      })
      if (userId !== null) {
        this.currentPasswordError = false
        updateUserDetailsByType(0, data)
          .then(resposne => {
            msg()
            this.$Message.success({
              content: '更新用户成功！',
              onClose: () => {
                this.isShow = true
                this.userDetail = {}
                this.$bus.$emit('modalShow', false)
              }
            })
          }).catch(error => {
            msg()
            if (error.response) {
              if (error.response.data.detail === '管理员密码不正确！') {
                this.currentPasswordError = true
                this.$Message.error('当前登录密码填写错误！')
              } else {
                this.$Message.error(error.response.data.detail)
              }
            }
          })
      } else {
        addUserDetailsByData(data)
          .then(resposne => {
            msg()
            this.$Message.success({
              content: '新建用户成功！',
              onClose: () => {
                this.userDetail = {}
                this.isShow = true
                this.$bus.$emit('modalShow', false)
              }
            })
          }).catch(error => {
            msg()
            if (error.response) {
              this.$Message.error(error.response.data.detail)
            }
          })
      }
    },
    handleReset () {
      if (this.editType === 'user-edit') {
        this.usernameEditable = false
        this.userId = this.$byStoreGet('user-edit').id
        this.getData(this.userId)
      } else {
        this.userId = null
        this.userDetail = {
          id: null,
          did: null,
          username: null,
          familyName: null,
          name: null,
          mobile: null,
          email: null,
          imgPath: null,
          lastLogin: null,
          discTitle: null,
          discDetail: null,
          registerTime: null,
          roleIds: [],
          roleNames: [],
          rolesList: [],
          postIds: [],
          postNames: [],
          postsList: [],
          currentUserPwd: '',
          groups: []
        }
        this.usernameEditable = true
        this.roles = []
        this.posts = []
        if (this.$refs['userDetailForm'] !== undefined) {
          this.$refs['userDetailForm'].resetFields()
        }
        this.userDetail.deptId = this.deptId ? this.deptId : null
        this.$refs.depttree.select(this.$refs.depttree.getNode(this.deptId))
      }
    },
    handleReset1 () {
      if (this.editType === 'user-edit') {
        this.usernameEditable = false
        this.userId = this.$byStoreGet('user-edit').id
        this.getData(this.userId)
      } else {
        this.userId = null
        this.userDetail = {
          id: null,
          did: null,
          username: null,
          familyName: null,
          name: null,
          mobile: null,
          email: null,
          imgPath: null,
          lastLogin: null,
          discTitle: null,
          discDetail: null,
          registerTime: null,
          roleIds: [],
          roleNames: [],
          postIds: [],
          postNames: [],
          currentUserPwd: '',
          groups: []
        }
        this.userDetail.deptId = undefined
        this.usernameEditable = true
        this.roles = []
        this.posts = []
        if (this.$refs['userDetailForm'] !== undefined) {
          this.$refs['userDetailForm'].resetFields()
        }
        this.$refs.depttree.clear()
      }
    },
    getData (userId) {
      if (!userId) {
        return
      }
      let msg = this.$Message.loading({
        content: '正在获取用户信息',
        duration: 0
      })
      getUserDetailsByUserId({ userId })
        .then(response => {
          response.data.currentUserPwd = ''
          this.userDetail = response.data
          this.roles = response.data.roleIds === null ? [] : response.data.roleIds.split(',')
          this.posts = response.data.postIds === null ? [] : response.data.postIds.split(',')
          msg()
        }).catch(error => {
          msg()
          if (error.response) {
            this.$Message.error('获取用户信息失败！')
          }
        })
    },
    initRoles () {
      getRoleListByParams({ size: 1000 })
        .then(response => {
          console.log(response, 'response')
          this.roleOptions = []
          for (const item of response.data.data) {
            this.roleOptions.push({
              value: item.id.toString(),
              label: item.name,
              icon: item.icon
            })
          }
        }).catch()
    },
    initPosts () {
      getPostByParams({ size: 1000 })
        .then(response => {
          console.log(response, 'response')
          this.postOptions = []
          for (const item of response.data.data) {
            this.postOptions.push({
              value: item.postId.toString(),
              label: item.postName
            })
          }
        }).catch()
    },
    realTime (data) {
      this.previewStyle = {
        width: data.w + 'px',
        height: data.h + 'px',
        overflow: 'hidden',
        margin: '0',
        zoom: 100 / data.h
      }
      this.previews = data
    },
    handleChange1 (e) {
      let file = e.target.files[0]
      let reader = new FileReader()
      reader.onload = () => {
        this.cropper1.img = reader.result
        this.$forceUpdate()
        reader.onload = null
      }
      reader.readAsDataURL(file)
    },
    handlecrop1 () {
      this.$refs.cropper.getCropData(data => {
        // do something
        if (data.length > 250 * 1024) {
          this.$Message.warning('选择的区域太大，超过了250K。请缩小选择区域！')
          return
        }
        this.option1.cropedImg = data
        this.userDetail.imgPath = data
        this.modalShow = false
      })
    },
    handleCancelCrop () {
      this.modalShow = false
      this.cropper1 = {}
    },
    showModal () {
      this.isShow = true
      this.modalShow = true
    },
    hideError () {
      if (this.userDetail.deptId === undefined) {
        if (this.$refs.depttree.$parent.$el.getElementsByClassName('ivu-form-item-error-tip').length > 0) {
          this.$refs.depttree.$parent.$el.getElementsByClassName('ivu-form-item-error-tip')[0].style.display = 'none'
        }
      }
    }
  }
}
</script>

<style lang="less">
@import 'image-editor.less';
@import '../../../styles/common.less';

.vertical-center-modal {
  display: flex;
  align-items: center;
  justify-content: center;

  .ivu-modal {
    top: 0;
  }
}
</style>
