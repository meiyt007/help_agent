/**
 * 套餐办件材料智审
 * @author: liangss
 * @date: 2020-12-24
 */
import request from '@/utils/request';


//套餐办件材料预审
export function intelligentPretrialmaterialPrePrial(caseOid,caseFileRecOid,caseMaterialOid,cataOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboPreTrial/intelligentPretrialmaterialPrePrial',
    params: {
      caseOid: caseOid,
      caseFileRecOid:caseFileRecOid,
      caseMaterialOid:caseMaterialOid,
      cataOid:cataOid,
    },
    method: 'get'
  })
}


//套餐办件材料审核
export function intelligentPretrial(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboPreTrial/intelligentPretrial',
    params: {
      pacCaseOid: caseOid,
    },
    method: 'get'
  })
}

//审核结果
export function viewResult(caseOid,caseFileRecOid,caseMaterialOid,cataOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboPreTrial/viewResult',
    params: {
      caseOid: caseOid,
      caseFileRecOid:caseFileRecOid,
      caseMaterialOid:caseMaterialOid,
      catalogOid:cataOid,
    },
    method: 'get'
  })
}



