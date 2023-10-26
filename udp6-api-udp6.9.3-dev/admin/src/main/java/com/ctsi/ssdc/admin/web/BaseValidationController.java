package com.ctsi.ssdc.admin.web;

import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.admin.domain.CscpOrgExample;
import com.ctsi.ssdc.admin.domain.CscpRolesExample;
import com.ctsi.ssdc.admin.domain.CscpWorkGroupExample;
import com.ctsi.ssdc.admin.repository.CscpOrgRepository;
import com.ctsi.ssdc.admin.repository.CscpRolesRepository;
import com.ctsi.ssdc.admin.repository.CscpWorkGroupRepository;
import com.ctsi.ssdc.admin.service.CscpUserDetailService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.model.AjaxBackData;
import com.ctsi.ssdc.security.SecurityHxUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 基础表单校验
 * <p>
 * The code change the world !!!
 *
 * @author guochui
 * @create 2021-10-15 14:15
 **/
@RestController
@RequestMapping("/api/validation")
public class BaseValidationController {

    @Autowired
    private CscpRolesRepository rolesRepository;

    @Autowired
    private CscpWorkGroupRepository workGroupRepository;

    @Autowired
    private CscpOrgRepository orgRepository;



    @Autowired
    private CscpUserDetailService cscpUserDetailService;


    /**
     * 角色名校验，不能重复
     * @author chxq
     * 2020年01月25日下午5:12:23
     * @param name
     * @return
     */

    @GetMapping("/role")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "checkRole")
    public AjaxBackData checkRole(String name) {
        CscpRolesExample rolesExample = new CscpRolesExample();
        CscpRolesExample.Criteria critieria = rolesExample.createCriteria();
        if (StringUtils.isNotBlank(name)) {
            critieria.andNameEqualTo(name);
        }


        // 增加tenantId
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        Long tenantId = cscpUserDetailService.findByUserId(currentUserId).getTenantId();

        critieria.andTenantIdEqualTo(tenantId);


        long l = rolesRepository.countByExample(rolesExample);
        AjaxBackData ajaxBackData = new AjaxBackData();
        if(l > 0 ){
            ajaxBackData.setCode(-1);
            ajaxBackData.setMsg("角色名不能重复!");
        }else{
            ajaxBackData.setCode(200);
            ajaxBackData.setMsg("校验成功");
        }
        return ajaxBackData;
    }

    /**
     * 工作组名校验，不能重复
     * @author chxq
     * 2020年01月25日下午5:12:23
     * @param name
     * @return
     */

    @GetMapping("/workGroup")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "checkWorkGroup")
    public AjaxBackData checkWorkGroup(String name){
        CscpWorkGroupExample cscpWorkGroupExample = new CscpWorkGroupExample();
        CscpWorkGroupExample.Criteria critieria = cscpWorkGroupExample.createCriteria();
        if (StringUtils.isNotBlank(name)) {
            critieria.andGroupNameEqualTo(name);
        }
        long l = workGroupRepository.countByExample(cscpWorkGroupExample);
        AjaxBackData ajaxBackData = new AjaxBackData();
        if(l > 0 ){
            ajaxBackData.setCode(-1);
            ajaxBackData.setMsg("工作组名不能重复!");
        }else{
            ajaxBackData.setCode(200);
            ajaxBackData.setMsg("校验成功");
        }
        return ajaxBackData;
    }

    /**
     * 机构名校验，不能重复
     * @author chxq
     * 2020年01月25日下午5:12:23
     * @param name
     * @return
     */

    @GetMapping("/org")
    @ComponentCallAnno(component = HxComponentConstant.ADMIN,method = "checkOrg")
    public AjaxBackData checkOrg(String name, Long parentId)  {
        CscpOrgExample orgExample = new CscpOrgExample();
        CscpOrgExample.Criteria critieria = orgExample.createCriteria();
        if (StringUtils.isNotBlank(name)) {
            critieria.andOrgNameEqualTo(name);
            critieria.andParentIdEqualTo(parentId);
        }
        long l = orgRepository.countByExample(orgExample);
        AjaxBackData ajaxBackData = new AjaxBackData();
        if(l > 0 ){
            ajaxBackData.setCode(-1);
            ajaxBackData.setMsg("名称不能重复!");
        }else{
            ajaxBackData.setCode(200);
            ajaxBackData.setMsg("校验成功");
        }
        return ajaxBackData;
    }

}
