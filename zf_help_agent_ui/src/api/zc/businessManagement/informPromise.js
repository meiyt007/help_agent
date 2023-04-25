import request from '@/utils/request';

//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/informPromise/queryPage',
    method: 'get',
    params: query
  })
}

//查询列表
export function pageSxService(query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/sxServicePage',
    method: 'post',
    params: query
  })
}

// 保存或者修改
export function saveInformPromise(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/informPromise/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 删除
export function deleteInformPromise(id) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/informPromise/deleteInformPromise?id='+id,
    method: 'get',
  })
}

export function allInformServiceOids() {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/informPromise/allInformServiceOids',
    method: 'get',
  })
}


