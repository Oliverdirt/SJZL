package com.ctsi.ssdc.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//mybaits dao 搜索路径
@MapperScan(basePackages= {"com.ctsi.ssdc","com.ctsi.ssdc.admin.repository"},annotationClass=Mapper.class)
public class MybatisDataSource {

}
