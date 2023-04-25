import request from '@/utils/request'
import { getToken } from '@/utils/auth'
const baseURL = process.env.VUE_APP_CASE_API;
//查询列表
export function performanceAppraisalStatisticVoList(query) {
  return request({
    url: baseURL + '/ha/performancePlusTimeRecord/queryHaPerformanceAppraisalStatisticVoRecordPageResult',
    method: 'post',
    params: query
  })
}
//查询列表
export function queryHaPerRegRecordPageResult(query) {
  return request({
    url: baseURL + '/ha/performancePlusTimeRecord/queryHaPerRegRecordPageResult',
    method: 'post',
    params: query
  })
}
//帮办人绩效统计导出
export function listExp (query) {
  const baseURL1 = process.env.VUE_APP_BUSINESS_CASE_API;
  var href = baseURL1 + '/performanceAppraisalStatistic/listExp?beginTime=' + encodeURI(query.beginTime) +
    '&endTime=' + query.endTime + '&pageNum=' + query.pageNum + '&pageSize=' + query.pageSize + '&access_token=' + getToken();
  window.location.href = href;
}

