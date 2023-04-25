import request from '@/utils/request'

// 获取详情
export function queryServiceTypeSimpleTree (id) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/common/queryServiceTypeSimpleTree?serviceType=' + id,
    method: 'get',
  })
}


// 查询事项类型-事项管理
export function getServiceTypeNumber (serviceStatus) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceType/getServiceTypeAndNumber?serviceStatus=' + serviceStatus,
    method: 'get'
  })
}

// 获取详情
export function queryUserTree () {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/sxServiceRegistrar/queryUserTree',
    method: 'post',
  })
}

