package com.ctsi.flow.repository;

import com.ctsi.flow.param.model.CscpFlowProcessDefExt;
import com.ctsi.flow.param.model.CscpFlowProcessDefExtExample;
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
 * CscpFlowProcessDefExtRepository
 *
 * @author hx
 * @date 2022-08-05 10:08:04
 */

@Mapper
public interface CscpFlowProcessDefExtRepository extends com.ctsi.ssdc.repository.BaseRepository<CscpFlowProcessDefExt, Long, CscpFlowProcessDefExtExample> {
    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_flow_process_def_ext
     *
     * @date 2022-08-05 10:08:04
     */
    @SelectProvider(type=CscpFlowProcessDefExtSqlProvider.class, method="countByExample")
    @Override
    long countByExample(CscpFlowProcessDefExtExample example);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_flow_process_def_ext
     *
     * @date 2022-08-05 10:08:04
     */
    @DeleteProvider(type=CscpFlowProcessDefExtSqlProvider.class, method="deleteByExample")
    @Override
    int deleteByExample(CscpFlowProcessDefExtExample example);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_flow_process_def_ext
     *
     * @date 2022-08-05 10:08:04
     */
    @Delete({
            "delete from cscp_flow_process_def_ext",
            "where id = #{id,jdbcType=BIGINT}"
    })
    @Override
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_flow_process_def_ext
     *
     *
     * @date 2022-08-05 10:08:04
     */
    @Insert({
            "insert into cscp_flow_process_def_ext (",
            "description,",
            "create_time,",
            "creator,",
            "model_id,",
            "form_id,",
            "process_definition_id,",
            "id,",
            "tenant_id,",
            "update_time,",
            "updater",
            ") values (",
            "#{description,jdbcType=VARCHAR}, ",
            "#{createTime,jdbcType=DATE}, ",
            "#{creator,jdbcType=VARCHAR}, ",
            "#{modelId,jdbcType=VARCHAR}, ",
            "#{formId,jdbcType=BIGINT}, ",
            "#{processDefinitionId,jdbcType=VARCHAR}, ",
            "#{id,jdbcType=BIGINT}, ",
            "#{tenantId,jdbcType=BIGINT}, ",
            "#{updateTime,jdbcType=DATE}, ",
            "#{updater,jdbcType=VARCHAR}",

            ")"

    })
    @Override
    int insert(CscpFlowProcessDefExt record);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_flow_process_def_ext
     *
     * @date 2022-08-05 10:08:04
     */
    @InsertProvider(type=CscpFlowProcessDefExtSqlProvider.class, method="insertSelective")
    @Override
    int insertSelective(CscpFlowProcessDefExt record);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_flow_process_def_ext
     *
     * @date 2022-08-05 10:08:04
     */
    @SelectProvider(type=CscpFlowProcessDefExtSqlProvider.class, method="selectByExample")
    @Results({
            @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR,id=true ),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.DATE),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="model_id", property="modelId", jdbcType=JdbcType.VARCHAR),
            @Result(column="form_id", property="formId", jdbcType=JdbcType.BIGINT),
            @Result(column="process_definition_id", property="processDefinitionId", jdbcType=JdbcType.VARCHAR),
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT),
            @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.DATE),
            @Result(column="updater", property="updater", jdbcType=JdbcType.VARCHAR)
    })

    @Override
    List<CscpFlowProcessDefExt> selectByExample(CscpFlowProcessDefExtExample example);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_flow_process_def_ext
     *
     * @date 2022-08-05 10:08:04
     */
    @Select({
            "select",
            "description,",
            "create_time,",
            "creator,",
            "model_id,",
            "form_id,",
            "process_definition_id,",
            "id,",
            "tenant_id,",
            "update_time,",
            "updater",
            "from cscp_flow_process_def_ext",
            "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR,id=true ),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.DATE),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="model_id", property="modelId", jdbcType=JdbcType.VARCHAR),
            @Result(column="form_id", property="formId", jdbcType=JdbcType.BIGINT),
            @Result(column="process_definition_id", property="processDefinitionId", jdbcType=JdbcType.VARCHAR),
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT),
            @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.DATE),
            @Result(column="updater", property="updater", jdbcType=JdbcType.VARCHAR)
    })
    @Override
    CscpFlowProcessDefExt selectByPrimaryKey(Long id);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_flow_process_def_ext
     *
     * @date 2022-08-05 10:08:04
     */
    @UpdateProvider(type=CscpFlowProcessDefExtSqlProvider.class, method="updateByExampleSelective")
    @Override
    int updateByExampleSelective(@Param("record") CscpFlowProcessDefExt record, @Param("example") CscpFlowProcessDefExtExample example);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_flow_process_def_ext
     *
     * @date 2022-08-05 10:08:04
     */
    @UpdateProvider(type=CscpFlowProcessDefExtSqlProvider.class, method="updateByExample")
    @Override
    int updateByExample(@Param("record") CscpFlowProcessDefExt record, @Param("example") CscpFlowProcessDefExtExample example);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_flow_process_def_ext
     *
     *@date 2022-08-05 10:08:04
     */
    @UpdateProvider(type=CscpFlowProcessDefExtSqlProvider.class, method="updateByPrimaryKeySelective")
    @Override
    int updateByPrimaryKeySelective(CscpFlowProcessDefExt record);

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_flow_process_def_ext
     *
     * @date 2022-08-05 10:08:04
     */
    @Update({
            "update cscp_flow_process_def_ext",
            "set ",
            "description = #{description,jdbcType=VARCHAR},",
            "create_time = #{createTime,jdbcType=DATE},",
            "creator = #{creator,jdbcType=VARCHAR},",
            "model_id = #{modelId,jdbcType=VARCHAR},",
            "form_id = #{formId,jdbcType=BIGINT},",
            "process_definition_id = #{processDefinitionId,jdbcType=VARCHAR},",
            "id = #{id,jdbcType=BIGINT},",
            "tenant_id = #{tenantId,jdbcType=BIGINT},",
            "update_time = #{updateTime,jdbcType=DATE},",
            "updater = #{updater,jdbcType=VARCHAR}",

            "where id = #{id,jdbcType=BIGINT}"
    })
    @Override
    int updateByPrimaryKey(CscpFlowProcessDefExt record);

    /**
     * 批量删除
     * @param ids
     */
    @Delete("<script>" +
            "delete from cscp_flow_process_def_ext where id in" +
            "<foreach collection=\"ids\" item=\"id\" open=\"(\" separator=\",\" close=\")\">" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    void deleteByIds(@Param("ids") List<Long> ids);


    @Select({
            "select",
            "id,",
            "process_definition_id,",
            "model_id,",
            "description,",
            "form_id,",
            "creator,",
            "create_time,",
            "updater,",
            "update_time,",
            "tenant_id",
            "from cscp_flow_process_def_ext",
            "where model_id = #{modelId,jdbcType=VARCHAR} order by create_time desc"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT,id=true ),
            @Result(column="process_definition_id", property="processDefinitionId", jdbcType=JdbcType.VARCHAR),
            @Result(column="model_id", property="modelId", jdbcType=JdbcType.VARCHAR),
            @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
            @Result(column="form_id", property="formId", jdbcType=JdbcType.BIGINT),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.DATE),
            @Result(column="updater", property="updater", jdbcType=JdbcType.VARCHAR),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.DATE),
            @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT)
    })
    List<CscpFlowProcessDefExt> selectByModelId(@Param("modelId") String modelId);

    @Select({
            "select",
            "process_definition_id",
            "from cscp_flow_process_def_ext",
            "where model_id = #{modelId,jdbcType=VARCHAR}"
    })
    List<String> listAllDeployIdByProcessDefinitionId(@Param("modelId") String modelId);


    @Select({
            "select",
            "id,",
            "process_definition_id,",
            "model_id,",
            "description,",
            "form_id,",
            "creator,",
            "create_time,",
            "updater,",
            "update_time,",
            "tenant_id",
            "from cscp_flow_process_def_ext",
            "where id in (<foreach collection='proDefIds' item='id' index='index' separator=','> (#{id})</foreach>)"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT,id=true ),
            @Result(column="process_definition_id", property="processDefinitionId", jdbcType=JdbcType.VARCHAR),
            @Result(column="model_id", property="modelId", jdbcType=JdbcType.VARCHAR),
            @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
            @Result(column="form_id", property="formId", jdbcType=JdbcType.BIGINT),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.DATE),
            @Result(column="updater", property="updater", jdbcType=JdbcType.VARCHAR),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.DATE),
            @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT)
    })
    List<CscpFlowProcessDefExt> selectListByProcessDefinitionIds(@Param("proDefIds") List<String> proDefIds);


    @Select({
            "select",
            "id,",
            "process_definition_id,",
            "model_id,",
            "description,",
            "form_id,",
            "creator,",
            "create_time,",
            "updater,",
            "update_time,",
            "tenant_id",
            "from cscp_flow_process_def_ext",
            "where process_definition_id = #{processDefinitionId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT,id=true ),
            @Result(column="process_definition_id", property="processDefinitionId", jdbcType=JdbcType.VARCHAR),
            @Result(column="model_id", property="modelId", jdbcType=JdbcType.VARCHAR),
            @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
            @Result(column="form_id", property="formId", jdbcType=JdbcType.BIGINT),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.DATE),
            @Result(column="updater", property="updater", jdbcType=JdbcType.VARCHAR),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.DATE),
            @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT)
    })
    CscpFlowProcessDefExt selectByProcessDefinitionId(String processDefinitionId);
}





