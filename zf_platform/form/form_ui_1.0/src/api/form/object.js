import request from '@/utils/request'

var applicationName = process.env.VUE_APP_FORM_API_ROUTE_PATH + '/manager/security/object';

// 查询存储对象信息列表
export function page(query) {
  return request({
    url: applicationName + '/page',
    method: 'get',
    params: query
  })
}

// 初始化存储对象信息
export function init(id) {
  if(id == undefined){
    id ='';
  }
  return request({
    url: applicationName + '/init?id='+id,
    method: 'get'
  })
}

// 存储对象的新增或者修改
export function save(data) {
  return request({
    url: applicationName + '/save',
    method: 'post',
    data: data
  })
}

//删除存储对象管理
export function del(id) {
  return request({
    url: applicationName + '/delete/' + id,
    method: 'delete'
  })
}

// 启禁用存储对象信息
export function able(id) {
  return request({
    url: applicationName + '/able/'+id,
    method: 'post'
  })
}

// 获取单个存储对象信息
export function getOne(id) {
  return request({
    url: applicationName + '/getOne/'+id,
    method: 'get'
  })
}

// 获取单个存储对象信息
export function getFormObjectByObjectOid(objectOid) {
  return request({
    url: applicationName + '/getFormObjectByObjectOid/'+objectOid,
    method: 'get'
  })
}

//存储对象列表
export function objectlist(query) {
  return request({
    url: applicationName + '/queryFormObjectList',
    method: 'get',
    params: query
  })
}
//数据源存储对象
export function dataSourceObjectlist(query) {
  return request({
    url: applicationName + '/queryDataSourceFormObjectList',
    method: 'get',
    params: query
  })
}
//根据objectOid查询是否存在填报的数据
export function checkReportDataExist(objectOid) {
  return request({
    url: applicationName + '/checkReportDataExist/'+objectOid,
    method: 'get'
  })
}
