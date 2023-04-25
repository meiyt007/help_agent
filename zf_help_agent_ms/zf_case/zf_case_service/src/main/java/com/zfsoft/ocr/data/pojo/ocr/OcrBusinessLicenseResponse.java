package com.zfsoft.ocr.data.pojo.ocr;



import com.zfsoft.ocr.data.pojo.BaseResponse;
import com.zfsoft.ocr.data.pojo.annotation.CataWordBind;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * ocr营业执照识别响应信息
 *
 * @Auther dusd
 * @Date 2019/6/22 13:27
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "ocr营业执照识别响应信息")
public class OcrBusinessLicenseResponse extends BaseResponse {

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    @CataWordBind(word = "单位名称")
    private String companyName;


    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称-区块位置Json")
    @CataWordBind(word = "单位名称-区块位置Json")
    private String companyNameLocationJson;


    /**
     * 统一社会信用代码
     */
    @ApiModelProperty(value = "统一社会信用代码")
    @CataWordBind(word = "统一社会信用代码")
    private String socialCode;

    /**
     * 统一社会信用代码
     */
    @ApiModelProperty(value = "统一社会信用代码-区块位置Json")
    @CataWordBind(word = "统一社会信用代码-区块位置Json")
    private String socialCodeLocationJson;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    @CataWordBind(word = "地址")
    private String address;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址-区块位置Json")
    @CataWordBind(word = "地址-区块位置Json")
    private String addressLocationJson;

    /**
     * 成立日期
     */
    @ApiModelProperty(value = "成立日期")
    @CataWordBind(word = "成立日期")
    private String establishDate;

    /**
     * 成立日期
     */
    @ApiModelProperty(value = "成立日期-区块位置Json")
    @CataWordBind(word = "成立日期-区块位置Json")
    private String establishDateLocationJson;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期")
    @CataWordBind(word = "有效期")
    private String effectiveDate;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期-区块位置Json")
    @CataWordBind(word = "有效期-区块位置Json")
    private String effectiveDateLocationJson;

    /**
     * 法人
     */
    @ApiModelProperty(value = "法人")
    @CataWordBind(word = "法人")
    private String lawRepresentative;

    /**
     * 法人
     */
    @ApiModelProperty(value = "法人-区块位置Json")
    @CataWordBind(word = "法人-区块位置Json")
    private String lawRepresentativeLocationJson;

    /**
     * 注册资本
     */
    @ApiModelProperty(value = "注册资本")
    @CataWordBind(word = "注册资本")
    private String registeredCapital;

    /**
     * 注册资本
     */
    @ApiModelProperty(value = "注册资本-区块位置Json")
    @CataWordBind(word = "注册资本-区块位置Json")
    private String registeredCapitalLocationJson;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    @CataWordBind(word = "类型")
    private String type;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型-区块位置Json")
    @CataWordBind(word = "类型-区块位置Json")
    private String typeLocationJson;

    /**
     * 组成形式
     */
    @ApiModelProperty(value = "组成形式")
    @CataWordBind(word = "组成形式")
    private String compositionForm;

    /**
     * 组成形式
     */
    @ApiModelProperty(value = "组成形式-区块位置Json")
    @CataWordBind(word = "组成形式-区块位置Json")
    private String compositionFormLocationJson;

    /**
     * 证件编号
     */
    @ApiModelProperty(value = "证件编号")
    @CataWordBind(word = "证件编号")
    private String idNumber;

    /**
     * 证件编号
     */
    @ApiModelProperty(value = "证件编号-区块位置Json")
    @CataWordBind(word = "证件编号-区块位置Json")
    private String idNumberLocationJson;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSocialCode() {
        return socialCode;
    }

