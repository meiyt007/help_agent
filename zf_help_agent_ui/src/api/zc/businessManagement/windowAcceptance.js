/**
 * 窗口受理接口api
 * @author: wangwg
 * @date: 2020-10-29
 */
import request from "@/utils/request";
import { WPS } from "@/utils/config";
import qs from "qs";
import store from "@/store";
const URL_PRE = () => store.state.config.deviceMap?.[WPS];
const DSXBL = process.env.VUE_APP_DSXBL_ROUTE_PATH,
  SX = process.env.VUE_APP_SX_ROUTE_PATH,
  BJFW = process.env.VUE_APP_BJFW_ROUTE_PATH,
  DZQZ = process.env.VUE_APP_MIDDLE_WEB_PATH,
  ZC = process.env.VUE_APP_ZC_ROUTE_PATH;

// 电子签章初始化数据
export function getElectronicSealInfo(serviceOid, reportOid) {
  return request({
    url: DSXBL + "/template/getTemplateList",
    method: "get",
    params: { reportOid, serviceOid }
  });
}

//系统智能生成材料
export function queryQlCaseMaterialListByCaseOidForZC(caseOid) {
  return request({
    url: DSXBL + "/interface/queryQlCaseMaterialListByCaseOidForZC",
    method: "get",
    params: { caseOid }
  });
}

// 多人签章判断该办件事项是否配置
export function getHtmlType(data) {
  return request({
    url: ZC + "/windows/getHtmlType",
    method: "get",
    params: data
  });
}

//获取签章角色列表
export function getSignRole(data) {
  return request({
    url: ZC + "/windows/getSignRole",
    method: "get",
    params: data
  });
}

//根据办件oid和材料oid查询多角色签章人数据
export function getSignaturePerson(data) {
  return request({
    url: ZC + "/windows/getSignaturePerson",
    method: "get",
    params: data
  });
}

/**材料签章与上传————获取创建签章流程中fileList参数**/
export function getTemplateList(data) {
  return request({
    url: ZC + "/windows/getTemplateList",
    method: "get",
    params: data
  });
}

//保存签章用户
export function saveSignaturePerson(params) {
  return request({
    url: ZC + "/windows/saveSignaturePerson",
    method: "post",
    data: params
  });
}
//短信通知签章
export function sendMessage(data) {
  return request({
    url: ZC + "/windows/sendMessage",
    method: "POST",
    data: data
  });
}

// 电子签章签章中返回二维码流程id数据
export function getElectronicSealInfo2(data) {
  return request({
    url: DZQZ + "/web/electronicSignature/createFlow",
    method: "post",
    data: data
  });
}
//电子签章使用流程id的接口
export function getElectronicSealInfo3(data) {
  return request({
    url:
      process.env.VUE_APP_DSXBL_ROUTE_PATH +
      "/interface/saveOrUpdateSignatureFlow",
    method: "post",
    data: data
  });
}
//电子签章客户签章查询状态的接口
export function getElectronicSealInfo4(caseOid, materialOid) {
  return request({
    url:
      process.env.VUE_APP_DSXBL_ROUTE_PATH +
      "/interface/getSignatureFlowRecordByCaseOid",
    method: "get",
    params: { caseOid, materialOid }
  });
}

// 分页查询列表操作
export function page(query) {
  return request({
    url: DSXBL + "/windowAcceptance/listWindowAcceptancePage",
    method: "get",
    params: query
  });
}

// 获取事项情形信息
export function getSituationList(serviceOid) {
  return request({
    url:
      SX +
      "/affair/sxService/getSxServiceSituationList?serviceOid=" +
      serviceOid,
    method: "get"
  });
}

// 获取事项情形信息
export function getAllSituationList(serviceOid) {
  return request({
    url:
      SX +
      "/affair/sxService/getSxServiceSituationAndOptions?serviceOid=" +
      serviceOid,
    method: "get"
  });
}

// 获取事项情形信息
export function getSituationMaterialListByOids(serviceOid, valOidList) {
  let optionValOids = "";
  if (valOidList != "") {
    valOidList.forEach(data => {
      optionValOids += data + ";";
    });
  }
  return request({
    url:
      SX +
      "/affair/serviceMaterial/getSituationMaterialListByOids?serviceOid=" +
      serviceOid +
      "&optionValOids=" +
      optionValOids,
    method: "get"
  });
}

// 获取事项情形信息
export function getSituationOpinionList(situationOid) {
  return request({
    url:
      SX +
      "/affair/sxServiceSituationOption/getOptionTitleAndValsOfSituation?situationOid=" +
      situationOid,
    method: "get"
  });
}

