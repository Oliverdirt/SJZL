package com.ctsi.ssdc.admin.repository.oracle;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;

import com.ctsi.ssdc.admin.domain.CscpUserRole;
import com.ctsi.ssdc.admin.repository.CscpUserRoleRepository;
import com.ctsi.ssdc.admin.repository.CscpUserRoleSqlProvider;

public interface CscpUserRoleRepositoryForOracle extends CscpUserRoleRepository{

    @Override
    @Insert({
        "insert into cscp_user_role (id, user_id, role_id)",
        "values (#{id,jdbcType=INTEGER}, #{userId,jdbcType=INTEGER}, #{roleId,jdbcType=INTEGER})"
    })
    //@SelectKey(statement="SELECT SEQ_CSCP_USER_ROLE_ID.nextval as id from dual",
    //        keyProperty="id", before=true, resultType=Integer.class)
    int insert(CscpUserRole record);

    @Override
    @InsertProvider(type=CscpUserRoleSqlProvider.class, method="insertSelective")
    //@SelectKey(statement="SELECT SEQ_CSCP_USER_ROLE_ID.nextval as id from dual",
    //        keyProperty="id", before=true, resultType=Integer.class)
    int insertSelective(CscpUserRole record);

}