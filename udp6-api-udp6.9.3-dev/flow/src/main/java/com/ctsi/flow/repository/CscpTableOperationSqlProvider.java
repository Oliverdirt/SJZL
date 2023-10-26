package com.ctsi.flow.repository;

import com.ctsi.flow.param.model.CscpTableOperationExample;
import com.ctsi.flow.param.model.CscpTableOperation;
import com.ctsi.flow.param.model.CscpTableOperationExample.Criteria;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;


/**
 * CscpTableOperationSqlProvider
 *
 * @author hx
 * @date 2022-07-26 16:50:51
 */

public class CscpTableOperationSqlProvider {

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_table_operation
     */
    public String countByExample(CscpTableOperationExample example) {
        SQL sql = new SQL();
        sql.SELECT("count(*)").FROM("cscp_table_operation");
        applyWhere(sql, example, false);
        return sql.toString();
    }


    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_table_operation
     */
    public String deleteByExample(CscpTableOperationExample example) {
        SQL sql = new SQL();
        sql.DELETE_FROM("cscp_table_operation");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_table_operation
     */
    public String insertSelective(CscpTableOperation record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("cscp_table_operation");

        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        if (record.getFieldId() != null) {
            sql.VALUES("field_id", "#{fieldId,jdbcType=VARCHAR}");
        }
        if (record.getFormId() != null) {
            sql.VALUES("form_id", "#{formId,jdbcType=VARCHAR}");
        }
        if (record.getFieldName() != null) {
            sql.VALUES("field_name", "#{fieldName,jdbcType=VARCHAR}");
        }
        if (record.getTaskDefinitionKey() != null) {
            sql.VALUES("task_definition_Key", "#{taskDefinitionKey,jdbcType=VARCHAR}");
        }
        if (record.getPerm() != null) {
            sql.VALUES("perm", "#{perm,jdbcType=VARCHAR}");
        }

        return sql.toString();
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_table_operation
     */
    public String selectByExample(CscpTableOperationExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("id");
        } else {
            sql.SELECT("id");
        }
        sql.SELECT("field_id");
        sql.SELECT("form_id");
        sql.SELECT("field_name");
        sql.SELECT("task_definition_Key");
        sql.SELECT("perm");
        sql.FROM("cscp_table_operation");
        applyWhere(sql, example, false);

        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }

        return sql.toString();
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_table_operation
     */
    public String updateByExampleSelective(Map<String, Object> parameter) {
        CscpTableOperation record = (CscpTableOperation) parameter.get("record");
        CscpTableOperationExample example = (CscpTableOperationExample) parameter.get("example");

        SQL sql = new SQL();
        sql.UPDATE("cscp_table_operation");

        if (record.getId() != null) {
            sql.SET("id = #{id,jdbcType=BIGINT}");
        }
        if (record.getFieldId() != null) {
            sql.SET("field_id = #{fieldId,jdbcType=VARCHAR}");
        }
        if (record.getFormId() != null) {
            sql.SET("form_id = #{formId,jdbcType=VARCHAR}");
        }
        if (record.getFieldName() != null) {
            sql.SET("field_name = #{fieldName,jdbcType=VARCHAR}");
        }
        if (record.getTaskDefinitionKey() != null) {
            sql.SET("task_definition_Key = #{taskDefinitionKey,jdbcType=VARCHAR}");
        }
        if (record.getPerm() != null) {
            sql.SET("perm = #{perm,jdbcType=VARCHAR}");
        }
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_table_operation
     */
    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("cscp_table_operation");
        sql.SET("id = #{id,jdbcType=BIGINT}");
        sql.SET("field_id = #{fieldId,jdbcType=VARCHAR}");
        sql.SET("form_id = #{formId,jdbcType=VARCHAR}");
        sql.SET("field_name = #{fieldName,jdbcType=VARCHAR}");
        sql.SET("task_definition_Key = #{taskDefinitionKey,jdbcType=VARCHAR}");
        sql.SET("perm = #{perm,jdbcType=VARCHAR}");

        CscpTableOperationExample example = (CscpTableOperationExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_table_operation
     */
    public String updateByPrimaryKeySelective(CscpTableOperation record) {
        SQL sql = new SQL();
        sql.UPDATE("cscp_table_operation");

        if (record.getFieldId() != null) {
            sql.SET("field_id = #{fieldId,jdbcType=VARCHAR}");
        }
        if (record.getFormId() != null) {
            sql.SET("form_id = #{formId,jdbcType=VARCHAR}");
        }
        if (record.getFieldName() != null) {
            sql.SET("field_name = #{fieldName,jdbcType=VARCHAR}");
        }
        if (record.getTaskDefinitionKey() != null) {
            sql.SET("task_definition_Key = #{taskDefinitionKey,jdbcType=VARCHAR}");
        }
        if (record.getPerm() != null) {
            sql.SET("perm = #{perm,jdbcType=VARCHAR}");
        }
        sql.WHERE("id = #{id,jdbcType=BIGINT}");

        return sql.toString();
    }


    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_table_operation
     */
    protected void applyWhere(SQL sql, CscpTableOperationExample example, boolean includeExamplePhrase) {
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
                List<CscpTableOperationExample.Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    CscpTableOperationExample.Criterion criterion = criterions.get(j);
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
