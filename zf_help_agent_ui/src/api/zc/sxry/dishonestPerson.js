/*
* @Author: liyanqing
* @Description: 失信人员
*/
import request from '@/utils/request';

//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/dishonestPerson/pageList',
    method: 'post',
    params: query
  })
}

// 新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/dishonestPerson/saveOrUpdate',
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
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/dishonestPerson/getOne/' + id,
    method: 'get',
  })
}

//删除操作
export function deletes(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/dishonestPerson/delete/' + id,
    method: 'get',
  })
}

//撤销操作
export function dishonestCancel(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/dishonestPerson/dishonestCancel',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 获取证件类型
export function getCertificateType(type) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/sysDictService/getSelectCertificateType?type='+type,
    method: 'get'
  })
}

// 获取单个数字字典信息
export function getOneDict(oid) {
  return request({
    url: '/settings/security/dict/getSysDictByDictOid/'+oid,
    method: 'get'
  })
}

