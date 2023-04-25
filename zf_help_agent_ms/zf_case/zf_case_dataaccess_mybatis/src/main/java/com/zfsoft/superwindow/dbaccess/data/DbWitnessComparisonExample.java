package com.zfsoft.superwindow.dbaccess.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbWitnessComparisonExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_witness_comparison
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_witness_comparison
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_witness_comparison
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_witness_comparison
     *
     * @mbggenerated
     */
    public DbWitnessComparisonExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_witness_comparison
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_witness_comparison
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_witness_comparison
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_witness_comparison
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_witness_comparison
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_witness_comparison
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_witness_comparison
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
     * This method corresponds to the database table t_witness_comparison
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
     * This method corresponds to the database table t_witness_comparison
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_witness_comparison
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
     * This class corresponds to the database table t_witness_comparison
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

        public Criteria andCompareOidIsNull() {
            addCriterion("COMPARE_OID is null");
            return (Criteria) this;
        }

        public Criteria andCompareOidIsNotNull() {
            addCriterion("COMPARE_OID is not null");
            return (Criteria) this;
        }

        public Criteria andCompareOidEqualTo(String value) {
            addCriterion("COMPARE_OID =", value, "compareOid");
            return (Criteria) this;
        }

        public Criteria andCompareOidNotEqualTo(String value) {
            addCriterion("COMPARE_OID <>", value, "compareOid");
            return (Criteria) this;
        }

        public Criteria andCompareOidGreaterThan(String value) {
            addCriterion("COMPARE_OID >", value, "compareOid");
            return (Criteria) this;
        }

        public Criteria andCompareOidGreaterThanOrEqualTo(String value) {
            addCriterion("COMPARE_OID >=", value, "compareOid");
            return (Criteria) this;
        }

        public Criteria andCompareOidLessThan(String value) {
            addCriterion("COMPARE_OID <", value, "compareOid");
            return (Criteria) this;
        }

        public Criteria andCompareOidLessThanOrEqualTo(String value) {
            addCriterion("COMPARE_OID <=", value, "compareOid");
            return (Criteria) this;
        }

        public Criteria andCompareOidLike(String value) {
            addCriterion("COMPARE_OID like", value, "compareOid");
            return (Criteria) this;
        }

        public Criteria andCompareOidNotLike(String value) {
            addCriterion("COMPARE_OID not like", value, "compareOid");
            return (Criteria) this;
        }

        public Criteria andCompareOidIn(List<String> values) {
            addCriterion("COMPARE_OID in", values, "compareOid");
            return (Criteria) this;
        }

        public Criteria andCompareOidNotIn(List<String> values) {
            addCriterion("COMPARE_OID not in", values, "compareOid");
            return (Criteria) this;
        }

        public Criteria andCompareOidBetween(String value1, String value2) {
            addCriterion("COMPARE_OID between", value1, value2, "compareOid");
            return (Criteria) this;
        }

        public Criteria andCompareOidNotBetween(String value1, String value2) {
            addCriterion("COMPARE_OID not between", value1, value2, "compareOid");
            return (Criteria) this;
        }

        public Criteria andCaseOidIsNull() {
            addCriterion("CASE_OID is null");
            return (Criteria) this;
        }

        public Criteria andCaseOidIsNotNull() {
            addCriterion("CASE_OID is not null");
            return (Criteria) this;
        }

        public Criteria andCaseOidEqualTo(String value) {
            addCriterion("CASE_OID =", value, "caseOid");
            return (Criteria) this;
        }

        public Criteria andCaseOidNotEqualTo(String value) {
            addCriterion("CASE_OID <>", value, "caseOid");
            return (Criteria) this;
        }

        public Criteria andCaseOidGreaterThan(String value) {
            addCriterion("CASE_OID >", value, "caseOid");
            return (Criteria) this;
        }

        public Criteria andCaseOidGreaterThanOrEqualTo(String value) {
            addCriterion("CASE_OID >=", value, "caseOid");
            return (Criteria) this;
        }

        public Criteria andCaseOidLessThan(String value) {
            addCriterion("CASE_OID <", value, "caseOid");
            return (Criteria) this;
        }

        public Criteria andCaseOidLessThanOrEqualTo(String value) {
            addCriterion("CASE_OID <=", value, "caseOid");
            return (Criteria) this;
        }

        public Criteria andCaseOidLike(String value) {
            addCriterion("CASE_OID like", value, "caseOid");
            return (Criteria) this;
        }

        public Criteria andCaseOidNotLike(String value) {
            addCriterion("CASE_OID not like", value, "caseOid");
            return (Criteria) this;
        }

        public Criteria andCaseOidIn(List<String> values) {
            addCriterion("CASE_OID in", values, "caseOid");
            return (Criteria) this;
        }

        public Criteria andCaseOidNotIn(List<String> values) {
            addCriterion("CASE_OID not in", values, "caseOid");
            return (Criteria) this;
        }

        public Criteria andCaseOidBetween(String value1, String value2) {
            addCriterion("CASE_OID between", value1, value2, "caseOid");
            return (Criteria) this;
        }

        public Criteria andCaseOidNotBetween(String value1, String value2) {
            addCriterion("CASE_OID not between", value1, value2, "caseOid");
            return (Criteria) this;
        }

        public Criteria andImgAttaOidIsNull() {
            addCriterion("IMG_ATTA_OID is null");
            return (Criteria) this;
        }

        public Criteria andImgAttaOidIsNotNull() {
            addCriterion("IMG_ATTA_OID is not null");
            return (Criteria) this;
        }

        public Criteria andImgAttaOidEqualTo(String value) {
            addCriterion("IMG_ATTA_OID =", value, "imgAttaOid");
            return (Criteria) this;
        }

        public Criteria andImgAttaOidNotEqualTo(String value) {
            addCriterion("IMG_ATTA_OID <>", value, "imgAttaOid");
            return (Criteria) this;
        }

        public Criteria andImgAttaOidGreaterThan(String value) {
            addCriterion("IMG_ATTA_OID >", value, "imgAttaOid");
            return (Criteria) this;
        }

        public Criteria andImgAttaOidGreaterThanOrEqualTo(String value) {
            addCriterion("IMG_ATTA_OID >=", value, "imgAttaOid");
            return (Criteria) this;
        }

        public Criteria andImgAttaOidLessThan(String value) {
            addCriterion("IMG_ATTA_OID <", value, "imgAttaOid");
            return (Criteria) this;
        }

        public Criteria andImgAttaOidLessThanOrEqualTo(String value) {
            addCriterion("IMG_ATTA_OID <=", value, "imgAttaOid");
            return (Criteria) this;
        }

        public Criteria andImgAttaOidLike(String value) {
            addCriterion("IMG_ATTA_OID like", value, "imgAttaOid");
            return (Criteria) this;
        }

        public Criteria andImgAttaOidNotLike(String value) {
            addCriterion("IMG_ATTA_OID not like", value, "imgAttaOid");
            return (Criteria) this;
        }

        public Criteria andImgAttaOidIn(List<String> values) {
            addCriterion("IMG_ATTA_OID in", values, "imgAttaOid");
            return (Criteria) this;
        }

        public Criteria andImgAttaOidNotIn(List<String> values) {
            addCriterion("IMG_ATTA_OID not in", values, "imgAttaOid");
            return (Criteria) this;
        }

        public Criteria andImgAttaOidBetween(String value1, String value2) {
            addCriterion("IMG_ATTA_OID between", value1, value2, "imgAttaOid");
            return (Criteria) this;
        }

        public Criteria andImgAttaOidNotBetween(String value1, String value2) {
            addCriterion("IMG_ATTA_OID not between", value1, value2, "imgAttaOid");
            return (Criteria) this;
        }

        public Criteria andIdcardAttaOidIsNull() {
            addCriterion("IDCARD_ATTA_OID is null");
            return (Criteria) this;
        }

        public Criteria andIdcardAttaOidIsNotNull() {
            addCriterion("IDCARD_ATTA_OID is not null");
            return (Criteria) this;
        }

        public Criteria andIdcardAttaOidEqualTo(String value) {
            addCriterion("IDCARD_ATTA_OID =", value, "idcardAttaOid");
            return (Criteria) this;
        }

        public Criteria andIdcardAttaOidNotEqualTo(String value) {
            addCriterion("IDCARD_ATTA_OID <>", value, "idcardAttaOid");
            return (Criteria) this;
        }

        public Criteria andIdcardAttaOidGreaterThan(String value) {
            addCriterion("IDCARD_ATTA_OID >", value, "idcardAttaOid");
            return (Criteria) this;
        }

        public Criteria andIdcardAttaOidGreaterThanOrEqualTo(String value) {
            addCriterion("IDCARD_ATTA_OID >=", value, "idcardAttaOid");
            return (Criteria) this;
        }

        public Criteria andIdcardAttaOidLessThan(String value) {
            addCriterion("IDCARD_ATTA_OID <", value, "idcardAttaOid");
            return (Criteria) this;
        }

        public Criteria andIdcardAttaOidLessThanOrEqualTo(String value) {
            addCriterion("IDCARD_ATTA_OID <=", value, "idcardAttaOid");
            return (Criteria) this;
        }

        public Criteria andIdcardAttaOidLike(String value) {
            addCriterion("IDCARD_ATTA_OID like", value, "idcardAttaOid");
            return (Criteria) this;
        }

        public Criteria andIdcardAttaOidNotLike(String value) {
            addCriterion("IDCARD_ATTA_OID not like", value, "idcardAttaOid");
            return (Criteria) this;
        }

        public Criteria andIdcardAttaOidIn(List<String> values) {
            addCriterion("IDCARD_ATTA_OID in", values, "idcardAttaOid");
            return (Criteria) this;
        }

        public Criteria andIdcardAttaOidNotIn(List<String> values) {
            addCriterion("IDCARD_ATTA_OID not in", values, "idcardAttaOid");
            return (Criteria) this;
        }

        public Criteria andIdcardAttaOidBetween(String value1, String value2) {
            addCriterion("IDCARD_ATTA_OID between", value1, value2, "idcardAttaOid");
            return (Criteria) this;
        }

        public Criteria andIdcardAttaOidNotBetween(String value1, String value2) {
            addCriterion("IDCARD_ATTA_OID not between", value1, value2, "idcardAttaOid");
            return (Criteria) this;
        }

        public Criteria andDistanceIsNull() {
            addCriterion("DISTANCE is null");
            return (Criteria) this;
        }

        public Criteria andDistanceIsNotNull() {
            addCriterion("DISTANCE is not null");
            return (Criteria) this;
        }

        public Criteria andDistanceEqualTo(String value) {
            addCriterion("DISTANCE =", value, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceNotEqualTo(String value) {
            addCriterion("DISTANCE <>", value, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceGreaterThan(String value) {
            addCriterion("DISTANCE >", value, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceGreaterThanOrEqualTo(String value) {
            addCriterion("DISTANCE >=", value, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceLessThan(String value) {
            addCriterion("DISTANCE <", value, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceLessThanOrEqualTo(String value) {
            addCriterion("DISTANCE <=", value, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceLike(String value) {
            addCriterion("DISTANCE like", value, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceNotLike(String value) {
            addCriterion("DISTANCE not like", value, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceIn(List<String> values) {
            addCriterion("DISTANCE in", values, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceNotIn(List<String> values) {
            addCriterion("DISTANCE not in", values, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceBetween(String value1, String value2) {
            addCriterion("DISTANCE between", value1, value2, "distance");
            return (Criteria) this;
        }

        public Criteria andDistanceNotBetween(String value1, String value2) {
            addCriterion("DISTANCE not between", value1, value2, "distance");
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

        public Criteria andCaseTypeIsNull() {
            addCriterion("CASE_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andCaseTypeIsNotNull() {
            addCriterion("CASE_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andCaseTypeEqualTo(Integer value) {
            addCriterion("CASE_TYPE =", value, "caseType");
            return (Criteria) this;
        }

        public Criteria andCaseTypeNotEqualTo(Integer value) {
            addCriterion("CASE_TYPE <>", value, "caseType");
            return (Criteria) this;
        }

        public Criteria andCaseTypeGreaterThan(Integer value) {
            addCriterion("CASE_TYPE >", value, "caseType");
            return (Criteria) this;
        }

        public Criteria andCaseTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("CASE_TYPE >=", value, "caseType");
            return (Criteria) this;
        }

        public Criteria andCaseTypeLessThan(Integer value) {
            addCriterion("CASE_TYPE <", value, "caseType");
            return (Criteria) this;
        }

        public Criteria andCaseTypeLessThanOrEqualTo(Integer value) {
            addCriterion("CASE_TYPE <=", value, "caseType");
            return (Criteria) this;
        }

        public Criteria andCaseTypeIn(List<Integer> values) {
            addCriterion("CASE_TYPE in", values, "caseType");
            return (Criteria) this;
        }

        public Criteria andCaseTypeNotIn(List<Integer> values) {
            addCriterion("CASE_TYPE not in", values, "caseType");
            return (Criteria) this;
        }

        public Criteria andCaseTypeBetween(Integer value1, Integer value2) {
            addCriterion("CASE_TYPE between", value1, value2, "caseType");
            return (Criteria) this;
        }

        public Criteria andCaseTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("CASE_TYPE not between", value1, value2, "caseType");
            return (Criteria) this;
        }

        public Criteria andCompareResultIsNull() {
            addCriterion("COMPARE_RESULT is null");
            return (Criteria) this;
        }

        public Criteria andCompareResultIsNotNull() {
            addCriterion("COMPARE_RESULT is not null");
            return (Criteria) this;
        }

        public Criteria andCompareResultEqualTo(Integer value) {
            addCriterion("COMPARE_RESULT =", value, "compareResult");
            return (Criteria) this;
        }

        public Criteria andCompareResultNotEqualTo(Integer value) {
            addCriterion("COMPARE_RESULT <>", value, "compareResult");
            return (Criteria) this;
        }

        public Criteria andCompareResultGreaterThan(Integer value) {
            addCriterion("COMPARE_RESULT >", value, "compareResult");
            return (Criteria) this;
        }

        public Criteria andCompareResultGreaterThanOrEqualTo(Integer value) {
            addCriterion("COMPARE_RESULT >=", value, "compareResult");
            return (Criteria) this;
        }

        public Criteria andCompareResultLessThan(Integer value) {
            addCriterion("COMPARE_RESULT <", value, "compareResult");
            return (Criteria) this;
        }

        public Criteria andCompareResultLessThanOrEqualTo(Integer value) {
            addCriterion("COMPARE_RESULT <=", value, "compareResult");
            return (Criteria) this;
        }

        public Criteria andCompareResultIn(List<Integer> values) {
            addCriterion("COMPARE_RESULT in", values, "compareResult");
            return (Criteria) this;
        }

        public Criteria andCompareResultNotIn(List<Integer> values) {
            addCriterion("COMPARE_RESULT not in", values, "compareResult");
            return (Criteria) this;
        }

        public Criteria andCompareResultBetween(Integer value1, Integer value2) {
            addCriterion("COMPARE_RESULT between", value1, value2, "compareResult");
            return (Criteria) this;
        }

        public Criteria andCompareResultNotBetween(Integer value1, Integer value2) {
            addCriterion("COMPARE_RESULT not between", value1, value2, "compareResult");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table t_witness_comparison
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
     * This class corresponds to the database table t_witness_comparison
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