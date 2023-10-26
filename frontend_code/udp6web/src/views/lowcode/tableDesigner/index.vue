<template>
  <div style="height: 100%">
    <div class="formCard-content">

      <div class="searchArea">
        <div class="top_left">
          <div class="btnBox">
            <span>开始创建新的应用模型吧！</span>
            <div @click=" okFlag = 'add';openAdd() "></div>
          </div>
          <div class="content">您可直接通过点击新建应用来建立完整的应用模型</div>
        </div>
        <img src="../../../assets/lowcode/analytics.png" alt="">
        <!--          <Form ref="queryForm" :model="queryForm" :label-width="80">-->
        <!--            <Row :gutter="40">-->
        <!--              <Col span="6" style="margin-bottom: 12px">-->
        <!--                <FormItem-->
        <!--                  label="模块名称"-->
        <!--                  prop="moduleName"-->
        <!--                  style="width: 100%"-->
        <!--                >-->
        <!--                  <Input-->
        <!--                    type="text"-->
        <!--                    :maxlength="100"-->
        <!--                    v-model.trim="queryForm.moduleName"-->
        <!--                    placeholder="请输入模块名称"-->
        <!--                    style="width: 100%"-->
        <!--                  />-->
        <!--                </FormItem>-->
        <!--              </Col>-->
        <!--              <Col span="24" style="text-align: right">-->
        <!--                <FormItem :label-width="0">-->
        <!--                  <Button type="primary" @click="queryList(true)">查询</Button>-->
        <!--                  <Button @click=" okFlag = 'add';openAdd() " type="primary" icon="md-add">新建模块</Button>-->
        <!--                </FormItem>-->
        <!--              </Col>-->
        <!--            </Row>-->
        <!--          </Form>-->
      </div>

      <Card
        :bordered="false"
        :dis-hover="true"
        :shadow="false"
        class="formCard"
      >
        <div class="moduleBox">
          <div
            class="moduleItem"
            v-for="(row,index) in data"
            :key="index+'data'"
          >
            <svg @click="remove(row, index)" t="1681887585469" class="icon" viewBox="0 0 1024 1024" version="1.1"
                 xmlns="http://www.w3.org/2000/svg" p-id="2923" width="16" height="16">
              <path
                d="M509.866667 32C245.333333 32 32 247.466667 32 512s213.333333 480 477.866667 480S987.733333 776.533333 987.733333 512 774.4 32 509.866667 32z m0 896C281.6 928 96 742.4 96 512S281.6 96 509.866667 96 923.733333 281.6 923.733333 512s-185.6 416-413.866666 416z"
                fill="#212121" p-id="2924"></path>
              <path
                d="M693.333333 330.666667c-12.8-12.8-32-12.8-44.8 0L512 467.2l-136.533333-136.533333c-12.8-12.8-32-12.8-44.8 0-12.8 12.8-12.8 32 0 44.8l136.533333 136.533333-136.533333 136.533333c-12.8 12.8-12.8 32 0 44.8 6.4 6.4 14.933333 8.533333 23.466666 8.533334s17.066667-2.133333 23.466667-8.533334l136.533333-136.533333 136.533334 136.533333c6.4 6.4 14.933333 8.533333 23.466666 8.533334s17.066667-2.133333 23.466667-8.533334c12.8-12.8 12.8-32 0-44.8L556.8 512l136.533333-136.533333c12.8-12.8 12.8-32 0-44.8z"
                fill="#212121" p-id="2925"></path>
            </svg>
            <ul>
              <li>
                <img v-if="row.iconName" :src="imgArray.filter(item=>row.iconName === item.name)[0].path" alt="">
                <svg v-else t="1681820828086" class="icon" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="3599" width="22" height="22">
                  <path
                    d="M597.3 426.7v170.7H426.6V426.7h170.7z m85.3 0h213.3v170.7H682.6V426.7zM597.3 896H426.6V682.7h170.7V896z m85.3 0V682.7h213.3v170.7a42.7 42.7 0 0 1-42.7 42.7H682.5z m-85.3-768v213.3H426.6V128h170.7z m85.3 0h170.7a42.7 42.7 0 0 1 42.7 42.7v170.7H682.7V128zM341.3 426.7v170.7H128V426.7h213.3z m0 469.3H170.7a42.7 42.7 0 0 1-42.7-42.7V682.6h213.3v213.3z m0-768v213.3H128V170.7a42.7 42.7 0 0 1 42.7-42.7h170.7z"
                    p-id="3600" fill="#246AD9"></path>
                </svg>
                <span>{{ row.moduleName }}</span>
                <svg @click="okFlag = 'update';openAdd(row, index);" t="1681885688526" class="icon"
                     viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="2930" width="16" height="16">
                  <path
                    d="M196 751h632v60H196v-60z m487.093-529.213L793.4 332.095c11.716 11.716 11.716 30.711 0 42.427L500.66 667.264a30 30 0 0 1-21.41 8.786l-111.006-0.731c-16.566-0.11-29.907-13.625-29.802-30.19l0.697-109.578a30 30 0 0 1 8.786-21.022l292.742-292.742c11.716-11.716 30.71-11.716 42.427 0z m-21.214 63.64l-262.82 262.82-0.429 67.27 68.471 0.452 262.66-262.66-67.882-67.883z"
                    fill="#333333" p-id="2931"></path>
                </svg>
              </li>
              <li>
                <span>创建时间：</span>
                <span>{{ row.createTime }}</span>
              </li>
              <li>
                <Button @click="clickModuleItem(row,1)" type="primary" ghost>模型设计</Button>
                <Button @click="clickModuleItem(row)" type="primary">页面设计</Button>
              </li>
            </ul>
          </div>
          <div style="visibility: hidden" :key="i+'hbw'" class="moduleItem" v-for="i in (4- (data.length%4))">

          </div>
        </div>
      </Card>
    </div>
    <!--    新建应用弹窗  -->
    <Modal class="newModuleDialog" v-model="modal1" :title="modalTitle">
      <Form
        ref="moduleRules"
        :model="detail"
        :rules="fieldRuleValidate"
        :label-width="100"
        style="margin-top: 20px"
      >
        <FormItem label="应用名称：" prop="moduleName">
          <Input
            type="text"
            :maxlength="100"
            v-model="detail.moduleName"
            placeholder="请输入应用名称"
            @keydown.native="moduleNameJudge($event)"
          ></Input>
        </FormItem>
        <FormItem label="选择logo：">
          <ChiosePng :imgArray="imgArray" :icon.sync="detail.iconName"></ChiosePng>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="primary" @click="save" style="width: 80px" :loading="saveLoading">确定</Button>
        <Button type="default" @click="cancel" style="width: 80px">取消</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import ChiosePng from '@/views/lowcode/tableDesigner/ChiosePng'
