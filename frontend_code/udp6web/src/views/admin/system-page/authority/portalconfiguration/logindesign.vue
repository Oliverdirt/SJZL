<template>
    <div style="height: 100%;display: flex;align-items: center;">
  <div class="login">
    <div class="loginLeft">
      <img style="width: 100%;height: 100%;" :src="imgData" alt="">
      <div class="loginBox">
        <div class="loginBoxContent">
            <p class="loginBoxText">{{loginBoxTextFn}}</p>
            <img style="width: 80%;height: 50%;margin-top: 10px;" src="./image/loginBox.png" alt="">
        </div>
      </div>
    </div>
    <div class="loginRight">
      <div style="margin-left: 50px">
        <div class="loginMsg">
            <div class="msgFirst">
                <div style="margin: 15px 0px 10px 0px">
                    <el-button type="primary" circle >1</el-button><span style="margin-left: 20px;color: black;font-weight: bold;font-size: 15px;">修改背景图</span>
                </div>
                <el-upload
                :before-upload="beforeAvatarUpload"
                :action="url"
                list-type="picture-card"
                :headers="uploadHeaders"
                :file-list="fileList"
                :on-success="onSuccess"
                :on-change="handFn"
                :on-remove="onRemove"
                :limit="1"
                :on-exceed='limitCheck'
                :class="{disabled: uploadDisabled}"
                >
                <i class="el-icon-plus"></i>
                </el-upload>
                <!-- <p>尺寸1920*930px</p> -->
            </div>
            <div class="msgSecond">
                <div style="margin: 0px 0px 10px 0px">
                    <el-button type="primary" circle>2</el-button><span style="margin-left: 20px;color: black;font-weight: bold;font-size: 15px;">修改项目名</span>
                </div>
                <el-input v-model="processName" type="text" id="processNameId"></el-input>
            </div>
        </div>
        <!--浏览器-->
        <div class="BrowserMsg">
            <div class="msgThird">
                <div style="margin: 0px 0px 10px 0px">
                    <el-button type="primary" circle >3</el-button><span style="margin-left: 20px;color: black;font-weight: bold;font-size: 15px;">修改浏览器logo</span>
                </div>
                <el-upload
                :before-upload="beforeAvatarUploadBrowser"
                :action="url"
                list-type="picture-card"
                :headers="uploadHeaders"
                :on-success="onBrowserSuccess"
                :on-change="handFn"
                :on-remove="onBrowserRemove"
                :file-list="BrowserFileList"
                :on-exceed='limitCheck'
                :limit="1"
                >
                <i class="el-icon-plus"></i>
                </el-upload>
                <!-- <p>尺寸1920*930px</p> -->
            </div>
            <div class="msgFour">
                <div style="margin: 0px 0px 10px 0px">
                    <el-button type="primary" circle >4</el-button><span style="margin-left: 20px;color: black;font-weight: bold;font-size: 15px;">修改浏览器系统名</span>
                </div>
                <el-input v-model="lebalName" type="text" id="lebalNameId"></el-input>
            </div>
            <div class="" style="margin: 30px 0px 0px 90px;">
                <el-button @click="apply" type="primary" >应用</el-button>
                <el-button @click="cancel" type="" >取消</el-button>
            </div>
        </div>
      </div>
    </div>
  </div>
