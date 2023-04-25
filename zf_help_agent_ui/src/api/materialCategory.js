// 材料分类
import request from "@/utils/request";
const TIMEOUT = 5 * 60 * 1000;

/**
 * 获取材料列表
 * @param {string} caseOid 办件oid
 */
export function getMaterialsList(params) {
  return request.get(
    process.env.VUE_APP_DSXBL_ROUTE_PATH + "/classify/getQlCaseMaterialList",
    { params }
  );
}

/**
 * 获取办件材料列表
 * @param {string} caseOid 办件oid
 */
export function getAllQlCaseMaterialList(params) {
  return request.get(
    process.env.VUE_APP_DSXBL_ROUTE_PATH + "/classify/getAllQlCaseMaterialList",
    { params }
  );
}

/**
 * 材料自动分类接口
 * @param {string} caseOid 办件oid
 * @param {string} attaOid 附件oid
 */
export function materialClassifyPrePrial(params) {
  return request.get(
    process.env.VUE_APP_DSXBL_ROUTE_PATH + "/classify/getClassifilerByYjy",
    { params }
  );
}

/**
 * 材料自动分类接口
 * @param {string} caseOid 办件oid
 * @param {string} attaOid 附件oid
 * @param {string} serviceOid 事项oid
 * @param {string} serviceName 事项名称
 * @param {string} fastdfsNginxUrl 地址
 */
export function getClassifilerResult(params) {
  return request.get(
    process.env.VUE_APP_DSXBL_ROUTE_PATH + "/classify/getClassifilerResult",
    { params }
  );
}

/**
 * 材料预审
 * @param {string} caseOid 办件oid
 */
export function intelligentPretrialmaterialPrePrialAllNew(params) {
  return request.get(
    process.env.VUE_APP_DSXBL_ROUTE_PATH +
      "/preTrial/intelligentPretrialmaterialPrePrialAllNew",
    { params, timeout: TIMEOUT }
  );
}

/**
 * 智审材料预审
 * @param {string} caseMaterialOid
 */
export function intelligentPretrialmaterialPrePrialByCaseMaterialOid(params) {
  return request.get(
    process.env.VUE_APP_DSXBL_ROUTE_PATH +
      "/preTrial/intelligentPretrialmaterialPrePrialByCaseMaterialOid",
    { params, timeout: TIMEOUT }
  );
}

/**
 * 材料审核列表
 * @param {string} caseOid 办件oid
 */
export function getMaterialPrePrialNew(params) {
  return request.get(
    process.env.VUE_APP_DSXBL_ROUTE_PATH + "/preTrial/intelligentPretrial",
    { params }
  );
}

/**
 * 查看材料审核结果
 * @param {string} caseOid 办件oid
 */
export function getViewResult(params) {
  return request.get(
    process.env.VUE_APP_DSXBL_ROUTE_PATH + "/preTrial/viewResult",
    { params }
  );
}

/**
 * 上传附件
 * @param {array} data MultipartFile[] files
 */
export function uploadMaterialsFile(params) {
  return request.post(
    process.env.VUE_APP_BJFW_ROUTE_PATH + "/qlcaseAtta/uploadCaseMaterialFile",
    params
  );
}

/**
 * 保存材料附件
 * @param {array} qlCaseMaterialAttaList 材料办件列表
 */
export function saveQlCaseMaterialAttaList(params) {
  return request.post(
    process.env.VUE_APP_BJFW_ROUTE_PATH +
      "/qlCaseMaterialAttaService/saveQlCaseMaterialList",
    params
  );
}

/**
 * 保存材料附件
 * @param {array} qlCaseMaterialAttaList 材料办件列表
 */
export function saveOrUpdateCaseMaterialAttaList(params) {
  return request.post(
    process.env.VUE_APP_BJFW_ROUTE_PATH +
      "/qlCaseMaterialAttaService/saveOrUpdateCaseMaterialAttaList",
    params
  );
}

