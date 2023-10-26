package com.ctsi.ssdc.admin.repository.oracle;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;

import com.ctsi.ssdc.admin.domain.CscpMenus;
import com.ctsi.ssdc.admin.repository.CscpMenusRepository;
import com.ctsi.ssdc.admin.repository.CscpMenusSqlProvider;

public interface CscpMenusRepositoryForOracle extends CscpMenusRepository{
	 @Insert({
	        "insert into cscp_menus (id, name, icon, ",
	        "title, url, http_method, ",
	        "component, parent_id, ",
	        "type, permission_code, ",
	        "orderby)",
	        "values (#{id,jdbcType=BIGINT}, #{name,jdbcType=VARCHAR}, #{icon,jdbcType=VARCHAR}, ",
	        "#{title,jdbcType=VARCHAR}, #{url,jdbcType=VARCHAR}, #{httpMethod,jdbcType=VARCHAR}, ",
	        "#{component,jdbcType=VARCHAR}, #{parentId,jdbcType=BIGINT}, ",
	        "#{type,jdbcType=VARCHAR}, #{permissionCode,jdbcType=VARCHAR}, ",
	        "#{orderby,jdbcType=INTEGER})"
	    })
	    //@SelectKey(statement="select SEQ_CSCP_MENUS_ID.nextval as id from dual",
		//		keyProperty="id", before=true, resultType=Long.class)
	 @Override
	    Long insert(CscpMenus record);
	 
	 @InsertProvider(type=CscpMenusSqlProvider.class, method="insertSelective")
	    //@SelectKey(statement="select SEQ_CSCP_MENUS_ID.nextval as id from dual",
		//		keyProperty="id", before=true, resultType=Long.class)
	 @Override
	 Long insertSelective(CscpMenus record);
}
