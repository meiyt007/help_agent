package com.zfsoft.ocr.controller.textInOcr;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrFormResponse;
import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrItemResponse;
import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrRequest;
import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrResponse;
import com.zfsoft.ocr.manager.textInOcr.TextInOcrCertificateRestManager;
import com.zfsoft.ocr.service.textInOcr.TextInOcrFormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/***
 * @Description:textInOcr卡证证照识别对接表单服务
 * @Author:wangyg
 * @Date:2022/6/1
 */
@RestController
@Slf4j
public class TextInOcrFormController implements TextInOcrFormService {

    @Resource
    private TextInOcrCertificateRestManager textInOcrCertificateRestManager;


    /***
     * @Description:textInOcr身份证
     * @Author:wangyg
     * @Date:2022/6/1
     * @param base64ImgStr 身份证图片base64编码数据
     * @param side face:正面，back:反面
     */
    @Override
    public TextInOcrFormResponse textInOcrIdCard(List<String> base64ImgStr, String side) {
        TextInOcrFormResponse textInOcrFormResponse = new TextInOcrFormResponse();
        Map<String, Object> responseData = new HashMap<>();
        try {
            List<String> picList = new LinkedList<>();
            for (String base64Img : base64ImgStr) {
                TextInOcrRequest textInOcrRequest = new TextInOcrRequest();
                textInOcrRequest.setType("id_card");
                textInOcrRequest.setImgBase64(base64Img);
                TextInOcrResponse textInOcrResponse = textInOcrCertificateRestManager.textInOcrCertificateRestOcr(textInOcrRequest);
                List<TextInOcrItemResponse> textInOcrItemResponseList = textInOcrResponse.getTextInOcrItemResponseList();
                Map<String, String> textData = textInOcrItemResponseList.stream().collect(Collectors.toMap(TextInOcrItemResponse::getKey, TextInOcrItemResponse::getWord));
                String name = textData.get("name");
                String sex = textData.get("sex");
                String nationality = textData.get("nationality");
                String birth = textData.get("birth");
                String address = textData.get("address");
                String idNumber = textData.get("id_number");
                String validateDate = textData.get("validate_date");
                String issueAuthority = textData.get("issue_authority");
                String cropImage = textData.get("crop_image");
                picList.add(cropImage);
                if (StrUtil.isNotEmpty(name)) {
                    responseData.put("name", name);
                }
                if (StrUtil.isNotEmpty(idNumber)) {
                    responseData.put("num", idNumber);
                }
                if (StrUtil.isNotEmpty(sex)) {
                    responseData.put("sex", sex);
                }
                if (StrUtil.isNotEmpty(address)) {
                    responseData.put("address", address);
                }
                if (StrUtil.isNotEmpty(birth)) {
                    responseData.put("birth", birth);
                }
                if (StrUtil.isNotEmpty(nationality)) {
                    responseData.put("nationality", nationality);
                }
                if (StrUtil.isNotEmpty(issueAuthority)) {
                    responseData.put("issue", issueAuthority);
                }
                if (StrUtil.isNotEmpty(validateDate)) {
                    String[] split = validateDate.split("-");
                    responseData.put("start_date", split[0]);
                    responseData.put("end_date", split[1]);
                }
            }
            responseData.put("picList",picList);
            textInOcrFormResponse.setCode(200);
            textInOcrFormResponse.setData(JSONObject.toJSONString(responseData));
        } catch (Exception e) {
            textInOcrFormResponse.setCode(201);
            textInOcrFormResponse.setMessage(e.getMessage());
        }
        return textInOcrFormResponse;
    }

