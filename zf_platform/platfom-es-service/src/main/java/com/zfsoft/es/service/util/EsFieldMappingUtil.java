package com.zfsoft.es.service.util;

import com.zfsoft.es.service.common.SysConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: kkfan
 * @create: 2020-08-30 15:54:41
 * @description: 通用方法
 */
public class EsFieldMappingUtil {
    private static final Logger logger = LoggerFactory.getLogger(EsFieldMappingUtil.class);

    private static Map<String, String> dataType = new HashMap<>();

    static {
        dataType.put("java.lang.String", SysConfig.ES_COMMON_FIELD_TYPE.STRING.TEXT);
        dataType.put("class java.lang.String", SysConfig.ES_COMMON_FIELD_TYPE.STRING.TEXT);

        dataType.put("java.util.Date", SysConfig.ES_COMMON_FIELD_TYPE.DATE);
        dataType.put("class java.util.Date", SysConfig.ES_COMMON_FIELD_TYPE.DATE);

        dataType.put("java.lang.Integer", SysConfig.ES_COMMON_FIELD_TYPE.WHOLE_NUMBER.INTEGER);
        dataType.put("class java.lang.Integer", SysConfig.ES_COMMON_FIELD_TYPE.WHOLE_NUMBER.INTEGER);
        dataType.put("int", SysConfig.ES_COMMON_FIELD_TYPE.WHOLE_NUMBER.INTEGER);

        dataType.put("java.lang.Long", SysConfig.ES_COMMON_FIELD_TYPE.WHOLE_NUMBER.LONG);
        dataType.put("class java.lang.Long", SysConfig.ES_COMMON_FIELD_TYPE.WHOLE_NUMBER.LONG);
        dataType.put("long", SysConfig.ES_COMMON_FIELD_TYPE.WHOLE_NUMBER.LONG);

        dataType.put("java.lang.Byte", SysConfig.ES_COMMON_FIELD_TYPE.WHOLE_NUMBER.BYTE);
        dataType.put("class java.lang.Byte", SysConfig.ES_COMMON_FIELD_TYPE.WHOLE_NUMBER.BYTE);
        dataType.put("byte", SysConfig.ES_COMMON_FIELD_TYPE.WHOLE_NUMBER.BYTE);

        dataType.put("java.lang.Short", SysConfig.ES_COMMON_FIELD_TYPE.WHOLE_NUMBER.SHORT);
        dataType.put("class java.lang.Short", SysConfig.ES_COMMON_FIELD_TYPE.WHOLE_NUMBER.SHORT);
        dataType.put("short", SysConfig.ES_COMMON_FIELD_TYPE.WHOLE_NUMBER.SHORT);

        dataType.put("java.lang.Float", SysConfig.ES_COMMON_FIELD_TYPE.FLOAT_NUMBER.FLOAT);
        dataType.put("class java.lang.Float", SysConfig.ES_COMMON_FIELD_TYPE.FLOAT_NUMBER.FLOAT);
        dataType.put("float", SysConfig.ES_COMMON_FIELD_TYPE.FLOAT_NUMBER.FLOAT);

        dataType.put("java.lang.Double", SysConfig.ES_COMMON_FIELD_TYPE.FLOAT_NUMBER.DOUBLE);
        dataType.put("class java.lang.Double", SysConfig.ES_COMMON_FIELD_TYPE.FLOAT_NUMBER.DOUBLE);
        dataType.put("double", SysConfig.ES_COMMON_FIELD_TYPE.FLOAT_NUMBER.DOUBLE);

        dataType.put("java.util.List", SysConfig.ES_COMMON_FIELD_TYPE.ARRAY);
        dataType.put("class java.util.List", SysConfig.ES_COMMON_FIELD_TYPE.ARRAY);
        dataType.put("java.util.Map", SysConfig.ES_COMMON_FIELD_TYPE.ARRAY);
        dataType.put("class java.util.Map", SysConfig.ES_COMMON_FIELD_TYPE.ARRAY);
    }


    /**
     * java数据类型与es数据类型对应
     * @param objFieldType  java数据类型
     * @return  对应的es数据类型
     */
    public static String objFieldTypeToEsType(String objFieldType) {
        if(StringUtils.isNotEmpty(dataType.get(objFieldType))) {
            return dataType.get(objFieldType);
        }
        logger.warn(MessageFormat.format("{0}为配置对应的es类型！", objFieldType));
        return SysConfig.ES_COMMON_FIELD_TYPE.OBJECT;
    }
}
