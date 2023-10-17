<template>
  <Modal
    v-model="modalVisible"
    title="数据库表导入"
    :width="780"
    @on-visible-change="change">
    <div style="padding: 20px">
      <RadioGroup v-model="tableName">
        <Radio v-for="(item,index) in tableArr" :label="item" :key="index"></Radio>
      </RadioGroup>
    </div>
    <div slot="footer">
      <Button size="large" @click="cancel">返回</Button>
      <Button type="primary" size="large" @click="ok">确定</Button>
    </div>
  </Modal>
</template>

<script>
import {
  getDbTableAll
} from '@/api/generator/generator'
export default {
  name: 'ImportDb',
  data () {
    return {
      modalVisible: false,
      tableArr: [],
      tableName: null
    }
  },
  methods: {
    ok () {
      if (this.tableName == null) {
        this.$Notice.error({
          title: '请先选择数据库表！'
        })
        return
      }
      this.modalVisible = false
      this.$parent.formInfoModal = true
      this.$parent.$refs['formDetailWeb'].initFormTableName(this.tableName)
    },
    change (flag) {
      if (!flag) {
        this.tableArr = []
        this.tableName = null
      } else {
        this.listDbTable()
      }
    },
    listDbTable () {
      let This = this
      getDbTableAll()
      // This.$http.get('/api/cscpHxTables')
        .then(function (response) {
          This.tableArr = response.data
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    cancel () {
      this.modalVisible = false
    }
  }

}
</script>

<style scoped>
  .ivu-radio-wrapper {
    width: 210px;
  }
</style>
