<template>
    <div class="personalPortal">
        <div class="personalTop">
            <div style="display: flex;justify-content: space-between">
                <p>常用流程</p>
                <el-button style="margin-right: 10px" type="text" @click="dialogVisibleCollect = true">更多</el-button>
            </div>
            <div class="commonProcess">
                <div class="collectContentChild"
                    v-for="(item, i) in collecttableData" :key="'children' + i">
                    <div class="collectTop">
                        <img src="./images/Vector.svg" alt="">已收藏
                    </div>
                    <div class="collectMiddle" style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
                        <span :title="item.procDefName">【{{ item.procDefName }}】</span>
                    </div>
                    <div class="collectBottom" @click="launchCollect(i)">
                        <img src="./images/Frame 427319277.svg" alt="">申请
                    </div>
                </div>
            </div>
        </div>
        <div class="personalBottom">
            <div class="portalLeft">
                <div class="portalTop">
                    <p>待办转办</p>
                    <card>
                        <div class="todoList">
                            <!-- <el-tabs v-model="activeName" @tab-click="handleClick"> -->
                            <el-tabs v-model="activeName">
                                <el-tab-pane label="我的待办" name="first">
                                    <TodoList :imgData="imgData" @listenClickDetail="listenClickDetail" ref='TodoList' />
                                </el-tab-pane>
                                <el-tab-pane label="当前转办" name="second">
                                    <TransferList :imgData="imgData" ref='TransferList' />
                                </el-tab-pane>
                            </el-tabs>
                        </div>
                    </card>
                </div>
                <div class="portalBottom">
                    <p>流程速览</p>
                    <card>
                        <div class="todoList">
                            <el-tabs v-model="activeNameCopy">
                                <el-tab-pane label="我的已办" name="first">
                                    <DoneList @listenClickDetail="listenClickDetail" ref='DoneList' />
                                </el-tab-pane>
                                <el-tab-pane label="我的流程" name="second">
                                    <DoneProcess @listenClickDetail="listenClickDetail" ref='DoneProcess' />
                                </el-tab-pane>
                            </el-tabs>
                        </div>
                    </card>
                </div>
            </div>
            <div class="portalRight">
                <div style="display: flex;justify-content: space-between">
                    <p>流程申请</p>
                    <el-button style="margin-right: 10px;padding: 8px 0px" type="text" @click="dialogVisibleProcess = true">更多</el-button>
                </div>
                <card style="height:910px;overflow: hidden">
                    <div class="processApply">
                        <div class="processContentChild"
                            v-for="(item, i) in processtableData" :key="'children' + i">
                            <div class="childLeft">
                                <img src="./images/image 28.svg" alt="">
                                <span>{{ item.procDefName }}</span>
                            </div>
                            <div class="childRight">
                                <i style="color: #1890ff" class="el-icon-document" @click="launchProcess(i)"><span style="margin: 0px 12px 0px 5px;">申请</span></i>

                                <span class="clickCollect" @click.stop="submitCollect(i)">
                                    <i style="font-size: 16px" class="el-icon-star-on" v-if="item.isCollect == 1"><span style="margin: 0px 10px 0px 3px;font-size: 14px">已收藏</span></i>
                                    <i class="el-icon-star-off" v-else><span style="margin: 0px 10px 0px 4px">收藏</span></i>
                                </span>
                            </div>
                        </div>
                    </div>
                </card>
            </div>
        </div>
        <!--  流程申请弹窗  -->
        <el-drawer
            :title="detaileQueryParam.type"
            :visible.sync="dialogVisibleProcess"
            size="75%">
            <div class="processApplyMore">
                    <div class="processContentChildMore"
                        v-for="(item, i) in processtableData" :key="'children' + i">
                        <div class="childLeft">
                            <img src="./images/image 28.svg" alt="">
                            <span>{{ item.procDefName }}</span>
                        </div>
                        <div class="childRight">
                            <i style="color: #1890ff" class="el-icon-document" @click="launchProcess(i)"><span style="margin: 0px 12px 0px 5px;">申请</span></i>
                            <span class="clickCollect" @click.stop="submitCollect(i)">
                                <i style="font-size: 16px" class="el-icon-star-on" v-if="item.isCollect == 1"><span style="margin: 0px 10px 0px 3px;font-size: 14px">已收藏</span></i>
                                <i class="el-icon-star-off" v-else><span style="margin: 0px 10px 0px 4px">收藏</span></i>
                            </span>
                        </div>
                    </div>
                </div>
        </el-drawer>
        <!--  常用流程弹窗  -->
        <el-drawer
            :title="detaileQueryParam.type"
            :visible.sync="dialogVisibleCollect"
            size="75%">
            <div class="personalTop">
                <div class="collectContentFn" v-if="collecttableData.length === 0">
                    <span>暂无流程</span>
                </div>
                <div class="commonProcessCollect" v-else>
                    <div class="collectContentChild"
                        v-for="(item, i) in collecttableData" :key="'children' + i">
                        <div class="collectTop">
                            <img src="./images/Vector.svg" alt="">已收藏
                        </div>
                        <div class="collectMiddle" style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
                            <span :title="item.procDefName">【{{ item.procDefName }}】</span>
                        </div>
                        <div class="collectBottom" @click="launchCollect(i)">
                            <img src="./images/Frame 427319277.svg" alt="">申请
                        </div>
                    </div>
                </div>
            </div>
        </el-drawer>
        <el-drawer
            :title="detaileQueryParam.type"
            :visible.sync="dialogVisible"
            size="75%">
            <ProcessDetail v-if="dialogVisible" :detaileQueryParam="detaileQueryParam" @reloadPage="reloadPage" />
        </el-drawer>
    </div>
