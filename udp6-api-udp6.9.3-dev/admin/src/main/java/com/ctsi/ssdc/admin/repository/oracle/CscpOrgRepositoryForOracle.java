package com.ctsi.ssdc.admin.repository.oracle;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;

import com.ctsi.ssdc.admin.domain.CscpOrg;
import com.ctsi.ssdc.admin.repository.CscpOrgRepository;
import com.ctsi.ssdc.admin.repository.CscpOrgSqlProvider;

/**
 * @author lym
*/
public interface CscpOrgRepositoryForOracle extends CscpOrgRepository{
    
    @Insert({
        "insert into cscp_org (id, org_name, description, ",
        "parent_id)",
        "values (#{id, jdbcType=BIGINT}, #{orgName,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{parentId,jdbcType=BIGINT})"
    })
    //@SelectKey(statement="select SEQ_CSCP_ORG_ID.nextval as id from dual",
    //        keyProperty="id", before=true, resultType=Integer.class)
    @Override
    int insert(CscpOrg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_org
     *
     * @mbg.generated Mon Apr 23 08:56:34 CST 2018
     */
    @InsertProvider(type=CscpOrgSqlProvider.class, method="insertSelective")
    //@SelectKey(statement="select SEQ_CSCP_ORG_ID.nextval as id from dual",
    //        keyProperty="id", before=true, resultType=Integer.class)
    @Override
    int insertSelective(CscpOrg record);

}