package com.ctsi.ssdc.es.service;


import com.ctsi.ssdc.log.domain.CscpComponentCallLog;

/**
 * ES文档操作接口
 */
public interface DocumentService {
    /**
     * 文档是否存在
     * @return
     */
    boolean existsDocument(String id);

    /**
     * 单个添加文档
     */
    void addDocument();

    /**
     * 批量添加文档
     */
    void batchAddDoc();

    /**
     * 获取文档信息
     */
    CscpComponentCallLog getDocument(String id);

    /**
     * 删除文档信息
     */
    void deleteDocument(String id);
}
