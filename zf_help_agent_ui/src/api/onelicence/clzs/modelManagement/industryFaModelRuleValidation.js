import request from '@/utils/request';

//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/sxServicePage',
    method: 'post',
    params: query
  })
}
//获取事项详情
export function getSxServiceOne(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/viewSxService?serviceOid='+id,
    method: 'get'
  })
}


//查询一级材料目录列表
export function pageList() {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCatalog/getMaterialCatalogList',
    method: 'post'
  })
}

//初始化事项验证规则数据
export function initFaModelRule(query) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryFaModelRuleValidation/initFaModelRule',
    method: 'post',
    params: query
  })
}

export function initAll() {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryFaModelRuleValidation/initAll',
    method: 'post'
  })
}


export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryFaModelRuleValidation/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//初始化一件事验证规则数据
export function initIndustryFaModelRule(query) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryFaModelRuleValidation/initIndustryFaModelRule',
    method: 'post',
    params: query
  })
}

