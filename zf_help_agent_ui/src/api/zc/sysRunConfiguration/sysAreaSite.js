/**
 * @Author: kkfan
 * @Date: 2020-10-28 15:49:52
 * @Last Modified by: kkfan
 * @Last Modified time: 2020-10-28 15:50:43
 * @Description: 受理辖区管理 api
 */
import request from '@/utils/request';

// 分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/sysAreaSite/pageList',
    method: 'post',
    params: query
  })
}

// 启禁用受理辖区信息
export function able(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/sysAreaSite/able',
    params: {
      id: id
    },
    method: 'post'
  })
}

// 受理辖区新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/sysAreaSite/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 获取一条受理辖区详情
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/sysAreaSite/getOne?id=' + id,
    method: 'post',
  })
}
