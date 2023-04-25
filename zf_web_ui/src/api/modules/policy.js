// 政策库中 分享办事人接口，pc端
//分享给办事人
import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_CASE_API;

export function policyPush(data) {
    return request({
      url: baseUrl + "/ha/web/policyPush",
      method: "post",
      data,
    });
  }