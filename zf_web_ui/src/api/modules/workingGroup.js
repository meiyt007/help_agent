/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-08 14:11:36
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-07 17:15:22
 * @FilePath: \zf_web_ui\src\api\modules\workingGroup.js
 * @Description: 这是办事群体接口列表
 */
import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_CASE_API;

//获取办事群众的信息
export function getMassesInfo(data) {
  return request({
    url: baseUrl + "/ha/masses/getMassesInfo",
    method: "get",
    params: data,
  });
}

//获取企业信息
export function getCompanyInfo(data) {
  return request({
    url: baseUrl + "/ha/masses/getCompanyInfo",
    method: "get",
    params: data,
  });
}

//获取某个用户的队列服务记录
export function queryQueueHistoryList(data) {
  return request({
    url: baseUrl + "/ha/workUser/queryQueueHistoryList",
    method: "get",
    params: data,
  });
}

//获取某个用户的帮办服务记录
export function queryHelpServiceHistoryList(data) {
  return request({
    url: baseUrl + "/ha/workUser/queryHelpServiceHistoryList",
    method: "get",
    params: data,
  });
}

//获取某个用户的帮办服务记录
export function queryCompanyHistoryList(data) {
  return request({
    url: baseUrl + "/ha/workUser/queryCompanyHistoryList",
    method: "post",
    data: data,
  });
}
