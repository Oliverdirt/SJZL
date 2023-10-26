<template>
  <div>
    <el-form ref="formData" :model="form" label-width="80px" :rules="rules">
      <el-form-item label="流程标识" prop="key">
        <el-input
          v-model="form.key"
          placeholder="请输入流标标识"
        />
      </el-form-item>
      <el-form-item label="流程名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入流程名称" clearable />
      </el-form-item>
      <el-form-item label="流程描述" prop="description">
        <el-input v-model="form.description" type="textarea" clearable />
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import request from '@/api/workflow/request'

export default {
  data() {
    return {
      form: {},
      page: 0,
      size: 10,
      modelData: {
        modelKey: '',
        modelName: ''
      },
      // 表单校验
      rules: {
        key: [{ required: true, message: "流程标识不能为空", trigger: "blur" }],
        name: [
          { required: true, message: "流程名称不能为空", trigger: "blur" },
        ],
      },
    };
  },
  methods: {

    queryList () {
      let params =  {
        page: 1,
        size: 10000
      }
      // this.$http
      //   .get('/api/flow/bpmn/page', {
      //     params: params
      //   })
      request.getCscpFlowBpmnPageList({ params: params })
        .then((res) => {
          this.tableData = res.data.result.list[0]
        })
        .catch(() => {
          this.$Message.error('列表查询失败')
        })
    },
     save() {
      return new Promise((resolve)=>{
        this.$refs.formData.validate((valid) => {
        if (!valid) return;
        let copyDetail = JSON.parse(JSON.stringify(this.form));
        // this.$http
        //   .post("/api/flow/bpmn/create", copyDetail)
        request.createBpmn(copyDetail)
          .then((res) => {
            this.$Message.success("新建模型成功");
           resolve(1)
          })
          .catch((error) => {
            this.$Message.error("新建模型失败:" + error.response.data.detail);
          });
      });
      
      })
    },
  },
};
</script>
<style scoped></style>
