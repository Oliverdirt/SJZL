<style scoped lang="less">
.selected-area{
    width:100%;
    height:50%;
    border: 1px solid #c9c5c5;
    overflow: auto;
}
.tag-box{
    display: flex;
    flex-wrap: wrap;
}

</style>

<template>
    <Modal v-model="showModal"
           :title="title"
           :closable="false"
           fullscreen
           width="100%">
        <div style="height: 100%;display:flex">
           <Card style="width:15%;padding:10px;margin-right:10px">
               <Tree :data="treeData" @on-select-change="changeTree"></Tree>
           </Card>
           <Card style="width:65%;padding:10px">
               <div style="height:40px;width:100%">
                    <Form ref="userForms" :model="userForm" :label-width="60" >
                      <Row :gutter="16">
                       <Col span="6">
                            <FormItem label="用户名">
                                <Input v-model.trim="userForm.usernameLike" :maxlength="100" placeholder="请输入用户名" />
                            </FormItem>
                       </Col>
                        <Col span="6">
                            <FormItem label="姓名">
                                <Input v-model.trim="userForm.nameLike"  :maxlength="100" placeholder="请输入姓名" />
                            </FormItem>
                        </Col>
                        <Col span="6">
                            <FormItem label="手机">
                                <Input  v-model.trim="userForm.mobileLike" :maxlength="11" placeholder="请输入手机号" />
                            </FormItem>
                        </Col>
                         <Col span="6">
                            <FormItem>
                                <Button type="primary"  @click="getUserList">查询</Button>
                                <Button type="default" style="margin-left:10px" @click="resetQuery('userForms')">重置</Button>
                            </FormItem>
                        </Col>
                       </Row>
                    </Form>
               </div>
               <div style="height: calc(100% - 40px);display:flex"> 
                    <div style="width:100%;padding:10px">
                        <Table ref='tableRef' :columns="columns" :data="tableData" :loading="isDisabled" border
                                @on-select='selectUser'
                                @on-select-all="selectAll"
                                @on-select-cancel='selectCancel'
                                @on-select-all-cancel="selectAllCancel"></Table>
                        <Page :total="pageTotal" show-elevator show-total show-sizer :page-size="pageSize"
                              @on-change="changePage" @on-page-size-change="changePageSize" :current="currentPageIndex"
                              style="margin-top: 20px"></Page>
                    </div>
               </div>
           </Card>
             <div style="width:20%;height:100%">
                        <div class="selected-area">
                            <div style="padding: 4px 7px">
                                <span>该角色新分配用户: {{newSelectNum}}项</span>
                            </div>
                            <div class="tag-box">
                                <Tag v-for="item in tagList" :key='item.id' :name='item.name' closable @on-close='closeTag(item)'>{{item.name}}</Tag>
                            </div>
                        </div>
                        <div class="selected-area">
                            <div style="padding: 4px 7px">
                                <span>该角色已分配用户: {{alreadySelectedNum}}项</span>
                            </div>
                            <div class="tag-box">
                                <Tag v-for="item in alreadyTagList" :key='item.id' :name='item.name' closable @on-close='closeAlreadyTag(item)'>{{item.name}}</Tag>
                            </div>
                        </div>
                    </div>

        </div>
        <div slot="footer">
            <Button type="default" @click="cancel">取消</Button>
            <Button type="primary" @click="handleSubmit">确定</Button>
        </div>
    </Modal>
</template>

