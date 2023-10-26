<template>
  <div class="done">
      <div v-for="(item,index2) of todoList" :key="index2">
        <div class="doneList">
          <div style="width: 35%;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
            <span style="padding: 10px;background-color: #e8f4ff;border-radius: 15px;line-height: 32px;" :title="item.processInstance.name">{{item.processInstance.name}}</span>
          </div>
          <div style="margin-left: 30px;width: 35%;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
            <p>流程节点</p>
            <span :title="item.name">{{ item.name }}</span>
          </div>
          <div class="doneListRight">
            <el-button style="padding: 5px" type="text" @click="processDetail(item)">流程详情</el-button>
            <p style="color: rgba(0,0,0,0.4) ">{{  item.createTime }}</p>
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
      todoList: [],
      pageData: {
        total: 0,
        pageSize: 5,
        pageNum: 1
      }
    }
  },
  created () {
    this.doneFn()
  },
  methods: {
    doneFn () {
      let params = {
        size: this.pageData.pageSize,
        page: this.pageData.pageNum
      }
      // this.$http
      //   .get('/api/flow/core/done-page', { params: params })
      request.getDonePageList({ params: params })
        .then((response) => {
          this.todoList = response.data.result.list
          this.pageData.total = response.data.result.total
        })
    },
    // 流程详情
    processDetail (row) {
      let json = {
        formId: row.formId,
        processInstanceId: row.processInstance.id,
        processDefinitionId: row.processInstance.processDefinitionId,
        parentProcessInstanceId: row.processInstance.parentProcessInstanceId,
        type: '流程详情'
      }
      this.$emit('listenClickDetail', json)
    },
    // 分页
    handleSizeChange (val) {
      this.pageData.pageSize = val
      this.doneFn()
    },
    handleCurrentChange (val) {
      this.pageData.pageNum = val
      this.doneFn()
    }
  }
}
</script>
<style lang="less" scoped>
  @import "../../admin/styles/formStyle.less";
  .doneList{
    display: flex;
    align-items: center;
    height: 60px;
    width: 100%;
      p:nth-child(1){
        font-weight: bold;
      }
      span{
        color: rgba(0,0,0,0.4);
      }
    .doneListRight{
      width: 30%;
      display: flex;
      flex-direction: column;
      align-items: flex-end;
  }
  }
  .el-pagination {
    width: 100%;
    // margin-top: 30px;
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
.done{
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
