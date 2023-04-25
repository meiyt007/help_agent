import request from '@/utils/request'
// 查询字段关联信息列表
export function page (query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFieldRelConfig/queryPageList',
    method: 'get',
    params: query
  })
}

export function queryBaseFieldList () {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/basicFormField/queryBaseFormFieldList?fieldType=2',
    method: 'get',
  })
}

// 材料类型新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFieldRelConfig/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

export function del (id) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFieldRelConfig/delRelConfig?id='+id,
    method: 'get',
  })
}

export function init (id) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFieldRelConfig/getOneRelConfig?id='+id,
    method: 'get',
  })
}

export function queryElecAndElementTree (serviceOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFieldRelConfig/queryElecAndElementTree?serviceOid='+serviceOid,
    method: 'get',
  })
}

export function queryMaterialAndCataAndElementTree(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFaModelRuleValidation/queryMaterialAndCataAndElementTree',
    method: 'post',
    params: query
  })
}

export function queryCatalogAndCataLogElementTree (serviceOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFieldRelConfig/queryCatalogAndCataLogElementTree?serviceOid='+serviceOid,
    method: 'get',
  })
}

//判断被填充的字段是否已添加
export function checkHasRepeat(serviceOid, fillType, fillFieldOid, oid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFieldRelConfig/checkHasRepeat?serviceOid=' +serviceOid+ '&fillType='+fillType+ '&fillFieldOid='+fillFieldOid+ '&oid='+oid,
    method: 'get'
  })
}

export function queryUseComboFillFieldListByThingOid (serviceOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillField/queryUseComboFillFieldListByThingOid?comboDirectoryOid='+serviceOid,
    method: 'get',
  })
}

// 基础表单和电子表单赋值
export function getAllBasicFieldByOid (caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFieldRelConfig/getAllBasicFieldByOid',
    params: {
      caseOid: caseOid,
    },
    method: 'get'
  })
}

// 基础表单和电子表单字段对应关系
export function getBasicAndFormFieldRel (serviceOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFieldRelConfig/getBasicAndFormFieldRel',
    params: {
      serviceOid: serviceOid,
    },
    method: 'get'
  })
}


