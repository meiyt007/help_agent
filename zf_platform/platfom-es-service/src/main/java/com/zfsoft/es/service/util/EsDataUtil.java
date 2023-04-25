package com.zfsoft.es.service.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.pagehelper.page.PageMethod;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zfsoft.es.service.annotation.EnabledToEsServer;
import com.zfsoft.es.service.common.SysConfig;
import com.zfsoft.es.service.dto.CommonQueryDto;
import com.zfsoft.es.service.dto.FieldDetailDto;
import com.zfsoft.es.service.dto.QueryFieldDto;
import com.zfsoft.es.service.dto.SendESObjDto;
import com.zfsoft.es.service.entity.Page;
import com.zfsoft.es.service.enums.PrimaryKeyEnum;
import com.zfsoft.es.service.enums.QueryType;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.plugin.nlpcn.QueryActionElasticExecutor;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.nlpcn.es4sql.SearchDao;
import org.nlpcn.es4sql.jdbc.ObjectResult;
import org.nlpcn.es4sql.jdbc.ObjectResultsExtractor;
import org.nlpcn.es4sql.query.QueryAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.IntStream;

/**
 * @author: kkfan
 * @create: 2021-01-18 15:10:19
 * @description: es数据转化帮助类
 */
public class EsDataUtil {

    private static final Logger logger = LoggerFactory.getLogger(EsDataUtil.class);

