<style lang="less">
@import '../../styles/common.less';
@import 'table.less';
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
                    v-model="searchUserName"
                    placeholder="查询用户名"
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
        <Table :columns="columns" :data="dataList" border></Table>
        <Page
          :total="dataCount"
          :page-size="pageSize"
          @on-change="handlePageChange"
          show-elevator
          show-total
          :current="currentPageNum"
          style="margin-top: 20px;"
        ></Page>
      </Card>
    </div>
  </div>
</template>

<script>
import {
  getLogLoginsByParams
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
          key: 'userName',
          title: '用户名'
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
          key: 'message',
          title: '操作信息'
        },
        {
          key: 'status',
          title: '状态'
        }
      ],
      searchUserName: '',
      dataList: []
    }
  },
  methods: {
    init () {
      this.search({
        size: this.pageSize,
        page: this.currentPageNum - 1,
        'userName.contains': this.searchUserName,
        sort: 'time,desc'
      })
    },
    search (argumentObj) {
      // this.$http
      //   .get('/api/system/cscpLogLoginsByCriteria', { params: argumentObj })
      getLogLoginsByParams(argumentObj)
        .then((response) => {
          this.dataList = response.data.data
          this.dataCount = response.data.recordsTotal
        })
        .catch()
    },
    handleSearch () {
      this.currentPageNum = 1
      this.search({
        size: this.pageSize,
        page: this.currentPageNum - 1,
        'userName.contains': this.searchUserName,
        sort: 'time,desc'
      })
    },
    handleCancel () {
      this.currentPageNum = 1
      this.searchUserName = ''
      this.init()
    },
    handlePageChange (pageNum) {
      this.currentPageNum = pageNum
      this.search({
        size: this.pageSize,
        page: this.currentPageNum - 1,
        'userName.contains': this.searchUserName,
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
</style>
