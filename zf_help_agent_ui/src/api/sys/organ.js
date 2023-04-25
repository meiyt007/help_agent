import request from '@/utils/request'
import { getToken } from '@/utils/auth'

//单条屏蔽
export function shieldById(id,isshield){
  return request({
    url: '/platform/security/organ/shieldById?id='+id+'&isshield='+isshield,
    method: 'get',
  })
}
//批量屏蔽
export function batchShieldOrgan(idList,isshield){
  return request({
    url: '/platform/security/organ/shieldOrganListid?ids='+idList+'&isshield='+isshield,
    method: 'get',
  })
}
// 查询组织机构信息列表
export function querySysOrganShieldWithPage(query) {
  return request({
    url: '/platform/security/organ/querySysOrganShieldWithPage',
    method: 'get',
    params: query
  })
}

// 查询组织机构信息列表
export function page(query) {
  return request({
    url: '/platform/security/organ/page',
    method: 'get',
    params: query
  })
}

// 初始化组织机构信息
export function init(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: '/platform/security/organ/init?oid='+oid,
    method: 'get'
  })
}
// 组织机构的新增或者修改
export function save(data) {
  return request({
    url: '/platform/security/organ/save',
    method: 'post',
    data: data
  })
}

// 获取组织机构树信息
export function queryOrganTree(districtOid) {
  if(districtOid == undefined){
    districtOid = '';
  }else{
    districtOid=districtOid.replace('DISTRICT-','');
  }
  return request({
    url: '/platform/security/common/queryOrganTree?districtOid='+districtOid,
    method: 'get'
  })
}

//删除组织机构管理
export function del(oid) {
  return request({
    url: '/platform/security/organ/delete/' + oid,
    method: 'post'
  })
}

// 启禁用组织机构信息
export function able(oid) {
  return request({
    url: '/platform/security/organ/able/'+oid,
    method: 'post'
  })
}

// 获取单个组织机构信息
export function getOne(oid) {
  return request({
    url: '/platform/security/organ/getOne/'+oid,
    method: 'get'
  })
}

// 导出组织机构信息
export function listExp(query) {
  const  baseURL = process.env.VUE_APP_BASE_API;
  if(query.districtOid == undefined){
    query.districtOid = '';
  }
  window.location.href = baseURL + '/platform/security/organ/listExp?name='+encodeURI(query.name) +
    '&districtOid='+query.districtOid+'&parentOid='+query.parentOid+'&pageNum='+query.pageNum + '&pageSize='+query.pageSize + '&access_token=' +  getToken();
}

// 查询组织机构信息用户列表
export function userListPage(query) {
  return request({
    url: '/platform/security/organ/userListByOrgan',
    method: 'get',
    params: query
  })
}
