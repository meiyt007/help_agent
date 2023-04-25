package com.zfsoft.microservice.form.manager;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.zfsoft.microservice.form.data.*;
import com.zfsoft.microservice.form.data.vo.FormDataVo;
import com.zfsoft.microservice.form.thread.DelTempFileThred;
import com.zfsoft.microservice.form.util.*;
import com.zfsoft.microservice.form.util.poitl.PoiTlUtil;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

import static com.zfsoft.microservice.form.util.poitl.PoiTlUtil.FIXED_THREAD_POOL;

/**
 * @description: 电子表单管理服务接口实现类
 * @author: wuxx
 * @Date: 2021/4/22 13:07
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "form:manager")
public class FormManagerManager {
    @Resource
    private FormMainManager formMainManager;

    @Resource
    private FormReportManager formReportManager;

    @Resource
    private FormObjectManager formObjectManager;

    @Resource
    private FormColumnManager formColumnManager;

    @Resource
    private FormDesignManager formDesignManager;

    @Resource
    private FormReportLogManager formReportLogManager;

    @Resource
    private FormDataSourceManager formDataSourceManager;

    @Resource
    private FormModuleManager formModuleManager;

    @Resource
    private FormAttaManager formAttaManager;

    @Resource
    private FormObjectExtandManager formObjectExtandManager;

    @Resource
    private DocxTemplateManager docxTemplateManager;

    /**
     * @description: 存储对象的数据的保存本地、数据库、接口返回
     * @author: wuxx
     * @Date: 2021/4/22 13:07
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public Map<String, Object> saveFormData(@ValidGroups(groups = {FormDataVo.INSERT_GROUP.class}) FormDataVo formDataVo) {
        Map<String, Object> resultMap = new HashMap<>();
        String designOid = formDataVo.getDesignOid();
        String formMainOid = formDataVo.getFormMainOid();
        FormDesign design = null;
        if (StrUtil.isBlank(designOid)) {
            design = formDesignManager.getFormDesignByFormMainOid(formMainOid);
        } else {
            design = formDesignManager.getFormDesignByDesignOid(designOid);
        }
        if (null == design) {
            throw new ResultInfoException("表单设计对象信息为空！");
        }
        FormMain formMain = formMainManager.getFormMainByFormMainOid(design.getFormMainOid());
        if (null == formMain) {
            throw new ResultInfoException("表单对象信息为空！");
        }
        //数据库存储类型  0本地  1API  2数据库
        //后续如果使用记录表的类型在放开
        Integer dataType = formMain.getSaveDataType();
        if (null == dataType) {
            dataType = FormStaticParameter.SAVE_DATA_TYPE_0;
        }
        FormObject formObject = formObjectManager.getFormObjectByObjectOid(formMain.getObjectOid());
        if (null == formObject) {
            throw new ResultInfoException("存储对象信息为空！");
        }

        String formData = formDataVo.getFormData();
        // 修改值为null时被读取成"null"问题
        JSONObject object = JSONObject.parseObject(formData);
        //原生的json数据
        Map<String, Object> dataMap = JSONObject.parseObject(object.toJSONString(), Map.class);
        FormColumn formColumn = new FormColumn();
        formColumn.setObjectOid(design.getObjectOid());
        Set<String> keySet = dataMap.keySet();
        if (keySet.size() == 0) {
            throw new ResultInfoException("填报数据不能为空！");
        }
        FormDataSource formDataSource = this.formDataSourceManager.getFormDataSourceByDataSourceOid(formObject.getDatasourceOid());
        List<FormColumn> formColumnList = formColumnManager.queryFormColumnList(formColumn);
        Map<String, Object> savaDataMap = new LinkedHashMap<>();
        Map<String, Object> apiDataMap = new LinkedHashMap<>();
        for (String key : keySet) {
            FormColumn column = formColumnList.stream().filter(innerFormColumn -> innerFormColumn.getObjectProperty().equals(key)).findFirst()
                    .orElse(null);
            if (null != column) {
                //实体类属性名称
                String objectProperty = column.getObjectProperty();
                //数据库字段名称
                String columnName = column.getColumnName();
                //默认值
                String defaultValue = column.getDefaultValue();
                Object data = dataMap.get(objectProperty);
                if (null == data && StrUtil.isNotBlank(defaultValue)) {
                    data = defaultValue;
                }
                //主键字段生成
                if ("id".equals(objectProperty)) {
                    IdWorker idWorker = new IdWorker(0, 0);
                    long nextId = idWorker.nextId();
                    data = nextId;
                }
                //创建时间
                /*if("createDate".equals(objectProperty)){
                    data = DateUtil.formatDateTime(new Date());
                }*/
                Integer type = column.getDataType();
                if (3 == type.intValue() && FormStaticParameter.SAVE_DATA_TYPE_2 == dataType) {
                    //文件类型的数据库存储
                    List<String> attaOidList = new ArrayList<>();
                    //图片类型的数据库存储
                    List<String> imagesOidList = new ArrayList<>();
                    //单文件
                    if (data instanceof JSONObject) {
                        if (data instanceof JSONObject) {
                            JSONObject jsonObject = (JSONObject) data;
                            String attaOid = jsonObject.getString("attaOid");
                            if (StrUtil.isNotBlank(attaOid))
                                attaOidList.add(attaOid);
                        }
                    }
                    //多文件
                    if (data instanceof JSONArray) {
                        JSONArray array = (JSONArray) data;
                        array.stream().forEach(item -> {
                            if (item instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) item;
                                String attaOid = jsonObject.getString("attaOid");
                                if (StrUtil.isNotBlank(attaOid))
                                    attaOidList.add(attaOid);
                            }
                            //多图片
                            if (item instanceof String) {
                                if (null != item && item.toString().contains("/form-api/form/manager/downloadFile/")) {
                                    String attaOid = ((String) item).replaceAll("/form-api/form/manager/downloadFile/", "");
                                    if (StrUtil.isNotBlank(attaOid))
                                        imagesOidList.add(attaOid);
                                }
                            }
                        });
                    }
                    //单图片
                    if (data instanceof String) {
                        if (null != data && data.toString().contains("/form-api/form/manager/downloadFile/")) {
                            String attaOid = ((String) data).replaceAll("/form-api/form/manager/downloadFile/", "");
                            if (StrUtil.isNotBlank(attaOid))
                                imagesOidList.add(attaOid);
                        }
                    }
                    if (attaOidList.size() > 0) {
                        savaDataMap.put(columnName, String.join("~", attaOidList) + "~");
                    } else if (imagesOidList.size() > 0) {
                        savaDataMap.put(columnName, String.join("@", imagesOidList) + "@");
                    } else {
                        savaDataMap.put(columnName, data);
                    }
                    apiDataMap.put(objectProperty, data);
                } else {
                    if (data instanceof JSONArray || data instanceof JSONObject || data instanceof Integer) {
                        savaDataMap.put(columnName, data);
                        apiDataMap.put(objectProperty, data);
                    } else {
                        apiDataMap.put(objectProperty, data);
                        //解决达蒙时间的问题
                        if (null != formDataSource && StringUtils.equalsAnyIgnoreCase(formDataSource.getType(), "达梦", "dm") && (null != column.getColumnType() && StringUtils.equalsAny(column.getColumnType().toUpperCase(), "DATE", "TIMESTAMP"))) {
                            if (null != data) {
                                String dataStr = data.toString();
                                if (dataStr.length() == 10) {
                                    data = "DATE:to_date('" + data + "','yyyy-mm-dd')";
                                } else if (dataStr.length() > 10) {
                                    data = "DATE:to_date('" + data + "','yyyy-mm-dd,hh24:mi:ss')";
                                }
                            }
                        }
                        //解决Oracle时间的问题
                        if (null != formDataSource && StringUtils.equalsAnyIgnoreCase(formDataSource.getType(), "oracle") && (null != column.getColumnType())) {
                            if (StringUtils.equalsAny(column.getColumnType().toUpperCase(), "DATE")) {
                                if (null != data) {
                                    String dataStr = data.toString();
                                    if (dataStr.length() == 10) {
                                        data = "DATE:to_date('" + data + "','yyyy-mm-dd')";
                                    } else if (dataStr.length() > 10) {
                                        data = "DATE:to_date('" + data + "','yyyy-mm-dd,hh24:mi:ss')";
                                    }
                                }
                            }
                            if (StringUtils.equalsAny(column.getColumnType().toUpperCase(), "TIMESTAMP")) {
                                if (null != data) {
                                    String dataStr = data.toString();
                                    if (dataStr.length() == 10) {
                                        data = "DATE:to_timestamp('" + data + "','yyyy-mm-dd')";
                                    } else if (dataStr.length() > 10) {
                                        data = "DATE:to_timestamp('" + data + "','yyyy-mm-dd,hh24:mi:ss')";
                                    }
                                }
                            }

                        }
                        savaDataMap.put(columnName, data);
                    }
                }

            }
        }
        //判断默认值 以及数据库是否必填
        if (!apiDataMap.isEmpty()) {
            for (FormColumn column : formColumnList) {
                //主键字段生成
                if ("id".equals(column.getObjectProperty())) {
                    IdWorker idWorker = new IdWorker(0, 0);
                    long nextId = idWorker.nextId();
                    savaDataMap.put(column.getColumnName(), nextId);
                    apiDataMap.put(column.getObjectProperty(), nextId);
                }
                //创建时间
                /*if("createDate".equals(column.getObjectProperty())){
                    String createDateStr = DateUtil.formatDateTime(new Date());
                    savaDataMap.put(column.getColumnName(),createDateStr);
                    apiDataMap.put(column.getObjectProperty(),createDateStr);
                }*/

                Object value = apiDataMap.get(column.getObjectProperty());
                //实体类属性名称
                String objectProperty = column.getObjectProperty();
                //数据库字段名称
                String columnName = column.getColumnName();
                if (null == value) {
                    String defaultValue = column.getDefaultValue();
                    if (StrUtil.isNotBlank(defaultValue)) {
                        savaDataMap.put(columnName, defaultValue);
                        apiDataMap.put(objectProperty, defaultValue);
                    }
                }
                Integer notNull = column.getNotNull();
                if (null != notNull && 1 == notNull && null == value) {
//                    throw new ResultInfoException("数据库字段"+columnName+"不能为空！");
                }
            }
        }
        if (apiDataMap.size() > 0) {
            //0本地
            if (FormStaticParameter.SAVE_DATA_TYPE_0 == dataType) {
                //表单本地数据库保存
                FormReport formReport = new FormReport();
                if (StrUtil.isNotBlank(formDataVo.getReportOid())) {
                    FormReport report = formReportManager.getFormReportByFormReportOid(formDataVo.getReportOid());
                    formReport.setReportOid(formDataVo.getReportOid());
                    formReport.setId(null != report ? report.getId() : null);
                }

                formReport.setBusinessKey(formDataVo.getBusinessKey());
                formReport.setFormMainOid(design.getFormMainOid());
                formReport.setDesignOid(formDataVo.getDesignOid());
                formReport.setAuthorizeKey(formDataVo.getAuthorizeKey());
                formReport.setFormData(JSONObject.toJSONString(apiDataMap, SerializerFeature.WriteMapNullValue));
                formReport.setFormName(formMain.getFormName());
                formReport.setVersion(formMain.getVersion());
                formReportManager.saveFormReport(formReport);
                formDataVo.setReportOid(formReport.getReportOid());
                //返回的数据库主键
                resultMap.put("reportOid", formReport.getReportOid());
            } else if (FormStaticParameter.SAVE_DATA_TYPE_2 == dataType) {
                //远程数据库保存
                String insertSql = DbSqlUtil.getInsertSql(formObject.getObjectForm(), savaDataMap);
                String datasourceOid = formObject.getDatasourceOid();
                FormDataSource dataSource = formDataSourceManager.getFormDataSourceByDataSourceOid(datasourceOid);
                if (null == dataSource) {
                    throw new ResultInfoException("数据源对象信息为空！");
                }
                DBHelper helper = new DBHelper(dataSource);
                String formDataId = null;
                try {
                    formDataId = helper.insertDbSql(insertSql, formObject.getObjectForm(), savaDataMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ResultInfoException("存储数据保存异常！" + e.getMessage());
                }
                if (StrUtil.isBlank(formDataId)) {
                    throw new ResultInfoException("存储数据保存异常！");
                }
                dataMap.put("reportOid", formDataId);
                //返回的数据库主键
                resultMap.put("reportOid", formDataId);
            } else if (FormStaticParameter.SAVE_DATA_TYPE_1 == dataType) {
                //API访问 post请求
                //后续如果使用记录表的类型在放开
                //String apiUrl = design.getApiUrl();
                String apiUrl = formMain.getApiUrl();
                //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("formData", JSONObject.toJSONString(apiDataMap, SerializerFeature.WriteMapNullValue));
                HttpUtil.post(apiUrl, paramMap);
            }

            //保存数据填报日志
            FormReportLog formReportLog = new FormReportLog();
            formReportLog.setFormMainOid(design.getFormMainOid());
            formReportLog.setReportOid(formDataVo.getReportOid());
            formReportLog.setDesignOid(formDataVo.getDesignOid());
            formReportLog.setAuthorizeKey(formDataVo.getAuthorizeKey());
            formReportLog.setFormData(JSONObject.toJSONString(apiDataMap, SerializerFeature.WriteMapNullValue));
            formReportLog.setFormName(formMain.getFormName());
            formReportLog.setVersion(formMain.getVersion());
            formReportLog.setSaveDataType(formMain.getSaveDataType());
            formReportLogManager.saveFormReportLog(formReportLog);
        } else {
            throw new ResultInfoException("保存失败，存储数据无关联存储对象！");
        }
        //设计历史表主键
        resultMap.put("designOid", design.getDesignOid());
        //存储的数据
        resultMap.put("objectData", dataMap);
        return resultMap;
    }

    public String saveToApi(String apiUrl, Map<String, Object> apiDataMap) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("formData", JSONObject.toJSONString(apiDataMap, SerializerFeature.WriteMapNullValue));
        String post = HttpUtil.post(apiUrl, paramMap);
        return post;
    }

    public boolean isUpdate(FormObject formObject, Map<String, Object> map) {
        Map<String, Object> stringMap = new HashMap<>();
        Object id = null;
        if (map.get("ID") != null) {
            id = map.get("ID");
        }
        if (map.get("id") != null) {
            id = map.get("id");
        }
        if (id == null) {
            return false;
        }
        stringMap.put("ID", id);
        String selectSql = DbSqlUtil.getSelectSql(formObject.getObjectForm(), stringMap);
        String datasourceOid = formObject.getDatasourceOid();
        FormDataSource dataSource = formDataSourceManager.getFormDataSourceByDataSourceOid(datasourceOid);
        DBHelper helper = new DBHelper(dataSource);
        Map<String, Object> resultMap = helper.selectDbSql(selectSql, formObject.getObjectForm(), stringMap, formObject.getObjectForm());
        if (null == resultMap) {
            return false;
        } else {
            return true;
        }
    }

    public String saveToDataBase(FormObject formObject, FormDataTemp formDataTemp) {
        String insertSql = "";
        String formDataId = null;
        boolean flag = true;
        if (formDataTemp.getSavaDataMap().size() == 1) {
            for (String id : formDataTemp.getSavaDataMap().keySet()) {
                if (id != null && id.toUpperCase().equals("ID")) {
                    flag = false;
                }
            }
        }
        if (flag) {
            if (isUpdate(formObject, formDataTemp.getSavaDataMap())) {
                insertSql = DbSqlUtil.getUpdateSql(formObject.getObjectForm(), formDataTemp.getSavaDataMap());
            } else {
                insertSql = DbSqlUtil.getInsertSql(formObject.getObjectForm(), formDataTemp.getSavaDataMap());
            }
            String datasourceOid = formObject.getDatasourceOid();
            FormDataSource dataSource = formDataSourceManager.getFormDataSourceByDataSourceOid(datasourceOid);
            if (null == dataSource) {
                throw new ResultInfoException("数据源对象信息为空！");
            }
            DBHelper helper = new DBHelper(dataSource);
            try {
                formDataId = helper.insertDbSql(insertSql, formObject.getObjectForm(), formDataTemp.getSavaDataMap());
            } catch (Exception e) {
                e.printStackTrace();
                throw new ResultInfoException("存储数据保存异常！" + e.getMessage());
            }
            if (StrUtil.isBlank(formDataId)) {
                throw new ResultInfoException("存储数据保存异常！");
            }
        }
        return formDataId;
    }

    public String saveToFormSystem(String formOid, String formName, Integer version, FormDataVo formDataVo, Map<String, Object> apiDataMap) {
        FormReport formReport = new FormReport();
        if (StrUtil.isNotBlank(formDataVo.getReportOid())) {
            FormReport report = formReportManager.getFormReportByFormReportOid(formDataVo.getReportOid());
            formReport.setReportOid(formDataVo.getReportOid());
            formReport.setId(null != report ? report.getId() : null);
        }

        formReport.setBusinessKey(formDataVo.getBusinessKey());
        formReport.setFormMainOid(formOid);
        formReport.setDesignOid(formDataVo.getDesignOid());
        formReport.setAuthorizeKey(formDataVo.getAuthorizeKey());
        formReport.setFormData(JSONObject.toJSONString(apiDataMap, SerializerFeature.WriteMapNullValue));
        formReport.setFormName(formName);
        formReport.setVersion(version);
        formReportManager.saveFormReport(formReport);
        return formReport.getReportOid();
        //formDataVo.setReportOid(formReport.getReportOid());
        //返回的数据库主键
        //resultMap.put("reportOid", formReport.getReportOid());
    }

    public FormDataTemp toDataBase(String objectOid, String dataId, Map<String, Object> dataMap, FormObjectExtand formObjectExtand, String foreignKeyValue) {
        FormDataTemp formDataTemp = new FormDataTemp();
        FormObject formObject = formObjectManager.getFormObjectByObjectOid(objectOid);
        FormColumn formColumn = new FormColumn();
        formColumn.setObjectOid(formObject.getObjectOid());
        FormDataSource formDataSource = this.formDataSourceManager.getFormDataSourceByDataSourceOid(formObject.getDatasourceOid());
        List<FormColumn> formColumnList = formColumnManager.queryFormColumnList(formColumn);
        Set<String> keySet = dataMap.keySet();
        Map<String, Object> savaDataMap = new LinkedHashMap<>();
        Map<String, Object> apiDataMap = new LinkedHashMap<>();
        for (String key : keySet) {
            FormColumn column = formColumnList.stream().filter(innerFormColumn -> innerFormColumn.getObjectProperty().equals(key)).findFirst()
                    .orElse(null);
            if (null != column) {
                //实体类属性名称
                String objectProperty = column.getObjectProperty();
                //数据库字段名称
                String columnName = column.getColumnName();
                //默认值
                String defaultValue = column.getDefaultValue();
                Object data = dataMap.get(objectProperty);
                if (null == data && StrUtil.isNotBlank(defaultValue)) {
                    data = defaultValue;
                }
                Integer type = column.getDataType();
                if (3 == type.intValue()) {
                    //文件类型的数据库存储
                    List<String> attaOidList = new ArrayList<>();
                    //图片类型的数据库存储
                    List<String> imagesOidList = new ArrayList<>();
                    //单文件
                    if (data instanceof JSONObject) {
                        if (data instanceof JSONObject) {
                            JSONObject jsonObject = (JSONObject) data;
                            String attaOid = jsonObject.getString("attaOid");
                            if (StrUtil.isNotBlank(attaOid)) {
                                attaOidList.add(attaOid);
                            }
                        }
                    }
                    //多文件
                    if (data instanceof JSONArray) {
                        JSONArray array = (JSONArray) data;
                        array.stream().forEach(item -> {
                            if (item instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) item;
                                String attaOid = jsonObject.getString("attaOid");
                                if (StrUtil.isNotBlank(attaOid)) {
                                    attaOidList.add(attaOid);
                                }

                            }
                            //多图片
                            if (item instanceof String) {
                                if (null != item && item.toString().contains("/form-api/form/manager/downloadFile/")) {
                                    String attaOid = ((String) item).replaceAll("/form-api/form/manager/downloadFile/", "");
                                    if (StrUtil.isNotBlank(attaOid)) {
                                        imagesOidList.add(attaOid);
                                    }

                                }
                            }
                        });
                    }
                    //单图片
                    if (data instanceof String) {
                        if (null != data && data.toString().contains("/form-api/form/manager/downloadFile/")) {
                            String attaOid = ((String) data).replaceAll("/form-api/form/manager/downloadFile/", "");
                            if (StrUtil.isNotBlank(attaOid)) {
                                imagesOidList.add(attaOid);
                            }

                        }
                    }
                    if (attaOidList.size() > 0) {
                        savaDataMap.put(columnName, String.join("~", attaOidList) + "~");
                    } else if (imagesOidList.size() > 0) {
                        savaDataMap.put(columnName, String.join("@", imagesOidList) + "@");
                    } else {
                        savaDataMap.put(columnName, data);
                    }
                    apiDataMap.put(objectProperty, data);
                }
                if (4 == type.intValue()) {
                    //boolean值
                    try {
                        data = Boolean.valueOf(data.toString());
                    } catch (Exception e) {

                    }
                    savaDataMap.put(columnName, data);
                    apiDataMap.put(objectProperty, data);
                } else {
                    if (data instanceof JSONArray || data instanceof JSONObject || data instanceof Integer) {
                        savaDataMap.put(columnName, data);
                        apiDataMap.put(objectProperty, data);
                    } else {
                       /* apiDataMap.put(objectProperty, data);
                        //解决Oracle时间的问题
                        if (null != formDataSource && StringUtils.equalsAnyIgnoreCase(formDataSource.getType(), "达梦", "oracle") && (null != column.getColumnType() && StringUtils.equalsAny(column.getColumnType().toUpperCase(), "DATE", "TIMESTAMP"))) {
                            if (null != data) {
                                String dataStr = data.toString();
                                if (dataStr.length() == 10) {
                                    data = "DATE:to_date('" + data + "','yyyy-mm-dd')";
                                } else if (dataStr.length() > 10) {
                                    data = "DATE:to_date('" + data + "','yyyy-mm-dd,hh24:mi:ss')";
                                }
                            }
                        }
                        savaDataMap.put(columnName, data);*/
                        apiDataMap.put(objectProperty, data);
                        //解决达蒙时间的问题
                        if (null != formDataSource && StringUtils.equalsAnyIgnoreCase(formDataSource.getType(), "达梦", "dm") && (null != column.getColumnType() && StringUtils.equalsAny(column.getColumnType().toUpperCase(), "DATE", "TIMESTAMP"))) {
                            if (null != data) {
                                String dataStr = data.toString();
                                if (dataStr.length() == 10) {
                                    data = "DATE:to_date('" + data + "','yyyy-mm-dd')";
                                } else if (dataStr.length() > 10) {
                                    data = "DATE:to_date('" + data + "','yyyy-mm-dd,hh24:mi:ss')";
                                }
                            }
                        }
                        //解决Oracle时间的问题
                        if (null != formDataSource && StringUtils.equalsAnyIgnoreCase(formDataSource.getType(), "oracle") && (null != column.getColumnType())) {
                            if (StringUtils.equalsAny(column.getColumnType().toUpperCase(), "DATE")) {
                                if (null != data) {
                                    String dataStr = data.toString();
                                    if (dataStr.length() == 10) {
                                        data = "DATE:to_date('" + data + "','yyyy-mm-dd')";
                                    } else if (dataStr.length() > 10) {
                                        data = "DATE:to_date('" + data + "','yyyy-mm-dd,hh24:mi:ss')";
                                    }
                                }
                            }
                            if (column.getColumnType().toUpperCase().contains("TIMESTAMP")) {
                                if (null != data) {
                                    String dataStr = data.toString();
                                    if (dataStr.length() == 10) {
                                        data = "DATE:to_timestamp('" + data + "','yyyy-mm-dd')";
                                    } else if (dataStr.length() > 10) {
                                        data = "DATE:to_timestamp('" + data + "','yyyy-mm-dd,hh24:mi:ss')";
                                    }
                                }
                            }

                        }
                        savaDataMap.put(columnName, data);
                    }
                }
            }
        }
        //判断默认值 以及数据库是否必填
