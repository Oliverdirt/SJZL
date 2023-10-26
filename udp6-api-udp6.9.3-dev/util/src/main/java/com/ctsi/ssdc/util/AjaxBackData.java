package com.ctsi.ssdc.util;

public class AjaxBackData {
    private boolean success = true;
    private String msg;
    private String id;
    private String token;
    private int code = 0;

    public AjaxBackData() {}

    public AjaxBackData(boolean success, String msg) {
        this.id = java.util.UUID.randomUUID().toString();
        this.success = success;
        this.msg = msg;
    }
    
    public AjaxBackData(boolean success, String msg, int code) {
    	this(success, msg);
    	this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
}
