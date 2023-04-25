import request from '@/utils/request'

var applicationName = process.env.VUE_APP_FORM_API_ROUTE_PATH + '/manager/security/member';

// 查询模块信息列表
export function page(query) {
  return request({
    url: applicationName + '/page',
    method: 'get',
    params: query
  })
}

// 初始化模块信息
export function init(id) {
  if(id == undefined){
    id ='';
  }
  return request({
    url: applicationName + '/init?id='+id,
    method: 'get'
  })
}

// 模块的新增或者修改
export function save(data) {
  return request({
    url: applicationName + '/save',
    method: 'post',
    data: data
  })
}

//删除模块管理
export function del(id) {
  return request({
    url: applicationName + '/delete/' + id,
    method: 'delete'
  })
}

// 启禁用模块信息
export function able(id) {
  return request({
    url: applicationName + '/able/'+id,
    method: 'post'
  })
}

// 获取单个模块信息
export function getOne(id) {
  return request({
    url: applicationName + '/getOne/'+id,
    method: 'get'
  })
}


//模块列表
export function modulelist(query) {
  return request({
    url: applicationName + '/list',
    method: 'get',
    params: query
  })
}
