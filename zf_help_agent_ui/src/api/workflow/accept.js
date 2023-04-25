import request from '@/utils/request'

// 查询流程信息列表
export function page(query) {
  return request({
    url: '/workflow/security/accept/page',
    method: 'get',
    params: query
  })
}

// 初始化流程信息
export function init(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: '/workflow/security/workflow/model/getOne/'+oid,
    method: 'get'
  })
}
// 获取任务执行条件
export function getTaskCondition(taskId) {
  if(taskId == undefined){
    taskId ='';
  }
  return request({
    url: '/workflow/security/accept/getTaskCondition/'+taskId,
    method: 'get'
  })
}

// 新增或者修改model信息
export function save(data) {
  return request({
    url: '/workflow/security/accept/save',
    method: 'post',
    data: data
  })
}

// 下一步
export function next(data) {
  return request({
    url: '/workflow/security/accept/next',
    method: 'post',
    data: data
  })
}
// 查看流程图
export function flowView(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: '/workflow/security/workflow/model/flowView/'+oid,
    method: 'get'
  })
}

//流程列表
export function list(query) {
  return request({
    url: '/workflow/security/workflow/bussInfo/list/',
    method: 'get',
    params: query
  })
}

