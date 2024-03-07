package com.ctsi.ssdc.base.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ctsi.ssdc.base.system.domain.FrameInfo;
import com.ctsi.ssdc.base.system.domain.bo.FrameInfoBo;
import com.ctsi.ssdc.base.system.domain.bo.FrameInfoUpdateBo;
import com.ctsi.ssdc.base.system.domain.vo.FrameInfoVo;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lxs
 * @since 2023-04-21
 */
public interface IFrameInfoService extends IService<FrameInfo> {
    /**
     * 添加框架管理记录
     * @param info
     */
    void insertFrameInfo(FrameInfoBo info);

    /**
     *  根据类型获取框架数据
     * @param bo
     * @return
     */
    FrameInfoVo getInfoByType(FrameInfoUpdateBo bo);

    /**
     * 修改框架
     * @param info
     */
    boolean updateFrameInfo(FrameInfoBo info);

}
