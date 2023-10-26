package com.ctsi.ssdc.mybatisplus.service;

import com.ctsi.ssdc.mybatisplus.domain.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> testFindAll();
    int add(Employee employee);
    int edit(Employee employee);
    int del(Long id);
    List<Employee> selectByName(String name);
}
