package com.ctsi.ssdc.model;

import java.io.Serializable;
import java.util.List;

public class PageResult<T> implements Serializable{

    /**
	 * default
	 */
	private static final long serialVersionUID = 1L;
	
	private List<T> data;
    private long recordsTotal;
    private long recordsFiltered;
    
    public PageResult() {
    	
    }
    
    public PageResult(List<T> data,long recordsTotal,long recordsFiltered) {
    	this.data = data;
    	this.recordsFiltered = recordsFiltered;
    	this.recordsTotal = recordsTotal;
    }
    
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public long getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public long getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
    

}
