<template>
  <div style="margin-top: 16px">
    <el-form-item label="处理用户">
      <el-select v-model="userTaskForm.assignee" filterable allow-create default-first-option
                 clearable @change="updateElementTask('assignee')">
        <el-option v-for="ak in userOptions" :key="ak.value" :label="ak.label" :value="ak.value"/>
      </el-select>
    </el-form-item>
    <el-form-item label="候选用户">
      <el-select v-model="userTaskForm.candidateUsers" filterable allow-create default-first-option
                 clearable multiple collapse-tags @change="updateElementTask('candidateUsers')">
        <el-option v-for="uk in userOptions" :key="uk.value" :label="uk.label" :value="uk.value"/>
      </el-select>
    </el-form-item>
    <el-form-item label="候选分组">
      <el-select v-model="userTaskForm.candidateGroups" filterable allow-create default-first-option
                 clearable multiple collapse-tags @change="updateElementTask('candidateGroups')">
        <el-option v-for="gk in operaterole" :key="gk.value" :label="gk.label" :value="gk.value"/>
      </el-select>
    </el-form-item>
    <el-form-item label="到期时间">
      <el-input v-model="userTaskForm.dueDate" clearable @change="updateElementTask('dueDate')"/>
    </el-form-item>
    <el-form-item label="跟踪时间">
      <el-input v-model="userTaskForm.followUpDate" clearable @change="updateElementTask('followUpDate')"/>
    </el-form-item>
    <el-form-item label="优先级">
      <el-input v-model="userTaskForm.priority" clearable @change="updateElementTask('priority')"/>
    </el-form-item>
  </div>
</template>

<script>
  export default {
    name: "UserTask",
    props: {
      id: String,
      type: String
    },
    data() {
      return {
        defaultTaskForm: {
          assignee: "",
          candidateUsers: [],
          candidateGroups: [],
          dueDate: "",
          followUpDate: "",
          priority: ""
        },
        userTaskForm: {},
        userOptions: [],
        operaterole: [],
        // mockData: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
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
    created() {
      this.init()
    },
    methods: {
      resetTaskForm() {
        for (let key in this.defaultTaskForm) {
          let value;
          if (key === "candidateUsers" || key === "candidateGroups") {
            value = this.bpmnElement?.businessObject[key] ? this.bpmnElement.businessObject[key].split(",") : [];
          } else {
            value = this.bpmnElement?.businessObject[key] || this.defaultTaskForm[key];
          }
          this.$set(this.userTaskForm, key, value);
        }
      },
      updateElementTask(key) {
        const taskAttr = Object.create(null);
        if (key === "candidateUsers" || key === "candidateGroups") {
          taskAttr[key] = this.userTaskForm[key] && this.userTaskForm[key].length ? this.userTaskForm[key].join() : null;
        } else {
          taskAttr[key] = this.userTaskForm[key] || null;
        }
        window.bpmnInstances.modeling.updateProperties(this.bpmnElement, taskAttr);
      },
      init() {
        // 人员
        this.$http.get('api/system/flowUserTaskInfo').then(response => {
          console.log("response------------", response)
          const userOptions = []
          const roleOptions = []
          for (let item of response.data.users) {
            userOptions.push({
              value: item.id,
              label: `${item.username} `
            })
          }
          for (let item of response.data.roles) {
            roleOptions.push({
              value: item.id,
              label: `${item.name} `
            })
          }
          this.userOptions = userOptions;
          this.operaterole = roleOptions;
        }).catch()

      },
    },
    beforeDestroy() {
      this.bpmnElement = null;
    }
  };
</script>
