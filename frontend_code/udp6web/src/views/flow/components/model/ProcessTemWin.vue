<template>
    <div>
        <Modal
                v-model="modalVisible"
                title="模型属性"
                :mask-closable="false"
                :closable = "false"
                @on-visible-change="change">
            <div style="padding: 20px 30px 20px 0;">
                <Form ref="formValidate" :model="process" :label-width="100" :rules="ruleValidate">
                    <FormItem label="模型KEY" prop="processDefinitionKey">
                        <Input v-model="process.processDefinitionKey" :maxlength="50" ></Input>
                    </FormItem>
                    <FormItem label="模型名称" prop="name">
                        <Input v-model="process.name" :maxlength="50"></Input>
                    </FormItem>
                </Form>
            </div>
            <div slot="footer">
                <Button size="large" @click="cancel">返回</Button>
                <Button type="primary" size="large" @click="ok">确定</Button>
            </div>
        </Modal>
    </div>

</template>

<script>

export default {
  name: 'ProcessTemWin',
  data () {
    return {
      modalVisible: false,
      process: {
        processDefinitionKey: null,
        name: null
      },
      ruleValidate: {
        processDefinitionKey: [
          { required: true, message: '模型KEY不能为空', trigger: 'blur' },
          {
            type: 'string',
            // pattern:  /^[A-Za-z0-9_]+$/,
            pattern: /^[A-Za-z_][A-Za-z0-9_]+$/,
            message: '只允许字母、数字和下划线组合，且不能以数字开头',
            trigger: 'blur'
          }
        ],
        name: [
          { required: true, message: '模型名称不能为空', trigger: 'blur' },
          {
            type: 'string',
            pattern: /^[A-Za-z0-9_\u4e00-\u9fa5]+$/,
            message: '特殊字符只允许下划线',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    ok () {
      let This = this
      this.$refs['formValidate'].validate((valid) => {
        if (valid) {
          This.$emit('refreshXml', This.process)
          This.modalVisible = false
        }
      })
    },
    change (flag) {
      if (!flag) {
        this.process = {
          processDefinitionKey: null,
          name: null
        }
      }
    },
    cancel () {
      this.$router.push({
        name: 'model-management'
      })
    }
  }
}
</script>

<style scoped>

</style>
