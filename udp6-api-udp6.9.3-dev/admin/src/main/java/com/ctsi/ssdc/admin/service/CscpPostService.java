package com.ctsi.ssdc.admin.service;



import com.ctsi.ssdc.admin.domain.dto.CscpPostCriteria;
import com.ctsi.ssdc.admin.domain.dto.CscpPostDTO;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.admin.domain.CscpPost;
import com.ctsi.ssdc.admin.domain.CscpPostExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

/**
 * Service Interface for managing CscpPost.
 *
 * @author ctsi-biyi-generator
 *
 */
public interface CscpPostService 
	extends StrengthenBaseService<CscpPost, Long, CscpPostExample>{


    /**
    * GET  /cscpPosts : get the cscpPosts firstStringBaseColumn.
    */
    PageResult<CscpPost> findFirstStringColumn(String postCode ,Pageable pageable);

    PageResult<CscpPost> getCscpPosts(CscpPost cscpPost, Pageable pageable);


    int checkCscpPostCode(CscpPost cscpPost);

    int checkCscpPostName(CscpPost cscpPost);

    PageResult<CscpPostDTO> findByCscpPostsCriteria(CscpPostCriteria cscpPostCriteria, Pageable page);

    CscpPostDTO insert(CscpPostDTO cscpPostDTO);

    CscpPostDTO update(CscpPostDTO cscpPostDTO);


}
