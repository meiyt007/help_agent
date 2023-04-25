import request from '@/utils/request';

//查询服务信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/server/list',
    method: 'post',
    params: query
  })
}

//查询服务状态
export function getServerStatus() {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/server/getServerStatus',
    method: 'post'
  })
}

//查询服务分类
export function getServiceType() {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/serverType/queryServerTypeJson',
    method: 'get'
  })
}

//启禁用
export function enable(id) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/server/able',
    params: {
      oid: id
    },
    method: 'post'
  })
}

//查看服务详情
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/server/view',
    params: {
      oid: id
    },
    method: 'post'
  })
}

//查看接口信息
export function getInterface(oid){
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/serverInterface/view?oid='+oid,
    method: 'get'
  })
}

//审核弹框
export function examine(id) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/server/initAuth',
    params: {
      oid: id
    },
    method: 'post'
  })
}

//审核提交
export function save(data) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH +'/middleManager/middle/server/saveServerAuthRec',
    method: 'post',
    data: data
  })
}

//查询历史版本列表
export function pageHistory(query) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/server/historyServerList',
    method: 'post',
    params: query
  })
}

//历史版本中的查看
export function getOneHistory(id) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/server/viewFile',
    params: {
      oid: id
    },
    method: 'post'
  })
}

//历史版本中查看接口
export function getInterfaceH(oid){
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/serverInterface/viewFile?oid='+oid,
    method: 'get'
  })
}

// 删除
export function batchDelete(oids) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/server/batchDelete?oids='+oids,
    method: 'post'
  })
}

//升级审核弹框
export function examineUp(id) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/server/initAuthUp',
    params: {
      oid: id
    },
    method: 'post'
  })
}

//升级审核提交
export function saveUp(data) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH +'/middleManager/middle/server/saveServerAuthRecUp',
    method: 'post',
    data: data
  })
}

//检测接口
export function check(id) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/server/checkServer',
    params: {
      serverOid: id
    },
    method: 'post'
  })
}

//获取用户表
export function getMiddleUser(query){
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/server/listUsers',
    method: 'post',
    params: query
  })
}

//预警策略初始化
export function getStrategy(oid){
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/server/ruleServer',
    params: {
      serverOid: oid
    },
    method: 'post'
  })
}

//预警策略新增或修改
export function saveStrategy(data) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH +'/middleManager/middle/server/saveSelect',
    method: 'post',
    data: data
  })
}

//查看接口UP信息
export function handleInterfaceUP(oid){
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/serverInterface/viewUp?oid='+oid,
    method: 'get'
  })
}
