import request from '@/utils/request';

// 分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseLicenseManager/pageList',
    method: 'post',
    params: query
  })
}

//保存或者修改
export function saveOrUpdateSign(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseLicenseManager/saveOrUpdateSign',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//查询一件事目录证照配置信息
export function queryIndustryDirectoryResult(industryCaseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseLicenseManager/queryIndustryDirectoryResult?industryCaseOid='+industryCaseOid,
    method: 'get'
  })
}

//通知发送短信
export function noticeSendMessage(industryCaseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseLicenseManager/noticeSendMessage?industryCaseOid='+industryCaseOid,
    method: 'get'
  })
}


export function queryElementList(materialCatalogOid){
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/combo/industryElement/queryIndustryBlockElementList/'+materialCatalogOid,
    method: 'get'
  })
}


export function getRequestUrl(code){
  return request({
    url:  '/settings/security/config/getSysConfigByCode?code='+code,
    method: 'get'
  })
}

//查询证照签收记录
export function queryIndustrySignByCaseOid(industryCaseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseLicenseManager/queryIndustrySignByCaseOid?industryCaseOid='+industryCaseOid,
    method: 'get'
  })
}
//保存证照打印记录
export function savePrintRecord(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseLicenseManager/saveLicensePrintRecord',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//查询证照签收记录
export function queryIndustrySignByOid(oid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseLicenseManager/queryIndustrySignByOid?oid='+oid,
    method: 'get'
  })
}


//查询证照签收记录
export function queryIndustrySignByMaps(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseLicenseManager/queryIndustrySignByMaps',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 获取详情
export function getLicenseListByCaseNumber(caseNumber) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseLicenseManager/selectByCaseNumber?caseNumber=' + caseNumber,
    method: 'get',
  })
}

//保存或者修改快递签发
export function saveOrUpdateKd(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseLicenseManager/saveOrUpdateKd?oid='+data.oid+"&kdCode="+data.kdCode+"&addresseeName="+data.addresseeName+"&addresseePhone="+data.addresseePhone,
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    }
  })
}

// 分页查询列表操作
export function pageListIssued(query) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseLicenseManager/pageListIssued',
    method: 'post',
    params: query
  })
}

// 获取详情
export function getLicenseTemplateByOid(resultOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseLicenseManager/getLicenseTemplateByOid?resultOid=' + resultOid,
    method: 'get',
  })
}










