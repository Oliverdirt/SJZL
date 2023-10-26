package com.ctsi.ssdc.admin.service.impl;


import com.ctsi.ssdc.admin.domain.*;
import com.ctsi.ssdc.admin.domain.CscpOrgExample.Criteria;
import com.ctsi.ssdc.admin.domain.dto.*;
import com.ctsi.ssdc.admin.repository.*;
import com.ctsi.ssdc.admin.service.CscpOrgService;
import com.ctsi.ssdc.admin.service.CscpUserOrgService;
import com.ctsi.ssdc.admin.service.CscpWorkGroupOrgService;
import com.ctsi.ssdc.admin.service.mapper.*;
import com.ctsi.ssdc.model.PageResult;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CscpOrg.
 *
 * @author ctsi biyi generator
 */
@Service
public class CscpOrgServiceImpl implements CscpOrgService {

    private final Logger log = LoggerFactory.getLogger(CscpOrgServiceImpl.class);

    @Autowired
    private CscpOrgRepository cscpOrgRepository;

    private final CscpOrgMapper cscpOrgMapper;

    @Autowired
    private CscpUserOrgRepository cscpUserOrgRepository;

    private final CscpUserOrgMapper cscpUserOrgMapper;

    @Autowired
    private CscpWorkGroupOrgRepository cscpWorkGroupOrgRepository;

    private final CscpWorkGroupOrgMapper cscpWorkGroupOrgMapper;

    @Autowired
    private CscpUserDetailRepository cscpUserDetailRepository;

    private final CscpUserDetailMapper cscpUserDetailMapper;

    @Autowired
    private CscpWorkGroupRepository cscpWorkGroupRepository;

    private final CscpWorkGroupMapper cscpWorkGroupMapper;

    private CscpUserOrgService cscpUserOrgService;
    @Autowired
    private CscpWorkGroupOrgService cscpWorkGroupOrgService;


    public CscpOrgServiceImpl(CscpOrgMapper cscpOrgMapper, CscpUserOrgMapper cscpUserOrgMapper,
                              CscpWorkGroupOrgMapper cscpWorkGroupOrgMapper, CscpUserDetailMapper cscpUserDetailMapper,
                              CscpWorkGroupMapper cscpWorkGroupMapper) {
        this.cscpOrgMapper = cscpOrgMapper;
        this.cscpUserOrgMapper = cscpUserOrgMapper;
        this.cscpWorkGroupOrgMapper = cscpWorkGroupOrgMapper;
        this.cscpUserDetailMapper = cscpUserDetailMapper;
        this.cscpWorkGroupMapper = cscpWorkGroupMapper;
    }

    /**
     * insert a cscpOrg.
     *
     * @param cscpOrgDTO the entity to insert
     * @return the persisted entity
     */
    @Override
    public CscpOrgDTO insert(CscpOrgDTO cscpOrgDTO) {
        log.debug("Request to insert CscpOrg : {}", cscpOrgDTO);

        CscpOrg cscpOrg = cscpOrgMapper.toEntity(cscpOrgDTO);
        cscpOrgRepository.insert(cscpOrg);
        return cscpOrgMapper.toDto(cscpOrg);
    }

    /**
     * update a cscpOrg.
     *
     * @param cscpOrgDTO the entity to update
     * @return the persisted entity
     */
    @Override
    public CscpOrgDTO update(CscpOrgDTO cscpOrgDTO) {
        log.debug("Request to update CscpOrg : {}", cscpOrgDTO);

        CscpOrg cscpOrg = cscpOrgMapper.toEntity(cscpOrgDTO);
        cscpOrgRepository.updateByPrimaryKeySelective(cscpOrg);
        return cscpOrgMapper.toDto(cscpOrg);
    }

