package com.ctsi.flow.web.roleact;

import java.time.ZonedDateTime;
import java.util.*;

import com.ctsi.flow.param.model.Act;
import com.ctsi.flow.param.model.CscpFlowProcessDefExt;
import com.ctsi.flow.param.model.CscpRoleAct;
import com.ctsi.flow.param.model.CscpUserAct;
import com.ctsi.flow.server.roleact.CscpRoleActService;
import com.ctsi.flow.server.useract.CscpUserActService;
import com.ctsi.ssdc.admin.domain.CscpUserRole;
import com.ctsi.ssdc.admin.service.CscpUserRoleService;
import com.ctsi.ssdc.annotation.ComponentCallAnno;
import com.ctsi.ssdc.annotation.Log;
import com.ctsi.ssdc.constants.HxComponentConstant;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.util.AjaxBackData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.lang.Long;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing CscpRoleAct.
 *
 * @author hx
 * @date 2022-07-26 15:53:56
 *
 */

@Api(tags = "工作流-权限管理")
@RestController
@RequestMapping("/api")
public class CscpRoleActApi {

    private final Logger log = LoggerFactory.getLogger(CscpRoleActApi.class);

    private final CscpRoleActService cscpRoleActService;
    @Autowired
    private CscpUserActService cscpUserActService;
    @Autowired
    private CscpUserRoleService cscpUserRoleService;

    public CscpRoleActApi(CscpRoleActService cscpRoleActService) {
        this.cscpRoleActService = cscpRoleActService;
    }

