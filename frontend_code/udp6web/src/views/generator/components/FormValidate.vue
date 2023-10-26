<template>
  <div class="tab-height-div" style="height: 100%;">
    <Spin fix v-if="isLoading">
      <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
      <div>Loading</div>
    </Spin>
    <Table  :columns="columns" :data="dataArr" border></Table>
  </div>
</template>

<script>
export default {
  name: 'FormValidate',
  data () {
    return {
      isLoading: false,
      arr: [
        { label: '手机号', value: 'cell_phone_number' },
        { label: '国内电话号', value: 'domestic_telephone_number' },
        { label: '邮件', value: 'mail' },
        { label: '网址', value: 'website' },
        { label: 'MAC地址纯数字', value: 'mac_address' },
        { label: '纯数字', value: 'pure_numbers' },
        { label: '纯字母', value: 'plain_letters' },
        { label: '英文+数字', value: 'english_and_number' },
        { label: '英文+数字+下划线', value: 'english_number_underline' }
      ],
      columns: [
        {
          title: '字段名称',
          key: 'columnName',
          filteredValue: [null],
          filterMethod (value, row) {
            return row.columnName.trim().length !== 0
          },
          render: (h, params) => {
            return h('span', params.row.columnName)
          }
        },
        {
          title: '字段备注',
          key: 'columnComment',
          render: (h, params) => {
            return h('span', params.row.columnComment)
          }
        },
        {
          title: '验证规则',
          key: 'validateRule',
          render: (h, params) => {
            return h(
              'div',
              [
                h(
                  'Select',
                  {
                    props: {
                      transfer: true,
                      value: params.row.validateRule,
                      clearable: true,
                      // disabled: !(params.row.columnType === 14 || params.row.columnType === 15)
                      disabled: ![14, 15, 17, 19, 21, 23].includes(params.row.columnType)
                    },
                    style: {
                      width: '200px'
                    },
                    class: 'ctinpt',
                    on: {
                      'on-change': event => {
                        this.dataArr[params.index].validateRule = event
                        // this.typeChange(params.index);
                      }
                    }
                  },
                  this.arr.map(item => {
                    return h('Option', {
                      props: {
                        value: item.value,
                        label: item.label
                      }
                    })
                  })
                )
              ]
            )
          }
        }
      ],
      dataArr: []
    }
  },
  props: {
    // dataArr: Array,
    closeFlag: {
      type: Boolean,
      default: false
    },
    addFlag: {
      type: String,
      default: 'add'
    }
  },
  computed: {
    currentData () {
      return this.$store.state.tabdata.info
    }
  },
  methods: {
    // getData() {
    //   if (this.isWeb) {
    //     this.dataArr = this.$store.state.tabdata.dataArr;
    //   }
    // }
  },
  watch: {
    currentData (v) {
      setTimeout(() => {
        this.dataArr = this.$store.state.tabdata.info
      }, 200)
    },
    closeFlag (val) {
      if (!val) {
        this.dataArr = []
      }
    }
  },
  mounted () {
    // console.log(this.dataArr,'123')
    // console.log("3", this.$store.state.tabdata.dataArr);
    // this.dataArr = this.$store.state.tabdata.dataArr;
    // this.getData();
  }
}
</script>

<style scoped>
.tab-height-div {
  height: calc(100vh - 180px);
  overflow: auto;
}
.ctName {
  height: 115px;
  position: relative;
}
.ctName .ctinpt {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
}
.ctName .cxname {
  position: absolute;
  bottom: 3px;
}

 .cxnameselect {
  position: absolute;
  bottom: 21px;
}
</style>
