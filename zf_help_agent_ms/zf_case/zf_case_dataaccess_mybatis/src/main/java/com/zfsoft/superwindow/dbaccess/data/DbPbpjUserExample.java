package com.zfsoft.superwindow.dbaccess.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbPbpjUserExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    public DbPbpjUserExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
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
     * This method corresponds to the database table t_pbpj_user
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
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
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
     * This class corresponds to the database table t_pbpj_user
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

        public Criteria andUserOidIsNull() {
            addCriterion("USER_OID is null");
            return (Criteria) this;
        }

        public Criteria andUserOidIsNotNull() {
            addCriterion("USER_OID is not null");
            return (Criteria) this;
        }

        public Criteria andUserOidEqualTo(String value) {
            addCriterion("USER_OID =", value, "userOid");
            return (Criteria) this;
        }

        public Criteria andUserOidNotEqualTo(String value) {
            addCriterion("USER_OID <>", value, "userOid");
            return (Criteria) this;
        }

        public Criteria andUserOidGreaterThan(String value) {
            addCriterion("USER_OID >", value, "userOid");
            return (Criteria) this;
        }

        public Criteria andUserOidGreaterThanOrEqualTo(String value) {
            addCriterion("USER_OID >=", value, "userOid");
            return (Criteria) this;
        }

        public Criteria andUserOidLessThan(String value) {
            addCriterion("USER_OID <", value, "userOid");
            return (Criteria) this;
        }

        public Criteria andUserOidLessThanOrEqualTo(String value) {
            addCriterion("USER_OID <=", value, "userOid");
            return (Criteria) this;
        }

        public Criteria andUserOidLike(String value) {
            addCriterion("USER_OID like", value, "userOid");
            return (Criteria) this;
        }

        public Criteria andUserOidNotLike(String value) {
            addCriterion("USER_OID not like", value, "userOid");
            return (Criteria) this;
        }

        public Criteria andUserOidIn(List<String> values) {
            addCriterion("USER_OID in", values, "userOid");
            return (Criteria) this;
        }

        public Criteria andUserOidNotIn(List<String> values) {
            addCriterion("USER_OID not in", values, "userOid");
            return (Criteria) this;
        }

        public Criteria andUserOidBetween(String value1, String value2) {
            addCriterion("USER_OID between", value1, value2, "userOid");
            return (Criteria) this;
        }

        public Criteria andUserOidNotBetween(String value1, String value2) {
            addCriterion("USER_OID not between", value1, value2, "userOid");
            return (Criteria) this;
        }

        public Criteria andAppraiseFlagIsNull() {
            addCriterion("APPRAISE_FLAG is null");
            return (Criteria) this;
        }

        public Criteria andAppraiseFlagIsNotNull() {
            addCriterion("APPRAISE_FLAG is not null");
            return (Criteria) this;
        }

        public Criteria andAppraiseFlagEqualTo(Integer value) {
            addCriterion("APPRAISE_FLAG =", value, "appraiseFlag");
            return (Criteria) this;
        }

        public Criteria andAppraiseFlagNotEqualTo(Integer value) {
            addCriterion("APPRAISE_FLAG <>", value, "appraiseFlag");
            return (Criteria) this;
        }

        public Criteria andAppraiseFlagGreaterThan(Integer value) {
            addCriterion("APPRAISE_FLAG >", value, "appraiseFlag");
            return (Criteria) this;
        }

        public Criteria andAppraiseFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("APPRAISE_FLAG >=", value, "appraiseFlag");
            return (Criteria) this;
        }

        public Criteria andAppraiseFlagLessThan(Integer value) {
            addCriterion("APPRAISE_FLAG <", value, "appraiseFlag");
            return (Criteria) this;
        }

        public Criteria andAppraiseFlagLessThanOrEqualTo(Integer value) {
            addCriterion("APPRAISE_FLAG <=", value, "appraiseFlag");
            return (Criteria) this;
        }

        public Criteria andAppraiseFlagIn(List<Integer> values) {
            addCriterion("APPRAISE_FLAG in", values, "appraiseFlag");
            return (Criteria) this;
        }

        public Criteria andAppraiseFlagNotIn(List<Integer> values) {
            addCriterion("APPRAISE_FLAG not in", values, "appraiseFlag");
            return (Criteria) this;
        }

        public Criteria andAppraiseFlagBetween(Integer value1, Integer value2) {
            addCriterion("APPRAISE_FLAG between", value1, value2, "appraiseFlag");
            return (Criteria) this;
        }

        public Criteria andAppraiseFlagNotBetween(Integer value1, Integer value2) {
            addCriterion("APPRAISE_FLAG not between", value1, value2, "appraiseFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagIsNull() {
            addCriterion("CONFIRM_FLAG is null");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagIsNotNull() {
            addCriterion("CONFIRM_FLAG is not null");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagEqualTo(Integer value) {
            addCriterion("CONFIRM_FLAG =", value, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagNotEqualTo(Integer value) {
            addCriterion("CONFIRM_FLAG <>", value, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagGreaterThan(Integer value) {
            addCriterion("CONFIRM_FLAG >", value, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("CONFIRM_FLAG >=", value, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagLessThan(Integer value) {
            addCriterion("CONFIRM_FLAG <", value, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagLessThanOrEqualTo(Integer value) {
            addCriterion("CONFIRM_FLAG <=", value, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagIn(List<Integer> values) {
            addCriterion("CONFIRM_FLAG in", values, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagNotIn(List<Integer> values) {
            addCriterion("CONFIRM_FLAG not in", values, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagBetween(Integer value1, Integer value2) {
            addCriterion("CONFIRM_FLAG between", value1, value2, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagNotBetween(Integer value1, Integer value2) {
            addCriterion("CONFIRM_FLAG not between", value1, value2, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andModifyDateIsNull() {
            addCriterion("MODIFY_DATE is null");
            return (Criteria) this;
        }

        public Criteria andModifyDateIsNotNull() {
            addCriterion("MODIFY_DATE is not null");
            return (Criteria) this;
        }

        public Criteria andModifyDateEqualTo(Date value) {
            addCriterion("MODIFY_DATE =", value, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateNotEqualTo(Date value) {
            addCriterion("MODIFY_DATE <>", value, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateGreaterThan(Date value) {
            addCriterion("MODIFY_DATE >", value, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateGreaterThanOrEqualTo(Date value) {
            addCriterion("MODIFY_DATE >=", value, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateLessThan(Date value) {
            addCriterion("MODIFY_DATE <", value, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateLessThanOrEqualTo(Date value) {
            addCriterion("MODIFY_DATE <=", value, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateIn(List<Date> values) {
            addCriterion("MODIFY_DATE in", values, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateNotIn(List<Date> values) {
            addCriterion("MODIFY_DATE not in", values, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateBetween(Date value1, Date value2) {
            addCriterion("MODIFY_DATE between", value1, value2, "modifyDate");
            return (Criteria) this;
        }

        public Criteria andModifyDateNotBetween(Date value1, Date value2) {
            addCriterion("MODIFY_DATE not between", value1, value2, "modifyDate");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_pbpj_user
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
     * This class corresponds to the database table t_pbpj_user
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