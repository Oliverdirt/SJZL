<template>
    <Modal
            v-model="modalVisible"
            title="属性设置"
            :mask-closable="false"
            :closable = "false"
            @on-visible-change="change">
        <div style="padding: 20px 30px 20px 0;">
            <Form  ref="formNode" :model="none" :label-width="80" :rules="ruleValidate">
                <FormItem label="环节">
                    <Input v-model="none.businessObject.id" disabled=""></Input>
                </FormItem>
                <FormItem label="名称" prop="businessObject.name">
                    <Input v-model="none.businessObject.name" :maxlength="30"></Input>
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
  name: 'NoneWin',
  data () {
    return {
      modalVisible: false,
      element: null,
      none: {
        businessObject: {
          id: null,
          name: null,
          data: {}
        }
      },
      ruleValidate: {
        'businessObject.name': [
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
  watch: {
    element: function (value) {
      if (!this.modalVisible) return
      if (value === null) return
      this.none.businessObject = value.businessObject
    }
  },
  methods: {
    ok () {
      let This = this
      this.$refs['formNode'].validate((valid) => {
        if (valid) {
          let elements = This.element
          elements.businessObject.name = This.none.businessObject.name
          This.$emit('refreshElement', elements)
          This.modalVisible = false
        }
      })
    },
    change (flag) {
      if (!flag) {
        this.element = null
        this.none = {
          businessObject: {
            id: null,
            name: null,
            data: {}
          }
        }
      }
    },
    cancel () {
      let This = this
      // this.$refs['formNode'].validate((valid) => {
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
