<template>
    <div>
        <el-form ref="formData" :model="form" label-width="80px" :rules="rules">
              <el-form-item label="流程标识" prop="key" >
                  <el-input v-model="form.key" :disabled="formOpen" placeholder="请输入流标标识"/>
              </el-form-item>
              <el-form-item label="流程名称" prop="name">
                  <el-input v-model="form.name" :disabled="formOpen" placeholder="请输入流程名称"/>
              </el-form-item>
              <el-form-item label="流程描述" prop="description">
                  <el-input v-model="form.description" type="textarea" placeholder="请输入流程描述" clearable />
              </el-form-item>
              <el-form-item label="表单类型" prop="">
                <el-radio-group v-model="form.formType" @change="formtypeFn">
                    <el-radio label="1">自定义</el-radio>
                    <el-radio label="2">路由</el-radio>
                </el-radio-group>
              </el-form-item>
              <div v-show="formTypeVisible != 2" style="display:flex;margin: 0px 0px 20px 80px;">
                <div class="boxLeft" @click="formCreated(0)" :class="formCreatedVisable == 0 ? 'active' : ''">
                    <span>通过已有表单创建模型</span>
                    <div class="check" v-show="formCreatedVisable == 0">
                        <i class="el-icon-check"></i>
                    </div>
                </div>
                <div class="boxRight" @click="formCreated(1)" :class="formCreatedVisable == 1 ? 'active' : ''">
                    <span>新建表单创建模型</span>
                    <div class="check" v-show="formCreatedVisable == 1">
                        <i class="el-icon-check"></i>
                    </div>
                </div>
              </div>
              <el-form-item v-if="formTypeVisible === 1 && formCreatedVisable != 1" label="表单选择" prop="formId">
                <el-select v-model="form.formId" clearable placeholder="请选择已有表单" filterable >
                    <el-option v-for="item in formArray" :key="item.formId" :label="item.formName" :value="item.formId">
                    </el-option>
                </el-select>
              </el-form-item>
              <el-form-item v-if="formTypeVisible === 2" label="表单名称" prop="formName">
                    <el-input v-model="form.formName" placeholder="请输入表单名称"></el-input>
              </el-form-item>
              <el-form-item v-if="formTypeVisible === 2" label="表单路径" prop="formCustomCreatePath">
                    <el-input v-model="form.formCustomCreatePath" placeholder="请输入表单路径"></el-input>
              </el-form-item>
              <!--新建自定义表单-->
              <el-form-item label="表单名称" v-if="formCreatedVisable == 1 && formTypeVisible != 2" prop="formName">
                  <el-input v-model="form.formName" placeholder="请输入表单名称"/>
              </el-form-item>
          </el-form>
    </div>
  </template>
