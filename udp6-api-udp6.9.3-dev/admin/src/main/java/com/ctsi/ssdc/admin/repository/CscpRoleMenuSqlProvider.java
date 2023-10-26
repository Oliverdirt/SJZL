package com.ctsi.ssdc.admin.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import com.ctsi.ssdc.admin.domain.CscpRoleMenu;
import com.ctsi.ssdc.admin.domain.CscpRoleMenuExample;
import com.ctsi.ssdc.admin.domain.CscpRoleMenuExample.Criteria;
import com.ctsi.ssdc.admin.domain.CscpRoleMenuExample.Criterion;

/**
 * @author ctsi-biyi-generator
*/
public class CscpRoleMenuSqlProvider {

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    public String countByExample(CscpRoleMenuExample example) {
        SQL sql = new SQL();
        sql.SELECT("count(*)").FROM("cscp_role_menu");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    public String deleteByExample(CscpRoleMenuExample example) {
        SQL sql = new SQL();
        sql.DELETE_FROM("cscp_role_menu");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    public String insertSelective(CscpRoleMenu record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("cscp_role_menu");
        
        if (record.getRoleId() != null) {
            sql.VALUES("role_id", "#{roleId,jdbcType=BIGINT}");
        }
        
        if (record.getMenuId() != null) {
            sql.VALUES("menu_id", "#{menuId,jdbcType=BIGINT}");
        }
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    public String selectByExample(CscpRoleMenuExample example) {
        if(example!=null) {
            example.setOrderByClause(example.buildOrderByClause());
        }
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("id");
        } else {
            sql.SELECT("id");
        }
        sql.SELECT("role_id");
        sql.SELECT("menu_id");
        sql.FROM("cscp_role_menu");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    public String updateByExampleSelective(Map<String, Object> parameter) {
        CscpRoleMenu record = (CscpRoleMenu) parameter.get("record");
        CscpRoleMenuExample example = (CscpRoleMenuExample) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("cscp_role_menu");
        
        if (record.getId() != null) {
            sql.SET("id = #{record.id,jdbcType=BIGINT}");
        }
        
        if (record.getRoleId() != null) {
            sql.SET("role_id = #{record.roleId,jdbcType=BIGINT}");
        }
        
        if (record.getMenuId() != null) {
            sql.SET("menu_id = #{record.menuId,jdbcType=BIGINT}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("cscp_role_menu");
        
        sql.SET("id = #{record.id,jdbcType=BIGINT}");
        sql.SET("role_id = #{record.roleId,jdbcType=BIGINT}");
        sql.SET("menu_id = #{record.menuId,jdbcType=BIGINT}");
        
        CscpRoleMenuExample example = (CscpRoleMenuExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    public String updateByPrimaryKeySelective(CscpRoleMenu record) {
        SQL sql = new SQL();
        sql.UPDATE("cscp_role_menu");
        
        if (record.getRoleId() != null) {
            sql.SET("role_id = #{roleId,jdbcType=BIGINT}");
        }
        
        if (record.getMenuId() != null) {
            sql.SET("menu_id = #{menuId,jdbcType=BIGINT}");
        }
        
        sql.WHERE("id = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_role_menu
     *
     * @mbg.generated Wed Feb 13 10:04:08 CST 2019
     */
    protected void applyWhere(SQL sql, CscpRoleMenuExample example, boolean includeExamplePhrase) {
        if (example == null) {
            return;
        }
        
        String parmPhrase1;
        String parmPhrase1Th;
        String parmPhrase2;
        String parmPhrase2Th;
        String parmPhrase3;
        String parmPhrase3Th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1Th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and " +
                    "#{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2Th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} " +
                    "and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3Th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1Th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and " +
                    "#{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2Th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and" +
                    " #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3Th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }
        
        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = example.getOredCriteria();
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            Criteria criteria = oredCriteria.get(i);
            if (criteria.isValid()) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }
                
                sb.append('(');
                List<Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }
                    
                    if (criterion.isNoValue()) {
                        sb.append(criterion.getCondition());
                    } else if (criterion.isSingleValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1Th,
                                    criterion.getCondition(), i, j,criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2Th, criterion.getCondition(),
                                    i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isListValue()) {
                        sb.append(criterion.getCondition());
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue();
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getTypeHandler() == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3Th, i, j, k, criterion.getTypeHandler()));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }
        
        if (sb.length() > 0) {
            sql.WHERE(sb.toString());
        }
    }
}