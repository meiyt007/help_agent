package com.zfsoft.microservice.workflow.dbaccess.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbWorkflowFallbackExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_workflow_fallback
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_workflow_fallback
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_workflow_fallback
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_fallback
     *
     * @mbggenerated
     */
    public DbWorkflowFallbackExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_fallback
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_fallback
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_fallback
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_fallback
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_fallback
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_fallback
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_fallback
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
     * This method corresponds to the database table t_workflow_fallback
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
     * This method corresponds to the database table t_workflow_fallback
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_workflow_fallback
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
     * This class corresponds to the database table t_workflow_fallback
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

        public Criteria andFallbackOidIsNull() {
            addCriterion("FALLBACK_OID is null");
            return (Criteria) this;
        }

        public Criteria andFallbackOidIsNotNull() {
            addCriterion("FALLBACK_OID is not null");
            return (Criteria) this;
        }

        public Criteria andFallbackOidEqualTo(String value) {
            addCriterion("FALLBACK_OID =", value, "fallbackOid");
            return (Criteria) this;
        }

        public Criteria andFallbackOidNotEqualTo(String value) {
            addCriterion("FALLBACK_OID <>", value, "fallbackOid");
            return (Criteria) this;
        }

        public Criteria andFallbackOidGreaterThan(String value) {
            addCriterion("FALLBACK_OID >", value, "fallbackOid");
            return (Criteria) this;
        }

        public Criteria andFallbackOidGreaterThanOrEqualTo(String value) {
            addCriterion("FALLBACK_OID >=", value, "fallbackOid");
            return (Criteria) this;
        }

        public Criteria andFallbackOidLessThan(String value) {
            addCriterion("FALLBACK_OID <", value, "fallbackOid");
            return (Criteria) this;
        }

        public Criteria andFallbackOidLessThanOrEqualTo(String value) {
            addCriterion("FALLBACK_OID <=", value, "fallbackOid");
            return (Criteria) this;
        }

        public Criteria andFallbackOidLike(String value) {
            addCriterion("FALLBACK_OID like", value, "fallbackOid");
            return (Criteria) this;
        }

        public Criteria andFallbackOidNotLike(String value) {
            addCriterion("FALLBACK_OID not like", value, "fallbackOid");
            return (Criteria) this;
        }

        public Criteria andFallbackOidIn(List<String> values) {
            addCriterion("FALLBACK_OID in", values, "fallbackOid");
            return (Criteria) this;
        }

        public Criteria andFallbackOidNotIn(List<String> values) {
            addCriterion("FALLBACK_OID not in", values, "fallbackOid");
            return (Criteria) this;
        }

        public Criteria andFallbackOidBetween(String value1, String value2) {
            addCriterion("FALLBACK_OID between", value1, value2, "fallbackOid");
            return (Criteria) this;
        }

        public Criteria andFallbackOidNotBetween(String value1, String value2) {
            addCriterion("FALLBACK_OID not between", value1, value2, "fallbackOid");
            return (Criteria) this;
        }

        public Criteria andFallbackTypeIsNull() {
            addCriterion("FALLBACK_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andFallbackTypeIsNotNull() {
            addCriterion("FALLBACK_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andFallbackTypeEqualTo(Integer value) {
            addCriterion("FALLBACK_TYPE =", value, "fallbackType");
            return (Criteria) this;
        }

        public Criteria andFallbackTypeNotEqualTo(Integer value) {
            addCriterion("FALLBACK_TYPE <>", value, "fallbackType");
            return (Criteria) this;
        }

        public Criteria andFallbackTypeGreaterThan(Integer value) {
            addCriterion("FALLBACK_TYPE >", value, "fallbackType");
            return (Criteria) this;
        }

        public Criteria andFallbackTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("FALLBACK_TYPE >=", value, "fallbackType");
            return (Criteria) this;
        }

        public Criteria andFallbackTypeLessThan(Integer value) {
            addCriterion("FALLBACK_TYPE <", value, "fallbackType");
            return (Criteria) this;
        }

        public Criteria andFallbackTypeLessThanOrEqualTo(Integer value) {
            addCriterion("FALLBACK_TYPE <=", value, "fallbackType");
            return (Criteria) this;
        }

        public Criteria andFallbackTypeIn(List<Integer> values) {
            addCriterion("FALLBACK_TYPE in", values, "fallbackType");
            return (Criteria) this;
        }

        public Criteria andFallbackTypeNotIn(List<Integer> values) {
            addCriterion("FALLBACK_TYPE not in", values, "fallbackType");
            return (Criteria) this;
        }

        public Criteria andFallbackTypeBetween(Integer value1, Integer value2) {
            addCriterion("FALLBACK_TYPE between", value1, value2, "fallbackType");
            return (Criteria) this;
        }

        public Criteria andFallbackTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("FALLBACK_TYPE not between", value1, value2, "fallbackType");
            return (Criteria) this;
        }

        public Criteria andActivityIdIsNull() {
            addCriterion("ACTIVITY_ID is null");
            return (Criteria) this;
        }

        public Criteria andActivityIdIsNotNull() {
            addCriterion("ACTIVITY_ID is not null");
            return (Criteria) this;
        }

        public Criteria andActivityIdEqualTo(String value) {
            addCriterion("ACTIVITY_ID =", value, "activityId");
            return (Criteria) this;
        }

        public Criteria andActivityIdNotEqualTo(String value) {
            addCriterion("ACTIVITY_ID <>", value, "activityId");
            return (Criteria) this;
        }

        public Criteria andActivityIdGreaterThan(String value) {
            addCriterion("ACTIVITY_ID >", value, "activityId");
            return (Criteria) this;
        }

        public Criteria andActivityIdGreaterThanOrEqualTo(String value) {
            addCriterion("ACTIVITY_ID >=", value, "activityId");
            return (Criteria) this;
        }

        public Criteria andActivityIdLessThan(String value) {
            addCriterion("ACTIVITY_ID <", value, "activityId");
            return (Criteria) this;
        }

        public Criteria andActivityIdLessThanOrEqualTo(String value) {
            addCriterion("ACTIVITY_ID <=", value, "activityId");
            return (Criteria) this;
        }

        public Criteria andActivityIdLike(String value) {
            addCriterion("ACTIVITY_ID like", value, "activityId");
            return (Criteria) this;
        }

        public Criteria andActivityIdNotLike(String value) {
            addCriterion("ACTIVITY_ID not like", value, "activityId");
            return (Criteria) this;
        }

        public Criteria andActivityIdIn(List<String> values) {
            addCriterion("ACTIVITY_ID in", values, "activityId");
            return (Criteria) this;
        }

        public Criteria andActivityIdNotIn(List<String> values) {
            addCriterion("ACTIVITY_ID not in", values, "activityId");
            return (Criteria) this;
        }

        public Criteria andActivityIdBetween(String value1, String value2) {
            addCriterion("ACTIVITY_ID between", value1, value2, "activityId");
            return (Criteria) this;
        }

        public Criteria andActivityIdNotBetween(String value1, String value2) {
            addCriterion("ACTIVITY_ID not between", value1, value2, "activityId");
            return (Criteria) this;
        }

        public Criteria andStepOidIsNull() {
            addCriterion("STEP_OID is null");
            return (Criteria) this;
        }

        public Criteria andStepOidIsNotNull() {
            addCriterion("STEP_OID is not null");
            return (Criteria) this;
        }

        public Criteria andStepOidEqualTo(String value) {
            addCriterion("STEP_OID =", value, "stepOid");
            return (Criteria) this;
        }

        public Criteria andStepOidNotEqualTo(String value) {
            addCriterion("STEP_OID <>", value, "stepOid");
            return (Criteria) this;
        }

        public Criteria andStepOidGreaterThan(String value) {
            addCriterion("STEP_OID >", value, "stepOid");
            return (Criteria) this;
        }

        public Criteria andStepOidGreaterThanOrEqualTo(String value) {
            addCriterion("STEP_OID >=", value, "stepOid");
            return (Criteria) this;
        }

        public Criteria andStepOidLessThan(String value) {
            addCriterion("STEP_OID <", value, "stepOid");
            return (Criteria) this;
        }

        public Criteria andStepOidLessThanOrEqualTo(String value) {
            addCriterion("STEP_OID <=", value, "stepOid");
            return (Criteria) this;
        }

        public Criteria andStepOidLike(String value) {
            addCriterion("STEP_OID like", value, "stepOid");
            return (Criteria) this;
        }

        public Criteria andStepOidNotLike(String value) {
            addCriterion("STEP_OID not like", value, "stepOid");
            return (Criteria) this;
        }

        public Criteria andStepOidIn(List<String> values) {
            addCriterion("STEP_OID in", values, "stepOid");
            return (Criteria) this;
        }

        public Criteria andStepOidNotIn(List<String> values) {
            addCriterion("STEP_OID not in", values, "stepOid");
            return (Criteria) this;
        }

        public Criteria andStepOidBetween(String value1, String value2) {
            addCriterion("STEP_OID between", value1, value2, "stepOid");
            return (Criteria) this;
        }

        public Criteria andStepOidNotBetween(String value1, String value2) {
            addCriterion("STEP_OID not between", value1, value2, "stepOid");
            return (Criteria) this;
        }

        public Criteria andTaskIdIsNull() {
            addCriterion("TASK_ID is null");
            return (Criteria) this;
        }

        public Criteria andTaskIdIsNotNull() {
            addCriterion("TASK_ID is not null");
            return (Criteria) this;
        }

        public Criteria andTaskIdEqualTo(String value) {
            addCriterion("TASK_ID =", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotEqualTo(String value) {
            addCriterion("TASK_ID <>", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdGreaterThan(String value) {
            addCriterion("TASK_ID >", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdGreaterThanOrEqualTo(String value) {
            addCriterion("TASK_ID >=", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdLessThan(String value) {
            addCriterion("TASK_ID <", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdLessThanOrEqualTo(String value) {
            addCriterion("TASK_ID <=", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdLike(String value) {
            addCriterion("TASK_ID like", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotLike(String value) {
            addCriterion("TASK_ID not like", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdIn(List<String> values) {
            addCriterion("TASK_ID in", values, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotIn(List<String> values) {
            addCriterion("TASK_ID not in", values, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdBetween(String value1, String value2) {
            addCriterion("TASK_ID between", value1, value2, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotBetween(String value1, String value2) {
            addCriterion("TASK_ID not between", value1, value2, "taskId");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNull() {
            addCriterion("IS_DELETE is null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNotNull() {
            addCriterion("IS_DELETE is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteEqualTo(Integer value) {
            addCriterion("IS_DELETE =", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotEqualTo(Integer value) {
            addCriterion("IS_DELETE <>", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThan(Integer value) {
            addCriterion("IS_DELETE >", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThanOrEqualTo(Integer value) {
            addCriterion("IS_DELETE >=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThan(Integer value) {
            addCriterion("IS_DELETE <", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThanOrEqualTo(Integer value) {
            addCriterion("IS_DELETE <=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIn(List<Integer> values) {
            addCriterion("IS_DELETE in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotIn(List<Integer> values) {
            addCriterion("IS_DELETE not in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteBetween(Integer value1, Integer value2) {
            addCriterion("IS_DELETE between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotBetween(Integer value1, Integer value2) {
            addCriterion("IS_DELETE not between", value1, value2, "isDelete");
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
     * This class corresponds to the database table t_workflow_fallback
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
     * This class corresponds to the database table t_workflow_fallback
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
