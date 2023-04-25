package com.zfsoft.superwindow.dbaccess.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbTerminalInfoExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    public DbTerminalInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
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
     * This method corresponds to the database table t_ahs_terminal_info
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
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
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
     * This class corresponds to the database table t_ahs_terminal_info
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

        public Criteria andTerminalCodeIsNull() {
            addCriterion("TERMINAL_CODE is null");
            return (Criteria) this;
        }

        public Criteria andTerminalCodeIsNotNull() {
            addCriterion("TERMINAL_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andTerminalCodeEqualTo(String value) {
            addCriterion("TERMINAL_CODE =", value, "terminalCode");
            return (Criteria) this;
        }

        public Criteria andTerminalCodeNotEqualTo(String value) {
            addCriterion("TERMINAL_CODE <>", value, "terminalCode");
            return (Criteria) this;
        }

        public Criteria andTerminalCodeGreaterThan(String value) {
            addCriterion("TERMINAL_CODE >", value, "terminalCode");
            return (Criteria) this;
        }

        public Criteria andTerminalCodeGreaterThanOrEqualTo(String value) {
            addCriterion("TERMINAL_CODE >=", value, "terminalCode");
            return (Criteria) this;
        }

        public Criteria andTerminalCodeLessThan(String value) {
            addCriterion("TERMINAL_CODE <", value, "terminalCode");
            return (Criteria) this;
        }

        public Criteria andTerminalCodeLessThanOrEqualTo(String value) {
            addCriterion("TERMINAL_CODE <=", value, "terminalCode");
            return (Criteria) this;
        }

        public Criteria andTerminalCodeLike(String value) {
            addCriterion("TERMINAL_CODE like", value, "terminalCode");
            return (Criteria) this;
        }

        public Criteria andTerminalCodeNotLike(String value) {
            addCriterion("TERMINAL_CODE not like", value, "terminalCode");
            return (Criteria) this;
        }

        public Criteria andTerminalCodeIn(List<String> values) {
            addCriterion("TERMINAL_CODE in", values, "terminalCode");
            return (Criteria) this;
        }

        public Criteria andTerminalCodeNotIn(List<String> values) {
            addCriterion("TERMINAL_CODE not in", values, "terminalCode");
            return (Criteria) this;
        }

        public Criteria andTerminalCodeBetween(String value1, String value2) {
            addCriterion("TERMINAL_CODE between", value1, value2, "terminalCode");
            return (Criteria) this;
        }

        public Criteria andTerminalCodeNotBetween(String value1, String value2) {
            addCriterion("TERMINAL_CODE not between", value1, value2, "terminalCode");
            return (Criteria) this;
        }

        public Criteria andTerminalTypeIsNull() {
            addCriterion("TERMINAL_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andTerminalTypeIsNotNull() {
            addCriterion("TERMINAL_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andTerminalTypeEqualTo(String value) {
            addCriterion("TERMINAL_TYPE =", value, "terminalType");
            return (Criteria) this;
        }

        public Criteria andTerminalTypeNotEqualTo(String value) {
            addCriterion("TERMINAL_TYPE <>", value, "terminalType");
            return (Criteria) this;
        }

        public Criteria andTerminalTypeGreaterThan(String value) {
            addCriterion("TERMINAL_TYPE >", value, "terminalType");
            return (Criteria) this;
        }

        public Criteria andTerminalTypeGreaterThanOrEqualTo(String value) {
            addCriterion("TERMINAL_TYPE >=", value, "terminalType");
            return (Criteria) this;
        }

        public Criteria andTerminalTypeLessThan(String value) {
            addCriterion("TERMINAL_TYPE <", value, "terminalType");
            return (Criteria) this;
        }

        public Criteria andTerminalTypeLessThanOrEqualTo(String value) {
            addCriterion("TERMINAL_TYPE <=", value, "terminalType");
            return (Criteria) this;
        }

        public Criteria andTerminalTypeLike(String value) {
            addCriterion("TERMINAL_TYPE like", value, "terminalType");
            return (Criteria) this;
        }

        public Criteria andTerminalTypeNotLike(String value) {
            addCriterion("TERMINAL_TYPE not like", value, "terminalType");
            return (Criteria) this;
        }

        public Criteria andTerminalTypeIn(List<String> values) {
            addCriterion("TERMINAL_TYPE in", values, "terminalType");
            return (Criteria) this;
        }

        public Criteria andTerminalTypeNotIn(List<String> values) {
            addCriterion("TERMINAL_TYPE not in", values, "terminalType");
            return (Criteria) this;
        }

        public Criteria andTerminalTypeBetween(String value1, String value2) {
            addCriterion("TERMINAL_TYPE between", value1, value2, "terminalType");
            return (Criteria) this;
        }

        public Criteria andTerminalTypeNotBetween(String value1, String value2) {
            addCriterion("TERMINAL_TYPE not between", value1, value2, "terminalType");
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

        public Criteria andPointOidIsNull() {
            addCriterion("POINT_OID is null");
            return (Criteria) this;
        }

        public Criteria andPointOidIsNotNull() {
            addCriterion("POINT_OID is not null");
            return (Criteria) this;
        }

        public Criteria andPointOidEqualTo(String value) {
            addCriterion("POINT_OID =", value, "pointOid");
            return (Criteria) this;
        }

        public Criteria andPointOidNotEqualTo(String value) {
            addCriterion("POINT_OID <>", value, "pointOid");
            return (Criteria) this;
        }

        public Criteria andPointOidGreaterThan(String value) {
            addCriterion("POINT_OID >", value, "pointOid");
            return (Criteria) this;
        }

        public Criteria andPointOidGreaterThanOrEqualTo(String value) {
            addCriterion("POINT_OID >=", value, "pointOid");
            return (Criteria) this;
        }

        public Criteria andPointOidLessThan(String value) {
            addCriterion("POINT_OID <", value, "pointOid");
            return (Criteria) this;
        }

        public Criteria andPointOidLessThanOrEqualTo(String value) {
            addCriterion("POINT_OID <=", value, "pointOid");
            return (Criteria) this;
        }

        public Criteria andPointOidLike(String value) {
            addCriterion("POINT_OID like", value, "pointOid");
            return (Criteria) this;
        }

        public Criteria andPointOidNotLike(String value) {
            addCriterion("POINT_OID not like", value, "pointOid");
            return (Criteria) this;
        }

        public Criteria andPointOidIn(List<String> values) {
            addCriterion("POINT_OID in", values, "pointOid");
            return (Criteria) this;
        }

        public Criteria andPointOidNotIn(List<String> values) {
            addCriterion("POINT_OID not in", values, "pointOid");
            return (Criteria) this;
        }

        public Criteria andPointOidBetween(String value1, String value2) {
            addCriterion("POINT_OID between", value1, value2, "pointOid");
            return (Criteria) this;
        }

        public Criteria andPointOidNotBetween(String value1, String value2) {
            addCriterion("POINT_OID not between", value1, value2, "pointOid");
            return (Criteria) this;
        }

        public Criteria andUnitIsNull() {
            addCriterion("UNIT is null");
            return (Criteria) this;
        }

        public Criteria andUnitIsNotNull() {
            addCriterion("UNIT is not null");
            return (Criteria) this;
        }

        public Criteria andUnitEqualTo(String value) {
            addCriterion("UNIT =", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitNotEqualTo(String value) {
            addCriterion("UNIT <>", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitGreaterThan(String value) {
            addCriterion("UNIT >", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitGreaterThanOrEqualTo(String value) {
            addCriterion("UNIT >=", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitLessThan(String value) {
            addCriterion("UNIT <", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitLessThanOrEqualTo(String value) {
            addCriterion("UNIT <=", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitLike(String value) {
            addCriterion("UNIT like", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitNotLike(String value) {
            addCriterion("UNIT not like", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitIn(List<String> values) {
            addCriterion("UNIT in", values, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitNotIn(List<String> values) {
            addCriterion("UNIT not in", values, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitBetween(String value1, String value2) {
            addCriterion("UNIT between", value1, value2, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitNotBetween(String value1, String value2) {
            addCriterion("UNIT not between", value1, value2, "unit");
            return (Criteria) this;
        }

        public Criteria andPeripheralIsNull() {
            addCriterion("PERIPHERAL is null");
            return (Criteria) this;
        }

        public Criteria andPeripheralIsNotNull() {
            addCriterion("PERIPHERAL is not null");
            return (Criteria) this;
        }

        public Criteria andPeripheralEqualTo(String value) {
            addCriterion("PERIPHERAL =", value, "peripheral");
            return (Criteria) this;
        }

        public Criteria andPeripheralNotEqualTo(String value) {
            addCriterion("PERIPHERAL <>", value, "peripheral");
            return (Criteria) this;
        }

        public Criteria andPeripheralGreaterThan(String value) {
            addCriterion("PERIPHERAL >", value, "peripheral");
            return (Criteria) this;
        }

        public Criteria andPeripheralGreaterThanOrEqualTo(String value) {
            addCriterion("PERIPHERAL >=", value, "peripheral");
            return (Criteria) this;
        }

        public Criteria andPeripheralLessThan(String value) {
            addCriterion("PERIPHERAL <", value, "peripheral");
            return (Criteria) this;
        }

        public Criteria andPeripheralLessThanOrEqualTo(String value) {
            addCriterion("PERIPHERAL <=", value, "peripheral");
            return (Criteria) this;
        }

        public Criteria andPeripheralLike(String value) {
            addCriterion("PERIPHERAL like", value, "peripheral");
            return (Criteria) this;
        }

        public Criteria andPeripheralNotLike(String value) {
            addCriterion("PERIPHERAL not like", value, "peripheral");
            return (Criteria) this;
        }

        public Criteria andPeripheralIn(List<String> values) {
            addCriterion("PERIPHERAL in", values, "peripheral");
            return (Criteria) this;
        }

        public Criteria andPeripheralNotIn(List<String> values) {
            addCriterion("PERIPHERAL not in", values, "peripheral");
            return (Criteria) this;
        }

        public Criteria andPeripheralBetween(String value1, String value2) {
            addCriterion("PERIPHERAL between", value1, value2, "peripheral");
            return (Criteria) this;
        }

        public Criteria andPeripheralNotBetween(String value1, String value2) {
            addCriterion("PERIPHERAL not between", value1, value2, "peripheral");
            return (Criteria) this;
        }

        public Criteria andDeleteStatusIsNull() {
            addCriterion("DELETE_STATUS is null");
            return (Criteria) this;
        }

        public Criteria andDeleteStatusIsNotNull() {
            addCriterion("DELETE_STATUS is not null");
            return (Criteria) this;
        }

        public Criteria andDeleteStatusEqualTo(String value) {
            addCriterion("DELETE_STATUS =", value, "deleteStatus");
            return (Criteria) this;
        }

        public Criteria andDeleteStatusNotEqualTo(String value) {
            addCriterion("DELETE_STATUS <>", value, "deleteStatus");
            return (Criteria) this;
        }

        public Criteria andDeleteStatusGreaterThan(String value) {
            addCriterion("DELETE_STATUS >", value, "deleteStatus");
            return (Criteria) this;
        }

        public Criteria andDeleteStatusGreaterThanOrEqualTo(String value) {
            addCriterion("DELETE_STATUS >=", value, "deleteStatus");
            return (Criteria) this;
        }

        public Criteria andDeleteStatusLessThan(String value) {
            addCriterion("DELETE_STATUS <", value, "deleteStatus");
            return (Criteria) this;
        }

        public Criteria andDeleteStatusLessThanOrEqualTo(String value) {
            addCriterion("DELETE_STATUS <=", value, "deleteStatus");
            return (Criteria) this;
        }

        public Criteria andDeleteStatusLike(String value) {
            addCriterion("DELETE_STATUS like", value, "deleteStatus");
            return (Criteria) this;
        }

        public Criteria andDeleteStatusNotLike(String value) {
            addCriterion("DELETE_STATUS not like", value, "deleteStatus");
            return (Criteria) this;
        }

        public Criteria andDeleteStatusIn(List<String> values) {
            addCriterion("DELETE_STATUS in", values, "deleteStatus");
            return (Criteria) this;
        }

        public Criteria andDeleteStatusNotIn(List<String> values) {
            addCriterion("DELETE_STATUS not in", values, "deleteStatus");
            return (Criteria) this;
        }

        public Criteria andDeleteStatusBetween(String value1, String value2) {
            addCriterion("DELETE_STATUS between", value1, value2, "deleteStatus");
            return (Criteria) this;
        }

        public Criteria andDeleteStatusNotBetween(String value1, String value2) {
            addCriterion("DELETE_STATUS not between", value1, value2, "deleteStatus");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("STATUS is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("STATUS is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("STATUS =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("STATUS <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("STATUS >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("STATUS >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("STATUS <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("STATUS <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("STATUS like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("STATUS not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("STATUS in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("STATUS not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("STATUS between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("STATUS not between", value1, value2, "status");
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
     * This class corresponds to the database table t_ahs_terminal_info
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
     * This class corresponds to the database table t_ahs_terminal_info
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
