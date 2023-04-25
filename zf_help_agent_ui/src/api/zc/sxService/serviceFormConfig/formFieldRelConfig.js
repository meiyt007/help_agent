import request from '@/utils/request'
// 查询字段关联信息列表
export function page (query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formFieldRelConfig/queryPageList',
    method: 'get',
    params: query
  })
}

export function queryBaseFieldList () {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/basicFormField/queryBaseFormFieldList?fieldType=1',
    method: 'get',
  })
}

// 材料类型新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formFieldRelConfig/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

export function del (id) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formFieldRelConfig/delRelConfig?id='+id,
    method: 'get',
  })
}

export function init (id) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formFieldRelConfig/getOneRelConfig?id='+id,
    method: 'get',
  })
}

export function queryElecAndElementTree (serviceOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formFieldRelConfig/queryElecAndElementTree?serviceOid='+serviceOid,
    method: 'get',
  })
}

export function queryMaterialAndCataAndElementTree(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/faModelRuleValidation/queryMaterialAndCataAndElementTree',
    method: 'post',
    params: query
  })
}

export function queryCatalogAndCataLogElementTree (serviceOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formFieldRelConfig/queryCatalogAndCataLogElementTree?serviceOid='+serviceOid,
    method: 'get',
  })
}

//判断被填充的字段是否已添加
export function checkHasRepeat(serviceOid, fillType, fillFieldOid, oid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formFieldRelConfig/checkHasRepeat?serviceOid=' +serviceOid+ '&fillType='+fillType+ '&fillFieldOid='+fillFieldOid+ '&oid='+oid,
    method: 'get'
  })
}


