import request from '@/utils/request'

var applicationName = process.env.VUE_APP_FORM_API_ROUTE_PATH + '/manager/security/column';

// 初始化表结构信息
export function init(id) {
  if(id == undefined){
    id ='';
  }
  return request({
    url: applicationName + '/init?id='+id,
    method: 'get'
  })
}

// 表结构的新增或者修改
export function save(data) {
  return request({
    url: applicationName + '/save',
    method: 'post',
    data: data
  })
}

// 获取单个表结构信息
export function getOne(id) {
  return request({
    url: applicationName + '/getOne/'+id,
    method: 'get'
  })
}

// 从数据库获取单个表结构信息
export function datasourceColumn(query) {
  return request({
    url: applicationName + '/getDatasourceColumn',
    method: 'get',
    params: query
  })
}

//表结构列表
export function queryFormColumnListByObjectOid(query) {
  return request({
    url: applicationName + '/queryFormColumnListByObjectOid',
    method: 'get',
    params: query
  })
}

//获取 存储对象 字段的 数据存储类型 码表
export function getObjectFieldSaveTypeList() {
  return request({
    url: applicationName + '/getObjectFieldSaveTypeList',
    method: 'get'
  })
}
