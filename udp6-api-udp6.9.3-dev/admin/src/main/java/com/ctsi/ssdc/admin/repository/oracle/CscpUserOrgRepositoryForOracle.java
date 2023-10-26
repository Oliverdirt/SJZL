package com.ctsi.ssdc.admin.repository.oracle;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;

import com.ctsi.ssdc.admin.domain.CscpUserOrg;
import com.ctsi.ssdc.admin.repository.CscpUserOrgRepository;
import com.ctsi.ssdc.admin.repository.CscpUserOrgSqlProvider;

/**
 * @author lym
*/

public interface CscpUserOrgRepositoryForOracle extends CscpUserOrgRepository{

    @Override
    @Insert({
        "insert into cscp_user_org (id, user_id, org_id)",
        "values (#{id,jdbcType=INTEGER}, #{userId,jdbcType=INTEGER}, #{orgId,jdbcType=INTEGER})"
    })
    //@SelectKey(statement="SELECT SEQ_CSCP_USER_ORG_ID.nextval as id from dual",
    //        keyProperty="id", before=true, resultType=Integer.class)
    int insert(CscpUserOrg record);

    @Override
    @InsertProvider(type=CscpUserOrgSqlProvider.class, method="insertSelective")
    //@SelectKey(statement="SELECT SEQ_CSCP_USER_ORG_ID.nextval as id from dual",
    //        keyProperty="id", before=true, resultType=Integer.class)
    int insertSelective(CscpUserOrg record);

}