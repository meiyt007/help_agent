import request from '@/utils/request'

// 查询信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/interfaceCheckLog/checkLogList',
    method: 'get',
    params: query
  })
}

// 获取单个信息
export function getOne(oid) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/interfaceCheckLog/checkLogView?oid='+oid,
    method: 'get'
  })
}

