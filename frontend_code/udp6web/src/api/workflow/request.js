import { requestMethod } from '@/libs/util'

const get = requestMethod.get
const post = requestMethod.post
const put = requestMethod.put
const del = requestMethod.delete

// flowform.vue
const TASKPERM_UPDATE_URL = '/api/flow/rule/taskPerm-update'
const FIELDSPERM_UPDATE_URL = '/api/updateFieldsPerm'
const ASSIGN_CREATE_URL = '/api/flow/rule/assign-create'
const ASSIGN_UPDATE_URL = '/api/flow/rule/update'
const CSCP_UESR_DETAIL_URL = '/api/system/cscpUserDetailsOr'
const CSCP_ROLE_URL = '/api/system/cscpRolessByCriteria'
const CSCP_DEPTS_TREESELECT_URL = '/api/cscpDepts/treeselect'
const CSCP_FLOW_BPMN_PAGE_URL = '/api/flow/bpmn/page'
const CSCP_CUSTOMIZE_VFORMBYFORMTYPE_URL = '/api/lowcode/customize/cscpCustomizeVforms/getByFormType'
const CSCP_FLOW_RULE_LIST_URL = '/api/flow/rule/list'
const CSCP_RULE_SELECT_TASKPERM_URL = '/api/flow/rule/taskPerm-select'
const CSCP_CUSTOMIZE_VFORM_LISTONE_URL = '/api/lowcode/customize/cscpCustomizeVforms/listOne'
const CSCP_CUSTOMIZE_QUERY_TABLEFIELDPERM_URL = '/api/lowcode/customize/qryTableFieldPerm'
const CSCP_FIELDPERMS_URL = '/api/queryFieldPerms'
const CSCP_TABLEOPERATEFIELDS_URL = '/api/getTableOperateFields'
const getActsById = function (params) {
  return get(`/api/cscpActs/getAct?id=${params}`)
}
const CSCP_ADDACT_URL = '/api/cscpActs/addAct'
const publishBpmnById = function (params) {
  return post(`/api/flow/bpmn/deploy?id=${params}`)
}
const CSCP_BPMN_UPDATESTATE_URL = '/api/flow/bpmn/update-state'
const CSCP_BPMN_UPDATE_URL = '/api/flow/bpmn/update' //有重复
const CSCP_BPMN_DELETE_URL = '/api/flow/bpmn/delete'
const CSCP_BPMN_CREATE_URL = '/api/flow/bpmn/create'
const CSCP_FLOW_FORM_PAGE_URL = '/flow/form/page'

// hotProcessAnalyse.vue
const HOT_COUNT_FLOW_URL = '/api/flow/count/hotFlow'

// modelEditor.vue
const GET_FLOW_BPMN_URL = '/api/flow/bpmn/get'

// person.vue
const GET_SELECT_ACT_BY_USER_ID = '/api/actRdCollects/selectActByUserId'
const GET_LAUNCH_PROCESS_URL = '/api/actRdCollects/launchProcess'
const GET_IMG_DATA_URL = '/api/system/cscpCurrentUserDetails'
const ADD_TO_COLLECTION_URL = '/api/actRdCollects/addCollect'

// processStatistics.vue
const GET_TREND_MESSAGE_URL = '/api/flow/count/queryTrendMessage'
const GET_PROP_MESSAGE_URL = '/api/flow/count/queryPropMessage'
const GET_MESSAGE_URL = '/api/flow/count/queryMessage'

// components/circulateList.vue
const GET_RD_TURN_TASK_SELECT_URL = '/api/flow/door/rdTurnTask-select'

// components/doneList.vue
const GET_DONE_PAGE_URL = '/api/flow/core/done-page'

// components/doneProcess.vue
const GET_MY_PROCESS_URL = '/api/flow/core/my-process'
const GET_END_PROCESS_URL = '/api/flow/core/endProcess'

// components/todoList.vue
const GET_TODO_PAGE_URL = '/api/flow/core/todo-page'
const postClaimTask = function (taskId) {
  return post(`/api/flow/core/claim/${taskId}`)
}

