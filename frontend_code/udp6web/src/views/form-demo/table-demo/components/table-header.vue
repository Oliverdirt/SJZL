<style lang="less" scoped>
.form-header{
  padding: 10px;
  .ivu-col{
    display: flex;
    justify-content: center;
  }
  .ivu-form-item{
    width: 100%;
  }
  .search{
    width: 210px;
    float:right;
    .expand-all{
      display: inline-block;
      color:#2d8cf0;
      margin-left: 16px;
      cursor: pointer;
    }
  }
}
</style>

<template>
  <div class="form-header">
    <Form :label-width="100" :model="formItem" label-colon>
      <Row :gutter="16">
        <i-col span="6">
          <FormItem label='规则名称' prop="name">
            <Input v-model="formItem.name"  placeholder="请输入"/>
          </FormItem>
        </i-col>
        <i-col span="6">
          <FormItem label='规则名称' prop="name">
            <Input v-model="formItem.name"  placeholder="请输入"/>
          </FormItem>
        </i-col>
        <i-col span="6">
          <FormItem label='模板类型' prop="name">
            <Select v-model="formItem.value" >
              <Option v-for="item in chooseList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem>
        </i-col>
        <i-col span="6">
          <FormItem label='模板类型' prop="name">
            <Select v-model="formItem.value" >
              <Option v-for="item in chooseList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem>
        </i-col>
      </Row>
      <Row :gutter="16">
        <i-col span="6">
          <FormItem label='规则名称' prop="name">
            <Input v-model="formItem.name"  placeholder="请输入"/>
          </FormItem>
        </i-col>
        <i-col span="6">
          <FormItem label='规则名称' prop="name">
            <Input v-model="formItem.name"  placeholder="请输入"/>
          </FormItem>
        </i-col>
        <i-col span="6">
          <FormItem label='模板类型' prop="name">
            <Select v-model="formItem.value" >
              <Option v-for="item in chooseList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem>
        </i-col>
        <i-col span="6">
          <FormItem label='模板类型' prop="name">
            <Select v-model="formItem.value" >
              <Option v-for="item in chooseList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem>
        </i-col>
      </Row>
      <Row :gutter="16" v-if="isExpand">
        <i-col span="6">
          <FormItem label='规则名称' prop="name">
            <Input v-model="formItem.name"  placeholder="请输入"/>
          </FormItem>
        </i-col>
        <i-col span="6">
          <FormItem label='规则名称' prop="name">
            <Input v-model="formItem.name"  placeholder="请输入"/>
          </FormItem>
        </i-col>
      </Row>
    </Form>
    <div style="height:32px">
      <div class="search">
        <Button type="primary">搜索</Button>
        <Button style="margin-left: 16px;">重置</Button>
        <div class="expand-all" @click="expandSearch">
          <span>{{isExpand ? '收起':'展开'}}</span>
          <Icon :type="isExpand? 'ios-arrow-up':'ios-arrow-down'" size="15" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      isExpand: false,
      formItem: {
        name: '',
        value: ''
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
  },
  methods: {
    expandSearch (value) {
      this.isExpand = typeof value === 'boolean' ? value : !this.isExpand
    }
  }
}
</script>
