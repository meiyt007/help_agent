import request from '@/utils/request'
import { getToken } from '@/utils/auth'

// 查询岗位信息列表
export function page(query) {
  return request({
    url: '/platform/security/post/page',
    method: 'get',
    params: query
  })
}

// 初始化岗位信息
export function init(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: '/platform/security/post/init/'+oid,
    method: 'get'
  })
}

// 岗位的新增或者修改
export function save(data) {
  return request({
    url: '/platform/security/post/save',
    method: 'post',
    data: data
  })
}

//删除岗位管理
export function del(oid) {
  return request({
    url: '/platform/security/post/delete/' + oid,
    method: 'delete'
  })
}

// 启禁用岗位信息
export function able(oid) {
  return request({
    url: '/platform/security/post/able/'+oid,
    method: 'post'
  })
}

// 获取单个岗位信息
export function getOne(oid) {
  return request({
    url: '/platform/security/post/getOne/'+oid,
    method: 'get'
  })
}

// 导出岗位信息
export function listExp(query) {
  const  baseURL = process.env.VUE_APP_BASE_API;
  window.location.href = baseURL + '/platform/security/post/listExp?name='+encodeURI(query.name) + '&districtOid='+query.districtOid +
    + '&organOid='+query.organOid + '&pageNum='+query.pageNum + '&pageSize='+query.pageSize + '&access_token=' +  getToken();
}

//根据组织机构oid和区划oid查询岗位列表
export function list(query) {
  return request({
    url: '/platform/security/post/list',
    method: 'get',
    params: query
  })
}

//根据岗位oid查询用户列表
export function userListPage(query) {
  return request({
    url: '/platform/security/sysuser/queryUserPostPage',
    method: 'get',
    params: query
  })
}
