package com.zfsoft.cases.dbaccess.data.example;

import com.zfsoft.cases.dbaccess.utils.Criteria;
import com.zfsoft.cases.dbaccess.utils.GeneratedCriterion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbSysConfigExample {

    private List<Criteria> criteriaList;

    public DbSysConfigExample() {
        criteriaList = new ArrayList<Criteria>();
    }

    public DbSysConfigExample andOidIsNull() {
        GeneratedCriterion.addCriterion("OID is null");
        return this;
    }

    public DbSysConfigExample andOidIsNotNull() {
        GeneratedCriterion.addCriterion("OID is not null");
        return this;
    }

    public DbSysConfigExample andOidEqualTo(Long value) {
        GeneratedCriterion.addCriterion("OID =", value, "oid");
        return this;
    }

    public DbSysConfigExample andOidNotEqualTo(Long value) {
        GeneratedCriterion.addCriterion("OID <>", value, "oid");
        return this;
    }

    public DbSysConfigExample andOidGreaterThan(Long value) {
        GeneratedCriterion.addCriterion("OID >", value, "oid");
        return this;
    }

    public DbSysConfigExample andOidGreaterThanOrEqualTo(Long value) {
        GeneratedCriterion.addCriterion("OID >=", value, "oid");
        return this;
    }

    public DbSysConfigExample andOidLessThan(Long value) {
        GeneratedCriterion.addCriterion("OID <", value, "oid");
        return this;
    }

    public DbSysConfigExample andOidLessThanOrEqualTo(Long value) {
        GeneratedCriterion.addCriterion("OID <=", value, "oid");
        return this;
    }

    public DbSysConfigExample andOidIn(List<Long> values) {
        GeneratedCriterion.addCriterion("OID in", values, "oid");
        return this;
    }

    public DbSysConfigExample andOidNotIn(List<Long> values) {
        GeneratedCriterion.addCriterion("OID not in", values, "oid");
        return this;
    }

    public DbSysConfigExample andOidBetween(Long value1, Long value2) {
        GeneratedCriterion.addCriterion("OID between", value1, value2, "oid");
        return this;
    }

    public DbSysConfigExample andOidNotBetween(Long value1, Long value2) {
        GeneratedCriterion.addCriterion("OID not between", value1, value2, "oid");
        return this;
    }

    public DbSysConfigExample andCodeIsNull() {
        GeneratedCriterion.addCriterion("CODE is null");
        return this;
    }

    public DbSysConfigExample andCodeIsNotNull() {
        GeneratedCriterion.addCriterion("CODE is not null");
        return this;
    }

    public DbSysConfigExample andCodeEqualTo(String value) {
        GeneratedCriterion.addCriterion("CODE =", value, "code");
        return this;
    }

    public DbSysConfigExample andCodeNotEqualTo(String value) {
        GeneratedCriterion.addCriterion("CODE <>", value, "code");
        return this;
    }

    public DbSysConfigExample andCodeGreaterThan(String value) {
        GeneratedCriterion.addCriterion("CODE >", value, "code");
        return this;
    }

    public DbSysConfigExample andCodeGreaterThanOrEqualTo(String value) {
        GeneratedCriterion.addCriterion("CODE >=", value, "code");
        return this;
    }

    public DbSysConfigExample andCodeLessThan(String value) {
        GeneratedCriterion.addCriterion("CODE <", value, "code");
        return this;
    }

    public DbSysConfigExample andCodeLessThanOrEqualTo(String value) {
        GeneratedCriterion.addCriterion("CODE <=", value, "code");
        return this;
    }

    public DbSysConfigExample andCodeLike(String value) {
        GeneratedCriterion.addCriterion("CODE like", value, "code");
        return this;
    }

    public DbSysConfigExample andCodeNotLike(String value) {
        GeneratedCriterion.addCriterion("CODE not like", value, "code");
        return this;
    }

    public DbSysConfigExample andCodeIn(List<String> values) {
        GeneratedCriterion.addCriterion("CODE in", values, "code");
        return this;
    }

    public DbSysConfigExample andCodeNotIn(List<String> values) {
        GeneratedCriterion.addCriterion("CODE not in", values, "code");
        return this;
    }

    public DbSysConfigExample andCodeBetween(String value1, String value2) {
        GeneratedCriterion.addCriterion("CODE between", value1, value2, "code");
        return this;
    }

    public DbSysConfigExample andCodeNotBetween(String value1, String value2) {
        GeneratedCriterion.addCriterion("CODE not between", value1, value2, "code");
        return this;
    }

    public DbSysConfigExample andNameIsNull() {
        GeneratedCriterion.addCriterion("NAME is null");
        return this;
    }

    public DbSysConfigExample andNameIsNotNull() {
        GeneratedCriterion.addCriterion("NAME is not null");
        return this;
    }

    public DbSysConfigExample andNameEqualTo(String value) {
        GeneratedCriterion.addCriterion("NAME =", value, "name");
        return this;
    }

    public DbSysConfigExample andNameNotEqualTo(String value) {
        GeneratedCriterion.addCriterion("NAME <>", value, "name");
        return this;
    }

    public DbSysConfigExample andNameGreaterThan(String value) {
        GeneratedCriterion.addCriterion("NAME >", value, "name");
        return this;
    }

    public DbSysConfigExample andNameGreaterThanOrEqualTo(String value) {
        GeneratedCriterion.addCriterion("NAME >=", value, "name");
        return this;
    }

    public DbSysConfigExample andNameLessThan(String value) {
        GeneratedCriterion.addCriterion("NAME <", value, "name");
        return this;
    }

    public DbSysConfigExample andNameLessThanOrEqualTo(String value) {
        GeneratedCriterion.addCriterion("NAME <=", value, "name");
        return this;
    }

    public DbSysConfigExample andNameLike(String value) {
        GeneratedCriterion.addCriterion("NAME like", value, "name");
        return this;
    }

    public DbSysConfigExample andNameNotLike(String value) {
        GeneratedCriterion.addCriterion("NAME not like", value, "name");
        return this;
    }

    public DbSysConfigExample andNameIn(List<String> values) {
        GeneratedCriterion.addCriterion("NAME in", values, "name");
        return this;
    }

    public DbSysConfigExample andNameNotIn(List<String> values) {
        GeneratedCriterion.addCriterion("NAME not in", values, "name");
        return this;
    }

    public DbSysConfigExample andNameBetween(String value1, String value2) {
        GeneratedCriterion.addCriterion("NAME between", value1, value2, "name");
        return this;
    }

    public DbSysConfigExample andNameNotBetween(String value1, String value2) {
        GeneratedCriterion.addCriterion("NAME not between", value1, value2, "name");
        return this;
    }

    public DbSysConfigExample andMemoIsNull() {
        GeneratedCriterion.addCriterion("MEMO is null");
        return this;
    }

    public DbSysConfigExample andMemoIsNotNull() {
        GeneratedCriterion.addCriterion("MEMO is not null");
        return this;
    }

    public DbSysConfigExample andMemoEqualTo(String value) {
        GeneratedCriterion.addCriterion("MEMO =", value, "memo");
        return this;
    }

    public DbSysConfigExample andMemoNotEqualTo(String value) {
        GeneratedCriterion.addCriterion("MEMO <>", value, "memo");
        return this;
    }

    public DbSysConfigExample andMemoGreaterThan(String value) {
        GeneratedCriterion.addCriterion("MEMO >", value, "memo");
        return this;
    }

    public DbSysConfigExample andMemoGreaterThanOrEqualTo(String value) {
        GeneratedCriterion.addCriterion("MEMO >=", value, "memo");
        return this;
    }

    public DbSysConfigExample andMemoLessThan(String value) {
        GeneratedCriterion.addCriterion("MEMO <", value, "memo");
        return this;
    }

    public DbSysConfigExample andMemoLessThanOrEqualTo(String value) {
        GeneratedCriterion.addCriterion("MEMO <=", value, "memo");
        return this;
    }

    public DbSysConfigExample andMemoLike(String value) {
        GeneratedCriterion.addCriterion("MEMO like", value, "memo");
        return this;
    }

    public DbSysConfigExample andMemoNotLike(String value) {
        GeneratedCriterion.addCriterion("MEMO not like", value, "memo");
        return this;
    }

    public DbSysConfigExample andMemoIn(List<String> values) {
        GeneratedCriterion.addCriterion("MEMO in", values, "memo");
        return this;
    }

    public DbSysConfigExample andMemoNotIn(List<String> values) {
        GeneratedCriterion.addCriterion("MEMO not in", values, "memo");
        return this;
    }

    public DbSysConfigExample andMemoBetween(String value1, String value2) {
        GeneratedCriterion.addCriterion("MEMO between", value1, value2, "memo");
        return this;
    }

    public DbSysConfigExample andMemoNotBetween(String value1, String value2) {
        GeneratedCriterion.addCriterion("MEMO not between", value1, value2, "memo");
        return this;
    }

    public DbSysConfigExample andValueIsNull() {
        GeneratedCriterion.addCriterion("VALUE is null");
        return this;
    }

    public DbSysConfigExample andValueIsNotNull() {
        GeneratedCriterion.addCriterion("VALUE is not null");
        return this;
    }

    public DbSysConfigExample andValueEqualTo(String value) {
        GeneratedCriterion.addCriterion("VALUE =", value, "value");
        return this;
    }

    public DbSysConfigExample andValueNotEqualTo(String value) {
        GeneratedCriterion.addCriterion("VALUE <>", value, "value");
        return this;
    }

    public DbSysConfigExample andValueGreaterThan(String value) {
        GeneratedCriterion.addCriterion("VALUE >", value, "value");
        return this;
    }

    public DbSysConfigExample andValueGreaterThanOrEqualTo(String value) {
        GeneratedCriterion.addCriterion("VALUE >=", value, "value");
        return this;
    }

    public DbSysConfigExample andValueLessThan(String value) {
        GeneratedCriterion.addCriterion("VALUE <", value, "value");
        return this;
    }

    public DbSysConfigExample andValueLessThanOrEqualTo(String value) {
        GeneratedCriterion.addCriterion("VALUE <=", value, "value");
        return this;
    }

    public DbSysConfigExample andValueLike(String value) {
        GeneratedCriterion.addCriterion("VALUE like", value, "value");
        return this;
    }

    public DbSysConfigExample andValueNotLike(String value) {
        GeneratedCriterion.addCriterion("VALUE not like", value, "value");
        return this;
    }

    public DbSysConfigExample andValueIn(List<String> values) {
        GeneratedCriterion.addCriterion("VALUE in", values, "value");
        return this;
    }

    public DbSysConfigExample andValueNotIn(List<String> values) {
        GeneratedCriterion.addCriterion("VALUE not in", values, "value");
        return this;
    }

    public DbSysConfigExample andValueBetween(String value1, String value2) {
        GeneratedCriterion.addCriterion("VALUE between", value1, value2, "value");
        return this;
    }

    public DbSysConfigExample andValueNotBetween(String value1, String value2) {
        GeneratedCriterion.addCriterion("VALUE not between", value1, value2, "value");
        return this;
    }

    public DbSysConfigExample andAttaOidIsNull() {
        GeneratedCriterion.addCriterion("ATTA_OID is null");
        return this;
    }

    public DbSysConfigExample andAttaOidIsNotNull() {
        GeneratedCriterion.addCriterion("ATTA_OID is not null");
        return this;
    }

    public DbSysConfigExample andAttaOidEqualTo(Long value) {
        GeneratedCriterion.addCriterion("ATTA_OID =", value, "attaOid");
        return this;
    }

    public DbSysConfigExample andAttaOidNotEqualTo(Long value) {
        GeneratedCriterion.addCriterion("ATTA_OID <>", value, "attaOid");
        return this;
    }

    public DbSysConfigExample andAttaOidGreaterThan(Long value) {
        GeneratedCriterion.addCriterion("ATTA_OID >", value, "attaOid");
        return this;
    }

    public DbSysConfigExample andAttaOidGreaterThanOrEqualTo(Long value) {
        GeneratedCriterion.addCriterion("ATTA_OID >=", value, "attaOid");
        return this;
    }

    public DbSysConfigExample andAttaOidLessThan(Long value) {
        GeneratedCriterion.addCriterion("ATTA_OID <", value, "attaOid");
        return this;
    }

    public DbSysConfigExample andAttaOidLessThanOrEqualTo(Long value) {
        GeneratedCriterion.addCriterion("ATTA_OID <=", value, "attaOid");
        return this;
    }

    public DbSysConfigExample andAttaOidIn(List<Long> values) {
        GeneratedCriterion.addCriterion("ATTA_OID in", values, "attaOid");
        return this;
    }

    public DbSysConfigExample andAttaOidNotIn(List<Long> values) {
        GeneratedCriterion.addCriterion("ATTA_OID not in", values, "attaOid");
        return this;
    }

    public DbSysConfigExample andAttaOidBetween(Long value1, Long value2) {
        GeneratedCriterion.addCriterion("ATTA_OID between", value1, value2, "attaOid");
        return this;
    }

    public DbSysConfigExample andAttaOidNotBetween(Long value1, Long value2) {
        GeneratedCriterion.addCriterion("ATTA_OID not between", value1, value2, "attaOid");
        return this;
    }

    public DbSysConfigExample andIsAbleIsNull() {
        GeneratedCriterion.addCriterion("IS_ABLE is null");
        return this;
    }

    public DbSysConfigExample andIsAbleIsNotNull() {
        GeneratedCriterion.addCriterion("IS_ABLE is not null");
        return this;
    }

    public DbSysConfigExample andIsAbleEqualTo(String value) {
        GeneratedCriterion.addCriterion("IS_ABLE =", value, "isAble");
        return this;
    }

    public DbSysConfigExample andIsAbleNotEqualTo(String value) {
        GeneratedCriterion.addCriterion("IS_ABLE <>", value, "isAble");
        return this;
    }

    public DbSysConfigExample andIsAbleGreaterThan(String value) {
        GeneratedCriterion.addCriterion("IS_ABLE >", value, "isAble");
        return this;
    }

    public DbSysConfigExample andIsAbleGreaterThanOrEqualTo(String value) {
        GeneratedCriterion.addCriterion("IS_ABLE >=", value, "isAble");
        return this;
    }

    public DbSysConfigExample andIsAbleLessThan(String value) {
        GeneratedCriterion.addCriterion("IS_ABLE <", value, "isAble");
        return this;
    }

    public DbSysConfigExample andIsAbleLessThanOrEqualTo(String value) {
        GeneratedCriterion.addCriterion("IS_ABLE <=", value, "isAble");
        return this;
    }

    public DbSysConfigExample andIsAbleLike(String value) {
        GeneratedCriterion.addCriterion("IS_ABLE like", value, "isAble");
        return this;
    }

    public DbSysConfigExample andIsAbleNotLike(String value) {
        GeneratedCriterion.addCriterion("IS_ABLE not like", value, "isAble");
        return this;
    }

    public DbSysConfigExample andIsAbleIn(List<String> values) {
        GeneratedCriterion.addCriterion("IS_ABLE in", values, "isAble");
        return this;
    }

    public DbSysConfigExample andIsAbleNotIn(List<String> values) {
        GeneratedCriterion.addCriterion("IS_ABLE not in", values, "isAble");
        return this;
    }

    public DbSysConfigExample andIsAbleBetween(String value1, String value2) {
        GeneratedCriterion.addCriterion("IS_ABLE between", value1, value2, "isAble");
        return this;
    }

    public DbSysConfigExample andIsAbleNotBetween(String value1, String value2) {
        GeneratedCriterion.addCriterion("IS_ABLE not between", value1, value2, "isAble");
        return this;
    }

    public DbSysConfigExample andIsDeleteIsNull() {
        GeneratedCriterion.addCriterion("IS_DELETE is null");
        return this;
    }

    public DbSysConfigExample andIsDeleteIsNotNull() {
        GeneratedCriterion.addCriterion("IS_DELETE is not null");
        return this;
    }

    public DbSysConfigExample andIsDeleteEqualTo(String value) {
        GeneratedCriterion.addCriterion("IS_DELETE =", value, "isDelete");
        return this;
    }

    public DbSysConfigExample andIsDeleteNotEqualTo(String value) {
        GeneratedCriterion.addCriterion("IS_DELETE <>", value, "isDelete");
        return this;
    }

    public DbSysConfigExample andIsDeleteGreaterThan(String value) {
        GeneratedCriterion.addCriterion("IS_DELETE >", value, "isDelete");
        return this;
    }

    public DbSysConfigExample andIsDeleteGreaterThanOrEqualTo(String value) {
        GeneratedCriterion.addCriterion("IS_DELETE >=", value, "isDelete");
        return this;
    }

    public DbSysConfigExample andIsDeleteLessThan(String value) {
        GeneratedCriterion.addCriterion("IS_DELETE <", value, "isDelete");
        return this;
    }

    public DbSysConfigExample andIsDeleteLessThanOrEqualTo(String value) {
        GeneratedCriterion.addCriterion("IS_DELETE <=", value, "isDelete");
        return this;
    }

    public DbSysConfigExample andIsDeleteLike(String value) {
        GeneratedCriterion.addCriterion("IS_DELETE like", value, "isDelete");
        return this;
    }

    public DbSysConfigExample andIsDeleteNotLike(String value) {
        GeneratedCriterion.addCriterion("IS_DELETE not like", value, "isDelete");
        return this;
    }

    public DbSysConfigExample andIsDeleteIn(List<String> values) {
        GeneratedCriterion.addCriterion("IS_DELETE in", values, "isDelete");
        return this;
    }

    public DbSysConfigExample andIsDeleteNotIn(List<String> values) {
        GeneratedCriterion.addCriterion("IS_DELETE not in", values, "isDelete");
        return this;
    }

    public DbSysConfigExample andIsDeleteBetween(String value1, String value2) {
        GeneratedCriterion.addCriterion("IS_DELETE between", value1, value2, "isDelete");
        return this;
    }

    public DbSysConfigExample andIsDeleteNotBetween(String value1, String value2) {
        GeneratedCriterion.addCriterion("IS_DELETE not between", value1, value2, "isDelete");
        return this;
    }

    public DbSysConfigExample andParentOidIsNull() {
        GeneratedCriterion.addCriterion("PARENT_OID is null");
        return this;
    }

    public DbSysConfigExample andParentOidIsNotNull() {
        GeneratedCriterion.addCriterion("PARENT_OID is not null");
        return this;
    }

    public DbSysConfigExample andParentOidEqualTo(Long value) {
        GeneratedCriterion.addCriterion("PARENT_OID =", value, "parentOid");
        return this;
    }

    public DbSysConfigExample andParentOidNotEqualTo(Long value) {
        GeneratedCriterion.addCriterion("PARENT_OID <>", value, "parentOid");
        return this;
    }

    public DbSysConfigExample andParentOidGreaterThan(Long value) {
        GeneratedCriterion.addCriterion("PARENT_OID >", value, "parentOid");
        return this;
    }

    public DbSysConfigExample andParentOidGreaterThanOrEqualTo(Long value) {
        GeneratedCriterion.addCriterion("PARENT_OID >=", value, "parentOid");
        return this;
    }

    public DbSysConfigExample andParentOidLessThan(Long value) {
        GeneratedCriterion.addCriterion("PARENT_OID <", value, "parentOid");
        return this;
    }

    public DbSysConfigExample andParentOidLessThanOrEqualTo(Long value) {
        GeneratedCriterion.addCriterion("PARENT_OID <=", value, "parentOid");
        return this;
    }

    public DbSysConfigExample andParentOidIn(List<Long> values) {
        GeneratedCriterion.addCriterion("PARENT_OID in", values, "parentOid");
        return this;
    }

    public DbSysConfigExample andParentOidNotIn(List<Long> values) {
        GeneratedCriterion.addCriterion("PARENT_OID not in", values, "parentOid");
        return this;
    }

    public DbSysConfigExample andParentOidBetween(Long value1, Long value2) {
        GeneratedCriterion.addCriterion("PARENT_OID between", value1, value2, "parentOid");
        return this;
    }

    public DbSysConfigExample andParentOidNotBetween(Long value1, Long value2) {
        GeneratedCriterion.addCriterion("PARENT_OID not between", value1, value2, "parentOid");
        return this;
    }

    public DbSysConfigExample andTypeIsNull() {
        GeneratedCriterion.addCriterion("TYPE is null");
        return this;
    }

    public DbSysConfigExample andTypeIsNotNull() {
        GeneratedCriterion.addCriterion("TYPE is not null");
        return this;
    }

    public DbSysConfigExample andTypeEqualTo(String value) {
        GeneratedCriterion.addCriterion("TYPE =", value, "type");
        return this;
    }

    public DbSysConfigExample andTypeNotEqualTo(String value) {
        GeneratedCriterion.addCriterion("TYPE <>", value, "type");
        return this;
    }

    public DbSysConfigExample andTypeGreaterThan(String value) {
        GeneratedCriterion.addCriterion("TYPE >", value, "type");
        return this;
    }

    public DbSysConfigExample andTypeGreaterThanOrEqualTo(String value) {
        GeneratedCriterion.addCriterion("TYPE >=", value, "type");
        return this;
    }

    public DbSysConfigExample andTypeLessThan(String value) {
        GeneratedCriterion.addCriterion("TYPE <", value, "type");
        return this;
    }

    public DbSysConfigExample andTypeLessThanOrEqualTo(String value) {
        GeneratedCriterion.addCriterion("TYPE <=", value, "type");
        return this;
    }

    public DbSysConfigExample andTypeLike(String value) {
        GeneratedCriterion.addCriterion("TYPE like", value, "type");
        return this;
    }

    public DbSysConfigExample andTypeNotLike(String value) {
        GeneratedCriterion.addCriterion("TYPE not like", value, "type");
        return this;
    }

    public DbSysConfigExample andTypeIn(List<String> values) {
        GeneratedCriterion.addCriterion("TYPE in", values, "type");
        return this;
    }

    public DbSysConfigExample andTypeNotIn(List<String> values) {
        GeneratedCriterion.addCriterion("TYPE not in", values, "type");
        return this;
    }

    public DbSysConfigExample andTypeBetween(String value1, String value2) {
        GeneratedCriterion.addCriterion("TYPE between", value1, value2, "type");
        return this;
    }

    public DbSysConfigExample andTypeNotBetween(String value1, String value2) {
        GeneratedCriterion.addCriterion("TYPE not between", value1, value2, "type");
        return this;
    }

    public DbSysConfigExample andPathIsNull() {
        GeneratedCriterion.addCriterion("PATH is null");
        return this;
    }

    public DbSysConfigExample andPathIsNotNull() {
        GeneratedCriterion.addCriterion("PATH is not null");
        return this;
    }

    public DbSysConfigExample andPathEqualTo(String value) {
        GeneratedCriterion.addCriterion("PATH =", value, "path");
        return this;
    }

    public DbSysConfigExample andPathNotEqualTo(String value) {
        GeneratedCriterion.addCriterion("PATH <>", value, "path");
        return this;
    }

    public DbSysConfigExample andPathGreaterThan(String value) {
        GeneratedCriterion.addCriterion("PATH >", value, "path");
        return this;
    }

    public DbSysConfigExample andPathGreaterThanOrEqualTo(String value) {
        GeneratedCriterion.addCriterion("PATH >=", value, "path");
        return this;
    }

    public DbSysConfigExample andPathLessThan(String value) {
        GeneratedCriterion.addCriterion("PATH <", value, "path");
        return this;
    }

    public DbSysConfigExample andPathLessThanOrEqualTo(String value) {
        GeneratedCriterion.addCriterion("PATH <=", value, "path");
        return this;
    }

    public DbSysConfigExample andPathLike(String value) {
        GeneratedCriterion.addCriterion("PATH like", value, "path");
        return this;
    }

    public DbSysConfigExample andPathNotLike(String value) {
        GeneratedCriterion.addCriterion("PATH not like", value, "path");
        return this;
    }

    public DbSysConfigExample andPathIn(List<String> values) {
        GeneratedCriterion.addCriterion("PATH in", values, "path");
        return this;
    }

    public DbSysConfigExample andPathNotIn(List<String> values) {
        GeneratedCriterion.addCriterion("PATH not in", values, "path");
        return this;
    }

    public DbSysConfigExample andPathBetween(String value1, String value2) {
        GeneratedCriterion.addCriterion("PATH between", value1, value2, "path");
        return this;
    }

    public DbSysConfigExample andPathNotBetween(String value1, String value2) {
        GeneratedCriterion.addCriterion("PATH not between", value1, value2, "path");
        return this;
    }

    public DbSysConfigExample andModifyDateIsNull() {
        GeneratedCriterion.addCriterion("MODIFY_DATE is null");
        return this;
    }

    public DbSysConfigExample andModifyDateIsNotNull() {
        GeneratedCriterion.addCriterion("MODIFY_DATE is not null");
        return this;
    }

    public DbSysConfigExample andModifyDateEqualTo(Date value) {
        GeneratedCriterion.addCriterion("MODIFY_DATE =", value, "modifyDate");
        return this;
    }

    public DbSysConfigExample andModifyDateNotEqualTo(Date value) {
        GeneratedCriterion.addCriterion("MODIFY_DATE <>", value, "modifyDate");
        return this;
    }

    public DbSysConfigExample andModifyDateGreaterThan(Date value) {
        GeneratedCriterion.addCriterion("MODIFY_DATE >", value, "modifyDate");
        return this;
    }

    public DbSysConfigExample andModifyDateGreaterThanOrEqualTo(Date value) {
        GeneratedCriterion.addCriterion("MODIFY_DATE >=", value, "modifyDate");
        return this;
    }

    public DbSysConfigExample andModifyDateLessThan(Date value) {
        GeneratedCriterion.addCriterion("MODIFY_DATE <", value, "modifyDate");
        return this;
    }

    public DbSysConfigExample andModifyDateLessThanOrEqualTo(Date value) {
        GeneratedCriterion.addCriterion("MODIFY_DATE <=", value, "modifyDate");
        return this;
    }

    public DbSysConfigExample andModifyDateIn(List<Date> values) {
        GeneratedCriterion.addCriterion("MODIFY_DATE in", values, "modifyDate");
        return this;
    }

    public DbSysConfigExample andModifyDateNotIn(List<Date> values) {
        GeneratedCriterion.addCriterion("MODIFY_DATE not in", values, "modifyDate");
        return this;
    }

    public DbSysConfigExample andModifyDateBetween(Date value1, Date value2) {
        GeneratedCriterion.addCriterion("MODIFY_DATE between", value1, value2, "modifyDate");
        return this;
    }

    public DbSysConfigExample andModifyDateNotBetween(Date value1, Date value2) {
        GeneratedCriterion.addCriterion("MODIFY_DATE not between", value1, value2, "modifyDate");
        return this;
    }

    public List<Criteria> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(List<Criteria> criteriaList) {
        this.criteriaList = criteriaList;
    }
}