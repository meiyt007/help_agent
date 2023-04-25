import request from '@/utils/request';
// 分页查询列表操作
export function pageUser(query) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/haManager/workQueue/page',
    method: 'post',
    params: query
  })

}
// 新增或者修改
export function saveUser(data) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/haManager/workQueue/save',
    method: 'post',
    data: data
  })
}
// 初始化用户列表
export function initUserList() {
  return request({
    url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/user/UserServicePage',
    method: 'post'
  })
}
// 根据id获取
export function initUser(id) {
  return request({
    url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/haManager/workQueue/getWorkQueueById?id='+id,
    method: 'get'
  })
}
//删除用户
export function del(id) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/haManager/workQueue/deleteQueueId?id='+id,
    method: 'get',
  })
}

