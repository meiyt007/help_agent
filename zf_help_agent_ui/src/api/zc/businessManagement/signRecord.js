/**
 * 签名
 * @author: wangwg
 * @date: 2021-08-17
 */
import request from '@/utils/request';

export function saveSignRecord (data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseSign/updateSignImg',
    method: 'post',
    data: data
  })
}