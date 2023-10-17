<style lang='less' scoped>
.ivu-col{
  display: flex;
  justify-content: center;
}
.ivu-form-item{
 width: 100%;
}
</style>

<template>
  <Card style ="margin-bottom:20px">
    <p slot="title">两列展示</p>
    <Form :model="formItem" :label-width="150" inline label-colon>
      <Row :gutter="16">
        <i-col span="12">
          <FormItem label='课程名称'>
            <Input v-model="formItem.name"  placeholder="课程的名称"/>
          </FormItem>
        </i-col>
        <i-col span="12">
          <FormItem label='课程描述'>
            <Input v-model="formItem.value"  placeholder="课程的描述"/>
          </FormItem>
        </i-col>
      </Row>
      <Row :gutter="16">
        <i-col span="12">
          <FormItem label='课程名称'>
            <Input v-model="formItem.name"  placeholder="课程的名称"/>
          </FormItem>
        </i-col>
        <i-col span="12">
          <FormItem label='课程描述'>
            <Input v-model="formItem.value"  placeholder="课程的描述"/>
          </FormItem>
        </i-col>
      </Row>
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
      }
    }
  }
}
</script>
