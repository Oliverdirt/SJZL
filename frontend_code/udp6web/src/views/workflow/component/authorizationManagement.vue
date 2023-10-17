<template>
        <el-form label-width="80px" :model="authpersonnelResize" ref="authpersonnelResize">
          <el-form-item label="流程标识" prop="key">
            <el-input v-model="authpersonnelResize.key" placeholder="请输入流标标识" disabled/>
          </el-form-item>
          <el-form-item label="流程名称" prop="name">
            <el-input v-model="authpersonnelResize.name" placeholder="请输入流程名称" disabled/>
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
</template>
<script>
import request from '@/api/workflow/request'

export default {
  data() {
    return {
      authpersonnelResize: {},
      operaterole: [],
      userOptions: [],
      page: 1,
      size: 10,
      authpersonnelOptions: [
        {
          value: 1,
          label: "角色",
        },
        {
          value: 2,
          label: "用户",
        },
      ],
    };
  },
  created(){
    this.queryList()
    this.init()
  },
  methods: {
    init () {
      // 人员
      // this.$http.get('api/system/cscpUserDetailsOr')
      request.getCscpUserDetailList()
        .then(response => {
        const op = []
        for (let item of response.data.data) {
          op.push({
            value: item.userId,
            label: `${item.username} `
          })
        }
        this.userOptions = op
      }).catch()
      // 角色
      const [url, httpConfig] = [
        '/api/system/cscpRolessByCriteria',
        {
          params: { size: 1000 }
        }
      ]
      // this.$http.get(url, httpConfig)
      request.getCscpRoleList(httpConfig)
        .then(response => {
        this.operaterole = []
        for (const item of response.data.data) {
          this.operaterole.push({
            value: item.id.toString(),
            label: item.name,
            icon: item.icon
          })
        }
      }).catch()
    },
    queryList() {
      let params = {
        page: 1,
        size: 100000,
      };
      // this.$http
      //   .get("/api/flow/bpmn/page", {
      //     params: params,
      //   })
      request.getCscpFlowBpmnPageList({ params: params })
        .then((res) => {
          this.rowAuth = res.data.result.list[0]
          const row = this.rowAuth
          this.authorizationManagement(row) 

        })
        .catch(() => {
          this.$Message.error("列表查询失败");
        });
    },
    authpersonnelResizeTypeChange() {
      this.$forceUpdate();
    },

    authorizationManagement(row) {
      this.authpersonnelResize = {
        ...row,
        authpersonneIds: [],
        authroleIds: [],
        relation: [],
        proDefName: "",
        processId: "",
      };
      if (row.type === 2) {
        this.authpersonnelResize.authpersonneIds.push(...row.relation);
      } else if (row.type === 1) {
        this.authpersonnelResize.authroleIds.push(...row.relation);
      }
      if (this.authpersonnelResize.processDefinition) {
        this.getdata();
      } else {
        this.$alert("流程尚未部署，请部署后进行此操作", "提示", {
          confirmButtonText: "确定",
        });
      }
    },
    getdata () {
      // this.$http.get(`/api/cscpActs/getAct?id=${this.authpersonnelResize.id}`)
      request.getActsById(this.authpersonnelResize.id)
        .then(response => {
        this.authpersonnelResize.type = response.data.type
        this.authorizationVisible = true
        if (response.data.type === 1) {
          this.authpersonnelResize.authroleIds = response.data.relation
        } else {
          this.authpersonnelResize.authpersonneIds = response.data.relation
        }
      }).catch()
    },
    submitAuthorization() {
      this.$refs.authpersonnelResize.validate((valid) => {
        if (valid) {
          // 构建表单
          let authpersonnelResize = {
            ...this.authpersonnelResize,
            // taskDefinitionName: undefined,
            processId: this.authpersonnelResize.processDefinition.id,
          };
          if (authpersonnelResize.type === 2) {
            authpersonnelResize.relation = authpersonnelResize.authpersonneIds;
          } else if (authpersonnelResize.type === 1) {
            authpersonnelResize.relation = authpersonnelResize.authroleIds;
          }
          authpersonnelResize.authpersonneIds = undefined;
          authpersonnelResize.authroleIds = undefined;
          authpersonnelResize.procDefName = authpersonnelResize.name;
          authpersonnelResize.procDefId = authpersonnelResize.id;
          // this.$http
          //   .put("/api/cscpActs/addAct", authpersonnelResize)
          request.addAct(authpersonnelResize)
            .then((response) => {
              this.$Message.success("修改成功");
            })
            .catch();
          this.authorizationVisible = false;
        }
      });
    },
  },
};
</script>
<style scoped></style>
