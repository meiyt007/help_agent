import request from '@/utils/request'

// 查询一件事主题信息列表
export function page (query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/page',
    method: 'get',
    params: query
  })
}

export function pagemulu (query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/pagemulu',
    method: 'get',
    params: query
  })
}

// 获取一件事主题树信息
export function queryComboThemeSimpleTree (themeOid) {
  if (themeOid == undefined) {
    themeOid = '';
  }
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/common/queryComboThemeSimpleTree?themeOid=' + themeOid,
    method: 'get'
  })
}

// 查询一件事服务对象
export function getComboServiceObject () {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/getComboServiceObject',
    method: 'get'
  })
}

// 初始化一件事主题信息
export function init (oid) {
  if (oid == undefined) {
    oid = '';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/init?oid=' + oid,
    method: 'get'
  })
}

// 一件事目录的新增或者修改
export function save (data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/save',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=UTF-8",
    },
    data: data
  })
}

//删除一件事目录管理
export function del (oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/delete/' + oid,
    method: 'delete'
  })
}

// 获取单个一件事目录信息
export function getOne (oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/getOne/' + oid,
    method: 'get'
  })
}
//发布目录
export function publishDire (oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/publishDire/' + oid,
    method: 'get'
  })
}
//取消发布目录
export function publishCancel (oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/publishCancel/' + oid,
    method: 'get'
  })
}

//获取上级区划主键
export function getParentOidByDistrictOid (districtOid, chooseType) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/common/getParentOidByDistrictOid?districtOid=' + districtOid + "&chooseType=" + chooseType,
    method: 'get'
  })



}
//区划机构事项树
export function queryServiceTree () {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/queryServiceTree',
    method: 'post',
  })
}

//区划机构树
export function queryDistrictAndOrganTree (districtOid, type) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/queryDistrictAndOrganTree?districtOid=' + districtOid + "&type=" + type,
    method: 'post',
  })
}
//查询所有已经发布的事项
export function queryAllDirectoryPublish () {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/queryAllDirectoryPublish',
    method: 'get',
  })
}

//查询所有已经发布的事项
export function queryAllDirectoryPublishByIndustryType (industryType) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/queryAllDirectoryPublishByIndustryType?industryType=' + industryType,
    method: 'get',
  })
}

export function queryComboDirectoryPrecheckList (params) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directoryPrecheck/queryComboDirectoryPrecheckList',
    method: 'post',
    data: params
  })
}

export function saveOrUpdateList (params) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directoryPrecheck/saveOrUpdateList',
    method: 'post',
    data: params
  })
}