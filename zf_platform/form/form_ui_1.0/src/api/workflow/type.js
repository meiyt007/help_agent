import request from '@/utils/request'

// 查询流程类型信息列表
export function page(query) {
  return request({
    url: '/workflow/security/workflow/type/page',
    method: 'get',
    params: query
  })
}

// 初始化流程类型信息
export function init(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: '/workflow/security/workflow/type/init/'+oid,
    method: 'get'
  })
}

// 流程类型的新增或者修改
export function save(data) {
  return request({
    url: '/workflow/security/workflow/type/save',
    method: 'post',
    data: data
  })
}

//删除流程类型管理
export function del(oid) {
  return request({
    url: '/workflow/security/workflow/type/delete/' + oid,
    method: 'delete'
  })
}

// 启禁用流程类型信息
export function able(oid) {
  return request({
    url: '/workflow/security/workflow/type/able/'+oid,
    method: 'post'
  })
}

// 获取单个流程类型信息
export function getOne(oid) {
  return request({
    url: '/workflow/security/workflow/type/getOne/'+oid,
    method: 'get'
  })
}

//根据应用oid查询流程类型列表
export function typeList(query) {
  return request({
    url: '/workflow/security/workflow/type/list',
    method: 'get',
    params: query
  })
}

//根据procdef Id查询列表信息
export function queryWorkflowBussFlowStepByProcessDefId(processDefId) {
  return request({
    url: '/workflow/security/workflow/step/queryWorkflowBussFlowStepByProcessDefId?processDefId='+processDefId,
    method: 'get'
  })
}
