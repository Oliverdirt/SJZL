<template>
  <div>
    <Row :gutter="16" type="flex" justify="center" align="top">
      <Col span="24" style="border: #fff 1px solid; margin:5px;">
        <Form ref="jobForm" :model="jobForm" :rules="ruleValidate">
          <Row type="flex" justify="center" :gutter="16">
            <Col span="18">
              <FormItem prop="id" label="ID" style="display:none;">
                <Input type="text" v-model="jobForm.id" placeholder="ID" disabled>
                </Input>
              </FormItem>
              <FormItem prop="jobName" label="任务名称">
                <Input type="text" v-model.trim="jobForm.jobName" placeholder="请输入任务名称"/>
              </FormItem>
              <FormItem prop="jobGroup" label="任务分组">
<!--                <Input type="text" v-model="jobForm.jobGroup" placeholder="请选择"/>-->
                <Select  v-model="jobForm.jobGroup" placeholder="请选择任务组名" clearable >
                  <Option v-for="item in jobGroups" :value="item.itemCode.toLocaleString()" :key="item.itemCode.toLocaleString()">{{ item.itemValue }}</Option>
                </Select>
              </FormItem>
              <FormItem prop="invokeTarget" label="调用方法">
                  <span>
                  <el-tooltip placement="top">
                    <div slot="content">
                      Bean调用示例：biyiTask.biyiParams('biyi')
                      <br />Class类调用示例：com.ctsi.ssdc.quartz.task.TestTask.biyiParams('biyi')
                      <br />参数说明：支持字符串，布尔类型，长整型，浮点型，整型
                    </div>
                    <i class="el-icon-question"></i>
                  </el-tooltip>
                  </span>
                  <Input type="text" v-model.trim="jobForm.invokeTarget" placeholder="请输入调用目标字符串"/>
              </FormItem>
              <FormItem prop="cronExpression" label="cron表达式">
                <Input type="text" v-model.trim="jobForm.cronExpression" placeholder="请输入cron执行表达式">
                  <template slot="append">
                    <Button type="primary" @click="handleShowCron">
                      生成表达式
                      <i class="el-icon-time el-icon--right"></i>
                    </Button>
                  </template>
                </Input>
              </FormItem>
              <FormItem prop="misfirePolicy" label="错误策略">
                <RadioGroup v-model="jobForm.misfirePolicy" >
                  <Radio v-for="item in misfirePolicyList" :label="item.value" :key="item.value">
                    <span>{{item.label}}</span>
                  </Radio>
                </RadioGroup>
              </FormItem>
              <FormItem prop="concurrent" label="是否并发">
                <RadioGroup v-model="jobForm.concurrent" >
                  <Radio v-for="item in concurrentList" :label="item.value" :key="item.value">
                    <span>{{item.label}}</span>
                  </Radio>
                </RadioGroup>
              </FormItem>
              <FormItem prop="status" label="状态">
                <RadioGroup v-model="jobForm.status">
                  <Radio v-for="item in statusList" :label="item.itemCode.toLocaleString()" :key="item.itemCode.toLocaleString()">
                    <span>{{item.itemValue}}</span>
                  </Radio>
                </RadioGroup>
              </FormItem>
            </Col>
          </Row>
          <FormItem>
            <Row type="flex" justify="center" style="margin-top: 16px;">
              <Button type="primary" @click="handleSubmit('jobForm')" :disabled="isSubmitDisabled">提交</Button>
              <Button type="default" @click="handleCancel" style="margin: 0 16px">返回</Button>
            </Row>
          </FormItem>
        </Form>
      </Col>
    </Row>
    <el-dialog title="Cron表达式生成器" :visible.sync="openCron" append-to-body destroy-on-close class="scrollbar">
      <crontab @hide="openCron=false" @fill="crontabFill" :expression="expression"></crontab>
    </el-dialog>

  </div>
</template>

<script>
import Crontab from './Crontab'
import Util from '@/libs/util'

