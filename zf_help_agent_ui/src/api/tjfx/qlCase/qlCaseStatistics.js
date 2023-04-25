import request from '@/utils/request'

// 零办件列表
export function qlCaseZeroCaseStatisticsList (query) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + '/qlCaseService/qlCaseZeroCaseStatisticsList',
    method: 'get',
    params: query
  })
}
//零办件统计导出
export function listExpZeroCase (query) {
  const baseURL = process.env.VUE_APP_STATISTIC_API;
  window.location.href = baseURL + '/qlCaseService/qlCaseZeroCaseStatisticsListExel?districtOid=' + query.districtOid +
    '&organOid=' + query.organOid + '&startDate=' + query.startDate + '&endDate=' + query.endDate + '&pageNum=' + query.pageNum
    + '&pageSize=' + query.pageSize;
}

//零办件事项列表
export function qlCaseSxListZeroList (query) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + '/qlCaseService/qlCaseSxListZeroList',
    method: 'get',
    params: query
  })
}

//事项办件列表
export function qlCaseServiceCaseStatisticsList (query) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + '/qlCaseService/qlCaseServiceCaseStatisticsList',
    method: 'get',
    params: query
  })
}
//事项办件-办件列表
export function qlCaseServiceCaseList (query) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + '/qlCaseService/qlCaseServiceCaseList',
    method: 'get',
    params: query
  })
}
//事项办件导出
export function listExpServiceCase (query) {
  const baseURL = process.env.VUE_APP_STATISTIC_API;
  window.location.href = baseURL + '/qlCaseService/qlCaseServiceCaseStatisticsListExel?serviceTypeOid=' + query.serviceTypeOid +
    '&startDate=' + query.startDate + '&endDate=' + query.endDate + '&pageNum=' + query.pageNum
    + '&pageSize=' + query.pageSize;
}
//办事人统计
export function qlCaseApplyUserStatisticsList (query) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + '/qlCaseService/qlCaseApplyUserStatisticsList',
    method: 'get',
    params: query
  })
}
//办事人导出
export function listExpApplyUser (query) {
  const baseURL = process.env.VUE_APP_STATISTIC_API;
  window.location.href = baseURL + '/qlCaseService/qlCaseApplyUserStatisticsListExel?districtOid=' + query.districtOid +
    '&organOid=' + query.organOid + '&startDate=' + query.startDate + '&endDate=' + query.endDate + '&pageNum=' + query.pageNum
    + '&pageSize=' + query.pageSize;
}

//即办率统计
export function qlCaseImmediateRateStatisticsList (query) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + '/qlCaseService/qlCaseImmediateRateStatisticsList',
    method: 'get',
    params: query
  })
}
//即办率办结列表
export function qlCaseImmediateRateCaseList (query) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + '/qlCaseService/qlCaseImmediateRateCaseList',
    method: 'get',
    params: query
  })
}
//即办件统计导出
export function listExpImmediateCase (query) {
  const baseURL = process.env.VUE_APP_STATISTIC_API;
  window.location.href = baseURL + '/qlCaseService/qlCaseImmediateRateStatisticsListExel?districtOid=' + query.districtOid +
    '&organOid=' + query.organOid + '&startDate=' + query.startDate + '&endDate=' + query.endDate + '&pageNum=' + query.pageNum
    + '&pageSize=' + query.pageSize;
}

//常办清单列表
export function getSxServiceCountByUserOid (userOid) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + `/qlCaseService/getSxServiceCountByUserOid?userOid=${userOid}`,
    method: 'get',
  })
}