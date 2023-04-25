package com.zfsoft.outer.inter.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;


/**
 * json帮助类
 *
 * @author gaolh
 * @date 2016-3-24
 */
@SuppressWarnings(value = { "rawtypes", "unchecked" })
public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * 将对象转换成JSONObject
     *
     * @param object
     *            转化对象
     */
    public static JSONObject objToJSONObject(Object object) {
        if(null == object){
            return null;
        }
        return (JSONObject)JSON.toJSON(object);
    }

    /**
     * 将对象转换成JSONArray
     *
     * @param object
     *            转化对象
     */
    public static JSONArray objToJSONArray(Object object) {
        if(null == object){
            return null;
        }
        JSONArray array = new JSONArray();
        try{
            array = (JSONArray)JSON.toJSON(object);
        }catch(Exception e){
            JSONObject jo = (JSONObject)JSON.toJSON(object);
            array.add(jo);
        }
        return array;
    }

    /**
     * 将JSON转换成数组,其中valueClz为数组中存放的对象的Class
     *
     * @param json
     *            转化为数组的json对象
     * @param valueClz
     *            数组中存放的对象的Class
     */
    public static Object jsonToArray(String json, Class valueClz) {
        if(StrUtil.isBlank(json)){
            return null;
        }
        return JSONObject.parseArray(json,valueClz);
    }

    /**
     * 将集合转换成JSON字符串
     *
     * @param object
     *            需要转化为json的Collection对象
     */
    public static String collectionToJson(Object object) {
        if(null == object){
            return null;
        }
        JSONArray jsonArray = objToJSONArray(object);
        return jsonArray.toJSONString();
    }

    /**
     * 将JSON转换成POJO,其中beanClz为POJO的Class
     *
     * @author gaolh
     * @date 2016-3-24
     * @param json
     *            转化为pojo的json字符串
     * @param beanClz
     *            POJO的Class
     * @return
     */
    public static Object jsonToObject(String json, Class beanClz) {
        return JSONObject.toJavaObject(JSON.parseObject(json), beanClz);
    }

    /**
     * json转对象
     * 使用fastjson
     * @data 2018-2-22
     * @author zhujiajian
     */
    public static Object jsonToObject2(String json, Class clz) {
        return JSONObject.toJavaObject(JSON.parseObject(json), clz);
    }

    /**
     * 将json字符串转化为list对象
     *
     * @param json
     *            json字符串
     * @param beanClz
     *            list中的对象
     */
    public static List jsonToCollection(String json, Class beanClz) {
        return JSONObject.parseArray(json, beanClz);
    }

    /**
     * json转换为java对象
     *
     * <pre>
     * return JackJson.fromJsonToObject(this.answersJson, JackJson.class);
     * </pre>
     *
     * @param <T>
     *            要转换的对象
     * @param json
     *            字符串
     * @param valueType
     *            对象的class
     * @return 返回对象
     */
    public static <T> T fromJsonToObject(String json, Class<T> valueType) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, valueType);
        } catch (JsonParseException e) {
            logger.error("JsonParseException: ", e);
        } catch (JsonMappingException e) {
            logger.error("JsonMappingException: ", e);
        } catch (IOException e) {
            logger.error("IOException: ", e);
        }
        return null;
    }

    // 将String转换成JSON
    /**
     * 根据key、value生成json对象
     *
     * @author gaolh
     * @date 2016-3-24
     * @param key
     *            json的key值
     * @param value
     *            json的value值
     * @return
     */
    public static String stringToJson(String key, String value) {
        JSONObject object = new JSONObject();
        object.put(key, value);
        return object.toJSONString();
    }

    /**
     * 获取json中某个key的值
     *
     * @author gaolh
     * @date 2016-3-24
     * @param json
     *            json字符串
     * @param key
     *            需要获取的key值
     * @return
     */
    public static String jsonToString(String json, String key) {
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.get(key).toString();
    }

    /***
     * 将List对象序列化为JSON文本
     */
    public static <T> String toJSONString(List<T> list) {
        if(null == list){
            return null;
        }
        JSONArray jsonArray = objToJSONArray(list);
        return jsonArray.toJSONString();
    }

    /***
     * 将对象序列化为JSON文本
     *
     * @param object
     * @return
     */
    public static String toJSONString(Object object) {
        if(null == object){
            return null;
        }
        JSONArray jsonArray = objToJSONArray(object);

        return jsonArray.toJSONString();
    }

    /***
     * 将JSON对象数组序列化为JSON文本
     *
     * @param jsonArray
     * @return
     */
    public static String toJSONString(JSONArray jsonArray) {
        return jsonArray.toJSONString();
    }

    /***
     * 将JSON对象序列化为JSON文本
     *
     * @param jsonObject
     * @return
     */
    public static String toJSONString(JSONObject jsonObject) {
        return jsonObject.toJSONString();
    }

    /***
     * 将对象转换为List对象
     *
     * @param object
     * @return
     */
    public static List toArrayList(Object object) {
        if(null == object){
            return null;
        }
        List arrayList = new ArrayList();

        JSONArray jsonArray = objToJSONArray(object);

        Iterator it = jsonArray.iterator();
        while (it.hasNext()) {
            JSONObject jsonObject = (JSONObject) it.next();

            for(String key:jsonObject.keySet()){
                Object value = jsonObject.get(key);
                arrayList.add(value);
            }
        }

        return arrayList;
    }

    /***
     * 将对象转换为JSON对象数组
     *
     * @param object
     * @return
     */
    public static JSONArray toJSONArray(Object object) {
        if(null == object){
            return null;
        }
        return objToJSONArray(object);
    }

    /***
     * 将对象转换为JSON对象
     *
     * @param object
     * @return
     */
    public static JSONObject toJSONObject(Object object) {
        if(null == object){
            return null;
        }
        return objToJSONObject(object);
    }

    /***
     * 将对象转换为HashMap
     *
     * @param object
     * @return
     */
    public static HashMap toHashMap(Object object) {
        HashMap<String, Object> data = new HashMap<String, Object>(16);
        if(object!=null){
            JSONObject jsonObject = JsonUtil.toJSONObject(object);
            for(String key:jsonObject.keySet()){
                Object value = jsonObject.get(key);
                data.put(key, value);
            }
        }
        return data;
    }

    /***
     * 将对象转换为List<Map<String,Object>>
     *
     * @param object
     * @return
     */
    public static List<Map<String, Object>> toList(Object object) {
        if(null == object){
            return null;
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        JSONArray jsonArray = objToJSONArray(object);
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            Map<String, Object> map = new HashMap<String, Object>(16);
            for(String key:jsonObject.keySet()){
                Object value = jsonObject.get(key);
                map.put((String) key, value);
            }
            list.add(map);
        }
        return list;
    }

    /***
     * 将JSON对象数组转换为传入类型的List
     *
     * @param <T>
     * @param jsonArray
     * @param objectClass
     * @return
     */
    public static <T> List<T> toList(JSONArray jsonArray, Class<T> objectClass) {
        return (List<T>) JSONArray.toJavaObject(jsonArray, objectClass);
    }

    /***
     * 将对象转换为传入类型的List
     *
     * @param <T>
     * @param
     * @param objectClass
     * @return
     */
    public static <T> List<T> toList(Object object, Class<T> objectClass) {
        if(null == object){
            return null;
        }
        JSONArray jsonArray = objToJSONArray(object);

        return (List<T>) JSONArray.toJavaObject(jsonArray, objectClass);
    }

    /***
     * 将JSON对象转换为传入类型的对象
     *
     * @param <T>
     * @param jsonObject
     * @param beanClass
     * @return
     */
    public static <T> T toBean(JSONObject jsonObject, Class<T> beanClass) {
        return (T) JSONObject.toJavaObject(jsonObject, beanClass);
    }

    /***
     * 将将对象转换为传入类型的对象
     *
     * @param <T>
     * @param object
     * @param beanClass
     * @return
     */
    public static <T> T toBean(Object object, Class<T> beanClass) {
        if(null == object){
            return null;
        }
        JSONObject jsonObject = objToJSONObject(object);

        return (T) JSONObject.toJavaObject(jsonObject, beanClass);
    }






    /**
     * list转化为json对象
     *
     * @author gaolh
     * @date 2016-3-24
     *
     * @param fields
     *            需要转化的list中对象的字段集合
     * @param total
     *            集合总数
     * @param list
     *            集合对象
     * @return
     * @throws Exception
     */
