<template >
    <div  style=" height: calc(100vh - 120px);">

        <!--表单区域-->
        <div class="form-area">
            <Form style="width: 900px" :model="model" :label-width="70" label-position="right" inline>

                <FormItem  label="请假天数">
                    <Input v-model="model.day" style="width:200px" type="text"></Input>
                </FormItem>
                <FormItem  label="出发地">
                  <Input v-model="model.fromArea" style="width:200px" type="text"></Input>
                </FormItem>
                <FormItem  label="目的地">
                  <Input v-model="model.toArea" style="width:200px" type="text"></Input>
                </FormItem>
                <FormItem  label="请假原因">
                    <Input v-model="model.reason" style="width:760px" type="textarea" :autosize="{minRows: 5,maxRows: 5}"></Input>
                </FormItem>

            </Form>
        </div>
      <div class="form-area">
        <el-button v-if="this.processInstanceId" size="small" type="primary" po="center" @click="handleFlow">处理</el-button>
        <el-button v-else size="small" type="primary" po="center" @click="approve">提交</el-button>
      </div>


    </div>

</template>

<script>

export default {
  name: 'approveForm',
  props: ["processDefinitionId","taskId","processInstanceId"],
  // props: ["processInstanceId"],
  data () {
    return {
      // 流程任务ID
      // approve: null,
      showUpload: false,
      dialogVisible: false,

      // ID
      id: null,
      saveLoading: false,
      // 表单参数
      model: {
        id: null,
        day: null,
        fromArea: null,
        toArea: null,
        reason: null,
        instanceId: null
      }
    }
  },
  mounted () {
    if (this.processInstanceId) {
      this.initForm()
    }

  },
  methods: {
    approve() {
      let queryParama = {
        processDefinitionId: this.processDefinitionId
      }
      this.$http.post('/api/flow/process/create', queryParama).then(res => {
        this.model.instanceId = res.data.result;
        this.create();
        this.$message.success('发起成功！')
        this.$router.back(-1)
      }).catch(err => {
        this.$message.error('发起失败！')
      })

    },
    handleFlow() {
      this.update ()
      let params = {
        //传入流程的变量
        flowMap: {},
        id: this.taskId,
        reason: this.model.reason
      }
      this.$http.put('/api/flow/core/approve', params).then((res) => {
          this.$Message.success('处理成功！')
          this.$emit('closed')
        }).catch((err) => {
          this.$Message.error('服务器错误！')
        this.$emit('closed')
        })
    },

    // 保存
    save () {
      let _this = this
      if (_this.model.id) {
        _this.update()
      } else {
        _this.create()
      }
    },

    create () {
      let _this = this
      _this.saveLoading = true
      const [url, httpConfig] = [
        '/api/approves',
        {
          headers: {
            'Content-Type': 'application/json'
          }
        }
      ]
      _this.$http.post(url, this.model, httpConfig).then(function (response) {
        _this.$Notice.success({
          title: '操作成功!'
        })
        //   _this.back()
      })
        .catch(function (error) {
          _this.$Notice.error({
            title: '操作失败！'
          })
          _this.saveLoading = false
          console.log(error)
        })
    },

    update () {
      let _this = this
      _this.saveLoading = true
      const [url, httpConfig] = [
        '/api/approves',
        {
          headers: {
            'Content-Type': 'application/json'
          }
        }
      ]
      _this.$http.put(url, this.model, httpConfig).then(function (response) {
        _this.$Notice.success({
          title: '操作成功!'
        })
        // _this.back()
      })

        .catch(function (error) {
          _this.$Notice.error({
            title: '操作失败！'
          })
          _this.saveLoading = false
          console.log(error)
        })
    },

    // 渲染表单
    initForm () {
      let _this = this
      _this.$http.get('/api/approves?instanceId.equals=' + _this.processInstanceId).then(function (response) {
        let tem = response.data.data
        if (tem.length > 0) {
          _this.model.id = tem[0].id
          _this.model.day = tem[0].day
          _this.model.fromArea = tem[0].fromArea
          _this.model.toArea = tem[0].toArea
          _this.model.reason = tem[0].reason
          _this.model.userId = tem[0].userId
          _this.model.instanceId = tem[0].instanceId
        }
      })
        .catch(function (error) {
          _this.$Notice.error({
            title: '获取失败！'
          })
          console.log(error)
        })
    },

    // 返回
    back () {
      let _this = this
      let tagName = _this.$route.name
      _this.$store.commit('removeTag', tagName)
      _this.$store.commit('closePage', tagName)
      _this.$router.push({
        name: 'approve'
      })
    }
  }
}
</script>

<style scoped>
    .form-area {
        margin-top: 40px;
        display: flex;
        justify-content: center;
    }
</style>
