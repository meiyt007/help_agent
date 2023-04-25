/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-09-07 15:28:42
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-08 09:30:27
 * @FilePath: \zf_web_ui\src\api\modules\commonProblem.js
 * @Description: 常见问题的接口
 */
import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_CASE_API;

//常见问题列表
export function listQuestion(data) {
  return request({
    url: baseUrl + "/ha/question/listQuestion",
    method: "get",
    params: data,
  });
}
//获取常见问题信息
export function getQuestionInfo(data) {
  return request({
    url: baseUrl + "/ha/question/getQuestionInfo",
    method: "get",
    params: data,
  });
}

//保存常见问题信息
export function saveQuestionInfo(data) {
  return request({
    url: baseUrl + "/ha/question/saveQuestionInfo",
    method: "post",
    params: data,
  });
}

//删除常见问题
export function deleteQuestion(data) {
  return request({
    url: baseUrl + "/ha/question/deleteQuestion",
    method: "post",
    params: data,
  });
}
