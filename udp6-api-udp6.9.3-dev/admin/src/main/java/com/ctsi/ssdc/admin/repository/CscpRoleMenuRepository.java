package com.ctsi.ssdc.admin.repository;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import com.ctsi.ssdc.admin.domain.CscpRoleMenu;
import com.ctsi.ssdc.admin.domain.CscpRoleMenuExample;
import com.ctsi.ssdc.database.annotation.InjectByDataBaseType;
import com.ctsi.ssdc.database.enums.EnumDatabaseName;

/**
 * @author ctsi-biyi-generator
*/
@InjectByDataBaseType(includes= {EnumDatabaseName.ORACLE})
public interface CscpRoleMenuRepository extends
        com.ctsi.ssdc.repository.BaseRepository<CscpRoleMenu, Long, CscpRoleMenuExample> {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     * @return
     */
    @Override
    @SelectProvider(type=CscpRoleMenuSqlProvider.class, method="countByExample")
    long countByExample(CscpRoleMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    @Override
    @DeleteProvider(type=CscpRoleMenuSqlProvider.class, method="deleteByExample")
    int deleteByExample(CscpRoleMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    @Override
    @Delete({
        "delete from cscp_role_menu",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    @Override
    @Insert({
        "insert into cscp_role_menu (id,role_id, menu_id)",
        "values (#{id,jdbcType=BIGINT},#{roleId,jdbcType=BIGINT}, #{menuId,jdbcType=BIGINT})"
    })
    //@Options(useGeneratedKeys=true, keyProperty="id")
    int
    insert(CscpRoleMenu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    @Override
    @InsertProvider(type=CscpRoleMenuSqlProvider.class, method="insertSelective")
    //@Options(useGeneratedKeys=true, keyProperty="id")
    int insertSelective(CscpRoleMenu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    @Override
    @SelectProvider(type=CscpRoleMenuSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.BIGINT),
        @Result(column="menu_id", property="menuId", jdbcType=JdbcType.BIGINT)
    })
    List<CscpRoleMenu> selectByExample(CscpRoleMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    @Override
    @Select({
        "select",
        "id, role_id, menu_id",
        "from cscp_role_menu",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.BIGINT),
        @Result(column="menu_id", property="menuId", jdbcType=JdbcType.BIGINT)
    })
    CscpRoleMenu selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    @Override
    @UpdateProvider(type=CscpRoleMenuSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") CscpRoleMenu record, @Param("example") CscpRoleMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    @Override
    @UpdateProvider(type=CscpRoleMenuSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") CscpRoleMenu record, @Param("example") CscpRoleMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    @Override
    @UpdateProvider(type=CscpRoleMenuSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(CscpRoleMenu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    @Override
    @Update({
        "update cscp_role_menu",
        "set role_id = #{roleId,jdbcType=BIGINT},",
          "menu_id = #{menuId,jdbcType=BIGINT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(CscpRoleMenu record);

    /**
     * 批量删除菜单角色关联
     * @param roleIds
     */

    @Delete("<script>" +
            "delete from cscp_role_menu where role_id in" +
            "<foreach collection=\"roleIds\" item=\"roleId\" open=\"(\" separator=\",\" close=\")\">" +
            "#{roleId}" +
            "</foreach>" +
            "</script>")
    void deleteMenusByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据菜单id删除菜单角色关联
     * @param menuId
     */
    @Delete("delete from cscp_role_menu where menu_id = #{menuId}")
    void deleteMenusByMenuId(@Param("menuId") Long menuId);

    /**
     * 根据菜单id 批量删除菜单角色关联
     * @param idList 菜单id 列表
     */
    @Delete("<script>" +
            "delete from cscp_role_menu where menu_id in" +
            "<foreach collection=\"idList\" item=\"id\" open=\"(\" separator=\",\" close=\")\">" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    void deleteMenusByMenuIdBatch(@Param("idList") List<Long> idList);

    @Insert({"<script>insert into cscp_role_menu (id,role_id,menu_id) values <foreach collection='list' item='c' separator=',' > (#{c.id},#{c.roleId},#{c.menuId})</foreach></script>"})
    @Options(useGeneratedKeys=true, keyProperty="id")
    int insertList(@Param("list") List<CscpRoleMenu> record);

    @Select({
            "select",
            "id, role_id, menu_id",
            "from cscp_role_menu",
            "where menu_id = #{menuId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="role_id", property="roleId", jdbcType=JdbcType.BIGINT),
            @Result(column="menu_id", property="menuId", jdbcType=JdbcType.BIGINT)
    })
    List<CscpRoleMenu> selectByMenuId(@Param("menuId") Long menuId);
    @Select({
            "select",
            "id, role_id, menu_id",
            "from cscp_role_menu",
            "where menu_id = #{menuId,jdbcType=BIGINT} AND role_id = #{roleId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="role_id", property="roleId", jdbcType=JdbcType.BIGINT),
            @Result(column="menu_id", property="menuId", jdbcType=JdbcType.BIGINT)
    })
    CscpRoleMenu selectByMenuIdAndRoleId(@Param("menuId") Long menuId,@Param("roleId") Long roleId);

    @Select({
            "select",
            "role_id",
            "from cscp_role_menu",
            "where menu_id = #{menuId,jdbcType=BIGINT} AND role_id != 1"
    })
    @Results({
            @Result(column="role_id", property="role_id", jdbcType=JdbcType.BIGINT)
    })
    List<Long> selectListByMenuId(@Param("menuId") Long menuId);

    /**
     * 根据菜单id删除菜单角色关联(不删除与admin的绑定)
     * @param menuId
     */
    @Delete("delete from cscp_role_menu where menu_id = #{menuId} AND role_id != 1")
    void deleteMenusByMenuIdNo(@Param("menuId") Long menuId);


}