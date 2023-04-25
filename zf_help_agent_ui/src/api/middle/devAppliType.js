import request from '@/utils/request'

// 查询信息列表
export function page(query) {
  return request({
    url:process.env.VUE_APP_MIDDLE_ROUTE_PATH+'/middleManager/middle/devAppliType/list',
    method: 'get',
    params: query
  })
}

// 初始化信息 编辑 编辑子项初始化页面时
export function init(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url:process.env.VUE_APP_MIDDLE_ROUTE_PATH+'/middleManager/middle/devAppliType/init?oid='+oid,
    method: 'get'
  })
}

// 的新增或者修改
export function save(data) {
  return request({
    url:process.env.VUE_APP_MIDDLE_ROUTE_PATH+'/middleManager/middle/devAppliType/save',
    method: 'post',
    data: data
  })
}

// 获取单个信息
export function getOne(oid) {
  return request({
    url:process.env.VUE_APP_MIDDLE_ROUTE_PATH+'/middleManager/middle/devAppliType/view?oid='+oid,
    method: 'get'
  })
}

// 上移
export function up(oid) {
  return request({
    url:process.env.VUE_APP_MIDDLE_ROUTE_PATH+'/middleManager/middle/devAppliType/up?oid='+oid,
    method: 'get'
  })
}

// 下移
export function down(oid) {
  return request({
    url:process.env.VUE_APP_MIDDLE_ROUTE_PATH+'/middleManager/middle/devAppliType/down?oid='+oid,
    method: 'get'
  })
}

// 批量删除
export function batchDelete(oids) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/devAppliType/batchDelete?oids='+oids,
    method: 'post'
  })
}

// 单个删除
export function del(oid) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/devAppliType/delete?oid='+oid,
    method: 'post'
  })
}
