<template>
  <div class="transfer" >
      <div v-for="(item,index2) of transferList" :key="index2">
        <div class="transferList">
          <div>
            <img v-if="imgData" :src="imgData" alt="" style="width:40px;height:40px;background: #619fe7;border-radius:50%;">
          </div>
          <div style="width: 20%;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
            <p>流程名称</p>
            <span :title="item.processName">{{ item.processName }}</span>
          </div>
          <div style="width: 20%;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
            <p>上一位处理人</p>
            <span :title="item.lastAssigneeName">{{ item.lastAssigneeName }}</span>
          </div>
          <div style="width: 20%;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
            <p>当前处理人</p>
            <span :title="item.curAssigneeName">{{ item.curAssigneeName }}</span>
          </div>
          <div style="width: 20%;display: flex;justify-content: flex-end;">
            <span style="color: rgba(0,0,0,0.4) ">{{  item.createTime }}</span>
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
  props: {
    imgData: String
  },
  data () {
    return {
      transferList: [],
      pageData: {
        total: 0,
        pageSize: 10,
        pageNum: 1
      }
    }
  },
  created () {
    this.transferFn()
  },
  methods: {
    transferFn () {
      let params = {
        page: this.pageData.pageNum,
        size: this.pageData.pageSize,
        type: 2
      }

      // this.$http
      //   .get('/api/flow/door/rdTurnTask-select', {
      //     params: params
      //   })
      request.getSelectTaskList({ params: params })
        .then((res) => {
          this.transferList = res.data.result.list
          this.pageData.total = res.data.result.total
        })
        .catch(() => {
          this.$Message.error('列表查询失败')
        })
    },
    // 分页
    handleSizeChange (val) {
      this.pageData.pageSize = val
      this.transferFn()
    },
    handleCurrentChange (val) {
      this.pageData.pageNum = val
      this.transferFn()
    }
  }
}
</script>
<style lang="less" scoped>
  @import "../../admin/styles/formStyle.less";
  .transferList{
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 65px;
    font-size: 14px;
    width: 100%;
    p:nth-child(1), p:nth-child(3), p:nth-child(5){
      font-weight: bold;
    }
    span:nth-child(2), span:nth-child(4), span:nth-child(6){
      color: rgba(0,0,0,0.4)
    }
    p{
      line-height: 25px;
    }
  }
  .el-pagination {
    width: 100%;
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
.transfer{
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
