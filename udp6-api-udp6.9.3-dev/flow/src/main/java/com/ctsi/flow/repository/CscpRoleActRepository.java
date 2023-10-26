package com.ctsi.flow.repository;

import com.ctsi.flow.param.model.CscpRoleAct;
import com.ctsi.flow.param.model.CscpRoleActExample;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * CscpRoleActRepository
 *
 * @author hx
 * @date 2022-07-26 15:53:56
 */

@Mapper
public interface CscpRoleActRepository extends com.ctsi.ssdc.repository.BaseRepository<CscpRoleAct, Long, CscpRoleActExample> {
    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_role_act
     *
     * @date 2022-07-26 15:53:56
     */
    @SelectProvider(type= CscpRoleActSqlProvider.class, method="countByExample")
    @Override
    long countByExample(CscpRoleActExample example);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_role_act
     *
     * @date 2022-07-26 15:53:56
     */
    @DeleteProvider(type= CscpRoleActSqlProvider.class, method="deleteByExample")
    @Override
    int deleteByExample(CscpRoleActExample example);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_role_act
     *
     * @date 2022-07-26 15:53:56
     */
    @Delete({
            "delete from cscp_role_act",
            "where id = #{id,jdbcType=BIGINT}"
    })
    @Override
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_role_act
     *
     *
     * @date 2022-07-26 15:53:56
     */
    @Insert({
            "insert into cscp_role_act (",
            "id,",
            "role_id,",
            "proc_def_id,",
            "proc_def_name,",
            "form_id,",
            "defined_id",
            ") values (",
            "#{id,jdbcType=BIGINT}, ",
            "#{roleId,jdbcType=BIGINT}, ",
            "#{procDefId,jdbcType=VARCHAR}, ",
            "#{procDefName,jdbcType=VARCHAR}, ",
            "#{formId,jdbcType=BIGINT}, ",
            "#{definedId,jdbcType=VARCHAR}",

            ")"

    })
    @Override
    int insert(CscpRoleAct record);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_role_act
     *
     * @date 2022-07-26 15:53:56
     */
    @InsertProvider(type= CscpRoleActSqlProvider.class, method="insertSelective")
    @Override
    int insertSelective(CscpRoleAct record);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_role_act
     *
     * @date 2022-07-26 15:53:56
     */
    @SelectProvider(type= CscpRoleActSqlProvider.class, method="selectByExample")
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT,id=true ),
            @Result(column="role_id", property="roleId", jdbcType=JdbcType.BIGINT),
            @Result(column="proc_def_id", property="procDefId", jdbcType=JdbcType.VARCHAR),
            @Result(column="proc_def_name", property="procDefName", jdbcType=JdbcType.VARCHAR),
            @Result(column="form_id", property="formId", jdbcType=JdbcType.BIGINT),
            @Result(column="defined_id", property="definedId", jdbcType=JdbcType.VARCHAR)
    })

    @Override
    List<CscpRoleAct> selectByExample(CscpRoleActExample example);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_role_act
     *
     * @date 2022-07-26 15:53:56
     */
    @Select({
            "select",
            "id,",
            "role_id,",
            "proc_def_id,",
            "proc_def_name,",
            "form_id,",
            "defined_id",
            "from cscp_role_act",
            "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT,id=true ),
            @Result(column="role_id", property="roleId", jdbcType=JdbcType.BIGINT),
            @Result(column="proc_def_id", property="procDefId", jdbcType=JdbcType.VARCHAR),
            @Result(column="proc_def_name", property="procDefName", jdbcType=JdbcType.VARCHAR),
            @Result(column="form_id", property="formId", jdbcType=JdbcType.BIGINT),
            @Result(column="defined_id", property="definedId", jdbcType=JdbcType.VARCHAR)
    })
    @Override
    CscpRoleAct selectByPrimaryKey(Long id);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_role_act
     *
     * @date 2022-07-26 15:53:56
     */
    @UpdateProvider(type= CscpRoleActSqlProvider.class, method="updateByExampleSelective")
    @Override
    int updateByExampleSelective(@Param("record") CscpRoleAct record, @Param("example") CscpRoleActExample example);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_role_act
     *
     * @date 2022-07-26 15:53:56
     */
    @UpdateProvider(type= CscpRoleActSqlProvider.class, method="updateByExample")
    @Override
    int updateByExample(@Param("record") CscpRoleAct record, @Param("example") CscpRoleActExample example);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_role_act
     *
     *@date 2022-07-26 15:53:56
     */
    @UpdateProvider(type= CscpRoleActSqlProvider.class, method="updateByPrimaryKeySelective")
    @Override
    int updateByPrimaryKeySelective(CscpRoleAct record);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_role_act
     *
     * @date 2022-07-26 15:53:56
     */
    @Update({
            "update cscp_role_act",
            "set ",
            "id = #{id,jdbcType=BIGINT},",
            "role_id = #{roleId,jdbcType=BIGINT},",
            "proc_def_id = #{procDefId,jdbcType=VARCHAR},",
            "proc_def_name = #{procDefName,jdbcType=VARCHAR},",
            "form_id = #{formId,jdbcType=BIGINT},",
            "defined_id = #{definedId,jdbcType=VARCHAR}",

            "where id = #{id,jdbcType=BIGINT}"
    })
    @Override
    int updateByPrimaryKey(CscpRoleAct record);

    /**
     * 批量删除
     * @param ids
     */
    @Delete("<script>" +
            "delete from cscp_role_act where id in" +
            "<foreach collection=\"ids\" item=\"id\" open=\"(\" separator=\",\" close=\")\">" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    void deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 根据流程模板id查询
     * */
    @Select({
            "select",
            "id,",
            "role_id,",
            "proc_def_id,",
            "proc_def_name,",
            "form_id,",
            "defined_id",
            "from cscp_role_act",
            "where proc_def_id = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT,id=true ),
            @Result(column="role_id", property="roleId", jdbcType=JdbcType.BIGINT),
            @Result(column="proc_def_id", property="procDefId", jdbcType=JdbcType.VARCHAR),
            @Result(column="proc_def_name", property="procDefName", jdbcType=JdbcType.VARCHAR),
            @Result(column="form_id", property="formId", jdbcType=JdbcType.BIGINT),
            @Result(column="defined_id", property="definedId", jdbcType=JdbcType.VARCHAR)
    })
    List<CscpRoleAct> findByProcDefId(String id);

    /**
     * 根据流程模板id删除
     *
     * */
    @Delete("delete from cscp_role_act where proc_def_id = #{id}")
    void deleteByProcDefId(@Param("id") String id);

    /**
     * 批量插入
     *
     * */
    @Insert({"<script>insert into cscp_role_act (id,role_id,proc_def_id,proc_def_name,form_id) values <foreach collection='list' item='c' separator=',' > (#{c.id},#{c.roleId},#{c.procDefId},#{c.procDefName},#{c.formId})</foreach></script>"})
    @Options(useGeneratedKeys=true, keyProperty="id")
    int insertList(@Param("list") List<CscpRoleAct> record);

    /**
     * 根据角色id批量查询
     *
     * */
    @Select({
            "<script>select * from cscp_role_act where role_id in <foreach collection='list' item='item' open='(' separator=',' close=')'> #{item}</foreach> " +
                    "limit #{page},#{size} </script>",
    })
    List<CscpRoleAct> findByRoleIds(@Param("list") List<Long> list,@Param("page")Integer page, @Param("size")Integer size);

}





