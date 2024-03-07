package com.ctsi.ssdc.base.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctsi.ssdc.base.system.domain.SysDeptInfo;
import com.ctsi.ssdc.admin.domain.SystemDict;
import com.ctsi.ssdc.base.system.domain.bo.FrameInfoBo;
import com.ctsi.ssdc.base.system.mapper.SysDeptInfoMapper;
import com.ctsi.ssdc.admin.mapper.SystemDicMapper;
import com.ctsi.ssdc.base.system.service.SystemDicService;
import com.ctsi.ssdc.model.AjaxResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务实现类
 *
 * @author hx
 * @since 2023-05-18 15:00:17
 */
@Service
public class SystemDicServiceImpl extends ServiceImpl<SystemDicMapper, SystemDict> implements SystemDicService {

    @Resource
    private SystemDicMapper systemDicMapper;

    @Resource
    private SysDeptInfoMapper sysDeptInfoMapper;

    /**
     * 根据主键删除记录
     *
     * @return
     */
    @Override
    @Transactional
    public AjaxResult removeSystemDicByTableId(Long id) {
        // 删除主表信息
        this.removeById(id);
        return AjaxResult.success("删除成功！！");
    }

    /**
     * 根据条件分页查询
     *
     * @return
     */
    @Override
    public Page<SystemDict> queryPageByCondition(SystemDict systemDic, Page<SystemDict> page) {

        QueryWrapper<SystemDict> queryWrapper = new QueryWrapper<>();
        // 构造复杂查询
        return systemDicMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<SystemDict> dictList() {
        return systemDicMapper.selectList(null);
    }

    @Override
    public void insertSysDept(FrameInfoBo info) {
        LambdaQueryWrapper<SysDeptInfo> deptwapper = new LambdaQueryWrapper<>();
        deptwapper.eq(SysDeptInfo::getSystemId, info.getSystemId());
        List<SysDeptInfo> sysDeptInfoList1 = sysDeptInfoMapper.selectList(deptwapper);
        if (!sysDeptInfoList1.isEmpty()) {
            sysDeptInfoMapper.delete(deptwapper);
        }
        String[] deptList = info.getDeptId().split(",");
        for (int i = 0; i < deptList.length; i++) {
            SysDeptInfo sysDeptInfo = new SysDeptInfo();
            sysDeptInfo.setSystemId(info.getSystemId());
            sysDeptInfo.setDeptId(Long.valueOf(Long.valueOf(deptList[i])));
            sysDeptInfoMapper.insert(sysDeptInfo);
        }
    }

    @Override
    public List<Long> getsysDepts(Long systemId) {
        LambdaQueryWrapper<SysDeptInfo> fwapper = new LambdaQueryWrapper<>();
        fwapper.eq(SysDeptInfo::getSystemId, Long.valueOf(systemId));
        List<SysDeptInfo> frameDeptInfoList = sysDeptInfoMapper.selectList(fwapper);
        return frameDeptInfoList.stream().map(SysDeptInfo::getDeptId).collect(Collectors.toList());
    }


}
