<template>
  <div class="user-mg" style="height: 100%">
    <Row :gutter="16" style="height: 100%">
      <Col span="24">
        <div class="formCard-content">
          <br><br>
          <Table border :columns="columns" :data="dataArr" draggable @on-drag-drop="changeOrder"></Table>
        </div>
      </Col>
    </Row>
  </div>
</template>
<script>
import { componentTypes, queryTypes, queryFieldTypes } from '@/views/lowcode/customize/config/types.js'
import request from '@/api/workflow/request'
export default {
  name: 'listConfig',
  data () {
    return {
      loading: false,
      tableArr: [],
      tableName: null,
      dataArr: [],
      detail: {},
      pageId: this.pageId,
      addOrEditFlag: 'add', // add/edit
      columns: [
        {
          title: '数据表名',
          align: 'center',
          key: 'tableName'
        },
        {
          title: '数据库字段名',
          align: 'center',
          key: 'fieldName'
        },
        {
          title: '字段名称',
          key: 'fieldComment',
          render: (h, params) => {
            return h('Input', {
              props: {
                value: params.row.fieldComment
              },
              on: {
                'on-blur': event => {
                  this.dataArr[params.index].fieldComment = event.target.value
                }
              }
            })
          }
        },
        {
          title: '是否表单展示',
          key: 'isForm',
          align: 'center',
          render: (h, params) => {
            return h('Checkbox', {
              props: {
                value: params.row.isForm === '1'
              },
              on: {
                'on-change': event => {
                  this.dataArr[params.index].isForm = event
                }
              }
            })
          }
        },
        {
          title: '表单类型',
          key: 'componentType',
          render: (h, params) => {
            return h(
              'div',
              [
                h(
                  'Select',
                  {
                    props: {
                      transfer: true,
                      value: params.row.componentType,
                      clearable: true
                    },
                    style: {
                      // width: '200px',
                      border: ((params.row.isForm === '1' || params.row.isForm === true) && params.row.componentType == null) ? '1px solid red' : ''
                    },
                    class: 'ctinpt',
                    on: {
                      'on-change': event => {
                        this.dataArr[params.index].componentType = event
                      }
                    }
                  },
                  [...componentTypes].map(item => {
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
                      display: ((params.row.isForm === '1' || params.row.isForm === true) && params.row.componentType == null) ? 'block' : 'none'
                    },
                    class: 'cxname'
                  },
                  [
                    h('Font', { style: { color: 'red' } }, '请选择表单类型！'),
                    h('Icon', { props: { type: '' } })
                  ]
                )
              ]
            )
          }
        }
      ]
    }
  },
  props: {
    // tableNameParams: String,
    // formIdParams: String
    pageIdListConfig: String
  },
  created () {
    this.pageId = this.pageIdListConfig
    this.pageIdListConfig ? this.formTableFn(this.pageId) : ''
  },
  methods: {
    // 行数据
    rowFn (row) {
      this.pageId = row.formId
      this.formTableFnFive(row.formId)
    },
    // 根据formId查模型名称
    formTableFn (params) {
      this.$http.get('/api/lowcode/customize/cscpCustomizeVforms/' + params).then((res) => {
        this.tableName = res.data.formTable
        this.queryFormColumns()
      })
    },
    // 50
    formTableFnFive (params) {
      this.$http.get('/api/lowcode/customize/cscpCustomizeVforms/' + params).then((res) => {
        this.$parent.$parent.formDataFn(res.data)
        this.tableName = res.data.formTable
        this.queryFormColumns()
      })
    },
    queryList () {
      return new Promise((resolve) => {
        let params = {
          page: 1,
          size: 100000,
          category: 'lowCode'
        }
        // this.$http
        //   .get('/api/flow/bpmn/page', {
        //     params: params
        //   })
        request.getCscpFlowBpmnPageList({ params })
          .then((res) => {
            resolve(1)
          })
          .catch(() => {
            this.$Message.error('列表查询失败')
          })
      })
    },
    // 同步数据库表字段
    changeOrder (oldIndex, newIndex) {
      oldIndex = parseInt(oldIndex)
      newIndex = parseInt(newIndex)
      let oldData = this.dataArr[oldIndex]
      this.dataArr.splice(oldIndex, 1, this.dataArr[newIndex])
      this.dataArr.splice(newIndex, 1, oldData)
    },
    getFromFormColumns () {
      if (this.tableName) {
        const [url, param] = ['/api/cscpHxFormColums', {
          params: { tableName: this.tableName }
        }]
        this.$http.get(url, param).then((res) => {
          let formArr = res.data
          if (formArr.length > 0) {
            this.dataArr = []
            for (let i = 0; i < formArr.length; i++) {
              let map = {
                fieldName: formArr[i].columnName,
                fieldComment: formArr[i].columnComment ? formArr[i].columnComment : formArr[i].columnName,
                controlType: '0',
                queryType: null,
                fieldQuery: '0',
                fieldList: '0',
                isForm: '0',
                componentType: null,
                pageId: this.pageId,
                fieldType: formArr[i].dataTypeInt,
                tableName: formArr[i].tableName
              }
              this.dataArr.push(map)
            }
          }
          console.log(this.dataArr, 'this.dataArr111')
        }).catch(() => {
          this.$Notice.error({
            title: '操作失败！'
          })
        })
      }
    },
    getFormVfields () {
      return new Promise((resolve, reject) => {
        const url = `/api/lowcode/customize/cscpCustomizeVfields/getListByPageId/${this.pageId}`
        this.$http.get(url).then(res => {
          this.dataArr = res.data
          if (this.dataArr) {
            let dataArrData = this.dataArr
            this.$emit('ListConfigEmit', dataArrData)
          }
          const fields = res.data
          if (fields.length > 0) {
            this.dataArr = res.data
            if (true) {
              this.dataArr.map(item => {
                const temp = item.fieldName.split('.')
                if (temp.length === 2) {
                  item.tableName = temp[0]
                  item.fieldName = temp[1]
                }
                return item
              })
            }
            this.addOrEditFlag = 'edit'
          } else {
            this.addOrEditFlag = 'add'
          }
          resolve(res)
        }).catch(err => {
          this.$Notice.error({
            title: '操作失败！'
          })
          reject(err)
        })
      })
    },
    queryFormColumns () {
      Promise.all([this.getFormVfields()]).then(res => {
        // if (this.addOrEditFlag === 'add') {
        this.getFromFormColumns()
        // }
      })
    }
  }
}
</script>
<style lang="less" scoped>
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
