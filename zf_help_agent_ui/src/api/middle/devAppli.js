import request from '@/utils/request'

// 查询信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/devAppli/list',
    method: 'get',
    params: query
  })
}

// 获取分类数据
export function queryDevAppliTypeJson(){
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/devAppli/queryDevAppliTypeJson',
    method: 'get'
  })
}



//删除管理
export function batchDelete(oids) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/devAppli/batchDelete?oids='+oids,
    method: 'post'
  })
}

// 启禁用信息
export function able(oid) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH +'/middleManager/middle/devAppli/able?oid='+oid,
    method: 'post'
  })
}

// 获取单个信息
export function getOne(oid) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/devAppli/view?oid='+oid,
    method: 'get'
  })
}

