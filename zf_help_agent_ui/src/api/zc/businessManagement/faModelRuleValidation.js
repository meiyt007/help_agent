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
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/faModelRuleValidation/initFaModelRule',
    method: 'post',
    params: query
  })
}

export function initAll() {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/faModelRuleValidation/initAll',
    method: 'post'
  })
}


export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/faModelRuleValidation/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}


//通过事项oid查询验证规则
export function initFaModelRuleValidation(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/faModelRuleValidation/initFaModelRuleValidation',
    method: 'post',
    params: query
  })
}

export function queryMaterialElementTreeSelect(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/faModelRuleValidation/queryMaterialElementTreeSelect',
    method: 'post',
    params: query
  })
}



export function queryFaModelRuleValidationList(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/faModelRuleValidation/queryFaModelRuleValidationListByRuleType',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: query
  })
}

export function saveOrUpdateFaModelRuleValidation(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/faModelRuleValidation/saveOrUpdateFaModelRuleValidation',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

export function queryMaterialAndCataAndElementTree(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/faModelRuleValidation/queryMaterialAndCataAndElementTree',
    method: 'post',
    params: query
  })
}


export function getBasicFieldListByType(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/basicFormField/getBasicFieldListByType',
    method: 'post',
    params: query
  })
}

export function queryElectronicLicenseTree(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/electronicLicense/queryElectronicLicenseTree',
    method: 'get',
    params: query
  })
}


//修改目录配置后修改对应的规则配置
export function updateFaModelRuleValidation(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/faModelRuleValidation/updateFaModelRuleValidation',
    method: 'post',
    params: query
  })
}


/*electronicLicense/queryElectronicLicenseTree
electronicLicense*/