    /***
     * @Description:textInOcr身份证
     * @Author:wangyg
     * @Date:2022/6/1
     * @param base64ImgStr 营业执照base64编码数据集合
     */
    @Override
    public TextInOcrFormResponse textInOcrBusinessLicense(List<String> base64ImgStr) {
        TextInOcrFormResponse textInOcrFormResponse = new TextInOcrFormResponse();
        Map<String, String> responseData = new HashMap<>();
        try {
            for (String base64Img : base64ImgStr) {
                TextInOcrRequest textInOcrRequest = new TextInOcrRequest();
                textInOcrRequest.setType("business_license");
                textInOcrRequest.setImgBase64(base64Img);
                TextInOcrResponse textInOcrResponse = textInOcrCertificateRestManager.textInOcrCertificateRestOcr(textInOcrRequest);
                List<TextInOcrItemResponse> textInOcrItemResponseList = textInOcrResponse.getTextInOcrItemResponseList();
                Map<String, String> textData = textInOcrItemResponseList.stream().collect(Collectors.toMap(TextInOcrItemResponse::getKey, TextInOcrItemResponse::getWord));
                String bizLicenseIsCopy = textData.get("BizLicenseIsCopy");
                String bizLicenseCreditCode = textData.get("BizLicenseCreditCode");
                String bizLicenseRegistrationCode = textData.get("BizLicenseRegistrationCode");
                String bizLicenseSerialNumber = textData.get("BizLicenseSerialNumber");
                String bizLicenseCompanyName = textData.get("BizLicenseCompanyName");
                String bizLicenseCompanyType = textData.get("BizLicenseCompanyType");
                String bizLicenseAddress = textData.get("BizLicenseAddress");
                String bizLicenseComposingForm = textData.get("BizLicenseComposingForm");
                String bizLicenseOwnerName = textData.get("BizLicenseOwnerName");
                String bizLicenseRegCapital = textData.get("BizLicenseRegCapital");
                String bizLicensePaidInCapital = textData.get("BizLicensePaidInCapital");
                String bizLicenseStartTime = textData.get("BizLicenseStartTime");
                String bizLicenseOperatingPeriod = textData.get("BizLicenseOperatingPeriod");
                String bizLicenseScope = textData.get("BizLicenseScope");
                String bizLicenseRegistrationDate = textData.get("BizLicenseRegistrationDate");
                if (StrUtil.isNotEmpty(bizLicenseIsCopy)) {
                    responseData.put("BizLicenseIsCopy", bizLicenseIsCopy);
                }
                if (StrUtil.isNotEmpty(bizLicenseCompanyType)) {
                    responseData.put("BizLicenseCompanyType", bizLicenseCompanyType);
                }
                if (StrUtil.isNotEmpty(bizLicenseCreditCode)) {
                    responseData.put("BizLicenseCreditCode", bizLicenseCreditCode);
                }
                if (StrUtil.isNotEmpty(bizLicenseCompanyName)) {
                    responseData.put("BizLicenseCompanyName", bizLicenseCompanyName);
                }
                if (StrUtil.isNotEmpty(bizLicenseSerialNumber)) {
                    responseData.put("BizLicenseSerialNumber", bizLicenseSerialNumber);
                }
                if (StrUtil.isNotEmpty(bizLicenseRegistrationCode)) {
                    responseData.put("BizLicenseRegistrationCode", bizLicenseRegistrationCode);
                }
                if (StrUtil.isNotEmpty(bizLicenseComposingForm)) {
                    responseData.put("BizLicenseComposingForm", bizLicenseComposingForm);
                }
                if (StrUtil.isNotEmpty(bizLicenseAddress)) {
                    responseData.put("BizLicenseAddress", bizLicenseAddress);
                }
                if (StrUtil.isNotEmpty(bizLicenseOwnerName)) {
                    responseData.put("BizLicenseOwnerName", bizLicenseOwnerName);
                }
                if (StrUtil.isNotEmpty(bizLicenseRegCapital)) {
                    responseData.put("BizLicenseRegCapital", bizLicenseRegCapital);
                }
                if (StrUtil.isNotEmpty(bizLicensePaidInCapital)) {
                    responseData.put("BizLicensePaidInCapital", bizLicensePaidInCapital);
                }
                if (StrUtil.isNotEmpty(bizLicenseStartTime)) {
                    responseData.put("BizLicenseStartTime", bizLicenseStartTime);
                }
                if (StrUtil.isNotEmpty(bizLicenseOperatingPeriod)) {
                    responseData.put("BizLicenseOperatingPeriod", bizLicenseOperatingPeriod);
                }
                if (StrUtil.isNotEmpty(bizLicenseScope)) {
                    responseData.put("BizLicenseScope", bizLicenseScope);
                }
                if (StrUtil.isNotEmpty(bizLicenseRegistrationDate)) {
                    responseData.put("BizLicenseRegistrationDate", bizLicenseRegistrationDate);
                }
            }
            textInOcrFormResponse.setCode(200);
            textInOcrFormResponse.setData(JSONObject.toJSONString(responseData));
        } catch (Exception e) {
            textInOcrFormResponse.setCode(201);
            textInOcrFormResponse.setMessage(e.getMessage());
        }
        return textInOcrFormResponse;
    }

