<template>
  <div style="height: 100%;position: absolute"  >
      <!-- 抽屉 -->
      <Drawer
        title="字典项列表"
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
                  <Button   type="primary" @click="queryList()" style="margin-right: 8px">查询</Button>
                  <Button type="default"  @click="handleReset('itemQuery')">重置</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
          <div class="operateBtn">
            <Button  icon="md-add"  type="primary"  @click="handleAdd()">新增</Button>
            <Button  icon="ios-trash-outline" type="error"   @click="handleDeleteMultiple()" style="margin-left: 20px">删除</Button>
          </div>
          <Table border :columns="columns" :data="itemData" @on-selection-change="rowSelected($event)" style="">
            <template slot-scope="{ row, index }" slot="action">
              <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="handleEdit(row)">编辑</Button>
              <Button type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="remove(row, index)">删除</Button>
            </template>
          </Table>
          <Page :total="pageData.total" :page-size="pageData.size" :current="pageData.page" show-elevator show-sizer show-total @on-page-size-change="changePage($event, 'size')" @on-change="changePage($event, 'page')" style="margin-top: 20px;"/>
        </div>
      </Drawer>
    <Modal
      v-model="itemModal"
      :title="title"
      ref="modal"
      @on-cancel="close"
      :width="620"
    >
      <Form  ref="itemDetail" :model="itemDetail" :label-width="100" :rules="ruleValidate" style="margin-top: 20px">
        <FormItem label="字典项编码"  prop="itemCode">
          <Input   type="text" :maxlength="100" v-model.trim="itemDetail.itemCode" placeholder="" ></Input>
        </FormItem>
        <FormItem label="字典值"  prop="itemValue">
          <Input   type="text" :maxlength="100" v-model.trim="itemDetail.itemValue" placeholder="" ></Input>
        </FormItem>
        <FormItem label="显示排序" prop="itemSort">
          <InputNumber v-model="itemDetail.itemSort" :min="0" :max="1000000" :precision="0" placeholder="" style="width: 100%"></InputNumber>
        </FormItem>
        <FormItem label="描述" prop="description">
          <Input   type="textarea" :maxlength="100" v-model.trim="itemDetail.description" placeholder="" ></Input>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="primary"  @click="ok(okFlag)"  style="width: 80px">确定</Button>
        <Button type="default"  @click="close()"  style="width: 80px">取消</Button>
      </div>
    </Modal>
  </div>

</template>

