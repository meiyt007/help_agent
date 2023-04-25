/*
* @Author: liyanqing
* @Description: 征询管理
*/
import request from '@/utils/request';

//我的查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/consultManage/pageList',
    method: 'post',
    params: query
  })
}
//查询被征询列表
export function paged(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/consultManage/pageListed',
    method: 'post',
    params: query
  })
}

// 新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/consultManage/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 获取一条详情
export function get(id) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/consultManage/getOne/' + id,
    method: 'get',
  })
}

//删除操作
export function deletes(id) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/consultManage/delete/' + id,
    method: 'post',
  })
}

//撤销操作
export function reply(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/consultManage/reply',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 一件事办理,获取列表
export function casePage(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboCaseDone/page',
    method: 'get',
    params: query
  })
}

// 获取单个一件事选项信息
export function getDirectory(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directory/getOne/'+oid,
    method: 'get'
  })
}

// 一件事办件参与部门
export function getOrganList(os) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/common/getOrganByOids?organOids='+os,
    method: 'get'
  })
}
