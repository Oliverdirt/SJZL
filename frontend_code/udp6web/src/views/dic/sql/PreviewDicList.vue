<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea" >
          <Form  :label-width="80" >
            <Row :gutter="24">
              <Col span="24" style="text-align: right;margin-top: 24px">
                <FormItem>
                  <Button  type="primary" @click="closeMe()" style="margin-right: 8px">返回</Button>
                  <Button  type="default"  @click="syncdic(model.id)">字典同步</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
       <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="form-area" >
          <Form
        style="width: 900px"
        :model="model"
        :label-width="110"
        label-position="left"
        inline
      >
        <FormItem :class="{ 'model-hidden': true }" label="文本框">
          <Input v-model="model.id" type="text"></Input>
        </FormItem>
        <FormItem :class="{ 'model-hidden': true }" label="文本框">
          <Input v-model="model.createTime" type="text"></Input>
        </FormItem>
        <FormItem :class="{ 'model-hidden': true }" label="文本框">
          <Input v-model="model.createUser" type="text"></Input>
        </FormItem>
        <FormItem :class="{ 'model-hidden': true }" label="文本框">
          <Input v-model="model.updateTime" type="text"></Input>
        </FormItem>
        <FormItem :class="{ 'model-hidden': true }" label="文本框">
          <Input v-model="model.updateUser" type="text"></Input>
        </FormItem>
        <!-- <label>查询SQL</label> -->
        <FormItem label="查询SQL" :label-width="120">
          <Input
            v-model="model.dicSelectSql"
            disabled
            style="width: 750px"
            type="textarea"
          ></Input>
        </FormItem>
        <FormItem label="字典名">
          <Input
            v-model="model.dicName"
            disabled
            style="width: 320px"
            type="text"
          ></Input>
        </FormItem>
        <FormItem label="字典编码">
          <Input
            v-model="model.dicCode"
            disabled
            style="width: 320px"
            type="text"
          ></Input>
        </FormItem>
        <FormItem label="字典项编码列">
          <Input
            v-model="model.dicItemCodeColumn"
            disabled
            style="width: 320px"
            type="text"
          ></Input>
        </FormItem>
        <FormItem label="字典项值列">
          <Input
            v-model="model.dicItemValueColumn"
            disabled
            style="width: 320px"
            type="text"
          ></Input>
        </FormItem>
      </Form>
        </div>
      </Card>
         <Card :bordered="false" :dis-hover="true" :shadow="false" class="listCard">
        <div class="list-area" style="margin-left: 80px;margin-right: 80px;" >
                <div class="table-list">
                <h4 slot="title" >查询SQL执行结果</h4>
                <Table border :columns="columns" :data="tableData"></Table>
                <Page :total="pageData.execTotal" :page-size="pageData.size" :current="pageData.page" show-elevator show-sizer show-total @on-page-size-change="changePage($event, 'size')" @on-change="changePage($event, 'page')" style="margin-top: 20px;"/>
                </div>
                <div class="table-list" style="margin-top: 21px;">
                <h4 slot="title">字典表预览</h4>
                <Table border :columns="columns2" :data="tableDicData"></Table>
                <Page :total="pageData.dicTotal" :page-size="pageData.size" :current="pageData.page" show-elevator show-sizer show-total @on-page-size-change="changePage2($event, 'size')" @on-change="changePage2($event, 'page')" style="margin-top: 20px;"/>
                </div>
        </div>
      </Card>

      </div>
  </div>
</template>

