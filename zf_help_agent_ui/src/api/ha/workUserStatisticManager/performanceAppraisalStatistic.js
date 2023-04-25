import request from '@/utils/request'
import { getToken } from '@/utils/auth'
const baseURL = process.env.VUE_APP_BJFW_ROUTE_PATH;
//查询列表
export function performanceAppraisalStatisticVoList(query) {
  return request({
    url: baseURL + '/performanceAppraisalStatistic/queryHaPerformanceAppraisalStatisticVoRecordPageResult',
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
//根据时间区间去查待审核状态下的组长的信息（包括组长姓名、组长手机号、所属分组、几条没审）
export function queryGroupLeaderPageListByAuditStatus(query) {
  return request({
    url : baseURL + '/performanceAppraisalStatistic/queryGroupLeaderPageListByAuditStatus',
    method: 'post',
    params: query
  })
}
//一键催审(多个帮办组长id)催审组长审核加分时长
export function batchAkeyReviewByGroupLeaderIds(idList){
  return request({
    url: baseURL +'/performanceAppraisalStatistic/batchAkeyReviewByGroupLeaderIds?ids='+idList,
    method: 'get',
  })
}

// 根据帮办组长id催审组长审核加分时长
export function akeyReviewByGroupLeaderId(id) {
  return request({
    url:  baseURL + '/performanceAppraisalStatistic/akeyReviewByGroupLeaderId?id='+id,
    method: 'get'
  })
}
