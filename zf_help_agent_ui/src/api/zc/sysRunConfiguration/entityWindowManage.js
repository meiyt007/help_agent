/**
 * @Author: kkfan
 * @Date: 2020-10-28 15:47:34
 * @Last Modified by: kkfan
 * @Last Modified time: 2020-10-28 15:48:26
 * @Description: 实体窗口管理api
 */
import request from '@/utils/request';

//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/entityWindowManage/pageList',
    method: 'post',
    params: query
  })
}

// 新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/entityWindowManage/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 获取一条详情
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/entityWindowManage/getOne?id=' + id,
    method: 'post',
  })
}

// 删除操作 支持批量删除  eg:  ids = 123,123,123,123
export function deletes(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/entityWindowManage/delete?ids=' + id,
    method: 'post',
  })
}