    /*增加、修改流程与用户、角色绑定关系*/
    @PutMapping("/cscpActs/addAct")
    @ApiOperation(value = "增加、修改流程与用户、角色绑定关系")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "addAct")
    @Log
    public AjaxBackData addAct(@RequestBody Act act){
        log.debug("REST request to put addAct : {}", act);
        AjaxBackData ajaxBackData = new AjaxBackData(true,"");
        //type = 1 与角色绑定  2 与用户绑定
        if("1".equals(act.getType())){
            cscpRoleActService.deleteByProcDefId(act.getProcDefId());
            cscpUserActService.deleteByProcDefId(act.getProcDefId());
            List<CscpRoleAct> cscpRoleActs = new ArrayList<>();
            List<Long> relation = act.getRelation();
            for(Long one : relation){
                CscpRoleAct cscpRoleAct = new CscpRoleAct();
                cscpRoleAct.setRoleId(one);
                cscpRoleAct.setProcDefId(act.getProcDefId());
                cscpRoleAct.setFormId(act.getFormId());
                cscpRoleAct.setProcDefName(act.getProcDefName());
                cscpRoleActs.add(cscpRoleAct);
            }
            cscpRoleActService.insertList(cscpRoleActs);

            List<CscpUserRole> cscpUserRoles = cscpUserRoleService.selectUserByRoleId(relation);
            if(cscpUserRoles != null && cscpUserRoles.size() > 0){
                List<CscpUserAct> cscpUserActs = new ArrayList<>();
                for (CscpUserRole oneUserRole : cscpUserRoles){
                    CscpUserAct cscpUserAct = new CscpUserAct();
                    cscpUserAct.setUserId(oneUserRole.getUserId());
                    cscpUserAct.setProcDefId(act.getProcDefId());
                    cscpUserAct.setFormId(act.getFormId());
                    cscpUserAct.setProcDefName(act.getProcDefName());
                    cscpUserAct.setIsCollect("0");
                    cscpUserAct.setProcessId(act.getProcessId());
                    cscpUserAct.setPublishTime(ZonedDateTime.now());
                    cscpUserActs.add(cscpUserAct);
                }
                cscpUserActService.insertList(cscpUserActs);
            }

        }else if("2".equals(act.getType())){
            cscpRoleActService.deleteByProcDefId(act.getProcDefId());
            cscpUserActService.deleteByProcDefId(act.getProcDefId());
            List<CscpUserAct> cscpUserActs = new ArrayList<>();
            for(Long one : act.getRelation()){
                CscpUserAct cscpUserAct = new CscpUserAct();
                cscpUserAct.setUserId(one);
                cscpUserAct.setProcDefId(act.getProcDefId());
                cscpUserAct.setFormId(act.getFormId());
                cscpUserAct.setProcDefName(act.getProcDefName());
                cscpUserAct.setIsCollect("0");
                cscpUserAct.setProcessId(act.getProcessId());
                cscpUserAct.setPublishTime(ZonedDateTime.now());
                cscpUserActs.add(cscpUserAct);
            }
            cscpUserActService.insertList(cscpUserActs);
        }
        return ajaxBackData;
    }

    /*查询流程与用户、角色绑定关系*/
    @GetMapping("/cscpActs/getAct")
    @ApiOperation(value = "查询流程与用户、角色绑定关系")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "getAct")
    @Log
    public Map<String, Object> getAct(String id){
        log.debug("REST request to get getAct : {}", id);
        Map<String, Object> map = new HashMap<>();
        List<CscpRoleAct> byProcInstId = cscpRoleActService.findByProcDefId(id);
        if(byProcInstId != null && byProcInstId.size() > 0){
            List<Long> strings = new ArrayList<>();
            for (CscpRoleAct one : byProcInstId){
                Long roleId = one.getRoleId();
                strings.add(roleId);
            }
            map.put("type",1);
            map.put("relation",strings);
            return map;
        }
        List<CscpUserAct> byProcInstId1 = cscpUserActService.findByProcDefId(id);
        if(byProcInstId1 != null && byProcInstId1.size() > 0){
            List<Long> strings = new ArrayList<>();
            for (CscpUserAct one : byProcInstId1){
                Long procDefId = one.getUserId();
                strings.add(procDefId);
            }
            map.put("type",2);
            map.put("relation",strings);
        }
        return map;
    }

    /*收藏按钮  type=1 添加收藏   0=取消收藏*/
    @GetMapping("/actRdCollects/addCollect")
    @ApiOperation(value = "添加、取消收藏")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "addCollect")
    @Log
    public Map<String, Object> addCollect(String type,String proDefId) {
        log.debug("REST request to get addCollect : {}", type + proDefId);
        Map<String, Object> map = new HashMap<>();
        Long userId = SecurityHxUtils.getCurrentUserId();
        int i = cscpUserActService.updateByProDefId(proDefId, type, userId,ZonedDateTime.now());
        if(i == 1){
            map.put("success",1);
            map.put("code",200);
        }
        return map;
    }

    /*我的流程  type = 1 我的流程  2 我的收藏*/
    @GetMapping("/actRdCollects/selectActByUserId")
    @ApiOperation(value = "我的流程、收藏")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "selectActByUserId")
    @Log
    public Map<String, Object> selectActByUserId(Integer type) {
        log.debug("REST request to get selectActByUserId : {}", type );
        Long currentUserId = SecurityHxUtils.getCurrentUserId();
        Map<String, Object> map = new HashMap<>();
        if(type == 1){
            List<CscpUserAct> byUserId = cscpUserActService.findByUserId(currentUserId);
            map.put("data",byUserId);
        }else if(type == 2){
            List<CscpUserAct> myCollect = cscpUserActService.findMyCollect(currentUserId);
            map.put("data",myCollect);
        }else{
            map.put("data","");
        }
        map.put("success",1);
        map.put("code",200);
        return map;
    }

    /*发起流程*/
    @GetMapping("/actRdCollects/launchProcess")
    @ApiOperation(value = "发起流程")
    @ComponentCallAnno(component = HxComponentConstant.ACTIVITI,method = "launchProcess")
    @Log
    public Map<String,Object> launchProcess(String proDefId){
        log.debug("REST request to get launchProcess : {}", proDefId );
        Map<String, Object> map = new HashMap<>();
        List<CscpFlowProcessDefExt> bpmProcessDefinition = cscpUserActService.selectByModelId(proDefId);
        map.put("data",bpmProcessDefinition.get(0));
        map.put("success",1);
        map.put("code",200);
        return map;
    }
}
