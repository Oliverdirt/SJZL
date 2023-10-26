package com.ctsi.ssdc.admin.service;

import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.admin.domain.CscpUserPost;
import com.ctsi.ssdc.admin.domain.CscpUserPostExample;
import com.ctsi.ssdc.service.StrengthenBaseService;

import java.util.List;

/**
 * Service Interface for managing CscpUserPost.
 *
 * @author hx
 * @date 2022-08-29 10:56:22
 *
 */

public interface CscpUserPostService
	extends StrengthenBaseService<CscpUserPost,Long , CscpUserPostExample>{

	/**
	* 批量删除
	* @param ids
	*/
	void deleteByIds(Long[] ids);

	void deleteById(Long id);

    public String saveUserPost(Long userId, String posts);


	List<CscpUserPost> findByPostId(Long postId);
}
