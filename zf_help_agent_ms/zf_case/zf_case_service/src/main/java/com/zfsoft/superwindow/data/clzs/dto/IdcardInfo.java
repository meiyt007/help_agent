package com.zfsoft.superwindow.data.clzs.dto;

import com.zfsoft.rest.annotation.CataWordBind;
import lombok.Data;

/**
 * 身份证信息
 *
 * @author chenbw
 * @date 2019年6月17日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@Data
public class IdcardInfo {
    /** 姓名 */
    @CataWordBind(word = "姓名")
    private String name;

    /** 性别 */
    @CataWordBind(word = "性别")
    private String sex;

    /** 民族 */
    @CataWordBind(word = "民族")
    private String nation;

    /** 出生 */
    @CataWordBind(word = "出生")
    private String birth;

    /** 住址 */
    @CataWordBind(word = "住址")
    private String address;

    /** 公民身份号码 */
    @CataWordBind(word = "公民身份号码")
    private String number;

    /** 签发机关 */
    @CataWordBind(word = "签发机关")
    private String signDepartment;

    /** 签发日期 */
    @CataWordBind(word = "签发日期")
    private String signDate;

    /** 失效日期 */
    @CataWordBind(word = "失效日期")
    private String endDate;


}
