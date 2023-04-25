import request from '@/utils/request'

// 查询信息列表
export function page (query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/service/repeatField/page',
    method: 'get',
    params: query
  })
}

// 初始化信息
export function init (oid) {
  if (oid === undefined) {
    oid = '';
  }
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/service/repeatField/getRepeatFieldConfig/' + oid,
    method: 'get'
  })
}

// 的新增或者修改
export function save (data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/service/repeatField/save',
    method: 'post',
    data: data
  })
}

//删除管理
export function del (oid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/service/repeatField/delete/' + oid,
    method: 'post',
  })
}

// 启禁用信息
export function able (oid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/service/repeatField/able/' + oid,
    method: 'post'
  })
}
