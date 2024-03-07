package com.ctsi.ssdc.api.center.controller;

import com.ctsi.ssdc.api.center.domain.IpBlacklist;
import com.ctsi.ssdc.api.center.service.IpBlacklistService;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.model.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @Description IP黑名单
 * @Author Len
 * @Date 2023/6/8 8:35
 */
@RestController
@RequestMapping("/api/ip/blacklist")
public class IpBlacklistController {

    @Autowired
    private IpBlacklistService ipBlacklistService;

    /**
     * 新增
     * @param ipBlacklist
     * @return
     */
    @PostMapping("/save")
    public R<Long> save(@RequestBody IpBlacklist ipBlacklist) {
        if (ipBlacklist == null || StringUtils.isEmpty(ipBlacklist.getIpAddress())) {
            return R.failed("操作失败");
        }
        Long id = ipBlacklistService.save(ipBlacklist);
        return id == null ? R.failed("操作失败") : R.ok(id);
    }

    /**
     * 删除
     * @param ipBlacklist
     * @return
     */
    @PostMapping("/deleteById")
    public R<String> deleteById(@RequestBody IpBlacklist ipBlacklist) {
        if (ipBlacklist == null || ipBlacklist.getId() == null) {
            return R.failed("操作失败");
        }
        ipBlacklist.setDelFlag(1);
        Integer count = ipBlacklistService.editById(ipBlacklist);
        return count > 0 ? R.ok("删除成功") : R.failed("操作失败");
    }

    /**
     * 详情
     * @param id
     * @return
     */
    @GetMapping("/selectById")
    public R<IpBlacklist> selectById(@RequestParam("id") Long id) {
        return R.ok(ipBlacklistService.selectById(id));
    }

    /**
     * 编辑
     * @param ipBlacklist
     * @return
     */
    @PostMapping("/updateById")
    public R<String> updateById(@RequestBody IpBlacklist ipBlacklist) {
        if (ipBlacklist == null || ipBlacklist.getId() == null) {
            return R.failed("失败");
        }
        Integer count = ipBlacklistService.editById(ipBlacklist);
        return count > 0 ? R.ok("成功") : R.failed("失败");
    }

    /**
     * 列表查询
     * @param ipBlacklist
     * @param page
     * @return
     */
    @GetMapping("/selectByPage")
    public PageResult<IpBlacklist> selectByPage(IpBlacklist ipBlacklist, Pageable page) {
        return ipBlacklistService.selectByPage(ipBlacklist, page);
    }

}
