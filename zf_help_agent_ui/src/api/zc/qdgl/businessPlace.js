import request from '@/utils/request';

//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceLocation/getLocationList',
    method: 'post',
    params: query
  })
}

//获取事项详情
export function getSxServiceExtendByServiceOid(serviceOid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceExtend/getSxServiceExtendBySericeOid/'+serviceOid,
    method: 'get'
  })
}

//查询列表
export function save(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceLocation/saveOrUpdateSxServiceLocation',
    method: 'post',
    params: data
  })
}

//查询详细
export function detail(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceLocation/detail?id='+id,
    method: 'get',
    params: id
  })
}

//删除
export function del(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceLocation/delete?id='+id,
    method: 'get',
    params: id
  })
}



