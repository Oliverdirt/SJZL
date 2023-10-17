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
  <Card>
    <p slot="title"> 三列展示</p>
    <Form :model="formItem"  :label-width="100" inline label-colon>
      <Row :gutter="16">
        <i-col span="8">
          <FormItem label='模板'>
            <Select v-model="formItem.name" placeholder="请选择" >
              <Option v-for="item in chooseList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem>
        </i-col>
        <i-col span="8">
          <FormItem label='模板类型'>
            <Select v-model="formItem.name" placeholder="请选择" >
              <Option v-for="item in chooseList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem>
        </i-col>
        <i-col span="8">
          <FormItem label='模板类型模板'>
            <Select v-model="formItem.name" placeholder="请选择" >
              <Option v-for="item in chooseList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem>
        </i-col>
      </Row>
      <Row :gutter="16">
        <i-col span="8">
          <FormItem label='模板类型'>
            <Select v-model="formItem.name" placeholder="请选择" >
              <Option v-for="item in chooseList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem>
        </i-col>
        <i-col span="8">
          <FormItem label='模板类型'>
            <Select v-model="formItem.name" placeholder="请选择" >
              <Option v-for="item in chooseList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem>
        </i-col>
        <i-col span="8">
          <FormItem label='模板类型'>
            <Select v-model="formItem.name" placeholder="请选择" >
              <Option v-for="item in chooseList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem>
        </i-col>
      </Row>
      <Row :gutter="16">
        <i-col span="8">
          <FormItem label='模板类型'>
            <Select v-model="formItem.name" placeholder="请选择" >
              <Option v-for="item in chooseList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem>
        </i-col>
        <i-col span="8">
          <FormItem label='模板类型'>
            <Select v-model="formItem.name" placeholder="请选择" >
              <Option v-for="item in chooseList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem>
        </i-col>
        <i-col span="8">
          <FormItem label='模板类型'>
            <Select v-model="formItem.name" placeholder="请选择" >
              <Option v-for="item in chooseList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
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
        name: ''
      },
      chooseList: [
        {
          value: 'front',
          label: '比翼前端'
        },
        {
          value: 'back',
          label: '比翼后端'
        }
      ]
    }
  }
}
</script>
