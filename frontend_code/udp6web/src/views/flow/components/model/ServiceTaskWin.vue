<template>
    <Modal
            v-model="modalVisible"
            title="属性设置"
            :mask-closable="false"
            :closable="false"
            @on-visible-change="change">
        <div style="padding: 20px 30px 20px 0;">
            <Form ref="formService" :model="none" :label-width="80" :rules="ruleValidate">
                <FormItem label="环节">
                    <Input v-model="none.id" disabled=""></Input>
                </FormItem>
                <FormItem label="名称" prop="name">
                    <Input v-model="none.name" :maxlength="40"></Input>
                </FormItem>
                <FormItem label="执行类" prop="taskListener">
                    <Input v-model="none.taskListener"></Input>
                </FormItem>
            </Form>
        </div>
        <div slot="footer">
            <Button size="large" @click="cancel">取消</Button>
            <Button type="primary" size="large" @click="ok">确定</Button>
        </div>
    </Modal>
</template>

<script>
export default {
  name: 'ServiceTaskWin',
  data () {
    return {
      modalVisible: false,
      element: null,
      none: {
        id: null,
        name: null,
        data: {},
        taskListener: null
      },
      ruleValidate: {
        name: [
          {
            type: 'string',
            pattern: /^[A-Za-z0-9_\u4e00-\u9fa5]+$/,
            message: '特殊字符只允许下划线',
            trigger: 'blur'
          }
        ],
        taskListener: [
          { required: true, message: '执行类不能为空', trigger: 'blur' },
          {
            type: 'string',
            pattern: /^[A-Za-z0-9]+$/,
            message: '只允许数字和字母',
            trigger: 'blur'
          },
          {
            max: 100,
            message: '最多允许100个字符',
            trigger: 'blur'
          }
        ]
      }

    }
  },
  watch: {
    element: function (value) {
      if (!this.modalVisible) return
      if (value === null) return
      this.none.taskListener = value.businessObject.data.taskListener
      // this.none.businessObject = value.businessObject;
      this.none.id = value.businessObject.id
      this.none.name = value.businessObject.name
    }
  },
  methods: {
    ok () {
      let This = this
      this.$refs['formService'].validate((valid) => {
        if (valid) {
          let elements = This.element
          elements.businessObject.data.taskListener = This.none.taskListener
          elements.businessObject.name = This.none.name
          This.$emit('refreshElement', elements)
          This.modalVisible = false
        }
      })
    },
    change (flag) {
      if (!flag) {
        this.element = null
        this.none = {
          id: null,
          name: null,
          data: {},
          taskListener: null
        }
      }
    },
    cancel () {
      let This = this
      // this.$refs['formService'].validate((valid) => {
      //     if (valid) {
      This.modalVisible = false
      //     }else {
      //         This.$Message.warning('先处理表单验证错误，才能关闭');
      //     }
      // });
    }
  }
}
</script>

<style scoped>

</style>
