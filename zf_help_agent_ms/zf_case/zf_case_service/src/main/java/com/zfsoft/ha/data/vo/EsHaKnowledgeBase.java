package com.zfsoft.ha.data.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * es 知识库实体类
 */
@Data
@ToString
//@Document(indexName = "hakb", type = "product",shards = 1,replicas = 0)
public class EsHaKnowledgeBase implements Serializable {
    private static final long serialVersionUID = -1L;
    private Long serviceId;

    private String  serviceOid;

    private String name;

    private String titleName;

    private List<Map<String,String>> materialList;

    private String keyword;

}
