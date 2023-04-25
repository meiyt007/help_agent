import request from '@/utils/request'

var applicationName = process.env.VUE_APP_FORM_API_ROUTE_PATH + '/manager/security/authorize';

// 查询接入系统授权信息列表
export function page(query) {
  return request({
    url: applicationName + '/page',
    method: 'get',
    params: query
  })
}

// 初始化接入系统授权信息
export function init(id) {
  if(id == undefined){
    id ='';
  }
  return request({
    url: applicationName + '/init?id='+id,
    method: 'get'
  })
}

// 接入系统授权的新增或者修改
export function save(data) {
  return request({
    url: applicationName + '/save',
    method: 'post',
    data: data
  })
}

//删除接入系统授权管理
export function del(id) {
  return request({
    url: applicationName + '/delete/' + id,
    method: 'delete'
  })
}

// 启禁用接入系统授权信息
export function able(id) {
  return request({
    url: applicationName + '/able/'+id,
    method: 'post'
  })
}

// 获取单个接入系统授权信息
export function getOne(id) {
  return request({
    url: applicationName + '/getOne/'+id,
    method: 'get'
  })
}


//接入系统授权列表
export function authorizelist(query) {
  return request({
    url: applicationName + '/list',
    method: 'get',
    params: query
  })
}

// 获取表单接入系统树信息
export function queryAuthorizeTree(userOid) {
  if(undefined == userOid){
    userOid = '';
  }
  return request({
    url: applicationName + '/queryAuthorizeTree?userOid='+userOid,
    method: 'get'
  })
}
