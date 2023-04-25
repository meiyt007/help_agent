/**
 * 好差评评价接口api
 * @author: wangwg
 * @date: 2021-07-15
 */
import request from '@/utils/request';

// 获取送好差评评价内容
export function contentList () {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/evaluateService/queryEvaluateContent',
    method: 'get'
  })
}

// 小屏推送好差评评价页面
export function pushEvaluatePage (caseNumber) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/evaluateService/pushEvaluatePage?caseNum=' + caseNumber,
    method: 'get'
  })
}

// 好差评保存办件信息
export function saveEvaluateCaseInfo (caseOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/evaluateService/evaluateaveQlCase?caseOid=' + caseOid,
    method: 'get'
  })
}

// 获取好差评办件评价信息
export function queryEvaluateInfo (caseNumber) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/evaluateService/getEvaluateInfo?caseNum=' + caseNumber,
    method: 'get'
  })
}

// 是否已评价
export function isEvaluate (caseNumber) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/evaluateService/isEvaluate?caseNum=' + caseNumber,
    method: 'get'
  })
}

//根据办件业务主键查询办件申请信息
export function queryQlCaseApplayByCaseOid (caseOid) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseApplayService/queryQlCaseApplayByCaseOid?caseOid=' + caseOid,
    method: 'get',
  })
}
