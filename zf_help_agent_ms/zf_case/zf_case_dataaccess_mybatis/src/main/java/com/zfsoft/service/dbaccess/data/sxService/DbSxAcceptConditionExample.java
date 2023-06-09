package com.zfsoft.service.dbaccess.data.sxService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbSxAcceptConditionExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_sx_accept_condition
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_sx_accept_condition
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_sx_accept_condition
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_accept_condition
     *
     * @mbggenerated
     */
    public DbSxAcceptConditionExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_accept_condition
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_accept_condition
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_accept_condition
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_accept_condition
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_accept_condition
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_accept_condition
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_accept_condition
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
     * This method corresponds to the database table t_sx_accept_condition
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
     * This method corresponds to the database table t_sx_accept_condition
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_accept_condition
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
     * This class corresponds to the database table t_sx_accept_condition
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

        public Criteria andConditionOidIsNull() {
            addCriterion("CONDITION_OID is null");
            return (Criteria) this;
        }

        public Criteria andConditionOidIsNotNull() {
            addCriterion("CONDITION_OID is not null");
            return (Criteria) this;
        }

        public Criteria andConditionOidEqualTo(String value) {
            addCriterion("CONDITION_OID =", value, "conditionOid");
            return (Criteria) this;
        }

        public Criteria andConditionOidNotEqualTo(String value) {
            addCriterion("CONDITION_OID <>", value, "conditionOid");
            return (Criteria) this;
        }

        public Criteria andConditionOidGreaterThan(String value) {
            addCriterion("CONDITION_OID >", value, "conditionOid");
            return (Criteria) this;
        }

        public Criteria andConditionOidGreaterThanOrEqualTo(String value) {
            addCriterion("CONDITION_OID >=", value, "conditionOid");
            return (Criteria) this;
        }

        public Criteria andConditionOidLessThan(String value) {
            addCriterion("CONDITION_OID <", value, "conditionOid");
            return (Criteria) this;
        }

        public Criteria andConditionOidLessThanOrEqualTo(String value) {
            addCriterion("CONDITION_OID <=", value, "conditionOid");
            return (Criteria) this;
        }

        public Criteria andConditionOidLike(String value) {
            addCriterion("CONDITION_OID like", value, "conditionOid");
            return (Criteria) this;
        }

        public Criteria andConditionOidNotLike(String value) {
            addCriterion("CONDITION_OID not like", value, "conditionOid");
            return (Criteria) this;
        }

        public Criteria andConditionOidIn(List<String> values) {
            addCriterion("CONDITION_OID in", values, "conditionOid");
            return (Criteria) this;
        }

        public Criteria andConditionOidNotIn(List<String> values) {
            addCriterion("CONDITION_OID not in", values, "conditionOid");
            return (Criteria) this;
        }

        public Criteria andConditionOidBetween(String value1, String value2) {
            addCriterion("CONDITION_OID between", value1, value2, "conditionOid");
            return (Criteria) this;
        }

        public Criteria andConditionOidNotBetween(String value1, String value2) {
            addCriterion("CONDITION_OID not between", value1, value2, "conditionOid");
            return (Criteria) this;
        }

        public Criteria andDelFlagIsNull() {
            addCriterion("DEL_FLAG is null");
            return (Criteria) this;
        }

        public Criteria andDelFlagIsNotNull() {
            addCriterion("DEL_FLAG is not null");
            return (Criteria) this;
        }

        public Criteria andDelFlagEqualTo(Short value) {
            addCriterion("DEL_FLAG =", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotEqualTo(Short value) {
            addCriterion("DEL_FLAG <>", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagGreaterThan(Short value) {
            addCriterion("DEL_FLAG >", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagGreaterThanOrEqualTo(Short value) {
            addCriterion("DEL_FLAG >=", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagLessThan(Short value) {
            addCriterion("DEL_FLAG <", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagLessThanOrEqualTo(Short value) {
            addCriterion("DEL_FLAG <=", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagIn(List<Short> values) {
            addCriterion("DEL_FLAG in", values, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotIn(List<Short> values) {
            addCriterion("DEL_FLAG not in", values, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagBetween(Short value1, Short value2) {
            addCriterion("DEL_FLAG between", value1, value2, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotBetween(Short value1, Short value2) {
            addCriterion("DEL_FLAG not between", value1, value2, "delFlag");
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

        public Criteria andCreateUserIsNull() {
            addCriterion("CREATE_USER is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNotNull() {
            addCriterion("CREATE_USER is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserEqualTo(String value) {
            addCriterion("CREATE_USER =", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotEqualTo(String value) {
            addCriterion("CREATE_USER <>", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThan(String value) {
            addCriterion("CREATE_USER >", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThanOrEqualTo(String value) {
            addCriterion("CREATE_USER >=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThan(String value) {
            addCriterion("CREATE_USER <", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThanOrEqualTo(String value) {
            addCriterion("CREATE_USER <=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLike(String value) {
            addCriterion("CREATE_USER like", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotLike(String value) {
            addCriterion("CREATE_USER not like", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserIn(List<String> values) {
            addCriterion("CREATE_USER in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotIn(List<String> values) {
            addCriterion("CREATE_USER not in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserBetween(String value1, String value2) {
            addCriterion("CREATE_USER between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotBetween(String value1, String value2) {
            addCriterion("CREATE_USER not between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andServiceOidIsNull() {
            addCriterion("SERVICE_OID is null");
            return (Criteria) this;
        }

        public Criteria andServiceOidIsNotNull() {
            addCriterion("SERVICE_OID is not null");
            return (Criteria) this;
        }

        public Criteria andServiceOidEqualTo(String value) {
            addCriterion("SERVICE_OID =", value, "serviceOid");
            return (Criteria) this;
        }

        public Criteria andServiceOidNotEqualTo(String value) {
            addCriterion("SERVICE_OID <>", value, "serviceOid");
            return (Criteria) this;
        }

        public Criteria andServiceOidGreaterThan(String value) {
            addCriterion("SERVICE_OID >", value, "serviceOid");
            return (Criteria) this;
        }

        public Criteria andServiceOidGreaterThanOrEqualTo(String value) {
            addCriterion("SERVICE_OID >=", value, "serviceOid");
            return (Criteria) this;
        }

        public Criteria andServiceOidLessThan(String value) {
            addCriterion("SERVICE_OID <", value, "serviceOid");
            return (Criteria) this;
        }

        public Criteria andServiceOidLessThanOrEqualTo(String value) {
            addCriterion("SERVICE_OID <=", value, "serviceOid");
            return (Criteria) this;
        }

        public Criteria andServiceOidLike(String value) {
            addCriterion("SERVICE_OID like", value, "serviceOid");
            return (Criteria) this;
        }

        public Criteria andServiceOidNotLike(String value) {
            addCriterion("SERVICE_OID not like", value, "serviceOid");
            return (Criteria) this;
        }

        public Criteria andServiceOidIn(List<String> values) {
            addCriterion("SERVICE_OID in", values, "serviceOid");
            return (Criteria) this;
        }

        public Criteria andServiceOidNotIn(List<String> values) {
            addCriterion("SERVICE_OID not in", values, "serviceOid");
            return (Criteria) this;
        }

        public Criteria andServiceOidBetween(String value1, String value2) {
            addCriterion("SERVICE_OID between", value1, value2, "serviceOid");
            return (Criteria) this;
        }

        public Criteria andServiceOidNotBetween(String value1, String value2) {
            addCriterion("SERVICE_OID not between", value1, value2, "serviceOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidIsNull() {
            addCriterion("ORGAN_OID is null");
            return (Criteria) this;
        }

        public Criteria andOrganOidIsNotNull() {
            addCriterion("ORGAN_OID is not null");
            return (Criteria) this;
        }

        public Criteria andOrganOidEqualTo(String value) {
            addCriterion("ORGAN_OID =", value, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidNotEqualTo(String value) {
            addCriterion("ORGAN_OID <>", value, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidGreaterThan(String value) {
            addCriterion("ORGAN_OID >", value, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidGreaterThanOrEqualTo(String value) {
            addCriterion("ORGAN_OID >=", value, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidLessThan(String value) {
            addCriterion("ORGAN_OID <", value, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidLessThanOrEqualTo(String value) {
            addCriterion("ORGAN_OID <=", value, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidLike(String value) {
            addCriterion("ORGAN_OID like", value, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidNotLike(String value) {
            addCriterion("ORGAN_OID not like", value, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidIn(List<String> values) {
            addCriterion("ORGAN_OID in", values, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidNotIn(List<String> values) {
            addCriterion("ORGAN_OID not in", values, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidBetween(String value1, String value2) {
            addCriterion("ORGAN_OID between", value1, value2, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidNotBetween(String value1, String value2) {
            addCriterion("ORGAN_OID not between", value1, value2, "organOid");
            return (Criteria) this;
        }

        public Criteria andSortIsNull() {
            addCriterion("SORT is null");
            return (Criteria) this;
        }

        public Criteria andSortIsNotNull() {
            addCriterion("SORT is not null");
            return (Criteria) this;
        }

        public Criteria andSortEqualTo(Long value) {
            addCriterion("SORT =", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotEqualTo(Long value) {
            addCriterion("SORT <>", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortGreaterThan(Long value) {
            addCriterion("SORT >", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortGreaterThanOrEqualTo(Long value) {
            addCriterion("SORT >=", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortLessThan(Long value) {
            addCriterion("SORT <", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortLessThanOrEqualTo(Long value) {
            addCriterion("SORT <=", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortIn(List<Long> values) {
            addCriterion("SORT in", values, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotIn(List<Long> values) {
            addCriterion("SORT not in", values, "sort");
            return (Criteria) this;
        }

        public Criteria andSortBetween(Long value1, Long value2) {
            addCriterion("SORT between", value1, value2, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotBetween(Long value1, Long value2) {
            addCriterion("SORT not between", value1, value2, "sort");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_sx_accept_condition
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
     * This class corresponds to the database table t_sx_accept_condition
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