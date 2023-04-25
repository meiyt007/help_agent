/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-11 11:12:47
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-26 16:35:40
 * @FilePath: \zf_web_ui\src\api\modules\business.js
 * @Description: 业务流程接口
 */
import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_CASE_API;
//将数据保存到redis中，推送到万达获取办件编码接口中，当做入参
export function saveData(query) {
  return request({
    url: baseUrl + "/ha/web/saveData",
    method: "post",
    params: query,
  });
}
//事项列表
export function listSxServicePage(data) {
  return request({
    url: baseUrl + "/ha/web/listSxServicePage",
    method: "get",
    params: data,
  });
}

//用户服务类型权限
export function serviceType(data) {
  return request({
    url: baseUrl + "/ha/web/serviceType",
    method: "get",
    params: data,
  });
}

//机构列表
export function listOrganByDistrictAndService(data) {
  return request({
    url: baseUrl + "/ha/web/listOrganByDistrictAndService",
    method: "post",
    params: data,
  });
}

//查询视频用户分页信息列表
export function queryVideoPage(data) {
  return request({
    url: baseUrl + "/ha/video/queryVideoPage",
    method: "post",
    params: data,
  });
}

//获取事项的选项标题(智能登记)
export function getSxServiceTitlesNoRelation(data) {
  return request({
    url: baseUrl + "/ha/web/getSxServiceTitlesNoRelation",
    method: "get",
    params: data,
  });
}

//获取事项有关的选项标题
export function querySxServiceSituationRelation(data) {
  return request({
    url: baseUrl + "/ha/web/querySxServiceSituationRelation",
    method: "post",
    params: data,
  });
}

//实施清单的信息
export function viewSxService(data) {
  return request({
    url: baseUrl + "/ha/web/viewSxService",
    method: "get",
    params: data,
  });
}

//暂存办件信息
export function nextStepSaveQlCase(data) {
  let datas = {};
  let qlCase = {};
  let qlCaseApply = {};
  let qlCaseExt = {};
  let materials = [];
  let titleValues = [];
  if (data.rqbzDueDate) {
    qlCase.rqbzDueDate = data.rqbzDueDate;
  }
  if (data.currentPersonNum) {
    qlCase.queueNum = data.currentPersonNum;
  }
  if (data.id != "") {
    qlCase.id = data.id;
  }
  if (data.caseOid != "") {
    qlCase.caseOid = data.caseOid;
    qlCaseApply.caseOid = data.caseOid;
    qlCaseExt.caseOid = data.caseOid;
  }
  if (data.caseStatus != "") {
    qlCase.caseStatus = data.caseStatus;
  }
  if (data.caseNumber != "") {
    qlCase.caseNumber = data.caseNumber;
  }
  if (data.serviceOid != "") {
    qlCase.serviceOid = data.serviceOid;
  }
  if (data.projectName != "") {
    qlCase.projectName = data.projectName;
  }
  if (data.sourceApp != "") {
    qlCase.sourceApp = data.sourceApp;
  }
  if (data.certWay) {
    qlCase.certWay = data.certWay;
  }
  if (data.expressCompany) {
    qlCase.expressCompany = data.expressCompany;
  }
  if (data.applyOid != "") {
    qlCaseApply.id = data.applyOid;
  }
  if (data.applyNumber != "") {
    qlCaseApply.applyNumber = data.applyNumber;
  }
  if (data.applyUserType != "") {
    qlCaseApply.applyUserType = data.applyUserType;
  }
  if (data.specificLocation != "") {
    qlCaseApply.specificLocation = data.specificLocation;
  }
  if (data.bussVenueDistrictOid) {
    // let districtOid = [];
    // let oids = "";
    // districtOid = data.bussVenueDistrictOid || [];
    // districtOid.forEach((oid) => {
    //   oids += oid + ",";
    // });
    qlCaseApply.bussVenueDistrictOid = data.bussVenueDistrictOid;
  }
  if (data.fillUserInfo) {
    if (data.fillUserInfo.credentialNumber != "") {
      qlCaseApply.credentialNumber = data.fillUserInfo.credentialNumber;
    }
  } else {
    qlCaseApply.credentialNumber = data.credentialNumber;
  }
  if (data.applyUserName != "") {
    qlCaseApply.applyUserName = data.applyUserName;
  }
  if (data.applyUserPhone != "") {
    qlCaseApply.applyUserPhone = data.applyUserPhone;
  }
  qlCaseApply.applyUserTel = data.applyUserTel;
  if (data.applyPostCode != "") {
    qlCaseApply.applyPostCode = data.applyPostCode;
  }
  if (data.credentialType != "") {
    qlCaseApply.credentialType = data.credentialType;
  }
  if (data.legalPersonName != "") {
    qlCaseApply.legalPersonName = data.legalPersonName;
  }
  qlCaseApply.applyUserAddress = data.applyUserAddress;
  if (data.addresseeName != "") {
    qlCaseApply.addresseeName = data.addresseeName;
  }
  if (data.addresseePhone != "") {
    qlCaseApply.addresseePhone = data.addresseePhone;
  }
  qlCaseApply.addresseeTel = data.addresseeTel;
  if (data.addresseePostCode != "") {
    qlCaseApply.addresseePostCode = data.addresseePostCode;
  }
  if (data.addresseeAddress != "") {
    qlCaseApply.addresseeAddress = data.addresseeAddress;
  }
  if (data.chooseAddress.length) {
    qlCaseApply.chooseAddress = data.chooseAddress;
  }
  if (data.addresseeDetailAddress != "") {
    qlCaseApply.addresseeDetailAddress = data.addresseeDetailAddress;
  }
  if (data.contactUserName != "") {
    qlCaseApply.contactUserName = data.contactUserName;
  }
  if (data.contactCredentialNumber != "") {
    qlCaseApply.contactCredentialNumber = data.contactCredentialNumber;
  }
  qlCaseApply.contactEmail = data.contactEmail;
  if (data.contactUserPhone != "") {
    qlCaseApply.contactUserPhone = data.contactUserPhone;
  }
  qlCaseApply.contactUserTel = data.contactUserTel;
  qlCaseApply.contactRemark = data.contactRemark;
  if (data.extOid != "") {
    qlCaseExt.id = data.extOid;
  }
  qlCaseExt.investProjecName = data.investProjecName;
  qlCaseExt.investProjectCode = data.investProjectCode;
  qlCaseExt.projectAbstract = data.projectAbstract;
  if (data.resultDeliveryWay != "") {
    qlCaseExt.resultDeliveryWay = data.resultDeliveryWay;
  }
  if (data.proxyFlag) {
    qlCaseExt.proxyFlag = data.proxyFlag;
  }
  if (data.sxServiceMaterialList.length) {
    for (let i = 0; i < data.sxServiceMaterialList.length; i++) {
      materials.push(data.sxServiceMaterialList[i]);
    }
  }
  if (data.qlCaseTitleValList.length) {
    for (let i = 0; i < data.qlCaseTitleValList.length; i++) {
      titleValues.push(data.qlCaseTitleValList[i]);
    }
  }
  // 窗口登记状态
  qlCase.sourceType = data.sourceType;
  datas.qlCase = qlCase;
  datas.qlCaseApply = qlCaseApply;
  datas.qlCaseExt = qlCaseExt;
  datas.materials = materials;
  datas.titleValues = titleValues;
  return request({
    url: baseUrl + "/ha/web/nextStepSaveQlCase",
    method: "post",
    data: datas,
  });
}
export function nextStepSaveQlCase1(data) {
  return request({
    url: baseUrl + "/ha/web/nextStepSaveQlCase",
    method: "post",
    data: data,
  });
}
//
//专属指南
export function getExclusiveGuide(data) {
  return request({
    url: baseUrl + "/ha/web/getExclusiveGuide",
    method: "get",
    params: data,
  });
}

