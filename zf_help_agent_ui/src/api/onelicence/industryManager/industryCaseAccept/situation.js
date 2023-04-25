import request from '@/utils/request';

/** 保存办件情形选项信息
 * @param {string} caseOid // 为空时自动生成 
 * @param {array} caseSituationList  
 * @param {array} caseOptionValList  
 * @param {array} caseSituTextList 
 */
export function industrysaveCaseSituation (params) {
  return request.post(process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/caseSituation/save', params);
}

/** 暂存受理页面获取情形信息
 * @param {string} caseOid
 */
 export function industrygetCaseSituationResultByCaseOid (params) {
  return request.get(process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/caseSituation/getCaseSituationResultByCaseOid', { params });
}