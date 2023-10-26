package com.ctsi.ssdc.customize.service;

import com.ctsi.ssdc.customize.domain.CscpHxDformTable;
import com.ctsi.ssdc.gen.domain.TemplateEntity;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Copyright (c) CSII.
 * All Rights Reserved.
 * @author cczz
 * @version 1.0  2022/8/26 9:53  by xx
 */
public interface CscpListModelDesignService {
    int checkFormTable(String tableName);

    int updateModelInfo(Long tableId,String tableName);

    PageResult<Map> queryTemplatePage(Long pageId, TemplateEntity templateEntity, Pageable pageable);

    Map qryDataBaseInfo();

    Map deleteTable(CscpHxDformTable cscpHxFormTable);

    Boolean importExcelData(MultipartFile file,TemplateEntity templateEntity);

    // 查询子表
    List<Long> selectChildTable(Long mainTableId);
}
