import request from '@/utils/request';

//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/sxServicePage',
    method: 'post',
    params: query
  })
}
// 获取详情
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/sxServiceRegistrar/getOne?id=' + id,
    method: 'get',
  })
}

//回填人员树
export function getSysUserRegistrarList(id) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/sxServiceRegistrar/getSysUserRegistrarList?id=' + id,
    method: 'get',
  })
}

//获取事项详情
export function getSxServiceByOid(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/getSxServiceByOid/'+id,
    method: 'get'
  })
}

//获取事项详情
export function getSxServiceOne(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/viewSxService?serviceOid='+id,
    method: 'get'
  })
}

// 保存或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/sxServiceRegistrar/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//查询人员列表
export function pageList(query) {
  return request({
    url: '/platform/security/sysuser/queryWithPageBySysUser',
    method: 'get',
    params: query
  })
}

// 查询当前人员下所有授权的事项
export function getOneByUserOid(id) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/sxServiceRegistrar/getOneByUserOid?id=' + id,
    method: 'post',
  })
}
//区划机构事项树
export function queryServiceTree() {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/sxServiceRegistrar/queryServiceTree',
    method: 'post',
  })
}

// 人员授权保存或者修改
export function saveOrUpdatePersonReg(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/sxServiceRegistrar/saveOrUpdatePersonReg',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//取消授权
export function del(oid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH +'/sxServiceRegistrar/deleteServiceReg?serviceOid=' + oid,
    method: 'delete'
  })
}

//人员取消授权
export function delUserService(oid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH +'/sxServiceRegistrar/deleteServiceUserAuth?userOid=' + oid,
    method: 'delete'
  })
}

//查询所有授权serviceOIds当前人
export function sxServiceOidsListByUserOid() {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH +'/sxServiceRegistrar/sxServiceOidsListByUserOid',
    method: 'get'
  })
}

