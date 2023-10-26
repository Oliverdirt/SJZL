<template>
    <Modal
            v-model="modalVisible"
            title="角色选择"
            :mask-closable="false"
            @on-ok="ok"
            @on-visible-change="change"
            @on-cancel="cancel">
        <div style="padding: 20px 30px 20px 0;">
            <CheckboxGroup v-model="roleCheck">
                <Checkbox v-for="(item,i) in roleArr" :label="item.id" :key="i">
                    <Icon :type="item.icon"></Icon>
                    <span>{{item.name}}</span>
                </Checkbox>
            </CheckboxGroup>
        </div>
    </Modal>
</template>

<script>
import util from '@/libs/util.js'

export default {
  name: 'RoleRuleSetWin',
  data () {
    return {
      modalVisible: false,
      role: null,
      roleCheck: [],
      roleArr: []
    }
  },
  mounted () {
    this.initRole()
  },
  watch: {
    role: function (value) {
      if (!this.modalVisible) return
      if (value.value !== null) {
        this.roleCheck = value.value
      }
    }
  },
  methods: {
    initRole () {
      let This = this
      util.http.get('/api/system/cscpRolessByCriteria?size=1000').then(response => {
        This.roleArr = response.data.data
      })
    },
    ok () {
      if (this.role !== null) {
        this.role.value = this.roleCheck
        this.$emit('refreshAssigneeRule', this.role)
      }
    },
    change (flag) {
      if (!flag) {
        this.role = null
        this.roleCheck = []
      }
    },
    cancel () {
    }
  }
}
</script>

<style scoped>

</style>
