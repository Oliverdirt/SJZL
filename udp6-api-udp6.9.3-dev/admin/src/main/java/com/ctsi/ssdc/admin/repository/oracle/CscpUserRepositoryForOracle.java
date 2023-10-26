package com.ctsi.ssdc.admin.repository.oracle;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;

import com.ctsi.ssdc.admin.domain.CscpUser;
import com.ctsi.ssdc.admin.repository.CscpUserRepository;
import com.ctsi.ssdc.admin.repository.CscpUserSqlProvider;

public interface CscpUserRepositoryForOracle extends CscpUserRepository{

    @Override
    @Insert({
        "insert into cscp_user (id, username, password)",
        "values (#{id,jdbcType=INTEGER}, #{username,jdbcType=VARCHAR}, #{password,jdbcType=VARCHAR})"
    })
    //@SelectKey(statement="SELECT SEQ_CSCP_USER_ID.nextval as id from dual",
    //        keyProperty="id", before=true, resultType=Long.class)
    Long insert(CscpUser record);

    @Override
    @InsertProvider(type=CscpUserSqlProvider.class, method="insertSelective")
    //@SelectKey(statement="SELECT SEQ_CSCP_USER_ID.nextval as id from dual",
    //        keyProperty="id", before=true, resultType=Long.class)
    Long insertSelective(CscpUser record);

}