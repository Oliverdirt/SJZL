<template>
  <div style="margin-top: 16px">
    <el-form-item label="配置流程ID">
      <el-input v-model="callActivityForm.calledElement" clearable @change="updateElementTask('calledElement')"/>
      <p style="color: #606266;font-size: 12px;">友情提示: 请确保配置流程与主流程绑定表单一致</p>
    </el-form-item>
  </div>
</template>

<script>
export default {
  name: "CallActivity",
  props: {
    id: String,
    type: String
  },
  data() {
    return {
      defaultCallActivity: {
        calledElement: ""
      },
      callActivityForm: {
        calledElement: ""
      }
    };
  },
  watch: {
    id: {
      immediate: true,
      handler() {
        this.bpmnElement = window.bpmnInstances.bpmnElement;
        this.$nextTick(() => this.resetTaskForm());
      }
    }
  },
  methods: {
    // 修改渲染流程图
    resetTaskForm() {
      for (let key in this.defaultCallActivity) {
        let value;
        if (key === "calledElement") {
          value = this.bpmnElement?.businessObject[key] ? this.bpmnElement.businessObject[key] : null;
        } else {
          value = this.bpmnElement?.businessObject[key] || this.defaultCallActivity[key];
        }
        this.$set(this.callActivityForm, key, value);
      }
    },

    updateElementTask(key) {
      const taskAttr = Object.create(null);
      if (key === "calledElement") {
        taskAttr[key] = this.callActivityForm[key] && this.callActivityForm[key].length ? this.callActivityForm[key]: null;
      } else {
        taskAttr[key] = this.callActivityForm[key] || null;
      }
      window.bpmnInstances.modeling.updateProperties(this.bpmnElement, taskAttr);
    },
  },
  beforeDestroy() {
    this.bpmnElement = null;
  }
};
</script>
