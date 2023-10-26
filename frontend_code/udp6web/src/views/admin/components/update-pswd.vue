<template>
  <div class="update-password">
    <div class="form-wrap">
      <h3>修改密码</h3>
      <Form ref="updatePswd" :model="updatePswd" :rules="ruleUpdatePswd" :label-width="80">

        <FormItem prop="tenantAccount" label="租户账号" v-show="$util.sassModel">
          <Input type="text" v-model="updatePswd.tenantAccount" placeholder="请输入租户账号" readonly>
          </Input>
        </FormItem>
        <FormItem prop="userName" label="用户账号">
          <Input type="text" v-model="updatePswd.userName" placeholder="请输入用户账号" readonly>
          </Input>
        </FormItem>
        <FormItem prop="oldPassword" label="原密码">
          <Input type="password" v-model="updatePswd.oldPassword" placeholder="请输入原密码">
          </Input>
        </FormItem>
        <FormItem prop="newPassword" label="新密码">
          <div style="position: relative;">
            <Input type="password" v-model="updatePswd.newPassword" placeholder="请输入新密码"></Input>
            <Tooltip style="position: absolute; right: 8px;" :content="passwordRule" max-width="320" transfer>
              <Icon type="md-help-circle" color="#ff9900" size="18" />
            </Tooltip>
          </div>
        </FormItem>
        <FormItem>
          <Button type="primary" :loading="loading" long @click="handleSubmit('updatePswd')">更新密码</Button>
          <Button style="margin-top: 8px;" long @click="cancel">取消</Button>
        </FormItem>
      </Form>
      <p class="tip">
        <Icon type="md-information-circle" size="18" color="#ff9900" />
        <span>
          您的密码存在一定风险，出于安全考虑，需更新密码后才能继续登录。
        </span>
      </p>
    </div>
  </div>
</template>

<script>
import {
  updateUserPasswordByData,
  getUserPasswordRule
} from '@/api/admin/user'
export default {
  data () {
    return {
      passwordRule: '',
      loading: false,
      updatePswd: {
        userName: '',
        newPassword: '',
        oldPassword: '',
        tenantAccount: this.$util.sassModel ? '' : 'default'
      },
      ruleUpdatePswd: {
        userName: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        tenantAccount: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
        oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }]
      }
    }
  },
  methods: {
    handleSubmit (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.loading = true
          this.submit()
        }
      })
    },
    submit () {
      let msg = this.$Message.loading({
        content: '正在更新密码',
        duration: 0
      })
      // const [url, data] = [
      //   '/api/system/cscpUserPassword',
      //   {
      //     userName: this.updatePswd.userName,
      //     tenantAccount: this.updatePswd.tenantAccount,
      //     newPassword: this.$util.encryptPassword(this.updatePswd.newPassword),
      //     oldPassword: this.$util.encryptPassword(this.updatePswd.oldPassword)
      //   }
      // ]
      const userDetailData = {
        userName: this.updatePswd.userName,
        tenantAccount: this.updatePswd.tenantAccount,
        newPassword: this.$util.encryptPassword(this.updatePswd.newPassword),
        oldPassword: this.$util.encryptPassword(this.updatePswd.oldPassword)
      }
      updateUserPasswordByData(userDetailData)
      // this.$http.put(url, data)
        .then(response => {
          msg()
          if (response.data.token) {
            localStorage.token = response.data.token
            this.$Message.success({
              content: '修改密码成功！',
              onClose: () => {
                this.$router.replace('/home')
              }
            })
          } else {
            this.loading = false
            this.$Message.error(`修改失败：${response.data.msg}`)
          }
        }).catch(err => {
          this.loading = false
          msg()
          this.$Message.error(`修改失败：${err.response.data.msg}`)
        })
    },
    cancel () {
      this.$router.replace('/home')
    },
    getPasswordRule () {
      getUserPasswordRule()
      // this.$http.get('/api/system/cscpUserPasswordRule')
        .then(res => {
          this.passwordRule = res.data
        })
    }
  },
  created () {
    this.getPasswordRule()
    this.updatePswd.userName = this.$byStoreGet('user-info').userName
    this.updatePswd.tenantAccount = this.$byStoreGet('user-info').tenantAccount
  }
}

</script>

<style lang="less" scoped>
  .update-password {
    position: relative;
    width: 100%;
    height: 100vh;
    background-color: #f8f8f9;
    .form-wrap {
      position: absolute;
      top: 40%;
      left: 50%;
      transform: translate(-50%, -50%);
      background-color: white;
      border-radius: 4px;
      width: 36%;
      min-width: 560px;
      padding: 32px;
      h3 {
        text-align: center;
        margin-bottom: 24px;
        border-bottom: 1px solid #e8eaec;
        padding-bottom: 4px;
      }
      .tip {
        margin-top: 24px;
        color: #808695;
        text-align: center;
      }
    }
  }
</style>
