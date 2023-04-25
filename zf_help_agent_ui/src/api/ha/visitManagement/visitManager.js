import request from '@/utils/request';
// 分页查询列表操作
export function pageVisit(query) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/help/haVisit/queryHaVisitByNameAndVisitType',
    method: 'get',
    params: query
  })
}

export function pageQueueVisit(serviceWorkUserId) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/help/haVisit/getVisitMassageByWorkQueueId?workQueueId='+serviceWorkUserId,
    method: 'get'
  })
}
