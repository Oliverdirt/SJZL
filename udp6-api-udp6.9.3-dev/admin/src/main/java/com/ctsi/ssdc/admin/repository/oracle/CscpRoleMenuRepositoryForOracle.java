package com.ctsi.ssdc.admin.repository.oracle;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;

import com.ctsi.ssdc.admin.domain.CscpRoleMenu;
import com.ctsi.ssdc.admin.repository.CscpRoleMenuRepository;
import com.ctsi.ssdc.admin.repository.CscpRoleMenuSqlProvider;


public interface CscpRoleMenuRepositoryForOracle extends CscpRoleMenuRepository {

	@Override
	@Insert({ "insert into cscp_role_menu (id, role_id, menu_id)",
			"values (#{id,jdbcType=BIGINT}, #{roleId,jdbcType=VARCHAR}, #{menuId,jdbcType=VARCHAR})" })
	//@SelectKey(statement = "SELECT SEQ_CSCP_ROLE_MENU_ID.nextval as id from dual",
	//		keyProperty = "id", before = true, resultType = Integer.class)
	int insert(CscpRoleMenu record);

	@Override
	@InsertProvider(type = CscpRoleMenuSqlProvider.class, method = "insertSelective")
	//@SelectKey(statement = "SELECT SEQ_CSCP_ROLE_MENU_ID.nextval as id from dual",
	//		keyProperty = "id", before = true, resultType = Integer.class)
	int insertSelective(CscpRoleMenu record);

}