import request from '@/utils/request';

//查询材料目录列表
export function page(query) {
  return request({
      url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/serverType/list',
    method: 'get',
    params: query
  })
}

//编辑、编辑下级，靠id区分
export function editServerType(id) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/serverType/init',
    method: 'post',
    params: {
      parentTypeOid: id
    },
  })
}

//查询服务分类详细信息
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/serverType/view',
    params: {
      oid: id
    },
    method: 'post',
  })
}

//批量删除
export function batchDelete(oids) {
  return request({
    url:  process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/serverType/batchDelete?oids='+oids,
    method: 'post'
  })
}

//上移
export function  upMove(oid){
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/serverType/up',
    params: {
      oid: oid
    },
    method: 'post',
  })
}

//下移
export function  downMove(oid){
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/serverType/down',
    params: {
      oid: oid
    },
    method: 'post',
  })
}

//新增或修改数据
export function save(data) {
  return request({
    url:process.env.VUE_APP_MIDDLE_ROUTE_PATH+'/middleManager/middle/serverType/save',
    method: 'post',
    data: data
  })
}

//删除子级
export function del(oid) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH +'/middleManager/middle/serverType/delete',
    method: 'post',
    params: {
      oid: oid
    }
  })
}
