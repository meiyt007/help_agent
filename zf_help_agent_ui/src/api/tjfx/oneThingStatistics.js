import request from '@/utils/request'

// 获取一件事零办件统计
export function getOnethingZeroCaseStatistics (query) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + '/statisticalAnalysis/getOnethingZeroCaseStatistics',
    method: 'get',
    params: query
  })
}

//一件事办事人统计
export function getOnethingApplyUserStatistics (query) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + '/statisticalAnalysis/getOnethingApplyUserStatistics',
    method: 'get',
    params: query
  })
}

//一件事目录统计
export function getOnethingComboDirectoryStatistics (query) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + '/statisticalAnalysis/getOnethingComboDirectoryStatistics',
    method: 'get',
    params: query
  })
}

//零办件目录
export function queryZeroCaseComboDirectoryWithPage (query) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + '/comboDirectory/queryOnethingZeroCaseComboDirectoryWithPage',
    method: 'get',
    params: query
  })
}

//根据办件类型查询办件列表
export function selectComboCaseListByTypePage (query) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + '/comboCaseService/selectComboCaseListByTypePage',
    method: 'get',
    params: query
  })
}


//导出一件事零办件统计
export function listExpOnethingZeroCaseStatistics (query) {
  const baseURL = process.env.VUE_APP_STATISTIC_API;
  window.location.href = baseURL + '/statisticalAnalysis/exportOnethingZeroCaseStatistics?districtOid=' + query.districtOid +
    '&organOid=' + query.organOid + '&startDate=' + query.startDate + '&endDate=' + query.endDate;
}

//导出一件事办事人统计
export function listExpOnethingApplyUserStatistics (query) {
  const baseURL = process.env.VUE_APP_STATISTIC_API;
  window.location.href = baseURL + '/statisticalAnalysis/exportOnethingApplyUserStatistics?districtOid=' + query.districtOid +
    '&organOid=' + query.organOid + '&startDate=' + query.startDate + '&endDate=' + query.endDate;
}

//导出一件事目录办件统计
export function listExpOnethingComboDirectoryStatistics (query) {
  const baseURL = process.env.VUE_APP_STATISTIC_API;
  window.location.href = baseURL + '/statisticalAnalysis/exportOnethingComboDirectoryStatistics?startDate=' + query.startDate +
    '&endDate=' + query.endDate + '&themeOid=' + query.themeOid;
}

//常办清单列表
export function getComboServiceCountByUserOid (userOid) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + `/comboCaseService/getComboServiceCountByUserOid?userOid=${userOid}`,
    method: 'get',
  })
}