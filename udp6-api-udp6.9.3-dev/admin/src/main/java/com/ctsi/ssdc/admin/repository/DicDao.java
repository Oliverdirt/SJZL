package com.ctsi.ssdc.admin.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.ctsi.ssdc.database.annotation.InjectByDataBaseType;
import com.ctsi.ssdc.model.DicForm;

@InjectByDataBaseType
public interface DicDao {

	@Select("select * from cscp_dic where dic_type=#{dicType}")
	@Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="dic_type", property="dicType", jdbcType=JdbcType.BIGINT),
        @Result(column="dic_value", property="dicValue", jdbcType=JdbcType.BIGINT),
        @Result(column="dic_display", property="dicDisplay", jdbcType=JdbcType.VARCHAR),
        @Result(column="dic_group_id", property="dicGroupId", jdbcType=JdbcType.BIGINT),
        @Result(column="dic_parent_id", property="dicParentId", jdbcType=JdbcType.BIGINT),
        @Result(column="dic_order", property="dicOrder", jdbcType=JdbcType.BIGINT),
        @Result(column="dic_icon", property="dicIcon", jdbcType=JdbcType.VARCHAR),
        @Result(column="dic_check_radio", property="dicCheckRadio", jdbcType=JdbcType.VARCHAR)
    })
	List<DicForm> selectDicByDicType(@Param("dicType") Long dicType);

}
