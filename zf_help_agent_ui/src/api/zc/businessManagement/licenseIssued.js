import request from '@/utils/request';

// 分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseLicenseManage/pageListIssued',
    method: 'post',
    params: query
  })
}

//保存或者修改签发
export function saveOrUpdateIssued(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/licenseIssued/saveLicenseIssued',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

export function getOneByCaseOid(caseOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseLicenseManage/getOneByCaseOid?caseOid='+caseOid,
    method: 'post'
  })
}

//保存或者修改快递签发
export function saveOrUpdateKd(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseLicenseManage/saveOrUpdateKd?oid='+data.oid+"&addresseeName="+data.addresseeName+"&addresseePhone="+data.addresseePhone+"&addresseeDetailAddress="+data.addresseeDetailAddress
    +"&addresseeAddress="+data.addresseeAddress+"&addresseePostCode="+data.addresseePostCode+"&addresseeTel="+data.addresseeTel
      +"&sendePerson="+data.sendePerson+"&senderMailCode="+data.senderMailCode+"&senderCall="+data.senderCall
      +"&senderPhone="+data.senderPhone+"&senderAddress="+data.senderAddress+"&senderDetailAddress="+data.senderDetailAddress,
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    }
  })
}
//根据办件业务主键查询签发信息
export function getLicenseIssuedByCaseOid(caseOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/licenseIssued/getLicenseIssuedByCaseOid?caseOid='+caseOid,
    method: 'post'
  })
}

// 获取证件类型
export function getCertificateType(type) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/sysDictService/getSelectCertificateType?type='+type,
    method: 'get'
  })
}
//根据业务主键查询信息
export function getLicenseIssuedByOid(oid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/licenseIssued/getLicenseIssuedByOid?oid='+oid,
    method: 'post'
  })
}

//根据签发信息
export function saveOrUpOutOfStock(oid,barCode) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/licenseIssued/saveOrUpOutOfStock?oid='+oid+"&barCode="+barCode,
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
  })
}

// 获取详情
export function getOneByCaseNumber(caseNumber) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseLicenseManage/selectByCaseNumber?caseNumber=' + caseNumber,
    method: 'post',
  })
}



