package com.zfsoft.microservice.form.dbaccess.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbFormComponentExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_form_component
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_form_component
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_form_component
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_component
     *
     * @mbggenerated
     */
    public DbFormComponentExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_component
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_component
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_component
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_component
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_component
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_component
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_component
     *
     * @mbggenerated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_component
     *
     * @mbggenerated
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
     * This method corresponds to the database table t_form_component
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_component
     *
     * @mbggenerated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_form_component
     *
     * @mbggenerated
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
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
            addCriterion("ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andComponentOidIsNull() {
            addCriterion("COMPONENT_OID is null");
            return (Criteria) this;
        }

        public Criteria andComponentOidIsNotNull() {
            addCriterion("COMPONENT_OID is not null");
            return (Criteria) this;
        }

        public Criteria andComponentOidEqualTo(String value) {
            addCriterion("COMPONENT_OID =", value, "componentOid");
            return (Criteria) this;
        }

        public Criteria andComponentOidNotEqualTo(String value) {
            addCriterion("COMPONENT_OID <>", value, "componentOid");
            return (Criteria) this;
        }

        public Criteria andComponentOidGreaterThan(String value) {
            addCriterion("COMPONENT_OID >", value, "componentOid");
            return (Criteria) this;
        }

        public Criteria andComponentOidGreaterThanOrEqualTo(String value) {
            addCriterion("COMPONENT_OID >=", value, "componentOid");
            return (Criteria) this;
        }

        public Criteria andComponentOidLessThan(String value) {
            addCriterion("COMPONENT_OID <", value, "componentOid");
            return (Criteria) this;
        }

        public Criteria andComponentOidLessThanOrEqualTo(String value) {
            addCriterion("COMPONENT_OID <=", value, "componentOid");
            return (Criteria) this;
        }

        public Criteria andComponentOidLike(String value) {
            addCriterion("COMPONENT_OID like", value, "componentOid");
            return (Criteria) this;
        }

        public Criteria andComponentOidNotLike(String value) {
            addCriterion("COMPONENT_OID not like", value, "componentOid");
            return (Criteria) this;
        }

        public Criteria andComponentOidIn(List<String> values) {
            addCriterion("COMPONENT_OID in", values, "componentOid");
            return (Criteria) this;
        }

        public Criteria andComponentOidNotIn(List<String> values) {
            addCriterion("COMPONENT_OID not in", values, "componentOid");
            return (Criteria) this;
        }

        public Criteria andComponentOidBetween(String value1, String value2) {
            addCriterion("COMPONENT_OID between", value1, value2, "componentOid");
            return (Criteria) this;
        }

        public Criteria andComponentOidNotBetween(String value1, String value2) {
            addCriterion("COMPONENT_OID not between", value1, value2, "componentOid");
            return (Criteria) this;
        }

        public Criteria andComponentCodeIsNull() {
            addCriterion("COMPONENT_CODE is null");
            return (Criteria) this;
        }

        public Criteria andComponentCodeIsNotNull() {
            addCriterion("COMPONENT_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andComponentCodeEqualTo(String value) {
            addCriterion("COMPONENT_CODE =", value, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNotEqualTo(String value) {
            addCriterion("COMPONENT_CODE <>", value, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeGreaterThan(String value) {
            addCriterion("COMPONENT_CODE >", value, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeGreaterThanOrEqualTo(String value) {
            addCriterion("COMPONENT_CODE >=", value, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeLessThan(String value) {
            addCriterion("COMPONENT_CODE <", value, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeLessThanOrEqualTo(String value) {
            addCriterion("COMPONENT_CODE <=", value, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeLike(String value) {
            addCriterion("COMPONENT_CODE like", value, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNotLike(String value) {
            addCriterion("COMPONENT_CODE not like", value, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeIn(List<String> values) {
            addCriterion("COMPONENT_CODE in", values, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNotIn(List<String> values) {
            addCriterion("COMPONENT_CODE not in", values, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeBetween(String value1, String value2) {
            addCriterion("COMPONENT_CODE between", value1, value2, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNotBetween(String value1, String value2) {
            addCriterion("COMPONENT_CODE not between", value1, value2, "componentCode");
            return (Criteria) this;
        }

        public Criteria andAuthorizeKeyIsNull() {
            addCriterion("AUTHORIZE_KEY is null");
            return (Criteria) this;
        }

        public Criteria andAuthorizeKeyIsNotNull() {
            addCriterion("AUTHORIZE_KEY is not null");
            return (Criteria) this;
        }

        public Criteria andAuthorizeKeyEqualTo(String value) {
            addCriterion("AUTHORIZE_KEY =", value, "authorizeKey");
            return (Criteria) this;
        }

        public Criteria andAuthorizeKeyNotEqualTo(String value) {
            addCriterion("AUTHORIZE_KEY <>", value, "authorizeKey");
            return (Criteria) this;
        }

        public Criteria andAuthorizeKeyGreaterThan(String value) {
            addCriterion("AUTHORIZE_KEY >", value, "authorizeKey");
            return (Criteria) this;
        }

        public Criteria andAuthorizeKeyGreaterThanOrEqualTo(String value) {
            addCriterion("AUTHORIZE_KEY >=", value, "authorizeKey");
            return (Criteria) this;
        }

        public Criteria andAuthorizeKeyLessThan(String value) {
            addCriterion("AUTHORIZE_KEY <", value, "authorizeKey");
            return (Criteria) this;
        }

        public Criteria andAuthorizeKeyLessThanOrEqualTo(String value) {
            addCriterion("AUTHORIZE_KEY <=", value, "authorizeKey");
            return (Criteria) this;
        }

        public Criteria andAuthorizeKeyLike(String value) {
            addCriterion("AUTHORIZE_KEY like", value, "authorizeKey");
            return (Criteria) this;
        }

        public Criteria andAuthorizeKeyNotLike(String value) {
            addCriterion("AUTHORIZE_KEY not like", value, "authorizeKey");
            return (Criteria) this;
        }

        public Criteria andAuthorizeKeyIn(List<String> values) {
            addCriterion("AUTHORIZE_KEY in", values, "authorizeKey");
            return (Criteria) this;
        }

        public Criteria andAuthorizeKeyNotIn(List<String> values) {
            addCriterion("AUTHORIZE_KEY not in", values, "authorizeKey");
            return (Criteria) this;
        }

        public Criteria andAuthorizeKeyBetween(String value1, String value2) {
            addCriterion("AUTHORIZE_KEY between", value1, value2, "authorizeKey");
            return (Criteria) this;
        }

        public Criteria andAuthorizeKeyNotBetween(String value1, String value2) {
            addCriterion("AUTHORIZE_KEY not between", value1, value2, "authorizeKey");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNull() {
            addCriterion("CREATE_DATE is null");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNotNull() {
            addCriterion("CREATE_DATE is not null");
            return (Criteria) this;
        }

        public Criteria andCreateDateEqualTo(Date value) {
            addCriterion("CREATE_DATE =", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotEqualTo(Date value) {
            addCriterion("CREATE_DATE <>", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThan(Date value) {
            addCriterion("CREATE_DATE >", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("CREATE_DATE >=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThan(Date value) {
            addCriterion("CREATE_DATE <", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThanOrEqualTo(Date value) {
            addCriterion("CREATE_DATE <=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateIn(List<Date> values) {
            addCriterion("CREATE_DATE in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotIn(List<Date> values) {
            addCriterion("CREATE_DATE not in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateBetween(Date value1, Date value2) {
            addCriterion("CREATE_DATE between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotBetween(Date value1, Date value2) {
            addCriterion("CREATE_DATE not between", value1, value2, "createDate");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_form_component
     *
     * @mbggenerated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_form_component
     *
     * @mbggenerated
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

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