//表单字段及预填信息
export function getFormFillInfos(data) {
  return request({
    url: baseUrl + "/ha/web/getFormFillInfos",
    method: "get",
    params: data,
  });
}

//表单预填信息
export function getAllBasicFieldByOid(data) {
  return request({
    url: baseUrl + "/ha/web/getAllBasicFieldByOid",
    method: "get",
    params: data,
  });
}

//获取表单信息（表单服务提供）
export function getFormData(data) {
  return request({
    url: baseUrl + "/ha/web/getFormData",
    method: "get",
    params: data,
  });
}

//办件关联更新表单信息
export function updateFormInfo(data) {
  return request({
    url: baseUrl + "/ha/web/updateFormInfo",
    method: "post",
    data: data,
  });
}

//获取表单填充模板集合
export function getTemplateList(data) {
  return request({
    url: baseUrl + "/ha/web/getTemplateList",
    method: "get",
    params: data,
  });
}

//查询材料信息
export function queryQlCaseMaterialListByCaseOid(data) {
  return request({
    url: baseUrl + "/ha/web/queryQlCaseMaterialListByCaseOid",
    method: "get",
    params: data,
  });
}

//查询材料信息
export function querySimpleQlCaseMaterialListByCaseOid(data) {
  return request({
    url: baseUrl + "/ha/web/querySimpleQlCaseMaterialListByCaseOid",
    method: "get",
    params: data,
  });
}

//材料上传
export function uploadCaseMaterialFile(data) {
  return request({
    url: baseUrl + "/ha/web/uploadCaseMaterialFile",
    method: "post",
    headers: {
      "Content-Type": "multipart/form-data",
    },
    data: data,
  });
}

//旗舰店对接材料上传
export function outerUploadCaseMaterialFile(data) {
  return request({
    url: baseUrl + "/ha/outer/uploadCaseMaterialFile",
    method: "post",
    headers: {
      "Content-Type": "multipart/form-data",
    },
    data: data,
  });
}