// component/addProcess.vue


// component/authorizationManagement.vue

// component/chargeForm.vue
const GET_ALL_LIST_URL = '/api/lowcode/customize/cscpCustomizeVforms/listAll'

// component/handleAssignRule.vue


// component/publishDesign.vue

// approval/LaunchProcess.vue
const CREATE_FLOW_PROCESS_URL = '/api/flow/process/create'
const GET_BPMN_XML_URL = '/api/flow/def/get-bpmn-xml'
const getDataByFormId = function (formId) {
  return get('/api/lowcode/customize/cscpCustomizeVforms/' + formId)
}

// approval/ProcessDetail.vue
const isMultiBytaskKey = function (params) {
  return get(`/api/flow/multi/isMulti?taskKey=${params}`)
}
const GET_OUTLINE_URL = '/api/flow/core/get-out-line'
const GET_FLOW_PROCESS_URL = '/api/flow/process/get'
const PUT_FLOW_CORE_APPROVE_URL = '/api/flow/core/approve'
const UPDATE_ASSIGNEE_URL = '/api/flow/core/update-assignee'
const CREATE_RDTURNTASK_URL = '/api/flow/door/rdTurnTask-create'
const GET_LIST_BY_PROCESS_INSTANCE_ID_URL = '/api/flow/core/list-by-process-instance-id'
const GET_FLOW_PROCESS_LIST_URL = '/api/flow/process/list'
const QUERY_LIST_BY_INSTANCE_ID_URL = '/api/lowcode/customize/template/queryByInstanceId/'


