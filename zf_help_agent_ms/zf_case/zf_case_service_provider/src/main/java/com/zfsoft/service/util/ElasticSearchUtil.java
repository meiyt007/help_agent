package com.zfsoft.service.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.ha.data.vo.EsHaKnowledgeBase;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.config.ElasticSearchConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.poi.ss.formula.functions.T;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.xcontent.XContentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hutao
 * @date 2022/5/10
 * ES查询工具类
 */
@Slf4j
public class ElasticSearchUtil {

    private static final RestHighLevelClient CLIENT;

    static {
        CLIENT = new RestHighLevelClient(RestClient.builder(new HttpHost(
                ElasticSearchConfig.getHost(),
                ElasticSearchConfig.getPort(),
                ElasticSearchConfig.getScheme())));
    }

    /**
     * 创建索引
     * @param indexName 索引名称
     */
    public static void createIndex(String indexName) {
        try {
            if (isExistsIndex(indexName)) {
                return;
            }
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);

            Settings.Builder settings = Settings.builder();
            if (StrUtil.isNotEmpty(ElasticSearchConfig.getAnalyzer())) {
                settings.put("analysis.analyzer.default.type", ElasticSearchConfig.getAnalyzer());
            }
            if (StrUtil.isNotEmpty(ElasticSearchConfig.getSearchAnalyzer())) {
                settings.put("analysis.analyzer.default.type", ElasticSearchConfig.getSearchAnalyzer());
            }

            createIndexRequest.settings(settings);
            CLIENT.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (Exception ex) {
            log.error("创建索引信息出现异常，索引名称：{}。",indexName);
            ex.printStackTrace();
        }
    }

    /**
     * 判断是否存在索引
     * @param indexName 索引名称
     * @return true/false
     */
    public static boolean isExistsIndex(String indexName) {
        boolean exists = false;
        try {
            exists = CLIENT.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
        } catch (Exception ex) {
            log.error("判断索引是否存在出现异常，索引名称：{}。",indexName);
            ex.printStackTrace();
        }
        return exists;
    }

    /**
     * 判断是否存在索引
     * @param indexName 索引名称
     * @return true/false
     */
    public static boolean isExistsIndexExce(String indexName) {
        boolean exists = false;
        try {
            exists = CLIENT.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
        } catch (Exception ex) {
            log.error("判断索引是否存在出现异常，索引名称：{}。",indexName);
            ex.printStackTrace();
            throw new RuntimeException("判断索引是否存在出现异常，索引名称："+indexName);
        }
        return exists;
    }


    /**
     * 删除索引
     * @param indexName 索引名称
     */
    public static void deleteIndex(String indexName) {
        try {
            if (isExistsIndex(indexName)) {
                CLIENT.indices().delete(new DeleteIndexRequest(indexName), RequestOptions.DEFAULT);
            }
        } catch (Exception ex) {
            log.error("删除索引信息出现异常，索引名称：{}。",indexName);
            ex.printStackTrace();
        }
    }

    /**
     * 删除索引
     * @param indexName 索引名称
     */
    public static void deleteIndexExce(String indexName) {
        try {
            if (isExistsIndex(indexName)) {
                CLIENT.indices().delete(new DeleteIndexRequest(indexName), RequestOptions.DEFAULT);
            }
        } catch (Exception ex) {
            log.error("删除索引信息出现异常，索引名称：{}。",indexName);
            ex.printStackTrace();
            throw new RuntimeException("删除索引信息出现异常，索引名称："+indexName);
        }
    }

    /**
     * 插入数据
     * @param indexName 索引名称
     * @param id 主键
     * @param jsonObject 数据json字符串
     */
    public static void insertData(String indexName, String id, String jsonObject) {
        try {
            createIndex(indexName);
            IndexRequest request= new IndexRequest(indexName)
                    .id(id).source(jsonObject, XContentType.JSON);
            CLIENT.index(request, RequestOptions.DEFAULT);
        } catch (Exception ex) {
            log.error("插入数据出现异常，索引名称：{}。",indexName);
            ex.printStackTrace();
        }
    }

