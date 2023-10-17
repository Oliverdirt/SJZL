<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea">
          <!--顶部查询条件-->
          <Form ref="queryFrom" :model="cscpHxDicCustom"  :label-width="80">
            <Row :gutter="40">
              <Col span="8">
                <FormItem label="字典名称"  prop="dicName" style="width: 100%">
                  <Input   type="text" :maxlength="100" v-model.trim="cscpHxDicCustom.dicName" placeholder="请输入字典名称" style="width: 100%"></Input>
                </FormItem>
              </Col>
              <Col span="8">
                <FormItem label="字典编码"  prop="dicCode" style="width: 100%">
                  <Input   type="text" :maxlength="100" v-model.trim="cscpHxDicCustom.dicCode" placeholder="请输入字典编码" style="width: 100%"></Input>
                </FormItem>
              </Col>
              <!--查询按钮-->
              <Col span="24" style="text-align: right">
                <FormItem :label-width="0">
                  <Button   type="primary" @click="listTable(true)" style="margin-right: 8px">查询</Button>
                  <Button type="default"  @click="handleReset('queryFrom')">重置</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <!--新建按钮-->
        <div class="tableBtn">
          <Button  icon="md-add"  type="primary"  @click="handleAdd()">新增</Button>
        </div>
        <Table border :columns="columns" :data="tableData">
          <template slot-scope="{ row}" slot="action">
            <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="preview(row.dicName)">字典配置</Button>
            <Button type="text" size="small"  style="margin-right: 5px;color: #0056B5" @click="edit(row.dicId)">编辑</Button>
            <Button  type="text" size="small" style="margin-right: 5px;color: #0056B5" @click="remove(row.dicId)">删除</Button>
          </template>
        </Table>
        <Page :total="pageData.total" :page-size="pageData.size" :current="pageData.page" show-elevator show-sizer show-total @on-page-size-change="changePage($event, 'size')" @on-change="changePage($event, 'page')" style="margin-top: 20px;"/>
      </Card>
      <custom-dic-item-list ref="customDicItemList"></custom-dic-item-list>
    </div>
    <Modal
      v-model="customModal"
      :title="title"
      ref="modal"
      @on-cancel="close"
      :width="580"
    >
      <Form  ref="customDetail" :model="customDetail" :label-width="80" :rules="ruleValidate" style="margin-top: 20px">
          <FormItem label="字典编码"  prop="dicCode">
            <Input   type="text" :maxlength="100" v-model.trim="customDetail.dicCode" placeholder="" ></Input>
          </FormItem>
          <FormItem label="字典名称"  prop="dicName">
            <Input   type="text" :maxlength="100" v-model.trim="customDetail.dicName" placeholder="" ></Input>
          </FormItem>
          <FormItem  label="类名" prop="dicClass">
            <Input   type="text" :maxlength="100" v-model.trim="customDetail.dicClass" placeholder="" ></Input>
          </FormItem>
          <FormItem  label="方法" prop="dicMethod">
            <Input   type="text" :maxlength="100" v-model.trim="customDetail.dicMethod" placeholder="" ></Input>
          </FormItem>
          <FormItem label="显示排序" prop="dicSort">
            <InputNumber v-model="customDetail.dicSort" :min="0" :max="1000000" :precision="0" placeholder="" style="width: 100%"/>
          </FormItem>
          <FormItem label="描述" prop="description">
            <Input   type="textarea" :maxlength="100" v-model.trim="customDetail.description" placeholder="" ></Input>
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
import CustomDicItemList from './customDicItemList'
import {
  getDicListbyParams
} from '@/api/dic/listdic'
import {
  addCustomDicByData,
  updateCustomDicByData,
  queryCustomDicById,
  delCustomDicById,
  getCustomDicByParams
} from '@/api/dic/custom-dic'
export default {
  name: 'cscpHxDicCustomList',
  components: { CustomDicItemList },
  data () {
    const validSort = (rule, value, callback) => {
      setTimeout(() => {
        if (!this.customDetail.dicSort) {
          callback(new Error('请输入显示排序'))
        } else {
          callback()
        }
      }, 0)
    }
    return {
      customModal: false,
      disabled: false,
      okFlag: '',
      pageData: {
        total: 0,
        size: 10,
        page: 1
      },
      // 查询参数
      cscpHxDicCustom: {
        dicName: null,
        dicCode: null
      },
      customDetail: {},
      columns: [
        {
          key: 'dicName',
          title: '字典名称'
        },
        {
          key: 'dicCode',
          title: '字典编码'
        },
        /*        {
          key: "dicSort",
          title: "排序"
        }, */
        {
          key: 'dicClass',
          title: '类名'
        },
        {
          key: 'dicMethod',
          title: '方法'
        },
        {
          key: 'description',
          title: '字典描述'
        },
        {
          key: 'dicSort',
          title: '排序'
        },
        {
          title: '操作',
          slot: 'action',
          width: 300,
          align: 'center'
        }
      ],
      tableData: [],
      ruleValidate: {
        dicCode: [
          { required: true, message: '请输入字典编码' },
          { max: 50, message: '字典编码不能超过50个字符' }
        ],
        dicName: [
          { required: true, message: '请输入字典名称' },
          { max: 20, message: '字典名称不能超过20个字符' }
        ],
        dicClass: [
          { required: true, message: '请输入类名' },
          { max: 120, message: '字典名称不能超过120个字符' }
        ],
        dicMethod: [
          { required: true, message: '请输入方法名' },
          { max: 120, message: '方法名不能超过120个字符' }
        ],
        dicSort: [
          { required: true, validator: validSort, trigger: 'blur' }
        ]
      }
    }
  },
  mounted () {
    this.listTable()
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
    changePage (e, type) {
      if (type === 'size') {
        this.pageData.size = e
      } else if (type === 'page') {
        this.pageData.page = e
      }
      this.listTable()
    },
    close () {
      this.customModal = false
      this.$refs['customDetail'].resetFields()
      this.disabled = false
    },

    handleReset (name) {
      this.$refs[name].resetFields()
      this.listTable(true)
    },
    // 渲染表格数据
    listTable (inquire) {
      if (inquire) {
        this.pageData.page = 1
      }
      let _this = this
      let obj = {}
      obj['dicName.contains'] = this.cscpHxDicCustom.dicName ? this.cscpHxDicCustom.dicName : null
      obj['dicCode.contains'] = this.cscpHxDicCustom.dicCode ? this.cscpHxDicCustom.dicCode : null

      let params = Object.assign(obj, {
        page: this.pageData.page - 1,
        size: this.pageData.size
      })

      // const [url, param] = [
      //   '/api/cscpHxDicCustoms', {
      //     params: params
      //   }
      // ]
      getCustomDicByParams(params)
      // _this.$http.get(url, param)
        .then(function (response) {
          _this.tableData = []
          let data = response.data
          _this.pageData.total = data.recordsTotal
          let list = data.data
          for (let i = 0; i < list.length; i++) {
            let map = {
              dicId: list[i].dicId,
              dicName: list[i].dicName,
              dicCode: list[i].dicCode,
              dicSort: list[i].dicSort,
              dicMethod: list[i].dicMethod,
              dicClass: list[i].dicClass,
              description: list[i].description
            }
            _this.tableData.push(map)
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    // 新建
    handleAdd () {
      this.customDetail = {}
      this.okFlag = 'add'
      this.customModal = true
      this.customDetail.dicSort = 1
    },

    ok (type) {
      this.$delete(this.customDetail, '')
      this.$delete(this.customDetail, '_rowKey')
      let copyDetail = JSON.parse(JSON.stringify(this.customDetail))
      this.$refs['customDetail'].validate((valid) => {
        if (valid) {
          if (type === 'update') {
            updateCustomDicByData(copyDetail)
            // this.$http.put('/api/cscpHxDicCustoms', copyDetail)
              .then(res => {
                this.$Message.success('修改成功')
                this.customModal = false
                this.listTable()
                this.$refs['customDetail'].resetFields()
              }).catch(() => {
                this.$Message.error('修改失败')
              })
          } else if (type === 'add') {
            addCustomDicByData(copyDetail)
            // this.$http.post('/api/cscpHxDicCustoms', copyDetail)
              .then(res => {
                this.$Message.success('新增成功')
                this.customModal = false
                this.listTable()
                this.$refs['customDetail'].resetFields()
              }).catch(error => {
                if (error.response) {
                  this.$Message.error(error.response.data.title ? error.response.data.title : '新增失败！')
                }
              })
          }
        } else {
          this.itemModal = true
        }
      })
    },
    // 编辑
    edit (id) {
      queryCustomDicById(id)
      // this.$http.get('/api/cscpHxDicCustoms/' + id)
        .then(res => {
          this.customModal = true
          this.customDetail = res.data
          this.okFlag = 'update'
        }).catch(() => {
          this.$message.error('行数据查询失败')
        })
    },
    // 删除
    remove (dicId) {
      let _this = this
      this.$Modal.confirm({
        title: '确定删除？',
        onOk () {
          delCustomDicById(dicId)
          // _this.$http.delete('/api/cscpHxDicCustoms/' + dicId)
            .then(response => {
              if (response.status === 200) {
                _this.$Notice.success({
                  title: '删除成功'
                })
                _this.listTable()
              } else {
                _this.$Notice.error({
                  title: '删除失败'
                })
              }
            })
        }
      })
    },
    handlePageChange (current) {
      this.pageData.page = current - 1
      this.listTable()
    },
    preview (dicName) {
      const params = {
        'dicName.equals': dicName,
        'dicAttr.equals': 'custom'
      }
      getDicListbyParams(params)
      // this.$http.get('/api/dic/cscpHxDics', {
      //   params: params
      // })
        .then(res => {
        // console.log("res:sd==============",res.data.data[0].dicId)
          if (res.data.data.length > 0) {
            this.$refs.customDicItemList.preview(res.data.data[0].dicId)
          } else {
            this.$message.error('列表查询失败')
          }
        }).catch(() => {
          this.$message.error('列表查询失败')
        })
    }
  }
}
</script>
<style lang="less" scoped>
  @import "../../admin/styles/formStyle.less";
</style>
