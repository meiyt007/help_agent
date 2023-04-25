/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-24 15:54:22
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-07 14:35:39
 * @FilePath: \zf_web_ui\src\api\modules\userCenter.js
 * @Description: 用户中心
 */
import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_CASE_API;

//修改用户信息
export function updateInfo(data) {
  return request({
    url: baseUrl + "/ha/user/updateInfo",
    method: "post",
    params: data,
  });
}

//修改密码
export function updatePass(data) {
  return request({
    url: baseUrl + "/ha/user/updatePass",
    method: "post",
    params: data,
  });
}
