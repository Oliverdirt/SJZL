<template>
    <div style=" height: calc(100vh - 120px);">

        <!--流程顶部操作区域,引入：ProcessHeader 组件-->
        <div class="process-action-button-top-area">
            <process-header :persistent-map="persistentMap" :actions-buttons="actionsButtons"
                            :user-node-key="userNodeKey"
                            :before-submit="beforeSubmit" :after-submit="afterSubmit"
                            ref="ProcessHeader"></process-header>
        </div>

        <!--表单区域-->
        <div class="form-area">
            <Form style="width: 900px" :model="model" :label-width="110" label-position="left" inline>
                            <FormItem :class="{'model-hidden': true}" label="文本框">
                    <Input v-model="model.id"  type="text"></Input>
                </FormItem>
                <FormItem :class="{'model-hidden': true}" label="文本框">
                    <Input v-model="model.userId"  type="text"></Input>
                </FormItem>
                <FormItem :class="{'model-hidden': true}" label="文本框">
                    <Input v-model="model.instanceId"  type="text"></Input>
                </FormItem>
                    <FormItem  label="请假天数">
                        <Input v-model="model.day" style="width:320px" type="text"></Input>
                    </FormItem>
                    <FormItem  label="请假原因">
                        <Input v-model="model.reason" style="width:320px" type="text"></Input>
                    </FormItem>

            </Form>
        </div>

        <!--底部意见区域，引入：ProcessBottom 组件-->
        <div class="process-comment-bottom-area">
            <process-bottom ref="ProcessBottom"></process-bottom>
        </div>
    </div>

</template>

<script>
import ProcessHeader from './components/web/ProcessHeader'
import ProcessBottom from './components/web/ProcessBottom'

export default {
  name: 'tLeaveForm',
  components: { ProcessHeader, ProcessBottom },
  data () {
    return {
      // 流程实例ID
      processInstanceId: null,
      // 流程任务ID
      taskId: null,
      // approve: null,

      // ID
      id: null,
      saveLoading: false,
      // 表单参数
      model: {
        id: null,
        day: null,
        reason: null,
        userId: null,
        instanceId: null
      }
    }
  },
  computed: {
    /// //////////////////////////////////////////////////////////////////////////////////
    // 以下是流程的ProcessHeader组件给出自定义属性，来满足一些业务，详情去了解流程的使用方式//
    /// /////////////////////////////////////////////////////////////////////////////////

    // 自定义操作成功返回路由地址，采用路由name名称去匹配,类型为String，默认为返回上一页
    backPathName () {
      return null
    },

    // 设定任务节点key，提交前打开人员选择框，进行指定nextHandle提交
    userNodeKey () {
      return []
    },
    // 自定义参数（会持久化）
    persistentMap () {
      return null
    },
    // 流程操作按钮

    actionsButtons () {
      return [
        // nodeKey 值与模型中任务环节的ID相同
        {
          nodeKey: 'UserTask_1xb6l5i',
          name: '提交1',
          expressionMap: {
            result: 1,
            day: this.model.day
          }
        }
      ]
    }
  },
  mounted () {
    /// ///////////////////////////////////////////////////////////////////////////////////
    // 模板采用的是路由传递流程实例ID，任务ID和表单数据ID,如果采用其他方式，请更改以下获取方法//
    /// ///////////////////////////////////////////////////////////////////////////////////
    if (typeof (this.$route.query.processInstanceId) !== 'undefined' && this.$route.query.processInstanceId !== null && this.$route.query.processInstanceId !== '') {
      this.processInstanceId = this.$route.query.processInstanceId
      this.model.instanceId = this.processInstanceId
      // ID存在渲染表单
      this.initForm()
    }
    if (typeof (this.$route.query.taskId) !== 'undefined' && this.$route.query.taskId !== null && this.$route.query.taskId !== '') {
      this.taskId = this.$route.query.taskId
    }

    if (typeof (this.$route.query.id) !== 'undefined' && this.$route.query.id !== null && this.$route.query.id !== '') {
      this.id = this.$route.query.id
    }
  },
  methods: {
    /// ///////////////////////////////////////////////////////////////////////////////////
    // 流程的ProcessHeader组件给出的钩子函数，流程提交之前的钩子，若返回 false 则停止提交流程//
    /// ///////////////////////////////////////////////////////////////////////////////////
    beforeSubmit () {
      // 流程提交之前先保存表单，如果没有采用此方案，可以按自己需求更改
      console.info(this)
      this.save()
    },

    /// ////////////////////////////////////////////////////////
    // 流程的ProcessHeader组件给出的钩子函数，流程提交之后的钩子//
    /// ///////////////////////////////////////////////////////
    afterSubmit () {
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
        '/api/tLeaves',
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
        '/api/tLeaves',
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
      _this.$http.get('/api/tLeaves?instanceId.equals=' + _this.model.instanceId).then(function (response) {
        let tem = response.data.data
        if (tem.length > 0) {
          _this.model.id = tem[0].id
          _this.model.day = tem[0].day
          _this.model.reason = tem[0].reason
          _this.model.userId = tem[0].userId
          _this.model.instanceId = tem[0].instanceId
        }
        console.log(_this.model.day)
        // console.log(this.approve);
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
        name: 't_leave'
      })
    }
  }
}
</script>

<style scoped>
    .button-area {
        display: flex;
        justify-content: flex-end;
    }

    .button-right-left {
        margin-right: 15px;
    }

    .button-right {
        margin-right: 55px;
    }

    .form-area {
        margin-top: 40px;
        display: flex;
        justify-content: center;
    }

    .form-flex {
        display: flex;
        justify-content: space-between;
    }
    .model-hidden{
        display: none;
    }
</style>
