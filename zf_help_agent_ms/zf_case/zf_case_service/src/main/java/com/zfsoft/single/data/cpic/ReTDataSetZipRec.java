package com.zfsoft.single.data.cpic;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/7/23 10:40
 */
@Data
@ToString
public class ReTDataSetZipRec {
    /**
     * 主键
     */
    private Long id;
    /**
     * 事项ids(事项id逗号隔开)
     */
    private String serviceIds;
    /**
     * zip文件地址
     */
    private String zipFileLocation;
    /**
     * 打包时间
     */
    private Date packingTime;
    /**
     * 发布时间
     */
    private Date publishTime;
    /**
     * 是否删除(0否，1是)
     */
    private Integer deleteStatus;
    /**
     * 打包人
     */
    private String createBy;
    /**
     * 是否发布(0否，1是)
     */
    private Integer isPublish;
    /**
     * 打包说明
     */
    private String publishExplain;
}
