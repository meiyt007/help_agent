import request from '@/utils/request'

// 查询登录信息列表
export function page(query) {
  return request({
    url: '/platform/security/syslogin/page',
    method: 'get',
    params: query
  })
}

// 初始化登录信息
export function init(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: '/platform/security/syslogin/init/'+oid,
    method: 'get'
  })
}

// 登录的新增或者修改
export function updatePassword(data) {
  return request({
    url: '/platform/security/syslogin/updatePassword',
    method: 'post',
    data: data
  })
}

// 解锁登录信息
export function handleUnLock(id) {
  return request({
    url: '/platform/security/syslogin/delock/'+id,
    method: 'post'
  })
}

// 获取单个登录信息
export function getOne(oid) {
  return request({
    url: '/platform/security/syslogin/getOneVo/'+oid,
    method: 'get'
  })
}

// 启禁用登录信息
export function able(oid) {
  return request({
    url: '/platform/security/syslogin/able/'+oid,
    method: 'post'
  })
}
