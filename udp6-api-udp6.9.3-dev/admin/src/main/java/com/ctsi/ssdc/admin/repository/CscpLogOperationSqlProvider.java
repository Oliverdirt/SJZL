package com.ctsi.ssdc.admin.repository;

import com.ctsi.ssdc.admin.domain.CscpLogOperation;
import com.ctsi.ssdc.admin.domain.CscpLogOperationExample;
import com.ctsi.ssdc.admin.domain.CscpLogOperationExample.Criteria;
import com.ctsi.ssdc.admin.domain.CscpLogOperationExample.Criterion;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * @author MyBatis Generator
*/
public class CscpLogOperationSqlProvider {

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_log_operation
     *
     * @mbg.generated Thu Aug 02 11:13:47 CST 2018
     */
    public String countByExample(CscpLogOperationExample example) {
        SQL sql = new SQL();
        sql.SELECT("count(*)").FROM("cscp_log_operation");
        if(null != example && null != example.getTenantId()){
            sql.WHERE("tenant_id = " + example.getTenantId().getEquals());
        }
        applyWhere(sql, example, false);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_log_operation
     *
     * @mbg.generated Thu Aug 02 11:13:47 CST 2018
     */
    public String deleteByExample(CscpLogOperationExample example) {
        SQL sql = new SQL();
        sql.DELETE_FROM("cscp_log_operation");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_log_operation
     *
     * @mbg.generated Thu Aug 02 11:13:47 CST 2018
     */
    public String insertSelective(CscpLogOperation record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("cscp_log_operation");
        
        if (record.getUserid() != null) {
            sql.VALUES("userid", "#{userid,jdbcType=BIGINT}");
        }
        if (record.getTenantId() != null) {
            sql.VALUES("tenant_id", "#{tenantId,jdbcType=BIGINT}");
        }
        
        if (record.getUsername() != null) {
            sql.VALUES("username", "#{username,jdbcType=VARCHAR}");
        }
        
        if (record.getUri() != null) {
            sql.VALUES("uri", "#{uri,jdbcType=VARCHAR}");
        }
        
        if (record.getIp() != null) {
            sql.VALUES("ip", "#{ip,jdbcType=VARCHAR}");
        }
        
        if (record.getParams() != null) {
            sql.VALUES("params", "#{params,jdbcType=VARCHAR}");
        }
        
        if (record.getMethod() != null) {
            sql.VALUES("method", "#{method,jdbcType=VARCHAR}");
        }
        
        if (record.getMessage() != null) {
            sql.VALUES("message", "#{message,jdbcType=VARCHAR}");
        }
        
        if (record.getStatus() != null) {
            sql.VALUES("status", "#{status,jdbcType=CHAR}");
        }
        
        if (record.getTime() != null) {
            sql.VALUES("time", "#{time,jdbcType=TIMESTAMP}");
        }
        
        if (record.getError() != null) {
            sql.VALUES("error", "#{error,jdbcType=VARCHAR}");
        }
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_log_operation
     *
     * @mbg.generated Thu Aug 02 11:13:47 CST 2018
     */
    public String selectByExample(CscpLogOperationExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("id");
        } else {
            sql.SELECT("id");
        }
        sql.SELECT("userid");
        sql.SELECT("tenant_id");
        sql.SELECT("username");
        sql.SELECT("uri");
        sql.SELECT("ip");
        sql.SELECT("params");
        sql.SELECT("method");
        sql.SELECT("message");
        sql.SELECT("status");
        sql.SELECT("time");
        sql.SELECT("error");
        sql.FROM("cscp_log_operation");
        if(null != example && null != example.getTenantId()){
            sql.WHERE("tenant_id="+example.getTenantId().getEquals());
        }
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_log_operation
     *
     * @mbg.generated Thu Aug 02 11:13:47 CST 2018
     */
    public String updateByExampleSelective(Map<String, Object> parameter) {
        CscpLogOperation record = (CscpLogOperation) parameter.get("record");
        CscpLogOperationExample example = (CscpLogOperationExample) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("cscp_log_operation");
        
        if (record.getId() != null) {
            sql.SET("id = #{record.id,jdbcType=BIGINT}");
        }
        
        if (record.getUserid() != null) {
            sql.SET("userid = #{record.userid,jdbcType=BIGINT}");
        }
        if (record.getTenantId() != null) {
            sql.SET("tenant_id = #{record.tenantId,jdbcType=BIGINT}");
        }
        
        if (record.getUsername() != null) {
            sql.SET("username = #{record.username,jdbcType=VARCHAR}");
        }
        
        if (record.getUri() != null) {
            sql.SET("uri = #{record.uri,jdbcType=VARCHAR}");
        }
        
        if (record.getIp() != null) {
            sql.SET("ip = #{record.ip,jdbcType=VARCHAR}");
        }
        
        if (record.getParams() != null) {
            sql.SET("params = #{record.params,jdbcType=VARCHAR}");
        }
        
        if (record.getMethod() != null) {
            sql.SET("method = #{record.method,jdbcType=VARCHAR}");
        }
        
        if (record.getMessage() != null) {
            sql.SET("message = #{record.message,jdbcType=VARCHAR}");
        }
        
        if (record.getStatus() != null) {
            sql.SET("status = #{record.status,jdbcType=CHAR}");
        }
        
        if (record.getTime() != null) {
            sql.SET("time = #{record.time,jdbcType=TIMESTAMP}");
        }
        
        if (record.getError() != null) {
            sql.SET("error = #{record.error,jdbcType=VARCHAR}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_log_operation
     *
     * @mbg.generated Thu Aug 02 11:13:47 CST 2018
     */
    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("cscp_log_operation");
        
        sql.SET("id = #{record.id,jdbcType=BIGINT}");
        sql.SET("userid = #{record.userid,jdbcType=BIGINT}");
        sql.SET("tenant_id = #{record.tenantId,jdbcType=BIGINT}");
        sql.SET("username = #{record.username,jdbcType=VARCHAR}");
        sql.SET("uri = #{record.uri,jdbcType=VARCHAR}");
        sql.SET("ip = #{record.ip,jdbcType=VARCHAR}");
        sql.SET("params = #{record.params,jdbcType=VARCHAR}");
        sql.SET("method = #{record.method,jdbcType=VARCHAR}");
        sql.SET("message = #{record.message,jdbcType=VARCHAR}");
        sql.SET("status = #{record.status,jdbcType=CHAR}");
        sql.SET("time = #{record.time,jdbcType=TIMESTAMP}");
        sql.SET("error = #{record.error,jdbcType=VARCHAR}");
        
        CscpLogOperationExample example = (CscpLogOperationExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_log_operation
     *
     * @mbg.generated Thu Aug 02 11:13:47 CST 2018
     */
    public String updateByPrimaryKeySelective(CscpLogOperation record) {
        SQL sql = new SQL();
        sql.UPDATE("cscp_log_operation");
        
        if (record.getUserid() != null) {
            sql.SET("userid = #{userid,jdbcType=BIGINT}");
        }

        if (record.getTenantId() != null) {
            sql.SET("tenant_id = #{tenantId,jdbcType=BIGINT}");
        }
        
        if (record.getUsername() != null) {
            sql.SET("username = #{username,jdbcType=VARCHAR}");
        }
        
        if (record.getUri() != null) {
            sql.SET("uri = #{uri,jdbcType=VARCHAR}");
        }
        
        if (record.getIp() != null) {
            sql.SET("ip = #{ip,jdbcType=VARCHAR}");
        }
        
        if (record.getParams() != null) {
            sql.SET("params = #{params,jdbcType=VARCHAR}");
        }
        
        if (record.getMethod() != null) {
            sql.SET("method = #{method,jdbcType=VARCHAR}");
        }
        
        if (record.getMessage() != null) {
            sql.SET("message = #{message,jdbcType=VARCHAR}");
        }
        
        if (record.getStatus() != null) {
            sql.SET("status = #{status,jdbcType=CHAR}");
        }
        
        if (record.getTime() != null) {
            sql.SET("time = #{time,jdbcType=TIMESTAMP}");
        }
        
        if (record.getError() != null) {
            sql.SET("error = #{error,jdbcType=VARCHAR}");
        }
        
        sql.WHERE("id = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_log_operation
     *
     * @mbg.generated Thu Aug 02 11:13:47 CST 2018
     */
    protected void applyWhere(SQL sql, CscpLogOperationExample example, boolean includeExamplePhrase) {
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
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and" +
                    " #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2Th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and " +
                    "#{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3Th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1Th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and" +
                    " #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2Th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} " +
                    "and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
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
     * This method corresponds to the database table cscp_log_operation
     *
     * @mbg.generated Thu Aug 02 11:13:47 CST 2018
     */
    public String selectByExampleWithPage(CscpLogOperationExample example) {
        return selectByExample(example);
    }
}