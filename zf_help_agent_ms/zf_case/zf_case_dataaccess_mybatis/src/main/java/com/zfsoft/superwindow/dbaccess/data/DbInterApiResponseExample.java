package com.zfsoft.superwindow.dbaccess.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbInterApiResponseExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_inter_api_response
     *
     * @mbg.generated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_inter_api_response
     *
     * @mbg.generated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_inter_api_response
     *
     * @mbg.generated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api_response
     *
     * @mbg.generated
     */
    public DbInterApiResponseExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api_response
     *
     * @mbg.generated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api_response
     *
     * @mbg.generated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api_response
     *
     * @mbg.generated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api_response
     *
     * @mbg.generated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api_response
     *
     * @mbg.generated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api_response
     *
     * @mbg.generated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api_response
     *
     * @mbg.generated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api_response
     *
     * @mbg.generated
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
     * This method corresponds to the database table t_inter_api_response
     *
     * @mbg.generated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api_response
     *
     * @mbg.generated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_inter_api_response
     *
     * @mbg.generated
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

        public Criteria andOidIsNull() {
            addCriterion("OID is null");
            return (Criteria) this;
        }

        public Criteria andOidIsNotNull() {
            addCriterion("OID is not null");
            return (Criteria) this;
        }

        public Criteria andOidEqualTo(String value) {
            addCriterion("OID =", value, "oid");
            return (Criteria) this;
        }

        public Criteria andOidNotEqualTo(String value) {
            addCriterion("OID <>", value, "oid");
            return (Criteria) this;
        }

        public Criteria andOidGreaterThan(String value) {
            addCriterion("OID >", value, "oid");
            return (Criteria) this;
        }

        public Criteria andOidGreaterThanOrEqualTo(String value) {
            addCriterion("OID >=", value, "oid");
            return (Criteria) this;
        }

        public Criteria andOidLessThan(String value) {
            addCriterion("OID <", value, "oid");
            return (Criteria) this;
        }

        public Criteria andOidLessThanOrEqualTo(String value) {
            addCriterion("OID <=", value, "oid");
            return (Criteria) this;
        }

        public Criteria andOidLike(String value) {
            addCriterion("OID like", value, "oid");
            return (Criteria) this;
        }

        public Criteria andOidNotLike(String value) {
            addCriterion("OID not like", value, "oid");
            return (Criteria) this;
        }

        public Criteria andOidIn(List<String> values) {
            addCriterion("OID in", values, "oid");
            return (Criteria) this;
        }

        public Criteria andOidNotIn(List<String> values) {
            addCriterion("OID not in", values, "oid");
            return (Criteria) this;
        }

        public Criteria andOidBetween(String value1, String value2) {
            addCriterion("OID between", value1, value2, "oid");
            return (Criteria) this;
        }

        public Criteria andOidNotBetween(String value1, String value2) {
            addCriterion("OID not between", value1, value2, "oid");
            return (Criteria) this;
        }

        public Criteria andInterApiIdIsNull() {
            addCriterion("INTER_API_ID is null");
            return (Criteria) this;
        }

        public Criteria andInterApiIdIsNotNull() {
            addCriterion("INTER_API_ID is not null");
            return (Criteria) this;
        }

        public Criteria andInterApiIdEqualTo(Long value) {
            addCriterion("INTER_API_ID =", value, "interApiId");
            return (Criteria) this;
        }

        public Criteria andInterApiIdNotEqualTo(Long value) {
            addCriterion("INTER_API_ID <>", value, "interApiId");
            return (Criteria) this;
        }

        public Criteria andInterApiIdGreaterThan(Long value) {
            addCriterion("INTER_API_ID >", value, "interApiId");
            return (Criteria) this;
        }

        public Criteria andInterApiIdGreaterThanOrEqualTo(Long value) {
            addCriterion("INTER_API_ID >=", value, "interApiId");
            return (Criteria) this;
        }

        public Criteria andInterApiIdLessThan(Long value) {
            addCriterion("INTER_API_ID <", value, "interApiId");
            return (Criteria) this;
        }

        public Criteria andInterApiIdLessThanOrEqualTo(Long value) {
            addCriterion("INTER_API_ID <=", value, "interApiId");
            return (Criteria) this;
        }

        public Criteria andInterApiIdIn(List<Long> values) {
            addCriterion("INTER_API_ID in", values, "interApiId");
            return (Criteria) this;
        }

        public Criteria andInterApiIdNotIn(List<Long> values) {
            addCriterion("INTER_API_ID not in", values, "interApiId");
            return (Criteria) this;
        }

        public Criteria andInterApiIdBetween(Long value1, Long value2) {
            addCriterion("INTER_API_ID between", value1, value2, "interApiId");
            return (Criteria) this;
        }

        public Criteria andInterApiIdNotBetween(Long value1, Long value2) {
            addCriterion("INTER_API_ID not between", value1, value2, "interApiId");
            return (Criteria) this;
        }

        public Criteria andResponseCodeIsNull() {
            addCriterion("RESPONSE_CODE is null");
            return (Criteria) this;
        }

        public Criteria andResponseCodeIsNotNull() {
            addCriterion("RESPONSE_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andResponseCodeEqualTo(String value) {
            addCriterion("RESPONSE_CODE =", value, "responseCode");
            return (Criteria) this;
        }

        public Criteria andResponseCodeNotEqualTo(String value) {
            addCriterion("RESPONSE_CODE <>", value, "responseCode");
            return (Criteria) this;
        }

        public Criteria andResponseCodeGreaterThan(String value) {
            addCriterion("RESPONSE_CODE >", value, "responseCode");
            return (Criteria) this;
        }

        public Criteria andResponseCodeGreaterThanOrEqualTo(String value) {
            addCriterion("RESPONSE_CODE >=", value, "responseCode");
            return (Criteria) this;
        }

        public Criteria andResponseCodeLessThan(String value) {
            addCriterion("RESPONSE_CODE <", value, "responseCode");
            return (Criteria) this;
        }

        public Criteria andResponseCodeLessThanOrEqualTo(String value) {
            addCriterion("RESPONSE_CODE <=", value, "responseCode");
            return (Criteria) this;
        }

        public Criteria andResponseCodeLike(String value) {
            addCriterion("RESPONSE_CODE like", value, "responseCode");
            return (Criteria) this;
        }

        public Criteria andResponseCodeNotLike(String value) {
            addCriterion("RESPONSE_CODE not like", value, "responseCode");
            return (Criteria) this;
        }

        public Criteria andResponseCodeIn(List<String> values) {
            addCriterion("RESPONSE_CODE in", values, "responseCode");
            return (Criteria) this;
        }

        public Criteria andResponseCodeNotIn(List<String> values) {
            addCriterion("RESPONSE_CODE not in", values, "responseCode");
            return (Criteria) this;
        }

        public Criteria andResponseCodeBetween(String value1, String value2) {
            addCriterion("RESPONSE_CODE between", value1, value2, "responseCode");
            return (Criteria) this;
        }

        public Criteria andResponseCodeNotBetween(String value1, String value2) {
            addCriterion("RESPONSE_CODE not between", value1, value2, "responseCode");
            return (Criteria) this;
        }

        public Criteria andResponseNameIsNull() {
            addCriterion("RESPONSE_NAME is null");
            return (Criteria) this;
        }

        public Criteria andResponseNameIsNotNull() {
            addCriterion("RESPONSE_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andResponseNameEqualTo(String value) {
            addCriterion("RESPONSE_NAME =", value, "responseName");
            return (Criteria) this;
        }

        public Criteria andResponseNameNotEqualTo(String value) {
            addCriterion("RESPONSE_NAME <>", value, "responseName");
            return (Criteria) this;
        }

        public Criteria andResponseNameGreaterThan(String value) {
            addCriterion("RESPONSE_NAME >", value, "responseName");
            return (Criteria) this;
        }

        public Criteria andResponseNameGreaterThanOrEqualTo(String value) {
            addCriterion("RESPONSE_NAME >=", value, "responseName");
            return (Criteria) this;
        }

        public Criteria andResponseNameLessThan(String value) {
            addCriterion("RESPONSE_NAME <", value, "responseName");
            return (Criteria) this;
        }

        public Criteria andResponseNameLessThanOrEqualTo(String value) {
            addCriterion("RESPONSE_NAME <=", value, "responseName");
            return (Criteria) this;
        }

        public Criteria andResponseNameLike(String value) {
            addCriterion("RESPONSE_NAME like", value, "responseName");
            return (Criteria) this;
        }

        public Criteria andResponseNameNotLike(String value) {
            addCriterion("RESPONSE_NAME not like", value, "responseName");
            return (Criteria) this;
        }

        public Criteria andResponseNameIn(List<String> values) {
            addCriterion("RESPONSE_NAME in", values, "responseName");
            return (Criteria) this;
        }

        public Criteria andResponseNameNotIn(List<String> values) {
            addCriterion("RESPONSE_NAME not in", values, "responseName");
            return (Criteria) this;
        }

        public Criteria andResponseNameBetween(String value1, String value2) {
            addCriterion("RESPONSE_NAME between", value1, value2, "responseName");
            return (Criteria) this;
        }

        public Criteria andResponseNameNotBetween(String value1, String value2) {
            addCriterion("RESPONSE_NAME not between", value1, value2, "responseName");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagIsNull() {
            addCriterion("DELETE_FLAG is null");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagIsNotNull() {
            addCriterion("DELETE_FLAG is not null");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagEqualTo(Integer value) {
            addCriterion("DELETE_FLAG =", value, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagNotEqualTo(Integer value) {
            addCriterion("DELETE_FLAG <>", value, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagGreaterThan(Integer value) {
            addCriterion("DELETE_FLAG >", value, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("DELETE_FLAG >=", value, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagLessThan(Integer value) {
            addCriterion("DELETE_FLAG <", value, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagLessThanOrEqualTo(Integer value) {
            addCriterion("DELETE_FLAG <=", value, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagIn(List<Integer> values) {
            addCriterion("DELETE_FLAG in", values, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagNotIn(List<Integer> values) {
            addCriterion("DELETE_FLAG not in", values, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagBetween(Integer value1, Integer value2) {
            addCriterion("DELETE_FLAG between", value1, value2, "deleteFlag");
            return (Criteria) this;
        }

        public Criteria andDeleteFlagNotBetween(Integer value1, Integer value2) {
            addCriterion("DELETE_FLAG not between", value1, value2, "deleteFlag");
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
     * This class corresponds to the database table t_inter_api_response
     *
     * @mbg.generated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_inter_api_response
     *
     * @mbg.generated
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