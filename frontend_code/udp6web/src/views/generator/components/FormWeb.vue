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
import {
  queryBasicDicList
} from '@/api/dic/listdic'
export default {
  name: 'FormWeb',
  data () {
    return {
      isLoading: false,
      arr: [
        { label: '文本框', value: 0 },
        { label: '密码', value: 1 },
        { label: '单选框', value: 2 },
        { label: '多选', value: 3 },
        { label: '日期(yyyy-MM-dd)', value: 4 },
        { label: '日期(yyyy-MM-dd HH:mm:ss)', value: 5 },
        { label: '多行文本', value: 6 },
        { label: '下拉框', value: 7 }
      ],
      dicArr: [],
      compareArr: [
        { label: '=', value: '0' },
        { label: '>=', value: '1' },
        { label: '<=', value: '2' },
        { label: 'like', value: '3' },
        { label: 'between', value: '4' }
      ],
      columns: [
        {
          title: '字段名称',
          key: 'columnName'
        },
        {
          title: '字段备注',
          key: 'columnComment'
        },
        {
          title: '表单显示',
          align: 'center',
          width: 120,
          key: 'isForm',
          render: (h, params) => {
            return h('Checkbox', {
              props: {
                value: params.row.isForm == '1'
              },
              on: {
                'on-change': event => {
                  this.dataArr[params.index].isForm = event
                  // this.typeChange(params.index);
                }
              }
            })
          }
        },
        {
          title: '列表显示',
          align: 'center',
          width: 120,
          key: 'isList',
          render: (h, params) => {
            return h('Checkbox', {
              props: {
                value: params.row.isList == '1'
              },
              on: {
                'on-change': event => {
                  this.dataArr[params.index].isList = event
                  // this.typeChange(params.index);
                }
              }
            })
          }
        },
        {
          title: '列表字段长度',
          key: 'controlLength',
          width: 200,
          render: (h, params) => {
            return h('Input', {
              props: {
                value: params.row.controlLength
              },
              on: {
                'on-blur': event => {
                  this.dataArr[params.index].controlLength = event.target.value
                }
              }
            })
          }
        },
        {
          title: '控件类型',
          width: 250,
          key: 'showType',
          render: (h, params) => {
            return h(
              'div',
              {
                class:
                  (params.row['isQuery'] || params.row['isForm']) &&
                  (!params.row['showType'] && !(params.row['showType'] === 0))
                    ? 'ctName'
                    : ''
              },
              [
                h(
                  'Select',
                  {
                    props: {
                      transfer: true,
                      value: params.row.showType,
                      clearable: true
                    },
                    style: {
                      width: '200px',
                      border:
                        (params.row['isQuery'] || params.row['isForm']) &&
                        (!params.row['showType'] &&
                          !(params.row['showType'] === 0))
                          ? '1px solid red' : ''
                    },
                    class: 'ctinpt',
                    on: {
                      'on-change': event => {
                        this.dataArr[params.index].showType = event
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
                ),
                h(
                  'div',
                  {
                    style: {
                      color: 'red',
                      display:
                        (params.row['isQuery'] || params.row['isForm']) &&
                        (!params.row['showType'] &&
                          !(params.row['showType'] === 0))
                          ? 'inline-block'
                          : 'none'
                    },
                    class: 'cxnameselect'
                  },
                  [
                    h('Icon', {
                      props: {
                        type: 'ios-alert'
                      }
                    }),
                    h('Font', { style: { color: 'red' } }, '控件类型必选')
                  ]
                )
              ]
            )
          }
        },
        {
          title: '是否查询',
          width: 120,
          align: 'center',
          key: 'query',
          render: (h, params) => {
            return h('Checkbox', {
              props: {
                value: params.row.isQuery == '1'
              },
              on: {
                'on-change': event => {
                  this.dataArr[params.index].isQuery = event
                  // this.typeChange(params.index);
                }
              }
            })
          }
        },
        {
          title: '查询类型',
          width: 200,
          key: 'queryType',
          render: (h, params) => {
            return h(
              'div',
              {
                class:
                  params.row['isQuery'] &&
                  (!params.row['queryType'] &&
                    !(params.row['queryType'] === '0'))
                    ? 'ctName'
                    : ''
              },
              [
                h(
                  'Select',
                  {
                    props: {
                      transfer: true,
                      value: params.row.queryType,
                      clearable: true
                    },
                    style: {
                      width: '100px',
                      border:
                        params.row['isQuery'] &&
                        (!params.row['queryType'] &&
                          !(params.row['queryType'] === '0'))
                          ? '1px solid red' : ''
                    },
                    class: 'ctinpt',
                    on: {
                      'on-change': event => {
                        this.dataArr[params.index].queryType = event
                        // this.typeChange(params.index);
                      }
                    }
                  },
                  this.compareArr.map(item => {
                    return h('Option', {
                      props: {
                        value: item.value,
                        label: item.label,
                        clearable: true
                      }
                    })
                  })
                ),
                h(
                  'div',
                  {
                    style: {
                      color: 'red',
                      display:
                        params.row['isQuery'] &&
                        (!params.row['queryType'] &&
                          !(params.row['queryType'] === '0'))
                          ? 'inline-block'
                          : 'none'
                    },
                    class: 'cxnameselect'
                  },
                  [
                    h('Icon', {
                      props: {
                        type: 'ios-alert'
                      }
                    }),
                    h('Font', { style: { color: 'red' } }, '查询类型必选')
                  ]
                )
              ]
            )
          }
        },
        {
          title: '数据字典',
          width: 200,
          key: 'dicCode',
          render: (h, params) => {
            return h(
              'div',
              [
                h(
                  'Select',
                  {
                    props: {
                      transfer: true,
                      value: params.row.dicCode,
                      clearable: true
                    },
                    style: {
                      width: '100px'
                    },
                    class: 'ctinpt',
                    on: {
                      'on-change': event => {
                        this.dataArr[params.index].dicCode = event
                        // this.typeChange(params.index);
                      }
                    }
                  },
                  this.dicArr.map(item => {
                    return h('Option', {
                      props: {
                        value: item.dicCode,
                        label: item.dicName
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
  watch: {
    currentData (v) {
      setTimeout(() => {
        this.dataArr = this.$store.state.tabdata.info
      }, 200)
    },
    // addFlag: (val) => {
    //   if (val === 'add') {

    //   } else {

    //   }
    // },
    closeFlag (val) {
      if (!val) {
        this.dataArr = []
      }
    }
  },
  mounted () {
    this.queryDic()
    // console.log(this.dataArr,'123')
    // console.log("3", this.$store.state.tabdata.dataArr);
    // this.dataArr = this.$store.state.tabdata.dataArr;
    // this.getData();
  },
  methods: {
    queryDic () {
      queryBasicDicList()
      // this.$http.get('/api/dic/cscpBasicHxDics', {
      //   // params: {
      //   //   'dicAttr.equals': 'tree'
      //   // }
      // })
        .then((response) => {
          this.dicArr = response.data.data
        })
        .catch(function (error) {
          console.log(error)
        })
    }
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
  border-radius: 4px;
}
.ctName .cxname {
  position: absolute;
  bottom: 21px;
}

 .cxnameselect {
  position: absolute;
  bottom: 21px;
}
</style>