    /**
     * Get all the cscpOrgs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpOrgDTO> findAll() {
        log.debug("Request to get all CscpOrgs");

        List<CscpOrgDTO> data = cscpOrgRepository.selectByExample(null).stream().map(cscpOrgMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));

        long count = 0L;

        if (CollectionUtils.isNotEmpty(data)) {
            count = cscpOrgRepository.countByExample(null);
        }

        return new PageResult<CscpOrgDTO>(data, count, count);

    }

    /**
     * Get one cscpOrg.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CscpOrgDTO findOne(Long id) {
        log.debug("Request to get CscpOrg : {} ", id);

        CscpOrg cscpOrg = cscpOrgRepository.selectByPrimaryKey(id);
        return cscpOrgMapper.toDto(cscpOrg);
    }

    /**
     * Delete the cscpOrg .
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CscpOrg : {} ", id);

        cscpOrgRepository.deleteByPrimaryKey(id);
    }

    private String getPageOrderBy(Pageable page) {

        if (page != null && page.getSort() != null) {

            StringBuilder sb = new StringBuilder();

            page.getSort()
                    .forEach(sort -> sb.append(sort.getProperty()).append(" ").append(sort.getDirection()).append(","));

            if (sb.length() > 1) {
                return (sb.substring(0, sb.length() - 1));
            }
        }

        return null;
    }

    /**
     * Get the cscpOrgs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<CscpOrgDTO> findByCscpOrgDTO(CscpOrgDTO cscpOrgDTO, Pageable page) {

        log.debug("Request to find CscpOrgs");

        CscpOrgExample example = new CscpOrgExample();

        example.setPage(page);

        Criteria critieria = example.createCriteria();

        if (cscpOrgDTO.getId() != null) {
            critieria.andIdEqualTo(cscpOrgDTO.getId());
        }
        if (cscpOrgDTO.getParentId() != null) {
            critieria.andParentIdEqualTo(cscpOrgDTO.getParentId());
        }

        if (StringUtils.isNotBlank(cscpOrgDTO.getOrgName())) {
            critieria.andOrgNameLike(String.format("%%%s%%", cscpOrgDTO.getOrgName()));
        }
        if (StringUtils.isNotBlank(cscpOrgDTO.getDescription())) {
            critieria.andDescriptionLike(String.format("%%%s%%", cscpOrgDTO.getDescription()));
        }

        String orderBy = getPageOrderBy(page);

        if (StringUtils.isNotEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<CscpOrgDTO> data = cscpOrgMapper.toDto(cscpOrgRepository.selectByExamplewithPage(example));

        long count = 0L;

        if (CollectionUtils.isNotEmpty(data)) {
            count = cscpOrgRepository.countByExample(example);
        }

        return new PageResult<CscpOrgDTO>(data, count, count);

    }

    /**
     * Get the cscpOrgs.
     *
     * @param cscpOrgCriteria
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public PageResult<CscpOrgDTO> findByCscpOrgCriteria(CscpOrgCriteria cscpOrgCriteria, Pageable page) {

        CscpOrgExample example = new CscpOrgExample();

        example.setPage(page);

        Criteria criteria = example.createCriteria();

        if (cscpOrgCriteria != null) {
            cscpOrgCriteria.buildCriteria(criteria);
        }

        String orderBy = getPageOrderBy(page);

        if (StringUtils.isNotEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<CscpOrgDTO> data = cscpOrgMapper.toDto(cscpOrgRepository.selectByExamplewithPage(example));

        long count = 0L;

        if (CollectionUtils.isNotEmpty(data)) {
            count = cscpOrgRepository.countByExample(example);
        }

        return new PageResult<CscpOrgDTO>(data, count, count);

    }

    ///重写save方法，单条数据增加以及修改
    @Override
    @Transactional
    public Long save(CscpOrgParamDTO cscpOrgParamDTO) {
        //增加cscpOrg机构相关信息
        CscpOrgDTO[] cscpOrgDTO = cscpOrgParamDTO.getcscpOrgDtos();
        Long cscpOrgId = cscpOrgDTO[0].getId();
        CscpOrgDTO cscpOrg = null;
        //获取orderby顺序
        Integer orderby = getOrgOrderBy(cscpOrgDTO[0]);
        //设置orderby
        cscpOrgDTO[0].setOrderby(orderby);
        if (findOne(cscpOrgId) == null) {
            cscpOrgId = insert(cscpOrgDTO[0]).getId();
        } else {
            cscpOrgId = update(cscpOrgDTO[0]).getId();
        }
        saveUserOrgInfo(cscpOrgParamDTO, cscpOrgId);
        saveOrgWorkGroupInfo(cscpOrgParamDTO, cscpOrgId);

        return cscpOrgId;
    }

    //获取orderby顺序
    @Override
    public Integer getOrgOrderBy(CscpOrgDTO cscpOrgDTO) {
        Long parentId = cscpOrgDTO.getParentId();
        Integer orderby;
        CscpOrgExample org = new CscpOrgExample();
        CscpOrgExample.Criteria orgCriteria = org.createCriteria();
        orgCriteria.andParentIdEqualTo(parentId);
        org.setOrderByClause("orderby");
        if (cscpOrgRepository.selectByExample(org).isEmpty()) {
            orderby = 1;
        } else {
            List<CscpOrg> cscpOrg = cscpOrgRepository.selectByExample(org);
            //System.out.println(cscpOrg.get(0));
            orderby = cscpOrg.get(cscpOrg.size() - 1).getOrderby() + 1;
        }
        return orderby;
    }

    //增加用户机构信息
    public void saveUserOrgInfo(CscpOrgParamDTO cscpOrgParamDTO, Long cscpOrgId) {
        //1.首先删除数据库中所有 org_id = cscpOrgId 的数据
        CscpUserOrgExample userOrg = new CscpUserOrgExample();
        CscpUserOrgExample.Criteria userOrgSelectCriteria = userOrg.createCriteria();
        userOrgSelectCriteria.andOrgIdEqualTo(cscpOrgId);
        if (cscpUserOrgRepository.selectByExample(userOrg).size() > 0) {
            cscpUserOrgRepository.deleteByExample(userOrg);
        }
        //2.增加cscpUserOrgDtos机构用户信息 （可能存在有多条数据）
        CscpUserOrgDTO[] cscpUserOrgDtos = cscpOrgParamDTO.getcscpUserOrgDtos();
        int cscpUserOrgDtosLength = cscpUserOrgDtos.length;
        if (cscpUserOrgDtosLength > 0) {
            for (int i = 0; i < cscpUserOrgDtosLength; i++) {
                Long cscpUserOrgIid = cscpUserOrgDtos[i].getId();
                CscpUserOrg cscpUserOrg = cscpUserOrgMapper.toEntity(cscpUserOrgDtos[i]);
                cscpUserOrg.setOrgId(cscpOrgId);
                cscpUserOrgRepository.insert(cscpUserOrg);
            }
        }
    }

    //增加机构工作组信息
    public void saveOrgWorkGroupInfo(CscpOrgParamDTO cscpOrgParamDTO, Long cscpOrgId) {
        //1.删除所有org_workgroup中org_id = cscpOrgId 的数据
        CscpWorkGroupOrgExample workGroupOrgExample = new CscpWorkGroupOrgExample();
        CscpWorkGroupOrgExample.Criteria workGroupCrieria = workGroupOrgExample.createCriteria();
        workGroupCrieria.andOrgIdEqualTo(cscpOrgId);
        if (cscpWorkGroupOrgRepository.selectByExample(workGroupOrgExample).size() > 0) {
            cscpWorkGroupOrgRepository.deleteByExample(workGroupOrgExample);
        }
        //机构工作组关联数据增加 （可能存在有多条数据）
        CscpWorkGroupOrgDTO[] cscpWorkGroupOrgDtos = cscpOrgParamDTO.getcscpWorkGroupOrgDtos();
        int cscpWorkGroupOrgDtosLength = cscpWorkGroupOrgDtos.length;
        if (cscpWorkGroupOrgDtosLength > 0) {
            for (int i = 0; i < cscpWorkGroupOrgDtosLength; i++) {
                Long cscpWorkGroupOrgLd = cscpWorkGroupOrgDtos[i].getId();
                CscpWorkGroupOrg inWorkGroupOrg = cscpWorkGroupOrgMapper.toEntity(cscpWorkGroupOrgDtos[i]);
                inWorkGroupOrg.setOrgId(cscpOrgId);
                cscpWorkGroupOrgRepository.insert(inWorkGroupOrg);
            }
        }

    }


    //用来存储传入节点的所有子孙节点的id
    List<Long> nodesId = new ArrayList<>();

    //新增删除接口
    @Override
    @Transactional
    public void deleteAll(CscpOrgParamDTO cscpOrgParamDTO) {
        //删除cscpOrg机构相关信息
        CscpOrgDTO[] cscpOrgDTO = cscpOrgParamDTO.getcscpOrgDtos();
        Long cscpOrgId = cscpOrgDTO[0].getId();
        nodesId.add(cscpOrgId);
        //递归删除所有子孙节点
        deleteOrgsData(cscpOrgId);

        //删除cscpUserOrgDtos机构用户信息和机构工作组关联数据
        CscpUserOrgExample userOrgExample = new CscpUserOrgExample();
        CscpUserOrgExample.Criteria userOrgCriteria = userOrgExample.createCriteria();
        userOrgCriteria.andOrgIdIn(nodesId);  //增加多个条件
        cscpUserOrgRepository.deleteByExample(userOrgExample);

        CscpWorkGroupOrgExample workGroupOrgExample = new CscpWorkGroupOrgExample();
        CscpWorkGroupOrgExample.Criteria workGroupCrieria = workGroupOrgExample.createCriteria();
        workGroupCrieria.andOrgIdIn(nodesId);
        cscpWorkGroupOrgRepository.deleteByExample(workGroupOrgExample);
    }

    //递归删除所有子孙节点
    public void deleteOrgsData(Long cscpOrgId) {
        CscpOrgExample orgExample = new CscpOrgExample();
        Criteria criteria = orgExample.createCriteria();
        criteria.andParentIdEqualTo(cscpOrgId);
        List<CscpOrg> cscpOrgs = cscpOrgRepository.selectByExample(orgExample);
        if (cscpOrgs.size() > 0) {
            for (CscpOrg cscporg : cscpOrgs) {
                Long id = cscporg.getId();
                nodesId.add(id);
                deleteOrgsData(id);
            }
        }
        delete(cscpOrgId);
    }

    //异步加载    把所有的组织机构和相关的工作组、人员查出来
    @Override
    public CscpOrgParamDTO fetchCscpOrgsUpdate(Long parentId) {
        CscpOrgExample example = new CscpOrgExample();
        Criteria criteria = example.createCriteria();
        if (parentId == null) {
            criteria.andParentIdEqualTo(0L);
        } else {
            criteria.andParentIdEqualTo(parentId);
        }
        example.setOrderByClause("orderby");
        //组织
        List<CscpOrg> orgs = cscpOrgRepository.selectByExample(example);

        //list筛选
        //用户组织
        CscpUserOrgExample example2 = new CscpUserOrgExample();
        List<CscpUserOrg> userOrgs = cscpUserOrgRepository.selectByExample(example2);

        //工作组和组织
        CscpWorkGroupOrgExample example3 = new CscpWorkGroupOrgExample();
        List<CscpWorkGroupOrg> workGroupOrgs = cscpWorkGroupOrgRepository.selectByExample(example3);

        List<Long> orgIdList = new ArrayList<>();
        for (CscpOrg org : orgs) {
            orgIdList.add(org.getId());
        }
        //lamda表达式筛选user_orgs.org_id = orgs.id and orgs.parent_id = parent_id
        List<CscpUserOrg> user = null;
        user = userOrgs.stream()
                .filter((CscpUserOrg userOrg) -> orgIdList.contains(userOrg.getOrgId()))
                .collect(Collectors.toList());
        //lamda表达式筛选 workGroup_orgs.org_id = orgs.id and orgs.parent_id = parent_id
        List<CscpWorkGroupOrg> workGroup = null;
        workGroup = workGroupOrgs.stream()
                .filter((CscpWorkGroupOrg workGroupOrg) -> orgIdList.contains(workGroupOrg.getOrgId()))
                .collect(Collectors.toList());
        CscpOrgParamDTO paramDto = new CscpOrgParamDTO();
        paramDto.setcscpOrgDtos(cscpOrgMapper.toDto(orgs).toArray(new CscpOrgDTO[]{}));
        paramDto.setcscpUserOrgDtos(cscpUserOrgMapper.toDto(user).toArray(new CscpUserOrgDTO[]{}));
        paramDto.setcscpWorkGroupOrgDtos(
                cscpWorkGroupOrgMapper.toDto(workGroup).toArray(new CscpWorkGroupOrgDTO[]{}));
        return paramDto;
    }


}
