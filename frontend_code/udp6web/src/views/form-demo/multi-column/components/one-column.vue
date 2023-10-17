<style lang='less' scoped>
@import '../../index.less';
.input-form{
  width: 652px;
  margin: 0 auto;
}
</style>

<template>
  <Card style="margin-bottom:20px">
    <p slot="title">一列展示</p>
    <Form class="input-form" :label-width="100" :model="formItem" :rules="ruleValidate" label-colon>
      <FormItem label='课程名称' prop="name">
        <Input v-model="formItem.name" class="xl-input" placeholder="课程的名称"/>
      </FormItem>
      <FormItem label='课程描述' prop="value">
        <Input v-model="formItem.value" class="xl-input" placeholder="课程的描述"/>
      </FormItem>
    </Form>
  </Card>
</template>

<script>
export default {
  data () {
    return {
      formItem: {
        name: '',
        value: ''
      },
      ruleValidate: {
        name: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        value: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      }
    }
  }
}
</script>
