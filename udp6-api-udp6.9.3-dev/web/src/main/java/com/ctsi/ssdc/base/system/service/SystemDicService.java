package com.ctsi.ssdc.base.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ctsi.ssdc.admin.domain.SystemDict;
import com.ctsi.ssdc.base.system.domain.bo.FrameInfoBo;
import com.ctsi.ssdc.model.AjaxResult;

import java.util.List;

/**
 * 服务类
 *
 * @author hx
 * @since 2023-05-18 15:00:17
 */
public interface SystemDicService extends IService<SystemDict> {

    /**
     * @param systemDic
     * @param page
     * @return
     */
    Page<SystemDict> queryPageByCondition(SystemDict systemDic, Page<SystemDict> page);

    /**
     * @param id
     * @return
     */
    AjaxResult removeSystemDicByTableId(Long id);

    /**
     * 查询系统信息
     * @return
     */
    List<SystemDict> dictList();

    /**
     * 保存系统与机构关系
     *
     * @param info
     * @return
     */
    void insertSysDept(FrameInfoBo info);

    /**
     * 查询 关联机构
     * @param systemId
     * @return
     */
    List<Long>  getsysDepts(Long systemId);
}
