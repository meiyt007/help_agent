// 材料分类
import request from '@/utils/request';
const TIMEOUT = 6 * 60 * 1000;
const REQUEST_PREFIX = process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH;
/**
 * 获取材料电子化列表
 * @param {string} caseOid 办件oid eq: 9ef8754efede4a51924c711cd4678c2a
 */
export function getElecMaterialsList (params) {
    return request.get(REQUEST_PREFIX + '/comboClassify/getComboCaseMaterialList', { params, timeout: 60000 });
}

/**
 * 更新材料列表（电子化页面）
 * @param {array} comboCaseMaterialList 材料列表
 */
export function updateComboCaseMaterialList (params) {
    return request.post(REQUEST_PREFIX + '/combo/caseMaterial/updateComboCaseMaterialList', params);
}

/**
 * 获取办件材料列表
 * @param {string} caseOid 办件oid eq: 9ef8754efede4a51924c711cd4678c2a
 */
export function getAllComboCaseMaterialList (params) {
    return request.get(REQUEST_PREFIX + '/comboClassify/getAllComboCaseMaterialList', { params });
}

/**
 * 上传附件
 * @param {object:form} MultipartFile[] files
 */
export function uploadFile (params) {
    return request.post(REQUEST_PREFIX + '/comboCase/atta/uploadFile', params);
}

/**
 * 附件去黑边接口
 * @param {string} baseValue
 */
export function getEditImageBase64 (params) {
    return request.post(REQUEST_PREFIX + '/comboClassify/getEditImageBase64', params);
}

/**
 * 材料自动分类接口
 * @param {string} caseOid
 * @param {string} attaOid
 * @param {string} comboDirectoryOid
 * @param {string} comboDirectoryName
 * @param {string} fastdfsNginxUrl
 */
export function getClassifilerResult (params) {
    return request.get(REQUEST_PREFIX + '/comboClassify/getClassifilerResult', { params });
}

/**
 * 保存材料附件
 * @param {array} qlCaseMaterialList
 */
export function saveComboCaseMaterialAttaList (params) {
    return request.post(REQUEST_PREFIX + '/comboCase/materialAtta/saveComboCaseMaterialAttaList', params);
}

/**
 * 保存后进行材料预审
 * @param {string} caseOid
 */
export function intelligentPretrialmaterialPrePrialAll (params) {
    return request.get(REQUEST_PREFIX + '/comboPreTrial/intelligentPretrialmaterialPrePrialAll', { params, timeout: TIMEOUT });
}

/**
 * 修改后根据材料caseMaterialOid进行材料预审
 * @param {string} caseMaterialOid
 */
export function intelligentPretrialmaterialPrePrialByCaseMaterialOid (params) {
    return request.get(REQUEST_PREFIX + '/comboPreTrial/intelligentPretrialmaterialPrePrialByCaseMaterialOid', { params, timeout: TIMEOUT });
}

/**
 * 获取办件材料列表和审核结果
 * @param {string} caseOid
 */
export function getComboCaseMaterialListAndAuditResult (params) {
    return request.get(REQUEST_PREFIX + '/comboPreTrial/getComboCaseMaterialListAndAuditResult', { params });
}

/**
 * 查询办件材料详细审核结果
 * @param {string} caseOid 办件oid
 * @param {string} caseMaterialOid 办件材料oid
 * @param {string} materialOid 材料oid
 * @param {string} serviceOid 事项oid
 */
export function viewDetailResult (params) {
    return request.get(REQUEST_PREFIX + '/comboPreTrial/viewDetailResult', { params });
}

/**
 * 查看页面进行确认功能
 * @param {string} caseMaterialOid
 */
export function confirmComboCaseMaterial (params) {
    return request.get(REQUEST_PREFIX + '/comboPreTrial/confirmComboCaseMaterial', { params, timeout: TIMEOUT });
}

/**
 * 通过办件材料oid查询材料列表
 * @param {string} caseMaterialOid
 */
export function getComboCaseMaterialAttaList (params) {
    return request.get(REQUEST_PREFIX + '/comboClassify/getComboCaseMaterialAttaList', { params });
}

/** 补齐补正 容缺后补 更新材料信息接口 */
/**
 * 更新材料信息接口
 * @param {array} ComboCaseMaterial  材料办件列表
 */
export function updateComboCaseMateriallList (params) {
    return request.post(REQUEST_PREFIX + '/comboCase/materialAtta/updateComboCaseMaterialAttaList', params);
}

/** 获取分类材料参数 */
export function getClassifilerMateial (params) {
    return request.post(REQUEST_PREFIX + '/comboClassify/getClassifilerMateial', params);
}

/** 材料自动矫正分类接口 */
export function getEditImageAndClassifilerResult (params) {
    return request.post(REQUEST_PREFIX + '/comboClassify/getEditImageAndClassifilerResult', params, { timeout: 60000 });
}

//删除
export function deleteByComboMaterialOid (materialAttaOid) {
    return request({
        url: process.env.REQUEST_PREFIX + '/comboCase/materialAtta/deleteComboCaseMaterialAttaByOid?materialAttaOid=' + materialAttaOid,
        method: 'get'
    })
}