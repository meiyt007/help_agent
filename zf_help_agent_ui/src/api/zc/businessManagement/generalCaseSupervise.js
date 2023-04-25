import request from '@/utils/request';

// 分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseSupervise/queryPageList',
    method: 'get',
    params: query
  })
}

// 获取详情
export function getOneByCaseOid(caseOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/doneBusiness/getOneByCaseOid?caseOid=' + caseOid,
    method: 'post',
  })
}

// 获取区划下级
export function querySysDistrictListByParentOid(oid) {
  if(oid!= undefined){
    oid=oid.replace('DISTRICT-','');
  }
  return request({
    url: '/platform/security/district/querySysDistrictListByParentOid?parentOid='+oid,
    method: 'get'
  })
}

// 获取机构用户信息
export function getOrganUserList() {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH +'/caseSupervise/getOrganUserList',
    method: 'get'
  })
}

// 获取区划机构用户信息
export function queryUserSimpleTree(organOid) {
  if(organOid == undefined){
    organOid = '';
  }
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH +'/caseSupervise/queryUserSimpleTree?districtOid='+organOid,
    method: 'post',
  })
}

