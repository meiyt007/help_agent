import request from "@/utils/request";

// 获取机构分组列表
export function queryOrganSelectOptions() {
  return request({
    // url:'/paltform/security/organ/queryOrganSelectOptions?serviceOid='+serviceOid,
    url: process.env.VUE_APP_SX_ROUTE_PATH + "/affair/sxService/queryOrganSelectOptions",
    method: "get"
  });
}

//同步万达事项库相关信息
export function dataSynchronization() {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + "/work/Matter/dataSynchronization",
    method: "post"

  });
}

//初始化知识库
export function initKnowledge() {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + "/ha/knowledge/init",
    method: "get"

  });
}
//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + "/affair/sxService/sxServicePage",
    method: "post",
    params: query
  });
}
//查询推荐关联事项列表
export function pageRecommendList(query) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/sxService/getSxRecommendServiceListByOid",
    method: "post",
    params: query
  });
}

//删除
export function delSxServiceByOid(id) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/sxService/delSxServiceByOid/" +
      id,
    method: "get"
  });
}

//获取事项详情
export function getSxServiceByOid(id) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/sxService/getSxServiceAndExtend/" +
      id,
    method: "get"
  });
}

// 保存或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + "/affair/sxService/saveSxService",
    method: "post",
    headers: {
      "Content-Type": "application/json;charset=UTF-8"
    },
    data: data
  });
}

// 保存前置核验
export function saveOrUpdateList(data) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/sxServicePrecheck/saveOrUpdateList",
    method: "post",
    headers: {
      "Content-Type": "application/json;charset=UTF-8"
    },
    data: data
  });
}

// 查询前置核验
export function querySxServicePrecheckList(data) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/sxServicePrecheck/querySxServicePrecheckList",
    method: "post",
    headers: {
      "Content-Type": "application/json;charset=UTF-8"
    },
    data: data
  });
}

// 查询前置核验
export function getSxServicePrecheck(data) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/sxServicePrecheck/getSxServicePrecheck",
    method: "get",
    headers: {
      "Content-Type": "application/json;charset=UTF-8"
    },
    params: data
  });
}

// 查询前置核验新接口
export function listSxConditionalRules(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + "/browser/listSxConditionalRules",
    method: "post",
    headers: {
      "Content-Type": "application/json;charset=UTF-8"
    },
    params: data
  });
}

//条件预检————验证预检是否通过
export function checkPreInspection(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + "/windows/execute/userNameLength",
    method: "post",
    headers: {
      "Content-Type": "application/json;charset=UTF-8"
    },
    data: data
  });
}

//事项字典值
export function getDistInfo() {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + "/affair/sxService/getDistInfo",
    method: "get"
  });
}

// 附件上传
export function uploadsxFile(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + "/sxSys/atta/uploadFile",
    method: "POST",
    headers: {
      "Content-Type": "multipart/form-data"
    },
    data: data
  });
}
//附件下载
export function downLoadFile(attaOid) {
  window.location.href =
    process.env.VUE_APP_SERVICE_API +
    "/sxSys/atta/downloadFile?attaOid=" +
    attaOid;
}
//情形列表
export function pageSituation(query) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/sxServiceSituation/queryxHotSituationsPag",
    method: "post",
    params: query
  });
}

//选项配置列表 add by shimh 2021-08-04
export function pageOption(query) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/sxServiceOptionTitle/querySxServiceOptionTitlePag",
    method: "post",
    params: query
  });
}

//细化材料列表
export function pageMaterialSplitList(materialOid) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/refindeMaterial/getRefinedMaterialListByMaterialOid?materialOid=" +
      materialOid,
    method: "get"
  });
}
//保存细化材料
export function saveOrUpdateMaterialXh(data) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/refindeMaterial/saveOrUpdateRefinedMaterial",
    method: "post",
    data: data
  });
}

//细化材料详细信息
export function getRefinedMaterialDetail(oid) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/refindeMaterial/getRefinedMaterialByOid?oid=" +
      oid,
    method: "get"
  });
}

//细化材料删除
export function deleteMaterialSplit(id) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/refindeMaterial/delete?id=" +
      id,
    method: "get",
    params: id
  });
}

//查询事项下面的标题和选项值
export function pageTitleAndOption(serviceOid) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/sxServiceOptionTitle/getListTitleAndOption?serviceOid=" +
      serviceOid,
    method: "get"
  });
}

//查询事项下面所有材料
export function sxMaterialList(serviceOid) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/serviceMaterial/getSxServiceMaterialByServiceOid?serviceOid=" +
      serviceOid,
    method: "get"
  });
}

//查询选项关联的所有材料
export function valMaterialList(valOid) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/sxServiceMateOptRel/getSxServiceMateOptRelsByOptionValOid?valOid=" +
      valOid,
    method: "get"
  });
}

//保存材料关系信息
export function saveOrUpdateMaterOptRel(query) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/sxServiceMateOptRel/saveOrUpdateMaterOptRel",
    method: "post",
    params: query
  });
}

//查询标题选项材料信息
export function getListTitleAndOptionMaterial(serviceOid) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/sxServiceOptionTitle/getListTitleAndOptionMaterial?serviceOid=" +
      serviceOid,
    method: "get"
  });
}
//关系选项图
export function getRelationJsonForPicture(serviceOid, titleOid, valOid) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/sxServiceOptionTitle/getRelationJsonForPicture?serviceOid=" +
      serviceOid +
      "&titleOid=" +
      titleOid +
      "&valOid=" +
      valOid,
    method: "get"
  });
}

//关系配置
export function relationList(query) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/serviceOptionRel/relationList",
    method: "post",
    params: query
  });
}
//根据选项关系主键查询信息
export function getServiceOptionRelByOid(relOid) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/serviceOptionRel/getServiceOptionRelByOid/" +
      relOid,
    method: "get"
  });
}

//保存关系配置
export function saveOrUpdateServiceOptionRel(data) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/serviceOptionRel/saveOrUpdateServiceOptionRel",
    method: "post",
    data: data
  });
}

//删除关系配置
export function delBatchRel(relOids) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/serviceOptionRel/delBatchRel?relOids=" +
      relOids,
    method: "get"
  });
}

export function getOptionMaterialByValOid(valOid) {
  return request({
    url:
      process.env.VUE_APP_THING_ROUTE_PATH +
      "/combo/optionMaterial/getOptionMaterialByValOid?valOid=" +
      valOid,
    method: "get"
  });
}

// 查询事项是否被授权
export function getServiceRegistrarByServiceOid(serviceOid) {
  return request({
    url:
      process.env.VUE_APP_DSXBL_ROUTE_PATH +
      "/sxServiceRegistrar/getServiceRegistrarByServiceOid?serviceOid=" +
      serviceOid,
    method: "post"
  });
}
//更新材料样本
export function saveOrUpdateMaterialSample(materialOid) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/affair/serviceMaterial/saveOrUpdateMaterialSample?materialOid=" +
      materialOid,
    method: "get"
  });
}

// 查询事项表单信息
export function getSxFormList(serviceOid) {
  return request({
    url:
      process.env.VUE_APP_SX_ROUTE_PATH +
      "/matter/formInfo/getSxFormInfoList?serviceOid=" +
      serviceOid,
    method: "get"
  });
}

// 查询事项表单模板
export function getTemplateByFormCode(formCode) {
  return request({
    url:
      process.env.VUE_APP_FORM_API_ROUTE_PATH +
      "/manager/security/docx/template/queryDocxTemplateList?formCode=" +
      formCode,
    method: "get"
  });
}