    /**
     * 参数转化为发送到es的对象
     * @param obj   参数对象
     * @param enabledToEsServer 注解详情
     * @return
     */
    public static SendESObjDto getSendESObj(Object obj, EnabledToEsServer enabledToEsServer) throws Exception {
        Class clazz = obj.getClass();
        Document documentAnnotation = (Document) clazz.getAnnotation(Document.class);
        SendESObjDto sendESObjDto = new SendESObjDto();
        if(documentAnnotation != null) {
            sendESObjDto.setIndexName(documentAnnotation.indexName());
            sendESObjDto.setType(documentAnnotation.type());
            sendESObjDto.setReplicasNum((int) documentAnnotation.replicas());
            sendESObjDto.setShardsNum((int) documentAnnotation.shards());
        } else {
            String indexName = StringUtils.isEmpty(enabledToEsServer.indexName()) ? NamingConversion.humpToUnderline(clazz.getSimpleName()).toLowerCase() : enabledToEsServer.indexName();
            indexName = indexName.replace("db_", "t_");
            sendESObjDto.setIndexName(indexName);
            // sendESObjDto.setType(StringUtils.isEmpty(enabledToEsServer.indexType()) ? clazz.getSimpleName() : enabledToEsServer.indexType());
            sendESObjDto.setType(NamingConversion.underlineToHump(indexName));
            sendESObjDto.setReplicasNum(enabledToEsServer.replicasNum());
            sendESObjDto.setShardsNum(enabledToEsServer.shardsNum());
        }


        List<FieldDetailDto> fieldDetailList = Lists.newArrayList();
        // 向上循环  遍历父类
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            List<Field> fieldList = Arrays.asList(clazz.getDeclaredFields());
            Optional.ofNullable(fieldList)
                    .orElseGet(Lists::newArrayList)
                    .stream()
                    .filter(field -> !StringUtils.equals(field.getName(), "serialVersionUID"))
                    .forEach(field -> {
                        try {
                            field.setAccessible(true);
                            FieldDetailDto fieldDetailDto = new FieldDetailDto();
                            org.springframework.data.elasticsearch.annotations.Field fieldAnnotation = field.getAnnotation(org.springframework.data.elasticsearch.annotations.Field.class);
                            if(fieldAnnotation != null) {
                                // 实体属性添加了 org.springframework.data.elasticsearch.annotations.Field 注解
                                fieldDetailDto.setFieldName(field.getName());
                                // 这里要排除注解中type的默认值
                                fieldDetailDto.setFieldType((StringUtils.isEmpty(fieldAnnotation.type().name()) && !StringUtils.equals(fieldAnnotation.type().name(), FieldType.Auto.name())) ? EsFieldMappingUtil.objFieldTypeToEsType(field.getGenericType().toString()) : fieldAnnotation.type().name());
                                fieldDetailDto.setAnalyzer(fieldAnnotation.analyzer());
                                fieldDetailDto.setSearch_analyzer(fieldAnnotation.searchAnalyzer());
                                fieldDetailDto.setFieldValue((StringUtils.isNotEmpty(fieldAnnotation.name()) || StringUtils.isNotEmpty(fieldAnnotation.value())) ? fieldAnnotation.name() : field.get(obj));
                                fieldDetailDto.setFielddata(fieldAnnotation.fielddata());
                            } else {
                                fieldDetailDto.setFieldName(field.getName());
                                fieldDetailDto.setFieldType(EsFieldMappingUtil.objFieldTypeToEsType(field.getGenericType().toString()));
                                fieldDetailDto.setAnalyzer(SysConfig.ANALYZER.IK.IK_MAX_WORD);
                                fieldDetailDto.setSearch_analyzer(SysConfig.ANALYZER.IK.IK_MAX_WORD);
                                fieldDetailDto.setFieldValue(field.get(obj));
                                fieldDetailDto.setFielddata(false);
                            }
                            fieldDetailList.add(fieldDetailDto);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        sendESObjDto.setFieldDtoList(fieldDetailList);
        return sendESObjDto;
    }

    /**
     * 获取方法的返回值类型
     * @param method    切入方法
     * @return
     * @throws Exception
     */
    public static JavaType getMethodReturnJavaType(Method method) throws Exception {
        Type type = method.getGenericReturnType();    //判断是否带有泛型
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            //获取泛型类型
            Class rowClass = (Class) ((ParameterizedType) type).getRawType();
            JavaType[] javaTypes = new JavaType[actualTypeArguments.length];
            for (int i = 0; i < actualTypeArguments.length; i++) {            //泛型也可能带有泛型，递归获取  暂未考虑实现
                // javaTypes[i] = getMethodReturnJavaType(actualTypeArguments[i]);
                javaTypes[i] = TypeFactory.defaultInstance().constructType(actualTypeArguments[i]);
            }
            return TypeFactory.defaultInstance().constructParametricType(rowClass, javaTypes);
        } else {        //简单类型直接用该类构建JavaType
            Class cla = (Class) type;
            return TypeFactory.defaultInstance().constructParametricType(cla, new JavaType[0]);
        }
    }

    /**
     * es搜索结果转化为对应实体的list
     * @author kkfan
     * @param list 搜索结果
     * @param clzz  实体类型
     * @param <T>
     * @return
     */
    public static <T, U> Object toObjectList(List list, Class<T> clzz, Class<U> contentClzz, QueryType queryType) {
        if(!CollectionUtils.isEmpty(list)){

            String result = clzz.getClass().getName();
            Class aClass = ReflectionUtil.typeMap.get(result);
            if(!clzz.getClass().isPrimitive() && null == aClass) {
                if(clzz.isAssignableFrom(List.class)) {
                    final List<U> resList = Lists.newArrayList();
                    list.forEach(item -> {
                        U u;
                        if(StringUtils.equals(queryType.name(), QueryType.REFLECTION.name())) {
                            u = JSONArray.parseObject(JSONObject.toJSONString(((HashMap) item).get("source")), contentClzz);
                        } else {
                            u = JSONArray.parseObject(JSONObject.toJSONString(item), contentClzz);
                        }
                        resList.add(u);
                    });
                    return resList;
                }
                final List<T> resList = Lists.newArrayList();
                T t;
                if(StringUtils.equals(queryType.name(), QueryType.REFLECTION.name())) {
                    t = JSONArray.parseObject(JSONObject.toJSONString(((HashMap)list.get(0)).get("source")), clzz);
                } else {
                    t = JSONArray.parseObject(JSONObject.toJSONString(list.get(0)), clzz);
                }
                return t;
            } else {
                throw new IllegalArgumentException("暂只支持List和java对象作为返回值");
            }
        }
        return null;
    }

    /**
     * 获取对象主键属性
     * @param clz
     * @return
     */
    public static Field getPrimaryKeyField(Class clz) {
        //利用反射创建对象实例
        Field[] fields = clz.getDeclaredFields();
        for (Field f : fields) {
            Id id = f.getAnnotation(Id.class);
            if(id != null || StringUtils.equalsAnyIgnoreCase(f.getName(), PrimaryKeyEnum.id.name(), PrimaryKeyEnum.oid.name())) {
                return f;
            }
        }
        return null;
    }

    /**
     * 封装es服务查询对象
     * @param oid
     * @param clz
     * @param enabledToEsServer
     * @return
     * @throws Exception
     */
    public static CommonQueryDto packageCommonQueryDto(String oid, Class clz, EnabledToEsServer enabledToEsServer) throws Exception {
        CommonQueryDto commonQueryDto = new CommonQueryDto()
                .setIndexName(StringUtils.isEmpty(enabledToEsServer.indexName()) ? NamingConversion.humpToUnderline(clz.getSimpleName()).toLowerCase() : enabledToEsServer.indexName())
                .setType(StringUtils.isEmpty(enabledToEsServer.indexType()) ? clz.getSimpleName() : enabledToEsServer.indexType())
                .setCurrentPageNo(1)
                .setPageSize(1);
        List<QueryFieldDto> queryFieldDtoList = Lists.newArrayList();
        // 获取对象主键属性
        Field field = getPrimaryKeyField(clz);

        // 查询oid
        QueryFieldDto oidFieldDto = new QueryFieldDto();
        oidFieldDto.setFieldName(field != null ? field.getName() : "oid");
        oidFieldDto.setFieldValue(oid);
        queryFieldDtoList.add(oidFieldDto);
        commonQueryDto.setQueryFields(queryFieldDtoList);
        return commonQueryDto;
    }

    /**
     * 封装es服务查询对象
     * @param paramsKeyValueMap 参数名 - 参数值
     * @param clz
     * @param enabledToEsServer
     * @return
     * @throws Exception
     */
    public static CommonQueryDto packageCommonQueryDto(Map<String, Object> paramsKeyValueMap, Class clz, EnabledToEsServer enabledToEsServer) throws Exception {
        CommonQueryDto commonQueryDto = new CommonQueryDto()
                .setIndexName(StringUtils.isEmpty(enabledToEsServer.indexName()) ? NamingConversion.humpToUnderline(clz.getSimpleName()).toLowerCase() : enabledToEsServer.indexName())
                .setType(StringUtils.isEmpty(enabledToEsServer.indexType()) ? clz.getSimpleName() : enabledToEsServer.indexType())
                .setCurrentPageNo(1)
                .setPageSize(1);
        List<QueryFieldDto> queryFieldDtoList = Lists.newArrayList();

        Optional.ofNullable(paramsKeyValueMap)
                .orElseGet(Maps::newHashMap)
                .forEach((k, v) -> {
                    // 查询oid
                    QueryFieldDto fieldDto = new QueryFieldDto();
                    fieldDto.setFieldName(k);
                    fieldDto.setFieldValue(v);
                    queryFieldDtoList.add(fieldDto);
                    commonQueryDto.setQueryFields(queryFieldDtoList);
                });
        return commonQueryDto;
    }

    /**
     * 此方式是获取将要执行的sql 通过sql转化为es查询语句查询 适应性更广
     *      暂未解决问题：
     *          pageHelper 分页暂无好的方法实现
     *          复杂sql可能查询结果不准确
     * @param proceedingJoinPoint   切点
     * @param sqlSessionFactory sql session 工厂
     * @param elasticsearchTemplate elasticsearch 模板
     * @return
     * @throws Exception
     */
    public static Object queryBySql(ProceedingJoinPoint proceedingJoinPoint,
                                    SqlSessionFactory sqlSessionFactory, ElasticsearchTemplate elasticsearchTemplate) throws Exception{
        com.github.pagehelper.Page<Object> localPage = PageMethod.getLocalPage();
        String sql = SqlUtil.getMybatisSql(proceedingJoinPoint, sqlSessionFactory);
        Object resObj = null;
        if(needPage(localPage)) {
            sql = sql + " limit " + localPage.getPageSize() * (localPage.getPageNum() - 1) + "," + localPage.getPageSize();
            resObj = queryEsForPage(sql, proceedingJoinPoint, elasticsearchTemplate, localPage.getPageNum(), localPage.getPageSize());
        } else {
            resObj = queryEsForList(sql, proceedingJoinPoint, elasticsearchTemplate);
        }
        if(null != resObj && (!(resObj instanceof List) || (resObj instanceof List && !CollectionUtils.isEmpty((List)resObj)))) {
            PageMethod.clearPage();
        }
        return resObj;

    }

    private static Object queryEsForPage(String sql, ProceedingJoinPoint proceedingJoinPoint, ElasticsearchTemplate elasticsearchTemplate, Integer pageNum, Integer pageSize) throws Exception{
        SearchDao searchDao = new SearchDao(elasticsearchTemplate.getClient());
        QueryAction queryAction = searchDao.explain(SqlUtil.convertSQLUnderscore(sql));
        Object execution = QueryActionElasticExecutor.executeAnyAction(searchDao.getClient(), queryAction);
        if(execution instanceof SearchResponse) {
            JavaType javaType = EsDataUtil.getMethodReturnJavaType(((MethodSignature)proceedingJoinPoint.getSignature()).getMethod());
            // 获取方法返回类型
            JavaType contentType = javaType.getContentType();
            //返回搜索结果
            SearchHits hits = ((SearchResponse)execution).getHits();
            Long totalCount = hits.getTotalHits();
            com.github.pagehelper.Page page = new com.github.pagehelper.Page(pageNum, pageSize);
            page.setTotal(totalCount.intValue());
            page.addAll(transformResToList(hits, contentType.getRawClass()));
            return page;
        }
        return null;
    }

    private static Object queryEsForList(String sql, ProceedingJoinPoint proceedingJoinPoint, ElasticsearchTemplate elasticsearchTemplate) throws Exception{
        SearchDao searchDao = new SearchDao(elasticsearchTemplate.getClient());
        QueryAction queryAction = searchDao.explain(SqlUtil.convertSQLUnderscore(sql));
        Object execution = QueryActionElasticExecutor.executeAnyAction(searchDao.getClient(), queryAction);
        ObjectResult objectResult = new ObjectResultsExtractor(false, false, false, false, queryAction).extractResults(execution, true);
        List<String> headersList = objectResult.getHeaders();
        List<List<Object>> linesList = objectResult.getLines();
        List<Map<String, Object>> resObject = Lists.newArrayList();
        linesList.forEach(lines -> {
            Map<String, Object> collect = IntStream.range(0, headersList.size())
                    .collect(HashMap::new, (m, i) -> m.put(headersList.get(i), lines.get(i)), (m1, m2) -> {});
            resObject.add(collect);
        });
        JavaType javaType = EsDataUtil.getMethodReturnJavaType(((MethodSignature)proceedingJoinPoint.getSignature()).getMethod());
        // 获取方法返回类型
        Class clz = javaType.getRawClass();
        JavaType contentType = javaType.getContentType();
        Object object = EsDataUtil.toObjectList(resObject, clz, contentType == null ? null : contentType.getRawClass(), QueryType.SQL);
        return object;
    }

    /**
     * 将搜索结果中的hits转化为list
     * @param hits
     * @param clzz
     * @param <T>
     * @return
     */
    private static <T> List<T> transformResToList(SearchHits hits, Class<T> clzz) {
        List<T> resObjList = Lists.newArrayList();
        for (SearchHit searchHit : hits) {
            T obj = JSONArray.parseObject(JSONObject.toJSONString(searchHit.getSourceAsMap()), clzz);
            resObjList.add(obj);
        }
        return resObjList;
    }

    /**
     * 此方式为通过反射获取入参及入参值 通过返回值推测查询索引 只能进行简单查询 局限性较大
     * @param proceedingJoinPoint
     * @param enabledToEsServer
     * @return
     * @throws Exception
     */
    public static Object queryByReflection(ProceedingJoinPoint proceedingJoinPoint, EnabledToEsServer enabledToEsServer) throws Exception {
        // 获取方法返回值类型
        JavaType javaType = EsDataUtil.getMethodReturnJavaType(((MethodSignature)proceedingJoinPoint.getSignature()).getMethod());
        // 获取方法返回类型
        Class clz = javaType.getRawClass();
        Map<String, Object> paramsKeyValueMap = ReflectionUtil.getFieldsName(proceedingJoinPoint);
        // 封装查询请求参数(通过主键查询)
        CommonQueryDto commonQueryDto = EsDataUtil.packageCommonQueryDto(paramsKeyValueMap, clz, enabledToEsServer);
        // 请求es中数据
        Object object = null;
        Page page = ElasticSearchUtil.universalQuery(commonQueryDto);
        if(!CollectionUtils.isEmpty(page.getResList())) {
            JavaType contentType = javaType.getContentType();
            // 转化查询结果
            object = EsDataUtil.toObjectList(page.getResList(), clz, contentType == null ? null : contentType.getRawClass(), QueryType.REFLECTION);
        }
        return object;
    }

    private static Boolean needPage(com.github.pagehelper.Page<Object> localPage) {
        try {
            localPage.getPageSize();
            localPage.getPageNum();
            return true;
        } catch (Exception e) {
            return false;
        }

    }


}
