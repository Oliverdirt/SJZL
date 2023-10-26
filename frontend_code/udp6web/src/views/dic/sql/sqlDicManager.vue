<template>
  <!-- <section> -->
  <div class="app-container" style="height: 100%">
    <!--顶部查询条件-->
    <div class="formCard-content">
      <Card
        :bordered="false"
        :dis-hover="true"
        :shadow="false"
        class="formCard"
      >
        <div class="searchArea">
          <Form
            :model="cscpHxSqldicBaseinfo"
            ref="cscpHxSqldicBaseinfo"
            :label-width="80"
          >
            <Row :gutter="48">
              <!-- <Col span="8">
                <FormItem label="查询SQL" prop="dicSelectSql">
                  <Input
                    v-model="cscpHxSqldicBaseinfo.dicSelectSql"
                    placeholder="请输入查询SQL"
                    clearable
                  />
                </FormItem>
              </Col> -->
              <Col span="8">
                <FormItem label="字典名" prop="dicName">
                  <Input
                    v-model="cscpHxSqldicBaseinfo.dicName"
                    placeholder="请输入字典名"
                    clearable
                  ></Input>
                </FormItem>
              </Col>
              <Col span="8">
                <FormItem label="字典编码" prop="dicCode">
                  <Input
                    v-model="cscpHxSqldicBaseinfo.dicCode"
                    placeholder="请输入字典编码"
                    clearable
                  ></Input>
                </FormItem>
              </Col>
              <Col span="24" style="text-align: right; margin-top: 24px">
                <FormItem>
                  <Button icon="ios-search" type="primary" @click="listTable(true)"
                    >查询</Button
                  >
                  <Button
                    type="default"
                    style="margin-left: 10px"
                    @click="resetQuery()"
                    >重置</Button
                  >
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>

      <Card
        :bordered="false"
        :dis-hover="true"
        :shadow="false"
        class="formCard"
      >
        <div class="tableBtn">
          <Button
            @click="handleAdd()"
            type="primary"
            style="margin-right: 10px; margin-bottom: 5px"
            icon="md-add"
            >新增</Button
          >
          <Button
            @click="handleExport"
            type="primary"
            icon="ios-cloud-download"
            shape="circle"
            >导出</Button
          >
        </div>

        <Table border :columns="columns" :data="tableData"></Table>
        <Page :total="pageData.total" :page-size="pageData.size" :page-size-opts="[5, 20, 30, 40]" :current="pageData.page" show-elevator show-sizer show-total @on-page-size-change="changePage($event, 'size')" @on-change="changePage($event, 'page')" style="margin-top: 20px;"/>
      </Card>
    </div>
    <Modal
      v-model="sqlModal"
      :title="title1"
      ref="modal"
      @on-cancel="close"
      :width="580"
    >
      <Form
        ref="customDetail"
        :model="model"
        :label-width="110"
        :rules="ruleCustom"
        style="margin-top: 20px"
      >
        <FormItem :class="{ 'model-hidden': true }" label="文本框">
          <Input v-model="model.id" type="text"></Input>
        </FormItem>
        <FormItem label="字典名" prop="dicName">
          <Input
            v-model="model.dicName"
            style="width: 320px"
            type="text"
          ></Input>
        </FormItem>
        <FormItem label="查询SQL" prop="dicSelectSql">
          <Input
            v-model="model.dicSelectSql"
            style="width: 320px"
            type="textarea"
            placeholder="请输入查询SQL，仅支持基础查询操作"
          ></Input>
        </FormItem>

        <FormItem label="字典编码" prop="dicCode">
          <Input
            v-model="model.dicCode"
            :disabled="flag"
            style="width: 320px"
            type="text"
          ></Input>
        </FormItem>
        <FormItem label="字典项值列" prop="dicItemValueColumn">
          <Input
            v-model="model.dicItemValueColumn"
            style="width: 320px"
            type="text"
          ></Input>
        </FormItem>
        <FormItem label="字典项编码列" prop="dicItemCodeColumn">
          <Input
            v-model="model.dicItemCodeColumn"
            style="width: 320px"
            type="text"
          ></Input>
        </FormItem>
        <FormItem label="字典项备注列" prop="dicItemValueNotes">
          <Input
            v-model="model.dicItemValueNotes"
            style="width: 320px"
            type="text"
          ></Input>
        </FormItem>

        <FormItem label="字典备注" prop="dicNotes">
          <Input
            v-model="model.dicNotes"
            style="width: 320px"
            type="text"
          ></Input>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button
          type="primary"
          @click="ok(okFlag)"
          :disabled="disabled"
          style="width: 80px"
          >确定</Button
        >
        <Button type="default" @click="close()" style="width: 80px"
          >取消</Button>
      </div>
    </Modal>
  </div>
  <!-- </section> -->