<script>
import { getRoleDeptTree2 } from '@/api/admin/dept'
import { getUserDetailByParams } from '@/api/admin/user'
import dateMixin from '../../../../mixins/date.js'
import { getUsersByRoleId, updateUserRoles } from '@/api/admin/role'

    export default {
        mixins: [dateMixin],
        name: 'role-assign',
        data() {
            return {
                showModal: false,
                title: '用户选择',
                isDisabled: false,
                treeData:[],    //部门树的数据
                userForm:{},    //筛选的表单
                tableData:[],    //表格的数据
                columns:[
                {
                type: 'selection',
                width: 60,
                align: 'center'
                },
                { 
                key: 'username',
                title: '用户名',
                align: 'center'
                },
                {
                key: 'name',
                title: '姓名',
                align: 'center'
                },
                {
                key: 'mobile',
                title: '手机',
                align: 'center'
                },
                {
                key: 'email',
                title: '电子邮件'
                },
                {
                key: 'lastLogin',
                title: '最后登录时间',
                align: 'center',
                overflow: 'none',
                textOverflow: 'ellipsis',
                whiteSpace: 'nowrap'
                }],
                alreadySelectedNum: 0,    //已经关联的人员数量
                newSelectNum: 0,    //准备新关联的人员数量
                pageTotal: 0,
                pageSize:10,
                currentPageIndex: 1,
                tagList: [],    //角色新关联的人员标签列表
                alreadyTagList:[],    //角色已关联的人员标签列表
                deptId:'',    //部门ID
                currentSelection: [],    //当前全选的临时变量
                roleId: '',    //人员ID
                
            }
        },
        methods: {
            //对象数组合并去重
            uniqueArr(arr1, arr2) {
                let arr3 = [...arr1, ...arr2]
                const map = new Map()
                return arr3.filter(item => {
                    return !map.has(item.id) && map.set(item.id, 1)
                })
            },
            //初始化已有该角色人员
            initRoleUser(id) {
                let that = this;
                that.roleId = id;
                getUsersByRoleId(id).then(res => {
                    res.data.forEach(function(e) {
                        that.alreadyTagList.push({
                            name: e.username,
                            id: e.id
                        })
                        that.alreadySelectedNum++
                    })
                })
            },
            //处理后台返回数据生成树结构数据
            getTree (tree) {
                let arr = []
                if (!!tree && tree.length !== 0) {
                    tree.forEach(item => {
                    let obj = {}
                    obj.title = item.label
                    obj.id = item.id 
                    obj.expand = true
                    obj.selected = false
                    obj.children = this.getTree(item.children) // 递归调用
                    arr.push(obj)
                    })
                }
                return arr
            },
            //获取表格数据
            getUserList() {
                let that = this
                let msg = this.$Message.loading({
                        content: '正在更新数据',
                        duration: 0
                    })
                    this.isDisabled = true
                getUserDetailByParams({
                    'usernameLike': this.userForm.usernameLike || null,
                    'nameLike': this.userForm.nameLike || null,
                    'mobileLike': this.userForm.mobileLike || null,
                    'deptIdEquals': this.deptId,
                    sort: 'user_id',
                    page: this.currentPageIndex,
                    size: this.pageSize
                }).then(response => {
                    this.tableData = response.data.data.map(el => {
                        el.lastLogin = el.lastLogin ? this.formatDate(new Date(el.lastLogin)) : null
                        el.registerTime = el.registerTime ? this.formatDate(new Date(el.registerTime)) : null
                        return el
                    })
                    this.$nextTick(function(){
                        this.toggleTableSelect(this.tagList, this.tableData)
                    })
                    this.pageTotal = response.data.recordsTotal
                    msg()
                    this.isDisabled = false
                    }).catch(error => {
                    msg()
                    this.isDisabled = false
                    if (error.response) {
                        this.$Message.error('数据获取失败！')
                    }
                    })
            },
            //初始化树数据
            initTreeData(){
                getRoleDeptTree2().then(res => {
                     this.treeData = this.getTree(res.data)
                }).catch(() => {
                    this.$message.error('部门树查询失败')
                })
            },
            //点击树节点，联动表格数据
            changeTree(e) {
                this.deptId = e[0].id
                this.getUserList()
            },
            //勾选表格已经选择的用户
            toggleTableSelect(tagList, tableData){
                let that = this
                let idList = tagList.map(function(e){
                    return e.id
                })
                tableData.forEach(function(item, index){
                    idList.forEach(function(e){
                        if(e === item.userId){
                            that.$refs.tableRef.toggleSelect(index)
                        }
                    })
                })
            },
            //关闭标签
            closeTag(item) {
               let that = this
               let idList = this.tagList.map(function(e){
                    return e.id
                })
                let index = idList.indexOf(item.id)
                this.tagList.splice(index, 1)
                this.tableData.forEach(function(e, index){
                    if(e.userId === item.id){
                        that.$refs.tableRef.toggleSelect(index)
                    }
                })
            },
            //关闭已关联用户
            closeAlreadyTag(item) {
                let idList = this.alreadyTagList.map(function(e){
                    return e.id
                })
                let index = idList.indexOf(item.id)
                this.alreadyTagList.splice(index, 1)
                this.alreadySelectedNum--
            },
            //弹出框确认提交
            handleSubmit(){
                const msg = this.$Message.loading({
                    content:  '正在更新角色数据',
                    duration: 0
                })
                let totalTagList = this.uniqueArr(this.tagList, this.alreadyTagList)
                let idList = totalTagList.map(function(e){
                    return e.id
                })
                updateUserRoles(this.roleId, idList)
                    .then(response => {
                        msg()
                    }).catch(error => {
                        msg()
                    if (error.response) {
                        this.$Message.error('角色绑定用户成功失败！')
                    }
                })
                this.cancel()
            },
            //弹出框关闭
            cancel() {
                this.newSelectNum = 0 
                this.alreadySelectedNum = 0
                this.deptId = ''
                this.tagList = []
                this.alreadyTagList = []
                this.showModal = false
                this.$refs.userForms.resetFields()
                this.$refs.tableRef.selectAll(false)
            },
            //翻页
            changePage (page) {
                this.currentPageIndex = page
                this.getUserList()
             },
            //改变每页显示条数
            changePageSize (size) {
                this.pageSize = size
                this.getUserList()
             },
             //单选某个用户
             selectUser(selection, row) {
                let idList = this.tagList.map(function(e){
                    return e.id
                })
                let index = idList.indexOf(row.userId)
                if (index === -1){
                    this.tagList.push({
                        name: row.username,
                        id: row.userId
                     })
                     this.newSelectNum++
                }
             },
            //全选用户
             selectAll(selection){
                let that = this
                this.currentSelection = selection;
                let idList = this.tagList.map(function(e) {
                    return e.id
                })
                selection.forEach(function(e) {
                  if (idList.indexOf(e.id)==-1) {
                    that.tagList.push({
                        name: e.username,
                        id: e.userId
                    })
                    that.newSelectNum++
                  }
                })
             },
             //取消勾选表格某一行数据
             selectCancel(selection, row) {
                let that = this
                let idList = this.tagList.map(function(e){
                    return e.id
                })
                idList.forEach(function(e, index){
                    if (e === row.userId){
                        that.tagList.splice(index, 1)
                    }
                })
                this.newSelectNum--
             },
             //取消全选
             selectAllCancel(){
                let that = this
                let idList = this.tagList.map(function(e){
                    return e.id
                })
                this.currentSelection.forEach(function(e){
                    if (idList.indexOf(e.userId)!=-1){
                        that.tagList.splice(idList.indexOf(e.userId), 1)
                        idList.splice(idList.indexOf(e.userId), 1)
                        that.newSelectNum--
                    }
                })
             },
             //重置表单
             resetQuery (name) {
                this.userForm = {}
                this.getUserList()   
             },

        },
    }
</script>