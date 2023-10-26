package com.ctsi.ssdc.repository;

import com.ctsi.ssdc.domain.Auth;
import com.ctsi.ssdc.domain.KafkaSource;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-01-05 14:10
 **/

@Mapper
public interface KafkaSourceDao {

    @Select("SELECT * FROM kafka_source")
    @Results({@Result(property = "name", column = "kafka_source_name"),
            @Result(property = "broker", column = "kafka_source_broker"),
            @Result(property = "id", column = "id")})
    List<KafkaSource> getAll();

    @Select("SELECT * FROM kafka_source where kafka_source_name = #{name}")
    @Results({@Result(property = "name", column = "kafka_source_name"),
            @Result(property = "broker", column = "kafka_source_broker"),
            @Result(property = "id", column = "id")})
    List<KafkaSource> getSourceByname(String name);

    @Insert("insert into kafka_source (id, kafka_source_name, kafka_source_broker) values (#{id,jdbcType=BIGINT},#{name}, #{broker})")
    void insert(KafkaSource source);

    @Delete("delete from kafka_source where id = #{id}")
    void delete(Long id);

    @Delete("delete from kafka_auth where kafka_source_id = #{sourceId}")
    int deleteAuth(Long sourceId);

    @Insert("insert into kafka_auth (kafka_source_id,add_auth,update_auth,remove_auth) values(#{sourceId},#{add},#{update}, #{remove})")
    void insertAuth(Long sourceId, Integer add, Integer update, Integer remove);

    @Select("select kafka_source_id,add_auth,update_auth,remove_auth from kafka_auth where kafka_source_id = #{sourceId}")
    @Results({@Result(property = "kafkaSourceId", column = "kafka_source_id"),
            @Result(property = "addAuth", column = "add_auth"),
            @Result(property = "updateAuth", column = "update_auth"),
            @Result(property = "removeAuth", column = "remove_auth")
            })
    Auth getAuthBySource(Long sourceId);

    @Update({"update kafka_auth set add_auth=#{add}, update_auth=#{update}, remove_auth=#{remove} where kafka_source_id = #{id}"})
    int updateAuth(@Param("id")Long id, @Param("add")int add, @Param("update")int update, @Param("remove")int remove);

    @Select("SELECT kafka_source_broker FROM kafka_source where id=#{sourceId}")
    String selectById(Long sourceId);

    @Insert("insert into kafka_auth (id,kafka_source_id,add_auth,update_auth,remove_auth) values(#{id,jdbcType=BIGINT},#{kafkaSourceId},#{addAuth},#{updateAuth},#{removeAuth})")
    void addAuth(Auth auth);

}
