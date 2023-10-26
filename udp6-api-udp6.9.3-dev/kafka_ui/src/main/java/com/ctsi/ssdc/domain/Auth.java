package com.ctsi.ssdc.domain;

import com.ctsi.ssdc.annocation.AutoId;
import com.ctsi.ssdc.util.LongtoStringSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-01-05 14:09
 **/
@Data
public class Auth {
    @AutoId(primaryKey = "id")
    @JsonSerialize(using = LongtoStringSerialize.class)
    Long id;
    Long kafkaSourceId;
    Integer addAuth;
    Integer updateAuth;
    Integer removeAuth;

}
