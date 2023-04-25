package com.zfsoft.es.service.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zfsoft.es.service.common.SysConfig;
import com.zfsoft.es.service.dto.CommonQueryDto;
import com.zfsoft.es.service.dto.FieldDetailDto;
import com.zfsoft.es.service.dto.QueryFieldDto;
import com.zfsoft.es.service.dto.SendESObjDto;
import com.zfsoft.es.service.entity.Page;
import com.zfsoft.es.service.enums.PrimaryKeyEnum;
import com.zfsoft.es.service.exception.CreateIndexException;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * es操作工具类
 * @author: kkfan
 * @create: 2021-1-18 21:35
 */
@Component
public class ElasticSearchUtil {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchUtil.class);

    private static ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 创建索引
     * @param index
     * @return
     */
    public static boolean createIndex(String index) {
        Assert.hasLength(index, "要创建的索引名称不能为空！");
        if (isIndexExist(index)) {
            logger.debug(MessageFormat.format("索引已存在，索引名称为：{0}", index));
            return true;
        }
        CreateIndexResponse indexresponse = null;
        try {
            indexresponse = elasticsearchTemplate.getClient()
                    .admin()
                    .indices()
                    .prepareCreate(index)
                    .execute()
                    .actionGet();
        } catch (Exception e) {
            logger.error(MessageFormat.format("索引创建出现异常，索引名称为：{0}", index), e);
            return false;
        }
        logger.debug(MessageFormat.format("索引创建{0}, 索引名称为：{1}", indexresponse.isAcknowledged() ? "成功" : "失败", index));

        return indexresponse.isAcknowledged();
    }

    /**
     * 删除索引
     *
     * @param index
     * @return
     */
    public static boolean deleteIndex(String index) {
        Assert.hasLength(index, "要删除的索引名称不能为空！");
        if (!isIndexExist(index)) {
            logger.info(MessageFormat.format("要删除的索引不存在，索引名称为：{0}", index));
            return false;
        }
        AcknowledgedResponse acknowledgedResponse = null;
        try {
            acknowledgedResponse = elasticsearchTemplate.getClient()
                    .admin()
                    .indices()
                    .prepareDelete(index)
                    .execute()
                    .actionGet();
        } catch (Exception e) {
            logger.error(MessageFormat.format("删除索引出现异常，索引名称为：{0}", index), e);
            return false;
        }
        logger.debug(MessageFormat.format("索引删除{0}, 索引名称为：{1}", acknowledgedResponse.isAcknowledged() ? "成功" : "失败", index));
        return acknowledgedResponse.isAcknowledged();
    }

    /**
     * 判断索引是否存在
     *
     * @param index
     * @return
     */
    public static boolean isIndexExist(String index) {
        Assert.hasLength(index, "操作数据索引不能为空！");
        IndicesExistsResponse inExistsResponse = elasticsearchTemplate.getClient()
                .admin()
                .indices()
                .exists(new IndicesExistsRequest(index))
                .actionGet();
        logger.debug(MessageFormat.format("索引{0}, 索引名称为：{1}", inExistsResponse.isExists() ? "存在" : "不存在", index));
        return inExistsResponse.isExists();
    }

    /**
     * 数据添加，指定ID
     * @param jsonObject 要增加的数据
     * @param index      索引，类似数据库
     * @param type       类型，类似表
     * @param id         数据ID
     * @return 新增数据id
     */
    public static String addData(JSONObject jsonObject, String index, String type, String id) {

        Assert.hasLength(index, "操作数据索引不能为空！");
        Assert.hasLength(type, "操作数据类型不能为空！");
        Assert.hasLength(id, "操作数据主键不能为空！");
        Assert.notNull(jsonObject, "更新的数据不能为空！");

        IndexResponse response = elasticsearchTemplate.getClient()
                .prepareIndex(index, type, id)
                .setSource(jsonObject)
                .get();

        logger.debug(MessageFormat.format("数据添加{0}, id为：{1}, 传入原始数据为：{2}, 索引名称为：{3}, 索引类型为：{4}",
                (response.status().getStatus() == 200) ? "存在" : "不存在",
                response.getId(),
                jsonObject.toJSONString(),
                index,
                type)
        );

        return response.getId();
    }

    /**
     * 数据添加 (无主键)
     * @param jsonObject 要增加的数据
     * @param index      索引，类似数据库
     * @param type       类型，类似表
     * @return 新增数据id
     */
    public static String addData(JSONObject jsonObject, String index, String type) {
        Assert.hasLength(index, "操作数据索引不能为空！");
        Assert.hasLength(type, "操作数据类型不能为空！");
        Assert.notNull(jsonObject, "更新的数据不能为空！");
        // return addData(jsonObject, index, type, UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
        IndexResponse response = elasticsearchTemplate.getClient()
                .prepareIndex(index, type)
                .setSource(jsonObject)
                .get();

        logger.debug(MessageFormat.format("数据添加{0}, id为：{1}, 传入原始数据为：{2}, 索引名称为：{3}, 索引类型为：{4}",
                (response.status().getStatus() == 200) ? "存在" : "不存在",
                response.getId(),
                jsonObject.toJSONString(),
                index,
                type)
        );

        return response.getId();
    }

    /**
     * 通过ID删除数据
     *
     * @param index 索引，类似数据库
     * @param type  类型，类似表
     * @param id    数据ID
     * @return true - 删除成功  false - 删除失败
     */
    public static Boolean deleteDataById(String index, String type, String id) {
        Assert.hasLength(index, "操作数据索引不能为空！");
        Assert.hasLength(type, "操作数据类型不能为空！");
        Assert.hasLength(id, "操作数据主键不能为空！");
        DeleteResponse response = elasticsearchTemplate.getClient()
                .prepareDelete(index, type, id)
                .execute()
                .actionGet();

        logger.debug(MessageFormat.format("数据删除{0}, id为：{1}, 索引名称为：{2}, 索引类型为：{3}",
                (response.status().getStatus() == 200) ? "成功" : "失败",
                response.getId(),
                index,
                type)
        );

        return response.status().getStatus() == 200;
    }

    /**
     * 通过ID 更新数据
     *
     * @param jsonObject 要增加的数据
     * @param index      索引，类似数据库
     * @param type       类型，类似表
     * @param id         数据ID
     * @return
     */
    public static Boolean updateDataById(JSONObject jsonObject, String index, String type, String id) {
        Assert.hasLength(index, "操作数据索引不能为空！");
        Assert.hasLength(type, "操作数据类型不能为空！");
        Assert.hasLength(id, "操作数据主键不能为空！");
        Assert.notNull(jsonObject, "更新的数据不能为空！");

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(index)
                .type(type)
                .id(id)
                .doc(jsonObject);
        UpdateResponse updateResponse = elasticsearchTemplate.getClient().update(updateRequest).actionGet();
        logger.debug(MessageFormat.format("数据更新{0}, id为：{1}, 索引名称为：{2}, 索引类型为：{3}, 原始数据为：{4}",
                (updateResponse.status().getStatus() == 200) ? "成功" : "失败",
                updateResponse.getId(),
                index,
                type,
                jsonObject.toJSONString())
        );

        return updateResponse.status().getStatus() == 200;
    }

    /**
     * 通过ID获取数据
     *
     * @param index  索引，类似数据库
     * @param type   类型，类似表
     * @param id     数据ID
     * @param fields 需要显示的字段，逗号分隔（缺省为全部字段）
     * @return
     */
    public static Map<String, Object> searchDataById(String index, String type, String id, String fields) {

        Assert.hasLength(index, "操作数据索引不能为空！");
        Assert.hasLength(type, "操作数据类型不能为空！");
        Assert.hasLength(id, "操作数据主键不能为空！");

        GetRequestBuilder getRequestBuilder = elasticsearchTemplate.getClient().prepareGet(index, type, id);

        if (StringUtils.isNotEmpty(fields)) {
            getRequestBuilder.setFetchSource(fields.split(","), null);
        }

        GetResponse getResponse = getRequestBuilder.execute().actionGet();

        return getResponse.getSource();
    }


    /**
     * 批量新增数据
     *
     * @param index    索引名称
     * @param type     索引类型
     * @param dataList 需要新增的数据
     * @param batchSize 每多少条提交一次 取值范围 1 - 7000 默认 5000
     */
    public static void insertBatch(String index, String type, List<Map<String, Object>> dataList, Integer batchSize) {
        Assert.hasLength(index, "操作数据索引不能为空！");
        Assert.hasLength(type, "操作数据类型不能为空！");
        Assert.notNull(dataList, "操作数据列表不能为空！");
        if(batchSize == null || batchSize < 1 || batchSize > 7000) {
            batchSize = 5000;
        }
        BulkRequestBuilder bulkRequest = elasticsearchTemplate.getClient().prepareBulk();
        for (int i = 0; i < dataList.size(); i++) {
            bulkRequest.add(elasticsearchTemplate.getClient().prepareIndex(index, type, (String) dataList.get(i).get("id")).setSource(dataList.get(i)));
            // 每5000条提交一次
            if ((i + 1) % batchSize == 0) {
                BulkResponse bulkItemResponses = bulkRequest.execute().actionGet();
                bulkRequest = elasticsearchTemplate.getClient().prepareBulk();
                logger.debug("已保存: {} 条,执行时间：{} ", batchSize, bulkItemResponses.getTook());
            }
        }
        if (dataList.size() % batchSize != 0) {
            BulkResponse bulkItemResponses = bulkRequest.execute().actionGet();
            logger.debug("保存: {}条,执行时间：{} ", dataList.size() % batchSize, bulkItemResponses.getTook());
        }
    }

    /**
     * 通用保存方法
     * @param sendESObjDto
     * @return
     */
    public static Map<String, Object> universalSave(SendESObjDto sendESObjDto) {
        Assert.notNull(sendESObjDto, "操作数据不能为空！");
        // 获取索引
        String index = sendESObjDto.getIndexName();
        // 判断索引是否存在
        if (!elasticsearchTemplate.indexExists(index)) {
            Boolean flag = createIndex(sendESObjDto);
            if (!flag) {
                throw new CreateIndexException(MessageFormat.format("创建索引出现异常,索引名称：{0}", index));
            }
        }
        // 检查索引是否创建成功
        if (!elasticsearchTemplate.indexExists(index)) {
            throw new CreateIndexException(MessageFormat.format("索引创建失败,索引名称：{0}", index));
        }
        Map<String, Object> resMap = saveOrUpdate(sendESObjDto);
        return resMap;
    }

    /**
     * 复杂条件查询
     *
     * @param commonQueryDto
     * @return
     */
    public static Page universalQuery(CommonQueryDto commonQueryDto) {
        Assert.notNull(commonQueryDto, "操作数据不能为空！");
        if(StringUtils.isEmpty(commonQueryDto.getIndexName()) || !elasticsearchTemplate.indexExists(commonQueryDto.getIndexName())) {
            return new Page();
        }
        Long startTime = null;
        Long endTime = null;
        try {
            if (null != commonQueryDto.getStartTime()) {
                startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(commonQueryDto.getStartTime()).getTime();
            }
            if (null != commonQueryDto.getEndTime()) {
                endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(commonQueryDto.getEndTime()).getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // 设置查询字段
        SearchRequestBuilder searchRequest = elasticsearchTemplate.getClient()
                .prepareSearch(commonQueryDto.getIndexName());
        HighlightBuilder highlightBuilder = null;
        //切割检索字段
        if (!CollectionUtils.isEmpty(commonQueryDto.getHighlightField())) {
            String[] fields = commonQueryDto.getHighlightField().toArray(new String[commonQueryDto.getHighlightField().size()]);
            // 设置高亮,使用默认的highlighter高亮器
            highlightBuilder = createHighlightBuilder(fields);
        }
        //拼接查询Query
        BoolQueryBuilder query = QueryBuilders.boolQuery();

        //查询类型
        BoolQueryBuilder modelTypeBoolQuery = QueryBuilders.boolQuery();
        if (!CollectionUtils.isEmpty(commonQueryDto.getQueryFields())) {
            List<QueryFieldDto> queryFieldDtoList = commonQueryDto.getQueryFields();
            queryFieldDtoList.forEach(queryFieldDto -> {
                if (StringUtils.isNotBlank(queryFieldDto.getAnalyzer())) {
                    modelTypeBoolQuery.must(createQueryBuilder(queryFieldDto.getFieldValue().toString(), queryFieldDto.getAnalyzer(), queryFieldDto.getFieldName()));
                } else {
                    modelTypeBoolQuery.must(QueryBuilders.termQuery(queryFieldDto.getFieldName(), queryFieldDto.getFieldValue()));
                }
                query.must(modelTypeBoolQuery);
            });
        }
        if (null != startTime || null != endTime) {
            query.must(QueryBuilders.rangeQuery(SysConfig.FIELD_MODIFIED).from(startTime).to(endTime).includeLower(true).includeUpper(true));
        }
        Optional.ofNullable(commonQueryDto.getRangeQueryDtoList())
                .orElseGet(Lists::newArrayList)
                .stream()
                .forEach(field -> query.must(QueryBuilders.rangeQuery(field.getFieldName()).from(field.getStartTime()).to(field.getEndTime()).includeLower(true).includeUpper(true)));

        if (!CollectionUtils.isEmpty(commonQueryDto.getSortFieldDtoList())) {
            commonQueryDto.getSortFieldDtoList()
                    .forEach(sortFieldDto -> searchRequest.addSort(sortFieldDto.getFieldName(), StringUtils.equals(sortFieldDto.getSortType(), SysConfig.SORT_TYPE.ASC) ? SortOrder.ASC : SortOrder.DESC));
        }
        SearchResponse response = searchRequest
                .setQuery(query)
                .highlighter(highlightBuilder)
                .setFrom((commonQueryDto.getCurrentPageNo() - 1) * commonQueryDto.getPageSize())
                .setSize(commonQueryDto.getPageSize()) // 设置一次返回的文档数量，最大值：10000
                .get();
        //返回搜索结果
        SearchHits hits = response.getHits();
        Long totalCount = hits.getTotalHits();
        return new Page(commonQueryDto.getCurrentPageNo(), commonQueryDto.getPageSize(), totalCount.intValue(), getHitList(hits));
    }

    /**
     * @param @param  fieldNames
     * @param @return
     * @return HighlightBuilder
     * @throws
     * @Description: 构造高亮器
     */
    public static HighlightBuilder createHighlightBuilder(String... fieldNames) {
        // 设置高亮,使用默认的highlighter高亮器
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                // .field("productName")
                .preTags("<span style='color:red'>")
                .postTags("</span>");

        // 设置高亮字段
        for (String fieldName : fieldNames) highlightBuilder.field(fieldName);
        return highlightBuilder;
    }

    /**
     * @param @param  hits
     * @param @return
     * @return List<Map   <   String   ,   Object>>
     * @throws
     * @Description: 处理高亮结果
     */
    public static List<Map<String, Object>> getHitList(SearchHits hits) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        for (SearchHit searchHit : hits) {
            map = new HashMap<>();
            // 处理源数据
            map.put("source", searchHit.getSourceAsMap());
            // 处理高亮数据
            Map<String, Object> hitMap = new HashMap<>();
            searchHit.getHighlightFields().forEach((k, v) -> {
                String hight = "";
                for (Text text : v.getFragments()) hight += text.string();
                hitMap.put(v.getName(), hight);
            });
            map.put("highlight", hitMap);
            list.add(map);
        }
        return list;
    }

    /**
     * 构造查询条件
     *
     * @param keyword
     * @param analyzer
     * @param fieldNames
     * @return
     */
    public static QueryBuilder createQueryBuilder(String keyword, String analyzer, String... fieldNames) {
        // 构造查询条件,使用标准分词器.
        return QueryBuilders.multiMatchQuery(keyword, fieldNames)   // matchQuery(),单字段搜索
                .analyzer(analyzer)
                .operator(Operator.OR);
    }

    /**
     * 构造新增/更新条件新增/更新
     *
     * @param sendESObjDto
     * @return
     */
    public static Map<String, Object> saveOrUpdate(SendESObjDto sendESObjDto) {
        Assert.notNull(sendESObjDto, "操作数据不能为空！");
        List<FieldDetailDto> fieldDtoList = sendESObjDto.getFieldDtoList();
        try {
            XContentBuilder mappingBuilder = XContentFactory.jsonBuilder();
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            final List<String> primaryKey = Lists.newArrayList("oid");
            if (!CollectionUtils.isEmpty(fieldDtoList)) {
                mappingBuilder.startObject();
                fieldDtoList.forEach(fieldDto -> {
                    try {
                        if (!StringUtils.equalsAnyIgnoreCase(fieldDto.getFieldName(), PrimaryKeyEnum.id.name(), PrimaryKeyEnum.oid.name())) {
                            mappingBuilder.field(fieldDto.getFieldName(), fieldDto.getFieldValue());
                        } else if (fieldDto.getFieldValue() != null && StringUtils.isNotBlank(String.valueOf(fieldDto.getFieldValue()))) {
                            primaryKey.set(0, fieldDto.getFieldName());
                            sendESObjDto.setId(fieldDto.getFieldValue().toString());
                        }
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                        throw new RuntimeException(e.getMessage());
                    }
                });
                mappingBuilder.field(SysConfig.FIELD_MODIFIED, new Date().getTime());
                if (StringUtils.isBlank(sendESObjDto.getId())) {
                    mappingBuilder.field(primaryKey.get(0), uuid);
                } else {
                    mappingBuilder.field(primaryKey.get(0), sendESObjDto.getId());
                }
                mappingBuilder.endObject();
            }
            Map<String, Object> resMap = Maps.newHashMap();
            String _index;
            String _type;
            String _id;
            long _version;
            RestStatus status;
            String result;
            if (StringUtils.isBlank(sendESObjDto.getId())) {

                IndexResponse response = elasticsearchTemplate.getClient()
                        .prepareIndex(sendESObjDto.getIndexName(), sendESObjDto.getType())
                        .setId(uuid)
                        .setSource(mappingBuilder).get();
                _index = response.getIndex();
                _type = response.getType();
                _id = response.getId();
                _version = response.getVersion();
                status = response.status();
                result = response.getResult().toString();
                logger.info(MessageFormat.format("插入操作（oid不存在）索引名称：{0}，类型：{1}，文档ID：{2}，版本：{3}，返回操作状态：{4}，操作结果：{5}", _index, _type, _id, _version, status, result));
            } else if (!existId(sendESObjDto.getIndexName(), sendESObjDto.getType(), sendESObjDto.getId())) {
                IndexResponse response = elasticsearchTemplate.getClient()
                        .prepareIndex(sendESObjDto.getIndexName(), sendESObjDto.getType())
                        .setId(sendESObjDto.getId())
                        .setSource(mappingBuilder).get();
                _index = response.getIndex();
                _type = response.getType();
                _id = response.getId();
                _version = response.getVersion();
                status = response.status();
                result = response.getResult().toString();
                logger.info(MessageFormat.format("插入操作（oid存在）索引名称：{0}，类型：{1}，文档ID：{2}，版本：{3}，返回操作状态：{4}，操作结果：{5}", _index, _type, _id, _version, status, result));
            } else {
                UpdateResponse updateResponse = elasticsearchTemplate.getClient()
                        .prepareUpdate(sendESObjDto.getIndexName(), sendESObjDto.getType(), sendESObjDto.getId())
                        .setDoc(mappingBuilder).get();
                _index = updateResponse.getIndex();
                _type = updateResponse.getType();
                _id = updateResponse.getId();
                _version = updateResponse.getVersion();
                status = updateResponse.status();
                result = updateResponse.getResult().toString();
                logger.info(MessageFormat.format("更新操作（oid存在）索引名称：{0}，类型：{1}，文档ID：{2}，版本：{3}，返回操作状态：{4}，操作结果：{5}", _index, _type, _id, _version, status, result));
            }
            //获取索引
            resMap.put("index", _index);
            //获取类型
            resMap.put("type", _type);
            // 文档ID
            resMap.put("id", _id);
            // 版本
            resMap.put("version", _version);
            // 返回的操作状态
            resMap.put("status", status);
            // 返回的操作结果
            resMap.put("result", result);
            return resMap;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 判断id是否存在
     *
     * @param indexName 索引名
     * @param indexType 索引类型
     * @param id        id
     * @return true存在 false不存在
     */
    private static boolean existId(String indexName, String indexType, String id) {
        Assert.hasLength(indexName, "操作数据索引不能为空！");
        Assert.hasLength(indexType, "操作数据类型不能为空！");
        Assert.hasLength(id, "操作数据主键不能为空！");
        boolean res = elasticsearchTemplate.indexExists(indexName) && CollectionUtils.isEmpty(searchDataById(indexName, indexType, id, null));
        logger.info(MessageFormat.format("type为{0}的索引{1}中{2}id为{3}的数据！", indexName, indexType, res ? "不存在" : "存在", id));
        return !res;
    }

    /**
     * 创建索引
     *
     * @param sendESObjDto
     * @return
     */
    public static boolean createIndex(SendESObjDto sendESObjDto) {
        Assert.notNull(sendESObjDto, "创建索引对象不能为空！");
        List<FieldDetailDto> fieldDtoList = sendESObjDto.getFieldDtoList();
        try {
            XContentBuilder mappingBuilder = XContentFactory.jsonBuilder();
            if (!CollectionUtils.isEmpty(fieldDtoList)) {
                mappingBuilder.startObject()
                        .startObject(sendESObjDto.getType())
//                        .startObject("_id")
//                            .field("path", "oid")
//                        .endObject()
                        .startObject("properties");
                fieldDtoList.forEach(fieldDto -> {
                    try {
                        mappingBuilder.startObject(fieldDto.getFieldName());
                        if (StringUtils.isNotEmpty(fieldDto.getFieldType())) {
                            mappingBuilder.field("type", fieldDto.getFieldType());
                        }
                        if (StringUtils.isNotEmpty(fieldDto.getAnalyzer())) {
                            mappingBuilder.field("analyzer", fieldDto.getAnalyzer());
                        }
                        if (StringUtils.isNotEmpty(fieldDto.getSearch_analyzer())) {
                            mappingBuilder.field("search_analyzer", fieldDto.getSearch_analyzer());
                        }
                        if (null != fieldDto.getFielddata() && fieldDto.getFielddata()) {
                            mappingBuilder.field("fielddata", fieldDto.getFielddata());
                        }
                        mappingBuilder.endObject();
                    } catch (IOException e) {
                        logger.error(e.toString(), e);
                        e.printStackTrace();
                    }
                });
                mappingBuilder.endObject()
                        .endObject()
                        .endObject();
            }
            XContentBuilder settingBuilder = XContentFactory.jsonBuilder();
            settingBuilder.startObject();
            if (null != sendESObjDto.getReplicasNum() && sendESObjDto.getReplicasNum() != 0) {
                settingBuilder.field("number_of_replicas", sendESObjDto.getReplicasNum());
            }
            if (null != sendESObjDto.getShardsNum() && sendESObjDto.getShardsNum() != 0) {
                settingBuilder.field("number_of_shards", sendESObjDto.getShardsNum());
            }
            settingBuilder.endObject();
            elasticsearchTemplate.getClient()
                    .admin()
                    .indices()
                    .prepareCreate(sendESObjDto.getIndexName())
                    .setSettings(settingBuilder)
                    .execute()
                    .actionGet();
            try{
                elasticsearchTemplate.getClient()
                        .admin()
                        .indices()
                        .preparePutMapping(sendESObjDto.getIndexName())
                        .setType(sendESObjDto.getType())
                        .setSource(mappingBuilder)
                        .execute()
                        .actionGet();
            } catch (Exception e) {
                logger.warn("es7中不赞成使用映射类型!");
            }

        } catch (IOException e) {
            logger.error(e.toString(), e);
            return false;
        }
        return true;
    }

    @Autowired
    public void setElasticsearchTemplate(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

}