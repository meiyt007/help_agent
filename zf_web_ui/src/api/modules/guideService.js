/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-08 13:25:21
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-07 14:35:25
 * @FilePath: \zf_web_ui\src\api\modules\guideService.js
 * @Description: 导服人员接口
 */
import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_CASE_API;

//是否有新的办事人员扫码
export function isHaveNewMesses() {
  return request({
    url: baseUrl + "/ha/df/isHaveNewMesses",
    method: "get",
  });
}

//查询办事队列
export function queryWorkQueueList(data) {
  return request({
    url: baseUrl + "/ha/df/queryWorkQueueList",
    method: "post",
    params: data,
  });
}

//统计队列数据
export function getQueueNum(data) {
  return request({
    url: baseUrl + "/ha/df/getQueueNum",
    method: "get",
  });
}

//获取帮代办人员列表
export function getWorkUserList(data) {
  return request({
    url: baseUrl + "/ha/df/getWorkUserList",
    method: "post",
    params: data,
  });
}

//分配帮代办人员
export function distributeWorkUser(data) {
  return request({
    url: baseUrl + "/ha/df/distributeWorkUser",
    method: "post",
    params: data,
  });
}

//窗口叫号
export function windowCall(data) {
  return request({
    url: baseUrl + "/ha/df/windowCall",
    method: "post",
    params: data,
  });
}
