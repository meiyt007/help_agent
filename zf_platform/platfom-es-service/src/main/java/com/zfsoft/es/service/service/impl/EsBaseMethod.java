package com.zfsoft.es.service.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zfsoft.es.service.service.BaseMethod;

import java.util.List;
import java.util.Map;

/**
 * @author: kkfan
 * @create: 2021-01-18 15:00:55
 * @description: es数据操作实现
 */
public class EsBaseMethod implements BaseMethod {
    @Override
    public Map<String, Object> saveOrUpdate(JSONObject jsonObject) {
        return null;
    }

    @Override
    public Object searchOneByOid(String oid) {
        return null;
    }

    @Override
    public Object searchOneByBusinessOid(String businessOid) {
        return null;
    }

    @Override
    public List<Object> searchOneByBusinessOid(JSONObject jsonObject) {
        return null;
    }
}
