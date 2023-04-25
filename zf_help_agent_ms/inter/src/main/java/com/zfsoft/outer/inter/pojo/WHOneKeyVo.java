package com.zfsoft.outer.inter.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //一键推送请求实体类
 * @Author: Wangyh
 * @Date: 2022/11/10 13:26
 */
@Data
@ToString
public class WHOneKeyVo {
    /**
     * case类型。1-咨询(办事指南)，2-材料准备(办件)
     */
    private String type;
    /**
     * 示例值：
     * xxx
     * 办件ID
     */
    private String caseOid;
    /**
     * 身份证
     */
    private String idcard;
    /**
     * 示例值：
     * 李默
     * 和身份证相关的人名
     */
    private String name;
    /**
     * 公司名
     */
    private String companyName;
    /**
     * 公司代码
     */
    private String companyCode;
    /**
     * 示例值：
     * 2022-09-02T12:05:06
     * 时间
     */
    private String dateTime;




}
