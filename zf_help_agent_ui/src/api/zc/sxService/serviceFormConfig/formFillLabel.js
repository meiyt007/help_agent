import request from '@/utils/request'
import http from '@/utils/http'
// 查询目录清单信息列表
export function page (query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillLabel/queryFillLabelPage',
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
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillLabel/getFillLabel',
    method: 'get',
    params: { labelOid: oid }
  })
}

// 目录清单的新增或者修改
export function save (data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillLabel/saveFillLabel',
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
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillLabel/deleteFillLabel',
    method: 'post',
    data: { labelOid: oid }
  })
}

// 获取字段分类列表
export function getFieldTypeList () {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/dataChange/fieldType/querySxFieldTypeList',
    method: 'get',
    params: { isAble: 1 }
  })
}

// 根据事项主键和表单分类主键查询标签 serviceOid ,typeOid  
export function querySxFillLabelList (params) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillLabel/querySxFillLabelList',
    method: 'get',
    params
  })
}

// 根据事项主键查询类型
export function querySxFieldTypeListByServiceOid (params) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/dataChange/fieldType/querySxFieldTypeListByServiceOid',
    method: 'get',
    params
  })
}
