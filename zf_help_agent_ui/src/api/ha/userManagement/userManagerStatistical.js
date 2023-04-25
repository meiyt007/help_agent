import request from '@/utils/request';
const baseUrl = process.env.VUE_APP_BJFW_ROUTE_PATH;

//查询列表
export function page(query) {
  return request({
    url: baseUrl + '/work/user/UserServicePage',
    method: 'post',
    params: query
  })
}
// 获取当前勾选帮办人全部的办事队列
export function queryAllWorkQueueListByWorkUserId(data) {
  return request({
    url: baseUrl + "/work/user/queryAllWorkQueueListByWorkUserId",
    method: "get",
    params: data,
  });
}
//获取当前选择的帮办人员转派服务记录
export function queryAllTurnServiceListByWorkUserId(data) {
  return request({
    url: baseUrl + "/work/user/queryAllTurnServiceListByWorkUserId",
    method: "get",
    params: data,
  });
}
