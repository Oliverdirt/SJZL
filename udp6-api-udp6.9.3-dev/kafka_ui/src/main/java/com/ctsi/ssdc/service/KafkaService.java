package com.ctsi.ssdc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctsi.ssdc.repository.KafkaSourceDao;
import com.ctsi.ssdc.domain.Auth;
import com.ctsi.ssdc.domain.KafkaSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-01-05 14:07
 **/

@Service
@Slf4j
public class KafkaService {

    @Autowired
    KafkaSourceDao kafkaSourceDao;

    public List<KafkaSource> getAllSource() {
        return kafkaSourceDao.getAll();
    }

    public List<KafkaSource> getSourceByname(String name) {
        return kafkaSourceDao.getSourceByname(name);
    }

    public List<KafkaSource> getAllSourceAuth() {
        List<KafkaSource> list = kafkaSourceDao.getAll();
        list.stream().forEach(t -> {
            Auth auth = kafkaSourceDao.getAuthBySource(t.getId());
            JSONObject authO = new JSONObject();
            authO.put("add", auth.getAddAuth().intValue() == 1 ? true : false);
            authO.put("update", auth.getUpdateAuth().intValue() == 1 ? true : false);
            authO.put("remove", auth.getRemoveAuth().intValue() == 1 ? true : false);
            t.setAuth(authO);
        });
        return list;

    }

    @Transactional
    public void add(KafkaSource source) {
        kafkaSourceDao.insert(source);

        Auth auth = new Auth();
        auth.setKafkaSourceId(source.getId());
        auth.setAddAuth(0);
        auth.setRemoveAuth(0);
        auth.setUpdateAuth(0);
        kafkaSourceDao.addAuth(auth);



    }

    @Transactional
    public void deleteSource(Long id) {
        kafkaSourceDao.delete(id);
        kafkaSourceDao.deleteAuth(id);
    }

    @Transactional
    public void auth(String param) {
        JSONObject jo = JSON.parseObject(param);
        Set<String> keys = jo.keySet();
        keys.stream().forEach(key -> {
            JSONObject auth = jo.getJSONObject(key);
            int add = auth.getBoolean("add") ? 1 : 0;
            int update = auth.getBoolean("update") ? 1 : 0;
            int remove = auth.getBoolean("remove") ? 1 : 0;
            int i = kafkaSourceDao.updateAuth(Long.parseLong(key), add, update, remove);
        });

    }

    public String getBroker(Long sourceId) {
        return kafkaSourceDao.selectById(sourceId);
    }
}