<script>
import {
  checkDicItemCodeExists,
  delDicItemById,
  addDicItemByData,
  updateDicItemByData,
  queryDicItemById,
  listDicItemByParams,
  delDicItemByIds
} from '@/api/dic/listdic'
export default {
  name: 'dicItemList',
  data () {
    const validateDicItemCode = (rule, value, callback) => {
      this.itemDetail.dicId = this.dicId
      // 检查itemCode是否存在
      checkDicItemCodeExists(this.itemDetail)
        .then(response => {
          console.log('checkDicItemCodeExists', response, response.data.code)
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
      itemModal: false,
      title: '新增',
      visible: false,
      okFlag: '',
      itemQuery: {
        itemCode: undefined,
        itemValue: undefined
      },
      itemData: [],
      selectedArr: [],
      itemDetail: {
        'itemId': null,
        'itemCode': '',
        'itemSort': 1,
        'itemValue': '',
        'dicId': null
      },
      pageData: {
        total: 0,
        size: 10,
        page: 1
      },
      dicId: null,
      dicCode: '',
      columns: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '字典项编码',
          align: 'center',
          key: 'itemCode'
        },
        {
          title: '字典值',
          align: 'center',
          key: 'itemValue'
        },
        {
          title: '排序',
          align: 'center',
          width: 70,
          key: 'itemSort'
        },
        {
          title: '操作',
          slot: 'action',
          width: 260,
          align: 'center'
        }
      ],
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
    edit (dicId, dicCode) {
      this.visible = true
      this.dicId = dicId
      this.dicCode = dicCode
      this.queryList()
    },
    changePage (e, type) {
      if (type === 'size') {
        this.pageData.size = e
      } else if (type === 'page') {
        this.pageData.page = e
      }
      this.queryList()
    },
    close () {
      this.itemModal = false
      this.$refs['itemDetail'].resetFields()
    },
    remove (row, index) {
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除这条数据？</p>',
        onOk: () => {
          delDicItemById(row.itemId)
            .then(res => {
              if (res.data.code !== 200) {
                this.$Message.error(res.data.msg)
                return
              } else {
                this.$Message.success('删除成功')
                this.$store.dispatch('dict/removeDict', this.dicCode)
              }
              this.queryList()
            }).catch(() => {
              this.$Message.error('删除失败')
            })
        },
        onCancel: () => {}
      })
    },
    ok (type) {
      this.$delete(this.itemDetail, '')
      this.$delete(this.itemDetail, '_rowKey')
      this.itemDetail.dicId = this.dicId
      this.itemDetail.dicCode = this.dicCode
      let copyDetail = JSON.parse(JSON.stringify(this.itemDetail))
      this.$refs['itemDetail'].validate((valid) => {
        if (valid) {
          if (type === 'update') {
            updateDicItemByData(copyDetail)
              .then(res => {
                if (res.data.code === 200) {
                  this.$Message.success(res.data.msg)
                  this.$store.dispatch('dict/removeDict', this.dicCode)
                } else {
                  this.$Message.error(res.data.msg)
                }
                this.itemModal = false
                this.$refs['itemDetail'].resetFields()
                this.itemDetail = {
                  'itemId': null,
                  'itemCode': '',
                  'itemSort': 1,
                  'itemValue': '',
                  'dicId': null
                }
                this.queryList()
              }).catch(() => {
                this.$Message.error('修改失败')
              })
          } else if (type === 'add') {
            addDicItemByData(copyDetail)
              .then(res => {
                if (res.data.code === 200) {
                  this.$Message.success(res.data.msg)
                  this.$store.dispatch('dict/removeDict', this.dicCode)
                } else {
                  this.$Message.error(res.data.msg)
                }

                this.itemModal = false
                this.$refs['itemDetail'].resetFields()
                this.itemDetail = {
                  'itemId': null,
                  'itemCode': '',
                  'itemSort': 1,
                  'itemValue': '',
                  'dicId': null
                }
                this.queryList()
              }).catch(() => {
                this.$Message.error('新增失败')
              })
          }
        } else {
          this.itemModal = true
        }
      })
    },
    handleAdd (row, type) {
      this.detail = {}
      this.itemModal = true
      this.okFlag = 'add'
    },
    handleEdit (row) {
      this.title = '修改'
      this.itemModal = true
      this.okFlag = 'update'
      queryDicItemById(row.itemId)
        .then(res => {
          this.itemDetail = res.data.data[0]
        }).catch(() => {
          this.$message.error('行数据查询失败')
        })
    },
    handleReset (name) {
      this.$refs[name].resetFields()
      this.selectedArr = []
      this.queryList()
    },
    rowSelected (e) {
      this.selectedArr = e
    },
    queryList () {
      let obj = {}
      for (let key in this.itemQuery) {
        obj[key + '.contains'] = this.itemQuery[key] === undefined ? null : this.itemQuery[key]
      }
      let params = Object.assign(obj, {
        page: this.pageData.page - 1,
        size: this.pageData.size,
        sort: 'item_sort',
        'dicId.equals': this.dicId
      })
      listDicItemByParams(params)
        .then(res => {
          this.itemData = res.data.data
          this.pageData.total = res.data.recordsTotal
        }).catch(() => {
          this.$message.error('列表查询失败')
        })
    },
    handleDeleteMultiple () {
      let arr = this.selectedArr.map(item => item.itemId)
      if (arr.length < 1) {
        return this.$Message.error('删除失败,当前选择列表为空！')
      }
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除勾选的数据？</p>',
        onOk: () => {
          delDicItemByIds(arr)
            .then(res => {
              if (res.data.code === 200) {
                this.$store.dispatch('dict/removeDict', this.dicCode)
                this.$message.success(res.data.msg)
              } else {
                this.$message.error(res.data.msg)
              }
              this.queryList()
            }).catch(error => {
              if (error.response) {
                this.$Message.error(error.response.data.title ? error.response.data.title : '删除失败！')
              }
            })
        },
        onCancel: () => {}
      })
    }
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
    border: 1px solid #e9e9e9
  } ;
  .operateBtn{
    width: 100%;
    text-align: left;
    padding: 0 0 16px;
  }
</style>
