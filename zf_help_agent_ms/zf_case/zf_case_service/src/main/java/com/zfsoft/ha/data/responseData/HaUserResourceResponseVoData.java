package com.zfsoft.ha.data.responseData;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户资源请求vo
 * @author zhaobf
 * @version 1.0
 * @date 2023/4/12 10:36
 */
@Data
public class HaUserResourceResponseVoData {
    /**
     * 资源id
     */
    private Long id;
    /**
     * 工作人员编号
     */
    private Long workUserId;
    /**
     * 资源类型;1-文件夹，2-文件
     */
    private String type;
    /**
     * 资源名称;文件夹名称或文件名称
     */
    private String name;

    /**
     * 父级主键;为null时，为父级
     */
    private Long parentId;
    /**
     * 资源信息;文件编号
     */
    private String resourceInfo;

    /**
     * 预览地址
     */
    private String fastdfsUploadUrl;
    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 附件名称
     */
    private String fileName;

    /**
     * 资源
     */
    private List<HaUserResourceResponseVoData> haUserResourceResponseVoData;
}
