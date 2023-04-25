import request from '@/utils/request'
import http from '@/utils/http'
// 查询目录清单信息列表
export function page (query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillField/queryFillFieldPage',
    method: 'get',
    params: query
  })
}

// 初始化目录清单信息
export function init (oid) {
  if (oid == undefined) {
    oid = '';
  }
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillField/getFillField',
    method: 'get',
    params: { fieldOid: oid }
  })
}

// 目录清单的新增或者修改
export function save (data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillField/saveFillField',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=UTF-8",
    },
    data: data
  })
}

//删除目录清单
export function del (oid) {
  return http({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillField/deleteFillField',
    method: 'post',
    data: { fieldOid: oid }
  })
}

// 获取字段分类列表
export function getSxFieldTypeAndLabelTree (oid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillLabel/querySxFieldTypeAndLabelTree',
    method: 'get',
    params: { serviceOid: oid }
  })
}




// 获取字段列表
export function getFieldList (serviceOid, fieldTypeOid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillField/queryFieldList',
    method: 'get',
    params: { serviceOid: serviceOid, fieldTypeOid: fieldTypeOid }
  })
}

// 参数serviceOId,fieldTypeOid,labelOid(为空时是主查询)
export function queryFieldListInfo (serviceOid, fieldTypeOid, labelOid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillField/queryFieldListInfo',
    method: 'get',
    params: { serviceOid, fieldTypeOid, labelOid }
  })
}

//更新事项字段数据集合
export function batchUpdateSxFillField (data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillField/batchUpdateFillField',
    method: 'post',
    data: data
  })
}


//获取分类标签字段树
export function querySxFieldTypeAndLabelAndSxFillFieldTree (oid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillLabel/querySxFieldTypeAndLabelAndSxFillFieldTree',
    method: 'get',
    params: { serviceOid: oid }
  })
}
