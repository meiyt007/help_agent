package com.zfsoft.superwindow.data.clzs.dto;

import com.zfsoft.rest.annotation.CataWordBind;
import lombok.Data;

/**
 * 营业执照信息对象
 *
 * @author cbc
 * @date 2019年3月14日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@Data
public class BusinessLiceInfo {
    /**
     * 单位名称
     */
    @CataWordBind(word = "单位名称")
    private String companyName;

    /**
     * 统一社会信用代码
     */
    @CataWordBind(word = "统一社会信用代码")
    private String socialCode;

    /**
     * 地址
     */
    @CataWordBind(word = "地址")
    private String address;

    /**
     * 成立日期
     */
    @CataWordBind(word = "成立日期")
    private String establishDate;

    /**
     * 有效期
     */
    @CataWordBind(word = "有效期")
    private String effectiveDate;

    /**
     * 法人
     */
    @CataWordBind(word = "法人")
    private String lawRepresentative;

    /**
     * 注册资本
     */
    @CataWordBind(word = "注册资本")
    private String registeredCapital;

    /**
     * 类型
     */
    @CataWordBind(word = "类型")
    private String type;

    /**
     * 组成形式
     */
    @CataWordBind(word = "组成形式")
    private String compositionForm;

    /**
     * 证件编号
     */
    @CataWordBind(word = "证件编号")
    private String idNumber;


}
