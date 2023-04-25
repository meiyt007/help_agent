import request from '@/utils/request';
//根据取号OID修改状态为已叫号,正在办理对外接口
export function handleCallNumber (data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/queueCall/handleCallNumber',
    method: 'get',
    params: data
  })
}

//调用取号叫号系统-叫号 - 完成办理（状态4）对外接口
export function completeCallNumber (data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/queueCall/completeCallNumber',
    method: 'get',
    params: data
  })
}

//调用取号叫号系统-跳过接口 对外接口
export function skipCallNumber (data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/queueCall/skipCallNumber',
    method: 'get',
    params: data
  })
}

//特呼，获取，当天所有的叫号。
export function selectCallNums (data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/callRecordService/selectCallNums',
    method: 'get',
    params: data
  })
}

// 通过叫号 saveCallRecordOid 获取叫好人信息
export function querySaveCallRecordByOid (data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/callRecordService/querySaveCallRecordByOid',
    method: 'get',
    params: data
  })
}