    /***
     * @Description:textInOcr身份证
     * @Author:wangyg
     * @Date:2022/6/1
     * @param base64ImgStr 户口本base64编码数据集合
     */
    @Override
    public TextInOcrFormResponse textInOcrHouseholdRegister(List<String> base64ImgStr) {
        TextInOcrFormResponse textInOcrFormResponse = new TextInOcrFormResponse();
        Map<String, String> responseData = new HashMap<>();
        try {
            for (String base64Img : base64ImgStr) {
                TextInOcrRequest textInOcrRequest = new TextInOcrRequest();
                textInOcrRequest.setType("household_register");
                textInOcrRequest.setImgBase64(base64Img);
                TextInOcrResponse textInOcrResponse = textInOcrCertificateRestManager.textInOcrCertificateRestOcr(textInOcrRequest);
                List<TextInOcrItemResponse> textInOcrItemResponseList = textInOcrResponse.getTextInOcrItemResponseList();
                Map<String, String> textData = textInOcrItemResponseList.stream().collect(Collectors.toMap(TextInOcrItemResponse::getKey, TextInOcrItemResponse::getWord));
                String memberName = textData.get("member_name");
                String memberHouseholder = textData.get("member_householder_or_relationship_with_householder");
                String memberUsedName = textData.get("member_used_name");
                String memberSex = textData.get("member_sex");
                String memberBirthplace = textData.get("member_birthplace");
                String memberNationality = textData.get("member_nationality");
                String memberOriginPlace = textData.get("member_origin_place");
                String memberBirth = textData.get("member_birth");
                String memberOtherAddressThisCity = textData.get("member_other_address_this_city");
                String memberReligion = textData.get("member_religion");
                String memberIdNumber = textData.get("member_id_number");
                String memberHeight = textData.get("member_height");
                String memberBloodType = textData.get("member_blood_type");
                String memberEducationBackground = textData.get("member_education_background");
                String memberMaritalStatus = textData.get("member_marital_status");
                String memberMilitaryServiceStatus = textData.get("member_military_service_status");
                String memberUnitOfService = textData.get("member_unit_of_service");
                String memberProfession = textData.get("member_profession");
                String memberWhenAndWhereToMoveToThisCity = textData.get("member_when_and_where_to_move_to_this_city");
                String memberWhenAndWhereToMoveToThisSite = textData.get("member_when_and_where_to_move_to_this_site");
                String memberRegistrationDate = textData.get("member_registration_date");
                String householdResidenceType = textData.get("household_residence_type");
                String householdHouseholderName = textData.get("household_householder_name");
                String householdAddress = textData.get("household_address");
                String householdResidenceNumber = textData.get("household_residence_number");
                String householdDate = textData.get("household_date");
                String subtype = textData.get("subtype");
                String type = textData.get("type");
                if (StrUtil.isNotEmpty(memberName)) {
                    responseData.put("member_name", memberName);
                }
                if (StrUtil.isNotEmpty(memberHouseholder)) {
                    responseData.put("member_householder_or_relationship_with_householder", memberHouseholder);
                }
                if (StrUtil.isNotEmpty(memberUsedName)) {
                    responseData.put("member_used_name", memberUsedName);
                }
                if (StrUtil.isNotEmpty(memberSex)) {
                    responseData.put("member_sex", memberSex);
                }
                if (StrUtil.isNotEmpty(memberBirthplace)) {
                    responseData.put("member_birthplace", memberBirthplace);
                }
                if (StrUtil.isNotEmpty(memberNationality)) {
                    responseData.put("member_nationality", memberNationality);
                }
                if (StrUtil.isNotEmpty(memberOriginPlace)) {
                    responseData.put("member_origin_place", memberOriginPlace);
                }
                if (StrUtil.isNotEmpty(memberBirth)) {
                    responseData.put("member_birth", memberBirth);
                }
                if (StrUtil.isNotEmpty(memberOtherAddressThisCity)) {
                    responseData.put("member_other_address_this_city", memberOtherAddressThisCity);
                }
                if (StrUtil.isNotEmpty(memberReligion)) {
                    responseData.put("member_religion", memberReligion);
                }
                if (StrUtil.isNotEmpty(memberIdNumber)) {
                    responseData.put("member_id_number", memberIdNumber);
                }
                if (StrUtil.isNotEmpty(memberHeight)) {
                    responseData.put("member_height", memberHeight);
                }
                if (StrUtil.isNotEmpty(memberBloodType)) {
                    responseData.put("member_blood_type", memberBloodType);
                }
                if (StrUtil.isNotEmpty(memberEducationBackground)) {
                    responseData.put("member_education_background", memberEducationBackground);
                }
                if (StrUtil.isNotEmpty(memberMaritalStatus)) {
                    responseData.put("member_marital_status", memberMaritalStatus);
                }
                if (StrUtil.isNotEmpty(memberMilitaryServiceStatus)) {
                    responseData.put("member_military_service_status", memberMilitaryServiceStatus);
                }
                if (StrUtil.isNotEmpty(memberUnitOfService)) {
                    responseData.put("member_unit_of_service", memberUnitOfService);
                }
                if (StrUtil.isNotEmpty(memberProfession)) {
                    responseData.put("member_profession", memberProfession);
                }
                if (StrUtil.isNotEmpty(memberWhenAndWhereToMoveToThisCity)) {
                    responseData.put("member_when_and_where_to_move_to_this_city", memberWhenAndWhereToMoveToThisCity);
                }
                if (StrUtil.isNotEmpty(memberWhenAndWhereToMoveToThisSite)) {
                    responseData.put("member_when_and_where_to_move_to_this_site", memberWhenAndWhereToMoveToThisSite);
                }
                if (StrUtil.isNotEmpty(memberRegistrationDate)) {
                    responseData.put("member_registration_date", memberRegistrationDate);
                }
                if (StrUtil.isNotEmpty(householdResidenceType)) {
                    responseData.put("household_residence_type", householdResidenceType);
                }
                if (StrUtil.isNotEmpty(householdHouseholderName)) {
                    responseData.put("household_householder_name", householdHouseholderName);
                }
                if (StrUtil.isNotEmpty(householdAddress)) {
                    responseData.put("household_address", householdAddress);
                }
                if (StrUtil.isNotEmpty(householdResidenceNumber)) {
                    responseData.put("household_residence_number", householdResidenceNumber);
                }
                if (StrUtil.isNotEmpty(householdDate)) {
                    responseData.put("household_date", householdDate);
                }
                if (StrUtil.isNotEmpty(subtype)) {
                    responseData.put("subtype", subtype);
                }
                if (StrUtil.isNotEmpty(type)) {
                    responseData.put("type", type);
                }
            }
            textInOcrFormResponse.setCode(200);
            textInOcrFormResponse.setData(JSONObject.toJSONString(responseData));
        } catch (Exception e) {
            textInOcrFormResponse.setCode(201);
            textInOcrFormResponse.setMessage(e.getMessage());
        }
        return textInOcrFormResponse;
    }
}
