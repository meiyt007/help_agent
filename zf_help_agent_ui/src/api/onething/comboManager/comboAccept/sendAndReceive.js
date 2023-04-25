import request from '@/utils/request';

// 一件事办理-办结业务，获取列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/sendAndReceive/page',
    method: 'get',
    params: query
  })
}

// 一件事办理-办结业务-查看、签收 发证信息
export function get(id) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/sendAndReceive/get/'+id,
    method: 'get'
  })
}


//签收信息
export function getQS(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/caseLicenseManageCombo/getOneByCaseOid?caseOid='+caseOid,
    method: 'Post'
  })
}


//发证信息
export function getFZ(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/licenseIssuedCombo/getLicenseIssuedComboByCaseOid?caseOid='+caseOid,
    method: 'post'
  })
}
