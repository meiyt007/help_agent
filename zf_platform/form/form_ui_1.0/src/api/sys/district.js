import request from '@/utils/request'
import { getToken } from '@/utils/auth'

// 查询区划信息列表
export function page(query) {
  return request({
    url: '/platform/security/district/page',
    method: 'get',
    params: query
  })
}

// 获取区划树信息
export function queryDistrictSimpleTree(districtOid) {
  if(districtOid == undefined){
    districtOid = '';
  }
  return request({
    url: '/platform/security/common/queryDistrictSimpleTree?districtOid='+districtOid,
    method: 'get'
  })
}

// 获取所有的区划树信息
export function queryAllDistrictSimpleTree() {
  return request({
    url: '/platform/security/common/queryAllDistrictSimpleTree',
    method: 'get'
  })
}

// 初始化区划信息
export function init(oid,parentOid) {
  if(oid == undefined){
    oid ='';
  }
  if(parentOid == undefined){
    parentOid ='';
  }
  return request({
    url: '/platform/security/district/init?oid='+oid+'&parentOid='+parentOid,
    method: 'get'
  })
}

// 区划的新增或者修改
export function save(data) {
  return request({
    url: '/platform/security/district/save',
    method: 'post',
    data: data
  })
}

//删除区划管理
export function del(oid) {
  return request({
    url: '/platform/security/district/delete/' + oid,
    method: 'delete'
  })
}

// 启禁用区划信息
export function able(oid) {
  return request({
    url: '/platform/security/district/able/'+oid,
    method: 'post'
  })
}


// 获取单个区划信息
export function getOne(oid) {
  return request({
    url: '/platform/security/district/getOne/'+oid,
    method: 'get'
  })
}

// 导出区划信息
export function listExp(query) {
  const  baseURL = process.env.VUE_APP_BASE_API;
  window.location.href = baseURL + '/platform/security/district/listExp?name='+encodeURI(query.name) +
    '&code='+query.code +'&parentOid='+query.parentOid+'&pageNum='+query.pageNum + '&pageSize='+query.pageSize + '&access_token=' +  getToken();
}
