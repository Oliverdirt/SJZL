<template>
    <div>
        <Modal
                v-model="modalVisible"
                title="模型属性"
                :mask-closable="false"
                :closable="false"
                @on-visible-change="change">
            <div style="padding: 20px 30px 20px 0;">
                <Form ref="formValidate" :model="process" :label-width="100" :rules="ruleValidate">
                    <FormItem label="模型KEY" prop="processDefinitionKey">
                        <Input v-model="process.processDefinitionKey" :maxlength="50"></Input>
                    </FormItem>
                    <FormItem label="模型名称" prop="name">
                        <Input v-model="process.name" :maxlength="50"></Input>
                    </FormItem>
                    <FormItem label="表单" style="display: none">
                        <Input v-model="formName" readonly="" :maxlength="50">
                            <Button slot="append" icon="ios-search" @click="selectForm"></Button>
                        </Input>
                    </FormItem>
                    <FormItem label="监听器" prop="processListener">
                        <Input v-model="process.processListener" ></Input>
                    </FormItem>
                </Form>
            </div>
            <div slot="footer">
                <Button size="large" @click="cancel">取消</Button>
                <Button type="primary" size="large" @click="ok">确定</Button>
            </div>
        </Modal>
        <form-list-win ref="FormListWin" @initForm="initForm"></form-list-win>
    </div>

</template>

<script>

import FormListWin from './FormListWin'

export default {
  name: 'ProcessWin',
  components: { FormListWin },
  data () {
    return {
      modalVisible: false,
      element: null,
      businessObject: {},
      process: {
        processDefinitionKey: null,
        name: null,
        processListener: null,
        forms: []
      },
      formName: null,
      ruleValidate: {
        processDefinitionKey: [
          { required: true, message: '模型KEY不能为空', trigger: 'blur' },
          {
            type: 'string',
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
        ],
        processListener: [
          {
            type: 'string',
            pattern: /^[A-Za-z0-9]+$/,
            message: '只允许数字和字母',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  watch: {
    element: function (value) {
      console.log(value)
      if (!this.modalVisible) return
      if (value === null) return
      this.businessObject = value.businessObject
      this.process.processDefinitionKey = value.businessObject.data.processDefinitionKey
      this.process.name = value.businessObject.data.name
      this.process.forms = value.businessObject.data.forms
      this.process.processListener = value.businessObject.data.processListener
    }
  },
  methods: {
    initForm (value) {
      this.process.forms = []
      this.formName = value.name
      this.process.forms.push(value)
    },
    selectForm () {
      if (this.process.forms.length > 0) {
        this.$refs.FormListWin.checked = this.process.forms[0]
      }
      this.$refs.FormListWin.modalVisible = true
    },
    ok () {
      let This = this
      this.$refs['formValidate'].validate((valid) => {
        if (valid) {
          this.element.businessObject.data.processDefinitionKey = This.process.processDefinitionKey
          this.element.businessObject.data.name = This.process.name
          this.element.businessObject.data.forms = This.process.forms
          this.element.businessObject.data.processListener = This.process.processListener

          This.modalVisible = false
        }
      })
    },
    change (flag) {
      if (!flag) {
        this.element = null
        this.businessObject = {}
        this.process = {
          processDefinitionKey: null,
          name: null,
          processListener: null,
          forms: []
        }
        this.formName = null
      }
    },
    cancel () {
      let This = this
      // this.$refs['formValidate'].validate((valid) => {
      //     if (valid) {
      This.modalVisible = false
      //     }else {
      //         This.$Message.warning('先处理表单验证错误，才能关闭');
      //     }
      // })
    }
  }
}
</script>

<style scoped>

</style>
