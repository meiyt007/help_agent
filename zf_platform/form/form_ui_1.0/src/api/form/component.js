import request from '@/utils/request'

var applicationName = process.env.VUE_APP_FORM_API_ROUTE_PATH + '/manager/security/component';

// 查询组件信息列表
export function page(query) {
  return request({
    url: applicationName + '/page',
    method: 'get',
    params: query
  })
}

// 查询组件信息列表-新
export function pageDict(query) {
  return request({
    url: applicationName + '/pageDict',
    method: 'get',
    params: query
  })
}

// 初始化组件信息
export function init(id) {
  if(id == undefined){
    id ='';
  }p
  return request({
    url: applicationName + '/init?id='+id,
    method: 'get'
  })
}

// 组件的新增或者修改
export function saveCheckData(data) {
  return request({
    url: applicationName + '/saveCheckData',
    method: 'post',
    data: data
  })
}

//删除组件管理
export function del(id) {
  return request({
    url: applicationName + '/delete/' + id,
    method: 'delete'
  })
}

// 获取单个组件信息
export function getOne(id) {
  return request({
    url: applicationName + '/getOne/'+id,
    method: 'get'
  })
}


//组件列表
export function componentlist(query) {
  return request({
    url: applicationName + '/queryFormComponentList',
    method: 'get',
    params: query
  })
}

// 根据编码获取数字字典列表
export function getSysDictByCode(code) {
  if(code == undefined){
    code = '';
  }
  return request({
    url: '/settings/security/dict/getSysDictByCode?code='+code,
    method: 'get'
  })
}


//组件列表(设计器中授权的组件)
export function desginComponentlist(query) {
  return request({
    url: applicationName + '/queryDesginFormComponentList',
    method: 'get',
    params: query
  })
}
