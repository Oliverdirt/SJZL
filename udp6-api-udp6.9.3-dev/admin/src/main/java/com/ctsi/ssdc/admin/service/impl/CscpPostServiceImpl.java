package com.ctsi.ssdc.admin.service.impl;

import com.ctsi.ssdc.admin.domain.CscpPostExample.Criteria;
import com.ctsi.ssdc.admin.domain.dto.CscpPostCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpPostDTO;
import com.ctsi.ssdc.admin.service.mapper.CscpPostMapper;
import com.ctsi.ssdc.admin.service.mapper.CscpRolesMapper;
import com.ctsi.ssdc.annotation.DataScope;
import com.ctsi.ssdc.utils.HxStringUtils;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;
import com.ctsi.ssdc.admin.domain.CscpPost;
import com.ctsi.ssdc.admin.domain.CscpPostExample;
import com.ctsi.ssdc.admin.service.CscpPostService;
import com.ctsi.ssdc.admin.repository.CscpPostRepository;
import org.apache.commons.lang3.StringUtils;
import java.util.List;
import java.util.Objects;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;
import com.github.pagehelper.PageHelper;

import javax.annotation.Resource;

/**
 * Service Implementation for managing CscpPost.
 *
 * @author ctsi-biyi-generator
 *
 */
@Service
public class CscpPostServiceImpl
	extends StrengthenBaseServiceImpl<CscpPostRepository, CscpPost, Long, CscpPostExample> 
	implements CscpPostService {

    private final Logger log = LoggerFactory.getLogger(CscpRolesServiceImpl.class);

    @Resource
    CscpPostRepository cscpPostRepository;

    private final CscpPostMapper cscpPostMapper;

    public CscpPostServiceImpl(CscpPostMapper cscpPostMapper) {
        this.cscpPostMapper = cscpPostMapper;
    }


    /**
    * GET  /cscpPosts : get the cscpPosts firstStringBaseColumn.
    */
    @Override
    public PageResult<CscpPost> findFirstStringColumn(String postCode,Pageable pageable){
        String str = postCode;
        if (Objects.nonNull(pageable)) {
            PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        }
        CscpPostExample cscpPostExample = new CscpPostExample();
        String orderBy = getPageOrderBy(pageable);
        if(StringUtils.isNotEmpty(orderBy)) {
            cscpPostExample.setOrderByClause(orderBy);
        }
        if (StringUtils.isEmpty(str)){
            cscpPostExample.createCriteria();
        } else{
            cscpPostExample.createCriteria().andPostCodeLike("%" + str +"%");
        }
        List<CscpPost>  data = cscpPostRepository.selectByExample(cscpPostExample);

        long count = 0L;
        if (CollectionUtils.isNotEmpty(data))
        {
            count = cscpPostRepository.countByExample(cscpPostExample);
        }
        return new PageResult<CscpPost>(data,count,count);
    }

    @Override
    //@DataScope(tenantAlias = "t",userAlias = "u",deptAlias = "d")
    public PageResult<CscpPost> getCscpPosts(CscpPost post, Pageable pageable) {
        if(Objects.nonNull(pageable)){
            PageHelper.startPage(pageable.getPageNumber()+1,pageable.getPageSize());
        }
        List<CscpPost> postList = cscpPostRepository.getCscpPosts(post);
        PageInfo<CscpPost> pageInfo = new PageInfo<CscpPost>(postList);
        PageResult<CscpPost> postResult = new PageResult<>();
        postResult.setData(pageInfo.getList());
        postResult.setRecordsTotal(pageInfo.getTotal());
        return postResult;
    }

    @Override
    public int checkCscpPostCode(CscpPost cscpPost) {
        if (cscpPost.getPostId() == null){
            return cscpPostRepository.checkCscpPostCode(cscpPost);
        }else {
            CscpPost cscpPost1 = cscpPostRepository.selectByPrimaryKey(cscpPost.getPostId());
            if (cscpPost1.getPostCode().equals(cscpPost.getPostCode())){
                return 0;
            }else {
                return cscpPostRepository.checkCscpPostCode(cscpPost);
            }
        }
    }

    @Override
    public int checkCscpPostName(CscpPost cscpPost) {
        if (cscpPost.getPostId() == null){
            return cscpPostRepository.checkCscpPostName(cscpPost);
        }else {
            CscpPost cscpPost1 = cscpPostRepository.selectByPrimaryKey(cscpPost.getPostId());
            if (cscpPost1.getPostName().equals(cscpPost.getPostName())){
                return 0;
            }else {
                return cscpPostRepository.checkCscpPostName(cscpPost);
            }
        }
    }

    @Override
    public PageResult<CscpPostDTO> findByCscpPostsCriteria(CscpPostCriteria cscpPostCriteria, Pageable page) {

        CscpPostExample example = new CscpPostExample();
        example.setPage(page);
        Criteria criteria = example.createCriteria();
        if (cscpPostCriteria != null){
            cscpPostCriteria.buildCriteria(criteria);
        }
        String orderBy = getPageOrderBy(page);
        if (HxStringUtils.isNotEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<CscpPostDTO> data = cscpPostMapper.toDto(cscpPostRepository.selectByExamplewithPage(example));
        long count = 0L;
        if (CollectionUtils.isNotEmpty(data)){
            count = cscpPostRepository.countByExample(example);
        }
        return new PageResult<CscpPostDTO>(data,count,count);
    }

    @Override
    public CscpPostDTO insert(CscpPostDTO cscpPostDTO) {
        log.debug("Request to insert CscpPost : {}", cscpPostDTO);
        CscpPost cscpPost = cscpPostMapper.toEntity(cscpPostDTO);
        cscpPostRepository.insert(cscpPost);
        return cscpPostMapper.toDto(cscpPost);
    }

    @Override
    public CscpPostDTO update(CscpPostDTO cscpPostDTO) {
        log.debug("Request to update CscpPost : {}", cscpPostDTO);
        CscpPost cscpPost = cscpPostMapper.toEntity(cscpPostDTO);
        cscpPostRepository.updateByPrimaryKeySelective(cscpPost);
        return cscpPostMapper.toDto(cscpPost);
    }


    private String getPageOrderBy(Pageable page) {
        if(page!= null && page.getSort() != null) {
            StringBuilder sb = new StringBuilder();
            page.getSort().forEach(sort -> sb.append(sort.getProperty())
            .append(" ").append(sort.getDirection()).append(","));
            if(sb.length() > 1) {
                return (sb.substring(0,sb.length()-1));
             }
        }
        return null;
    }
}
