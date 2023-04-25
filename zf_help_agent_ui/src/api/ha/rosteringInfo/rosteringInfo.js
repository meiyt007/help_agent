/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-09-23 16:55:51
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-23 17:13:16
 * @FilePath: \zf_help_agent_ui\src\api\ha\rosteringInfo\rosteringInfo.js
 * @Description: 排班接口
 */
import request from '@/utils/request';
const baseUrl = process.env.VUE_APP_BJFW_ROUTE_PATH

//获取排班列表
export function queryScheduleList(data) {
  return request({
    url:baseUrl + '/help/schedule/queryScheduleList',
    method: 'get',
    params: data
  })
}


//获取排班列表
export function initScheduleList(data) {
  return request({
    url:baseUrl + '/help/schedule/init',
    method: 'get',
    params: data
  })
}

//获取排班列表
export function delectScheduleList(data) {
  return request({
    url:baseUrl + '/help/schedule/delectYearMonth',
    method: 'get',
    params: data
  })
}

//修改帮代办人员的某一天的排班状态
export function scheduleUpdate(data) {
  return request({
    url:baseUrl + '/help/schedule/update',
    method: 'get',
    params: data
  })
}


//获取分组列表
export function getGroupListSub(data) {
  return request({
    url:baseUrl + '/ha/outer/getGroupListSub',
    method: 'get',
    params: data
  })
}
