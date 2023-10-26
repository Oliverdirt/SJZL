package com.ctsi.ssdc.gen.service.impl;

import com.ctsi.ssdc.admin.domain.CscpMenus;
import com.ctsi.ssdc.admin.service.CscpMenusService;
import com.ctsi.ssdc.gen.domain.CscpCustomizeVfield;
import com.ctsi.ssdc.gen.domain.CscpCustomizeVform;
import com.ctsi.ssdc.gen.domain.CscpCustomizeVformExample;
import com.ctsi.ssdc.gen.repository.CscpCustomizeVfieldRepository;
import com.ctsi.ssdc.gen.repository.CscpCustomizeVformRepository;
import com.ctsi.ssdc.gen.service.CscpCustomizeVformService;
import com.ctsi.ssdc.gen.repository.CscpHxInfoSchemaMysqlRepository;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CscpCustomizeVform.
 *
 * @author hx
 * @date 2022-05-23 09:59:33
 */

@Service
public class CscpCustomizeVformServiceImpl
        extends StrengthenBaseServiceImpl<CscpCustomizeVformRepository, CscpCustomizeVform, Long, CscpCustomizeVformExample>
        implements CscpCustomizeVformService {


    @Autowired
    private CscpCustomizeVformRepository cscpCustomizeVformRepository;
    @Autowired
    private CscpCustomizeVfieldRepository customizeVfieldRepository;
    @Autowired
    private CscpMenusService cscpMenusService;

    @Autowired
    private CscpHxInfoSchemaMysqlRepository informationSchemaMysqlRepository;


    /**
     * 批量删除
     *
     * @param formIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] formIds) {
        List<Long> delList = new ArrayList<>(Arrays.asList(formIds));
        if (CollectionUtils.isEmpty(delList)) {
            return;
        }
        //查询子表的formId
        List<Long> childFormIds = this.selectFormIdsByMainFormId(delList);
        if (CollectionUtils.isNotEmpty(childFormIds)) {
            delList.addAll(childFormIds);
        }
        //批量删除field表信息
        customizeVfieldRepository.deleteByFormIdBatch(delList);
        //批量删除form表信息
        cscpCustomizeVformRepository.deleteByIds(delList);
        //批量删除菜单及角色表信息
        this.deleteMenuBatch(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deleteById(Long formId) {
        if (formId == null) {
            return AjaxResult.error(400, "formId不存在");
        }
        List<Long> formIdList = new ArrayList<>();
        formIdList.add(formId);
        //查询子表的formId
        CscpCustomizeVform cscpCustomizeVform = queryCscpCustomizeVform(formId);

        //根据表名查询是否有数据
//        String formTable = cscpCustomizeVform.getFormTable();
//        int recordTotal = informationSchemaMysqlRepository.getRecordTotal(formTable);

        if (StringUtils.equals(cscpCustomizeVform.getDelFlag(), "1")) {
            return AjaxResult.error(400, "表单已使用，不能删除");
        }
        List<Long> childFormIds = this.selectFormIdsByMainFormId(formIdList);
        if (CollectionUtils.isNotEmpty(childFormIds)) {
            formIdList.addAll(childFormIds);
        }
        //批量删除field表信息
        customizeVfieldRepository.deleteByFormIdBatch(formIdList);
        //批量删除form表信息
        cscpCustomizeVformRepository.deleteByIds(formIdList);
        //批量删除菜单及角色表信息
        this.deleteMenuBatch(formIdList);
        return AjaxResult.success("删除成功");
    }

    @Override
    public List<Long> selectFormIdsByMainFormId(List<Long> mainFormIds) {

        List<Long> mainFormIdList = cscpCustomizeVformRepository.selectFormIdsByMainFormIdList(mainFormIds);
        return mainFormIdList == null ? new ArrayList<>() : mainFormIdList;
    }

    @Override
    public CscpCustomizeVform queryCscpCustomizeVform(Long formId) {
        // 获取表单信息
        CscpCustomizeVform cscpCustomizeVform = cscpCustomizeVformRepository.selectByPrimaryKey(formId);
        // 获取字段信息
        List<CscpCustomizeVfield> listByFormId = customizeVfieldRepository.getListByFormId(formId);
        cscpCustomizeVform.setVfieldList(listByFormId);
        return cscpCustomizeVform;
    }

    @Override
    public List<CscpCustomizeVform> queryChildCscpCustomizeVforms(Long[] formIds) {
        return cscpCustomizeVformRepository.getCscpCustomizeVformsByMainFormIds(Arrays.asList(formIds));
    }

    @Override
    public CscpCustomizeVform getMainCscpCustomizeVform(Long formId) {
        return cscpCustomizeVformRepository.getMainCscpCustomizeVform(formId);
    }

    @Override
    public PageResult<CscpCustomizeVform> getCscpCustomizeVformsPageList(CscpCustomizeVform form, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        //设置排序字段
        form.setSort(StringUtils.isEmpty(form.getSort()) ? "form_id" : form.getSort() + ", form_id");
        List<CscpCustomizeVform> cscpCustomizeVformsList = cscpCustomizeVformRepository.getCscpCustomizeVformsList(form);
        PageInfo<CscpCustomizeVform> cscpCustomizeVformPageInfo = new PageInfo<CscpCustomizeVform>(cscpCustomizeVformsList);
        PageResult<CscpCustomizeVform> pageResult = new PageResult<>();
        pageResult.setData(cscpCustomizeVformPageInfo.getList());
        pageResult.setRecordsTotal(cscpCustomizeVformPageInfo.getTotal());
        return pageResult;
    }


    @Override
    public List<CscpCustomizeVform> getCscpCustomizeVformsListAll() {
        CscpCustomizeVform form = new CscpCustomizeVform();
        form.setSort(StringUtils.isEmpty(form.getSort()) ? "form_id" : form.getSort() + ", form_id");
        form.setFormType("3");
        List<CscpCustomizeVform> cscpCustomizeVformsList = cscpCustomizeVformRepository.getCscpCustomizeVformsList(form);
        return cscpCustomizeVformsList;
    }

    @Override
    public CscpCustomizeVform getCscpCustomizeVformsByFormName(String formName) {
        CscpCustomizeVform form = new CscpCustomizeVform();
        form.setFormName(formName);
        return cscpCustomizeVformRepository.getCscpCustomizeVformsListOne(form);
    }

    void deleteMenu(Long formId) {
        CscpMenus menuByUrl = cscpMenusService.getMenuByUrl("/customize/" + formId);
        if (menuByUrl != null) {
            cscpMenusService.delete(menuByUrl.getId());
        }
    }

    /**
     * 批量删除菜单
     *
     * @param formIds formId列表
     * @author wbb
     */
    void deleteMenuBatch(List<Long> formIds) {
        if (CollectionUtils.isEmpty(formIds)) {
            return;
        }
        List<String> urlList = new ArrayList<>();
        //设置角色查询参数
        for (Long fd : formIds) {
            urlList.add("/customize/" + fd);
        }
        //批量查询 菜单角色信息
        List<CscpMenus> cscpMenusList = cscpMenusService.getMenuByUrlBatch(urlList);
        if (CollectionUtils.isEmpty(cscpMenusList)) {
            return;
        }
        //菜单Id 列表获取
        List<Long> idList = cscpMenusList.stream().map(CscpMenus::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        //根据菜单列表删除菜单及角色关系
        cscpMenusService.deleteByIdBatch(idList);
    }

    @Override
    public List<CscpCustomizeVform> selectByFormType(String formType) {
        List<CscpCustomizeVform> cscpCustomizeVform = cscpCustomizeVformRepository.selectByFormType(formType);
        return cscpCustomizeVform;
    }

    @Override
    public void updateDelFlagByFormId(Long formId) {
        cscpCustomizeVformRepository.updateDelFlagByFormId(formId);
    }
}
