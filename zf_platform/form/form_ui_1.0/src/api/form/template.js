import request from '@/utils/request'

var applicationName = process.env.VUE_APP_FORM_API_ROUTE_PATH + '/manager/security/template';

// 查询对象信息列表
export function page(query) {
  return request({
    url: applicationName + '/page',
    method: 'get',
    params: query
  })
}

// 查询对象信息列表(设计表单)
export function selectPage(query) {
  return request({
    url: applicationName + '/selectPage',
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
export function save(data) {
  return request({
    url: applicationName + '/saveFormTemplate',
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

// 启禁用对象信息
export function able(id) {
  return request({
    url: applicationName + '/able/'+id,
    method: 'post'
  })
}
//发布
export function setFormTemplateByDesignOid(designOid) {
  return request({
    url: applicationName + '/setFormTemplateByDesignOid/'+designOid,
    method: 'post'
  })
}

// 获取单个对象信息
export function getOne(id) {
  return request({
    url: applicationName + '/getOne/'+id,
    method: 'get'
  })
}
// 获取单个对象信息
export function getFormTemplateByOid(oid) {
  return request({
    url: applicationName + '/getFormTemplateByTemplateOid/'+oid,
    method: 'get'
  })
}

//对象列表
export function querylist(query) {
  return request({
    url: applicationName + '/queryFormTemplateList',
    method: 'get',
    params: query
  })
}

//模板图片上传
export function uploadBase64Images(data) {
  return request({
    url: process.env.VUE_APP_FORM_API_ROUTE_PATH + "/manager/uploadBase64Images",
    method: "post",
    data: data
  });
}
