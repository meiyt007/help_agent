import request from '@/utils/request'

// 查询开发商信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/developUser/list',
    method: 'get',
    params: query
  })
}

// 初始化开发商信息
export function init(oid) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/developUser/init?oid='+oid,
    method: 'get'
  })
}

// 启禁用信息
export function able(oid) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH +'/middleManager/middle/user/able?oid='+oid,
    method: 'post'
  })
}

// 开发商新增或者修改
export function save(data) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH +'/middleManager/middle/developUser/save',
    method: 'post',
    data: data
  })
}

// 获取单个信息
export function getOne(oid) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/user/view?oid='+oid,
    method: 'get'
  })
}

//删除管理
export function del(oid) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH +'/middleManager/middle/user/delete?oid='+oid,
    method: 'get',
  })
}

// 审核
export function checkView(oid) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/user/checkView?oid='+oid,
    method: 'get'
  })
}

// 审核保存
export function saveCheck(data) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/user/check',
    method: 'post',
    data: data
  })
}

// 查询服务商信息列表
export function pageUser(query) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/user/list',
    method: 'get',
    params: query
  })
}

// 初始化服务商信息
export function initUser(oid) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/user/init?oid='+oid,
    method: 'get'
  })
}

// 开发商新增或者修改
export function saveUser(data) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH +'/middleManager/middle/user/save',
    method: 'post',
    data: data
  })
}

