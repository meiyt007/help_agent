import request from '@/utils/request';

// 分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/caseLicenseManageCombo/pageListIssued',
    method: 'post',
    params: query
  })
}

//保存或者修改签发
export function saveOrUpdateIssued(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/licenseIssuedCombo/saveLicenseIssuedCombo',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

export function getOneByCaseOid(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/caseLicenseManageCombo/getOneByCaseOid?caseOid='+caseOid,
    method: 'post'
  })
}

//保存或者修改快递签发
export function saveOrUpdateKd(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/caseLicenseManageCombo/saveOrUpdateKd?oid='+data.oid+"&kdCode="+data.kdCode+"&addresseeName="+data.addresseeName+"&addresseePhone="+data.addresseePhone,
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    }
  })
}
//根据办件业务主键查询签发信息
export function getLicenseIssuedComboByCaseOid(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/licenseIssuedCombo/getLicenseIssuedComboByCaseOid?caseOid='+caseOid,
    method: 'get'
  })
}

// 获取证件类型
export function getCertificateType(type) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/common/getSelectCertificateType?type='+type,
    method: 'get'
  })
}

//根据签发信息
export function saveOrUpOutOfStock(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/licenseIssuedCombo/saveOrUpOutOfStock',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data:data
  })
}

// 获取详情
export function getOneByCaseNumber(caseNumber) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/caseLicenseManageCombo/selectByCaseNumber?caseNumber=' + caseNumber,
    method: 'post',
  })
}

//查询证照签收记录
export function queryComboCaseSignByOid(oid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/licenseIssuedCombo/queryComboCaseSignByOid?oid='+oid,
    method: 'get'
  })
}

//根据签发信息业务主键查询发证信息
export function getComboCaseIssuedBySignOid(signOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/licenseIssuedCombo/getComboCaseIssuedBySignOid?signOid='+signOid,
    method: 'get'
  })
}

// 获取详情
export function getComboLicenseListByCaseNumber(caseNumber) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/licenseIssuedCombo/selectByComboCaseNumber?caseNumber=' + caseNumber,
    method: 'get',
  })
}

//根据签发信息业务主键查询发证信息
export function getComboCaseIssuedByCaseOid(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/licenseIssuedCombo/getComboCaseIssuedByCaseOid?caseOid='+caseOid,
    method: 'get'
  })
}






