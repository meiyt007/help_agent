import request from '@/utils/request';

// 分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/caseReturnService/queryCaseReturnList',
    method: 'get',
    params: query
  })
}


export function saveCaseReturn(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/caseReturnService/saveCaseReturn',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//保存或者修改
export function outReturnFile(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/caseReturnService/outReturnFile',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//根据办件oid查询
export function queryCaseReturnByCaseOid(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/caseReturnService/queryCaseReturnByCaseOid?caseOid='+caseOid,
    method: 'get'
  })
}

//保存或者修改退件告知
export function saveOrUpdateInfrom(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/caseReturnService/saveCaseReturnInform',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}


// 获取证件类型
export function getCertificateType(type) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/commonService/getSelectCertificateType?type='+type,
    method: 'get'
  })
}

//根据办件编号查询退件附件
export function getOneRetuenCaseByCaseNumber(caseNumber) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/caseReturnService/getOneRetuenCaseByCaseNumber?caseNumber='+caseNumber,
    method: 'get'
  })
}
