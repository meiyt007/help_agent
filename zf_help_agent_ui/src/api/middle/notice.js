import request from '@/utils/request'

// 查询信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/notice/list',
    method: 'get',
    params: query
  })
}

// 获取单个信息
export function getOne(oid) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/notice/view?oid='+oid,
    method: 'get'
  })
}

// 批量删除
export function batchDelete(oids) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/notice/batchDelete?oids='+oids,
    method: 'post'
  })
}

// 初始信息
export function init(oid) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/notice/init?oid='+oid,
    method: 'get'
  })
}

// 公告已读信息查看
export function noticeRec(query) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/notice/noticeRecList',
    method: 'get',
    params: query
  })
}

export function save(data) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH +'/middleManager/middle/notice/save',
    method: 'post',
    data: data
  })
}
