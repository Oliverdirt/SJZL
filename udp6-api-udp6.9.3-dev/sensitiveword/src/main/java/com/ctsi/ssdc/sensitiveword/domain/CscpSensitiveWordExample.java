package com.ctsi.ssdc.sensitiveword.domain;

import com.ctsi.ssdc.criteria.*;
import java.time.ZonedDateTime;
import com.ctsi.ssdc.example.BaseExample;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * CscpSensitiveWordExample
 *
 * @author hx
 * @date 2022-09-01 14:24:45
 */

public class CscpSensitiveWordExample extends BaseExample {

    /**
     * This field was generated by  Generator.
     * This field corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    protected String orderByClause;

    /**
     * This field was generated by  Generator.
     * This field corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    protected boolean distinct;

    /**
     * This field was generated by  Generator.
     * This field corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    protected List<Criteria> oredCriteria;

    /**
     * This field was generated by  Generator.
     * This field corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    private static final String LIKE_FORMAT = "%%%s%%";


    /**
     *
     * This field was generated by  Generator.
     * This field corresponds to the database column cscp_sensitive_word.id
     *
     * @date 2022-09-01 14:24:45
     */
    private LongCriteria id;

    /**
     *
     * This field was generated by  Generator.
     * This field corresponds to the database column cscp_sensitive_word.sen_type_id
     *
     * @date 2022-09-01 14:24:45
     */
    private LongCriteria senTypeId;

    /**
     *
     * This field was generated by  Generator.
     * This field corresponds to the database column cscp_sensitive_word.sen_content
     *
     * @date 2022-09-01 14:24:45
     */
    private StringCriteria senContent;

    /**
     *
     * This field was generated by  Generator.
     * This field corresponds to the database column cscp_sensitive_word.sen_replace
     *
     * @date 2022-09-01 14:24:45
     */
    private StringCriteria senReplace;

