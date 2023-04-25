import request from '@/utils/request'

// 查询信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/serverInterface/visitRecList',
    method: 'get',
    params: query
  })
}

// 获取单个信息
export function getOne(oid) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/serverInterface/visitRecView?oid='+oid,
    method: 'get'
  })
}

//删除管理
export function del(oid) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH +'/middleManager/middle/serverInterface/visitRecDelete?oid='+oid,
    method: 'post',
  })
}
