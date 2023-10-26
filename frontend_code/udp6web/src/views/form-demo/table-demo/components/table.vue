<style lang='less'>
@import '../../../../assets/iconfont/iconfont.css';
.table{
  padding: 10px;
  .table-action{
    margin-bottom: 20px;
    button{
      margin-right: 10px;
    }
  }
  .circle{
  display: inline-block;
  width: 6px;
  height: 6px;
  border-radius: 3px;
  background-color: #52c41a;
  position: relative;
  top: -1px;
  margin-right: 5px;
  }
  .ivu-table-header thead tr th{
      background-color: #F0F9FE;
  }
  .table-action-btns {
    display: flex;
    justify-content: center;
    .table-action-btn {
      color: #2d8cf0;
      cursor: pointer;
    }
    .table-action-divider {
      width: 1px;
      background-color: #2d8cf0;
      margin: 0 10px;
    }
    .table-action-btn:hover {
      color: #5cadff;
    }
  }
}
</style>
<template>
  <div class='table'>
    <div class="table-action">
      <Button type="primary" @click="handleSelectAll(true)">设置全选</Button>
      <Button @click="handleSelectAll(false)">取消选择</Button>
    </div>
    <Table border ref='selection' :columns='columns' :data='data'></Table>
  </div>
</template>
<script>
export default {
  data () {
    return {
      columns: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '规则名称',
          width: 150,
          key: 'name',
          align: 'center'
        },
        {
          title: '描述',
          key: 'explain',
          align: 'center'
        },
        {
          title: '服务调用次数',
          key: 'times',
          sortable: true,
          align: 'center'
        },
        {
          title: '状态一',
          key: 'state',
          width: 150,
          align: 'center',
          filters: [
            {
              label: '已上线',
              value: 1
            },
            {
              label: '异常',
              value: 2
            }
          ],
          filterMultiple: false,
          filterMethod (value, row) {
            if (value === 1) {
              return row.state === 1
            } else if (value === 2) {
              return row.state === 0
            }
          },
          render: (h, params) => {
            return h('div', {
              style: {
                color: params.row.state === 0 ? 'red' : params.row.state === 1 ? '#52c41a' : params.row.state === 2 ? '#2D8CF0' : '#c5c8ce'
              }
            },
            params.row.state === 0 ? '异常' : params.row.state === 1 ? '已上线' : params.row.state === 2 ? '运行中' : '关闭'
            )
          }
        },
        {
          title: '状态二',
          width: 150,
          key: 'state',
          filters: [
            {
              label: '已上线',
              value: 1
            },
            {
              label: '异常',
              value: 2
            }
          ],
          filterMultiple: false,
          filterMethod (value, row) {
            if (value === 1) {
              return row.state === 1
            } else if (value === 2) {
              return row.state === 0
            }
          },
          render: (h, params) => {
            return h(
              'div', [
                h('span',
                  {
                    class: 'circle',
                    style: {
                      background: params.row.state === 0 ? 'red' : '#52c41a'
                    }
                  }
                ),
                h('span',
                  params.row.state === 0 ? '异常' : '已上线'
                )
              ]
            )
          }
        },
        {
          title: '上次调用时间',
          key: 'date',
          sortable: true,
          align: 'center'
        },
        {
          title: '操作一',
          align: 'center',
          render: (h, params) => {
            return h(
              'Row',
              {
                props: {
                  type: 'flex',
                  justify: 'center'
                }
              },
              [
                h(
                  'div',
                  {
                    style: {
                      float: 'left',
                      marginRight: '15px'
                    }
                  },
                  [
                    h('Icon', {
                      props: {
                        custom: 'iconfont icon-bianji'
                      },
                      style: {
                        fontSize: '22px',
                        color: '#2d8cf0',
                        cursor: 'pointer'
                      }
                    })
                  ]
                ),
                h(
                  'div',
                  {
                    style: {
                      float: 'left',
                      marginRight: '15px'
                    }
                  },
                  [
                    h('Icon', {
                      props: {
                        custom: 'iconfont icon-chakan'
                      },
                      style: {
                        fontSize: '22px',
                        color: '#2d8cf0',
                        cursor: 'pointer'
                      }
                    })
                  ]
                ),
                h(
                  'div',
                  {
                    style: {
                      float: 'left',
                      marginRight: '15px'
                    }
                  },
                  [
                    h('Icon', {
                      props: {
                        custom: 'iconfont icon-xiazai1'
                      },
                      style: {
                        fontSize: '22px',
                        color: '#c5c8ce',
                        cursor: 'pointer'
                      }
                    })
                  ]
                ),
                h(
                  'div',
                  {
                    style: {
                      float: 'left',
                      marginRight: '15px'
                    }
                  },
                  [
                    h('Icon', {
                      props: {
                        custom: 'iconfont icon-xiazai14'
                      },
                      style: {
                        fontSize: '22px',
                        color: '#ED4014',
                        cursor: 'point  er'
                      }
                    })
                  ]
                )
              ]
            )
          }
        },
        {
          title: '操作二',
          align: 'center',
          render: (h, params) => {
            return h('div', {
              class: ['table-action-btns']
            }, [
              h(
                'div',
                {
                  class: ['table-action-btn']
                },
                '编辑'
              ),
              h('div', { class: ['table-action-divider'] }),
              h(
                'div',
                {
                  class: ['table-action-btn']
                },
                '订阅报警'
              )
            ])
          }
        }
      ],
      data: [
        {
          name: 'z7fbFq',
          explain: '这里是一段描述',
          times: 28,
          state: 0,
          date: '2020-05-03 05:46:38'
        },
        {
          name: 'z7fbFq',
          explain: '这里是一段描述',
          times: 45,
          state: 1,
          date: '2020-05-03 05:46:38'
        },
        {
          name: 'z7fbFq',
          explain: '这里是一段描述',
          times: 30,
          state: 2,
          date: '2020-05-03 05:46:38'
        },
        {
          name: 'z7fbFq',
          explain: '这里是一段描述',
          times: 26,
          state: 3,
          date: '2020-05-03 05:46:38'
        },
        {
          name: 'z7fbFq',
          explain: '这里是一段描述',
          times: 28,
          state: 0,
          date: '2020-05-03 05:46:38'
        },
        {
          name: 'z7fbFq',
          explain: '这里是一段描述',
          times: 45,
          state: 1,
          date: '2020-05-03 05:46:38'
        },
        {
          name: 'z7fbFq',
          explain: '这里是一段描述',
          times: 30,
          state: 1,
          date: '2020-05-03 05:46:38'
        },
        {
          name: 'z7fbFq',
          explain: '这里是一段描述',
          times: 26,
          state: 1,
          date: '2020-05-03 05:46:38'
        }
      ]
    }
  },
  methods: {
    handleSelectAll (status) {
      this.$refs.selection.selectAll(status)
    }
  }
}
</script>
