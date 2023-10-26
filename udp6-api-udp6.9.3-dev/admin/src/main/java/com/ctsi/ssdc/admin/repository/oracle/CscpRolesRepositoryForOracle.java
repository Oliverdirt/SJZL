package com.ctsi.ssdc.admin.repository.oracle;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;

import com.ctsi.ssdc.admin.domain.CscpRoles;
import com.ctsi.ssdc.admin.repository.CscpRolesRepository;
import com.ctsi.ssdc.admin.repository.CscpRolesSqlProvider;


public interface CscpRolesRepositoryForOracle extends CscpRolesRepository{

    @Override
    @Insert({
        "insert into cscp_roles (id, name, role_extent, ",
        "parent_id, icon, tenantId)",
        "values (#{id,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, #{roleExtent,jdbcType=VARCHAR}, ",
        "#{parentId,jdbcType=INTEGER}, #{icon,jdbcType=VARCHAR}, #{tenantId,jdbcType=BIGINT})"
    })
    //@SelectKey(statement="SELECT SEQ_CSCP_ROLES_ID.nextval as id from dual",
    //        keyProperty="id", before=true, resultType=Integer.class)
    int insert(CscpRoles record);

    @Override
    @InsertProvider(type=CscpRolesSqlProvider.class, method="insertSelective")
    //@SelectKey(statement="SELECT SEQ_CSCP_ROLES_ID.nextval as id from dual",
    //        keyProperty="id", before=true, resultType=Integer.class)
    int insertSelective(CscpRoles record);

}