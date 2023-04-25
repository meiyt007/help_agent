import request from '@/utils/request'

// 查询流程环节类型信息列表
export function page(query) {
  return request({
    url: '/workflow/security/workflow/link/page',
    method: 'get',
    params: query
  })
}

// 初始化流程环节类型信息
export function init(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: '/workflow/security/workflow/link/init/'+oid,
    method: 'get'
  })
}

// 流程环节类型的新增或者修改
export function save(data) {
  return request({
    url: '/workflow/security/workflow/link/save',
    method: 'post',
    data: data
  })
}

//删除流程环节类型管理
export function del(oid) {
  return request({
    url: '/workflow/security/workflow/link/delete/' + oid,
    method: 'delete'
  })
}

// 启禁用流程环节类型信息
export function able(oid) {
  return request({
    url: '/workflow/security/workflow/link/able/'+oid,
    method: 'post'
  })
}

// 获取单个流程环节类型信息
export function getOne(oid) {
  return request({
    url: '/workflow/security/workflow/link/getOne/'+oid,
    method: 'get'
  })
}

//根据应用oid查询流程环节类型列表
export function linkList(query) {
  return request({
    url: '/workflow/security/workflow/link/list',
    method: 'get',
    params: query
  })
}

