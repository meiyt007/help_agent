package com.zfsoft.microservice.form.dbaccess.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbFormObjectExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_form_object
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_form_object
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_form_object
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_object
     *
     * @mbggenerated
     */
    public DbFormObjectExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_object
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_object
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_object
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_object
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_object
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_object
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_object
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
     * This method corresponds to the database table t_form_object
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
     * This method corresponds to the database table t_form_object
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_object
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
     * This class corresponds to the database table t_form_object
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

        public Criteria andObjectOidIsNull() {
            addCriterion("OBJECT_OID is null");
            return (Criteria) this;
        }

        public Criteria andObjectOidIsNotNull() {
            addCriterion("OBJECT_OID is not null");
            return (Criteria) this;
        }

        public Criteria andObjectOidEqualTo(String value) {
            addCriterion("OBJECT_OID =", value, "objectOid");
            return (Criteria) this;
        }

        public Criteria andObjectOidNotEqualTo(String value) {
            addCriterion("OBJECT_OID <>", value, "objectOid");
            return (Criteria) this;
        }

        public Criteria andObjectOidGreaterThan(String value) {
            addCriterion("OBJECT_OID >", value, "objectOid");
            return (Criteria) this;
        }

        public Criteria andObjectOidGreaterThanOrEqualTo(String value) {
            addCriterion("OBJECT_OID >=", value, "objectOid");
            return (Criteria) this;
        }

        public Criteria andObjectOidLessThan(String value) {
            addCriterion("OBJECT_OID <", value, "objectOid");
            return (Criteria) this;
        }

        public Criteria andObjectOidLessThanOrEqualTo(String value) {
            addCriterion("OBJECT_OID <=", value, "objectOid");
            return (Criteria) this;
        }

        public Criteria andObjectOidLike(String value) {
            addCriterion("OBJECT_OID like", value, "objectOid");
            return (Criteria) this;
        }

        public Criteria andObjectOidNotLike(String value) {
            addCriterion("OBJECT_OID not like", value, "objectOid");
            return (Criteria) this;
        }

        public Criteria andObjectOidIn(List<String> values) {
            addCriterion("OBJECT_OID in", values, "objectOid");
            return (Criteria) this;
        }

        public Criteria andObjectOidNotIn(List<String> values) {
            addCriterion("OBJECT_OID not in", values, "objectOid");
            return (Criteria) this;
        }

        public Criteria andObjectOidBetween(String value1, String value2) {
            addCriterion("OBJECT_OID between", value1, value2, "objectOid");
            return (Criteria) this;
        }

        public Criteria andObjectOidNotBetween(String value1, String value2) {
            addCriterion("OBJECT_OID not between", value1, value2, "objectOid");
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

        public Criteria andDatasourceOidIsNull() {
            addCriterion("DATASOURCE_OID is null");
            return (Criteria) this;
        }

        public Criteria andDatasourceOidIsNotNull() {
            addCriterion("DATASOURCE_OID is not null");
            return (Criteria) this;
        }

        public Criteria andDatasourceOidEqualTo(String value) {
            addCriterion("DATASOURCE_OID =", value, "datasourceOid");
            return (Criteria) this;
        }

        public Criteria andDatasourceOidNotEqualTo(String value) {
            addCriterion("DATASOURCE_OID <>", value, "datasourceOid");
            return (Criteria) this;
        }

        public Criteria andDatasourceOidGreaterThan(String value) {
            addCriterion("DATASOURCE_OID >", value, "datasourceOid");
            return (Criteria) this;
        }

        public Criteria andDatasourceOidGreaterThanOrEqualTo(String value) {
            addCriterion("DATASOURCE_OID >=", value, "datasourceOid");
            return (Criteria) this;
        }

        public Criteria andDatasourceOidLessThan(String value) {
            addCriterion("DATASOURCE_OID <", value, "datasourceOid");
            return (Criteria) this;
        }

        public Criteria andDatasourceOidLessThanOrEqualTo(String value) {
            addCriterion("DATASOURCE_OID <=", value, "datasourceOid");
            return (Criteria) this;
        }

        public Criteria andDatasourceOidLike(String value) {
            addCriterion("DATASOURCE_OID like", value, "datasourceOid");
            return (Criteria) this;
        }

        public Criteria andDatasourceOidNotLike(String value) {
            addCriterion("DATASOURCE_OID not like", value, "datasourceOid");
            return (Criteria) this;
        }

        public Criteria andDatasourceOidIn(List<String> values) {
            addCriterion("DATASOURCE_OID in", values, "datasourceOid");
            return (Criteria) this;
        }

        public Criteria andDatasourceOidNotIn(List<String> values) {
            addCriterion("DATASOURCE_OID not in", values, "datasourceOid");
            return (Criteria) this;
        }

        public Criteria andDatasourceOidBetween(String value1, String value2) {
            addCriterion("DATASOURCE_OID between", value1, value2, "datasourceOid");
            return (Criteria) this;
        }

        public Criteria andDatasourceOidNotBetween(String value1, String value2) {
            addCriterion("DATASOURCE_OID not between", value1, value2, "datasourceOid");
            return (Criteria) this;
        }

        public Criteria andModuleOidIsNull() {
            addCriterion("MODULE_OID is null");
            return (Criteria) this;
        }

        public Criteria andModuleOidIsNotNull() {
            addCriterion("MODULE_OID is not null");
            return (Criteria) this;
        }

        public Criteria andModuleOidEqualTo(String value) {
            addCriterion("MODULE_OID =", value, "moduleOid");
            return (Criteria) this;
        }

        public Criteria andModuleOidNotEqualTo(String value) {
            addCriterion("MODULE_OID <>", value, "moduleOid");
            return (Criteria) this;
        }

        public Criteria andModuleOidGreaterThan(String value) {
            addCriterion("MODULE_OID >", value, "moduleOid");
            return (Criteria) this;
        }

        public Criteria andModuleOidGreaterThanOrEqualTo(String value) {
            addCriterion("MODULE_OID >=", value, "moduleOid");
            return (Criteria) this;
        }

        public Criteria andModuleOidLessThan(String value) {
            addCriterion("MODULE_OID <", value, "moduleOid");
            return (Criteria) this;
        }

        public Criteria andModuleOidLessThanOrEqualTo(String value) {
            addCriterion("MODULE_OID <=", value, "moduleOid");
            return (Criteria) this;
        }

        public Criteria andModuleOidLike(String value) {
            addCriterion("MODULE_OID like", value, "moduleOid");
            return (Criteria) this;
        }

        public Criteria andModuleOidNotLike(String value) {
            addCriterion("MODULE_OID not like", value, "moduleOid");
            return (Criteria) this;
        }

        public Criteria andModuleOidIn(List<String> values) {
            addCriterion("MODULE_OID in", values, "moduleOid");
            return (Criteria) this;
        }

        public Criteria andModuleOidNotIn(List<String> values) {
            addCriterion("MODULE_OID not in", values, "moduleOid");
            return (Criteria) this;
        }

        public Criteria andModuleOidBetween(String value1, String value2) {
            addCriterion("MODULE_OID between", value1, value2, "moduleOid");
            return (Criteria) this;
        }

        public Criteria andModuleOidNotBetween(String value1, String value2) {
            addCriterion("MODULE_OID not between", value1, value2, "moduleOid");
            return (Criteria) this;
        }

        public Criteria andObjectNameIsNull() {
            addCriterion("OBJECT_NAME is null");
            return (Criteria) this;
        }

        public Criteria andObjectNameIsNotNull() {
            addCriterion("OBJECT_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andObjectNameEqualTo(String value) {
            addCriterion("OBJECT_NAME =", value, "objectName");
            return (Criteria) this;
        }

        public Criteria andObjectNameNotEqualTo(String value) {
            addCriterion("OBJECT_NAME <>", value, "objectName");
            return (Criteria) this;
        }

        public Criteria andObjectNameGreaterThan(String value) {
            addCriterion("OBJECT_NAME >", value, "objectName");
            return (Criteria) this;
        }

        public Criteria andObjectNameGreaterThanOrEqualTo(String value) {
            addCriterion("OBJECT_NAME >=", value, "objectName");
            return (Criteria) this;
        }

        public Criteria andObjectNameLessThan(String value) {
            addCriterion("OBJECT_NAME <", value, "objectName");
            return (Criteria) this;
        }

        public Criteria andObjectNameLessThanOrEqualTo(String value) {
            addCriterion("OBJECT_NAME <=", value, "objectName");
            return (Criteria) this;
        }

        public Criteria andObjectNameLike(String value) {
            addCriterion("OBJECT_NAME like", value, "objectName");
            return (Criteria) this;
        }

        public Criteria andObjectNameNotLike(String value) {
            addCriterion("OBJECT_NAME not like", value, "objectName");
            return (Criteria) this;
        }

        public Criteria andObjectNameIn(List<String> values) {
            addCriterion("OBJECT_NAME in", values, "objectName");
            return (Criteria) this;
        }

        public Criteria andObjectNameNotIn(List<String> values) {
            addCriterion("OBJECT_NAME not in", values, "objectName");
            return (Criteria) this;
        }

        public Criteria andObjectNameBetween(String value1, String value2) {
            addCriterion("OBJECT_NAME between", value1, value2, "objectName");
            return (Criteria) this;
        }

        public Criteria andObjectNameNotBetween(String value1, String value2) {
            addCriterion("OBJECT_NAME not between", value1, value2, "objectName");
            return (Criteria) this;
        }

        public Criteria andObjectCodeIsNull() {
            addCriterion("OBJECT_CODE is null");
            return (Criteria) this;
        }

        public Criteria andObjectCodeIsNotNull() {
            addCriterion("OBJECT_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andObjectCodeEqualTo(String value) {
            addCriterion("OBJECT_CODE =", value, "objectCode");
            return (Criteria) this;
        }

        public Criteria andObjectCodeNotEqualTo(String value) {
            addCriterion("OBJECT_CODE <>", value, "objectCode");
            return (Criteria) this;
        }

        public Criteria andObjectCodeGreaterThan(String value) {
            addCriterion("OBJECT_CODE >", value, "objectCode");
            return (Criteria) this;
        }

        public Criteria andObjectCodeGreaterThanOrEqualTo(String value) {
            addCriterion("OBJECT_CODE >=", value, "objectCode");
            return (Criteria) this;
        }

        public Criteria andObjectCodeLessThan(String value) {
            addCriterion("OBJECT_CODE <", value, "objectCode");
            return (Criteria) this;
        }

        public Criteria andObjectCodeLessThanOrEqualTo(String value) {
            addCriterion("OBJECT_CODE <=", value, "objectCode");
            return (Criteria) this;
        }

        public Criteria andObjectCodeLike(String value) {
            addCriterion("OBJECT_CODE like", value, "objectCode");
            return (Criteria) this;
        }

        public Criteria andObjectCodeNotLike(String value) {
            addCriterion("OBJECT_CODE not like", value, "objectCode");
            return (Criteria) this;
        }

        public Criteria andObjectCodeIn(List<String> values) {
            addCriterion("OBJECT_CODE in", values, "objectCode");
            return (Criteria) this;
        }

        public Criteria andObjectCodeNotIn(List<String> values) {
            addCriterion("OBJECT_CODE not in", values, "objectCode");
            return (Criteria) this;
        }

        public Criteria andObjectCodeBetween(String value1, String value2) {
            addCriterion("OBJECT_CODE between", value1, value2, "objectCode");
            return (Criteria) this;
        }

        public Criteria andObjectCodeNotBetween(String value1, String value2) {
            addCriterion("OBJECT_CODE not between", value1, value2, "objectCode");
            return (Criteria) this;
        }

        public Criteria andObjectFormIsNull() {
            addCriterion("OBJECT_FORM is null");
            return (Criteria) this;
        }

        public Criteria andObjectFormIsNotNull() {
            addCriterion("OBJECT_FORM is not null");
            return (Criteria) this;
        }

        public Criteria andObjectFormEqualTo(String value) {
            addCriterion("OBJECT_FORM =", value, "objectForm");
            return (Criteria) this;
        }

        public Criteria andObjectFormNotEqualTo(String value) {
            addCriterion("OBJECT_FORM <>", value, "objectForm");
            return (Criteria) this;
        }

        public Criteria andObjectFormGreaterThan(String value) {
            addCriterion("OBJECT_FORM >", value, "objectForm");
            return (Criteria) this;
        }

        public Criteria andObjectFormGreaterThanOrEqualTo(String value) {
            addCriterion("OBJECT_FORM >=", value, "objectForm");
            return (Criteria) this;
        }

        public Criteria andObjectFormLessThan(String value) {
            addCriterion("OBJECT_FORM <", value, "objectForm");
            return (Criteria) this;
        }

        public Criteria andObjectFormLessThanOrEqualTo(String value) {
            addCriterion("OBJECT_FORM <=", value, "objectForm");
            return (Criteria) this;
        }

        public Criteria andObjectFormLike(String value) {
            addCriterion("OBJECT_FORM like", value, "objectForm");
            return (Criteria) this;
        }

        public Criteria andObjectFormNotLike(String value) {
            addCriterion("OBJECT_FORM not like", value, "objectForm");
            return (Criteria) this;
        }

        public Criteria andObjectFormIn(List<String> values) {
            addCriterion("OBJECT_FORM in", values, "objectForm");
            return (Criteria) this;
        }

        public Criteria andObjectFormNotIn(List<String> values) {
            addCriterion("OBJECT_FORM not in", values, "objectForm");
            return (Criteria) this;
        }

        public Criteria andObjectFormBetween(String value1, String value2) {
            addCriterion("OBJECT_FORM between", value1, value2, "objectForm");
            return (Criteria) this;
        }

        public Criteria andObjectFormNotBetween(String value1, String value2) {
            addCriterion("OBJECT_FORM not between", value1, value2, "objectForm");
            return (Criteria) this;
        }

        public Criteria andIsAbleIsNull() {
            addCriterion("IS_ABLE is null");
            return (Criteria) this;
        }

        public Criteria andIsAbleIsNotNull() {
            addCriterion("IS_ABLE is not null");
            return (Criteria) this;
        }

        public Criteria andIsAbleEqualTo(Integer value) {
            addCriterion("IS_ABLE =", value, "isAble");
            return (Criteria) this;
        }

        public Criteria andIsAbleNotEqualTo(Integer value) {
            addCriterion("IS_ABLE <>", value, "isAble");
            return (Criteria) this;
        }

        public Criteria andIsAbleGreaterThan(Integer value) {
            addCriterion("IS_ABLE >", value, "isAble");
            return (Criteria) this;
        }

        public Criteria andIsAbleGreaterThanOrEqualTo(Integer value) {
            addCriterion("IS_ABLE >=", value, "isAble");
            return (Criteria) this;
        }

        public Criteria andIsAbleLessThan(Integer value) {
            addCriterion("IS_ABLE <", value, "isAble");
            return (Criteria) this;
        }

        public Criteria andIsAbleLessThanOrEqualTo(Integer value) {
            addCriterion("IS_ABLE <=", value, "isAble");
            return (Criteria) this;
        }

        public Criteria andIsAbleIn(List<Integer> values) {
            addCriterion("IS_ABLE in", values, "isAble");
            return (Criteria) this;
        }

        public Criteria andIsAbleNotIn(List<Integer> values) {
            addCriterion("IS_ABLE not in", values, "isAble");
            return (Criteria) this;
        }

        public Criteria andIsAbleBetween(Integer value1, Integer value2) {
            addCriterion("IS_ABLE between", value1, value2, "isAble");
            return (Criteria) this;
        }

        public Criteria andIsAbleNotBetween(Integer value1, Integer value2) {
            addCriterion("IS_ABLE not between", value1, value2, "isAble");
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

        public Criteria andIsSaveIsNull() {
            addCriterion("IS_SAVE is null");
            return (Criteria) this;
        }

        public Criteria andIsSaveIsNotNull() {
            addCriterion("IS_SAVE is not null");
            return (Criteria) this;
        }

        public Criteria andIsSaveEqualTo(Integer value) {
            addCriterion("IS_SAVE =", value, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveNotEqualTo(Integer value) {
            addCriterion("IS_SAVE <>", value, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveGreaterThan(Integer value) {
            addCriterion("IS_SAVE >", value, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveGreaterThanOrEqualTo(Integer value) {
            addCriterion("IS_SAVE >=", value, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveLessThan(Integer value) {
            addCriterion("IS_SAVE <", value, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveLessThanOrEqualTo(Integer value) {
            addCriterion("IS_SAVE <=", value, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveIn(List<Integer> values) {
            addCriterion("IS_SAVE in", values, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveNotIn(List<Integer> values) {
            addCriterion("IS_SAVE not in", values, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveBetween(Integer value1, Integer value2) {
            addCriterion("IS_SAVE between", value1, value2, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveNotBetween(Integer value1, Integer value2) {
            addCriterion("IS_SAVE not between", value1, value2, "isSave");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_form_object
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
     * This class corresponds to the database table t_form_object
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