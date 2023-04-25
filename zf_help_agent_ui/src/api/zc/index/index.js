import request from '@/utils/request';

//获取我的工作成效api
export function getCaseTjInfo () {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/qlCaseService/getCaseTjInfo',
    method: 'GET'
  })
}

//获取待办任务api
export function getWorkTaskCase (params) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/yanshi/getWorkTaskCase',
    method: 'GET',
    params
  })
}

//获取一件事我的工作成效api
export function getOneThingCaseTjInfo () {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/getOneThingCaseTjInfo',
    method: 'GET'
  })
}

//获取一件事待办任务api
export function getOneThingWorkTaskCase (params) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/getOneThingWorkTaskCase',
    method: 'GET',
    params
  })
}