const updateTaskPerm = function (params) {
  return post(TASKPERM_UPDATE_URL, params)
}
const updateFieldsPerm = function (params) {
  return post(FIELDSPERM_UPDATE_URL, params)
}
const createAssign = function (params) {
  return post(ASSIGN_CREATE_URL, params)
}
const updateAssign = function (params) {
  return put(ASSIGN_UPDATE_URL, params)
}
const getCscpUserDetailList = function (params) {
  return get(CSCP_UESR_DETAIL_URL,params)
}
const getCscpRoleList = function (params) {
  return get(CSCP_ROLE_URL, params)
}
const getCscpDeptsTreeselectList = function () {
  return get(CSCP_DEPTS_TREESELECT_URL)
}
const getCscpFlowBpmnPageList = function (params) {
  return get(CSCP_FLOW_BPMN_PAGE_URL, params)
}
const getCscpCustomizeVformsList = function (params) {
  return get(CSCP_CUSTOMIZE_VFORMBYFORMTYPE_URL, params)
}
const getRuleList = function (params) {
  return get(CSCP_FLOW_RULE_LIST_URL, params)
}
const getRuleTaskPermList = function (params) {
  return get(CSCP_RULE_SELECT_TASKPERM_URL, params)
}
const getCscpCustomizeVformsListOne = function (params) {
  return get(CSCP_CUSTOMIZE_VFORM_LISTONE_URL, params)
}
const getTableFieldPerm = function (params) {
  return get(CSCP_CUSTOMIZE_QUERY_TABLEFIELDPERM_URL, params)
}
const getFieldPermsList = function (params) {
  return get(CSCP_FIELDPERMS_URL, params)
}
const getTableOperateFieldsList = function (params) {
  return get(CSCP_TABLEOPERATEFIELDS_URL, params)
}
const addAct = function (params) {
  return put(CSCP_ADDACT_URL, params)
}
const updateState = function (params) {
  return put(CSCP_BPMN_UPDATESTATE_URL, params)
}
const updateBpmn = function (params) {
  return put(CSCP_BPMN_UPDATE_URL, params)
}
const deleteBpmn = function (params) {
  return del(CSCP_BPMN_DELETE_URL, params)
}
const createBpmn = function (params) {
  return post(CSCP_BPMN_CREATE_URL, params)
}
const getFlowFormPage = function (params) {
  return get(CSCP_FLOW_FORM_PAGE_URL, params)
}
const getHotFlowList = function (params) {
  return get(HOT_COUNT_FLOW_URL, params)
}
const getFlowBpmnList = function (params) {
  return get(GET_FLOW_BPMN_URL, params)
}
const getSelectActByUserId = function (params) {
  return get(GET_SELECT_ACT_BY_USER_ID, params)
}
const getLaunchProcess = function (params) {
  return get(GET_LAUNCH_PROCESS_URL, params)
}
const getImgData = function (params) {
  return get(GET_IMG_DATA_URL, params)
}
const addToCollection = function (params) {
  return get(ADD_TO_COLLECTION_URL, params)
}
const getTrendMessageList = function () {
  return get(GET_TREND_MESSAGE_URL)
}
const getPropMessageList = function () {
  return get(GET_PROP_MESSAGE_URL)
}
const getMessageList = function (params) {
  return get(GET_MESSAGE_URL, params)
}
const getSelectTaskList = function (params) {
  return get(GET_RD_TURN_TASK_SELECT_URL, params)
}
const getDonePageList = function (params) {
  return get(GET_DONE_PAGE_URL, params)
}
const getMyProcessList = function (params) {
  return get(GET_MY_PROCESS_URL, params)
}
const getEndProcess = function (params) {
  return get(GET_END_PROCESS_URL, params)
}
const getTodoPageList = function (params) {
  return get(GET_TODO_PAGE_URL, params)
}
const getAllList = function (params) {
  return get(GET_ALL_LIST_URL, params)
}
const createFlowProcess = function (params) {
  return post(CREATE_FLOW_PROCESS_URL, params)
}
const getBpmnXmlData = function (params) {
  return get(GET_BPMN_XML_URL, params)
}
const getOutlineData = function (params) {
  return get(GET_OUTLINE_URL,params)
}
const getFlowProcessData = function (params) {
  return get(GET_FLOW_PROCESS_URL, params)
}
const putApproveData = function (params) {
  return put(PUT_FLOW_CORE_APPROVE_URL, params)
}
const updateAssignData = function (params) {
  return put(UPDATE_ASSIGNEE_URL, params)
}
const createRdTurnTask = function (params) {
  return post(CREATE_RDTURNTASK_URL, params)
}
const getListByInstanceId = function (params) {
  return get(GET_LIST_BY_PROCESS_INSTANCE_ID_URL, params)
}
const getProcessList = function (params) {
  return get(GET_FLOW_PROCESS_LIST_URL, params)
}
const queryListByInstanceId = function (formId, params) {
  return get(QUERY_LIST_BY_INSTANCE_ID_URL + formId, params)
}




export default {
  updateTaskPerm,
  updateFieldsPerm,
  createAssign,
  updateAssign,
  getCscpUserDetailList,
  getCscpRoleList,
  getCscpDeptsTreeselectList,
  getCscpFlowBpmnPageList,
  getCscpCustomizeVformsList,
  getRuleList,
  getRuleTaskPermList,
  getCscpCustomizeVformsListOne,
  getTableFieldPerm,
  getFieldPermsList,
  getTableOperateFieldsList,
  getActsById,
  addAct,
  publishBpmnById,
  updateState,
  updateBpmn,
  deleteBpmn,
  createBpmn,
  getFlowFormPage,
  getHotFlowList,
  getFlowBpmnList,
  getSelectActByUserId,
  getLaunchProcess,
  getImgData,
  addToCollection,
  getTrendMessageList,
  getPropMessageList,
  getMessageList,
  getSelectTaskList,
  getDonePageList,
  getMyProcessList,
  getEndProcess,
  getTodoPageList,
  postClaimTask,
  getAllList,
  createFlowProcess,
  getBpmnXmlData,
  getDataByFormId,
  isMultiBytaskKey,
  getOutlineData,
  getFlowProcessData,
  putApproveData,
  updateAssignData,
  createRdTurnTask,
  getListByInstanceId,
  getProcessList,
  queryListByInstanceId
}
