package com.ctsi.ssdc.demo;

import com.ctsi.ssdc.admin.domain.CscpTenant;
import com.ctsi.ssdc.admin.domain.CscpTenantExample;
import com.ctsi.ssdc.admin.repository.CscpTenantRepository;
import com.ctsi.ssdc.dic.domain.CscpHxDicItem;
import com.ctsi.ssdc.dic.model.DicItemModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("testService")
public class TestService {

    @Autowired
    CscpTenantRepository cscpTenantRepository;

    public DicItemModel getTenants(){
        DicItemModel dicItemModel = new DicItemModel();
        List<CscpHxDicItem> cscpHxDicItems = new ArrayList<>();
        CscpTenantExample cscpTenantExample = new CscpTenantExample();
        List<CscpTenant> cscpTenants = cscpTenantRepository.selectByExample(cscpTenantExample);
        for (CscpTenant cscpTenant : cscpTenants) {
            Long tenantId = cscpTenant.getId();
            String tenantName = cscpTenant.getTenantName();
            CscpHxDicItem dicItem = new CscpHxDicItem();
            dicItem.setItemCode(tenantId+"");
            dicItem.setItemValue(tenantName);
            cscpHxDicItems.add(dicItem);
        }
        dicItemModel.setCscpHxDicItems(cscpHxDicItems);
        return dicItemModel;
    }


    public DicItemModel getTenantAccounts(){
        DicItemModel dicItemModel = new DicItemModel();
        List<CscpHxDicItem> cscpHxDicItems = new ArrayList<>();
        CscpTenantExample cscpTenantExample = new CscpTenantExample();
        List<CscpTenant> cscpTenants = cscpTenantRepository.selectByExample(cscpTenantExample);
        for (CscpTenant cscpTenant : cscpTenants) {
            String tenantAccount = cscpTenant.getTenantAccount();
            String tenantName = cscpTenant.getTenantName();
            CscpHxDicItem dicItem = new CscpHxDicItem();
            dicItem.setItemCode(tenantAccount);
            dicItem.setItemValue(tenantName);
            cscpHxDicItems.add(dicItem);
        }
        dicItemModel.setCscpHxDicItems(cscpHxDicItems);
        return dicItemModel;
    }
}
