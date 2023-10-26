package com.ctsi.ssdc.admin.repository.oracle;

import com.ctsi.ssdc.admin.domain.CscpUserPasswordChangeLog;
import com.ctsi.ssdc.admin.repository.CscpUserPasswordChangeLogRepository;
import com.ctsi.ssdc.admin.repository.CscpWorkGroupSqlProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;

/**
 * @author lym
*/

public interface CscpUserPasswordChangeLogRepositoryForOracle extends CscpUserPasswordChangeLogRepository {

    @Override
    @Insert({
        "insert into cscp_user_password_change_log (id, user_id, password, time)",
        "values (#{id,jdbcType=INTEGER}, #{userId,jdbcType=INTEGER}," +
                " #{password,jdbcType=VARCHAR}, #{time,jdbcType=TIMESTAMP})"
    })
    //@SelectKey(statement="SELECT SEQ_PASSWORD_CHANGE_LOG_ID.nextval as id from dual",
    //        keyProperty="id", before=true, resultType=Integer.class)
    int insert(CscpUserPasswordChangeLog record);

    @Override
    @InsertProvider(type=CscpWorkGroupSqlProvider.class, method="insertSelective")
    //@SelectKey(statement="SELECT SEQ_PASSWORD_CHANGE_LOG_ID.nextval as id from dual",
    //        keyProperty="id", before=true, resultType=Integer.class)
    int insertSelective(CscpUserPasswordChangeLog record);

}