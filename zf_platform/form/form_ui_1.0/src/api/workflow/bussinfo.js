import request from '@/utils/request'

var applicationName = '/workflow';

// 查询流程信息列表
export function page(data) {
  return request({
    url: applicationName + '/security/workflow/bussInfo/page',
    method: 'post',
    data: JSON.parse(JSON.stringify(data).replace(/DISTRICT-/g,"").replace(/ORGAN-/g,""))
  })
}

// 查询流程信息列表
export function pageByTypeOid(params) {
  return request({
    url: applicationName + '/security/workflow/bussInfo/queryWorkflowBussInfoByTypeOid',
    method: 'get',
    params: params
  })
}

// 查询流程信息列表
export function workflowCopy(params) {
  return request({
    url: applicationName + '/security/workflow/bussInfo/workflowCopy',
    method: 'get',
    params: params
  })
}

// 初始化流程信息
export function init(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: applicationName + '/security/workflow/model/getOne/'+oid,
    method: 'get'
  })
}
// 流程编制
export function edit(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: applicationName + '/security/workflow/model/'+oid+'/xml',
    method: 'get'
  })
}

// 新增或者修改流程信息
export function saveOrUpdate(data) {
  return request({
    url: applicationName + '/security/workflow/bussInfo/saveOrUpdate',
    method: 'post',
    data: JSON.parse(JSON.stringify(data).replace(/DISTRICT-/g,"").replace(/ORGAN-/g,""))
  })
}

// 保存流程的编辑
export function saveModelBpmn(data,infoOid) {
  return request({
    url: applicationName + '/security/workflow/model/'+infoOid+'/xml/save',
    method: 'post',
    data: data
  })
}
// 查看流程图
export function processView(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: '/workflow/security/workflow/model/flowViewByInfoOid/'+oid,
    method: 'get'
  })
}

// 查看流程图
export function processViewByModelId(modelId) {
  if(modelId == undefined){
    modelId ='';
  }
  return request({
    url: '/workflow/security/workflow/model/flowViewByModelId/'+modelId,
    method: 'get'
  })
}
// 查看流程图 历史记录
export function flowViewByProcessDefId(processDefId) {
  if(processDefId == undefined){
    processDefId ='';
  }
  return request({
    url: '/workflow/security/workflow/model/flowViewByProcessDefId/'+processDefId,
    method: 'get'
  })
}

// 查看办理的流程图
export function flowView(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: applicationName + '/security/workflow/model/flowView/'+oid,
    method: 'get'
  })
}

//删除管理
export function del(oids) {
  return request({
    url: applicationName + '/security/workflow/bussInfo/delete/' + oids,
    method: 'delete'
  })
}


// 获取单个流程信息
export function getOne(oid) {
  return request({
    url: '/workflow/security/workflow/model/getOne/'+oid,
    method: 'get'
  })
}

//部署流程
export function deploy(oid, modelId) {
  return request({
    url: applicationName + '/security/workflow/bussInfo/deploy',
    method: 'get',
    params: {oid, modelId}
  })
}


// 查询所有区划的组织机构、用户信息Tree
export function queryAllDistrictOrganUserTree() {
  return request({
    url: '/platform/security/common/queryAllDistrictOrganUserTree',
    method: 'get'
  })
}
//查询所有区划的组织机构、岗位信息Tree
export function queryAllDistrictOrganPostTree() {
  return request({
    url: '/platform/security/common/queryAllDistrictOrganPostTree',
    method: 'get'
  })
}

//根据流程实例ID获取流程图已办的节点
export function viewFlow(processInstanceId) {
  return request({
    url: applicationName + '/security/workflow/model/viewFlow/'+processInstanceId,
    method: 'get'
  })
}

//根据流程实例ID和任务环节id 获取环节的办理信息
export function getWorkflowTaskVoByInstanceIdAndActivityId(data) {
  return request({
    url: applicationName + '/security/workflow/model/getWorkflowTaskVoByInstanceIdAndActivityId',
    method: 'get',
    params: data
  })
}

//根据流程实例ID和任务环节id 获取环节step信息
export function getWorkflowStepByInstanceIdAndActivityId(data) {
  return request({
    url: applicationName + '/security/workflow/model/getWorkflowStepByInstanceIdAndActivityId',
    method: 'get',
    params: data
  })
}

//查询所有区划的组织机构、流程信息Tree
export function queryAllDistrictOrganBussInfoTree() {
  return request({
    url: applicationName + '/security/workflow/bussInfo/queryAllDistrictOrganBussInfoTree',
    method: 'get'
  })
}

//根据infoOid查询流程类型环节集合
export function queryWorkflowLinkListByInfoOid(infoOid) {
  return request({
    url: applicationName + '/security/workflow/bussInfo/queryWorkflowLinkListByInfoOid?infoOid='+infoOid,
    method: 'get'
  })
}


//根据流程实例ID获取内嵌流程图
export function getInnerViewFlowByProcessInstanceIdAndActivityId(data) {
  return request({
    url: applicationName + '/security/workflow/model/getInnerViewFlowByProcessInstanceIdAndActivityId',
    method: 'get',
    params: data
  })
}

//根据流程infoOID获取内嵌流程图
export function getWorkflowBussFlowStepByInfoIdAndActivityId(data) {
  return request({
    url: applicationName + '/security/workflow/model/getWorkflowBussFlowStepByInfoIdAndActivityId',
    method: 'get',
    params: data
  })
}

//根据流程部署defOID获取内嵌流程图
export function getWorkflowBussFlowStepByProcessDefIdAndActivityId(data) {
  return request({
    url: applicationName + '/security/workflow/model/getWorkflowBussFlowStepByProcessDefIdAndActivityId',
    method: 'get',
    params: data
  })
}


//根据用户oids获取用户名称集合
export function getUserNames(oids) {
  if(oids == undefined){
    oids ='';
  }
  return request({
      url: applicationName + '/security/workflow/bussInfo/getUserNames?oids='+oids,
    method: 'get'
  })
}