    /**
     *
     * This field was generated by  Generator.
     * This field corresponds to the database column cscp_sensitive_word.update_time
     *
     * @date 2022-09-01 14:24:45
     */
    private ZonedDateTimeCriteria updateTime;


    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public CscpSensitiveWordExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    @Override
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }


    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public LongCriteria getId() {
        return id;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public void setId(LongCriteria id) {
        this. id = id;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    private void buildIdCriteria(Criteria criteria) {
        if (this.getId() == null) {
            return;
        }
        if (this.getId().getEquals() != null) {
            criteria.andIdEqualTo(this.getId().getEquals());
        } else {
            if (this.getId().getGreaterOrEqualThan() != null) {
                criteria.andIdGreaterThanOrEqualTo(this.getId().getGreaterOrEqualThan());
            }
            if (this.getId().getLessOrEqualThan() != null) {
                criteria.andIdLessThanOrEqualTo(this.getId().getLessOrEqualThan());
            }
        }
    }
    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public LongCriteria getSenTypeId() {
        return senTypeId;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public void setSenTypeId(LongCriteria senTypeId) {
        this. senTypeId = senTypeId;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    private void buildSenTypeIdCriteria(Criteria criteria) {
        if (this.getSenTypeId() == null) {
            return;
        }
        if (this.getSenTypeId().getEquals() != null) {
            criteria.andSenTypeIdEqualTo(this.getSenTypeId().getEquals());
        } else {
            if (this.getSenTypeId().getGreaterOrEqualThan() != null) {
                criteria.andSenTypeIdGreaterThanOrEqualTo(this.getSenTypeId().getGreaterOrEqualThan());
            }
            if (this.getSenTypeId().getLessOrEqualThan() != null) {
                criteria.andSenTypeIdLessThanOrEqualTo(this.getSenTypeId().getLessOrEqualThan());
            }
        }
    }
    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public StringCriteria getSenContent() {
        return senContent;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public void setSenContent(StringCriteria senContent) {
        this. senContent = senContent;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    private void buildSenContentCriteria(Criteria criteria) {
        if (this.getSenContent() == null) {
            return;
        }
        if (this.getSenContent().getEquals() != null) {
            criteria.andSenContentEqualTo(this.getSenContent().getEquals());
        } else {
            if (StringUtils.isNotBlank(this.getSenContent().getContains())) {
                criteria.andSenContentLike(String.format(LIKE_FORMAT, this.getSenContent().getContains()));
            }
        }
    }
    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public StringCriteria getSenReplace() {
        return senReplace;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public void setSenReplace(StringCriteria senReplace) {
        this. senReplace = senReplace;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    private void buildSenReplaceCriteria(Criteria criteria) {
        if (this.getSenReplace() == null) {
            return;
        }
        if (this.getSenReplace().getEquals() != null) {
            criteria.andSenReplaceEqualTo(this.getSenReplace().getEquals());
        } else {
            if (StringUtils.isNotBlank(this.getSenReplace().getContains())) {
                criteria.andSenReplaceLike(String.format(LIKE_FORMAT, this.getSenReplace().getContains()));
            }
        }
    }
    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public ZonedDateTimeCriteria getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public void setUpdateTime(ZonedDateTimeCriteria updateTime) {
        this. updateTime = updateTime;
    }

    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    private void buildUpdateTimeCriteria(Criteria criteria) {
        if (this.getUpdateTime() == null) {
            return;
        }
        if (this.getUpdateTime().getEquals() != null) {
            criteria.andUpdateTimeEqualTo(this.getUpdateTime().getEquals());
        } else {
            if (this.getUpdateTime().getGreaterOrEqualThan() != null) {
                criteria.andUpdateTimeGreaterThanOrEqualTo(this.getUpdateTime().getGreaterOrEqualThan());
            }
            if (this.getUpdateTime().getLessOrEqualThan() != null) {
                criteria.andUpdateTimeLessThanOrEqualTo(this.getUpdateTime().getLessOrEqualThan());
            }
        }
    }
    /**
     * This method was generated by  Generator.
     * This method corresponds to the database table cscp_hx_form_column
     *
     * @date Thu Feb 17 14:02:06 CST 2022
     */
    @Override
    public Object buildCriteria() {
        Criteria criteria = this.createCriteria();
        this.buildIdCriteria(criteria);
        this.buildSenTypeIdCriteria(criteria);
        this.buildSenContentCriteria(criteria);
        this.buildSenReplaceCriteria(criteria);
        this.buildUpdateTimeCriteria(criteria);
        return criteria;
    }

    protected abstract static class BaseGeneratedCriteria {
        protected List<Criterion> criteria;

        protected BaseGeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }


        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andSenTypeIdIsNull() {
            addCriterion("sen_type_id is null");
            return (Criteria) this;
        }

        public Criteria andSenTypeIdIsNotNull() {
            addCriterion("sen_type_id is not null");
            return (Criteria) this;
        }

        public Criteria andSenTypeIdEqualTo(Long value) {
            addCriterion("sen_type_id =", value, "senTypeId");
            return (Criteria) this;
        }

        public Criteria andSenTypeIdNotEqualTo(Long value) {
            addCriterion("sen_type_id <>", value, "senTypeId");
            return (Criteria) this;
        }

        public Criteria andSenTypeIdGreaterThan(Long value) {
            addCriterion("sen_type_id >", value, "senTypeId");
            return (Criteria) this;
        }

        public Criteria andSenTypeIdGreaterThanOrEqualTo(Long value) {
            addCriterion("sen_type_id >=", value, "senTypeId");
            return (Criteria) this;
        }

        public Criteria andSenTypeIdLessThan(Long value) {
            addCriterion("sen_type_id <", value, "senTypeId");
            return (Criteria) this;
        }

        public Criteria andSenTypeIdLessThanOrEqualTo(Long value) {
            addCriterion("sen_type_id <=", value, "senTypeId");
            return (Criteria) this;
        }

        public Criteria andSenTypeIdIn(List<Long> values) {
            addCriterion("sen_type_id in", values, "senTypeId");
            return (Criteria) this;
        }

        public Criteria andSenTypeIdNotIn(List<Long> values) {
            addCriterion("sen_type_id not in", values, "senTypeId");
            return (Criteria) this;
        }

        public Criteria andSenTypeIdBetween(Long value1, Long value2) {
            addCriterion("sen_type_id between", value1, value2, "senTypeId");
            return (Criteria) this;
        }

        public Criteria andSenTypeIdNotBetween(Long value1, Long value2) {
            addCriterion("sen_type_id not between", value1, value2, "senTypeId");
            return (Criteria) this;
        }

        public Criteria andSenContentIsNull() {
            addCriterion("sen_content is null");
            return (Criteria) this;
        }

        public Criteria andSenContentIsNotNull() {
            addCriterion("sen_content is not null");
            return (Criteria) this;
        }

        public Criteria andSenContentEqualTo(String value) {
            addCriterion("sen_content =", value, "senContent");
            return (Criteria) this;
        }

        public Criteria andSenContentNotEqualTo(String value) {
            addCriterion("sen_content <>", value, "senContent");
            return (Criteria) this;
        }

        public Criteria andSenContentGreaterThan(String value) {
            addCriterion("sen_content >", value, "senContent");
            return (Criteria) this;
        }

        public Criteria andSenContentGreaterThanOrEqualTo(String value) {
            addCriterion("sen_content >=", value, "senContent");
            return (Criteria) this;
        }

        public Criteria andSenContentLessThan(String value) {
            addCriterion("sen_content <", value, "senContent");
            return (Criteria) this;
        }

        public Criteria andSenContentLessThanOrEqualTo(String value) {
            addCriterion("sen_content <=", value, "senContent");
            return (Criteria) this;
        }

        public Criteria andSenContentLike(String value) {
            addCriterion("sen_content like", value, "senContent");
            return (Criteria) this;
        }

        public Criteria andSenContentNotLike(String value) {
            addCriterion("sen_content not like", value, "senContent");
            return (Criteria) this;
        }

        public Criteria andSenContentIn(List<String> values) {
            addCriterion("sen_content in", values, "senContent");
            return (Criteria) this;
        }

        public Criteria andSenContentNotIn(List<String> values) {
            addCriterion("sen_content not in", values, "senContent");
            return (Criteria) this;
        }

        public Criteria andSenContentBetween(String value1, String value2) {
            addCriterion("sen_content between", value1, value2, "senContent");
            return (Criteria) this;
        }

        public Criteria andSenContentNotBetween(String value1, String value2) {
            addCriterion("sen_content not between", value1, value2, "senContent");
            return (Criteria) this;
        }

        public Criteria andSenReplaceIsNull() {
            addCriterion("sen_replace is null");
            return (Criteria) this;
        }

        public Criteria andSenReplaceIsNotNull() {
            addCriterion("sen_replace is not null");
            return (Criteria) this;
        }

        public Criteria andSenReplaceEqualTo(String value) {
            addCriterion("sen_replace =", value, "senReplace");
            return (Criteria) this;
        }

        public Criteria andSenReplaceNotEqualTo(String value) {
            addCriterion("sen_replace <>", value, "senReplace");
            return (Criteria) this;
        }

        public Criteria andSenReplaceGreaterThan(String value) {
            addCriterion("sen_replace >", value, "senReplace");
            return (Criteria) this;
        }

        public Criteria andSenReplaceGreaterThanOrEqualTo(String value) {
            addCriterion("sen_replace >=", value, "senReplace");
            return (Criteria) this;
        }

        public Criteria andSenReplaceLessThan(String value) {
            addCriterion("sen_replace <", value, "senReplace");
            return (Criteria) this;
        }

        public Criteria andSenReplaceLessThanOrEqualTo(String value) {
            addCriterion("sen_replace <=", value, "senReplace");
            return (Criteria) this;
        }

        public Criteria andSenReplaceLike(String value) {
            addCriterion("sen_replace like", value, "senReplace");
            return (Criteria) this;
        }

        public Criteria andSenReplaceNotLike(String value) {
            addCriterion("sen_replace not like", value, "senReplace");
            return (Criteria) this;
        }

        public Criteria andSenReplaceIn(List<String> values) {
            addCriterion("sen_replace in", values, "senReplace");
            return (Criteria) this;
        }

        public Criteria andSenReplaceNotIn(List<String> values) {
            addCriterion("sen_replace not in", values, "senReplace");
            return (Criteria) this;
        }

        public Criteria andSenReplaceBetween(String value1, String value2) {
            addCriterion("sen_replace between", value1, value2, "senReplace");
            return (Criteria) this;
        }

        public Criteria andSenReplaceNotBetween(String value1, String value2) {
            addCriterion("sen_replace not between", value1, value2, "senReplace");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(ZonedDateTime value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(ZonedDateTime value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(ZonedDateTime value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(ZonedDateTime value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(ZonedDateTime value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(ZonedDateTime value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<ZonedDateTime> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<ZonedDateTime> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(ZonedDateTime value1, ZonedDateTime value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(ZonedDateTime value1, ZonedDateTime value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by  Generator.
     * This class corresponds to the database table  cscp_sensitive_word
     *
     * @date do_not_delete_during_merge 2022-09-01 14:24:45
     */
    public static class Criteria extends BaseGeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by  Generator.
     * This class corresponds to the database table  cscp_sensitive_word
     *
     * @date 2022-09-01 14:24:45
     */
    public static class Criterion {
        @ApiParam(hidden = true)
        private String condition;

        @ApiParam(hidden = true)
        private Object value;

        @ApiParam(hidden = true)
        private Object secondValue;

        @ApiParam(hidden = true)
        private boolean noValue;

        @ApiParam(hidden = true)
        private boolean singleValue;

        @ApiParam(hidden = true)
        private boolean betweenValue;

        @ApiParam(hidden = true)
        private boolean listValue;

        @ApiParam(hidden = true)
        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }

    }
}
