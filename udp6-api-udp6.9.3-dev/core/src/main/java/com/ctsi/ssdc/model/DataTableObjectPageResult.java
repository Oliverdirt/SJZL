package com.ctsi.ssdc.model;

import java.util.List;



public class DataTableObjectPageResult {

    private AjaxBackData ajax;
    private String dtid;
    private List<?> cols;
    private List<?> data;
    private Integer recordsTotal;
    private Integer recordsFiltered;

    public AjaxBackData getAjax() {
        return ajax;
    }

    public void setAjax(AjaxBackData ajax) {
        this.ajax = ajax;
    }

    public List<?> getCols() {
        return cols;
    }

    public void setCols(List<?> cols) {
        this.cols = cols;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public Integer getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Integer recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Integer getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Integer recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public String getDtid() {
        return dtid;
    }

    public void setDtid(String dtid) {
        this.dtid = dtid;
    }

}