import {imgArray} from '@/views/lowcode/tableDesigner/assets'

export default {
  name: 'tableDesigner',
  components: {
    ChiosePng
  },
  data() {
    let validateFormName = (rule, value, callback) => {
      this.isEditModuleName = true
      if (value.length > 15) {
        callback(new Error('应用名长度不能超过15个字符'))
      } else if (value.length <= 15) {
        if (this.okFlag === 'update' && this.oldModuleName === value) {
          this.isEditModuleName = false
          callback()
        } else {
          const [url, httpConfig] = [
            '/api/getCscpCustomizeModuleByName',
            {
              params: {moduleName: value}
            }
          ]
          this.$http
            .get(url, httpConfig)
            .then((response) => {
              if (response.data.length > 0) {
                callback(new Error('应用名已重复'))
              } else {
                callback()
              }
            })
            .catch(() => {
              callback(new Error('异步校验出错！'))
            })
        }
      }
    }
    return {
      createModelDialogVisible: false,
      oldModuleName: '',
      isEditModuleName: true,
      saveLoading: false,
      fieldRuleValidate: {
        moduleName: [
          {required: true, message: '应用名称不能为空！', trigger: 'blur'},
          {required: true, validator: validateFormName, trigger: 'blur'},
        ],
      },
      formTypeOption: [
        {value: '0', label: '单表'},
        {value: '1', label: '多表'},
        {value: '3', label: '流程表'},
      ],
      isShowTip: false,
      modal1: false,
      selectedArr: [],
      queryForm: {},
      formId: null,
      formType: '0',
      pageData: {
        total: 0,
        size: 1000, // 应用不多，暂时保留分页，只是隐藏掉
        page: 1
      },
      data: [],
      detail: {},
      okFlag: 'add',
      modalValid: true,
      ruleValidate: {},
      modalTitle: '',
      typeDisabled: false,
      tagetRow: '',
      moduleName: '',
      imgArray: []
    }
  },
  mounted() {
    this.queryList()
    this.imgArray = imgArray
  },
  methods: {
    clickModuleItem(row, flag = 0) {
      const queryData = {moduleId: row.moduleId, moduleName: row.moduleName}
      if (flag) {
        // 模型
        this.$router.push({path: '/dataModelMng', query: queryData})
      } else {
        // 页面
        this.$router.push({path: '/pageManage', query: queryData})
      }
    },
    moduleNameJudge(event) {
      if (event.keyCode == 32) {
        event.returnValue = false
      }
    },
    handleTableChild(row) {
      this.$byStoreSet('mainForm', row)
      this.$router.push({
        name: 'sub-lowcode-programming'
      })
    },
    // 表单名称是否重复 验证
    judgeFormNameRepeat() {
      let params = {formName: this.detail.formName}
      this.$http
        .get('/api/lowcode/customize/getCscpCustomizeVformByName', {params})
        .then((res) => {
          res.data.length ? (this.isShowTip = true) : (this.isShowTip = false)
        })
    },
    openAdd(row, index) {
      this.isShowTip = false
      this.modal1 = true
      this.detail = {formType: 0}
      if (this.okFlag === 'update') {
        this.oldModuleName = row.moduleName
        this.modalTitle = '编辑应用'
        this.$http
          .get('api/cscpCustomizeModules/' + row.moduleId)
          .then((res) => {
            this.detail = res.data
            this.typeDisabled = true
          })
          .catch(() => {
            this.$Message.error('行数据查询失败')
          })
      } else {
        this.modalTitle = '新建应用'
        this.typeDisabled = false
      }
    },
    save() {
      this.saveLoading = true
      this.$refs.moduleRules.validate((valid) => {
        if (valid) {
          if (this.okFlag === 'update') {
            if (!this.isEditModuleName) {
              this.saveLoading = false
              this.modal1 = false
              return
            }
            this.$http
              .put('api/cscpCustomizeModules', this.detail)
              .then((res) => {
                this.$Message.success('修改成功')
                // this.modal1 = false
                this.saveLoading = false
                this.queryList()
                this.$refs['moduleRules'].resetFields()
              })
              .catch(() => {
                this.saveLoading = false
                this.$Message.error('修改失败')
              })
            this.modal1 = false
          } else if (this.okFlag === 'add') {
            if (this.detail.moduleName) {
              let copyDetail = JSON.parse(JSON.stringify(this.detail))
              this.$http
                .post('/api/cscpCustomizeModules', copyDetail)
                .then((res) => {
                  this.$Message.success('新建应用成功')
                  this.saveLoading = false
                  this.queryList()
                })
                .catch(() => {
                  this.saveLoading = false
                  this.$Message.error('新建应用失败')
                })
              this.modal1 = false
            }
          }
        } else {
          this.saveLoading = false
          this.modal1 = true
          this.$Message.error({
            background: true,
            content: '检验数据失败！'
          })
        }
      })
    },
    handleAdd() {
      this.detail = {}
      this.okFlag = 'add'
    },
    handleEdit(row, index) {
      window.localStorage.setItem('widget__list__backup', '')
      window.localStorage.setItem('form__config__backup', '')
      // 页面状态记录
      let pageJson = JSON.parse(row.formJson ? row.formJson : null)
      if (pageJson) {
        window.localStorage.setItem(
          'widget__list__backup',
          JSON.stringify(pageJson.widgetList)
        )
        window.localStorage.setItem(
          'form__config__backup',
          JSON.stringify(pageJson.formConfig)
        )
      }
      // 将行数据传递给子组件
      this.$refs['moduleRules'].resetFields()
      this.formId = row.formId
      this.$refs.configRef.currentStep = 0
      this.$refs.configRef.configModal = true
      this.$http
        .get('/api/lowcode/customize/cscpCustomizeVforms/' + row.formId)
        .then((res) => {
          this.detail = res.data
          this.formType = res.data.formType
          this.$refs.configRef.tableName = res.data.formTable
          this.$refs.configRef.tableDesc = res.data.tableDesc
          this.$refs.configRef.formId = res.data.formId
          this.$refs.configRef.formJson = res.data.formJson
          this.okFlag = 'update'
          if (res.data.formTable) {
            this.$refs.configRef.isDisable = true
            this.$refs.configRef.$refs.formDbRef.getColumns(res.data.formTable)
            this.$refs.configRef.$refs.formDbRef.tableName = res.data.formTable
            this.$refs.configRef.$refs.formDbRef.tableDesc = res.data.tableDesc
          } else {
            this.$refs.configRef.isDisable = false
          }
        })
        .catch(() => {
          this.$Message.error('行数据查询失败')
        })
    },
    remove(row, index) {
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除这条数据？</p>',
        onOk: () => {
          this.$http
            .delete('api/cscpCustomizeModules/' + row.moduleId)
            .then((res) => {
              if (res.data.msg) {
                this.$Message.error(res.data.msg)
                return
              } else {
                this.$Message.success('删除成功')
              }
              this.queryList()
            })
            .catch(() => {
              this.$Message.error('删除失败')
            })
        }
      })
    },
    ok(type) {
      this.$delete(this.detail, '')
      this.$delete(this.detail, '_rowKey')
      let copyDetail = JSON.parse(JSON.stringify(this.detail))
      if (this.detail.createdTime) {
        copyDetail.createdTime = this.$moment(
          new Date(this.detail.createdTime)
        ).format('yyyy-MM-DD HH:mm:ss')
      }

      this.$refs['detail'].validate((valid) => {
        if (valid) {
          if (type === 'update') {
            this.$http
              .put('/api/customizeVforms', copyDetail)
              .then((res) => {
                this.$Message.success('修改成功')
                this.queryList()
                this.$refs['moduleRules'].resetFields()
              })
              .catch(() => {
                this.$Message.error('修改失败')
              })
          } else if (type === 'add') {
            this.$http
              .post('/api/customizeVforms', copyDetail)
              .then((res) => {
                this.$Message.success('新增成功')
                this.$refs['queryForm'].resetFields()
                this.queryList()
                this.$refs['moduleRules'].resetFields()
              })
              .catch(() => {
                this.$Message.error('新增失败')
              })
          }
        } else {
          // this.modal = true
        }
      })
    },
    queryList(flag) {
      if (flag) {
        this.pageData.page = 1
      }
      const page = this.pageData.page - 1
      const size = this.pageData.size
      const moduleName = this.queryForm.moduleName || ''
      this.$http
        .get(
          `api/cscpCustomizeModules?moduleName.contains=${moduleName}&page=${page}&size=${size}&sort=update_time,desc`
        )
        .then((res) => {
          this.data = res.data.data
          this.pageData.total = res.data.recordsTotal
        })
        .catch(() => {
          this.$Message.error('列表查询失败')
        })
    },
    cancel() {
      this.modal1 = false
      this.$refs['moduleRules'].resetFields()
    }
  }
}
</script>
<style lang="less">
.myTipContainer {
  .ivu-input {
    border-color: red;
  }
}

