/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-20 17:37:38
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-07 14:35:23
 * @FilePath: \zf_web_ui\src\api\modules\evaluate.js
 * @Description: 评价
 */
import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_CASE_API;

//获取评价项
export function evalGetItem() {
  return request({
    url: baseUrl + "/ha/eval/getItem",
    method: "get",
  });
}

//评价
export function saveEvalResult(data) {
  return request({
    url: baseUrl + "/ha/eval/saveEvalResult",
    method: "post",
    data: data,
  });
}

//获取工作人员的评价分数
export function getWorkUserEvalScore() {
  return request({
    url: baseUrl + "/ha/eval/getWorkUserEvalScore",
    method: "get",
  });
}

//获取评价项
export function scanCertQrCode(data) {
  return request({
    url: baseUrl + "/ha/outer/scanCertQrCode",
    method: "post",
    data:data
  });
}