</template>
<script>
import {
  checkSqlDicCodeExistsByParams,
  getSqlDicByParams,
  updateSqlDicByData,
  addSqlDicByData,
  querySqlDicById,
  delSqlDicById,
  exportSqlDic
} from '@/api/dic/sqldic'
export default {
  name: 'cscpHxSqldicBaseinfoList',

  data () {
    const validatePass = (rule, value, callback) => {
      // 检查字典编码是否存在
      // const [url, httpConfig] = [
      //   '/api/cscpHxSqldicBaseinfos/validateDicExisted',
      //   {
      //     params: { dicCode: this.model.dicCode }
      //   }
      // ]
      const sqlDicParams = { dicCode: this.model.dicCode }
      if (this.okFlag === 'add') {
        checkSqlDicCodeExistsByParams(sqlDicParams)
        // this.$http
        //   .get(url, httpConfig)
          .then((response) => {
            if (response.data.code === -1) {
              console.info(response.data)
              callback(new Error(response.data.msg))
            } else {
              callback()
            }
          })
          .catch(() => {
            callback(new Error('异步校验出错！'))
          })
      } else {
        callback()
      }
    }

    return {
      sqlModal: false,
      disabled: false,
      saveLoading: false,
      okFlag: '',
      flag: false,
      // 分页参数
      pageData: {
        total: 0,
        size: 5,
        page: 1
      },
      // 查询参数
      cscpHxSqldicBaseinfo: {
        dicSelectSql: null,
        dicName: null,
        dicCode: null
      },
      columns: [
        // {
        //   key: 'dicSelectSql',
        //   title: '查询SQL'
        // },
        {
          key: 'dicName',
          title: '字典名'
        },
        {
          key: 'dicCode',
          title: '字典编码'
        },
        {
          key: 'dicNotes',
          title: '字典备注'
        },
        {
          key: 'dicItemCodeColumn',
          title: '字典项编码列名'
        },
        {
          key: 'dicItemValueColumn',
          title: '字典项值列名'
        },
        // {
        //   key: 'dicItemValueNotes',
        //   title: '字典项备注列名'
        // },

        {
          title: '操作',
          align: 'center',
          width: 300,
          key: 'id',
          render: (h, params) => {
            return h('div', [
              h(
                'Button',
                {
                  props: {
                    type: 'text',
                    size: 'small'
                  },
                  style: {
                    marginLeft: '5px',
                    color: '#0056B5'
                  },
                  on: {
                    click: () => {
                      this.edit(params.row.id)
                    }
                  }
                },
                '编辑'
              ),
              h(
                'Button',
                {
                  props: {
                    type: 'text',
                    size: 'small'
                  },
                  style: {
                    marginLeft: '5px',
                    color: '#0056B5'
                  },
                  on: {
                    click: () => {
                      this.delete(params.row.id)
                    }
                  }
                },
                '删除'
              ),
              h(
                'Button',
                {
                  props: {
                    type: 'text',
                    size: 'small'
                  },
                  style: {
                    // marginLeft: '5px',
                    color: '#0056B5'
                  },
                  on: {
                    click: () => {
                      this.preview(params.row.id)
                    }
                  }
                },
                '执行预览'
              )
            ])
          }
        }
      ],
      tableData: [],
      model: {
        id: null,
        dicSelectSql: '', // 非空
        dicName: null,
        dicCode: null, // 非空
        dicNotes: null,
        dicItemCodeColumn: null,
        dicItemValueColumn: null, // 非空
        dicItemValueNotes: null,
        createTime: null,
        createUser: null,
        updateTime: null,
        updateUser: null
      },
      ruleCustom: {

        dicName: [
          { required: true, message: '字典名不能为空!', trigger: 'blur' },
          {
            max: 32,
            message: '字典名不可超过32个字符',
            trigger: 'blur'
          }
        ],
        dicSelectSql: [
          { required: true, message: '查询sql不能为空!', trigger: 'blur' },
          {
            max: 250,
            message: '查询sql不能超过250个字符',
            trigger: 'blur'
          }
        ],
        dicCode: [
          { required: true, message: '字典编码不能为空!', trigger: 'blur' },
          {
            max: 32,
            message: '字典编码不能超过32个字符',
            trigger: 'blur'
          },
          { validator: validatePass, trigger: 'blur' }
        ],
        dicItemValueColumn: [
          { required: true, message: '字典项值不能为空!', trigger: 'blur' },
          {
            max: 32,
            message: '字典项值不能超过32个字符',
            trigger: 'blur'
          }
        ],
        dicItemCodeColumn: [
          // { required: true, message: '字典编码不能为空!', trigger: 'blur' },
          {
            max: 32,
            message: '字典项编码列不能超过32个字符',
            trigger: 'blur'
          }

        ],
        dicNotes: [
          // { required: true, message: '字典编码不能为空!', trigger: 'blur' },
          {
            max: 32,
            message: '字典备注不能超过32个字符',
            trigger: 'blur'
          }

        ],
        dicItemValueNotes: [
          // { required: true, message: '字典编码不能为空!', trigger: 'blur' },
          {
            max: 32,
            message: '字典项备注不能超过32个字符',
            trigger: 'blur'
          }

        ]
      }
    }
  },
  mounted () {
    this.listTable()
  },
  computed: {
    title1: function () {
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
      this.sqlModal = false
      this.model = {}
      this.disabled = false
      this.listTable()
      this.$refs['customDetail'].resetFields()
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.$refs['cscpHxSqldicBaseinfo'].resetFields()
      this.listTable(true)
    },

    formatTime (UTCDateString) {
      if (!UTCDateString) {
        return '-'
      }
      function formatFunc (str) {
        // 格式化显示
        return str > 9 ? str : '0' + str
      }
      var date2 = new Date(UTCDateString) // 这步是关键
      var year = date2.getFullYear()
      var mon = formatFunc(date2.getMonth() + 1)
      var day = formatFunc(date2.getDate())
      var hour = date2.getHours()
      var min = formatFunc(date2.getMinutes())
      var second = date2.getSeconds()
      var dateStr =
        year + '-' + mon + '-' + day + ' ' + hour + ':' + min + ':' + second
      return dateStr
    },
    // 渲染表格数据
    listTable (inquire) {
      if (inquire) {
        this.pageData.page = 1
      }
      let _this = this
      let obj = {}
      obj['dicName.contains'] = this.cscpHxSqldicBaseinfo.dicName ? this.cscpHxSqldicBaseinfo.dicName : null
      obj['dicCode.contains'] = this.cscpHxSqldicBaseinfo.dicCode ? this.cscpHxSqldicBaseinfo.dicCode : null

      let params = Object.assign(obj, {
        page: this.pageData.page - 1,
        size: this.pageData.size
      })

      // const [url, param] = [
      //   '/api/cscpHxSqldicBaseinfos',
      //   {
      //     params: params
      //   }
      // ]
      getSqlDicByParams(params)
      // _this.$http
      //   .get(url, param)
        .then(function (response) {
          _this.tableData = []
          let data = response.data
          _this.pageData.total = data.recordsTotal
          let list = data.data
          for (let i = 0; i < list.length; i++) {
            let map = {
              id: list[i].id,
              dicSelectSql: list[i].dicSelectSql,
              dicName: list[i].dicName,
              dicCode: list[i].dicCode,
              dicNotes: list[i].dicNotes,
              dicItemCodeColumn: list[i].dicItemCodeColumn,
              dicItemValueColumn: list[i].dicItemValueColumn,
              dicItemValueNotes: list[i].dicItemValueNotes,
              createTime: list[i].createTime,
              createUser: list[i].createUser,
              updateTime: list[i].updateTime,
              updateUser: list[i].updateUser
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
      this.model = {}
      this.okFlag = 'add'
      this.flag = false
      this.sqlModal = true
      this.$refs['customDetail'].resetFields()
    },
    ok (type) {
      this.$refs['customDetail'].validate((valid) => {
        if (valid) {
          let copyDetail = JSON.parse(JSON.stringify(this.model))
          if (type === 'update') {
            updateSqlDicByData(copyDetail)
            // this.$http
            //   .put('/api/cscpHxSqldicBaseinfos', copyDetail)
              .then((res) => {
                this.$Message.success('修改成功')
                this.sqlModal = false
                this.listTable()
                this.$refs['customDetail'].resetFields()
              })
              .catch(() => {
                this.$Message.error('修改失败')
              })
          } else if (type === 'add') {
            addSqlDicByData(copyDetail)
            // this.$http
            //   .post('/api/cscpHxSqldicBaseinfos', copyDetail)
              .then((res) => {
                this.$Message.success('新增成功')
                this.sqlModal = false
                this.listTable()
                this.$refs['customDetail'].resetFields()
              })
              .catch(() => {
                this.$Message.error('新增失败')
              })
          }
        } else {
          this.itemModal = true
        }
      })
    },
    // 编辑
    edit (id) {
      querySqlDicById(id)
      // this.$http
      //   .get('/api/cscpHxSqldicBaseinfos/' + id)
        .then((res) => {
          this.sqlModal = true
          this.model = res.data
          this.okFlag = 'update'
          this.flag = true
          this.$refs['customDetail'].resetFields()
        })
        .catch(() => {
          this.$message.error('行数据查询失败')
        })
    },
    // 预览
    preview (id) {
      this.$router.push({
        name: 'cscp_hx_sqldic_preview',
        query: {
          id: id
        }
      })
    },
    // 删除
    delete (id) {
      let _this = this
      this.$Modal.confirm({
        title: '确定删除？',
        onOk () {
          delSqlDicById(id)
          // _this.$http
          //   .delete('/api/cscpHxSqldicBaseinfos/' + id)
            .then((response) => {
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
    // 导出按钮
    handleExport () {
      this.$Modal.confirm({
        title: '是否确认导出所有数据项？',
        onOk () {
          this.$http.defaults.timeout = 15000 * 60
          exportSqlDic('blob')
          // this.$http
          //   .post(
          //     `/api/cscpHxSqldicBaseinfos/export`,
          //     {},
          //     {
          //       responseType: 'blob'
          //     }
          //   )
            .then((res) => {
              const link = document.createElement('a')
              const blob = new Blob([res.data], {
                type: 'application/vnd.ms-excel'
              })
              link.style.display = 'none'
              link.href = URL.createObjectURL(blob)
              document.body.appendChild(link)
              link.click()
              URL.revokeObjectURL(link.href)
              document.body.removeChild(link)
              this.$Notice.success({
                title: '导出数据成功'
              })
            })
            .catch(() => {
              this.$Notice.error({
                title: '导出数据失败'
              })
            })
        }
      })
    }
  }
}
</script>
<style lang="less" scoped>
@import "../../admin/styles/formStyle.less";
/* .query-model {
        width: 170px;
    } */

.page-area {
  display: flex;
  justify-content: flex-end;
  padding-top: 35px;
  padding-right: 50px;
}

.model-hidden {
  display: none;
}
</style>
