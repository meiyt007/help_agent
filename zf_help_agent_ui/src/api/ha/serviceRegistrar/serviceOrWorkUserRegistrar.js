import request from "@/utils/request";

//查询所有的帮代办人员
export function getAllHelpWorkUserList(serviceOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/serviceRegistrar/getAllHelpWorkUserList?serviceOid=' + serviceOid,
    method: 'get'
  })
}

// 保存或者修改
export function saveOrUpdateServiceRegistrar(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/serviceRegistrar/saveOrUpdateServiceRegistrar',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=UTF-8",
    },
    data: data
  })
}

//查询所有的分组
export function getAllGroup() {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/serviceRegistrar/queryAllGroup', method: 'get'
  })
}

//查询授权帮代办人员列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/serviceRegistrar/queryWorkUserRegistrarPage',
    method: 'post',
    params: query
  })
}

//查询事项列表
export function servicePage(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/serviceRegistrar/queryServiceInfoPage', method: 'post', params: query
  })
}

//保存帮代办人员授权
export function saveOrUpdateWorkUserRegistrar(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/serviceRegistrar/saveOrUpdateWorkUserRegistrar',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=UTF-8",
    },
    data: data
  })
}

//查询授权事项tree
export function getServiceTree(workUserId) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/serviceRegistrar/queryServiceTree?workUserId=' + workUserId,
    method: 'get'
  })
}

//批量保存帮代办人员授权
export function bathSaveOrUpdateWorkUserRegistrar(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/serviceRegistrar/bathSaveOrUpdateWorkUserRegistrar',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=UTF-8",
    },
    data: data
  })

}
