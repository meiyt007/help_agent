// author:ningzz
import request from '@/utils/request';

// 一件事办理-已办业务，获取列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboCaseDone/page',
    method: 'get',
    params: query
  })
}

// 一件事办理-已办业务-查看
export function get(id) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboCaseDone/get/'+id,
    method: 'get'
  })
}

// 获取详情
export function getOneByCaseOidForYbyw(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/getOneByCaseOidForYbyw?caseOid=' + caseOid,
    method: 'get',
  })
}

// 获取签名图片
export function querySignImgPath(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/signRecord/querySignImgPath?caseOid=' + caseOid,
    method: 'get',
  })
}
