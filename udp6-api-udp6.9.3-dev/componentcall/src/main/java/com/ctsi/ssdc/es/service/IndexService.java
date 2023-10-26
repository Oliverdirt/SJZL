package com.ctsi.ssdc.es.service;

public interface IndexService {

    /**
     * 索引是否存在
     * @param indexName
     * @return
     */
    boolean   existsIndex(String indexName);
    /**
     * 创建索引
     */
    void createIndex();

    /**
     * 删除索引
     * @param indexName
     */
    void deleteIndex(String indexName);
}
