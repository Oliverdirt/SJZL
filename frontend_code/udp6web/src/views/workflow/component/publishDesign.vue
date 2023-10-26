<template></template>
<script>
import request from '@/api/workflow/request'

export default {
  data() {
    return {
      page: 1,
      size: 10,
    };
  },
  created() {
    this.queryList();
  },
  methods: {
    queryList() {
      let params = {
        page: 1,
        size: 100000,
      };
      // this.$http
      //   .get("/api/flow/bpmn/page", {
      //     params: params,
      //   })
      request.getCscpFlowBpmnPageList({
          params: params,
        })
        .then((res) => {
          this.id = res.data.result.list[0].id;
          const row = this.id 
          this.publishDesign(row);
        })
        .catch(() => {
          this.$Message.error("列表查询失败");
        });
    },
    publishDesign(row) {
      this.$Modal.confirm({
        title: "提示",
        content: "<p>是否发布该流程！</p>",
        onOk: () => {
          // this.$http
          //   .post("/api/flow/bpmn/deploy?id=" + row)
          request.publishBpmnById(row)
            .then((res) => {
              this.$Message.success("发布成功");
            })
            .catch((error) => {
              this.$Message.error("发布失败:" + error.response.data.detail);
            });
        },
        onCancel: () => {
            this.$emit('cancelNextStep')
        },
      });
    },
  },
};
</script>
<style scoped></style>
