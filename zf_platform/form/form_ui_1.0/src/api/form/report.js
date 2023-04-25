import request from '@/utils/request'

var applicationName = process.env.VUE_APP_FORM_API_ROUTE_PATH + '/manager/security/report';

// 查询对象信息列表
export function page(query) {
  return request({
    url: applicationName + '/page',
    method: 'get',
    params: query
  })
}

// 初始化对象信息
export function init(id) {
  if(id == undefined){
    id ='';
  }
  return request({
    url: applicationName + '/init?id='+id,
    method: 'get'
  })
}

// 对象的新增或者修改
export function saveReport(data) {
  return request({
    url: applicationName + '/save',
    method: 'post',
    data: data
  })
}

//删除对象管理
export function del(id) {
  return request({
    url: applicationName + '/delete/' + id,
    method: 'delete'
  })
}


// 获取单个对象信息
export function getOne(id) {
  return request({
    url: applicationName + '/getOne/'+id,
    method: 'get'
  })
}


//对象列表
export function querylist(query) {
  return request({
    url: applicationName + '/queryFormReportList',
    method: 'get',
    params: query
  })
}