//更新材料附件
export function saveOrUpdateCaseMaterialAttaList(data) {
  return request({
    url: baseUrl + "/ha/web/saveOrUpdateCaseMaterialAttaList",
    method: "post",
    data: data,
  });
}

//旗舰店对接更新材料附件
export function outerSaveOrUpdateCaseMaterialAttaList(data) {
  return request({
    url: baseUrl + "/ha/outer/saveOrUpdateCaseMaterialAttaList",
    method: "post",
    data: data,
  });
}

//历史办件列表
export function queryDoneQlCasePage(data) {
  return request({
    url: baseUrl + "/ha/web/queryDoneQlCasePage",
    method: "post",
    params: data,
  });
}

//根据办件业务主键查询办件申请信息
export function queryQlCaseApplayByCaseOid(data) {
  return request({
    url: baseUrl + "/ha/web/queryQlCaseApplayByCaseOid",
    method: "get",
    params: data,
  });
}

//根据办件编号获取情形标题选项信息
export function getCaseTitleValueList(data) {
  return request({
    url: baseUrl + "/ha/web/getCaseTitleValueList",
    method: "get",
    params: data,
  });
}

//获取证件类型
export function getSelectCertificateType(data) {
  return request({
    url: baseUrl + "/ha/web/getSelectCertificateType",
    method: "get",
    params: data,
  });
}

//根据办件材料oid查询当前办件的材料附件情况
export function queryQlCaseMaterialAttaByCaseMaterialOid(data) {
  return request({
    url: baseUrl + "/ha/outer/queryQlCaseMaterialAttaByCaseMaterialOid",
    method: "get",
    params: data,
  });
}

//根据证件号查询办件和申请人信息
export function queryQlCaseByCredentialNumber(data) {
  return request({
    url: baseUrl + "/ha/web/queryQlCaseByCredentialNumber",
    method: "get",
    params: data,
  });
}

//更新材料关联
export function updateQlCaseMaterialList(data) {
  return request({
    url: baseUrl + "/ha/web/updateQlCaseMaterialList",
    method: "post",
    data: data,
    headers: {
      "Content-Type": "application/json",
    },
  });
}

//查询材料列表
export function getSituationMaterialListByOids(data) {
  return request({
    url: baseUrl + "/ha/web/getSituationMaterialListByOids",
    method: "get",
    params: data,
  });
}

//办件受理
export function saveCaseAccpet(data) {
  return request({
    url: baseUrl + "/ha/web/saveCaseAccpet",
    method: "get",
    params: data,
  });
}

//办事指南一键推送
export function onekeyPush(data) {
  return request({
    url: baseUrl + "/outer/inter/onekeyPush",
    method: "get",
    params: data,
  });
}

//办件一键推送
export function handlingOnekeyPush(data) {
  return request({
    url: baseUrl + "/ha/web/onekeyPush",
    method: "post",
    data: data,
  });
}

//保存表单信息 /form-api/form/manager/saveFormData
export function saveFormData(data) {
  return request({
    url: baseUrl + "/form-api/form/manager/saveFormData",
    method: "post",
    data: data,
  });
}

//根据办件编号获取电子表单信息
export function queryFormInfoByCaseOid(data) {
  return request({
    url: baseUrl + "/ha/web/queryFormInfoByCaseOid",
    method: "get",
    params: data,
  });
}

//获取表单信息
export function getViewFormData(data) {
  return request({
    url: baseUrl + "/form-api/form/manager/getFormData",
    method: "get",
    params: data,
  });
}

//基础表单和电子表单关联字段
export function getBasicAndFormFieldRel(data) {
  return request({
    url: baseUrl + "/ha/web/getBasicAndFormFieldRel",
    method: "get",
    params: data,
  });
}

//办事指南填报须知
export function queryComPro(data) {
  return request({
    url: baseUrl + "/ha/service/commonProblem/queryComPro",
    method: "get",
    params: data,
  });
}

//根据办件编号查询办件信息
export function queryQlCaseByCaseOid(data) {
  return request({
    url: baseUrl + "/ha/web/queryQlCaseByCaseOid",
    method: "get",
    params: data,
  });
}

//
export function pushData(data) {
  return request({
    url: baseUrl + "/ha/wanda/pushData",
    method: "post",
    data: data,
    headers: {
      "Content-Type": "application/json",
    },
  });
}

//附件业务主键查看附件信息
export function querySysAttaByOid(data) {
  return request({
    url: baseUrl + "/ha/web/querySysAttaByOid",
    method: "get",
    params: data,
  });
}


//一码续办模糊查询企业名称
export function queryCompanyName(data) {
  return request({
    url: baseUrl + "/ha/workUser/queryCompanyName",
    method: "get",
    params: data,
  });
}

//sms短信
export function sendHPSms(data) {
  return request({
    url: baseUrl + "/ha/web/sendHPSms",
    method: "get",
    params: data,
  });
}
