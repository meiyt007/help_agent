package com.zfsoft.outer.inter.pojo;

import lombok.Data;

@Data
public class HaPolicyPushVo {


    /**
     * 主题
     */
    private String title;

    /**
     * 姓名
     */
    private String name;

    /**
     * 受理部门
     */
    private String organName;

    /**
     * 链接名称
     */
    private String linkName;

    /**
     * 政策链接
     */
    private String policyLink;

    /**
     * 与该政策相关的涉企办事服务
     */
    private String serviceName;

    /**
     * 线上申报链接
     */
    private String declareLink;

    /**
     * 政策体检
     */
    private String experienceName;

    /**
     * 政策体检链接
     */
    private String experienceLink;


}