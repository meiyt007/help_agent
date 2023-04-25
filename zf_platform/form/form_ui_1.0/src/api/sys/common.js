import request from '@/utils/request'

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


// 获取系统配置树信息
export function queryConfigTree(parentOid) {
  if(parentOid == undefined){
    parentOid = '';
  }
  return request({
    url: '/settings/security/config/queryConfigTree?parentOid='+parentOid,
    method: 'get'
  })
}


// 获取组织机构树信息
export function queryOrganTree(districtOid) {
  if(districtOid == undefined){
    districtOid = '';
  }
  return request({
    url: '/platform/security/common/queryOrganTree?districtOid='+districtOid,
    method: 'get'
  })
}
//获取区划机构用户树
export function queryDistrictOrganUserTree(query) {
  if(!query){
    query={
      oid:'',
      identity:''
    }
  }
   return request({
    url: '/platform/security/common/queryDistrictOrganUserTree?oid='+query.oid+"&identity="+query.identity,
    method: 'get'
  })
}
