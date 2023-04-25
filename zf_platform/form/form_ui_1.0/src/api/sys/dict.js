import request from '@/utils/request'
import { getToken } from '@/utils/auth'

// 查询数字字典信息列表
export function page(query) {
  return request({
    url: '/settings/security/dict/page',
    method: 'get',
    params: query
  })
}

// 获取系统配置树信息
export function queryDictTree(parentOid) {
  if(parentOid == undefined){
    parentOid = '';
  }
  return request({
    url: '/settings/security/dict/queryDictTree?parentOid='+parentOid,
    method: 'get'
  })
}

// 根据父类编码获取数字字典列表
export function getDictList(parentCode) {
  if(parentCode == undefined){
    parentCode =null;
  }
  return request({
    url: '/settings/security/dict/querySysDictListByParentCode?parentCode='+parentCode,
    method: 'get'
  })
}

// 初始化数字字典信息
export function init(id,parentOid) {
  if(id == undefined){
    id ='';
  }
  if(parentOid == undefined){
    parentOid ='';
  }
  return request({
    url: '/settings/security/dict/init?id='+id+'&parentOid='+parentOid,
    method: 'get'
  })
}

// 数字字典的新增或者修改
export function save(data) {
  return request({
    url: '/settings/security/dict/save',
    method: 'post',
    data: data
  })
}

//删除数字字典管理
export function del(id,dictOid) {
  return request({
    url: '/settings/security/dict/delete?id='+id+'&dictOid='+dictOid,
    method: 'delete'
  })
}

// 启禁用数字字典信息
export function able(oid) {
  return request({
    url: '/settings/security/dict/able/'+oid,
    method: 'post'
  })
}

// 获取单个数字字典信息
export function getOne(oid) {
  return request({
    url: '/settings/security/dict/getOne/'+oid,
    method: 'get'
  })
}

// 导出数字字典信息
export function listExp(query) {
  const baseURL = process.env.VUE_APP_BASE_API;
  window.location.href = baseURL + '/settings/security/dict/listExp?name='+encodeURI(query.name) +
    '&code='+query.code +'&parentOid='+query.parentOid+'&pageNum='+query.pageNum + '&pageSize='+query.pageSize + '&access_token=' +  getToken();}
