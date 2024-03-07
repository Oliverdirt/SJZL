package com.ctsi.ssdc.base.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctsi.ssdc.base.system.domain.FileInfo;
import com.ctsi.ssdc.base.system.domain.FrameInfo;
import com.ctsi.ssdc.base.system.domain.bo.FrameInfoBo;
import com.ctsi.ssdc.base.system.domain.bo.FrameInfoUpdateBo;
import com.ctsi.ssdc.base.system.domain.vo.FrameInfoVo;
import com.ctsi.ssdc.base.system.mapper.FileInfoMapper;
import com.ctsi.ssdc.base.system.mapper.FrameInfoMapper;
import com.ctsi.ssdc.base.system.service.IFrameInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lxs
 * @since 2023-04-21
 */
@Service
@RequiredArgsConstructor
public class FrameInfoServiceImpl extends ServiceImpl<FrameInfoMapper, FrameInfo> implements IFrameInfoService {

    @Autowired
    private FrameInfoMapper frameInfoMapper;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Override
    public void insertFrameInfo(FrameInfoBo info) {

        FrameInfo frameInfo = BeanUtil.copyProperties(info, FrameInfo.class);
        frameInfo.setCreateTime(new Date());
        frameInfoMapper.insert(frameInfo);
    }

    @Override
    public FrameInfoVo getInfoByType(FrameInfoUpdateBo bo) {
        LambdaQueryWrapper<FrameInfo> wapper = new LambdaQueryWrapper<>();
        wapper.eq(FrameInfo::getFrameType, bo.getFrameType());
        FrameInfo frameInfo = frameInfoMapper.selectOne(wapper);

        FrameInfoVo vo = new FrameInfoVo();

        if (ObjectUtil.isAllNotEmpty(frameInfo)) {
            //登录背景图片文件id
            BigInteger backgroundUrl = frameInfo.getBackgroundUrlId();
            FileInfo backgroundFileInfo = fileInfoMapper.selectById(backgroundUrl);
            if (ObjectUtil.isAllNotEmpty(backgroundFileInfo))
                vo.setBackgroundUrl(backgroundFileInfo.getFileUrl());

            //LOGO_URL图标文件id
            BigInteger logoUrl = frameInfo.getLogoUrlId();
            FileInfo logoFileInfo = fileInfoMapper.selectById(logoUrl);
            if (ObjectUtil.isAllNotEmpty(logoFileInfo))
                vo.setLogoUrl(logoFileInfo.getFileUrl());

            vo.setTitle(frameInfo.getTitle());
            vo.setFrameType(frameInfo.getFrameType());

            vo.setName(frameInfo.getName());
            vo.setId(frameInfo.getId());
            if (frameInfo.getFrameType() == 0) {
                vo.setLogoUrlId(frameInfo.getLogoUrlId());
                vo.setBackgroundUrlId(frameInfo.getBackgroundUrlId());
            }
        }
        return vo;
    }


    @Override
    public boolean updateFrameInfo(FrameInfoBo info) {

        if (ObjectUtil.isAllEmpty(info.getId())) {
            return false;
        }

        FrameInfo frameInfo = BeanUtil.copyProperties(info, FrameInfo.class);

        frameInfoMapper.updateById(frameInfo);

        return true;
    }
}
