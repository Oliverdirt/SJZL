<style lang="less">
@import "../../styles/common.less";
@import "table.less";
</style>

<template>
  <div  style="height: 100%">
    <div class="formCard-content">
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <div class="searchArea" >
          <Form>
            <Row :gutter="48">
              <Col span="8">
                <FormItem label=""  prop="tenantAccount">
                  <Input
                    v-model="searchUsername"
                    placeholder="查询用户名"
                    style=""
                  />
                </FormItem>
              </Col>
              <Col span="16" style="text-align: right;">
                <FormItem>
                  <Button @click="handleSearch" type="primary" icon="md-search" style="margin-right:10px"
                  >查询</Button
                  >
                  <Button @click="handleCancel" type="default">重置</Button>
                </FormItem>
              </Col>
            </Row>
          </Form>
        </div>
      </Card>
      <Card :bordered="false" :dis-hover="true" :shadow="false" class="formCard">
        <Table :columns="columns" :data="dataList" border>
          <template slot-scope="{ row }" slot="errorSlot">
            <Tooltip :content="row.error" placement="left-start">
              <span>{{row.error?row.error.substring(0,20)+'...':''}}</span>
            </Tooltip>
          </template>
          <template slot-scope="{ row }" slot="paramsSlot" >
            <Tooltip :content="row.params" placement="left-start">
              <span>{{row.params?row.params.substring(0,20)+'...':''}}</span>
            </Tooltip>
          </template>
        </Table>
        <Page
          :total="dataCount"
          :page-size="pageSize"
          @on-change="handlePageChange"
          show-elevator
          show-total
          style="padding: 10px;"
          :current="currentPageNum"
        ></Page>
      </Card>
    </div>
  </div>
</template>

<script>
import {
  getLogOperationsByParams
} from '@/api/admin/log'
export default {
  name: 'searchable-table',
  data () {
    return {
      dataCount: 0,
      currentPageNum: 1,
      pageSize: 13,
      columns: [
        // {
        //   key: 'id',
        //   title: 'ID'
        // },
        {
          key: 'username',
          title: '用户'
        },
        {
          key: 'uri',
          title: '访问路径'
        },
        {
          key: 'ip',
          title: 'IP'
        },
        {
          key: 'time',
          title: '时间'
        },
        {
          key: 'params',
          title: '参数',
          slot: 'paramsSlot'
        },
        {
          key: 'message',
          title: '操作信息'
        },
        {
          key: 'error',
          title: '错误信息',
          slot: 'errorSlot'
        },
        {
          key: 'status',
          title: '状态'
        }
      ],
      searchUsername: '',
      dataList: []
    }
  },
  methods: {
    init () {
      this.currentPageNum = 1
      this.search({
        page: this.currentPageNum - 1,
        size: this.pageSize,
        'username.contains': this.searchUsername,
        sort: 'time,desc'
      })
    },
    search (argumentObj) {
      getLogOperationsByParams(argumentObj)
        .then(response => {
          this.dataList = response.data.data
          this.dataCount = response.data.recordsTotal
        }).catch()
    },
    handleSearch () {
      this.init()
    },
    handleCancel () {
      this.searchUsername = ''
      this.init()
    },
    handlePageChange (pageNum) {
      this.currentPageNum = pageNum
      this.search({
        page: this.currentPageNum - 1,
        size: this.pageSize,
        'username.contains': this.searchUsername,
        sort: 'time,desc'
      })
    }
  },
  created () {
    this.init()
  }
}
</script>
<style lang="less" scoped>
@import "../../../admin/styles/formStyle.less";
.btn-group {
  float: right;
}
  ::v-deep .ivu-tooltip-inner {
    max-width: 100%;
    min-height: 34px;
    padding: 8px 12px;
    color: #fff;
    text-align: left;
    text-decoration: none;
    background-color: rgba(70,76,91,.9);
    border-radius: 4px;
    box-shadow: 0 1px 6px rgb(0 0 0 / 20%);
    white-space: normal;
  }
</style>
