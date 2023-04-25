import request from '@/utils/request'


//
export function materialClassifyPrePrial(caseOid,attaOid,classifierId) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryClassify/materialClassifyPrePrial',
    params: {
      caseOid: caseOid,
      attaOid:attaOid,
      classifierId:classifierId,
    },
    method: 'get'
  })
}



//初始化分类数据
export function initAutoClassifierCaseFileRecUploadList(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH +'/industryClassify/initAutoClassifierCaseFileRecUploadList?caseOid='+caseOid,
    method: 'get'
  })
}


//删除材料分类信息
export function deleteClassifiler(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryClassify/deleteClassifiler',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data: data
  })
}

//删除并更新材料分类表
export function updateClassifiler(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryClassify/updateClassifiler',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data: data
  })
}

/**
 * 获取材料电子化列表
 * @param {string} caseOid 办件oid eq: 9ef8754efede4a51924c711cd4678c2a
 */
 export function getIndustryCaseMaterialList(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryClassify/getIndustryCaseMaterialList',
    method: 'get',
    params: data
  })
}
/**
 * 保存材料附件一业一证
 * 
 */
 export function saveIndustryCaseMaterialAttaList(params) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCase/materialAtta/saveIndustryCaseMaterialAttaList',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data: params
  })
}
/**
 * 更新材料列表（电子化页面）
 * 
 */
 export function updateIndustryCaseMaterialList(params) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCase/caseMaterial/updateIndustryCaseMaterialList',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data: params
  })
}
/**
 * 获取办件材料列表和审核结果
 * 
 */
 export function getIndustryCaseMaterialListAndAuditResult(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryPreTrial/getIndustryCaseMaterialListAndAuditResult',
    method: 'get',
    params: data
  })
}
/**
 * 查看页面进行确认功能
 * 
 */
 export function confirmIndustryCaseMaterial(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryPreTrial/confirmIndustryCaseMaterial',
    method: 'get',
    params: data
  })
}
/**
 * 查询办件材料详细审核结果
 * @param {string} caseOid 办件oid
 * @param {string} caseMaterialOid 办件材料oid
 * @param {string} materialOid 材料oid
 * @param {string} serviceOid 事项oid
 */
 export function industryViewDetailResult(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryPreTrial/viewDetailResult',
    method: 'get',
    params: data
  })
}
/**
 * 获取办件材料列表
 * @param {string} caseOid 办件oid eq: 9ef8754efede4a51924c711cd4678c2a
 */
 export function getAllIndustryCaseMaterialList(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryClassify/getAllIndustryCaseMaterialList',
    method: 'get',
    params: data
  })
}
/** 获取分类材料参数 */
export function industrygetClassifilerMateial(params) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryClassify/getClassifilerMateial',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data: params
  })
}
//查询已出库信息列表
export function industrybatchOutList(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryMaterialOut/batchOutList?caseOid=' + caseOid,
    method: 'post',
  })
}