//        if (!apiDataMap.isEmpty()) {
        for (FormColumn column : formColumnList) {
            //主键字段生成
            if ("id".equals(column.getObjectProperty())) {
                if (StrUtil.isBlank(dataId)) {
                    IdWorker idWorker = new IdWorker(0, 0);
                    long nextId = idWorker.nextId();
                    formDataTemp.setId(nextId);
                    savaDataMap.put(column.getColumnName(), nextId);
                    apiDataMap.put(column.getObjectProperty(), nextId);
                } else {
                    formDataTemp.setId(dataId);
                    savaDataMap.put(column.getColumnName(), dataId);
                    apiDataMap.put(column.getObjectProperty(), dataId);
                }
            }
            Object value = apiDataMap.get(column.getObjectProperty());
            //实体类属性名称
            String objectProperty = column.getObjectProperty();
            //数据库字段名称
            String columnName = column.getColumnName();
            if (null == value) {
                String defaultValue = column.getDefaultValue();
                if (StrUtil.isNotBlank(defaultValue)) {
                    savaDataMap.put(columnName, defaultValue);
                    apiDataMap.put(objectProperty, defaultValue);
                }
            }
//                Integer notNull = column.getNotNull();
//                if (null != notNull && 1 == notNull && null == value) {
//                    throw new ResultInfoException("数据库字段"+columnName+"不能为空！");
//                }
            //关联外键处理
            if (formObjectExtand != null && StrUtil.isNotBlank(formObjectExtand.getForeignKey())
                    //综窗的集成方法，由于不同系统两边字段没有关联，导致原先这边的column.getObjectProperty()（驼峰处理过）和他们的关联键（未驼峰处理），导致不一致，所以加了匹配ColumnName（未驼峰处理）
                    && (formObjectExtand.getForeignKey().equals(column.getObjectProperty()) || formObjectExtand.getForeignKey().equals(column.getColumnName()))) {
                savaDataMap.put(columnName, foreignKeyValue);
                apiDataMap.put(objectProperty, foreignKeyValue);
            }
        }
        //      }

        formDataTemp.setApiDataMap(apiDataMap);
        formDataTemp.setSavaDataMap(savaDataMap);
        return formDataTemp;
    }

    public FormDataTemp toObject(String objectOid, String extandOid, Map<String, Object> dataMap) {
        FormObject formObject = formObjectManager.getFormObjectByObjectOid(objectOid);
        FormColumn formColumn = new FormColumn();
        formColumn.setObjectOid(formObject.getObjectOid());
        List<FormColumn> formColumnList = null;
        if (StrUtil.isBlank(extandOid)) {
            formColumnList = formColumnManager.queryFormColumnList(formColumn);
        } else {
            FormObjectExtand extand = formObjectExtandManager.getFormObjectExtandByExtandOid(extandOid);
            if (StrUtil.isNotBlank(extand.getSecondaryObjectOid())) {
                formColumn.setObjectOid(extand.getSecondaryObjectOid());
                formColumnList = formColumnManager.queryFormColumnList(formColumn);
            } else {
                formColumn.setExtandOid(extandOid);
                formColumnList = formColumnManager.queryFormObjectExtandColumnList(formColumn);
            }

        }
        Set<String> keySet = dataMap.keySet();
        Map<String, Object> savaDataMap = new LinkedHashMap<>();
        Map<String, Object> apiDataMap = new LinkedHashMap<>();
        for (String key : keySet) {
            FormColumn column = formColumnList.stream().filter(innerFormColumn -> innerFormColumn.getObjectProperty().equals(key)).findFirst()
                    .orElse(null);
            if (null != column) {
                //实体类属性名称
                String objectProperty = column.getObjectProperty();
                //数据库字段名称
                String columnName = column.getColumnName();
                //默认值
                String defaultValue = column.getDefaultValue();
                Object data = dataMap.get(objectProperty);
                if (null == data && StrUtil.isNotBlank(defaultValue)) {
                    data = defaultValue;
                }
                Integer type = column.getDataType();

                if (data instanceof JSONArray || data instanceof JSONObject || data instanceof Integer) {
                    savaDataMap.put(columnName, data);
                    apiDataMap.put(objectProperty, data);
                } else {
                    apiDataMap.put(objectProperty, data);
                    savaDataMap.put(columnName, data);
                }
            }
        }

        FormDataTemp formDataTemp = new FormDataTemp();
        formDataTemp.setApiDataMap(apiDataMap);
        formDataTemp.setSavaDataMap(savaDataMap);
        return formDataTemp;
    }

    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public Map<String, Object> saveObject(@ValidGroups(groups = {FormDataVo.INSERT_GROUP.class}) FormDataVo formDataVo) {
        String reportId = null;
        Map<String, Object> resultMap = new HashMap<>();
        String designOid = formDataVo.getDesignOid();
        String formMainOid = formDataVo.getFormMainOid();
        FormDesign design = null;
        if (StrUtil.isBlank(designOid)) {
            design = formDesignManager.getFormDesignByFormMainOid(formMainOid);
        } else {
            design = formDesignManager.getFormDesignByDesignOid(designOid);
        }
        if (null == design) {
            throw new ResultInfoException("表单设计对象信息为空！");
        }

        FormMain formMain = formMainManager.getFormMainByFormMainOid(design.getFormMainOid());
        if (null == formMain) {
            throw new ResultInfoException("表单对象信息为空！");
        }
        //数据库存储类型  0本地  1API  2数据库
        //后续如果使用记录表的类型在放开
        Integer dataType = formMain.getSaveDataType();
        if (null == dataType) {
            dataType = FormStaticParameter.SAVE_DATA_TYPE_0;
        }
        FormObject formObject = formObjectManager.getFormObjectByObjectOid(formMain.getObjectOid());
        if (null == formObject) {
            throw new ResultInfoException("存储对象信息为空！");
        }


        String formData = formDataVo.getFormData();
        List<FormObjectExtand> formObjectExtands = formObjectExtandManager.queryFormObjectExtandList(formObject.getObjectOid(), null);
        FormColumn formColumn = new FormColumn();
        formColumn.setObjectOid(formObject.getObjectOid());
        Set<String> objectPropertys = new HashSet<>();
        formObjectExtands.stream().forEach(extand -> objectPropertys.add(extand.getVariableName()));
        //验证保存的数据是否符合验证规则 by wuxx 20210818
        /*String message = FormUtil.checkFormDataByFormConfig(design.getFormConfig(), formData, formObjectExtands, objectPropertys, formDataVo.getIsZc());
        if (StrUtil.isNotEmpty(message)) {
            throw new ResultInfoException(message);
        }*/

        // 修改值为null时被读取成"null"问题
        JSONObject object = JSONObject.parseObject(formData);
        //原生的json数据
        Map<String, Object> dataMap = JSONObject.toJavaObject(object, Map.class);
        //实际业务存在表单不填任何内容保存的情况。所以注释了这部分
//        Set<String> keySet = dataMap.keySet();
//        if (keySet.size() == 0) {
//            throw new ResultInfoException("填报数据不能为空！");
//        }
        FormDataTemp formDataTemp = null;
        if (FormStaticParameter.SAVE_DATA_TYPE_2.equals(dataType)) {
            if (formObject.getIsSave().equals(1)) {
                formDataTemp = this.toDataBase(formObject.getObjectOid(), formDataVo.getReportOid(), dataMap, null, null);
                reportId = String.valueOf(formDataTemp.getId());
                this.saveToDataBase(formObject, formDataTemp);
            } else {
                if (StrUtil.isNotBlank(formDataVo.getReportOid())) {
                    reportId = formDataVo.getReportOid();
                } else {
                    Object id = dataMap.get("id");
                    if (id == null) {
                        throw new ResultInfoException("填报对象【" + formObject.getObjectName() + "】主键不能为空！");
                    } else {
                        reportId = String.valueOf(id);
                    }

                }

            }

            for (FormObjectExtand objectExtand : formObjectExtands) {
                FormObject secondaryObject = formObjectManager.getFormObjectByObjectOid(objectExtand.getSecondaryObjectOid());
                Object dataValue = dataMap.get(objectExtand.getVariableName());
                if (dataValue != null) {
                    if (objectExtand.getType().equals(2)) {//对象
                        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(dataValue, SerializerFeature.WriteMapNullValue));
                        //原生的json数据
                        Map<String, Object> map = JSONObject.parseObject(JSONObject.toJSONString(dataValue, SerializerFeature.WriteMapNullValue), Map.class);
                        this.handleSecondaryObjectData(reportId, objectExtand, secondaryObject, jsonObject);
                        FormDataTemp dataTemp = this.toDataBase(objectExtand.getSecondaryObjectOid(), getDataId(map), map, objectExtand, reportId);
                        //第一次保存时存在子表单不填的情况，这时传过来的是一个空对象{}，不做保存处理
                        if (jsonObject.size() == 0 && !isUpdate(secondaryObject, dataTemp.getSavaDataMap())) {
                            continue;
                        }
                        this.saveToDataBase(secondaryObject, dataTemp);
                    } else {//数组

                        //原生的json数据
                        JSONObject arrayObjectmap = JSONObject.parseObject(JSONObject.toJSONString(dataValue, SerializerFeature.WriteMapNullValue));
                        JSONArray jsonArray = arrayObjectmap.getJSONArray(objectExtand.getVariableName());

                        //未填写任何内容，前端传的是"service_change":[{}]或者"service_change":[{},{},{}]
                        //实际等同 "service_change":[]，改成"service_change":[]处理
                        boolean tagNullData = true;
                        for (Object o : jsonArray) {
                            if (((JSONObject) o).size() > 0) {
                                tagNullData = false;
                            }
                        }
                        if (tagNullData) {
                            jsonArray = new com.alibaba.fastjson.JSONArray();
                        }

                        this.handleSecondaryObjectData(reportId, objectExtand, secondaryObject, jsonArray);
                        for (Object o : jsonArray) {
                            //原生的json数据
                            Map<String, Object> map = JSONObject.parseObject(JSONObject.toJSONString(o, SerializerFeature.WriteMapNullValue), Map.class);
                            FormDataTemp dataTemp = this.toDataBase(objectExtand.getSecondaryObjectOid(), getDataId(map), map, objectExtand, reportId);
                            this.saveToDataBase(secondaryObject, dataTemp);
                        }
                    }
                }
            }
        } else {
            formDataTemp = this.toObject(formObject.getObjectOid(), null, dataMap);
            // List<FormObjectExtand> formObjectExtands = formObjectExtandManager.queryFormObjectExtandList(formObject.getObjectOid(), null);
            for (FormObjectExtand objectExtand : formObjectExtands) {
                //FormObject extendObject = formObjectManager.getFormObjectByObjectOid(objectExtand.getSecondaryObjectOid());
                Object dataValue = dataMap.get(objectExtand.getVariableName());
                if (dataValue == null) {
                    continue;
                }
                if (objectExtand.getType().equals(2)) {

                    //原生的json数据
                    Map<String, Object> map = JSONObject.parseObject(JSONObject.toJSONString(dataValue, SerializerFeature.WriteMapNullValue), Map.class);
                    FormDataTemp dataTemp = this.toObject(formObject.getObjectOid(), objectExtand.getExtandOid(), map);
                    Map<String, Object> savaDataMap = formDataTemp.getSavaDataMap();
                    savaDataMap.put(objectExtand.getVariableName(), dataTemp.getSavaDataMap());
                    formDataTemp.setSavaDataMap(savaDataMap);
                    Map<String, Object> apiDataMap = formDataTemp.getApiDataMap();
                    apiDataMap.put(objectExtand.getVariableName(), dataTemp.getApiDataMap());
                    formDataTemp.setApiDataMap(apiDataMap);
                } else {

                    //原生的json数据
                    JSONObject arrayObjectmap = JSONObject.parseObject(JSONObject.toJSONString(dataValue, SerializerFeature.WriteMapNullValue));
                    JSONArray jsonArray = arrayObjectmap.getJSONArray(objectExtand.getVariableName());

                    List<Object> apiList = new ArrayList<>();
                    List<Object> saveList = new ArrayList<>();
                    for (Object o : jsonArray) {
                        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(o, SerializerFeature.WriteMapNullValue));
                        //原生的json数据
                        Map<String, Object> map = JSONObject.parseObject(jsonObject.toJSONString(), Map.class);
                        FormDataTemp dataTemp = this.toObject(formObject.getObjectOid(), objectExtand.getExtandOid(), map);
                        apiList.add(dataTemp.getApiDataMap());
                        saveList.add(dataTemp.getSavaDataMap());
                    }
                    Map<String, Object> savaDataMap = formDataTemp.getSavaDataMap();
                    savaDataMap.put(objectExtand.getVariableName(), saveList);
                    formDataTemp.setSavaDataMap(savaDataMap);
                    Map<String, Object> apiDataMap = formDataTemp.getApiDataMap();
                    if (objectExtand.getType().equals(1)) {
                        Map<String, Object> arrSaveValue = new HashMap<>();
                        arrSaveValue.put(objectExtand.getVariableName(), apiList);
                        apiDataMap.put(objectExtand.getVariableName(), arrSaveValue);
                    } else {
                        apiDataMap.put(objectExtand.getVariableName(), apiList);
                    }

                    formDataTemp.setApiDataMap(apiDataMap);
                }
            }
            if (FormStaticParameter.SAVE_DATA_TYPE_1.equals(dataType)) {
                reportId = this.saveToApi(formMain.getApiUrl(), formDataTemp.getApiDataMap());
            } else {
                reportId = this.saveToFormSystem(formMainOid, formMain.getFormName(), formMain.getVersion(), formDataVo, formDataTemp.getApiDataMap());
            }
        }
        dataMap.put("reportOid", reportId);
        //返回的数据库主键
        resultMap.put("reportOid", reportId);
        FormReportLog formReportLog = new FormReportLog();
        formReportLog.setFormMainOid(design.getFormMainOid());
        formReportLog.setReportOid(reportId);
        formReportLog.setDesignOid(formDataVo.getDesignOid());
        formReportLog.setAuthorizeKey(formDataVo.getAuthorizeKey());
        formReportLog.setFormData(formData);
        //formReportLog.setFormData(JSONUtil.toJsonStr(formDataTemp.getApiDataMap()));
        formReportLog.setFormName(formMain.getFormName());
        formReportLog.setVersion(formMain.getVersion());
        formReportLog.setSaveDataType(formMain.getSaveDataType());
        formReportLogManager.saveFormReportLog(formReportLog);
        //设计历史表主键
        resultMap.put("designOid", design.getDesignOid());
        //存储的数据
        resultMap.put("objectData", dataMap);
        return resultMap;
    }

    /**
     * 获取从表主键
     *
     * @param dataMap
     * @return
     */
    public String getDataId(Map<String, Object> dataMap) {
        String id = null;
        Object idObject = dataMap.get("id");
        if (idObject == null) {
            return id;
        }
        String dataId = String.valueOf(idObject);
        if (StrUtil.isNotBlank(dataId) && !"null".equals(dataId)) {
            id = dataId;
        }
        return id;
    }

    /**
     * 处理从表数据
     *
     * @param mainReportId
     * @param formObjectExtand
     * @param freeData
     */
    public void handleSecondaryObjectData(String mainReportId, FormObjectExtand formObjectExtand, FormObject formObject, com.alibaba.fastjson.JSONArray freeData) {
        //获取原数据
        String data = getSecondaryObjectData(formObjectExtand.getSecondaryObjectOid(), formObjectExtand.getForeignKey(), mainReportId, formObjectExtand.getType());
        //比对数据
        if (StrUtil.isNotBlank(data)) {
            JSONArray entiyData = JSONArray.parseArray(data);
            List<String> oids = new ArrayList<>();
            Set<String> freeIds = new HashSet<>();
            if (freeData != null && freeData.size() > 0) {
                for (Object free : freeData) {
                    Object id = JSONObject.parseObject(JSONObject.toJSONString(free, SerializerFeature.WriteMapNullValue)).get("id");
                    if (id != null) {
                        freeIds.add(String.valueOf(id));
                    }
                }
            }
            if (entiyData != null && entiyData.size() > 0) {
                for (Object entiy : entiyData) {
                    Object entiyid = JSONObject.parseObject(JSONObject.toJSONString(entiy, SerializerFeature.WriteMapNullValue)).get("id");
                    if (entiyid != null) {
                        String s = String.valueOf(entiyid);
                        if (!freeIds.contains(s)) {
                            oids.add(s);
                        }
                    }
                }
            }

            //删除数据
            if (oids.size() > 0) {
                for (String entiyId : oids) {
                    Map<String, Object> stringMap = new HashMap<>();
                    stringMap.put("ID", entiyId);
                    this.deleteData(formObject, stringMap);
                }
            }
        }


    }

    public void handleSecondaryObjectData(String mainReportId, FormObjectExtand formObjectExtand, FormObject formObject, JSONObject jsonObject) {
        //获取原数据
        String data = getSecondaryObjectData(formObjectExtand.getSecondaryObjectOid(), formObjectExtand.getForeignKey(), mainReportId, formObjectExtand.getType());
        Object objectId = JSONObject.parseObject(data).get("id");
        String id = "";
        if (objectId != null) {
            id = String.valueOf(objectId);
        }
        if (jsonObject == null) {
            Map<String, Object> stringMap = new HashMap<>();
            stringMap.put("ID", id);
            this.deleteData(formObject, stringMap);
        } else {
            Object oid = jsonObject.get("id");
            if (objectId != null) {
                String s = String.valueOf(objectId);
                if (!StringUtils.equals(String.valueOf(oid), s)) {
                    Map<String, Object> stringMap = new HashMap<>();
                    stringMap.put("ID", id);
                    this.deleteData(formObject, stringMap);
                }
            } else {
                Map<String, Object> stringMap = new HashMap<>();
                stringMap.put("ID", id);
                this.deleteData(formObject, stringMap);
            }
        }

        //比对数据
        //删除数据
    }

    public void deleteData(FormObject formObject, Map<String, Object> map) {
        if (isUpdate(formObject, map)) {
            String deleteSql = DbSqlUtil.getDeleteSql(formObject.getObjectForm(), map);
            String datasourceOid = formObject.getDatasourceOid();
            FormDataSource dataSource = formDataSourceManager.getFormDataSourceByDataSourceOid(datasourceOid);
            if (null == dataSource) {
                throw new ResultInfoException("数据源对象信息为空！");
            }
            DBHelper helper = new DBHelper(dataSource);
            helper.executeByDbSql(deleteSql);
        }

    }

    /**
     * @param designOid 设计主键
     * @param reportOid 表单数据的主键 （API方式可以为null）
     * @description: 根据设计主键和表单数据主键获取数据
     * @author: wuxx
     * @Date: 2021/4/23 9:43
     **/
    @Cacheable(key = "'getFormData:'+#designOid+#reportOid", unless = "#result == null")
    public FormDataVo getFormData(String authorizeKey, String designOid, String reportOid, boolean flag) {
        FormDataVo formDataVo = new FormDataVo();
        FormDesign design = formDesignManager.getFormDesignByDesignOid(designOid);
        if (null == design) {
            throw new ResultInfoException("表单设计对象信息为空！");
        }
        String formMainOid = design.getFormMainOid();
        FormMain formMain = formMainManager.getFormMainByFormMainOid(formMainOid);
        if (null == formMain) {
            throw new ResultInfoException("表单对象信息为空！");
        }
        if (!authorizeKey.equals(formMain.getAuthorizeKey())) {
            throw new ResultInfoException("参数authorizeKey不正确！");
        }
        formDataVo.setAuthorizeKey(formMain.getAuthorizeKey());
        formDataVo.setDesignOid(designOid);
        formDataVo.setReportOid(reportOid);
        formDataVo.setFormMainOid(design.getFormMainOid());
        String formConfig = design.getFormConfig();
        if (flag) {
            formDataVo.setFormConfig(formConfig);
        }
        //数据库存储类型  0表单系统  1API  2数据库
        //后续如果使用记录表的类型在放开
        //Integer dataType = design.getSaveDataType();
        Integer dataType = formMain.getSaveDataType();
        if (null == dataType) {
            dataType = FormStaticParameter.SAVE_DATA_TYPE_0;
        }
        formDataVo.setSaveDataType(FormStaticParameter.SAVE_DATA_TYPE_MAP.get(dataType));
        String objectOid = design.getObjectOid();
        FormObject formObject = formObjectManager.getFormObjectByObjectOid(objectOid);
        List<FormObjectExtand> formObjectExtands = formObjectExtandManager.queryFormObjectExtandList(objectOid, null);
        if (FormStaticParameter.SAVE_DATA_TYPE_0.equals(dataType)) {
            //表单本地数据库保存
            FormReport report = formReportManager.getFormReportByFormReportOid(reportOid);
            if (null != report) {
                String formData = report.getFormData();

                //数组存储进行数据转换
                JSONObject object = JSONObject.parseObject(formData);
                //原生的json数据
                Map<String, Object> dataMap = object.toJavaObject(Map.class);
                Map<String, Object> formDataMap = new HashMap<>();
                formDataMap.putAll(dataMap);
                if (formObjectExtands != null) {
                    for (FormObjectExtand formObjectExtand : formObjectExtands) {
                        if (formObjectExtand.getType().equals(1)) {
                            formDataMap.put(formObjectExtand.getVariableName(), dataMap.get(formObjectExtand.getVariableName()));
                        }
                    }
                }
                formDataVo.setFormData(JSONObject.toJSONString(formDataMap, SerializerFeature.WriteMapNullValue));
                //暂时去除
                /*if(!flag){
                    formDataVo.setFormApiData(FormUtil.getApiFormDataByConfig(formConfig,formData));
                }*/
            }
        } else if (FormStaticParameter.SAVE_DATA_TYPE_2.equals(dataType)) {
            if (null == formObject) {
                throw new ResultInfoException("存储对象信息为空！");
            }
            Map<String, Object> jsonMap = new HashMap<>();
            if (formObject.getIsSave() == null || formObject.getIsSave().equals(1)) {//存储对象默认要保存，所以将null等同1处理
                FormColumn formColumn = new FormColumn();
                formColumn.setObjectOid(design.getObjectOid());
                List<FormColumn> formColumnList = formColumnManager.queryFormColumnList(formColumn);
                Map<String, Object> selectDataMap = new LinkedHashMap<>();
                String columnkey = "";
                for (FormColumn column : formColumnList) {
                    //数据库字段名称
                    String columnName = column.getColumnName();
                    if ("ID".equals(columnName) || "id".equals(columnName) || "_id".equals(columnName)) {
                        selectDataMap.put(columnName, reportOid);
                        columnkey = columnName;
                    } else {
                        selectDataMap.put(columnName, null);
                    }
                }
                //远程数据库获取
                String selectSql = DbSqlUtil.getSelectSql(formObject.getObjectForm(), selectDataMap);
                //System.out.println(selectSql);
                String datasourceOid = formObject.getDatasourceOid();
                FormDataSource dataSource = formDataSourceManager.getFormDataSourceByDataSourceOid(datasourceOid);
                if (null == dataSource) {
                    throw new ResultInfoException("数据源对象信息为空！");
                }
                DBHelper helper = new DBHelper(dataSource);
                Map<String, Object> resultMap = helper.selectDbSql(selectSql, formObject.getObjectForm(), selectDataMap, formObject.getObjectForm());
                if (null != resultMap) {
                    //throw new ResultInfoException("数据查询信息失败！");
                    for (FormColumn column : formColumnList) {
                        //数据库字段名称
                        String columnName = column.getColumnName();

                        //实体类属性名称
                        String objectProperty = column.getObjectProperty();
                        Object data = resultMap.get(columnName);
                        if (null == data) {
                            continue;
                        }
                        int dataType1 = column.getDataType();
                        //对象
                        try {
                            if (2 == dataType1) {
                                JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(data, SerializerFeature.WriteMapNullValue));
                                data = jsonObject;
                            } else if (1 == dataType1) {
                                //数组

                                String str;
                                JSONArray jsonArray = JSONArray.parseArray((data.toString()));
                                data = jsonArray;
                            } else if (4 == dataType1) {
                                //boolean
                                try {
                                    data = Boolean.valueOf(data.toString());
                                } catch (Exception e) {

                                }
                            } else if (3 == dataType1) {
                                //文件类
                                String[] attaOidStrs = data.toString().contains("~") ? data.toString().split("~") : new String[0];
                                if (attaOidStrs.length > 1) {
                                    //多文件
                                    JSONArray jsonArray = new JSONArray();
                                    for (int i = 0; i < attaOidStrs.length; i++) {
                                        JSONObject jsonObject = new JSONObject();
                                        String attaOid = attaOidStrs[i];
                                        if (StrUtil.isEmpty(attaOid)) {
                                            continue;
                                        }
                                        jsonObject.put("attaOid", attaOid);
                                        jsonObject.put("url", "/form-api/form/manager/downloadFile/" + attaOid);
                                        jsonObject.put("uid", new Date().getTime());
                                        FormAtta atta = formAttaManager.getFormAttaByAttaOid(attaOid);
                                        if (null != atta) {
                                            jsonObject.put("name", atta.getOriginName());
                                            jsonObject.put("size", this.getLongSize(atta.getFileSize()));
                                        } else {
                                            jsonObject.put("name", "文件" + (i + 1));
                                            jsonObject.put("size", "");
                                        }
                                        jsonArray.add(jsonObject);
                                    }
                                    data = jsonArray;
                                } else if (attaOidStrs.length == 1) {
                                    JSONObject jsonObject = new JSONObject();
                                    String attaOid = attaOidStrs[0];
                                    jsonObject.put("attaOid", attaOid);
                                    jsonObject.put("url", "/form-api/form/manager/downloadFile/" + attaOid);
                                    jsonObject.put("uid", new Date().getTime());
                                    FormAtta atta = formAttaManager.getFormAttaByAttaOid(attaOid);
                                    if (null != atta) {
                                        jsonObject.put("name", atta.getOriginName());
                                        jsonObject.put("size", this.getLongSize(atta.getFileSize()));
                                    } else {
                                        jsonObject.put("name", "文件");
                                        jsonObject.put("size", "");
                                    }
                                    data = jsonObject;
                                } else {
                                    //图片类
                                    String[] imageOidStrs = data.toString().contains("@") ? data.toString().split("@") : new String[0];
                                    if (imageOidStrs.length > 1) {
                                        //多图片
                                        JSONArray jsonArray = new JSONArray();
                                        for (int i = 0; i < imageOidStrs.length; i++) {
                                            String attaOid = imageOidStrs[i];
                                            if (StrUtil.isEmpty(attaOid)) {
                                                continue;
                                            }
                                            jsonArray.add("/form-api/form/manager/downloadFile/" + attaOid);
                                        }
                                        data = jsonArray;
                                    } else if (imageOidStrs.length == 1) {
                                        String attaOid = imageOidStrs[0];
                                        data = "/form-api/form/manager/downloadFile/" + attaOid;
                                    }
                                }
                            } else if (data instanceof Float || data instanceof Double) {
                                data = String.valueOf(data);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        jsonMap.put(objectProperty, null != data ? data : null);
                    }
                }

            } else {
                jsonMap.put("id", reportOid);
            }
            if (formObjectExtands != null) {
                for (FormObjectExtand formObjectExtand : formObjectExtands) {
                    String data = getSecondaryObjectData(formObjectExtand.getSecondaryObjectOid(), formObjectExtand.getForeignKey(), reportOid, formObjectExtand.getType());
                    if (StrUtil.isNotBlank(data)) {
                        if (formObjectExtand.getType().equals(1)) {
                            Map listMap = new HashMap();
                            listMap.put(formObjectExtand.getVariableName(), JSONArray.parseArray(data));
                            jsonMap.put(formObjectExtand.getVariableName(), listMap);
                        } else {
                            jsonMap.put(formObjectExtand.getVariableName(), JSONObject.parseObject(data));
                        }
                    } else {
                        jsonMap.put(formObjectExtand.getVariableName(), null);
                    }

                }
            }
            formDataVo.setFormData(com.alibaba.fastjson.JSONObject.toJSONString(jsonMap));
        } else {
            throw new ResultInfoException("数据存放方式为API，无法获取数据！");
        }
        return formDataVo;
    }

    public String getSecondaryObjectData(String objectOid, String foreignKey, String id, Integer type) {
        FormObject formObject = formObjectManager.getFormObjectByObjectOid(objectOid);
        FormColumn formColumn = new FormColumn();
        formColumn.setObjectOid(objectOid);
        List<FormColumn> formColumnList = formColumnManager.queryFormColumnList(formColumn);
        Map<String, Object> selectDataMap = new LinkedHashMap<>();
        for (FormColumn column : formColumnList) {
            //数据库字段名称
            String columnName = column.getColumnName();
            if (foreignKey.equals(column.getObjectProperty()) || foreignKey.equals(columnName)) {
                selectDataMap.put(columnName, id);
            } else {
                selectDataMap.put(columnName, null);
            }
        }
        //远程数据库获取
        String selectSql = DbSqlUtil.getSelectSql(formObject.getObjectForm(), selectDataMap);
        String datasourceOid = formObject.getDatasourceOid();
        FormDataSource dataSource = formDataSourceManager.getFormDataSourceByDataSourceOid(datasourceOid);
        if (null == dataSource) {
            throw new ResultInfoException("数据源对象信息为空！");
        }
        DBHelper helper = new DBHelper(dataSource);
        if (type != null && type.equals(1)) {
            List<Map<String, Object>> list = helper.selectListDbSql(selectSql, formObject.getObjectForm(), selectDataMap, formObject.getObjectForm());
            List dataList = new ArrayList();
            if (list != null) {
                for (Map<String, Object> resultMap : list) {
                    Map<String, Object> jsonMap = new HashMap<>();
                    for (FormColumn column : formColumnList) {
                        //数据库字段名称
                        String columnName = column.getColumnName();
                        //实体类属性名称
                        String objectProperty = column.getObjectProperty();
                        Object data = resultMap.get(columnName);
                        if (null == data) {
                            continue;
                        }
                        int dataType1 = column.getDataType();
                        //对象
                        try {
                            if (2 == dataType1) {
                                String json;
                                JSONObject jsonObject = JSONObject.parseObject(data.toString());
                                data = jsonObject;
                            } else if (4 == dataType1) {
                                //boolean值
                                try {
                                    data = Boolean.valueOf(data.toString());
                                } catch (Exception e) {

                                }
                            } else if (1 == dataType1) {
                                //数组
                                JSONUtil.parseArray(data.toString());
                                JSONArray jsonArray = JSONArray.parseArray(data.toString());
                                data = jsonArray;
                            } else if (3 == dataType1) {
                                //文件类
                                String[] attaOidStrs = data.toString().contains("~") ? data.toString().split("~") : new String[0];
                                if (attaOidStrs.length > 1) {
                                    //多文件
                                    JSONArray jsonArray = new JSONArray();
                                    for (int i = 0; i < attaOidStrs.length; i++) {
                                        JSONObject jsonObject = new JSONObject();
                                        String attaOid = attaOidStrs[i];
                                        if (StrUtil.isEmpty(attaOid)) {
                                            continue;
                                        }
                                        jsonObject.put("attaOid", attaOid);
                                        jsonObject.put("url", "/form-api/form/manager/downloadFile/" + attaOid);
                                        jsonObject.put("uid", new Date().getTime());
                                        FormAtta atta = formAttaManager.getFormAttaByAttaOid(attaOid);
                                        if (null != atta) {
                                            jsonObject.put("name", atta.getOriginName());
                                            jsonObject.put("size", this.getLongSize(atta.getFileSize()));
                                        } else {
                                            jsonObject.put("name", "文件" + (i + 1));
                                            jsonObject.put("size", "");
                                        }
                                        jsonArray.add(jsonObject);
                                    }
                                    data = jsonArray;
                                } else if (attaOidStrs.length == 1) {
                                    JSONObject jsonObject = new JSONObject();
                                    String attaOid = attaOidStrs[0];
                                    jsonObject.put("attaOid", attaOid);
                                    jsonObject.put("url", "/form-api/form/manager/downloadFile/" + attaOid);
                                    jsonObject.put("uid", new Date().getTime());
                                    FormAtta atta = formAttaManager.getFormAttaByAttaOid(attaOid);
                                    if (null != atta) {
                                        jsonObject.put("name", atta.getOriginName());
                                        jsonObject.put("size", this.getLongSize(atta.getFileSize()));
                                    } else {
                                        jsonObject.put("name", "文件");
                                        jsonObject.put("size", "");
                                    }
                                    data = jsonObject;
                                } else {
                                    //图片类
                                    String[] imageOidStrs = data.toString().contains("@") ? data.toString().split("@") : new String[0];
                                    if (imageOidStrs.length > 1) {
                                        //多图片
                                        JSONArray jsonArray = new JSONArray();
                                        for (int i = 0; i < imageOidStrs.length; i++) {
                                            String attaOid = imageOidStrs[i];
                                            if (StrUtil.isEmpty(attaOid)) {
                                                continue;
                                            }
                                            jsonArray.add("/form-api/form/manager/downloadFile/" + attaOid);
                                        }
                                        data = jsonArray;
                                    } else if (imageOidStrs.length == 1) {
                                        String attaOid = imageOidStrs[0];
                                        data = "/form-api/form/manager/downloadFile/" + attaOid;
                                    }
                                }
                            } else if (data instanceof Float || data instanceof Double) {
                                data = String.valueOf(data);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        jsonMap.put(objectProperty, null != data ? data : null);
                    }
                    dataList.add(jsonMap);
                }

            }
            return JSONObject.toJSONString(dataList, SerializerFeature.WriteMapNullValue);
        } else {
            Map<String, Object> resultMap = helper.selectDbSql(selectSql, formObject.getObjectForm(), selectDataMap, formObject.getObjectForm());
            Map<String, Object> jsonMap = new HashMap<>();
            if (null != resultMap) {
                //throw new ResultInfoException("数据查询信息失败！");
                for (FormColumn column : formColumnList) {
                    //数据库字段名称
                    String columnName = column.getColumnName();
                    //实体类属性名称
                    String objectProperty = column.getObjectProperty();
                    Object data = resultMap.get(columnName);
                    if (null == data) {
                        continue;
                    }
                    Integer dataType1 = column.getDataType();
                    //对象
                    try {
                        if (2 == dataType1) {
                            String json;
                            JSONObject jsonObject = JSONObject.parseObject(data.toString());
                            data = jsonObject;
                        } else if (1 == dataType1) {
                            //数组
                            JSONArray jsonArray = JSONArray.parseArray(data.toString());
                            data = jsonArray;
                        } else if (4 == dataType1) {
                            //boolean值
                            try {
                                data = Boolean.valueOf(data.toString());
                            } catch (Exception e) {

                            }
                        } else if (3 == dataType1) {
                            //文件类
                            String[] attaOidStrs = data.toString().contains("~") ? data.toString().split("~") : new String[0];
                            if (attaOidStrs.length > 1) {
                                //多文件
                                JSONArray jsonArray = new JSONArray();
                                for (int i = 0; i < attaOidStrs.length; i++) {
                                    JSONObject jsonObject = new JSONObject();
                                    String attaOid = attaOidStrs[i];
                                    if (StrUtil.isEmpty(attaOid)) {
                                        continue;
                                    }
                                    jsonObject.put("attaOid", attaOid);
                                    jsonObject.put("url", "/form-api/form/manager/downloadFile/" + attaOid);
                                    jsonObject.put("uid", new Date().getTime());
                                    FormAtta atta = formAttaManager.getFormAttaByAttaOid(attaOid);
                                    if (null != atta) {
                                        jsonObject.put("name", atta.getOriginName());
                                        jsonObject.put("size", this.getLongSize(atta.getFileSize()));
                                    } else {
                                        jsonObject.put("name", "文件" + (i + 1));
                                        jsonObject.put("size", "");
                                    }
                                    jsonArray.add(jsonObject);
                                }
                                data = jsonArray;
                            } else if (attaOidStrs.length == 1) {
                                JSONObject jsonObject = new JSONObject();
                                String attaOid = attaOidStrs[0];
                                jsonObject.put("attaOid", attaOid);
                                jsonObject.put("url", "/form-api/form/manager/downloadFile/" + attaOid);
                                jsonObject.put("uid", new Date().getTime());
                                FormAtta atta = formAttaManager.getFormAttaByAttaOid(attaOid);
                                if (null != atta) {
                                    jsonObject.put("name", atta.getOriginName());
                                    jsonObject.put("size", this.getLongSize(atta.getFileSize()));
                                } else {
                                    jsonObject.put("name", "文件");
                                    jsonObject.put("size", "");
                                }
                                data = jsonObject;
                            } else {
                                //图片类
                                String[] imageOidStrs = data.toString().contains("@") ? data.toString().split("@") : new String[0];
                                if (imageOidStrs.length > 1) {
                                    //多图片
                                    JSONArray jsonArray = new JSONArray();
                                    for (int i = 0; i < imageOidStrs.length; i++) {
                                        String attaOid = imageOidStrs[i];
                                        if (StrUtil.isEmpty(attaOid)) {
                                            continue;
                                        }
                                        jsonArray.add("/form-api/form/manager/downloadFile/" + attaOid);
                                    }
                                    data = jsonArray;
                                } else if (imageOidStrs.length == 1) {
                                    String attaOid = imageOidStrs[0];
                                    data = "/form-api/form/manager/downloadFile/" + attaOid;
                                }
                            }
                        } else if (data instanceof Float || data instanceof Double) {
                            data = String.valueOf(data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    jsonMap.put(objectProperty, null != data ? data : null);
                }
            }

            return com.alibaba.fastjson.JSONObject.toJSONString(jsonMap);
        }

    }

    /**
     * @param sizeStr
     * @description: 文件的大小字符转long字节
     * @author: wuxx
     * @Date: 2021/6/16 16:48
     **/
    public long getLongSize(String sizeStr) {
        //获取到的size为：1705230
        int GB = 1024 * 1024 * 1024;//定义GB的计算常量
        int MB = 1024 * 1024;//定义MB的计算常量
        int KB = 1024;//定义KB的计算常量
        float floatSize = 0;
        if (sizeStr.endsWith("GB")) {
            float gb = Float.parseFloat(sizeStr.replaceAll("GB", ""));
            floatSize = gb * GB;
        } else if (sizeStr.endsWith("MB")) {
            float mb = Float.parseFloat(sizeStr.replaceAll("MB", ""));
            floatSize = mb * MB;
        } else if (sizeStr.endsWith("KB")) {
            float kb = Float.parseFloat(sizeStr.replaceAll("KB", ""));
            floatSize = kb * KB;
        }
        String floatSizeStr = String.valueOf(floatSize);
        int i = floatSizeStr.indexOf(".");//首先获取字符的位置
        String newStr = floatSizeStr.substring(0, i);//再对字符串进行截取，获得想要得到的字符串
        return Long.parseLong(newStr);
    }

    /**
     * @param reportOid 设计数据主键
     * @description: 根据设计主键和表单数据主键获取数据
     * @author: wuxx
     * @Date: 2021/5/17 9:43
     **/
    @Cacheable(key = "'getFormDataByReportOid:'+#reportOid", unless = "#result == null")
    public FormDataVo getFormDataByReportOid(String authorizeKey, String reportOid) {
        FormDataVo formDataVo = new FormDataVo();
        //表单本地数据库
        FormReport report = formReportManager.getFormReportByFormReportOid(reportOid);
        if (null != report) {
            if (!authorizeKey.equals(report.getAuthorizeKey())) {
                throw new ResultInfoException("参数authorizeKey不正确！");
            }
            FormDesign design = formDesignManager.getFormDesignByDesignOid(report.getDesignOid());
            if (null == design) {
                throw new ResultInfoException("表单设计对象信息为空！");
            }
            String formMainOid = design.getFormMainOid();
            FormMain formMain = formMainManager.getFormMainByFormMainOid(formMainOid);
            if (null == formMain) {
                throw new ResultInfoException("表单对象信息为空！");
            }
            formDataVo.setDesignOid(report.getDesignOid());
            formDataVo.setReportOid(reportOid);
            formDataVo.setFormMainOid(design.getFormMainOid());
            String formConfig = design.getFormConfig();
            //formDataVo.setFormConfig(formConfig);
            String formData = report.getFormData();
            formDataVo.setCreateDate(report.getCreateDate());
            formDataVo.setFormData(formData);
            //暂时去除
            //formDataVo.setFormApiData(FormUtil.getApiFormDataByConfig(formConfig,formData));
        }
        return formDataVo;
    }

    /**
     * @param formCode 表单编码
     * @description: 根据表单编码获取填报数据的集合
     * @author: wuxx
     * @Date: 2021/5/17 9:43
     **/
    public List<FormDataVo> getFormReportByFormCode(String authorizeKey, String formCode) {
        FormMain mainCode = formMainManager.getFormMainByFormMainCode(formCode);
        if (null == mainCode) {
            throw new ResultInfoException("表单对象信息为空！");
        }
        if (!mainCode.getAuthorizeKey().equals(authorizeKey)) {
            throw new ResultInfoException("参数authorizeKey或参数formCode错误！");
        }
        FormReport report = new FormReport();
        report.setAuthorizeKey(authorizeKey);
        report.setFormMainOid(mainCode.getFormMainOid());
        List<FormReport> formReportList = formReportManager.queryFormReportList(report);
        List<FormDataVo> formDataVoList = new ArrayList<>();
        for (FormReport formReport : formReportList) {
            FormDataVo formDataVo = new FormDataVo();
            formDataVo.setReportOid(formReport.getReportOid());
            formDataVo.setFormMainOid(formReport.getFormMainOid());
            formDataVo.setDesignOid(formReport.getDesignOid());
            formDataVo.setFormData(formReport.getFormData());
            formDataVo.setCreateDate(formReport.getCreateDate());
            //暂时去除
            /*FormDesign design = formDesignManager.getFormDesignByDesignOid(formReport.getDesignOid());
            if(null!=design){
                formDataVo.setFormApiData(FormUtil.getApiFormDataByConfig(design.getFormConfig(),formReport.getFormData()));
            }*/
            formDataVoList.add(formDataVo);
        }
        return formDataVoList;
    }

    /**
     * 通过表单编码获取绑定数据项列表
     *
     * @param authorizeKey 授权key
     * @param formMainOid  表单表嘛
     * @return 绑定数据项列表
     */
    public List<FormColumn> queryFormColumnListByFormMainCode(String authorizeKey, String formMainOid) {
        FormMain formMain = this.formMainManager.getFormMainByFormMainOid(formMainOid);
        checkAuthorizeKey(authorizeKey, formMain.getModuleOid());
        FormObject object = formObjectManager.getFormObjectByObjectOid(formMain.getObjectOid());
        FormColumn formColumn = new FormColumn();
        formColumn.setObjectOid(formMain.getObjectOid());
        formColumn.setDatasourceOid(object.getDatasourceOid());
        return formColumnManager.queryFormColumnList(formColumn);
    }

    public List<FormObjectExtand> queryFormObjectExtandListByFormMainCode(String authorizeKey, String formMainOid, Integer type) {
        FormMain formMain = this.formMainManager.getFormMainByFormMainOid(formMainOid);
        checkAuthorizeKey(authorizeKey, formMain.getModuleOid());
        FormObject object = formObjectManager.getFormObjectByObjectOid(formMain.getObjectOid());
//        FormColumn formColumn = new FormColumn();
//        formColumn.setObjectOid(formMain.getObjectOid());
//        formColumn.setDatasourceOid(object.getDatasourceOid());
        return formObjectExtandManager.queryFormObjectExtandList(object.getObjectOid(), type);
    }

    /**
     * 根据授权码和模块业务id鉴权
     *
     * @param authorizeKey
     * @param moduleOid
     */
    public void checkAuthorizeKey(String authorizeKey, String moduleOid) {
        FormModule formModule = this.formModuleManager.getFormModuleByModuleOid(moduleOid);
        Assert.notNull(formModule, "模块信息不存在，请联系管理员！");
        Assert.isTrue(StrUtil.equals(formModule.getAuthorizeKey(), authorizeKey), "授权校验失败，请联系管理员！");
    }

    /**
     * @param designAndReportOid      设计主键-填报主键
     * @param linkDesignAndReportOids 关联设计主键-填报主键，多个用逗号隔开
     * @param docxTemplateOid         模板主键
     * @param isPdf                   是否导出pdf
     * @param initDataMap             模板设置默认值，优先设置
     * @param isSaveFileServer        是否存储到文件服务器中
     * @description: 填报数据导出docx或者pdf
     * @author: wuxx
     * @Date: 2021/12/3 13:42
     *
     * @return*/
    public cn.hutool.json.JSONObject exportFormDataToDocx(String designAndReportOid, String linkDesignAndReportOids,
                                                          String docxTemplateOid, Boolean isPdf, HashMap<String, Object> initDataMap,
                                                          boolean isSaveFileServer) {
        if (!designAndReportOid.contains("-")) {
            throw new ResultInfoException("designAndreportOid数据格式不符合规范！");
        }
        String[] splitDesignAndReportOid = designAndReportOid.split("-");
        String designOid = splitDesignAndReportOid[0];
        String reportOid = splitDesignAndReportOid[1];
        FormDesign design = formDesignManager.getFormDesignByDesignOid(designOid);
        if (null == design) {
            throw new ResultInfoException("未查询" + designOid + "对应的设计对象信息！");
        }
        FormMain main = formMainManager.getFormMainByFormMainOid(design.getFormMainOid());
        //主填报
        FormDataVo formDataVo = this.getFormData(main.getAuthorizeKey(), designOid, reportOid, true);
        //替换参数
        HashMap<String, Object> mappings = new HashMap<>();
        //列表标识
        List<String> flagList = new ArrayList<>();
//        flagList.add("gudongs");
        // 图片流
        Map<String, Map<String, Object>> imagesMap = new HashMap<>();
        //多图片流
        Map<String, Map<String, Object>> pictureRenderDataMap = new HashMap<>();
        Configure config = PoiTlUtil.initConfigure();
        //填报数据处理
        this.formDataVoToDocxMap(formDataVo, mappings, flagList, imagesMap, pictureRenderDataMap);

        //获取模板
        DocxTemplate template = docxTemplateManager.getDocxTemplateByTemplateOid(docxTemplateOid);
        if (null == template) {
            throw new ResultInfoException("未查询到对应的模板信息！");
        }
        //处理模板中的集合标签
        InputStream dealFlagStreamMeta = this.getInputStreamByAtta(template.getDocxAttaOid());
        //处理设置值的列表字段显示的标签问题
        PoiTlUtil.dealFlagListMetas(dealFlagStreamMeta, flagList);

        //获取模板中的标签
        InputStream inputStreamMeta = this.getInputStreamByAtta(template.getDocxAttaOid());
        //转换后只有一个mappings
        PoiTlUtil.flagListImageToMappings(config, inputStreamMeta, mappings, flagList, imagesMap, pictureRenderDataMap);

        //关联填报
        if (StrUtil.isNotEmpty(linkDesignAndReportOids)) {
            String[] designAndReportOids = linkDesignAndReportOids.split(",");
            for (String designReportOid : designAndReportOids) {
                if (!designReportOid.contains("-")) {
                    throw new ResultInfoException("linkDesignAndReportOids数据格式不符合规范！");
                }
                String[] splitDesignReportOid = designReportOid.split("-");
                String linkDesignOid = splitDesignReportOid[0];
                String linkReportOid = splitDesignReportOid[1];
                FormDesign linkDesign = formDesignManager.getFormDesignByDesignOid(linkDesignOid);
                if (null == linkDesign) {
                    throw new ResultInfoException("未查询" + linkDesignOid + "对应的设计对象信息！");
                }
                FormMain linkMain = formMainManager.getFormMainByFormMainOid(linkDesign.getFormMainOid());
                String linkAuthorizeKey = linkMain.getAuthorizeKey();

                String appendObjectOids = template.getAppendObjectOids();
                if (StrUtil.isNotEmpty(appendObjectOids)) {
                    String objectOid = linkDesign.getObjectOid();
                    FormDataVo appendFormDataVo = this.getFormData(linkAuthorizeKey, linkDesignOid, linkReportOid, true);

                    String[] appendObjectOidList = appendObjectOids.split(";");
                    for (String objects : appendObjectOidList) {
                        String[] keyObject = objects.split("~");
                        if (objectOid.equals(keyObject[1])) {
                            String prefix = keyObject[0];
                            //替换参数
                            HashMap<String, Object> appendMappings = new HashMap<>();
                            //列表标识
                            List<String> appendFlagList = new ArrayList<>();
                            // 图片流
                            Map<String, Map<String, Object>> appendImagesMap = new HashMap<>();
                            // 多图片流
                            Map<String, Map<String, Object>> appendpictureRenderDataMap = new HashMap<>();

                            //填报关联的数据处理
                            this.formDataVoToDocxMap(appendFormDataVo, appendMappings, appendFlagList, appendImagesMap, appendpictureRenderDataMap);
                            //获取模板中的标签
                            InputStream appendInputStreamMeta = this.getInputStreamByAtta(template.getDocxAttaOid());
                            //转换后只有一个mappings
                            PoiTlUtil.flagListImageToMappings(config, appendInputStreamMeta, appendMappings, appendFlagList, appendImagesMap, appendpictureRenderDataMap);
                            mappings.put(prefix, appendMappings);

                        }
                    }
                }
            }
        }
        //System.err.println(JSONUtil.toJsonStr(mappings));
        //设置默认值
        if (null != initDataMap) {
            mappings.putAll(initDataMap);
        }

        InputStream inputStreamByAtta = this.getInputStreamByAtta(template.getDocxAttaOid());
        cn.hutool.json.JSONObject jsonObject = null;
        if (isSaveFileServer){
            String path = PoiTlUtil.exportDocxInLocalStage(config,  inputStreamByAtta, mappings,isPdf);
            try {
                File file = new File(path);
                FileInputStream input = new FileInputStream(file);
                MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "text/plain", input);
                jsonObject = formAttaManager.uploadImage(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(),
                        multipartFile, null, null);
                jsonObject.putOnce("docxTemplate",template);
                //删除temp文件
                FIXED_THREAD_POOL.execute(new DelTempFileThred(path));
            } catch (Exception e) {
                throw new ResultInfoException("文件转换失败："+e.getMessage());
            }
        }else {
//            TextRenderData selSymbol = new TextRenderData("R",new Style("Wingdings 2",10.5));
//            TextRenderData unselSymbol = new TextRenderData("\u00A3",new Style("Wingdings 2",10.5));
//            mappings.put("newselect_1",unselSymbol);
//            mappings.put("newselect_2",selSymbol);
//            mappings.put("newselect_3","☑");
            PoiTlUtil.exportDocxAndPDF(config, template.getDocxTemplateName(), inputStreamByAtta, mappings, isPdf);
        }
        return jsonObject;
    }


    /**
     * @param formDataVo 填报数据对象
     * @param mappings   替换参数
     * @param flagList   列表标识
     * @param imagesMap  图片流
     * @description: formDataVo 转docxMap
     * @author: wuxx
     * @Date: 2021/12/6 9:40
     **/
    private void formDataVoToDocxMap(FormDataVo formDataVo, HashMap<String, Object> mappings,
                                     List<String> flagList, Map<String, Map<String, Object>> imagesMap,
                                     Map<String, Map<String, Object>> pictureRenderDataMap) {
        String formConfig = formDataVo.getFormConfig();
        String formData = formDataVo.getFormData();
        formConfig = FormUtil.changeToRightJsonStr(formConfig);
        cn.hutool.json.JSONObject formConfigObject = JSONUtil.parseObj(formConfig);
        cn.hutool.json.JSONObject formDataObject = JSONUtil.parseObj(formData);
        if (null != formDataObject) {
            cn.hutool.json.JSONObject formDescObject = formConfigObject.getJSONObject("formDesc");
            for (String key : formDataObject.keySet()) {
                if (!formDescObject.containsKey(key)){
                    //某字段的值 在 表单设计 中 没有 对应组件绑定它，这时当做普通组件的值 ， 放进去
                    Object data = formDataObject.get(key);
                    mappings.put(key, data);
                    continue;
                }
                cn.hutool.json.JSONObject jsonObject = FormUtil.getFormDescConfigByCode(formDescObject, key);
                Object formDataObj = formDataObject.get(key);
                //数值型
                if (null != formDataObj) {
                    mappings.put(key, formDataObject.getStr(key));
                }

                this.formDataObjToPoiMap(mappings,jsonObject,key,formDataObject,flagList,pictureRenderDataMap,null);

                //对象和数组型
                if (null != formDataObj && formDataObj instanceof cn.hutool.json.JSONObject) {
                    cn.hutool.json.JSONObject innerFormDataObj = (cn.hutool.json.JSONObject) formDataObj;
                    //只针对是对象
                    if(null == innerFormDataObj.getJSONArray(key)){
                        Map<String, Object> dataMap = this.childFormDataToMap(jsonObject, innerFormDataObj, pictureRenderDataMap, flagList);
                        mappings.put(key, dataMap);
                    }else{
                        //数组类型
                        List<Map<String, Object>> dataListMap = this.arrayFormDataToMap(key,jsonObject, innerFormDataObj, pictureRenderDataMap, flagList);
                        mappings.put(key, dataListMap);
                    }
                }
            }
        }
    }

    /**
     * @description:  列表formData转成mappings数据
     * @author: wuxx
     * @Date: 2021/12/14 16:01
     **/
    private List<Map<String, Object>> arrayFormDataToMap(String key,cn.hutool.json.JSONObject jsonObject,cn.hutool.json.JSONObject innerFormDataObj,
                                                   Map<String, Map<String, Object>> pictureRenderDataMap,List<String> flagList){
        List<Map<String, Object>> mapList = new ArrayList<>();
        if(null!=innerFormDataObj){
            cn.hutool.json.JSONArray innerJsonDataArray = innerFormDataObj.getJSONArray(key);
            cn.hutool.json.JSONObject innerFormItemListObject = jsonObject.getJSONObject("formItemList");
            Boolean isArray = jsonObject.getBool("isArray");//子对象设置为数组
            if (isArray != null && isArray && innerFormItemListObject != null){
                innerFormItemListObject = innerFormItemListObject.getJSONObject(key).getJSONObject("formItemList");
            }
            if(null == innerJsonDataArray || null == innerFormItemListObject){
                return null;
            }
            Iterator<Object> iterator = innerJsonDataArray.iterator();
            while (iterator.hasNext()) {
                cn.hutool.json.JSONObject optObject = (cn.hutool.json.JSONObject) iterator.next();
                Map<String, Object> dataMap = new HashMap();
                for(String dataKey : optObject.keySet()){
                    Object dataKeyata = optObject.get(dataKey);
                    cn.hutool.json.JSONObject dataKeyJsonData = new cn.hutool.json.JSONObject().putOpt(dataKey,dataKeyata);
                    cn.hutool.json.JSONObject dataKeyJsonObject = innerFormItemListObject.getJSONObject(dataKey);
                    if(null == dataKeyJsonObject){
                        continue;
                    }
                    Object formDataObj = dataKeyJsonData.get(dataKey);
                    //数值型
                    if (null != formDataObj) {
                        dataMap.put(dataKey,dataKeyJsonData.get(dataKey));
                    }
                    this.formDataObjToPoiMap(dataMap, dataKeyJsonObject, dataKey, dataKeyJsonData, flagList, pictureRenderDataMap,true);

                }
                mapList.add(dataMap);
            }
        }
        return mapList;
    }

    /**
     * @description:  子表单formData转成mappings数据
     * @author: wuxx
     * @Date: 2021/12/10 16:01
     **/
    private Map<String, Object> childFormDataToMap(cn.hutool.json.JSONObject jsonObject,cn.hutool.json.JSONObject innerFormDataObj,
                                                   Map<String, Map<String, Object>> pictureRenderDataMap,List<String> flagList){
        Map<String, Object> dataMap = new HashMap();
        if(null!=innerFormDataObj){
            for(String objKey : innerFormDataObj.keySet()){
                cn.hutool.json.JSONObject innerFormItemListObject = jsonObject.getJSONObject("formItemList");
                cn.hutool.json.JSONObject innerJsonObject = FormUtil.getObjByCode(innerFormItemListObject, objKey);
                //System.err.println(objKey+"=="+(null!=objByCode ? objByCode.toString():"111111"));
                //cn.hutool.json.JSONObject innerJsonObject = innerFormItemListObject.getJSONObject(objKey);
                if(null == innerJsonObject){
                    continue;
                }
                Object formDataObj = innerFormDataObj.get(objKey);
                //数值型
                if (null != formDataObj && formDataObj instanceof String) {
                    dataMap.put(objKey,innerFormDataObj.get(objKey));
                }

                this.formDataObjToPoiMap(dataMap,innerJsonObject,objKey,innerFormDataObj,flagList,pictureRenderDataMap,true);
            }
        }
        return dataMap;
    }

    /**
     * @param mappings   替换参数
     * @param flagList   列表标识
     * @param pictureRenderDataMap  图片流
     * @description: formData转poiMap
     * @author: wuxx
     * @Date: 2021/12/14 12:40
     **/
    private Map<String, Object> formDataObjToPoiMap(Map<String, Object> mappings, cn.hutool.json.JSONObject jsonObject, String key,
                                                        cn.hutool.json.JSONObject formDataObject, List<String> flagList,
                                                        Map<String, Map<String, Object>> pictureRenderDataMap,Boolean isChild) {
        String type = jsonObject.getStr("type");
        //图片类
        if ("image-uploader".equals(type) || "ocr-image".equals(type)) {
            cn.hutool.json.JSONObject attrsObject = jsonObject.getJSONObject("attrs");
            if (null != attrsObject) {
                Object actionObj = formDataObject.get(key);
                List<InputStream> inputStreamList = new ArrayList<>();
                if (actionObj instanceof String) {
                    String attaOid = actionObj.toString().replace("/form-api/form/manager/downloadFile/", "");
                    String size = attrsObject.getStr("size");
                    Map<String, Object> file = new HashMap<>();
                    inputStreamList.add(getInputStreamByAtta(attaOid));
                    file.put("fileInputStream", inputStreamList);
                    file.put("height", Integer.valueOf(size));
                    file.put("width", Integer.valueOf(size));
                    //imagesMap.put(key,file);
                    pictureRenderDataMap.put(key, file);

                    //针对子表单、子表格
                    if(null!=isChild && isChild){
                        //文件流
                        InputStream inputStream = getInputStreamByAtta(attaOid);
                        String ofUrl = attaOid;
                        if (null == inputStream && null == ofUrl) {
                            return mappings;
                        }
                        Integer height = Integer.valueOf(size);
                        Integer width = Integer.valueOf(size);

                        List<PictureRenderData> pictureRenderDataList = new ArrayList<>();
                        if (null != inputStream) {
                            PictureRenderData pictureRenderData =Pictures.ofStream(inputStream, PictureType.JPEG)
                                    .size(width, height).create();
                            pictureRenderDataList.add(pictureRenderData);
                        }else if (null == inputStream && null != ofUrl) {
                            PictureRenderData pictureRenderData = Pictures.ofUrl(ofUrl).size(height, width).create();
                            pictureRenderDataList.add(pictureRenderData);
                        }
                        mappings.put(key,pictureRenderDataList);
                    }
                    return mappings;
                }
                if (actionObj instanceof cn.hutool.json.JSONArray) {
                    String size = "200";
                    try {
                        size = attrsObject.getStr("size");
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                    cn.hutool.json.JSONArray defaultArr = (cn.hutool.json.JSONArray) actionObj;
                    List<String> list = defaultArr.toList(String.class);
                    for (String attaUrl : list) {
                        String attaOid = attaUrl.replace("/form-api/form/manager/downloadFile/", "");
                        inputStreamList.add(getInputStreamByAtta(attaOid));
                    }
                    Map<String, Object> file = new HashMap<>();
                    file.put("fileInputStream", inputStreamList);
                    file.put("height", Integer.valueOf(size));
                    file.put("width", Integer.valueOf(size));
                    pictureRenderDataMap.put(key, file);

                    //针对子表单、子表格
                    if(null!=isChild && isChild){
                        List<PictureRenderData> pictureRenderDataList = new ArrayList<>();
                        if (null != inputStreamList) {

                            List<InputStream> fileInputStreamList = (List<InputStream>) inputStreamList;
                            for(InputStream fileInputStream : fileInputStreamList){
                                PictureRenderData pictureRenderData =Pictures.ofStream(fileInputStream, PictureType.JPEG)
                                        .size(Integer.valueOf(size), Integer.valueOf(size)).create();
                                pictureRenderDataList.add(pictureRenderData);
                            }
                        }else  if (null == inputStreamList && null != list) {
                            List<String> ofUrlList = (List<String>) list;
                            for(String ofurl : ofUrlList){
                                PictureRenderData pictureRenderData = Pictures.ofUrl(ofurl).size(Integer.valueOf(size), Integer.valueOf(size)).create();
                                pictureRenderDataList.add(pictureRenderData);
                            }
                        }
                    }

                    return mappings;
                }

            }
        }
        //table-editor  列表模式
        if ("table-editor".equals(type)) {
            flagList.add(key);
            String objectStr = formDataObject.getStr(key);
            cn.hutool.json.JSONArray parseArray = JSONUtil.parseArray(objectStr);
            mappings.put(key, PoiTlUtil.jsonArr2List(parseArray));
            return mappings;
        }
        //时间类
        if ("datetime".equals(type)) {
            Object dateObj = formDataObject.get(key);
            if (null != dateObj && dateObj instanceof Long) {
                Long dateLong = (Long) dateObj;
                Date date = DateUtil.date(dateLong);
                String formatDateTime = DateUtil.formatDateTime(date);
                mappings.put(key, formatDateTime);
                return mappings;
            }
        }
        //日期类
        if ("date".equals(type)) {
            Object dateObj = formDataObject.get(key);
            if (null != dateObj && dateObj instanceof Long) {
                Long dateLong = (Long) dateObj;
                Date date = DateUtil.date(dateLong);
                String formatDateTime = DateUtil.format(date, "yyyy年MM月dd日");
                mappings.put(key, formatDateTime);
                return mappings;
            }
        }
        //展示图片类
        if ("image".equals(type)) {
            cn.hutool.json.JSONArray defaultArr = jsonObject.getJSONArray("default");
            cn.hutool.json.JSONObject attrsObject = jsonObject.getJSONObject("attrs");
            String size = "200";
            try {
                size = attrsObject.getStr("size");
            } catch (Exception e) {
                // e.printStackTrace();
            }
            if (null != defaultArr) {
                List<InputStream> inputStreamList = new ArrayList<>();
                List<String> list = defaultArr.toList(String.class);
                for (String attaUrl : list) {
                    inputStreamList.add(FileDownUtil.getFileInputStream(attaUrl));
                }
                Map<String, Object> file = new HashMap<>();
                //file.put("ofUrlList",list);
                file.put("fileInputStream", inputStreamList);
                file.put("height", Integer.valueOf(size));
                file.put("width", Integer.valueOf(size));
                pictureRenderDataMap.put(key, file);
                return mappings;

            }
        }

        //是否、开关
        if("yesno".equals(type) || "switch".equals(type)){
            //Object yesno = formDataObject.get(key);
            //mappings.put(key, null != yesno && "true".equals(yesno.toString()) ?"是":"否");
            mappings.put(key,formDataObject.get(key));
            return mappings;
        }
        if("number".equals(type)){
            Object data = formDataObject.get(key);
            mappings.put(key, data);
            return mappings;
        }
        //日期范围、时间范围
        if("daterange".equals(type) || "datetimerange".equals(type)){
            cn.hutool.json.JSONArray jsonArray = formDataObject.getJSONArray(key);
            String optionsStr = StringUtils.join(jsonArray.toArray(), "到");
            mappings.put(key,optionsStr);
            return mappings;
        }

        cn.hutool.json.JSONObject formItemListObject = jsonObject.getJSONObject("formItemList");
        //列表模式
        if (null != formItemListObject && formItemListObject.size() > 0) {
            Object o = formDataObject.get(key);
            if(null!=formDataObject && o instanceof cn.hutool.json.JSONObject){
                Object object = formDataObject.getJSONObject(key).get(key);
                if (null != object && object instanceof cn.hutool.json.JSONArray) {
                    cn.hutool.json.JSONArray jsonArray = (cn.hutool.json.JSONArray) object;
                    mappings.put(key, PoiTlUtil.jsonArr2List(jsonArray));
                    flagList.add(key);
                    return mappings;
                }
            }
        }

        //子表格模式
        if ("column".equals(type)) {
            cn.hutool.json.JSONObject formItemObject = jsonObject.getJSONObject("formItemList");
            jsonObject = formItemObject.getJSONObject(key);
            String columType = jsonObject.getStr("type");
            //是否、开关
            if("yesno".equals(columType) || "switch".equals(columType)){
                Object yesno = formDataObject.get(key);
                mappings.put(key, null != yesno && "true".equals(yesno.toString()) ?"是":"否");
                return mappings;
            }
            if("number".equals(columType)){
                Object data = formDataObject.get(key);
                mappings.put(key, data);
                return mappings;
            }
            //日期范围、时间范围
            if("daterange".equals(columType) || "datetimerange".equals(columType)){
                cn.hutool.json.JSONArray jsonArray = formDataObject.getJSONArray(key);
                String optionsStr = StringUtils.join(jsonArray.toArray(), "到");
                mappings.put(key,optionsStr);
                return mappings;
            }
            //图片类
            if ("image-uploader".equals(columType) || "ocr-image".equals(type)) {
                cn.hutool.json.JSONObject attrsObject = jsonObject.getJSONObject("attrs");
                if (null != attrsObject) {
                    Object actionObj = formDataObject.get(key);
                    //List<InputStream> inputStreamList = new ArrayList<>();
                    if (actionObj instanceof String) {
                        String attaOid = actionObj.toString().replace("/form-api/form/manager/downloadFile/", "");
                        String size = attrsObject.getStr("size");
                        PictureRenderData pictureRenderData = Pictures.ofStream(getInputStreamByAtta(attaOid), PictureType.JPEG)
                                .size(Integer.valueOf(size), Integer.valueOf(size)).create();
                        mappings.put(key,pictureRenderData);

                    }
                    if (actionObj instanceof cn.hutool.json.JSONArray) {
                        String size = "200";
                        try {
                            size = attrsObject.getStr("size");
                        } catch (Exception e) {
                            // e.printStackTrace();
                        }
                        cn.hutool.json.JSONArray defaultArr = (cn.hutool.json.JSONArray) actionObj;
                        List<String> list = defaultArr.toList(String.class);
                        List<PictureRenderData> pictureRenderDataList = new ArrayList<>();
                        for (String attaUrl : list) {
                            String attaOid = attaUrl.replace("/form-api/form/manager/downloadFile/", "");
                            //inputStreamList.add(getInputStreamByAtta(attaOid));
                            PictureRenderData pictureRenderData = Pictures.ofStream(getInputStreamByAtta(attaOid), PictureType.JPEG)
                                    .size(Integer.valueOf(size), Integer.valueOf(size)).create();
                            pictureRenderDataList.add(pictureRenderData);

                        }
                        mappings.put(key,pictureRenderDataList);
                    }

                }

            }


            //下拉，多选等值
            Object options = jsonObject.get("options");
            //options 是api接口
            cn.hutool.json.JSONArray optionsJSONArray = null;
            if(null != options && options.toString().startsWith("http")){
                String result = HttpUtil.get(options.toString(), CharsetUtil.CHARSET_UTF_8);
                try {
                    optionsJSONArray = JSONUtil.parseArray(result);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if (options instanceof cn.hutool.json.JSONArray) {
                optionsJSONArray = (cn.hutool.json.JSONArray) options;
            }
            cn.hutool.json.JSONObject attr = jsonObject.getJSONObject("attr");
            Object multipleFlag = null!=attr ? attr.get("multiple"):null;
            if (null != optionsJSONArray && optionsJSONArray.size() > 0) {
                Iterator<Object> iterator = optionsJSONArray.iterator();
                List<String> optionsDoc = new ArrayList<>();
                cn.hutool.json.JSONObject prop = jsonObject.getJSONObject("prop");
                while (iterator.hasNext()) {
                    cn.hutool.json.JSONObject optObject = (cn.hutool.json.JSONObject) iterator.next();
                    String propText = "text";
                    String propValue = "value";
                    if(null != prop){
                        propText = prop.getStr("text");
                        propValue = prop.getStr("value");
                    }
                    String text = optObject.getStr(propText);
                    String value = optObject.getStr(propValue);
                    String keyVal = formDataObject.getStr(key);
                    if(null ==multipleFlag && ("select".equals(columType) || "tree-select".equals(columType) || "select".equals(columType))){
                        if(StrUtil.isEmpty(value)){
                            optionsDoc.clear();
                            optionsDoc.add(keyVal);
                        }else if(keyVal.contains(value)){
                            optionsDoc.add(text);
                        }
                    }else {
                        String emptyCheck = "□";
                        if (StrUtil.isNotEmpty(value) && keyVal.contains(value)) {
                            //选择的哪项
                            emptyCheck = "☑";
                        }
                        optionsDoc.add(emptyCheck + text);
                    }

                }
                String optionsStr = StringUtils.join(optionsDoc.toArray(), " ");
                mappings.put(key, optionsStr);
                return mappings;
            }
            if (options instanceof String && !"undefined".equals(options.toString())) {
                String decodeStr = cn.hutool.core.codec.Base64.decodeStr(options.toString());
                //System.out.println("encode:"+decodeStr);
                try {
                    ScriptEngineManager manager = new ScriptEngineManager();
                    ScriptEngine engine = manager.getEngineByName("javascript");
                    engine.eval("getInfo = "+decodeStr);
                    Invocable invocable = (Invocable) engine;
                    //Map map = new HashMap();
                    //System.out.println(formDataObject);
                    //map.put("province","1");
                    Object res = invocable.invokeFunction("getInfo", formDataObject);
                    //System.out.println(res);
                    if(res instanceof ScriptObjectMirror){
                        ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) res;
                        Iterator<Object> iterator = scriptObjectMirror.values().iterator();
                        List<String> optionsDoc = new ArrayList<>();
                        while (iterator.hasNext()) {
                            ScriptObjectMirror optObject = (ScriptObjectMirror) iterator.next();
                            String propText = "text";
                            String propValue = "value";
                            String text = null!=optObject.get(propText) ? optObject.get(propText).toString():"";
                            String value = null!=optObject.get(propValue) ? optObject.get(propValue).toString():"";
                            String keyVal = formDataObject.getStr(key);
                            if(null ==multipleFlag && ("select".equals(columType) || "tree-select".equals(columType) || "select".equals(columType))){
                                if(StrUtil.isEmpty(value)){
                                    optionsDoc.clear();
                                    optionsDoc.add(keyVal);
                                }else if(keyVal.contains(value)){
                                    optionsDoc.add(text);
                                }
                            }else {
                                String emptyCheck = "□";
                                if (StrUtil.isNotEmpty(value) && keyVal.contains(value)) {
                                    //选择的哪项
                                    emptyCheck = "☑";
                                }
                                optionsDoc.add(emptyCheck + text);
                            }

                        }
                        String optionsStr = StringUtils.join(optionsDoc.toArray(), " ");
                        mappings.put(key, optionsStr);
                        return mappings;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            return mappings;
        }

        //下拉，多选等值
        Object options = jsonObject.get("options");
        //options 是api接口
        cn.hutool.json.JSONArray optionsJSONArray = null;
        if(null != options && options.toString().startsWith("http")){
            String result = HttpUtil.get(options.toString(), CharsetUtil.CHARSET_UTF_8);
            try {
                optionsJSONArray = JSONUtil.parseArray(result);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (options instanceof cn.hutool.json.JSONArray) {
            optionsJSONArray = (cn.hutool.json.JSONArray) options;
        }
        cn.hutool.json.JSONObject attr = jsonObject.getJSONObject("attr");
        Object multipleFlag = null!=attr ? attr.get("multiple"):null;
        if (null != optionsJSONArray && optionsJSONArray.size() > 0) {
            Iterator<Object> iterator = optionsJSONArray.iterator();
            List<String> optionsDoc = new ArrayList<>();
            cn.hutool.json.JSONObject prop = jsonObject.getJSONObject("prop");
            while (iterator.hasNext()) {
                cn.hutool.json.JSONObject optObject = (cn.hutool.json.JSONObject) iterator.next();
                String propText = "text";
                String propValue = "value";
                if(null != prop){
                     propText = prop.getStr("text");
                     propValue = prop.getStr("value");
                }
                String text = optObject.getStr(propText);
                String value = optObject.getStr(propValue);
                String keyVal = formDataObject.getStr(key);
                if(null ==multipleFlag && ("select".equals(type) || "tree-select".equals(type) || "select".equals(type))){
                    if(StrUtil.isEmpty(value)){
                        optionsDoc.clear();
                        optionsDoc.add(keyVal);
                    }else if(keyVal.contains(value)){
                        optionsDoc.add(text);
                    }
                }else {
                    String emptyCheck = "□";
                    if (StrUtil.isNotEmpty(value) && keyVal.contains(value)) {
                        //选择的哪项
                        emptyCheck = "☑";
                        mappings.put(key+"_"+value,"☑");//CheckBoxRenderPolicy方式
                    }
                    optionsDoc.add(emptyCheck + text);
                }

            }
            String optionsStr = StringUtils.join(optionsDoc.toArray(), " ");
            mappings.put(key, optionsStr);
            return mappings;
        }
        if (options instanceof String && !"undefined".equals(options.toString())) {
            String decodeStr = cn.hutool.core.codec.Base64.decodeStr(options.toString());
            //System.out.println("encode:"+decodeStr);
            try {
                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByName("javascript");
                engine.eval("getInfo = "+decodeStr);
                Invocable invocable = (Invocable) engine;
                Map map = new HashMap();
                System.out.println(formDataObject);
                map.put("province","1");
                Object res = invocable.invokeFunction("getInfo", formDataObject);
                //System.out.println(res);
                if(res instanceof ScriptObjectMirror){
                    ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) res;
                    Iterator<Object> iterator = scriptObjectMirror.values().iterator();
                    List<String> optionsDoc = new ArrayList<>();
                    while (iterator.hasNext()) {
                        ScriptObjectMirror optObject = (ScriptObjectMirror) iterator.next();
                        String propText = "text";
                        String propValue = "value";
                        String text = null!=optObject.get(propText) ? optObject.get(propText).toString():"";
                        String value = null!=optObject.get(propValue) ? optObject.get(propValue).toString():"";
                        String keyVal = formDataObject.getStr(key);
                        if(null ==multipleFlag && ("select".equals(type) || "tree-select".equals(type) || "select".equals(type))){
                            if(StrUtil.isEmpty(value)){
                                optionsDoc.clear();
                                optionsDoc.add(keyVal);
                            }else if(keyVal.contains(value)){
                                optionsDoc.add(text);
                            }
                        }else {
                            String emptyCheck = "□";
                            if (StrUtil.isNotEmpty(value) && keyVal.contains(value)) {
                                //选择的哪项
                                emptyCheck = "☑";
                            }
                            optionsDoc.add(emptyCheck + text);
                        }

                    }
                    String optionsStr = StringUtils.join(optionsDoc.toArray(), " ");
                    mappings.put(key, optionsStr);
                    return mappings;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return mappings;
    }


    /**
     * @description: 根据附件主键获取文件流
     * @param attaOid 附件oid
     * @author: wuxx
     * @Date: 2021/12/3 16:04
     **/
    private InputStream getInputStreamByAtta(String attaOid){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            @Cleanup InputStream inputStream = formAttaManager.getFileInputStream(request, attaOid);
            return inputStream;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
