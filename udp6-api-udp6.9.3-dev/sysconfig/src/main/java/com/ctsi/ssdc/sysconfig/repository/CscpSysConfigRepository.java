package com.ctsi.ssdc.sysconfig.repository;

import com.ctsi.ssdc.sysconfig.domain.CscpSysConfig;
import com.ctsi.ssdc.sysconfig.domain.CscpSysConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

/**
 * CscpSysConfigRepository
 *
 * @author hx
 * @date 2022-08-24 14:40:57
 */

@Mapper
public interface CscpSysConfigRepository extends com.ctsi.ssdc.repository.BaseRepository<CscpSysConfig, Long, CscpSysConfigExample> {
    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sys_config
     *
     * @date 2022-08-24 14:40:57
     */
    @SelectProvider(type=CscpSysConfigSqlProvider.class, method="countByExample")
    @Override
    long countByExample(CscpSysConfigExample example);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sys_config
     *
     * @date 2022-08-24 14:40:57
     */
    @DeleteProvider(type=CscpSysConfigSqlProvider.class, method="deleteByExample")
    @Override
    int deleteByExample(CscpSysConfigExample example);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sys_config
     *
     * @date 2022-08-24 14:40:57
     */
    @Delete({
            "delete from cscp_sys_config",
            "where config_id = #{configId,jdbcType=BIGINT}"
    })
    @Override
    int deleteByPrimaryKey(Long configId);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sys_config
     *
     *
     * @date 2022-08-24 14:40:57
     */
    @Insert({
            "insert into cscp_sys_config (",
            "config_id,",
            "config_name,",
            "config_key,",
            "config_value,",
            "config_option,",
            "create_by,",
            "create_time,",
            "update_by,",
            "update_time,",
            "remark,",
            "config_status",
            ") values (",
            "#{configId,jdbcType=BIGINT}, ",
            "#{configName,jdbcType=VARCHAR}, ",
            "#{configKey,jdbcType=VARCHAR}, ",
            "#{configValue,jdbcType=VARCHAR}, ",
            "#{configOption,jdbcType=VARCHAR}, ",
            "#{createBy,jdbcType=BIGINT}, ",
            "#{createTime,jdbcType=DATE}, ",
            "#{updateBy,jdbcType=BIGINT}, ",
            "#{updateTime,jdbcType=DATE}, ",
            "#{remark,jdbcType=VARCHAR}, ",
            "#{configStatus,jdbcType=INTEGER}",
            ")"
    })
    @Override
    int insert(CscpSysConfig record);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sys_config
     *
     * @date 2022-08-24 14:40:57
     */
    @InsertProvider(type=CscpSysConfigSqlProvider.class, method="insertSelective")
    @Override
    int insertSelective(CscpSysConfig record);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sys_config
     *
     * @date 2022-08-24 14:40:57
     */
    @SelectProvider(type=CscpSysConfigSqlProvider.class, method="selectByExample")
    @Results({
            @Result(column="config_id", property="configId", jdbcType=JdbcType.BIGINT,id=true ),
            @Result(column="config_name", property="configName", jdbcType=JdbcType.VARCHAR),
            @Result(column="config_key", property="configKey", jdbcType=JdbcType.VARCHAR),
            @Result(column="config_value", property="configValue", jdbcType=JdbcType.VARCHAR),
            @Result(column="config_option", property="configOption", jdbcType=JdbcType.VARCHAR),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.DATE),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.DATE),
            @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
            @Result(column="config_status", property="configStatus", jdbcType=JdbcType.INTEGER)
    })

    @Override
    List<CscpSysConfig> selectByExample(CscpSysConfigExample example);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sys_config
     *
     * @date 2022-08-24 14:40:57
     */
    @Select({
            "select",
            "config_id,",
            "config_name,",
            "config_key,",
            "config_value,",
            "config_option,",
            "create_by,",
            "create_time,",
            "update_by,",
            "update_time,",
            "remark,",
            "config_status",
            "from cscp_sys_config",
            "where config_id = #{configId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="config_id", property="configId", jdbcType=JdbcType.BIGINT,id=true ),
            @Result(column="config_name", property="configName", jdbcType=JdbcType.VARCHAR),
            @Result(column="config_key", property="configKey", jdbcType=JdbcType.VARCHAR),
            @Result(column="config_value", property="configValue", jdbcType=JdbcType.VARCHAR),
            @Result(column="config_option", property="configOption", jdbcType=JdbcType.VARCHAR),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.DATE),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.DATE),
            @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
            @Result(column="config_status", property="configStatus", jdbcType=JdbcType.INTEGER)
    })
    @Override
    CscpSysConfig selectByPrimaryKey(Long configId);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sys_config
     *
     * @date 2022-08-24 14:40:57
     */
    @UpdateProvider(type=CscpSysConfigSqlProvider.class, method="updateByExampleSelective")
    @Override
    int updateByExampleSelective(@Param("record") CscpSysConfig record, @Param("example") CscpSysConfigExample example);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sys_config
     *
     * @date 2022-08-24 14:40:57
     */
    @UpdateProvider(type=CscpSysConfigSqlProvider.class, method="updateByExample")
    @Override
    int updateByExample(@Param("record") CscpSysConfig record, @Param("example") CscpSysConfigExample example);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sys_config
     *
     *@date 2022-08-24 14:40:57
     */
    @UpdateProvider(type=CscpSysConfigSqlProvider.class, method="updateByPrimaryKeySelective")
    @Override
    int updateByPrimaryKeySelective(CscpSysConfig record);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sys_config
     *
     * @date 2022-08-24 14:40:57
     */
    @Update({
            "update cscp_sys_config",
            "set ",
            "config_id = #{configId,jdbcType=BIGINT},",
            "config_name = #{configName,jdbcType=VARCHAR},",
            "config_key = #{configKey,jdbcType=VARCHAR},",
            "config_value = #{configValue,jdbcType=VARCHAR},",
            "config_option = #{configOption,jdbcType=VARCHAR},",
            "create_by = #{createBy,jdbcType=BIGINT},",
            "create_time = #{createTime,jdbcType=DATE},",
            "update_by = #{updateBy,jdbcType=BIGINT},",
            "update_time = #{updateTime,jdbcType=DATE},",
            "remark = #{remark,jdbcType=VARCHAR},",
            "config_status = #{configStatus,jdbcType=INTEGER}",

            "where config_id = #{configId,jdbcType=BIGINT}"
    })
    @Override
    int updateByPrimaryKey(CscpSysConfig record);

    /**
     * 批量删除
     * @param configIds
     */
    @Delete("<script>" +
            "delete from cscp_sys_config where config_id in" +
            "<foreach collection=\"configIds\" item=\"configId\" open=\"(\" separator=\",\" close=\")\">" +
            "#{configId}" +
            "</foreach>" +
            "</script>")
    void deleteByIds(@Param("configIds") List<Long> configIds);

    @Select({
            "select",
            "config_id,",
            "config_name,",
            "config_key,",
            "config_value,",
            "config_option,",
            "create_by,",
            "create_time,",
            "update_by,",
            "update_time,",
            "remark,",
            "config_status",
            "from cscp_sys_config",
            "where config_key = #{configKey,jdbcType=VARCHAR} and config_status =1"
    })
    @Results({
            @Result(column="config_id", property="configId", jdbcType=JdbcType.BIGINT,id=true ),
            @Result(column="config_name", property="configName", jdbcType=JdbcType.VARCHAR),
            @Result(column="config_key", property="configKey", jdbcType=JdbcType.VARCHAR),
            @Result(column="config_value", property="configValue", jdbcType=JdbcType.VARCHAR),
            @Result(column="config_option", property="configOption", jdbcType=JdbcType.VARCHAR),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.DATE),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.DATE),
            @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
            @Result(column="config_status", property="configStatus", jdbcType=JdbcType.INTEGER)
    })
    CscpSysConfig getCscpSysConfigByConfigKey(String configKey);
}





