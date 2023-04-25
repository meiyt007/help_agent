import request from '@/utils/request';


//保存或者修改签发
export function saveOrUpdateIssued(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/licenseIssuedIndustry/saveIndustryLicenseIssued',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data:data
  })
}

//根据办件业务主键查询签发信息
export function getLicenseIssuedIndustryByCaseOid(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/licenseIssuedIndustry/getIndustryLicenseIssuedByCaseOid?caseOid='+caseOid,
    method: 'get'
  })
}

//根据签发信息业务主键查询发证信息
export function getIndustryIssuedBySignOid(signOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/licenseIssuedIndustry/getIndustryIssuedBySignOid?signOid='+signOid,
    method: 'get'
  })
}

// 获取证件类型
export function getCertificateType(type) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/commonService/getSelectCertificateType?type='+type,
    method: 'get'
  })
}
//根据业务主键查询信息
export function getLicenseIssuedIndustryByOid(oid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/licenseIssuedIndustry/getIndustryLicenseIssuedByOid?oid='+oid,
    method: 'get'
  })
}

//根据签发信息
export function saveOrUpOutOfStock(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/licenseIssuedIndustry/saveOrUpOutOfStock',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data:data
  })
}

//根据签发信息业务主键查询发证信息
export function getLicenseIssuedByCaseOid(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/licenseIssuedIndustry/getLicenseIssuedIndustryByCaseOid?caseOid='+caseOid,
    method: 'get'
  })
}

//扫描身份证获取身份证信息
export function getIdCard() {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/interface/idcardOpenAndGetData',
    method: 'get'
  })
}
