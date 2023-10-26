package com.ctsi.ssdc.dic.service;

import java.util.LinkedHashMap;
import java.util.List;

public interface SqlDicService {
    List<LinkedHashMap<String, Object>> select(String sql);
}
