import request from '@/utils/request'

// 查询权限信息列表
export function page(query) {
  return request({
    url: '/platform/security/syspermission/page',
    method: 'get',
    params: query
  })
}

// 增加修改初始化数据
export function init(query) {
  return request({
    url: '/platform/security/syspermission/init',
    method: 'get',
    params: query
  })
}

// 权限信息的新增或者修改
export function save(data) {
  return request({
    url: '/platform/security/syspermission',
    method: 'post',
    data: data
  })
}

// 权限链接保存修改
export function saveLink(data) {
  return request({
    url: '/platform/security/syspermissionlink',
    method: 'post',
    data: data
  })
}

//删除权限
export function del(oid) {
  return request({
    url: '/platform/security/syspermission/' + oid,
    method: 'post'
  })
}

// 启禁用权限信息
export function able(oid) {
  return request({
    url: '/platform/security/syspermission/'+oid,
    method: 'patch'
  })
}

// 获取单个权限信息
export function get(oid) {
  return request({
    url: '/platform/security/syspermission/'+oid,
    method: 'get'
  })
}

// 获取应用权限树
export function appPermissionTree(name){
  return request({
    url: '/platform/security/syspermission/appPermissionTree?name=' + name,
    method: 'get'
  })
}

// 获取权限树
export function permissionTree(query){
  return request({
    url: '/platform/security/syspermission/permissionTree?appOid='+query.appOid+"&disable="+query.disable,
    method: 'get'
  })
}
