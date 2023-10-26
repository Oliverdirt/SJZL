package com.ctsi.flow.server.rule;


import com.ctsi.flow.param.model.CscpFlowFieldPerms;
import com.ctsi.flow.param.model.CscpFlowFieldPermsExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

import java.util.List;

/**
 * Service Interface for managing CscpFlowFieldPerms.
 *
 * @author hx
 * @date 2022-10-24 15:47:57
 */

public interface CscpFlowFieldPermsService
        extends StrengthenBaseService<CscpFlowFieldPerms, Long, CscpFlowFieldPermsExample> {

    /**
     * 批量删除
     *
     * @param ids
     */
    void deleteByIds(Long[] ids);

    void deleteById(Long id);
    /**
     * @Description: 更新字段权限
     **/
    void updateFieldsPerm(CscpFlowFieldPerms cscpFlowFieldPerms);

   /**
    * @Description: 查询字段权限
    **/
    CscpFlowFieldPerms findByTaskDefKey(CscpFlowFieldPerms cscpFlowFieldPerms);

}
