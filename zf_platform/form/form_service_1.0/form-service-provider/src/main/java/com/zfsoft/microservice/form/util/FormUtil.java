package com.zfsoft.microservice.form.util;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import com.zfsoft.microservice.form.data.FormObjectExtand;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.regex.Matcher;

/**
 * @ClassName FormDataUtil
 * @Description: 表单数据的工具类
 * @Author wuxx
 * @Date 2021/4/30
 **/
public class FormUtil {

    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
            "X", "Y", "Z"};

    /**
     * @param formConfig 配置json
     * @param formData   数据json
     * @description: 根据表单配置和表单数据重新调整数据结构--数据的展示和修改
     * @author: wuxx
     * @Date: 2021/4/30 10:38
     **/
    public static String getFormDataByConfig(String formConfig, String formData) {
        //解析配置,生成新的结构数据
        JSONObject config = JSONUtil.parseObj(formConfig);
        JSONObject formDataObject = JSONUtil.parseObj(formData);
        JSONObject formDesc = config.get("formDesc", JSONObject.class);
        Map<String, Object> mapByConfigMap = FormUtil.getMapByConfigMap(formDesc, formDataObject, new HashMap<>());
        return JSONUtil.toJsonStr(mapByConfigMap);
    }

    /**
     * @param formConfig 配置json
     * @param formData   数据json
     * @description: 根据表单配置和表单数据重新调整数据结构--用户API数据的获取
     * @author: wuxx
     * @Date: 2021/4/30 10:38
     **/
    public static String getApiFormDataByConfig(String formConfig, String formData) {
        //解析配置,生成新的结构数据
        try {
            JSONObject config = JSONUtil.parseObj(formConfig);
            JSONObject formDataObject = JSONUtil.parseObj(formData);
            JSONObject formDesc = config.get("formDesc", JSONObject.class);
            Map<String, Object> mapByConfigMap = FormUtil.getApiMapByConfigMap(formDesc, formDataObject, new HashMap<>());
            return JSONUtil.toJsonStr(mapByConfigMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @param formConfig 配置json
     * @param components 组件name
     * @description: 根据表单配置结构--用户API数据结构的获取
     * @author: wuxx
     * @Date: 2021/5/6 10:38
     **/
    public static String getApiFormConfig(String formConfig, String components) {
        //解析配置,生成新的结构数据
        JSONObject config = JSONUtil.parseObj(formConfig);
        String[] split = null == components ? new String[0] : components.split(",");
        List<String> componentList = Arrays.asList(split);
        JSONObject formDesc = config.get("formDesc", JSONObject.class);
        Map<String, Object> mapByConfigMap = FormUtil.getApiMapConfig(formDesc, componentList, new HashMap<>());
        return JSONUtil.toJsonStr(mapByConfigMap);
    }

    /**
     * @param configMap 配置map
     * @param formData  json数据
     * @param resultMap 接受map
     * @description: 根据配置解析json格式--用户API数据的获取
     * @author: wuxx
     * @Date: 2021/4/30 10:18
     **/
    private static Map<String, Object> getApiMapByConfigMap(Map<String, Object> configMap, JSONObject formData, Map<String, Object> resultMap) {
        for (Map.Entry<String, Object> entry : configMap.entrySet()) {
            String mapKey = entry.getKey();
            Object mapValue = entry.getValue();
            resultMap.put(mapKey, null == formData.get(mapKey) ? "" : formData.get(mapKey));
            JSONObject object = JSONUtil.parseObj(mapValue.toString());
            String formItemList = object.getStr("formItemList");
            if (StrUtil.isNotBlank(formItemList) && !formItemList.equals("[]")) {
                Map<String, Object> formItemListMap = JSONUtil.toBean(formItemList, Map.class);
                Map<String, Object> objectMap = FormUtil.getApiMapByConfigMap(formItemListMap, formData, new HashMap<>());
                resultMap.put(mapKey, objectMap);
            }
        }
        return resultMap;
    }


    /**
     * @param configMap 配置map
     * @param formData  json数据
     * @param resultMap 接受map
     * @description: 根据配置解析json格式--数据的展示和修改
     * @author: wuxx
     * @Date: 2021/4/30 10:18
     **/
    private static Map<String, Object> getMapByConfigMap(Map<String, Object> configMap, JSONObject formData, Map<String, Object> resultMap) {
        for (Map.Entry<String, Object> entry : configMap.entrySet()) {
            String mapKey = entry.getKey();
            Object mapValue = entry.getValue();
            resultMap.put(mapKey, null == formData.get(mapKey) ? "" : formData.get(mapKey));
            JSONObject object = JSONUtil.parseObj(mapValue.toString());
            String formItemList = object.getStr("formItemList");
            if (StrUtil.isNotBlank(formItemList) && !formItemList.equals("[]")) {
                Map<String, Object> formItemListMap = JSONUtil.toBean(formItemList, Map.class);
                Map<String, Object> objectMap = FormUtil.getInnerMapByConfigMap(formItemListMap, formData);
                resultMap.put(mapKey, objectMap);
            }
        }
        return resultMap;
    }

    /**
     * @param configMap 配置map
     * @param formData  json数据
     * @description: 根据配置解析json格式--数据的展示和修改
     * @author: wuxx
     * @Date: 2021/4/30 10:18
     **/
    private static Map<String, Object> getInnerMapByConfigMap(Map<String, Object> configMap, JSONObject formData) {
        Map<String, Object> resultMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : configMap.entrySet()) {
            //String mapKey = entry.getKey();
            Object mapValue = entry.getValue();
            JSONObject object = JSONUtil.parseObj(mapValue.toString());
            String formItemList = object.getStr("formItemList");
            if (StrUtil.isNotBlank(formItemList) && !formItemList.equals("[]")) {
                Map<String, Object> formItemListMap = JSONUtil.toBean(formItemList, Map.class);
                for (Map.Entry<String, Object> formItem : formItemListMap.entrySet()) {
                    String formItemKey = formItem.getKey();
                    resultMap.put(formItemKey, null == formData.get(formItemKey) ? "" : formData.get(formItemKey));
                }
            }
        }
        return resultMap;
    }

    /**
     * @param configMap  配置map
     * @param components 组件集合
     * @param resultMap  接受map
     * @description: 根据配置解析json格式--用户API结构的获取
     * @author: wuxx
     * @Date: 2021/4/30 10:18
     **/
    private static Map<String, Object> getApiMapConfig(Map<String, Object> configMap, List<String> components, Map<String, Object> resultMap) {
        for (Map.Entry<String, Object> entry : configMap.entrySet()) {
            String mapKey = entry.getKey();
            Object mapValue = entry.getValue();
            if (components.size() > 0 && !components.contains(mapKey)) {
                continue;
            }
            //resultMap.put(mapKey,null==mapValue?"":mapValue);
            JSONObject object = JSONUtil.parseObj(mapValue.toString());
            JSONObject result = new JSONObject();
            result.putOpt("label", object.getStr("label"));
            result.putOpt("type", object.getStr("type"));
            resultMap.put(mapKey, result);
            String formItemList = object.getStr("formItemList");
            if (StrUtil.isNotBlank(formItemList) && !formItemList.equals("[]")) {
                Map<String, Object> formItemListMap = JSONUtil.toBean(formItemList, Map.class);
                Map<String, Object> objectMap = FormUtil.getApiMapConfig(formItemListMap, components, new HashMap<>());
                resultMap.put(mapKey, objectMap);
            }
        }
        return resultMap;
    }


    /**
     * @description: 生成短位UUID
     * @author: wuxx
     * @Date: 2021/5/18 16:25
     **/
    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString().toUpperCase();

    }

    /**
     * @param jSONObject formDescJSON对象
     * @param code       编码
     * @description: 根据编码获取设计对象的单个信息
     * @author: wuxx
     * @Date: 2021/7/30 14:19
     **/
    public static JSONObject getObjByCode(JSONObject jSONObject, String code) {
        /*Set<String> keySets = jSONObject.keySet();
        Iterator<String> iterator = keySets.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (!key.equals(code)) {
                JSONObject formItemList = jSONObject.getJSONObject(key).getJSONObject("formItemList");
                if (null == formItemList || formItemList.size() == 0) {
                    iterator.remove();
                } else {
                    FormUtil.getObjByCode(formItemList, code);
                }
            }
        }*/
        Map<String, JSONObject> map = FormUtil.getObjMap(jSONObject, code, new HashMap<>());
        if(null!=map){
            return map.get(code);
        }
        return null;
    }

    /**
     * @param jSONObject formDescJSON对象
     * @param code       编码
     * @description: 根据编码获取设计对象的单个信息
     * @author: wuxx
     * @Date: 2021/7/30 14:19
     **/
    private static Map<String, JSONObject> getObjMap(JSONObject jSONObject, String code,Map<String, JSONObject> map) {
        Set<String> keySets = jSONObject.keySet();
        Iterator<String> iterator = keySets.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (!key.equals(code)) {
                JSONObject formItemList = jSONObject.getJSONObject(key).getJSONObject("formItemList");
                if (null != formItemList && formItemList.size() > 0) {
                    FormUtil.getObjMap(formItemList, code,map);
                }
            }else {
                map.put(key,jSONObject.getJSONObject(key));
                break;
            }
        }
        return map;
    }


    /**
     * @param jSONObject formDescJSON对象
     * @param code       编码
     * @description: 根据编码获取设计对象的信息--多层镶嵌
     * @author: wuxx
     * @Date: 2021/7/30 14:19
     **/
    public JSONObject getAllObjByKey(JSONObject jSONObject, String code) {
        Set<String> keySets = jSONObject.keySet();
        Iterator<String> iterator = keySets.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (!key.equals(code)) {
                JSONObject formItemList = jSONObject.getJSONObject(key).getJSONObject("formItemList");
                this.getAllObjByKey(formItemList, code);
                if (null == formItemList || formItemList.size() == 0) {
                    //jSONObject.remove(key);
                    iterator.remove();
                }
            }
        }
        return jSONObject;
    }

    /**
     * @description: 初始化formConfig参数
     * @author: wuxx
     * @Date: 2021/8/4 14:45
     **/
    public static JSONObject initFormConfig() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.putOpt("order", new JSONArray());
        jSONObject.putOpt("dynamicCss", new JSONArray());
        jSONObject.putOpt("dynamicJs", new JSONArray());
        jSONObject.putOpt("order", new JSONArray());
        jSONObject.putOpt("span", 24);
        jSONObject.putOpt("isShowLabel", true);
        jSONObject.putOpt("labelWidth", 100);
        jSONObject.putOpt("readonly", false);
        jSONObject.putOpt("isShowErrorNotify", true);
        jSONObject.putOpt("isShowSubmitBtn", false);
        jSONObject.putOpt("isShowBackBtn", false);
        jSONObject.putOpt("isShowResetBtn", false);
        jSONObject.putOpt("isShowCancelBtn", false);
        jSONObject.putOpt("formDesc", new JSONObject());
        return jSONObject;
    }

    /**
     * @param objectList     子组件配置
     * @param formConfigJson 配置规则json
     * @param replaceMap     配置替换的数据
     * @description: 创建折叠面板配置
     * @author: wuxx
     * @Date: 2021/8/4 15:26
     **/
    public static String createMergeFormConfig(List<JSONObject> objectList, String formConfigJson, Map<String, String> replaceMap) {
        JSONObject formConfig = FormUtil.initFormConfig();
        LinkedHashMap formConfigMap = JSONUtil.toBean(formConfig, LinkedHashMap.class);
        JSONArray orderArray = new JSONArray();
        try {
            /*formConfigJson="[\n" +
                    "    {\n" +
                    "        \"个人信息\" : {\n" +
                    "            \"tip\":\"个人信息后面的提示内容\",\n" +
                    "            \"contents\":[\n" +
                    "                {\n" +
                    "                    \"code\":\"name\",\n" +
                    "                    \"label\":\"名称1\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"code\":\"phone\",\n" +
                    "                    \"label\":\"电话2\"\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"其他信息1111\" : {\n" +
                    "            \"contents\": [\n" +
                    "                {\n" +
                    "                    \"code\":\"age\",\n" +
                    "                    \"label\":\"年龄111\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"code\":\"sex\",\n" +
                    "                    \"label\":\"性别\"\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    }\n" +
                    "]";*/
            LinkedHashMap formDescObject = new LinkedHashMap();
            String collapse_formDesc_key = "eleFormCollapse_" + FormUtil.generateShortUuid();
            JSONArray objectArray = JSONUtil.parseArray(formConfigJson);
            List<JSONObject> jsonObjectList = objectArray.toList(JSONObject.class);
            LinkedHashMap eleFormCollapseTop = new LinkedHashMap();
            eleFormCollapseTop.put("type", "eleFormCollapse");
            eleFormCollapseTop.put("label", "折叠面板");
            eleFormCollapseTop.put("layoutComponent", true);
            eleFormCollapseTop.put("isShowLabel", false);
            eleFormCollapseTop.put("labelWidth", "0px");
            eleFormCollapseTop.put("parentsKey", collapse_formDesc_key);
            orderArray.add(collapse_formDesc_key);
            LinkedHashMap collapse_formItemList = new LinkedHashMap();
            int i = 1;
            for (JSONObject object : jsonObjectList) {
                LinkedHashMap eleFormCollapseKey = new LinkedHashMap();
                for (String key : object.keySet()) {
                    LinkedHashMap eleFormCollapse = new LinkedHashMap();
                    String collapse_key = "collapse_" + i + FormUtil.generateShortUuid();
                    String titleTip = object.getJSONObject(key).getStr("tip");
                    eleFormCollapse.put("title", key);
                    eleFormCollapse.put("titleTip", null == titleTip ? "" : titleTip);
                    eleFormCollapse.put("parentsKey", collapse_formDesc_key);
                    JSONArray contentsArray = object.getJSONObject(key).getJSONArray("contents");
                    List<JSONObject> contentsList = contentsArray.toList(JSONObject.class);
                    LinkedHashMap linkedHashMap = new LinkedHashMap<>();
                    for (JSONObject contentsObject : contentsList) {
                        String code = contentsObject.getStr("code");
                        String label = contentsObject.getStr("label");
                        //System.out.println(code + "====="+label);
                        JSONObject jsonObject = FormUtil.getConfigByCode(objectList, code);
                        if (null!=jsonObject && jsonObject.size() > 0) {
                            jsonObject.set("isShowLabel",true);
                            JSONObject objectJSONObject = jsonObject.getJSONObject(code);
                            if(null!=objectJSONObject && objectJSONObject.size()>0){
                                objectJSONObject.putOpt("label", null==label?jsonObject.getStr("label"):label);
                                objectJSONObject.putOpt("parentsKey", collapse_formDesc_key + ":" + collapse_key);
                                //objectJSONObject.set("formConfig",jsonObject.getStr("formConfig"));
                                linkedHashMap.put(code,objectJSONObject);
                            }else{
                                jsonObject.putOpt("label", null==label?jsonObject.getStr("label"):label);
                                jsonObject.putOpt("parentsKey", collapse_formDesc_key + ":" + collapse_key);
                                //jsonObject.set("formConfig",jsonObject.getStr("formConfig"));
                                if("column".equals(jsonObject.getStr("type"))){
                                    String parentsKey = jsonObject.getJSONObject("formItemList").getJSONObject(code).getStr("parentsKey");
                                    String keyCode = parentsKey.substring(0, parentsKey.indexOf(":"));
                                    JSONObject jsonObjectParent = FormUtil.getConfigByCode(objectList, keyCode);
                                    linkedHashMap.put(keyCode,jsonObjectParent);
                                }else if("ChildForm".equals(jsonObject.getStr("type"))){//子表单
                                    child : for(JSONObject ob : objectList){
                                        JSONObject jsonObjectChildForm = ob.getJSONObject(code);
                                        if(null != jsonObjectChildForm){
                                            linkedHashMap.put(code,jsonObjectChildForm);
                                            break child;
                                        }
                                    }
                                }else if("ChildTableForm".equals(jsonObject.getStr("type"))){//子表格
                                    child : for(JSONObject ob : objectList){
                                        JSONObject jsonObjectChildForm = ob.getJSONObject(code);
                                        if(null != jsonObjectChildForm){
                                            linkedHashMap.put(code,jsonObjectChildForm);
                                            break child;
                                        }
                                    }
                                }else {
                                    linkedHashMap.put(code,jsonObject);
                                }
                            }
                            i++;
                        }
                    }
                    eleFormCollapse.put("formItemList", linkedHashMap);
                    eleFormCollapseKey.put(collapse_key, eleFormCollapse);
                    collapse_formItemList.put(collapse_key, eleFormCollapse);
                    // System.out.println(key + "====="+object.getJSONObject(key).getJSONArray("contents"));
                }
                eleFormCollapseTop.put("formItemList", collapse_formItemList);
                formDescObject.put(collapse_formDesc_key, eleFormCollapseTop);
                formConfigMap.put("formDesc", formDescObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        formConfigMap.put("order", orderArray);
        formConfig = JSONUtil.parseObj(formConfigMap);
        //System.out.println(formConfig.toString());
        String formConfigStr = formConfig.toString();
        //替换之前转义的数据
        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            formConfigStr = formConfigStr.replace(entry.getKey(), entry.getValue());
        }
        return formConfigStr;
    }

    /**
     * @param objectList     子组件配置
     * @param formConfigJson 配置规则json
     * @param replaceMap     配置替换的数据
     * @description: 创建折叠面板配置--子表单模式
     * @author: wuxx
     * @Date: 2021/8/4 15:26
     **/
    public static String createMergeFormConfigByChildForms(List<JSONObject> objectList, String formConfigJson, Map<String, String> replaceMap) {
        JSONObject formConfig = FormUtil.initFormConfig();
        LinkedHashMap formConfigMap = JSONUtil.toBean(formConfig, LinkedHashMap.class);
        JSONArray orderArray = new JSONArray();
        try {
            /*formConfigJson="[\n" +
                    "    {\n" +
                    "        \"个人信息\" : {\n" +
                    "            \"tip\":\"个人信息后面的提示内容\",\n" +
                    "            \"childProperty\":\"gldx1\",\n" +
                    "            \"contents\":[\n" +
                    "                {\n" +
                    "                    \"code\":\"name\",\n" +
                    "                    \"label\":\"名称1\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"code\":\"time11\",\n" +
                    "                    \"label\":\"时间1\"\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"其他信息1111\" : {\n" +
                    "            \"childProperty\":\"gldx2\",\n" +
                    "            \"contents\": [\n" +
                    "                {\n" +
                    "                    \"code\":\"erter\",\n" +
                    "                    \"label\":\"年龄111\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"code\":\"fsf\",\n" +
                    "                    \"label\":\"性别\"\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    }\n" +
                    "]";*/
            LinkedHashMap formDescObject = new LinkedHashMap();
            String collapse_formDesc_key = "eleFormCollapse_" + FormUtil.generateShortUuid();
            JSONArray objectArray = JSONUtil.parseArray(formConfigJson);
            List<JSONObject> jsonObjectList = objectArray.toList(JSONObject.class);
            LinkedHashMap eleFormCollapseTop = new LinkedHashMap();
            eleFormCollapseTop.put("type", "eleFormCollapse");
            eleFormCollapseTop.put("label", "折叠面板");
            eleFormCollapseTop.put("layoutComponent", true);
            eleFormCollapseTop.put("isShowLabel", false);
            eleFormCollapseTop.put("labelWidth", "0px");
            eleFormCollapseTop.put("parentsKey", collapse_formDesc_key);
            orderArray.add(collapse_formDesc_key);
            LinkedHashMap collapse_formItemList = new LinkedHashMap();
            int i = 1;
            for (JSONObject object : jsonObjectList) {
                LinkedHashMap eleFormCollapseKey = new LinkedHashMap();
                for (String key : object.keySet()) {
                    LinkedHashMap eleFormCollapse = new LinkedHashMap();
                    String collapse_key = "collapse_" + i + FormUtil.generateShortUuid();
                    String titleTip = object.getJSONObject(key).getStr("tip");
                    eleFormCollapse.put("title", key);
                    eleFormCollapse.put("titleTip", null == titleTip ? "" : titleTip);

                    String childProperty = object.getJSONObject(key).getStr("childProperty");
                    if(StrUtil.isBlank(childProperty)){
                        continue;
                    }
                    LinkedHashMap childForms = new LinkedHashMap();
                    LinkedHashMap childFormsAttrs = new LinkedHashMap();
                    childFormsAttrs.put("isShowBackBtn",false);
                    childFormsAttrs.put("isShowSubmitBtn",false);
                    childFormsAttrs.put("isShowLabel",true);
                    childFormsAttrs.put("labelWidth",100);
                    childFormsAttrs.put("readonly",false);
                    childFormsAttrs.put("isShowErrorNotify",true);
                    childFormsAttrs.put("isShowResetBtn",false);
                    childFormsAttrs.put("isShowCancelBtn",false);
                    childFormsAttrs.put("isShowChildFormBorder",true);
                    childFormsAttrs.put("span",24);
                    LinkedHashMap childFormsFormConfig = new LinkedHashMap();
                    childFormsFormConfig.put("order",new ArrayList<>());
                    childFormsFormConfig.put("dynamicCss",new ArrayList<>());
                    childFormsFormConfig.put("dynamicJs",new ArrayList<>());
                    childFormsFormConfig.put("labelPosition",null);
                    childFormsFormConfig.put("isShowLabel",true);
                    childFormsFormConfig.put("labelWidth",100);
                    childFormsFormConfig.put("isShowSubmitBtn",false);
                    childFormsFormConfig.put("isShowResetBtn",false);
                    childFormsFormConfig.put("isShowCancelBtn",false);
                    childFormsFormConfig.put("isShowChildFormBorder",true);

                    childForms.put("label","");
                    childForms.put("layoutComponent",true);
                    childForms.put("type","ChildForm");
                    childForms.put("isShowLabel",false);
                    childForms.put("labelWidth","0px");
                    childForms.put("attrs",childFormsAttrs);
                    childForms.put("formConfig",childFormsFormConfig);
                    childForms.put("options","");
                    childForms.put("prop","");
                    childForms.put("optionsLinkageFields","");
                    childForms.put("isArray",false);
                    childForms.put("isShowTitle",false);
                    childForms.put("title","");
                    childForms.put("isShowAdd",true);
                    childForms.put("parentsKey",collapse_formDesc_key + ":"+collapse_key);


                    eleFormCollapse.put("parentsKey", collapse_formDesc_key);
                    JSONArray contentsArray = object.getJSONObject(key).getJSONArray("contents");
                    List<JSONObject> contentsList = contentsArray.toList(JSONObject.class);
                    LinkedHashMap linkedHashMap = new LinkedHashMap<>();
                    for (JSONObject contentsObject : contentsList) {
                        String code = contentsObject.getStr("code");
                        String label = contentsObject.getStr("label");
                        //System.out.println(code + "====="+label);
                        JSONObject jsonObject = FormUtil.getConfigByCode(objectList, code);
                        if (null!=jsonObject && jsonObject.size() > 0) {
                            JSONObject objectJSONObject = jsonObject.getJSONObject(code);
                            if(null!=objectJSONObject && objectJSONObject.size()>0){
                                objectJSONObject.putOpt("label", null==label?jsonObject.getStr("label"):label);
                                objectJSONObject.putOpt("parentsKey", collapse_formDesc_key + ":" + collapse_key);
                                //objectJSONObject.set("formConfig",jsonObject.getStr("formConfig"));
                                linkedHashMap.put(code,objectJSONObject);
                            }else{
                                jsonObject.putOpt("label", null==label?jsonObject.getStr("label"):label);
                                jsonObject.putOpt("parentsKey", collapse_formDesc_key + ":" + collapse_key);
                                //jsonObject.set("formConfig",jsonObject.getStr("formConfig"));
                                if("column".equals(jsonObject.getStr("type"))){
                                    String parentsKey = jsonObject.getJSONObject("formItemList").getJSONObject(code).getStr("parentsKey");
                                    String keyCode = parentsKey.substring(0, parentsKey.indexOf(":"));
                                    JSONObject jsonObjectParent = FormUtil.getConfigByCode(objectList, keyCode);
                                    linkedHashMap.put(keyCode,jsonObjectParent);
                                }else {
                                    linkedHashMap.put(code,jsonObject);
                                }
                            }
                            i++;
                        }
                    }
                    LinkedHashMap collapseChildForms = new LinkedHashMap();
                    childForms.put("formItemList", linkedHashMap);
                    collapseChildForms.put(childProperty, childForms);


                    eleFormCollapse.put("formItemList", collapseChildForms);
                    eleFormCollapseKey.put(collapse_key, eleFormCollapse);
                    collapse_formItemList.put(collapse_key, eleFormCollapse);
                    // System.out.println(key + "====="+object.getJSONObject(key).getJSONArray("contents"));
                }
                eleFormCollapseTop.put("formItemList", collapse_formItemList);
                formDescObject.put(collapse_formDesc_key, eleFormCollapseTop);
                formConfigMap.put("formDesc", formDescObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        formConfigMap.put("order", orderArray);
        formConfig = JSONUtil.parseObj(formConfigMap);
        //System.out.println(formConfig.toString());
        String formConfigStr = formConfig.toString();
        //替换之前转义的数据
        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            formConfigStr = formConfigStr.replace(entry.getKey(), entry.getValue());
        }

        return formConfigStr;
    }


    /**
     * @param objectListParm json集合
     * @param code
     * @description: 根据code获取对应的配置
     * @author: wuxx
     * @Date: 2021/8/4 16:56
     **/
    private static JSONObject getConfigByCode(List<JSONObject> objectListParm, String code) {
        List<JSONObject> objectList = JSONUtil.toList(JSONUtil.parseArray(objectListParm.toString()), JSONObject.class);
        if (null != objectList && objectList.size() > 0) {
            for (JSONObject object : objectList) {
                JSONObject jsonObject = FormUtil.getObjByCode(object, code);
                if (null != jsonObject) {
                    return JSONUtil.parseObj(jsonObject.toString());
                }

            }
        }
        return null;
    }

    /**
     * @param formDescJSONObject formDesc
     * @param code
     * @description: 根据code获取对应的配置
     * @author: wuxx
     * @Date: 2021/8/4 16:56
     **/
    public static JSONObject getFormDescConfigByCode(JSONObject formDescJSONObject, String code) {
        JSONObject jsonObject = FormUtil.getObjByCode(formDescJSONObject, code);
        if (null != jsonObject) {
            return JSONUtil.parseObj(jsonObject.toString());
        }
        return null;
    }

    /**
     * @param formConfig formDesc
     * @description: 获取对应的配置集合
     * @author: wuxx
     * @Date: 2021/8/4 16:56
     **/
    public static Map<String, JSONObject> getCodeListByFormConfig(String formConfig,Set<String> columns, Map<String, Object> dataMap) {
        Map<String, JSONObject> resultMap = new HashMap<>();
        Map<String, String> replaceMap = new HashMap<>();
        if (StrUtil.isNotEmpty(formConfig)) {
            JSONObject jsonObject = null;
            try {
                //去除正则
                formConfig = FormUtil.subJsonStr(formConfig, "new RegExp", "),", replaceMap);
                //去除方法
                formConfig = FormUtil.subJsonStr(formConfig, "function", "},", replaceMap);
                //formConfig = FormUtil.subJsonStr(formConfig, "\"on\":", "},", replaceMap);
                jsonObject = JSONUtil.parseObj(formConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject formDescJSONObject = jsonObject.getJSONObject("formDesc");
            Map<String, JSONObject> jsonObjectMap = getCodeListByFormDesc(formDescJSONObject, new HashMap<>(),columns);
            Map<String, JSONObject> rulesObjectMap = Maps.newHashMap();
            rulesObjectMap.put("_mainObject", jsonObject.getJSONObject("rules"));
            getRulesListByColumns(formDescJSONObject, rulesObjectMap, columns);
            //配置里面所有属性的配置
            JSONObject configObj = JSONUtil.parseObj(jsonObjectMap);
            //无法转换城json需要替换的字符串信息
            JSONObject replaceObj = JSONUtil.parseObj(replaceMap);
            resultMap.put("configObj", configObj);
            resultMap.put("rulesObj", JSONUtil.parseObj(rulesObjectMap));
            resultMap.put("replaceObj", replaceObj);
        }
        return resultMap;
    }

    public static Map<String, JSONObject> getRulesListByColumns(JSONObject formDescJSONObject, Map<String, JSONObject> map, Set<String> columns) {
        Set<String> keySets = formDescJSONObject.keySet();
        Iterator<String> iterator = keySets.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object formItemListObject = formDescJSONObject.getJSONObject(key).get("formItemList");
            if (formItemListObject instanceof JSONObject) {
                JSONObject object = (JSONObject) formItemListObject;
                if (!(null == formItemListObject || object.size() == 0)) {
                    if(columns.contains(key)){
                        map.put(key, OptionalUtils.resolve(() -> formDescJSONObject.getJSONObject(key).getJSONObject("attrs").getJSONObject("rules")).orElse(new JSONObject()));
                    }else{
                        getRulesListByColumns(object, map,columns);
                    }

                }
            }
            if (formItemListObject instanceof JSONArray) {
                JSONArray array = (JSONArray) formItemListObject;
                if (!(null == array || array.size() == 0)) {
                    JSONObject formItemList = JSONUtil.parseObj(formItemListObject);
                    if(columns.contains(key)){
                        map.put(key, OptionalUtils.resolve(() -> formDescJSONObject.getJSONObject(key).getJSONObject("attrs").getJSONObject("rules")).orElse(new JSONObject()));
                    }else{
                        getRulesListByColumns(formItemList, map,columns);
                    }
                }
            }
        }
        return map;
    }

    public static String checkFormData(com.alibaba.fastjson.JSONObject formDataJsonObj, String formConfig, List<FormObjectExtand> formObjectExtands,Set<String> columns, Integer isZc) {
        Map<String, Object> dataMap = formDataJsonObj.toJavaObject(Map.class);
        String message = "";
        Map<String, JSONObject> codeListMap = getCodeListByFormConfig(formConfig ,columns, dataMap);
        JSONObject configObj = codeListMap.get("configObj");
        JSONObject replaceObj = codeListMap.get("replaceObj");
        JSONObject rulesObj = codeListMap.get("rulesObj");
        if (formObjectExtands != null && formObjectExtands.size() > 0) {
            for (int i = 0; i < formObjectExtands.size(); i++) {
                FormObjectExtand formObjectExtand = formObjectExtands.get(i);
                if (formObjectExtand.getType().equals(2)) {
                    // 20211102 modify by kkfan null值处理
                    Map<String, Object> dempMap = OptionalUtils.resolve(() -> formDataJsonObj.getJSONObject(formObjectExtand.getVariableName())).orElse(new com.alibaba.fastjson.JSONObject()).toJavaObject(Map.class);
                    JSONObject extandConfigObj = JSONUtil.parseObj(configObj.get(formObjectExtand.getVariableName()));
                    message = check(dempMap, extandConfigObj, replaceObj, rulesObj.getJSONObject(formObjectExtand.getVariableName()), isZc);
                    if (StrUtil.isNotBlank(message)) {
                        return message;
                    }
                } else {
                    com.alibaba.fastjson.JSONArray jsonArray = OptionalUtils.resolve(() -> formDataJsonObj.getJSONObject(formObjectExtand.getVariableName()).getJSONArray(formObjectExtand.getVariableName())).orElse(new com.alibaba.fastjson.JSONArray());
                    //未填写任何内容，前端传的是"service_change":[{}]或者"service_change":[{},{},{}]
                    //实际等同 "service_change":[]，改成"service_change":[]处理
                    boolean tagNullData = true;
                    for (Object o : jsonArray) {
                        if (((com.alibaba.fastjson.JSONObject) o).size() > 0) {
                            tagNullData = false;
                        }
                    }
                    if (tagNullData) {
                        jsonArray = new com.alibaba.fastjson.JSONArray();
                    }
                    jsonArray.stream().forEach(o -> {

                    });
                    for (Object o : jsonArray) {
                        //原生的json数据
                        Map<String, Object> map = ((com.alibaba.fastjson.JSONObject)o).toJavaObject(Map.class);
                        JSONObject extandConfigObj = JSONUtil.parseObj(configObj.get(formObjectExtand.getVariableName()));
                        message = check(map, (extandConfigObj.get(formObjectExtand.getVariableName()) == null) ? extandConfigObj : (JSONObject)extandConfigObj.get(formObjectExtand.getVariableName()), replaceObj, rulesObj.getJSONObject(formObjectExtand.getVariableName()), isZc);
                        if (StrUtil.isNotBlank(message)) {
                            return message;
                        }
                    }
                }
                configObj.remove(formObjectExtand.getVariableName());
            }
        }
        message = check(dataMap, configObj, replaceObj, rulesObj.getJSONObject("_mainObject"), isZc);
        if (StrUtil.isNotBlank(message)) {
            return message;
        }
        return message;
    }

    /**
     * @param formConfig 配置json
     * @param formData   表单数据 {"age":18,"sex":"1","phone":"15200001111","name":"4545","height":"1212cm"}
     * @description: 根据配置验证表单数据是否规范
     * @author: wuxx
     * @Date: 2021/8/17 15:23
     **/
    public static String checkFormDataByFormConfig(String formConfig, String formData, List<FormObjectExtand> formObjectExtands,Set<String> columns, Integer isZc) {
        com.alibaba.fastjson.JSONObject formDataJsonObj = null;
        try {
            formDataJsonObj = com.alibaba.fastjson.JSONObject.parseObject(formData);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        String message = checkFormData(formDataJsonObj, formConfig, formObjectExtands,columns, isZc == null ? 0 : isZc);
        return message;
    }

    public static String check(Map<String, Object> dataMap, JSONObject configObj, JSONObject replaceObj, JSONObject rulesObj, @NonNull Integer isZc) {
        for (String key : configObj.keySet()) {
            JSONObject jsonObject = configObj.getJSONObject(key);
            //是否必填标识
            String required = jsonObject.getStr("required");
            String label = jsonObject.getStr("label");
            String vif = jsonObject.getStr("vif");
            // 20210910 kkfan 忽略隐藏属性
            if(StringUtils.isNotEmpty(vif) && !StringUtils.equals(vif, "undefined")) {
                vif = Base64Decoder.decodeStr(vif);
                Boolean isShow = (Boolean)ScriptUtil.execJs(vif, dataMap);
                if (!BooleanUtils.isTrue(isShow)) {
                    continue;
                }
            }
            Object data = dataMap.get(key);
            if(data instanceof Integer && ((Integer)data) == 0 && isZc == 1) {
                continue;
            }
            if ("true".equals(required) && isZc != 1) {

                if (null == data) {
                    return label + "必填";
                } else {
                    if (StrUtil.isBlank(String.valueOf(data))) {
                        return label + "必填";
                    }
                    if (data instanceof List && CollectionUtils.isEmpty((List)data)) {
                        return label + "必填";
                    }
                }
            }
            JSONObject attrs = jsonObject.getJSONObject("attrs");
            if (null != attrs) {
                if (null != data && data instanceof String && StringUtils.isNotEmpty((String)data)) {
                    Integer minlength = null == attrs.getInt("minlength") ? 0 : attrs.getInt("minlength");
                    Integer maxlength = null == attrs.getInt("maxlength") ? 0 : attrs.getInt("maxlength");
                    if (data != null && 0 != maxlength && (((String)data).length() < minlength || ((String)data).length() > maxlength)) {
                        return label + "长度不能小于" + minlength + "或者大于" + maxlength;
                    }
                }
            }
            //正则验证
            Object rules = OptionalUtils.resolve(() -> "undefined".equals(String.valueOf(jsonObject.get("rules"))) ? null : jsonObject.get("rules")).orElse(new JSONArray());
            JSONArray rulesJSONArray = null;
            if(rules instanceof JSONObject) {
                rulesJSONArray = JSONUtil.parseArray("[" + JSONUtil.toJsonStr(rules) + "]");
            } else {
                rulesJSONArray = JSONUtil.parseArray(JSONUtil.toJsonStr(rules));
            }
            Object ruleObjs = OptionalUtils.resolve(() -> "undefined".equals(String.valueOf(rulesObj.getObj(key))) ? null : rulesObj.getObj(key)).orElse(new JSONArray());
            JSONArray rulesObjJSONArray = null;
            if(ruleObjs instanceof JSONObject) {
                rulesObjJSONArray = JSONUtil.parseArray("[" + JSONUtil.toJsonStr(ruleObjs) + "]");
            } else {
                rulesObjJSONArray = JSONUtil.parseArray(JSONUtil.toJsonStr(ruleObjs));
            }
            rulesJSONArray.addAll(rulesObjJSONArray);
            if(null != rulesJSONArray){
//                String data = String.valueOf(dataMap.get(key));
                List<JSONObject> objectList = rulesJSONArray.toList(JSONObject.class);
                for (JSONObject ruleObject : objectList) {
                    String type = ruleObject.getStr("type");
                    Boolean requiredFlag = ruleObject.getBool("required", false);
                    if(!("true".equals(required) || data != null && !"null".equals(data) && StringUtils.isNotEmpty(String.valueOf(data))
                            || requiredFlag )
                                || (isZc == 1 && ((data == null) || (data instanceof String && StringUtils.isEmpty((String) data))))) {
                        continue;
                    }
                    String message = ruleObject.getStr("message");
                    if(requiredFlag) {
                        Object curData = dataMap.get(key);
                        if (null == curData && isZc == 1) {
                            return label + "必填";
                        } else {
                            if (StrUtil.isBlank(String.valueOf(curData)) && isZc == 1) {
                                return label + "必填";
                            }
                            if (curData instanceof List && CollectionUtils.isEmpty((List)curData) && isZc == 1) {
                                return label + "必填";
                            }
                        }
                    }
                    if("string".equals(type) || "url".equals(type) || StringUtils.isEmpty(type)){
                        String pattern = ruleObject.getStr("pattern");
                        if(StrUtil.isNotBlank(pattern)){

                            String replaceObjStr = replaceObj.getStr("\"" + pattern + "\"");
                            String patternStr = "";
                            if (StrUtil.isNotBlank(replaceObjStr)) {
                                patternStr = replaceObjStr.substring(replaceObjStr.indexOf("^"), replaceObjStr.lastIndexOf("$") + 1);
                            } else {
                                patternStr = pattern;
                            }
                            patternStr = patternStr.replaceAll(Matcher.quoteReplacement("\\\\"), Matcher.quoteReplacement("\\"));
                            boolean match = ReUtil.isMatch(patternStr, String.valueOf(dataMap.get(key)));
                            if (!match) {
                                return StringUtils.isEmpty(message) ? label + "校验失败！" : message;
                            }
                        }
                    }

                    if("email".equals(type)){
                        boolean flag = Validator.isEmail(String.valueOf(data));
                        if(!flag){
                            return StringUtils.isEmpty(message) ? label + "格式不正确" : message;
                        }
                    }
                    if("hex".equals(type)){
                        boolean hex = Validator.isHex(String.valueOf(data));
                        if(!hex){
                            return StringUtils.isEmpty(message) ? label + "格式不正确" : message;
                        }
                    }

                    if("array".equals(type)){
                        if(!(dataMap.get(key) instanceof JSONArray)){
                            return StringUtils.isEmpty(message) ? label + "格式不正确" : message;
                        }
                    }
                    if("object".equals(type)){
                        if(!(dataMap.get(key) instanceof JSONObject)){
                            return StringUtils.isEmpty(message) ? label + "格式不正确" : message;
                        }
                    }
                    if("url".equals(type)){
                        String patternStr = "^(?!mailto:)(?:(?:http|https|ftp)://|//)(?:\\\\S+(?::\\\\S*)?@)?(?:(?:(?:[1-9]\\\\d?|1\\\\d\\\\d|2[01]\\\\d|22[0-3])(?:\\\\.(?:1?\\\\d{1,2}|2[0-4]\\\\d|25[0-5])){2}(?:\\\\.(?:[0-9]\\\\d?|1\\\\d\\\\d|2[0-4]\\\\d|25[0-4]))|(?:(?:[a-z\\\\u00a1-\\\\uffff0-9]+-*)*[a-z\\\\u00a1-\\\\uffff0-9]+)(?:\\\\.(?:[a-z\\\\u00a1-\\\\uffff0-9]+-*)*[a-z\\\\u00a1-\\\\uffff0-9]+)*(?:\\\\.(?:[a-z\\\\u00a1-\\\\uffff]{2,})))|localhost)(?::\\\\d{2,5})?(?:(/|\\\\?|#)[^\\\\s]*)?$";
                        boolean flag = ReUtil.isMatch(patternStr.replaceAll(Matcher.quoteReplacement("\\\\"), Matcher.quoteReplacement("\\")), String.valueOf(dataMap.get(key)));
                        // if (StringUtils.startsWith(String.valueOf(dataMap.get(key)), "//")) {
                        //     dataMap.put(key, "http:".concat(String.valueOf(dataMap.get(key))));
                        // }
                        // boolean flag = Validator.isUrl(data);
                        if(!flag){
                            return StringUtils.isEmpty(message) ? label + "格式不正确" : message;
                        }
                    }
                    if("boolean".equals(type)){
                        if(!(dataMap.get(key) instanceof Boolean)){
                            return StringUtils.isEmpty(message) ? label + "格式不正确" : message;
                        }
                    }

                    //String type = ruleObject.getStr("type");
                    String minStr=ruleObject.getStr("min");
                    String maxStr=ruleObject.getStr("max");
                    if("number".equals(type)||"integer".equals(type)){
                        if(StrUtil.isNotBlank(minStr)){
                            Double value=Double.valueOf(String.valueOf(data));
                            Integer min=Integer.valueOf(minStr);
                            if(value<min){
                                return label + "不能小于" + min;
                            }
                        }
                        if(StrUtil.isNotBlank(maxStr)){
                            Double value=Double.valueOf(String.valueOf(data));
                            Integer max=Integer.valueOf(maxStr);
                            if(value>max){
                                return label + "不能大于" + max;
                            }
                        }
                    }else{
                        if(StrUtil.isNotBlank(minStr)){
                            Integer min=Integer.valueOf(minStr);
                            if(String.valueOf(data).length()<min){
                                return label + "长度不能小于" + min;
                            }
                        }
                        if(StrUtil.isNotBlank(maxStr)){
                            Integer max=Integer.valueOf(maxStr);
                            if(String.valueOf(data).length()>max){
                                return label + "长度不能大于" + max;
                            }
                        }
                    }
                }
            }
        }
        return "";
    }

    /**
     * @param formDescJSONObject formDesc
     * @description: 获取对应的配置集合
     * @author: wuxx
     * @Date: 2021/8/4 16:56
     **/
    private static Map<String, JSONObject> getCodeListByFormDesc(JSONObject formDescJSONObject, Map<String, JSONObject> map,Set<String> columns) {
        Set<String> keySets = formDescJSONObject.keySet();
        Iterator<String> iterator = keySets.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object formItemListObject = formDescJSONObject.getJSONObject(key).get("formItemList");
            if (formItemListObject instanceof JSONObject) {
                JSONObject object = (JSONObject) formItemListObject;
                if (null == formItemListObject || object.size() == 0) {
                   // if(columns.contains(key)){
                        map.put(key, formDescJSONObject.getJSONObject(key));
                   // }
                } else {
                    if(columns.contains(key)){
                        Map<String, JSONObject> codeListByFormDesc = getCodeListByFormDesc(object, new HashMap<>(),columns);
                        map.put(key, JSONUtil.parseObj(codeListByFormDesc));
                    }else{
                        getCodeListByFormDesc(object, map,columns);
                    }

                }
            }
            if (formItemListObject instanceof JSONArray) {
                JSONArray array = (JSONArray) formItemListObject;
                if (null == array || array.size() == 0) {
                  //  if(columns.contains(key)){
                        map.put(key, formDescJSONObject.getJSONObject(key));
                  //  }
                } else {
                    JSONObject formItemList = JSONUtil.parseObj(formItemListObject);
                    if(columns.contains(key)){
                        Map<String, JSONObject> codeListByFormDesc = getCodeListByFormDesc(formItemList, new HashMap<>(),columns);
                        map.put(key, JSONUtil.parseObj(codeListByFormDesc));
                    }else{
                        getCodeListByFormDesc(formItemList, map,columns);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 截取 startStr endStr 内的字符串
     *
     * @param origin 源字符串
     * @return
     */
    public static String subJsonStr(String origin, String startStr, String endStr, Map<String, String> replaceMap) {
        if (StrUtil.isEmpty(origin)) {
            return null;
        }
        String originTemp = origin;
        int start, end;
        while (true) {
            // 索引出现负数，说明在源字符串指定位置之后
            start = origin.indexOf(startStr);
            if (start == -1) {
                break;
            }
            origin = origin.substring(start);
            end = origin.indexOf(endStr);
            // substring 内部索引禁止出现负数
            if (end == -1) {
        //        break;
            }

            String tmp = "";
            tmp=genSubStr(tmp,endStr,origin);
//            String subKey = "\"" + "SUB" + DateUtil.format(new Date(), "yyyyMMdd") + FormUtil.generateShortUuid() + "\"";
            String subKey = "\"" + Base64Encoder.encode(tmp) + "\"";
            originTemp = originTemp.replace(tmp, subKey);
            replaceMap.put(subKey, tmp);
            //保存上一次截取时的索引
            origin = originTemp;
            //return originTemp;
        }
        return originTemp;
    }

    /**
     * 将含有子列表的字符串的formConfig改成符合规则的JSON串
     */
    public static String changeToRightJsonStr(String formConfig) {
        //匹配：     "pattern": new RegExp("^(([^0][0-9]+|[0-9])\\.([0-9]{1,2}))$", ""),
        formConfig = formConfig.replaceAll(": new RegExp\\(.*\\),\n", ":\"\",\n");

        //匹配：  "valueFormatter": function(e){return e||null},   和  "valueFormatter": function(e){return e||null}
        formConfig = formConfig.replaceAll(": function\\(.*}((,\n)|(\n))", ":\"\",\n");

        //匹配 设计表单页面时写的各种function 是有问题，要约定俗成 方法体内不得出现 },  这两个字符的这样组合。
        formConfig = FormUtil.subJsonStr(formConfig, "function", "},", new HashMap<>());
        return formConfig;
    }

    public static String genSubStr(String subStr,String endStr,String originStr){
       int end = originStr.indexOf(endStr);
        // substring 内部索引禁止出现负数
        String tmp="";
        if(end==-1&&endStr.equals("},")){
            tmp=subStr+originStr.substring(0, originStr.lastIndexOf("}"));
        }else{
            tmp = subStr+originStr.substring(0, end + endStr.length() - 1);
        }
        if(StrUtil.isNotBlank(tmp)){
            char[] chars = tmp.toCharArray();
            int left=0;
            int right=0;
            for (int i = 0; i < chars.length; i++) {
                if(chars[i]=='{'){
                    left++;
                }
                if(chars[i]=='}'){
                    right++;
                }
            }
            int subIndex=0;
            if(left<right){
                int index=Math.abs(left-right);
                for (int i = 0; i < index; i++) {
                    subIndex= tmp.lastIndexOf("}");
                    tmp=tmp.substring(0,subIndex);
                }
            }
            if(left>right){
                originStr=originStr.substring(originStr.indexOf(endStr) + endStr.length() - 1);
                tmp=genSubStr(tmp,endStr,originStr);
            }
            return tmp;
        }
      return "";
    }
}
