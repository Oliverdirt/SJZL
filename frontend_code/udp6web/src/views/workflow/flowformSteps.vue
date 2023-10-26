<template>
  <Modal v-model="flowformStepsVisiable" :width="1568" :fullscreen="isFullScreen" @on-cancel="close">
    <p slot="header">
      <Row :gutter="40">
        <Col span="20">
        <!-- <span v-html="modalTitle" class="modalHeaderClass"></span> -->
        </Col>
        <Col span="4" style="text-align: right">
        <a class="dark-a" href="javascript:void(0)" @click="screenClick">
          <Icon custom="iconfont  icon-FullScreen" v-if="!isFullScreen" :size="20" />
          <Icon custom="iconfont  icon-ExitFullScreen" v-if="isFullScreen" :size="20" />
        </a>
        <a class="dark-a" href="javascript:void(0)">
          <Icon style="margin:0 0 0 26px" :size="20" />
        </a>
        </Col>
      </Row>
    </p>
    <br>
    <br>
    <!--步骤-->
    <Steps :current="step">
      <Step title="新建流程"></Step>
      <Step title="表单绑定"></Step>
      <Step title="设计流程"></Step>
      <Step title="分配规则"></Step>
      <Step title="发布流程"></Step>
      <Step title="权限管理"></Step>
    </Steps>
    <br>
    <br>
    <!--中间内容-->
    <div>
      <!-- step1 新建流程      -->
      <div class="step0" v-if="step == 0"
        style="display: flex;padding-top: 100px;justify-content: center;align-items: center;">
        <addProcess ref='addProcessRef' style="width:50%" />
      </div>
      <!-- step1 表单绑定     -->
      <div class="step1" v-if="step == 1"
        style="display: flex;padding-top: 100px;justify-content: center;align-items: center;">
        <chargeForm ref='chargeFormRef' style="width:50%" />
      </div>
      <!-- step1 设计流程      -->
      <div class="step2" v-if="step == 2" style="height: 700px;overflow-y: auto;">
        <ModelEditor ref='ModelEditorRef' :modelEditorData="modelEditorData" />
      </div>
      <!-- step1 分配规则      -->
      <div class="step3" v-if="step == 3"
        style="display: flex;padding-top: 100px;justify-content: center;align-items: center;">
        <handleAssignRule ref='handleAssignRuleRef' style="width:50%" />
      </div>
      <!-- step1 发布流程      -->
      <div class="step4" v-if="step == 4">
        <publishDesign ref='publishDesignRef' @cancelNextStep='cancelNextStep' />
      </div>
      <!-- step1 权限管理      -->
      <div class="step5" v-if="step == 5"
        style="display: flex;padding-top: 100px;justify-content: center;align-items: center;">
        <authorizationManagement ref='authorizationManagementRef' style="width:50%" />
      </div>
    </div>
    <!--上下步-->
    <div slot="footer">
      <Button type="primary" v-if="this.step > 1" @click="back"><span>上一步</span></Button>
      <Button type="primary" v-if="this.step < 5 && this.NextStep" @click="next"><span>下一步</span></Button>
      <Button type="primary" v-if="step == 4" @click="finish"><span>完成</span></Button>
      <Button type="primary" v-if="step == 5" @click="finishStep"><span>完成</span></Button>
    </div>
  </Modal>
</template>
<script>
import addProcess from './component/addProcess.vue'
import chargeForm from './component/chargeForm.vue'
import handleAssignRule from './component/handleAssignRule.vue'
import publishDesign from './component/publishDesign.vue'
import authorizationManagement from './component/authorizationManagement.vue'
import request from '@/api/workflow/request'

export default {
  components: {
    addProcess,
    chargeForm,
    handleAssignRule,
    publishDesign,
    authorizationManagement
  },
  data () {
    return {
      flowformStepsVisiable: false,
      isFullScreen: true,
      step: 0,
      page: 1,
      size: 10,
      NextStep: true,
      modelEditorData: {
        // modelId:'',
        // processId: '',
        // processName:''
      }
    }
  },
  methods: {
    cancelNextStep () {
      this.NextStep = false
    },
    close () {
      window.location.reload()
    },
    finish () {
      window.location.reload()
    },
    finishStep () {
      this.$refs.authorizationManagementRef.submitAuthorization()
      window.location.reload()
    },
    // 列表
    queryList () {
      return new Promise((resolve) => {
        let params = {
          page: 1,
          size: 100000
        }
        // this.$http
        //   .get('/api/flow/bpmn/page', {
        //     params: params
        //   })
        request.getCscpFlowBpmnPageList({ params })
          .then((res) => {
            this.modelEditorData = res.data.result.list[0]
            resolve(1)
          })
          .catch(() => {
            this.$Message.error('列表查询失败')
          })
      })
    },
    back () {
      this.step--
      this.NextStep = true
    },
    async next () {
      if (this.step == 0) {
        await this.$refs.addProcessRef.save()
        if (this.$refs.addProcessRef.form.key) {
          this.step += 1
        }
      } else if (this.step == 1) {
        this.$refs.chargeFormRef.save()
        await this.queryList()
        this.step += 1
      } else if (this.step == 2) {
        await this.$refs.ModelEditorRef.$refs.processDesigner.processSave()
        this.step += 1
      } else if (this.step == 3) {
        this.step += 1
      } else if (this.step == 4) {
        this.step += 1
      }
    },
    ok () {
      this.$Message.info('Clicked ok')
    },
    cancel () {
      this.$Message.info('Clicked cancel')
    },
    screenClick () {
      if (this.isFullScreen) {
        this.isFullScreen = false
      } else {
        this.isFullScreen = true
      }
    }
  }
}
</script>
<style scoped>

</style>
