package com.zfsoft.microservice.form.dbaccess.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbFormTemplateExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    public DbFormTemplateExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
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
     * This method corresponds to the database table t_form_template
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
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
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
     * This class corresponds to the database table t_form_template
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

        public Criteria andTemplateOidIsNull() {
            addCriterion("TEMPLATE_OID is null");
            return (Criteria) this;
        }

        public Criteria andTemplateOidIsNotNull() {
            addCriterion("TEMPLATE_OID is not null");
            return (Criteria) this;
        }

        public Criteria andTemplateOidEqualTo(String value) {
            addCriterion("TEMPLATE_OID =", value, "templateOid");
            return (Criteria) this;
        }

        public Criteria andTemplateOidNotEqualTo(String value) {
            addCriterion("TEMPLATE_OID <>", value, "templateOid");
            return (Criteria) this;
        }

        public Criteria andTemplateOidGreaterThan(String value) {
            addCriterion("TEMPLATE_OID >", value, "templateOid");
            return (Criteria) this;
        }

        public Criteria andTemplateOidGreaterThanOrEqualTo(String value) {
            addCriterion("TEMPLATE_OID >=", value, "templateOid");
            return (Criteria) this;
        }

        public Criteria andTemplateOidLessThan(String value) {
            addCriterion("TEMPLATE_OID <", value, "templateOid");
            return (Criteria) this;
        }

        public Criteria andTemplateOidLessThanOrEqualTo(String value) {
            addCriterion("TEMPLATE_OID <=", value, "templateOid");
            return (Criteria) this;
        }

        public Criteria andTemplateOidLike(String value) {
            addCriterion("TEMPLATE_OID like", value, "templateOid");
            return (Criteria) this;
        }

        public Criteria andTemplateOidNotLike(String value) {
            addCriterion("TEMPLATE_OID not like", value, "templateOid");
            return (Criteria) this;
        }

        public Criteria andTemplateOidIn(List<String> values) {
            addCriterion("TEMPLATE_OID in", values, "templateOid");
            return (Criteria) this;
        }

        public Criteria andTemplateOidNotIn(List<String> values) {
            addCriterion("TEMPLATE_OID not in", values, "templateOid");
            return (Criteria) this;
        }

        public Criteria andTemplateOidBetween(String value1, String value2) {
            addCriterion("TEMPLATE_OID between", value1, value2, "templateOid");
            return (Criteria) this;
        }

        public Criteria andTemplateOidNotBetween(String value1, String value2) {
            addCriterion("TEMPLATE_OID not between", value1, value2, "templateOid");
            return (Criteria) this;
        }

        public Criteria andTemplateNameIsNull() {
            addCriterion("TEMPLATE_NAME is null");
            return (Criteria) this;
        }

        public Criteria andTemplateNameIsNotNull() {
            addCriterion("TEMPLATE_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andTemplateNameEqualTo(String value) {
            addCriterion("TEMPLATE_NAME =", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameNotEqualTo(String value) {
            addCriterion("TEMPLATE_NAME <>", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameGreaterThan(String value) {
            addCriterion("TEMPLATE_NAME >", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameGreaterThanOrEqualTo(String value) {
            addCriterion("TEMPLATE_NAME >=", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameLessThan(String value) {
            addCriterion("TEMPLATE_NAME <", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameLessThanOrEqualTo(String value) {
            addCriterion("TEMPLATE_NAME <=", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameLike(String value) {
            addCriterion("TEMPLATE_NAME like", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameNotLike(String value) {
            addCriterion("TEMPLATE_NAME not like", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameIn(List<String> values) {
            addCriterion("TEMPLATE_NAME in", values, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameNotIn(List<String> values) {
            addCriterion("TEMPLATE_NAME not in", values, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameBetween(String value1, String value2) {
            addCriterion("TEMPLATE_NAME between", value1, value2, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameNotBetween(String value1, String value2) {
            addCriterion("TEMPLATE_NAME not between", value1, value2, "templateName");
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

        public Criteria andDesignOidIsNull() {
            addCriterion("DESIGN_OID is null");
            return (Criteria) this;
        }

        public Criteria andDesignOidIsNotNull() {
            addCriterion("DESIGN_OID is not null");
            return (Criteria) this;
        }

        public Criteria andDesignOidEqualTo(String value) {
            addCriterion("DESIGN_OID =", value, "designOid");
            return (Criteria) this;
        }

        public Criteria andDesignOidNotEqualTo(String value) {
            addCriterion("DESIGN_OID <>", value, "designOid");
            return (Criteria) this;
        }

        public Criteria andDesignOidGreaterThan(String value) {
            addCriterion("DESIGN_OID >", value, "designOid");
            return (Criteria) this;
        }

        public Criteria andDesignOidGreaterThanOrEqualTo(String value) {
            addCriterion("DESIGN_OID >=", value, "designOid");
            return (Criteria) this;
        }

        public Criteria andDesignOidLessThan(String value) {
            addCriterion("DESIGN_OID <", value, "designOid");
            return (Criteria) this;
        }

        public Criteria andDesignOidLessThanOrEqualTo(String value) {
            addCriterion("DESIGN_OID <=", value, "designOid");
            return (Criteria) this;
        }

        public Criteria andDesignOidLike(String value) {
            addCriterion("DESIGN_OID like", value, "designOid");
            return (Criteria) this;
        }

        public Criteria andDesignOidNotLike(String value) {
            addCriterion("DESIGN_OID not like", value, "designOid");
            return (Criteria) this;
        }

        public Criteria andDesignOidIn(List<String> values) {
            addCriterion("DESIGN_OID in", values, "designOid");
            return (Criteria) this;
        }

        public Criteria andDesignOidNotIn(List<String> values) {
            addCriterion("DESIGN_OID not in", values, "designOid");
            return (Criteria) this;
        }

        public Criteria andDesignOidBetween(String value1, String value2) {
            addCriterion("DESIGN_OID between", value1, value2, "designOid");
            return (Criteria) this;
        }

        public Criteria andDesignOidNotBetween(String value1, String value2) {
            addCriterion("DESIGN_OID not between", value1, value2, "designOid");
            return (Criteria) this;
        }

        public Criteria andTemplateAttaIsNull() {
            addCriterion("TEMPLATE_ATTA is null");
            return (Criteria) this;
        }

        public Criteria andTemplateAttaIsNotNull() {
            addCriterion("TEMPLATE_ATTA is not null");
            return (Criteria) this;
        }

        public Criteria andTemplateAttaEqualTo(String value) {
            addCriterion("TEMPLATE_ATTA =", value, "templateAtta");
            return (Criteria) this;
        }

        public Criteria andTemplateAttaNotEqualTo(String value) {
            addCriterion("TEMPLATE_ATTA <>", value, "templateAtta");
            return (Criteria) this;
        }

        public Criteria andTemplateAttaGreaterThan(String value) {
            addCriterion("TEMPLATE_ATTA >", value, "templateAtta");
            return (Criteria) this;
        }

        public Criteria andTemplateAttaGreaterThanOrEqualTo(String value) {
            addCriterion("TEMPLATE_ATTA >=", value, "templateAtta");
            return (Criteria) this;
        }

        public Criteria andTemplateAttaLessThan(String value) {
            addCriterion("TEMPLATE_ATTA <", value, "templateAtta");
            return (Criteria) this;
        }

        public Criteria andTemplateAttaLessThanOrEqualTo(String value) {
            addCriterion("TEMPLATE_ATTA <=", value, "templateAtta");
            return (Criteria) this;
        }

        public Criteria andTemplateAttaLike(String value) {
            addCriterion("TEMPLATE_ATTA like", value, "templateAtta");
            return (Criteria) this;
        }

        public Criteria andTemplateAttaNotLike(String value) {
            addCriterion("TEMPLATE_ATTA not like", value, "templateAtta");
            return (Criteria) this;
        }

        public Criteria andTemplateAttaIn(List<String> values) {
            addCriterion("TEMPLATE_ATTA in", values, "templateAtta");
            return (Criteria) this;
        }

        public Criteria andTemplateAttaNotIn(List<String> values) {
            addCriterion("TEMPLATE_ATTA not in", values, "templateAtta");
            return (Criteria) this;
        }

        public Criteria andTemplateAttaBetween(String value1, String value2) {
            addCriterion("TEMPLATE_ATTA between", value1, value2, "templateAtta");
            return (Criteria) this;
        }

        public Criteria andTemplateAttaNotBetween(String value1, String value2) {
            addCriterion("TEMPLATE_ATTA not between", value1, value2, "templateAtta");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("REMARK is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("REMARK is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("REMARK =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("REMARK <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("REMARK >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("REMARK >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("REMARK <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("REMARK <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("REMARK like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("REMARK not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("REMARK in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("REMARK not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("REMARK between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("REMARK not between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andIsPublicIsNull() {
            addCriterion("IS_PUBLIC is null");
            return (Criteria) this;
        }

        public Criteria andIsPublicIsNotNull() {
            addCriterion("IS_PUBLIC is not null");
            return (Criteria) this;
        }

        public Criteria andIsPublicEqualTo(Integer value) {
            addCriterion("IS_PUBLIC =", value, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicNotEqualTo(Integer value) {
            addCriterion("IS_PUBLIC <>", value, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicGreaterThan(Integer value) {
            addCriterion("IS_PUBLIC >", value, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicGreaterThanOrEqualTo(Integer value) {
            addCriterion("IS_PUBLIC >=", value, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicLessThan(Integer value) {
            addCriterion("IS_PUBLIC <", value, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicLessThanOrEqualTo(Integer value) {
            addCriterion("IS_PUBLIC <=", value, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicIn(List<Integer> values) {
            addCriterion("IS_PUBLIC in", values, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicNotIn(List<Integer> values) {
            addCriterion("IS_PUBLIC not in", values, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicBetween(Integer value1, Integer value2) {
            addCriterion("IS_PUBLIC between", value1, value2, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicNotBetween(Integer value1, Integer value2) {
            addCriterion("IS_PUBLIC not between", value1, value2, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsFixedIsNull() {
            addCriterion("IS_FIXED is null");
            return (Criteria) this;
        }

        public Criteria andIsFixedIsNotNull() {
            addCriterion("IS_FIXED is not null");
            return (Criteria) this;
        }

        public Criteria andIsFixedEqualTo(Integer value) {
            addCriterion("IS_FIXED =", value, "isFixed");
            return (Criteria) this;
        }

        public Criteria andIsFixedNotEqualTo(Integer value) {
            addCriterion("IS_FIXED <>", value, "isFixed");
            return (Criteria) this;
        }

        public Criteria andIsFixedGreaterThan(Integer value) {
            addCriterion("IS_FIXED >", value, "isFixed");
            return (Criteria) this;
        }

        public Criteria andIsFixedGreaterThanOrEqualTo(Integer value) {
            addCriterion("IS_FIXED >=", value, "isFixed");
            return (Criteria) this;
        }

        public Criteria andIsFixedLessThan(Integer value) {
            addCriterion("IS_FIXED <", value, "isFixed");
            return (Criteria) this;
        }

        public Criteria andIsFixedLessThanOrEqualTo(Integer value) {
            addCriterion("IS_FIXED <=", value, "isFixed");
            return (Criteria) this;
        }

        public Criteria andIsFixedIn(List<Integer> values) {
            addCriterion("IS_FIXED in", values, "isFixed");
            return (Criteria) this;
        }

        public Criteria andIsFixedNotIn(List<Integer> values) {
            addCriterion("IS_FIXED not in", values, "isFixed");
            return (Criteria) this;
        }

        public Criteria andIsFixedBetween(Integer value1, Integer value2) {
            addCriterion("IS_FIXED between", value1, value2, "isFixed");
            return (Criteria) this;
        }

        public Criteria andIsFixedNotBetween(Integer value1, Integer value2) {
            addCriterion("IS_FIXED not between", value1, value2, "isFixed");
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

        public Criteria andTemplateConfigIsNotNull() {
            addCriterion("TEMPLATE_CONFIG is not null");
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
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_form_template
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
     * This class corresponds to the database table t_form_template
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