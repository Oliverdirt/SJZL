<template>
  <el-dialog title="新建模块" :visible.sync="dialogFormVisible" @close="cancel">
    <el-form :model="form" :rules="rules" ref="formRef">
      <el-form-item label="模块名称" :label-width="formLabelWidth" prop="name">
        <el-input v-model="form.name" autocomplete="off" placeholder="请输入模块名称"></el-input>
      </el-form-item>
      <el-form-item label="模块标识" :label-width="formLabelWidth" prop="id">
        <el-input v-model="form.id" autocomplete="off" placeholder="请输入模块标识"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="cancel">取 消</el-button>
      <el-button type="primary" @click="confirm">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  props: ['addModuleDialogVisible'],
  watch: {
    addModuleDialogVisible(newVal) {
      this.dialogFormVisible = newVal;
    }
  },
  data() {
    return {
      dialogFormVisible: false,
      form: {
        name: '',
        id: '',
      },
      formLabelWidth: '120px',
      rules: {
        name: [
          { required: true, message: '模块名称不能为空', trigger: 'blur' },
        ],
        id: [
          { required: true, message: '模块标识不能为空', trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    cancel() {
      this.$refs.formRef.resetFields();
      this.$parent.addModuleDialogVisible = false;
    },
    confirm() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          const params = {
            name: this.form.name,
            id: this.form.id,
          }
          this.$http.post('/xxx', params).then(res => {
            if (res) {
              this.cancel();
              this.$parent.getTreeData();
            }
          }).catch(() => {
            this.$message.error('新建模块失败!')
          })
        }
      })
    },
  },
}
</script>

<style>
</style>