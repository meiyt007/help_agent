package com.zfsoft.es.service.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @author: kkfan
 * @create: 2021-01-18 14:50:14
 * @description: 方法定义接口
 */
public interface BaseMethod {

    /**
     * 通过主键oid/id 新增/更新业务实体
     * @param jsonObject
     * @return
     */
    Map<String, Object> saveOrUpdate(JSONObject jsonObject);

    /**
     * 通过主键查询实体
     * @param oid
     * @return
     */
    Object searchOneByOid(String oid);

    /**
     * 通过业务主键查询实体
     * @param businessOid
     * @return
     */
    Object searchOneByBusinessOid(String businessOid);

    /**
     * 通过条件查询业务实体
     * @param jsonObject
     * @return
     */
    List<Object> searchOneByBusinessOid(JSONObject jsonObject);
}
