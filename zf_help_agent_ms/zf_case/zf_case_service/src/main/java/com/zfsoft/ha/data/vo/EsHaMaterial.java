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
public class EsHaMaterial implements Serializable {
    private static final long serialVersionUID = -1L;
    //必须有 id,这里的 id 是全局唯一的标识，等同于 es 中的"_id"
//    private Long id;//商品唯一标识
    /**
     * type : 字段数据类型
     * analyzer : 分词器类型
     * index : 是否索引(默认:true)
     * Keyword : 短语,不进行分词
     */

    /**
     * 材料名称
     */
    private String MaterialName;
    /**
     * 材料地址
     */
    private String MaterialUrl;
    /**
     * 事项主键
     */
    private String serviceOid;
    /**
     * 事项名称
     */
    private String serviceName;

//    private String keyword;

}