// 获取选项关联的标题
export function getOptionTitleAndVals(serviceOid, titleOid, optionValOid) {
  let url =
    SX +
    "/affair/serviceOptionRel/getOptionRelationDataById?serviceOid=" +
    serviceOid;
  if (titleOid != "" && titleOid != undefined) {
    url += "&optionTitleOid=" + titleOid;
  }
  if (optionValOid != "" && optionValOid != undefined) {
    url += "&optionValOid=" + optionValOid;
  }
  return request({
    url: url,
    method: "get"
  });
}

// 获取事项情形信息
export function getSxServiceOptionAllTitleValRelation(serviceOid, titleOid) {
  return request({
    url:
      SX +
      "/affair/sxServiceOptionTitle/getSxServiceOptionAllTitleValRelation?serviceOid=" +
      serviceOid +
      "&titleOid=" +
      titleOid,
    method: "get"
  });
}

// 获取事项信息
export function getSxService(serviceOid) {
  return request({
    url: SX + "/affair/sxService/viewSxService?serviceOid=" + serviceOid,
    method: "get"
  });
}

// 获取办件信息
export function getQlCaseByOid(caseOid) {
  return request({
    url: BJFW + "/qlCaseService/queryQlCaseByCaseOid?caseOid=" + caseOid,
    method: "get"
  });
}

// 获取证件类型
export function getCertificateType(type) {
  return request({
    url: BJFW + "/sysDictService/getSelectCertificateType?type=" + type,
    method: "get"
  });
}

// 获取证件类型
export function changeCredentialType(oid) {
  return request({
    url: DSXBL + "/windowAcceptance/querySysDictByOid?dictOid=" + oid,
    method: "get"
  });
}

// 获取用户信息
export function getloginUser() {
  return request({
    url: BJFW + "/qlCaseService/getLoginUser",
    method: "get"
  });
}

// 获取用户信息
export function queryOptionTitleValStr(valueOid) {
  return request({
    url:
      SX +
      "/affair/sxServiceOptionVal/getSxServiceOptionTitleValStr?oid=" +
      valueOid,
    method: "get"
  });
}

// 保存办件受理信息
export function saveCaseAccpet(data) {
  return request({
    url: BJFW + "/qlCaseService/saveCaseAccpet",
    method: "POST",
    headers: {
      "Content-Type": "application/json;charset=utf-8"
    },
    data: data
  });
}

// 材料出库信息保存
export function saveOut(data) {
  return request({
    url: DSXBL + "/caseMaterialOut/saveOrUpdate",
    method: "POST",
    headers: {
      "Content-Type": "application/json;charset=utf-8"
    },
    data: data
  });
}

