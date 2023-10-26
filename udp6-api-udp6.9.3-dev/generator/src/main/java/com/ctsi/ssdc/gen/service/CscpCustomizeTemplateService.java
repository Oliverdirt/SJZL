package com.ctsi.ssdc.gen.service;

import com.ctsi.ssdc.gen.domain.TemplateEntity;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CscpCustomizeTemplateService {
    PageResult<Map> queryTemplatePage(Long formId, TemplateEntity templateEntity, Pageable pageable);
    Map queryTemplate(Long formId , Map<String,Object> queryMap);
    int delTemplate(Long formI,Map<String , Object> delMap);
    int addTemplate(Long formId,Map<String , Object> addMap);
    int updateTemplate( Long formId,  Map<String , Object> updateMap);
    int batchDelTemplate(Long formId, Map<String, Object> delMap);
    Map<String,String> getPkColumn(List<String> tableNameList, List<String> tableSchemaList);
    Map queryByInstanceId(Long formId, Map<String, Object> queryMap);
    AjaxResult taskUploadExcel(MultipartFile file,Long formId) throws Exception;
    void export(Long formId, TemplateEntity templateEntity, HttpServletResponse response) throws IOException;
}
