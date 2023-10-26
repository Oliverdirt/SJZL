<template>
  <div class="todo">
      <div v-for="(item,index2) of todoList" :key="index2">
        <div class="todoList">
          <div class="todoListLeft">
            <img v-if="imgData" :src="imgData" alt="" style="width:40px;height:40px;background: #619fe7;margin: 5px 10px 0px 0px;border-radius:50%;">
            <div style="margin-left: 20px">
              <p style="font-weight: bold">请审批【{{item.processInstance.name}}】流程</p>
              <p style="color: rgba(0,0,0,0.4)">流程节点: <span style="color: grey">{{ item.name }}</span></p>
            </div>
          </div>
          <div class="todoListRight">
            <el-button v-if="item.claimFalg === 1" type="text" @click="claimTask(item)">签收</el-button>
            <el-button v-else type="text" @click="processDetail(item)">审批</el-button>
            <p style="color: rgba(0,0,0,0.4)">{{  item.createTime }}</p>
          </div>
        </div>
      </div>
      <div class="pageFn">
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
  </div>
</template>
<script>
import request from '@/api/workflow/request'

export default {
  props: {
    imgData: String
  },
  data () {
    return {
      todoList: [],
      pageData: {
        total: 0,
        pageSize: 5,
        page: 0,
        pageNum: 1,
      },
    }
  },
  created () {
    this.todoFn()
  },
  methods: {
    todoFn () {
      let params = {
        size: this.pageData.pageSize,
        page: this.pageData.pageNum,
      }
      // this.$http
      //   .get('/api/flow/core/todo-page', { params: params })
      request.getTodoPageList({ params: params })
        .then((response) => {
          this.todoList = response.data.result?.list
          this.pageData.total = response.data.result?.total
        })
    },
    // 流程处理
    processDetail (row) {
      let json = {
        formId: row.formId,
        processInstanceId: row.processInstance.id,
        processDefinitionId: row.processInstance.processDefinitionId,
        parentProcessInstanceId: row.processInstance.parentProcessInstanceId,
        processName: row.processInstance.name,
        createTime: row.createTime,
        taskCode: row.taskCode,
        type: '流程处理',
        id: row.id,
        processDefinitionKey: row.processDefinitionKey
      }
      this.$emit('listenClickDetail', json)
    },
    // 签收任务
    claimTask (row) {
      let taskId = row.id
      // this.$http.post('/api/flow/core/claim/' + taskId)
      request.postClaimTask(taskId)
        .then(res => {
        this.$Message.success('签收成功！')
        this.todoFn()
      }).catch(err => {
        this.$Message.error('签收失败!' + err)
      })
    },
    // 分页
    handleSizeChange (val) {
      this.pageData.pageSize = val
      this.todoFn()
    },
    handleCurrentChange (val) {
      this.pageData.pageNum = val
      this.todoFn()
    },
  },
}
</script>
<style lang="less" scoped>
@import "../../admin/styles/formStyle.less";
.todoList{
  font-size: 14px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 65px;
  .todoListLeft{
    display: flex;
    justify-content: space-between;
    p{
      line-height: 25px;
    }
  }
  .todoListRight{
    display: flex;
    flex-direction: column;
    align-items: flex-end;
  }
}
.el-pagination {
  width: 100%;
  white-space: normal !important
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
.todo{
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
