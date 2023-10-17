<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea" >
          <Form ref="queryForm" :model="queryForm" :label-width="80" >
            <Row :gutter="40">
              <Col span="6" style="margin-bottom: 6px">
                <FormItem label="分类名"  prop="senTypeName" style="width: 100%">
                  <Input  type="text" :maxlength="100" v-model.trim="queryForm.senTypeName" placeholder="请输入敏感词分类名称" style="width: 100%"></Input>
                </FormItem>
            </Col>
              <Col span="24" style="text-align: right;margin-top: 12px">
                <FormItem :label-width="0">
                  <Button   type="primary" @click="queryList(true)" style="margin-right: 8px">查询</Button>
                  <Button type="default"  @click="handleReset('queryForm')">重置</Button>
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
            <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="handleEdit(row,index)">编辑</Button>
            <Button  type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="remove(row, index)">删除</Button>
          </template>
        </Table>
        <Page :total="pageData.total" :page-size="pageData.size" :current="pageData.page" show-elevator show-sizer show-total @on-page-size-change="changePage($event, 'size')" @on-change="changePage($event, 'page')" style="margin-top: 20px;"/>
      </Card>
    </div>
    <Modal
      v-model="modal"
      :title="title"
      ref="modal"
      @on-cancel="close"
      :width="580"
    >
      <Form ref="detail" :model="detail" :label-width="80" :rules="ruleValidate" style="margin-top: 20px">
        <FormItem label="分类名"  prop="senTypeName" >
            <Input type="text" :maxlength="100" v-model="detail.senTypeName" placeholder="请输入敏感词分类名称" ></Input>
        </FormItem>
        <FormItem label="排序"  prop="senTypeOrder">
            <Input type="text" :maxlength="100" v-model="detail.senTypeOrder" placeholder="请输入敏感词分类排序" ></Input>
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
  querySensitiveWordTypeById,
  delSensitiveWordTypeById,
  delSensitiveWordTypeByIds,
  updateSensitiveWordTypeByData,
  addSensitiveWordTypeByData,
  listSensitiveWordTypeByParams
} from '@/api/sensitiveword/sensitiveword-type'
export default {
  name: 'SensitiveWordTypeManager',
  data () {
    return {
      modal: false,
      disabled: false,
      selectedArr: [],
      queryForm: {},
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
          type: 'index',
          align: 'center',
          title: '序号',
          width: 80
        },
        {
          title: '分类名',
          key: 'senTypeName'
        },
        {
          title: '分类排序',
          key: 'senTypeOrder'
        },
        {
          title: '更新时间',
          key: 'updateTime'
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
      detail: {
      },
      okFlag: '',
      modalValid: true,
      ruleValidate: {
        senTypeName: [
          { required: true, message: '分类名不能为空', trigger: 'blur' }
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
      this.modal = false
      this.$refs['detail'].resetFields()
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
    handleAdd () {
      this.detail = {}
      this.okFlag = 'add'
      this.modal = true
    },
    handleEdit (row, index) {
      querySensitiveWordTypeById(row.id)
      // this.$http.get(`/api/system/cscpSensitiveWordTypes/${row.id}`)
        .then(res => {
          this.modal = true
          this.detail = res.data
          this.okFlag = 'update'
        }).catch(() => {
          this.$Message.error('行数据查询失败')
        })
    },
    handleReset (name) {
      this.$refs[name].resetFields()
      this.selectedArr = []
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
          delSensitiveWordTypeById(row.id)
          // this.$http.delete('/api/system/cscpSensitiveWordTypes/' + row.id)
            .then(res => {
              if (res.data.code == 200) {
                this.$Message.success('删除成功')
              } else {
                this.$Message.error({
                  content: res.data.msg,
                  duration: 3
                })
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
      this.$delete(this.detail, '')
      this.$delete(this.detail, '_rowKey')
      let copyDetail = JSON.parse(JSON.stringify(this.detail))

      this.$refs['detail'].validate((valid) => {
        if (valid) {
          if (type === 'update') {
            updateSensitiveWordTypeByData(copyDetail)
            // this.$http.put('/api/system/cscpSensitiveWordTypes', copyDetail)
              .then(res => {
                if (res.data.code == 200) {
                  this.$Message.success('修改成功')
                  this.modal = false
                  this.queryList()
                  this.$refs['detail'].resetFields()
                } else {
                  this.$Message.error({
                    content: res.data.msg,
                    duration: 3
                  })
                }
              }).catch(() => {
              // this.$Message.error('修改失败')
              })
          } else if (type === 'add') {
            addSensitiveWordTypeByData(copyDetail)
            // this.$http.post('/api/system/cscpSensitiveWordTypes', copyDetail)
              .then(res => {
                if (res.data.code == 200) {
                  this.$Message.success('新增成功')
                  this.modal = false
                  this.$refs['queryForm'].resetFields()
                  this.queryList()
                  this.$refs['detail'].resetFields()
                } else {
                  this.$Message.error({
                    content: res.data.msg,
                    duration: 3
                  })
                }
              }).catch(() => {
              // this.$Message.error('新增失败')
              })
          }
        } else {
          this.modal = true
        }
      })
    },
    queryList (flag) {
      if (flag) {
        this.pageData.page = 1
      }
      let obj = {}
      obj['senTypeName.contains'] = this.queryForm['senTypeName'] ? this.queryForm['senTypeName'] : null
      let params = Object.assign(obj, {
        page: this.pageData.page - 1,
        size: this.pageData.size,
        sort: 'sen_type_order,asc'
      })
      listSensitiveWordTypeByParams(params)
      // this.$http.get('/api/system/cscpSensitiveWordTypesByCriteria', {
      //   params: params
      // })
        .then(res => {
          this.data = res.data.data
          this.pageData.total = res.data.recordsTotal
        }).catch(() => {
          this.$Message.error('列表查询失败')
        })
    },
    handleDeleteMultiple: function () {
      let arr = this.selectedArr.map(item => item.id)
      if (arr.length < 1) {
        return this.$Message.error({
          content: '删除失败,当前选择列表为空！',
          duration: 3
        })
      }
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除勾选的数据？</p>',
        onOk: () => {
          delSensitiveWordTypeByIds(arr)
          // this.$http.delete('/api/system/cscpSensitiveWordTypes/delAll?ids=' + arr)
            .then(res => {
              console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>', res.data.code)
              if (res.data.code != 200) {
                this.$Message.error({
                  content: res.data.msg,
                  duration: 3
                })
              } else {
                this.$Message.success('删除成功')
                this.selectedArr = []
                this.queryList()
              }
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
   @import "~@/views/admin/styles/formStyle.less";
</style>
