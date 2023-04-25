/**
 * 暂存办件作废接口api
 * @author: wangwg
 * @date: 2020-12-08
 */
import request from '@/utils/request';

// 保存办件作废信息
export function saveCaseInvalid(data) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseService/saveCaseInvalid',
    method: 'POST',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data:data
  })
}
