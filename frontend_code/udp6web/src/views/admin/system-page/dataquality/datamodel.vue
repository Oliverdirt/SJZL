<template>
  <div style="height: 100%">
    <div class="formCard-content">
      <Form ref="formInline" :model="formInline" :label-width="80">
        <Row :gutter="30">
          <Col span="8">
            <FormItem label="功能名称" prop="title">
              <Input
                type="text"
                :maxlength="100"
                v-model.trim="formInline.title"
                placeholder="请输入功能名称"
              ></Input> 
            </FormItem>
          </Col>
           <!--<Col span="8">
            <FormItem label="路径名称" prop="name">
              <Input
                type="text"
                :maxlength="100"
                v-model.trim="formInline.name"
                placeholder="请输入路径名称"
              ></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="选择系统" prop="systemId">
              <Select
                v-model="formInline.systemId"
                placeholder="请选择"
                style="width: 100%"
              >
                <Option
                  v-for="item in systemOptions"
                  :key="item.key"
                  :label="item.label"
                  :value="item.value"
                ></Option>
              </Select>
            </FormItem>
          </Col>-->
        </Row>
        <!-- <Row :gutter="48">
          <Col span="8">
            <FormItem label="选择模块" prop="modelId">
              <Select
                v-model="formInline.modelId"
                placeholder="请选择"
                style="width: 100%"
              >
                <Option
                  v-for="item in moduleOptions"
                  :key="item.key"
                  :label="item.label"
                  :value="item.value"
                ></Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="选择状态" prop="status">
              <Select
                v-model="formInline.status"
                placeholder="请选择"
                style="width: 100%"
              >
                <Option value="0">启用</Option>
                <Option value="1">停用</Option>
              </Select>
            </FormItem>
          </Col>
        </Row> -->
      </Form>
      <div class="search-btn">
        <Button type="primary" @click="handleSearch()" style="margin-right: 8px"
          >查询</Button
        >
        <Button type="default" @click="handleReset('formInline')">重置</Button>
      </div>
    </div>
    <div class="tableBtn">
      <Button
        icon="md-add"
        type="primary"
        @click="handleAdd()"
        style="margin-right: 10px"
        >添加功能</Button
      >
    </div>
    <Table
      :columns="columns"
      :data="data"
      @on-selection-change="rowSelected($event)"
    >
      <template slot-scope="{ row }" slot="dataStatus">
        <el-switch
          v-model="row.status"
          active-value="0"
          inactive-value="1"
          @change="changeStatus(row)"
        >
        </el-switch>
      </template>

      <template slot-scope="{ row, index }" slot="action">
        <div class="table-operate">
          <el-button type="primary" @click="show(row, index, false)"
            >编辑</el-button
          >
          <el-button
            type="danger"
            size="small"
            style="margin-right: 5px"
            @click="remove(row, index)"
            >删除</el-button
          >
        </div>
      </template>
    </Table>
    <div class="table-page">
      <Page
        :total="pageData.total"
        :page-size="pageData.size"
        :current="pageData.page"
        show-elevator
        show-sizer
        show-total
        @on-page-size-change="changePage($event, 'size')"
        @on-change="changePage($event, 'page')"
        style="margin-top: 20px"
      />
    </div>
    <div>
      <el-dialog
        :visible.sync="dialogVisible"
        :closeOnClickModal="false"
        :title="dialogTitle"
        width="70%"
      >
        <el-form
          size="descall"
          label-width="100px"
          :model="detail"
          ref="detail"
          label-suffix="："
          :rules="ruleValidate"
        >
          <div class="source-name" v-show="addOrChange">添加常用功能</div>
          <div class="source-name" v-show="!addOrChange">修改常用功能</div>
          <Row :gutter="10">
            <Col span="24">
              <el-form-item label="关联菜单" prop="menuId">
                <Input
                  readonly
                  type="text"
                  :maxlength="50"
                  v-model="detail.menuName"
                  placeholder="请选择关联菜单"
                  search
                  enter-button="选择菜单"
                  @on-search="menuChoose()"
                ></Input>
              </el-form-item>
            </Col>
          </Row>
          <Row :gutter="10">
            <Col span="12">
              <el-form-item label="功能名称">
                <Input
                  type="text"
                  :maxlength="100"
                  v-model="detail.title"
                  placeholder="请输入功能名称"
                ></Input>
              </el-form-item>
            </Col>
            <Col span="12">
              <el-form-item label="路径名称" prop="name">
                <Input
                  type="text"
                  :maxlength="100"
                  v-model="detail.name"
                  placeholder="请输入路径名称"
                  :disabled="flag"
                ></Input>
              </el-form-item>
            </Col>
          </Row>
          <Row :gutter="10">
            <Col span="12">
              <el-form-item label="选择模块" prop="modelId">
                <Select v-model="detail.modelId" placeholder="请选择模块">
                  <Option
                    v-for="item in moduleOptions"
                    :key="item.key"
                    :label="item.label"
                    :value="item.value"
                  ></Option>
                </Select>
              </el-form-item>
            </Col>
            <Col span="12">
              <el-form-item label="选择系统" prop="systemId">
                <Select v-model="detail.systemId" placeholder="请选择系统">
                  <Option
                    v-for="item in systemOptions"
                    :key="item.key"
                    :label="item.label"
                    :value="item.value"
                  ></Option>
                </Select>
              </el-form-item>
            </Col>
          </Row>
          <Row :gutter="10">
            <Col span="12">
              <el-form-item label="背景颜色" prop="bgColor">
                <Select v-model="detail.bgColor" placeholder="请选择背景颜色">
                  <Option
                    v-for="item in bgColorList"
                    :key="item.key"
                    :label="item.label"
                    :value="item.key"
                  ></Option>
                </Select>
              </el-form-item>
            </Col>
          </Row>
          <span class="source-name">缩略图上传</span
          ><span class="tips">仅支持JPG/JPEG/PNG格式文件</span>
          <Row :gutter="10">
            <Col span="24">
              <el-form-item label-width="auto" prop="images">
                <el-upload
                  class="avatar-uploader"
                  :action="actionUrl"
                  :headers="headers"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess"
                  :on-error="handleFileError"
                  :before-upload="beforeAvatarUpload"
                  accept="image/png, image/jpeg,image/jpg"
                >
                  <img v-if="imageUrl" :src="imageUrl" class="avatar" />
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
              </el-form-item>
            </Col>
          </Row>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="ok(okFlag)">确 定</el-button>
        </span>
      </el-dialog>
    </div>
    <div>
      <el-dialog
        :visible.sync="menuChooseVisible"
        :closeOnClickModal="false"
        title="选择菜单"
        width="85%"
      >
        <el-form
          size="descall"
          label-width="100px"
          :model="detail"
          ref="detail"
          label-suffix="："
          :rules="ruleValidate"
        >
          <div class="source-name">选择菜单</div>
          <Form ref="menuInline" :model="menuInline" :label-width="80">
            <Row :gutter="48">
              <Col span="6">
                <FormItem label="搜索内容" prop="menuId" :label-width="90">
                  <Input
                    type="text"
                    :maxlength="100"
                    v-model.trim="menuInline.menuId"
                    placeholder="请输入精确的菜单名"
                  ></Input>
                </FormItem>
              </Col>
              <Col span="6">
                <FormItem label="选择模块" prop="modelId" :label-width="69">
                  <Select v-model="menuInline.modelId" placeholder="请选择模块">
                    <Option
                      v-for="item in moduleOptions"
                      :key="item.key"
                      :label="item.label"
                      :value="item.value"
                    ></Option>
                  </Select>
                </FormItem>
              </Col>
              <Col span="6">
                <FormItem label="选择系统" prop="systemId">
                  <Select
                    v-model="menuInline.systemId"
                    placeholder="请选择系统"
                  >
                    <Option
                      v-for="item in systemOptions"
                      :key="item.key"
                      :label="item.label"
                      :value="item.value"
                    ></Option>
                  </Select>
                </FormItem>
              </Col>
              <Col span="6">
                <FormItem :label-width="30">
                  <Button
                    type="primary"
                    @click="menuSearch()"
                    style="margin-right: 8px"
                    >查询</Button
                  >
                  <Button type="default" @click="handleReset('menuInline')"
                    >重置</Button
                  >
                </FormItem>
              </Col>
            </Row>
            <el-tree
              :highlight-current="true"
              :check-on-click-node="true"
              ref="tree"
              :props="props"
              node-key="id"
              :expand-on-click-node="false"
              :default-expanded-keys="[0]"
              lazy
              :filter-node-method="filterNode"
              :default-checked-keys="no"
              @check="treeCheck"
              :load="loadNode"
            >
              <span class="custom-tree-node" slot-scope="{ node }">
                <span>{{ node.label }}</span>
              </span>
            </el-tree>
          </Form>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="close2">取 消</el-button>
          <el-button type="primary" @click="menuOk">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import {
  validPostCodeByData,
  validPostNameByData,
  getPostByParams11,
  getPostByParams12,
  getPostByParams22,
  getPostByParams33,
  delPostByPostId1,
  getPostByPostId1,
  updateOrAddPostByData1,
} from "@/api/admin/post";
import { getMenussByParentId } from "@/api/admin/menu";
export default {
  name: "postList1",
  data() {
    return {
      dialogImageUrl: "",
      addOrChange: true,
      dialogVisible: false,
      imgVisible: false,
      menuChooseVisible: false,
      upload_btn: false,
      disabled: false,
      actionUrl: "",
      headers: { Authorization: localStorage.token },
      imageUrl: "",
      fileIds: "",
      dialogTitle: "新增常用功能",
      no: [],
      editorOption: {
        placeholder: "请输入文本...",
        modules: {
          toolbar: "#toolbar",
        },
      },
      props: {
        label: "title",
        children: "children",
      },
      formInline: {
        title: "",
        name: "",
        systemId: "",
        modelId: "",
        status: "",
      },
      menuInline: {
        menuId: "",
        systemId: "",
        modelId: "",
      },
      systemOptions: [],
      bgColorList: [
        {
          label: "蓝色",
          key: "1",
        },
        {
          label: "红色",
          key: "2",
        },
        {
          label: "黄色",
          key: "3",
        },
      ],
      moduleOptions: [],
      statusList: [
        { label: "启用", value: 0 },
        { label: "停用", value: 1 },
      ],
      columns: [
        {
          border: true,
          type: "selection",
          width: 30,
          align: "left",
        },
        {
          title: "序号",
          type: "index",
          align: "left",
          indexMethod: (row, index) => {
            return (
              (this.pageData.page - 1) * this.pageData.size + row._index + 1
            );
          },
        },
        {
          title: "功能名称",
          key: "title",
          align: "left",
        },
        {
          title: "所属模块",
          key: "modelName",
          align: "left",
        },
        {
          title: "所属系统",
          key: "systemName",
          align: "left",
        },
        {
          title: "关联菜单",
          key: "menuValue",
          align: "left",
        },
        {
          title: "路径名称",
          key: "name",
          align: "left",
        },
        {
          title: "图片路径",
          key: "imgPath",
          align: "left",
        },
        {
          title: "状态",
          align: "left",
          key: "status",
          slot: "dataStatus",
        },
        {
          title: "操作",
          slot: "action",
          width: 150,
          fixed: "right",
          align: "left",
        },
      ],
      selectedArr: [],
      data: [],
      pageData: {
        total: 0,
        size: 10,
        page: 1,
      },
      fileList: [],
      images: [],
      detail: {},
      flag: false,
      modalValid: true,
      okFlag: "",

      ruleValidate: {
        name: [{ required: true, message: "不能为空!", trigger: "blur" }],
        menuId: [{ required: true, message: "不能为空!", trigger: "blur" }],
        modelId: [{ required: true, message: "不能为空!", trigger: "blur" }],
        systemId: [{ required: true, message: "不能为空!", trigger: "blur" }],
      },
    };
  },
  mounted() {
    this.actionUrl = this.$util.baseUrl + "/api/fileInfo/uploadImgs";
    this.queryList();
    this.initPost();
  },
  methods: {
    close() {
      this.dialogVisible = false;
      this.$refs["detail"].resetFields();
      this.fileIds = "";
      this.imageUrl = "";
      this.disabled = false;
    },
    close2() {
      this.menuChooseVisible = false;
      this.$refs["menuInline"].resetFields();
      this.disabled = false;
    },
    treeCheck(node, list) {
      //node 该节点所对应的对象、list 树目前的选中状态对象
      //选中事件在选中后执行，当lis中有两个选中时，使用setCheckedKeys方法，选中一个节点
      if (list.checkedKeys.length == 2) {
        //单选实现
        this.$refs.tree.setCheckedKeys([node.id]);
      }
    },
    menuOk() {
      let nArray = this.$refs.tree.getCheckedNodes();
      if (nArray.length !== 0) {
        this.detail.menuId = nArray[0].id;
        this.detail.menuName = nArray[0].title;
      }
      this.menuChooseVisible = false;
      this.$refs["menuInline"].resetFields();
      this.disabled = false;
    },
    handleSearch() {
      let url = "?";
      let arr = [];
      for (let key in this.formInline) {
        arr.push(
          key +
            "=" +
            (this.formInline[key] === undefined ? "" : this.formInline[key])
        );
      }
      arr.push("page=" + (this.pageData.page - 1));
      arr.push("size=" + this.pageData.size);
      url += arr.join("&");
      getPostByParams22(url)
        .then((res) => {
          this.data = res.data.data.data;
          this.pageData.total = res.data.data.recordsTotal;
        })
        .catch(() => {
          this.$message.error("列表查询失败");
        });
    },
    initPost() {
      getPostByParams11({ size: 1000 })
        .then((response) => {
          this.systemOptions = [];
          for (const item of response.data.data) {
            this.systemOptions.push({
              value: item.id.toString(),
              label: item.name,
              key: item.code,
            });
          }
        })
        .catch();
      // getPostByParams12({ size: 1000 })
      //   .then((response) => {
      //     this.moduleOptions = [];
      //     for (const item of response.data) {
      //       this.moduleOptions.push({
      //         value: item.itemCode,
      //         label: item.itemValue,
      //         key: item.id,
      //       });
      //     }
      //   })
      //   .catch();
    },
    handleAdd() {
      this.dialogTitle = "新增常用功能";
      this.detail = {};
      this.flag = false;
      this.okFlag = "add";
      this.addOrChange = true;
      this.dialogVisible = true;
      this.detail.postSort = 1;
    },
    loadNode(node, resolve) {
      if (node.level === 0) {
        let root = {
          id: 0,
          title: "菜单",
          parentId: null,
        };
        resolve([root]);
      } else {
        setTimeout(() => {
          this.getNodes(node.data.id).then((value) => {
            return resolve(value);
          });
        }, 300);
      }
    },
    getNodes(parentId) {
      return new Promise((resolve, reject) => {
        const msg = this.$Message.loading({
          content: "正在获取数据",
          duration: 0,
        });
        getMenussByParentId({
          parentId,
          systemId: this.systemId,
        })
          .then((response) => {
            msg();
            resolve(response.data.data);
          })
          .catch((error) => {
            msg();
            this.$Message.error("获取数据失败！");
            reject(error);
          });
      });
    },
    filterNode(value, data) {
      if (data.id == 0) return false;
      if (!value) return true;
      if (value.a == "" && value.b == "" && value.c == "") return true;
      if (value.a == "" && value.b == "")
        return data.systemId.indexOf(value.c) !== -1;
      if (value.b == "" && value.c == "")
        return data.title.indexOf(value.a) !== -1;
      if (value.a == "" && value.c == "")
        return data.modelId.indexOf(value.b) !== -1;
      if (value.a == "")
        return (
          data.modelId.indexOf(value.b) !== -1 &&
          data.systemId.indexOf(value.c) !== -1
        );
      if (value.b == "")
        return (
          data.title.indexOf(value.a) !== -1 &&
          data.systemId.indexOf(value.c) !== -1
        );
      if (value.c == "")
        return (
          data.title.indexOf(value.a) !== -1 &&
          data.modelId.indexOf(value.b) !== -1
        );
      return (
        data.title.indexOf(value.a) !== -1 &&
        data.modelId.indexOf(value.b) !== -1 &&
        data.systemId.indexOf(value.c) !== -1
      );
    },
    menuSearch() {
      let obj = {
        a: this.menuInline.menuId,
        b: this.menuInline.modelId,
        c: this.menuInline.systemId,
      };
      this.$refs.tree.filter(obj);
    },
    menuChoose() {
      this.menuChooseVisible = true;
    },
    handleReset(name) {
      this.$refs[name].resetFields();
      this.selectedArr = [];
      this.queryList();
      if (name == "menuInline") {
        this.$refs.tree.filter();
      }
    },
    rowSelected(e) {
      this.selectedArr = e;
    },
    remove(row, index) {
      this.$Modal.confirm({
        title: "警告",
        content: "<p>确定删除这条数据？</p>",
        onOk: () => {
          delPostByPostId1(row.id)
            .then((res) => {
              this.$Message.success("删除成功");
              this.handleSearch();
            })
            .catch((error) => {
              if (error.response) {
                this.$Message.error(
                  error.response.data.title
                    ? error.response.data.title
                    : "删除失败！"
                );
              }
            });
        },
        onCancel: () => {},
      });
    },
    show(row, index, flag) {
      this.addOrChange = false;
      this.dialogTitle = "编辑常用功能";
      getPostByPostId1(row.id)
        .then((res) => {
          this.detail = res.data.data;
          this.fileIds = this.detail.fileIds;
          this.flag = flag;
          this.okFlag = "update";
          this.dialogVisible = true;
        })
        .catch(() => {
          this.$message.error("行数据查询失败");
        });
    },
    ok(type) {
      if (this.flag) {
        this.$refs["detail"].resetFields();
        this.fileIds = "";
        this.imageUrl = "";
        this.dialogVisible = false;
        return;
      }
      this.$delete(this.detail, "_index");
      this.$delete(this.detail, "_rowKey");
      let copyDetail = JSON.parse(JSON.stringify(this.detail));
      copyDetail.fileIds = this.fileIds;
      this.$refs["detail"].validate((valid) => {
        if (valid) {
          if (type === "update") {
            updateOrAddPostByData1("put", copyDetail)
              .then((res) => {
                this.$Message.success("修改成功");
                this.dialogVisible = false;
                this.$refs["detail"].resetFields();
                this.queryList();
              })
              .catch(() => {
                this.$Message.error("修改失败");
              });
          } else if (type === "add") {
            updateOrAddPostByData1("post", copyDetail)
              .then((res) => {
                if (res.data.code == 200) {
                  this.$Message.success(res.data.msg);
                  this.dialogVisible = false;
                  this.$refs["detail"].resetFields();
                  this.queryList();
                } else {
                  this.$Message.error(res.data.msg);
                }
              })
              .catch(() => {
                this.$Message.error("新增失败");
              });
          }
        } else {
          this.dialogVisible = true;
        }
      });
    },
    queryList() {
      let url = "";
      let arr = [];
      arr.push("size=" + this.pageData.size);
      arr.push("page=" + this.pageData.page);
      url += arr.join("&");
      console.log(1)
      getPostByParams33(url)
        .then((res) => {
          
          this.data = res.data.data.data;
          
          this.pageData.total = res.data.data.recordsTotal;
          
        })
        .catch(() => {
          this.$message.error("列表查询失败");
        });
    },
    changePage(e, type) {
      if (type === "size") {
        this.pageData.size = e;
      } else if (type === "page") {
        this.pageData.page = e;
      }
      this.queryList();
    },

    // 图片上传
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 2MB!");
      }
      return isLt2M;
    },
    handleAvatarSuccess(res, file) {
      this.imageUrl = this.$util.baseUrl + res.data.fileUrl;
      this.fileIds = res.data.id;
    },
    handleFileError(err, file, fileList) {
      this.$message({
        type: "error",
        message: "上传失败",
      });
    },
    changeStatus(row) {
      let copyDetail = {};
      copyDetail.id = row.id;
      copyDetail.status = row.status;
      updateOrAddPostByData1("put", copyDetail);
    },
  },
};
</script>

<style lang="less" scoped>
@import "~@/views/admin/styles/formStyle.less";
.source-name {
  width: 220px;
  border-left: 5px solid blue;
  line-height: 20px;
  font-size: 18px;
  margin: 20px;
  font-weight: bold;
  text-align: left;
  padding-left: 20px;
}
.tips {
  color: crimson;
  font-size: 10px;
}
.imgs {
  margin-top: 20px;
  margin-left: 20px;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  margin: 20px;
  border: 1px dashed #d9d9d9;
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
