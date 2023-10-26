<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea" >
          <Form ref="queryDicFrom" :model="queryDicFrom" :label-width="80" >
            <Row :gutter="40">
              <Col span="8">
                <FormItem label="字典编码"  prop="dicCode" style="width: 100%">
                  <Input   type="text" :maxlength="100" v-model.trim="queryDicFrom.dicCode" placeholder="请输入字典编码" style="width: 100%"></Input>
                </FormItem>
              </Col>
              <Col span="8">
                <FormItem label="字典名称"  prop="dicName" style="width: 100%">
                  <Input   type="text" :maxlength="100" v-model.trim="queryDicFrom.dicName" placeholder="请输入字典名称" style="width: 100%"></Input>
                </FormItem>
              </Col>
              <Col span="24" style="text-align: right">
                <FormItem :label-width="0">
                  <Button   type="primary" @click="queryList(true)" style="margin-right: 8px">查询</Button>
                  <Button type="default"  @click="handleReset('queryDicFrom')">重置</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="tableBtn">
          <Button  icon="md-add"  type="primary"  @click="handleAdd()">新增</Button>
          <Button  icon="ios-trash-outline" type="error"  @click="handleDeleteMultiple()" style="margin-left: 20px">删除</Button>
        </div>
        <Table border :columns="columns" :data="data" @on-selection-change="rowSelected($event)" style="">
          <template slot-scope="{ row, index }" slot="action">
            <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="editDicItem(row.dicId)">字典配置</Button>
            <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="handleEdit(row,index)">编辑</Button>
            <Button  type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="remove(row, index)">删除</Button>
          </template>
        </Table>
        <Page :total="pageData.total" :page-size="pageData.size" :current="pageData.page" show-elevator show-sizer show-total @on-page-size-change="changePage($event, 'size')" @on-change="changePage($event, 'page')" style="margin-top: 20px;"/>
      </Card>
    </div>
    <Modal
      v-model="dicModal"
      :title="title"
      ref="modal"
      @on-cancel="close"
      :width="580"
    >
      <Form  ref="dicDetail" :model="dicDetail" :label-width="80" :rules="ruleValidate" style="margin-top: 20px">
        <FormItem label="字典编码"  prop="dicCode">
          <Input   type="text" :maxlength="100" v-model.trim="dicDetail.dicCode" placeholder="" ></Input>
        </FormItem>
        <FormItem label="字典名称"  prop="dicName">
          <Input   type="text" :maxlength="100" v-model.trim="dicDetail.dicName" placeholder="" ></Input>
        </FormItem>
        <FormItem label="显示排序" prop="dicSort">
          <InputNumber v-model="dicDetail.dicSort" :min="0" :max="1000000" :precision="0" placeholder="" style="width: 100%"></InputNumber>
        </FormItem>
        <FormItem label="描述" prop="description">
          <Input   type="textarea" :maxlength="100" v-model.trim="dicDetail.description" placeholder="" ></Input>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="primary"  @click="ok(okFlag)" :disabled="disabled"  style="width: 80px">确定</Button>
        <Button type="default"  @click="close()"  style="width: 80px">取消</Button>
      </div>
    </Modal>
    <dic-tree-item-list ref="dicItemList"></dic-tree-item-list>
  </div>
</template>