    /**
     * 批量插入数据
     * @param indexName 索引名称
     * @param idName 指定主键名称
     * @param jsonArray 数据json字符串
     */
    public static void insertDataBatch(String indexName, String idName, String jsonArray) {
        try {
            createIndex(indexName);
            BulkRequest bulkRequest  = new BulkRequest();
            JSONArray array = JSONArray.parseArray(jsonArray);
            for (int i = 0; i < array.size(); i++) {
                JSONObject jo = array.getJSONObject(i);
                bulkRequest.add(new IndexRequest(indexName)
                        .id(jo.getString(idName)).source(jo.toJSONString(), XContentType.JSON));
            }
            CLIENT.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (Exception ex) {
            log.error("批量插入数据出现异常，索引名称：{}。",indexName);
            ex.printStackTrace();
        }
    }

    /**
     * 根据id查询数据
     * @param clazz 泛型类
     * @param indexName 索引名称
     * @param id 主键
     * @return 对象信息
     * @param <T> 泛型
     */
    public static <T> T searchDataById(Class<T> clazz, String indexName, String id) {
        try {
            GetRequest request = new GetRequest(indexName).id(id);
            GetResponse response = CLIENT.get(request, RequestOptions.DEFAULT);
            Map<String, Object> source = response.getSourceAsMap();
            return JSONObject.parseObject(JSON.toJSONString(source), clazz);
        } catch (Exception ex) {
            log.error("根据id查询数据出现异常，索引名称：{}，id：{}。", indexName, id);
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 查询分页数据
     * @param clazz 泛型类
     * @param queryBuilder 查询构建器
     * @param highlightBuilder 高亮构建器
     * @param pageNumber 页码
     * @param pageSize 每页大小
     * @param indexNames 索引名称
     * @return 分页信息
     * @param <T> 泛型
     */
    public static <T> PageResult<T> searchDataPage(Class<T> clazz, QueryBuilder queryBuilder, HighlightBuilder highlightBuilder,
                                                   Integer pageNumber, Integer pageSize, String... indexNames) {
        PageResult<T> pageResult = new PageResult<>(pageNumber, pageSize, 0);
        try {
            //创建查询请求
            SearchRequest request = new SearchRequest(indexNames);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                    .query(queryBuilder)
                    .from((pageNumber - 1) * pageSize)
                    .size(pageSize)
                    .highlighter(highlightBuilder);
            request.source(searchSourceBuilder);
            SearchResponse response = CLIENT.search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            int total = (int)hits.getTotalHits().value;
            List<T> list = new ArrayList<>();
            for (SearchHit hit : hits) {
                Map<String, Object> source = hit.getSourceAsMap();
                // 高亮
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
                    String key = entry.getKey();
                    HighlightField h = highlightFields.get(key);
                    String fragmentString = h.fragments()[0].string();
                    source.put(key, fragmentString);
                }
                T entry = JSONObject.parseObject(JSON.toJSONString(source), clazz);
                list.add(entry);
            }
            pageResult.setTotal(total);
            pageResult.setData(list);
        } catch (Exception ex) {
            log.error("查询分页数据出现异常，索引名称：{}。", (Object) indexNames);
            ex.printStackTrace();
        }
        return pageResult;
    }

    /**
     * 查询数据数量
     * @param queryBuilder 查询构建器
     * @param indexNames 索引名称
     * @return 数量
     */
    public static int searchDataCount(QueryBuilder queryBuilder, String... indexNames) {
        try {
            CountRequest request = new CountRequest(indexNames).query(queryBuilder);
            return (int)CLIENT.count(request, RequestOptions.DEFAULT).getCount();
        } catch (Exception ex) {
            log.error("查询数据数量出现异常，索引名称：{}。", (Object) indexNames);
            ex.printStackTrace();
            return 0;
        }
    }

    public static <T> List<T> searchDataList(Class<T> clazz, QueryBuilder queryBuilder, HighlightBuilder highlightBuilder,
                                        String... indexNames) {

        List<T> list = new ArrayList<>();
        try {
            //创建查询请求
            SearchRequest request = new SearchRequest(indexNames);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                    .query(queryBuilder)
//                    .from((pageNumber - 1) * pageSize)
                    .size(20)
                    .highlighter(highlightBuilder);
            request.source(searchSourceBuilder);
            SearchResponse response = CLIENT.search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            int total = (int)hits.getTotalHits().value;
//            List<T> list = new ArrayList<>();
            for (SearchHit hit : hits) {
                Map<String, Object> source = hit.getSourceAsMap();
                // 高亮
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
                    String key = entry.getKey();
                    HighlightField h = highlightFields.get(key);
                    String fragmentString = h.fragments()[0].string();
                    source.put(key, fragmentString);
                }
                T entry = JSONObject.parseObject(JSON.toJSONString(source), clazz);
                list.add(entry);
            }
        } catch (Exception ex) {
            log.error("查询分页数据出现异常，索引名称：{}。", (Object) indexNames);
            ex.printStackTrace();
        }
        return list;
    }

}
