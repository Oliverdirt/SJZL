package com.ctsi.ssdc.admin.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import com.ctsi.ssdc.admin.domain.CscpUserOrg;
import com.ctsi.ssdc.admin.domain.CscpUserOrgExample;
import com.ctsi.ssdc.admin.domain.CscpUserOrgExample.Criteria;
import com.ctsi.ssdc.admin.domain.CscpUserOrgExample.Criterion;

/**
 * @author MyBatis Generator
*/
public class CscpUserOrgSqlProvider {

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_org
     *
     * @mbg.generated Thu May 03 10:11:11 CST 2018
     */
    public String countByExample(CscpUserOrgExample example) {
        SQL sql = new SQL();
        sql.SELECT("count(*)").FROM("cscp_user_org");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_org
     *
     * @mbg.generated Thu May 03 10:11:11 CST 2018
     */
    public String deleteByExample(CscpUserOrgExample example) {
        SQL sql = new SQL();
        sql.DELETE_FROM("cscp_user_org");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_org
     *
     * @mbg.generated Thu May 03 10:11:11 CST 2018
     */
    public String insertSelective(CscpUserOrg record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("cscp_user_org");
        
        if (record.getUserId() != null) {
            sql.VALUES("user_id", "#{userId,jdbcType=BIGINT}");
        }
        
        if (record.getOrgId() != null) {
            sql.VALUES("org_id", "#{orgId,jdbcType=BIGINT}");
        }
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_org
     *
     * @mbg.generated Thu May 03 10:11:11 CST 2018
     */
    public String selectByExample(CscpUserOrgExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("id");
        } else {
            sql.SELECT("id");
        }
        sql.SELECT("user_id");
        sql.SELECT("org_id");
        sql.FROM("cscp_user_org");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_org
     *
     * @mbg.generated Thu May 03 10:11:11 CST 2018
     */
    public String updateByExampleSelective(Map<String, Object> parameter) {
        CscpUserOrg record = (CscpUserOrg) parameter.get("record");
        CscpUserOrgExample example = (CscpUserOrgExample) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("cscp_user_org");
        
        if (record.getId() != null) {
            sql.SET("id = #{record.id,jdbcType=BIGINT}");
        }
        
        if (record.getUserId() != null) {
            sql.SET("user_id = #{record.userId,jdbcType=BIGINT}");
        }
        
        if (record.getOrgId() != null) {
            sql.SET("org_id = #{record.orgId,jdbcType=BIGINT}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_org
     *
     * @mbg.generated Thu May 03 10:11:11 CST 2018
     */
    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("cscp_user_org");
        
        sql.SET("id = #{record.id,jdbcType=BIGINT}");
        sql.SET("user_id = #{record.userId,jdbcType=BIGINT}");
        sql.SET("org_id = #{record.orgId,jdbcType=BIGINT}");
        
        CscpUserOrgExample example = (CscpUserOrgExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_org
     *
     * @mbg.generated Thu May 03 10:11:11 CST 2018
     */
    public String updateByPrimaryKeySelective(CscpUserOrg record) {
        SQL sql = new SQL();
        sql.UPDATE("cscp_user_org");
        
        if (record.getUserId() != null) {
            sql.SET("user_id = #{userId,jdbcType=BIGINT}");
        }
        
        if (record.getOrgId() != null) {
            sql.SET("org_id = #{orgId,jdbcType=BIGINT}");
        }
        
        sql.WHERE("id = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_org
     *
     * @mbg.generated Thu May 03 10:11:11 CST 2018
     */
    protected void applyWhere(SQL sql, CscpUserOrgExample example, boolean includeExamplePhrase) {
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
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and" +
                    " #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2Th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and " +
                    "#{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
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

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_user_org
     *
     * @mbg.generated Thu May 03 10:11:11 CST 2018
     */
    public String selectByExampleWithPage(CscpUserOrgExample example) {
        String sql = selectByExample(example);
        /*Pageable page = example.getPage();
        if(page != null) {
            sql = String.format("%s limit %d,%d", sql, page.getOffset(), page.getPageSize());
        }*/
        return sql;
    }
}