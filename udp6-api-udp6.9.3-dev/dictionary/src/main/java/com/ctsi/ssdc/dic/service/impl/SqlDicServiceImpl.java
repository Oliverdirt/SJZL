package com.ctsi.ssdc.dic.service.impl;

import com.ctsi.ssdc.dic.repository.SqlDicMapper;
import com.ctsi.ssdc.dic.service.SqlDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-02-09 18:15
 **/
@Service
public class SqlDicServiceImpl implements SqlDicService {

    @Autowired
    SqlDicMapper sqlDicMapper;

    @Override
    public List<LinkedHashMap<String, Object>> select(String sql) {
        return sqlDicMapper.select(sql);
    }
}
