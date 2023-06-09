package com.zfsoft.single.dbaccess.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbFormFieldRelConfigExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    public DbFormFieldRelConfigExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
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
     * This method corresponds to the database table t_form_field_rel_config
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
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
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
     * This class corresponds to the database table t_form_field_rel_config
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

        public Criteria andOcrFieldOidIsNull() {
            addCriterion("OCR_FIELD_OID is null");
            return (Criteria) this;
        }

        public Criteria andOcrFieldOidIsNotNull() {
            addCriterion("OCR_FIELD_OID is not null");
            return (Criteria) this;
        }

        public Criteria andOcrFieldOidEqualTo(String value) {
            addCriterion("OCR_FIELD_OID =", value, "ocrFieldOid");
            return (Criteria) this;
        }

        public Criteria andOcrFieldOidNotEqualTo(String value) {
            addCriterion("OCR_FIELD_OID <>", value, "ocrFieldOid");
            return (Criteria) this;
        }

        public Criteria andOcrFieldOidGreaterThan(String value) {
            addCriterion("OCR_FIELD_OID >", value, "ocrFieldOid");
            return (Criteria) this;
        }

        public Criteria andOcrFieldOidGreaterThanOrEqualTo(String value) {
            addCriterion("OCR_FIELD_OID >=", value, "ocrFieldOid");
            return (Criteria) this;
        }

        public Criteria andOcrFieldOidLessThan(String value) {
            addCriterion("OCR_FIELD_OID <", value, "ocrFieldOid");
            return (Criteria) this;
        }

        public Criteria andOcrFieldOidLessThanOrEqualTo(String value) {
            addCriterion("OCR_FIELD_OID <=", value, "ocrFieldOid");
            return (Criteria) this;
        }

        public Criteria andOcrFieldOidLike(String value) {
            addCriterion("OCR_FIELD_OID like", value, "ocrFieldOid");
            return (Criteria) this;
        }

        public Criteria andOcrFieldOidNotLike(String value) {
            addCriterion("OCR_FIELD_OID not like", value, "ocrFieldOid");
            return (Criteria) this;
        }

        public Criteria andOcrFieldOidIn(List<String> values) {
            addCriterion("OCR_FIELD_OID in", values, "ocrFieldOid");
            return (Criteria) this;
        }

        public Criteria andOcrFieldOidNotIn(List<String> values) {
            addCriterion("OCR_FIELD_OID not in", values, "ocrFieldOid");
            return (Criteria) this;
        }

        public Criteria andOcrFieldOidBetween(String value1, String value2) {
            addCriterion("OCR_FIELD_OID between", value1, value2, "ocrFieldOid");
            return (Criteria) this;
        }

        public Criteria andOcrFieldOidNotBetween(String value1, String value2) {
            addCriterion("OCR_FIELD_OID not between", value1, value2, "ocrFieldOid");
            return (Criteria) this;
        }

        public Criteria andElecLiecenseFieldOidIsNull() {
            addCriterion("ELEC_LIECENSE_FIELD_OID is null");
            return (Criteria) this;
        }

        public Criteria andElecLiecenseFieldOidIsNotNull() {
            addCriterion("ELEC_LIECENSE_FIELD_OID is not null");
            return (Criteria) this;
        }

        public Criteria andElecLiecenseFieldOidEqualTo(String value) {
            addCriterion("ELEC_LIECENSE_FIELD_OID =", value, "elecLiecenseFieldOid");
            return (Criteria) this;
        }

        public Criteria andElecLiecenseFieldOidNotEqualTo(String value) {
            addCriterion("ELEC_LIECENSE_FIELD_OID <>", value, "elecLiecenseFieldOid");
            return (Criteria) this;
        }

        public Criteria andElecLiecenseFieldOidGreaterThan(String value) {
            addCriterion("ELEC_LIECENSE_FIELD_OID >", value, "elecLiecenseFieldOid");
            return (Criteria) this;
        }

        public Criteria andElecLiecenseFieldOidGreaterThanOrEqualTo(String value) {
            addCriterion("ELEC_LIECENSE_FIELD_OID >=", value, "elecLiecenseFieldOid");
            return (Criteria) this;
        }

        public Criteria andElecLiecenseFieldOidLessThan(String value) {
            addCriterion("ELEC_LIECENSE_FIELD_OID <", value, "elecLiecenseFieldOid");
            return (Criteria) this;
        }

        public Criteria andElecLiecenseFieldOidLessThanOrEqualTo(String value) {
            addCriterion("ELEC_LIECENSE_FIELD_OID <=", value, "elecLiecenseFieldOid");
            return (Criteria) this;
        }

        public Criteria andElecLiecenseFieldOidLike(String value) {
            addCriterion("ELEC_LIECENSE_FIELD_OID like", value, "elecLiecenseFieldOid");
            return (Criteria) this;
        }

        public Criteria andElecLiecenseFieldOidNotLike(String value) {
            addCriterion("ELEC_LIECENSE_FIELD_OID not like", value, "elecLiecenseFieldOid");
            return (Criteria) this;
        }

        public Criteria andElecLiecenseFieldOidIn(List<String> values) {
            addCriterion("ELEC_LIECENSE_FIELD_OID in", values, "elecLiecenseFieldOid");
            return (Criteria) this;
        }

        public Criteria andElecLiecenseFieldOidNotIn(List<String> values) {
            addCriterion("ELEC_LIECENSE_FIELD_OID not in", values, "elecLiecenseFieldOid");
            return (Criteria) this;
        }

        public Criteria andElecLiecenseFieldOidBetween(String value1, String value2) {
            addCriterion("ELEC_LIECENSE_FIELD_OID between", value1, value2, "elecLiecenseFieldOid");
            return (Criteria) this;
        }

        public Criteria andElecLiecenseFieldOidNotBetween(String value1, String value2) {
            addCriterion("ELEC_LIECENSE_FIELD_OID not between", value1, value2, "elecLiecenseFieldOid");
            return (Criteria) this;
        }

        public Criteria andBasicFormFieldOidIsNull() {
            addCriterion("BASIC_FORM_FIELD_OID is null");
            return (Criteria) this;
        }

        public Criteria andBasicFormFieldOidIsNotNull() {
            addCriterion("BASIC_FORM_FIELD_OID is not null");
            return (Criteria) this;
        }

        public Criteria andBasicFormFieldOidEqualTo(String value) {
            addCriterion("BASIC_FORM_FIELD_OID =", value, "basicFormFieldOid");
            return (Criteria) this;
        }

        public Criteria andBasicFormFieldOidNotEqualTo(String value) {
            addCriterion("BASIC_FORM_FIELD_OID <>", value, "basicFormFieldOid");
            return (Criteria) this;
        }

        public Criteria andBasicFormFieldOidGreaterThan(String value) {
            addCriterion("BASIC_FORM_FIELD_OID >", value, "basicFormFieldOid");
            return (Criteria) this;
        }

        public Criteria andBasicFormFieldOidGreaterThanOrEqualTo(String value) {
            addCriterion("BASIC_FORM_FIELD_OID >=", value, "basicFormFieldOid");
            return (Criteria) this;
        }

        public Criteria andBasicFormFieldOidLessThan(String value) {
            addCriterion("BASIC_FORM_FIELD_OID <", value, "basicFormFieldOid");
            return (Criteria) this;
        }

        public Criteria andBasicFormFieldOidLessThanOrEqualTo(String value) {
            addCriterion("BASIC_FORM_FIELD_OID <=", value, "basicFormFieldOid");
            return (Criteria) this;
        }

        public Criteria andBasicFormFieldOidLike(String value) {
            addCriterion("BASIC_FORM_FIELD_OID like", value, "basicFormFieldOid");
            return (Criteria) this;
        }

        public Criteria andBasicFormFieldOidNotLike(String value) {
            addCriterion("BASIC_FORM_FIELD_OID not like", value, "basicFormFieldOid");
            return (Criteria) this;
        }

        public Criteria andBasicFormFieldOidIn(List<String> values) {
            addCriterion("BASIC_FORM_FIELD_OID in", values, "basicFormFieldOid");
            return (Criteria) this;
        }

        public Criteria andBasicFormFieldOidNotIn(List<String> values) {
            addCriterion("BASIC_FORM_FIELD_OID not in", values, "basicFormFieldOid");
            return (Criteria) this;
        }

        public Criteria andBasicFormFieldOidBetween(String value1, String value2) {
            addCriterion("BASIC_FORM_FIELD_OID between", value1, value2, "basicFormFieldOid");
            return (Criteria) this;
        }

        public Criteria andBasicFormFieldOidNotBetween(String value1, String value2) {
            addCriterion("BASIC_FORM_FIELD_OID not between", value1, value2, "basicFormFieldOid");
            return (Criteria) this;
        }

        public Criteria andFillFieldOidIsNull() {
            addCriterion("FILL_FIELD_OID is null");
            return (Criteria) this;
        }

        public Criteria andFillFieldOidIsNotNull() {
            addCriterion("FILL_FIELD_OID is not null");
            return (Criteria) this;
        }

        public Criteria andFillFieldOidEqualTo(String value) {
            addCriterion("FILL_FIELD_OID =", value, "fillFieldOid");
            return (Criteria) this;
        }

        public Criteria andFillFieldOidNotEqualTo(String value) {
            addCriterion("FILL_FIELD_OID <>", value, "fillFieldOid");
            return (Criteria) this;
        }

        public Criteria andFillFieldOidGreaterThan(String value) {
            addCriterion("FILL_FIELD_OID >", value, "fillFieldOid");
            return (Criteria) this;
        }

        public Criteria andFillFieldOidGreaterThanOrEqualTo(String value) {
            addCriterion("FILL_FIELD_OID >=", value, "fillFieldOid");
            return (Criteria) this;
        }

        public Criteria andFillFieldOidLessThan(String value) {
            addCriterion("FILL_FIELD_OID <", value, "fillFieldOid");
            return (Criteria) this;
        }

        public Criteria andFillFieldOidLessThanOrEqualTo(String value) {
            addCriterion("FILL_FIELD_OID <=", value, "fillFieldOid");
            return (Criteria) this;
        }

        public Criteria andFillFieldOidLike(String value) {
            addCriterion("FILL_FIELD_OID like", value, "fillFieldOid");
            return (Criteria) this;
        }

        public Criteria andFillFieldOidNotLike(String value) {
            addCriterion("FILL_FIELD_OID not like", value, "fillFieldOid");
            return (Criteria) this;
        }

        public Criteria andFillFieldOidIn(List<String> values) {
            addCriterion("FILL_FIELD_OID in", values, "fillFieldOid");
            return (Criteria) this;
        }

        public Criteria andFillFieldOidNotIn(List<String> values) {
            addCriterion("FILL_FIELD_OID not in", values, "fillFieldOid");
            return (Criteria) this;
        }

        public Criteria andFillFieldOidBetween(String value1, String value2) {
            addCriterion("FILL_FIELD_OID between", value1, value2, "fillFieldOid");
            return (Criteria) this;
        }

        public Criteria andFillFieldOidNotBetween(String value1, String value2) {
            addCriterion("FILL_FIELD_OID not between", value1, value2, "fillFieldOid");
            return (Criteria) this;
        }

        public Criteria andFillTypeIsNull() {
            addCriterion("FILL_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andFillTypeIsNotNull() {
            addCriterion("FILL_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andFillTypeEqualTo(Integer value) {
            addCriterion("FILL_TYPE =", value, "fillType");
            return (Criteria) this;
        }

        public Criteria andFillTypeNotEqualTo(Integer value) {
            addCriterion("FILL_TYPE <>", value, "fillType");
            return (Criteria) this;
        }

        public Criteria andFillTypeGreaterThan(Integer value) {
            addCriterion("FILL_TYPE >", value, "fillType");
            return (Criteria) this;
        }

        public Criteria andFillTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("FILL_TYPE >=", value, "fillType");
            return (Criteria) this;
        }

        public Criteria andFillTypeLessThan(Integer value) {
            addCriterion("FILL_TYPE <", value, "fillType");
            return (Criteria) this;
        }

        public Criteria andFillTypeLessThanOrEqualTo(Integer value) {
            addCriterion("FILL_TYPE <=", value, "fillType");
            return (Criteria) this;
        }

        public Criteria andFillTypeIn(List<Integer> values) {
            addCriterion("FILL_TYPE in", values, "fillType");
            return (Criteria) this;
        }

        public Criteria andFillTypeNotIn(List<Integer> values) {
            addCriterion("FILL_TYPE not in", values, "fillType");
            return (Criteria) this;
        }

        public Criteria andFillTypeBetween(Integer value1, Integer value2) {
            addCriterion("FILL_TYPE between", value1, value2, "fillType");
            return (Criteria) this;
        }

        public Criteria andFillTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("FILL_TYPE not between", value1, value2, "fillType");
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

        public Criteria andDelFlagEqualTo(Integer value) {
            addCriterion("DEL_FLAG =", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotEqualTo(Integer value) {
            addCriterion("DEL_FLAG <>", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagGreaterThan(Integer value) {
            addCriterion("DEL_FLAG >", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("DEL_FLAG >=", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagLessThan(Integer value) {
            addCriterion("DEL_FLAG <", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagLessThanOrEqualTo(Integer value) {
            addCriterion("DEL_FLAG <=", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagIn(List<Integer> values) {
            addCriterion("DEL_FLAG in", values, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotIn(List<Integer> values) {
            addCriterion("DEL_FLAG not in", values, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagBetween(Integer value1, Integer value2) {
            addCriterion("DEL_FLAG between", value1, value2, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotBetween(Integer value1, Integer value2) {
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
     * This class corresponds to the database table t_form_field_rel_config
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
     * This class corresponds to the database table t_form_field_rel_config
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
