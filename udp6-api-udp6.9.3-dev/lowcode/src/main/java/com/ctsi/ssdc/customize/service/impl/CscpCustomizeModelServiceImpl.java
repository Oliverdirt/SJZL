package com.ctsi.ssdc.customize.service.impl;

import com.ctsi.ssdc.customize.domain.CscpCustomizeModel;
import com.ctsi.ssdc.customize.domain.CscpCustomizeModelExample;
import com.ctsi.ssdc.customize.domain.CscpCustomizeVpage;
import com.ctsi.ssdc.customize.repository.CscpCustomizeModelRepository;
import com.ctsi.ssdc.customize.repository.CscpCustomizeVpageRepository;
import com.ctsi.ssdc.customize.repository.CscpHxDformColumnRepository;
import com.ctsi.ssdc.customize.repository.CscpHxDformTableRepository;
import com.ctsi.ssdc.customize.service.CscpCustomizeModelService;
import com.ctsi.ssdc.gen.domain.CscpHxFormTable;
import com.ctsi.ssdc.gen.repository.CscpHxFormColumnRepository;
import com.ctsi.ssdc.gen.repository.CscpHxFormTableRepository;
import com.ctsi.ssdc.gen.service.CscpHxFormTableService;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
 * Service Implementation for managing CscpCustomizeModel.
 *
 * @author hx
 * @date 2022-08-31 09:40:19
 *
 */

@Service
public class CscpCustomizeModelServiceImpl
	extends StrengthenBaseServiceImpl<CscpCustomizeModelRepository, CscpCustomizeModel, Long, CscpCustomizeModelExample>
	implements CscpCustomizeModelService {

    @Autowired
    private CscpCustomizeModelRepository cscpCustomizeModelRepository;
    @Autowired
    private CscpCustomizeVpageRepository cscpCustomizeVpageRepository;
    @Autowired
    private CscpCustomizeModelService cscpCustomizeModelService;
    @Autowired
    private CscpHxFormTableService cscpHxFormTableService;
    @Autowired
    private CscpHxDformColumnRepository cscpHxFormColumnRepository;
    @Autowired
    private CscpHxDformTableRepository cscpHxFormTableRepository;


    /**
     * 批量删除
     * @param modelIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] modelIds) {
        List<Long> delList = new ArrayList<>(Arrays.asList(modelIds));
        // 批量删除
        cscpCustomizeModelRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long modelId) {
        cscpCustomizeModelRepository.deleteByPrimaryKey(modelId);
    }

    @Override
    public List<CscpCustomizeModel> selectByModuleId(Long moduleId) {
        List<CscpCustomizeModel> customizeModels = cscpCustomizeModelRepository.selectByModuleId(moduleId);
        return customizeModels;
    }

    @Override
    public List<CscpCustomizeModel> selectByModuleIds(List<Long> moduleIds) {
        return cscpCustomizeModelRepository.selectByModuleIds(moduleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> delAll(Long[] modelIds) {
        List<CscpCustomizeVpage> customizevPagesByModelIds = cscpCustomizeVpageRepository.getCscpCustomizevPagesByModelIds(Arrays.asList(modelIds));
        if(CollectionUtils.isNotEmpty(customizevPagesByModelIds)){
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("code",500);
            hashMap.put("message","有页面在使用该数据模型");
            return hashMap;
        }
        //删除子表字段
        cscpHxFormColumnRepository.deleteChildColumnByModelIds(Arrays.asList(modelIds));
        //删除主表字段
        cscpHxFormColumnRepository.deleteMainColumnByModelIds(Arrays.asList(modelIds));
        //删除子表
        cscpHxFormTableRepository.deleteChileTableByModelId(Arrays.asList(modelIds));
        //删除主表
        cscpHxFormTableRepository.deleteByTableId(Arrays.asList(modelIds));
        //删除数据模型
        cscpCustomizeModelRepository.deleteByIds(Arrays.asList(modelIds));
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("code",200);
        hashMap.put("message","删除成功");
        return hashMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> delOne(Long modelId) {
        List<Long> listOne = new ArrayList<>();
        listOne.add(modelId);
        List<CscpCustomizeVpage> customizevPagesByModelIds = cscpCustomizeVpageRepository.getCscpCustomizevPagesByModelIds(listOne);
        if(CollectionUtils.isNotEmpty(customizevPagesByModelIds)){
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("code",500);
            hashMap.put("message","有页面在使用该数据模型");
            return hashMap;
        }
        //删除子表字段
        cscpHxFormColumnRepository.deleteChildColumnByModelIds(listOne);
        //删除主表字段
        cscpHxFormColumnRepository.deleteMainColumnByModelIds(listOne);
        //删除子表
        cscpHxFormTableRepository.deleteChileTableByModelId(listOne);
        //删除主表
        cscpHxFormTableRepository.deleteByTableId(listOne);
        //删除数据模型
        cscpCustomizeModelRepository.deleteByIds(listOne);

//        cscpCustomizeModelRepository.deleteByPrimaryKey(modelId);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("code",200);
        hashMap.put("message","删除成功");
        return hashMap;
    }
}
