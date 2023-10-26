package com.ctsi.ssdc.customize.service;

import com.ctsi.ssdc.gen.domain.TemplateEntity;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CscpCustomizePageTemplateService {
    PageResult<Map> queryTemplatePage(Long pageId, TemplateEntity templateEntity, Pageable pageable);
    Map queryTemplate(Long pageId , Map<String,Object> queryMap);
    int delTemplate(Long pageId,Map<String , Object> delMap);
    int addTemplate(Long pageId,Map<String , Object> addMap);
    int updateTemplate( Long pageId,  Map<String , Object> updateMap);
    int batchDelTemplate(Long pageId, Map<String, Object> delMap);
    Map<String,String> getPkColumn(List<String> tableNameList, List<String> tableSchemaList);
    Map queryByInstanceId(Long pageId, Map<String, Object> queryMap);
    AjaxResult taskUploadExcel(MultipartFile file,Long formId,String queryStr) throws Exception;
//    void export(Long formId, TemplateEntity templateEntity, HttpServletResponse response) throws IOException;
PageResult<Map> queryTemplatePageBatch(Long pageId, TemplateEntity templateEntity, Pageable pageable);
}
