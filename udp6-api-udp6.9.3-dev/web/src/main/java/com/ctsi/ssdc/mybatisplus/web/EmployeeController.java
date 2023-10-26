package com.ctsi.ssdc.mybatisplus.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctsi.ssdc.model.AjaxResult;
import com.ctsi.ssdc.mybatisplus.mapper.EmployeeMapper;
import com.ctsi.ssdc.mybatisplus.domain.Employee;
import com.ctsi.ssdc.mybatisplus.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 员工控制器
 *
 * @author hx
 * @date 2022/11/21
 */
@RestController
@RequestMapping("/api/test/mybatisplus/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Resource
    EmployeeMapper employeeMapper;

    /**
     * 得到列表
     *
     * @return {@link AjaxResult}
     */
    @GetMapping("/getList")
    public AjaxResult getList(){
        List<Employee> employees = employeeService.testFindAll();
        return AjaxResult.success("人员列表",employees);
    }

    /**
     * 分页查询
     * @param current
     * @param size
     * @return
     */
    @GetMapping("/getListByPage")
    public AjaxResult getListByPage( @RequestParam("current") long current,@RequestParam("size") long size){

        Page<Employee> employeePage = new Page<>(current,size,false);
        Page<Employee> selectPage = employeeMapper.selectPage(employeePage,new QueryWrapper<>());

        List<Employee> records = selectPage.getRecords();
        return AjaxResult.success("人员列表",records);
    }

    /**
     * 添加
     *
     * @param employee 员工
     * @return {@link AjaxResult}
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody Employee employee){
        return toAjax(employeeService.add(employee));
    }

    /**
     * 删除
     *
     * @param id id
     * @return {@link AjaxResult}
     */
    @DeleteMapping("/{id}")
    public AjaxResult del(@PathVariable  Long id){
        return toAjax(employeeService.del(id));
    }

    @GetMapping("/selectByName")
    public AjaxResult selectByName(String name){
        return AjaxResult.success(employeeService.selectByName(name));
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

}
