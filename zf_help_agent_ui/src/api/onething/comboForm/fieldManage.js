import request from '@/utils/request'
import http from '@/utils/http'
// 查询目录清单信息列表
export function page (query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillField/queryFillFieldPage',
    method: 'get',
    params: query
  })
}

// 初始化目录清单信息
export function get (oid) {
  if (oid === undefined) {
    oid = '';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillField/getFillField',
    method: 'get',
    params: { fieldOid: oid }
  })
}

// 目录清单的新增或者修改
export function save (data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillField/saveFillField',
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
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillField/deleteFillField',
    method: 'post',
    data: { fieldOid: oid }
  })
}

// 获取字段标签列表
export function getLabelList (thingOid, fieldTypeOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/label/queryComboFillLabelList',
    method: 'get',
    params: { thingOid: thingOid, fieldTypeOid: fieldTypeOid }
  })
}

// 获取字段标签列表
export function connectLabelAndField (labelOid, fieldOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillField/connectLabelAndField',
    method: 'post',
    params: { labelOid: labelOid, fieldOid: fieldOid }
  })
}

// 更新事项字段数据集合
export function batchUpdateComboFillField (data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillField/batchUpdateFillField',
    method: 'post',
    data: data
  })
}

// 查询分类
export function queryComboFieldTypeList (thingOid, formOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/label/queryComboFieldTypeList',
    method: 'get',
    params: { thingOid: thingOid, formOid: formOid }
  })
}

// 查询分类/标签下字段列表
export function queryFieldListInfo (thingOid, fieldTypeOid, labelOid, formOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillField/queryFieldListInfo',
    method: 'get',
    params: { thingOid, fieldTypeOid, labelOid, formOid }
  })
}


//查询分类标签字段树
export function querySxFieldTypeAndLabelAndFieldTree (thingOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/label/querySxFieldTypeAndLabelAndFieldTree',
    method: 'get',
    params: { thingOid: thingOid }
  })
}

