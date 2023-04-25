package com.zfsoft.superwindow.data.clzs;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: qiaol
 * @Description: 内部接口
 * @Date: 2022/5/17 16:40
 */
@Data
public class InterApi {

    /**
     * 主键
     */
    private Long id;
    /**
     * 业务主键
     */
    private String oid;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    //删除标识
    private Integer deleteFlag;
    //创建时间
    private Date createDate;
    //修改时间
    private Date modifyDate;

    /**
     * 接口响应值
     */
    private List<InterApiResponse> responseList;

    private Integer pageNum;

    private Integer pageSize;

    /**
     * 接口中台所属服务
     */
    private String serverOid;

    /**
     * 接口中台接口
     */
    private String interfaceOid;
}
