// 窗口取号
import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_CASE_API;

// 获取标准取号目录列表
export function getWandaCatalogue(params) {
    return request({
      url: baseUrl + "/ha/takeCatalogue/getWandaCatalogue",
      method: "post",
      params,
    });
}
// 获取指定取号目录列表
export function getAppointCatalogue(params) {
    return request({
      url: baseUrl + "/ha/takeCatalogue/getAppointCatalogue",
      method: "post",
      params,
    });
}

// 取号
export function takeNumber(data) {
    return request({
      url: baseUrl + "/ha/takeCatalogue/takeNumber",
      method: "post",
      data,
    });
}
