<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea" >
          <Form ref="formInline" :model="formInline" :label-width="80" >
            <Row :gutter="48">
              <Col span="8">
                <FormItem label="部门名称"  prop="deptName" style="width: 100%">
                  <Input   type="text" :maxlength="100" v-model.trim="formInline.deptName" placeholder="请输入部门名称" style="width: 100%"></Input>
                </FormItem>
              </Col>
<!--              <Col span="8">
                <FormItem label="状态" prop="status" style="width: 100%">
                  <Select   v-model="formInline.status" >
                    <Option :value="0" >正常</Option>
                    <Option :value="1" >停用</Option>
                  </Select>
                </FormItem>
              </Col>-->
              <Col span="24" style="text-align: right">
                <FormItem :label-width="0">
                  <Button   type="primary" @click="handleSearch()" style="margin-right: 8px">查询</Button>
                  <Button type="default"  @click="handleReset('formInline')">重置</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="tableBtn">
          <Button  icon="md-add" code="cscp.dept.add" type="primary"  @click="handleAdd()">新增</Button>
          <Button type="default" @click="handleExpand()" style="margin-left: 20px">展开/折叠</Button>
        </div>
        <el-table
          v-if="refreshTable"
          v-loading="loading"
          :data="data"
          border
          row-key="deptId"
          :default-expand-all="isExpandAll"
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        >
          <el-table-column prop="deptName" label="部门名称" ></el-table-column>
          <el-table-column prop="orderNum" label="排序" width="200" align="center"></el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime" >
            <template slot-scope="scope">
              <span>{{ handleTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="200">
            <template slot-scope="{ row, index }">
              <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="handleAdd(row, 'addInRow')">新增</Button>
              <Button v-if="row.parentId !== '0'" type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="handleEdit(row)">编辑</Button>
              <Button v-if="row.parentId !== '0'" type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="remove(row, index)">删除</Button>
            </template>
          </el-table-column>
        </el-table>
      </Card>
    </div>
    <Modal
      v-model="modal1"
      :title="title"
      ref="modal"
      @on-cancel="close"
      :width="580"
    >
      <Form  ref="detail" :model="detail" :label-width="80" :rules="ruleValidate" style="margin-top: 20px">
        <FormItem v-if="!rootFlag" label="上级部门"  prop="parentId">
          <Treeselect  v-model="detail.parentId"  :options="treeArr" style="width:100%" placeholder="请输入上级部门">
          </TreeSelect>
        </FormItem>
        <Row :gutter="20">
          <Col span="12">
            <FormItem label="部门名称"  prop="deptName">
              <Input   type="text" :maxlength="100" v-model.trim="detail.deptName" placeholder="" :disabled="flag"></Input>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="显示排序" prop="orderNum">
              <InputNumber   v-model="detail.orderNum" :min="0" :max="1000000" :precision="0" placeholder="" :disabled="flag" style="width: 100%"></InputNumber>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="20">
          <Col span="12">
            <FormItem label="负责人"  prop="leader">
              <Input  type="text" :maxlength="100" v-model.trim="detail.leader" placeholder="" :disabled="flag"></Input>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="邮箱"  prop="email">
              <Input  type="email" v-model.trim="detail.email" placeholder="" :disabled="flag"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="20">
          <Col span="12">
            <FormItem label="联系电话"  prop="phone">
              <Input   type="tel" v-model.trim="detail.phone" placeholder="" :disabled="flag"></Input>
            </FormItem>
          </Col>
        </Row>
      </Form>
      <div slot="footer">
        <Button type="primary"  @click="ok(okFlag)" :disabled="disabled"  style="width: 80px">确定</Button>
        <Button type="default"  @click="close()"  style="width: 80px">取消</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import {
  getDeptByParams,
  getDeptByDeptId,
  delDeptByDeptId,
  updateOrAddDeptByData,
  getDeptList,
  getRoleDeptTree2
} from '@/api/admin/dept'
export default {
  name: 'departmentManager',
  data () {
    const validSort = (rule, value, callback) => {
      setTimeout(() => {
        if (!this.detail.orderNum) {
          callback(new Error('请输入显示排序'))
        } else {
          callback()
        }
      }, 0)
    }
    return {
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 遮罩层
      loading: true,
      modal1: false,
      disabled: false,
      title: '新增',
      treeArr: [],
      formInline: {},
      showChildren: true,
      data: [
      ],
      detail: {
        'deptId': 0,
        'parentId': 0
      },
      flag: false,
      modalValid: true,
      rootFlag: true,
      okFlag: '',
      ruleValidate: {
        leader: [{ pattern: /^[\u4e00-\u9fa5a-zA-Z]+$/, message: '仅支持中文，英文字母' },
          {
            max: 20,
            message: '负责人不可以超过20个字符',
            trigger: 'blur'
          }],
        deptName: [
          { required: true, message: '请输入部门名称' },
          { max: 20, message: '部门长度不能超过20个字符' }
        ],
        parentId: [
          { required: true, message: '请选择上级部门' }
        ],
        orderNum: [
          { required: true, validator: validSort, trigger: 'blur' }
        ],
        email: [
          { required: false, message: '请输入正确的邮箱', pattern: /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/ },
          { max: 45, message: '邮箱长度不能超过45位' }
        ],
        phone: [
          { pattern: /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/, message: '请输入正确的手机号' }
        ]
      }
    }
  },
  mounted () {
    this.queryList()
    this.queryTreeList()
  },
  methods: {
    handleTime (createTime) {
      return this.$moment(new Date(createTime)).format('yyyy-MM-DD HH:mm:ss')
    },
    close () {
      this.modal1 = false
      this.$refs['detail'].resetFields()
      this.disabled = false
    },
    handleExpand () {
      this.refreshTable = false
      this.isExpandAll = !this.isExpandAll
      this.$nextTick(() => {
        this.refreshTable = true
      })
    },
    handleSearch () {
      let url = '?'
      let arr = []
      for (let key in this.formInline) {
        arr.push(key + '.contains=' + (this.formInline[key] === undefined ? '' : this.formInline[key]))
      }
      url += arr.join('&')
      getDeptByParams(url)
      // this.$http.get('/api/cscpDepts' + url)
        .then(res => {
          res.data.data.forEach(item => {
            item._showChildren = true
          })
          this.data = this.handleTree(res.data.data, 'deptId')
          this.loading = false
        }).catch(() => {
          this.$message.error('列表查询失败')
        })
    },
    /**
     * 构造树型结构数据
     * @param {*} data 数据源
     * @param {*} id id字段 默认 'id'
     * @param {*} parentId 父节点字段 默认 'parentId'
     * @param {*} children 孩子节点字段 默认 'children'
     */
    handleTree (data, id, parentId, children) {
      let config = {
        id: id || 'id',
        parentId: parentId || 'parentId',
        childrenList: children || 'children'
      }

      var childrenListMap = {}
      var nodeIds = {}
      var tree = []

      for (let d of data) {
        let parentId = d[config.parentId]
        if (childrenListMap[parentId] == null) {
          childrenListMap[parentId] = []
        }
        nodeIds[d[config.id]] = d
        childrenListMap[parentId].push(d)
      }

      for (let d of data) {
        let parentId = d[config.parentId]
        if (nodeIds[parentId] == null) {
          tree.push(d)
        }
      }

      for (let t of tree) {
        adaptToChildrenList(t)
      }

      function adaptToChildrenList (o) {
        if (childrenListMap[o[config.id]] !== null) {
          o[config.childrenList] = childrenListMap[o[config.id]]
        }
        if (o[config.childrenList]) {
          for (let c of o[config.childrenList]) {
            adaptToChildrenList(c)
          }
        }
      }
      return tree
    },
    handleAdd (row, type) {
      this.title = '新增'
      this.rootFlag = false
      this.detail = {}
      //  this.detail.status = '0'
      this.detail.orderNum = 1
      if (type === 'addInRow') {
        this.detail.parentId = row.deptId
      }
      this.modal1 = true
      this.flag = false
      this.okFlag = 'add'
    },
    handleEdit (row) {
      this.title = '修改'
      this.modal1 = true
      this.flag = false
      this.okFlag = 'update'
      if (row.parentId === '0') {
        this.rootFlag = true
      } else {
        this.rootFlag = false
      }
      getDeptByDeptId(row.deptId)
      // this.$http.get('/api/cscpDepts?deptId.equals=' + row.deptId)
        .then(res => {
          this.detail = res.data.data[0]
        }).catch(() => {
          this.$message.error('行数据查询失败')
        })
    },
    handleReset (name) {
      this.$refs[name].resetFields()
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
          delDeptByDeptId(row.deptId)
          // this.$http.delete('/api/cscpDepts/' + row.deptId)
            .then(res => {
              if (res.data.msg) {
                this.$Message.error(res.data.msg)
                return
              } else this.$Message.success('删除成功')
              this.queryList()
            }).catch(() => {
              this.$Message.error('删除失败')
            })
        },
        onCancel: () => {}
      })
    },
    ok (type) {
      this.$refs['detail'].validate((valid) => {
        if (valid) {
          this.disabled = true
          if (type === 'update') {
            updateOrAddDeptByData('put', this.detail)
            // this.$http.put('/api/cscpDepts', this.detail)
              .then(res => {
                if (res.data.code === 1) {
                  this.$Message.error(res.data.msg)
                  this.modal1 = false
                  this.disabled = false
                  this.$refs['detail'].resetFields()
                  return
                }
                this.$Message.success('修改成功')
                this.modal1 = false
                this.$refs['detail'].resetFields()
                this.queryList()
                this.queryTreeList()
              }).catch(error => {
                if (error.response) {
                  this.$Message.error(error.response.data.title ? error.response.data.title : '修改失败！')
                }
              })
          } else if (type === 'add') {
            this.detail.createTime = new Date()
            updateOrAddDeptByData('post', this.detail)
            // this.$http.post('/api/cscpDepts', this.detail)
              .then(res => {
                if (res.data.code === 1) {
                  this.$Message.error(res.data.msg)
                  this.disabled = false
                  this.modal1 = false
                  this.$refs['detail'].resetFields()
                  return
                }
                this.$Message.success('新增成功')
                this.modal1 = false
                this.queryList()
                this.queryTreeList()
                this.$refs['detail'].resetFields()
              }).catch(error => {
                this.$Message.error(error.response.data.title ? error.response.data.title : '新增失败！')
                this.disabled = false
              })
          }
        } else {
          this.modal1 = true
        }
      })
    },
    queryList () {
      getDeptList()
      // this.$http.get('/api/cscpDepts')
        .then(res => {
          res.data.data.forEach(item => {
            item._showChildren = true
          })
          this.data = this.handleTree(res.data.data, 'deptId')
          this.loading = false
          this.disabled = false
        }).catch(() => {
          this.$message.error('列表查询失败')
        })
    },
    queryTreeList () {
      getRoleDeptTree2()
      // this.$http.get('/api/cscpDepts/treeselect')
        .then(res => {
          this.treeArr = res.data
        }).catch(() => {
          this.$message.error('部门树查询失败')
        })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../../../admin/styles/formStyle.less";

</style>
