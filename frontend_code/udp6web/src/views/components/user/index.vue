<template>
  <div ref="div" width="100px">
    <div @click="clickChioseUser" class="ryzjContainer">
      <Input placeholder="请选择人员" v-model="fieldModel" readonly></Input>
    </div>
    <el-dialog
      :append-to-body="true"
      title="人员选择"
      :visible.sync="dialogVisible"
      :width="width">
      <user-table :fieldModel.sync="fieldModel" :multiSelect="multiSelect" ref="usertable"></user-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="clickConfirmeBtn">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import UserTable from '@/views/components/user/Usertable.vue'

export default {
  name: 'UserSelect',
  components: { UserTable },
  props: {
    multiSelect: {
      type: Boolean,
      required: false,
      default () {
        return true // 默认多选
      }
    },
    width: {
      type: String,
      required: false,
      default () {
        return '48%'
      }
    },
    value: {
      type: String
    }
  },
  watch: {
    fieldModel (val) {
      this.$emit('update:fieldModel', val)
    },
    value: {
      handler (newVal) {
        this.fieldModel = newVal
      },
      deep: true
    }
  },
  data () {
    return {
      fieldModel: null,
      dialogVisible: false
    }
  },
  methods: {
    clickChioseUser () {
      this.dialogVisible = true
    },
    clickConfirmeBtn () {
      let temArr = []
      this.$refs.usertable.selectList.forEach(item => {
        temArr.push(item.username)
      })
      this.fieldModel = temArr.join(',')
      this.dialogVisible = false
    }
  },
  mounted () {
    this.fieldModel = this.value
  }

}
</script>

<style lang="scss" scoped>
@import "~@/styles/global"; //* form-item-wrapper已引入，还需要重复引入吗？ *//
.el-dialog__wrapper {
  .el-dialog__body {
    padding: 30px 20px !important;
  }
}

.ryzjContainer {
  display: flex;
  align-items: center;

  span {
    width: 80px;
    flex-shrink: 0;
  }

  .vue-treeselect {
    flex: 1;
  }
}
</style>
