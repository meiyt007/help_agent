import request from '@/utils/request'

// 查询信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/log/list',
    method: 'get',
    params: query
  })
}

