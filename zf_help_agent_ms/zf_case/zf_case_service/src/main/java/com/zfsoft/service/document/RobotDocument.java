package com.zfsoft.service.document;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hutao
 * @date 2022/5/16
 * 问答机器人es数据结构
 */
@Data
@ToString
public class RobotDocument implements Serializable {
    private static final long serialVersionUID = 2841590921129349447L;

    /** 唯一主键 1-serviceOid，2-questionOid */
    private String docId;
    /** 类型 1-办事指南，2-常见问题 */
    private String modelType;

    /** 事项名称 */
    private String serviceName;
    /** 区划oid */
    private String districtOid;
    /** 区划path */
    private String districtPath;
    /** 区划名称 */
    private String districtName;
    /** 办理部门名称 */
    private String organName;
    /** 承诺期限 */
    private String promiseLimit;
    /** 咨询电话 */
    private String consultTel;
    /** 办理地址 */
    private String handleLocation;
    /** 公交路线 */
    private String busRoute;
    /** 事项材料 */
    private String serviceMaterial;
    /** 跳转地址 */
    private String jumpUrl;

    /** 事项oid */
    private String serviceOid;
    /** 问题标题 */
    private String askTitle;
    /** 问题答案 */
    private String askAnswer;

    /** 创建时间 */
    private Date createDate;
    /** 更新时间 */
    private Date updateDate;

}
