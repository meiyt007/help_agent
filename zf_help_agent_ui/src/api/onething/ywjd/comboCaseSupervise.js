// author:ningzz
import request from '@/utils/request';

export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboCaseSupervise/page',
    method: 'get',
    params: query
  })
}

export function get(id) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboCaseSupervise/get/'+id,
    method: 'get'
  })
}

// 获取用户信息
export function getloginUser() {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH +'/combo/case/getLoginUser',
    method: 'get'
  })
}

// 获取机构用户信息
export function getOrganUserList() {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH +'/comboCaseSupervise/getOrganUserList',
    method: 'get'
  })
}


// 获取区划机构用户信息
export function queryUserSimpleTree(organOid) {
  if(organOid == undefined){
    organOid = '';
  }
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH +'/comboCaseSupervise/queryUserSimpleTree?districtOid='+organOid,
    method: 'post',
  })
}
