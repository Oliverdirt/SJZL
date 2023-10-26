package com.ctsi.ssdc.admin.repository.oracle;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;

import com.ctsi.ssdc.admin.domain.CscpWorkGroup;
import com.ctsi.ssdc.admin.repository.CscpWorkGroupRepository;
import com.ctsi.ssdc.admin.repository.CscpWorkGroupSqlProvider;

/**
 * @author lym
*/

public interface CscpWorkGroupRepositoryForOracle extends CscpWorkGroupRepository{

    @Override
    @Insert({
        "insert into cscp_work_group (id, group_name, description, ",
        "org_id)",
        "values (#{id,jdbcType=INTEGER}, #{groupName,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{orgId,jdbcType=INTEGER})"
    })
    //@SelectKey(statement="SELECT SEQ_CSCP_WORK_GROUP_ID.nextval as id from dual",
    //        keyProperty="id", before=true, resultType=Integer.class)
    int insert(CscpWorkGroup record);

    @Override
    @InsertProvider(type=CscpWorkGroupSqlProvider.class, method="insertSelective")
    //@SelectKey(statement="SELECT SEQ_CSCP_WORK_GROUP_ID.nextval as id from dual",
    //        keyProperty="id", before=true, resultType=Integer.class)
    int insertSelective(CscpWorkGroup record);

}