import request from '@/utils/request';

// 分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryMaterialOut/pageList',
    method: 'post',
    params: query
  })
}
//保存或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryMaterialOut/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}
export function getOneMaterialInfo(caseOid){
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseCorrection/queryMaterialOutByCaseOid?caseOid='+caseOid,
    method: 'post'
  })
}
// 获取详情
export function getOneByCaseNumber(caseNumber) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryMaterialOut/getOneByCaseNumber?caseNumber=' + caseNumber,
    method: 'post',
  })
}

export function batchOutMaterial(ids) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryMaterialOut/batchOutMaterial?ids='+ids,
    method: 'post',
  })
}

export function printOutMaterial(ids) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryMaterialOut/printOutMaterial?ids='+ids,
    method: 'post',
  })
}

export function getIndustryMaterialOut(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryMaterialOut/queryIndustryOutMaterialsByCaseOid?caseOid='+caseOid,
    method: 'post',
  })
}

export function getIndustryOneMaterialInfo(id) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryMaterialOut/getOneMaterialInfo?id=' + id,
    method: 'post',
  })
}

export function updateIndustryMaterialOutOfstock(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryMaterialOut/updateIndustryMaterialOutOfstock',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

