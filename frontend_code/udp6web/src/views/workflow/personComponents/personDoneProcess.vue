<template>
  <div class="doneProcess">
    <div v-for="(item,index2) of doneProcessList" :key="index2">
        <div class="doneProcessList">
          <div style="width: 35%;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
            <span style="padding: 10px;background-color: #e8f4ff;border-radius: 15px;line-height: 32px;" :title="item.name">{{item.name}}</span>
          </div>
          <p style="color: rgba(0,0,0,0.4);width: 35%;margin-left: 20px;">{{  item.createTime }}</p>
          <div class="listRight">
            <el-button type="text" @click="processDetail(item)">流程详情</el-button>
            <el-button type="text" v-if="item.back" @click="processEnd(item)">终止</el-button>
          </div>
        </div>
    </div>
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
      doneProcessList: [],
      pageData: {
        total: 0,
        pageSize: 5,
        pageNum: 1
      }
    }
  },
  created () {
    this.doneProcessFn()
  },
  methods: {
    doneProcessFn () {
      let params = {
        size: this.pageData.pageSize,
        page: this.pageData.pageNum
      }
      // this.$http
      //   .get('/api/flow/core/my-process', { params: params })
      request.getMyProcessList({ params: params })
        .then((response) => {
          this.doneProcessList = response.data.result.list
          this.pageData.total = response.data.result.total
        })
    },
    // 终止
    processEnd (row) {
      this.$confirm('您确定要终止此流程吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = { processInstanceId: row.processInstanceId }
        // this.$http.get('/api/flow/core/endProcess', { params })
        request.getEndProcess({ params })
          .then((res) => {
            if (res.data.success) {
              this.$message.success('终止成功！')
              this.doneProcessFn()
            } else {
              this.$message.error('服务器错误！！')
            }
          })
      })
    },
    // 流程详情
    processDetail (row) {
      let json = {
        formId: row.formId,
        processInstanceId: row.processInstanceId,
        processDefinitionId: row.processDeployId,
        type: '流程详情'
      }
      this.$emit('listenClickDetail', json)
    },
    // 分页
    handleSizeChange (val) {
      this.pageData.pageSize = val
      this.doneProcessFn()
    },
    handleCurrentChange (val) {
      this.pageData.pageNum = val
      this.doneProcessFn()
    }
  }
}
</script>
<style lang="less" scoped>
@import "../../admin/styles/formStyle.less";
.doneProcessList{
  display: flex;
  align-items: center;
  height: 50px;
  .listRight{
    width: 30%;
    display: flex;
    justify-content: flex-end;
  }
}
.el-pagination {
  width: 100%;
  white-space: normal !important
}

el-card {
  margin-bottom: 100px;
}

::v-deep .el-pagination__total {
  margin: 0px;
}

::v-deep .el-pagination__jump {
  margin: 0px;
}

::v-deep .el-table__fixed-right::before {
  height: 0px;
}

.doneProcess {
  animation: tabs 0.6s linear
}

@keyframes tabs {
  from {
    transform: translateX(-500px);
  }
  to {
    transform: translateX(0px);
  }
}
</style>
