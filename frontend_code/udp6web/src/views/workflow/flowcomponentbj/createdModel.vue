<template>
    <div>
        <el-form ref="formData" :model="form" label-width="80px" :rules="rules">
              <el-form-item label="流程标识" prop="key" >
                  <el-input v-model="form.key" disabled placeholder="请输入流标标识"/>
              </el-form-item>
              <el-form-item label="流程名称" prop="name">
                  <el-input v-model="form.name" disabled placeholder="请输入流程名称"/>
              </el-form-item>
              <el-form-item label="流程描述" prop="description">
                  <el-input v-model="form.description" type="textarea" placeholder="请输入流程描述" clearable />
              </el-form-item>
              <el-form-item label="表单类型" prop="">
                <el-radio-group v-model="form.formType" disabled>
                    <el-radio label="1">自定义</el-radio>
                    <el-radio label="2">路由</el-radio>
                </el-radio-group>
              </el-form-item>
              <!-- <div v-show="formTypeVisible != 2" style="display:flex;margin: 0px 0px 20px 80px;">
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
              </div> -->
              <el-form-item v-if="formTypeVisible === 1 && formCreatedVisable != 1" label="表单选择" prop="formId">
                <el-select v-model="form.formId" clearable placeholder="请选择已有表单" filterable>
                    <el-option v-for="item in formArray" :key="item.formId" :label="item.formName" :value="item.formId">
                    </el-option>
                </el-select>
              </el-form-item>
              <el-form-item v-if="formTypeVisible === 2" label="表单名称" prop="formName">
                    <el-input v-model="form.formName" disabled placeholder="请输入路由表单名称"></el-input>
              </el-form-item>
              <el-form-item v-if="formTypeVisible === 2" label="表单路径" prop="formCustomCreatePath">
                    <el-input v-model="form.formCustomCreatePath" placeholder="请输入路由表单路径"></el-input>
              </el-form-item>
              <!--11-->
              <!-- <el-form-item label="表单名称" v-if="formCreatedVisable == 1 && formTypeVisible != 2" prop="formName">
                  <el-input v-model="form.formName" placeholder="请输入自定义表单名称"/>
              </el-form-item> -->
          </el-form>
    </div>
  </template>
<script>
import request from '@/api/workflow/request'
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
        formId: [{ required: true, message: '请选择表单', trigger: 'change' }],
        formName: [{ required: true, message: '请输入路由表单名称', trigger: 'blur' }],
        formCustomCreatePath: [{ required: true, message: '请输入路由表单路径', trigger: 'blur' }]
      }
    }
  },
  mounted () {
    // this.folwParamsFn()
  },
  methods: {
    formChooseFn (row) {
      this.paramsData = row
      this.id = row.id
      this.formId = row.formId
      let rowData = row
      rowData.formCustomCreatePath = row.formUrl
      this.form = { ...rowData }
      if (row.formType == 1) {
        this.formChange()
      } else {
        this.formTypeVisible = 2
      }
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
    // 表单
    cancel () {
      this.form = {
        formId: undefined,
        formName: undefined,
        formCustomCreatePath: undefined,
        formType: undefined,
        // key: undefined,
        // name: undefined,
        description: undefined
      }
      // if (this.$refs.formData !== undefined) this.$refs.formData.resetFields()
    },
    // 保存
    modelSave () {
      return new Promise((resolve) => {
        if (this.form.formType == 1) {
          let formObj = {}
          formObj.id = this.id
          formObj.key = this.form.key
          formObj.name = this.form.name
          formObj.description = this.form.description
          formObj.formType = this.form.formType
          formObj.formId = this.form.formId
          this.$refs.formData.validate((valid) => {
            if (!valid) return
            let copyDetail = JSON.parse(JSON.stringify(formObj))
            this.$http.put('/api/flow/bpmn/update', copyDetail).then((res) => {
              this.$Message.success('更新成功')
              // window.location.reload()
              this.$parent.$parent.flowStepsVisiable = false
              resolve(1)
              this.$parent.$parent.$parent.queryList()
            }).catch(() => {
              this.$Message.error('更新失败')
            })
          })
        } else {
          let routeObj = {}
          routeObj.id = this.id
          routeObj.key = this.form.key
          routeObj.name = this.form.name
          routeObj.description = this.form.description
          routeObj.formType = this.form.formType
          routeObj.formName = this.form.formName
          routeObj.formId = this.form.formId
          routeObj.formUrl = this.form.formCustomCreatePath
          this.$refs.formData.validate((valid) => {
            if (!valid) return
            let copyDetail = JSON.parse(JSON.stringify(routeObj))
            this.$http.put('/api/lowcode/customize/cscpCustomizeVforms', copyDetail).then((res) => {
              this.$Message.success('更新成功')
              this.$parent.$parent.flowStepsVisiable = false
              resolve(1)
              this.$parent.$parent.$parent.queryList()
            }).catch(() => {
              this.$Message.error('更新失败')
            })
          })
        }
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
