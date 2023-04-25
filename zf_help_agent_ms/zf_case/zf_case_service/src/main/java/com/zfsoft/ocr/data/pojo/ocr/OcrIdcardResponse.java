package com.zfsoft.ocr.data.pojo.ocr;

import com.zfsoft.ocr.data.pojo.BaseResponse;
import com.zfsoft.ocr.data.pojo.annotation.CataWordBind;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * ocr身份证识别，响应信息
 * 
 * @author chenbw
 * @date 2019年6月25日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "ocr身份证识别，响应信息")
public class OcrIdcardResponse extends BaseResponse {

    /** 姓名 */
    @CataWordBind(word = "姓名")
    @ApiModelProperty(value = "姓名")
    private String name;

    /** 姓名-区块位置Json*/
    @CataWordBind(word = "姓名-区块位置Json")
    @ApiModelProperty(value = "姓名-区块位置Json")
    private String nameLocationJson;

    /** 性别 */
    @CataWordBind(word = "性别")
    @ApiModelProperty(value = "性别")
    private String sex;

    /** 性别-区块位置Json*/
    @CataWordBind(word = "性别-区块位置Json")
    @ApiModelProperty(value = "性别-区块位置Json")
    private String sexLocationJson;

    /** 民族 */
    @CataWordBind(word = "民族")
    @ApiModelProperty(value = "民族")
    private String nation;

    /** 民族-区块位置Json*/
    @CataWordBind(word = "民族-区块位置Json")
    @ApiModelProperty(value = "民族-区块位置Json")
    private String nationLocationJson;

    /** 出生 */
    @CataWordBind(word = "出生")
    @ApiModelProperty(value = "出生")
    private String birth;

    /** 出生-区块位置Json*/
    @CataWordBind(word = "出生-区块位置Json")
    @ApiModelProperty(value = "出生-区块位置Json")
    private String birthLocationJson;

    /** 住址 */
    @CataWordBind(word = "住址")
    @ApiModelProperty(value = "住址")
    private String address;

    /** 住址-区块位置Json*/
    @CataWordBind(word = "住址-区块位置Json")
    @ApiModelProperty(value = "住址-区块位置Json")
    private String addressLocationJson;

    /** 公民身份号码 */
    @CataWordBind(word = "公民身份号码")
    @ApiModelProperty(value = "公民身份号码")
    private String number;

    /** 公民身份号码-区块位置Json*/
    @CataWordBind(word = "公民身份号码-区块位置Json")
    @ApiModelProperty(value = "公民身份号码-区块位置Json")
    private String numberLocationJson;

    /** 签发机关 */
    @CataWordBind(word = "签发机关")
    @ApiModelProperty(value = "签发机关")
    private String signDepartment;

    /** 签发机关-区块位置Json*/
    @CataWordBind(word = "签发机关-区块位置Json")
    @ApiModelProperty(value = "签发机关-区块位置Json")
    private String signDepartmentLocationJson;

    /** 签发日期 */
    @CataWordBind(word = "签发日期")
    @ApiModelProperty(value = "签发日期")
    private String signDate;

    /** 签发日期-区块位置Json*/
    @CataWordBind(word = "签发日期-区块位置Json")
    @ApiModelProperty(value = "签发日期-区块位置Json")
    private String signDateLocationJson;

    /** 失效日期 */
    @CataWordBind(word = "失效日期")
    @ApiModelProperty(value = "失效日期")
    private String endDate;

    /** 失效日期-区块位置Json*/
    @CataWordBind(word = "失效日期-区块位置Json")
    @ApiModelProperty(value = "失效日期-区块位置Json")
    private String endDateLocationJson;




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

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSignDepartment() {
        return signDepartment;
    }

