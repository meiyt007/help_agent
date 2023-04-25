import request from '@/utils/request';

const HTTP_PREFIX = process.env.VUE_APP_THING_ROUTE_PATH;

/** 
 * @param {string} comboDirectoryOid  
 */
export function queryNoRelSituationListByDireOid (params) {
  return request.get(HTTP_PREFIX + '/combo/situation/queryNoRelSituationListByDireOid', { params });
}

/** 
 * @param {string} comboDirectoryOid  
 * @param {string} situationOids  (多个用逗号,隔开)  
 * @param {string} valOid (多个用逗号,隔开)
 * @param {string} checkOids string (多个用逗号,隔开)  
 */
export function queryComboDirectorySituationByValOid (params) {
  return request.get(HTTP_PREFIX + '/combo/situation/queryComboDirectorySituationByValOid', { params });
}

/** 获取文本验证信息 */
export function getOptionValBySituOid (params) {
  return request.get(HTTP_PREFIX + '/combo/OptionVal/getOptionValBySituOidAndTextVal', { params });
}

/** 阻塞情形
 * @param {string} comboDirectoryOid 
 * @param {string} selectValOids(多个用逗号,隔开)
 */
export function judgeStopCondition (params) {
  return request.get(HTTP_PREFIX + '/combo/optionWarning/judgeStopCondition', { params });
}

/** 查询材料信息接口
 * @param {string} direOid
 * @param {string} valOidS
 */
export function queryMaterialListByDireOidAndValOids (params) {
  return request.get(HTTP_PREFIX + '/combo/directoryMaterial/queryMaterialListByDireOidAndValOids', { params });
}


/** 标签信息
 * @param {string} comboDirectoryOid 
 * @param {string} selectValOids 
 */
export function getAllInformLableByVals (params) {
  return request.get(HTTP_PREFIX + '/combo/inform/getAllInformLableByVals', { params });
}

/** 保存办件情形选项信息
 * @param {string} caseOid // 为空时自动生成 
 * @param {array} caseSituationList  
 * @param {array} caseOptionValList  
 * @param {array} caseSituTextList 
 */
export function saveCaseSituation (params) {
  return request.post(process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/caseSituation/save', params);
}

/** 暂存受理页面获取情形信息
 * @param {string} caseOid
 */
export function getCaseSituationResultByCaseOid (params) {
  return request.get(process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/caseSituation/getCaseSituationResultByCaseOid', { params });
}