import request from '@/utils/request'

// 查询信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/message/list',
    method: 'get',
    params: query
  })
}

// 获取单个信息
export function getOne(oid) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/message/view?oid='+oid,
    method: 'get'
  })
}

// 批量删除
export function batchDelete(oids) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/message/batchDelete?oids='+oids,
    method: 'post'
  })
}
