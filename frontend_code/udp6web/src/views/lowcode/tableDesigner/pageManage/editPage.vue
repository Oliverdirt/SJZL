<template>
  <div>
    <Card>
      <v-form-designer ref="vfDesigner" :designer-config="designerConfig" :field-list-api="fieldListApi">
        <!-- 自定义按钮插槽演示 -->
        <template #customToolButtons>
          <el-button type="text" @click="saveFormJson(false)"><i class="el-icon-finished"/>保存</el-button>
        </template>
      </v-form-designer>
      <div style="height:44px;"></div>
      <div class="btnClass">
        <Button type="primary" @click="saveFormJson(false)" style="width: 80px;margin-right:12px">确定
        </Button>
        <Button type="default" @click="close" style="width: 80px">取消</Button>
      </div>
    </Card>

  </div>
</template>

<script>
export default {
  name: 'editPage',
  props: {
    rowData: {
      type: Object
    },
    editPageCancel: {
      type: Function
    }
  },
  data() {
    return {
      detail: {},
      pageId: '',
      subPageFk: '',
      modelName: '',
      moduleName: '',
      createPageForm: {},
      pageJson: '',
      designerConfig: {
        resetFormJson: false,
        toolbarMaxWidth: 490
      },
      fieldListApi: {
        URL: this.$util.baseUrl + '/api/lowcode/customize/cscpCustomizeVfields/getListByPageId/' + this.pageId,
        labelKey: 'fieldComment',
        nameKey: 'fieldName',
        headers: { Authorization: localStorage.token }
      },
    }
  },


  created() {
   // let rowData = JSON.parse(window.localStorage.getItem('rowDataBackup'))
   // this.pageId = rowData.pageId
    this.pageId = this.rowData.pageId ? this.rowData.pageId : JSON.parse(window.localStorage.getItem('rowDataBackup'))
    this.fieldListApi.URL = this.$util.baseUrl + '/api/lowcode/customize/cscpCustomizeVfields/getListByPageId/' + this.pageId
  },
  methods: {
    saveFormJson () {
      let formJson = this.$refs.vfDesigner.getFormJson()
      // this.detail一共有23个参数,前面22个需要提前配置好
      this.detail.pageJson = JSON.stringify(formJson)
      this.detail.pageId = this.pageId
      this.pageJson = JSON.stringify(formJson)
      console.log('this.detail', this.detail)
      this.$http.put('/api/lowcode/customize/cscpCustomizeVpages', this.detail).then(response => {
        this.$message.success('表单已保存！')
        this.close()
      }).catch(err => {
        this.$Message.error(`修改失败：${err.response.data.msg}`)
      })
    },
    close () {
      window.localStorage.setItem('widget__list__backup', '')
      window.localStorage.setItem('form__config__backup', '')
      window.localStorage.setItem('rowDataBackup', '')
      this.detail = {}
      this.editPageCancel()
      // 返回页面管理
      //this.$router.push('/pageManage')
    }
  },
}
</script>

<style scoped>
.btnClass {
  display: flex;
  justify-content: right;
  right: 24px;
  bottom: 12px
}
</style>
