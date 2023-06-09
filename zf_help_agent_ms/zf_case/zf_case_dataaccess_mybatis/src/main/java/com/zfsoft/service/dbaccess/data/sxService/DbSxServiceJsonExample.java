package com.zfsoft.service.dbaccess.data.sxService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbSxServiceJsonExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_sx_service_json
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_sx_service_json
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_sx_service_json
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_json
     *
     * @mbggenerated
     */
    public DbSxServiceJsonExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_json
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_json
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_json
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_json
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_json
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_json
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_json
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
     * This method corresponds to the database table t_sx_service_json
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
     * This method corresponds to the database table t_sx_service_json
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_json
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
     * This class corresponds to the database table t_sx_service_json
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

        public Criteria andDirOidIsNull() {
            addCriterion("DIR_OID is null");
            return (Criteria) this;
        }

        public Criteria andDirOidIsNotNull() {
            addCriterion("DIR_OID is not null");
            return (Criteria) this;
        }

        public Criteria andDirOidEqualTo(String value) {
            addCriterion("DIR_OID =", value, "dirOid");
            return (Criteria) this;
        }

        public Criteria andDirOidNotEqualTo(String value) {
            addCriterion("DIR_OID <>", value, "dirOid");
            return (Criteria) this;
        }

        public Criteria andDirOidGreaterThan(String value) {
            addCriterion("DIR_OID >", value, "dirOid");
            return (Criteria) this;
        }

        public Criteria andDirOidGreaterThanOrEqualTo(String value) {
            addCriterion("DIR_OID >=", value, "dirOid");
            return (Criteria) this;
        }

        public Criteria andDirOidLessThan(String value) {
            addCriterion("DIR_OID <", value, "dirOid");
            return (Criteria) this;
        }

        public Criteria andDirOidLessThanOrEqualTo(String value) {
            addCriterion("DIR_OID <=", value, "dirOid");
            return (Criteria) this;
        }

        public Criteria andDirOidLike(String value) {
            addCriterion("DIR_OID like", value, "dirOid");
            return (Criteria) this;
        }

        public Criteria andDirOidNotLike(String value) {
            addCriterion("DIR_OID not like", value, "dirOid");
            return (Criteria) this;
        }

        public Criteria andDirOidIn(List<String> values) {
            addCriterion("DIR_OID in", values, "dirOid");
            return (Criteria) this;
        }

        public Criteria andDirOidNotIn(List<String> values) {
            addCriterion("DIR_OID not in", values, "dirOid");
            return (Criteria) this;
        }

        public Criteria andDirOidBetween(String value1, String value2) {
            addCriterion("DIR_OID between", value1, value2, "dirOid");
            return (Criteria) this;
        }

        public Criteria andDirOidNotBetween(String value1, String value2) {
            addCriterion("DIR_OID not between", value1, value2, "dirOid");
            return (Criteria) this;
        }

        public Criteria andDistrictOidIsNull() {
            addCriterion("DISTRICT_OID is null");
            return (Criteria) this;
        }

        public Criteria andDistrictOidIsNotNull() {
            addCriterion("DISTRICT_OID is not null");
            return (Criteria) this;
        }

        public Criteria andDistrictOidEqualTo(String value) {
            addCriterion("DISTRICT_OID =", value, "districtOid");
            return (Criteria) this;
        }

        public Criteria andDistrictOidNotEqualTo(String value) {
            addCriterion("DISTRICT_OID <>", value, "districtOid");
            return (Criteria) this;
        }

        public Criteria andDistrictOidGreaterThan(String value) {
            addCriterion("DISTRICT_OID >", value, "districtOid");
            return (Criteria) this;
        }

        public Criteria andDistrictOidGreaterThanOrEqualTo(String value) {
            addCriterion("DISTRICT_OID >=", value, "districtOid");
            return (Criteria) this;
        }

        public Criteria andDistrictOidLessThan(String value) {
            addCriterion("DISTRICT_OID <", value, "districtOid");
            return (Criteria) this;
        }

        public Criteria andDistrictOidLessThanOrEqualTo(String value) {
            addCriterion("DISTRICT_OID <=", value, "districtOid");
            return (Criteria) this;
        }

        public Criteria andDistrictOidLike(String value) {
            addCriterion("DISTRICT_OID like", value, "districtOid");
            return (Criteria) this;
        }

        public Criteria andDistrictOidNotLike(String value) {
            addCriterion("DISTRICT_OID not like", value, "districtOid");
            return (Criteria) this;
        }

        public Criteria andDistrictOidIn(List<String> values) {
            addCriterion("DISTRICT_OID in", values, "districtOid");
            return (Criteria) this;
        }

        public Criteria andDistrictOidNotIn(List<String> values) {
            addCriterion("DISTRICT_OID not in", values, "districtOid");
            return (Criteria) this;
        }

        public Criteria andDistrictOidBetween(String value1, String value2) {
            addCriterion("DISTRICT_OID between", value1, value2, "districtOid");
            return (Criteria) this;
        }

        public Criteria andDistrictOidNotBetween(String value1, String value2) {
            addCriterion("DISTRICT_OID not between", value1, value2, "districtOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidIsNull() {
            addCriterion("ORGAN_OID is null");
            return (Criteria) this;
        }

        public Criteria andOrganOidIsNotNull() {
            addCriterion("ORGAN_OID is not null");
            return (Criteria) this;
        }

        public Criteria andOrganOidEqualTo(String value) {
            addCriterion("ORGAN_OID =", value, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidNotEqualTo(String value) {
            addCriterion("ORGAN_OID <>", value, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidGreaterThan(String value) {
            addCriterion("ORGAN_OID >", value, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidGreaterThanOrEqualTo(String value) {
            addCriterion("ORGAN_OID >=", value, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidLessThan(String value) {
            addCriterion("ORGAN_OID <", value, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidLessThanOrEqualTo(String value) {
            addCriterion("ORGAN_OID <=", value, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidLike(String value) {
            addCriterion("ORGAN_OID like", value, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidNotLike(String value) {
            addCriterion("ORGAN_OID not like", value, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidIn(List<String> values) {
            addCriterion("ORGAN_OID in", values, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidNotIn(List<String> values) {
            addCriterion("ORGAN_OID not in", values, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidBetween(String value1, String value2) {
            addCriterion("ORGAN_OID between", value1, value2, "organOid");
            return (Criteria) this;
        }

        public Criteria andOrganOidNotBetween(String value1, String value2) {
            addCriterion("ORGAN_OID not between", value1, value2, "organOid");
            return (Criteria) this;
        }

        public Criteria andContentStatusIsNull() {
            addCriterion("CONTENT_STATUS is null");
            return (Criteria) this;
        }

        public Criteria andContentStatusIsNotNull() {
            addCriterion("CONTENT_STATUS is not null");
            return (Criteria) this;
        }

        public Criteria andContentStatusEqualTo(Integer value) {
            addCriterion("CONTENT_STATUS =", value, "contentStatus");
            return (Criteria) this;
        }

        public Criteria andContentStatusNotEqualTo(Integer value) {
            addCriterion("CONTENT_STATUS <>", value, "contentStatus");
            return (Criteria) this;
        }

        public Criteria andContentStatusGreaterThan(Integer value) {
            addCriterion("CONTENT_STATUS >", value, "contentStatus");
            return (Criteria) this;
        }

        public Criteria andContentStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("CONTENT_STATUS >=", value, "contentStatus");
            return (Criteria) this;
        }

        public Criteria andContentStatusLessThan(Integer value) {
            addCriterion("CONTENT_STATUS <", value, "contentStatus");
            return (Criteria) this;
        }

        public Criteria andContentStatusLessThanOrEqualTo(Integer value) {
            addCriterion("CONTENT_STATUS <=", value, "contentStatus");
            return (Criteria) this;
        }

        public Criteria andContentStatusIn(List<Integer> values) {
            addCriterion("CONTENT_STATUS in", values, "contentStatus");
            return (Criteria) this;
        }

        public Criteria andContentStatusNotIn(List<Integer> values) {
            addCriterion("CONTENT_STATUS not in", values, "contentStatus");
            return (Criteria) this;
        }

        public Criteria andContentStatusBetween(Integer value1, Integer value2) {
            addCriterion("CONTENT_STATUS between", value1, value2, "contentStatus");
            return (Criteria) this;
        }

        public Criteria andContentStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("CONTENT_STATUS not between", value1, value2, "contentStatus");
            return (Criteria) this;
        }

        public Criteria andContentVersionIsNull() {
            addCriterion("CONTENT_VERSION is null");
            return (Criteria) this;
        }

        public Criteria andContentVersionIsNotNull() {
            addCriterion("CONTENT_VERSION is not null");
            return (Criteria) this;
        }

        public Criteria andContentVersionEqualTo(Integer value) {
            addCriterion("CONTENT_VERSION =", value, "contentVersion");
            return (Criteria) this;
        }

        public Criteria andContentVersionNotEqualTo(Integer value) {
            addCriterion("CONTENT_VERSION <>", value, "contentVersion");
            return (Criteria) this;
        }

        public Criteria andContentVersionGreaterThan(Integer value) {
            addCriterion("CONTENT_VERSION >", value, "contentVersion");
            return (Criteria) this;
        }

        public Criteria andContentVersionGreaterThanOrEqualTo(Integer value) {
            addCriterion("CONTENT_VERSION >=", value, "contentVersion");
            return (Criteria) this;
        }

        public Criteria andContentVersionLessThan(Integer value) {
            addCriterion("CONTENT_VERSION <", value, "contentVersion");
            return (Criteria) this;
        }

        public Criteria andContentVersionLessThanOrEqualTo(Integer value) {
            addCriterion("CONTENT_VERSION <=", value, "contentVersion");
            return (Criteria) this;
        }

        public Criteria andContentVersionIn(List<Integer> values) {
            addCriterion("CONTENT_VERSION in", values, "contentVersion");
            return (Criteria) this;
        }

        public Criteria andContentVersionNotIn(List<Integer> values) {
            addCriterion("CONTENT_VERSION not in", values, "contentVersion");
            return (Criteria) this;
        }

        public Criteria andContentVersionBetween(Integer value1, Integer value2) {
            addCriterion("CONTENT_VERSION between", value1, value2, "contentVersion");
            return (Criteria) this;
        }

        public Criteria andContentVersionNotBetween(Integer value1, Integer value2) {
            addCriterion("CONTENT_VERSION not between", value1, value2, "contentVersion");
            return (Criteria) this;
        }

        public Criteria andImplementCodeIsNull() {
            addCriterion("IMPLEMENT_CODE is null");
            return (Criteria) this;
        }

        public Criteria andImplementCodeIsNotNull() {
            addCriterion("IMPLEMENT_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andImplementCodeEqualTo(String value) {
            addCriterion("IMPLEMENT_CODE =", value, "implementCode");
            return (Criteria) this;
        }

        public Criteria andImplementCodeNotEqualTo(String value) {
            addCriterion("IMPLEMENT_CODE <>", value, "implementCode");
            return (Criteria) this;
        }

        public Criteria andImplementCodeGreaterThan(String value) {
            addCriterion("IMPLEMENT_CODE >", value, "implementCode");
            return (Criteria) this;
        }

        public Criteria andImplementCodeGreaterThanOrEqualTo(String value) {
            addCriterion("IMPLEMENT_CODE >=", value, "implementCode");
            return (Criteria) this;
        }

        public Criteria andImplementCodeLessThan(String value) {
            addCriterion("IMPLEMENT_CODE <", value, "implementCode");
            return (Criteria) this;
        }

        public Criteria andImplementCodeLessThanOrEqualTo(String value) {
            addCriterion("IMPLEMENT_CODE <=", value, "implementCode");
            return (Criteria) this;
        }

        public Criteria andImplementCodeLike(String value) {
            addCriterion("IMPLEMENT_CODE like", value, "implementCode");
            return (Criteria) this;
        }

        public Criteria andImplementCodeNotLike(String value) {
            addCriterion("IMPLEMENT_CODE not like", value, "implementCode");
            return (Criteria) this;
        }

        public Criteria andImplementCodeIn(List<String> values) {
            addCriterion("IMPLEMENT_CODE in", values, "implementCode");
            return (Criteria) this;
        }

        public Criteria andImplementCodeNotIn(List<String> values) {
            addCriterion("IMPLEMENT_CODE not in", values, "implementCode");
            return (Criteria) this;
        }

        public Criteria andImplementCodeBetween(String value1, String value2) {
            addCriterion("IMPLEMENT_CODE between", value1, value2, "implementCode");
            return (Criteria) this;
        }

        public Criteria andImplementCodeNotBetween(String value1, String value2) {
            addCriterion("IMPLEMENT_CODE not between", value1, value2, "implementCode");
            return (Criteria) this;
        }

        public Criteria andServiceNameIsNull() {
            addCriterion("SERVICE_NAME is null");
            return (Criteria) this;
        }

        public Criteria andServiceNameIsNotNull() {
            addCriterion("SERVICE_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andServiceNameEqualTo(String value) {
            addCriterion("SERVICE_NAME =", value, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameNotEqualTo(String value) {
            addCriterion("SERVICE_NAME <>", value, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameGreaterThan(String value) {
            addCriterion("SERVICE_NAME >", value, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameGreaterThanOrEqualTo(String value) {
            addCriterion("SERVICE_NAME >=", value, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameLessThan(String value) {
            addCriterion("SERVICE_NAME <", value, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameLessThanOrEqualTo(String value) {
            addCriterion("SERVICE_NAME <=", value, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameLike(String value) {
            addCriterion("SERVICE_NAME like", value, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameNotLike(String value) {
            addCriterion("SERVICE_NAME not like", value, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameIn(List<String> values) {
            addCriterion("SERVICE_NAME in", values, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameNotIn(List<String> values) {
            addCriterion("SERVICE_NAME not in", values, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameBetween(String value1, String value2) {
            addCriterion("SERVICE_NAME between", value1, value2, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameNotBetween(String value1, String value2) {
            addCriterion("SERVICE_NAME not between", value1, value2, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceTypeOidIsNull() {
            addCriterion("SERVICE_TYPE_OID is null");
            return (Criteria) this;
        }

        public Criteria andServiceTypeOidIsNotNull() {
            addCriterion("SERVICE_TYPE_OID is not null");
            return (Criteria) this;
        }

        public Criteria andServiceTypeOidEqualTo(String value) {
            addCriterion("SERVICE_TYPE_OID =", value, "serviceTypeOid");
            return (Criteria) this;
        }

        public Criteria andServiceTypeOidNotEqualTo(String value) {
            addCriterion("SERVICE_TYPE_OID <>", value, "serviceTypeOid");
            return (Criteria) this;
        }

        public Criteria andServiceTypeOidGreaterThan(String value) {
            addCriterion("SERVICE_TYPE_OID >", value, "serviceTypeOid");
            return (Criteria) this;
        }

        public Criteria andServiceTypeOidGreaterThanOrEqualTo(String value) {
            addCriterion("SERVICE_TYPE_OID >=", value, "serviceTypeOid");
            return (Criteria) this;
        }

        public Criteria andServiceTypeOidLessThan(String value) {
            addCriterion("SERVICE_TYPE_OID <", value, "serviceTypeOid");
            return (Criteria) this;
        }

        public Criteria andServiceTypeOidLessThanOrEqualTo(String value) {
            addCriterion("SERVICE_TYPE_OID <=", value, "serviceTypeOid");
            return (Criteria) this;
        }

        public Criteria andServiceTypeOidLike(String value) {
            addCriterion("SERVICE_TYPE_OID like", value, "serviceTypeOid");
            return (Criteria) this;
        }

        public Criteria andServiceTypeOidNotLike(String value) {
            addCriterion("SERVICE_TYPE_OID not like", value, "serviceTypeOid");
            return (Criteria) this;
        }

        public Criteria andServiceTypeOidIn(List<String> values) {
            addCriterion("SERVICE_TYPE_OID in", values, "serviceTypeOid");
            return (Criteria) this;
        }

        public Criteria andServiceTypeOidNotIn(List<String> values) {
            addCriterion("SERVICE_TYPE_OID not in", values, "serviceTypeOid");
            return (Criteria) this;
        }

        public Criteria andServiceTypeOidBetween(String value1, String value2) {
            addCriterion("SERVICE_TYPE_OID between", value1, value2, "serviceTypeOid");
            return (Criteria) this;
        }

        public Criteria andServiceTypeOidNotBetween(String value1, String value2) {
            addCriterion("SERVICE_TYPE_OID not between", value1, value2, "serviceTypeOid");
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
     * This class corresponds to the database table t_sx_service_json
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
     * This class corresponds to the database table t_sx_service_json
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