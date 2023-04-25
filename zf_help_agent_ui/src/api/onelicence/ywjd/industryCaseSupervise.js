// author:ningzz
import request from '@/utils/request';

export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseSupervise/page',
    method: 'get',
    params: query
  })
}

export function get(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseSupervise/queryIndustryCaseByCaseOid/'+caseOid,
    method: 'get'
  })
}

// 获取机构用户信息
export function getOrganUserList() {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH +'/industryCaseSupervise/getOrganUserList',
    method: 'get'
  })
}

// 获取区划机构用户信息
export function queryUserSimpleTree(organOid) {
  if(organOid == undefined){
    organOid = '';
  }
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH +'/industryCaseSupervise/queryUserSimpleTree?districtOid='+organOid,
    method: 'post',
  })
}
