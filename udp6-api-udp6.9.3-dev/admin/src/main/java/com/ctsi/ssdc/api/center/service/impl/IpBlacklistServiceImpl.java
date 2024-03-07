package com.ctsi.ssdc.api.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ctsi.ssdc.api.center.domain.IpBlacklist;
import com.ctsi.ssdc.api.center.mapper.IpBlacklistDao;
import com.ctsi.ssdc.api.center.service.IpBlacklistService;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.security.SecurityHxUtils;
import com.ctsi.ssdc.util.NetUtils;
import com.ctsi.ssdc.utils.RedisUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description IP黑名单
 * @Author Len
 * @Date 2023/6/8 8:35
 */
@Service
@Transactional
public class IpBlacklistServiceImpl implements IpBlacklistService {

    @Autowired
    private IpBlacklistDao ipBlacklistDao;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Long save(IpBlacklist ipBlacklist) {
        if (NetUtils.isValidIp(ipBlacklist.getIpAddress())) {
            ipBlacklist.setCreateId(SecurityHxUtils.getCurrentUserId());
            ipBlacklist.setCreateTime(LocalDateTime.now());
            ipBlacklist.setUpdateId(ipBlacklist.getCreateId());
            ipBlacklist.setUpdateTime(ipBlacklist.getCreateTime());
            int count = ipBlacklistDao.insert(ipBlacklist);
            //如果新增成功，则更新redis缓存 ipBlacklist
            addRedisCache(count);
        }
        return ipBlacklist.getId();
    }

    @Override
    public Integer editById(IpBlacklist ipBlacklist) {
        int count = 0;

        if (!StringUtils.isEmpty(ipBlacklist.getIpAddress()) && !NetUtils.isValidIp(ipBlacklist.getIpAddress())) {
            return count;
        }

        IpBlacklist blacklist = ipBlacklistDao.selectById(ipBlacklist.getId());
        if (!StringUtils.isEmpty(ipBlacklist.getIpAddress())) {
            blacklist.setIpAddress(ipBlacklist.getIpAddress());
        }

        if (!StringUtils.isEmpty(ipBlacklist.getComments())) {
            blacklist.setComments(ipBlacklist.getComments());
        }

        if (ipBlacklist.getDelFlag() != null) {
            blacklist.setDelFlag(ipBlacklist.getDelFlag());
        }

        blacklist.setUpdateId(SecurityHxUtils.getCurrentUserId());
        blacklist.setUpdateTime(LocalDateTime.now());
        count = ipBlacklistDao.updateById(blacklist);

        //如果修改成功，则更新redis缓存 ipBlacklist
        addRedisCache(count);

        return count;
    }

    private void addRedisCache(int count) {
        if (count > 0) {
            //先移除
            redisUtil.remove("ipBlacklist");
            //再添加
            QueryWrapper<IpBlacklist> wrapper = new QueryWrapper<>();
            wrapper.eq("DEL_FLAG", 0);
            List<IpBlacklist> result = ipBlacklistDao.selectList(wrapper);
            if (!result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    redisUtil.add("ipBlacklist", result.get(i).getIpAddress());
                }
            }
        }
    }

    @Override
    public IpBlacklist selectById(Long id) {
        return ipBlacklistDao.selectById(id);
    }

    @Override
    public PageResult<IpBlacklist> selectByPage(IpBlacklist ipBlacklist, Pageable page) {
        //开启分页
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());

        QueryWrapper<IpBlacklist> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(ipBlacklist.getIpAddress())) {
            wrapper.like("IP_ADDRESS", ipBlacklist.getIpAddress());
        }

        if (!StringUtils.isEmpty(ipBlacklist.getComments())) {
            wrapper.like("COMMENTS", ipBlacklist.getComments());
        }

        wrapper.eq("DEL_FLAG", 0);
        wrapper.orderByDesc("UPDATE_TIME");
        List<IpBlacklist> result = ipBlacklistDao.selectList(wrapper);
        PageInfo<IpBlacklist> info = new PageInfo<>(result);

        return new PageResult<>(result, info.getTotal(), info.getTotal());
    }
}
