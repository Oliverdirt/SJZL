<template>
  <div class="tab-height-div" style="height: 100%">
    <div class="tableBtn">
      <!--      <Button  type="primary" style="margin-right: 10px;margin-bottom: 5px" icon="md-add" :disabled="selectedRows.length > 1" @click="add()">新增</Button>-->
      <!--      <Button  type="error" style="margin-right: 10px;margin-bottom: 5px" icon="ios-trash-outline" >删除</Button>-->
    </div>
    <Spin fix v-if="isLoading">
      <Icon type="ios-loading" size="18" class="demo-spin-icon-load"></Icon>
      <div>Loading</div>
    </Spin>
    <Table :columns="columns" :data="dataArr" ref="table" border>
      <template slot-scope="{ row, index }" slot="key" style="color: darkred">
        <Checkbox
          v-if="editDisabled"
          v-model="dataArr[index]['isPk']"
          :disabled="!(disabled === dataArr[index]['isPk'])"
          @on-change="keyChange"
        ></Checkbox>
        <Checkbox v-else v-model="dataArr[index]['isPk']" disabled></Checkbox>
      </template>
    </Table>
  </div>
</template>

<script>
import { getColumnTypesByParams } from '@/api/generator/generator'
export default {
  name: 'FormData',
  data () {
    return {
      listDbTypeData: '',
      mgc: [],
      isLoading: false,
      editDisabled: true,
      disabled: false,
      selectedRows: [],
      dbArr: [],
      columns: [
        {
          title: '数据库字段名',
          key: 'columnName',
          render: (h, params) => {
            return h(
              'div',
              { class: params.row['columnName'] ? '' : 'ctName' },
              [
                h('Input', {
                  props: {
                    value: params.row.columnName
                  },
                  class: 'ctinpt',
                  style: {
                    border:
                      (params.row.columnName === '' ||
                        params.row.columnName === null) &&
                      this.closeFlag
                        ? '1px solid red'
                        : ''
                  },
                  on: {
                    'on-blur': (event) => {
                      this.dataArr[params.index].columnName =
                        event.target.value
                      this.$store.commit('setInfo', this.dataArr)
                    }
                  }
                }),
                h(
                  'div',
                  {
                    style: {
                      color: 'red',
                      display:
                        (params.row.columnName === '' ||
                          params.row.columnName === null ||
                          this.mgc.indexOf(params.row.columnName) !== -1) &&
                        this.closeFlag
                          ? 'inline-block'
                          : 'none'
                    },
                    class: 'cxname'
                  },
                  [
                    h(
                      'Font',
                      { style: { color: 'red' } },
                      params.row.columnName ? params.row.columnName + '与数据库系统字段重复' : '字段名称必填'
                    ),
                    h('Icon', { props: { type: '' } })
                  ]
                )
              ]
            )
          }
        },
        {
          title: '字段名称',
          key: 'columnComment',
          render: (h, params) => {
            return h('Input', {
              props: {
                value: params.row.columnComment
              },
              on: {
                'on-blur': (event) => {
                  this.dataArr[params.index].columnComment = event.target.value
                }
              }
            })
          }
        },
        {
          title: '字段类型',
          width: 150,
          key: 'columnType',
          render: (h, params) => {
            return h(
              'div',
              {
                class: params.row['columnType'] ? '' : 'ctName'
                // style: { display: "flex", flexDirection: "column" }
              },
              [
                h(
                  'Select',
                  {
                    props: {
                      transfer: true,
                      value: params.row.columnType,
                      filterable: true
                    },
                    class: 'ctinpt',
                    style: {
                      border:
                        !params.row['columnType'] && this.closeFlag
                          ? '1px solid red'
                          : ''
                    },
                    on: {
                      'on-change': (event) => {
                        this.dataArr[params.index].columnType = event
                        this.typeChange(params.index)
                      }
                    }
                  },
                  this.dbArr.map((item) => {
                    return h('Option', {
                      props: {
                        value: item.typeNum,
                        label: item.typeName
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
                        !params.row['columnType'] && this.closeFlag
                          ? 'inline-block'
                          : 'none'
                    },
                    class: 'cxnameselect'
                  },
                  [
                    h('Icon', {
                      props: {
                        type: ''
                      }
                    }),
                    h('Font', { style: { color: 'red' } }, '字段类型必填')
                  ]
                )
              ]
            )
          }
        },
        {
          title: '字段长度',
          key: 'columnLength',
          render: (h, params) => {
            return h(
              'div',
              {
                class:
                  (params.row['columnType'] === 6 ||
                    params.row['columnType'] === 7 ||
                    params.row['columnType'] === 8) &&
                  !params.row['columnLength']
                    ? 'ctName'
                    : ''
              },
              [
                h('Input', {
                  props: {
                    value: params.row.columnLength
                  },
                  class: 'ctinpt',
                  on: {
                    'on-blur': (event) => {
                      this.dataArr[params.index].columnLength =
                        event.target.value
                    }
                  }
                }),
                h(
                  'div',
                  {
                    style: {
                      color: 'red',
                      display:
                        (params.row['columnType'] === 6 ||
                          params.row['columnType'] === 7 ||
                          params.row['columnType'] === 8) &&
                        !params.row['columnLength']
                          ? 'inline-block'
                          : 'none'
                    },
                    class: 'cxname'
                  },
                  [
                    h('Icon', {
                      props: {
                        type: 'ios-alert'
                      }
                    }),
                    h('Font', { style: { color: 'red' } }, '字段长度必填')
                  ]
                )
              ]
            )
          }
        },
        {
          title: '小数点',
          key: 'pointLength',
          render: (h, params) => {
            return h(
              'div',
              {
                class:
                  (params.row['columnType'] === 6 ||
                    params.row['columnType'] === 7 ||
                    params.row['columnType'] === 8) &&
                  !params.row['pointLength']
                    ? 'ctName'
                    : ''
              },
              [
                h('Input', {
                  props: {
                    value: params.row.pointLength
                  },
                  class: 'ctinpt',
                  on: {
                    'on-blur': (event) => {
                      this.dataArr[params.index].pointLength =
                        event.target.value
                    }
                  }
                }),
                h(
                  'div',
                  {
                    style: {
                      color: 'red',
                      bottom: '21px',
                      display:
                        (params.row['columnType'] === 6 ||
                          params.row['columnType'] === 7 ||
                          params.row['columnType'] === 8) &&
                        !params.row['pointLength']
                          ? 'inline-block'
                          : 'none'
                    },
                    class: 'cxname'
                  },
                  [
                    h('Icon', {
                      props: {
                        type: 'ios-alert'
                      }
                    }),
                    h('Font', { style: { color: 'red' } }, '小数点必填')
                  ]
                )
              ]
            )
          }
        },
        {
          title: '默认值',
          key: 'defaultValue',
          render: (h, params) => {
            return h('Input', {
              props: {
                value: params.row.defaultValue
              },
              on: {
                'on-blur': (event) => {
                  this.dataArr[params.index].defaultValue = event.target.value
                }
              }
            })
          }
        },
        {
          title: '允许空值',
          width: 100,
          align: 'center',
          key: 'isNull',
          render: (h, params) => {
            return h('Checkbox', {
              props: {
                value: params.row.isNull == '1'
              },
              on: {
                'on-change': (event) => {
                  this.dataArr[params.index].isNull = event
                  // this.typeChange(params.index);
                }
              }
            })
          }
        },
        {
          title: '是否为主键',
          width: 150,
          align: 'center',
          slot: 'key',
          renderHeader: (h, params) => {
            let isKeySelectFlag = false
            let allRows = this.dataArr
            let len = allRows.length
            for (let i = 0; i < len; i++) {
              let isKey = allRows[i].isPk
              if (isKey === true || isKey === 1) {
                isKeySelectFlag = true
              }
            }
            if (isKeySelectFlag) {
              return h('span', '是否为主键')
            } else {
              return h('div', [
                h(
                  'span',
                  {
                    style: {
                      color: '#ff0000'
                    }
                  },
                  '是否为主键'
                ),
                h(
                  'font',
                  {
                    style: {
                      // color: '#ff0000',
                      content: '*',
                      display: 'inline-block',
                      'margin-left': '4px',
                      'line-height': '1',
                      'font-family': 'SimSun',
                      'font-size': '14px',
                      color: '#ed4014'
                    }
                  },
                  '  *'
                )
              ])
            }
          }
        },
        // {
        //   title: '排序',
        //   key: 'orderNum',
        //   render: (h, params) => {
        //     return h('Input', {
        //       props: {
        //         value: params.row.orderNum
        //       },
        //       on: {
        //         'on-blur': event => {
        //           this.dataArr[params.index].orderNum = event.target.value
        //         }
        //       }
        //     })
        //   }
        // },
        {
          title: '操作',
          slot: 'action',
          align: 'center',
          render: (h, params) => {
            let index = params.index
            let length = this.dataArr.length
            return h('div', [
                h('Button', {
                  props: {
                    type: 'primary',
                    size: 'small',
                    icon: 'md-add'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.add(params.index)
                    }
                  }
                }),
                h('Button', {
                  props: {
                    type: 'error',
                    size: 'small',
                    icon: 'md-remove'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.remove(params.index)
                    }
                  }
                }),
                h('Button', {
                  props: {
                    type: 'success',
                    size: 'small',
                    icon: 'md-arrow-round-up'
                  },
                  style: {
                    marginRight: '5px',
                    display: (length == 1 || index == 0) ? 'none' : 'inline-block'
                  },
                  on: {
                    click: () => {
                      this.moveup(params.index)
                    }
                  }
                }),
                h('Button', {
                  props: {
                    type: 'warning',
                    size: 'small',
                    icon: 'md-arrow-round-down'
                  },
                  on: {
                    click: () => {
                      this.movedown(params.index)
                    }
                  },
                  style: {
                    display: index == length - 1 ? 'none' : 'inline-block'
                  },
                })
              ])
       
          }
        }
      ],
      tempDatalist: [],
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
      ]
    }
  },
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
  mounted () {
    this.cscpSqlTypeFn()
    if (this.$util.Setting.dataBankType === '0') {
      this.mgc = this.$util.Setting.columnNamesql
    } else if (this.$util.Setting.dataBankType === '1' || this.$util.Setting.dataBankType === '2') {
      this.mgc = this.$util.Setting.columnNamegs
    }
  },
  computed: {
    currentData () {
      return this.$store.state.tabdata.info
    }
  },
  watch: {
    dbType (value, old) {
      this.listDbType(value)
    },
    currentData (v) {
      setTimeout(() => {
        this.dataArr = this.$store.state.tabdata.info
        // this.isLoading = false;
      }, 20)
    },
    // addFlag: (val) => {
    //   if (val === 'add') {

    //   } else {

    //   }
    // },
    closeFlag (val) {
      console.log(val, 'val1')
      if (!val) {
        this.disabled = false
        this.editDisabled = true
        this.dataArr = [
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
        ]
      }
    }
  },
  methods: {
    add (index) {
      let max = 1
      let orderArr = []
      for (let i = 0; i < this.dataArr.length; i++) {
        orderArr.push(this.dataArr[i].orderNum)
      }
      if (orderArr.length > 0) {
        max = Math.max.apply(null, orderArr)
        max++
      }
      let map = {
        columnName: '',
        columnComment: '',
        columnLength: '',
        pointLength: '',
        defaultValue: '',
        columnType: '',
        isNull: true,
        isPk: false,
        orderNum: max,
        isForm: false,
        isList: false,
        controlLength: null,
        isQuery: false,
        queryType: null,
        showType: null
      }
      if (index) {
        this.dataArr.splice(index + 1, 0, map)
      } else this.dataArr.push(map)
      this.$store.commit('setInfo', this.dataArr)
    },
    remove (index) {
      var rowInfo = this.dataArr[index]
      var isKey = rowInfo.isPk
      if (isKey === true) {
        this.disabled = false
        this.editDisabled = true
      }
      this.dataArr.splice(index, 1)
      this.$store.commit('setInfo', this.dataArr)
      if (this.dataArr.length === 0) {
        this.disabled = false
        this.editDisabled = true
        this.add()
      }
    },
    moveup (index) {
      let moveitem = this.dataArr.splice(index, 1)
      this.dataArr.splice(index - 1, 0, moveitem[0])
      let tempOrderNum = this.dataArr[index].orderNum
      this.dataArr[index].orderNum = this.dataArr[index - 1].orderNum
      this.dataArr[index - 1].orderNum = tempOrderNum
    },
    movedown (index) {
      let moveitem = this.dataArr.splice(index, 1)
      this.dataArr.splice(index + 1, 0, moveitem[0])
      let tempOrderNum = this.dataArr[index].orderNum
      this.dataArr[index].orderNum = this.dataArr[index + 1].orderNum
      this.dataArr[index + 1].orderNum = tempOrderNum
    },
    keyChange (flag) {
      this.disabled = flag
    },
    typeChange (index) {},
    cscpSqlTypeFn () {
      this.$http.get('/api/cscpSqlType').then((res) => {
        this.listDbTypeData = res.data.data
        this.listDbType()
      })
    },
    listDbType (value) {
      // const [url, param] = [
      //   '/api/cscpHxColumnTypes', {
      //     params: {
      //       dbType: 0
      //     }
      //   }
      // ]
      const dbTypeParams = {
        dbType: this.listDbTypeData
      }
      getColumnTypesByParams(dbTypeParams)
        // this.$http.get(url, param)
        .then((response) => {
          this.dbArr = response.data
        })
        .catch(function (error) {
          console.log(error)
        })
    }
  }
}
</script>
<style>
.tab-height-div {
  height: calc(100vh - 180px);
  overflow: auto;
}

.ctName {
  height: 115px;
  position: relative;
  /* display: flex;
  flex-direction: column */
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

.ctName {
  font-size: 13px;
}

.cxnameselect {
  position: absolute;
  bottom: 21px;
}

.demo-spin-icon-load {
  animation: ani-demo-spin 1s linear infinite;
}

@keyframes ani-demo-spin {
  from {
    transform: rotate(0deg);
  }
  50% {
    transform: rotate(180deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
