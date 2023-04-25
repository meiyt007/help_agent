package com.zfsoft.es.service.test;

import com.alibaba.fastjson.JSONObject;
import com.zfsoft.es.service.enums.PrimaryKeyEnum;
import com.zfsoft.es.service.enums.OperationTypeEnum;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author: kkfan
 * @create: 2021-01-15 13:11:34
 * @description:
 */
public class Test1 {
    public static void main(String[] args) {
        System.out.println(PrimaryKeyEnum.id.name());
        System.out.println(PrimaryKeyEnum.values().toString());
        System.out.println(PrimaryKeyEnum.valueOf("id"));

        Map<PrimaryKeyEnum, Object> primaryKeyEnumObjectEnumMap = new EnumMap<>(PrimaryKeyEnum.class);
        System.out.println(JSONObject.toJSONString(primaryKeyEnumObjectEnumMap));

        System.out.println(OperationTypeEnum.DELETE);
        System.out.println(OperationTypeEnum.DELETE.toString());
        System.out.println(OperationTypeEnum.DELETE.name());

    }
}
