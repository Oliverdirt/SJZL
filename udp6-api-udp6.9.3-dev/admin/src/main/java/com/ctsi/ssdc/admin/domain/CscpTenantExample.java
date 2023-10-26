package com.ctsi.ssdc.admin.domain;

import com.ctsi.ssdc.criteria.LongCriteria;
import com.ctsi.ssdc.criteria.StringCriteria;
import com.ctsi.ssdc.criteria.ZonedDateTimeCriteria;
import com.ctsi.ssdc.example.BaseExample;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ctsi-biyi-generator
*/
public class CscpTenantExample extends BaseExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    protected List<Criteria> oredCriteria;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    private static final String LIKE_FORMAT = "%%%s%%";

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_tenant.id
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    private LongCriteria id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_tenant.tenant_account
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    private StringCriteria tenantAccount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_tenant.tenant_name
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    private StringCriteria tenantName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_tenant.contacts
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    private StringCriteria contacts;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_tenant.phone
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    private StringCriteria phone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_tenant.expire_time
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    private ZonedDateTimeCriteria expireTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cscp_tenant.active_flag
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    private StringCriteria activeFlag;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public CscpTenantExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    @Override
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public LongCriteria getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public void setId(LongCriteria id) {
        this. id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
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
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public StringCriteria getTenantAccount() {
        return tenantAccount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public void setTenantAccount(StringCriteria tenantAccount) {
        this. tenantAccount = tenantAccount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    private void buildTenantAccountCriteria(Criteria criteria) {
        if (this.getTenantAccount() == null) {
            return;
        }
        if (this.getTenantAccount().getEquals() != null) {
            criteria.andTenantAccountEqualTo(this.getTenantAccount().getEquals());
        } else {
            if (StringUtils.isNotBlank(this.getTenantAccount().getContains())) {
                criteria.andTenantAccountLike(String.format(LIKE_FORMAT, this.getTenantAccount().getContains()));
            }
        }
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public StringCriteria getTenantName() {
        return tenantName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public void setTenantName(StringCriteria tenantName) {
        this. tenantName = tenantName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    private void buildTenantNameCriteria(Criteria criteria) {
        if (this.getTenantName() == null) {
            return;
        }
        if (this.getTenantName().getEquals() != null) {
            criteria.andTenantNameEqualTo(this.getTenantName().getEquals());
        } else {
            if (StringUtils.isNotBlank(this.getTenantName().getContains())) {
                criteria.andTenantNameLike(String.format(LIKE_FORMAT, this.getTenantName().getContains()));
            }
        }
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public StringCriteria getContacts() {
        return contacts;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public void setContacts(StringCriteria contacts) {
        this. contacts = contacts;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    private void buildContactsCriteria(Criteria criteria) {
        if (this.getContacts() == null) {
            return;
        }
        if (this.getContacts().getEquals() != null) {
            criteria.andContactsEqualTo(this.getContacts().getEquals());
        } else {
            if (StringUtils.isNotBlank(this.getContacts().getContains())) {
                criteria.andContactsLike(String.format(LIKE_FORMAT, this.getContacts().getContains()));
            }
        }
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public StringCriteria getPhone() {
        return phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public void setPhone(StringCriteria phone) {
        this. phone = phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    private void buildPhoneCriteria(Criteria criteria) {
        if (this.getPhone() == null) {
            return;
        }
        if (this.getPhone().getEquals() != null) {
            criteria.andPhoneEqualTo(this.getPhone().getEquals());
        } else {
            if (StringUtils.isNotBlank(this.getPhone().getContains())) {
                criteria.andPhoneLike(String.format(LIKE_FORMAT, this.getPhone().getContains()));
            }
        }
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public ZonedDateTimeCriteria getExpireTime() {
        return expireTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public void setExpireTime(ZonedDateTimeCriteria expireTime) {
        this. expireTime = expireTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    private void buildExpireTimeCriteria(Criteria criteria) {
        if (this.getExpireTime() == null) {
            return;
        }
        if (this.getExpireTime().getEquals() != null) {
            criteria.andExpireTimeEqualTo(this.getExpireTime().getEquals());
        } else {
            if (this.getExpireTime().getGreaterOrEqualThan() != null) {
                criteria.andExpireTimeGreaterThanOrEqualTo(this.getExpireTime().getGreaterOrEqualThan());
            }
            if (this.getExpireTime().getLessOrEqualThan() != null) {
                criteria.andExpireTimeLessThanOrEqualTo(this.getExpireTime().getLessOrEqualThan());
            }
        }
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public StringCriteria getActiveFlag() {
        return activeFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public void setActiveFlag(StringCriteria activeFlag) {
        this. activeFlag = activeFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    private void buildActiveFlagCriteria(Criteria criteria) {
        if (this.getActiveFlag() == null) {
            return;
        }
        if (this.getActiveFlag().getEquals() != null) {
            criteria.andActiveFlagEqualTo(this.getActiveFlag().getEquals());
        } else {
            if (StringUtils.isNotBlank(this.getActiveFlag().getContains())) {
                criteria.andActiveFlagLike(String.format(LIKE_FORMAT, this.getActiveFlag().getContains()));
            }
        }
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    @Override
    public Object buildCriteria() {
        Criteria criteria = this.createCriteria();
        this.buildIdCriteria(criteria);
        this.buildTenantAccountCriteria(criteria);
        this.buildTenantNameCriteria(criteria);
        this.buildContactsCriteria(criteria);
        this.buildPhoneCriteria(criteria);
        this.buildExpireTimeCriteria(criteria);
        this.buildActiveFlagCriteria(criteria);
        return criteria;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
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

        public Criteria andTenantAccountIsNull() {
            addCriterion("tenant_account is null");
            return (Criteria) this;
        }

        public Criteria andTenantAccountIsNotNull() {
            addCriterion("tenant_account is not null");
            return (Criteria) this;
        }

        public Criteria andTenantAccountEqualTo(String value) {
            addCriterion("tenant_account =", value, "tenantAccount");
            return (Criteria) this;
        }

        public Criteria andTenantAccountNotEqualTo(String value) {
            addCriterion("tenant_account <>", value, "tenantAccount");
            return (Criteria) this;
        }

        public Criteria andTenantAccountGreaterThan(String value) {
            addCriterion("tenant_account >", value, "tenantAccount");
            return (Criteria) this;
        }

        public Criteria andTenantAccountGreaterThanOrEqualTo(String value) {
            addCriterion("tenant_account >=", value, "tenantAccount");
            return (Criteria) this;
        }

        public Criteria andTenantAccountLessThan(String value) {
            addCriterion("tenant_account <", value, "tenantAccount");
            return (Criteria) this;
        }

        public Criteria andTenantAccountLessThanOrEqualTo(String value) {
            addCriterion("tenant_account <=", value, "tenantAccount");
            return (Criteria) this;
        }

        public Criteria andTenantAccountLike(String value) {
            addCriterion("tenant_account like", value, "tenantAccount");
            return (Criteria) this;
        }

        public Criteria andTenantAccountNotLike(String value) {
            addCriterion("tenant_account not like", value, "tenantAccount");
            return (Criteria) this;
        }

        public Criteria andTenantAccountIn(List<String> values) {
            addCriterion("tenant_account in", values, "tenantAccount");
            return (Criteria) this;
        }

        public Criteria andTenantAccountNotIn(List<String> values) {
            addCriterion("tenant_account not in", values, "tenantAccount");
            return (Criteria) this;
        }

        public Criteria andTenantAccountBetween(String value1, String value2) {
            addCriterion("tenant_account between", value1, value2, "tenantAccount");
            return (Criteria) this;
        }

        public Criteria andTenantAccountNotBetween(String value1, String value2) {
            addCriterion("tenant_account not between", value1, value2, "tenantAccount");
            return (Criteria) this;
        }

        public Criteria andTenantNameIsNull() {
            addCriterion("tenant_name is null");
            return (Criteria) this;
        }

        public Criteria andTenantNameIsNotNull() {
            addCriterion("tenant_name is not null");
            return (Criteria) this;
        }

        public Criteria andTenantNameEqualTo(String value) {
            addCriterion("tenant_name =", value, "tenantName");
            return (Criteria) this;
        }

        public Criteria andTenantNameNotEqualTo(String value) {
            addCriterion("tenant_name <>", value, "tenantName");
            return (Criteria) this;
        }

        public Criteria andTenantNameGreaterThan(String value) {
            addCriterion("tenant_name >", value, "tenantName");
            return (Criteria) this;
        }

        public Criteria andTenantNameGreaterThanOrEqualTo(String value) {
            addCriterion("tenant_name >=", value, "tenantName");
            return (Criteria) this;
        }

        public Criteria andTenantNameLessThan(String value) {
            addCriterion("tenant_name <", value, "tenantName");
            return (Criteria) this;
        }

        public Criteria andTenantNameLessThanOrEqualTo(String value) {
            addCriterion("tenant_name <=", value, "tenantName");
            return (Criteria) this;
        }

        public Criteria andTenantNameLike(String value) {
            addCriterion("tenant_name like", value, "tenantName");
            return (Criteria) this;
        }

        public Criteria andTenantNameNotLike(String value) {
            addCriterion("tenant_name not like", value, "tenantName");
            return (Criteria) this;
        }

        public Criteria andTenantNameIn(List<String> values) {
            addCriterion("tenant_name in", values, "tenantName");
            return (Criteria) this;
        }

        public Criteria andTenantNameNotIn(List<String> values) {
            addCriterion("tenant_name not in", values, "tenantName");
            return (Criteria) this;
        }

        public Criteria andTenantNameBetween(String value1, String value2) {
            addCriterion("tenant_name between", value1, value2, "tenantName");
            return (Criteria) this;
        }

        public Criteria andTenantNameNotBetween(String value1, String value2) {
            addCriterion("tenant_name not between", value1, value2, "tenantName");
            return (Criteria) this;
        }

        public Criteria andContactsIsNull() {
            addCriterion("contacts is null");
            return (Criteria) this;
        }

        public Criteria andContactsIsNotNull() {
            addCriterion("contacts is not null");
            return (Criteria) this;
        }

        public Criteria andContactsEqualTo(String value) {
            addCriterion("contacts =", value, "contacts");
            return (Criteria) this;
        }

        public Criteria andContactsNotEqualTo(String value) {
            addCriterion("contacts <>", value, "contacts");
            return (Criteria) this;
        }

        public Criteria andContactsGreaterThan(String value) {
            addCriterion("contacts >", value, "contacts");
            return (Criteria) this;
        }

        public Criteria andContactsGreaterThanOrEqualTo(String value) {
            addCriterion("contacts >=", value, "contacts");
            return (Criteria) this;
        }

        public Criteria andContactsLessThan(String value) {
            addCriterion("contacts <", value, "contacts");
            return (Criteria) this;
        }

        public Criteria andContactsLessThanOrEqualTo(String value) {
            addCriterion("contacts <=", value, "contacts");
            return (Criteria) this;
        }

        public Criteria andContactsLike(String value) {
            addCriterion("contacts like", value, "contacts");
            return (Criteria) this;
        }

        public Criteria andContactsNotLike(String value) {
            addCriterion("contacts not like", value, "contacts");
            return (Criteria) this;
        }

        public Criteria andContactsIn(List<String> values) {
            addCriterion("contacts in", values, "contacts");
            return (Criteria) this;
        }

        public Criteria andContactsNotIn(List<String> values) {
            addCriterion("contacts not in", values, "contacts");
            return (Criteria) this;
        }

        public Criteria andContactsBetween(String value1, String value2) {
            addCriterion("contacts between", value1, value2, "contacts");
            return (Criteria) this;
        }

        public Criteria andContactsNotBetween(String value1, String value2) {
            addCriterion("contacts not between", value1, value2, "contacts");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNull() {
            addCriterion("phone is null");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNotNull() {
            addCriterion("phone is not null");
            return (Criteria) this;
        }

        public Criteria andPhoneEqualTo(String value) {
            addCriterion("phone =", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotEqualTo(String value) {
            addCriterion("phone <>", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThan(String value) {
            addCriterion("phone >", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("phone >=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThan(String value) {
            addCriterion("phone <", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThanOrEqualTo(String value) {
            addCriterion("phone <=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLike(String value) {
            addCriterion("phone like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotLike(String value) {
            addCriterion("phone not like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneIn(List<String> values) {
            addCriterion("phone in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotIn(List<String> values) {
            addCriterion("phone not in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneBetween(String value1, String value2) {
            addCriterion("phone between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotBetween(String value1, String value2) {
            addCriterion("phone not between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andExpireTimeIsNull() {
            addCriterion("expire_time is null");
            return (Criteria) this;
        }

        public Criteria andExpireTimeIsNotNull() {
            addCriterion("expire_time is not null");
            return (Criteria) this;
        }

        public Criteria andExpireTimeEqualTo(ZonedDateTime value) {
            addCriterion("expire_time =", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeNotEqualTo(ZonedDateTime value) {
            addCriterion("expire_time <>", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeGreaterThan(ZonedDateTime value) {
            addCriterion("expire_time >", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeGreaterThanOrEqualTo(ZonedDateTime value) {
            addCriterion("expire_time >=", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeLessThan(ZonedDateTime value) {
            addCriterion("expire_time <", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeLessThanOrEqualTo(ZonedDateTime value) {
            addCriterion("expire_time <=", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeIn(List<ZonedDateTime> values) {
            addCriterion("expire_time in", values, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeNotIn(List<ZonedDateTime> values) {
            addCriterion("expire_time not in", values, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeBetween(ZonedDateTime value1, ZonedDateTime value2) {
            addCriterion("expire_time between", value1, value2, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeNotBetween(ZonedDateTime value1, ZonedDateTime value2) {
            addCriterion("expire_time not between", value1, value2, "expireTime");
            return (Criteria) this;
        }

        public Criteria andActiveFlagIsNull() {
            addCriterion("active_flag is null");
            return (Criteria) this;
        }

        public Criteria andActiveFlagIsNotNull() {
            addCriterion("active_flag is not null");
            return (Criteria) this;
        }

        public Criteria andActiveFlagEqualTo(String value) {
            addCriterion("active_flag =", value, "activeFlag");
            return (Criteria) this;
        }

        public Criteria andActiveFlagNotEqualTo(String value) {
            addCriterion("active_flag <>", value, "activeFlag");
            return (Criteria) this;
        }

        public Criteria andActiveFlagGreaterThan(String value) {
            addCriterion("active_flag >", value, "activeFlag");
            return (Criteria) this;
        }

        public Criteria andActiveFlagGreaterThanOrEqualTo(String value) {
            addCriterion("active_flag >=", value, "activeFlag");
            return (Criteria) this;
        }

        public Criteria andActiveFlagLessThan(String value) {
            addCriterion("active_flag <", value, "activeFlag");
            return (Criteria) this;
        }

        public Criteria andActiveFlagLessThanOrEqualTo(String value) {
            addCriterion("active_flag <=", value, "activeFlag");
            return (Criteria) this;
        }

        public Criteria andActiveFlagLike(String value) {
            addCriterion("active_flag like", value, "activeFlag");
            return (Criteria) this;
        }

        public Criteria andActiveFlagNotLike(String value) {
            addCriterion("active_flag not like", value, "activeFlag");
            return (Criteria) this;
        }

        public Criteria andActiveFlagIn(List<String> values) {
            addCriterion("active_flag in", values, "activeFlag");
            return (Criteria) this;
        }

        public Criteria andActiveFlagNotIn(List<String> values) {
            addCriterion("active_flag not in", values, "activeFlag");
            return (Criteria) this;
        }

        public Criteria andActiveFlagBetween(String value1, String value2) {
            addCriterion("active_flag between", value1, value2, "activeFlag");
            return (Criteria) this;
        }

        public Criteria andActiveFlagNotBetween(String value1, String value2) {
            addCriterion("active_flag not between", value1, value2, "activeFlag");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table cscp_tenant
     *
     * @mbg.generated do_not_delete_during_merge Thu Jan 06 14:30:38 CST 2022
     */
    public static class Criteria extends BaseGeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table cscp_tenant
     *
     * @mbg.generated Thu Jan 06 14:30:38 CST 2022
     */
    public static class Criterion {
        @ApiParam(hidden=true)
        private String condition;

        @ApiParam(hidden=true)
        private Object value;

        @ApiParam(hidden=true)
        private Object secondValue;

        @ApiParam(hidden=true)
        private boolean noValue;

        @ApiParam(hidden=true)
        private boolean singleValue;

        @ApiParam(hidden=true)
        private boolean betweenValue;

        @ApiParam(hidden=true)
        private boolean listValue;

        @ApiParam(hidden=true)
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