/**
 * 更新材料附件
 * @param {array} qlCaseMaterialList  材料办件列表
 */
export function updateQlCaseMaterialList(params) {
  return request.post(
    process.env.VUE_APP_BJFW_ROUTE_PATH +
      "/qlCaseMaterialService/updateQlCaseMaterialList",
    params
  );
}

/**
 * 获取办件材料列表和审核结果
 */
export function getQlCaseMaterialListAndAuditResult(params) {
  return request.get(
    process.env.VUE_APP_DSXBL_ROUTE_PATH +
      "/preTrial/getQlCaseMaterialListAndAuditResult",
    { params }
  );
}

/**
 * 查询办件材料详细审核结果
 */
export function viewDetailResult(params) {
  return request.get(
    process.env.VUE_APP_DSXBL_ROUTE_PATH + "/preTrial/viewDetailResult",
    { params }
  );
}

//智能问答审查要点
export function viewDetailRefinedMaterial(params) {
  return request.get(
    process.env.VUE_APP_DSXBL_ROUTE_PATH +
      "/preTrial/viewDetailRefinedMaterial",
    { params }
  );
}

/**
 * 去黑边
 * @param data
 * @returns {*}
 */
export function getEditImageBase64(params) {
  return request.post(
    process.env.VUE_APP_DSXBL_ROUTE_PATH + "/classify/getEditImageBase64",
    params
  );
}

// 通过办件获取上传附件数据记录 材料电子化页面查看
export function getAllQlCaseMaterialListByAttaOid(caseOid, userName, idCard) {
  return request({
    url:
      process.env.VUE_APP_DSXBL_ROUTE_PATH +
      "/classify/getAllQlCaseMaterialListByAttaOid",
    method: "get",
    params: {
      caseOid,
      userName,
      idCard,
      timeout: 60000
    }
  });
}

/**
 * 确认材料无误
 * @param {string} caseMaterialOid
 */
export function confirmQlCaseMaterialt(params) {
  return request.get(
    process.env.VUE_APP_DSXBL_ROUTE_PATH + "/preTrial/confirmQlCaseMaterialt",
    { params }
  );
}

/**
 * 通过办件材料oid查询材料列表
 * @param {string} caseMaterialOid
 */
export function getQlCaseMaterialAttaList(params) {
  return request.get(
    process.env.VUE_APP_DSXBL_ROUTE_PATH +
      "/classify/getQlCaseMaterialAttaList",
    { params }
  );
}

/** 补齐补正 容缺后补 更新材料信息接口 */
/**
 * 更新材料信息接口
 * @param {array} QlCaseMaterialAtta  材料办件列表
 */
export function updateCaseMaterialAttaList(params) {
  return request.post(
    process.env.VUE_APP_BJFW_ROUTE_PATH +
      "/qlCaseMaterialAttaService/updateCaseMaterialAttaList",
    params
  );
}

/** 获取分类材料参数 */
export function getClassifilerMateial(params) {
  return request.post(
    process.env.VUE_APP_DSXBL_ROUTE_PATH + "/classify/getClassifilerMateial",
    params
  );
}

/** 材料自动矫正分类接口 */
export function getEditImageAndClassifilerResult(params) {
  return request.post(
    process.env.VUE_APP_DSXBL_ROUTE_PATH +
      "/classify/getEditImageAndClassifilerResult",
    params,
    { timeout: 60000 }
  );
}

/** 根据分类查询排序, 并返回排序好的id
 * @param {string} idList 逗号分割字符串
 */
export function getFileSortingInformation(params) {
  return request.get(
    process.env.VUE_APP_DSXBL_ROUTE_PATH +
      "/classify/getFileSortingInformation",
    { params }
  );
}

//删除
export function deleteByMaterialOid(materialAttaOid) {
  return request({
    url:
      process.env.VUE_APP_BJFW_ROUTE_PATH +
      "/qlCaseMaterialAttaService/deleteByOid?materialAttaOid=" +
      materialAttaOid,
    method: "get"
  });
}
