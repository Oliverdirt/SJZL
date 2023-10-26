package com.ctsi.ssdc.model;


import net.sf.json.JSONObject;

public class SolrQueryObject {

    private JSONObject example;
    private AjaxBackData ajax;

    public JSONObject getExample() {
        return example;
    }

    public void setExample(JSONObject example) {
        this.example = example;
    }

    public AjaxBackData getAjax() {
        return ajax;
    }

    public void setAjax(AjaxBackData ajax) {
        this.ajax = ajax;
    }

}
