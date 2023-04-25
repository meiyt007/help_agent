package com.zfsoft.ha.xindianResponse;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //样表响应实体类
 * @Author: Wangyh
 * @Date: 2023/3/17 15:53
 */
@Data
@ToString
public class MaterialexampletableVo {
    /**
     * 样表格文件主键
     */
    private String attachguid;
    /**
     *  样表格文件名称
     */
    private String attachfilename;
    /**
     * 文件大小
     */
    private String attachlength;
    /**
     * 附件地址
     */
    private String url;
    /**
     * 文件类型
     */
    private String contenttype;
    /**
     * 材料标识
     */
    private String cliengguid;
}