<script>
import request from '@/api/workflow/request'
//   import loginVue from '../../admin/system-page/login/login.vue'
export default {
  data () {
    return {
      formParamsVisible: false,
      formOpen: false,
      form: {
        formType: '1'
      },
      formArray: [],
      formTypeVisible: 1,
      formCreatedVisable: '',
      rules: {
        key: [{ required: true, message: '流程标识不能为空', trigger: 'blur' }],
        name: [{ required: true, message: '流程名称不能为空', trigger: 'blur' }],
        formId: [{ required: true, message: '请选择已有表单', trigger: 'change' }],
        formName: [
          { required: true, message: '请输入表单名称', trigger: 'blur' },
          // { required: true, validator: validateFormName, trigger: 'blur' }
          {
            validator: (rule, value, callback) => {
              if (value.length > 15) {
                callback(new Error('表单名长度小于等于15'))
              } else if (value.length <= 15) {
                // if (value === this.oldFormName) {
                //   callback()
                //   return
                // }
                const [url, httpConfig] = [
                  '/api/lowcode/customize/getCscpCustomizeVformByName',
                  {
                    params: { formName: value }
                  }
                ]
                this.$http
                  .get(url, httpConfig)
                  .then((response) => {
                    console.log('response.data.length > 0', response.data.length > 0)
                    if (response.data.length > 0) {
                      callback(new Error('表单名已重复'))
                    } else {
                      callback()
                    }
                  })
                  .catch(() => {
                    callback(new Error('异步校验出错！'))
                  })
              }
            },
            trigger: 'blur'
          }
        ],
        formCustomCreatePath: [{ required: true, message: '请输入路由表单路径', trigger: 'blur' }]
      }
    }
  },
  mounted () {
    this.formChange()
    this.formCreated(0)
  },
  methods: {
    formChoose (row) {
      this.formOpen = true
      this.formtypeFn(row.formType)
      this.form = row
    },
    //
    formCreated (num) {
      if (num == 0) {
        this.formCreatedVisable = 0
        this.$emit('stepParams', 0)
        this.cancel()
        this.form.formType = '1'
      } else if (num == 1) {
        this.formCreatedVisable = 1
        this.$emit('stepParams', 1)
        this.cancel()
        this.form.formType = '1'
      }
    },
    // 选择表单类型
    formtypeFn (e) {
      if (e == 1) {
        this.formTypeVisible = 1
        this.cancel()
        this.$forceUpdate()
        this.form.formType = '1'
      } else {
        this.formTypeVisible = 2
        this.$emit('stepParams', 0)
        this.cancel()
        this.$forceUpdate()
        this.form.formType = '2'
      }
    },
    cancel () {
      this.$set(this.form, 'formId', undefined)
      this.$set(this.form, 'formName', undefined)
      this.$set(this.form, 'formCustomCreatePath', undefined)
      // if (this.$refs.formData !== undefined) this.$refs.formData.resetFields()
      if (this.$refs.formData !== undefined) {
        this.$refs['formData'].clearValidate('formId')
        this.$refs['formData'].clearValidate('formName')
        this.$refs['formData'].clearValidate('formCustomCreatePath')
      }
    },
    cancelAll () {
      this.$set(this.form, 'key', undefined)
      this.$set(this.form, 'name', undefined)
      this.$set(this.form, 'description', undefined)
      this.$set(this.form, 'formId', undefined)
      this.$set(this.form, 'formName', undefined)
      this.$set(this.form, 'formCustomCreatePath', undefined)
      if (this.$refs.formData !== undefined) this.$refs.formData.resetFields()
    },
    // 已有表单下拉数据
    formChange () {
      const params = {
        formType: '1',
        formName: ''
      }
      // this.$http
      //   .get('/api/lowcode/customize/cscpCustomizeVforms/getByFormType', {
      //     params: params
      //   })
      request.getCscpCustomizeVformsList({ params })
        .then((res) => {
          this.formArray = res.data
        })
        .catch(() => {
          this.$Message.error('表单查询失败')
        })
    },
    // 新建模型下一步
    modelSave () {
      return new Promise((resolve) => {
        this.$refs.formData.validate((valid) => {
          if (!valid) return
          const params = this.form
          for (let key in params) {
            // let value = obj[key]
            if (params[key] == undefined) {
              delete params[key]
            }
          }
          params.backState = '0'
          params.category = 'lowCode'
          console.log(params, 'paramsparams')
          this.$http.post('/api/flow/bpmn/createNew', params).then((res) => {
            resolve(1)
          })
            .catch((error) => {
              this.$message.error('新建模型失败:' + error.response.data.detail)
            })
        })
      })
    }
  }
}
</script>
  <style lang="less" scoped>
    .boxLeft, .boxRight{
        width: 170px;
        height: 40px;
        border: 1px solid #e7e7e7;
        line-height: 40px;
        text-align: center;
        color: black;
        position: relative;
        cursor: pointer;
        margin-right: 20px;
        .check{
            position: absolute;
            width: 12px;
            height: 12px;
            top: 0px;
            right: 0px;
            background-color: #348fe0;
            i {
              position: absolute;
              top: 0;
              right: 0;
              color: #fff;
            }
        }
    }
    .active {
        border: 1.2px solid #348fe0 !important;
        background-color: rgb(233, 240, 251);
        border-radius: 5px;
    }
  </style>
