package com.ctsi.ssdc.admin.service.impl;

import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;
import com.ctsi.ssdc.admin.domain.CscpUserPost;
import com.ctsi.ssdc.admin.domain.CscpUserPostExample;
import com.ctsi.ssdc.admin.service.CscpUserPostService;
import com.ctsi.ssdc.admin.repository.CscpUserPostRepository;

import org.apache.commons.lang3.StringUtils;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Arrays;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Service Implementation for managing CscpUserPost.
 *
 * @author hx
 * @date 2022-08-29 10:56:22
 *
 */

@Service
public class CscpUserPostServiceImpl
	extends StrengthenBaseServiceImpl<CscpUserPostRepository, CscpUserPost, Long, CscpUserPostExample>
	implements CscpUserPostService {

    @Resource
    private CscpUserPostRepository cscpUserPostRepository;


    /**
     * 批量删除
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Long[] ids) {
        List<Long> delList = new ArrayList<>(Arrays.asList(ids));
        // 批量删除
        cscpUserPostRepository.deleteByIds(delList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        cscpUserPostRepository.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public String saveUserPost(Long userId, String posts) {
        try {
            String[] ms = posts.split(",");
            CscpUserPostExample example = new CscpUserPostExample();
            example.or().andUserIdEqualTo(userId);
            cscpUserPostRepository.deleteByExample(example);
            for (int i = 0; i < ms.length; i++){
                CscpUserPost record = new CscpUserPost();
                record.setUserId(userId);
                record.setPostId(Long.parseLong(ms[i]));
                cscpUserPostRepository.insert(record);
            }
            return "true";
        }catch (Exception e){
            return "false";
        }
    }

    @Override
    public List<CscpUserPost> findByPostId(Long postId) {
        return cscpUserPostRepository.selectPostId(postId);
    }


}
