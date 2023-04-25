import request from '@/utils/request';

//查询列表
export function sxServicePage (query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/sxServicePage',
    method: 'post',
    params: query
  })
}

//获取事项详情
export function getSxServiceOne (id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/viewSxService?serviceOid=' + id,
    method: 'get'
  })
}