<script>
import {
  syncSqlDicById,
  getSqlDicItemByDicId,
  getSqlDicItemByDicId2,
  querySqlDicById,
  addSqlDicByData,
  updateSqlDicByData
} from '@/api/dic/sqldic'
export default {
  name: 'cscpHxSqldicPreview',
  data () {
    return {
      pageData: {
        execTotal: 0,
        dicTotal: 0,
        size: 5,
        page: 1
      },
      // ID
      id: null,
      saveLoading: false,
      // // 分页参数
      // pageLength: 10, // 每页条数
      // pageTotal: 0, // 总数
      // pageOffset: 0, // 页码
      // 查询参数
      cscpHxSqldicBaseinfo: {
        dicSelectSql: null,
        dicName: null,
        dicCode: null
      },
      // 表单参数
      model: {
        id: null,
        dicSelectSql: null,
        dicName: null,
        dicCode: null,
        dicNotes: null,
        dicItemCodeColumn: null,
        dicItemValueColumn: null,
        dicItemValueNotes: null,
        createTime: null,
        createUser: null,
        updateTime: null,
        updateUser: null
      },
      tableData: [],
      tableDicData: [],
      sqldiccount: 0,
      dicItemcount: 0,

      columns: [
        {
          key: 'item_code',
          title: '字典项编码'
        },
        {
          key: 'item_value',
          title: '字典项值'
        }
      ],
      columns2: [
        {
          key: 'itemCode',
          title: '字典项编码'
        },
        {
          key: 'itemValue',
          title: '字典项值'
        }
      ]
    }
  },
  mounted () {
    if (
      typeof this.$route.query.id !== 'undefined' &&
      this.$route.query.id !== null &&
      this.$route.query.id !== ''
    ) {
      this.id = this.$route.query.id

      // ID存在渲染表单
      this.initForm()
      // 执行查询
      this.selectSqlDic2()
      this.selectDicItemByDicCode()
    }
  },
  methods: {
    changePage (e, type) {
      console.info(e)
      if (type === 'size') {
        this.pageData.size = e
      } else if (type === 'page') {
        this.pageData.page = e
      }
      this.pageData.page = e
      console.info('changePage')
      console.info(this.pageData.page)
      console.info(this.pageData.size)
      console.info('changePage')
      this.selectSqlDic2()
      this.selectDicItemByDicCode()
    },
    changePage2 (e, type) {
      console.info(e)
      if (type === 'size') {
        this.pageData.size = e
      } else if (type === 'page') {
        this.pageData.page = e
      }
      this.pageData.page = e
      this.selectSqlDic2()
      this.selectDicItemByDicCode()
    },
    syncdic (id) {
      let _this = this
      this.$Modal.confirm({
        title: '确定同步？',
        onOk () {
          syncSqlDicById(id)
          // _this.$http.post('/api/syncdic/' + id)
            .then((response) => {
              if (response.status === 200) {
                _this.$Notice.success({
                  title: '同步成功'

                })
                _this.selectDicItemByDicCode()
              } else {
                _this.$Notice.error({
                  title: '同步失败'
                })
              }
            })
        }

      })
    },
    selectDicItemByDicCode () {
      let _this = this
      getSqlDicItemByDicId(this.id)
      // _this.$http
      //   .get('/api/selectDicItemByDicCode/' + this.id)
        .then(function (response) {
          _this.tableDicData = []
          let data = response.data
          // let list = data.data
          let list = data
          _this.pageData.dicTotal = list.length
          // _this.dicItemcount = list.length
          // let dispalycount = list.length
          // if (list.length > 10) {
          //   dispalycount = 10
          // }
          let startpos2 = (_this.pageData.page - 1) * _this.pageData.size
          let endpos2 = _this.pageData.page * _this.pageData.size > _this.pageData.dicTotal ? _this.pageData.dicTotal : _this.pageData.page * _this.pageData.size
          for (let i = startpos2; i < endpos2; i++) {
            let map = {
              itemValue: list[i].itemValue,
              itemCode: list[i].itemCode
            }
            _this.tableDicData.push(map)
          }
          // for (let i = 0; i < dispalycount; i++) {
          //   let map = {
          //     itemValue: list[i].itemValue,
          //     itemCode: list[i].itemCode
          //   }
          //   _this.tableDicData.push(map)
          // }
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    selectSqlDic2 () {
      let _this = this
      getSqlDicItemByDicId2(this.id)
      // _this.$http
      //   .get('/api/selectSqlDic/' + this.id)
        .then(function (response) {
          _this.tableData = []
          let data = response.data
          let list = data
          _this.pageData.execTotal = list.length

          // _this.sqldiccount = list.length
          // let dispalycount = list.length
          // if (list.length > 10) {
          //   dispalycount = 10
          // }
          let startpos = (_this.pageData.page - 1) * _this.pageData.size
          let endpos = _this.pageData.page * _this.pageData.size > _this.pageData.execTotal ? _this.pageData.execTotal : _this.pageData.page * _this.pageData.size

          for (let i = startpos; i < endpos; i++) {
            _this.tableData.push(list[i])
          }
          console.info(_this.tableData)
        })
        .catch(function (error) {
          console.log(error)
          _this.$Notice.error({
            title: 'SQL查询出错，详情请查看日志!',
            desc: error.message
          })
        })
    },
    // 保存
    save () {
      if (this.id) {
        this.update()
      } else {
        this.create()
      }
    },

    create () {
      let _this = this
      _this.saveLoading = true
      // const [url, httpConfig] = [
      //   '/api/cscpHxSqldicBaseinfos',
      //   {
      //     headers: {
      //       'Content-Type': 'application/json'
      //     }
      //   }
      // ]

      let copyDetail = JSON.parse(JSON.stringify(this.model))
      addSqlDicByData(copyDetail)
      // _this.$http
      //   .post(url, this.model, httpConfig)
        .then(function (response) {
          _this.$Notice.success({
            title: '保存成功!'
          })
          _this.back()
        })
        .catch(function (error) {
          _this.$Notice.error({
            title: '保存失败!'
          })
          _this.saveLoading = false
          console.log(error)
        })
    },

    update () {
      let _this = this
      _this.saveLoading = true
      // const queryHeaders = {
      //   'Content-Type': 'application/json'
      // }
      let copyDetail = JSON.parse(JSON.stringify(this.model))
      updateSqlDicByData(copyDetail)
      // const [url, httpConfig] = [
      //   '/api/cscpHxSqldicBaseinfos',
      //   {
      //     headers: {
      //       'Content-Type': 'application/json'
      //     }
      //   }
      // ]
      // _this.$http
      //   .put(url, this.model, httpConfig)
        .then(function (response) {
          _this.$Notice.success({
            title: '更新成功!'
          })
          _this.back()
        })
        .catch(function (error) {
          _this.$Notice.error({
            title: '更新失败!'
          })
          _this.saveLoading = false
          console.log(error)
        })
    },

    // 渲染表单
    initForm () {
      let _this = this
      querySqlDicById(this.id)
      // _this.$http
      //   .get('/api/cscpHxSqldicBaseinfos/' + this.id)
        .then(function (response) {
          let tem = response.data;
          // this.dicCode = tem.dicCode;
          (_this.model.id = tem.id),
          (_this.model.dicSelectSql = tem.dicSelectSql),
          (_this.model.dicName = tem.dicName),
          (_this.model.dicCode = tem.dicCode),
          (_this.model.dicNotes = tem.dicNotes),
          (_this.model.dicItemCodeColumn = tem.dicItemCodeColumn),
          (_this.model.dicItemValueColumn = tem.dicItemValueColumn),
          (_this.model.dicItemValueNotes = tem.dicItemValueNotes),
          (_this.model.createTime = tem.createTime),
          (_this.model.createUser = tem.createUser),
          (_this.model.updateTime = tem.updateTime),
          (_this.model.updateUser = tem.updateUser)
        })
        .catch(function (error) {
          _this.$Notice.error({
            title: '获取表单失败!'
          })
          console.log(error)
        })
    },
    closeMe () {
      this.$store.commit('app/closePage', {
        vm: this,
        fromName: this.$route.name,
        toName: 'cscp_hx_sqldic_baseinfo'
      })
    }

    // 返回,需要自定义配置路由
    // back () {
    //   let _this = this
    //   let tagName = _this.$route.name
    //   _this.$store.commit('removeTag', tagName)
    //   _this.$store.commit('closePage', tagName)
    //   _this.$router.push({
    //     name: 'cscp_hx_sqldic_baseinfo'
    //   })
    // }
  }
}
</script>

<style lang="less" scoped>
@import "../../admin/styles/formStyle.less";
::v-deep .ql-editor{
  min-height: 200px;
}
.model-hidden {
  display: none;
}
  .form-area {
    margin-top: 40px;
    display: flex;
    justify-content: center;
  }
.form-area .ivu-form .ivu-form-item {
    margin-bottom: 15px;
}

</style>
