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
import { componentTypes, queryTypes, queryFieldTypes } from '../../config/types.js'
export default {
  name: 'listConfig',
  data () {
    return {
      loading: false,
      tableArr: [],
      tableName: null,
      dataArr: [],
      detail: {},
      pageId: this.formDetail.formId,
      // pageId: null,
      addOrEditFlag: 'add', // add/edit
      columns: this.formDetail.formType === '1' ? [
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
                value: params.row.fieldComment,
                disabled: this.editOrCreate === 'edit'
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
                value: params.row.isForm === '1',
                disabled: this.editOrCreate === 'edit'
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
                      clearable: true,
                      disabled: this.editOrCreate === 'edit'
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
        : [
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
                  value: params.row.fieldComment,
                  disabled: this.editOrCreate === 'edit'
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
            title: '是否列表展示',
            key: 'fieldList',
            align: 'center',
            render: (h, params) => {
              return h('Checkbox', {
                props: {
                  value: params.row.fieldList === '1',
                  disabled: this.editOrCreate === 'edit'
                },
                on: {
                  'on-change': event => {
                    this.dataArr[params.index].fieldList = event
                  }
                }
              })
            }
          },
          {
            title: '是否查询',
            key: 'fieldQuery',
            align: 'center',
            render: (h, params) => {
              return h('Checkbox', {
                props: {
                  value: params.row.fieldQuery === '1',
                  disabled: this.editOrCreate === 'edit'
                },
                on: {
                  'on-change': event => {
                    this.dataArr[params.index].fieldQuery = event
                  }
                }
              })
            }
          },
          {
            title: '查询字段类型',
            key: 'controlType',
            render: (h, params) => {
              return h(
                'div',
                [
                  h(
                    'Select',
                    {
                      props: {
                        transfer: true,
                        value: params.row.controlType,
                        clearable: true,
                        disabled: this.editOrCreate === 'edit'
                      },
                      style: {
                      // width: '200px',
                        border: ((params.row.fieldQuery === '1' || params.row.fieldQuery === true) && !params.row.controlType) ? '1px solid red' : ''
                      },
                      class: 'ctinpt',
                      on: {
                        'on-change': event => {
                          this.dataArr[params.index].controlType = event
                        }
                      }
                    },
                    [...queryFieldTypes].map(item => {
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
                        display: ((params.row.fieldQuery === '1' || params.row.fieldQuery === true) && !params.row.controlType) ? 'block' : 'none'
                      },
                      class: 'cxname'
                    },
                    [
                      h('Font', { style: { color: 'red' } }, '请选择查询字段类型！'),
                      h('Icon', { props: { type: '' } })
                    ]
                  )
                ]
              )
            }
          },
          {
            title: '查询类型',
            key: 'queryType',
            render: (h, params) => {
              return h(
                'div',
                [
                  h(
                    'Select',
                    {
                      props: {
                        transfer: true,
                        value: params.row.queryType,
                        clearable: true,
                        disabled: this.editOrCreate === 'edit'
                      },
                      style: {
                      // width: '200px',
                        border: ((params.row.fieldQuery === '1' || params.row.fieldQuery === true) && !params.row.queryType) ? '1px solid red' : ''
                      },
                      class: 'ctinpt',
                      on: {
                        'on-change': event => {
                          this.dataArr[params.index].queryType = event
                        }
                      }
                    },
                    [...queryTypes].map(item => {
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
                        display: ((params.row.fieldQuery === '1' || params.row.fieldQuery === true) && !params.row.queryType) ? 'block' : 'none'
                      },
                      class: 'cxname'
                    },
                    [
                      h('Font', { style: { color: 'red' } }, '请选择查询类型！'),
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
    formDetail: Object,
    editOrCreate: String
  },
  methods: {
    // 同步数据库表字段
    syncDb () {
      this.loading = true
      if (this.tableName) {
        const [url, param] = ['/api/cscpHxFormColums', {
          params: { tableName: this.tableName }
        }]
        this.$http.get(url, param).then((res) => {
          let formArr = res.data
          if (formArr.length > 0) {
            formArr.forEach((item1, i) => {
              this.dataArr.forEach(item2 => {
                if (item2.fieldName == item1.columnName) {
                  delete formArr[i]
                }
              })
            })
            const newFields = []
            formArr.forEach(newField => {
              let map = {
                fieldName: newField.columnName,
                fieldComment: newField.columnComment ? newField.columnComment : newField.columnName,
                controlType: '0',
                queryType: null,
                fieldQuery: '0',
                fieldList: '0',
                isForm: '0',
                componentType: null,
                pageId: this.formDetail.pageId,
                fieldType: newField.dataTypeInt
              }
              this.dataArr.push(map)
              newFields.push(map)
            })
            this.addFields(newFields)
          }
        }).catch(() => {
          this.loading = false
          this.$Notice.error({
            title: '操作失败！'
          })
        })
      }
    },
    // 添加新增字段
    addFields (newFields) {
      this.loading = false
      if (newFields.length == 0) {
        this.$Message.success(`添加新增字段成功`)
        return
      }
      let url = '/api/lowcode/customize/cscpCustomizeVfields/save'
      this.$util.http.post(url, newFields).then(res => {
        this.$Message.success(`添加新增字段成功`)
      }).catch(() => {
        this.$Message.error(`添加新增字段失败`)
      })
    },
    changeOrder (oldIndex, newIndex) {
      oldIndex = parseInt(oldIndex)
      newIndex = parseInt(newIndex)
      let oldData = this.dataArr[oldIndex]
      this.dataArr.splice(oldIndex, 1, this.dataArr[newIndex])
      this.dataArr.splice(newIndex, 1, oldData)
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
    getAllFormColumns () {
      if (this.formDetail.tableType === '1') {
        let mainTableName
        let subTableNames
        if (typeof this.tableName === 'string') {
          mainTableName = this.tableName
          subTableNames = []
        } else if (typeof this.tableName === 'object') {
          mainTableName = this.tableName[0]
          subTableNames = this.tableName.slice(1) || []
        }
        const [url, param] = ['/api/cscpHxFormColums', {
          params: { tableName: mainTableName }
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
                pageId: this.formDetail.formId,
                fieldType: formArr[i].dataTypeInt,
                // 这里应该需要加一个tableName参数,来自于查询字段自带
                tableName: formArr[i].tableName
              }
              this.dataArr.push(map)
            }
            if (this.dataArr) {
              let dataArrData = this.dataArr
              this.$emit('ListConfigEmit', dataArrData)
            }
          }
          // 再根据子表的表名获取子表字段
          if (subTableNames.length > 0) {
            let promiseArr = []
            for (let i = 0; i < subTableNames.length; i++) {
              const [url, param] = ['/api/cscpHxFormColums', {
                params: { tableName: subTableNames[i] }
              }]
              promiseArr.push(this.$http.get(url, param))
            }
            Promise.all(promiseArr).then(res => {
              // 将返回值res(是个数组)根据subTableNames进行双层遍历;同时筛选掉主表已包含的字段;根据tableName的不同,将筛选的字段push到dataArr里面
              let tempArr = []
              res.map(item => {
                for (let i = 0; i < subTableNames.length; i++) {
                  if (item.config.params.tableName === subTableNames[i]) {
                    item.data = item.data.map(temp => {
                      const map = {
                        fieldName: temp.columnName,
                        fieldComment: temp.columnComment ? temp.columnComment : temp.columnName,
                        controlType: '0',
                        queryType: null,
                        fieldQuery: '0',
                        fieldList: '0',
                        isForm: '0',
                        componentType: null,
                        pageId: this.formDetail.formId,
                        fieldType: temp.dataTypeInt,
                        // 这里应该需要加一个tableName参数,来自于查询字段自带
                        tableName: temp.tableName
                      }
                      return map
                    })

                    for (let i = 0; i < item.data.length; i++) {
                      const temp = item.data[i]
                      let tempRes = this.dataArr.filter(item => item.fieldName === temp.fieldName)
                      if (tempRes.length <= 0) {
                        tempArr.push(temp)
                      }
                    }
                  }
                }
              })
              this.dataArr.push(...tempArr)
            }).catch(() => {
              this.$Notice.error({
                title: '获取子表字段失败！'
              })
            })
          }
        }).catch(() => {
          this.$Notice.error({
            title: '操作失败！'
          })
        })
      } else {
        this.getFromFormColumns()
      }
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
                pageId: this.formDetail.formId,
                fieldType: formArr[i].dataTypeInt,
                tableName: formArr[i].tableName
              }
              this.dataArr.push(map)
            }
          }
          if (this.dataArr) {
            let dataArrData = this.dataArr
            this.$emit('ListConfigEmit', dataArrData)
          }
        }).catch(() => {
          this.$Notice.error({
            title: '操作失败！'
          })
        })
      }
    },
    queryFormColumns () {
      Promise.all([this.getFormVfields()]).then(res => {
        if (this.addOrEditFlag === 'add') {
          this.getAllFormColumns()
        }
      })
    }
  },
  created () {
    this.pageId = this.formDetail.formId
    this.tableName = this.formDetail.formTable
    // if (this.formDetail.pageTable.split(',').length > 1) {
    //   this.tableName = this.formDetail.pageTable.split(',')
    
    // }
    this.queryFormColumns()
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
