<template>
  <div style="height: 100%;position: absolute"  >
      <!-- 抽屉 -->
      <Drawer
        title="树型字典项列表"
        v-model="visible"
        width="800"
        :closable="false"
      >
        <div class="dataArea">
          <Form ref="itemQuery" :model="itemQuery" :label-width="90" >
            <Row :gutter="40">
              <Col span="12">
                <FormItem label="字典项编码"  prop="itemCode" style="width: 100%">
                  <Input   type="text" :maxlength="100" v-model.trim="itemQuery.itemCode" placeholder="请输入字典项编码" style="width: 100%"></Input>
                </FormItem>
              </Col>
              <Col span="12">
                <FormItem label="字典值"  prop="itemValue" style="width: 100%">
                  <Input  type="text" :maxlength="100" v-model.trim="itemQuery.itemValue" placeholder="请输入字典值" style="width: 100%"></Input>
                </FormItem>
              </Col>
              <Col span="24" style="text-align: right">
                <FormItem :label-width="0">
                  <Button   type="primary" @click="handleSearch()" style="margin-right: 8px">查询</Button>
                  <Button type="default"  @click="handleReset('itemQuery')">重置</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
          <div class="operateBtn">
            <Button  icon="md-add"  type="primary"  @click="handleAdd()">新增</Button>
            <Button type="default" @click="handleExpand()" style="margin-left: 20px">展开/折叠</Button>
          </div>
          <!-- <Table  row-key="itemId" ref="table"  :columns="columns" :data="itemData"  style="">
            <template slot-scope="{ row, index }" slot="action">
              <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="handleAdd(row, 'addInRow')">新增</Button>
              <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="handleEdit(row)">编辑</Button>
              <Button type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="remove(row, index)">删除</Button>
            </template>
          </Table> -->
          <el-table
          v-if="refreshTable"
          v-loading="loading"
          :data="itemData"
          row-key="itemId"
          :default-expand-all="isExpandAll"
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
          >
            <el-table-column prop="itemCode" label="字典项编码" ></el-table-column>
            <el-table-column prop="itemValue" label="字典值" align="center"></el-table-column>
            <el-table-column prop="itemSort" label="排序" width="200" align="center"></el-table-column>
            <el-table-column label="操作" align="center" width="200">
              <template slot-scope="{ row, index }">
                <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="handleAdd(row, 'addInRow')">新增</Button>
                <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="handleEdit(row)">编辑</Button>
                <Button type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="remove(row, index)">删除</Button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </Drawer>
    <Modal
      v-model="itemTreeModal"
      :title="title"
      ref="modal"
      @on-cancel="close"
      :width="620"
    >
      <Form  ref="itemDetail" :model="itemDetail" :label-width="100" :rules="ruleValidate" style="margin-top: 20px">
        <FormItem  label="父级节点"  prop="itemParentId">
          <Treeselect  v-model="itemDetail.itemParentId"  :options="treeArr" style="width:100%"  placeholder="请输入父级节点"/>
        </FormItem>
        <FormItem label="字典项编码"  prop="itemCode">
          <Input   type="text" :maxlength="100" v-model.trim="itemDetail.itemCode" placeholder="" ></Input>
        </FormItem>
        <FormItem label="字典值"  prop="itemValue">
          <Input   type="text" :maxlength="100" v-model.trim="itemDetail.itemValue" placeholder="" ></Input>
        </FormItem>
        <FormItem label="显示排序" prop="itemSort">
          <InputNumber v-model="itemDetail.itemSort" :min="0" :max="1000000" :precision="0" placeholder=""  style="width: 100%"></InputNumber>
        </FormItem>
        <FormItem label="描述" prop="description">
          <Input   type="textarea" :maxlength="100" v-model.trim="itemDetail.description" placeholder="" ></Input>
        </FormItem>
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
  checkDicItemCodeExists,
  getDicItemsbyDicId,
  updateDicItemByData,
  addDicItemByData,
  getDicItemByQuery,
  queryDicItemById,
  getDicItemByParams,
  getTreeListByDicId,
  delTreeDicItemById
} from '@/api/dic/listdic'
export default {
  name: 'dicTreeItemList',
  data () {
    const validateDicItemCode = (rule, value, callback) => {
      this.itemDetail.dicId = this.dicId
      // 检查itemCode是否存在
      checkDicItemCodeExists(this.itemDetail)
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
        if (!this.itemDetail.itemSort) {
          callback(new Error('请输入显示排序'))
        } else {
          callback()
        }
      }, 0)
    }
    return {
      isExpandAll: true,
      refreshTable: true,
      loading: true,
      itemTreeModal: false,
      disabled: false,
      title: '新增',
      treeArr: [],
      formInline: {},
      showChildren: true,
      visible: false,
      okFlag: '',
      itemQuery: {
        itemCode: undefined,
        itemValue: undefined
      },
      itemData: [],
      itemDetail: {
        'itemId': 0,
        'itemParentId': null,
        'itemCode': '',
        'itemSort': null,
        'itemValue': '',
        'dicId': null
      },
      dicId: null,
      // columns: [
      //   {
      //     title: '字典项编码',
      //     key: 'itemCode',
      //     width: 200,
      //     tree: true
      //   },
      //   {
      //     title: '字典值',
      //     key: 'itemValue',
      //     width: 150
      //   },
      //   {
      //     title: '排序',
      //     key: 'itemSort',
      //     width: 70,
      //     align: 'center'
      //   },
      //   {
      //     title: '操作',
      //     slot: 'action',
      //     width: 260,
      //     align: 'center'
      //   }
      // ],
      ruleValidate: {
        itemCode: [
          { required: true, message: '请输入字典项编码' },
          { max: 20, message: '字典项编码不能超过20个字符' },
          { validator: validateDicItemCode, trigger: 'blur' }
        ],
        itemValue: [
          { required: true, message: '请输入字典值' },
          { max: 20, message: '字典值长度不能超过20个字符' }
        ],
        itemSort: [
          { required: true, validator: validSort, trigger: 'blur' }
        ]
      }

    }
  },
  created () {
  },
  methods: {
    edit (dicId) {
      this.visible = true
      getDicItemsbyDicId(dicId)
      // this.$http.get('/api/dic/cscpHxDicItems/query/' + dicId)
        .then(res => {
          res.data.data.forEach(item => {
            item._showChildren = true
          })
          this.itemData = this.handleTree(res.data.data)
          this.loading = false
          this.dicId = dicId
          this.queryTreeList()
        }).catch(() => {
          this.$message.error('行数据查询失败')
        })
    },
    close () {
      this.itemTreeModal = false
      this.$refs['itemDetail'].resetFields()
      this.disabled = false
    },
    remove (row, index) {
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除这条数据？</p>',
        onOk: () => {
          delTreeDicItemById(row.itemId)
            .then(res => {
              if (res.data.code == 500) {
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
      this.$refs['itemDetail'].validate((valid) => {
        if (valid) {
          this.disabled = true
          this.itemDetail.dicId = this.dicId
          if (type === 'update') {
            updateDicItemByData(this.itemDetail)
            // this.$http.put('/api/dic/cscpHxDicItems', this.itemDetail)
              .then(res => {
                if (res.data.code === 1) {
                  this.$Message.error(res.data.msg)
                  this.itemTreeModal = false
                  this.disabled = false
                  this.$refs['itemDetail'].resetFields()
                  return
                }
                this.$Message.success('修改成功')
                this.itemTreeModal = false
                this.$refs['itemDetail'].resetFields()
                this.itemDetail = {
                  'itemId': 0,
                  'itemParentId': null,
                  'itemCode': '',
                  'itemSort': null,
                  'itemValue': '',
                  'dicId': null
                }
                this.queryList()
                this.queryTreeList()
              }).catch(error => {
                if (error.response) {
                  this.$Message.error(error.response.data.title ? error.response.data.title : '修改失败！')
                }
              })
          } else if (type === 'add') {
            addDicItemByData(this.itemDetail)
            // this.$http.post('/api/dic/cscpHxDicItems', this.itemDetail)
              .then(res => {
                if (res.data.code === 1) {
                  this.$Message.error(res.data.msg)
                  this.disabled = false
                  this.itemTreeModal = false
                  this.$refs['itemDetail'].resetFields()
                  this.itemDetail = {
                    'itemId': 0,
                    'itemParentId': null,
                    'itemCode': '',
                    'itemSort': null,
                    'itemValue': '',
                    'dicId': null
                  }
                  return
                }
                this.$Message.success('新增成功')
                this.itemTreeModal = false
                this.queryList()
                this.queryTreeList()
                this.$refs['itemDetail'].resetFields()
              }).catch(error => {
                this.$Message.error(error.response.data.title ? error.response.data.title : '新增失败！')
                this.disabled = false
              })
          }
        } else {
          this.itemTreeModal = true
        }
      })
    },
    /**
     * 构造树型结构数据
     * @param {*} data 数据源
     * @param {*} id id字段 默认 'itemId'
     * @param {*} parentId 父节点字段 默认 'itemParentId'
     * @param {*} children 孩子节点字段 默认 'children'
     */
    handleTree (data, id, parentId, children) {
      let config = {
        id: id || 'itemId',
        parentId: parentId || 'itemParentId',
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
    handleSearch () {
      let url = '?'
      let arr = []
      for (let key in this.itemQuery) {
        arr.push(key + '.contains=' + (this.itemQuery[key] === undefined ? '' : this.itemQuery[key]))
      }
      arr.push('dicId.equals= ' + this.dicId)
      url += arr.join('&')
      getDicItemByQuery(url)
      // this.$http.get('/api/dic/cscpHxDicItems/list' + url)
        .then(res => {
          res.data.data.forEach(item => {
            item._showChildren = true
          })
          this.itemData = this.handleTree(res.data.data, 'itemId')
          this.loading = false
        }).catch(() => {
          this.$message.error('列表查询失败')
        })
    },
    handleAdd (row, type) {
      this.title = '新增'
      this.itemDetail = {}
      this.itemDetail.itemSort = 1
      if (type === 'addInRow') {
        this.itemDetail.itemParentId = row.itemId
      }
      this.itemTreeModal = true
      this.okFlag = 'add'
    },
    handleEdit (row) {
      this.title = '修改'
      this.itemTreeModal = true
      this.okFlag = 'update'
      queryDicItemById(row.itemId)
      // this.$http.get('/api/dic/cscpHxDicItems?itemId.equals=' + row.itemId)
        .then(res => {
          this.itemDetail = res.data.data[0]
        }).catch(() => {
          this.$message.error('行数据查询失败')
        })
    },
    handleReset (name) {
      this.$refs[name].resetFields()
      this.queryList()
    },
    handleExpand () {
      this.refreshTable = false
      this.isExpandAll = !this.isExpandAll
      this.$nextTick(() => {
        this.refreshTable = true
      })
    },
    queryList () {
      let params = {
        'dicId.equals': this.dicId
      }
      getDicItemByParams(params)
      // this.$http.get('/api/dic/cscpHxDicItems/list', { params: params })
        .then(res => {
          res.data.data.forEach(item => {
            item._showChildren = true
          })
          this.itemData = this.handleTree(res.data.data, 'itemId')
          this.loading = false
          this.disabled = false
        }).catch(() => {
          this.$message.error('列表查询失败')
        })
    },
    queryTreeList () {
      getTreeListByDicId(this.dicId)
        .then(res => {
          this.treeArr = res.data.data
        }).catch(() => {
          this.$message.error('字典树查询失败')
        })
    }
    // toTree (data) {
    //   // 删除 所有 children,以防止多次调用
    //   data.forEach(function (item) {
    //     delete item.children
    //   })

    //   // 将数据存储为 以 id 为 KEY 的 map 索引数据列
    //   var map = {}
    //   data.forEach(function (item) {
    //     map[item.itemId] = item
    //   })
    //   var val = []
    //   data.forEach(function (item) {
    //     // 以当前遍历项，的pid,去map对象中找到索引的id
    //     var parent = map[item.itemParentId]
    //     // 好绕啊，如果找到索引，那么说明此项不在顶级当中,那么需要把此项添加到，他对应的父级中
    //     if (parent) {
    //       (parent.children || (parent.children = [])).push(item)
    //     } else {
    //       // 如果没有在map中找到对应的索引ID,那么直接把 当前的item添加到 val结果集中，作为顶级
    //       val.push(item)
    //     }
    //   })
    //   return val
    // }

  }
}
</script>
<style lang="less" scoped>
  .dataArea{
    width: 100%;
    left: 0;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: right;
    background: #fff;
    border: 1px solid #e9e9e9
  } ;
  .operateBtn{
    width: 100%;
    text-align: left;
    padding: 0 0 16px;
  } ;
//   ::v-deep .el-table thead {
//   color: #909399;
//   font-weight: 500;
// } ;
// ::v-deep .el-table th {
//   height: 40px;
//   white-space: nowrap;
//   overflow: hidden;
//   background-color: #f7f7f9;
// } ;
// ::v-deep .el-table th.el-table__cell > .cell {
//   color: #515a6e;
// }
</style>
