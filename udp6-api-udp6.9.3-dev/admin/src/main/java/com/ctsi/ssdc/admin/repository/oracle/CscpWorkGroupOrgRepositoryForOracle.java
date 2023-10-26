package com.ctsi.ssdc.admin.repository.oracle;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;

import com.ctsi.ssdc.admin.domain.CscpWorkGroupOrg;
import com.ctsi.ssdc.admin.repository.CscpWorkGroupOrgRepository;
import com.ctsi.ssdc.admin.repository.CscpWorkGroupOrgSqlProvider;

/**
 * @author lym
*/

public interface CscpWorkGroupOrgRepositoryForOracle extends CscpWorkGroupOrgRepository{

    @Override
    @Insert({
        "insert into cscp_work_group_org (id, group_id, org_id)",
        "values (#{id,jdbcType=INTEGER}, #{groupId,jdbcType=INTEGER}, #{orgId,jdbcType=INTEGER})"
    })
    //@SelectKey(statement="SELECT SEQ_CSCP_WORK_GROUP_ORG_ID.nextval as id from dual",
    //        keyProperty="id", before=true, resultType=Integer.class)
    int insert(CscpWorkGroupOrg record);

    @Override
    @InsertProvider(type=CscpWorkGroupOrgSqlProvider.class, method="insertSelective")
    //@SelectKey(statement="SELECT SEQ_CSCP_WORK_GROUP_ORG_ID.nextval as id from dual",
    //        keyProperty="id", before=true, resultType=Integer.class)
    int insertSelective(CscpWorkGroupOrg record);
}