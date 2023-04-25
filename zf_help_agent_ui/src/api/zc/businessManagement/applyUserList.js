import request from '@/utils/request';

// 用户信息列表
// 分页查询列表操作
export function getUserInfoList(query) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseService/queryQlCaseByCredentialNumber',
    method: 'get',
    params: query
  })
}


