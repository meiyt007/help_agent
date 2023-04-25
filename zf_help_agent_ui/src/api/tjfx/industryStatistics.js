import request from '@/utils/request'

// 获取一业一证零办件统计
export function getIndustryZeroCaseStatistics(query) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + '/statisticalAnalysis/getIndustryZeroCaseStatistics',
    method: 'get',
    params: query
  })
}

//一业一证办事人统计
export function getIndustryApplyUserStatistics(query) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + '/statisticalAnalysis/getIndustryApplyUserStatistics',
    method: 'get',
    params: query
  })
}

//一业一证目录统计
export function getIndustryComboDirectoryStatistics(query) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + '/statisticalAnalysis/getIndustryComboDirectoryStatistics',
    method: 'get',
    params: query
  })
}

//零办件目录
export function queryIndustryZeroCaseComboDirectoryWithPage(query) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + '/comboDirectory/queryIndustryZeroCaseComboDirectoryWithPage',
    method: 'get',
    params: query
  })
}
//根据办件类型查询办件列表
export function selectIndustryCaseListByTypePage(query) {
  return request({
    url: process.env.VUE_APP_STATISTICS_ROUTE_PATH + '/industryCaseService/selectIndustryCaseListByTypePage',
    method: 'get',
    params: query
  })
}



//导出一业一证事零办件统计
export function listExpIndustryZeroCaseStatistics(query) {
  const  baseURL = process.env.VUE_APP_STATISTIC_API;
  window.location.href = baseURL + '/statisticalAnalysis/exportIndustryZeroCaseStatistics?districtOid='+query.districtOid+
    '&organOid='+query.organOid+'&startDate='+query.startDate+'&endDate='+query.endDate;
}

//导出一业一证办事人统计
export function listExpIndustryApplyUserStatistics(query) {
  const  baseURL = process.env.VUE_APP_STATISTIC_API;
  window.location.href = baseURL + '/statisticalAnalysis/exportIndustryApplyUserStatistics?districtOid='+query.districtOid+
    '&organOid='+query.organOid+'&startDate='+query.startDate+'&endDate='+query.endDate;
}

//导出一业一证事目录办件统计
export function listExpIndustryComboDirectoryStatistics(query) {
  const  baseURL = process.env.VUE_APP_STATISTIC_API;
  window.location.href = baseURL + '/statisticalAnalysis/exportIndustryComboDirectoryStatistics?startDate='+query.startDate+
    '&endDate='+query.endDate+'&themeOid='+query.themeOid;
}
