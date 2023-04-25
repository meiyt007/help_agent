package com.zfsoft.cases.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author : 王柯
 * @version : 1.0
 * @Project : eeds_siban
 * @Package : com.zfsoft.util
 * @ClassName : .java
 * @createTime : 2022/6/13 14:23
 * @Email : o1117@vip.163.com
 * @Description :
 */
public class JsonChange {

    /**
     * 将对象转换成 Json 字符串
     * @param obj 对象
     * @return String
     * @throws JsonProcessingException
     */
    public static String objectToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
