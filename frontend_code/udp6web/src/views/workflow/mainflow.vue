<template>
  <div class="mainflow">
      <div class="flowtop">
          <div class="processLeft">
              <div class="createModel">
                  <p>开始创建新的流程模型吧 !</p>
                    <img @click="addForm" src="./images/Frame 427319004.svg" alt="">
              </div>
              <p>可以通过点击新建数据模型来建立完整的流程模型哦</p>
          </div>
          <div class="formRight">
              <img src="./images/Office work-rafiki 1.svg" alt="">
          </div>
      </div>
      <div class="flowbottom">
          <el-card>
              <div class="processTabs">
                  <div style="display:flex;justify-content: center;align-items: center;">
                    <p>流程模型管理</p>
                    <div style="width: 200px"><el-input type="text" placeholder="输入流程名称可快速查询哦" v-model="modelData.modelName" @input="queryList(true)"></el-input></div>
                  </div>
                  <div style="display: flex;">
                      <!-- <div style="display: flex;align-items: center;cursor: pointer;justify-content: space-around;color:rgb(69, 124, 224)" @click="handleImport">
                          <img src="./images/clarity_import-solid.svg" alt="">导入流程
                      </div> -->
                      <span style="display: flex;align-items: center;cursor: pointer;" @click="processTab(2)" :class="{ active: cur == 2}">
                        <i style="font-size:21px" class="el-icon-s-operation"></i>列表模式
                      </span>
                      <span style="display: flex;align-items: center;cursor: pointer;margin: 0px 15px 0px 25px;" @click="processTab(1)" :class="{ active: cur == 1}">
                        <i style="font-size:21px" class="el-icon-menu"></i>卡片模式
                      </span>
                  </div>
              </div>
          </el-card>
              <!--卡片-->
              <div  v-show ="cur === 1" >
                <div class="processCard">
                    <div class="cardChild" v-for="(item,index2) of tableData" :key="index2">
                        <el-card>
                            <img :src="imgData(item)" alt="" style="position:absolute;right:80px;top: 75px;cursor: pointer;" @click="progressFn(item)">
                            <div class="cardTop">
                                <div style="display: flex;width: 55%;">
                                  <div style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis;font-size: 20px;font-weight: bold;margin-right: 20px;" :title="item.name">{{ item.name }}</div>
                                  <el-tag size="medium" v-if="item.processDefinition">已发布</el-tag>
                                  <el-tag size="medium" type="warning" v-else>未发布</el-tag>
                                </div>
                                <div class="childTopRight">
                                  <div style="display: flex;align-items: center;" @click="handleAssignRule(item)">
                                    <img src="./images/material-symbols_edit-document-rounded.svg" alt="">分配规则
                                  </div>
                                  <div style="display: flex;align-items: center;margin: 0px 20px;" @click="publishDesign(item)">
                                    <img src="./images/ic_round-publish.svg" alt="">发布
                                  </div>
                                  <div style="display: flex;align-items: center;">
                                    <el-dropdown>
                                      <span class="el-dropdown-link" style="color:rgb(69, 124, 224);font-size: 14px;display: flex;align-items: center;">
                                        <img src="./images/material-symbols_more-rounded.svg" alt="">操作
                                      </span>
                                      <div>
                                        <el-dropdown-menu slot="dropdown" v-if="item.progressBar !== 'D'">
                                          <el-dropdown-item @click.native="progressFn(item)" command="f">继续创建</el-dropdown-item>
                                          <el-dropdown-item command="e" @click.native="deleteDesign(item)">删除流程</el-dropdown-item>
                                        </el-dropdown-menu>
                                        <el-dropdown-menu slot="dropdown" v-else>
                                          <el-dropdown-item @click.native="formChoose(item)" command="a">表单选择</el-dropdown-item>
                                          <el-dropdown-item command="b" @click.native="modelEdit(item)">模型设计</el-dropdown-item>
                                          <el-dropdown-item command="c" @click.native="beforeHandleEdit(item)">表单设计</el-dropdown-item>
                                          <el-dropdown-item command="d" @click.native="handleDesign(item)">流程设计</el-dropdown-item>
                                          <el-dropdown-item command="e" @click.native="authorizationManagement(item)">权限管理</el-dropdown-item>
                                          <el-dropdown-item command="f" @click.native="deleteDesign(item)">删除流程</el-dropdown-item>
                                        </el-dropdown-menu>
                                      </div>
                                    </el-dropdown>
                                  </div>
                                </div>
                            </div>
                            <div class="cardMiddle">
                                <p style="font-weight: bold;color: #333333">基础信息</p>
                                <div style="display: flex;margin-top: 10px;">
                                    <div style="width: 25%;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
                                        <p>流程标识</p>
                                        <span :title="item.key">{{ item.key }}</span>
                                    </div>
                                    <div style="width: 25%;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
                                        <p>表单信息</p>
                                        <span :title="item.formName">{{ item.formName }}</span>
                                    </div>
                                    <div style="width: 40%">
                                        <p>创建时间</p>
                                        <span>{{ item.createTime }}</span>
                                    </div>
                                </div>
                            </div>
                            <div class="cardBottom" v-if="item.processDefinition">
                                <p style="font-weight: bold;color: #333333;">部署信息</p>
                                <div style="display: flex;margin-top: 10px;">
                                    <div style="width: 25%">
                                        <p>流程版本</p>
                                        <div style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
                                            <el-tag size="medium" v-if="item.processDefinition">v{{ item.processDefinition.version }}
                                            </el-tag>
                                            <el-tag size="medium" type="warning" v-else>未部署</el-tag>
                                        </div>
                                    </div>
                                    <div style="width: 25%;">
                                        <p>激活状态</p>
                                        <div>
                                            <el-switch v-if="item.processDefinition" v-model="item.processDefinition.suspensionState"
                                            :active-value="1" :inactive-value="2" @change="handleChangeState(item)" />
                                        </div>
                                    </div>
                                    <div style="width: 20%">
                                        <p>是否可终止</p>
                                        <div>
                                            <el-switch v-if="item.processDefinition" v-model="item.backState" :active-value="1"
                                            :inactive-value="0" @change="changeBackState(item)" />
                                        </div>
                                    </div>
                                    <div style="width: 30%">
                                        <p>部署时间</p>
                                        <div>
                                            <span v-if="item.processDefinition">{{item.processDefinition.deploymentTime}}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="cardBottom" v-else>
                            </div>
                        </el-card>
                    </div>
                  </div>
              </div>
              <!--列表-->
              <div v-show ="cur === 2" class="processList">
                  <div class="processChild"  v-for="(item,index) of tableData" :key="index">
                    <el-card>
                      <img :src="imgData(item)" alt="" style="position:absolute;right:83px;top: 70px;cursor: pointer;" @click="progressFn(item)">
                      <div class="childTop">
                          <div style="display: flex;width: 60%">
                            <span style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis;font-size: 20px;font-weight: bold;margin-right: 20px;" :title="item.name">{{ item.name }}</span>
                              <el-tag size="medium" v-if="item.processDefinition">已发布</el-tag>
                              <el-tag size="medium" type="warning" v-else>未发布</el-tag>
                          </div>
                          <div class="childTopRight">
                            <div style="display: flex;align-items: center;margin-left: 20px;" @click="handleAssignRule(item)">
                              <img src="./images/material-symbols_edit-document-rounded.svg" alt="">分配规则
                            </div>
                            <div style="display: flex;align-items: center;margin: 0px 20px;" @click="publishDesign(item)">
                              <img src="./images/ic_round-publish.svg" alt="">发布
                            </div>
                            <div style="display: flex;align-items: center;">
                                <el-dropdown>
                                <span class="el-dropdown-link" style="color:rgb(69, 124, 224);font-size: 14px;display: flex;align-items: center;">
                                  <img src="./images/material-symbols_more-rounded.svg" alt="">操作
                                </span>
                                <div>
                                  <el-dropdown-menu slot="dropdown" v-if="item.progressBar !== 'D'">
                                    <el-dropdown-item @click.native="progressFn(item)" command="f">继续创建</el-dropdown-item>
                                    <el-dropdown-item command="e" @click.native="deleteDesign(item)">删除流程</el-dropdown-item>
                                  </el-dropdown-menu>
                                  <el-dropdown-menu slot="dropdown" v-else>
                                    <el-dropdown-item @click.native="formChoose(item)" command="a">表单选择</el-dropdown-item>
                                    <el-dropdown-item command="b" @click.native="modelEdit(item)">模型设计</el-dropdown-item>
                                    <el-dropdown-item command="c" @click.native="beforeHandleEdit(item)">表单设计</el-dropdown-item>
                                    <el-dropdown-item command="d" @click.native="handleDesign(item)">流程设计</el-dropdown-item>
                                    <el-dropdown-item command="e" @click.native="authorizationManagement(item)">权限管理</el-dropdown-item>
                                    <el-dropdown-item command="f" @click.native="deleteDesign(item)">删除流程</el-dropdown-item>
                                  </el-dropdown-menu>
                                </div>
                              </el-dropdown>
                            </div>
                          </div>
                      </div>
                      <div class="childMiddle" style="margin: 10px 0px;">
                          <span>流程标识<span style="margin-left: 10px;color:#333333;" :title="item.key">{{ item.key }}</span></span>
                          <span style="margin: 0px 30px">表单信息<span style="margin-left: 10px;color:#333333">{{ item.formName }}</span></span>
                          <span>创建时间<span style="margin-left: 8px;color:#333333">{{ item.createTime }}</span></span>
                      </div>
                      <div class="childBottom" v-if="item.processDefinition">
                          <div>
                              <span>流程版本</span>
                              <el-tag size="medium" v-if="item.processDefinition">v{{ item.processDefinition.version }}
                              </el-tag>
                              <el-tag size="medium" type="warning" v-else>未部署</el-tag>
                          </div>
                          <div style="margin: 0px 25px">
                              <span>激活状态</span>
                              <el-switch v-if="item.processDefinition" v-model="item.processDefinition.suspensionState"
                              :active-value="1" :inactive-value="2" @change="handleChangeState(item)" />
                          </div>
                          <div>
                              <span>是否可终止</span>
                              <el-switch v-if="item.processDefinition" v-model="item.backState" :active-value="1"
                              :inactive-value="0" @change="changeBackState(item)" />
                          </div>
                          <div style="margin-left: 25px">
                              <span>部署时间</span>
                              <span style="color:#333333" v-if="item.processDefinition">{{
                                  item.processDefinition.deploymentTime
                              }}</span>
                          </div>
                      </div>
                      <div class="childBottom" v-else>
                      </div>
                    </el-card>
                  </div>
              </div>
              <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                  :current-page="pageData.page" :page-sizes="[6, 10, 20, 30]" :page-size="6"
                  layout="total, sizes, prev, pager, next, jumper" :total="pageData.total">
              </el-pagination>
          <!-- </el-card> -->
      </div>
      <!--权限管理弹框-->
      <el-dialog title="流程发起权限" :visible.sync="authorizationVisible" width="550px" append-to-body>
        <el-form label-width="80px" :model="authpersonnelResize" ref="authpersonnelResize">
          <el-form-item label="流程标识" prop="key">
            <el-input v-model="authpersonnelResize.key" placeholder="请输入流标标识" disabled />
          </el-form-item>
          <el-form-item label="流程名称" prop="name">
            <el-input v-model="authpersonnelResize.name" placeholder="请输入流程名称" disabled />
          </el-form-item>
          <el-form-item label="操作权限" prop="type">
            <el-select v-model="authpersonnelResize.type" @change="authpersonnelResizeTypeChange">
              <el-option v-for="option in authpersonnelOptions" :value="option.value" :key="option.value"
                :label="option.label">
              </el-option>
            </el-select>
          </el-form-item>
          <!--人员-->
          <el-form-item v-show="authpersonnelResize.type === 1" label="指定角色" prop="authroleIds">
            <el-select ref="roleSelect" v-model="authpersonnelResize.authroleIds" multiple>
              <el-option v-for="option in operaterole" :value="option.value" :key="option.value" :label="option.label">
              </el-option>
            </el-select>
          </el-form-item>
          <!--用户-->
          <el-form-item v-show="authpersonnelResize.type === 2" label="指定用户" prop="authpersonneIds">
            <el-select ref="userSelect" v-model="authpersonnelResize.authpersonneIds" multiple filterable>
              <el-option v-for="(option, index) in userOptions" :value="option.value" :key="option.value + index"
                :label="option.label">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitAuthorization">确 定</el-button>
            <el-button @click="cancelAuthorization">取 消</el-button>
        </div>
      </el-dialog>
      <!-- 用户导入对话框 -->
      <el-dialog title="导入流程" :visible.sync="upload.open" width="400px" append-to-body>
          <el-upload ref="upload" :limit="1" accept=".bpmn, .xml, .BPMN, .XML" :headers="upload.headers"
          :action="upload.url" :disabled="upload.isUploading" :on-progress="handleFileUploadProgress"
          :on-success="handleFileSuccess" :on-error="handleFileError" :auto-upload="false" name="bpmnFile"
          :data="upload.form" drag style="margin-left: 4px">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text" style="margin-left: 4px !important">
              将文件拖到此处，或
              <em>点击上传</em>
          </div>
          <div class="el-upload__tip" style="color: red; text-align: center; margin: 0px 0px 15px 0px" slot="tip">
              提示：仅允许导入“bpmn”或“xml”格式文件！
          </div>
          <div class="el-upload__tip" slot="tip">
              <el-form style="margin-right: 5px !important" ref="uploadForm" size="mini" label-width="80px"
              :model="upload.form" @submit.native.prevent>
              <el-form-item label="流程描述" prop="description">
                  <el-input type="textarea" v-model="upload.form.description" clearable />
              </el-form-item>
              </el-form>
          </div>
          </el-upload>
          <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="uploadClose">取 消</el-button>
          </div>
      </el-dialog>
      <!-- 分配规则弹窗 -->
      <el-dialog title="任务分配规则" :visible.sync="Assignvisible" width="1124px">
        <el-tabs v-model="activeName" type="card" @tab-click="handleTabClick">
          <el-tab-pane v-for="(item, index) in tabsList" :label="item.title" :name="item.name"
            :key="index">
            <!-- 放点击修改展示的form表单 -->
            <el-form :model="formResize" ref="formResize">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="任务名称" label-width="85px" prop="taskDefinitionName">
                    <el-input v-model="formResize.taskDefinitionName" disabled />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="任务标识" label-width="85px" prop="taskDefinitionKey">
                    <el-input v-model="formResize.taskDefinitionKey" disabled/>
                  </el-form-item>
                </el-col>
              </el-row>
                <el-form-item label="流程标识" label-width="85px" prop="taskDefinitionFlag">
                  <el-input v-model="formResize.taskDefinitionFlag" disabled/>
                </el-form-item>
                              <!--111-->
              <el-form-item v-show="intersectionNum > 1" label="交并集" prop="intersection" label-width="85px">
                  <el-radio v-model="formResize.radio" label="0">交集</el-radio>
                  <el-radio v-model="formResize.radio" label="1">并集</el-radio>
              </el-form-item>
              <el-form-item label="处理人配置" prop="type" label-width="85px">
                <el-select v-model="personnelResize.type" placeholder="请选择" multiple @change="typeChange">
                  <el-option v-for="(option, index2) in personnelOptions" :key="'ry' + index2" :label="option.label"
                    :value="option.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <!--人员-->
              <el-form-item v-show="typeChangeValue.indexOf(1) !== -1" label="指定人员" prop="personneIds" label-width="85px">
                <el-select v-model="personnelResize.personneIds" placeholder="请选择" multiple>
                  <el-option v-for="(option, index) in userOptions" :key="option.value + index" :label="option.label"
                    :value="option.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <!--角色-->
              <el-form-item v-show="typeChangeValue.indexOf(2) !== -1" label="指定角色" prop="roleIds" label-width="85px">
                <el-select ref="roleSelect" v-model="personnelResize.roleIds" placeholder="请选择" multiple>
                  <el-option v-for="option in operaterole" :key="option.value" :label="option.label"
                    :value="option.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <!--部门-->
              <el-form-item v-show="typeChangeValue.indexOf(3) !== -1" label="指定部门" prop="department" label-width="85px">
                <treeselect v-model="personnelResize.department" :options="depTreeData" multiple
                  :value-consists-of="valueConsistsOf" :normalizer="normalizer" placeholder="请选择指定部门" />
              </el-form-item>
              <!--自定义脚本-->
              <el-form-item v-show="typeChangeValue.indexOf(4) !== -1" label="指定脚本" prop="customize" label-width="85px">
                <el-select ref="customizeSelect" v-model="personnelResize.customize" placeholder="请选择" multiple>
                  <el-option v-for="option in customizeSelect" :key="option.value" :label="option.label"
                    :value="option.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <!--222-->
              <el-form-item label="节点表单" prop="intersection" label-width="85px">
                  <el-radio v-model="formResize.radioNode" label="0" @change="radioNodeFn">是</el-radio>
                  <el-radio v-model="formResize.radioNode" label="1" @change="radioNodeFn">否</el-radio>
              </el-form-item>
              <el-form-item label="表单选择" prop="formId" label-width="85px" v-if="radioNodeVisible">
                <el-select v-model="formResize.formId" clearable placeholder="请选择已有表单" filterable>
                    <el-option v-for="item in formArray" :key="item.formId" :label="item.formName" :value="item.formId">
                    </el-option>
                </el-select>
              </el-form-item>
              <!--操作权限-->
              <!-- <el-form-item label="操作权限" prop="name" label-width="85px">
                <el-select v-model="formResize.id" placeholder="请选择" multiple>
                  <el-option v-for="(option, index1) in roleOptions" :key="'qx' + index1" :label="option.label"
                    :value="option.value">
                  </el-option>
                </el-select>
              </el-form-item> -->
              <!-- <el-form-item label="可编辑字段" label-width="85px" v-if="!radioNodeVisible">
                <el-checkbox-group v-model="formResize.fieldName">
                  <el-checkbox @change="fieldNameFn" v-for="(option, index3) in editFieldOptions" :label="option.value" :key="index3">{{
                      option.label
                  }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
              <el-form-item label="可显示字段" label-width="85px"  v-if="!radioNodeVisible">
                <el-checkbox-group v-model="formResize.showFieldName">
                  <el-checkbox @change="showFieldNameFn" v-for="(option, index) in showFieldOptions" :label="option.value" :key="index">{{
                      option.label
                  }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item> -->
              <el-row v-if="!radioNodeVisible">
                <el-col :span="13">
                  <el-form-item label="字段配置" prop="name" label-width="85px">
                    <el-transfer v-model="formResize.fieldName" :titles="['字段列表', '可编辑字段']" @change="handleChange" :data="editFieldOptions"></el-transfer>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-transfer v-model="formResize.showFieldName" :titles="['字段列表', '可显示字段']" @change="handleChangeCopy" :data="showFieldOptions"></el-transfer>
                </el-col>
              </el-row>
              <!--自定义按钮-->
              <el-form-item label="自定义按钮" prop="name" label-width="85px">
                <el-table :data="dataArr" :header-cell-style="{ 'background-color': '#d3e1f7' }" :row-style="{ height: '51px' }"  style="width: 100%">
                    <!-- <el-table-column type="selection" width="55"></el-table-column> -->
                    <el-table-column type="index" prop="operationCode" label="方法名"  width="260">
                      <template v-slot="scope">
                        <el-select v-model="scope.row.operationCode" placeholder="请选择" @change="operationCodeFn($event,scope.row.key,scope.row.id)">
                          <el-option v-for="option in operationCodeArr" :value="option.operationCode" :key="option.operationCode" :label="option.operationCode">
                          </el-option>
                        </el-select>
                      </template>
                    </el-table-column>
                    <el-table-column type="index" prop="operationName" label="默认名称"  width="260">
                      <template v-slot="scope">
                        <el-input v-model="scope.row.operationName">
                        </el-input>
                      </template>
                    </el-table-column>
                    <el-table-column prop="operationCode" label="操作" >
                      <template v-slot="scope">
                        <el-button v-if="scope.row.id" @click="attributeFn(scope.row)" size="mini" type="text">属性</el-button>
                        <el-button size="mini" type="text" @click="deleteFn(scope.row)">删除</el-button>
                      </template>
                    </el-table-column>
                </el-table>
                <el-button style="width: 100%;margin-left: 0" icon="el-icon-plus" plain @click="addFn"
                             type="primary">新建
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitResize">确 定</el-button>
          <el-button @click="cancelAssignForm">取 消</el-button>
        </div>
      </el-dialog>
      <!-- 属性弹窗 -->
      <el-dialog title="属性" :visible.sync="AttributeVisible" width="600px">
        <el-table :data="attDataArr" :header-cell-style="{ 'background-color': '#d3e1f7' }" :row-style="{ height: '51px' }"  style="width: 100%">
            <el-table-column type="index" prop="paramKey" label="参数键"  width="200">
              <template v-slot="scope">
                <el-input v-model="scope.row.paramKey">
                </el-input>
              </template>
            </el-table-column>
            <el-table-column type="index" prop="paramValue" label="参数值"  width="200">
              <template v-slot="scope">
                <el-input v-model="scope.row.paramValue">
                </el-input>
              </template>
            </el-table-column>
            <el-table-column prop="operationCode" label="操作" >
                <template v-slot="scope">
                  <el-button size="mini" type="text" @click="attDeleteFn(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-button style="width: 100%;margin-left: 0" icon="el-icon-plus" plain @click="attFn"
                             type="primary">新建
        </el-button>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitAttribute">确 定</el-button>
          <el-button @click="cancelAttribute">取 消</el-button>
        </div>
      </el-dialog>
      <flowSteps ref="flowStepsRef"></flowSteps>
      <flowStepsBj ref="flowStepsBjRef"></flowStepsBj>
  </div>
</template>
<script>
import flowSteps from './flowSteps.vue'
import flowStepsBj from './flowStepsBj.vue'
import request from '@/api/workflow/request'
export default ({
components: {
  flowSteps,
  flowStepsBj
},
data () {
  return {
    radioNodeVisible: false,
    formArray: [],
    AttributeVisible: false,
    dataArr: [],
    attDataArr: [],
    operationCodeArr: [],
    progressBar: '',
    intersectionNum: 0,
      personneIdsData: {},
      roleIdsData: {},
      departmentData: {},
      customizeData: {},
      typeChangeValue: [],
      editFlag: false,
      tabsList: [],
      activeName: '',
      valueConsistsOf: 'ALL',
      formResize: {
        taskDefinitionName: '',
        taskDefinitionKey: '',
        taskDefinitionFlag: '',
        id: [],
        formName: '',
        formTable: '',
        fieldName: [],
        showFieldName: [],
        idFlag: '',
        unionFlag: '',
        radio: '0',
        radioNode: '1'
      },
      personnelResize: {
        flowTaskAssignRuleBaseList: [],
      },
      authpersonnelResize: {},
      saveModelId: '',
      ids: '',
      fieldId: [],
      modelData: {
        modelKey: '',
        modelName: ''
      },
      open: false,
      form: {},
      cur: 2,
      tableData: [],
      pageData: {
        total: 0,
        pageSize: 6,
        pageNum: 1
      },
      userOptions: [],
      operaterole: [],
      customizeSelect: [
        {
          value: '10',
          label: '流程发起人'
        }
      ],
      depTreeData: [],
      intersectionUnion: [
        {
            value: 0,
            label: '交集'
        },
        {
           value: 1,
           label: '并集'
        },
      ],
      personnelOptions: [
        {
          value: 1,
          label: '人员'
        },
        {
          value: 2,
          label: '角色'
        },
        {
          value: 3,
          label: '部门'
        },
        {
          value: 4,
          label: '自定义脚本'
        }
      ],
      authpersonnelOptions: [
        {
          value: 1,
          label: '角色'
        },
        {
          value: 2,
          label: '用户'
        }
      ],
      // 表单校验
      rules: {
        key: [{ required: true, message: '流程标识不能为空', trigger: 'blur' }],
        name: [{ required: true, message: '流程名称不能为空', trigger: 'blur' }],
        formType: [{ required: true, message: '请选择表单类型', trigger: 'blur' }],
        formId: [{ required: true, message: '请选择表单', trigger: 'blur' }]
      },
      // 流程导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: {
          Authorization: localStorage.token
        },
        // 上传的地址
        url: this.$util.baseUrl + '/api/flow/bpmn/import',
        // 表单
        form: {}
      },
      userRuleData: [],
      options: [],
      formTypeOpts: [
        {
          label: '自定义表单',
          value: '1'
        },
        {
          label: '路由表单',
          value: '2'
        }
      ],
      editFieldOptions: [],
      showFieldOptions: [],
      modal1: false,
      typeDisabled: false,
      form: {
        formType: '1'
      },
      open: false,
      Assignvisible: false,
      authorizationVisible: false,
      formOpen: false,
      title: '',
      currentTaskDefinitionKey: ''
  }
},
created () {
  this.queryList()
},
methods: {
  handleChange(value, direction, movedKeys) {
    if(direction == 'right'){
      this.arrAll = [...this.formResize.showFieldName, ...movedKeys]
      this.formResize.showFieldName = []
      for (var i = 0;i < this.arrAll.length; i++){
        if(this.formResize.showFieldName.indexOf(this.arrAll[i]) == -1){
          this.formResize.showFieldName.push(this.arrAll[i])
        }
      }
    }
  },
  handleChangeCopy(value, direction, movedKeys) {
        console.log(value, direction, movedKeys,'456');
        if(direction == 'left'){
          // this.transferValue = value
          movedKeys.forEach((item) =>{
            if(this.formResize.fieldName.includes(item)){
              this.formResize.fieldName = this.formResize.fieldName.filter(i => i != item)
            }
          })
        }
  },
  radioNodeFn(row){
    if(row == 0){
      this.radioNodeVisible = true
    }else{
      this.radioNodeVisible = false
    }
  },
  // 按钮属性配置
  attributeFn(row){
    this.operationId = row.id
    this.AttributeVisible = true
    let params = {
      operationId: row.id
    }
    this.$http.get('/api/flowNodeParams/queryNodeParam',{params: params}).then((res)=>{
      this.attDataArr = res.data.result
    })
  },
  attFn(val){
      let mapKong = {
        key: Math.random() + '',
        paramKey: '',
        paramValue: '',
      }
      this.attDataArr.push(mapKong)
      console.log(this.attDataArr,'this.attDataArr')
  },
  attDeleteFn(val){
      this.attDataArr = this.attDataArr.filter((item) => {
        if(item.id){
          return item.id != val.id
        }else{
          return item.key != val.key
        }
      })
  },
  cancelAttribute(){
    this.AttributeVisible = false
  },
  submitAttribute(){
    // 参数键校验
    let paramKeyArr = []
      for(let i = 0; i < this.attDataArr.length; i++){
        if(!this.attDataArr[i].paramKey || !this.attDataArr[i].paramValue){
          this.$message.error('参数键或参数值不能为空')
          return
        }
        if(paramKeyArr.indexOf(this.attDataArr[i].paramKey) > -1){
          this.$message.error('参数键不能重复')
          return
        }
        paramKeyArr.push(this.attDataArr[i].paramKey)
      }
    let params = {
      operationId: this.operationId,
      nodeParamList: this.attDataArr
    }
    this.$http.post('/api/flowNodeParams/createOrUpdateNodeParam', params).then((res)=>{
      this.$message.success('保存成功')
      this.AttributeVisible = false
    })
  },
  // 自定义按钮
  operationCodeFn(row,key,id){
    // 获取方法对应的默认名称
    let methodsName = this.operationCodeArr.filter(item => item.operationCode == row)[0].operationName
    this.dataArr.map((item)=>{
      if(item.key && item.key == key){
        item.operationName = methodsName
      }else if(item.id && item.id == id){
        item.operationName = methodsName
      }
      item.nodeKey = this.formResize.taskDefinitionKey
      return item
    })
  },
  //新增自定义按钮字段
  addFn(val){
      let mapKong = {
        key: Math.random() + '',
        id: '',
        nodeKey: '',
        operationCode: '',
        operationName: '',
        paramFlag: '',
      }
      this.dataArr.push(mapKong)
  },
  // 新增自定义按钮字段
  deleteFn (val) {
      this.dataArr = this.dataArr.filter((item) => {
        if(item.id){
          return item.id != val.id
        }else{
          return item.key != val.key
        }
 
      })
  },
  // 针对已完成流程修改每一步 
  // 1表单选择
  formChoose(row){
    this.$http.get('/api/lowcode/customize/cscpCustomizeVforms/' + row.formId).then((res)=>{
        let rowData = res.data
        rowData.id = row.id
        rowData.key = row.key
        rowData.name = row.name
        rowData.description = row.description
        this.$refs.flowStepsBjRef.step = 0
        this.$refs.flowStepsBjRef.flowStepsVisiable = true
        this.$nextTick(()=>{
        this.$refs.flowStepsBjRef.$refs.createdModelRef.formChooseFn(rowData)
      })
    })
  },
  // 2修改模型
  modelEdit(row){
    this.$refs.flowStepsBjRef.modelDesignFn(true)
    this.$http.get('/api/lowcode/customize/cscpCustomizeVforms/' + row.formId).then((res)=>{
        let rowData = res.data
        rowData.id = row.id
        rowData.key = row.key
        rowData.name = row.name
        this.$refs.flowStepsBjRef.step = 1
        this.$refs.flowStepsBjRef.flowStepsVisiable = true
        this.$nextTick(()=>{
        this.$refs.flowStepsBjRef.$refs.modelDesignRef.modelEditFn(rowData)
      })
    })
  },
  // 3编辑表单
  beforeHandleEdit(row){
    this.formTableFn(row.formId)
  },
  // 根据formId查模型名称
  formTableFn(params){
      this.$http.get('/api/lowcode/customize/cscpCustomizeVforms/' + params).then((res)=>{
        this.$refs.flowStepsBjRef.step = 2
        this.$refs.flowStepsBjRef.formDesignTab = true
        this.$refs.flowStepsBjRef.flowStepsVisiable = true
        this.$refs.flowStepsBjRef.initSecondStep(res.data)
        // this.$router.push({ path: '/formDesign', query: { formRow: res.data, routeFlag: 1 } })
      })
    },
  // 4设计流程
  handleDesign(row) {
      // this.$router.push({
      //   path: '/modelEditor',
      //   query: {
      //     modelId: row.id,
      //     processId: row.key,
      //     processName: row.name
      //   }
      // })
      this.$refs.flowStepsBjRef.step = 3
      this.$refs.flowStepsBjRef.flowStepsVisiable = true
      this.$refs.flowStepsBjRef.modelRowFn(row)
  },
  // 针对未完成流程继续创建
  // 进度条入口
  progressFn(row){
    if(row.progressBar == 'A'){
      this.$refs.flowStepsBjRef.step = 1
      this.$refs.flowStepsBjRef.flowStepsVisiable = true
      this.$refs.flowStepsBjRef.createdModelSaveFn()
      this.$nextTick(()=>{
        this.$refs.flowStepsBjRef.$refs.modelDesignRef.pageIdFn(row.formId)
      })
      this.$refs.flowStepsBjRef.pageIdSon(row)
    }else if(row.progressBar == 'B'){
      this.$refs.flowStepsBjRef.step = 2
      this.$refs.flowStepsBjRef.formDesignTab = false
      this.$refs.flowStepsBjRef.flowStepsVisiable = true
      this.$nextTick(()=>{
        this.$refs.flowStepsBjRef.$refs.listConfigRef.rowFn(row)
      })
    }else if(row.progressBar == 'C'){
      this.$refs.flowStepsBjRef.step = 3
      this.$refs.flowStepsBjRef.flowStepsVisiable = true
      this.$refs.flowStepsBjRef.modelRowFn(row)
    }else if(row.progressBar == 'D'){
    }
  },
  imgData(row){
    if(row.progressBar == 'A'){
      return require('./images/Group 1437-2.svg')
    }else if(row.progressBar == 'B'){
      return require('./images/Group 1437-3.svg')
    }else if(row.progressBar == 'C'){
      return require('./images/Group 1437.svg')
    }else if(row.progressBar == 'D'){
      return require('./images/Group 1437-1.svg')
    }
  },
    //新建流程
    addForm(){
        this.$refs.flowStepsRef.flowStepsVisiable = true
        this.$refs.flowStepsRef.step = 0
    },
  cancel(){
      this.open = false
  },
  // 切换
  processTab (num) {
   if(num == 1){
      this.cur = 1
   }else{
      this.cur = 2
   }
  },
  queryList (inquire) {
      if(inquire){
      this.pageData.pageNum = 1
    }
    let obj = {}
    obj['key'] = this.modelData['modelKey']
      ? this.modelData['modelKey']
      : null
    obj['name'] = this.modelData['modelName']
      ? this.modelData['modelName']
      : null
    let params = Object.assign(obj, {
      page: this.pageData.pageNum,
      size: this.pageData.pageSize,
      category: 'lowCode'
    })
    // this.$http
    //   .get('/api/flow/bpmn/page', {
    //     params: params
    //   })
    request.getCscpFlowBpmnPageList({ params: params })
      .then((res) => {
        this.tableData = res.data.result.list
        this.pageData.total = res.data.result.total
      })
      .catch(() => {
        this.$Message.error('列表查询失败')
      })
  },
  // 发布流程
  publishDesign(row) {
      this.$Modal.confirm({
        title: '提示',
        content: '<p>是否发布该流程！</p>',
        onOk: () => {
          // this.$http
          //   .post('/api/flow/bpmn/deploy?id=' + row.id)
          request.publishBpmnById(row.id)
            .then((res) => {
              this.$Message.success('发布成功')
              this.queryList()
            })
            .catch((error) => {
              this.$Message.error('发布失败:' + error.response.data.detail)
            })
        },
        onCancel: () => { }
      })
  },
  // 分配规则
  handleAssignRule(row) {
    if(row.progressBar != 'D'){
      this.$alert('流程尚未完成 , 请完成后进行此操作 !', '提示', {
          confirmButtonText: '确定'
        })
        return
    }
    console.log(row,'e');
    this.$http.get('/api/flowHandle/listAllOperation').then((res)=>{
      this.operationCodeArr = res.data.result
    })
      if (!row.formId) {
        this.$Message.error('请先绑定表单!')
        return
      }
      this.saveModelId = row.id
      this.handleAssignRuleRn = row
      this.formResize.formName = row.formName
      // 调用接口查询
      let obj = {}
      obj['modelId'] = row.id
      obj['processDefinitionId'] = undefined
      let params = Object.assign(obj)
      // this.$http
      //   .get('/api/flow/rule/list', { params: params })
      request.getRuleList({ params })
        .then((res) => {
          console.log(res,'res1');
          res?.data?.result.map((item) => (item.taskDefinitionFlag = row.key))
          this.userRuleData = res.data?.result
          this.tabsList = this.userRuleData?.map((item) => {
            let obj = {}
            // obj.id = item.idradio
            obj.id = item.id
            obj.title = item.taskDefinitionName
            obj.name = item.taskDefinitionKey
            return obj
          })
          // 默认展示第一个tab
          if (this.tabsList && this.tabsList.length) {
            this.activeName = this.tabsList[0].name
            this.handleTabClick({ name: this.activeName })
          }
        })
        .catch(() => {
          this.$Message.error('任务表查询失败')
        })
      this.Assignvisible = true
  },
  handleTabClick(tab) {
    this.formResize.fieldName = []
    this.formResize.showFieldName = []
    let param = tab.name
    this.$http.post(`/api/flowNodeOperations/findNodeOperation?nodeKey=${param}`).then((res)=>{
      this.dataArr = res.data.result
    })
    console.log(tab,'tab1');
      const tabName = tab.name
      let obj = {}
      obj['modelId'] = this.handleAssignRuleRn.id
      obj['processDefinitionId'] = undefined
      let params = Object.assign(obj)
      request.getRuleList({ params })
        .then((res) => {
          res?.data?.result.map((item) => (item.taskDefinitionFlag = this.handleAssignRuleRn.key))
          this.userRuleData = res.data?.result
          this.tabsList = this.userRuleData?.map((item) => {
            let obj = {}
            obj.id = item.id
            obj.title = item.taskDefinitionName
            obj.name = item.taskDefinitionKey
            return obj
          })
          const row = this.userRuleData.filter(
            (item) => item.taskDefinitionKey === tabName
          )
          this.reviseFn(row[0])
        })
        .catch(() => {
          this.$Message.error('任务表查询失败2')
        })
    },
        // 点击修改
    reviseFn(row) {
      this.formResize.radio = ''
       if(row.unionFlag){
        this.formResize.radio = row.unionFlag + ''
      }else{
        this.formResize.radio = '0'
      }
      // 操作配置
      this.formResize.taskDefinitionName = row.taskDefinitionName
      this.formResize.taskDefinitionKey = row.taskDefinitionKey
      this.formResize.taskDefinitionFlag = row.taskDefinitionFlag
      this.ids = row.id
      let params = {
        taskDefinitionKey: row.taskDefinitionKey,
        taskDefinitionFlag: row.taskDefinitionFlag
      }
      // 操作权限数据回显
      // this.$http
      //   .get('/api/flow/rule/taskPerm-select', { params: params })
      // request.getRuleTaskPermList({ params })
      //   .then((response) => {
      //     this.formResize.idFlag = ''
      //     this.formResize.id = []
      //     if (response?.data?.result[0]) {
      //       this.formResize.idFlag = response.data.result[0].id
      //       if (response?.data?.result[0].perms) this.formResize.id = response.data.result[0].perms.split(',')
      //       const temp = this.formResize.id.filter(item => item)
      //       this.formResize.id = temp
      //     }
      //   })

      // 人员
      this.personnelResize = {
        ...row,
        personneIds: [],
        roleIds: [],
        department: [],
        customize: [],
        options: [],
      }
      let typeData = this.personnelResize.flowTaskAssignRuleBaseList.map((item)=>item.type).filter((item)=>{
        return String(item) !== 'null'
      })
      this.personnelResize.type = typeData
      // 规则类型多选联动
      this.typeChange(this.personnelResize.type)
      if(this.personnelResize.flowTaskAssignRuleBaseList.length > 0){
        if(this.personnelResize.flowTaskAssignRuleBaseList[0].type){
            this.personnelResize.personneIds.push(...this.personnelResize.flowTaskAssignRuleBaseList[0].options)
        }
      }
      if(this.personnelResize.flowTaskAssignRuleBaseList.length > 0){
        if(this.personnelResize.flowTaskAssignRuleBaseList[1].type){
          this.personnelResize.roleIds.push(...this.personnelResize.flowTaskAssignRuleBaseList[1].options)
        }
      }
      if(this.personnelResize.flowTaskAssignRuleBaseList.length > 0){
        if(this.personnelResize.flowTaskAssignRuleBaseList[2].type){
          this.personnelResize.department.push(...this.personnelResize.flowTaskAssignRuleBaseList[2].options)
        }
      }
      if(this.personnelResize.flowTaskAssignRuleBaseList.length > 0){
        if(this.personnelResize.flowTaskAssignRuleBaseList[3].type){
          this.personnelResize.customize.push(...this.personnelResize.flowTaskAssignRuleBaseList[3].options)
        }
      }
      // 操作字段权限控制
      let qryTableNameParam = {
        formName: this.formResize.formName ? this.formResize.formName : ''
      }
      // this.$http
      //   .get('/api/lowcode/customize/cscpCustomizeVforms/listOne', {
      //     params: qryTableNameParam
      //   })
      request.getCscpCustomizeVformsListOne({
        params: qryTableNameParam
      })
        .then((response) => {
          console.log(response,'response1');
          this.initField(response.data.formTable)
        })
      // 333操作字段权限查询
      this.currentTaskDefinitionKey = row
      // this.getKeyName()
      this.getFieldKeyName()
      this.init()
      this.formArrayFn()
    },
        // 已有表单下拉数据
    formArrayFn () {
      const params = {
        formType: '1',
        formName: ''
      }
      // this.$http
      //   .get('/api/lowcode/customize/cscpCustomizeVforms/getByFormType', {
      //     params: params
      //   })
      request.getCscpCustomizeVformsList({ params })
        .then((res) => {
          this.formArray = res.data
        })
        .catch(() => {
          this.$Message.error('表单查询失败')
        })
    },
    getFieldKeyName() {
      // this.$http
      //   .get('/api/queryFieldPerms', {
      //     params: {
      //       taskDefinitionKey: this.currentTaskDefinitionKey.taskDefinitionKey,
      //     }
      //   })
      request.getFieldPermsList({
        params: {
          taskDefinitionKey: this.currentTaskDefinitionKey.taskDefinitionKey,
        }
      })
        .then((res) => {
          if(res.data.result.formName){
            // this.formResize.formId = res.data.result.formId
            this.$set(this.formResize,'formId',res.data.result.formId)
            this.formResize.radioNode = '0'
            this.radioNodeVisible = true
          }else{
            this.formResize.radioNode = '1'
            this.radioNodeVisible = false
            let temp = res.data?.result?.fieldsPerms || []
            const fieldNameArr = temp.map(item => {
              if (item.editFlag === '1') {
                return item.fieldId
              }
            })
            const showFieldNameArr = temp.map(item => {
              if (item.showFlag === '1') {
                return item.fieldId
              }
            })
            this.formResize.fieldName = fieldNameArr
            this.formResize.showFieldName = showFieldNameArr
            // console.log(this.formResize.fieldName,'s12');
          }
      })
    },
    init() {
      // 人员
      // this.$http
      //   .get('/api/system/cscpUserDetailsOr')
      request.getCscpUserDetailList()
        .then((response) => {
          const op = []
          for (let item of response.data.data) {
            op.push({
              value: item.userId,
              label: `${item.username} `
            })
          }
          this.userOptions = op
        })
        .catch()
      // 角色
      // const [url, httpConfig] = [
      //   '/api/system/cscpRolessByCriteria',
      //   {
      //     params: { size: 1000 }
      //   }
      // ]
      // this.$http
      //   .get(url, httpConfig)
      request.getCscpRoleList({ params: { size: 1000 } })
        .then((response) => {
          this.operaterole = []
          for (const item of response.data.data) {
            this.operaterole.push({
              value: item.id.toString(),
              label: item.name,
              icon: item.icon
            })
          }
        })
        .catch()
      // 部门
      // this.$http
      //   .get('/api/cscpDepts/treeselect')
      request.getCscpDeptsTreeselectList()
        .then((response) => {
          this.depTreeData = this.getTree(response.data)
        })
        .catch()
    },
    // 部门
    getTree(tree) {
      let arr = []
      if (!!tree && tree.length !== 0) {
        tree.forEach((item) => {
          let obj = {}
          obj.label = item.label
          obj.id = item.id // 其他你想要添加的属性
          obj.expand = true
          obj.selected = false
          obj.children = this.getTree(item.children) // 递归调用
          arr.push(obj)
        })
      }
      return arr
    },
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
    },
    // filterNode(value, data) {
    //   if (!value) return true
    //   return data.label.indexOf(value) !== -1
    // },
    // 字段下拉数据
    initField(tableName) {
      let params = {
        tableName: tableName ? tableName : ''
      }
      // this.$http
      //   .get('/api/getTableOperateFields', { params: params })
      request.getTableOperateFieldsList({ params })
        .then((response) => {
          this.editFieldOptions = []
          this.showFieldOptions = []
          for (const item of response.data) {
            this.editFieldOptions.push({
              key: item.columnName,
              value: item.columnName,
              label: item.columnComment
            })
            this.showFieldOptions.push({
              key: item.columnName,
              value: item.columnName,
              label: item.columnComment
            })
          }
        })
        .catch()
    },
    // 可编辑字段联动
        fieldNameFn(row,c){
      if(row){
        if(this.formResize.showFieldName.indexOf(c.target.value) == -1){
          this.formResize.showFieldName.push(c.target.value)
        }
      }
    },
    showFieldNameFn(row,b){
      if(!row){
        this.formResize.fieldName = this.formResize.fieldName.filter((item)=> item !== b.target.value)
      }
    },
    typeChange(row){
      this.typeChangeValue = row
      this.intersectionNum = row.length
    },
    // 分配规则确定
    submitResize() {
      // 默认名称校验
      let operationNameArr = []
      console.log(this.dataArr,'this.dataArr');
      for(let i = 0; i < this.dataArr.length; i++){
        if(!this.dataArr[i].operationName || !this.dataArr[i].operationCode){
          this.$message.error('方法名或默认名称不能为空')
          return
        }
        if(operationNameArr.indexOf(this.dataArr[i].operationName) > -1){
          this.$message.error('默认名称不能重复')
          return
        }
        operationNameArr.push(this.dataArr[i].operationName)
      }
      let params = {
        nodeKey: this.formResize.taskDefinitionKey,
        operationList: this.dataArr
      }
      this.$http.post('/api/flowNodeOperations/createOrUpdateNodeOperation', params).then((res)=>{
        console.log(res,'save');
      })
      let fieldsPerms = this.editFieldOptions.map(item => {
        let obj = {}
        obj.fieldId = item.value
        obj.editFlag = this.formResize.fieldName.includes(item.value) ? '1' : '0'
        obj.showFlag = this.formResize.showFieldName.includes(item.value) ? '1' : '0'
        return obj
      })
      let that = this
      // const params1 = {
      //   taskDefinitionKey: this.formResize.taskDefinitionKey,
      //   taskDefinitionName: this.formResize.taskDefinitionName,
      //   taskDefinitionFlag: this.formResize.taskDefinitionFlag,
      //   perms: !this.formResize.id ? '' : this.formResize.id.join(','),
      //   id: this.formResize.idFlag || ''
      // }
      // 人员
      if(this.personnelResize.type.indexOf(1) !== -1){
        this.personneIdsData = {options: this.personnelResize.personneIds,type: 1}
        if(this.personneIdsData.options.length == 0){
          this.personneIdsRules = false
        }else{
          this.personneIdsRules = true
        }
      }else{
        this.personneIdsData = {options: [],type: ''}
        this.personneIdsRules = true
      }
      // 角色
      if(this.personnelResize.type.indexOf(2) !== -1){
        this.roleIdsData = {options: this.personnelResize.roleIds,type: 2}
        if(this.roleIdsData.options.length == 0){
          this.roleIdsDataRules = false
        }else{
          this.roleIdsDataRules = true
        }
      }else{
        this.roleIdsData = {options: [],type: ''}
        this.roleIdsDataRules = true
      }
      // 部门
      if(this.personnelResize.type.indexOf(3) !== -1){
        this.departmentData = {options: this.personnelResize.department,type: 3}
        if(this.departmentData.options.length == 0){
          this.departmentDataRules = false
        }else{
          this.departmentDataRules = true
        }
      }else{
        this.departmentData = {options: [],type: ''}
        this.departmentDataRules = true
      }
      //自定义
      if(this.personnelResize.type.indexOf(4) !== -1){
        this.customizeData = {options: this.personnelResize.customize,type: 4}
        if(this.customizeData.options.length == 0){
          this.customizeDataRules = false
        }else{
          this.customizeDataRules = true
        }
      }else{
        this.customizeData = {options: [],type: ''}
        this.customizeDataRules = true
      }
      let flowTaskAssignRuleBaseList = [this.personneIdsData,this.roleIdsData,this.departmentData,this.customizeData]
      let personnelResize = {
        ...this.personnelResize,
        flowTaskAssignRuleBaseList: flowTaskAssignRuleBaseList,
        unionFlag: this.formResize.radio
      }
      let arrayRules = [this.personneIdsRules, this.roleIdsDataRules,this.departmentDataRules,this.customizeDataRules]
      if(arrayRules.indexOf(false) !== -1){
        this.$message.error('处理人配置不能为空')
        return
      }
      // let promise1 = this.$http.post('/api/flow/rule/taskPerm-update', params1)
      // let promise1 = request.updateTaskPerm(params1)
      let promise2 = {}
      if(this.radioNodeVisible){
        this.params3 = {
          taskDefinitionKey: this.formResize.taskDefinitionKey,
          taskDefinitionName: this.formResize.taskDefinitionName,
          formId: that.formResize.formId,
          formName: that.handleAssignRuleRn.formName,
          fieldsPerms: [],
          // 33
        }
      }else{
        this.params3 = {
          taskDefinitionKey: this.formResize.taskDefinitionKey,
          taskDefinitionName: this.formResize.taskDefinitionName,
          formId: that.handleAssignRuleRn.formId,
          fieldsPerms: fieldsPerms,
        }
      }
      // const params3 = {
      //   taskDefinitionKey: this.formResize.taskDefinitionKey,
      //   taskDefinitionName: this.formResize.taskDefinitionName,
      //   formId: that.handleAssignRuleRn.formId,
      //   fieldsPerms: fieldsPerms,
      // }
      // 替换成新的接口兼容可显示字段
      // let promise3 = this.$http.post(
      //   '/api/updateFieldsPerm',
      //   params3
      // )
      let promise3 = request.updateFieldsPerm(this.params3)
      if (!personnelResize.id) {
        personnelResize.modelId = that.saveModelId || ''
        // promise2 = this.$http.post(
        //   '/api/flow/rule/assign-create',
        //   personnelResize
        // )
        promise2 = request.createAssign(personnelResize)
      } else {
        // promise2 = this.$http.put('/api/flow/rule/update', personnelResize)
        promise2 = request.updateAssign(personnelResize)
      }

      // Promise.all([promise1, promise2, promise3])
      Promise.all([promise2, promise3])
        .then((res) => {
          const resolve2 = res.filter(item => item.config.url === '/api/flow/rule/assign-create')
          if (resolve2 && resolve2.length > 0) {
            let personnelResizeId = resolve2[0].data.result
            this.personnelResize.id = personnelResizeId
          }
          this.$message({
            message: '修改成功',
            type: 'success',
            duration: 3000
          })
        })
        .catch(() => {
          this.$message({
            message: '修改失败',
            type: 'error',
            duration: 3000
          })
        })
    },
    // 分配规则取消
    cancelAssignForm() {
      this.personnelResize = {}
      if (this.$refs?.formResize && this.$refs?.formResize.length) {
        this.$refs?.formResize[0].resetFields()
      }
      this.Assignvisible = false
      this.formResize.id = []
    },
  // 激活状态
  handleChangeState (row) {
    const id = row.id
    let state = row.processDefinition.suspensionState
    let statusState = state === 1 ? '激活' : '挂起'
    this.$Modal.confirm({
      title: '提示',
      content: '<p>是否确认' + statusState + '流程:' + row.name + '?</p>',
      onOk: () => {
        const [url, data] = [
          '/api/flow/bpmn/update-state',
          {
            id: id,
            state: state
          }
        ]
        // this.$http
        //   .put(url, data)
        request.updateState(data)
          .then((res) => {
            if (res.data.code === 0) {
              this.$Message.success(statusState + '成功')
              this.queryList()
            }
          })
          .catch(() => {
            this.$Message.error(statusState + '失败')
          })
        this.reset()
      },
      onCancel: () => { }
    })
  },
  // 更改可终止状态
  changeBackState (row) {
    let statusState = row.backState == 0 ? 0 : 1
    let data = {
      id: row.id,
      name: row.name,
      backState: statusState
    }
    // this.$http
    //   .put('/api/flow/bpmn/update', data)
    request.updateBpmn(data)
      .then((res) => {
        this.$Message.success('更新成功')
        this.queryList()
      })
      .catch((error) => {
        this.$Message.error('更新失败:' + error.response.data.detail)
      })
  },
  reset () {
    this.form = {
      id: undefined,
      key: undefined,
      name: undefined,
      description: undefined
    }
    if (this.$refs.formData !== undefined) this.$refs.formData.resetFields()
  },
  /** 导入按钮操作 */
  handleImport () {
    this.upload.open = true
  },
  // 文件上传中处理
  handleFileUploadProgress (event, file, fileList) {
    this.upload.isUploading = true
  },
  // 文件上传失败的处理
  handleFileError (err, file, fileList) {
    const detail = JSON.parse(err.message).detail
    this.$message.error('导入流程失败:' + detail)
    this.uploadClose()
  },
  // 文件上传成功处理
  handleFileSuccess (response, file, fileList) {
    if (response.code !== 0) {
      this.$message.error(response.error)
      return
    }
    // 提示，并刷新
    this.$message.success(
      '导入流程成功！请点击【设计流程】按钮，进行编辑保存后，才可以进行【发布流程】'
    )
    // 重置表单

    this.$Message.success('导入成功')
    this.queryList()
    this.uploadClose()
  },
  uploadClose () {
    // 关闭弹窗
    this.upload.open = false
    // 重置上传状态和文件
    this.upload.isUploading = false
    this.$refs.upload.clearFiles()
    // 重置表单
    this.upload.form = {}
    this.resetForm('uploadForm')
  },
  /** 提交上传文件 */
  submitFileForm () {
    this.$refs['uploadForm'].validate((valid) => {
      if (!valid) {
        return
      }
      this.$refs.upload.submit()
    })
  },
    // 权限管理
    authorizationManagement(row) {
      this.init()
      this.authpersonnelResize = {
        ...row,
        authpersonneIds: [],
        authroleIds: [],
        relation: [],
        proDefName: '',
        processId: ''
      }
      if (row.type === 2) {
        this.authpersonnelResize.authpersonneIds.push(...row.relation)
      } else if (row.type === 1) {
        this.authpersonnelResize.authroleIds.push(...row.relation)
      }
      if (this.authpersonnelResize.processDefinition) {
        this.getdata()
      } else {
        this.$alert('流程尚未部署，请部署后进行此操作', '提示', {
          confirmButtonText: '确定'
        })
      }
    },
    getdata() {
      // this.$http
      //   .get(`/api/cscpActs/getAct?id=${this.authpersonnelResize.id}`)
      request.getActsById(this.authpersonnelResize.id)
        .then((response) => {
          this.authpersonnelResize.type = response.data.type
          this.authorizationVisible = true
          if (response.data.type === 1) {
            this.authpersonnelResize.authroleIds = response.data.relation
          } else {
            this.authpersonnelResize.authpersonneIds = response.data.relation
          }
        })
        .catch()
    },

    // 权限管理提交
    submitAuthorization() {
      this.$refs.authpersonnelResize.validate((valid) => {
        if (valid) {
          // 构建表单
          let authpersonnelResize = {
            ...this.authpersonnelResize,
            // taskDefinitionName: undefined,
            processId: this.authpersonnelResize.processDefinition.id
          }
          if (authpersonnelResize.type === 2) {
            authpersonnelResize.relation = authpersonnelResize.authpersonneIds
          } else if (authpersonnelResize.type === 1) {
            authpersonnelResize.relation = authpersonnelResize.authroleIds
          }
          authpersonnelResize.authpersonneIds = undefined
          authpersonnelResize.authroleIds = undefined
          authpersonnelResize.procDefName = authpersonnelResize.name
          authpersonnelResize.procDefId = authpersonnelResize.id
          // this.$http
          //   .put('/api/cscpActs/addAct', authpersonnelResize)
          request.addAct(authpersonnelResize)
            .then((response) => {
              this.$Message.success('修改成功')
            })
            .catch()
          this.authorizationVisible = false
        }
      })
    },
    authpersonnelResizeTypeChange() {
      this.$forceUpdate()
    },
    cancelAuthorization() {
      this.authorizationVisible = false
    },
    // 删除
    deleteDesign(row) {
      this.$Modal.confirm({
        title: '警告',
        content: '<p>确定删除这条数据？</p>',
        onOk: () => {
          let obj = {}
          obj['id'] = row.id
          let params = Object.assign(obj)
          // this.$http
          //   .delete('/api/flow/bpmn/delete', { params: params })
          request.deleteBpmn({ params })
            .then((res) => {
              this.$Message.success('删除成功')
              this.queryList()
            })
            .catch((error) => {
              this.$Message.error(
                `${'删除失败！'}<br/>${error.response.data.detail}`
              )
            })

          this.reset()
        },
        onCancel: () => { }
      })
    },
  // 分页
  handleSizeChange (val) {
    this.pageData.pageSize = val
    this.queryList()
  },
  handleCurrentChange (val) {
    this.pageData.pageNum = val
    this.queryList()
  }
}
})
</script>
<style lang="less" scoped>
  .mainflow{
      width: 100%;
      display: flex;
      flex-direction: column;
      .flowtop{
          width: 100%;
          height: 110px;
          background-color: #457CE0;
          display: flex;
          border-radius: 5px;
          margin-bottom: 10px;
          justify-content: space-between;
          align-items: center;
          .processLeft{
              margin: 0px 0px 6px 30px;
              p{
                font-size: 14px;
                color: #ccc;
              }
              .createModel{
                  display: flex;
                  justify-content: center;
                  p{
                      font-size: 24px;
                      color: white;
                      margin-right: 20px;
                  }
                  img{
                    height: 55px;
                    cursor: pointer;
                  }
              }
          }
          .formRight{
              flex-direction: column;
              margin: 0px 112px 0px 0px;
              img{
                width: 130px;
              }
          }
      }
      .flowbottom{
          width: 100%;
          flex: 1;
          .processTabs{
              display: flex;
              justify-content: space-between;
              align-items: center;
              p {
                  color: black;
                  line-height: 22px;
                  font-size: 18px;
                  font-weight: bold;
                  padding-left: 10px; 
                  border-left: 5px solid rgb(69, 124, 224);
                  margin-right: 30px;
              }
          }
          .processCard{
              width: 100%;
              display: flex;
              flex-wrap: wrap;
              justify-content: space-between;
              margin-top: 10px;
              .cardChild{
                  width: 49.5%;
                  margin-bottom: 6px;
                  position: relative;
                  .cardTop{
                    display: flex;
                    justify-content: space-between;
                    .childTopRight{
                      cursor: pointer;
                      display: flex;
                      color:rgb(69, 124, 224);
                      font-size: 14px;
                      // font-weight: bold;
                      // p:nth-child(2){
                      //   margin: 0px 15px;
                      // }
                    }
                  }
                  .cardMiddle{
                      padding: 10px 0px 15px 15px;
                      margin: 10px 0px;
                      background-color: #fffdf8;
                      p{
                        color: #999999;
                        margin-bottom: 5px;
                      }
                  }
                  .cardBottom{
                    height: 110px;
                    padding: 10px 0px 15px 15px;
                    background-color: rgb(246, 252, 246);
                      p{
                        color: #999999;
                        margin-bottom: 5px;
                      }
                  }
              }
          }
          .processList{
              width: 100%;
              display: flex;
              flex-wrap: wrap;
              margin-top: 10px;
              .processChild{
                  width: 100%;
                  margin-bottom: 6px;
                  position: relative;
                  .childTop{
                    display: flex;
                    justify-content: space-between;
                    .childTopRight{
                      cursor: pointer;
                      display: flex;
                      color:rgb(69, 124, 224);
                      font-size: 14px;
                      // font-weight: bold;
                      p:nth-child(2){
                        margin: 0px 15px;
                      }
                    }
                  }
                  .childMiddle{
                    color: #999999;
                    padding: 10px 0px 15px 15px;
                  }
                  .childBottom{
                    padding-left: 15px;
                      width: 84%;
                      display: flex;
                      align-items: center;
                      background-color: rgb(246, 252, 246);
                      height: 40px;
                      color: #999999;
                      span{
                        margin-right: 10px;
                      }
                  }
              }
          }
      }
      .active{
      color: rgb(69, 124, 224) !important;
  }
  }
  ::v-deep .has-gutter{
    line-height: 1px;
  }
  //el-transfer-panel
  ::v-deep .el-transfer-panel{
    width: 165px;
  }
  ::v-deep .el-transfer__buttons{
    padding: 0 10px;
  }
  ::v-deep .el-transfer-panel__header{
    background-color: #d3e1f7;
  }
</style>