package com.zfsoft.ocr.dbaccess.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @（#）:GeneratedCriterion
 * @description: GeneratedCriterion
 * @author: wangwg
 * @date 2020-10-22
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
public class GeneratedCriterion {

    public static List<Criterion> criterionList;

    public GeneratedCriterion() {
        super();
        criterionList = new ArrayList<Criterion>();
    }

    public boolean isValid() {
        return criterionList.size() > 0;
    }

    public List<Criterion> getAllcriterion() {
        return criterionList;
    }

    public List<Criterion> getcriterion() {
        return criterionList;
    }

    public static void addCriterion(String condition) {
        if (condition == null) {
            throw new RuntimeException("Value for condition cannot be null");
        }
        criterionList.add(new Criterion(condition));
    }

    public static void addCriterion(String condition, Object value, String property) {
        if (value == null) {
            throw new RuntimeException("Value for " + property + " cannot be null");
        }
        criterionList.add(new Criterion(condition, value));
    }

    public static void addCriterion(String condition, Object value1, Object value2, String property) {
        if (value1 == null || value2 == null) {
            throw new RuntimeException("Between values for " + property + " cannot be null");
        }
        criterionList.add(new Criterion(condition, value1, value2));
    }
}