/*    public static String listToJson(String[] fields, int total, List list) throws Exception {
        Object[] values = new Object[fields.length];
        StringBuilder jsonTemp = new StringBuilder("{\"total\":" + total + ",\"rows\":[");
        for (int j = 0; j < list.size(); j++) {
            jsonTemp = jsonTemp.append("{\"state\":\"closed\",");
            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i].toString();
                values[i] = fieldNametoValues(fieldName, list.get(j));
                jsonTemp = jsonTemp.append("\"" + fieldName + "\"" + ":\"" + values[i] + "\"");
                if (i != fields.length - 1) {
                    jsonTemp = jsonTemp.append(",");
                }
            }
            if (j != list.size() - 1) {
                jsonTemp = jsonTemp.append("},");
            } else {
                jsonTemp = jsonTemp.append("}");
            }
        }
        jsonTemp = jsonTemp.append(jsonTemp + "]}");
        return jsonTemp.toString();
    }*/

    /**
     *
     * 获取对象内对应字段的值
     *
     * @param
     */
   /* public static Object fieldNametoValues(String filedName, Object o) {
        if (o == null || StrUtil.isBlank(filedName)) {
            return null;
        }
        Object value = "";
        String fName = "";
        String childFieldName = null;
        ReflectHelper reflectHelper = new ReflectHelper(o);
        int dotPos = filedName.indexOf(".");
        int underlinePos = filedName.indexOf("_");
        if (underlinePos == -1) {
            if (dotPos == -1) {
                fName = filedName;
            } else {
                // 外键字段引用名
                fName = filedName.substring(0, dotPos);
                // 外键字段名
                childFieldName = filedName.substring(dotPos + 1);
            }
        } else {
            // 外键字段引用名
            fName = filedName.substring(0, underlinePos);
            // 外键字段名
            childFieldName = filedName.substring(underlinePos + 1);
        }
        value = reflectHelper.getMethodValue(fName) == null ? "" : reflectHelper.getMethodValue(fName);
        boolean boolOne = underlinePos != -1 || dotPos != -1;
        if (value != null && boolOne) {
            if (value instanceof List) {
                StringBuilder tempValue = new StringBuilder();
                for (Object listValue : (List) value) {
                    tempValue = tempValue.append(fieldNametoValues(childFieldName, listValue) + ",");
                }
                value = tempValue;
            } else {
                value = fieldNametoValues(childFieldName, value);
            }
        }
        if (value != null) {
            value = converUnicode(value.toString());
        }
        return value;
    }
*/
    private static Object converUnicode(String jsonValue) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < jsonValue.length(); i++) {
            char c = jsonValue.charAt(i);
            switch (c) {
            case '\"':
                sb.append("\\\"");
                break;
            case '\'':
                sb.append("\\\'");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '/':
                sb.append("\\/");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            default:
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 将对象转换成JSON字符串
     *
     * @param object
     *            转化对象
     */
    public static String objToJsonStr(Object object) {
        if(null == object){
            return null;
        }
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        return JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
    }


}
