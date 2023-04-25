import request from '@/utils/request';

//查询当前登录者所授权的一件事数据列表
export function pageIndustryOfUser(query) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/accept/page',
    method: 'get',
    params: query
  })
}


//查询信息
export function downGzcnsWord (obj) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/printNoticeInfo/printGzWord',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
    data: obj
  })
}
