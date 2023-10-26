package com.ctsi.ssdc.admin.repository.postgresql;

import com.ctsi.ssdc.admin.domain.dto.CscpUserDetailDTO;
import com.ctsi.ssdc.admin.repository.CscpUserDetailRepository;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author MyBatis Generator
*/

public interface CscpUserDetailRepositoryForPostgreSQL extends CscpUserDetailRepository {
    @Results({
            @Result(column="last_login", property="lastLogin"),
            @Result(column="mobile", property="mobile"),
            @Result(column="disc_title", property="discTitle"),
            @Result(column="register_time", property="registerTime"),
            @Result(column="user_id", property="userId"),
            @Result(column="img_path", property="imgPath"),
            @Result(column="name", property="name"),
            @Result(column="disc_detail", property="discDetail"),
            @Result(column="id", property="id"),
            @Result(column="family_name", property="familyName"),
            @Result(column="email", property="email"),
            @Result(column="username", property="username"),
            @Result(column="roleNames", property="roleNames"),
            @Result(column="roleIds", property="roleIds"),
            @Result(column="admin_flag", property="adminFlag")

    })
    @Select("<script> select cscp_user.username " +
            "        , cscp_user_detail.id as id " +
            "        , cscp_user_detail.user_id " +
            "        , cscp_user_detail.family_name " +
            "        , cscp_user_detail.name " +
            "        , cscp_user_detail.mobile " +
            "        , cscp_user_detail.email " +
            "        , cscp_user_detail.img_path " +
            "        , cscp_user_detail.last_login " +
            "        , cscp_user_detail.disc_title " +
            "        , cscp_user_detail.disc_detail " +
            "        , cscp_user_detail.register_time  " +
            "        , cscp_user_detail.admin_flag  "+
            "          from cscp_user  " +
            "        left JOIN cscp_user_detail on cscp_user.id=cscp_user_detail.user_id where  " +
            " cscp_user_detail.tenant_id = #{tenantId} "+

            " <if test=\"deptIdEquals != null and deptIdEquals != '' \" >" +
            " and (cscp_user_detail.dept_id = #{deptIdEquals} or cscp_user_detail.dept_id in " +
            "(select t.dept_id FROM cscp_dept t WHERE ancestors like CONCAT('%,',#{deptIdEquals}::varchar,'%')) )  " +
            " </if>" +

            " <if test=\"usernameLike != null and usernameLike != '' \" >" +
            " and  cscp_user.username like  concat('%',#{usernameLike}::varchar,'%') " +
            " </if>" +

            " <if test=\"familyNameLike != null and familyNameLike != '' \" >" +
            " and cscp_user_detail.family_name like  concat('%',#{familyNameLike}::varchar,'%') " +
            " </if>" +

            " <if test=\"nameLike != null and nameLike != '' \" >" +
            " and cscp_user_detail.name like  concat('%',#{nameLike}::varchar,'%') " +
            " </if>" +

            " <if test=\"mobileLike != null and mobileLike != '' \" >" +
            " and cscp_user_detail.mobile like  concat('%',#{mobileLike}::varchar,'%') " +
            " </if>" +

            " <if test=\"emailLike != null and emailLike != '' \" >" +
            " and cscp_user_detail.email like  concat('%',#{emailLike}::varchar,'%') " +
            " </if>" +

            " <if test=\"discTitleLike != null and discTitleLike != '' \" >" +
            " and cscp_user_detail.disc_title like  concat('%',#{discTitleLike}::varchar,'%') " +
            " </if>" +

            " <if test=\"discDetailLike != null and discDetailLike != '' \" >" +
            " and cscp_user_detail.disc_detail like  concat('%',#{discDetailLike}::varchar,'%') ) " +
            " </if>" +
            " <if test=\"orderby != null and orderby != '' \" >" +
            " order by   cscp_user_detail.${orderby} " +
            " </if>" +

            "</script>"

    )
    @Override
    List<CscpUserDetailDTO> queryUserDetailPreventSqlAttack(
            @Param("usernameLike")String usernameLike,
            @Param("familyNameLike")String familyNameLike,
            @Param("nameLike")String nameLike,
            @Param("mobileLike")String mobileLike,
            @Param("emailLike")String emailLike,
            @Param("discTitleLike")String discTitleLike,
            @Param("discDetailLike")String discDetailLike,
            @Param("deptIdEquals")String deptIdEquals,
            @Param("orderby")String orderby,
            @Param("tenantId")Long tenantId
    );

}