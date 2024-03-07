package com.ctsi.ssdc.base.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctsi.ssdc.admin.domain.SystemDict;
import com.ctsi.ssdc.base.system.domain.bo.FrameInfoBo;
import com.ctsi.ssdc.base.system.service.SystemDicService;
import com.ctsi.ssdc.model.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * 系统字典
 *
 * @author hx
 * @since 2023-05-18 15:00:17
 */
@RestController
@RequestMapping("/api/systemDics")
public class SystemDicController {

    @Autowired
    SystemDicService systemDicService;

    /**
     * 新增/更新数据
     *
     * @param systemDic
     * @return
     */
    @PostMapping("/saveOrUpdateSystemDic")
    public R<String> saveOrUpdateSystemDic(@RequestBody SystemDict systemDic) {
        systemDicService.saveOrUpdate(systemDic);
        return R.ok("保存成功");
    }

    /**
     * 条件分页条件查询
     *
     * @param
     * @param page
     * @return
     */
    @PostMapping("/queryPageSystemDicByCondition")
    public R<Page<SystemDict>> queryPageSystemDicByCondition(@RequestBody SystemDict systemDic, @RequestBody Page<SystemDict> page) {

        Page<SystemDict> systemDicPage = systemDicService.queryPageByCondition(systemDic, page);

        return R.ok(systemDicPage);
    }

    /**
     * 根据主键id查询详情
     *
     * @param id
     * @return
     */
    @GetMapping("/querySystemDicByTableId/{id}")
    public R<SystemDict> querySystemDicByTableId(@PathVariable("id") Long id) {
        SystemDict systemDic = systemDicService.getById(id);
        return R.ok(systemDic);
    }

    /**
     * 根据主键id 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/removeSystemDicByTableId/{id}")
    public R<String> removeSystemDicByTableId(@PathVariable("id") Long id) {
        systemDicService.removeSystemDicByTableId(id);
        return R.ok("删除成功");
    }


    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/del/ids")
    public R<String> removeSystemDicByTableIds(Long[] ids) {

        // 判空
        if (ids.length == 0) {
            return R.failed("子表删除失败,列表为空！！");
        }

        // 清理主表信息
        boolean removeSystemDicFlag = systemDicService.removeByIds(Arrays.asList(ids));
        if (!removeSystemDicFlag) {
            return R.failed("主表删除失败");
        }
        return R.ok("删除成功");
    }

    /**
     * 查询系统信息
     *
     * @return
     */
    @GetMapping("/getSystemList")
    public R<List<SystemDict>> getSystemList() {
        return R.ok(systemDicService.dictList());
    }

    /**
     * 保存系统与机构关系
     *
     * @param bo
     * @return
     */
    @PostMapping("/insertSysDeptInfo")
    public R<String> insertSysDeptInfo(@RequestBody FrameInfoBo bo) {
        if (bo == null || bo.getSystemId() == null || StringUtils.isEmpty(bo.getDeptId())) {
            return R.failed("操作失败");
        }
        systemDicService.insertSysDept(bo);
        return R.ok("操作成功");
    }

    /**
     * 查询 关联机构
     * @param systemId
     * @return
     */
    @GetMapping("/getdeptsBySystemId")
    public R<List<Long>> getdeptsBySystemId(@RequestParam(value = "systemId", required = false) Long systemId) {
        return R.ok(systemDicService.getsysDepts (systemId));
    }

}

