package com.ctsi.ssdc.admin.repository;

import com.ctsi.ssdc.admin.domain.CscpUserPasswordChangeLog;
import com.ctsi.ssdc.admin.domain.CscpUserPasswordChangeLogExample;
import java.util.List;

import com.ctsi.ssdc.database.annotation.InjectByDataBaseType;
import com.ctsi.ssdc.database.enums.EnumDatabaseName;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

/**
 * @author ctsi-biyi-generator
*/
@InjectByDataBaseType(includes= {EnumDatabaseName.ORACLE})
public interface CscpUserPasswordChangeLogRepository /*extends BaseRepository<CscpUserPasswordChangeLog, Integer, CscpUserPasswordChangeLogExample> */{
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_password_change_log
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    @SelectProvider(type=CscpUserPasswordChangeLogSqlProvider.class, method="countByExample")
    long countByExample(CscpUserPasswordChangeLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_password_change_log
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    @DeleteProvider(type=CscpUserPasswordChangeLogSqlProvider.class, method="deleteByExample")
    int deleteByExample(CscpUserPasswordChangeLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_password_change_log
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    @Delete({
        "delete from cscp_user_password_change_log",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_password_change_log
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    @Insert({
        "insert into cscp_user_password_change_log (id,user_id, password, ",
        "time)",
        "values (#{id,jdbcType=BIGINT},#{userId,jdbcType=BIGINT}, #{password,jdbcType=VARCHAR}, ",
        "#{time,jdbcType=TIMESTAMP})"
    })
    //@Options(useGeneratedKeys=true, keyProperty="id")
    int insert(CscpUserPasswordChangeLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_password_change_log
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    @InsertProvider(type=CscpUserPasswordChangeLogSqlProvider.class, method="insertSelective")
    //@Options(useGeneratedKeys=true, keyProperty="id")
    int insertSelective(CscpUserPasswordChangeLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_password_change_log
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    @SelectProvider(type=CscpUserPasswordChangeLogSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
        @Result(column="time", property="time", jdbcType=JdbcType.TIMESTAMP)
    })
    List<CscpUserPasswordChangeLog> selectByExample(CscpUserPasswordChangeLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_password_change_log
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    @Select({
        "select",
        "id, user_id, password, time",
        "from cscp_user_password_change_log",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
        @Result(column="time", property="time", jdbcType=JdbcType.TIMESTAMP)
    })
    CscpUserPasswordChangeLog selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_password_change_log
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    @UpdateProvider(type=CscpUserPasswordChangeLogSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") CscpUserPasswordChangeLog record,
                                 @Param("example") CscpUserPasswordChangeLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_password_change_log
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    @UpdateProvider(type=CscpUserPasswordChangeLogSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") CscpUserPasswordChangeLog record,
                        @Param("example") CscpUserPasswordChangeLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_password_change_log
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    @UpdateProvider(type=CscpUserPasswordChangeLogSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(CscpUserPasswordChangeLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_password_change_log
     *
     * @mbg.generated Mon Jan 06 10:20:10 CST 2020
     */
    @Update({
        "update cscp_user_password_change_log",
        "set user_id = #{userId,jdbcType=BIGINT},",
          "password = #{password,jdbcType=VARCHAR},",
          "time = #{time,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(CscpUserPasswordChangeLog record);
}