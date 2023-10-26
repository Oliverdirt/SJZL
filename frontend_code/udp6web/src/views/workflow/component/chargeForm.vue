<template>
  <div>
    <el-form ref="formData" :model="form" label-width="80px">
      <el-form-item label="流程标识" prop="key">
        <el-input v-model="form.key" placeholder="请输入流标标识" disabled />
      </el-form-item>
      <el-form-item label="流程名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入流程名称" clearable disabled />
      </el-form-item>
      <el-form-item label="流程描述" prop="description">
        <el-input v-model="form.description" type="textarea" clearable />
      </el-form-item>
      <el-form-item label="表单类型" prop="formType">
        <el-select v-model="form.formType" clearable placeholder="请选择" @change="formTypeChange">
          <el-option v-for="item in formTypeOpts" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="表单选择" prop="formId">
        <el-select v-model="form.formId" clearable placeholder="请选择">
          <el-option v-for="item in options" :key="item.formId" :label="item.formName" :value="item.formId">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <!-- <div slot="footer" class="dialog-footer">
      <el-button @click="save" type="primary">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div> -->
  </div>
</template>
<script>
import request from '@/api/workflow/request'

export default {
  data() {
    return {
      editFlag: false,
      form: {},
      options: [],
      formTypeOpts: [
        {
          label: '自定义表单',
          value: '1'
        },
        {
          label: '路由表单',
          value: '2'
        }
      ],
      page: 1,
      size: 10,
      modelData: {
        modelKey: '',
        modelName: ''
      },
    };
  },
  created() {
    let params = {
      page: 1,
      size: 100000
    }
    // this.$http
    //   .get('/api/flow/bpmn/page', {
    //     params: params
    //   })
    request.getCscpFlowBpmnPageList({ params: params })
      .then((res) => {
        // 上面的接口需要在后续的返回值里加上formType参数
        this.form = res.data.result.list[0]
        console.log('this.form.formType1', this.form.formType)
        this.formTypeChange()
        console.log('调用了this.formTypeChange')
        this.editFlag = true
      })
      .catch(() => {
        this.$Message.error('列表查询失败')
      })
  },
  methods: {
    formTypeChange() {
      console.log('this.editFlag', this.editFlag)
      const formType = this.form.formType
      let obj = {}
      obj['formType'] = formType
      let params = Object.assign(obj)
      if (this.editFlag) {
        this.form.formId = ''
        console.log('重置formId')
      }
      // this.$http
      //   .get('/api/lowcode/customize/cscpCustomizeVforms/getByFormType', {
      //     params: params
      //   })
      request.getCscpCustomizeVformsList({ params: params })
        .then((res) => {
          console.log('res.data', res.data)
          this.options = res.data
        })
        .catch(() => {
          this.$Message.error('表单查询失败')
        })
    },
    save() {
      let copyDetail = JSON.parse(JSON.stringify(this.form));
      // this.$http.put('/api/flow/bpmn/update', copyDetail)
      request.updateBpmn(copyDetail)
        .then((res) => {
          this.$Message.success('更新成功')
          //   this.queryList()
        })
        .catch((error) => {
          this.$Message.error('更新失败:' + error.response.data.detail)
        })
    },
    queryList() {
      let params = {
        page: 1,
        size: 100000
      }
      // this.$http
      //   .get('/api/flow/bpmn/page', {
      //     params: params
      //   })
      request.getCscpFlowBpmnPageList({params: params})
        .then((res) => {
          // 上面的接口需要在后续的返回值里加上formType参数
          this.form = res.data.result.list[0]
        })
        .catch(() => {
          this.$Message.error('列表查询失败')
        })
    },
    getFormAll() {
      let obj = {}
      obj['formType'] = '3'
      let params = Object.assign(obj)
      // this.$http
      //   .get('/api/lowcode/customize/cscpCustomizeVforms/listAll', {
      //     params: params
      //   })
      request.getAllList({params: params})
        .then((res) => {
          this.options = res.data
        })
        .catch(() => {
          this.$Message.error('流程表查询失败')
        })
    },
  },
};
</script>
<style scoped>

</style>
