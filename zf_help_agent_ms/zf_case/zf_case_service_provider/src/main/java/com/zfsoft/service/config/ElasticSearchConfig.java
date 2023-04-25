package com.zfsoft.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author hutao
 * @date 2022/5/18
 * ES配置文件类
 */
@Configuration
public class ElasticSearchConfig {

    /** 连接方式 http tcp */
    private static String scheme;
    /** IP地址 */
    private static String host;
    /** 端口 */
    private static Integer port;
    /** 插入分词器 */
    private static String analyzer;
    /** 查询分词器 */
    private static String searchAnalyzer;


    @Value("${elasticsearch.scheme}")
    public void setScheme(String scheme) {
        ElasticSearchConfig.scheme = scheme;
    }
    public static String getScheme() {
        return ElasticSearchConfig.scheme;
    }

    @Value("${elasticsearch.host}")
    public void setHost(String host) {
        ElasticSearchConfig.host = host;
    }
    public static String getHost() {
        return ElasticSearchConfig.host;
    }

    @Value("${elasticsearch.port}")
    public void setPort(Integer port) { ElasticSearchConfig.port = port; }
    public static Integer getPort() {
        return ElasticSearchConfig.port;
    }

    @Value("${elasticsearch.analyzer}")
    public void setAnalyzer(String analyzer) {
        ElasticSearchConfig.analyzer = analyzer;
    }
    public static String getAnalyzer() {
        return ElasticSearchConfig.analyzer;
    }

    @Value("${elasticsearch.search_analyzer}")
    public void setSearchAnalyzer(String searchAnalyzer) {
        ElasticSearchConfig.searchAnalyzer = searchAnalyzer;
    }
    public static String getSearchAnalyzer() {
        return ElasticSearchConfig.searchAnalyzer;
    }

}
