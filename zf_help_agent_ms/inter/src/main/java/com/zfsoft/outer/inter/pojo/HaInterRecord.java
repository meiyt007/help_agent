package com.zfsoft.outer.inter.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 帮代办外部接口调用记录信息
 */
@Data
public class HaInterRecord implements Serializable {

    public HaInterRecord() {
    }

    /**
     * @param name         接口名称
     * @param url          接口地址
     * @param sourceIp     调用来源IP地址
     * @param method       方法
     * @param param        调用参数
     * @param resultStatus 结果状态;1-成功，2-失败
     * @param result       结果只
     * @param totalTime    用时
     */
    public HaInterRecord(String name, String url, String sourceIp, String method, String param, String resultStatus, String result, Long totalTime) {
        this.name = name;
        this.url = url;
        this.sourceIp = sourceIp;
        this.method = method;
        this.param = param;
        this.resultStatus = resultStatus;
        this.result = result;
        this.totalTime = totalTime;
    }

    /**
     * 主键
     */
    private Long id;
    /**
     * 接口名称
     */
    private String name;
    /**
     * 地址
     */
    private String url;
    /**
     * 来源IP地址
     */
    private String sourceIp;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 调用参数
     */
    private String param;
    /**
     * 结果状态;1-成功，2-失败
     */
    private String resultStatus;
    /**
     * 执行结果
     */
    private String result;
    /**
     * 总用时;单位：毫秒
     */
    private Long totalTime;
    /**
     * 创建时间
     */
    private Date createDate;

}
