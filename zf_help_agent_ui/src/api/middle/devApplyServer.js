import request from '@/utils/request'

// 查询信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/devApplyServer/list',
    method: 'get',
    params: query
  })
}

// 初始化信息
export function init(oid) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/devApplyServer/initAuth?oid='+oid,
    method: 'get'
  })
}

// 的新增或者修改
export function save(data) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH +'/middleManager/middle/devApplyServer/saveDevApplyAuthRec',
    method: 'post',
    data: data
  })
}

// 获取单个信息
export function getOne(oid) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/devApplyServer/view?oid='+oid,
    method: 'get'
  })
}

