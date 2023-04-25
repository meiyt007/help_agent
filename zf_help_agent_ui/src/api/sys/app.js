import request from '@/utils/request'
import { getToken } from '@/utils/auth'

// 查询应用信息列表
export function page(query) {
  return request({
    url: '/platform/security/app/page',
    method: 'get',
    params: query
  })
}

// 初始化应用信息
export function init(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: '/platform/security/app/init/'+oid,
    method: 'get'
  })
}

// 应用的新增或者修改
export function save(data) {
  return request({
    url: '/platform/security/app/save',
    method: 'post',
    data: data
  })
}

//删除应用管理
export function del(oid) {
  return request({
    url: '/platform/security/app/delete/' + oid,
    method: 'post'
  })
}

// 启禁用应用信息
export function able(oid) {
  return request({
    url: '/platform/security/app/able/'+oid,
    method: 'post'
  })
}

// 获取单个应用信息
export function getOne(oid) {
  return request({
    url: '/platform/security/app/getOne/'+oid,
    method: 'get'
  })
}

// 导出应用信息
export function listExp(query) {
  const  baseURL = process.env.VUE_APP_BASE_API;
  window.location.href = baseURL + '/platform/security/app/listExp?name='+encodeURI(query.name) + '&pageNum='+query.pageNum + '&pageSize='+query.pageSize + '&access_token=' +  getToken();
}
//应用列表
export function applist(query) {
  return request({
    url: '/platform/security/app/list',
    method: 'get',
    params: query
  })
}
