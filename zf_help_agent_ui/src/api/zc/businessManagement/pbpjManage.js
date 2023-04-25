/**
 * 平板评价接口api
 * @author: wangwg
 * @date: 2020-12-14
 */
import request from '@/utils/request';

// 检查平板评价用户启用信息
export function pushPbpjUser (userOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/pbpj/user/getPbpjUserByUserOid?userOid=' + userOid,
    method: 'get'
  })
}

// 推送平板评价检查是否登录
export function pushPbpjCheckLogin () {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/pbpjService/checkUserLogin',
    method: 'get'
  })
}

// 推送平板评价信息确认
export function pushPbpjConfirmInfo (data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/pbpjService/pushPbpjConfirmInfo',
    method: 'POST',
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
    data: data
  })
}

// 获取平板评价点击确认信息
export function getPbpjConfirmInfo (userOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/pbpjService/getCallBackInfo?userOid=' + userOid,
    method: 'get'
  })
}

// 获取平板评价推动评价信息
export function pushPbpjInfo (caseNumber) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/pbpjService/pushPbpjInfo?caseNumber=' + caseNumber,
    method: 'get'
  })
}

// 获取平板评价点击保存办件信息
export function pbpjSaveCaseInfo (caseOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/pbpjService/pbpjSaveQlCase?caseOid=' + caseOid,
    method: 'get'
  })
}
