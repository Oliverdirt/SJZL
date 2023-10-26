package com.ctsi.ssdc.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ctsi.ssdc.admin.constants.PermisionCodeConstant;
import com.ctsi.ssdc.admin.domain.dto.CscpMenusDTO;
import com.ctsi.ssdc.admin.repository.CscpMenusRepository;
import com.ctsi.ssdc.admin.service.CscpCheckPermisionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("cscpCheckPermisionService")
public class CscpCheckPermisionServiceImpl implements CscpCheckPermisionService {
    @Autowired
    private CscpMenusRepository cscpMenusRepository;

    @Override
    public boolean checkPermisionByUrl() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
        //获取url
        String url = request.getRequestURI();
        if(CollectionUtil.isEmpty(PermisionCodeConstant.permisionCodeList)){
            return false;
        }
        //如果是低代码多表 校验其子表的权限 与主表权限保持一致
        if(url.contains("/api/lowcode/customize/page/template")){
            String[] urlSplit = url.split("/");
            Long pageId = Long.parseLong(urlSplit[urlSplit.length - 1]);
            Long mainPageId = cscpMenusRepository.selectMainPageIdByPrimaryKey(pageId);
            //如果获取主表formId 则判断主表权限
            if(mainPageId != null){
                List<String> urlList = PermisionCodeConstant.permisionCodeList.stream().map(CscpMenusDTO :: getUrl).collect(Collectors.toList());
                //子表是否配置菜单 未配置菜单直接放开权限
                if(!urlList.contains("/customize/page/" + pageId)){
                    return true;
                }
            }
        }
        //过滤获取按钮菜单
        List<CscpMenusDTO> buttonMenu = PermisionCodeConstant.permisionCodeList.stream().filter(p->StringUtils.equalsIgnoreCase("button",p.getType())).collect(Collectors.toList());
        if(CollectionUtil.isEmpty(buttonMenu)){
            return false;
        }
        List<String> permitUrlList = buttonMenu.stream().map(CscpMenusDTO :: getUrl).collect(Collectors.toList());
        return permitUrlList.contains(url);
    }
}