.newModuleDialog {
  .ivu-modal-content {
    background-image: url("../../../assets/lowcode/dialogBg.png");
    background-size: cover;
  }
}
</style>
<style lang="less" scoped>
@import "~@/views/admin/styles/formStyle.less";

.modalHeaderClass {
  position: relative;
  color: #2d8cf0;
  font-size: 16px;
}

.normal {
  margin-right: 5px;
  color: #0056b5;
}

.red {
  margin-right: 5px;
  color: red;
}

.moduleBox {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;

  .moduleItem {
    display: flex;
    align-items: center;
    width: 264px;
    height: 154px;
    padding: 12px 8px 2px 16px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 4px 6px #0c1f500a, 0 0 0 1px #e6eaf0;
    cursor: pointer;
    position: relative;
    margin: 15px;

    > svg {
      position: absolute;
      top: 8px;
      right: 8px;
    }

    ul {
      display: flex;
      flex-direction: column;
      justify-content: center;
      width: 100%;
      height: 100%;

      li:nth-child(1) {
        display: flex;
        align-items: center;
        justify-content: flex-start;

        span {
          color: #333333;
          margin: 0 10px;
          font-size: 18px;
        }
      }

      li:nth-child(2) {
        color: #000;
        font-size: 14px;
        margin: 15px 0;
      }

      li:nth-child(3) {
        display: flex;
        align-items: center;
        justify-content: space-between;
      }

      li {
        list-style: none;
      }
    }

  }

  .moduleItem:hover {
    background-image: url("../../../assets/lowcode/moduleHoverBg.png");
    background-size: cover;
  }
}

.searchArea {
  height: 174px;
  background-color: #457CE0;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .top_left {
    height: 100%;

    .btnBox {
      margin: 33px 0 0 43px;
      display: flex;
      align-items: center;

      span {
        width: 288px;
        height: 32px;
        font-size: 24px;
        font-family: Microsoft YaHei-Bold, Microsoft YaHei;
        font-weight: bold;
        color: #FFFFFF;
        line-height: 28px;
        -webkit-background-clip: text;
      }

      div {
        width: 162px;
        height: 42px;
        background-image: url("../../../assets/lowcode/Frame.png");
        background-size: cover;
        cursor: pointer;
      }
    }

    .content {
      margin: 33px 0 0 43px;
      width: 609px;
      height: 46px;
      font-size: 14px;
      font-family: Microsoft YaHei-Regular, Microsoft YaHei;
      font-weight: 400;
      color: rgba(255, 255, 255, 0.7);
      line-height: 23px;
      -webkit-background-clip: text;
    }
  }

  > img {
    width: 171px;
    height: 171px;
    margin-right: 134px;
  }
}
</style>
