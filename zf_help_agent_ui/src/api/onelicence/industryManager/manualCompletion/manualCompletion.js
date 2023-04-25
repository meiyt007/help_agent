// author:ningzz
import request from '@/utils/request';

export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/manualCompletion/page',
    method: 'get',
    params: query
  })
}

export function get(id) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/manualCompletion/get/'+id,
    method: 'get'
  })
}
// 获取办件信息
export function getQlCaseByOid(caseOid) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseService/queryQlCaseByCaseOid?caseOid='+caseOid,
    method: 'get'
  })
}

export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/manualCompletion/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}
