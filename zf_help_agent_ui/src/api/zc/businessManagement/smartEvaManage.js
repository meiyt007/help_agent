/**
 * 智能评价接口api
 * @author: wangwg
 * @date: 2020-12-14
 */
import request from "@/utils/request";

// 获取评价设备启动类型
export function getPjDeviceType() {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/martService/getEvaluationType',
    method: 'get'
  })
}
export function grantWindowUser(userOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/martService/grantWindowUser?userOid='+userOid,
    method: 'get'
  })
}

// 推送办件事项基本信息
export function getServiceBaseInfo(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/martService/sendServiceBaseInfo',
    method: 'POST',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data:data
  })
}

// 推送办件确认信息
export function pushSmartEvaConfirm(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/martService/sendConfirmCaseInfo',
    method: 'POST',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data:data
  })
}
// 获取办件确认返回信息
export function getSmartPjConfirmFlag(userOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/martService/queryConfirmFlag?userOid='+userOid,
    method: 'get'
  })
}

// 获取办件确认返回信息
export function savePjMark(caseOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/martService/sendSmartPj?caseOid='+caseOid,
    method: 'get'
  })
}



