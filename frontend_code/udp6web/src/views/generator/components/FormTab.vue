<template>
  <div>
    <Row :gutter="48">
      <Col span="24">
        <Tabs v-model="tab1" @on-click="tabClick" :animated="false">
          <TabPane label="字段数据库属性" name="tab1">
            <form-data ref="FormData" :close-flag="closeFlag" :add-flag="addFlag"></form-data>
          </TabPane>
          <TabPane label="字段页面属性" name="tab2">
            <form-web ref="FormWeb"  :close-flag="closeFlag" :add-flag="addFlag" ></form-web>
          </TabPane>
          <TabPane label="字段校验" name="tab3">
            <form-validate ref="FormValidate"  :close-flag="closeFlag" :add-flag="addFlag" ></form-validate>
          </TabPane>
        </Tabs>
      </Col>
    </Row>
  </div>
</template>

<script>
import FormData from './FormData'
import FormWeb from './FormWeb'
import FormValidate from './FormValidate'

export default {
  name: 'Form',
  components: { FormWeb, FormData, FormValidate },
  props: {
    tableName: String,
    closeFlag: {
      type: Boolean,
      default: false
    },
    addFlag: {
      type: String,
      default: 'add'
    }
  },
  data () {
    return {
      tab1: 'tab1',
      id: null,
      saveLoading: false,
      suiteArr: [],
      info: {
        id: null,
        tableName: null,
        tableContent: null,
        packageName: null,
        formType: 0,
        formSuite: null,
        dbType: 0,
        keyType: 0,
        persistenceType: 0,
        formFields: []
      },
      keyFlag: false,
      dataArr: [
        {
          columnName: '',
          columnComment: '',
          columnLength: '',
          pointLength: '',
          defaultValue: '',
          columnType: '',
          isNull: false,
          isPk: false,
          orderNum: 1,
          isForm: false,
          isList: false,
          controlLength: null,
          isQuery: false,
          queryType: null,
          showType: null
        }
      ],
      formDataLabel: h => {
        return h('div', [
          h(
            'span',
            { style: { display: this.keyFlag ? 'inline' : 'none' } },
            '数据库属性'
          ),
          h('div', { style: { display: this.keyFlag ? 'none' : 'inline' } }, [
            h(
              'span',
              {
                style: {
                  color: '#ff0000'
                }
              },
              '数据库属性'
            ),
            h(
              'font',
              {
                style: {
                  color: '#ff0000'
                }
              },
              '*'
            )
          ])
        ])
      }
    }
  },
  mounted () {
  },
  watch: {
    closeFlag: function (val) {
      if (!val) {
        this.tab1 = 'tab1'
      }
    }
  },
  methods: {
    exportForm (name) {
    },
    listSuite () {
    },
    back () {
      this.closeMe()
    },
    tabClick (v) {

    }
  }
}
</script>

<style scoped>
  .button-right-left {
    margin-right: 15px;
  }

  .button-right {
    margin-right: 55px;
  }

</style>
