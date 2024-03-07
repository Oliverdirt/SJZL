package com.ctsi.ssdc.api.center.controller;

import com.ctsi.ssdc.api.center.domain.ApiInfo;
import com.ctsi.ssdc.api.center.mapper.ApiInfoDao;
import com.ctsi.ssdc.api.center.service.ApiInfoService;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.model.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

/**
 * @Description API信息
 * @Author Len
 * @Date 2023/6/5 16:10
 */
@RestController
@RequestMapping("/api/center")
public class ApiInfoController {

    @Autowired
    private ApiInfoService apiInfoService;

    @Autowired
    private ApiInfoDao apiInfoDao;

    /**
     * 新增
     *
     * @param apiInfo
     * @return
     */
    @PostMapping("/save")
    public R<Long> save(@RequestBody ApiInfo apiInfo) {
        if (apiInfo == null) {
            return R.failed("操作失败");
        }
        Long id = apiInfoService.save(apiInfo);
        return id != null ? R.ok(id) : R.failed("操作失败");
    }

    /**
     * 列表查询
     *
     * @param apiInfo
     * @param page
     * @return
     */
    @GetMapping("/selectByPage")
    public PageResult<ApiInfo> selectByPage(ApiInfo apiInfo, Pageable page) {
        return apiInfoService.selectByPage(apiInfo, page);
    }

    /**
     * 修改状态
     *
     * @param apiInfo
     * @return
     */
    @PostMapping("/updateApiStatus")
    public R<String> updateApiStatus(@RequestBody ApiInfo apiInfo) {
        if (apiInfo == null || apiInfo.getApiStatus() == null || apiInfo.getId() == null) {
            return R.failed("操作失败");
        }

        ApiInfo info = apiInfoDao.selectById(apiInfo.getId());
        if (info != null && info.getConfirm() != 0) {
            return R.failed("API可用性未校验，请先校验");
        }
        Integer count = apiInfoService.editApiInfo(apiInfo);
        return count > 0 ? R.ok("操作成功") : R.failed("操作失败");
    }

    /**
     * 编辑
     *
     * @param apiInfo
     * @return
     */
    @PostMapping("/updateApiInfo")
    public R<String> updateApiInfo(@RequestBody ApiInfo apiInfo) {
        if (apiInfo == null || apiInfo.getId() == null) {
            return R.failed("操作失败");
        }
        Integer count = apiInfoService.editApiInfo(apiInfo);
        return count > 0 ? R.ok("操作成功") : R.failed("操作失败");
    }

    /**
     * 删除
     *
     * @param apiInfo
     * @return
     */
    @PostMapping("/deleteById")
    public R<String> deleteById(@RequestBody ApiInfo apiInfo) {
        if (apiInfo == null || apiInfo.getId() == null) {
            return R.failed("操作失败");
        }
        apiInfo.setDelFlag(1);
        Integer count = apiInfoService.editApiInfo(apiInfo);
        return count > 0 ? R.ok("操作成功") : R.failed("操作失败");
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @GetMapping("/selectDetail")
    public R<ApiInfo> selectDetail(@RequestParam("id") Long id) {
        ApiInfo result = apiInfoService.selectDetail(id);
        return R.ok(result);
    }

    /**
     * 调试
     *
     * @param id
     * @param requestParam
     * @return
     */
    @GetMapping("/test")
    public R<Object> test(@RequestParam("id") Long id,
                          @RequestParam(value = "requestParam", required = false) String requestParam) {
        return apiInfoService.test(id, requestParam);
    }

    //@Scheduled(cron = "0 0 */1 * * ?")
    public void autoCollection() {
        apiInfoService.autoCollection();
    }
}
