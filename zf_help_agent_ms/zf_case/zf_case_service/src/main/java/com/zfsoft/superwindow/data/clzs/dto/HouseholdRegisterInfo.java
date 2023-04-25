package com.zfsoft.superwindow.data.clzs.dto;

import com.zfsoft.rest.annotation.CataWordBind;
import lombok.Data;

/**
 * 户口本
 *
 * @author chenbw
 * @date 2019年6月17日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@Data
public class HouseholdRegisterInfo {
    /** 姓名 */
    @CataWordBind(word = "姓名")
    private String name;

    /** 姓名 */
    @CataWordBind(word = "性别")
    private String sex;

    /** 民族 */
    @CataWordBind(word = "民族")
    private String nation;

    /** 出生地 */
    @CataWordBind(word = "出生地")
    private String birthaddress;

    /** 生日 */
    @CataWordBind(word = "生日")
    private String birthday;

    /** 身份证号 */
    @CataWordBind(word = "身份证号")
    private String cardno;

    /** 与户主关系 */
    @CataWordBind(word = "与户主关系")
    private String relationship;

}
