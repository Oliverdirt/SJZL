package com.ctsi.ssdc.dic.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * SQL字典查询mapper
 * <p>
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-02-09 18:08
 **/
@Mapper
public interface SqlDicMapper {


    @Select("${value}")
    List<LinkedHashMap<String, Object>> select(String sql);


}
