package com.zfsoft.superwindow.dbaccess.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbCardCatalogueElementExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_card_catalogue_element
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_card_catalogue_element
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_card_catalogue_element
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_card_catalogue_element
     *
     * @mbggenerated
     */
    public DbCardCatalogueElementExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_card_catalogue_element
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_card_catalogue_element
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_card_catalogue_element
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_card_catalogue_element
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_card_catalogue_element
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_card_catalogue_element
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_card_catalogue_element
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
     * This method corresponds to the database table t_card_catalogue_element
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
     * This method corresponds to the database table t_card_catalogue_element
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_card_catalogue_element
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
     * This class corresponds to the database table t_card_catalogue_element
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

        public Criteria andCardCatalogueOidIsNull() {
            addCriterion("CARD_CATALOGUE_OID is null");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueOidIsNotNull() {
            addCriterion("CARD_CATALOGUE_OID is not null");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueOidEqualTo(String value) {
            addCriterion("CARD_CATALOGUE_OID =", value, "cardCatalogueOid");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueOidNotEqualTo(String value) {
            addCriterion("CARD_CATALOGUE_OID <>", value, "cardCatalogueOid");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueOidGreaterThan(String value) {
            addCriterion("CARD_CATALOGUE_OID >", value, "cardCatalogueOid");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueOidGreaterThanOrEqualTo(String value) {
            addCriterion("CARD_CATALOGUE_OID >=", value, "cardCatalogueOid");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueOidLessThan(String value) {
            addCriterion("CARD_CATALOGUE_OID <", value, "cardCatalogueOid");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueOidLessThanOrEqualTo(String value) {
            addCriterion("CARD_CATALOGUE_OID <=", value, "cardCatalogueOid");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueOidLike(String value) {
            addCriterion("CARD_CATALOGUE_OID like", value, "cardCatalogueOid");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueOidNotLike(String value) {
            addCriterion("CARD_CATALOGUE_OID not like", value, "cardCatalogueOid");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueOidIn(List<String> values) {
            addCriterion("CARD_CATALOGUE_OID in", values, "cardCatalogueOid");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueOidNotIn(List<String> values) {
            addCriterion("CARD_CATALOGUE_OID not in", values, "cardCatalogueOid");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueOidBetween(String value1, String value2) {
            addCriterion("CARD_CATALOGUE_OID between", value1, value2, "cardCatalogueOid");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueOidNotBetween(String value1, String value2) {
            addCriterion("CARD_CATALOGUE_OID not between", value1, value2, "cardCatalogueOid");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementCodeIsNull() {
            addCriterion("CARD_CATALOGUE_ELEMENT_CODE is null");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementCodeIsNotNull() {
            addCriterion("CARD_CATALOGUE_ELEMENT_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementCodeEqualTo(String value) {
            addCriterion("CARD_CATALOGUE_ELEMENT_CODE =", value, "cardCatalogueElementCode");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementCodeNotEqualTo(String value) {
            addCriterion("CARD_CATALOGUE_ELEMENT_CODE <>", value, "cardCatalogueElementCode");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementCodeGreaterThan(String value) {
            addCriterion("CARD_CATALOGUE_ELEMENT_CODE >", value, "cardCatalogueElementCode");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementCodeGreaterThanOrEqualTo(String value) {
            addCriterion("CARD_CATALOGUE_ELEMENT_CODE >=", value, "cardCatalogueElementCode");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementCodeLessThan(String value) {
            addCriterion("CARD_CATALOGUE_ELEMENT_CODE <", value, "cardCatalogueElementCode");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementCodeLessThanOrEqualTo(String value) {
            addCriterion("CARD_CATALOGUE_ELEMENT_CODE <=", value, "cardCatalogueElementCode");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementCodeLike(String value) {
            addCriterion("CARD_CATALOGUE_ELEMENT_CODE like", value, "cardCatalogueElementCode");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementCodeNotLike(String value) {
            addCriterion("CARD_CATALOGUE_ELEMENT_CODE not like", value, "cardCatalogueElementCode");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementCodeIn(List<String> values) {
            addCriterion("CARD_CATALOGUE_ELEMENT_CODE in", values, "cardCatalogueElementCode");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementCodeNotIn(List<String> values) {
            addCriterion("CARD_CATALOGUE_ELEMENT_CODE not in", values, "cardCatalogueElementCode");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementCodeBetween(String value1, String value2) {
            addCriterion("CARD_CATALOGUE_ELEMENT_CODE between", value1, value2, "cardCatalogueElementCode");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementCodeNotBetween(String value1, String value2) {
            addCriterion("CARD_CATALOGUE_ELEMENT_CODE not between", value1, value2, "cardCatalogueElementCode");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementNameIsNull() {
            addCriterion("CARD_CATALOGUE_ELEMENT_NAME is null");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementNameIsNotNull() {
            addCriterion("CARD_CATALOGUE_ELEMENT_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementNameEqualTo(String value) {
            addCriterion("CARD_CATALOGUE_ELEMENT_NAME =", value, "cardCatalogueElementName");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementNameNotEqualTo(String value) {
            addCriterion("CARD_CATALOGUE_ELEMENT_NAME <>", value, "cardCatalogueElementName");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementNameGreaterThan(String value) {
            addCriterion("CARD_CATALOGUE_ELEMENT_NAME >", value, "cardCatalogueElementName");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementNameGreaterThanOrEqualTo(String value) {
            addCriterion("CARD_CATALOGUE_ELEMENT_NAME >=", value, "cardCatalogueElementName");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementNameLessThan(String value) {
            addCriterion("CARD_CATALOGUE_ELEMENT_NAME <", value, "cardCatalogueElementName");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementNameLessThanOrEqualTo(String value) {
            addCriterion("CARD_CATALOGUE_ELEMENT_NAME <=", value, "cardCatalogueElementName");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementNameLike(String value) {
            addCriterion("CARD_CATALOGUE_ELEMENT_NAME like", value, "cardCatalogueElementName");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementNameNotLike(String value) {
            addCriterion("CARD_CATALOGUE_ELEMENT_NAME not like", value, "cardCatalogueElementName");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementNameIn(List<String> values) {
            addCriterion("CARD_CATALOGUE_ELEMENT_NAME in", values, "cardCatalogueElementName");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementNameNotIn(List<String> values) {
            addCriterion("CARD_CATALOGUE_ELEMENT_NAME not in", values, "cardCatalogueElementName");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementNameBetween(String value1, String value2) {
            addCriterion("CARD_CATALOGUE_ELEMENT_NAME between", value1, value2, "cardCatalogueElementName");
            return (Criteria) this;
        }

        public Criteria andCardCatalogueElementNameNotBetween(String value1, String value2) {
            addCriterion("CARD_CATALOGUE_ELEMENT_NAME not between", value1, value2, "cardCatalogueElementName");
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
     * This class corresponds to the database table t_card_catalogue_element
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
     * This class corresponds to the database table t_card_catalogue_element
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