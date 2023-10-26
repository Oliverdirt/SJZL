package com.ctsi.ssdc.gen.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class NoModelWriteData implements Serializable {
    private String fileName;//文件名
    private List<String> headMap;//表头数组
    private List<String> dataStrMap;//对应数据字段数组
    private List<Map<String, Object>> dataList;//数据集合

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getHeadMap() {
        return headMap;
    }

    public void setHeadMap(List<String> headMap) {
        this.headMap = headMap;
    }

    public List<String> getDataStrMap() {
        return dataStrMap;
    }

    public void setDataStrMap(List<String> dataStrMap) {
        this.dataStrMap = dataStrMap;
    }

    public List<Map<String, Object>> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }
}
