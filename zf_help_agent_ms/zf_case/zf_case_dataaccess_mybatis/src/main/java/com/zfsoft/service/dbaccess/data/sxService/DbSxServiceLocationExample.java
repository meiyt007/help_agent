package com.zfsoft.service.dbaccess.data.sxService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbSxServiceLocationExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    public DbSxServiceLocationExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
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
     * This method corresponds to the database table t_sx_service_location
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
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
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
     * This class corresponds to the database table t_sx_service_location
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

        public Criteria andServiceLocationOidIsNull() {
            addCriterion("SERVICE_LOCATION_OID is null");
            return (Criteria) this;
        }

        public Criteria andServiceLocationOidIsNotNull() {
            addCriterion("SERVICE_LOCATION_OID is not null");
            return (Criteria) this;
        }

        public Criteria andServiceLocationOidEqualTo(String value) {
            addCriterion("SERVICE_LOCATION_OID =", value, "serviceLocationOid");
            return (Criteria) this;
        }

        public Criteria andServiceLocationOidNotEqualTo(String value) {
            addCriterion("SERVICE_LOCATION_OID <>", value, "serviceLocationOid");
            return (Criteria) this;
        }

        public Criteria andServiceLocationOidGreaterThan(String value) {
            addCriterion("SERVICE_LOCATION_OID >", value, "serviceLocationOid");
            return (Criteria) this;
        }

        public Criteria andServiceLocationOidGreaterThanOrEqualTo(String value) {
            addCriterion("SERVICE_LOCATION_OID >=", value, "serviceLocationOid");
            return (Criteria) this;
        }

        public Criteria andServiceLocationOidLessThan(String value) {
            addCriterion("SERVICE_LOCATION_OID <", value, "serviceLocationOid");
            return (Criteria) this;
        }

        public Criteria andServiceLocationOidLessThanOrEqualTo(String value) {
            addCriterion("SERVICE_LOCATION_OID <=", value, "serviceLocationOid");
            return (Criteria) this;
        }

        public Criteria andServiceLocationOidLike(String value) {
            addCriterion("SERVICE_LOCATION_OID like", value, "serviceLocationOid");
            return (Criteria) this;
        }

        public Criteria andServiceLocationOidNotLike(String value) {
            addCriterion("SERVICE_LOCATION_OID not like", value, "serviceLocationOid");
            return (Criteria) this;
        }

        public Criteria andServiceLocationOidIn(List<String> values) {
            addCriterion("SERVICE_LOCATION_OID in", values, "serviceLocationOid");
            return (Criteria) this;
        }

        public Criteria andServiceLocationOidNotIn(List<String> values) {
            addCriterion("SERVICE_LOCATION_OID not in", values, "serviceLocationOid");
            return (Criteria) this;
        }

        public Criteria andServiceLocationOidBetween(String value1, String value2) {
            addCriterion("SERVICE_LOCATION_OID between", value1, value2, "serviceLocationOid");
            return (Criteria) this;
        }

        public Criteria andServiceLocationOidNotBetween(String value1, String value2) {
            addCriterion("SERVICE_LOCATION_OID not between", value1, value2, "serviceLocationOid");
            return (Criteria) this;
        }

        public Criteria andExtendOidIsNull() {
            addCriterion("EXTEND_OID is null");
            return (Criteria) this;
        }

        public Criteria andExtendOidIsNotNull() {
            addCriterion("EXTEND_OID is not null");
            return (Criteria) this;
        }

        public Criteria andExtendOidEqualTo(String value) {
            addCriterion("EXTEND_OID =", value, "extendOid");
            return (Criteria) this;
        }

        public Criteria andExtendOidNotEqualTo(String value) {
            addCriterion("EXTEND_OID <>", value, "extendOid");
            return (Criteria) this;
        }

        public Criteria andExtendOidGreaterThan(String value) {
            addCriterion("EXTEND_OID >", value, "extendOid");
            return (Criteria) this;
        }

        public Criteria andExtendOidGreaterThanOrEqualTo(String value) {
            addCriterion("EXTEND_OID >=", value, "extendOid");
            return (Criteria) this;
        }

        public Criteria andExtendOidLessThan(String value) {
            addCriterion("EXTEND_OID <", value, "extendOid");
            return (Criteria) this;
        }

        public Criteria andExtendOidLessThanOrEqualTo(String value) {
            addCriterion("EXTEND_OID <=", value, "extendOid");
            return (Criteria) this;
        }

        public Criteria andExtendOidLike(String value) {
            addCriterion("EXTEND_OID like", value, "extendOid");
            return (Criteria) this;
        }

        public Criteria andExtendOidNotLike(String value) {
            addCriterion("EXTEND_OID not like", value, "extendOid");
            return (Criteria) this;
        }

        public Criteria andExtendOidIn(List<String> values) {
            addCriterion("EXTEND_OID in", values, "extendOid");
            return (Criteria) this;
        }

        public Criteria andExtendOidNotIn(List<String> values) {
            addCriterion("EXTEND_OID not in", values, "extendOid");
            return (Criteria) this;
        }

        public Criteria andExtendOidBetween(String value1, String value2) {
            addCriterion("EXTEND_OID between", value1, value2, "extendOid");
            return (Criteria) this;
        }

        public Criteria andExtendOidNotBetween(String value1, String value2) {
            addCriterion("EXTEND_OID not between", value1, value2, "extendOid");
            return (Criteria) this;
        }

        public Criteria andHandleLocationOidIsNull() {
            addCriterion("HANDLE_LOCATION_OID is null");
            return (Criteria) this;
        }

        public Criteria andHandleLocationOidIsNotNull() {
            addCriterion("HANDLE_LOCATION_OID is not null");
            return (Criteria) this;
        }

        public Criteria andHandleLocationOidEqualTo(String value) {
            addCriterion("HANDLE_LOCATION_OID =", value, "handleLocationOid");
            return (Criteria) this;
        }

        public Criteria andHandleLocationOidNotEqualTo(String value) {
            addCriterion("HANDLE_LOCATION_OID <>", value, "handleLocationOid");
            return (Criteria) this;
        }

        public Criteria andHandleLocationOidGreaterThan(String value) {
            addCriterion("HANDLE_LOCATION_OID >", value, "handleLocationOid");
            return (Criteria) this;
        }

        public Criteria andHandleLocationOidGreaterThanOrEqualTo(String value) {
            addCriterion("HANDLE_LOCATION_OID >=", value, "handleLocationOid");
            return (Criteria) this;
        }

        public Criteria andHandleLocationOidLessThan(String value) {
            addCriterion("HANDLE_LOCATION_OID <", value, "handleLocationOid");
            return (Criteria) this;
        }

        public Criteria andHandleLocationOidLessThanOrEqualTo(String value) {
            addCriterion("HANDLE_LOCATION_OID <=", value, "handleLocationOid");
            return (Criteria) this;
        }

        public Criteria andHandleLocationOidLike(String value) {
            addCriterion("HANDLE_LOCATION_OID like", value, "handleLocationOid");
            return (Criteria) this;
        }

        public Criteria andHandleLocationOidNotLike(String value) {
            addCriterion("HANDLE_LOCATION_OID not like", value, "handleLocationOid");
            return (Criteria) this;
        }

        public Criteria andHandleLocationOidIn(List<String> values) {
            addCriterion("HANDLE_LOCATION_OID in", values, "handleLocationOid");
            return (Criteria) this;
        }

        public Criteria andHandleLocationOidNotIn(List<String> values) {
            addCriterion("HANDLE_LOCATION_OID not in", values, "handleLocationOid");
            return (Criteria) this;
        }

        public Criteria andHandleLocationOidBetween(String value1, String value2) {
            addCriterion("HANDLE_LOCATION_OID between", value1, value2, "handleLocationOid");
            return (Criteria) this;
        }

        public Criteria andHandleLocationOidNotBetween(String value1, String value2) {
            addCriterion("HANDLE_LOCATION_OID not between", value1, value2, "handleLocationOid");
            return (Criteria) this;
        }

        public Criteria andLocationNameIsNull() {
            addCriterion("LOCATION_NAME is null");
            return (Criteria) this;
        }

        public Criteria andLocationNameIsNotNull() {
            addCriterion("LOCATION_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andLocationNameEqualTo(String value) {
            addCriterion("LOCATION_NAME =", value, "locationName");
            return (Criteria) this;
        }

        public Criteria andLocationNameNotEqualTo(String value) {
            addCriterion("LOCATION_NAME <>", value, "locationName");
            return (Criteria) this;
        }

        public Criteria andLocationNameGreaterThan(String value) {
            addCriterion("LOCATION_NAME >", value, "locationName");
            return (Criteria) this;
        }

        public Criteria andLocationNameGreaterThanOrEqualTo(String value) {
            addCriterion("LOCATION_NAME >=", value, "locationName");
            return (Criteria) this;
        }

        public Criteria andLocationNameLessThan(String value) {
            addCriterion("LOCATION_NAME <", value, "locationName");
            return (Criteria) this;
        }

        public Criteria andLocationNameLessThanOrEqualTo(String value) {
            addCriterion("LOCATION_NAME <=", value, "locationName");
            return (Criteria) this;
        }

        public Criteria andLocationNameLike(String value) {
            addCriterion("LOCATION_NAME like", value, "locationName");
            return (Criteria) this;
        }

        public Criteria andLocationNameNotLike(String value) {
            addCriterion("LOCATION_NAME not like", value, "locationName");
            return (Criteria) this;
        }

        public Criteria andLocationNameIn(List<String> values) {
            addCriterion("LOCATION_NAME in", values, "locationName");
            return (Criteria) this;
        }

        public Criteria andLocationNameNotIn(List<String> values) {
            addCriterion("LOCATION_NAME not in", values, "locationName");
            return (Criteria) this;
        }

        public Criteria andLocationNameBetween(String value1, String value2) {
            addCriterion("LOCATION_NAME between", value1, value2, "locationName");
            return (Criteria) this;
        }

        public Criteria andLocationNameNotBetween(String value1, String value2) {
            addCriterion("LOCATION_NAME not between", value1, value2, "locationName");
            return (Criteria) this;
        }

        public Criteria andLocationAddrIsNull() {
            addCriterion("LOCATION_ADDR is null");
            return (Criteria) this;
        }

        public Criteria andLocationAddrIsNotNull() {
            addCriterion("LOCATION_ADDR is not null");
            return (Criteria) this;
        }

        public Criteria andLocationAddrEqualTo(String value) {
            addCriterion("LOCATION_ADDR =", value, "locationAddr");
            return (Criteria) this;
        }

        public Criteria andLocationAddrNotEqualTo(String value) {
            addCriterion("LOCATION_ADDR <>", value, "locationAddr");
            return (Criteria) this;
        }

        public Criteria andLocationAddrGreaterThan(String value) {
            addCriterion("LOCATION_ADDR >", value, "locationAddr");
            return (Criteria) this;
        }

        public Criteria andLocationAddrGreaterThanOrEqualTo(String value) {
            addCriterion("LOCATION_ADDR >=", value, "locationAddr");
            return (Criteria) this;
        }

        public Criteria andLocationAddrLessThan(String value) {
            addCriterion("LOCATION_ADDR <", value, "locationAddr");
            return (Criteria) this;
        }

        public Criteria andLocationAddrLessThanOrEqualTo(String value) {
            addCriterion("LOCATION_ADDR <=", value, "locationAddr");
            return (Criteria) this;
        }

        public Criteria andLocationAddrLike(String value) {
            addCriterion("LOCATION_ADDR like", value, "locationAddr");
            return (Criteria) this;
        }

        public Criteria andLocationAddrNotLike(String value) {
            addCriterion("LOCATION_ADDR not like", value, "locationAddr");
            return (Criteria) this;
        }

        public Criteria andLocationAddrIn(List<String> values) {
            addCriterion("LOCATION_ADDR in", values, "locationAddr");
            return (Criteria) this;
        }

        public Criteria andLocationAddrNotIn(List<String> values) {
            addCriterion("LOCATION_ADDR not in", values, "locationAddr");
            return (Criteria) this;
        }

        public Criteria andLocationAddrBetween(String value1, String value2) {
            addCriterion("LOCATION_ADDR between", value1, value2, "locationAddr");
            return (Criteria) this;
        }

        public Criteria andLocationAddrNotBetween(String value1, String value2) {
            addCriterion("LOCATION_ADDR not between", value1, value2, "locationAddr");
            return (Criteria) this;
        }

        public Criteria andAcceptDateIsNull() {
            addCriterion("ACCEPT_DATE is null");
            return (Criteria) this;
        }

        public Criteria andAcceptDateIsNotNull() {
            addCriterion("ACCEPT_DATE is not null");
            return (Criteria) this;
        }

        public Criteria andAcceptDateEqualTo(String value) {
            addCriterion("ACCEPT_DATE =", value, "acceptDate");
            return (Criteria) this;
        }

        public Criteria andAcceptDateNotEqualTo(String value) {
            addCriterion("ACCEPT_DATE <>", value, "acceptDate");
            return (Criteria) this;
        }

        public Criteria andAcceptDateGreaterThan(String value) {
            addCriterion("ACCEPT_DATE >", value, "acceptDate");
            return (Criteria) this;
        }

        public Criteria andAcceptDateGreaterThanOrEqualTo(String value) {
            addCriterion("ACCEPT_DATE >=", value, "acceptDate");
            return (Criteria) this;
        }

        public Criteria andAcceptDateLessThan(String value) {
            addCriterion("ACCEPT_DATE <", value, "acceptDate");
            return (Criteria) this;
        }

        public Criteria andAcceptDateLessThanOrEqualTo(String value) {
            addCriterion("ACCEPT_DATE <=", value, "acceptDate");
            return (Criteria) this;
        }

        public Criteria andAcceptDateLike(String value) {
            addCriterion("ACCEPT_DATE like", value, "acceptDate");
            return (Criteria) this;
        }

        public Criteria andAcceptDateNotLike(String value) {
            addCriterion("ACCEPT_DATE not like", value, "acceptDate");
            return (Criteria) this;
        }

        public Criteria andAcceptDateIn(List<String> values) {
            addCriterion("ACCEPT_DATE in", values, "acceptDate");
            return (Criteria) this;
        }

        public Criteria andAcceptDateNotIn(List<String> values) {
            addCriterion("ACCEPT_DATE not in", values, "acceptDate");
            return (Criteria) this;
        }

        public Criteria andAcceptDateBetween(String value1, String value2) {
            addCriterion("ACCEPT_DATE between", value1, value2, "acceptDate");
            return (Criteria) this;
        }

        public Criteria andAcceptDateNotBetween(String value1, String value2) {
            addCriterion("ACCEPT_DATE not between", value1, value2, "acceptDate");
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
     * This class corresponds to the database table t_sx_service_location
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
     * This class corresponds to the database table t_sx_service_location
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