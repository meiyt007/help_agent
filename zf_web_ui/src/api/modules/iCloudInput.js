// 云客服录入

import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_CASE_API;

//获取云客服绩效详情列表
export function queryPerPhoneWithPage(params) {
    return request({
      url: baseUrl + "/ha/perPhone/queryPerPhoneWithPage",
      method: "post",
      params,
    });
  }
//云客服文件导入
export function ImportPhoneExcel(data) {
    return request({
      url: baseUrl + "/ha/performancePlusTimeRecord/ImportPhoneExcel",
      method: "post",
      headers: {
        "Content-Type": "multipart/form-data",
      },
      data: data,
    });
  }