package com.zfsoft.ocr.data.pojo.ocr;

import com.zfsoft.ocr.data.pojo.BaseResponse;
import com.zfsoft.ocr.data.pojo.annotation.CataWordBind;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * ocr户口本识别响应信息
 * 
 * @author chenbw
 * @date 2019年6月26日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "ocr户口本识别响应信息")
public class HouseholdRegisterResponse extends BaseResponse {

    /** 姓名 */
    @CataWordBind(word = "姓名")
    @ApiModelProperty(value = "姓名")
    private String name;

    /** 姓名 */
    @CataWordBind(word = "性别")
    @ApiModelProperty(value = "性别")
    private String sex;

    /** 民族 */
    @CataWordBind(word = "民族")
    @ApiModelProperty(value = "民族")
    private String nation;

    /** 出生地 */
    @CataWordBind(word = "出生地")
    @ApiModelProperty(value = "出生地")
    private String birthaddress;

    /** 生日 */
    @CataWordBind(word = "生日")
    @ApiModelProperty(value = "生日")
    private String birthday;

    /** 身份证号 */
    @CataWordBind(word = "身份证号")
    @ApiModelProperty(value = "身份证号")
    private String cardno;

    /** 与户主关系 */
    @CataWordBind(word = "与户主关系")
    @ApiModelProperty(value = "与户主关系")
    private String relationship;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthaddress() {
        return birthaddress;
    }

    public void setBirthaddress(String birthaddress) {
        this.birthaddress = birthaddress;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