    public void setSocialCode(String socialCode) {
        this.socialCode = socialCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEstablishDate() {
        return establishDate;
    }

    public void setEstablishDate(String establishDate) {
        this.establishDate = establishDate;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getLawRepresentative() {
        return lawRepresentative;
    }

    public void setLawRepresentative(String lawRepresentative) {
        this.lawRepresentative = lawRepresentative;
    }

    public String getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompositionForm() {
        return compositionForm;
    }

    public void setCompositionForm(String compositionForm) {
        this.compositionForm = compositionForm;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }


    /**
     * 获取 单位名称
     *
     * @return companyNameLocationJson 单位名称
     */
    public String getCompanyNameLocationJson() {
        return this.companyNameLocationJson;
    }

    /**
     * 设置 单位名称
     *
     * @param companyNameLocationJson 单位名称
     */
    public void setCompanyNameLocationJson(String companyNameLocationJson) {
        this.companyNameLocationJson = companyNameLocationJson;
    }

    /**
     * 获取 统一社会信用代码
     *
     * @return socialCodeLocationJson 统一社会信用代码
     */
    public String getSocialCodeLocationJson() {
        return this.socialCodeLocationJson;
    }

    /**
     * 设置 统一社会信用代码
     *
     * @param socialCodeLocationJson 统一社会信用代码
     */
    public void setSocialCodeLocationJson(String socialCodeLocationJson) {
        this.socialCodeLocationJson = socialCodeLocationJson;
    }

    /**
     * 获取 地址
     *
     * @return addressLocationJson 地址
     */
    public String getAddressLocationJson() {
        return this.addressLocationJson;
    }

    /**
     * 设置 地址
     *
     * @param addressLocationJson 地址
     */
    public void setAddressLocationJson(String addressLocationJson) {
        this.addressLocationJson = addressLocationJson;
    }

    /**
     * 获取 成立日期
     *
     * @return establishDateLocationJson 成立日期
     */
    public String getEstablishDateLocationJson() {
        return this.establishDateLocationJson;
    }

    /**
     * 设置 成立日期
     *
     * @param establishDateLocationJson 成立日期
     */
    public void setEstablishDateLocationJson(String establishDateLocationJson) {
        this.establishDateLocationJson = establishDateLocationJson;
    }

    /**
     * 获取 有效期
     *
     * @return effectiveDateLocationJson 有效期
     */
    public String getEffectiveDateLocationJson() {
        return this.effectiveDateLocationJson;
    }

    /**
     * 设置 有效期
     *
     * @param effectiveDateLocationJson 有效期
     */
    public void setEffectiveDateLocationJson(String effectiveDateLocationJson) {
        this.effectiveDateLocationJson = effectiveDateLocationJson;
    }

    /**
     * 获取 法人
     *
     * @return lawRepresentativeLocationJson 法人
     */
    public String getLawRepresentativeLocationJson() {
        return this.lawRepresentativeLocationJson;
    }

    /**
     * 设置 法人
     *
     * @param lawRepresentativeLocationJson 法人
     */
    public void setLawRepresentativeLocationJson(String lawRepresentativeLocationJson) {
        this.lawRepresentativeLocationJson = lawRepresentativeLocationJson;
    }

    /**
     * 获取 注册资本
     *
     * @return registeredCapitalLocationJson 注册资本
     */
    public String getRegisteredCapitalLocationJson() {
        return this.registeredCapitalLocationJson;
    }

    /**
     * 设置 注册资本
     *
     * @param registeredCapitalLocationJson 注册资本
     */
    public void setRegisteredCapitalLocationJson(String registeredCapitalLocationJson) {
        this.registeredCapitalLocationJson = registeredCapitalLocationJson;
    }

    /**
     * 获取 类型
     *
     * @return typeLocationJson 类型
     */
    public String getTypeLocationJson() {
        return this.typeLocationJson;
    }

    /**
     * 设置 类型
     *
     * @param typeLocationJson 类型
     */
    public void setTypeLocationJson(String typeLocationJson) {
        this.typeLocationJson = typeLocationJson;
    }

    /**
     * 获取 组成形式
     *
     * @return compositionFormLocationJson 组成形式
     */
    public String getCompositionFormLocationJson() {
        return this.compositionFormLocationJson;
    }

    /**
     * 设置 组成形式
     *
     * @param compositionFormLocationJson 组成形式
     */
    public void setCompositionFormLocationJson(String compositionFormLocationJson) {
        this.compositionFormLocationJson = compositionFormLocationJson;
    }

    /**
     * 获取 证件编号
     *
     * @return idNumberLocationJson 证件编号
     */
    public String getIdNumberLocationJson() {
        return this.idNumberLocationJson;
    }

    /**
     * 设置 证件编号
     *
     * @param idNumberLocationJson 证件编号
     */
    public void setIdNumberLocationJson(String idNumberLocationJson) {
        this.idNumberLocationJson = idNumberLocationJson;
    }
}