<script>
import DicTreeItemList from './dicTreeItemList'
import {
  checkDicCodeExists,
  checkDicNameExists,
  queryDicListById,
  delDicListById,
  updateDicListByData,
  addDicListByData,
  getDicListbyParams,
  delDicListByIds
} from '@/api/dic/listdic'
export default {
  name: 'dicTreeManager',
  components: { DicTreeItemList },
  data () {
    // 自定义校验
    const validateDicCode = (rule, value, callback) => {
      this.dicDetail.dicAttr = 'tree'
      checkDicCodeExists(this.dicDetail)
      // 检查dicCode是否存在
        .then(response => {
          if (response.data.code !== 200) {
            callback(new Error(response.data.msg))
          } else {
            callback()
          }
        }).catch(() => {
          callback(new Error('异步校验出错！'))
        })
    }
    const validSort = (rule, value, callback) => {
      setTimeout(() => {
        if (!this.dicDetail.dicSort) {
          callback(new Error('请输入显示排序'))
        } else {
          callback()
        }
      }, 0)
    }
    const validateDicName = (rule, value, callback) => {
      this.dicDetail.dicAttr = 'tree'
      // 检查dicName是否存在
      checkDicNameExists(this.dicDetail)
        .then(response => {
          if (response.data.code !== 200) {
            callback(new Error(response.data.msg))
          } else {
            callback()
          }
        }).catch(() => {
          callback(new Error('异步校验出错！'))
        })
    }
    return {
      dicModal: false,
      disabled: false,
      selectedArr: [],
      queryDicFrom: {},
      pageData: {
        total: 0,
        size: 10,
        page: 1
      },
      columns: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '字典编码',
          key: 'dicCode'
        },
        {
          title: '字典名称',
          key: 'dicName'
        },
        {
          title: '排序',
          align: 'center',
          key: 'dicSort'
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
      dicDetail: {
      },
      okFlag: '',
      modalValid: true,
      ruleValidate: {
        dicName: [
          { required: true, message: '请输入字典名称' },
          { max: 20, message: '字典名称长度不能超过20个字符' },
          { validator: validateDicName, trigger: 'blur' }
        ],
        dicCode: [
          { required: true, message: '请输入字典编码' },
          { max: 20, message: '字典编码长度不能超过20个字符' },
          { validator: validateDicCode, trigger: 'blur' }
        ],
        dicSort: [
          { required: true, validator: validSort, trigger: 'blur' }
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
        return '编辑'
      }
    }
  },
  methods: {

    close () {
      this.dicModal = false
      this.$refs['dicDetail'].resetFields()
      this.disabled = false
    },
    changePage (e, type) {
      if (type === 'size') {
        this.pageData.size = e
      } else if (type === 'page') {
        this.pageData.page = e
      }
      this.queryList()
    },
    // 编辑字典数据
    editDicItem (dicId) {
      this.$refs.dicItemList.edit(dicId)
    },
    handleAdd () {
      this.dicDetail = {}
      this.okFlag = 'add'
      this.dicModal = true
      this.dicDetail.dicSort = 1
    },
    handleEdit (row, index) {
      queryDicListById(row.dicId)
        .then(res => {
          this.dicModal = true
          this.dicDetail = res.data.data
          this.okFlag = 'update'
        }).catch(() => {
          this.$message.error('行数据查询失败')
        })
    },
    handleReset (name) {
      this.$refs[name].resetFields()
      this.selectedArr = []
      this.queryList(true)
    },
    rowSelected (e) {
      this.selectedArr = e
    },
    remove (row, index) {
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除这条数据？</p>',
        onOk: () => {
          delDicListById(row.dicId)
            .then(res => {
              if (res.data.code !== 200) {
                this.$Message.error(res.data.msg)
                return
              } else this.$Message.success(res.data.msg)
              this.queryList()
            }).catch(() => {
              this.$Message.error('删除失败')
            })
        },
        onCancel: () => {}
      })
    },
    ok (type) {
      this.$delete(this.dicDetail, '')
      this.$delete(this.dicDetail, '_rowKey')
      this.dicDetail.dicAttr = 'tree'
      let copyDetail = JSON.parse(JSON.stringify(this.dicDetail))

      this.$refs['dicDetail'].validate((valid) => {
        if (valid) {
          if (type === 'update') {
            updateDicListByData(copyDetail)
              .then(res => {
                if (res.data.code === 200) {
                  this.$Message.success(res.data.msg)
                } else {
                  this.$Message.error(res.data.msg)
                }

                this.dicModal = false
                this.queryList()
              }).catch(() => {
                this.$Message.error('修改失败')
              })
          } else if (type === 'add') {
            addDicListByData(copyDetail)
              .then(res => {
                if (res.data.code === 200) {
                  this.$Message.success(res.data.msg)
                } else {
                  this.$Message.error(res.data.msg)
                }
                this.dicModal = false
                this.queryList()
              }).catch(() => {
                this.$Message.error('新增失败')
              })
          }
        } else {
          this.dicModal = true
        }
      })
    },
    queryList (flag) {
      if (flag) {
        this.pageData.page = 1
      }
      let obj = {}
      for (let key in this.queryDicFrom) {
        obj[key + '.contains'] = this.queryDicFrom[key] === undefined ? null : this.queryDicFrom[key]
      }
      let params = Object.assign(obj, {
        page: this.pageData.page - 1,
        size: this.pageData.size,
        'dicAttr.equals': 'tree'
      })
      getDicListbyParams(params)
        .then(res => {
          this.data = res.data.data
          this.pageData.total = res.data.recordsTotal
        }).catch(() => {
          this.$message.error('列表查询失败')
        })
    },

    handleDeleteMultiple: function () {
      let arr = this.selectedArr.map(item => item.dicId)
      if (arr.length < 1) {
        return this.$Message.error('删除失败,当前选择列表为空！')
      }
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除勾选的数据？</p>',
        onOk: () => {
          delDicListByIds(arr)
            .then(res => {
              if (res.data.code !== 200) {
                this.$message.error(res.data.msg)
              } else this.$message.success(res.data.msg)
              this.queryList()
            }).catch(error => {
              if (error.response) {
                this.$Message.error(error.response.data.title ? error.response.data.title : '删除失败！')
              }
            })
        },
        onCancel: () => {
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../../admin/styles/formStyle.less";
</style>
