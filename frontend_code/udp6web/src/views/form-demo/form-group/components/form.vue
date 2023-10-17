<style lang='less'>
@import '../../index.less';
.form-body{
  width: 1000px;
  margin: 0 auto;

   //上传附件样式示例
  .upload-form{
    .notice{
      color: #ed4014;
      vertical-align: middle;
      margin-left: 20px;
      font-size: 14px;
      line-height: 1;
      padding: 10px 12px 10px 0;
      box-sizing: border-box;
    }
    .upload-file{
        display: inline-flex;
        justify-content: center;
        align-items: center;
        width:552px;
        position: relative;
        button{
          position: absolute;
          width:100%;
        }
        input{
          position: absolute;
          opacity: 0;
          width:100%;
          cursor: pointer;
        }
    }
  }
  // 上传图片样式示例
  .upload-form-img{
    .img-notice{
      margin-left: 50px;
      color: #ed4014;
      font-size: 14px;
      line-height: 1;
      padding: 10px 12px 10px 0;
      box-sizing: border-box;
    }
    .img-wrapper {
      display: flex;
      .add-img {
        margin-top: 10px;
        width: 180px;
        height: 130px;
        border: 1px dashed #dcdee2;
        border-radius: 4px;
        cursor: pointer;
        position: relative;
        margin-right: 10px;
        .close-icon {
          position:absolute;
          filter: opacity(0.7);
          right:-13px;
          top:-13px;
        }
        img {
          width: 100%;
          height: 100%;
        }
        span {
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
          z-index: 1;
        }
        input {
          position: relative;
          z-index: 10;
          width: 100%;
          height: 100%;
          opacity: 0;
          cursor: pointer;
        }
      }
    }
  }

}
</style>

<template>
  <div>
    <Form class="form-body" :model="formItem" :label-width="100" :rules="ruleValidate" label-colon>
      <FormItem label='搜索输入框'>
        <Input v-model="formItem.value1" prefix="ios-search" class="m-input" placeholder="请输入"/>
        <Button type="primary" style="margin-left:20px" icon="ios-search">搜索</Button>
      </FormItem>
      <FormItem label='普通输入框' prop="value2">
        <Input v-model="formItem.value2" class="m-input" placeholder="请输入"/>
      </FormItem>
      <FormItem label='单选选择框'>
        <Select v-model="formItem.value3" class="m-input" placeholder="请选择模板工具">
          <Option value="front">前端模板</Option>
          <Option value="back">后端模板</Option>
        </Select>
      </FormItem>
       <FormItem label='多选选择框'>
        <Select v-model="formItem.value4" multiple class="m-input">
          <Option v-for="item in chooseList" :value="item.value" :key="item.value">{{ item.label }}</Option>
        </Select>
      </FormItem>
      <FormItem label='多行文本输入'>
        <Input v-model="formItem.value6" type="textarea" class="xl-input" :rows="4" placeholder="请输入文本" />
        <Tooltip content="提示信息" style="position:absolute;margin-top:-10px;margin-left:5px" placement="right"  transfer>
            <Icon type="ios-alert-outline" />
        </Tooltip>
      </FormItem>
      <FormItem label='选择时间'>
       <DatePicker type="daterange" class="s-input" placement="bottom-end" placeholder="请选择出发时间" ></DatePicker>
      </FormItem>
      <FormItem label='上传附件' class="upload-form">
        <div class="upload-file m-input">
          <Button type="dashed">
            <Icon type="ios-cloud-upload-outline" size="24"/>
            <span style="position:relative;top:-3px">上传</span>
          </Button>
          <input ref="uploadFile" type="file" @change="changeFile" >
        </div>
         <span class="notice">注意：申请书应以WORD形式提及，大小不超过3M</span>
         <div style="margin-top:10px">
          <Tag v-if="formItem.file.name != ''" closable >{{formItem.file.name}}</Tag>
         </div>
      </FormItem>
      <FormItem label='上传图片' class="upload-form-img">
       <Button type="primary" style="width:150px" @click="uploadCover">选择图片</Button>
       <span class="img-notice">要求：可上传jpg png gif图片 大小要求小于300k</span>
       <div class="img-wrapper">
          <div>
            <div class="add-img">
              <span style="filter: opacity(0.5)">
                <Icon type="ios-add-circle" size='40' />
              </span>
              <input ref="coverImage" type="file" accept="image/*" @change="imgChange" >
            </div>
            <p style="text-align:center">可上传图片</p>
          </div>
          <div v-if="formItem.cover.url != ''">
            <div class="add-img" style="position:relative" >
              <img :src="formItem.cover.url">
              <Icon class="close-icon" type="md-close-circle" size='26' />
            </div>
            <p style="text-align:center">{{formItem.cover.file != null ? formItem.cover.file.name : ''}}</p>
          </div>
       </div>
      </FormItem>
      <FormItem label='单项选择框'>
        <RadioGroup v-model="formItem.value7">
          <Radio label="apple">
            <span>选择一</span>
          </Radio>
          <Radio label="android">
            <span>选择二</span>
          </Radio>
          <Radio label="windows">
            <span>选择三</span>
          </Radio>
        </RadioGroup>
      </FormItem>
      <FormItem label='多项选择框'>
        <CheckboxGroup v-model="formItem.value8">
          <Checkbox label="选择一"></Checkbox>
          <Checkbox label="选择二"></Checkbox>
          <Checkbox label="选择三"></Checkbox>
        </CheckboxGroup>
      </FormItem>
      <FormItem label='报表单数' >
        <InputNumber class="xs-input" :max="1000" :min="1" v-model="formItem.value9"></InputNumber>
      </FormItem>
    </Form>
  </div>
</template>

<script>
export default {
  data () {
    return {
      formItem: {
        value1: '',
        value2: '',
        value3: '',
        value4: ['one', 'two'],
        value5: '',
        value6: '',
        value7: '',
        value8: [],
        value9: 0,
        cover: {
          file: null,
          url: '',
          name: ''
        },
        file: {
          name: ''
        }
      },
      chooseList: [
        {
          value: 'one',
          label: '选择一'
        },
        {
          value: 'two',
          label: '选择二'
        },
        {
          value: 'three',
          label: '选择三'
        },
        {
          value: 'four',
          label: '选择四'
        }
      ],
      ruleValidate: {
        value2: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        value5: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    changeFile () {
      let file = this.$refs.uploadFile.files[0]
      this.formItem.file.name = file.name
    },
    uploadCover () {
      this.$refs.coverImage.click()
    },
    imgChange () {
      let file = this.$refs.coverImage.files[0]
      let fileName = file.name
      let fileType = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase()
      if (fileType !== 'png' && fileType !== 'jpg' && fileType !== 'gif') {
        this.$Message.error('上传图片格式不正确')
        return
      }
      // 可以对文件大小进行限制
      if (file.size > 300 * 1024) {
        this.$Message.error('图片大小应在300k以下')
        return
      }
      this.formItem.cover.file = file
      this.formItem.cover.name = fileName
      // 读取文件
      this.formItem.cover.url = this.toBlobURL(file)
    },
    toBlobURL (blob) {
      return window[window.webkitURL ? 'webkitURL' : 'URL']['createObjectURL'](blob)
    }
  }
}
</script>