    public void setSignDepartment(String signDepartment) {
        this.signDepartment = signDepartment;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    /**
     * 获取 姓名-区块位置Json
     *
     * @return nameLocationJson 姓名-区块位置Json
     */
    public String getNameLocationJson() {
        return this.nameLocationJson;
    }

    /**
     * 设置 姓名-区块位置Json
     *
     * @param nameLocationJson 姓名-区块位置Json
     */
    public void setNameLocationJson(String nameLocationJson) {
        this.nameLocationJson = nameLocationJson;
    }

    /**
     * 获取 性别-区块位置Json
     *
     * @return sexLocationJson 性别-区块位置Json
     */
    public String getSexLocationJson() {
        return this.sexLocationJson;
    }

    /**
     * 设置 性别-区块位置Json
     *
     * @param sexLocationJson 性别-区块位置Json
     */
    public void setSexLocationJson(String sexLocationJson) {
        this.sexLocationJson = sexLocationJson;
    }

    /**
     * 获取 民族-区块位置Json
     *
     * @return nationLocationJson 民族-区块位置Json
     */
    public String getNationLocationJson() {
        return this.nationLocationJson;
    }

    /**
     * 设置 民族-区块位置Json
     *
     * @param nationLocationJson 民族-区块位置Json
     */
    public void setNationLocationJson(String nationLocationJson) {
        this.nationLocationJson = nationLocationJson;
    }

    /**
     * 获取 出生-区块位置Json
     *
     * @return birthLocationJson 出生-区块位置Json
     */
    public String getBirthLocationJson() {
        return this.birthLocationJson;
    }

    /**
     * 设置 出生-区块位置Json
     *
     * @param birthLocationJson 出生-区块位置Json
     */
    public void setBirthLocationJson(String birthLocationJson) {
        this.birthLocationJson = birthLocationJson;
    }

    /**
     * 获取 住址-区块位置Json
     *
     * @return addressLocationJson 住址-区块位置Json
     */
    public String getAddressLocationJson() {
        return this.addressLocationJson;
    }

    /**
     * 设置 住址-区块位置Json
     *
     * @param addressLocationJson 住址-区块位置Json
     */
    public void setAddressLocationJson(String addressLocationJson) {
        this.addressLocationJson = addressLocationJson;
    }

    /**
     * 获取 公民身份号码-区块位置Json
     *
     * @return numberLocationJson 公民身份号码-区块位置Json
     */
    public String getNumberLocationJson() {
        return this.numberLocationJson;
    }

    /**
     * 设置 公民身份号码-区块位置Json
     *
     * @param numberLocationJson 公民身份号码-区块位置Json
     */
    public void setNumberLocationJson(String numberLocationJson) {
        this.numberLocationJson = numberLocationJson;
    }

    /**
     * 获取 签发机关-区块位置Json
     *
     * @return signDepartmentLocationJson 签发机关-区块位置Json
     */
    public String getSignDepartmentLocationJson() {
        return this.signDepartmentLocationJson;
    }

    /**
     * 设置 签发机关-区块位置Json
     *
     * @param signDepartmentLocationJson 签发机关-区块位置Json
     */
    public void setSignDepartmentLocationJson(String signDepartmentLocationJson) {
        this.signDepartmentLocationJson = signDepartmentLocationJson;
    }

    /**
     * 获取 签发日期-区块位置Json
     *
     * @return signDateLocationJson 签发日期-区块位置Json
     */
    public String getSignDateLocationJson() {
        return this.signDateLocationJson;
    }

    /**
     * 设置 签发日期-区块位置Json
     *
     * @param signDateLocationJson 签发日期-区块位置Json
     */
    public void setSignDateLocationJson(String signDateLocationJson) {
        this.signDateLocationJson = signDateLocationJson;
    }

    /**
     * 获取 失效日期-区块位置Json
     *
     * @return endDateLocationJson 失效日期-区块位置Json
     */
    public String getEndDateLocationJson() {
        return this.endDateLocationJson;
    }

    /**
     * 设置 失效日期-区块位置Json
     *
     * @param endDateLocationJson 失效日期-区块位置Json
     */
    public void setEndDateLocationJson(String endDateLocationJson) {
        this.endDateLocationJson = endDateLocationJson;
    }
}
