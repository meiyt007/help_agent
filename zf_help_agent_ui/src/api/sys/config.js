import request from '@/utils/request'
import { getToken } from '@/utils/auth'

// 查询参数配置信息列表
export function page (query) {
  return request({
    url: '/settings/security/config/page',
    method: 'get',
    params: query
  })
}

// 获取系统配置树信息
export function queryConfigTree (parentOid) {
  if (parentOid == undefined) {
    parentOid = '';
  }
  return request({
    url: '/settings/security/config/queryConfigTree?parentOid=' + parentOid,
    method: 'get'
  })
}

// 初始化参数配置信息
export function init (oid, parentOid) {
  if (oid == undefined) {
    oid = '';
  }
  if (parentOid == undefined) {
    parentOid = '';
  }
  return request({
    url: '/settings/security/config/init?id=' + oid + '&parentOid=' + parentOid,
    method: 'get'
  })
}

// 参数配置的新增或者修改
export function save (data) {
  return request({
    url: '/settings/security/config/save',
    method: 'post',
    data: data
  })
}

//删除参数配置管理
export function del (oid) {
  return request({
    url: '/settings/security/config/delete/' + oid,
    method: 'post'
  })
}

// 启禁用参数配置信息
export function able (oid) {
  return request({
    url: '/settings/security/config/able/' + oid,
    method: 'post'
  })
}

// 获取单个参数配置信息
export function getOne (oid) {
  return request({
    url: '/settings/security/config/getOne/' + oid,
    method: 'get'
  })
}

//根据code获取配置信息
export function getConfigByCode (code) {
  return request({
    url: '/settings/security/config/getSysConfigByCode?code=' + code,
    method: 'get',
    isConfig: true,
  })
}

//根据ParentCode获取配置信息集合
export function getConfigListByParentCode (parentCode) {
  return request({
    url: '/settings/security/config/querySysConfigListByParentCode?parentCode=' + parentCode,
    method: 'get',
    isConfig: true,
  })
}

// 导出参数配置信息
export function listExp (query) {
  const baseURL = process.env.VUE_APP_BASE_API;
  window.location.href = baseURL + '/settings/security/config/listExp?name=' + encodeURI(query.name) +
    '&code=' + query.code + '&parentOid=' + query.parentOid + '&pageNum=' + query.pageNum + '&pageSize=' + query.pageSize + '&access_token=' + getToken();
}

