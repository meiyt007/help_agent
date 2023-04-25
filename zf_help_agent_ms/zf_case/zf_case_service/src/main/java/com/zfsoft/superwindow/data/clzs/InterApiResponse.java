package com.zfsoft.superwindow.data.clzs;

import lombok.Data;

import java.util.Date;

/**
 * @Author: qiaol
 * @Description: 内部接口响应
 * @Date: 2022/5/17 16:40
 */
@Data
public class InterApiResponse {

    /**
     * 主键
     */
    private Long id;
    /**
     * 业务主键
     */
    private String oid;
    /**
     * 内部接口id
     */
    private Long interApiId;
    /**
     * 编码
     */
    private String responseCode;
    /**
     * 名称
     */
    private String responseName;
    //删除标识
    private Integer deleteFlag;
    //创建时间
    private Date createDate;
    //修改时间
    private Date modifyDate;


}
