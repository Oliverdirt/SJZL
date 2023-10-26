package com.ctsi.ssdc.customize.repository;

import com.ctsi.ssdc.customize.domain.CscpCustomizeModule;
import com.ctsi.ssdc.customize.domain.CscpCustomizeModuleExample.Criteria;
import com.ctsi.ssdc.customize.domain.CscpCustomizeModuleExample.Criterion;
import com.ctsi.ssdc.customize.domain.CscpCustomizeModuleExample;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;


/**
 * CscpCustomizeModuleSqlProvider
 *
 * @author hx
 * @date 2022-08-31 09:44:42
*/

public class CscpCustomizeModuleSqlProvider {

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_customize_module
     */
    public String countByExample(CscpCustomizeModuleExample example) {
        SQL sql = new SQL();
        sql.SELECT("count(*)").FROM("cscp_customize_module");
        applyWhere(sql, example, false);
        return sql.toString();
    }


    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_customize_module
     */
    public String deleteByExample(CscpCustomizeModuleExample example) {
        SQL sql = new SQL();
        sql.DELETE_FROM("cscp_customize_module");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_customize_module
     */
    public String insertSelective(CscpCustomizeModule record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("cscp_customize_module");

        if (record.getModuleId() != null) {
            sql.VALUES("module_id", "#{moduleId,jdbcType=BIGINT}");
        }
        if (record.getModuleName() != null) {
            sql.VALUES("module_name", "#{moduleName,jdbcType=VARCHAR}");
        }
        if (record.getModuleIdentify() != null) {
            sql.VALUES("module_identify", "#{moduleIdentify,jdbcType=VARCHAR}");
        }
        if (record.getCreateTime() != null) {
            sql.VALUES("create_time", "#{createTime,jdbcType=DATE}");
        }
        if (record.getCreatedBy() != null) {
            sql.VALUES("created_by", "#{createdBy,jdbcType=BIGINT}");
        }
        if (record.getUpdateTime() != null) {
            sql.VALUES("update_time", "#{updateTime,jdbcType=DATE}");
        }
        if (record.getUpdateBy() != null) {
            sql.VALUES("update_by", "#{updateBy,jdbcType=BIGINT}");
        }
        if (record.getTenantId() != null) {
            sql.VALUES("tenant_id", "#{tenantId,jdbcType=BIGINT}");
        }

        return sql.toString();
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_customize_module
     */
    public String selectByExample(CscpCustomizeModuleExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("module_id");
        } else {
            sql.SELECT("module_id");
        }
        sql.SELECT("module_name");
        sql.SELECT("module_identify");
        sql.SELECT("create_time");
        sql.SELECT("created_by");
        sql.SELECT("update_time");
        sql.SELECT("update_by");
        sql.SELECT("tenant_id");
        sql.FROM("cscp_customize_module");
        applyWhere(sql, example, false);

        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }

        return sql.toString();
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_customize_module
     */
    public String updateByExampleSelective(Map<String, Object> parameter) {
        CscpCustomizeModule record = (CscpCustomizeModule) parameter.get("record");
        CscpCustomizeModuleExample example = (CscpCustomizeModuleExample) parameter.get("example");

        SQL sql = new SQL();
        sql.UPDATE("cscp_customize_module");

        if (record.getModuleId() != null) {
        sql.SET("module_id = #{moduleId,jdbcType=BIGINT}");
        }
        if (record.getModuleName() != null) {
        sql.SET("module_name = #{moduleName,jdbcType=VARCHAR}");
        }
        if (record.getModuleIdentify() != null) {
        sql.SET("module_identify = #{moduleIdentify,jdbcType=VARCHAR}");
        }
        if (record.getCreateTime() != null) {
        sql.SET("create_time = #{createTime,jdbcType=DATE}");
        }
        if (record.getCreatedBy() != null) {
        sql.SET("created_by = #{createdBy,jdbcType=BIGINT}");
        }
        if (record.getUpdateTime() != null) {
        sql.SET("update_time = #{updateTime,jdbcType=DATE}");
        }
        if (record.getUpdateBy() != null) {
        sql.SET("update_by = #{updateBy,jdbcType=BIGINT}");
        }
        if (record.getTenantId() != null) {
        sql.SET("tenant_id = #{tenantId,jdbcType=BIGINT}");
        }
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_customize_module
     */
    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("cscp_customize_module");
        sql.SET("module_id = #{moduleId,jdbcType=BIGINT}");
        sql.SET("module_name = #{moduleName,jdbcType=VARCHAR}");
        sql.SET("module_identify = #{moduleIdentify,jdbcType=VARCHAR}");
        sql.SET("create_time = #{createTime,jdbcType=DATE}");
        sql.SET("created_by = #{createdBy,jdbcType=BIGINT}");
        sql.SET("update_time = #{updateTime,jdbcType=DATE}");
        sql.SET("update_by = #{updateBy,jdbcType=BIGINT}");
        sql.SET("tenant_id = #{tenantId,jdbcType=BIGINT}");

        CscpCustomizeModuleExample example = (CscpCustomizeModuleExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_customize_module
     */
    public String updateByPrimaryKeySelective(CscpCustomizeModule record) {
        SQL sql = new SQL();
        sql.UPDATE("cscp_customize_module");

        if (record.getModuleName() != null) {
            sql.SET("module_name = #{moduleName,jdbcType=VARCHAR}");
        }
        if (record.getModuleIdentify() != null) {
            sql.SET("module_identify = #{moduleIdentify,jdbcType=VARCHAR}");
        }
        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{createTime,jdbcType=DATE}");
        }
        if (record.getCreatedBy() != null) {
            sql.SET("created_by = #{createdBy,jdbcType=BIGINT}");
        }
        if (record.getUpdateTime() != null) {
            sql.SET("update_time = #{updateTime,jdbcType=DATE}");
        }
        if (record.getUpdateBy() != null) {
            sql.SET("update_by = #{updateBy,jdbcType=BIGINT}");
        }
        if (record.getTenantId() != null) {
            sql.SET("tenant_id = #{tenantId,jdbcType=BIGINT}");
        }
        sql.WHERE("module_id = #{moduleId,jdbcType=BIGINT}");

        return sql.toString();
    }


    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_customize_module
     */
    protected void applyWhere(SQL sql, CscpCustomizeModuleExample example, boolean includeExamplePhrase) {
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
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2Th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3Th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1Th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2Th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
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
                            sb.append(String.format(parmPhrase1Th, criterion.getCondition(), i, j,criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2Th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
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