</div>
</template>
<script>
export default {
  data () {
    return {
      uploadDisabled: false,
      processName: '',
      lebalName: '',
      uploadHeaders: {
        Authorization: localStorage.token
      },
      fileList: [{ url: '' }],
      BrowserFileList: [{ url: '' }],
      url: this.$util.baseUrl + '/api/system/uploadImage',
      imgData: ''
    }
  },
  created () {
    this.LoginQuery()
  },
  methods: {
    LoginQuery () {
      this.$http.get('/api/cscpPortalConfigureLogin').then((res) => {
        this.imgData = res.data.data.backgroundUrl ? res.data.data.backgroundUrl : require('@/assets/background.png')
        this.logoData = res.data.data.logoUrl ? res.data.data.logoUrl : 'http://10.2.16.78:31246/images/2013190292logo-min.png'
        this.processName = res.data.data.systemName ? res.data.data.systemName : '统一开发平台研发框架'
        this.lebalName = res.data.data.lebalName ? res.data.data.lebalName : '统一开发平台研发框架'
        this.fileList[0].url = this.imgData
        this.BrowserFileList[0].url = this.logoData
        // this.$refs['upload'].clearFiles()
      })
    },
    apply () {
      let processNameValue = document.getElementById('processNameId').value
      if (processNameValue.length > 20) {
        this.$message.warning('项目名长度不能超过20')
        return
      }
      let lebalNameValue = document.getElementById('lebalNameId').value
      if (lebalNameValue.length > 20) {
        this.$message.warning('浏览器logo名长度不能超过20')
        return
      }
      if (!this.logoData) {
        this.$Message.warning('浏览器logo不能为空')
        return
      }
      if (!this.lebalName) {
        this.$Message.warning('浏览器系统名不能为空')
        return
      }
      if (this.imgData) {
        if (this.processName) {
          let params = {
            'backgroundUrl': this.imgData,
            'logoUrl': this.logoData,
            'lebalName': this.lebalName,
            'systemName': this.processName
          }
          this.$http.post('/api/cscpPortalConfigureLogins', params).then((res) => {
            this.$Message.success('应用成功！')
          })
        } else {
          this.$Message.warning('项目名称不能为空')
        }
      } else {
        this.$Message.warning('登录背景不能为空')
      }
    },
    beforeAvatarUpload (file) {
      // 上传图片类型
      const name = file.name
      const index = name.lastIndexOf('.')
      const imgType = name.substring(index)
      if (imgType === '.jpg' || imgType === '.jpeg' || imgType === '.png' || imgType === '.PNG') {
        console.log(imgType)
      } else {
        this.$message.error(`图片格式仅支持.png、.jpg、.jpeg!`)
        return false
      }
      // 上传图片大小
      const isLt5M = file.size / 1024 / 1024 < 5
      if (!isLt5M) {
        this.$message.error('上传背景图大小不能超过 5MB!')
      }
      return isLt5M
    },
    beforeAvatarUploadBrowser (file) {
      // 上传图片类型
      const nameBrowser = file.name
      const indexBrowser = nameBrowser.lastIndexOf('.')
      const imgTypeBrowser = nameBrowser.substring(indexBrowser)
      if (imgTypeBrowser === '.jpg' || imgTypeBrowser === '.jpeg' || imgTypeBrowser === '.png' || imgTypeBrowser === '.PNG' || imgTypeBrowser === '.ico') {
        console.log(imgTypeBrowser)
      } else {
        this.$message.error(`图片格式仅支持.png、.jpg、.jpeg、.ico!`)
        return false
      }
      // 上传图片大小
      const isLt1M = file.size / 1024 / 1024 < 1
      if (!isLt1M) {
        this.$message.error('上传logo大小不能超过 1MB!')
      }
      return isLt1M
    },
    cancel () {
      window.location.reload()
    },
    onSuccess (res, file, lilelist) {
      this.imgData = res
    },
    onBrowserSuccess (res, file, lilelist) {
      this.logoData = res
    },
    onRemove (file, fileList) {
      this.imgData = ''
    },
    onBrowserRemove (file, fileList) {
      this.logoData = ''
    },
    handFn (file, fileList) {
    },
    limitCheck () {
      this.$message.warning('每次只能上传一个文件')
    }
  },
  computed: {
    loginBoxTextFn () {
      return this.processName
    }
  }
}
</script>
<style lang="less" scoped>

.login {
  display: flex;
//   justify-content: space-between;
  height: 98%;
  width: 100%;
  .loginLeft {
    height: 100%;
    flex: 2.8;
    // width: 70%;
    background-size: 100% 100%;
    display: flex;
    align-items: center;
    position: relative;
    .loginBox {
      width: 30%;
      height: 44%;
      position: absolute;
      right: 40px;
      background-color: #999cac;
      border-radius: 2%;
      display: flex;
      align-items: center;
      justify-content: center;
      .loginBoxContent{
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-items: center;
        .loginBoxText{
            margin-bottom: 10px;
            color: black;
            font-weight: bold;
        }
      }
    }
  }
  .loginRight {
    height: 100%;
    background-color: rgb(218, 229, 238);
    flex: 1;
    display: flex;
    // justify-content: center;
    overflow-y: auto;
    .loginMsg{
        border-bottom: 1px solid rgb(184, 184, 184);
        padding-bottom: 30px;
        margin-bottom: 30px;
    }
    ::v-deep .el-upload--picture-card{
        width: 100px;
        height: 100px;
        transition: none;
    }

    ::v-deep .el-upload-list__item, .el-upload-list--picture-card{
        width: 100px;
        height: 100px;
        // transition: none;
    }
    ::v-deep .el-button--primary{
         padding: 5px 8px 5px 8px;
    }
    .el-icon-plus {
        position: relative;
        bottom: 18px;
    }
  }
}

</style>
