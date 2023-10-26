package com.ctsi.ssdc.customize.service;
import com.ctsi.ssdc.customize.domain.CscpCustomizeVpage;
import com.ctsi.ssdc.customize.domain.CscpCustomizeVpageExample;
import com.ctsi.ssdc.customize.domain.CscpHxDformColumn;
import com.ctsi.ssdc.service.StrengthenBaseService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Service Interface for managing CscpCustomizeVpage.
 *
 * @author hx
 * @date 2022-09-01 14:40:59
 *
 */

public interface CscpCustomizeVpageService
        extends StrengthenBaseService<CscpCustomizeVpage,Long , CscpCustomizeVpageExample>{

    /**
     * 批量删除
     * @param pageIds
     */
    void deleteByIds(Long[] pageIds);

    void deleteById(Long pageId);

    /**
     * 生成业务模块表单信息查询
     * @param cscpCustomizeVpage
     * @return
     */
    List<CscpCustomizeVpage> qryPageTableInfo(CscpCustomizeVpage cscpCustomizeVpage);
    /**
     * 根据module_id批量删除
     * @param moduleIds
     */
    void deleteByModuleIds(List<Long> moduleIds);

    List<CscpCustomizeVpage> queryChildCscpCustomizeVpages(Long[] mainPageId);

    List<CscpCustomizeVpage> getCscpCustomizeVpagesByModuleIds(Long[] moduleIds);

    void deleteMenuBatch(List<Long> pageIds);
    void export(Long formId, Map<String, Object> addMap, HttpServletResponse response) throws IOException;

    List<Long> selectPageIdsByMainPageId(List<Long> mainPageIds);

    List<CscpHxDformColumn> selectRelationColumnByPageId(Long pageId);

    List<CscpHxDformColumn> selectRelationColumnByTableNames(List<String> tableNames);
}
