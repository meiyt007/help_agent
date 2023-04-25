package com.zfsoft.ha.data.requestData;

import lombok.Data;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/11/29 16:05
 */
@Data
public class CataloguePageRequestData {
    /**
     * 目录名称
     */
    private String cateGoryName;
    /**
     * 展示标识
     */
    private Short showFlag;
    /**
     * 父级Id
     */
    private Long parentId;

    private Integer pageNum;

    private Integer pageSize;
}
