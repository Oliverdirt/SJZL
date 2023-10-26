package com.ctsi.ssdc.model;



public class AjaxForm {

    AjaxBackData ajax;

    public AjaxForm(AjaxBackData ajax) {
        this.ajax = ajax;
    }

    public AjaxForm(boolean success, String msg) {
        this.ajax = new AjaxBackData(success, msg);
    }

    public AjaxForm() {

    }

    public AjaxBackData getAjax() {
        return ajax;
    }

    public void setAjax(AjaxBackData ajax) {
        this.ajax = ajax;
    }

}
