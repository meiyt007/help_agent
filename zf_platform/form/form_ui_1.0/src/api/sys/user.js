import request from '@/utils/request'

// 查询用户信息列表
export function page(query) {
  return request({
    //url: '/platform/security/sysuser/page',
    url: '/platform/security/sysuser/queryWithPage',
    method: 'get',
    params: query
  })
}

// 增加修改初始化数据
export function init(id) {
  return request({
    url: '/platform/security/sysuser/init/'+id,
    method: 'get'
  })
}

// 用户信息的新增或者修改
export function save(data) {
  return request({
    url: '/platform/security/sysuser',
    method: 'post',
    data: data
  })
}

// 用户信息的新增或者修改(加密方式)
export function saveEncrypt(data) {
  return request({
    url: '/platform/security/sysuser/saveEncrypt',
    method: 'post',
    params: data
  })
}

//删除用户
export function del(oid) {
  return request({
    url: '/platform/security/sysuser/' + oid,
    method: 'delete'
  })
}

// 启禁用用户信息
export function able(id) {
  return request({
    url: '/platform/security/sysuser/'+id,
    method: 'patch'
  })
}

// 获取单个用户信息
export function get(oid) {
  return request({
    url: '/platform/security/sysuser/'+oid,
    method: 'get'
  })
}

// 获取用户信息角色
export function getUserRole(userOid) {
  return request({
    url: '/platform/security/sysuser/role/'+userOid,
    method: 'get'
  })
}

// 用户授权保存
export function saveUserRole(data) {
  return request({
    url: '/platform/security/sysuser/saveUserRole',
    method: 'post',
    data: data
  })
}

// 用户头像更改
export function uploadAvatar(oid) {
  return request({
    url: '/platform/security/sysuser/avatar?oid='+oid,
    method: 'patch'
  })
}

//根据机构获取用户树
export function queryUserTreeByOrganOid(organOid) {
  return request({
    url: '/platform/security/sysuser/userTree?disable=false&organOid='+organOid,
    method: 'get'
  })
}

// 用户皮肤更改
export function updateUserSkinClassname(userOid,skinClassname) {
  return request({
    url: '/platform/security/sysuser/updateUserSkinClassname?userOid='+userOid+'&skinClassname='+skinClassname,
    method: 'post'
  })
}

// 根据组织机构oid获取用户
export function getSysUserListByOrganOid(organOid) {
  return request({
    url: '/platform/security/sysuser/getSysUserListByOrganOid/'+organOid,
    method: 'get'
  })
}
