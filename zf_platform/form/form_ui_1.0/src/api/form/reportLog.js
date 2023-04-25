import request from '@/utils/request'

var applicationName = process.env.VUE_APP_FORM_API_ROUTE_PATH + '/manager/security/reportLog';

// 查询对象信息列表
export function page(query) {
  return request({
    url: applicationName + '/page',
    method: 'get',
    params: query
  })
}

// 获取日志信息
export function getFormConfigAndData(id) {
  return request({
    url: applicationName + '/getFormConfigAndData/'+id,
    method: 'get'
  })
}
