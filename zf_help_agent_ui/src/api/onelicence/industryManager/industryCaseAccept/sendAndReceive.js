import request from '@/utils/request';

// 一件事办理-办结业务，获取列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industrySendAndReceive/page',
    method: 'get',
    params: query
  })
}

// 一件事办理-办结业务-查看、签收 发证信息
export function get(id) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industrySendAndReceive/get/'+id,
    method: 'get'
  })
}
