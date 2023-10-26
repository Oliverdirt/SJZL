package com.ctsi.ssdc.domain;

import com.alibaba.fastjson.JSONObject;
import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-01-05 14:08
 **/
@Data
public class KafkaSource {
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)
    private Long id;
    String name;
    String broker;

    JSONObject auth;
}
