import request from '@/utils/request';

// 用户信息列表
// 分页查询列表操作
export function getUserInfoList(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/getComboCasePageByCredentialNumber',
    method: 'get',
    params: query
  })
}


