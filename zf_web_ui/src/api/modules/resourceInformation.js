/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-08 14:29:09
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-18 15:43:07
 * @FilePath: \zf_web_ui\src\api\modules\resourceInformation.js
 * @Description: 这是资源接口
 */
import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_CASE_API;

//资源列表
export function listResource(data) {
  return request({
    url: baseUrl + "/ha/resource/listResource",
    method: "get",
    params: data,
  });
}

//获取当天的办事队列
export function queryWorkQueueList(data) {
  return request({
    url: baseUrl + "/ha/workUser/queryWorkQueueList",
    method: "get",
    params: data,
  });
}

//获取资源信息
export function getResourceInfo(data) {
  return request({
    url: baseUrl + "/ha/resource/getResourceInfo",
    method: "get",
    params: data,
  });
}


//附件推送
export function attaPush(data) {
  return request({
    url: baseUrl + "/ha/web/attaPush",
    method: "post",
    data: data,
  });
}


//资源上传
export function uploadFile(data) {
  return request({
    url: baseUrl + "/ha/resource/uploadFile",
    method: "post",
    headers: {
      "Content-Type": "multipart/form-data",
    },
    data: data,
  });
}

//保存资源信息
export function saveResourceInfo(data) {
  return request({
    url: baseUrl + "/ha/resource/saveResourceInfo",
    method: "post",
    params: data,
  });
}

//删除资源
export function deleteResource(data) {
  return request({
    url: baseUrl + "/ha/resource/deleteResource",
    method: "get",
    params: data,
  });
}

//资源分享帮办人
export function shareResource(data) {
  return request({
    url: baseUrl + "/ha/resource/shareResource",
    method: "get",
    params: data,
  });
}
