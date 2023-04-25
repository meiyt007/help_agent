package com.zfsoft.single.dbaccess.data.ywbl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbQlCorrectionMaterialExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_ql_correction_material
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_ql_correction_material
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_ql_correction_material
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ql_correction_material
     *
     * @mbggenerated
     */
    public DbQlCorrectionMaterialExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ql_correction_material
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ql_correction_material
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ql_correction_material
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ql_correction_material
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ql_correction_material
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ql_correction_material
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ql_correction_material
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
     * This method corresponds to the database table t_ql_correction_material
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
     * This method corresponds to the database table t_ql_correction_material
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ql_correction_material
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
     * This class corresponds to the database table t_ql_correction_material
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

        public Criteria andCorrectionMaterialOidIsNull() {
            addCriterion("CORRECTION_MATERIAL_OID is null");
            return (Criteria) this;
        }

        public Criteria andCorrectionMaterialOidIsNotNull() {
            addCriterion("CORRECTION_MATERIAL_OID is not null");
            return (Criteria) this;
        }

        public Criteria andCorrectionMaterialOidEqualTo(String value) {
            addCriterion("CORRECTION_MATERIAL_OID =", value, "correctionMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionMaterialOidNotEqualTo(String value) {
            addCriterion("CORRECTION_MATERIAL_OID <>", value, "correctionMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionMaterialOidGreaterThan(String value) {
            addCriterion("CORRECTION_MATERIAL_OID >", value, "correctionMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionMaterialOidGreaterThanOrEqualTo(String value) {
            addCriterion("CORRECTION_MATERIAL_OID >=", value, "correctionMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionMaterialOidLessThan(String value) {
            addCriterion("CORRECTION_MATERIAL_OID <", value, "correctionMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionMaterialOidLessThanOrEqualTo(String value) {
            addCriterion("CORRECTION_MATERIAL_OID <=", value, "correctionMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionMaterialOidLike(String value) {
            addCriterion("CORRECTION_MATERIAL_OID like", value, "correctionMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionMaterialOidNotLike(String value) {
            addCriterion("CORRECTION_MATERIAL_OID not like", value, "correctionMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionMaterialOidIn(List<String> values) {
            addCriterion("CORRECTION_MATERIAL_OID in", values, "correctionMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionMaterialOidNotIn(List<String> values) {
            addCriterion("CORRECTION_MATERIAL_OID not in", values, "correctionMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionMaterialOidBetween(String value1, String value2) {
            addCriterion("CORRECTION_MATERIAL_OID between", value1, value2, "correctionMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionMaterialOidNotBetween(String value1, String value2) {
            addCriterion("CORRECTION_MATERIAL_OID not between", value1, value2, "correctionMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionOidIsNull() {
            addCriterion("CORRECTION_OID is null");
            return (Criteria) this;
        }

        public Criteria andCorrectionOidIsNotNull() {
            addCriterion("CORRECTION_OID is not null");
            return (Criteria) this;
        }

        public Criteria andCorrectionOidEqualTo(String value) {
            addCriterion("CORRECTION_OID =", value, "correctionOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionOidNotEqualTo(String value) {
            addCriterion("CORRECTION_OID <>", value, "correctionOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionOidGreaterThan(String value) {
            addCriterion("CORRECTION_OID >", value, "correctionOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionOidGreaterThanOrEqualTo(String value) {
            addCriterion("CORRECTION_OID >=", value, "correctionOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionOidLessThan(String value) {
            addCriterion("CORRECTION_OID <", value, "correctionOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionOidLessThanOrEqualTo(String value) {
            addCriterion("CORRECTION_OID <=", value, "correctionOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionOidLike(String value) {
            addCriterion("CORRECTION_OID like", value, "correctionOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionOidNotLike(String value) {
            addCriterion("CORRECTION_OID not like", value, "correctionOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionOidIn(List<String> values) {
            addCriterion("CORRECTION_OID in", values, "correctionOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionOidNotIn(List<String> values) {
            addCriterion("CORRECTION_OID not in", values, "correctionOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionOidBetween(String value1, String value2) {
            addCriterion("CORRECTION_OID between", value1, value2, "correctionOid");
            return (Criteria) this;
        }

        public Criteria andCorrectionOidNotBetween(String value1, String value2) {
            addCriterion("CORRECTION_OID not between", value1, value2, "correctionOid");
            return (Criteria) this;
        }

        public Criteria andCaseMaterialOidIsNull() {
            addCriterion("CASE_MATERIAL_OID is null");
            return (Criteria) this;
        }

        public Criteria andCaseMaterialOidIsNotNull() {
            addCriterion("CASE_MATERIAL_OID is not null");
            return (Criteria) this;
        }

        public Criteria andCaseMaterialOidEqualTo(String value) {
            addCriterion("CASE_MATERIAL_OID =", value, "caseMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCaseMaterialOidNotEqualTo(String value) {
            addCriterion("CASE_MATERIAL_OID <>", value, "caseMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCaseMaterialOidGreaterThan(String value) {
            addCriterion("CASE_MATERIAL_OID >", value, "caseMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCaseMaterialOidGreaterThanOrEqualTo(String value) {
            addCriterion("CASE_MATERIAL_OID >=", value, "caseMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCaseMaterialOidLessThan(String value) {
            addCriterion("CASE_MATERIAL_OID <", value, "caseMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCaseMaterialOidLessThanOrEqualTo(String value) {
            addCriterion("CASE_MATERIAL_OID <=", value, "caseMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCaseMaterialOidLike(String value) {
            addCriterion("CASE_MATERIAL_OID like", value, "caseMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCaseMaterialOidNotLike(String value) {
            addCriterion("CASE_MATERIAL_OID not like", value, "caseMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCaseMaterialOidIn(List<String> values) {
            addCriterion("CASE_MATERIAL_OID in", values, "caseMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCaseMaterialOidNotIn(List<String> values) {
            addCriterion("CASE_MATERIAL_OID not in", values, "caseMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCaseMaterialOidBetween(String value1, String value2) {
            addCriterion("CASE_MATERIAL_OID between", value1, value2, "caseMaterialOid");
            return (Criteria) this;
        }

        public Criteria andCaseMaterialOidNotBetween(String value1, String value2) {
            addCriterion("CASE_MATERIAL_OID not between", value1, value2, "caseMaterialOid");
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
     * This class corresponds to the database table t_ql_correction_material
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
     * This class corresponds to the database table t_ql_correction_material
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