import {
  getDicItemsbyParams
} from '@/api/dic/listdic'
import {
  addQuartzByData,
  updateQuartzByData,
  queryQuartzById
} from '@/api/quartz/quartz'
export default {
  name: 'jobAddOrEdit',
  components: { Crontab },
  data () {
    return {
      // 是否显示弹出层
      open: false,
      // 是否显示详细弹出层
      openView: false,
      // 是否显示Cron表达式弹出层
      openCron: false,
      // 传入的表达式
      expression: '',
      isSubmitDisabled: false,
      jobForm: {
        jobId: undefined,
        jobName: undefined,
        jobGroup: undefined,
        invokeTarget: undefined,
        cronExpression: undefined,
        misfirePolicy: '1',
        concurrent: '1',
        status: '2'
      },
      concurrentList: [
        {
          label: '允许',
          value: '0'
        }, {
          label: '禁止',
          value: '1'
        }
      ],
      misfirePolicyList: [
        {
          label: '立即执行',
          value: '0'
        },
        {
          label: '执行一次',
          value: '1'
        },
        {
          label: '放弃执行',
          value: '2'
        }
      ],
      statusList: [],
      userOptions: [],
      jobGroups: [],
      ruleValidate: {
        jobName: [{ required: true, message: '任务名称不能为空!', trigger: 'blur' },
          { pattern: /^[\u4e00-\u9fa5a-zA-Z0-9]+$/, message: '仅支持中文，英文字母和数字' },
          {
            max: 60,
            message: '任务名称不可以超过64个字符',
            trigger: 'blur'
          }],
        jobGroup: [{ required: true, message: '任务组名为必选项!', trigger: 'blur' }],
        invokeTarget: [{ required: true, message: '调用方法不能为空!', trigger: 'blur' },
          {
            max: 500,
            message: '调用目标字符串长度不能超过500个字符',
            trigger: 'blur'
          }],
        cronExpression: [{ required: true, message: '表达式不能为空!', trigger: 'blur' },
          {
            max: 255,
            message: 'Cron执行表达式不能超过255个字符',
            trigger: 'blur'
          }]
      }
    }
  },
  methods: {
    init () {
      this.getDicStaus(8)
      this.getJobGroup(9)
      this.handleReset()
    },
    getDicStaus (dicType) {
      // const [url, httpConfig] = [
      //   '/api/dic/cscpHxDicItems/getItems',
      //   {
      //     params: {
      //       'dicAttr.equals': 'list',
      //       'dicCode.equals': 'status'
      //     }
      //   }
      // ]
      const dicParams = {
        'dicAttr.equals': 'list',
        'dicCode.equals': 'status'
      }
      getDicItemsbyParams(dicParams)
      // this.$http.get(url, httpConfig)
        .then(response => {
          this.statusList = response.data.data
        })
    },
    getJobGroup (dicType) {
      // const [url, httpConfig] = [
      //   '/api/dic/cscpHxDicItems/getItems',
      //   {
      //     params: {
      //       'dicAttr.equals': 'list',
      //       'dicCode.equals': 'taskName'
      //     }
      //   }
      // ]
      const dicParams = {
        'dicAttr.equals': 'list',
        'dicCode.equals': 'taskName'
      }
      getDicItemsbyParams(dicParams)
      // this.$http.get(url, httpConfig)
        .then(response => {
          this.jobGroups = response.data.data
        })
    },
    handleSubmit (jobForm) {
      this.$refs['jobForm'].validate((valid) => {
        if (valid) {
          const msg = this.$Message.loading({
            content: this.jobForm.id === null ? '正在创建新任务' : '正在更新任务',
            duration: 0
          })

          this.isSubmitDisabled = true
          // const [method, url, data] = [
          //   (this.jobForm.id === null) ? 'post' : 'put',
          //   '/api/jobs',
          //   {
          //     id: this.jobForm.id,
          //     jobName: this.jobForm.jobName,
          //     jobGroup: this.jobForm.jobGroup,
          //     invokeTarget: this.jobForm.invokeTarget,
          //     cronExpression: this.jobForm.cronExpression,
          //     misfirePolicy: this.jobForm.misfirePolicy,
          //     concurrent: this.jobForm.concurrent,
          //     status: this.jobForm.status
          //   }
          // ]
          const quartzData = {
            id: this.jobForm.id,
            jobName: this.jobForm.jobName,
            jobGroup: this.jobForm.jobGroup,
            invokeTarget: this.jobForm.invokeTarget,
            cronExpression: this.jobForm.cronExpression,
            misfirePolicy: this.jobForm.misfirePolicy,
            concurrent: this.jobForm.concurrent,
            status: this.jobForm.status
          }

          if (this.jobForm.id === null) {
            addQuartzByData(quartzData).then(response => {
              msg()
              this.$Message.success({
                content: '新建任务成功！',
                onClose: () => {
                  this.closeMe()
                }
              })
            }).catch(error => {
              msg()
              const data = error.response.data
              this.isSubmitDisabled = false
              if (error.response) {
                this.$Message.error('新建任务失败: ' + data.title)
              }
            })
          } else {
            updateQuartzByData(quartzData).then(response => {
              msg()
              this.$Message.success({
                content: '更新任务成功！',
                onClose: () => {
                  this.closeMe()
                }
              })
            }).catch(error => {
              msg()
              const data = error.response.data
              this.isSubmitDisabled = false
              if (error.response) {
                this.$Message.error('更新任务失败: ' + data.title)
              }
            })
          }
          // const httpConfig = { method, url, data }
          // this.$http(httpConfig).then(response => {
          //   msg()
          //   this.$Message.success({
          //     content: (this.jobForm.id === null) ? '新建任务成功！' : '更新任务成功！',
          //     onClose: () => {
          //       this.closeMe()
          //     }
          //   })
          // }).catch(error => {
          //   msg()
          //   const data = error.response.data
          //   this.isSubmitDisabled = false
          //   if (error.response) {
          //     this.$Message.error((this.jobForm.id === null) ? '新建任务失败: ' + data.title : '更新任务失败: ' + data.title)
          //   }
          // })
        }
      })
    },
    handleReset () {
      if (this.$route.name === 'job-edit') {
        this.jobForm.id = this.$byStoreGet('job-edit').id
        this.getData(this.jobForm.id)
      } else {
        this.jobForm = {
          id: null,
          jobName: null,
          jobGroup: null,
          invokeTarget: null,
          cronExpression: null,
          misfirePolicy: '2',
          concurrent: '1',
          status: '2'
        }
      }
    },
    getData (id) {
      this.isSubmitDisabled = true
      const msg = this.$Message.loading({
        content: '正在获取数据',
        duration: 0
      })
      queryQuartzById(id)
      // this.$http.get(`/api/jobs/${id}`)
        .then(response => {
          this.jobForm.jobName = response.data.jobName
          this.jobForm.jobGroup = response.data.jobGroup
          this.jobForm.invokeTarget = response.data.invokeTarget
          this.jobForm.cronExpression = response.data.cronExpression
          this.jobForm.misfirePolicy = response.data.misfirePolicy
          this.jobForm.concurrent = response.data.concurrent
          this.jobForm.status = response.data.status
          this.isSubmitDisabled = false
          msg()
        }).catch(error => {
          msg()
          this.isSubmitDisabled = false
          if (error.response) {
            this.$Message.error('数据获取失败！')
          }
        })
    },
    closeMe () {
      this.$store.commit('app/closePage', {
        vm: this,
        fromName: this.$route.name,
        toName: 'job-list'
      })
    },
    handleCancel () {
      this.closeMe()
      // this.$router.back()
    },
    /** cron表达式按钮操作 */
    handleShowCron () {
      this.expression = this.jobForm.cronExpression
      this.openCron = true
    },
    /** 确定后回传值 */
    crontabFill (value) {
      this.jobForm.cronExpression = value
    }
  },
  created () {
    this.init()
  }
}

</script>

<style></style>
