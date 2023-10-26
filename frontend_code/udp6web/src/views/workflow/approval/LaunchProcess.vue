<template>
  <div>
    <div v-if="showVueComponent">
      <!-- 这里直接展示formUrl参数指向的vue文件 -->
      <component :processDefinitionId="processDefinitionId" :taskId="taskId" :processInstanceId="processInstanceId" :is="testComponent" />
    </div>
    <div id="LaunchProcess" v-else>
      <header>
        <p>申请信息</p>
        <div class="formBox">
          <VFormRender ref="preForm" :form-json="formJson" :preview-state="true" @formChange="handleFormChange"
            :form-data="testFormData" :option-data="testOptionData" v-if="addDialogVisible">
          </VFormRender>
          <div class="btnBox">
            <el-button size="small" type="primary" @click="submitForm()">提交</el-button>
            <el-button size="small" @click="resetForm()">重置</el-button>
          </div>
        </div>
      </header>
      <main>
        <p>流程图</p>
        <div>
          <ProcessViewer v-model="bpmnXML" />
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import VFormRender from '@/views/lowcode/vformPro/components/form-render/index'
import ProcessViewer from '@/views/workflow/approval/ProcessViewer'
import request from '@/api/workflow/request'
const loadComponent = url => import(`@/views${url}`)

export default {
  name: 'LaunchProcess',
  components: {
    VFormRender,
    ProcessViewer
  },
  data() {
    return {
      showVueComponent: false,
      activeUrl: '',
      testComponent: null,
      bpmnXML: '',
      formJson: {},
      formData: {},
      rules: {
        name: [
          { required: true, message: '请输入活动名称', trigger: 'blur' },
          { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        resource: [
          { required: true, message: '请选择', trigger: 'blur' }
        ]
      },
      formId: '',
      processDefinitionId: '',
      taskId:'',
      processInstanceId:'',
      testFormData: {},
      testOptionData: {},
      addDialogVisible: false
    }
  },
  methods: {
    // 初始化
    initPage() {
      this.$route.query.formId ? window.localStorage.setItem('launchFormId', this.$route.query.formId) : ''
      this.$route.query.processDefinitionId ? window.localStorage.setItem('launchProcessDefinitionId', this.$route.query.processDefinitionId) : ''
      this.formId = window.localStorage.getItem('launchFormId')
      this.processDefinitionId = window.localStorage.getItem('launchProcessDefinitionId')
      this.getProcessChart()
      this.getFormJson()
      this.resetForm()
    },
    // 提交表单
    submitForm() {
      this.$refs['preForm'].getFormData().then(formData => {
        let queryParama = {
          processDefinitionId: this.processDefinitionId,
          variables: formData,
          formId: this.formId
        }
        // this.$http.post('/api/flow/process/create', queryParama)
        request.createFlowProcess(queryParama)
          .then(res => {
          this.$message.success('发起成功！')
          this.$router.back(-1)
        }).catch(err => {
          this.$message.error('发起失败！')
        })
      })
    },
    // 重置表单
    resetForm() {

      if (this.$refs.preForm !== undefined) this.$refs.preForm.resetForm()
    },
    handleFormChange(fieldName, newValue, oldValue, formModel) {

    },
    // 流程图
    getProcessChart() {
      // this.$http.get('/api/flow/def/get-bpmn-xml', { params: { id: this.processDefinitionId } })
      request.getBpmnXmlData({ params: { id: this.processDefinitionId } })
        .then(res => {
        this.bpmnXML = res.data.result
      })
    },
    // 获取formJson
    getFormJson() {
      // this.$http.get('/api/lowcode/customize/cscpCustomizeVforms/' + this.formId)
      request.getDataByFormId(this.formId)
        .then(res => {
        if (res.data.formType === '2') {
          this.activeUrl = res.data?.formUrl
          this.showVueComponent = true
        }
        this.formJson = JSON.parse(res.data.formJson)
        this.addDialogVisible = true
      })
    }
  },
  watch: {
    $route: {
      handler(to, from) {
        this.initPage()
      }
    },
    // 监控变量的变化来动态引入组件
    activeUrl: {
      handler(value) {
        loadComponent(value).then(component => {
          this.testComponent = component.default;
        });
      },
      immediate: true
    }
  },
  mounted() {
    this.initPage()
  }
}
</script>

<style lang="less">
#LaunchProcess {
  width: 100%;
  height: 100%;
  background-color: #fff;
  display: flex;
  flex-direction: column;

  header {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin-bottom: 15px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    p {
      padding: 15px;
      width: 100%;
      border-bottom: 1px solid rgba(0, 0, 0, .12);
      margin-bottom: 15px;
    }

    .formBox {
      width: 60%;
      padding: 15px;
      max-height: 350px;
      overflow-y: auto;

      .btnBox {
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }

    .formBox::-webkit-scrollbar {
      display: none;
    }
  }

  main {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    flex: 1;
    // min-height: 600px;
    background-color: #fff;

    p {
      padding: 15px;
      border-bottom: 1px solid rgba(0, 0, 0, .12);
      margin-bottom: 15px;
    }
  }
}
</style>
