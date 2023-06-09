package com.zfsoft.ha.dbaccess.data.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbHaEvalResultExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_ha_eval_result
     *
     * @mbg.generated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_ha_eval_result
     *
     * @mbg.generated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_ha_eval_result
     *
     * @mbg.generated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result
     *
     * @mbg.generated
     */
    public DbHaEvalResultExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result
     *
     * @mbg.generated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result
     *
     * @mbg.generated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result
     *
     * @mbg.generated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result
     *
     * @mbg.generated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result
     *
     * @mbg.generated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result
     *
     * @mbg.generated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result
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
     * This method corresponds to the database table t_ha_eval_result
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
     * This method corresponds to the database table t_ha_eval_result
     *
     * @mbg.generated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result
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
     * This class corresponds to the database table t_ha_eval_result
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

        public Criteria andWorkUserIdIsNull() {
            addCriterion("WORK_USER_ID is null");
            return (Criteria) this;
        }

        public Criteria andWorkUserIdIsNotNull() {
            addCriterion("WORK_USER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andWorkUserIdEqualTo(Long value) {
            addCriterion("WORK_USER_ID =", value, "workUserId");
            return (Criteria) this;
        }

        public Criteria andWorkUserIdNotEqualTo(Long value) {
            addCriterion("WORK_USER_ID <>", value, "workUserId");
            return (Criteria) this;
        }

        public Criteria andWorkUserIdGreaterThan(Long value) {
            addCriterion("WORK_USER_ID >", value, "workUserId");
            return (Criteria) this;
        }

        public Criteria andWorkUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("WORK_USER_ID >=", value, "workUserId");
            return (Criteria) this;
        }

        public Criteria andWorkUserIdLessThan(Long value) {
            addCriterion("WORK_USER_ID <", value, "workUserId");
            return (Criteria) this;
        }

        public Criteria andWorkUserIdLessThanOrEqualTo(Long value) {
            addCriterion("WORK_USER_ID <=", value, "workUserId");
            return (Criteria) this;
        }

        public Criteria andWorkUserIdIn(List<Long> values) {
            addCriterion("WORK_USER_ID in", values, "workUserId");
            return (Criteria) this;
        }

        public Criteria andWorkUserIdNotIn(List<Long> values) {
            addCriterion("WORK_USER_ID not in", values, "workUserId");
            return (Criteria) this;
        }

        public Criteria andWorkUserIdBetween(Long value1, Long value2) {
            addCriterion("WORK_USER_ID between", value1, value2, "workUserId");
            return (Criteria) this;
        }

        public Criteria andWorkUserIdNotBetween(Long value1, Long value2) {
            addCriterion("WORK_USER_ID not between", value1, value2, "workUserId");
            return (Criteria) this;
        }

        public Criteria andQueueIdIsNull() {
            addCriterion("QUEUE_ID is null");
            return (Criteria) this;
        }

        public Criteria andQueueIdIsNotNull() {
            addCriterion("QUEUE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andQueueIdEqualTo(Long value) {
            addCriterion("QUEUE_ID =", value, "queueId");
            return (Criteria) this;
        }

        public Criteria andQueueIdNotEqualTo(Long value) {
            addCriterion("QUEUE_ID <>", value, "queueId");
            return (Criteria) this;
        }

        public Criteria andQueueIdGreaterThan(Long value) {
            addCriterion("QUEUE_ID >", value, "queueId");
            return (Criteria) this;
        }

        public Criteria andQueueIdGreaterThanOrEqualTo(Long value) {
            addCriterion("QUEUE_ID >=", value, "queueId");
            return (Criteria) this;
        }

        public Criteria andQueueIdLessThan(Long value) {
            addCriterion("QUEUE_ID <", value, "queueId");
            return (Criteria) this;
        }

        public Criteria andQueueIdLessThanOrEqualTo(Long value) {
            addCriterion("QUEUE_ID <=", value, "queueId");
            return (Criteria) this;
        }

        public Criteria andQueueIdIn(List<Long> values) {
            addCriterion("QUEUE_ID in", values, "queueId");
            return (Criteria) this;
        }

        public Criteria andQueueIdNotIn(List<Long> values) {
            addCriterion("QUEUE_ID not in", values, "queueId");
            return (Criteria) this;
        }

        public Criteria andQueueIdBetween(Long value1, Long value2) {
            addCriterion("QUEUE_ID between", value1, value2, "queueId");
            return (Criteria) this;
        }

        public Criteria andQueueIdNotBetween(Long value1, Long value2) {
            addCriterion("QUEUE_ID not between", value1, value2, "queueId");
            return (Criteria) this;
        }

        public Criteria andEvalTimeIsNull() {
            addCriterion("EVAL_TIME is null");
            return (Criteria) this;
        }

        public Criteria andEvalTimeIsNotNull() {
            addCriterion("EVAL_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andEvalTimeEqualTo(Date value) {
            addCriterion("EVAL_TIME =", value, "evalTime");
            return (Criteria) this;
        }

        public Criteria andEvalTimeNotEqualTo(Date value) {
            addCriterion("EVAL_TIME <>", value, "evalTime");
            return (Criteria) this;
        }

        public Criteria andEvalTimeGreaterThan(Date value) {
            addCriterion("EVAL_TIME >", value, "evalTime");
            return (Criteria) this;
        }

        public Criteria andEvalTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("EVAL_TIME >=", value, "evalTime");
            return (Criteria) this;
        }

        public Criteria andEvalTimeLessThan(Date value) {
            addCriterion("EVAL_TIME <", value, "evalTime");
            return (Criteria) this;
        }

        public Criteria andEvalTimeLessThanOrEqualTo(Date value) {
            addCriterion("EVAL_TIME <=", value, "evalTime");
            return (Criteria) this;
        }

        public Criteria andEvalTimeIn(List<Date> values) {
            addCriterion("EVAL_TIME in", values, "evalTime");
            return (Criteria) this;
        }

        public Criteria andEvalTimeNotIn(List<Date> values) {
            addCriterion("EVAL_TIME not in", values, "evalTime");
            return (Criteria) this;
        }

        public Criteria andEvalTimeBetween(Date value1, Date value2) {
            addCriterion("EVAL_TIME between", value1, value2, "evalTime");
            return (Criteria) this;
        }

        public Criteria andEvalTimeNotBetween(Date value1, Date value2) {
            addCriterion("EVAL_TIME not between", value1, value2, "evalTime");
            return (Criteria) this;
        }

        public Criteria andEvalScoreIsNull() {
            addCriterion("EVAL_SCORE is null");
            return (Criteria) this;
        }

        public Criteria andEvalScoreIsNotNull() {
            addCriterion("EVAL_SCORE is not null");
            return (Criteria) this;
        }

        public Criteria andEvalScoreEqualTo(BigDecimal value) {
            addCriterion("EVAL_SCORE =", value, "evalScore");
            return (Criteria) this;
        }

        public Criteria andEvalScoreNotEqualTo(BigDecimal value) {
            addCriterion("EVAL_SCORE <>", value, "evalScore");
            return (Criteria) this;
        }

        public Criteria andEvalScoreGreaterThan(BigDecimal value) {
            addCriterion("EVAL_SCORE >", value, "evalScore");
            return (Criteria) this;
        }

        public Criteria andEvalScoreGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("EVAL_SCORE >=", value, "evalScore");
            return (Criteria) this;
        }

        public Criteria andEvalScoreLessThan(BigDecimal value) {
            addCriterion("EVAL_SCORE <", value, "evalScore");
            return (Criteria) this;
        }

        public Criteria andEvalScoreLessThanOrEqualTo(BigDecimal value) {
            addCriterion("EVAL_SCORE <=", value, "evalScore");
            return (Criteria) this;
        }

        public Criteria andEvalScoreIn(List<BigDecimal> values) {
            addCriterion("EVAL_SCORE in", values, "evalScore");
            return (Criteria) this;
        }

        public Criteria andEvalScoreNotIn(List<BigDecimal> values) {
            addCriterion("EVAL_SCORE not in", values, "evalScore");
            return (Criteria) this;
        }

        public Criteria andEvalScoreBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("EVAL_SCORE between", value1, value2, "evalScore");
            return (Criteria) this;
        }

        public Criteria andEvalScoreNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("EVAL_SCORE not between", value1, value2, "evalScore");
            return (Criteria) this;
        }

        public Criteria andEvalContentIsNull() {
            addCriterion("EVAL_CONTENT is null");
            return (Criteria) this;
        }

        public Criteria andEvalContentIsNotNull() {
            addCriterion("EVAL_CONTENT is not null");
            return (Criteria) this;
        }

        public Criteria andEvalContentEqualTo(String value) {
            addCriterion("EVAL_CONTENT =", value, "evalContent");
            return (Criteria) this;
        }

        public Criteria andEvalContentNotEqualTo(String value) {
            addCriterion("EVAL_CONTENT <>", value, "evalContent");
            return (Criteria) this;
        }

        public Criteria andEvalContentGreaterThan(String value) {
            addCriterion("EVAL_CONTENT >", value, "evalContent");
            return (Criteria) this;
        }

        public Criteria andEvalContentGreaterThanOrEqualTo(String value) {
            addCriterion("EVAL_CONTENT >=", value, "evalContent");
            return (Criteria) this;
        }

        public Criteria andEvalContentLessThan(String value) {
            addCriterion("EVAL_CONTENT <", value, "evalContent");
            return (Criteria) this;
        }

        public Criteria andEvalContentLessThanOrEqualTo(String value) {
            addCriterion("EVAL_CONTENT <=", value, "evalContent");
            return (Criteria) this;
        }

        public Criteria andEvalContentLike(String value) {
            addCriterion("EVAL_CONTENT like", value, "evalContent");
            return (Criteria) this;
        }

        public Criteria andEvalContentNotLike(String value) {
            addCriterion("EVAL_CONTENT not like", value, "evalContent");
            return (Criteria) this;
        }

        public Criteria andEvalContentIn(List<String> values) {
            addCriterion("EVAL_CONTENT in", values, "evalContent");
            return (Criteria) this;
        }

        public Criteria andEvalContentNotIn(List<String> values) {
            addCriterion("EVAL_CONTENT not in", values, "evalContent");
            return (Criteria) this;
        }

        public Criteria andEvalContentBetween(String value1, String value2) {
            addCriterion("EVAL_CONTENT between", value1, value2, "evalContent");
            return (Criteria) this;
        }

        public Criteria andEvalContentNotBetween(String value1, String value2) {
            addCriterion("EVAL_CONTENT not between", value1, value2, "evalContent");
            return (Criteria) this;
        }

        public Criteria andCreateByIsNull() {
            addCriterion("CREATE_BY is null");
            return (Criteria) this;
        }

        public Criteria andCreateByIsNotNull() {
            addCriterion("CREATE_BY is not null");
            return (Criteria) this;
        }

        public Criteria andCreateByEqualTo(String value) {
            addCriterion("CREATE_BY =", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotEqualTo(String value) {
            addCriterion("CREATE_BY <>", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByGreaterThan(String value) {
            addCriterion("CREATE_BY >", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByGreaterThanOrEqualTo(String value) {
            addCriterion("CREATE_BY >=", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLessThan(String value) {
            addCriterion("CREATE_BY <", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLessThanOrEqualTo(String value) {
            addCriterion("CREATE_BY <=", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLike(String value) {
            addCriterion("CREATE_BY like", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotLike(String value) {
            addCriterion("CREATE_BY not like", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByIn(List<String> values) {
            addCriterion("CREATE_BY in", values, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotIn(List<String> values) {
            addCriterion("CREATE_BY not in", values, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByBetween(String value1, String value2) {
            addCriterion("CREATE_BY between", value1, value2, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotBetween(String value1, String value2) {
            addCriterion("CREATE_BY not between", value1, value2, "createBy");
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

        public Criteria andUpdateByIsNull() {
            addCriterion("UPDATE_BY is null");
            return (Criteria) this;
        }

        public Criteria andUpdateByIsNotNull() {
            addCriterion("UPDATE_BY is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateByEqualTo(String value) {
            addCriterion("UPDATE_BY =", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotEqualTo(String value) {
            addCriterion("UPDATE_BY <>", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByGreaterThan(String value) {
            addCriterion("UPDATE_BY >", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByGreaterThanOrEqualTo(String value) {
            addCriterion("UPDATE_BY >=", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLessThan(String value) {
            addCriterion("UPDATE_BY <", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLessThanOrEqualTo(String value) {
            addCriterion("UPDATE_BY <=", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLike(String value) {
            addCriterion("UPDATE_BY like", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotLike(String value) {
            addCriterion("UPDATE_BY not like", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByIn(List<String> values) {
            addCriterion("UPDATE_BY in", values, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotIn(List<String> values) {
            addCriterion("UPDATE_BY not in", values, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByBetween(String value1, String value2) {
            addCriterion("UPDATE_BY between", value1, value2, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotBetween(String value1, String value2) {
            addCriterion("UPDATE_BY not between", value1, value2, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateDateIsNull() {
            addCriterion("UPDATE_DATE is null");
            return (Criteria) this;
        }

        public Criteria andUpdateDateIsNotNull() {
            addCriterion("UPDATE_DATE is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateDateEqualTo(Date value) {
            addCriterion("UPDATE_DATE =", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateNotEqualTo(Date value) {
            addCriterion("UPDATE_DATE <>", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateGreaterThan(Date value) {
            addCriterion("UPDATE_DATE >", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("UPDATE_DATE >=", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateLessThan(Date value) {
            addCriterion("UPDATE_DATE <", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateLessThanOrEqualTo(Date value) {
            addCriterion("UPDATE_DATE <=", value, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateIn(List<Date> values) {
            addCriterion("UPDATE_DATE in", values, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateNotIn(List<Date> values) {
            addCriterion("UPDATE_DATE not in", values, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateBetween(Date value1, Date value2) {
            addCriterion("UPDATE_DATE between", value1, value2, "updateDate");
            return (Criteria) this;
        }

        public Criteria andUpdateDateNotBetween(Date value1, Date value2) {
            addCriterion("UPDATE_DATE not between", value1, value2, "updateDate");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_ha_eval_result
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
     * This class corresponds to the database table t_ha_eval_result
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