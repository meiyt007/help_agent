// author:ningzz
import request from '@/utils/request';

// 一件事办理-已办业务，获取列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseDone/page',
    method: 'get',
    params: query
  })
}

// 一件事办理-已办业务-查看
export function get(id) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseDone/get/'+id,
    method: 'get'
  })
}

// 获取详情
export function getOneByCaseOidForYbyw(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseDone/getOneByCaseOidForYbyw?caseOid=' + caseOid,
    method: 'get',
  })
}

