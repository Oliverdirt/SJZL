package com.ctsi.flow.constant;

import com.ctsi.flow.exception.ErrorCode;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-13 16:08
 * @description ：异常信息
 */
public interface ErrorCodeConstants {

    // ==========  通用流程处理 模块 9-000-000 ==========
    ErrorCode HIGHLIGHT_IMG_ERROR = new ErrorCode(00002, "获取高亮流程图异常");

    // ========== 流程模型 9-002-000 ==========
    ErrorCode MODEL_KEY_EXISTS = new ErrorCode(02000, "已经存在流程标识为【{}】的流程");
    ErrorCode MODEL_NOT_EXISTS = new ErrorCode(02001, "流程模型不存在");
    ErrorCode MODEL_KEY_VALID = new ErrorCode(02002, "流程标识格式不正确，需要以字母或下划线开头，后接任意字母、数字、中划线、下划线、句点！");
    ErrorCode MODEL_DEPLOY_FAIL_FORM_NOT_CONFIG = new ErrorCode(02003, "部署流程失败，原因：流程表单未配置，请点击【修改流程】按钮进行配置");
    ErrorCode MODEL_DEPLOY_FAIL_TASK_ASSIGN_RULE_NOT_CONFIG = new ErrorCode(02004, "部署流程失败，" +
            "原因：用户任务({})未配置分配规则，请点击【修改流程】按钮进行配置");
    ErrorCode MODEL_DEPLOY_FAIL_TASK_INFO_EQUALS = new ErrorCode(03005, "流程定义部署失败，原因：信息未发生变化");
    ErrorCode MODEL_FILE_WRONG = new ErrorCode(02005, "导入模型文件配置异常,请检查后重试!");

    // ========== 流程定义 9-003-000 ==========
    ErrorCode PROCESS_DEFINITION_KEY_NOT_MATCH = new ErrorCode(03000, "流程定义的标识期望是({})，当前是({})，请修改 BPMN 流程图");
    ErrorCode PROCESS_DEFINITION_NAME_NOT_MATCH = new ErrorCode(03001, "流程定义的名字期望是({})，当前是({})，请修改 BPMN 流程图");
    ErrorCode PROCESS_DEFINITION_NOT_EXISTS = new ErrorCode(03002, "流程定义不存在");
    ErrorCode PROCESS_DEFINITION_IS_SUSPENDED = new ErrorCode(03003, "流程定义处于挂起状态");
    ErrorCode PROCESS_DEFINITION_BPMN_MODEL_NOT_EXISTS = new ErrorCode(03004, "流程定义的模型不存在");

    // ========== 流程实例 9-004-000 ==========
    ErrorCode PROCESS_INSTANCE_NOT_EXISTS = new ErrorCode(04000, "流程实例不存在");
    ErrorCode PROCESS_INSTANCE_CANCEL_FAIL_NOT_EXISTS = new ErrorCode(04001, "流程取消失败，流程不处于运行中");
    ErrorCode PROCESS_INSTANCE_CANCEL_FAIL_NOT_SELF = new ErrorCode(04002, "流程取消失败，该流程不是你发起的");

    // ========== 流程任务 9-005-000 ==========
    ErrorCode TASK_COMPLETE_FAIL_NOT_EXISTS = new ErrorCode(05000, "审批任务失败，原因：该任务不处于未审批");
    ErrorCode TASK_COMPLETE_FAIL_ASSIGN_NOT_SELF = new ErrorCode(05001, "审批任务失败，原因：该任务的审批人不是你");
    ErrorCode TASK_BACK_FAIL_CAN_NOT_BACK = new ErrorCode(05002, "任务收回失败，原因：当前节点无可收回任务");
    ErrorCode TASK_BACK_FAIL_COMPARED = new ErrorCode(05002, "任务收回失败，原因：当前任务已处理，无法收回");

    // ========== 流程任务分配规则 9-006-000 ==========
    ErrorCode TASK_ASSIGN_RULE_EXISTS = new ErrorCode(06000, "流程({}) 的任务({}) 已经存在分配规则");
    ErrorCode TASK_ASSIGN_RULE_NOT_EXISTS = new ErrorCode(06001, "流程任务分配规则不存在");
    ErrorCode TASK_UPDATE_FAIL_NOT_MODEL = new ErrorCode(06002, "只有流程模型的任务分配规则，才允许被修改");
    ErrorCode TASK_CREATE_FAIL_NO_CANDIDATE_USER = new ErrorCode(06003, "操作失败，原因：找不到任务的审批人！");
    ErrorCode TASK_ASSIGN_SCRIPT_NOT_EXISTS = new ErrorCode(06004, "操作失败，原因：任务分配自定义脚本({}) 不存在");

    // ========== 用户组模块 9-011-000 ==========
    ErrorCode USER_GROUP_NOT_EXISTS = new ErrorCode(11000, "用户组不存在");
    ErrorCode USER_GROUP_IS_DISABLE = new ErrorCode(11001, "名字为【{}】的用户组已被禁用");

}
