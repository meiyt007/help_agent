/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-08 13:49:53
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-07 14:35:35
 * @FilePath: \zf_web_ui\src\api\modules\message.js
 * @Description: 消息接口列表
 */
import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_CASE_API;

//获取未读消息的数量
export function getNoReadNum() {
  return request({
    url: baseUrl + "/ha/message/getNoReadNum",
    method: "get",
  });
}

//获取消息列表
export function queryMessageList(data) {
  return request({
    url: baseUrl + "/ha/message/queryMessageList",
    method: "post",
    params: data,
  });
}

//获取消息详细信息
export function getMessageInfo(data) {
  return request({
    url: baseUrl + "/ha/message/getMessageInfo",
    method: "get",
    params: data,
  });
}

//读取消息
export function readMessage(data) {
  return request({
    url: baseUrl + "/ha/message/readMessage",
    method: "get",
    params: data,
  });
}

//删除消息
export function deleteMessage(data) {
  return request({
    url: baseUrl + "/ha/message/deleteMessage",
    method: "post",
    params: data,
  });
}
