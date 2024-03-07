package com.ctsi.ssdc.utils;

import com.alibaba.fastjson.JSONObject;

import static com.ctsi.ssdc.utils.ConstantValue.*;

public class JSONResult {

    private JSONResult() {

    }

    public static JSONObject result(Object object, Integer code, String msg) {
        JSONObject result = new JSONObject();
        result.put(FIELD_CODE, code);
        result.put(FIELD_MSG, msg);
        result.put(FIELD_DATA, object);
        return result;
    }

    public static JSONObject success(Object object) {
        JSONObject result = new JSONObject();
        result.put(FIELD_CODE, NUM_ZERO);
        result.put(FIELD_MSG, FIELD_SUCCESS);
        result.put(FIELD_DATA, object);
        return result;
    }

    public static JSONObject success(Object object, Integer flag, String msg) {
        JSONObject result = new JSONObject();
        result.put(FIELD_CODE, flag);
        result.put(FIELD_MSG, msg);
        result.put(FIELD_DATA, object);
        return result;
    }


    public static JSONObject successInfo(Object object, Integer flag) {
        JSONObject result = new JSONObject();
        result.put(FIELD_CODE, NUM_ZERO);
        result.put(FIELD_MSG, FIELD_SUCCESS);
        result.put(FIELD_DATA, object);
        result.put(FIELD_FLAG, flag);
        return result;
    }

    public static JSONObject success() {
        JSONObject result = new JSONObject();
        result.put(FIELD_CODE, NUM_ZERO);
        result.put(FIELD_MSG, FIELD_SUCCESS);
        return result;
    }

    public static JSONObject fail(Object object) {
        JSONObject result = new JSONObject();
        result.put(FIELD_CODE, NUM_ONE);
        result.put(FIELD_MSG, FIELD_FAIL);
        result.put(FIELD_DATA, object);
        return result;
    }


    public static JSONObject failMsg(Object object) {
        JSONObject result = new JSONObject();
        result.put(FIELD_CODE, NUM_ONE);
        result.put(FIELD_MSG, FIELD_FAIL);
        result.put(FIELD_DESCRIPTION, object);

        return result;
    }


    public static JSONObject failDetail(Object object) {
        JSONObject result = new JSONObject();
        result.put(FIELD_CODE, NUM_TWO);
        result.put(FIELD_MSG, FIELD_FAIL);
        result.put(FIELD_DESCRIPTION, object);

        return result;
    }

    public static JSONObject toAjax(int rows) {
        return rows > 0 ? success("操作成功") : fail("操作失败");
    }

    public static JSONObject crmSuccess() {
        JSONObject result = new JSONObject();
        result.put(FIELD_CODE, NUM_ZERO);
        result.put(FIELD_MSG, FIELD_SUCCESS_CHINESE);
        return result;
    }

    public static JSONObject crmFail(Object errorCode, Object errorMsg) {
        JSONObject result = new JSONObject();
        result.put("success", false);
        result.put("code", errorCode);
        result.put("errorMsg", errorMsg);
        return result;
    }
}
