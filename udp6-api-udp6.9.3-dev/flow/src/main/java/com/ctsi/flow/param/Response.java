package com.ctsi.flow.param;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author ：guoyanpei
 * @date ：Created in 2022-07-05 16:26
 * @description ：返回结果包装类
 */

public class Response<T> implements Serializable {

    private static final long serialVersionUID = -750644833749014618L;

    private boolean success;
    private T result;
    private Integer code;
    private String error;

    public Response() {
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.success = true;
        this.result = result;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.success = false;
        this.error = error;
    }

    public void setError(Integer code, String error) {
        this.success = false;
        this.code = code;
        this.error = error;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("success", this.success)
                .add("result", this.result)
                .add("code", this.code)
                .add("error", this.error)
                .omitNullValues().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Response)) {
            return false;
        } else {
            Response other = (Response)o;
            if (!Objects.equals(this.success, other.success)) {
                return false;
            } else if (!Objects.equals(this.result, other.result)) {
                return false;
            } else if (!Objects.equals(this.code, other.code)) {
                return false;
            } else if (!Objects.equals(this.error, other.error)){
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + (this.success ? 1 : 0);
        result = result * 59 + (this.result == null ? 0 : this.result.hashCode());
        result = result * 59 + (this.code == null ? 0 : this.code.hashCode());
        result = result * 59 + (this.error == null ? 0 : this.error.hashCode());
        return result;
    }

    public static <T> Response<T> ok(T data) {
        Response<T> resp = new Response();
        resp.setCode(0);
        resp.setResult(data);
        return resp;
    }

    public static <T> Response<T> ok() {
        return (Response<T>) ok((Object)null);
    }

    public static <T> Response<T> fail(String error) {
        Response<T> resp = new Response();
        resp.setError(error);
        return resp;
    }

    public static <T> Response<T> fail(Integer code, String error) {
        Response<T> resp = new Response();
        resp.setError(code, error);
        return resp;
    }

}