// 保存办件信息
export function save(data) {
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
  if (data.bussVenueDistrictOid != "") {
    let districtOid = [];
    let oids = "";
    districtOid = data.bussVenueDistrictOid || [];
    districtOid.forEach(oid => {
      oids += oid + ",";
    });
    qlCaseApply.bussVenueDistrictOid = oids;
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
  if (data.proxyFlag != "") {
    qlCaseExt.proxyFlag = data.proxyFlag;
  }
  if (data.sxServiceMaterialList != "") {
    for (let i = 0; i < data.sxServiceMaterialList.length; i++) {
      materials.push(data.sxServiceMaterialList[i]);
    }
  }
  if (data.qlCaseTitleValList != "" && data.qlCaseTitleValList != undefined) {
    for (let i = 0; i < data.qlCaseTitleValList.length; i++) {
      titleValues.push(data.qlCaseTitleValList[i]);
    }
  }
  // 窗口登记状态
  qlCase.sourceType = 9;
  datas.qlCase = qlCase;
  datas.qlCaseApply = qlCaseApply;
  datas.qlCaseExt = qlCaseExt;
  datas.materials = materials;
  datas.titleValues = titleValues;
  return request({
    url: BJFW + "/qlCaseService/nextStepSaveQlCase",
    method: "post",
    headers: {
      "Content-Type": "application/json;charset=utf-8"
    },
    data: datas
  });
}

// 样本材料下载方法
export function downloadAttaSimple(attaOid) {
  return request({
    url: SX + "/sxSys/file/downloadFile/" + attaOid,
    method: "get"
  });
}

// 根据事项id查询材料
export function getSxMaterialByServiceOid(serviceOid) {
  return request({
    url:
      SX +
      "/affair/serviceMaterial/getSxServiceMaterialListByServiceOid/" +
      serviceOid,
    method: "get"
  });
}

// 根据事项id查询是否为告知清单
export function getInformPromiseByServiceOid(serviceOid) {
  return request({
    url:
      DSXBL + "/informPromise/getInformByServiceOid?serviceOid=" + serviceOid,
    method: "get"
  });
}

// 判断是否是失信人员
export function getDishonestPerson(name, cardNumber) {
  return request({
    url:
      process.env.VUE_APP_ZC_ROUTE_PATH +
      "/dishonestPerson/getDishonestPerson?name=" +
      name +
      "&cardNumber=" +
      cardNumber,
    method: "get"
  });
}

// 保存承诺补正信息
export function saveOrUpdateGzBqbz(data) {
  return request({
    url: DSXBL + "/qlCaseCorrection/saveOrUpdateGzBqbz",
    method: "post",
    headers: {
      "Content-Type": "application/json;charset=utf-8"
    },
    data: data
  });
}

//保存表单信息
export function updateFormCase(data) {
  return request({
    url: BJFW + "/qlCaseService/updateFormInfo",
    method: "post",
    headers: {
      "Content-Type": "application/json;charset=utf-8"
    },
    data: data
  });
}

//保存表单信息
export function saveOrUpdateInfo(data) {
  return request({
    url: DSXBL + "/yanshi/saveOrUpdateInfo",
    method: "post",
    headers: {
      "Content-Type": "application/json;charset=utf-8"
    },
    data: data
  });
}

//查询表单信息
export function getCaseFormInfo(caseOid) {
  return request({
    url: DSXBL + "/yanshi/getCaseFormInfo?caseOid=" + caseOid,
    method: "get"
  });
}

//查询信息
export function getFileDownPath(obj) {
  return request({
    url: DSXBL + "/yanshi/acceptedPrint",
    method: "post",
    headers: {
      "Content-Type": "application/json;charset=utf-8"
    },
    data: obj
  });
}

export function downloadPrintFile(filePath) {
  return request({
    url: URL_PRE() + "/Download?url=" + filePath,
    method: "get"
    /*headers:{
           "Content-Type":"application/json;charset=utf-8",
         },*/
  });
}

//查询电子表单列表信息
export function selectBySxSerFormByServiceOid(obj) {
  return request({
    url:
      process.env.VUE_APP_DSXBL_ROUTE_PATH +
      "/formRunConfiguration/selectBySxSerFormByServiceOid?serviceOid=" +
      obj,
    method: "get",
    headers: {
      "Content-Type": "application/json;charset=utf-8"
    },
    data: obj
  });
}

//查询一个电子表单信息
export function selectOneSxSerFormByOid(oid) {
  return request({
    url:
      process.env.VUE_APP_DSXBL_ROUTE_PATH +
      "/formRunConfiguration/getSxSerFormByOid?sxSerFormOid=" +
      oid,
    method: "get"
  });
}

// 1、获取热门情形接口
export const getSxServiceHotSituations = params => {
  return request.get(
    SX + "/affair/sxServiceSituation/getSxServiceHotSituations",
    { params }
  );
};

// 3、默认自定义情形查询当前事项下标题没有跟任何其他选项值关联的数据
export const getSxServiceTitlesNoRelation = params => {
  return request.get(SX + "/affair/sxService/getSxServiceTitlesNoRelation", {
    params
  });
};

// 4、默认自定义情形查询点击标题下选项值，查询出相关联的标题信息
export const getSxServiceTitlesByRelationOids = params => {
  return request.get(SX + "/affair/sxService/querySxServiceSituationRelation", {
    params
  });
};

//查询当前登录人的存储信息
export function getRegUserInfoByUserOid() {
  return request({
    url:
      process.env.VUE_APP_ZC_ROUTE_PATH +
      "/reguserInfo/getRegUserInfoByUserOid",
    method: "get"
  });
}

//保存当前登录人的存储信息
export function saveRegUserInfo(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + "/reguserInfo/saveOrUpdate",
    method: "post",
    data: data
  });
}

export function queryQlCaseByCaseOid(caseOid) {
  return request({
    url:
      process.env.VUE_APP_BJFW_ROUTE_PATH +
      "/qlCaseService/queryQlCaseByCaseOid?caseOid=" +
      caseOid,
    method: "get"
  });
}
//根据办件业务主键查询办件申请信息
export function queryQlCaseApplayByCaseOid(caseOid) {
  return request({
    url:
      process.env.VUE_APP_BJFW_ROUTE_PATH +
      "/qlCaseApplayService/queryQlCaseApplayByCaseOid?caseOid=" +
      caseOid,
    method: "get"
  });
}

export function downGzcnsWord(obj) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + "/yanshi/printGzWord",
    method: "post",
    headers: {
      "Content-Type": "application/json;charset=utf-8"
    },
    data: obj
  });
}

export function listSxConditionalRules(data) {
  return request({
    url: ZC + "/windows/fastApproval/listSxConditionalRules",
    method: "post",
    data: data
  });
}

export function executeInterApiCode(code, data) {
  return request({
    url: ZC + "/windows/fastApproval/execute/" + code,
    method: "post",
    data: data
  });
}
