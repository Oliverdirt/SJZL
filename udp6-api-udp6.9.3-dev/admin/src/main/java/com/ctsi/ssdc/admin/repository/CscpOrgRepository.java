package com.ctsi.ssdc.admin.repository;

import java.util.List;


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

import com.ctsi.ssdc.admin.domain.CscpOrg;
import com.ctsi.ssdc.admin.domain.CscpOrgExample;
import com.ctsi.ssdc.database.annotation.InjectByDataBaseType;
import com.ctsi.ssdc.database.enums.EnumDatabaseName;

/**
 * @author MyBatis Generator
*/
@InjectByDataBaseType(includes= {EnumDatabaseName.ORACLE})
public interface CscpOrgRepository {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_org
     *
     * @mbg.generated Mon Apr 23 08:56:34 CST 2018
     */
    @SelectProvider(type=CscpOrgSqlProvider.class, method="countByExample")
    long countByExample(CscpOrgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_org
     *
     * @mbg.generated Mon Apr 23 08:56:34 CST 2018
     */
    @DeleteProvider(type=CscpOrgSqlProvider.class, method="deleteByExample")
    int deleteByExample(CscpOrgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_org
     *
     * @mbg.generated Mon Apr 23 08:56:34 CST 2018
     */
    @Delete({
        "delete from cscp_org",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);
    
    /**
     * truncate table
     * @param id
     * @return
     */
    @Delete({
        "truncate table cscp_org"
    })
    int truncateCscpOrg();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_org
     *
     * @mbg.generated Mon Apr 23 08:56:34 CST 2018
     */
    @Insert({
            "insert into cscp_org (id,org_name, description, ",
            "parent_id, orderby)",
            "values (#{id,jdbcType=BIGINT},#{orgName,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, ",
            "#{parentId,jdbcType=BIGINT}, #{orderby,jdbcType=INTEGER})"
    })
    //@Options(useGeneratedKeys=true, keyProperty="id")
    int insert(CscpOrg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_org
     *
     * @mbg.generated Mon Apr 23 08:56:34 CST 2018
     */
    @InsertProvider(type=CscpOrgSqlProvider.class, method="insertSelective")
    int insertSelective(CscpOrg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_org
     *
     * @mbg.generated Mon Apr 23 08:56:34 CST 2018
     */
    @SelectProvider(type=CscpOrgSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="org_name", property="orgName", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_id", property="parentId", jdbcType=JdbcType.BIGINT),
         @Result(column="orderby", property="orderby", jdbcType=JdbcType.INTEGER)
    })
    List<CscpOrg> selectByExample(CscpOrgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_org
     *
     * @mbg.generated Mon Apr 23 08:56:34 CST 2018
     */
    @Select({
        "select",
        "id, org_name, description, parent_id,orderby",
        "from cscp_org",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="org_name", property="orgName", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_id", property="parentId", jdbcType=JdbcType.BIGINT),
            @Result(column="orderby", property="orderby", jdbcType=JdbcType.INTEGER)
    })
    CscpOrg selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_org
     *
     * @mbg.generated Mon Apr 23 08:56:34 CST 2018
     */
    @UpdateProvider(type=CscpOrgSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") CscpOrg record, @Param("example") CscpOrgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_org
     *
     * @mbg.generated Mon Apr 23 08:56:34 CST 2018
     */
    @UpdateProvider(type=CscpOrgSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") CscpOrg record, @Param("example") CscpOrgExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_org
     *
     * @mbg.generated Mon Apr 23 08:56:34 CST 2018
     */
    @UpdateProvider(type=CscpOrgSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(CscpOrg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_org
     *
     * @mbg.generated Mon Apr 23 08:56:34 CST 2018
     */
    @Update({
        "update cscp_org",
        "set org_name = #{orgName,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "parent_id = #{parentId,jdbcType=BIGINT}",
            "orderby = #{orderby,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(CscpOrg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_org
     *
     * @mbg.generated Mon Apr 23 08:56:34 CST 2018
     */
    @SelectProvider(type=CscpOrgSqlProvider.class, method="selectByExampleWithPage")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="org_name", property="orgName", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_id", property="parentId", jdbcType=JdbcType.BIGINT),
            @Result(column="orderby", property="orderby", jdbcType=JdbcType.INTEGER)
    })
    List<CscpOrg> selectByExamplewithPage(CscpOrgExample example);
}