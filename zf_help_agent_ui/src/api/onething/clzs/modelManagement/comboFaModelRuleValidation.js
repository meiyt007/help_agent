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
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFaModelRuleValidation/initFaModelRule',
    method: 'post',
    params: query
  })
}

export function initAll() {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFaModelRuleValidation/initAll',
    method: 'post'
  })
}


export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFaModelRuleValidation/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//初始化一件事验证规则数据
export function initComboFaModelRule(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFaModelRuleValidation/initComboFaModelRule',
    method: 'post',
    params: query
  })
}



//根据一件事目录oid查询验证规则
export function getComboFaModelRuleValidationList(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFaModelRuleValidation/getComboFaModelRuleValidationList',
    method: 'post',
    params: query
  })
}


//获取一件事目录树
export function queryMaterialElementTreeSelect(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFaModelRuleValidation/queryMaterialElementTreeSelect',
    method: 'post',
    params: query
  })
}

export function queryMaterialAndCataAndElementTree(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFaModelRuleValidation/queryMaterialAndCataAndElementTree',
    method: 'post',
    params: query
  })
}





//查询电子证照规则列表
export function queryFaModelRuleValidationList(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFaModelRuleValidation/queryComboFaModelRuleValidationListByRule',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: query
  })
}

//更新保存电子证照规则
export function saveOrUpdateFaModelRuleValidation(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFaModelRuleValidation/saveOrUpdateFaModelRuleValidationList',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
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
export function updateComboFaModelRuleValidation(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFaModelRuleValidation/updateComboFaModelRuleValidation',
    method: 'post',
    params: query
  })
}




