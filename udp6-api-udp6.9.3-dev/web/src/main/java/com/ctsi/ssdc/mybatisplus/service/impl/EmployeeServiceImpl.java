package com.ctsi.ssdc.mybatisplus.service.impl;

import com.ctsi.ssdc.mybatisplus.domain.Employee;
import com.ctsi.ssdc.mybatisplus.mapper.EmployeeMapper;
import com.ctsi.ssdc.mybatisplus.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hx
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;


    @Override
    public List<Employee> testFindAll() {
        List<Employee> employees = employeeMapper.selectList(null);
        employees.forEach(employee -> System.out.println("user=" + employee));
        return employees;
    }

    @Override
    public int add(Employee employee) {
       return employeeMapper.insert(employee);
    }

    @Override
    public int edit(Employee employee) {
        return employeeMapper.updateById(employee);
    }
    @Override
    public int del(Long id) {
        return employeeMapper.deleteById(id);
    }

    @Override
    public List<Employee> selectByName(String name) {
        return employeeMapper.selectByName(name);
    }
}