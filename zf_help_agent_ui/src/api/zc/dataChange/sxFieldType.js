import request from '@/utils/request'

// 查询信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH +'/dataChange/fieldType/page',
    method: 'get',
    params: query
  })
}

// 初始化信息
export function init(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH +'/dataChange/fieldType/init/'+oid,
    method: 'get'
  })
}

// 的新增或者修改
export function save(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH +'/dataChange/fieldType/save',
    method: 'post',
    data: data
  })
}

//删除管理
export function del(oid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH +'/dataChange/fieldType/delete/' + oid,
    method: 'post',
  })
}

// 启禁用信息
export function able(oid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH +'/dataChange/fieldType/able/'+oid,
    method: 'post'
  })
}

// 获取单个信息
export function getOne(oid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH +'/dataChange/fieldType/getOne/'+oid,
    method: 'get'
  })
}

