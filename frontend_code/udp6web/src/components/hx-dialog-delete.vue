<template>
  <Modal v-model="visible_" width="495" :closable="false">
    <div class="delete">
      <img class="delete-icon" src="~@/assets/img/delete.png" alt="" />
      <div class="delete-content">
        <h3 class="title">确定要删除该项吗？</h3>
        <p class="content">删除后对应配置不可恢复</p>
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
      <Button
        type="primary"
        size="large"
        :loading="loading"
        @click="handleDelete"
        >确 定</Button
      >
      <Button size="large" @click="handleCancel">取 消</Button>
    </span>
  </Modal>
</template>
<script>
export default {
  name: "HxDeleteDialog", // 删除弹窗
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    loading: {
      type: Boolean,
      default: false,
    },
  },
  model: {
    prop: "visible",
    event: "on-change-visible",
  },
  computed: {
    visible_: {
      get() {
        return this.visible;
      },
      set(val) {
        this.$emit("on-change-visible", val);
      },
    },
  },
  methods: {
    // 确定删除
    handleDelete() {
      this.$emit("on-delete");
    },
    // 取消
    handleCancel() {
      this.visible_ = false;
    },
  },
};
</script>
<style lang="less" scoped>
.delete {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 28px 0 0;
  .delete-icon {
    width: 46px;
    height: 46px;
  }
  .delete-content {
    margin-left: 10px;
    .title {
      color: #333;
    }
    .content {
      color: #666;
    }
  }
}
.dialog-footer {
  display: inline-block;
  width: 100%;
  text-align: center;
}
/deep/ .ivu-modal-content {
  border-radius: 1px;
}
/deep/ .ivu-modal-footer {
  padding: 20px;
  border-top: none;
}
</style>
