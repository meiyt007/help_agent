import request from '@/utils/request'

// 查询信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/applyHardware/list',
    method: 'get',
    params: query
  })
}

// 初始化信息
export function init(oid) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/applyHardware/initAuth?oid='+oid,
    method: 'get'
  })
}

// 初始化终端包上传
export function initTerminal(oid) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/applyHardware/initUpTerminal?oid='+oid,
    method: 'get'
  })
}

// 的新增或者修改
export function save(data) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH +'/middleManager/middle/applyHardware/saveHardwareAuthRec',
    method: 'post',
    data: data
  })
}

// 获取单个信息
export function getOne(oid) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/applyHardware/view?oid='+oid,
    method: 'get'
  })
}

// 获取单个信息
export function batchDelete(oids) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/applyHardware/batchDelete?oids='+oids,
    method: 'post'
  })
}

export function saveTerminal(data) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH +'/middleManager/middle/applyHardware/saveUpTerminal',
    method: 'post',
    data: data
  })
}

