package com.ctsi.ssdc.customize.service.impl;

import com.ctsi.ssdc.customize.domain.CscpCustomizeModel;
import com.ctsi.ssdc.customize.domain.CscpCustomizeModule;
import com.ctsi.ssdc.customize.domain.CscpCustomizeModuleExample;
import com.ctsi.ssdc.customize.domain.CscpCustomizeVpage;
import com.ctsi.ssdc.customize.repository.*;
import com.ctsi.ssdc.customize.service.CscpCustomizeModelService;
import com.ctsi.ssdc.customize.service.CscpCustomizeModuleService;
import com.ctsi.ssdc.customize.service.CscpCustomizeVpageService;
import com.ctsi.ssdc.gen.domain.CscpHxFormTable;
import com.ctsi.ssdc.gen.repository.CscpHxFormColumnRepository;
import com.ctsi.ssdc.gen.repository.CscpHxFormTableRepository;
import com.ctsi.ssdc.gen.service.CscpHxFormTableService;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import io.swagger.annotations.Example;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service Implementation for managing CscpCustomizeModule.
 *
 * @author hx
 * @date 2022-08-29 16:34:09
 *
 */

@Service
public class CscpCustomizeModuleServiceImpl
	extends StrengthenBaseServiceImpl<CscpCustomizeModuleRepository, CscpCustomizeModule, Long, CscpCustomizeModuleExample>
	implements CscpCustomizeModuleService {

    @Autowired
    private CscpCustomizeModuleRepository cscpCustomizeModuleRepository;
    @Autowired
    private CscpCustomizeModelService cscpCustomizeModelService;
    @Autowired
    private CscpHxFormTableService cscpHxFormTableService;
    @Autowired
    private CscpCustomizeVpageService cscpCustomizeVpageService;
    @Autowired
    private CscpHxDformColumnRepository cscpHxFormColumnRepository;
    @Autowired
    private CscpHxDformTableRepository cscpHxFormTableRepository;
    @Autowired
    private CscpCustomizeModelRepository cscpCustomizeModelRepository;
    @Autowired
    private CscpCustomizeVfieldPageRepository cscpCustomizeVfieldPageRepository;
    @Autowired
    private CscpCustomizeVpageRepository cscpCustomizeVpageRepository;


    /**
     * 批量删除
     * @param moduleIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] moduleIds) {
        List<Long> delList = new ArrayList<>(Arrays.asList(moduleIds));
        // 批量删除
        cscpCustomizeModuleRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long moduleId) {
        cscpCustomizeModuleRepository.deleteByPrimaryKey(moduleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Long[] moduleId) {
        List<CscpCustomizeModel> customizeModels = cscpCustomizeModelService.selectByModuleIds(Arrays.asList(moduleId));
        if(CollectionUtils.isNotEmpty(customizeModels)){
            List<Long> modelList = new ArrayList<>();
            for (CscpCustomizeModel oneModels : customizeModels){
                modelList.add(oneModels.getModelId());
            }

            //删除子表字段
            cscpHxFormColumnRepository.deleteChildColumnByModelIds(modelList);
            //删除主表字段
            cscpHxFormColumnRepository.deleteMainColumnByModelIds(modelList);
            //删除子表
            cscpHxFormTableRepository.deleteChileTableByModelId(modelList);
            //删除主表
            cscpHxFormTableRepository.deleteByTableId(modelList);
            //删除数据模型
            cscpCustomizeModelRepository.deleteByIds(modelList);
        }

        List<CscpCustomizeVpage> customizevPagesByModuleIds = cscpCustomizeVpageService.getCscpCustomizeVpagesByModuleIds(moduleId);
        List<Long> pageList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(customizevPagesByModuleIds)){
            for(CscpCustomizeVpage onePage : customizevPagesByModuleIds){
                pageList.add(onePage.getPageId());
            }
            //删除VFieldPage表
            cscpCustomizeVfieldPageRepository.deleteByPageIdBatch(pageList);
            //删除菜单
            cscpCustomizeVpageService.deleteMenuBatch(pageList);
        }

        List<Long> delList = new ArrayList<>(Arrays.asList(moduleId));
        //删除VPage表
        cscpCustomizeVpageRepository.deleteByModuleIds(delList);
        //删除module表
        cscpCustomizeModuleRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOne(Long moduleId) {
        List<CscpCustomizeModel> customizeModels = cscpCustomizeModelService.selectByModuleId(moduleId);
        if(CollectionUtils.isNotEmpty(customizeModels)) {
            List<Long> modelList = new ArrayList<>();
            for (CscpCustomizeModel oneModels : customizeModels) {
                modelList.add(oneModels.getModelId());
            }

            //删除子表字段
            cscpHxFormColumnRepository.deleteChildColumnByModelIds(modelList);
            //删除主表字段
            cscpHxFormColumnRepository.deleteMainColumnByModelIds(modelList);
            //删除子表
            cscpHxFormTableRepository.deleteChileTableByModelId(modelList);
            //删除主表
            cscpHxFormTableRepository.deleteByTableId(modelList);
            //删除数据模型
            cscpCustomizeModelRepository.deleteByIds(modelList);
        }
//        for (int i = 0; i < customizeModels.size(); i++) {
//            Long tableId = cscpCustomizeModelService.findOne(customizeModels.get(i).getModelId()).getTableId();
//            List<Long> list = new ArrayList<>();
//            list.add(tableId);
//            String subTableName = cscpHxFormTableService.findOne(tableId).getSubTableName();
//            if(StringUtils.isNotEmpty(subTableName)){
//                List<String> tableNameList = Arrays.asList(subTableName.split(","));
////                for (int j = 0; j <tableNameList.size() ; j++) {
////                    CscpHxFormTable cscpHxFormTable = cscpHxFormTableService.selectByTableName(tableNameList.get(j));
////                    list.add(cscpHxFormTable.getTableId());
////                }
//                List<CscpHxFormTable> cscpHxFormTable = cscpHxFormTableService.selectByTableName(tableNameList);
//                for (CscpHxFormTable oneTableId : cscpHxFormTable) {
//                    list.add(oneTableId.getTableId());
//                }
//            }
//            //删除column表
//            cscpHxFormColumnRepository.deleteByTableIds(list);
//            //删除table表
//            cscpHxFormTableRepository.deleteByIds(list);
//            //删除model表
//            cscpCustomizeModelRepository.deleteByPrimaryKey(customizeModels.get(i).getModelId());
//        }

        List<Long> moduleList = new ArrayList<>();
        moduleList.add(moduleId);
        Long[] longs = moduleList.stream().toArray(Long[]::new);
        List<CscpCustomizeVpage> customizevPagesByModuleIds = cscpCustomizeVpageService.getCscpCustomizeVpagesByModuleIds(longs);
        List<Long> pageList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(customizevPagesByModuleIds)){
            for(CscpCustomizeVpage onePage : customizevPagesByModuleIds){
                pageList.add(onePage.getPageId());
            }
            //删除VFieldPage表
            cscpCustomizeVfieldPageRepository.deleteByPageIdBatch(pageList);
            //删除菜单
            cscpCustomizeVpageService.deleteMenuBatch(pageList);
        }

        List<Long> delList = new ArrayList<>(Arrays.asList(moduleId));
        //删除VPage表
        cscpCustomizeVpageRepository.deleteByModuleIds(delList);
        //删除module表
        cscpCustomizeModuleRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByModuleId(Map<String,Object> map){
        cscpCustomizeModuleRepository.updateByModuleId(map);
    }
}
