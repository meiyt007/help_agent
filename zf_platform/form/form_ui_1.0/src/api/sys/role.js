import request from '@/utils/request'

// 查询角色信息列表
export function page(query) {
  return request({
    url: '/platform/security/sysrole/page',
    method: 'get',
    params: query
  })
}

// 增加修改初始化数据
export function init(query) {
  return request({
    url: '/platform/security/sysrole/init',
    method: 'get',
    params: query
  })
}

// 角色信息的新增或者修改
export function save(data) {
  return request({
    url: '/platform/security/sysrole',
    method: 'post',
    data: data
  })
}

//删除角色
export function del(oid) {
  return request({
    url: '/platform/security/sysrole/' + oid,
    method: 'delete'
  })
}

// 启禁用角色信息
export function able(oid) {
  return request({
    url: '/platform/security/sysrole/'+oid,
    method: 'patch'
  })
}

// 获取单个角色信息
export function get(oid) {
  return request({
    url: '/platform/security/sysrole/'+oid,
    method: 'get'
  })
}
//获取应用角色树
export function roleTree(query) {
  return request({
    url: '/platform/security/sysrole/roleTree?appOid='+query.appOid+"&disable="+query.disable,
    method: 'get'
  })
}

//获取角色下的用户
export function queryRoleUser(oid) {
  return request({
    url: '/platform/security/sysrole/userTree/'+oid,
    method: 'get'
  })
}
// 保存授权用户
export function saveRoleUser(data) {
  return request({
    url: '/platform/security/sysrole/saveRoleUser',
    method: 'post',
    data: data
  })
}
