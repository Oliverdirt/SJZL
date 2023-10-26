<template>
  <div class="circulate" >
      <el-table :data="circulateList" style="width: 100%" border>
        <el-table-column prop="processName" label="流程名称">
        </el-table-column>
        <el-table-column prop="createTime" label="时间" >
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right">
       <template slot-scope="scope">
          <el-button  type="text" @click="processDetail(scope.row)">流程详情</el-button>
         </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageData.pageNum"
        :page-sizes="[5, 10, 15, 30]"
        :page-size="5"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageData.total"
        :pagerCount="5"
      >
      </el-pagination>
  </div>
</template>
<script>
import request from '@/api/workflow/request'

export default {
  data () {
    return {
      circulateList: [],
      pageData: {
        total: 0,
        pageSize: 5,
        pageNum: 1
      }
    }
  },
  created () {
    this.circulateFn()
  },
  methods: {
    // 流程详情
    processDetail (row) {
      let json = {
        formId: row.formId,
        processInstanceId: row.processInstid,
        processDefinitionId: row.processId,
        type: '流程详情'
      }
      this.$emit('listenClickDetail', json)
    },
    circulateFn () {
      let params = {
        page: this.pageData.pageNum,
        size: this.pageData.pageSize,
        type: 1
      }

      // this.$http
      //   .get('/api/flow/door/rdTurnTask-select', {
      //     params: params
      //   })
      request.getSelectTaskList({ params: params })
        .then((res) => {
          this.circulateList = res.data.result.list
          this.pageData.total = res.data.result.total
        })
        .catch(() => {
          this.$Message.error('列表查询失败')
        })
    },
    // 分页
    handleSizeChange (val) {
      this.pageData.pageSize = val
      this.circulateFn()
    },
    handleCurrentChange (val) {
      this.pageData.pageNum = val
      this.circulateFn()
    }
  }
}
</script>
<style lang="less" scoped>
  @import "../../admin/styles/formStyle.less";
  .el-pagination {
    width: 100%;
    margin-top: 30px;
    white-space: normal !important
 }
el-card{
  margin-bottom: 100px;
}
 ::v-deep .el-pagination__total{
   margin: 0px;
 }
 ::v-deep .el-pagination__jump{
   margin: 0px;
 }

 ::v-deep .el-table__fixed-right::before{
  height: 0px;
}
.circulate{
  animation:tabs 0.6s linear
}
@keyframes tabs {
  from{
    transform: translateX(-500px);
  }to{
    transform: translateX(0px);
  }
}
</style>