</template>
<script>
import request from '@/api/workflow/request'
import TodoList from './personComponents/todoList.vue'
import TransferList from './personComponents/transferList.vue'
import DoneList from './personComponents/personDoneList.vue'
import DoneProcess from './personComponents/personDoneProcess.vue'
import ProcessDetail from '@/views/workflow/approval/ProcessDetail'
export default {
  components: {
    TodoList,
    TransferList,
    DoneList,
    DoneProcess,
    ProcessDetail
  },
  data () {
    return {
      imgData: null,
      dialogVisibleProcess: false,
      dialogVisibleCollect: false,
      dialogVisible: false,
      collecttableData: [],
      processtableData: [],
      activeName: 'first',
      activeNameCopy: 'first',
      detaileQueryParam: { type: '' }
    }
  },
  created () {
    this.collectFn()
    this.processFn()
    this.imgFn()
  },
  methods: {
    imgFn () {
      request.getImgData()
      //   this.$http.get('/api/system/cscpCurrentUserDetails')
        .then(response => {
          this.imgData = response.data.imgPath
        }).catch()
    },
    init () {
      this.processFn()
      this.collectFn()
    },
    // 常用流程
    collectFn () {
      let param = {
        type: 2
      }
      request.getSelectActByUserId({ params: param })
        .then((response) => {
          this.collecttableData = response.data.data
          this.collecttableDataFour = response.data.data.slice(0, 4)
          this.collectListNum = response.data.data.length
        })
    },
    // 发起流程(常用流程)
    launchCollect (index) {
      let param = {
        proDefId: this.collecttableData[index].procDefId
      }
      // this.$http.get('/api/actRdCollects/launchProcess', { params: param })
      request.getLaunchProcess({ params: param })
        .then((response) => {
          this.$router.push({
            path: '/launchprocess',
            query: {
              formId: response.data.data.formId,
              processDefinitionId: response.data.data.processDefinitionId
            }
          })
        })
    },
    // 流程申请
    processFn () {
      let param = {
        type: 1
      }
      request.getSelectActByUserId({ params: param })
        .then((response) => {
          this.processtableData = response.data.data
          this.processListNum = response.data.data.length
        })
    },
    // 流程申请
    launchProcess (index) {
      let param = {
        proDefId: this.processtableData[index].procDefId
      }
      // this.$http.get('/api/actRdCollects/launchProcess', { params: param })
      request.getLaunchProcess({ params: param })
        .then((response) => {
          this.$router.push({
            path: '/launchprocess',
            query: {
              formId: response.data.data.formId,
              processDefinitionId: response.data.data.processDefinitionId
            }
          })
        })
    },
    // 点击收藏
    submitCollect (index) {
      if (this.processtableData[index].isCollect == 0) {
        let param = {
          proDefId: this.processtableData[index].procDefId,
          type: 1
        }
        // this.$http.get('/api/actRdCollects/addCollect', { params: param })
        request.addToCollection({ params: param })
          .then((response) => {
            this.$message({
              message: '已收藏',
              type: 'success',
              center: true
            })
            this.init()
          })
      } else {
        let param = {
          proDefId: this.processtableData[index].procDefId,
          type: 0
        }
        // this.$http.get('/api/actRdCollects/addCollect', { params: param })
        request.addToCollection({ params: param })
          .then((response) => {
            this.$message({
              message: '已取消收藏',
              type: 'success',
              center: true
            })
            this.init()
          })
      }
    },
    // 流程详情
    listenClickDetail (json) {
      this.detaileQueryParam = json
      this.dialogVisible = true
    },
    reloadPage () {
      window.location.reload()
    }
  }
}
</script>
<style lang="less" scoped>
.personalPortal{
    // max-height: 600px;
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    background-color: #f5f9ff;
    .personalTop{
        width: 100%;
        margin-bottom: 20px;
        p{
            height: 30px;
            font-weight: bold;
            margin-left: 10px;
        }
        .commonProcess, .commonProcessCollect{
            width: 100%;
            // height: 118px;
            display: flex;
            flex-wrap: wrap;
            overflow: hidden;
            justify-content: space-between;
            .collectContentChild{
                width: 224px;
                height: 118px;
                background: url('./images/Rectangle271.svg') no-repeat;
                background-size: 100% 100%;
                margin: 0px 6px;
                .collectTop{
                    margin-left: 20px;
                    display: flex;
                    align-items: center;
                    justify-content: space-around;
                    width: 60px;
                    height: 20px;
                    font-size: 8px;
                    color: rgba(250, 249, 249, 0.978);
                    background-image: linear-gradient(to right, #4c65ff,#85bfff);
                    border-bottom-right-radius: 5px;
                    border-bottom-left-radius: 5px;
                }
                .collectBottom{
                    margin-left: 20px;
                    font-size: 10px;
                    color: #4B72C0;
                    cursor: pointer;
                    display: flex;
                    align-items: center;
                    img{
                        margin-right: 10px;
                    }
                }
                .collectMiddle{
                    margin: 15px 0px 10px 12px;
                    color: #244AA2;
                }
            }
        }
        .commonProcess{
            height: 118px;
        }
        .commonProcessCollect{
            overflow: visible;
            justify-content: flex-start;
            .collectContentChild{
                margin: 6px 8px;
            }
        }
    }
    .personalBottom{
        background-color: #f5f9ff;
        display: flex;
        .portalLeft{
            margin-right: 20px;
            width: 65%;
            .portalTop{
                margin-bottom: 20px;
                p{
                    height: 30px;
                    font-weight: bold;
                    margin-left: 10px;
                }
            }
            .portalBottom{
                p{
                    height: 30px;
                    font-weight: bold;
                    margin-left: 10px;
                }
            }
        }
        .portalRight{
            // height: 500px;
            width: 35%;
            p{
                font-weight: bold;
                margin: 0px 0px 10px 10px;
            }
            .processApply{
                display: flex;
                flex-direction: column;
                align-items: center;
                .processContentChild{
                    border-radius: 5px;
                    width: 94%;
                    margin: 10px 0px;
                    background-color: #f4f7fd;
                    height: 90px;
                    display: flex;
                    align-items: center;
                    justify-content: space-between;
                    .childLeft{
                        display: flex;
                        align-items: center;
                        font-size: 16px;
                        font-weight: bold;
                        margin-right: 5px;
                        img{
                            width: 65px;
                            margin: 0px 15px 0px 5px
                        }
                    }
                    .childRight{
                        cursor: pointer;
                        font-size: 14px;
                        // font-weight: bold;
                    }
                }
            }
        }
    }
    .processApplyMore{
        display: flex;
        width: 100%;
        flex-wrap: wrap;
        .processContentChildMore{
            border-radius: 5px;
            width: 30%;
            margin: 10px 10px;
            background-color: #f4f7fd;
            height: 90px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            .childLeft{
                display: flex;
                align-items: center;
                font-size: 16px;
                font-weight: bold;
                img{
                    width: 65px;
                    margin: 0px 15px 0px 5px
                }
            }
            .childRight{
                cursor: pointer;
                font-size: 14px;
            }
        }
    }
}
</style>
